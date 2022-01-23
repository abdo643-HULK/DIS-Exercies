import com.google.common.collect.Lists;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import environment.EnvData;
import environment.EnvironmentGrpc;
import environment.Sensor;
import environment.SensorType;
import environment.SensorTypes;
import environment.SensorValues;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class Main {
	static final int PORT = 5002;
	static final String NAME = "localhost";
	static final Logger mLogger = Logger.getLogger(Main.class.getName());

	/**
	 * builds the communication to the server
	 * and calls the methods that request the server
	 *
	 * @param _args
	 */
	public static void main(String[] _args) {
		var channel = ManagedChannelBuilder
				.forAddress(NAME, PORT)
				.usePlaintext()
				.intercept(new MetaDataInterceptorJava())
				.build();

		blockingEnvironmentTest(channel);

		try {
			asyncEnvironmentTest(channel);
		} catch (InterruptedException _e) {
			mLogger.info("InterruptedException: " + _e.getMessage());
		}

		channel.shutdown();
	}

	/**
	 * this uses the blocking stub which means the Executor will not do anything if
	 * one request takes a large amount of time to finish
	 *
	 * @param _channel
	 */
	static void blockingEnvironmentTest(Channel _channel) {
		final var stub = EnvironmentGrpc.newBlockingStub(_channel);

		// =========== UNARY ===========
		final var types = stub.requestEnvironmentDataTypes(Empty.newBuilder().build());
		mLogger.info("requestEnvironmentDataTypes Response ->: \n" + types);

		final var sensor = Sensor
				.newBuilder()
				.setValue(SensorType.HUMIDITY)
				.build();
		final var data = stub.requestEnvironmentData(sensor);
		mLogger.info("requestEnvironmentData Humidity: \n " + data);

		final var s = stub.requestAll(Empty.newBuilder().build());
		mLogger.info("requestAll: \n " + Arrays.toString(Lists.newArrayList(s).toArray()));
	}

	/**
	 * calls all methods async
	 * to make the async Requests sequential and still non-blocking we are using `CountDownLatch`
	 *
	 * @param _channel
	 * @throws InterruptedException
	 */
	static void asyncEnvironmentTest(Channel _channel) throws InterruptedException {
		var stub = EnvironmentGrpc.newStub(_channel);

		mLogger.info("========== Requests started ==========\n");

		// =========== UNARY ===========
		requestEnvironmentDataTypes(stub);
		requestEnvironmentData(stub, "");
		// =========== CLIENT STREAMING ===========
		setValues(stub);
		// =========== UNARY ===========
		requestEnvironmentData(stub, "after setValues");
		// =========== SERVER STREAMING ===========
		requestAll(stub);

		mLogger.info("========== Requests finished ==========");
	}

	static void requestEnvironmentDataTypes(EnvironmentGrpc.EnvironmentStub _stub) {
		final var finishLatch = new CountDownLatch(1);

		mLogger.info("-- REQUEST_ENVIRONMENT_DATA_TYPES started!\n");
		_stub.requestEnvironmentDataTypes(Empty.newBuilder().build(), new StreamObserver<>() {
			@Override
			public void onNext(SensorTypes _value) {
				mLogger.info("requestEnvironmentDataTypes Response ->: \n" + _value);
			}

			@Override
			public void onError(Throwable _t) {
				mLogger.info("Error: " + _t.getMessage());
			}

			@Override
			public void onCompleted() {
				mLogger.info("-- REQUEST_ENVIRONMENT_DATA_TYPES Finished!\n");
				finishLatch.countDown();
			}
		});

		if (finishLatch.getCount() == 0) {
			mLogger.warning("requestEnvironmentData completed or errored before we finished sending.");
		}

		try {
			if (!finishLatch.await(1, TimeUnit.SECONDS)) {
				mLogger.warning("requestEnvironmentDataTypes FAILED : cannot finish within 10 seconds");
			}
		} catch (InterruptedException _e) {
			_e.printStackTrace();
		}
	}

	static void requestEnvironmentData(EnvironmentGrpc.EnvironmentStub _stub, String _msg) {
		final var finishLatch = new CountDownLatch(1);

		mLogger.info("-- REQUEST_ENVIRONMENT_DATA started!\n");
		final var sensor = Sensor
				.newBuilder()
				.setValue(SensorType.HUMIDITY)
				.build();

		_stub.requestEnvironmentData(sensor, new StreamObserver<>() {
			@Override
			public void onNext(EnvData _value) {
				mLogger.info("requestEnvironmentData " + _msg + "\n"
				             + _value.getSensor().name()
				             + " - Timestamp: " + _value.getTimestamp().getSeconds()
				             + " - Values: " + _value.getValuesList()
				             + "\n"
				);
			}

			@Override
			public void onError(Throwable _t) {
				mLogger.info("Error: " + _t.getMessage());
			}

			@Override
			public void onCompleted() {
				mLogger.info("-- REQUEST_ENVIRONMENT_DATA Finished!\n");
				finishLatch.countDown();
			}
		});

		if (finishLatch.getCount() == 0) {
			mLogger.warning("requestEnvironmentData completed or errored before we finished sending.");
		}

		try {
			if (!finishLatch.await(2, TimeUnit.SECONDS)) {
				mLogger.warning("requestEnvironmentData FAILED : cannot finish within 10 seconds");
			}
		} catch (InterruptedException _e) {
			_e.printStackTrace();
		}
	}

	static void setValues(EnvironmentGrpc.EnvironmentStub _stub) throws InterruptedException {
		final var finishLatch = new CountDownLatch(1);

		mLogger.info("-- SET_VALUES started!\n");
		final var requestObserver = _stub.setValues(new StreamObserver<>() {
			@Override
			public void onNext(StringValue _value) {
				mLogger.info("setValues Response : " + _value.getValue() + "\n");
			}

			@Override
			public void onError(Throwable _t) {
				mLogger.info("Error: " + _t.getMessage());
			}

			@Override
			public void onCompleted() {
				mLogger.info("-- SET_VALUES finished!\n");
				finishLatch.countDown();
			}
		});

		requestObserver.onNext(
				SensorValues
						.newBuilder()
						.setSensor(SensorType.HUMIDITY)
						.addValues(5)
						.build()
		);

		TimeUnit.MILLISECONDS.sleep(1000);
		requestObserver.onNext(
				SensorValues
						.newBuilder()
						.setSensor(SensorType.HUMIDITY)
						.addValues(5)
						.addValues(15)
						.build()
		);

		TimeUnit.MILLISECONDS.sleep(1000);
		requestObserver.onNext(
				SensorValues
						.newBuilder()
						.setSensor(SensorType.PRESSURE)
						.addValues(349)
						.build()
		);

		if (finishLatch.getCount() == 0) {
			mLogger.warning("setValues completed or errored before we finished sending.");
			return;
		}

		requestObserver.onCompleted();

		try {
			if (!finishLatch.await(10, TimeUnit.SECONDS)) {
				mLogger.warning("setValues FAILED : cannot finish within 10 seconds");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	static void requestAll(EnvironmentGrpc.EnvironmentStub _stub) {
		final var finishLatch = new CountDownLatch(1);

		mLogger.info("-- REQUEST_ALL started!\n");
		_stub.requestAll(Empty.newBuilder().build(), new StreamObserver<>() {
			@Override
			public void onNext(EnvData _value) {
				mLogger.info(_value.getSensor().name()
				             + " - Timestamp: " + _value.getTimestamp()
				                                        .getSeconds()
				             + " - Values: "
				             + _value.getValuesList() + "\n");
			}

			@Override
			public void onError(Throwable _t) {
				mLogger.info("Error: " + _t.getMessage());
			}

			@Override
			public void onCompleted() {
				mLogger.info("-- REQUEST_ALL finished!\n");
				finishLatch.countDown();
			}
		});

		if (finishLatch.getCount() == 0) {
			mLogger.warning("requestEnvironmentData completed or errored before we finished sending.");
		}

		try {
			if (!finishLatch.await(10, TimeUnit.SECONDS)) {
				mLogger.warning("requestEnvironmentData FAILED : cannot finish within 10 seconds");
			}
		} catch (InterruptedException _e) {
			_e.printStackTrace();
		}
	}
}

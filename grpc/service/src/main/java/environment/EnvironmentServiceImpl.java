package environment;

import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EnvironmentServiceImpl extends EnvironmentGrpc.EnvironmentImplBase {
	private static final Logger mLogger = Logger.getLogger(EnvironmentServiceImpl.class.getName());
	private static final List<SensorType> mSensorTypes = Arrays
			.stream(SensorType.values())
			.filter((sensorType) -> sensorType != SensorType.UNRECOGNIZED).collect(Collectors.toList());

	private final Map<SensorType, List<Long>> mSensors = new HashMap<>() {{
		put(SensorType.PRESSURE, null);
		put(SensorType.HUMIDITY, null);
		put(SensorType.TEMPERATURE, null);
	}};

	@Override
	public void requestEnvironmentData(Sensor _request, StreamObserver<EnvData> _responseObserver) {
		final var timestamp = Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build();
		final var values = getSensorValues(_request.getValue());
		final var data = EnvData
				.newBuilder()
				.setSensor(_request.getValue())
				.setTimestamp(timestamp)
				.addAllValues(values)
				.build();

		_responseObserver.onNext(data);
		_responseObserver.onCompleted();
	}

	@Override
	public void requestEnvironmentDataTypes(Empty _request, StreamObserver<SensorTypes> _responseObserver) {
		_responseObserver.onNext(
				SensorTypes
						.newBuilder()
						.addAllTypeValue(
								mSensorTypes
										.stream()
										.map(SensorType::getNumber)
										.collect(Collectors.toList()))
						.build()
		);
		_responseObserver.onCompleted();
	}

	/**
	 * sends all available data as a non-blocking stream
	 */
	@Override
	public void requestAll(Empty _request, StreamObserver<EnvData> _responseObserver) {
		try {
			for (final var value : mSensorTypes) {
				final var values = getSensorValues(value);
				final var timestamp = Timestamp
						.newBuilder()
						.setSeconds(Instant.now().getEpochSecond())
						.build();
				final var data = EnvData
						.newBuilder()
						.setSensor(value)
						.setTimestamp(timestamp)
						.addAllValues(values)
						.build();

				_responseObserver.onNext(data);
				TimeUnit.SECONDS.sleep(1);
			}
		} catch (RuntimeException _e) {
			_responseObserver.onError(_e);
			throw _e;
		} catch (InterruptedException _e) {
			_responseObserver.onError(_e);
		}

		_responseObserver.onCompleted();
	}

	/**
	 * collects all the data as a non-blocking stream and returns ok, when
	 * it finishes
	 */
	@Override
	public StreamObserver<SensorValues> setValues(StreamObserver<StringValue> _responseObserver) {
		return new StreamObserver<>() {
			@Override
			public void onNext(SensorValues value) {
				mSensors.put(value.getSensor(), value.getValuesList());
			}

			@Override
			public void onError(Throwable _t) {
				mLogger.info("Error setting values: " + _t.getMessage());
			}

			@Override
			public void onCompleted() {
				_responseObserver.onNext(
						StringValue
								.newBuilder()
								.setValue("ok")
								.build()
				);
				_responseObserver.onCompleted();
			}
		};
	}

	/**
	 * helper function to generate random values if the map entry doesn't have a value
	 *
	 * @param _value - key of the entry (type of the Sensor)
	 * @return - list of values
	 */
	private List<Long> getSensorValues(SensorType _value) {
		final var values = mSensors.get(_value);
		if (values != null) return values;

		final var r = new Random();
		final var low = 0;
		final var high = 1000;
		final var result = (long) (r.nextInt(high - low) + low);

		return List.of(result);
	}
}

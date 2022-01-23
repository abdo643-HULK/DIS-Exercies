package environment;

import io.grpc.*;

import java.util.logging.Logger;

public class EnvironmentServiceInterceptorImpl implements ServerInterceptor {
	private static final Logger mLogger = Logger.getLogger(EnvironmentServiceImpl.class.getName());

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
			ServerCall<ReqT, RespT> _call,
			Metadata _headers,
			ServerCallHandler<ReqT, RespT> _next
	) {
		mLogger.info(
				"======= [Server Interceptor] : Remote Method Invoked - "
						+ _call.getMethodDescriptor().getFullMethodName()
						+ '\n');

		final var serverCall = new EnvironmentServerCallJava<>(_call);

		return new EnvironmentServerCallListenerJava<>(_next.startCall(serverCall, _headers));
	}
}

/**
 * this class/middleware/interceptor enables listening
 * to incoming requests
 */
class EnvironmentServerCallListenerJava<R> extends ForwardingServerCallListener<R> {
	private static final Logger mLogger = Logger.getLogger(EnvironmentServerCallListenerJava.class.getName());

	private final ServerCall.Listener<R> mDelegate;

	EnvironmentServerCallListenerJava(ServerCall.Listener<R> _delegate) {
		mDelegate = _delegate;
	}

	@Override
	protected ServerCall.Listener<R> delegate() {
		return mDelegate;
	}

	@Override
	public void onMessage(R message) {
		mLogger.info("Message Received from Client -> Service " + message);
		super.onMessage(message);
	}
}

/**
 * this class/middleware/interceptor enables listening
 * to outgoing responses
 */
class EnvironmentServerCallJava<ReqT, RespT> extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> {
	private final Logger mLogger = Logger.getLogger(EnvironmentServerCallJava.class.getName());

	EnvironmentServerCallJava(ServerCall<ReqT, RespT> _delegate) {
		super(_delegate);
	}

	@Override
	protected ServerCall<ReqT, RespT> delegate() {
		return super.delegate();
	}

	@Override
	public MethodDescriptor<ReqT, RespT> getMethodDescriptor() {
		return super.getMethodDescriptor();
	}

	@Override
	public void sendMessage(RespT _message) {
		super.sendMessage(_message);
		mLogger.info("Message Send from Service -> Client : " + _message);
	}
}
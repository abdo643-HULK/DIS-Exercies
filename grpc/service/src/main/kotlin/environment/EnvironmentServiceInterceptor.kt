package environment

import io.grpc.*
import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import java.util.logging.Logger


class EnvironmentServiceInterceptor : ServerInterceptor {
    private val mLogger = Logger.getLogger(EnvironmentServiceInterceptor::class.java.name)

    override fun <ReqT, RespT> interceptCall(
        _call: ServerCall<ReqT, RespT>,
        _headers: Metadata?,
        _next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        mLogger.info("======= [Server Interceptor] : Remote Method Invoked - ${_call.methodDescriptor.fullMethodName}\n")

        val serverCall = EnvironmentServerCall(_call)

        return EnvironmentServerCallListener(_next.startCall(serverCall, _headers))
    }
}

/**
 * this class/middleware/interceptor enables listening
 * to incoming requests
 */
class EnvironmentServerCallListener<R> internal constructor(private val mDelegate: ServerCall.Listener<R>) :
    ForwardingServerCallListener<R>() {
    private val mLogger = Logger.getLogger(EnvironmentServerCallListener::class.java.name)

    override fun delegate(): ServerCall.Listener<R> {
        return mDelegate
    }

    override fun onMessage(message: R) {
        super.onMessage(message)
        mLogger.info("Message Received from Client -> Service $message")
    }
}

/**
 * this class/middleware/interceptor enables listening
 * to outgoing responses
 */
class EnvironmentServerCall<ReqT, RespT> internal constructor(mDelegate: ServerCall<ReqT, RespT>?) :
    SimpleForwardingServerCall<ReqT, RespT>(mDelegate) {
    private val mLogger = Logger.getLogger(EnvironmentServerCall::class.java.name)

    override fun sendMessage(message: RespT) {
        super.sendMessage(message)
        mLogger.info("Message Send from Service -> Client : $message")
    }

}
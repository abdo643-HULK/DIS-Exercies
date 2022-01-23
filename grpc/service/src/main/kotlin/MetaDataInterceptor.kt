import io.grpc.Metadata
import io.grpc.Metadata.ASCII_STRING_MARSHALLER
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import java.util.logging.Logger

/**
 * logs our client metadata
 */
class MetaDataInterceptor : ServerInterceptor {
    private val logger = Logger.getLogger(MetaDataInterceptor::class.java.name)

    override fun <ReqT, RespT> interceptCall(
        _call: ServerCall<ReqT, RespT>,
        _headers: Metadata,
        _next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val metaData = _headers.get(Metadata.Key.of("MY_MD_1", ASCII_STRING_MARSHALLER))

        logger.info("Metadata Retrieved : $metaData");

        return _next.startCall(_call, _headers)
    }
}
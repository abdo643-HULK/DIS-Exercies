import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import java.util.logging.Logger;

/**
 * logs our client metadata
 */
public class MetaDataInterceptorJava implements ServerInterceptor {
	private static final Logger logger = Logger.getLogger(MetaDataInterceptorJava.class.getName());

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
			ServerCall<ReqT, RespT> _call,
			Metadata _headers,
			ServerCallHandler<ReqT, RespT> _next
	) {
		final var metaData = _headers.get(Metadata.Key.of("MY_MD_1", Metadata.ASCII_STRING_MARSHALLER));

		logger.info("Metadata Retrieved : " + metaData);

		return _next.startCall(_call, _headers);
	}
}

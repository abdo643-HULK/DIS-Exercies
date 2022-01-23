import io.grpc.*;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class MetaDataInterceptorJava implements ClientInterceptor {
	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
			MethodDescriptor<ReqT, RespT> _method,
			CallOptions _callOptions,
			Channel _next
	) {
		return new ForwardingClientCall.SimpleForwardingClientCall<>(_next.newCall(_method, _callOptions)) {
			/**
			 * Metadata are like Headers in HTTP
			 * because grpc uses a binary format the string needs to be encoded with the
			 * help of the `ASCII_STRING_MARSHALLER`
			 */
			@Override
			public void start(Listener<RespT> responseListener, Metadata headers) {
				headers.put(Metadata.Key.of("MY_MD_1", ASCII_STRING_MARSHALLER), "This is metadata of MY_MD_1");
				super.start(responseListener, headers);
			}
		};
	}
}

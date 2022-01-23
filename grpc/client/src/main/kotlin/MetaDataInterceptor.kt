import io.grpc.*
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.Metadata.ASCII_STRING_MARSHALLER


class MetaDataInterceptor : ClientInterceptor {
    override fun <ReqT, RespT> interceptCall(
        _method: MethodDescriptor<ReqT, RespT>,
        _callOptions: CallOptions,
        _next: Channel
    ): ClientCall<ReqT, RespT> {
        return object : SimpleForwardingClientCall<ReqT, RespT>(_next.newCall(_method, _callOptions)) {
            /**
             * Metadata are like Headers in HTTP
             * because grpc uses a it`s own binary format the string needs to be encoded with the
             * help of the `ASCII_STRING_MARSHALLER`
             */
            override fun start(responseListener: Listener<RespT>, headers: Metadata) {
                headers.put(Metadata.Key.of("MY_MD_1", ASCII_STRING_MARSHALLER), "This is metadata of MY_MD_1")
                super.start(responseListener, headers)
            }
        }
    }
}
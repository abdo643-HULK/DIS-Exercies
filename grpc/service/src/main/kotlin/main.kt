import environment.EnvironmentService
import environment.EnvironmentServiceInterceptor
import io.grpc.ServerBuilder
import io.grpc.ServerInterceptors
import java.util.concurrent.Executors

const val PORT = 5001

/**
 * uses a custom Executor with 4 Threads
 * this is the server for the environment service
 */
fun main(_ignore: Array<String>) {
    val executorService = Executors.newFixedThreadPool(4)
    val server = ServerBuilder
        .forPort(PORT)
        .executor(executorService)
        // this interceptor is for all Services
        .intercept(MetaDataInterceptor())
        .addService(
            ServerInterceptors
                .intercept(
                    EnvironmentService(),
                    // this interceptor is only for this service
                    EnvironmentServiceInterceptor()
                )
        )
        .build()

    server.start()
    println("Server started, listening on $PORT")

    Runtime.getRuntime().addShutdownHook(
        Thread {
            println("Shutting down gRPC server since JVM is shutting down")
            server?.shutdown()
            println("Server shut down")
        }
    )

    server.awaitTermination()
    executorService.shutdown()
}
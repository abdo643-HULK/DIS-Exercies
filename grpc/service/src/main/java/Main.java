import environment.EnvironmentServiceImpl;
import environment.EnvironmentServiceInterceptorImpl;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;

import java.io.IOException;
import java.util.concurrent.Executors;

public class Main {
	static final int PORT = 5002;

	/**
	 * uses a custom Executor with 4 Threads
	 * this is the server for the environment service
	 */
	public static void main(String[] _args) throws IOException, InterruptedException {
		var executorService = Executors.newFixedThreadPool(4);
		var server = ServerBuilder
				.forPort(PORT)
				.executor(executorService)
				// this interceptor is for all Services
				.intercept(new MetaDataInterceptorJava())
				.addService(
						ServerInterceptors.intercept(
								new EnvironmentServiceImpl(),
								// this interceptor is only for this service
								new EnvironmentServiceInterceptorImpl()
						)
				)
				.build();

		server.start();
		System.out.println("Server started, listening on " + PORT);

		Runtime.getRuntime().addShutdownHook(
				new Thread(() -> {
					System.out.println("Shutting down gRPC server since JVM is shutting down");
					server.shutdown();
					System.out.println("Server shut down");
				})
		);

		server.awaitTermination();
		executorService.shutdown();
	}
}

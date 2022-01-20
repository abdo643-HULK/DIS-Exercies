import com.shehatamilo.HelloWorldService;

/**
 * calls the HelloWorld service with
 * the help of the created class from wsimport
 */
public class HelloClient {
	public static void main(String[] _args) {
		var server = new HelloWorldService();
		var soap = server.getHelloWorldPort();
		System.out.println("\nserver --> " + soap.saySomething());

		System.out.println("server --> " + soap.getData("").getFirstName() + " " + soap.getData("").getLastName());
	}
}

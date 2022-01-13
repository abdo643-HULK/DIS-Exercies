import com.shehatamilo.HelloWorldService;

public class HelloClient {
    public static void main(String[] _args) {
        var server = new HelloWorldService();
        var soap = server.getHelloWorldPort();
        System.out.println("\nserver --> " + soap.saySomething());

        System.out.println("server --> " + soap.getData("").getMFirstName() + " " + soap.getData("").getMLastName());

    }
}

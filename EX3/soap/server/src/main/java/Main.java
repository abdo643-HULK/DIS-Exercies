import hello.HelloWorld;
import jakarta.xml.ws.Endpoint;

public class Main {
    public static void main(String[] _args) {
        HelloWorld test = new HelloWorld();
        Endpoint endpoint =
                Endpoint.publish("http://localhost:8081/HelloWorld", test);
        System.out.println("server up and running ...");
    }
}

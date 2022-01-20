import hello.HelloWorld;

import javax.xml.ws.Endpoint;

/**
 * serves our soap HelloWorld service
 */
public class Main {
    public static void main(String[] _args) {
        HelloWorld test = new HelloWorld();
        Endpoint.publish("http://localhost:8081/HelloWorld", test);
        System.out.println("server up and running ...");
    }
}

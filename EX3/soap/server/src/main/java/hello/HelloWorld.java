package hello;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public class HelloWorld {

	@WebMethod
	String saySomething() {
		return "saying some things";
	}

	@WebMethod
	DummyData getData(String _name){ //sollten wir nicht den Parameter returnen?
		return new DummyData("Marko", "Milo");
	}
}

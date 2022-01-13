package hello;

import classes.DummyData;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public class HelloWorld {

	@WebMethod
	public String saySomething() {
		return "saying some things";
	}

	@WebMethod
	public DummyData getData(String _name){ //sollten wir nicht den Parameter returnen?
		return new DummyData("Marko", "Milo");
	}
}

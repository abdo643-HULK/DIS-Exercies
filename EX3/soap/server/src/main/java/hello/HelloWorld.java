package hello;

import classes.DummyData;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Warmup service for the soap exercise
 */
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

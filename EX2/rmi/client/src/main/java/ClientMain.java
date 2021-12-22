import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {

	public static void main(String[] _args) {
		try {
			String adr = "EnvService";
			Registry reg = LocateRegistry.getRegistry();
			ICookiesService cookiesStub = (ICookiesService) reg.lookup(adr);
			cookiesStub.saySomething();
		} catch (Exception _e) {
			_e.printStackTrace();
		}

	}
}

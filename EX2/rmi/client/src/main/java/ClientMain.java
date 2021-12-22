import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientMain {

	public static void main(String[] _args) {
		try {
			String adr = "EnvService";
			Registry reg = LocateRegistry.getRegistry();
			ICookiesService cookiesStub = (ICookiesService) reg.lookup(adr);
			cookiesStub.saySomething();

			//------------
			IEnvService envStub = (IEnvService) reg.lookup(adr);

			Scanner in = new Scanner(System.in);
			int inputMenu;
			boolean exit = false;

			while (!exit) {
				do {
					System.out.println();
					System.out.println("Envi Client Menu:");
					System.out.println("------------------------:");
					System.out.println("1. Get sensor types:");
					System.out.println("2. Get sensor data:");
					System.out.println("3. Get all Sensor:");
					System.out.println("4. EXIT");
					System.out.println("------------------------:");

					inputMenu = in.nextInt();

				} while (inputMenu < 1 || inputMenu > 4);

				switch (inputMenu) {
					case 1 -> envStub.requestEnvironmentDataTypes();
					case 2 -> envStub.requestEnvironmentData("air");
					case 3 -> envStub.requestAll();
					default -> exit = true;
				}
			}



		} catch (Exception _e) {
			_e.printStackTrace();
		}





	}
}

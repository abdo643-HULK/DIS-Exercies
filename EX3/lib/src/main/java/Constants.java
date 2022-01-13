import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Constants {
	private static Properties mProperties = new Properties();

	static {
		try {
			mProperties.load(new FileInputStream("gradle.properties"));
		} catch (IOException _e) {
			_e.printStackTrace();
		}
	}

	public static String USER_LANG = System.getProperty("user.language");

	public static String ENV_SERVER_URL = "http://localhost:5000/Environment";

	public static String OWM_URL = "http://api.openweathermap.org/data/2.5/weather";
	public static String OWM_UNIT = "metric";
	public static String OWM_KEY = mProperties.getProperty("OWM_KEY");
}

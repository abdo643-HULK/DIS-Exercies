import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * holds all our constants for all the modules
 */
public class Constants {
	private static final Properties mProperties = new Properties();


	static {
		try {
			/// reads the file with the secret keys that shouldn't be exposed on our git repo
			mProperties.load(new FileInputStream("gradle.properties"));
		} catch (IOException _e) {
			_e.printStackTrace();
		}
	}

	public static final String USER_LANG = System.getProperty("user.language");

	public static final String ENV_SERVER_URL = "http://localhost:5000/Environment";

	public static final String OWM_URL = "http://api.openweathermap.org/data/2.5/weather";
	public static final String OWM_UNIT = "metric";
	public static final String OWM_KEY = mProperties.getProperty("OWM_KEY");
}

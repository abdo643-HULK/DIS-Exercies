import java.io.File;
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

	static String OWM_KEY = mProperties.getProperty("OWM_KEY");
}

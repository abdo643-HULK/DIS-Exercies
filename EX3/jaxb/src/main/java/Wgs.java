// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import java.util.regex.*;
import java.util.*;

/**
 * Class that holds the values of the example to parse
 */
class Wgs {
	final float mLatitude;
	final float mLongitude;

	Wgs(float _latitude, float _longitude) {
		mLatitude = _latitude;
		mLongitude = _longitude;
	}

	/**
	 * Parses the xml string with the
	 * help of a regex and puts
	 * the result into a hashmap
	 * from the result we construct
	 * the object with the properties
	 * from the map
	 *
	 * @param _args the args passed to the program
	 */
	public static void main(String[] _args) {
		var xml = "<wgs84><latitude>48.31</latitude><longitude>14.29</longitude></wgs84>";

		var rootRegex = Pattern.compile("<wgs84>(.*?)</wgs84>", Pattern.DOTALL);
		var matcher = rootRegex.matcher(xml);


		var map = new HashMap<String, Float>();
		var regex2 = Pattern.compile("<([^<>]+)>([^<>]+)</\\1>");
		if (matcher.find()) {
			var data = matcher.group(1);
			Matcher matcher2 = regex2.matcher(data);
			while (matcher2.find()) {
				var tag = matcher2.group(1);
				var value = matcher2.group(2);
				map.put(tag, Float.valueOf(value));
			}
		}

		var wgs84 = new Wgs(map.get("latitude"), map.get("longitude"));

		System.out.println(wgs84);
	}

	@Override
	public String toString() {
		return "WGS {"
				+ "\n\tlatitude=" + mLatitude
				+ "\n\tlongitude=" + mLongitude
				+ "\n}";
	}
}
// Online Java Compiler
// Use this editor to write, compile and run your Java code online
import java.util.regex.*;
import java.util.*;

class WGS {
	final float mLatitude;
	final float mLongitude;

	WGS(float _latitude, float _longitude) {
		mLatitude = _latitude;
		mLongitude = _longitude;
	}

	public static void main(String[] args) {
		var xml = "<wgs84><latitude>48.31</latitude><longitude>14.29</longitude></wgs84>";

		var rootRegex = Pattern.compile("<wgs84>(.*?)</wgs84>", Pattern.DOTALL);
		var matcher = rootRegex.matcher(xml);


		var map = new HashMap<String, Float>();
		var regex2 = Pattern.compile("<([^<>]+)>([^<>]+)</\\1>");
		if (matcher.find()) {
			var DataElements = matcher.group(1);
			Matcher matcher2 = regex2.matcher(DataElements);
			while (matcher2.find()) {
				var tag =  matcher2.group(1);
				var value= matcher2.group(2);
				map.put(tag, Float.valueOf(value));
			}
		}

		var wgs84 = new WGS(map.get("latitude"), map.get("longitude"));
		
		System.out.println(wgs84);
	}

	@Override
	public String toString() {
		return "WGS {" +
				"\n\tlatitude=" + mLatitude +
				"\n\tlongitude=" + mLongitude +
				"\n}";
	}
}
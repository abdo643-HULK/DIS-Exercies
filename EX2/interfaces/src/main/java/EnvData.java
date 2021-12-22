import java.io.Serializable;

public class EnvData implements Serializable {
	final String mTimestamp;
	final String mSensor;
	final int[] mValues;

	EnvData(String _timestamp, String _sensor, int[] _values) {
		mTimestamp = _timestamp;
		mSensor = _sensor;
		mValues = _values;
	}
}

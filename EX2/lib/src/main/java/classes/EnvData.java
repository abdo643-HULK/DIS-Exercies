package classes;

import java.io.Serializable;
import java.util.Arrays;

public class EnvData implements Serializable {
	final String mTimestamp;
	final String mSensor;
	final int[] mValues;

	public EnvData(String _timestamp, String _sensor, int[] _values) {
		mTimestamp = _timestamp;
		mSensor = _sensor;
		mValues = _values;
	}

	@Override
	public String toString() {
		return "classes.EnvData{"
				+ "mTimestamp='" + mTimestamp + '\''
				+ ", mSensor='" + mSensor + '\''
				+ ", mValues=" + Arrays.toString(mValues)
				+ '}';
	}

	public int[] getmValues() {
		return mValues;
	}

	public String getmSensor() {
		return mSensor;
	}

	public String getmTimestamp() {
		return mTimestamp;
	}
}

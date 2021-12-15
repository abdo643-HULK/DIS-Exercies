package com.fh.clients.envi;

/**
 * EnvData communicates with the TcpEnvi server and  shows the received data.
 */
public class EnvData {
	final String mTimeStamp;
	final String[] mValues;

	EnvData(final String _timeStamp, final String[] _values) {
		mTimeStamp = _timeStamp;
		mValues = _values;
	}

	public String toString() {
		StringBuilder str = new StringBuilder("Timestamp " + mTimeStamp + " - ");
		if (mValues.length <= 0) return "";

		for (int i = 0; i < mValues.length - 1; ++i) {
			str.append(mValues[i]).append(", ");
		}

		str.append(mValues[mValues.length - 1]);
		return str.toString();
	}
}

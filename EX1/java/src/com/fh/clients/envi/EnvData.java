package com.fh.clients.envi;

/**
 * EnvData communicates with the TcpEnvi server and  shows the received data.
 */
public class EnvData {
	/**
	 * contains the timestamp of the sensor data
	 */
    final String mTimeStamp;
	/**
	 * contains the data of the sensor
	 */
    final String[] mValues;

	/**
	 * constructor for class EnvData
	 *
	 * @param _timeStamp contains the timestamp of the sensor data
	 * @param _values contains the data of the sensor
	 */
    EnvData(final String _timeStamp, final String[] _values) {
        mTimeStamp = _timeStamp;
        mValues = _values;
    }
}

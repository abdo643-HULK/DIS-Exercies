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
}

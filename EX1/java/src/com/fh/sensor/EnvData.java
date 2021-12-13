package com.fh.sensor;

import java.sql.Timestamp;

public class EnvData {
    String timeStamp = "";
    String[] values;

    EnvData(String timeStamp, String[] values) {
        this.timeStamp = timeStamp;
        this.values = values;
    }
}

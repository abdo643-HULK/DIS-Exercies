package classes

import java.io.Serializable

class EnvData(val mTimestamp: String, val mSensor: String, val mValues: IntArray) : Serializable {

    companion object {
        const val serialVersionUID = 123L
    }

    override fun toString(): String {
        return ("classes.EnvData{"
                + "mTimestamp='" + mTimestamp + '\''
                + ", mSensor='" + mSensor + '\''
                + ", mValues=" + mValues.contentToString()
                + '}')
    }
}
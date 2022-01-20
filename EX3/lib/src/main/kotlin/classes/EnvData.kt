package classes

import java.io.Serializable
import javax.xml.bind.annotation.*

/**
 * Our Class that holds our Sensordata
 * and gets converted from/to xml
 */
@XmlRootElement(name = "env-data")
data class EnvData(
    /**
     * seconds timestamp
     */
    @field:XmlElement
    val mTimestamp: String,
    /**
     * the name of our sensor
     */
    @field:XmlElement
    val mSensor: String,
    /**
     * all the values that the Sensor holds
     */
    @field:XmlElementWrapper(name = "values")
    @field:XmlElement(name = "value")
    val mValues: IntArray
) : Serializable {
    companion object {
        const val serialVersionUID = 123L
    }

    constructor() : this("", "", intArrayOf())
}
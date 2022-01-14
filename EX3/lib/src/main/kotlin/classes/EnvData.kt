package classes

import jakarta.xml.bind.annotation.*
import java.io.Serializable

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
data class EnvData(
    @field:XmlElement
    val mTimestamp: String,
    @field:XmlElement
    val mSensor: String,
    @field:XmlElementWrapper(name = "values")
    @field:XmlElement(name = "value")
    val mValues: FloatArray
) : Serializable {
    companion object {
        const val serialVersionUID = 123L
    }

    constructor() : this("", "", floatArrayOf())
}
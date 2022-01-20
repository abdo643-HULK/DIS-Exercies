package classes

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * our root class for the Weather API response
 */
@XmlRootElement(name = "current")
@XmlAccessorType(XmlAccessType.FIELD)
data class WeatherResponse(
    @field:XmlElement(name = "main")
    val mMain: WeatherMain?,
    @field:XmlElement(name = "base")
    val mBase: String
) {
    constructor() : this(null, "")
}

/**
 * the class for the main property
 * on the root object
 */
@XmlRootElement(name = "main")
@XmlAccessorType(XmlAccessType.FIELD)
data class WeatherMain(
    @field:XmlElement(name = "temp")
    val mTemp: Float,
    @field:XmlElement(name = "feels_like")
    val mFeelsLike: Float,
    @field:XmlElement(name = "temp_min")
    val mTempMin: Float,
    @field:XmlElement(name = "temp_max")
    val mTempMax: Float,
    @field:XmlElement(name = "pressure")
    val mPressure: Float,
    @field:XmlElement(name = "humidity")
    val mHumidity: Float,
) {
    constructor() : this(0f, 0f, 0f, 0f, 0f, 0f)
}

//@XmlRootElement(name = "weather")
//@XmlAccessorType(XmlAccessType.FIELD)
//class WeatherResponse {
//    @XmlElement(name = "main")
//    var mMain: WeatherMain? = null
//
//    @XmlElement(name = "base")
//    val mBase: String
//
//    constructor() {
//        mBase = ""
//    }
//
//    constructor(_main: WeatherMain, _base: String) {
//        mMain = _main
//        mBase = _base
//    }
//}
//
//
//@XmlRootElement(name = "main")
//@XmlAccessorType(XmlAccessType.FIELD)
//class WeatherMain {
//    @XmlElement(name = "temp")
//    val mTemp: Float
//
//    @XmlElement(name = "feels_like")
//    val mFeelsLike: Float
//
//    @XmlElement(name = "temp_min")
//    val mTempMin: Float
//
//    @XmlElement(name = "temp_max")
//    val mTempMax: Float
//
//    @XmlElement(name = "pressure")
//    val mPressure: Float
//
//    @XmlElement(name = "humidity")
//    val mHumidity: Float
//
//    constructor() {
//        mTemp = 0f
//        mFeelsLike = 0f
//        mTempMin = 0f
//        mTempMax = 0f
//        mPressure = 0f
//        mHumidity = 0f
//    }
//
//    constructor(
//        _temp: Float,
//        _feelsLike: Float,
//        _tempMin: Float,
//        _tempMax: Float,
//        _pressure: UInt,
//        _humidity: UInt
//    ) {
//        mTemp = _temp
//        mFeelsLike = _feelsLike
//        mTempMin = _tempMin
//        mTempMax = _tempMax
//        mPressure = _pressure
//        mHumidity = _humidity
//    }
//
//    override fun toString(): String {
//        return "WeatherMain(" +
//                "mTemp: $mTemp, " +
//                "mFeelsLike: $mFeelsLike, " +
//                "mTempMin: $mTempMin, " +
//                "mTempMax: $mTempMax, " +
//                "mPressure: $mPressure, " +
//                "mHumidity: $mHumidity" +
//                ")"
//    }
//}

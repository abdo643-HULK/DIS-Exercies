package classes

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

//@XmlRootElement(name = "weather")
//@XmlAccessorType(XmlAccessType.FIELD)
//data class WeatherResponse(
//    @XmlElement(name = "main")
//    val mMain: WeatherMain?,
//    @XmlElement(name = "base")
//    val mBase: String
//) {
//    constructor() : this(null, "")
//}

//@XmlRootElement(name = "main")
//@XmlAccessorType(XmlAccessType.FIELD)
//data class WeatherMain(
//    @XmlElement(name = "temp") val mTemp: Float,
//    @XmlElement(name = "feels_like") val mFeelsLike: Float,
//    @XmlElement(name = "temp_min") val mTempMin: Float,
//    @XmlElement(name = "temp_max") val mTempMax: Float,
//    @XmlElement(name = "pressure") val mPressure: UInt,
//    @XmlElement(name = "humidity") val mHumidity: UInt,
//) {
//    constructor() : this(0f, 0f, 0f, 0f, 0u, 0u)
//}

@XmlRootElement(name = "weather")
@XmlAccessorType(XmlAccessType.FIELD)
class WeatherResponse {
    @XmlElement(name = "main")
    var mMain: WeatherMain? = null

    @XmlElement(name = "base")
    val mBase: String

    constructor() {
        mBase = ""
    }

    constructor(_main: WeatherMain, _base: String) {
        mMain = _main
        mBase = _base
    }
}


@XmlRootElement(name = "main")
@XmlAccessorType(XmlAccessType.FIELD)
class WeatherMain {
    @XmlElement(name = "temp")
    val mTemp: Float

    @XmlElement(name = "feels_like")
    val mFeelsLike: Float

    @XmlElement(name = "temp_min")
    val mTempMin: Float

    @XmlElement(name = "temp_max")
    val mTempMax: Float

    @XmlElement(name = "pressure")
    val mPressure: UInt

    @XmlElement(name = "humidity")
    val mHumidity: UInt

    constructor() {
        mTemp = 0f
        mFeelsLike = 0f
        mTempMin = 0f
        mTempMax = 0f
        mPressure = 0u
        mHumidity = 0u
    }

    constructor(
        _temp: Float,
        _feelsLike: Float,
        _tempMin: Float,
        _tempMax: Float,
        _pressure: UInt,
        _humidity: UInt
    ) {
        mTemp = _temp
        mFeelsLike = _feelsLike
        mTempMin = _tempMin
        mTempMax = _tempMax
        mPressure = _pressure
        mHumidity = _humidity
    }
}

import classes.EnvData
import interfaces.ICookiesService
import interfaces.IEnvService
import java.rmi.server.UnicastRemoteObject

const val SECONDS_DIVISOR = 1000

@Suppress("MagicNumber")
val SENSOR_VALUES_RANGE= (0..200)

class Server : UnicastRemoteObject(), IEnvService,
    ICookiesService {
    private val mSensorTypes = mapOf("air" to 3u)

    override fun requestEnvironmentDataTypes(): Array<String> {
        return mSensorTypes.keys.toTypedArray()
    }

    override fun requestEnvironmentData(_type: String?): EnvData {
        val time = System.currentTimeMillis() / SECONDS_DIVISOR

        return mSensorTypes[_type]?.let {
            val values = IntArray(it.toInt()) { SENSOR_VALUES_RANGE.random() }
            EnvData(time.toString(), _type, values)
        } ?: EnvData(time.toString(), "none", intArrayOf())
    }

    override fun requestAll(): Array<EnvData> {
        val time = System.currentTimeMillis() / SECONDS_DIVISOR

        return mSensorTypes.map {
            val values = IntArray(it.value.toInt()) { SENSOR_VALUES_RANGE.random() }
            EnvData(time.toString(), it.key, values)
        }.toTypedArray()
    }

    override fun saySomething() {
        println("cookies!")
    }
}
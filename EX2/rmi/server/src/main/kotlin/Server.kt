import java.rmi.server.UnicastRemoteObject

class Server : UnicastRemoteObject(), IEnvService {
    private val sensorTypes = mapOf("air" to 3u)

    override fun requestEnvironmentDataTypes(): Array<String> {
        return sensorTypes.keys.toTypedArray()
    }

    override fun requestEnvironmentData(_type: String?): EnvData {
        val time = System.currentTimeMillis() / 1000
        val values = IntArray(3) { (0..200).random() }

        return sensorTypes[_type]?.let {
            EnvData(time.toString(), _type, values)
        } ?: EnvData(time.toString(), "none", intArrayOf())
    }

    override fun requestAll(): Array<EnvData> {
        val time = System.currentTimeMillis() / 1000

        return sensorTypes.map {
            val values = IntArray(it.value.toInt()) { (0..200).random() }
            EnvData(time.toString(), it.key, values)
        }.toTypedArray()
    }

}
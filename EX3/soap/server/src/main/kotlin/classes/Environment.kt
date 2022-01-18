package classes

import Constants
import interfaces.IEnvironmentService
import jakarta.jws.WebService
import jakarta.xml.bind.JAXBContext
import jakarta.xml.soap.SOAPException
import org.eclipse.persistence.jaxb.UnmarshallerProperties
import org.eclipse.persistence.oxm.MediaType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URI
import javax.xml.transform.stream.StreamSource

const val SECONDS_DIVISOR = 1000

@WebService(endpointInterface = "interfaces.IEnvironmentService")
class Environment : IEnvironmentService {

    init {
        System.setProperty("javax.xml.bind.context.factory", "org.eclipse.persistence.jaxb.JAXBContextFactory")
        System.setProperty("jakarta.xml.bind.JAXBContextFactory", "org.eclipse.persistence.jaxb.JAXBContextFactory")
//        System.setProperty("jakarta.xml.bind.JAXBContextFactory", "org.glassfish.jaxb.runtime.v2.JAXBContextFactory")
    }

    private val mSupportedCities = setOf("Wien", "Linz")

    private val mJC by lazy { JAXBContext.newInstance(WeatherResponse::class.java) }

    private fun fetchFromOwm(_city: String, _mode: Mode): String {
        val url = StringBuilder(Constants.OWM_URL)
        url.append("?q=$_city,at")
        url.append("&units=${Constants.OWM_UNIT}")
        url.append("&lang=${Constants.USER_LANG}")
        url.append("&appid=${Constants.OWM_KEY}")

        if (_mode != Mode.JSON) url.append("&mode=${_mode.mType}")
        val connection = URI(url.toString()).toURL().openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

//        val status: Int = connection.responseCode
        val input = BufferedReader(InputStreamReader(connection.inputStream))
        var inputLine: String?
        val content = StringBuilder()

        while (input.readLine().also { inputLine = it } != null) {
            content.append(inputLine)
        }

        input.close()
        connection.disconnect()

        return content.toString()
    }


    private fun requestJSONData(_city: String): WeatherResponse {
        val unmarshaller = mJC.createUnmarshaller()
        unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false)
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON)

        val data = fetchFromOwm(_city, Mode.JSON)
        val json = StreamSource(StringReader(data))

        return unmarshaller.unmarshal(json, WeatherResponse::class.java).value
    }

    private fun requestXmlData(_city: String): WeatherResponse {
        val unmarshaller = mJC.createUnmarshaller()
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_XML)

        val data = fetchFromOwm(_city, Mode.XML)
        val xml = StreamSource(StringReader(data))

        return unmarshaller.unmarshal(xml) as WeatherResponse
    }

    override fun requestEnvironmentDataTypes(): Array<String> {
        return mSupportedCities.toTypedArray();
    }

    override fun requestData(_city: String): EnvData {
        if (!mSupportedCities.contains(_city))
            throw SOAPException(
                "Invalid city provided as input, for more information about the available cities " +
                        "call the following method: 'requestEnvironmentDataTypes()'"
            )

        val response = requestJSONData(_city)
        val time = (System.currentTimeMillis() / SECONDS_DIVISOR).toString()


        return EnvData(time, "humidity", intArrayOf(response.mMain?.mHumidity?.toInt() ?: 0))
    }
}

private enum class Mode(val mType: String) {
    JSON(""),
    XML("xml"),
    HTML("html")
}
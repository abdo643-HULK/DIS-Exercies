package classes

import Constants
import interfaces.IEnvironmentService
import jakarta.jws.WebService
import jakarta.xml.bind.JAXBContext
import org.eclipse.persistence.jaxb.UnmarshallerProperties
import org.eclipse.persistence.oxm.MediaType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URI
import javax.xml.transform.stream.StreamSource


@WebService(
    name = "classes.Environment",
    endpointInterface = "interfaces.IEnvironmentService",
    targetNamespace = ""
)
class Environment : IEnvironmentService {

    init {
        System.setProperty("jakarta.xml.bind.JAXBContextFactory", "org.eclipse.persistence.jaxb.JAXBContextFactory")
    }

    private val mSupportedCities = arrayOf("Wien", "Linz")

    private val mJC by lazy { JAXBContext.newInstance(WeatherResponse::class.java) }

    private fun fetchFromOwm(_city: String, _mode: Mode): String {
        val url = StringBuilder(Constants.OWM_URL)
        url.append("?q=$_city,at")
        url.append("&mode=${_mode}")
        url.append("&units=${Constants.OWM_UNIT}")
        url.append("&lang=${Constants.USER_LANG}")
        url.append("&appid=${Constants.OWM_KEY}")

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

    override fun requestEnvironmentDataTypes(): Array<String> {
        return mSupportedCities;
    }

    override fun requestJSONData(_city: String): WeatherResponse {
        val unmarshaller = mJC.createUnmarshaller()
        unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false)
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON)

        val data = fetchFromOwm(_city, Mode.JSON)
        val json = StreamSource(StringReader(data))

        return unmarshaller.unmarshal(json, WeatherResponse::class.java).value
    }

    override fun requestXmlData(_city: String): WeatherResponse {
        val unmarshaller = mJC.createUnmarshaller()
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_XML)

        val data = fetchFromOwm(_city, Mode.XML)
        val xml = StreamSource(StringReader(data))

        return unmarshaller.unmarshal(xml) as WeatherResponse
    }
}

private enum class Mode {
    XML, JSON
}
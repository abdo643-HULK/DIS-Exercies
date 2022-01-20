import interfaces.IEnvironmentService
import jakarta.xml.ws.Service
import java.net.URI
import javax.xml.namespace.QName

/**
 * connects to the soap service
 * and calls the methods with the
 * help of our Interface
 */
fun main() {
    val url = URI("${Constants.ENV_SERVER_URL}?wsdl")
    val name = QName("http://classes/", IEnvironmentService.ENVIRONMENT_SERVICE_KEY)
    val service = Service.create(url.toURL(), name)

    val soap = service.getPort(IEnvironmentService::class.java)

    println(soap.requestEnvironmentDataTypes().contentToString())

    try {
        val res = soap.requestData("Wien")
        println(res.contentToString())
    } catch (_e: Exception) {
        println(_e.localizedMessage)
    }
}
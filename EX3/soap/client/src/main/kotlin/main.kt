import interfaces.IEnvironmentService
import jakarta.xml.soap.SOAPException
import jakarta.xml.ws.Service
import java.net.URI
import javax.xml.namespace.QName

fun main() {
    val url = URI("${Constants.ENV_SERVER_URL}?wsdl")
    val name = QName("http://classes/", IEnvironmentService.ENVIRONMENT_SERVICE_KEY)
    val service = Service.create(url.toURL(), name)

    val soap: IEnvironmentService = service.getPort(IEnvironmentService::class.java)

    println(soap.requestEnvironmentDataTypes().contentToString())

    try {
        val res = soap.requestData("Wien")
        println(res.contentToString())
    } catch (_e: Exception) {
        println(_e.localizedMessage)
    }
}
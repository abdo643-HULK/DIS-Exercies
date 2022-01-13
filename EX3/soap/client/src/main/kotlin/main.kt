import interfaces.IEnvironmentService
import jakarta.xml.ws.Service
import java.net.URI
import javax.xml.namespace.QName

fun main() {
    val url = URI("${Constants.ENV_SERVER_URL}?wsdl")
    val name = QName("http://classes/","EnvironmentService")
    val service = Service.create(url.toURL(), name)

    val soap: IEnvironmentService = service.getPort(IEnvironmentService::class.java)
    val res = soap.requestJSONData(_city = "Wien")
    println(res.mMain)
    println(res.mBase)
}
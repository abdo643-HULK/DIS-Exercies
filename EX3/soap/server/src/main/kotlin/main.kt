import classes.Environment
import javax.xml.ws.Endpoint

/**
 * serves our Environment service
 */
fun main(_args: Array<String>) {
    val environment = Environment()
    Endpoint.publish(Constants.ENV_SERVER_URL, environment)
    println("server up and running ...")
}
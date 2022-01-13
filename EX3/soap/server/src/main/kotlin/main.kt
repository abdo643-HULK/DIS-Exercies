import classes.Environment
import jakarta.xml.ws.Endpoint

fun main(_args: Array<String>) {
    val environment = Environment()
    Endpoint.publish(Constants.ENV_SERVER_URL, environment)
    println("server up and running ...")
}
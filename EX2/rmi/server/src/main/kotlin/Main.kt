import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry

fun main(_args: Array<String>) {
    val server = Server()
    val reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT)
    reg.bind("EnvService", server)
    println("Server is waiting for queries ...")
}
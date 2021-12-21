import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry

const val ENV_SERVICE = "EnvService"

class ServiceMgmt {
    private val mServer by lazy { Server() }

    init {
        startMenu()
    }

    private fun startMenu() {
        while (true) {
            var inputMenu: UInt
            do {
                println();
                println("Choose:");
                println("------------------------:");
                println("1. Start RMI Service:");
                println("2. Stop RMI Service:");
                println("3. Quit:");
                println("------------------------:");
                inputMenu = readln().toUInt()
            } while (inputMenu < 1u || inputMenu > 3u)

            when (inputMenu) {
                1u -> startRmi()
                2u -> stopRmi()
                3u -> quit()
            }
        }
    }

    private fun startRmi() {
        val reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT)
        reg.bind(ENV_SERVICE, mServer)
    }

    private fun stopRmi() {

    }

    private fun quit() {

    }
}
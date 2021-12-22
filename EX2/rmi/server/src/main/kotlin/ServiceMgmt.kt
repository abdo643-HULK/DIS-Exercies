import java.lang.Exception
import java.rmi.NotBoundException
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.registry.Registry
import java.rmi.server.UnicastRemoteObject

const val ENV_SERVICE = "EnvService"

class ServiceMgmt {
    private val mRegistry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT)
    private val mServer by lazy { Server() }

    init {
        try {
            startMenu()
        } catch (_e: RemoteException) {
            System.err.println("ERROR CREATING REGISTRY: $_e")
        }
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
                3u -> return quit()
            }
        }
    }

    private fun startRmi() {
        try {
            mRegistry.rebind(ENV_SERVICE, mServer)
            println("RMI SERVICE STARTED")
        } catch (_e: RemoteException) {
            System.err.println("ERROR STARTING SERVER: $_e")
        }
    }

    private fun stopRmi() {
        try {
            mRegistry.unbind(ENV_SERVICE)
            UnicastRemoteObject.unexportObject(mServer, true)
            println("RMI SERVICE STOPPED")
        } catch (_e: Exception) {
            when (_e) {
                is RemoteException, is NotBoundException -> {
                    println("SERVER ALREADY STOPPED")
                }
            }
        }
    }

    private fun quit() {
        stopRmi()
        try {
            UnicastRemoteObject.unexportObject(mRegistry, true)
        } catch (_e: Exception) {
            System.err.println("ERROR QUITTING SERVICE MGMT: $_e")
        }
    }
}
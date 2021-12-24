import classes.EnvData
import interfaces.IEnvService
import java.rmi.registry.LocateRegistry

private const val ENV_SERVICE = "EnvService"

class EnvClient {
    private val mReg = LocateRegistry.getRegistry()
    private val mStub = mReg.lookup(ENV_SERVICE) as IEnvService

    fun requestAll(): Array<EnvData>? {
        return try {
            mStub.requestAll()
        } catch (_e: Exception) {
            null
        }
    }
}
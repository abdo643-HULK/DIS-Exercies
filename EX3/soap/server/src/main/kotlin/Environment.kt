import interfaces.EnvironmentService
import jakarta.jws.WebService

@WebService(endpointInterface = "interfaces/EnvironmentService")
class Environment : EnvironmentService{
    override fun requestEnvironmentDataTypes(): Array<String> {
        TODO("Not yet implemented")
    }

    override fun request(): Array<String> {
        TODO("Not yet implemented")
    }
}
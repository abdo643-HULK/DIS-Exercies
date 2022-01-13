package interfaces

import jakarta.jws.WebMethod
import jakarta.jws.WebService

@WebService
interface EnvironmentService {
    @WebMethod
    fun requestEnvironmentDataTypes() : Array<String>

    @WebMethod
    fun request() : Array<String>
}
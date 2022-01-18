package interfaces

import classes.EnvData
import jakarta.jws.WebMethod
import jakarta.jws.WebParam
import jakarta.jws.WebService
import jakarta.xml.soap.SOAPException
import kotlin.jvm.Throws

@WebService
interface IEnvironmentService {
    companion object {
        const val ENVIRONMENT_SERVICE_KEY = "EnvironmentService"
    }

    @WebMethod
    fun requestEnvironmentDataTypes(): Array<String>

    @Throws(SOAPException::class)
    @WebMethod
    fun requestData(
        @WebParam(name = "city") _city: String
    ): EnvData

//    @WebMethod
//    fun requestJSONData(_city: String): WeatherResponse
//
//    @WebMethod
//    fun requestXmlData(_city: String): WeatherResponse
}
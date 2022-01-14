package interfaces

import classes.EnvData
import jakarta.jws.WebMethod
import jakarta.jws.WebParam
import jakarta.jws.WebService

@WebService
interface IEnvironmentService {
    @WebMethod
    fun requestEnvironmentDataTypes(): Array<String>

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
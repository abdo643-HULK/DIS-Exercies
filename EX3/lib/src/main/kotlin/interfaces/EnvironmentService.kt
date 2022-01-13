package interfaces

import classes.WeatherResponse
import jakarta.jws.WebMethod
import jakarta.jws.WebService

@WebService
interface IEnvironmentService {
    @WebMethod
    fun requestEnvironmentDataTypes(): Array<String>

    @WebMethod
    fun requestJSONData(_city: String): WeatherResponse

    @WebMethod
    fun requestXmlData(_city: String): WeatherResponse
}
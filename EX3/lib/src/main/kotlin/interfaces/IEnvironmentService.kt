package interfaces

import classes.EnvData
import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebService
import javax.xml.soap.SOAPException
import kotlin.jvm.Throws

/**
 * The Interface for our Soap Service and Client
 */
@WebService
interface IEnvironmentService {
    companion object {
        /**
         * was better than adding it to the Constants class
         * Got it from the decompiled EnvironmentServiceIF
         * in the Jax-rs folder
         */
        const val ENVIRONMENT_SERVICE_KEY = "EnvironmentService"
    }

    /**
     * @return all the supported Locations
     */
    @WebMethod
    fun requestEnvironmentDataTypes(): Array<String>

    /**
     * @param _city the City to get the weather data of
     *
     * @return temp, humidity and pressure of the specified city as Array of EnvData
     */
    @Throws(SOAPException::class)
    @WebMethod
    fun requestData(
        @WebParam(name = "city") _city: String
    ): Array<EnvData>
}
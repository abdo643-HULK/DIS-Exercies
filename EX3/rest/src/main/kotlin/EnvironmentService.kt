//import jakarta.ws.rs.*
//import jakarta.ws.rs.core.Application
//import jakarta.ws.rs.core.MediaType

import classes.EnvData
import classes.EnvDataList
import javax.servlet.ServletContext
import javax.ws.rs.*
import javax.ws.rs.core.Application
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

const val SECONDS_DIVISOR = 1000

/**
 * Sends Sensor Data in multiple Types like xml,html etc.
 */
@Path("/")
class EnvironmentService : Application() {
    /**
     * gets initialized by TomCat and holds the Object we need
     * to access resources
     */
    @Context
    private lateinit var mServletContext: ServletContext

    companion object {
        /**
         * Holds our data that gets converted to EnvData
         * If its empty we generate random values until
         * it gets filled from the post Request
         */
        @JvmStatic
        private val sensors = mutableMapOf(
            "humidity" to intArrayOf(),
            "temperature" to intArrayOf(25),
        )
    }

    /**
     * reads the env html file and replaces the var with the table
     *
     * @return the html with the table for all sensors or a server error
     */
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    fun getDataHtml(): Response {
        val htmlFile = mServletContext.getResource("/env.html")?.readText()
            ?: return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .build()

        val time = (System.currentTimeMillis() / SECONDS_DIVISOR).toString()
        val envArray = sensors.map {
            EnvData(
                time,
                it.key,
                if (it.value.isNotEmpty()) it.value else intArrayOf((0..1000).random())
            )
        }
        val table = createTableBody(envArray.toTypedArray())
        val html = htmlFile.replace("%data%", table)

        return Response.ok().entity(html).build()
    }

    /**
     *  @return all available sensor types
     */
    @GET
    @Path("/sensors")
    @Produces(MediaType.TEXT_XML)
    fun getDataXml(): String {
        val sensorTypes = sensors.map { "<sensor>${it.key}</sensor>" }
        return """
            <xml version="1.0" encoding="UTF-8" />
            <sensors>
               ${sensorTypes.joinToString("\n")}
            </sensors>
            """.trimIndent()
    }

    /**
     * @return all sensors and their data as an EnvDataList
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_XML)
    fun getAll(): Response {
        val time = (System.currentTimeMillis() / SECONDS_DIVISOR).toString()
        val data = sensors.map {
            EnvData(
                time,
                it.key,
                if (it.value.isNotEmpty()) it.value else intArrayOf((0..1000).random())
            )
        }

        val response = EnvDataList(data)
//        val response = object : GenericEntity<List<EnvData>>(data) {}

        return Response.ok(response).build()
    }

    /**
     * @return an EnvData object of the requested sensor or a "NOT_FOUND" Response
     */
    @GET
    @Path("/{sensor}")
    @Produces(MediaType.APPLICATION_XML)
    fun getSensor(@PathParam("sensor") _sensor: String): Response {
        val sensorData = sensors[_sensor] ?: return Response.status(Response.Status.NOT_FOUND).build()
        val time = (System.currentTimeMillis() / SECONDS_DIVISOR).toString()

        return Response.ok().entity(
            EnvData(
                time,
                _sensor,
                if (sensorData.isNotEmpty()) sensorData else intArrayOf((0..1000).random())
            )
        ).build()
    }

    /**
     * Sets the values on the specified Sensor
     *
     * @return a Response with a message or a "NOT_FOUND" Response if the sensor couldn't be found
     */
    @POST
    @Path("/values")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_PLAIN)
    fun setValues(_data: EnvData): Response {
        sensors[_data.mSensor] ?: return Response.status(Response.Status.NOT_FOUND).build()
        sensors[_data.mSensor] = _data.mValues

        return Response.ok("Successfully Added").build()
    }

    /**
     * creates the table body for the html
     *
     * @return the html table body
     */
    private fun createTableBody(_envData: Array<EnvData>): String {
        return _envData.map {
            """
            <tr class="mdc-data-table__row">
                <td class="mdc-data-table__cell">${it.mTimestamp}</td>
                <td class="mdc-data-table__cell">${it.mSensor}</td>
                <td class="mdc-data-table__cell">${it.mValues.joinToString(separator = "; ") { value -> "$value" }}</td>
            </tr>                    
        """.trimIndent()
        }.joinToString(separator = "\n") { it }
    }
}
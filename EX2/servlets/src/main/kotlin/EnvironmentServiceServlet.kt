import java.io.IOException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import classes.EnvData

const val CPP_SERVER_PORT = 3000

const val OFFLINE_RMI =
    """
        <tr>
            <td colspan="3">
                <div class="offline">
                    RMI server offline
                </div>
            </td>
        </tr>
    """
const val OFFLINE_CPP =
    """
        <tr>
            <td colspan="3">
                <div class="offline">
                    C++ server offline
                </div>
            </td>
        </tr>
    """

@WebServlet(name = "EnvironmentServiceServlet", urlPatterns = ["/env"])
class EnvironmentServiceServlet : HttpServlet() {
//    private val mRmiClient by lazy {
//        try {
//            EnvClient()
//        } catch (_e: Exception) {
//            null
//        }
//    }
//    private val mCppClient by lazy {
//        try {
//            TcpEnviClient(CPP_SERVER_PORT, "127.0.0.1")
//        } catch (_e: IOException) {
//            null
//        }
//    }

    override fun doGet(_req: HttpServletRequest, _res: HttpServletResponse) {
        val rmiClient = try {
            EnvClient()
        } catch (_e: Exception) {
            null
        }

        val cppClient = try {
            TcpEnviClient(CPP_SERVER_PORT, "127.0.0.1")
        } catch (_e: Exception) {
            null
        }

        println(rmiClient)
        println(cppClient)

        val htmlFile =
            servletContext
                .getResource("/WEB-INF/classes/env.html")
                ?.readText() ?: run {
                _res.sendError(INTERNAL_SERVER_ERROR, "Internal Server Error")
                _res.writer.close()
                return
            }

        val rmiTable = rmiClient?.requestAll()?.let {
            createTableBody(it)
        } ?: OFFLINE_RMI

        val cppTable = cppClient?.requestAll()?.let {
            createTableBody(it)
        } ?: OFFLINE_CPP

        val html = htmlFile
            .replace("%cpp-data%", cppTable)
            .replace("%rmi-data%", rmiTable)
        _res.writer.println(html)
        _res.writer.close()
        cppClient?.close()
    }

//    override fun destroy() {
//        super.destroy()
//        mCppClient?.close()
//    }
}

fun createTableBody(_envData: Array<EnvData>): String {
    return _envData.map {
        """
            <tr class="mdc-data-table__row">
                <td class="mdc-data-table__cell">${it.getmTimestamp()}</td>
                <td class="mdc-data-table__cell">${it.getmSensor()}</td>
                <td class="mdc-data-table__cell">${it.getmValues().joinToString(separator = "; ") { value -> "$value" }}</td>
            </tr>                    
        """.trimIndent()
    }.joinToString(separator = "\n") { it }
}
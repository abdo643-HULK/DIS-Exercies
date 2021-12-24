import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val INTERNAL_SERVER_ERROR = 500

@WebServlet(name = "HelloWorldServlet", urlPatterns = ["/"])
class HelloWorldServlet : HttpServlet() {
    var mCalls = 0

    override fun doGet(_req: HttpServletRequest, _res: HttpServletResponse) {
        _res.contentType = "text/html"
        val htmlFile = servletContext.getResource("/WEB-INF/classes/template.html")
            ?.readText()
            ?.replace("%counter%", "${++mCalls}")

        htmlFile?.let {
            _res.writer.println(htmlFile)
            _res.writer.close()
            return
        }

        _res.sendError(INTERNAL_SERVER_ERROR, "Internal Server Error")
    }
}
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "HelloWorldServlet", urlPatterns = ["/"])
class HelloWorldServlet : HttpServlet() {
    var mCalls = 0

    override fun doGet(_req: HttpServletRequest, _res: HttpServletResponse) {
        val defaultHtml = "<body><div>hello world</div></body>";
        _res.contentType = "text/html"
        val htmlFile = servletContext.getResource("/WEB-INF/classes/template.html")
            ?.readText()
            ?.replace("%counter%", "${++mCalls}")
            ?: defaultHtml
        _res.writer.println(htmlFile)
        _res.writer.close()
    }
}
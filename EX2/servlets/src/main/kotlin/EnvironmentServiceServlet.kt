import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "EnvironmentServiceServlet", urlPatterns = ["/env"])
class EnvironmentServiceServlet: HttpServlet() {
    override fun doGet(_req: HttpServletRequest, _res: HttpServletResponse) {

    }
}
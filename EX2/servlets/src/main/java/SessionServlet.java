import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "SessionServlet", urlPatterns = {"/session"})
public class SessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest _req, HttpServletResponse _resp) throws IOException {
        HttpSession session = _req.getSession();
        String userAgent = _req.getHeader("user-agent");
        String browserName = "";
        int magicNumber = 0;

        try {
            magicNumber = Integer.parseInt(_req.getParameter("magicNumber"));
        } catch (Exception _ignored) {

        }

	    browserName = getBrowser(userAgent);

	    InputStream htmlFile = getServletContext().getResourceAsStream("/WEB-INF/classes/session.html");
        String html = new String(htmlFile.readAllBytes(), StandardCharsets.UTF_8);

        PrintWriter writer = _resp.getWriter();

        html = html
                .replace("%browser%", browserName)
                .replace("%magicNumber%", "" + magicNumber)
                .replace("%prevMagicNumber%", "" + session.getAttribute("magicNumber"));

        session.setAttribute("magicNumber", magicNumber);

        writer.println(html);
        writer.close();
    }

    String getBrowser(String _userAgent) {
        if (_userAgent.contains("Chrome")) { //checking if Chrome
            String substring = _userAgent.substring(_userAgent.indexOf("Chrome")).split(" ")[0];
            return substring.split("/")[0];
        } else if (_userAgent.contains("Firefox")) {  //Checking if Firefox
            String substring = _userAgent.substring(_userAgent.indexOf("Firefox")).split(" ")[0];
            return substring.split("/")[0];
        }

        return "";
    }
}

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userAgent = req.getHeader("user-agent");
        String browserName = "";
        int magicNumber = 0;

        try {
            magicNumber = Integer.parseInt(req.getParameter("magicNumber"));
        } catch (Exception ignored) {

        }


        if (userAgent.contains("Chrome")) { //checking if Chrome
            String substring = userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0];
            browserName = substring.split("/")[0];
        } else if (userAgent.contains("Firefox")) {  //Checking if Firefox
            String substring = userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0];
            browserName = substring.split("/")[0];
        }


        InputStream htmlFile = getServletContext().getResourceAsStream("/WEB-INF/classes/session.html");
        String html = new String(htmlFile.readAllBytes(), StandardCharsets.UTF_8);

        PrintWriter writer = resp.getWriter();

        html = html
                .replace("%browser%", browserName)
                .replace("%magicNumber%", "" + magicNumber)
                .replace("%prevMagicNumber%", "" + session.getAttribute("magicNumber"));

        session.setAttribute("magicNumber", magicNumber);

        writer.println(html);
        writer.close();
    }


}

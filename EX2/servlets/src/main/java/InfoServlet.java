import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "InfoServlet", urlPatterns = {"/info"})
public class InfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ipAddress = req.getHeader("X-FORWARDED-FOR");
        String port = String.valueOf(req.getRemotePort());
        String userAgent = req.getHeader("user-agent");
        String browserName = "";
        String mimeTypes = req.getHeader("accept");
        String clientProtocol = req.getProtocol();
        String serverName = req.getServerName();
        String backgroundParameter = req.getParameter("background");


        if (ipAddress == null) {
            ipAddress = req.getRemoteAddr();
        }

        if(userAgent.contains("Chrome")){ //checking if Chrome
            String substring=userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0];
            browserName=substring.split("/")[0];
        }
        else if(userAgent.contains("Firefox")){  //Checking if Firefox
            String substring=userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0];
            browserName=substring.split("/")[0];
        }

        if(backgroundParameter == null || backgroundParameter.equals("")) {
            backgroundParameter = "turquoise";
        }





        InputStream htmlFile = getServletContext().getResourceAsStream("/WEB-INF/classes/info.html");
        String html = new String(htmlFile.readAllBytes(), StandardCharsets.UTF_8);

        PrintWriter writer = resp.getWriter();


        html = html
                .replace("%ipAddress%", ipAddress)
                .replace("%browser%", browserName)
                .replace("%mimeTypes%", mimeTypes)
                .replace("%clientProtocol%", clientProtocol)
                .replace("%port%", port)
                .replace("%serverName%", serverName)
                .replace("%backgroundColor%", backgroundParameter);



        writer.println(html);
        writer.close();
    }
}

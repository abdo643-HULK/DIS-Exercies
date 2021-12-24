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
	protected void doGet(HttpServletRequest _req, HttpServletResponse _resp) throws ServletException, IOException {
		String ipAddress = _req.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = _req.getRemoteAddr();
		}

		String userAgent = _req.getHeader("user-agent");
		String browserName = "";
		browserName = getBrowser(userAgent);

		String backgroundParameter = _req.getParameter("background");
		if (backgroundParameter == null || backgroundParameter.equals("")) {
			backgroundParameter = "turquoise";
		}

		String port = String.valueOf(_req.getRemotePort());
		String mimeTypes = _req.getHeader("accept");
		String clientProtocol = _req.getProtocol();
		String serverName = _req.getServerName();

		InputStream htmlFile = getServletContext().getResourceAsStream("/WEB-INF/classes/info.html");
		String html = new String(htmlFile.readAllBytes(), StandardCharsets.UTF_8);

		PrintWriter writer = _resp.getWriter();

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

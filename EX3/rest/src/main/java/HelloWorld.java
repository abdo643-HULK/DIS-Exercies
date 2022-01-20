//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.Application;
//import jakarta.ws.rs.core.MediaType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

/**
 * A Test Class
 * Pretty much copied from the slides
 */
@Path("/")
public class HelloWorld extends Application {
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getDataHtml() {
		return "<html>"
				+ "<head></head>"
				+ "<body>Hello World (HTML text)!!!</body>"
				+ "</html>";
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getDataPlain() {
		return "Hello World (plain text)";
	}

	@GET
	@Path("/xml")
	@Produces(MediaType.TEXT_XML)
	public String getDataXml() {
		return "<HelloWorld>Hello World (XML text)!!!</HelloWorld>";
	}

	@GET
	@Path("/json")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDataJson() {
		return "{\"val\":\"HelloWorld\"}";
	}
}

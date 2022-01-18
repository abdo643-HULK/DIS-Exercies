package helloworld;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloWorld extends Application {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getDataHTML() {
        return """
                <html>
                <head></head>
                    <body>Hello World (HTML text)!!!</body>
                </html>
                """;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getDataPlain() {
        return "Hello World (plain text)";
    }
    @GET
    @Path("/xml")
    @Produces(MediaType.TEXT_XML)
    public String getDataXML() {
        return "<helloworld.HelloWorld>Hello World (XML text)!!!</helloworld.HelloWorld>";
    }
    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDataJSON() {
        return "{\"val\":\"helloworld.HelloWorld\"}";
    }

}

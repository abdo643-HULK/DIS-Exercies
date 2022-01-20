import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.oxm.MediaType;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Our class that Tests the Jaxb
 * marshaller and unmarshaller
 */
public class Main {
	/**
	 * The first part parses the xml string and
	 * constructs a Pet Object and this
	 * again gets serialized to a xml string
	 *
	 * The second part parses the json string and
	 * constructs a Pet Object and this
	 * again gets serialized to a json string
	 *
	 * @param _args the args passed to the program
	 */
	public static void main(String[] _args) {
//		System.setProperty("jakarta.xml.bind.JAXBContextFactory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
		System.setProperty("javax.xml.bind.JAXBContextFactory","org.eclipse.persistence.jaxb.JAXBContextFactory");

		try {
			System.out.println("-------------- XML --------------");
			var xmlString = new StringReader(""" 
					<pet nick-name="Tom" xmlns:ns2="http://www.example.com/TYPE">
					    <name>Thomas</name>
					    <birthday>1979-10-21T03:31:12</birthday>
					    <ns2:typ>CAT</ns2:typ>
					    <vaccinations>
					        <vaccination>cat flu</vaccination>
					        <vaccination>feline distemper</vaccination>
					        <vaccination>rabies</vaccination>
					        <vaccination>leucosis</vaccination>
					    </vaccinations>
					    <id>123456789</id>
					</pet>""");

			var sw = new StringWriter();

			JAXBContext jc = JAXBContext.newInstance(Pet.class);
			final var unmarshaller = jc.createUnmarshaller();
			Pet xmlRes = (Pet) unmarshaller.unmarshal(xmlString);
			System.out.println(xmlRes);


			final var marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(xmlRes, sw);

			final var xml = sw.getBuffer().toString();
			System.out.println('\n' + xml);

			System.out.println("-------------- JSON --------------");

			final var jsonString = new StringReader("""
					{
					      "nick-name" : "Tom",
					      "name" : "Thomas",
					      "typ" : "CAT",
					      "id" : "123456789",
					      "birthday" : "1979-10-21T03:31:12+01:00",
					      "vaccinations" : {
					         "vaccination" : [ "cat flu", "feline distemper", "rabies", "leucosis" ]
					      }
					}
					""");
			unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, false);
			unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
			Pet jsonRes =  unmarshaller.unmarshal(new StreamSource(jsonString), Pet.class).getValue();
			System.out.println(jsonRes);

			marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
			sw.getBuffer().setLength(0);
			marshaller.marshal(jsonRes, sw);

			final var json = sw.getBuffer().toString();
			System.out.println('\n' + json);
		} catch (Exception _e) {
			_e.printStackTrace();
		}
	}
}

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) throws JAXBException {

        try { var xml = new StringReader(""" 
                <Pet nick-name="Tom">
                    <name>Thomas</name>
                    <birthday>10.02.1940</birthday>
                    <TYPE:typ>CAT</TYPE>
                    <vaccinations>
                        <vaccination>cat flu</vaccination>
                        <vaccination>feline distemper</vaccination>
                        <vaccination>rabies</vaccination>
                        <vaccination>leucosis</vaccination>
                    </vaccinations>
                    <id>123456789</id>
                </Pet>""");
            JAXBContext jc = JAXBContext.newInstance(Pet.class);

            Unmarshaller um = jc.createUnmarshaller();
            Pet res = (Pet)um.unmarshal(xml);

            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

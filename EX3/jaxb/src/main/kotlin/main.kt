import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.SAXParserFactory

fun main() {
    val xml = StringReader(
        """
    <?xml version="1.0" encoding="UTF-8"?>
    <wind>
         <speed>50.25</speed>
         <deg>225</deg>
    </wind>
    """.trimIndent()
    )

    try {
        val factory = SAXParserFactory.newInstance()
        val saxParser = factory.newSAXParser()
        val handler = WeatherHandler()

        saxParser.parse(InputSource(xml), handler)
        println(handler.mWind)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
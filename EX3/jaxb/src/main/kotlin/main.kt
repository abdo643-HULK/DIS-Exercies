import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.SAXParserFactory

/**
 * Uses a stream parser (sax) to
 * construct a Wind class from the xml
 */
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
    } catch (_e: Exception) {
        _e.printStackTrace()
    }
}
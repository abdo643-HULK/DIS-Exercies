import org.xml.sax.InputSource
import java.io.File
import java.io.FileInputStream
import java.io.StringReader
import java.util.*
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
        val props = Properties(10)
        props.load(FileInputStream(File("gradle.properties")))

        val factory = SAXParserFactory.newInstance()
        val saxParser = factory.newSAXParser()
        val handler = WeatherHandler()

        saxParser.parse(InputSource(xml), handler)
        println(handler.mWind)
    } catch (_e: Exception) {
        _e.printStackTrace()
    }
}
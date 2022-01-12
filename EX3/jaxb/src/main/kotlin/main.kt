import javax.xml.parsers.SAXParserFactory

fun main() {
    val xml = """
    <wind>
         <speed>48.31</speed>
         <deg>14.29</deg>
    </wind>
    """.trimIndent()

    try {
        val factory = SAXParserFactory.newInstance()
        val saxParser = factory.newSAXParser()
        val handler = WeatherHandler()

        saxParser.parse(xml, handler)

    } catch (e: Exception) {
        e.printStackTrace()
    }
}
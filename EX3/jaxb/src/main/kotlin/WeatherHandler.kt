import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler


const val DEG_TAG = "deg"
const val SPEED_TAG = "speed"


/**
 * The Stream Parser for the Sax Example
 */
class WeatherHandler : DefaultHandler() {
    /**
     * the class that gets constructed from the XML
     */
    val mWind: Wind by lazy { Wind() }

    /**
     * holds the value of the current Value to parse
     */
    private val mElementValue by lazy { StringBuilder() }

    override fun characters(_ch: CharArray?, _start: Int, _length: Int) {
        mElementValue.append(_ch, _start, _length)
    }

    /**
     * clears the string builder when a new element gets read
     */
    override fun startElement(_uri: String?, _localName: String?, _qName: String?, _attributes: Attributes?) {
        when (_qName) {
            SPEED_TAG, DEG_TAG -> mElementValue.clear()
        }
    }

    /**
     * Sets the value of the current tag
     */
    override fun endElement(_uri: String?, _localName: String?, _qName: String?) {
        when (_qName) {
            DEG_TAG -> mWind.mDeg = mElementValue.toString().toFloat()
            SPEED_TAG -> mWind.mSpeed = mElementValue.toString().toFloat()
        }
    }
}
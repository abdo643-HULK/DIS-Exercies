package classes

import java.io.Serializable
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

/**
 * Our root/container for a list of EnvData
 * because the GenericEntity Class didn't work
 */
@XmlRootElement(name = "env-data-list")
class EnvDataList(
    @field:XmlElement(name = "env-data")
    val mData: List<EnvData>
) : Serializable {
    companion object {
        const val serialVersionUID = 124L
    }

    constructor() : this(emptyList<EnvData>())
}

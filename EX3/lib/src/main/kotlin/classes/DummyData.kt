package classes

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "dummy")
@XmlAccessorType(XmlAccessType.FIELD)
data class DummyData(
    @field:XmlElement(name = "first-name")
    val mFirstName: String,
    @field:XmlElement(name = "last-name")
    val mLastName: String
) {
    constructor() : this("", "")
}
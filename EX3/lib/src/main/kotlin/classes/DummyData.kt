package classes

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

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
package classes

import jakarta.xml.bind.annotation.XmlElement

data class DummyData(@XmlElement val mFirstName: String, @XmlElement val mLastName: String)
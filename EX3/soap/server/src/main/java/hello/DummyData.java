package hello;

import jakarta.xml.bind.annotation.XmlElement;

public class DummyData {
    @XmlElement
    String mFirstName;
    @XmlElement
    String mLastName;
    public DummyData() {}
    public DummyData(String _FirstName, String _lastName) {
        mFirstName = _FirstName;
        mLastName = _lastName;
    }
    public String toString() {
        return "hello.DummyData from (" + mLastName + ") --> " + mFirstName;
    }
}

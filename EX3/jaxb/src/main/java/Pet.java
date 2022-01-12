import javax.xml.bind.annotation.*;
import java.util.Arrays;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Pet {
	@XmlElement(name="name")
	String mName;
	@XmlAttribute(name="nick-name")
	String mNickName;
	@XmlElement(name="birthday")
	Date mBirthday;

	@XmlElement(name="typ", namespace = "TYPE")
	Type mTyp; // enum cat, dog, mouse and bird
	@XmlElementWrapper(name = "vaccinations")
	@XmlElement(name="vaccination")
	String[] mVaccinations;
	@XmlElement(name="id")
	String mID;

	public Pet() {

	}
	public Pet(String _name, String _nickName, Date _birthDay, Type _typ, String[] _vaccinations, String _id) {
		mName = _name;
		mNickName = _nickName;
		mBirthday = _birthDay;
		mTyp = _typ;
		mVaccinations = _vaccinations;
		mID = _id;
	}

	@Override
	public String toString() {
		return "Name: " + mName + "\nNickname: " + mNickName + "\nBirtday: " + mBirthday + "\nTyp: " + mTyp + "\nVaccinations: " + Arrays.toString(mVaccinations) + "\nID: " +mID;
	}

}

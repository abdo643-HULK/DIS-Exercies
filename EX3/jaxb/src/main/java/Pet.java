import jakarta.xml.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

@XmlRootElement(name = "pet")
@XmlType(propOrder = { "mName", "mTyp", "mId", "mBirthday", "mVaccinations" })
public class Pet {
	@XmlElement(name = "name")
	String mName;

	@XmlAttribute(name = "nick-name")
	String mNickName;

	@XmlElement(name = "birthday")
	Date mBirthday;

	@XmlElement(name = "typ", namespace = "http://www.example.com/TYPE")
	Type mTyp;

	@XmlElementWrapper(name = "vaccinations")
	@XmlElement(name = "vaccination")
	String[] mVaccinations;

	@XmlElement(name = "id")
	String mId;

	public Pet() {
	}

	public Pet(String _name, String _nickName, Date _birthday, Type _typ, String[] _vaccinations, String _id) {
		mName = _name;
		mNickName = _nickName;
		mBirthday = _birthday;
		mTyp = _typ;
		mVaccinations = _vaccinations;
		mId = _id;
	}

	@Override
	public String toString() {
		return "Name: " + mName
				+ "\nNickname: " + mNickName
				+ "\nBirtday: " + mBirthday
				+ "\nTyp: " + mTyp
				+ "\nVaccinations: " + Arrays.toString(mVaccinations)
				+ "\nID: " + mId;
	}
}

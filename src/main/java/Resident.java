import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("residents")
public abstract class Resident {
	@Id
	public short Id;//6 digit id

	public String FirstName;
	public String LastName;
	public int DormRoom;
	public int DormFloor;

	public abstract float GetRent();

	public String GetFullName() {
		return FirstName + " " + LastName;
	}

	public Object[] ToObjectArray() {
		return new Object[]{this.getClass().getName().replace("Resident", ""), Id, FirstName, LastName, GetRent(), DormFloor, DormRoom};
	}

	public static String[] Labels() {
		return new String[]{"Resident Type", "ID", "First Name", "Last Name", "Rent", "Dorm Floor", "Dorm Number"};
	}
}
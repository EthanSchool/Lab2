import com.mongodb.BasicDBObject;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

@Entity("users")
public class User {

	@Id
	public String id;

	@Property("Password")
	public String password;

}
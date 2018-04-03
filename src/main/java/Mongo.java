import com.mongodb.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class Mongo {

	private static final Morphia morphia = new Morphia();
			//username:PjWtMobwpnHG4W
	public static final Datastore datastore = morphia.createDatastore(new MongoClient(new MongoClientURI("mongodb://username:PjWtMobwpnHG4W@ds231719.mlab.com:31719/javatesing")), "javatesing");
//	private static MongoClient client;
//	private static MongoDatabase database;

//	private static final DBCollection users = datastore.getCollection(User.class);
//	private static MongoCollection<User> users;
//	private static MongoCollection<Resident> residents;

//	private static Mongo Instance = new Mongo();

	public Mongo() {
		morphia.mapPackage("User");
		datastore.ensureIndexes();
//		final DBCollection users = datastore.getCollection(User.class);
//		datastore.collec
//		users.find


//		client = new MongoClient(new MongoClientURI("mongodb://username:5wsvfrcDf@ds223609.mlab.com:23609/testing"));
//		database = client.getDatabase("testing");
//
//		users = database.getCollection("users", User.class);
//		residents = database.getCollection("residents", Resident.class);

//		User user = users.find().first();
//		user.hashCode();

//		BsonTypeClassMap

//		Resident resident = new Resident();
//		resident.FirstName = "Will";
//		resident.LastName = "Johnson";
//		resident.DormFloor = 6;
//		resident.DormRoom = 8;
//		datastore.save(resident);
	}

	public static boolean CheckLogin(String username, String pass) {
		Query<User> query = datastore.createQuery(User.class)
			.field("_id").equal(username)
			.field("Password").equal(pass);

		return query.count() != 0;
	}
}
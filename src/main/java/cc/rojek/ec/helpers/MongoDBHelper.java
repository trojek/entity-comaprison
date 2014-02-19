package cc.rojek.ec.helpers;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoDBHelper {

	private static final int port = 27017;
	private static final String host = "localhost";
	
	public static DB connectWithDB(String dbName) {

		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		DB mongoDB = mongoClient.getDB(dbName);
		return mongoDB;
	}
	
}

package cc.rojek.ec.application_domain_model;

import java.net.UnknownHostException;
import java.util.Set;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class ApplicationDomainModel {

	DB db;

	public ApplicationDomainModel(String dbName) {

		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		DB db = mongoClient.getDB(dbName);
		this.db = db;
	}

	public void addData(String data, String collectionName) {
		DBCollection collection = db.getCollection(collectionName);

		DBObject dbObject = (DBObject) JSON.parse(data);

		collection.insert(dbObject);
	}

	public void printData(String collectionName) {
		DBCollection table = db.getCollection(collectionName);

		DBCursor cursor = table.find();

		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			DBObject object_connections = (DBObject) object.get("connections");
			System.out.println(object.get("object"));
			Set<String> connectionsSet = object_connections.keySet();
			for (String key : connectionsSet) {
				System.out.println("   " + object_connections.get(key));
			}
		}
	}

	public void joinObjectNodesWithOntology(String collectionName) {
		DBCollection table = db.getCollection(collectionName);

		DBCursor cursor = table.find();

		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			DBObject object_connections = (DBObject) object.get("connections");
			System.out.println(object.get("object"));
			Set<String> connectionsSet = object_connections.keySet();
			for (String key : connectionsSet) {
				System.out.println("   " + object_connections.get(key));
			}
		}
	}
}

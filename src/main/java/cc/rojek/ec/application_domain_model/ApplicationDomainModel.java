package cc.rojek.ec.application_domain_model;

import java.net.UnknownHostException;

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
		
		DBObject dbObject = (DBObject)JSON.parse(data);
		 
		collection.insert(dbObject);

	}
	
	public void printData(String collectionName){
		DBCollection table = db.getCollection(collectionName);

		BasicDBObject searchQuery = new BasicDBObject();

		DBCursor cursor = table.find(searchQuery);

		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}
	
}

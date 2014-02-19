package cc.rojek.ec.application_domain_model;

import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import cc.rojek.ec.helpers.MongoDBHelper;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
//import cc.rojek.ec.algorithm1.JoinOntology;

public class ApplicationDomainModel {

	DB db;

	public ApplicationDomainModel(String dbName) {

		this.db = MongoDBHelper.connectWithDB(dbName);
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

	public ArrayList<ObjectModel> getListOfObjectAndConnectedNodes(String collectionName) throws OWLOntologyCreationException {
		
		ArrayList<ObjectModel> objectList = new ArrayList<ObjectModel>();
		
		DBCollection table = db.getCollection(collectionName);

		DBCursor cursor = table.find();
		
		while (cursor.hasNext()) {
			
			DBObject object = cursor.next();
			DBObject object_connections = (DBObject) object.get("connections");

			Set<String> connectionsSet = object_connections.keySet();
			ObjectModel om = new ObjectModel( (String) object.get("object") );
			for (String key : connectionsSet) {
				om.connectionsList.add((String) object_connections.get(key));
			}
			objectList.add(om);
		}
		return objectList;
	}
}

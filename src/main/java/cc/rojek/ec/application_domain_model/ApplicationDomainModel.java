package cc.rojek.ec.application_domain_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

//import cc.rojek.ec.algorithm1.JoinOntology;

public class ApplicationDomainModel {

	MongoOperations mongoOperation;

	public ApplicationDomainModel(MongoOperations mongoOperation) {
		this.mongoOperation = mongoOperation;
	}

	public void printData() {
		Query query = new Query();
		// query5.addCriteria(Criteria.where("age").gte(30));
		// query5.with(new Sort(Sort.Direction.DESC, "age"));

		List<Object> objectList = mongoOperation.find(query, Object.class);

		for (Object object : objectList) {
			System.out.println("obj: " + object.getNodeName());
			for (String connection : object.getConnectionsList()) {
				System.out.print(" " + connection);
			}
			System.out.println();
		}
	}

	public ArrayList<Object> getListOfObjectAndConnectedNodes() {

		ArrayList<Object> objectList = new ArrayList<Object>();

		DBCollection table = db.getCollection(collectionName);

		DBCursor cursor = table.find();

		while (cursor.hasNext()) {

			DBObject object = cursor.next();
			DBObject object_connections = (DBObject) object.get("connections");

			Set<String> connectionsSet = object_connections.keySet();
			Object om = new Object((String) object.get("object"));
			for (String key : connectionsSet) {
				om.getConnectionsList().add(
						(String) object_connections.get(key));
			}
			objectList.add(om);
		}
		return objectList;
	}
}

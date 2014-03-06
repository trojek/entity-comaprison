package cc.rojek.ec.mongodb_domain_model;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

public class ObjectOperations {

	MongoOperations mongoOperation;
	
	public ObjectOperations(MongoOperations mongoOperation) {
		this.mongoOperation = mongoOperation;
	}

	public void printData() {
		Query query = new Query();

		List<Object> objectList = mongoOperation.find(query, Object.class);

		for (Object object : objectList) {
			System.out.println("obj: " + object.getNodeName());
			for (String connection : object.getConnectionsList()) {
				System.out.print(" " + connection);
			}
			System.out.println();
		}
	}

	public List<Object> getListOfObjectAndConnectedNodes() {
		Query query = new Query();
		List<Object> objectList = mongoOperation.find(query, Object.class);
		return objectList;
	}
	
}

package cc.rojek.ec.application_domain_model;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

//import cc.rojek.ec.algorithm1.JoinOntology;

public class ApplicationDomainModel {

	MongoOperations mongoOperation;

	public ApplicationDomainModel(MongoOperations mongoOperation) {
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

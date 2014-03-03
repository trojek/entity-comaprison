package cc.rojek.ec;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.semanticweb.owlapi.model.OWLOntology;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import cc.rojek.ec.algorithm1.JoinOntology;
import cc.rojek.ec.algorithm2.DataSet;
import cc.rojek.ec.application_domain_model.ApplicationDomainModel;
import cc.rojek.ec.helpers.Neo4jHelper;
import cc.rojek.ec.helpers.OntologyHelper;
import cc.rojek.ec.mapdata.Neo4jAndOWL;
import cc.rojek.ec.mongodb_domain_model.Object;

public class Main {

	private static final String DB_PATH = "/home/tomasz/Programy/neo4j-community-2.0.0/data/sample.db";
	private static final String ONTOLOGY_URL = "data/olympic_games_pure.owl";

	public static void main(String[] args) throws Exception {

		ApplicationContext ctx = new GenericXmlApplicationContext("SpringMongoConfig.xml");
		MongoOperations mongoOperation = (MongoOperations)ctx.getBean("olympicID");
				
		ApplicationDomainModel adm = new ApplicationDomainModel(mongoOperation);
		// adm.printData();
		
		List<Object> mainList = adm.getListOfObjectAndConnectedNodes();
		JoinOntology jOnto = new JoinOntology(ONTOLOGY_URL);
		String newOntologyURL = jOnto.moveJoinPointsToOWLFile(mainList);
		
		OWLOntology ontology = OntologyHelper.loadOntologyFromFile(newOntologyURL);
		GraphDatabaseService db = Neo4jHelper.startNeo4jDB(DB_PATH);

		Neo4jAndOWL exampleon = new Neo4jAndOWL(db, ontology);
		exampleon.importOntology();
		
		DataSet compareResult = new DataSet(db);
		compareResult.compareObjectsWith(16l);
	}
}

package cc.rojek.ec;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import cc.rojek.ec.algorithm1.JoinOntology;
import cc.rojek.ec.algorithm2.CompareObject;
import cc.rojek.ec.application_domain_model.ApplicationDomainModel;
import cc.rojek.ec.mapdata.Neo4jAndOWL;

public class Main {

	private static final String DB_PATH = "/home/tomasz/Programy/neo4j-community-2.0.0/data/sample.db";
	private static final String ONTOLOGY_URL = "data/olympic_games_pure.owl";
	private static final String NEW_ONTOLOGY_URL = "data/olympic_games_new.owl";


	public static void main(String[] args) {

		OWLOntology ontology = loadOntologyFromFile(ONTOLOGY_URL);

		try {
			JoinOntology.createIndividualNode("s");
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//GraphDatabaseService db = new GraphDatabaseFactory()
		//		.newEmbeddedDatabase(DB_PATH);
		//registerShutdownHook(db);

		// Neo4jAndOWL exampleon = new Neo4jAndOWL(db, ontology);

		// mapOntologyIntoNeo4j(exampleon);
		
		//CompareObject compareResult = new CompareObject(db, 13l);
		//WcompareResult.compareObjectsWith();
		
		
		//String json = "{'object' : 'object1', 'name' : 'Turin', connections: { 'Location' : 'Italy', 'Season' : 'Summer' } } }";
		//ApplicationDomainModel test = new ApplicationDomainModel("olympic");
		//test.addData(json, "objects");
		//test.printData("objects");
	}

	// Method which ensures that the database shut down cleanly
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
				System.out.println("Neo4j shutdown (shutdown hook)");
			}
		});
	}

	private static OWLOntology loadOntologyFromFile(String fileName) {
		File file = new File(fileName);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		// Load the local copy
		OWLOntology localOntology = null;
		try {
			localOntology = manager.loadOntologyFromOntologyDocument(file);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		return localOntology;
	}
	
	private static void mapOntologyIntoNeo4j(Neo4jAndOWL obj) {
		try {
			obj.importOntology();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

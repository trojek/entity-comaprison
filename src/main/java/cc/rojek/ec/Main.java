package cc.rojek.ec;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import cc.rojek.ec.algorithm2.CompareObject;
import cc.rojek.ec.mapdata.Neo4jAndOWL;

public class Main {

	private static final String DB_PATH = "/home/tomasz/Programy/neo4j-community-2.0.0/data/sample.db";
	private static final String ONTOLOGY_URL = "data/olympic_games.owl";

	public static void main(String[] args) {

		OWLOntology ontology = loadOntologyFromFile(ONTOLOGY_URL);

		GraphDatabaseService db = new GraphDatabaseFactory()
				.newEmbeddedDatabase(DB_PATH);
		registerShutdownHook(db);

		// Neo4jAndOWL exampleon = new Neo4jAndOWL(db, ontology);

		// mapOntologyIntoNeo4j(exampleon);
		
		CompareObject compareResult = new CompareObject(db, 13l);
		compareResult.compareObjectsWith();
	}

	// Method which ensures that the database shut down cleanly
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
				System.out.println("Db shutdown (shutdown hook)");
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

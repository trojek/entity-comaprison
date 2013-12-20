package cc.rojek.ec;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Main {

	private static final String DB_PATH = "/opt/neo4j/neo4j-community-2.0.0-M06/data/sample.db";
	private static final String ONTOLOGY_URL = "data/pizza_original.owl";

	static GraphDatabaseService db;

	public static void main(String[] args) throws Exception {
		// Get hold of an ontology manager
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File(ONTOLOGY_URL);

		// Load the local copy
		OWLOntology localPizza = manager.loadOntologyFromOntologyDocument(file);

		// Initialize database
		db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		
		MapOWLtoNeo4j.importOntology(localPizza, db);
	}

	
}


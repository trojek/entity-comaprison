package cc.rojek.ec;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Main {

	private static final String DB_PATH = "/home/tomasz/Programy/neo4j-community-2.0.0/data/sample.db";
	private static final String ONTOLOGY_URL = "data/olympic_games.owl";
	// private static final String ADM0 = "data/adm0_test.xml";
	
	static GraphDatabaseService db;

	public static void main(String[] args) throws Exception {
		
		// ADM adm0 = new ADM(ADM0);

		File file = new File(ONTOLOGY_URL);
		OWLOntology ontology = loadOntologyFromFile(file);

		// Initialize database
		db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		registerShutdownHook(db);
		
		MapOWLtoNeo4j.importOntology(ontology, db);
	}
	
	// Method which ensures that the database shut down cleanly
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	            System.out.println("used shut down hook.");
	        }
	    } );
	}

	private static OWLOntology loadOntologyFromFile(File file){
		// Get hold of an ontology manager
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
}


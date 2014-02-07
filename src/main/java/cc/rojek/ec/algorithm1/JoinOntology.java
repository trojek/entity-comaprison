package cc.rojek.ec.algorithm1;

import java.io.File;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Join OWL file with Neo4j graph database
 */
public class JoinOntology {

	private static final String ONTOLOGY_URL = "data/it.owl";

	public void doOperation() throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		File file = new File(ONTOLOGY_URL);
		OWLOntology localPizza = manager.loadOntologyFromOntologyDocument(file);
	}

	private void createNode() {
	}

	private void updateNode() {
	}

	private void deleteNode() {
	}

}

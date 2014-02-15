package cc.rojek.ec.helpers;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OntologyHelper {

	public static OWLOntology loadOntologyFromFile(String fileName) {
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

}

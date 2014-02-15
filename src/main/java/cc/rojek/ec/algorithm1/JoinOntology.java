package cc.rojek.ec.algorithm1;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Join OWL file with Neo4j graph database
 */
public class JoinOntology {

	public static void createIndividualNode(String nodeName) throws OWLOntologyCreationException {
		
		File file1 = new File("data/olympic_games_pure.owl");
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		OWLOntology localOntology = manager.loadOntologyFromOntologyDocument(file1);
		
		IRI ontologyIRI = IRI.create("http://rojek.cc/ec/ontologies/olimpic.owl");
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		OWLClass person = factory.getOWLClass(IRI.create(ontologyIRI + "#Turin"));
		
		OWLNamedIndividual object1 = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Object1"));
		
		OWLAxiom axiom = factory.getOWLClassAssertionAxiom(person, object1);

		AddAxiom addAxiom = new AddAxiom(localOntology, axiom);
        manager.applyChange(addAxiom);
        
		File file = new File("data/olympic_games_new.owl");

		try {
			manager.saveOntology(localOntology, IRI.create(file.toURI()));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private void updateNode() {
	}

	private void deleteNode() {
	}

}

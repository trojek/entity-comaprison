package cc.rojek.ec.algorithm1;

import java.io.File;
import java.sql.Timestamp;

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
	private static String ONTOLOGY_URL = "";
	
	public JoinOntology(String ONTOLOGY_URL){
		JoinOntology.ONTOLOGY_URL = ONTOLOGY_URL;
	}
	
	public void createIndividualNodeConnectedToClass(String nodeName,
			String className) throws OWLOntologyCreationException {

		File sourceFile = new File(ONTOLOGY_URL);
		File targetFile = new File(renameFileUrl(ONTOLOGY_URL));

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(sourceFile);
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI();
		OWLDataFactory factory = manager.getOWLDataFactory();

		OWLClass owlClass = factory.getOWLClass(IRI.create(ontologyIRI + "#"
				+ className));
		OWLNamedIndividual owlIndividual = factory.getOWLNamedIndividual(IRI
				.create(ontologyIRI + "#" + nodeName));

		OWLAxiom axiom = factory.getOWLClassAssertionAxiom(owlClass,
				owlIndividual);
		AddAxiom addAxiom = new AddAxiom(ontology, axiom);

		if (ontology.containsClassInSignature(IRI.create(ontologyIRI + "#"
				+ className))) {
			manager.applyChange(addAxiom);
		} else {
			System.out.println("There is no class " + className + " in the ontology");
			return;
		}
		try {
			manager.saveOntology(ontology, IRI.create(targetFile.toURI()));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String renameFileUrl(String fileUrl) {
		fileUrl = fileUrl.replace(".owl", "_" + System.currentTimeMillis()
				+ ".owl");
		return fileUrl;
	}
}

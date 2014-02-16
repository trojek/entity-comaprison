package cc.rojek.ec.algorithm1;

import java.io.File;
import java.util.ArrayList;

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

import cc.rojek.ec.application_domain_model.ObjectModel;

/**
 * Join OWL file with Neo4j graph database
 */
public class JoinOntology {

	String ONTOLOGY_URL = " ";
	static OWLOntologyManager manager;
	static OWLOntology ontology;
	static IRI ontologyIRI;

	public JoinOntology(String ONTOLOGY_URL)
			throws OWLOntologyCreationException {
		this.ONTOLOGY_URL = ONTOLOGY_URL;
		File sourceFile = new File(ONTOLOGY_URL);
		manager = OWLManager.createOWLOntologyManager();
		ontology = manager.loadOntologyFromOntologyDocument(sourceFile);
		ontologyIRI = ontology.getOntologyID().getOntologyIRI();

	}

	public void createIndividualNodeConnectedToClass(
			ArrayList<ObjectModel> listOfObjects)
			throws OWLOntologyCreationException {

		File targetFile = new File(renameFileUrl(ONTOLOGY_URL));

		for (ObjectModel list : listOfObjects) {
			for (String joinPoint : list.connectionsList) {
				setOWLElements(joinPoint, list.nodeName);
			}
		}

		try {
			manager.saveOntology(ontology, IRI.create(targetFile.toURI()));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

	}

	private static void setOWLElements(String className, String nodeName) {

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
			System.out.println("There is no class " + className
					+ " in the ontology");

		}

	}

	private static String renameFileUrl(String fileUrl) {
		fileUrl = fileUrl.replace(".owl", "_" + System.currentTimeMillis()
				+ ".owl");
		return fileUrl;
	}
}

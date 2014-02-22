package cc.rojek.ec.algorithm1;

import java.io.File;
import java.util.List;

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

import cc.rojek.ec.application_domain_model.Object;

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

	public String moveJoinPointsToOWLFile(
			List<Object> mainList)
			throws OWLOntologyCreationException {

		for (Object list : mainList) {
			for (String joinPoint : list.getConnectionsList()) {
				setOWLElement(joinPoint, list.getId());
			}
		}

		String fileName = renameFileUrl(ONTOLOGY_URL);
		File targetFile = new File(fileName);
		
		try {
			manager.saveOntology(ontology, IRI.create(targetFile.toURI()));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}

		return fileName;
	}

	private static void setOWLElement(String className, String nodeName) {

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

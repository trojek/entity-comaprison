package cc.rojek.ec;

import java.util.Map;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.UniqueFactory;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

/**
 * Map OWL file to Neo4j graph database
 */
public class Neo4jAndOWL {

	static GraphDatabaseService db;
	static OWLOntology ontology;
	static OWLReasoner reasoner;

	Neo4jAndOWL(GraphDatabaseService db, OWLOntology ontology) {
		Neo4jAndOWL.db = db;
		Neo4jAndOWL.ontology = ontology;
		Neo4jAndOWL.reasoner = new Reasoner(ontology);
	}

	// import ontology from OWL file into Neo4j graph database
	void importOntology() throws Exception {

		// Check if ontology is consistent!
		if (!reasoner.isConsistent()) {
			// Throw your exception of choice here
			throw new Exception("Ontology is inconsistent");
		}

		// begin transaction
		moveData();
	}

	private static void moveData() {
		try (Transaction tx = db.beginTx()) {
			System.out.println("Transaction begin.");

			// Create root node of the graph
			Node thingNode = getOrCreateNodeWithUniqueFactory("owl:Thing", db);
			thingNode.addLabel(Labels.Root);

			// System.out.println(thingNode.getProperty( "name" ).toString());

			// Get all the classes defined in the ontology and add them to the
			// graph.
			for (OWLClass c : ontology.getClassesInSignature(true)) {
				String classString = c.toString();
				if (classString.contains("#")) {
					classString = getReadableName(classString);
				}
				Node classNode = getOrCreateNodeWithUniqueFactory(classString,
						db);
				classNode.addLabel(Labels.Class);

				/*
				 * Find out if they have any super classes. If they do, link
				 * them. If they don't, link back to owl:Thing. Make sure only
				 * to link to the direct super classes!
				 */
				NodeSet<OWLClass> superclasses = reasoner.getSuperClasses(c,
						true);

				if (superclasses.isEmpty()) {
					classNode.createRelationshipTo(thingNode,
							DynamicRelationshipType.withName("subClassOf"));
				} else {
					for (org.semanticweb.owlapi.reasoner.Node<OWLClass> parentOWLNode : superclasses) {

						OWLClassExpression parent = parentOWLNode
								.getRepresentativeElement();
						String parentString = parent.toString();

						if (parentString.contains("#")) {
							parentString = getReadableName(parentString);
						}
						Node parentNode = getOrCreateNodeWithUniqueFactory(
								parentString, db);
						classNode.createRelationshipTo(parentNode,
								DynamicRelationshipType.withName("subClassOf"));
					}
				}

				// Get all the individuals for each class. Create nodes and link
				// them back to their parent class.
				for (org.semanticweb.owlapi.reasoner.Node<OWLNamedIndividual> in : reasoner
						.getInstances(c, true)) {
					OWLNamedIndividual i = in.getRepresentativeElement();
					String indString = i.toString();
					if (indString.contains("#")) {
						indString = getReadableName(indString);
					}
					Node individualNode = getOrCreateNodeWithUniqueFactory(
							indString, db);
					individualNode.addLabel(Labels.Individual);

					individualNode.createRelationshipTo(classNode,
							DynamicRelationshipType.withName("instanceOf"));

					/*
					 * For each individual, get all object properties and all
					 * data properties. Add them to the graph as node properties
					 * or relationships.
					 */

					for (OWLObjectPropertyExpression objectProperty : ontology
							.getObjectPropertiesInSignature()) {

						for (org.semanticweb.owlapi.reasoner.Node<OWLNamedIndividual> object : reasoner
								.getObjectPropertyValues(i, objectProperty)) {
							String reltype = objectProperty.toString();
							reltype = getReadableName(reltype);

							String s = object.getRepresentativeElement()
									.toString();
							s = getReadableName(s);
							Node objectNode = getOrCreateNodeWithUniqueFactory(
									s, db);
							individualNode.createRelationshipTo(objectNode,
									DynamicRelationshipType.withName(reltype));
						}
					}

					for (OWLDataPropertyExpression dataProperty : ontology
							.getDataPropertiesInSignature()) {

						for (OWLLiteral object : reasoner
								.getDataPropertyValues(i,
										dataProperty.asOWLDataProperty())) {
							String reltype = dataProperty.asOWLDataProperty()
									.toString();
							reltype = getReadableName(reltype);

							String s = object.toString();
							individualNode.setProperty(reltype, s);
						}
					}
				}
			}

			tx.success();
			System.out.println("Transaction finished.");

		}
	}

	// Create or get node using UniqueFactory (whatever it is?)
	private static Node getOrCreateNodeWithUniqueFactory(String nodeName,
			GraphDatabaseService graphDb) {
		UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory(
				graphDb, "index") {
			@Override
			protected void initialize(Node created,
					Map<String, Object> properties) {
				created.setProperty("name", properties.get("name"));
			}
		};

		return factory.getOrCreate("name", nodeName);
	}

	// extract human readable part of string
	static String getReadableName(String fullName) {
		return fullName.substring(fullName.indexOf("#") + 1,
				fullName.lastIndexOf(">"));
	}

	/*
	 * Actually I have no need to use this method It was replaced by enum labels
	 * 
	 * private static void DynamicSetLabelOnNode(Node node, String label){ Label
	 * myLabel = DynamicLabel.label(label); node.addLabel(myLabel); }
	 */

	private enum Labels implements Label {
		Root, Class, Individual
	}

}

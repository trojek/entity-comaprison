package cc.rojek.ec.mapdata;

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
		reasoner = new Reasoner(ontology);
	}

	// import ontology from OWL file into Neo4j graph database
	public void importOntology() throws Exception {

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
			Node thingNode = createRootNode();

			// get and set all classes in owl file into neo4j db
			getClassesAndIndividualsAndConnectThem(thingNode);

			tx.success();
			System.out.println("Transaction finished.");

		}
	}

	private static Node createRootNode(){
		Node thingNode = getOrCreateNodeWithUniqueFactory("owl:Thing", db);
		thingNode.addLabel(Labels.Root);
		return thingNode;
	}
	
	private static void getClassesAndIndividualsAndConnectThem(Node thingNode) {
		for (OWLClass c : ontology.getClassesInSignature(true)) {
			String classString = c.toString();
			if (classString.contains("#")) {
				classString = getReadableName(classString);
			}
			Node classNode = getOrCreateNodeWithUniqueFactory(classString, db);
			classNode.addLabel(Labels.Class);

			// Make relation to super classes (or thingNode)
			findOutSuperClasses(classNode, thingNode, c);

			// Get all the individuals for each class. Create nodes and link
			// them back to their parent class.
			getAllIndividuals(classNode, c);
		}
	}

	private static void findOutSuperClasses(Node classNode, Node thingNode,
			OWLClass c) {
		NodeSet<OWLClass> superclasses = reasoner.getSuperClasses(c, true);

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
	}

	private static void getAllIndividuals(Node classNode, OWLClass c) {
		for (org.semanticweb.owlapi.reasoner.Node<OWLNamedIndividual> in : reasoner
				.getInstances(c, true)) {
			OWLNamedIndividual i = in.getRepresentativeElement();
			String indString = i.toString();
			if (indString.contains("#")) {
				indString = getReadableName(indString);
			}
			Node individualNode = getOrCreateNodeWithUniqueFactory(indString,
					db);
			individualNode.addLabel(Labels.Individual);

			individualNode.createRelationshipTo(classNode,
					DynamicRelationshipType.withName("instanceOf"));

			// get object property and set them as the relationship
			setObjectProperty(individualNode, i);

			// get data property and set them as a node property
			setDataProperty(individualNode, i);
		}
	}

	private static void setObjectProperty(Node individualNode,
			OWLNamedIndividual i) {
		for (OWLObjectPropertyExpression objectProperty : ontology
				.getObjectPropertiesInSignature()) {

			for (org.semanticweb.owlapi.reasoner.Node<OWLNamedIndividual> object : reasoner
					.getObjectPropertyValues(i, objectProperty)) {
				String reltype = objectProperty.toString();
				reltype = getReadableName(reltype);

				String s = object.getRepresentativeElement().toString();
				s = getReadableName(s);
				Node objectNode = getOrCreateNodeWithUniqueFactory(s, db);
				individualNode.createRelationshipTo(objectNode,
						DynamicRelationshipType.withName(reltype));
			}
		}
	}

	private static void setDataProperty(Node individualNode,
			OWLNamedIndividual i) {
		for (OWLDataPropertyExpression dataProperty : ontology
				.getDataPropertiesInSignature()) {

			for (OWLLiteral object : reasoner.getDataPropertyValues(i,
					dataProperty.asOWLDataProperty())) {
				String reltype = dataProperty.asOWLDataProperty().toString();
				reltype = getReadableName(reltype);

				String s = object.toString();
				individualNode.setProperty(reltype, s);
			}
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
	public static String getReadableName(String fullName) {
		return fullName.substring(fullName.indexOf("#") + 1,
				fullName.lastIndexOf(">"));
	}

	private enum Labels implements Label {
		Root, Class, Individual
	}

}

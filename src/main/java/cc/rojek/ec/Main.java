package cc.rojek.ec;

import java.util.ArrayList;

import org.neo4j.graphdb.GraphDatabaseService;
import org.semanticweb.owlapi.model.OWLOntology;

import cc.rojek.ec.algorithm1.JoinOntology;
import cc.rojek.ec.algorithm2.DataSet;
import cc.rojek.ec.application_domain_model.ApplicationDomainModel;
import cc.rojek.ec.application_domain_model.ObjectModel;
import cc.rojek.ec.helpers.Neo4jHelper;
import cc.rojek.ec.helpers.OntologyHelper;
import cc.rojek.ec.mapdata.Neo4jAndOWL;
//import cc.rojek.ec.algorithm1.JoinOntology;

public class Main {

	private static final String DB_PATH = "/home/tomasz/Programy/neo4j-community-2.0.0/data/sample.db";
	private static final String ONTOLOGY_URL = "data/olympic_games_pure.owl";

	public static void main(String[] args) throws Exception {

		//String json = "{'object' : 'object1', 'name' : 'Turin', connections: { 'Location' : 'Italy', 'Season' : 'Summer' } } }";
		ApplicationDomainModel adm = new ApplicationDomainModel("olympic");
		//adm.addData(json, "objects");
		
		ArrayList<ObjectModel> mainList = adm.getListOfObjectAndConnectedNodes("objects");
		JoinOntology jOnto = new JoinOntology(ONTOLOGY_URL);
		String newOntologyURL = jOnto.moveJoinPointsToOWLFile(mainList);
		
		OWLOntology ontology = OntologyHelper.loadOntologyFromFile(newOntologyURL);
		GraphDatabaseService db = Neo4jHelper.startNeo4jDB(DB_PATH);

		Neo4jAndOWL exampleon = new Neo4jAndOWL(db, ontology);
		exampleon.importOntology();
		
		DataSet compareResult = new DataSet(db);
		compareResult.compareObjectsWith(30l);
	}
}

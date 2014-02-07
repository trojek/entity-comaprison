package cc.rojek.ec;

import java.util.ArrayList;
import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

public class CompareObject {

	static ExecutionEngine engine;
	static GraphDatabaseService db;
	static ArrayList<Pathway> listOfPathways = new ArrayList<Pathway>(); 


	CompareObject(GraphDatabaseService db) {
		CompareObject.db = db;
		engine = new ExecutionEngine(db);
	}

	public static void compareObjects(int object_id) {
		
		
		try (Transaction takeNodesID = db.beginTx()) {
			String query = "MATCH (individuals:Individual) RETURN individuals";

			ExecutionResult id = engine.execute(query);

			Iterator<Node> n_column = id.columnAs("individuals");
			for (Node node : IteratorUtil.asIterable(n_column)) {
				long node_id = node.getId();
				getAllPathBetweenRootAndNode(node_id);
			}

			takeNodesID.success();
		}
		
		//System.out.println(listOfPathways.get(0).toString());
		
	}

	private static void getAllPathBetweenRootAndNode(long individualNode) {

		try (Transaction getAllPath = db.beginTx()) {
			
			String query = "START indv=node(" + individualNode
					+ "), root=node(0) MATCH allPaths=root<-[*]-indv RETURN allPaths";
			ExecutionResult result = engine.execute(query);

			Iterator<Path> allPaths_column = result.columnAs("allPaths");
			for (Path path : IteratorUtil.asIterable(allPaths_column)) {
				Pathway pw = new Pathway(individualNode);
				// System.out.println("Path with id: " + individualNode + " has been created");
				Iterable<Node> nodeResult = path.nodes();
				int pl = path.length();
				int counter = 0;
				for (Node node : nodeResult) {
					if(counter==1){
						pw.setGroupId(node.getId());
					} else if (counter > 1 && counter < pl) {
						pw.add(node.getId());
					}
					// System.out.print(" -> " + node.getId());
					counter++;
				}
				listOfPathways.add(pw); 

			}
			getAllPath.success();
		}
	}
}
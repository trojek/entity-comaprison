package cc.rojek.ec;

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
				System.out.println(node_id);
				getAllPathBetweenRootAndNode(node_id);
			}

			takeNodesID.success();
		}
	}

	private static void getAllPathBetweenRootAndNode(long inviduvualNode) {

		try (Transaction getAllPath = db.beginTx()) {
			
			String query = "START a=node(" + inviduvualNode
					+ "), d=node(0) MATCH allPaths=a-[*]->d RETURN allPaths";
			ExecutionResult result = engine.execute(query);

			Iterator<Path> allPaths_column = result.columnAs("allPaths");
			for (Path path : IteratorUtil.asIterable(allPaths_column)) {
				Iterable<Node> nodeResult = path.nodes();
				for (Node node : nodeResult) {
					if (inviduvualNode == node.getId()){
						System.out.println("   ");
					} else {
						System.out.println("-- " + node.getId());
					}
				}
			}
			getAllPath.success();
		}
	}
}
package cc.rojek.ec;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

public class CompareObject {

	static long nodeResult;
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
				nodeResult = node.getId();
				System.out.println(nodeResult);
				getAllPathBetweenRootAndNode(nodeResult);
			}

			takeNodesID.success();
		}

	}

	private static void getAllPathBetweenRootAndNode(long nodeResult2) {

		try (Transaction getAllPath = db.beginTx()) {
			String query = "START a=node(" + nodeResult2
					+ "), d=node(0) MATCH allPaths=a-[*]->d RETURN allPaths";
			ExecutionResult result = engine.execute(query);

			for (Map<String, Object> map : result) {

				for (Entry<String, Object> entry : map.entrySet()) {
					System.out.println(entry.getKey() + "/     " + entry.getValue());
				}

				;
			}

			getAllPath.success();
		}
	}
}
package cc.rojek.ec;

import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

public class CompareObject {

	static long nodeResult;

	public static void compareObjects(GraphDatabaseService db) {

		ExecutionEngine engine = new ExecutionEngine(db);

		ExecutionResult id;

		try (Transaction takeNodesID = db.beginTx()) {
			id = engine.execute("MATCH (n:Individual) RETURN n");

			Iterator<Node> n_column = id.columnAs("n");
			for (Node node : IteratorUtil.asIterable(n_column)) {
				nodeResult = node.getId();
				//getAllPathBetweenRootAndNode(engine, db, nodeResult);
			}

			takeNodesID.success();
		}

	}

	/*private static void getAllPathBetweenRootAndNode(ExecutionEngine engine,
			GraphDatabaseService db, long nodeResult2) {

		ExecutionResult result;

		try (Transaction getAllPath = db.beginTx()) {
			result = engine.execute("START a=node(" + nodeResult2
					+ "), d=node(0) MATCH p=a-[*]->d RETURN p");

			Iterator<Node> n_column = result.columnAs("p");
			for (Node node : IteratorUtil.asIterable(n_column)) {
				nodeResult = node.getId();
				System.out.println(nodeResult);
			}

			getAllPath.success();
		}
	}*/
}
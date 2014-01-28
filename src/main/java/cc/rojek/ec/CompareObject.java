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

		ExecutionResult result;
		
		try (Transaction takeNodesID = db.beginTx()) {
			result = engine
					.execute("MATCH (n:Individual) RETURN n");

			Iterator<Node> n_column = result.columnAs("n");
			for (Node node : IteratorUtil.asIterable(n_column)) {
				nodeResult = node.getId();
				System.out.println(nodeResult);
			}
			

		}
	}
}
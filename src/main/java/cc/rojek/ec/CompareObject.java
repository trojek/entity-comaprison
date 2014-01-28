package cc.rojek.ec;

import java.util.Iterator;


// I'm not sure if here should be imported libraries from org.neo4j.cypher.javacompat.
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

public class CompareObject {

	static String nodeResult;
	
	public static void compareObjects(GraphDatabaseService db) {
		// START SNIPPET: execute
		ExecutionEngine engine = new ExecutionEngine(db);

		ExecutionResult result;
		try (Transaction ignored = db.beginTx()) {
			result = engine
					.execute("MATCH (n) RETURN n");
			// END SNIPPET: execute
			// START SNIPPET: items
			Iterator<Node> n_column = result.columnAs("n");
			for (Node node : IteratorUtil.asIterable(n_column)) {
				// note: we're grabbing the name property from the node,
				// not from the n.name in this case.
				nodeResult = node + ": " + node.getProperty("name");
				System.out.println(nodeResult);
			}
			// END SNIPPET: items

		}
	}
}
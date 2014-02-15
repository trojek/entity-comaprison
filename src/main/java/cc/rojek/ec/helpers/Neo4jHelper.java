package cc.rojek.ec.helpers;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4jHelper {

	public static GraphDatabaseService startNeo4jDB(String dbPath) {
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(dbPath);
		registerShutdownHook(db);
		return db;
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
				System.out.println("Neo4j shutdown (shutdown hook)");
			}
		});
	}
}

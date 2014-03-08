package cc.rojek.ec.helpers;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neo4jHelper {

    private final static Logger logger = LoggerFactory.getLogger(Neo4jHelper.class);

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
                logger.info("Neo4j shutdown (shutdown hook); db: " + graphDb.toString());
            }
        });
    }
}

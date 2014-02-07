/**
 * 
 */
package cc.rojek.ec.mapdata;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.test.TestGraphDatabaseFactory;

import cc.rojek.ec.mapdata.Neo4jAndOWL;

/**
 * @author tomasz
 *
 */
public class Neo4jAndOWLTest {

	protected GraphDatabaseService graphDb;
	/**
	 * Test method for {@link cc.rojek.ec.mapdata.Neo4jAndOWL#getReadableName(java.lang.String)}.
	 */
	@Test
	public final void testGetReadableName() {
		String tested_phrase= "<http://www.co-ode.org/ontologies/pizza/pizza.owl#Mushroom>";
		String result = Neo4jAndOWL.getReadableName(tested_phrase);
		assertEquals( "Mushroom", result ); 
				
	}
	
    @Before
    public void prepareTestDatabase()
    {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
    }

    @After
    public void destroyTestDatabase()
    {
        graphDb.shutdown();
    }

    @Test
    public void startWithConfiguration()
    {
         GraphDatabaseService db = new TestGraphDatabaseFactory()
            .newImpermanentDatabaseBuilder()
            .setConfig( GraphDatabaseSettings.nodestore_mapped_memory_size, "10M" )
            .setConfig( GraphDatabaseSettings.string_block_size, "60" )
            .setConfig( GraphDatabaseSettings.array_block_size, "300" )
            .newGraphDatabase();
         db.shutdown();
    }

    @Test
    public void shouldCreateNode()
    {
        // START SNIPPET: unitTest
        Node n = null;
        try ( Transaction tx = graphDb.beginTx() )
        {
            n = graphDb.createNode();
            n.setProperty( "name", "Nancy" );
            tx.success();
        }

        // The node should have a valid id
        assertThat( n.getId(), is( greaterThan( -1L ) ) );

        // Retrieve a node by using the id of the created node. The id's and
        // property should match.
        try ( Transaction tx = graphDb.beginTx() )
        {
            Node foundNode = graphDb.getNodeById( n.getId() );
            assertThat( foundNode.getId(), is( n.getId() ) );
            assertThat( (String) foundNode.getProperty( "name" ), is( "Nancy" ) );
        }
        // END SNIPPET: unitTest
    }

}

/**
 * 
 */
package cc.rojek.ec;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author tomasz
 *
 */
public class MapOWLtoNeo4jTest {

	/**
	 * Test method for {@link cc.rojek.ec.MapOWLtoNeo4j#getReadableName(java.lang.String)}.
	 */
	@Test
	public final void testGetReadableName() {
		String tested_phrase= "<http://www.co-ode.org/ontologies/pizza/pizza.owl#Mushroom>";
		String result = MapOWLtoNeo4j.getReadableName(tested_phrase);
		assertEquals( "Mushroom", result ); 
				
	}

}

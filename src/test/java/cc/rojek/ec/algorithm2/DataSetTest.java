package cc.rojek.ec.algorithm2;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class DataSetTest {
	
	static ArrayList<Pathway> listOfPathways = new ArrayList<Pathway>();
	
	@Before
	public void setUp(){
			
		
	}

	@Test
	public final void testGetAllUniqeGroup() {
		ArrayList<Long> uniqeGroupList = new ArrayList<Long>();
		uniqeGroupList.add(13l);
		uniqeGroupList.add(6l);
		uniqeGroupList.add(10l);
		
		Pathway path0 = new Pathway(0l);
		path0.setGroupId(13l);
		Pathway path1 = new Pathway(1l);
		path1.setGroupId(6l);
		Pathway path2 = new Pathway(2l);
		path2.setGroupId(10l);
		Pathway path3 = new Pathway(3l);
		path3.setGroupId(10l);
		
		listOfPathways.add(path0);
		listOfPathways.add(path1);
		listOfPathways.add(path2);
		listOfPathways.add(path3);
		
		DataSet.listOfPathways = listOfPathways;
		
		assertEquals(uniqeGroupList, DataSet.getAllUniqeGroup());
	}

}

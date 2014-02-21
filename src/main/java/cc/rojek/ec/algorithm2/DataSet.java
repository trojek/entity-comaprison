package cc.rojek.ec.algorithm2;

import java.util.ArrayList;
import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

public class DataSet {

	static ExecutionEngine engine;
	static GraphDatabaseService db;
	public static ArrayList<Pathway> listOfPathways = new ArrayList<Pathway>();

	public DataSet(GraphDatabaseService db) {
		DataSet.db = db;
		engine = new ExecutionEngine(db);
	}

	public void compareObjectsWith(Long cObjectId) {

		ArrayList<Long> listOfObjectId = getAllIndividualNodes();

		setAllPaths(listOfObjectId);

		ArrayList<Long> listOfGroups = getAllUniqeGroup();

		ArrayList<ArrayList<Integer>> listOfCompare = makeCompareList(
				listOfObjectId, listOfGroups, cObjectId);

		printCompareList(listOfObjectId, listOfCompare);
	}

	private static void setAllPaths(ArrayList<Long> ids) {
		for (Long objId : ids) {
			setAllPathBetweenRootNodeAndIndividualNode(objId);
		}
	}

	private static ArrayList<Long> getAllIndividualNodes() {
		ArrayList<Long> objectIds = new ArrayList<Long>();

		try (Transaction takeNodesID = db.beginTx()) {
			String query = "MATCH (individuals:Individual) RETURN individuals";

			ExecutionResult id = engine.execute(query);

			Iterator<Node> n_column = id.columnAs("individuals");
			for (Node node : IteratorUtil.asIterable(n_column)) {
				objectIds.add(node.getId());
			}
			takeNodesID.success();
		}
		return objectIds;
	}

	private static void setAllPathBetweenRootNodeAndIndividualNode(
			long individualNode) {
		try (Transaction getAllPath = db.beginTx()) {
			String query = "START indv=node("
					+ individualNode
					+ "), root=node(0) MATCH allPaths=root<-[*]-indv RETURN allPaths";
			ExecutionResult result = engine.execute(query);

			Iterator<Path> allPaths_column = result.columnAs("allPaths");

			iterateThroughPaths(allPaths_column, individualNode);

			getAllPath.success();
		}
	}

	private static void iterateThroughPaths(Iterator<Path> allPaths_column,
			long individualNode) {
		for (Path pathTrace : IteratorUtil.asIterable(allPaths_column)) {
			int counter = 0;
			Pathway pw = new Pathway(individualNode);
			Iterable<Node> nodeResult = pathTrace.nodes();
			int pathLength = pathTrace.length();
			// nodes iterate
			for (Node node : nodeResult) {
				if (counter == 1) {
					pw.setGroupId(node.getId());
				} else if (counter > 1 && counter < pathLength) {
					pw.add(node.getId());
				}
				counter++;
			}
			listOfPathways.add(pw);
		}
	}

	private static ArrayList<ArrayList<Integer>> makeCompareList(
			ArrayList<Long> listOfObjectId, ArrayList<Long> listOfGroups,
			Long cObjectId) {
		ArrayList<ArrayList<Integer>> listOfCompare = new ArrayList<ArrayList<Integer>>();
		for (Long group : listOfGroups) {
			ArrayList<Integer> listForGroup = new ArrayList<Integer>();
			Pathway base = findPathwayWhere(cObjectId, group);
			for (Long objectId : listOfObjectId) {
				listForGroup.add(countPoints(base,
						findPathwayWhere(objectId, group)));
			}
			listOfCompare.add(listForGroup);
		}
		return listOfCompare;
	}

	static ArrayList<Long> getAllUniqeGroup() {
		ArrayList<Long> uniqeGroupList = new ArrayList<Long>();
		for (Pathway path : listOfPathways) {
			if (!uniqeGroupList.contains(path.groupId)) {
				uniqeGroupList.add(path.groupId);
			}
		}
		return uniqeGroupList;
	}

	private static Pathway findPathwayWhere(long objectId, long groupId) {
		Pathway pathway = null;
		for (Pathway pw : listOfPathways) {
			if (pw.groupId == groupId && pw.individualId == objectId) {
				pathway = pw;
				break;
			}
		}
		return pathway;
	}

	private static int countPoints(Pathway base, Pathway extra) {
		int counter = 0;
		for (int i = 0; i < base.path.size(); i++) {
			if (base.path.get(i) == extra.path.get(i)) {
				counter++;
			}
		}
		return counter;
	}

	private void printCompareList(ArrayList<Long> listOfObjectId,
			ArrayList<ArrayList<Integer>> levelList) {
		System.out.println("First row are IDs");
		for (Long id : listOfObjectId) {
			System.out.print(id + " ");
		}

		System.out.println();
		System.out.println();

		for (ArrayList<Integer> level0 : levelList) {
			for (int level1 : level0) {
				System.out.print(level1 + " ");
			}
			System.out.println();
		}
	}
}
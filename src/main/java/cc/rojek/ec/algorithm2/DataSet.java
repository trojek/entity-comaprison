package cc.rojek.ec.algorithm2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.rojek.ec.application_domain_model.ApplicationDomainModel;

public class DataSet {

    static ExecutionEngine engine;
    static GraphDatabaseService db;
    public static ArrayList<Pathway> listOfPathways = new ArrayList<Pathway>();

    private final static Logger logger = LoggerFactory.getLogger(DataSet.class);

    public DataSet(GraphDatabaseService db) {
        DataSet.db = db;
        engine = new ExecutionEngine(db);
    }

    public void compareObjectsWith(Long cObjectId) {

        ArrayList<Long> listOfObjectId = getAllIndividualNodes();

        ApplicationDomainModel adm = new ApplicationDomainModel();
        adm.comparisonCriteria.add(2l); // Season
        adm.comparisonCriteria.add(19l); // Location / pizzaTopping

        ArrayList<Long> listOfGroups = adm.comparisonCriteria;

        setAllPaths(listOfObjectId, listOfGroups);

        ArrayList<ArrayList<Integer>> listOfCompare = makeCompareList(listOfObjectId, listOfGroups,
                cObjectId);

        printCompareList(listOfObjectId, listOfCompare);
    }

    private static void setAllPaths(ArrayList<Long> ids, ArrayList<Long> groups) {
        for (Long objId : ids) {
            for (Long groupId : groups) {
                setAllPathBetweenGroupNodeAndIndividualNode(objId, groupId);
            }
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

    private static void setAllPathBetweenGroupNodeAndIndividualNode(long individualNode,
            long groupNode) {
        try (Transaction getAllPath = db.beginTx()) {
            String query = "START indv=node(" + individualNode + "), root=node(" + groupNode
                    + ") MATCH allPaths=root<-[*]-indv RETURN allPaths";
            ExecutionResult result = engine.execute(query);

            Iterator<Path> allPaths_column = result.columnAs("allPaths");

            iterateThroughPaths(allPaths_column, individualNode);

            getAllPath.success();
        }
    }

    private static void iterateThroughPaths(Iterator<Path> allPaths_column, long individualNode) {
        for (Path pathTrace : IteratorUtil.asIterable(allPaths_column)) {
            int counter = 0;
            Pathway pw = new Pathway(individualNode);
            Iterable<Node> nodeResult = pathTrace.nodes();
            int pathLength = pathTrace.length();
            // nodes iterate
            for (Node node : nodeResult) {
                if (counter == 0) {
                    pw.setGroupId(node.getId());
                } else if (counter > 0 && counter < pathLength) {
                    pw.add(node.getId());
                }
                counter++;
            }
            listOfPathways.add(pw);
        }
    }

    private static ArrayList<ArrayList<Integer>> makeCompareList(ArrayList<Long> listOfObjectId,
            ArrayList<Long> listOfGroups, Long cObjectId) {
        ArrayList<ArrayList<Integer>> listOfCompare = new ArrayList<ArrayList<Integer>>();
        for (Long group : listOfGroups) {

            ArrayList<Integer> listForGroup = new ArrayList<Integer>();
            ArrayList<Pathway> basePaths = findPathwaysListWhere(cObjectId, group);

            for (Long objectId : listOfObjectId) {
                ArrayList<Pathway> extraPaths = findPathwaysListWhere(objectId, group);
                int sum = 0;
                for (Pathway extraPath : extraPaths) {
                    ArrayList<Integer> sums = new ArrayList<Integer>();
                    for (Pathway basePath : basePaths) {
                        // System.out.print("s: "+ countPoints(basePath,
                        // extraPath) + " ");
                        sums.add(countPoints(basePath, extraPath));
                    }
                    sum += Collections.max(sums);
                    System.out.println("");
                }
                System.out.println("");
                listForGroup.add(sum);
            }
            listOfCompare.add(listForGroup);
        }
        return listOfCompare;
    }

    private static ArrayList<Pathway> findPathwaysListWhere(long objectId, long groupId) {
        ArrayList<Pathway> listOfPathwaysWhere = new ArrayList<Pathway>();
        for (Pathway pw : listOfPathways) {
            if (pw.groupId == groupId && pw.individualId == objectId) {
                listOfPathwaysWhere.add(pw);
            }
        }
        return listOfPathwaysWhere;
    }

    private static int countPoints(Pathway base, Pathway extra) {
        int counter = 0;
        for (int i = 0; i < base.path.size(); i++) {
            if (i < extra.path.size()) {
                if (base.path.get(i) == extra.path.get(i)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private void printCompareList(ArrayList<Long> listOfObjectId,
            ArrayList<ArrayList<Integer>> levelList) {
        System.out.println("IDs");
        for (Long id : listOfObjectId) {
            System.out.print(id + "  ");
        }
        System.out.println();
        System.out.println("------------------------------------");

        for (ArrayList<Integer> level0 : levelList) {
            // int max = Collections.max(level0);
            for (int level1 : level0) {
                // float restul = (float)level1/max;
                // System.out.printf("%.2f", restul);
                // System.out.print("  ");
                System.out.print(level1 + "   ");
            }
            System.out.println();
        }
    }
}
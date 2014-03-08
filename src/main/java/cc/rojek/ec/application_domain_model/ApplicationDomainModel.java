package cc.rojek.ec.application_domain_model;

import java.util.ArrayList;

public class ApplicationDomainModel {

    public ArrayList<Long> comparisonCriteria = new ArrayList<Long>();

    /*
     * Method buildModel should give us proposals comparison criteria During
     * building model it is important to do not add node to comparisonCriteria
     * which is a child/parent (or deeper) of current comparisonCriteria node.
     */
    public void buildModel() {
    }

    public void checkIfObjectsAreAddedCorrect() {
    }
}

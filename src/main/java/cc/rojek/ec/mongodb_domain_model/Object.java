package cc.rojek.ec.mongodb_domain_model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "objects")
public class Object {

    @Id
    private String id;

    private String nodeName = "";
    private ArrayList<ArrayList<String>> information = new ArrayList<ArrayList<String>>();

    public Object(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getId() {
        return id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public ArrayList<ArrayList<String>> getInformation() {
        return information;
    }

    public void setInformation(ArrayList<ArrayList<String>> information) {
        this.information = information;
    }

}

package cc.rojek.ec.application_domain_model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "objects")
public class Object {

	@Id
    private String id;
	
	private String nodeName = "";
	private ArrayList<String> connectionsList = new ArrayList<String>();

	public Object(String nodeName, ArrayList<String> connectionsList) {
		this.nodeName = nodeName;
		this.connectionsList = connectionsList;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public ArrayList<String> getConnectionsList() {
		return connectionsList;
	}

	public void setConnectionsList(ArrayList<String> connectionsList) {
		this.connectionsList = connectionsList;
	}
}

package cc.rojek.ec.application_domain_model;

import java.util.ArrayList;

public class ObjectModel {

	public String nodeName = "";
	public ArrayList<String> connectionsList = new ArrayList<String>();

	ObjectModel(String name) {
		this.nodeName = name;
	}
}

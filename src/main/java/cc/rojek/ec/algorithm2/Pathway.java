package cc.rojek.ec.algorithm2;

import java.util.ArrayList;

public class Pathway {

	public long individualId;
	public long groupId;
	public ArrayList<Long> path = new ArrayList<Long>();
	
	Pathway(long individualId){
		this.individualId = individualId;
	}
	
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	
	public void add(long l) {
		path.add(l);
	}
}

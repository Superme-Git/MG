package fire.pb.http.beans;

import java.util.ArrayList;

public class BagBean {
	private int bagtype;
	private java.util.ArrayList<BagItem> bagitems=new ArrayList<BagItem>();
	public int getBagtype() {
		return bagtype;
	}
	public void setBagtype(int bagtype) {
		this.bagtype = bagtype;
	}
	public java.util.ArrayList<BagItem> getBagitems() {
		return bagitems;
	}
	public void setBagitems(java.util.ArrayList<BagItem> bagitems) {
		this.bagitems = bagitems;
	}
	
	
	
	

}


package fire.pb.activity.award;

public class Items {
	private int itemID;
	private int itemNum;
	private int prop;
	private int propValue;

	public Items(int itemID, int itemNum, int prop, int propValue) {

		super();
		this.itemID = itemID;
		this.itemNum = itemNum;
		this.prop = prop;
		this.propValue = propValue;
	}

	
	public int getItemID() {
	
		return itemID;
	}

	
	public void setItemID(int itemID) {
	
		this.itemID = itemID;
	}

	
	public int getItemNum() {
	
		return itemNum;
	}

	
	public void setItemNum(int itemNum) {
	
		this.itemNum = itemNum;
	}

	
	public int getProp() {
	
		return prop;
	}

	
	public void setProp(int prop) {
	
		this.prop = prop;
	}

	
	public int getPropValue() {
	
		return propValue;
	}

	
	public void setPropValue(int propValue) {
	
		this.propValue = propValue;
	}
	
}


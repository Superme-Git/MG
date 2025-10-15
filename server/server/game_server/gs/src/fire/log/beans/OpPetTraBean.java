package fire.log.beans;


public class OpPetTraBean {
	private int ItemId;				// 培养所用的物品ID
	private int ItemNum;			// 培养所用的物品个数
	private int PropId;				// 使用在哪项属性上（使用属性id—）参照AttrType
	public OpPetTraBean(int itemId, int itemNum, int propId) {
		ItemId = itemId;
		ItemNum = itemNum;
		PropId = propId;
	}
	
	public int getItemId() {
		return ItemId;
	}
	public int getItemNum() {
		return ItemNum;
	}
	public int getPropId() {
		return PropId;
	}
	
}

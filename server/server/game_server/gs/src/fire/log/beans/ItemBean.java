package fire.log.beans;

public class ItemBean {
	private int ItemId;//道具ID
	private int amt;//道具数量
	private int aftAmt;//道具剩余量
	
	public ItemBean() {
	}
	
	public ItemBean(int ItemId,int amt,int aftAmt){
		this.ItemId=ItemId;//道具ID
		this.amt=amt;//道具数量
		this.aftAmt=aftAmt;//道具剩余量
	}
	
	public int getItemId() {
		return ItemId;
	}
	public void setItemId(int itemId) {
		ItemId = itemId;
	}
	public int getAmt() {
		return amt;
	}
	public void setAmt(int amt) {
		this.amt = amt;
	}
	public int getAftAmt() {
		return aftAmt;
	}
	public void setAftAmt(int aftAmt) {
		this.aftAmt = aftAmt;
	}
		 

}

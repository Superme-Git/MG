package fire.log.beans;


public class OpPointGoldExBean {
	private int Typte;			//操作类型（1求购/2寄卖）
	private int ChgAmnt;		//交易数量
	private int Price;			//货币单价
	private int Fee;			//手续费
	private int IsOK;			//是否成功（取消挂单为失败）0失败，1成功
	
	public OpPointGoldExBean(int typte, int chgAmnt, int price, int fee, int isOK) {
		Typte = typte;
		ChgAmnt = chgAmnt;
		Price = price;
		Fee = fee;
		IsOK = isOK;
	}
	
	public int getTypte() {
		return Typte;
	}
	public int getChgAmnt() {
		return ChgAmnt;
	}
	public int getPrice() {
		return Price;
	}
	public int getFee() {
		return Fee;
	}
	public int getIsOK() {
		return IsOK;
	}
}

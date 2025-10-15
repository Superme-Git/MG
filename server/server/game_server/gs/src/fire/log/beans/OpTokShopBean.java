package fire.log.beans;


public class OpTokShopBean {
	public static int OpTokShopBean_Oper_Buy = 0;
	public static int OpTokShopBean_Oper_Sell = 1;
	
	private int Oper;		//0购买/1出售
	private int ClaId;		//物品类id
	private int IteId;		//物品id
	private int IteAmt;		//物品数量
	private int TokTyp;		//货币类型
	private int TokChg;		//货币量
	private float Off;		//折扣（几折）
	private int GetTop;		//购买量是否当日达到当日该物品购买上限0否，1是
	public OpTokShopBean(int oper, int claId, int iteId, int iteAmt, int tokTyp, int tokChg, float off, int getTop) {
		Oper = oper;
		ClaId = claId;
		IteId = iteId;
		IteAmt = iteAmt;
		TokTyp = tokTyp;
		TokChg = tokChg;
		Off = off;
		GetTop = getTop;
	}
	public int getOper() {
		return Oper;
	}
	public int getClaId() {
		return ClaId;
	}
	public int getIteId() {
		return IteId;
	}
	public int getIteAmt() {
		return IteAmt;
	}
	public int getTokTyp() {
		return TokTyp;
	}
	public int getTokChg() {
		return TokChg;
	}
	public float getOff() {
		return Off;
	}
	public int getGetTop() {
		return GetTop;
	}
	
}

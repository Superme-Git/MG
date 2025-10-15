package fire.log.beans;


public class OpSilShopBean {
	public static int Op_SilShopBean_Buy = 1;
	public static int Op_SilShopBean_Sell = 2;
	
	private int IsTran;					//交易“1买/2卖”状态
	private int ItemId;					//物品id
	private int ItClasId;				//物品类id
	private int ItQA;					//物品品质
	private int ItNum;					//物品数量
	private int MonTyp;					//交易货币类型
	private int MonAmt;					//货币量
	private int ItOrPric;				//物品原价
	private float Up;						//涨幅
	private int ItNowPric;				//成交现价
	private int isUp;					//购买量是否当日达到当日上限0无，1是
	private int isSuc;					//交易是否成功0非，1成功
	private int FaiDesc;				//失败原因
	public OpSilShopBean(int isTran, int itemId, int itClasId, int itQA, int itNum, int monTyp, int monAmt, int itOrPric, float up, int itNowPric, int isUp, int isSuc,
			int faiDesc) {
		IsTran = isTran;
		ItemId = itemId;
		ItClasId = itClasId;
		ItQA = itQA;
		ItNum = itNum;
		MonTyp = monTyp;
		MonAmt = monAmt;
		ItOrPric = itOrPric;
		Up = up;
		ItNowPric = itNowPric;
		this.isUp = isUp;
		this.isSuc = isSuc;
		FaiDesc = faiDesc;
	}
	
	public int getIsTran() {
		return IsTran;
	}
	public int getItemId() {
		return ItemId;
	}
	public int getItClasId() {
		return ItClasId;
	}
	public int getItQA() {
		return ItQA;
	}
	public int getItNum() {
		return ItNum;
	}
	public int getMonTyp() {
		return MonTyp;
	}
	public int getMonAmt() {
		return MonAmt;
	}
	public int getItOrPric() {
		return ItOrPric;
	}
	public float getUp() {
		return Up;
	}
	public int getItNowPric() {
		return ItNowPric;
	}
	public int getIsUp() {
		return isUp;
	}
	public int getIsSuc() {
		return isSuc;
	}
	public int getFaiDesc() {
		return FaiDesc;
	}
}

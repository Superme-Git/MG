package fire.log.beans;


public class OpTradExBean {
	private int ItemId;			//物品id
	private int ItClasId;		//物品类id
	private int ItQA;			//物品品质
	private int IsTrea;			//是否珍品0否，1是
	private long ItKey;			//唯一ID（如果是珍品的话）
	private float Pric;			//物品单价
	private int TraItAmt;		//交易物品数量
	private int TraMonTyp;		//交易货币类型
	private int MonChg;			//货币量
	private int TaxMonTyp;		//出售者税收货币类型
	private int TaxAmt;			//出售者税收量
	private float Barg;			//出售者物品定价建议价格百分比

	public OpTradExBean(int itemId, int itClasId, int itQA, int isTrea, long itKey, float pric, int traItAmt, int traMonTyp, int monChg, int taxMonTyp, int taxAmt, float barg) {
		ItemId = itemId;
		ItClasId = itClasId;
		ItQA = itQA;
		IsTrea = isTrea;
		ItKey = itKey;
		Pric = pric;
		TraItAmt = traItAmt;
		TraMonTyp = traMonTyp;
		MonChg = monChg;
		TaxMonTyp = taxMonTyp;
		TaxAmt = taxAmt;
		Barg = barg;
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

	public int getIsTrea() {
		return IsTrea;
	}

	public long getItKey() {
		return ItKey;
	}

	public float getPric() {
		return Pric;
	}

	public int getTraItAmt() {
		return TraItAmt;
	}

	public int getTraMonTyp() {
		return TraMonTyp;
	}

	public int getMonChg() {
		return MonChg;
	}

	public int getTaxMonTyp() {
		return TaxMonTyp;
	}

	public int getTaxAmt() {
		return TaxAmt;
	}

	public float getBarg() {
		return Barg;
	}
}

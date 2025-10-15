package fire.log.beans;


public class OpAucRackBean {
	private int RacStat;						//“1上/0下”架
	private int ItSubPag;						//物品归类次页签id
	private int OfRacDesc;						//下架原因（1自动、2手动）
	private int StaUsCnt;						//我的摊位使用数
	private int ItemId;							//物品id
	private int ItClasId;						//物品类id
	private int ItQA;							//物品品质
	private int IsTrea;							//是否珍品0否，1是
	private long ItKey;							//珍品系列字段（如果是珍品的话）
	private int IsDur;							//是否公示期0无，1是
	
	public OpAucRackBean(int racStat, int itSubPag, int ofRacDesc, int staUsCnt, int itemId, int itClasId, int itQA, int isTrea, long itKey, int isDur) {
		RacStat = racStat;
		ItSubPag = itSubPag;
		OfRacDesc = ofRacDesc;
		StaUsCnt = staUsCnt;
		ItemId = itemId;
		ItClasId = itClasId;
		ItQA = itQA;
		IsTrea = isTrea;
		ItKey = itKey;
		IsDur = isDur;
	}

	public int getRacStat() {
		return RacStat;
	}

	public int getItSubPag() {
		return ItSubPag;
	}

	public int getOfRacDesc() {
		return OfRacDesc;
	}

	public int getStaUsCnt() {
		return StaUsCnt;
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

	public int getIsDur() {
		return IsDur;
	}
}

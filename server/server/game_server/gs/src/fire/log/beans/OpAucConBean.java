package fire.log.beans;


public class OpAucConBean {
	private int ItMaiPag;					//物品归类主页签id
	private int ItSubPag;					//物品归类次页签id
	private int ItemId;						//物品id
	private int ItClasId;					//物品类id
	private int ItQA;						//物品品质
	private int IsTrea;						//是否珍品0否，1是
	private long ItKey;					//珍品系列字段（如果是珍品的话）
	private int ConStat;					//关注状态（1关注\0取消关注
	private int ConItCnt;					//已关注总物品数
	
	public OpAucConBean(int itMaiPag, int itSubPag, int itemId, int itClasId, int itQA, int isTrea, long itKey, int conStat, int conItCnt) {
		ItMaiPag = itMaiPag;
		ItSubPag = itSubPag;
		ItemId = itemId;
		ItClasId = itClasId;
		ItQA = itQA;
		IsTrea = isTrea;
		ItKey = itKey;
		ConStat = conStat;
		ConItCnt = conItCnt;
	}

	public int getItMaiPag() {
		return ItMaiPag;
	}

	public int getItSubPag() {
		return ItSubPag;
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

	public int getConStat() {
		return ConStat;
	}

	public int getConItCnt() {
		return ConItCnt;
	}
}

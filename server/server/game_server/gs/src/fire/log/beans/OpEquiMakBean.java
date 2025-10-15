package fire.log.beans;

public class OpEquiMakBean {
	private int EquID;			//所要打造的装备ID
	private int EquLvl;			//所要打造的装备等级
	private int IsTrea;		 	//是否珍品 0否，1是
	private long ItKey;			//物品唯一ID
	private int ConsuBookId;	//消耗制造书ID
	private int ConsuBookNum;	//消耗制造书-数量
	private int ConsuTrId;		//消耗铁ID
	private int ConsuTrNum;		//消耗铁数量
	private int ConsuFuId;		//消耗打造符ID
	private int ConsuFuNum;		//消耗打造符数量
	private int ConsuStoId;		//消耗原石ID（为可选项，没有就空）
	private int ConsuStoNum;	//消耗原石数量（为可选项，没有就空）
	private int ConsuSilId;		//消耗银币ID
	private int ConsuSilNum;	//消耗银币数量
	
	public OpEquiMakBean(int equID, int equLvl, int isTrea, long itKey, int consuBookId, int consuBookNum, int consuTrId, int consuTrNum, int consuFuId, int consuFuNum, int consuStoId,
			int consuStoNum, int consuSilId, int consuSilNum) {
		EquID = equID;
		EquLvl = equLvl;
		IsTrea = isTrea;
		ItKey = itKey;
		ConsuBookId = consuBookId;
		ConsuBookNum = consuBookNum;
		ConsuTrId = consuTrId;
		ConsuTrNum = consuTrNum;
		ConsuFuId = consuFuId;
		ConsuFuNum = consuFuNum;
		ConsuStoId = consuStoId;
		ConsuStoNum = consuStoNum;
		ConsuSilId = consuSilId;
		ConsuSilNum = consuSilNum;
	}
	
	public int getEquID() {
		return EquID;
	}
	public int getEquLvl() {
		return EquLvl;
	}
	public int getIsTrea() {
		return IsTrea;
	}
	public long getItKey() {
		return ItKey;
	}
	public int getConsuBookId() {
		return ConsuBookId;
	}
	public int getConsuBookNum() {
		return ConsuBookNum;
	}
	public int getConsuTrId() {
		return ConsuTrId;
	}
	public int getConsuTrNum() {
		return ConsuTrNum;
	}
	public int getConsuFuId() {
		return ConsuFuId;
	}
	public int getConsuFuNum() {
		return ConsuFuNum;
	}
	public int getConsuStoId() {
		return ConsuStoId;
	}
	public int getConsuStoNum() {
		return ConsuStoNum;
	}
	public int getConsuSilId() {
		return ConsuSilId;
	}
	public int getConsuSilNum() {
		return ConsuSilNum;
	}
	
}

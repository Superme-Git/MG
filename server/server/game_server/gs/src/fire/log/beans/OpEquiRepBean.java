package fire.log.beans;

public class OpEquiRepBean {

	private int EquID;					//所要修理的装备ID
	private int EquLvl;					//所要修理的装备等级
	private int IsTrea;					//是否珍品 0否，1是
	private int ItKey;					//唯一key
	private int AftReDegre;				//修理之后耐久度
	private int RepType;				//修理类型：0普通/1特殊
	
	public OpEquiRepBean(int equID, int equLvl, int isTrea, int itKey, int aftReDegre, int repType) {
		EquID = equID;
		EquLvl = equLvl;
		IsTrea = isTrea;
		ItKey = itKey;
		AftReDegre = aftReDegre;
		RepType = repType;
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

	public int getItKey() {
		return ItKey;
	}

	public int getAftReDegre() {
		return AftReDegre;
	}

	public int getRepType() {
		return RepType;
	}
}

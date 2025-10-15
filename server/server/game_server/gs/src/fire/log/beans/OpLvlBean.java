package fire.log.beans;


public class OpLvlBean {
	public static final String Op_LvlBean_LvlType_Game = "game";// 等级类型 游戏等级/vip等级，game/vip
	public static final String Op_LvlBean_LvlType_VIP = "vip";
	
	private String LvlType;					//等级类型游戏等级/vip等级，
	private int BfLvl;						//玩家变化前等级
	private int AfLvl;						//玩家变化后等级
	private long BfExp;						//变化前经验没有记为-1
	private long AfExp;						//变化后经验没有记为-1
	private int Egettype;					//获得或者失去经验的类别
	
	public OpLvlBean(String lvlType, int bfLvl, int afLvl, long bfExp, long afExp, int egettype) {
		LvlType = lvlType;
		BfLvl = bfLvl;
		AfLvl = afLvl;
		BfExp = bfExp;
		AfExp = afExp;
		Egettype = egettype;
	}

	public String getLvlType() {
		return LvlType;
	}

	public int getBfLvl() {
		return BfLvl;
	}

	public int getAfLvl() {
		return AfLvl;
	}

	public long getBfExp() {
		return BfExp;
	}

	public long getAfExp() {
		return AfExp;
	}

	public int getEgettype() {
		return Egettype;
	}
}

package fire.log.beans;


public class OpBonusBean {
	public static int OpBonusBean_BonuType_World = 1;
	public static int OpBonusBean_BonuType_Guild = 2;
	public static int OpBonusBean_BonuType_Team = 3;
	
	public static String OpBonusBean_Payflg_Get = "get";
	public static String OpBonusBean_Payflg_Use = "use";
	
	private int BonuType;			//红包类型（世界红包、公会红包、队伍红包）
	private String Payflg;				//获得或发放标记(标记是获取get/发放use)
	private int TokType;			//代币类型（金币或者符石 看配表）代币
	private int ChgAmnt;			//代币变化数量
	private int BonuxCnt;			//红包个数
	private long SndUsrId;           //红包发送者roleid
	
	public OpBonusBean(int bonuType, String payflg, int tokType, int chgAmnt, int bonuxCnt ,long sndUsrId) {
		BonuType = bonuType;
		Payflg = payflg;
		TokType = tokType;
		ChgAmnt = chgAmnt;
		BonuxCnt = bonuxCnt;
		SndUsrId = sndUsrId;
	}
	
	public int getBonuType() {
		return BonuType;
	}
	public String getPayflg() {
		return Payflg;
	}
	public int getTokType() {
		return TokType;
	}
	public int getChgAmnt() {
		return ChgAmnt;
	}
	public int getBonuxCnt() {
		return BonuxCnt;
	}
	public long getSndUsrId() {
		return SndUsrId;
	}
	
	
}

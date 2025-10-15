package fire.log.beans;

public class OpNoviBattleBean {
	private int TaskNo;				//任务编号（180134、180138、180147、180149）
	private int FinishFlag;			//成功/失败(成功为succ,失败为fail)
	private int FailDesc;			//成功/失败原因(自定义字符串,可留空)[
	private int MapId;				//地图ID
	private int IsTeam;				//是否组队1是，0否
	private int IsAssBatl;			//是否有助战1是，0否
	private int AssBatlId;			//助战ID
	private int IsAutBatl;			//是否自动战斗1是，0否
	private int BatlCom;			//战斗指令
	
	public OpNoviBattleBean(int taskNo, int finishFlag, int failDesc, int mapId, int isTeam, int isAssBatl, int assBatlId, int isAutBatl, int batlCom) {
		TaskNo = taskNo;
		FinishFlag = finishFlag;
		FailDesc = failDesc;
		MapId = mapId;
		IsTeam = isTeam;
		IsAssBatl = isAssBatl;
		AssBatlId = assBatlId;
		IsAutBatl = isAutBatl;
		BatlCom = batlCom;
	}

	public int getTaskNo() {
		return TaskNo;
	}

	public int getFinishFlag() {
		return FinishFlag;
	}

	public int getFailDesc() {
		return FailDesc;
	}

	public int getMapId() {
		return MapId;
	}

	public int getIsTeam() {
		return IsTeam;
	}

	public int getIsAssBatl() {
		return IsAssBatl;
	}

	public int getAssBatlId() {
		return AssBatlId;
	}

	public int getIsAutBatl() {
		return IsAutBatl;
	}

	public int getBatlCom() {
		return BatlCom;
	}
}

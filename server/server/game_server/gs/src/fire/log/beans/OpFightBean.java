package fire.log.beans;

public class OpFightBean {
	public static int OpFightBean_FailDesc_BattleEnd = 1;		// 战斗结束
	public static int OpFightBean_FailDesc_Faild = 2;		// 失败
	public static int OpFightBean_FailDesc_Runaway = 3;		// 逃跑
	public static int OpFightBean_FailDesc_Feedback = 4;		// 超时
	
	private long TeamId;		// 队伍ID
	private int TeamAmd; 		// 组队人数
	private int Map; 			// 所在地图
	private String Result; 		// 战斗结果（胜利、失败）
	private int FailDesc; 		// 成功/失败原因(自定义)
	private long BattleId;		// 战斗唯一ID
	
	public OpFightBean(long teamId, int teamAmd, int map, String result, int failDesc, long battleId) {
		TeamId = teamId;
		TeamAmd = teamAmd;
		Map = map;
		Result = result;
		FailDesc = failDesc;
		BattleId = battleId;
	}
	
	public long getTeamId() {
		return TeamId;
	}
	public int getTeamAmd() {
		return TeamAmd;
	}
	public int getMap() {
		return Map;
	}
	public String getResult() {
		return Result;
	}
	public int getFailDesc() {
		return FailDesc;
	}
	public long getBattleId() {
		return BattleId;
	}
}

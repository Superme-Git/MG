package fire.log.beans;

import java.util.ArrayList;
import java.util.List;

public class TeamOpBean {
	private long teamId; // 队伍ID
	private int insId; // 组队刷的任务id
	private int lightId; // 组队光环ID
	private int lightLvl; // 组队光环等级
	private int teaMemCnt; // 组队人数
	private List<Integer> memPro = new ArrayList<Integer>(); // 组队时人员职业分布
	private List<Integer> race = new ArrayList<Integer>(); // 组队时人员种族分布
	private List<Integer> lvl = new ArrayList<Integer>(); // 组队时人员等级分布
	private long teamDur; // 时长
	private long teamLed; // 队伍队长
	
	private int mapId; // 地图ID
	private long battleId; // 战斗ID

	public TeamOpBean(long teamId, int insId, int lightId, int lightLvl, List<int[]> logInfo, long teamDur, long teamLed, int mapId, long battleId) {
		this.teamId = teamId;
		this.insId = insId;
		this.lightId = lightId;
		this.lightLvl = lightLvl;
		
		for (int[] arg : logInfo) {
			if(arg.length != 3){
				System.out.println("Error in TeamOpBean");
				continue;
			}
			memPro.add(arg[0]);
			race.add(arg[1]);
			lvl.add(arg[2]);
		}

		this.teaMemCnt = logInfo.size();
		this.teamDur = teamDur;
		this.teamLed = teamLed;
		
		this.mapId = mapId;
		this.battleId = battleId;
	}

	public long getTeamDur() {
		return teamDur;
	}

	public long getTeamLed() {
		return teamLed;
	}

	public List<Integer> getRace() {
		return race;
	}

	public long getTeamId() {
		return teamId;
	}

	public int getInsId() {
		return insId;
	}

	public int getLightId() {
		return lightId;
	}

	public int getLightLvl() {
		return lightLvl;
	}

	public int getTeaMemCnt() {
		return teaMemCnt;
	}

	public List<Integer> getMemPro() {
		return memPro;
	}
	public int getMapId() {
		return mapId;
	}
	public long getBattleId() {
		return battleId;
	}
}

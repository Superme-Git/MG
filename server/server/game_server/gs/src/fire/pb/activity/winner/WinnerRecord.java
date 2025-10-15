package fire.pb.activity.winner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WinnerRecord {
	private long teamid;//队伍id
	private int round;//用来记录已经完成了多少轮冠军试炼
	private long startTime;//用来记录最近一次冠军试炼的开始时间,用long来记录,方便转换
	private long roundStartTime;//用来记录最近一轮冠军试炼的开始时间
	private long roundCostTime;//用来记录该队伍完成一轮冠军试炼的最快时间
	private long leaderID;//队长的id
	private ConcurrentHashMap<Long, Long> teamMembers=new ConcurrentHashMap<Long, Long>();//队伍的成员的id,包括队长的
	private int teamScore;//队伍的积分
	private List<Integer> stageList=new ArrayList<Integer>();//冠军试炼关卡的先后顺序
	private int stageSucc;//当前通过了多少关,和stageList对应
	private long allCostTime;//该队伍做任务一共消耗的时间值
	private long taskstartTime;//任务开始时间，记录每次接受任务开始的时间
	
	public WinnerRecord(long teamid){
		this.teamid=teamid;
	}
	
	public long getTeamid() {
		return teamid;
	}
	public void setTeamid(long teamid) {
		this.teamid = teamid;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getRoundStartTime() {
		return roundStartTime;
	}
	public void setRoundStartTime(long roundStartTime) {
		this.roundStartTime = roundStartTime;
	}
	public long getRoundCostTime() {
		return roundCostTime;
	}
	public void setRoundCostTime(long roundCostTime) {
		this.roundCostTime = roundCostTime;
	}
	public long getLeaderID() {
		return leaderID;
	}
	public void setLeaderID(long leaderID) {
		this.leaderID = leaderID;
	}
	
	public ConcurrentHashMap<Long, Long> getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(ConcurrentHashMap<Long, Long> teamMembers) {
		this.teamMembers = teamMembers;
	}

	public int getTeamScore() {
		return teamScore;
	}
	public void setTeamScore(int teamScore) {
		this.teamScore = teamScore;
	}
	public List<Integer> getStageList() {
		return stageList;
	}
	public void setStageList(List<Integer> stageList) {
		this.stageList = stageList;
	}
	public int getStageSucc() {
		return stageSucc;
	}
	public void setStageSucc(int stageSucc) {
		this.stageSucc = stageSucc;
	}
	public long getAllCostTime() {
		return allCostTime;
	}
	public void setAllCostTime(long allCostTime) {
		this.allCostTime = allCostTime;
	}
	public long getTaskstartTime() {
		return taskstartTime;
	}
	public void setTaskstartTime(long taskstartTime) {
		this.taskstartTime = taskstartTime;
	}
	
	
	

}

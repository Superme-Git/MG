package robot.team;

import java.util.HashMap;
import java.util.Map;

/**
 * 隊伍管理器
 * 
 * @author Bill_Ye
 */
public class TeamMgr {
	public static TeamMgr _instance = new TeamMgr();

	public static TeamMgr getInstance() {
		return _instance;
	}

	private Map<Long, Team> teams = new HashMap<Long, Team>();

	public Map<Long, Team> getTeams() {
		return teams;
	}

	/**
	 * 获取组队状态
	 */
	public int getTeamState(long roleId, long teamId) {
		if (teamId < 0)
			return TeamRoleState.TEAM_NULL;

		Team team = teams.get(teamId);
		if (team == null)
			return TeamRoleState.TEAM_NULL;

		if (team.isLeader(roleId))
			return TeamRoleState.TEAM_LEADER;

		Integer state = team.getMemberState(roleId);
		if (state != null)
			return state;

		return TeamRoleState.TEAM_NULL;
	}

	public synchronized Team createTeam(long teamId, long roleId) {
		Team team = teams.get(teamId);
		if (team != null)
			return team;

		team = new Team(teamId);
		team.leaderRoleId = roleId;// 设置队长的id
		teams.put(teamId, team);
		System.out.println("ROLEID=" + roleId + "\tINFO=创建了一个队伍。目前共有 "
				+ TeamMgr.getInstance().getTeams().size() + "个队伍");
		return team;
	}

}
package robot.team;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.team.SAddTeamApply;

/**
 * 队伍相关
 * 
 * @author Bill_Ye
 */
public class Team {
	public final long teamId;
	public long leaderRoleId = -1;
	public Map<Long, Integer> teamMembers = new LinkedHashMap<Long, Integer>();// value=TeamState
	public List<SAddTeamApply> teamApplys = new LinkedList<SAddTeamApply>();

	public Team(long teamId) {
		this.teamId = teamId;
	}

	public int getMembersNum() {
		return teamMembers.size();
	}

	public boolean isTeamFull() {
		return teamMembers.size() >= 3;
	}

	public boolean isMember(long roleId) {
		return teamMembers.containsKey(roleId);
	}

	public Integer getMemberState(long roleId) {
		return teamMembers.get(roleId);
	}

	public void setTeamLeader(long newleaderRoleId) {
		if (leaderRoleId == newleaderRoleId)
			return;
		long oldleaderId = leaderRoleId;
		leaderRoleId = newleaderRoleId;
		if (oldleaderId > 0) {
			teamMembers.remove(oldleaderId);
			teamMembers.put(oldleaderId, TeamRoleState.TEAM_NORMAL);
		}
	}

	public synchronized int addRole(long roleId, int state) {
		if (state == TeamRoleState.TEAM_NORMAL && leaderRoleId <= 0) {
			leaderRoleId = roleId;
			return TeamRoleState.TEAM_LEADER;
		}
		teamMembers.put(roleId, state);
		return state;
	}

	public synchronized boolean delRole(long roleId) {
		if (roleId == leaderRoleId) {
			TeamMgr.getInstance().getTeams().remove(teamId);
		}
		return teamMembers.remove(roleId) != null;
	}

	public boolean contains(long roleId) {
		if (leaderRoleId == roleId)
			return true;
		return teamMembers.containsKey(roleId);
	}

	public int setMemberState(long roleId, int newstate) {
		if (roleId == leaderRoleId)
			throw new IllegalArgumentException("不能改变队长状态");
		teamMembers.put(roleId, newstate);
		return newstate;
	}

	public boolean isLeader(long roleId) {
		if (leaderRoleId == roleId)
			return true;
		else
			return false;
	}

	public void pushApply(SAddTeamApply apply) {
		synchronized (teamApplys) {
			teamApplys.add(apply);
		}
	}

	public SAddTeamApply pollApply() {
		synchronized (teamApplys) {
			if (teamApplys.isEmpty())
				return null;
			return teamApplys.remove(0);
		}
	}

	public void clearApplies() {
		synchronized (teamApplys) {
			teamApplys.clear();
		}
	}
}

package robot.team;

import fire.pb.team.TeamMemberState;

public class TeamRoleState {
	public final static int TEAM_NULL = -1; // 无队伍状态
	public final static int TEAM_LEADER = 0; // 队长状态
	public final static int TEAM_NORMAL = TeamMemberState.eTeamNormal; // 正常状态
	public final static int TEAM_ABSENT = TeamMemberState.eTeamAbsent; // 暂离状态
	public final static int TEAM_RETURN = TeamMemberState.eTeamReturn; // 回归队伍状态
	public final static int TEAM_OFFLINE = TeamMemberState.eTeamFallline; // 离线状态
}

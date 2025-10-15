package fire.pb.gm;

import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;
import fire.pb.scene.movable.SceneTeam;
import fire.pb.team.TeamMemberState;

public class GM_getteam extends GMCommand {

	@Override
	boolean exec(String[] args) {
		String rolename = args[0];
		final Long roleid = xtable.Rolename2key.select(rolename);

		final Long teamId = xtable.Roleid2teamid.select(roleid);
		if (teamId == null) {
			sendToGM(rolename + " 没有队伍.");
			return true;
		}
		final xbean.TeamInfo teaminfo = xtable.Team.select(teamId);
		if (teaminfo == null) {
			sendToGM(rolename + "有teamId但是teaminfo为空.");
			return true;
		}
		String info = "";
		String tlname = xtable.Properties.selectRolename(teaminfo
				.getTeamleaderid());
		info += "逻辑队伍队长为 " + tlname + " , 队员为：";
		for (xbean.TeamMember member : teaminfo.getMembers()) {
			info += xtable.Properties.selectRolename(member.getRoleid());
			String state = " 错误状态； ";
			if (member.getState() == TeamMemberState.eTeamNormal)
				state = " 正常；";
			else if (member.getState() == TeamMemberState.eTeamAbsent)
				state = " 暂离；";
			else if (member.getState() == TeamMemberState.eTeamReturn)
				state = " 归队中；";
			else if (member.getState() == TeamMemberState.eTeamFallline)
				state = " 离线；";
			info += state;
		}
		info += "\n";
		sendToGM(info);
		info = "";

		final Role role = RoleManager.getInstance().getRoleByID(roleid);
		if (null == role)
			return false;
		final SceneTeam steam = role.getTeam();
		if (steam == null) {
			sendToGM(rolename + " 没有场景队伍.");
			return true;
		}
		final Role cap = steam.getCapitan();
		if (cap != null)
			tlname = cap.getName();
		else
			tlname = " 未知队长 ";
		info += "场景队伍队长为 " + tlname + " , 队员为：";

		for (Role mem : steam.getMembers()) {
			info += mem.getName();

			info += "正常";
		}
		for (Role mem : steam.getLeavers()) {
			info += mem.getName();

			info += "暂离";
		}
		info += "\n";
		sendToGM(info);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}

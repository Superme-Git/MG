

package fire.pb.mission.fairyland;

import java.util.List;

import fire.pb.map.Transfer;
import fire.pb.mission.SFairylandStatus;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;
import xbean.RolePos;


public class PAbandonFairyland extends Procedure {

	private long roleid;

	public PAbandonFairyland(long roleid) {
		super();
		this.roleid = roleid;

	}

	@Override
	protected boolean process() throws Exception {
		if (!TeamManager.isInTeam(roleid)) {
			xbean.RolePos rolePos = xtable.Roleposes.get(roleid);
			int mapid = rolePos.getMapid();
			int posx = rolePos.getPosx();
			int posy = rolePos.getPosy();
			xtable.Roleposes.remove(roleid);
			// 跳转
			Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, SRoleEnterScene.QUEST);
			// 设状态
			SFairylandStatus sds = new SFairylandStatus(RolePos.OUTDREAM);
			psendWhileCommit(roleid, sds);
		} else {// 如果在team里
			Team team = TeamManager.getTeamByRoleId(roleid);
			if (team.isTeamLeader(roleid)) {// 如果是队长
				xbean.RolePos leaderPos = xtable.Roleposes.get(roleid);
				int mapid = leaderPos.getMapid();
				int posx = leaderPos.getPosx();
				int posy = leaderPos.getPosy();
				// 如果是队长离开副本或放弃任务,他的记录被删除
				xtable.Roleposes.remove(roleid);
				List<Long> roleids = team.getAllMemberIds();
				for (Long mem : roleids) {
					xbean.RolePos rolePos = xtable.Roleposes.get(mem);
					if (rolePos == null)
						continue;
					SFairylandStatus sds = null;
					// 其他人只要还有梦境任务,就是暂离,否则就是outdream
					if (rolePos.getHastask() >0) {
						sds = new SFairylandStatus(RolePos.ABSENTDREAM);
						rolePos.setStatus(RolePos.ABSENTDREAM);
					} else {
						sds = new SFairylandStatus(RolePos.OUTDREAM);
						xtable.Roleposes.remove(roleid);
					}

					psendWhileCommit(mem, sds);
				}
				Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, SRoleEnterScene.QUEST);
				//如果有暂离的队员,也把他们踢出副本
				List<Long> absentids=team.getAbsentMemberIds();
				for (Long mem : absentids) {
					xbean.RolePos pos = xtable.Roleposes.select(mem);
					if (pos!=null&&pos.getStatus()==RolePos.INDREAM) 
					Transfer.justGotoWhileCommit(mem, mapid, posx, posy, SRoleEnterScene.FORCE_GOTO);
				}
			}else {//如果只是普通队员,则变成没有梦境任务的状态,但是仍然在梦境副本中
				xtable.Roleposes.remove(roleid);
				// 设状态
				SFairylandStatus sds = new SFairylandStatus(RolePos.OUTDREAM);
				psendWhileCommit(roleid, sds);
			}
		}
		return true;
	}

}

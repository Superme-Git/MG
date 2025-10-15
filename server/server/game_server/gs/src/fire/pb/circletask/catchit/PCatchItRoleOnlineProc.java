package fire.pb.circletask.catchit;

import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.SpecialQuestID;
import fire.pb.circletask.SpecialQuestState;
import mkdb.Procedure;

public class PCatchItRoleOnlineProc extends Procedure {
	private final long roleid;
	
	public PCatchItRoleOnlineProc(final long roleid) {
		this.roleid = roleid;
	}
	
	public boolean process() {
		
		//判断玩家是否还在队伍里
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		if (null == team) {
			return false;
		}
		
		long teamLeader = team.getTeamLeaderId();
		
		//获取队长的捉鬼进度		
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(teamLeader);
		if (quest_map == null)
			return false;
		xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(SpecialQuestID.catchIt);
		if (null == sqinfo) {
			return false;
		}
		
		if (sqinfo.getQueststate() != 0 && sqinfo.getQueststate() != SpecialQuestState.FAIL){
//			final long now = Calendar.getInstance().getTimeInMillis();
//			sqinfo.setSumnum(1);
//			//sqinfo.setRound(1);
			
			CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, false);
		}
		
		
		return true;
	}

}

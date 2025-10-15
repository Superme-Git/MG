package fire.pb.circletask;
import fire.pb.battle.PNewBattle;
import fire.pb.circletask.CircTaskClass;
import mkdb.Transaction;


public class EnterCircTaskPatrolBattle {
	private long roleid;
	private int mapid;
	
	public EnterCircTaskPatrolBattle(long roleid, int mapid){
		this.roleid = roleid;
		this.mapid = mapid;
	}
	
	public void enterBattle(){
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		if (null != team)
			if (!team.getAbsentMemberIds().contains(roleid) && team.getTeamLeaderId() != roleid){
			return;
		}
		
		final CircleTask sq = new CircleTask(roleid, true);
		for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()){
			final xbean.CircleTaskInfo sqinfo = sq.getSpecialQuestInfo(curType);
			if (null == sqinfo
					|| sqinfo.getQuestclass() != CircTaskClass.CircTask_Patrol
					|| sqinfo.getDstmapid() != mapid)
				continue;
			int battleCfgId = sqinfo.getDstitemid();
			if (battleCfgId <= 0)
				continue;
		
			CircTaskPatrolBattleEndHandler handler = new CircTaskPatrolBattleEndHandler(curType);
			if (Transaction.current() != null){
				new PNewBattle(roleid, battleCfgId, handler).call();
			}
			else {
				new PNewBattle(roleid, battleCfgId, handler).submit();
			}
			break;
		}
	}
}

package fire.pb.circletask;
import java.util.Map;

import fire.pb.battle.PNewBattle;
import fire.pb.circletask.CircTaskClass;
import mkdb.Transaction;


public class EnterAnYePatrolBattle {
	private long roleid;
	private int mapid;
	
	public EnterAnYePatrolBattle(long roleid, int mapid){
		this.roleid = roleid;
		this.mapid = mapid;
	}
	
	public void enterBattle(){
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		if (null != team)
			if (!team.getAbsentMemberIds().contains(roleid) && team.getTeamLeaderId() != roleid){
			return;
		}
		
		xbean.AnYeData xxx = xtable.Anyemaxituan.select(roleid);
		if(xxx == null)
			return;
		
		for (Map.Entry<Integer, xbean.AnYeTask> e : xxx.getAnyetasks().entrySet()) {
			if(e.getValue().getKind() == CircTaskClass.CircTask_Patrol) {
				if(e.getValue().getState() == SpecialQuestState.UNDONE &&
						e.getValue().getKind() == CircTaskClass.CircTask_Patrol &&
						e.getValue().getDstnpcid() == mapid){
					int battleCfgId = e.getValue().getDstitemid();
					if (battleCfgId <= 0)
						continue;
					
					AnYePatrolBattleEndHandler handler = new AnYePatrolBattleEndHandler(e.getKey());
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
	}
}

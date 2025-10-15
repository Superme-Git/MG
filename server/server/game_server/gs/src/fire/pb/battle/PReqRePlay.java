package fire.pb.battle;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

/**为没有选择操作的战斗单元 设置默认的操作*/
public class PReqRePlay extends Procedure {
	PReqRePlay(final String battlecameraurl, final long roleid){
		this.battlecameraurl = battlecameraurl;
		this.roleid = roleid;
	}
	
	public boolean process(){
		fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByRoleId(roleid);
			
		BuffAgent agent = new BuffRoleImpl(roleid);
		byte candoit = 1;
		if (agent.existBuff(BuffConstant.StateType.STATE_REPLAY)
				||agent.existBuff(BuffConstant.StateType.STATE_BATTLE_FIGHTER)
				||agent.existBuff(BuffConstant.StateType.STATE_BATTLE_WATCHER))
		{
			candoit = 0;
			// 观战中
		}
		if(candoit == 1&&agent.canAddBuff(BuffConstant.StateType.STATE_REPLAY))
		{
			agent.addCBuff(BuffConstant.StateType.STATE_REPLAY);
		}
		else
			candoit = 0;
		SReqRePlay send = new SReqRePlay(candoit, battlecameraurl);
		psend(roleid, send);
		return true;
	}
	
	private final String battlecameraurl;
	private final long roleid;
}

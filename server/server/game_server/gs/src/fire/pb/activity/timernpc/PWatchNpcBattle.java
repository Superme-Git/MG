package fire.pb.activity.timernpc;

import fire.pb.battle.watch.PSendWatchBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.util.FireProp;
import mkdb.Procedure;

public class PWatchNpcBattle extends Procedure {

	private final long roleId;
	private final int npcId;
	private final long npcKey;
	
	public PWatchNpcBattle(final long roleid, final int npcId, final long npcKey) {
		this.roleId = roleid;
		this.npcId = npcId;
		this.npcKey = npcKey;
	}

	@Override
	protected boolean process() throws Exception {
		int actId = TimerNpcService.getInstance().getActId(npcId);
		//活动错误
		if (actId == -1) {
			return false;
		}
		
		//检查NPC状态
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.select(actId);
		if (npcInfo == null) {
			return false;
		}
		if(!npcInfo.getNpcinfo().containsKey(npcKey)){
			return false;
		}
		//怪物已经消失
		xbean.timerNpcInfo tInfo = npcInfo.getNpcinfo().get(npcKey);
		if (tInfo == null) {
			return false;
		}
		
		long battleroleid = tInfo.getRoleid();
		if (battleroleid > 0) {
			if (battleroleid == roleId)
				return false;
			BuffAgent agent = new BuffRoleImpl(roleId, true);
			if (!agent.canAddBuff(FireProp.getIntValue("battle", "mtgserv.battle_watchBuff"))) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, fire.pb.util.FireProp.getIntValue("message", "mtgserv.buffConflict"), null);
				return false;
			}
			new PSendWatchBattle(roleId, battleroleid).call();
		}
		
		return true;
	}

	
}

package fire.pb.mission.treasuremap;

import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.battle.watch.PSendWatchBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.map.EventTimerNpc;
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
		int biaoId = TimerNpcService.getInstance().getEventId(npcId);
		//事件错误
		if (biaoId == -1) {
			return false;
		}
		
		EventTimerNpc eventTimer = BaoTuMapManager.getInstance().getTimerNpc(biaoId);
		if (eventTimer == null)
			return false;
		
		long battleroleid = TimerNpcService.getInstance().getEventNpcBattleRole(biaoId, npcKey);
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

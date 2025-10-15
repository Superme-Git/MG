package fire.pb.instancezone;

import fire.pb.battle.watch.PSendWatchBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.util.FireProp;
import mkdb.Procedure;

public class PWatchNpcBattle extends Procedure {

	private final long roleId;
	private final long npcKey;
	
	public PWatchNpcBattle(final long roleid, final long npcKey) {
		this.roleId = roleid;
		this.npcKey = npcKey;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, true);
		if (instzone == null) {
			return false;
		}
		
		if (!(instzone instanceof FactionInstZone)) {
			return false;
		}
		
		long battleid = Module.getInstance().getBattleid(instzone.getLandkey(), npcKey);
		if (battleid > 0) {
			BuffAgent agent = new BuffRoleImpl(roleId, true);
			if (!agent.canAddBuff(FireProp.getIntValue("battle", "mtgserv.battle_watchBuff"))) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, fire.pb.util.FireProp.getIntValue("message", "mtgserv.buffConflict"), null);
				return false;
			}
			new PSendWatchBattle(roleId, 0L, battleid).call();
		}
		
		return true;
	}

	
}

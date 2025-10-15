package fire.pb.instancezone;

import fire.pb.battle.BattleField;
import fire.pb.clan.ClanPositionType;
import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

public class PEndNpcBattle extends Procedure {

	private final long roleId;
	private final long npcKey;
	
	public PEndNpcBattle(final long roleid, final long npcKey) {
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
		
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, true);
		if (clanInfo == null) {
			MessageMgr.sendMsgNotify(roleId, 166136, null);
			return false;
		}
		
		xbean.ClanMemberInfo masterInfo = clanInfo.getMembers().get(roleId);
		if (masterInfo.getClanposition() != ClanPositionType.ClanMaster
				&& masterInfo.getClanposition() != ClanPositionType.ClanViceMaster) {
			MessageMgr.sendMsgNotify(roleId, 166136, null);
			return false;
		}
		
		long battleid = Module.getInstance().getBattleid(instzone.getLandkey(), npcKey);
		if (battleid > 0) {
			int result = BattleField.BATTLE_LOSE;
			new fire.pb.battle.PBattleEnd(battleid, result).call();
		}
		
		return true;
	}
}

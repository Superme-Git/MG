package fire.pb.instancezone;

import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.npc.SNpcShare;
import fire.pb.talk.MessageMgr;

public class PActNpcService extends mkdb.Procedure {

	private final long roleId;
	private final long npckey;
	private final SNpcShare npcshare;
	private final int serviceId;

	PActNpcService(long roleId, long npckey, SNpcShare npcshare, int serviceId) {
		this.roleId = roleId;
		this.npckey = npckey;
		this.npcshare = npcshare;
		this.serviceId = serviceId;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(
				roleId, false);
		if (instzone == null)
			return false;

		if (instzone instanceof FactionInstZone) {
			if (!Module.getInstance().checkDynamicNpcFree(
					instzone.getLandkey(), npckey)) {
				MessageMgr.psendMsgNotifyWhileRollback(roleId, 160185,
						npcshare.id, null);
				return false;
			}
		}
		instzone.actNpcService(roleId, npckey, npcshare.id, serviceId);
		return true;
	}
}

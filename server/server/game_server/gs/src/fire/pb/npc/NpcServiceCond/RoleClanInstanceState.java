package fire.pb.npc.NpcServiceCond;

import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.conf.InstanceZoneConfig;

public class RoleClanInstanceState implements Condition {

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		/*args1 instid*/
		int instid = args1;
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (null == clanInfo)
			return false;
		// 公会副本是否已经存在
		xbean.ClanInstances clanInst = xtable.Claninstances.select(clanInfo.getKey());
		if (clanInst == null)
			return false;
		
		InstanceZoneConfig zoneconfig = fire.pb.instancezone.Module.getInstance().getInstanceZoneConfigs().get(instid);
		if (zoneconfig == null)
			return false;
		
		final Long instanceKey = clanInst.getInstkey().get(instid);
		if (instanceKey != null)
			return true;
		
		if (zoneconfig.beforeZoneId != 0) {
			if (clanInst.getInststate().get(zoneconfig.beforeZoneId) != 1) {
				return false;
			}
		}
		
		return true;
	}

}

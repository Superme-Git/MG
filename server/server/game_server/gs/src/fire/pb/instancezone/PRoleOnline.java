package fire.pb.instancezone;

import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;

public class PRoleOnline extends mkdb.Procedure {
	final private long roleId;
	final private long sceneId;

	public PRoleOnline(long roleId, long sceneId) {
		this.roleId = roleId;
		this.sceneId = sceneId;
	}

	@Override
	protected boolean process() throws Exception {
		if (!Module.getInstance().map2Instzone.containsKey((int) sceneId))
			return true;
		Long landkey = xtable.Role2instancezone.select(roleId);
		if (landkey == null)
			return outInstanceZone();

		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(
				roleId, false);
		if (instzone == null)
			return outInstanceZone();

//		instzone = InstanceZoneFactory.getInstanceZone(landkey, false);
		instzone.enterWhileOnline(roleId, sceneId);
		return true;
	}

	private boolean outInstanceZone() {
		int mapid = (int) sceneId;
		Integer instid = Module.getInstance().map2Instzone.get(mapid);
		if (instid != null) {
			InstanceZoneConfig cfg = Module.getInstance()
					.getInstanceZoneConfigs().get(instid);
			Transfer.justGotoWhileCommit(roleId, cfg.outMapid, cfg.outPosx,
					cfg.outPosy, SRoleEnterScene.FORCE_GOTO);
		}
		return true;
	}

}

package fire.pb.instancezone.timer;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.conf.InstanceSaveConfig;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.GridPos;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import mkdb.Procedure;

public class PNextTimerProcess extends Procedure {
	private final int mapId;
	private final int posx;
	private final int posy;
	public final long landkey;

	public PNextTimerProcess(final int mapId, final int posx, final int posy,
			final long landkey) {
		this.mapId = mapId;
		this.posx = posx;
		this.posy = posy;
		this.landkey = landkey;
	}

	protected boolean process() throws Exception {
		InstanceZone.logger.debug("come on baby:" + landkey);

		InstanceZone zone = InstanceZoneFactory.getInstanceZone(landkey, true);
		if (null != zone) {
			int bossid = 0;
			int curactsave = zone.getCurActiveSave();
			if (curactsave > 0) {
				InstanceSaveConfig savecfg = zone.getZoneConfig().saves.get(curactsave);
				bossid = savecfg.haveboss;
			}
			
			int transType = SRoleEnterScene.CHEFU;
			if (bossid > 0) {
				transType = SRoleEnterScene.CLAN_GOTO;
			}
			
			for (long roleId : zone.getRoleIds()) {
				final Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
				if (team != null) {
					if (team.isTeamLeader(roleId)) {
						Role mapRole = RoleManager.getInstance().getRoleByID(roleId);
						if (mapRole != null) {
							GridPos gridPos = mapRole.getPos().toGridPos();
							DuplicateHelper.enterDynamicSceneWhileCommit(roleId,
									mapId, gridPos.getX(), gridPos.getY(), landkey, "", true,
									DynamicSceneType.NORMAL_SCENE,
									transType);
						}
						else {
							DuplicateHelper.enterDynamicSceneWhileCommit(roleId,
									mapId, posx, posy, landkey, "", true,
									DynamicSceneType.NORMAL_SCENE,
									transType);
						}
						
					} else {
						if (!team.isNormalMember(roleId)) {
							Role mapRole = RoleManager.getInstance().getRoleByID(roleId);
							if (mapRole != null) {
								GridPos gridPos = mapRole.getPos().toGridPos();
								DuplicateHelper.enterDynamicSceneWhileCommit(
										roleId, mapId, gridPos.getX(), gridPos.getY(), landkey, "",
										true, DynamicSceneType.NORMAL_SCENE,
										transType);
							}
							else {
								DuplicateHelper.enterDynamicSceneWhileCommit(
										roleId, mapId, posx, posy, landkey, "",
										true, DynamicSceneType.NORMAL_SCENE,
										transType);
							}
						}
					}
				} else {
					Role mapRole = RoleManager.getInstance().getRoleByID(roleId);
					if (mapRole != null) {
						GridPos gridPos = mapRole.getPos().toGridPos();
						DuplicateHelper.enterDynamicSceneWhileCommit(roleId, mapId,
								gridPos.getX(), gridPos.getY(), landkey, "", true,
								DynamicSceneType.NORMAL_SCENE,
								transType);
					}
					else {
						DuplicateHelper.enterDynamicSceneWhileCommit(roleId, mapId,
								posx, posy, landkey, "", true,
								DynamicSceneType.NORMAL_SCENE,
								transType);
					}
				}
				
				if (InstanceZone.logger.isInfoEnabled()) {
					InstanceZone.logger.info(new StringBuilder().append("公会副本进入下一层:")
							.append("mapid:").append(mapId)
							.append(",landkey:").append(landkey));
				}
			}
		}

		return true;
	};
}

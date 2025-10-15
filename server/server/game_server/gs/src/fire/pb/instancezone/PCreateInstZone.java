package fire.pb.instancezone;

import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class PCreateInstZone extends mkdb.Procedure {
	private final long roleId;
	private final int landId;
	private final long ownerId;
	private final boolean isGM;

	public PCreateInstZone(long roleId, int landId) {
		this.roleId = roleId;
		this.landId = landId;
		this.ownerId = 0;
		this.isGM = false;
	}

	public PCreateInstZone(long roleId, int landId, long ownerId, boolean isGM) {
		this.roleId = roleId;
		this.landId = landId;
		this.ownerId = ownerId;
		this.isGM = isGM;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.ClanInfo clanInfo = xtable.Clans.select(ownerId);
		if (clanInfo == null) {
			return false;
		}
		// 创建公会副本锁定
		xbean.ClanInstances clanCreateInst = xtable.Claninstances.get(clanInfo.getKey());
		if (clanCreateInst != null) {
			final Long instanceKey = clanCreateInst.getInstkey().get(landId);
			if (instanceKey != null) {
				//副本已经存在,直接进入
				InstanceZone oldzone = InstanceZoneFactory.getInstanceZone(instanceKey, true);
				if (null != oldzone) {
					if (!(oldzone instanceof FactionInstZone)) {
						return false;
					}
					
					pexecuteWhileCommit(new PEnterInstZoneByNpc(landId, instanceKey, roleId));
					return true;
				}
			}
		}
		
		InstanceZoneConfig zoneconfig = Module.getInstance().getInstanceZoneConfigs().get(landId);
		if (zoneconfig == null) {
			InstanceZone.logger.error("没找到副本配置文件！");
			return false;
		}
		
		//队伍和个人都能进入的公会副本
		if (zoneconfig.teamtype == InstanceZoneConfig.TEAM_TYPE_ALL) {
			Team team = TeamManager.getTeamByRoleId(roleId);
			if (team != null)
				lock(xtable.Locks.ROLELOCK, team.getNormalMemberIds());
		}
		// 创建地图
		InstanceZone instzone = InstanceZoneFactory.createInstanceZoneByRole(landId, roleId, ownerId, isGM);
		if (instzone == null) {
			InstanceZone.logger.error("角色[" + roleId + "]创建副本[" + landId + "]实例为空,失败！");
			return false;
		}
		
		InstanceZone.logger.info(new StringBuilder().append("角色[").append(roleId).append("]创建副本[")
				.append(landId).append("]创建公会副本成功").append(",landkey:").append(ownerId)
				.append(",公会名字:").append(clanInfo.getClanname())
				.append(",公会id:").append(clanInfo.getIndex()));
		return instzone.enter(roleId, false);
	}
}

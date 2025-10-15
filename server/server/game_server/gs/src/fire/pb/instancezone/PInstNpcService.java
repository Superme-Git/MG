package fire.pb.instancezone;

/**
 * 公会副本的Npc服务处理
 */

import java.util.ArrayList;
import java.util.List;

import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import mkdb.Procedure;

public class PInstNpcService extends Procedure {

	private final int serviceid;
	private final int instid;
	private final long roleid;
	
	private final boolean isGm;
	
	public PInstNpcService(final int instid, final int serviceid, final long roleid) {
		this.instid = instid;
		this.serviceid = serviceid;
		this.roleid = roleid;
		this.isGm = false;
	}
	
	public PInstNpcService(final int instid, final long roleid, final boolean isgm) {
		this.instid = instid;
		this.roleid = roleid;
		this.serviceid = ClanUtils.CLAN_INST_SERVICE_1;
		this.isGm = isgm;
	}
	
	protected boolean process() {
		// 拿到当前角色公会信息
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (null == clanInfo)
			return false;
		
		if (!isGm) {
			Integer claninstservice = clanInfo.getClaninstservice();
			if (claninstservice == 0) {
				if (serviceid != ClanUtils.CLAN_INST_SERVICE_1)
					return false;
			}
			else {
				if (serviceid != claninstservice)
					return false;
			}
		}

		// 拿队伍信息
		Team team = TeamManager.selectTeamByRoleId(roleid);
		if (team != null) {
			// 有队伍,判断队伍其他成员是否都和自己同一公会
			boolean isSameFaction = true;
			for (long memid : team.getNormalMemberIds()) {
				if (memid != roleid) {
					long memfactionid = ClanUtils.getClanIdByRoleId(roleid);
					if (memfactionid <= 0L || memfactionid != clanInfo.getKey()) {
						isSameFaction = false;
						break;
					}
				}
			}
			if (isSameFaction == false)
				return false;
		}
		
		long now = System.currentTimeMillis();
		InstanceZoneConfig zoneconfig = fire.pb.instancezone.Module.getInstance().getInstanceZoneConfigs().get(instid);
		if (!isGm) {
			if (!zoneconfig.inEnterTime(now)) {
				MessageMgr.psendMsgNotify(roleid, 160364, null);
				InstanceZone.logger.error("角色[" + roleid + "]不在副本[" + zoneconfig.instZoneId + "]进入时间,创建验证失败！");
				return false;
			}
			
			// 公会副本是否已经存在
			xbean.ClanInstances clanInst = xtable.Claninstances.select(clanInfo.getKey());
			// 对进度进行验证
			if (clanInst != null) {
				if (zoneconfig.beforeZoneId != 0) {
					if (!clanInst.getInstreset().containsKey(zoneconfig.beforeZoneId)) {
						InstanceZoneConfig afterzoneconfig = fire.pb.instancezone.Module.getInstance().getInstanceZoneConfigs().get(zoneconfig.beforeZoneId);
						List<String> para = new ArrayList<String>(2);
						para.add(afterzoneconfig.name);
						para.add(zoneconfig.name);
						MessageMgr.psendMsgNotify(roleid, 160301, para);
						return false;
					}
					
					if (clanInst.getInststate().get(zoneconfig.beforeZoneId) != 1) {
						InstanceZoneConfig afterzoneconfig = fire.pb.instancezone.Module.getInstance().getInstanceZoneConfigs().get(zoneconfig.beforeZoneId);
						List<String> para = new ArrayList<String>(2);
						para.add(afterzoneconfig.name);
						para.add(zoneconfig.name);
						MessageMgr.psendMsgNotify(roleid, 160301, para);
						return false;
					}
				}
			}
		}
		
		xbean.ClanInfo InfoclanCreate = ClanUtils.getClanInfoById(roleid, true);
		if (null == InfoclanCreate)
			return false;
		
		// 公会副本是否已经存在
		xbean.ClanInstances clanCreateInst = xtable.Claninstances.select(InfoclanCreate.getKey());
		do {
			if (clanCreateInst == null)
				break;
			
			final Long instanceKey = clanCreateInst.getInstkey().get(instid);
			if (instanceKey == null)
				break;
			
			InstanceZone oldzone = InstanceZoneFactory.getInstanceZone(instanceKey, true);
			if (null == oldzone)
				break;
			
			if (!(oldzone instanceof FactionInstZone)) {
				break;
			}
			
			if (((FactionInstZone)oldzone).getClanInstData().getInstreset().containsKey(instid)) {
				final long insttime = ((FactionInstZone)oldzone).getClanInstData().getInstreset().get(instid);
				if (!DateValidate.inTheSameDay(insttime, now))
					break;
			}
			// 进入已经创建的
			pexecuteWhileCommit(new PEnterInstZoneByNpc(instid, instanceKey, roleid));
			
			return true;
			
		} while (false);
		// 创建公会副本
		new PCreateInstZone(roleid, instid,	InfoclanCreate.getKey(), isGm).call();
		return true;
	}
}

package fire.pb.instancezone.faction;

import java.util.HashMap;
import java.util.Map;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.conf.InstanceSaveConfig;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Lockeys;
import mkdb.Procedure;

public class PGiveTongGuanAward extends Procedure {

	private final long landkey;
	private final int saveid;
	private final long clankey;
	
	public PGiveTongGuanAward(final long landkey, final int saveid, final long clankey) {
		this.landkey = landkey;
		this.saveid = saveid;
		this.clankey = clankey;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey, true);
		if (instzone == null)
			return false;
		
		if (instzone instanceof FactionInstZone) {
			//发通关奖励
			InstanceSaveConfig saveconf = instzone.getZoneConfig().saves.get(saveid);
			if (null == saveconf)
				return false;
			
			if (saveconf.pastaward > 0) {
				Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, instzone.getRoleIds()));
				for (Long roleid : instzone.getRoleIds()) {
					BagUtil.addItem(roleid, saveconf.pastaward, 1, "公会副本通关奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben, saveconf.pastaward);
					MessageUtil.psendAddItemWhileCommit(roleid, saveconf.pastaward, 1);
				}
				
				fire.pb.instancezone.Module.logger.info(new StringBuilder().append("发通关奖励:").append("公会key:").append(clankey).append(",landkey:")
						.append(landkey));
			}
			
			if (saveconf.pastexpaward > 0) {
				Map<String, Object> paras = new HashMap<String, Object>(10);
				paras.put(RewardMgr.SAVE_ID, saveid);
				paras.put(RewardMgr.FUBEN_ID, instzone.getZoneConfig().instZoneId);
				
				Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, instzone.getRoleIds()));
				for (Long roleid : instzone.getRoleIds()) {
					int teamnum = 1;
					Team team = TeamManager.selectTeamByRoleId(roleid);
					if (team == null) {
						teamnum = 1;
					}
					else {
						teamnum = team.getNormalMemberIds().size();
					}
					
					paras.put(RewardMgr.TEAM_NUM, teamnum);
					
					fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(
							roleid, saveconf.pastexpaward, paras,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben, saveconf.pastexpaward,
							PAddExpProc.INST_ZONE, instzone.getZoneConfig().name);
				}
				
				fire.pb.instancezone.Module.logger.info(new StringBuilder().append("发通关经验奖励:").append(clankey));
			}
			
		}
		return true;
	}
	
	

}

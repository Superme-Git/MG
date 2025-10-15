package fire.pb.instancezone.faction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.conf.InstanceSaveConfig;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SNpcShare;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Lockeys;
import mkdb.Procedure;

public class PGiveBossAward extends Procedure {

	private final long roleid;
	private final long landkey;
	private final int saveid;
	
	public PGiveBossAward(final long roleid, final long landkey,
			final int saveid) {
		this.roleid = roleid;
		this.landkey = landkey;
		this.saveid = saveid;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey, true);
		
		List<Long> allRoleIDs = new LinkedList<Long>();
		Team team = TeamManager.getTeamByRoleId(roleid);
		if (team == null) {
			allRoleIDs.add(roleid);
		}
		else {
			allRoleIDs.addAll(team.getNormalMemberIds());
		}
		
		if (instzone instanceof FactionInstZone) {
			Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
			xbean.ClanInstances clanInst = ((FactionInstZone) instzone).getClanInstData();
			
			//发最后一击的奖励
			InstanceSaveConfig savecfg = instzone.getZoneConfig().saves.get(saveid);
			if (savecfg == null)
				return true;
			
			if (savecfg.mendaward > 0) {
				for (Long rid : allRoleIDs) {
					BagUtil.addItem(rid, savecfg.mendaward, 1, "最后一击奖励",
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben, savecfg.mendaward);
					MessageUtil.psendAddItemWhileCommit(rid, savecfg.mendaward, 1);
				}
				
				SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(savecfg.haveboss);
				if (npc == null)
					return true;
				String npcname = npc.getName();
				String rolename = xtable.Properties.selectRolename(roleid);
				
				if (team == null) {
					List<String> paras = new ArrayList<String>();
					paras.add(rolename);
					paras.add(npcname);
					
					xbean.ClanInfo claninfo = xtable.Clans.select(clanInst.getClanid());
					if (claninfo != null) {
						MessageMgr.psendMsgNotifyWhileCommit(claninfo.getMembers().keySet(), 166101, 0, paras);
					}
				}
				else {
					List<String> paras = new ArrayList<String>();
					paras.add(rolename);
					paras.add(npcname);
					
					xbean.ClanInfo claninfo = xtable.Clans.select(clanInst.getClanid());
					if (claninfo != null) {
						MessageMgr.psendMsgNotifyWhileCommit(claninfo.getMembers().keySet(), 166102, 0, paras);
					}
				}
			}
		}
	
		return true;
	}

}

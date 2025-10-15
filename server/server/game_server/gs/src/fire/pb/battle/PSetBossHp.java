package fire.pb.battle;

import java.util.LinkedList;
import java.util.List;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.instancezone.faction.PGiveBossAward;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Lockeys;
import mkdb.Procedure;

public class PSetBossHp extends Procedure {
	private final long instkey;
	private final long roleid;
	private final int battleid;
	private final long changehp;
	
	public PSetBossHp(final long instkey, final long roleid, final int battleid, final long changehp) {
		this.instkey = instkey;
		this.roleid = roleid;
		this.battleid = battleid;
		this.changehp = changehp;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(instkey, false);
		if (null == instzone) {
			return false;
		}
		
		if (((FactionInstZone) instzone).checkBattleid(battleid)) {
			xbean.ClanInstances clanInst = ((FactionInstZone) instzone).getClanInstData();
			xbean.ClanBossInfo bossinfo = clanInst.getBossinfo().get(battleid);
			
			long hp = bossinfo.getHp();
			if (hp <= 0) {
				fire.pb.instancezone.Module.logger.info(new StringBuilder().append("landkey:").append(instkey).append(",最后一击后又发生了攻击:").append(roleid));
				return false;
			}
			
			hp -= changehp;
			bossinfo.setHp(hp);
			if (hp <= 0) {
				bossinfo.setHp(0);
				int saveid = instzone.getCurActiveSave();
				//发奖励
				List<Long> allRoleIDs = new LinkedList<Long>();
				Team team = TeamManager.getTeamByRoleId(roleid);
				if (team == null) {
					allRoleIDs.add(roleid);
				}
				else {
					allRoleIDs.addAll(team.getNormalMemberIds());
				}
				
				Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
				((FactionInstZone)instzone).updateSubSaveByNpcKey(allRoleIDs, bossinfo.getBossnpckey(), saveid, 0, 0);
				
				//最后一击奖励
				Procedure.pexecuteWhileCommit(new PGiveBossAward(roleid, instkey, saveid));
				fire.pb.instancezone.Module.logger.info(new StringBuilder().append("landkey:").append(instkey).append(",最后一击产生了:").append(roleid));
			}
			
			//给场景的角色同步血量
			pexecuteWhileCommit(new fire.pb.battle.PSynchroBossHp(
					bossinfo.getBossmonsterid(),
					roleid,
					bossinfo.getMaxhp(),
					bossinfo.getHp(),
					(byte)1, changehp));
			
			float hpcent = (float) bossinfo.getHp() / (float) bossinfo.getMaxhp();
			long starttime = instzone.getStartTime();
			
			int saveid = instzone.getCurActiveSave();
			if (saveid >= instzone.getZoneConfig().cdsave) {
				saveid = instzone.getZoneConfig().cdsave - 1;
			}
			
			fire.pb.ranklist.proc.PFactionProgressUpdateProc clanprogressupdateproc = new fire.pb.ranklist.proc.PFactionProgressUpdateProc(
					instzone.getZoneBean().getOwnerid(), saveid, instzone.getZoneConfig().instZoneId,
					starttime, hpcent, instzone.getZoneConfig().name);
			
			mkdb.Procedure.pexecuteWhileCommit(clanprogressupdateproc);	
		}
		
		return true;
	}
	
}

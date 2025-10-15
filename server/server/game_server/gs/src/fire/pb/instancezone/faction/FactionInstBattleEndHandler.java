package fire.pb.instancezone.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mkdb.Lockeys;
import mkdb.Procedure;
import fire.msp.role.GSetNpcBattleInfo;
import fire.pb.GsClient;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.BattleType;
import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.Module;
import fire.pb.instancezone.PDeleteRole2Inst;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.map.DuplicateHelper;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class FactionInstBattleEndHandler extends BasicEndHandler {
	private final long roleid;
	private final long npckey;
	private final int awardid;
	private final long landkey;
	private final int saveid;
	private final InstanceZoneConfig zoneconfig;
	
	public FactionInstBattleEndHandler(final long roleid, final long npckey, final int awardid, final long landkey, final InstanceZoneConfig zoneconfig, final int saveid) {
		this.roleid = roleid;
		this.npckey = npckey;
		this.awardid = awardid;
		this.landkey = landkey;
		this.zoneconfig = zoneconfig;
		this.saveid = saveid;
	}

	@Override
	public boolean handleDeath() {
		super.handleDeath();
		
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey, true);
		if (instzone == null)
			return false;
		if (instzone.getZoneState() == xbean.InstanceZone.STATE_CLOSING) {
			
			transformRoleids.add(roleid);
			
			List<Long> allRoleIDs = new ArrayList<Long>();
			Team team = TeamManager.selectTeamByRoleId(roleid);
			if (team == null) {
				allRoleIDs.add(roleid);
			}
			else {
				allRoleIDs.addAll(team.getAllMemberIds());
			}
			
			Procedure.pexecuteWhileCommit(new PDeleteRole2Inst(allRoleIDs));
			return true;
		}
		
		if (battle.getBattleresult() == BattleField.BATTLE_LOSE || battle.getBattleresult() == BattleField.BATTLE_DRAW) {
			Module.getInstance().setDynamicNpcState(landkey, npckey, 0);
			GsClient.pSendWhileCommit(new GSetNpcBattleInfo(npckey, 0));
		}
		/*else if (battle.getBattleresult() == BattleField.BATTLE_WIN) {
			if (battle.getBattletype() == BattleType.BATTLE_CLANBOSS) {
				Procedure.pexecuteWhileCommit(new PGiveBossAward(roleid, landkey, saveid));
			}
		}*/
		
		return true;
	}
	
	@Override
	protected void transformAfterBattle() {
		if (transformRoleids.isEmpty())
			return;

		for (long roleid : transformRoleids) {
			transferRoleOutZone(roleid);
		}
	}

	@Override
	protected boolean handleAward() {
		List<Long> allRoleIDs = new LinkedList<Long>();
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey, false);
		
		Team team = TeamManager.getTeamByRoleId(roleid);
		if (team == null) {
			allRoleIDs.add(roleid);
		}
		else {
			allRoleIDs.addAll(team.getNormalMemberIds());
		}
		
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
		if (instzone == null) {
			Map<String, Object> paras = new HashMap<String, Object>(10);
			paras.put(RewardMgr.SAVE_ID, saveid);
			paras.put(RewardMgr.TEAM_NUM, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
			paras.put(RewardMgr.FUBEN_ID, zoneconfig.instZoneId);
			
			for (Long rid : allRoleIDs) {
				fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(
						rid, awardid, paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben, awardid,
						PAddExpProc.INST_ZONE, zoneconfig.name);
			}
		}
		else {
			if (battle.getBattletype() != BattleType.BATTLE_CLANBOSS) {
				return ((FactionInstZone)instzone).updateSubSaveByNpcKey(allRoleIDs, npckey, saveid, 0, 0);
			}
		}
		
		return true;
	}
	
	public void transferRoleOutZone(final long roleId) {
		Long clankey = xtable.Properties.selectClankey(roleId);
		if (clankey == null || clankey <= 0) {
			MessageMgr.sendMsgNotify(roleId, 145077, null);
			return;
		}

		final Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
		if (team != null) {
			if (team.isTeamLeader(roleId)) {
				DuplicateHelper.enterDynamicSceneWhileCommit(roleId, ClanUtils.MAPID,
						zoneconfig.outPosx, zoneconfig.outPosy, clankey, "", -1,
						DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CLAN_GOTO, null);
			}
			else if (team.isAbsentMember(roleId)) {
				DuplicateHelper.enterDynamicSceneWhileCommit(roleId, ClanUtils.MAPID,
						zoneconfig.outPosx, zoneconfig.outPosy, clankey, "", -1,
						DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CLAN_GOTO, null);
			}
		} else {
			DuplicateHelper.enterDynamicSceneWhileCommit(roleId, ClanUtils.MAPID,
					zoneconfig.outPosx, zoneconfig.outPosy, clankey, "", -1,
					DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CLAN_GOTO, null);
		}
	}
}

package fire.pb.instancezone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import fire.pb.battle.BattleField;
import fire.pb.battle.PBattleEnd;
import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.timer.InstDestroyTimer;
import fire.pb.map.DuplicateHelper;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import mkdb.Procedure;

public class PTransOutRole extends Procedure {
	private final long landkey;
//	private final List<Long> roles;
	private final int posx;
	private final int posy;
	
	public PTransOutRole(final long landkey, /*final List<Long> roles,*/
			final int posx,
			final int posy) {
		this.landkey = landkey;
//		this.roles = roles;
		this.posx = posx;
		this.posy = posy;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey, true);
		if (instzone == null)
			return false;
		
		Set<Long> inbattleid = new HashSet<Long>();
		Set<Long> transOuts = new HashSet<Long>();
		
		for (Long roleid : instzone.getRoleIds())
		{
			Long battleId = xtable.Roleid2battleid.select(roleid);
			if (battleId == null) {
				transOuts.add(roleid);
				transferRoleOutZone(roleid);
			}
			else {
				inbattleid.add(battleId);
			}
		}
		
		for (Long battleId : inbattleid) {
			Procedure.pexecuteWhileCommit(new PBattleEnd(battleId, BattleField.BATTLE_DRAW));
		}
		
		if (transOuts.size() != 0) {
			List<Long> transrole = new ArrayList<Long>(transOuts.size());
			transrole.addAll(transOuts);
			Procedure.pexecuteWhileCommit(new PDeleteRole2Inst(transrole));
		}
		
		//创建定时器删除副本地图信息 FIXME 改成1分钟,不管里面的战斗有没有打完,都结束了
		mkdb.Executor.getInstance().schedule(new InstDestroyTimer(landkey), 60, TimeUnit.SECONDS);
		
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
						posx, posy, clankey, "", -1,
						DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CLAN_GOTO, null);
			}
			else if (team.isAbsentMember(roleId)) {
				DuplicateHelper.enterDynamicSceneWhileCommit(roleId, ClanUtils.MAPID,
						posx, posy, clankey, "", -1,
						DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CLAN_GOTO, null);
			}
		} else {
			DuplicateHelper.enterDynamicSceneWhileCommit(roleId, ClanUtils.MAPID,
					posx, posy, clankey, "", -1,
					DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CLAN_GOTO, null);
		}
	}
}

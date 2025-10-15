package fire.pb.instancezone;

import java.util.ArrayList;
import java.util.List;

import fire.pb.battle.SSynchroBossHp;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.instancezone.conf.InstanceSaveConfig;
import fire.pb.instancezone.conf.NpcConfig;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PSynchroBossHp extends Procedure {
	public final long roleid;

	public PSynchroBossHp(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(
				roleid, true);
		if (instzone != null) {
			if (instzone instanceof FactionInstZone) {
				int curactsave = instzone.getCurActiveSave();
				InstanceSaveConfig savecfg = instzone.getZoneConfig().saves.get(curactsave);
				int bossid = savecfg.haveboss;

				NpcConfig npcConfig = instzone.getZoneConfig().npcs.get(bossid);
				if (npcConfig == null)
					return false;

				List<Long> normalRoleIds = new ArrayList<Long>();
				// 检查队伍情况
				Team team = TeamManager.getTeamByRoleId(roleid);
				if (team != null) {// 有队伍
					if (team.isTeamLeader(roleid))
						normalRoleIds.addAll(team.getNormalMemberIds());
					else if (team.isAbsentMember(roleid))
						normalRoleIds.add(roleid);
				} else {
					normalRoleIds.add(roleid);
				}

				ActionConfig actcfg = instzone.getZoneConfig().acts.get(npcConfig.actid);
				if (actcfg == null)
					return false;

				if (actcfg.type != ActionConfig.TYPE_BATTLE)
					return false;

				Integer battleid = (Integer) actcfg.params.get(ConfigParamName.BATTLE_ID);
				if (((FactionInstZone) instzone).checkBattleid(battleid)) {
					xbean.ClanInstances clanInst = ((FactionInstZone) instzone).getClanInstData();
					xbean.ClanBossInfo bossinfo = clanInst.getBossinfo().get(battleid);
					if (bossinfo.getHp() > 1) {
						// 给场景的角色同步血量
						new fire.pb.battle.PSynchroBossHp(
								bossinfo.getBossmonsterid(), roleid,
								bossinfo.getMaxhp(), bossinfo.getHp(), (byte) 1, 0).call();
					}
				} else {
					for (Long eroleid : normalRoleIds) {
						SSynchroBossHp send = new SSynchroBossHp();
						send.bossmonsterid = bossid;
						send.maxhp = 100;
						send.hp = 100;
						send.flag = 0;
						send.rolename = "";
						send.changehp = 0;

						mkdb.Procedure.psendWhileCommit(eroleid, send);
					}
				}
			}
		}

		return true;
	}

}

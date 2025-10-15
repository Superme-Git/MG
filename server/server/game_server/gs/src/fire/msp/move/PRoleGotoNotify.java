package fire.msp.move;

import fire.pb.battle.watch.PEndWatchBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.move.SRoleEnterScene;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.team.TeamMemberState;

public class PRoleGotoNotify extends mkdb.Procedure
{
	final private long roleId;
	final private int gototype;
	final private long oldscene;
	final private long newscene;
	

	PRoleGotoNotify(long roleId,int gototype, long oldscene, long newscene)
	{
		this.roleId = roleId;
		this.gototype = gototype;
		this.oldscene = oldscene;
		this.newscene = newscene;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		if (xtable.Role2instancezone.select(roleId) != null) {// 当前在副本中
			InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, false);
			if (instzone != null)
				instzone.afterGoto(roleId, oldscene, newscene);
			else {
				xtable.Role2instancezone.remove(roleId);
			}
		}
		
		Long clanfightid = xtable.Roleid2clanfightid.select(roleId);
		if (clanfightid != null) //如果在战场中 by changhao
		{
			Team team = TeamManager.getTeamByRoleId(roleId); //锁定队伍 by changhao
			fire.pb.clan.fight.ClanFightBattleField zone = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
			if (zone != null)
			{
				zone.afterGoto(roleId, oldscene, newscene, true);
			}
			else
			{
				xtable.Roleid2clanfightid.remove(roleId);					
			}
			
			if (xtable.Role2instancezone.select(roleId) != null)
			{
				fire.pb.clan.fight.ClanFightBattleField.logger.error("角色(ID=" + roleId + ")身上残留公会副本状态");					
			}
		}
		
		//final fire.pb.map.Role mapRole =
		//		fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (gototype == SRoleEnterScene.GM_HOLD)
		{
			MessageMgr.psendMsgNotify(roleId, 141828 , null);
		}
		else if (gototype == SRoleEnterScene.FORCE_GOTO)
		{
			Team team = TeamManager.getTeamByRoleId(roleId);
			if (team == null)
				return true;
			if (team.isTeamLeader(roleId))
			{// 是队长，正常队员都暂离
				for (long memberrid : team.getNormalMemberIds())
					team.setTeamMemberStateWithSP(memberrid, TeamMemberState.eTeamAbsent);
			} else if (team.isNormalMember(roleId))
			{// 是正常队员，自己暂离
				team.setTeamMemberStateWithSP(roleId, TeamMemberState.eTeamAbsent);
			}
			// 强制跳转的特殊处理
			BuffAgent agent = new BuffRoleImpl(roleId);
			/*
			 * if(agent.existState(BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			 * //战斗中，战斗中不可能出现任何跳转后的notify，战斗内跳转在之前就已经处理了 }
			 */
			if (agent.existState(BuffConstant.StateType.STATE_BATTLE_WATCHER))
			{
				// 观战中
				new PEndWatchBattle(roleId).call();
			}

			if (agent.existBuff(BuffConstant.StateType.STATE_REPLAY))
			{	// 回放
				agent.removeCBuff(BuffConstant.StateType.STATE_REPLAY);
			}
			
			if (agent.existBuff(BuffConstant.CONTINUAL_PLAY_CG))
			{
				// 播动画
				agent.removeCBuff(BuffConstant.CONTINUAL_PLAY_CG);
			}

		} else if (gototype == SRoleEnterScene.INSTANCE) {
			new fire.pb.mission.instance.PArriveInstanceMap(roleId, newscene).call();
		} else if (gototype == SRoleEnterScene.ENTER_LINE_SCENCE) {
			new fire.pb.mission.instance.line.PArriveLineMap(roleId, newscene).call();
		} else if (gototype == SRoleEnterScene.CLAN_GOTO) {
			new fire.pb.instancezone.PSynchroBossHp(roleId).call();
		} else if (gototype == SRoleEnterScene.BING_FENG_GOTO) {
			fire.pb.instancezone.bingfeng.BingFengLandMgr.getInstance().sendAfterEnterBingFengLand(roleId, false);
		}
		
		return true;
	}
	
}

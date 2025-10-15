package fire.pb.team;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.OperateType;
import fire.pb.event.LeaveTeamSpecialQuestEvent;
import fire.pb.talk.MessageMgr;


/***
 * 离开队伍
 * @author changhao
 *
 */
public class PQuitTeamProc extends mkdb.Procedure{
	
	private final long leverRoleId;
	
	public PQuitTeamProc(long roleId){
		this.leverRoleId = roleId;
	}

	@Override/*加锁顺序battle->team->roleid->match by changhao*/
	protected boolean process()
	{		
		Team team = null;
		Long leverTeamId = xtable.Roleid2teamid.select(leverRoleId);
		if (leverTeamId == null)
		{
			return true;
		}
		
		xbean.BattleInfo battleinfo = null;
		Long battleid = null;
		if (fire.pb.buff.Module.existState(leverRoleId, BuffConstant.StateType.STATE_BATTLE_WATCHER)) //如果在观战中退队 by changhao
		{
			battleid = xtable.Watcherid2battleid.select(leverRoleId); //观看的战斗 by changhao
		}
		else
		{
			battleid = xtable.Roleid2battleid.select(leverRoleId); //如果在战斗 by changhao		
		}
		
		if (battleid != null)
		{
			 battleinfo = xtable.Battle.get(battleid);
		}
		
		xbean.TeamInfo teamInfo = xtable.Team.get(leverTeamId);
		if(teamInfo == null)
			return false;
		 team = new Team(leverTeamId,false);

		if(!team.isInTeam(leverRoleId)) //如果不在队伍中直接返回 by changhao
			return true;
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getAllMemberIdSet()));//锁队伍内所有人

		if (checkPvP(leverRoleId, team.isTeamLeader(leverRoleId)) != 0) {
			return true;
		}

		if(checkRoleIsLeader(leverRoleId, team))
		{
			//离开者是队长
			TeamManager.logger.debug("INFO:离开者是队长,TeamId: "+leverTeamId);
			if(!checkLeadLeverStatus(leverRoleId))
			{
				//队长离开者处于不能离队状态（飞行，战斗等）(illegal)
				TeamManager.logger.info("FAIL:队长离开者处于不能离队状态,TeamId: "+leverTeamId);
			}
			else//else if(checkTeamHasOnlineMembers(team))
			{
				//被动交换队长 by changhao
				if(team.passiveSwitchLeaderWithSP(-1))
				{
					//队伍中还存在其他在线成员，删除原队长现队员信息
					team.removeTeamMemberWithSP(leverRoleId,true);
					MessageMgr.psendMsgNotifyWhileCommit(team.getAllMemberIdSet(), 141198,0,MessageMgr.getStringList(xtable.Properties.selectRolename(leverRoleId)));
					team.refreshAllAppliersWithSendProtocol(team.getTeamInfo().getTeamleaderid());
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(leverRoleId, 141049, null);
					TeamManager.logger.info("SUCC:队伍中还存在其他在线成员,交换队长,再删除原队长，TeamId: "+leverTeamId);
				}
				else
				{
					//队伍中不存在其他在线成员，解散队伍
					PDisMissTeam pDisMissTeam =new PDisMissTeam(team.teamId,PDisMissTeam.REASON_LEADER_QUIT);
					mkdb.Procedure.pexecute(pDisMissTeam);
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(leverRoleId, 141049, null);
					TeamManager.logger.info("SUCC:队伍中不存在其他在线成员,解散队伍,TeamId: "+leverTeamId);
				}
				
			}
		}
		else
		{
			//离开者不是队长 by changhao
			TeamManager.logger.info("INFO:离开者不是队长,TeamId: "+leverTeamId);
			if(!checkNormalLeverStatus(leverRoleId))
			{
				if (battleinfo != null) //在战斗状态或观战中中,等战斗结束退队 by changhao
				{
					battleinfo.getAbsentteamroleids().remove(leverRoleId);
					battleinfo.getQuitteamroleids().add(leverRoleId); // 在战斗中退队等到战斗结束 by changhao						
				}
			}
			else
			{
				//从队伍中删除离队者
				team.removeTeamMemberWithSP(leverRoleId,true);
				MessageMgr.psendMsgNotifyWhileCommit(team.getAllMemberIdSet(), 141198,0,MessageMgr.getStringList(xtable.Properties.selectRolename(leverRoleId)));
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(leverRoleId, 141049, null);
				TeamManager.logger.info("SUCC:从队伍中删除离队者,RoleId: "+leverRoleId);
			}
		}
		
		fire.pb.event.Poster.getPoster().dispatchEvent(new LeaveTeamSpecialQuestEvent(leverRoleId));
		
		return true;
	}

	// 检测PVP
	private static int checkPvP(long leverRoleId, boolean isLeader) {
		// 退出队伍
		return fire.pb.battle.pvp.PvPTeamHandle.onQuitTeamProc(leverRoleId, isLeader);
	}

	//离队者是队长？
	private boolean checkRoleIsLeader(long leverRoleId, Team team)
	{
		//xtable.Roleid2teamid.get(leverRoleId);
		if(team.getTeamInfo().getTeamleaderid() == leverRoleId)
			return true;
		else 
			return false;
	}
	
	//队长离开者处于可以离队的状态?
	private boolean checkLeadLeverStatus(long leverRoleId)
	{
		BuffAgent agent = new BuffRoleImpl(leverRoleId);
		return agent.canAddBuff(OperateType.TEAM_QUIT);
	}
	
	
	//队员离开者处于可以离队的状态?
	private boolean checkNormalLeverStatus(long leverRoleId)
	{
		//队员不能离队:战斗状态
		//还有哪些情况不能离队
		BuffAgent agent = new BuffRoleImpl(leverRoleId);
		return agent.canAddBuff(OperateType.TEAM_QUIT);
	}
	
	/**
	 * 交换队长并移除，不需要往外发送协议 
	 * 可能失败，没有其他在线成员，外面需要判断是解散队伍还是不换队长
	 * 
	 * @return true表示交换队长成功，fasle表示队伍中已经没有其他在线成员，交换队长失败
	 */
	public boolean switchTeamLeader(Team team)
	{
		xbean.TeamInfo teamInfo = team.getTeamInfo();
		if(teamInfo.getMembers().size() == 0 || teamInfo.getMembers().get(0).getState() == TeamMemberState.eTeamFallline)
			return false;//第一个队员处于离线状态，后面的都是离线状态,无法交换队长
		long newLeaderId = teamInfo.getMembers().get(0).getRoleid();
		
		teamInfo.setSwitchleaderid(-1);//不管是主动还是被动交换队长后，都需要清这个状态
		long oldLeaderId = teamInfo.getTeamleaderid();
		
		//前队长构建新的队员
		xbean.TeamMember newMember = xbean.Pod.newTeamMember();
		newMember.setRoleid(oldLeaderId);
		newMember.setState(TeamMemberState.eTeamAbsent);
		
		for(xbean.TeamMember member : teamInfo.getMembers())
		{//设置所有归队中状态为暂离状态
			if(member.getState() == TeamMemberState.eTeamReturn)
				member.setState(TeamMemberState.eTeamAbsent);
		}
		
		//添加新队长,原队长放在新队长处
		teamInfo.setTeamleaderid(newLeaderId);
		teamInfo.getMembers().set(0,newMember);
		if(oldLeaderId == teamInfo.getCommanderroleid())
			team.SetCommanderRoleId(newLeaderId);
		return true;
	}
	
	

}

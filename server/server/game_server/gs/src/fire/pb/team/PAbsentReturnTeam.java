package fire.pb.team;

import fire.pb.PropRole;
import fire.pb.battle.watch.PSendWatchBattle;
import fire.pb.event.ArriveTeamSpecialQuestEvent;
import fire.pb.event.LeaveTeamSpecialQuestEvent;

/***
 * 暂离回归队伍
 * @author changhao
 *
 */
public class PAbsentReturnTeam extends mkdb.Procedure
{
	private final long memberRoleId;
	private final long absent; //1是暂离0是回归 by changhao
	
	public PAbsentReturnTeam(long roleid, int absent)
	{
		this.memberRoleId = roleid;
		this.absent = absent;
	}
	
	@Override  /*加锁顺序 team->roleid by changhao */
	protected boolean process() throws Exception
	{		
		Team team;
 
		Long teamId = xtable.Roleid2teamid.select(memberRoleId);
		
		xbean.BattleInfo battleinfo = null;	
		//先验证队伍是否为空 by changhao
		if(teamId != null)
		{
			Long battleid = xtable.Roleid2battleid.select(memberRoleId); //如果在战斗我们要先锁battle by changhao
			if (battleid != null)
			{
				battleinfo = xtable.Battle.get(battleid);
				if (absent == 0)
					fire.pb.talk.MessageMgr.psendMsgNotify(memberRoleId, 162135, null);
				else
					fire.pb.talk.MessageMgr.psendMsgNotify(memberRoleId, 162141, null);
				
				return true;
			}
			
			xbean.TeamInfo teaminfo = xtable.Team.get(teamId);
			if (teaminfo == null)
			{
				return false;
			}
			
			team = new Team(teamId,false);
		}
		else
			return true;
		
		if(!team.isInTeam(memberRoleId))
			return true;//验证该角色是否还在队伍中 by changhao
		
		long leaderRoleId = team.getTeamInfo().getTeamleaderid();
		
		final java.util.List<Long> roleids = new java.util.ArrayList<Long>();
		roleids.add(leaderRoleId);
		roleids.add(memberRoleId);
		this.lock(xtable.Locks.ROLELOCK, roleids);
		
		if(!TeamManager.getInstance().checkOnline(memberRoleId)) //检查是否在线 by changhao
		{
			TeamManager.logger.info("FAIL:成员不在线 , memberRoleId" +memberRoleId);
		}
		else
		{
			if(absent == 1) //如果是要暂离 by changhao
			{
				if(team.getTeamMemberState(memberRoleId) == TeamMemberState.eTeamAbsent)
				{
					//队员已经处于暂离中(illegal)
					TeamManager.logger.info("FAIL:队员已经处于暂离中 , memberRoleId" +memberRoleId);
				}
				else if(!TeamManager.getInstance().checkMemberAbsentStatusValid(memberRoleId))
				{
					if (battleinfo != null) //在战斗状态中,等战斗结束暂离 by changhao
					{
						battleinfo.getQuitteamroleids().remove(memberRoleId);
						battleinfo.getAbsentteamroleids().add(memberRoleId); // 在战斗中退队等到战斗结束 by changhao
						fire.pb.talk.MessageMgr.psendMsgNotify(memberRoleId, 160035, null);
					}
					else
					{
						//成员处在不可暂离队伍的状态（战斗等）(illegal) by changhao
						TeamManager.logger.info("FAIL:成员处在不可暂离队伍的状态 , memberRoleId" +memberRoleId);					
					}
				} 
				else
				{
					if (team.isTeamLeader(memberRoleId)) //如果队长暂离交出队长 by changhao
					{
						if (team.allMemberAbsent()) //所有成员都在暂离队长不能暂离了 by changhao
						{
							TeamManager.logger.info("CAbsentReturnTeam:SUCC:所有成员都在暂离队长不能暂离了");
							return true;
						}
						
						if(team.passiveSwitchLeaderWithSP(TeamMemberState.eTeamAbsent))
						{
							TeamManager.logger.info("CAbsentReturnTeam:SUCC:队长暂离交换队长，TeamId: ");
						}								
					}
					else
					{
						//改变队员状态，并群发更新状态协议 by changhao
						team.setTeamMemberStateWithSP(memberRoleId, TeamMemberState.eTeamAbsent);
						
						//排序并广播队员新顺序 by changhao
						if(team.getTeamInfo().getSwitchleaderid() == memberRoleId)
						{
							team.getTeamInfo().setSwitchleaderid(0);
							mkdb.Procedure.psendWhileCommit(team.getTeamInfo().getTeamleaderid(), new STeamError(TeamError.RefuseChangeLeader));
						}
						
						TeamManager.getInstance().delTeamMatchAsyn(memberRoleId); //删除队长对应的队伍匹配 by changhao
													
						TeamManager.logger.info("SUCC:成员暂离队伍 , memberRoleId" +memberRoleId);								
					}
				}
				
				fire.pb.event.Poster.getPoster().dispatchEvent(new LeaveTeamSpecialQuestEvent(memberRoleId));
			}
			else
			{
				/*
				PropRole leaderprole = new PropRole(leaderRoleId, true);
				if(leaderprole.getProperties().getCruise() > 0) {
					TeamManager.logger.debug("队长的巡游状态,此时不能召回暂离队员,teamId: " + teamId);
					fire.pb.talk.MessageMgr.sendMsgNotify(memberRoleId, 160434, null);
					return true;
				}
				*/
				
				//请求回归 by changhao
				if(team.getTeamMemberState(memberRoleId) != TeamMemberState.eTeamAbsent
						&&team.getTeamMemberState(memberRoleId) != TeamMemberState.eTeamReturn)
				{
					//队员不处于暂离中(illegal) by changhao
					TeamManager.logger.info("FAIL:队员不处于暂离中 , memberRoleId" +memberRoleId);
				}
				else if(!TeamManager.getInstance().checkMemberReturnStatusValid(memberRoleId))
				{
					//成员处在不可归队的状态（战斗等）(illegal) by changhao
					TeamManager.logger.info("FAIL:成员处在不可归队的状态 , memberRoleId" +memberRoleId);
					fire.pb.talk.MessageMgr.psendMsgNotify(memberRoleId, 141412, null);
				}
				else
				{
					if(team.isMemberInReturnScale(memberRoleId)) //检查看是否在同一张地图等 by changhao
					{
						//在回归范围之内 by changhao
						//如果队长在战斗或观战状态中，则进入归队中状态 by changhao
						if(TeamManager.getInstance().checkTeamInReturnState(team))
						{
							//队伍处在战斗或观战等状态中 设置其为归队状态 by changhao
							team.setTeamMemberStateWithSP(memberRoleId, TeamMemberState.eTeamReturn);
							fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit( memberRoleId, 143867 , null);
							//归队中状态不需要  排序并广播队员新顺序
							//team.broadcastMemberSequence();
							TeamManager.logger.debugWhileCommit("SUCC:成员回归队伍 , memberRoleId" +memberRoleId);
							
							if (TeamManager.getInstance().checkTeamInFightState(team)) //如果队长在战斗中,队员进入观察列表 by changhao
							{
								mkdb.Procedure.pexecuteWhileCommit(new PSendWatchBattle(memberRoleId,team.getTeamLeaderId()));			
							}
						}
						else
						{
							if (TeamManager.getInstance().checkTeamReturnStatusValid(team))
							{
								// 队伍处在可以归队的状态
								// 改变队员为正常状态，并群发更新状态协议
								team.setTeamMemberStateWithSP(memberRoleId, TeamMemberState.eTeamNormal);
								fire.pb.event.Poster.getPoster().dispatchEvent(new ArriveTeamSpecialQuestEvent(team.getTeamLeaderId(), memberRoleId));
								// 排序并广播队员新顺序
//								team.updateMemberSequenceWithSendProtocol();
								TeamManager.logger.debugWhileCommit("SUCC:队伍处在可以归队的状态，改变队员为正常状态 , memberRoleId" + memberRoleId);
							}
							else
							{
								TeamManager.logger.info("FAIL: 队伍处于不可归队的状态，teamId" +teamId);
								return true;
							}
						}								
					}
					else
					{
						//在回归范围之外
						//TODO 自动寻路找队长  OR  返回不能回归的消息
						psend(memberRoleId, new STeamError(TeamError.TooFar));
						fire.pb.talk.MessageMgr.psendMsgNotify(memberRoleId, 141205, null);
						TeamManager.logger.info("FAIL:在回归范围之外 , memberRoleId" +memberRoleId);
					}
				}
			}
		}
		
		return true;
	}
}

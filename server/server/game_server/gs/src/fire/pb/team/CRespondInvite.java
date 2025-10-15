
package fire.pb.team;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.battle.PlayPKManage;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.clan.ClanUtils;
import fire.pb.event.ArriveTeamSpecialQuestEvent;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.talk.MessageMgr;




// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRespondInvite__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 回应邀请
 * @author changhao
 *
 */
public class CRespondInvite extends __CRespondInvite__ {
		
	//final static int MEMBER_MAX_COUNT = 4;//4个，作用于队伍成员人数（不包括队长），同时发出的邀请个数
	
	//final static long MAX_INVITE_TIMEOUT = 30*1000;//30s,作用于邀请超时，同一邀请限制时间
	private long now = 0L;//procedure开始时保存一个值，保证此procedure中时间的统一性
	Team team;
	
	@Override
	protected void process() {
		// protocol handle
		TeamManager.logger.debug("Enter: " + this.getClass());
		final long invitedRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (invitedRoleId < 0)
			return;	
		
		mkdb.Procedure resInviteP = new mkdb.Procedure()
		{

			@Override
			protected boolean process()
			{
				now = System.currentTimeMillis();
				//lock start
				//验证是否被邀请
				try
				{
				xbean.InviteInfo inviteInfo = xtable.Teaminvite.select(invitedRoleId);
				if(inviteInfo == null)
					return true;
				else if(!inviteInfo.getBeinginvited())
					return true;
				else if((now - inviteInfo.getInviting().getInvitetime()) > TeamManager.MAX_INVITE_TIMEOUT)
				{   //邀请超时
					inviteInfo.setBeinginvited(false);
					return true;
				}
				
				xbean.TeamInvite inviting = inviteInfo.getInviting();
				long inviterRoleId = inviting.getRoleid();
				Long inviterTeamId = null;
				if(inviting.getTeamid() > -1)
					inviterTeamId = inviting.getTeamid();
				int lockState = 0;
				if(agree == 1)
				{
					Long inviterclanfightid = xtable.Roleid2clanfightid.select(inviterRoleId);//如果邀请者在公会战场中 by changhao
					if (inviterclanfightid != null)
					{
						Long invitedclanfightid = xtable.Roleid2clanfightid.select(invitedRoleId);//如果被邀请者在公会战场中 by changhao
						if (!inviterclanfightid.equals(invitedclanfightid))
						{
			 			    MessageMgr.sendMsgNotify(inviterRoleId, 410022,  null);
			 			    MessageMgr.sendMsgNotify(invitedRoleId, 410031,  null);
			 			    return true;
						}
						else
						{
							xbean.ClanInfo claninfo = ClanUtils.getClanInfoById(inviterRoleId, true);
							if (claninfo == null)
							{
								return true;
							}
							
							xbean.ClanMemberInfo memberinfo = claninfo.getMembers().get(invitedRoleId);
							if (memberinfo == null)
							{
				 			    MessageMgr.sendMsgNotify(inviterRoleId, 410023,  null);
				 			    MessageMgr.sendMsgNotify(invitedRoleId, 410029,  null);
								return true;
							}
						}
					}
					else
					{
						Long invitedclanfightid = xtable.Roleid2clanfightid.select(invitedRoleId);//如果被邀请者在公会战场中 by changhao
						if (invitedclanfightid != null)
						{
							if (!invitedclanfightid.equals(inviterclanfightid))
							{
				 			    MessageMgr.sendMsgNotify(inviterRoleId, 410024,  null);
				 			    MessageMgr.sendMsgNotify(invitedRoleId, 410032,  null);	
				 			    return true;
							}				
						}
					}						
					
					PropRole inviterprop = new PropRole(inviterRoleId, true);
					if(inviterprop.getProperties().getCruise() > 0)
					{
						MessageMgr.sendMsgNotify(invitedRoleId, 162026, null);
						MessageMgr.sendMsgNotify(inviterRoleId, 162027, null);
						TeamManager.logger.info("CRespondInvite1:邀请人" + inviterRoleId + "被邀请人" + invitedRoleId + ",邀请人在巡游状态,不能邀请某人");
						return true;
					}
					
					PropRole invitedprop = new PropRole(invitedRoleId, true);
					if(invitedprop.getProperties().getCruise() > 0)
					{
						MessageMgr.sendMsgNotify(invitedRoleId, 162027, null);
						MessageMgr.sendMsgNotify(inviterRoleId, 162026, null);
						TeamManager.logger.info("CRespondInvite2:邀请人" + inviterRoleId + "被邀请人" + invitedRoleId + ",邀请人在巡游状态,不能邀请某人");
						return true;
					}
					
					if(checkInviteFromTeam(inviterTeamId))
					{
						//来自队伍的邀请，锁team，队长和被邀请者
						lockState = 3;
					}
					else
					{
						//来自个人的邀请，看现在有无队伍
						inviterTeamId = xtable.Roleid2teamid.select(inviterRoleId);
						if(inviterTeamId!= null)
						{//有队伍，看是否是队长
							lockState = 3;//锁team，队长和被邀请者
						}
						else
						{//无队伍，锁邀请者和被邀请者
							lockState = 2;
						}
					}
				}
				else
				{//只锁被邀请者的rolelock
					lockState = 1;
				}
				
				switch (lockState)
				{
				case 1://只锁被邀请者的rolelock
					Long[] roleids1 = new Long[1];
					roleids1[0] = invitedRoleId;
					this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK,(Object[])roleids1));
					break;
				case 2://无队伍，锁邀请者和被邀请者
					Object[] roleids2 = new Object[2];
					if(inviterRoleId < invitedRoleId)
					{
						roleids2[0] = inviterRoleId;
						roleids2[1] = invitedRoleId;
					}
					else
					{
						roleids2[0] = invitedRoleId;
						roleids2[1] = inviterRoleId;
					}
					this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids2));
					break;

				case 3://锁team，和所有人
					xbean.TeamInfo teamInfo = xtable.Team.get(inviterTeamId);
					if(teamInfo == null)
					{
						psend(invitedRoleId, new STeamError(TeamError.InviterTeamNotExist));
						TeamManager.logger.debug("FAIL:邀请您的队伍已经解散,TeamId: "+inviterTeamId);
						return true;
					}
					team = new Team(inviterTeamId,false);
					Set<Long> roleids = team.getAllMemberIdSet();
					roleids.add(invitedRoleId);
					this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));
					break;
				default:
					return true;
				}
				if(agree == 1){
				}
				PropRole prole = new PropRole(invitedRoleId, true);
				String invitedName = prole.getName();
				
				Long invitedTeamId = xtable.Roleid2teamid.get(invitedRoleId);
				if(!checkInviteExist(invitedRoleId))
				{
					//邀请已经超时或者邀请不存在（illegal）
					TeamManager.logger.debug("FAIL:邀请已经超时或者邀请不存在,RoleId: "+invitedRoleId);
					return true;
				}
				if(agree == 1)
				{
					if (checkPvP(inviterRoleId, invitedRoleId) != 0) {
						return true;
					}

					if(!checkOnline(invitedRoleId))
					{
						//被邀请者不在线,接受邀请后又下线了？（illegal）
						TeamManager.logger.debug("FAIL:被邀请者不在线,接受邀请后又下线了？,RoleId: "+invitedRoleId);
					}
					/*else if(!checkInvitedStatus(invitedRoleId))
					{
						//被邀请者处于不能组队的状态
						//psend(invitedRoleId, new STeamError(TeamError.SelfInUnteamState));
						Message.psendMsgNotify(invitedRoleId, TeamManager.ERROR_MSG_SELF_CANT_IN_TEAM, null);
						TeamManager.logger.debug("FAIL:被邀请者处于不能组队的状态,RoleId: "+invitedRoleId);
					}*/
					else if(!checkInvitedTeamFuctionEnable(invitedRoleId))
					{
						//被邀请者的组队功能没有打开
						psend(invitedRoleId, new STeamError(TeamError.SelfTeamFunctionClose));
						TeamManager.logger.debug("FAIL:被邀请者的组队功能没有打开,RoleId: "+invitedRoleId);
					}
					else if(!checkInvitedInNoTeam(invitedTeamId))
					{
						//被邀请者在队伍中（illegal）
						TeamManager.logger.debug("FAIL:被邀请者在队伍中,RoleId: "+invitedTeamId);
					}
//					else if (fire.pb.buff.Module.existState(inviterRoleId, BuffConstant.StateType.STATE_INSTANCE_ZONE)) {
//						MessageMgr.sendMsgNotify(invitedRoleId, 160202, null);
//						TeamManager.logger.debug("CRespondInvite:玩家(roleId=" + inviterRoleId+")invite处于副本中,不能组队");
//					}
					else if(inviting.getTeamid() > -1)
					{
						TeamManager.logger.debug("INFO:来自队伍的邀请,TeamId: "+inviterTeamId);
						//来自队伍的邀请
						xbean.TeamInfo teamInfo = xtable.Team.get(inviterTeamId);
						if(!checkInviterTeamExist(teamInfo))
						{
							//邀请您的队伍已经解散
							psend(invitedRoleId, new STeamError(TeamError.InviterTeamNotExist));
							TeamManager.logger.debug("FAIL:邀请您的队伍已经解散,TeamId: "+inviterTeamId);
						}
						else if(!checkTeamInvitingValid(teamInfo, invitedRoleId))
						{
							//队伍邀请已经超时（illegal）
							TeamManager.logger.debug("FAIL:队伍邀请已经超时,TeamId: "+inviterTeamId);
						}
						
						else if(!checkTeamNotFull(teamInfo)){
							MessageMgr.psendMsgNotify(invitedRoleId, 145740, null);
							//对方队伍人数已满
							psend(invitedRoleId, new STeamError(TeamError.TeamFull));
							TeamManager.logger.debug("FAIL:对方队伍人数已满,TeamId: "+inviterTeamId);
						}
						else if(isLeaderInDuel(teamInfo.getTeamleaderid()))
						{
							// 队长在决斗 by changhao
							TeamManager.logger.debug("FAIL:队长在决斗,TeamId: "+inviterTeamId);
						}else if(checkMap(teamInfo.getTeamleaderid(), invitedRoleId)){
							//队伍可以加入这个新成员
							Team team = new Team(inviterTeamId,false);
							TeamManager.logger.debugWhileCommit("SUCC:队伍可以加入这个新成员(原来的队伍),TeamId: "+inviterTeamId);
							boolean ok = team.addNewMemberWithSP(invitedRoleId);
							//如果成功入队查看是否需要拉他到队长旁边 by changhao
							if (ok)
							{
								boolean iscruise = checkCruiseWhenInvited(invitedRoleId);
								if(iscruise){
									//被邀请者巡游状态,入队后立即暂离！
									new PAbsentReturnTeam(invitedRoleId, 1).call();
								} else {
									int ret = TeamManager.getInstance().execGotoLeader(invitedRoleId, team, true, 2);
									if (ret == 0)
									{
										xbean.Properties invitedprop = xtable.Properties.get(invitedRoleId);
										
										List<String> params = new ArrayList<String>();
										params.add(invitedprop.getRolename());
										fire.pb.talk.MessageMgr.sendMsgNotify(teamInfo.getTeamleaderid(), 160196 ,params);	
									}
								}
							}
							
							fire.pb.event.Poster.getPoster().dispatchEvent(new ArriveTeamSpecialQuestEvent(team.getTeamLeaderId(), invitedRoleId));
							
							return ok;
						}
					}
					else //这里是不应该进来的，没有队伍不能邀请 by changhao
					{
						TeamManager.logger.debug("INFO:来自个人的邀请,inviterRoleId: "+inviterRoleId);
						//来自个人的邀请
						if(!checkOnline(inviterRoleId))
						{
							// 邀请者不在线
							psend(invitedRoleId, new STeamError(TeamError.ObjectOffline));
							TeamManager.logger.debug("FAIL:邀请者不在线,inviterRoleId: "+inviterRoleId);
						}
						/*else if(!checkInviterStatus(inviterRoleId))
						{
							//邀请者处于不能组队的状态
							//psend(invitedRoleId, new STeamError(TeamError.ObjectInUnteamState));
							Message.psendMsgNotify(inviterRoleId, TeamManager.ERROR_MSG_SELF_CANT_IN_TEAM, null);
							TeamManager.logger.debug("FAIL:邀请者处于不能组队的状态,inviterRoleId: "+inviterRoleId);
						}*/else
						{
							//获取邀请者现在的队伍ID
							inviterTeamId = xtable.Roleid2teamid.get(inviterRoleId);
							if(checkInviterInTeam(inviterTeamId))
							{
								mkdb.Trace.log(mkdb.Trace.DEBUG, "邀请者邀请时没有队伍，但是回复时已经有队伍,TeamId: "+inviterTeamId);
								//邀请者邀请时没有队伍，但是回复时已经有队伍，可能是刚建立的，这时也可以加入
								xbean.TeamInfo teamInfo = xtable.Team.get(inviterTeamId); 
								if(!checkInviterIsLeader(inviterRoleId, teamInfo))
								{
									//邀请者不是队长
									//psend(invitedRoleId, new STeamError(TeamError.ObjectNotLeader));
									MessageMgr.psendMsgNotify(invitedRoleId, 141861, null);
									TeamManager.logger.debug("FAIL:邀请者不是队长,TeamId: "+inviterTeamId);
								}
								else if(isLeaderInDuel(inviterRoleId))
								{
									// 队长在决斗
									TeamManager.logger.debug("FAIL:队长在决斗,TeamId: "+inviterTeamId);
								}
								else if(!checkTeamInvitingValid(teamInfo, invitedRoleId))
								{
									//邀请已经超时（illegal）
									TeamManager.logger.debug("FAIL:邀请已经超时,TeamId: "+inviterTeamId);
								}
								else if(checkTeamNotFull(teamInfo) && checkMap(teamInfo.getTeamleaderid(), invitedRoleId))
								{
									Team team = new Team(inviterTeamId,false);
									//如果有结婚任务是不允许和其他人组队的
//									if(!MarryTaskStepManager.checkMarryTaskState(team.getAllMemberIds(), invitedRoleId)){
//										//TODO
//										
//									}else{
										TeamManager.logger.debugWhileCommit("SUCC:队伍加入这个新成员(后来的队伍),TeamId: "+inviterTeamId);
										return team.addNewMemberWithSP(invitedRoleId);
									//}
								}
							}
							else
							{
								//邀请者邀请时没有队伍，现在仍然没有，需要建立新的队伍，再加入
								TeamManager.logger.debug("INFO:邀请者邀请时没有队伍，现在仍然没有,inviterRoleId: "+inviterRoleId);
								if(!checkSingleInvitingExist(inviterRoleId, invitedRoleId))
								{
									//邀请者的邀请已经超时（illigal）
									TeamManager.logger.debug("FAIL:邀请者的邀请已经超时,inviterRoleId: "+inviterRoleId);
								}
								else if(isLeaderInDuel(inviterRoleId))
								{
									// 队长在决斗
									TeamManager.logger.debug("FAIL:队长在决斗,TeamId: "+inviterTeamId);
								}
								else if(checkMap(inviterRoleId, invitedRoleId))
								{
									//建立新的队伍，邀请者为队长，邀请者的邀请全移到队伍中
									Team team = TeamManager.getInstance().createNewTeam(inviterRoleId);
									if(team == null)
									{
										//fire.pb.talk.Message.sendMsgNotify(inviterRoleId, TeamManager.ERROR_MSG_SELF_CANT_IN_TEAM, null);
										fire.pb.talk.MessageMgr.sendMsgNotify(invitedRoleId, TeamManager.ERROR_MSG_OBJECT_CANT_IN_TEAM, null);
										TeamManager.logger.debug("FAIL:创建队伍失败（可能由于状态冲突）。");
										return true;
									}
									//如果有结婚任务是不允许和其他人组队的
//									if(!MarryTaskStepManager.checkMarryTaskState(team.getAllMemberIds(), invitedRoleId)){
//										//TODO
//										return true;
//									}
									//添加新成员
									TeamManager.logger.debugWhileCommit("SUCC:队伍加入这个新成员(新建的队伍，邀请者为队长),TeamId: "+inviterTeamId);
									return team.addNewMemberWithSP(invitedRoleId);
								}
							}
						}
					}
				}
				else
				{
					java.util.List<String> name = new java.util.ArrayList<String>();
					name.add(invitedName);
					if(checkInviteFromTeam(inviterTeamId))
					{
						//发送给队长
						Long leaderId = xtable.Team.selectTeamleaderid(inviterTeamId);
						if(leaderId!=null)
						{
							fire.pb.talk.MessageMgr.psendMsgNotify(leaderId, 140851, name);
							psendWhileCommit(leaderId, new SRespondInvite(invitedRoleId,(byte)0));
						}
					}
					else
					{
						//发送给邀请者
						fire.pb.talk.MessageMgr.psendMsgNotify(inviterRoleId, 140851, name);
						psendWhileCommit(inviterRoleId, new SRespondInvite(invitedRoleId,(byte)0));
					}
					TeamManager.logger.debug("FAIL，不接受组队邀请,invitedRoleId: "+invitedRoleId);
				}
				//不接受邀请或者接受邀请的条件未满足，都要删除邀请
				}
				finally{
					deleteInvite(invitedRoleId);
				}
				return true;
			}
			
		};
		resInviteP.submit();		
	}
	
	private boolean checkMap(long leaderRoleId, long applierRoleId){
		boolean inWaiting1 = false;
		boolean inWaiting = false;
		
		//发起人
		final fire.pb.map.Role  invitMaprole = fire.pb.map.RoleManager.getInstance().getRoleByID(leaderRoleId);
		final fire.pb.map.Role  desMaprole = fire.pb.map.RoleManager.getInstance().getRoleByID(applierRoleId);
		if(invitMaprole == null || desMaprole == null){
			return true;
		}
		int srcMapId = invitMaprole.getMapId();
		MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(srcMapId);
		
		int desMapId = desMaprole.getMapId();
		MapConfig descfg = ConfigManager.getInstance().getConf(MapConfig.class).get(desMapId);
		
		if(inWaiting || inWaiting1){
			return false;
		}
		
		//在安全地图 不校验组队
		if(cfg == null || descfg == null){
			return true;
		}
		if(cfg.getSafemap() == descfg.getSafemap() && cfg.getSafemap() == 1){
			return true;
		}
		
		return true;
	}

	// 检测PVP
	private static int checkPvP(long inviterRoleId, long invitedRoleId) {
		// 回应邀请
		return fire.pb.battle.pvp.PvPTeamHandle.onRespondInvite(inviterRoleId, invitedRoleId);
	}

	//邀请存在而且没超时？只能在Procedure中被调用
	private boolean checkInviteExist(long invitedRoleId)
	{
		//先从邀请表中查找该人是否被邀请
		xbean.InviteInfo invite = xtable.Teaminvite.get(invitedRoleId);
		if(invite == null)
			return false;
		else if(!invite.getBeinginvited())
			return false;
		else if((now - invite.getInviting().getInvitetime()) > TeamManager.MAX_INVITE_TIMEOUT)
		{   //邀请超时
			invite.setBeinginvited(false);
			cleanTimeoutInvites(invite.getInvited());
			return false;
		}
		else
			return true;
	}
	
	// 被邀请者在线?只能在Procedure中被调用
	private boolean checkOnline(long roleId)
	{
		if(StateCommon.isOnline(roleId))
			return true;
		else
			return false;
	}	
	
	
	// 被邀请者组队开关已打开?只能在Procedure中被调用
	private boolean checkInvitedTeamFuctionEnable(long invitedRoleId)
	{
		return true;
	}
	// 被邀请者不在队伍中？只能在Procedure中被调用
	private boolean checkInvitedInNoTeam(Long invitedTeamId)
	{
		if(invitedTeamId == null)
			return true;
		else
			return false;
	}
	
	private boolean checkCruiseWhenInvited(long invitedRoleId){
		PropRole prole = new PropRole(invitedRoleId, true);
		if(prole.getProperties().getCruise() > 0) {
			return true;
		}
		return false;
	}
	
	//来自队伍的邀请？（false是来自个人）只能在Procedure中被调用
	private boolean checkInviteFromTeam(Long inviterTeamId)
	{
		if(inviterTeamId != null)
			return true;
		else
			return false;
	}
	
	//邀请队伍还存在？只能在Procedure中被调用
	private boolean checkInviterTeamExist(xbean.TeamInfo teamInfo)
	{
		if(teamInfo != null)
			return true;
		else
			return false;
	}
	
	//队伍的邀请中存在这个邀请，而且还未超时？只能在Procedure中被调用
	private boolean checkTeamInvitingValid(xbean.TeamInfo teamInfo, long invitedRoleId)
	{
		java.util.Map<Long,Long> invitings = teamInfo.getInvitingids();
	    cleanTimoutInvitings(invitings);
	    if(invitings.get(invitedRoleId) != null)
	    	return true;
	    else
	    	return false;
	}
	//队伍的人数未达到5人？只能在Procedure中被调用
	private boolean checkTeamNotFull(xbean.TeamInfo teamInfo)
	{
		if (teamInfo.getMembers().size() < 4)
			return true;
		else
			return false;
	}
	
/*	//邀请者处于可以组队的状态？（非跑商、飞行、单人任务等）
	private boolean checkInviterStatus(long inviterRoleId)
	{
		BuffAgent buffagent = new BuffRoleImpl(inviterRoleId,true);
		if(!buffagent.canAddBuff(StateType.STATE_TEAM))
		{
			TeamManager.logger.info("玩家(roleId=" + inviterRoleId+")处于不能组队的状态");
			return true;
		}
		return true;
	}
*/	
	//邀请者处于队伍中？只能在Procedure中被调用
	private boolean checkInviterInTeam(Long inviterTeamId)
	{
		if(inviterTeamId != null)
			return true;
		else
			return false;
	}
	
	// 邀请者是队长？只能在Procedure中被调用
	private boolean checkInviterIsLeader(long inviterRoleId , xbean.TeamInfo teamInfo)
	{
		if(teamInfo.getTeamleaderid() == inviterRoleId)
			return true;
		else
			return false;
	}
	
	//个人邀请表中的邀请存在？只能在Procedure中调用
	private boolean checkSingleInvitingExist(long inviterRoleId, long invitedRoleId)
	{
		xbean.SingleInvitings singleInvitings = xtable.Singleinviting.get(inviterRoleId);
		if(singleInvitings == null)
			return false;
		else
		{
			cleanTimoutInvitings(singleInvitings.getInvitingids());
			if(singleInvitings.getInvitingids().get(invitedRoleId) == null)
				return false;
		    else
		    	return true;
		}
	}
	
	
	//使玩家回到未被邀请状态,同时删除过期邀请
	private void deleteInvite(final long invitedRoleId)
	{
		xbean.InviteInfo inviteInfo = xtable.Teaminvite.select(invitedRoleId);
		if(inviteInfo == null)
			return;
		//删除team表中的inviting信息或者SingleInvitings中的inviting信息
		if(inviteInfo.getInviting().getTeamid() > -1)
		{
			xbean.TeamInfo team = xtable.Team.get(inviteInfo.getInviting().getTeamid());
			inviteInfo = xtable.Teaminvite.get(invitedRoleId);
			if(inviteInfo == null)
				return;
			if(team != null)
				team.getInvitingids().remove(invitedRoleId);
		}
		else
		{
			/*Set<Long> rids = new HashSet<Long>();
			rids.add(invitedRoleId);
			rids.add(inviteInfo.getInviting().getRoleid());
			Lockeys.lock(xtable.Locks.ROLELOCK, rids);*/
			inviteInfo = xtable.Teaminvite.get(invitedRoleId);
			if(inviteInfo == null)
				return;
			final long invitingroleid = inviteInfo.getInviting().getRoleid();
			mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure(){
				@Override
				protected boolean process() throws Exception
				{
					xbean.SingleInvitings singleInvitings = xtable.Singleinviting.get(invitingroleid);
					if(singleInvitings != null)
					{
						singleInvitings.getInvitingids().remove(invitedRoleId);
						if(singleInvitings.getInvitingids().size() == 0)
							xtable.Singleinviting.remove(invitingroleid);
					}
					return true;
				}
			});
		}
		
		//更新invites表中的信息
		inviteInfo.setBeinginvited(false);
		cleanTimeoutInvites(inviteInfo.getInvited());
		//如果invited中也没有任何信息，则此条InviteInfo没有存在的必要，可以删除
		if(inviteInfo.getInvited().size() == 0)
			xtable.Teaminvite.remove(invitedRoleId);
		
		
	}
	
	
	//清除邀请表中的过期邀请,只能在Procedure中被调用
	private void cleanTimeoutInvites(java.util.List<xbean.TeamInvite> invites)
	{
		java.util.List<xbean.TeamInvite> timeoutList = new java.util.ArrayList<xbean.TeamInvite>();
		for (xbean.TeamInvite invite : invites)
		{
			if ((now - invite.getInvitetime()) > TeamManager.MAX_INVITE_TIMEOUT)
				timeoutList.add(invite);
		}
		invites.removeAll(timeoutList);
	}
	
	//清除队伍和个人邀请者中的过期邀请
	private void cleanTimoutInvitings(java.util.Map<Long,Long> invitings)
	{
		Object[] keys = invitings.keySet().toArray();
		for(int i = 0; i <keys.length;i++)
		{
			if ((now - invitings.get(keys[i])) > TeamManager.MAX_INVITE_TIMEOUT)
			invitings.remove(keys[i]);
		}
	}
	
	public boolean isLeaderInDuel(long leaderRoleId)
	{
		BuffAgent agent = new BuffRoleImpl(leaderRoleId, true);
		if (agent.existBuff(PlayPKManage.BuffDuelID))
		{
			MessageMgr.sendMsgNotify(leaderRoleId, 141867,  null);
			return true;
		}
		else
			return false;
	}
	
	
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794448;

	public int getType() {
		return 794448;
	}

	public byte agree; // agree为1接受邀请，为0拒绝邀请

	public CRespondInvite() {
	}

	public CRespondInvite(byte _agree_) {
		this.agree = _agree_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(agree);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		agree = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRespondInvite) {
			CRespondInvite _o_ = (CRespondInvite)_o1_;
			if (agree != _o_.agree) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)agree;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(agree).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRespondInvite _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = agree - _o_.agree;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


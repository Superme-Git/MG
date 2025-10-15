package fire.pb.team;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.event.ArriveTeamSpecialQuestEvent;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.talk.MessageMgr;

public class PAcceptToTeam extends mkdb.Procedure
{
	private final long leaderRoleId;
	private final long applierRoleId;
	private final int accept;
	private final boolean needapply;
	
	private boolean needCheck;
	
	public boolean isNeedCheck() {
		return needCheck;
	}

	public void setNeedCheck(boolean needCheck) {
		this.needCheck = needCheck;
	}

	public PAcceptToTeam(long leaderRoleId, long applierRoleId, int accept, boolean needaapply)
	{
		this.leaderRoleId = leaderRoleId;
		this.applierRoleId = applierRoleId;
		this.accept = accept;
		this.needapply = needaapply;
		this.needCheck = true;
	}
	
	public PAcceptToTeam(long leaderRoleId, long applierRoleId, int accept, boolean needaapply, boolean needCheck)
	{
		this.leaderRoleId = leaderRoleId;
		this.applierRoleId = applierRoleId;
		this.accept = accept;
		this.needapply = needaapply;
		this.needCheck = needCheck;
	}
	
	
	
	@Override
	protected boolean process() throws Exception
	{
		Team team;
		//lock start 
		Long teamId = xtable.Roleid2teamid.select(leaderRoleId);
		//先验证队伍是否为空
		if(teamId != null)
		{
			xbean.TeamInfo teamInfo = xtable.Team.get(teamId);
			if (teamInfo == null)
			{
				return true;
			}
			
			team = new Team(teamId,false);
		}
		else
		{
			return true;
		}
		
		if(!team.isTeamLeader(leaderRoleId))
			return true;//验证该角色是否还是队伍的队长
		//三种情况 accept=1接受申请；accept=0拒绝申请；applierRoleId=0清空申请。
		if(accept == 1)
		{//接受申请时锁队伍内所有人的锁rolelock
			final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
			roleids.addAll(team.getAllMemberIds());
			roleids.add(applierRoleId);
			this.lock(xtable.Locks.ROLELOCK, roleids);
		}
		//lock end
		
		//清空申请列表
		if(applierRoleId == 0)
		{
			SRemoveTeamApply sRemoveTeamApply = new SRemoveTeamApply();
			for(long roleId : team.getTeamInfo().getApplierids().keySet())
			{
				sRemoveTeamApply.applyids.add(roleId);
				
			}
			psendWhileCommit(team.getTeamInfo().getTeamleaderid(), sRemoveTeamApply);
			team.getTeamInfo().getApplierids().clear();
			return true;
		}
		//删除该申请
		if(accept == 0)
		{
			team.removeTeamApplyWithSendProtocol(applierRoleId);
			//向被拒绝者发送消息
			PropRole prole = new PropRole(team.getTeamInfo().getTeamleaderid(), true);
			List<String> name = new ArrayList<String>();
			name.add(prole.getName());
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(applierRoleId, 140640, name);
			return true;
		}
		//接受申请
		//首先清理过期申请
		team.removeTimeoutTeamApplys();
		PropRole prole = new PropRole(applierRoleId, true);
		if(!checkOnline(applierRoleId)) //如果申请者已经离线发送提示 by changhao
		{
			//申请者不在线(illegal)
			team.removeTeamApplyWithSendProtocol(applierRoleId);
			fire.pb.talk.MessageMgr.sendMsgNotify(leaderRoleId, 160215, null);
			TeamManager.logger.info("FAIL:PAcceptToTeam:申请者不在线, applierRoleId" + applierRoleId);
			return true;
		}
		if(!checkApplierNotInTeam(applierRoleId))
		{
			//申请者在队伍中
			team.removeTeamApplyWithSendProtocol(applierRoleId);
			psend(leaderRoleId, new STeamError(TeamError.ObjectInTeam));
			TeamManager.logger.info("FAIL:PAcceptToTeam:申请者已经在队伍中, applierRoleId" + applierRoleId);
			return true;
		}
		if(!checkApplierStatusValid(applierRoleId))
		{
			//申请者处于不可以组队的状态？（飞行，跑商，摆摊，护送等）
			team.removeTeamApplyWithSendProtocol(applierRoleId);
			MessageMgr.psendMsgNotify(leaderRoleId, TeamManager.ERROR_MSG_OBJECT_CANT_IN_TEAM, null);
			TeamManager.logger.info("FAIL:PAcceptToTeam:申请者处于不可以组队的状态, applierRoleId" + applierRoleId);
			return true;
		}
		if(!checkTeamExist(team, leaderRoleId))
		{
			//玩家不是一个队伍的队长(illegal)
			TeamManager.logger.info("FAIL:PAcceptToTeam:玩家不是一个队伍的队长, leaderRoleId" + leaderRoleId);
			return true;
		}		
		//如果有结婚任务是不允许和其他人组队的
//		if(!MarryTaskStepManager.checkMarryTaskState(team.getAllMemberIds(), applierRoleId)){
//			team.removeTeamApplyWithSendProtocol(applierRoleId);
//			return false;
//		}
		if(!checkTeamNotFull(team))
		{
			//队伍人数已满
			psend(leaderRoleId, new STeamError(TeamError.TeamFull));
			TeamManager.logger.info("FAIL:PAcceptToTeam:队伍人数已满, teamId" + teamId);
			return true;
		}
		if(needapply && !team.getTeamInfo().getApplierids().containsKey(applierRoleId))
		{
			//申请者不在队伍的申请列表中（可能申请超时）
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(leaderRoleId, 150174, null);
			SRemoveTeamApply sRemoveTeamApply = new SRemoveTeamApply();
			sRemoveTeamApply.applyids.add(applierRoleId);
			psend(team.getTeamInfo().getTeamleaderid(), sRemoveTeamApply);
			TeamManager.logger.info("PAcceptToTeam:申请者不在队伍的申请列表中,或者申请超时, teamId" + teamId);
			return true;
		}
		
		if(!checkMap()){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(leaderRoleId,145050, null);
			return true;
		}
		if (checkPvP(leaderRoleId, applierRoleId) != 0) {
			return true;
		}

		team.removeTeamApplyWithSendProtocol(applierRoleId);
		TeamManager.logger.debugWhileCommit("SUCC：PAcceptToTeam:加入申请者, teamId" + teamId);
		boolean ok = team.addNewMemberWithSP(applierRoleId);
		
		//如果成功入队查看是否需要拉他到队长旁边 by changhao
		if (ok)
		{
			int ret = TeamManager.getInstance().execGotoLeader(applierRoleId, team, true, 2);
			if (ret == 0)
			{
				xbean.Properties applierprop = xtable.Properties.get(applierRoleId);
				List<String> params = new ArrayList<String>();
				params.add(applierprop.getRolename());					
				
				fire.pb.talk.MessageMgr.sendMsgNotify(leaderRoleId, 160196 ,params);				
			}
		}
		
		fire.pb.event.Poster.getPoster().dispatchEvent(new ArriveTeamSpecialQuestEvent(leaderRoleId, applierRoleId));
		
		
		if (team.isAbsentMember(applierRoleId))
		{
			java.util.List<String> p = new java.util.ArrayList<String>();
			p.add(prole.getName());		
			MessageMgr.psendMsgNotifyWhileCommit(leaderRoleId, 150121, p);			
		}
		
		return ok;
	}
	
	private boolean checkMap(){
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
		
//		if (srcMapId != desMapId) {
//			if (fire.pb.buff.Module.existState(leaderRoleId,
//					BuffConstant.StateType.STATE_INSTANCE_ZONE)) {
//				MessageMgr.sendMsgNotify(leaderRoleId, 160202, null);
//				return false;
//			}
//		} else {
			if (inWaiting || inWaiting1) {
				return false;
			}
//		}
		
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
	private static int checkPvP(long leaderRoleId, long applierRoleId) {
		// 队长同意或拒绝申请者加入队伍
		return fire.pb.battle.pvp.PvPTeamHandle.onAcceptToTeam(leaderRoleId, applierRoleId);
	}

	//申请者在线？
	private boolean checkOnline(long roleId)
	{
		if(StateCommon.isOnline(roleId))
			return true;
		else
			return false;
	}
	
	//申请者不在队伍中？
	private boolean checkApplierNotInTeam(long applierRoleId)
	{
		if(xtable.Roleid2teamid.get(applierRoleId) == null)
			return true;
		else
			return false;
	}
	
	//申请者处于能组队的状态？（飞行，跑商，摆摊，护送等不能组队）
	private boolean checkApplierStatusValid(long applierRoleId)
	{
		BuffAgent buffagent = new BuffRoleImpl(applierRoleId,true);
		if(!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM))
		{
			TeamManager.logger.info("玩家(roleId=" + applierRoleId+")处于不能组队的状态");
			return false;
		}
		return true;
	}
	
	//队长的队伍存在？（并且是队长）
	private boolean checkTeamExist(Team team, long leaderRoleId)
	{
			xbean.TeamInfo teamInfo = team.getTeamInfo();
			if(teamInfo.getTeamleaderid() == leaderRoleId)
				return true;
			else
				return false;
	}
	
	//队伍人数未满？
	private boolean checkTeamNotFull(Team team)
	{
		if(team.getTeamInfo().getMembers().size() < TeamManager.MAX_MEMBER_COUNT)
			return true;
		else
			return false;
	}
	
}

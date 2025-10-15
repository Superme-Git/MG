
package fire.pb.team;

import fire.pb.StateCommon;
import fire.pb.battle.PlayPKManage;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.OperateType;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.talk.MessageMgr;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CFInviteJoinTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CFInviteJoinTeam extends __CFInviteJoinTeam__ {
private long now = 0L;//procedure开始时保存一个当前时间，保证此procedure中时间的统一性
	
	@Override
	protected void process()
	{
		// protocol handle
		TeamManager.logger.debug("Enter: " + this.getClass());
		final long inviterRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (inviterRoleId < 0)
			return;
		final long invitedRoleId = roleid;
		if(inviterRoleId == invitedRoleId)
			return;
		//add by cn 如果在单挑决斗状态,不允许加入任何队伍
		BuffAgent agent = new BuffRoleImpl(inviterRoleId, true);
		if (agent.existBuff(PlayPKManage.BuffDuelID)) {
			if (fire.pb.team.TeamManager.isInTeam(inviterRoleId))
				MessageMgr.sendMsgNotify(inviterRoleId, 141866,  null);
			else 
 			    MessageMgr.sendMsgNotify(inviterRoleId, 141133,  null);
			return;
		}
		
		agent = new BuffRoleImpl(invitedRoleId,true);
		if (agent.existBuff(PlayPKManage.BuffDuelID)) {
			MessageMgr.sendMsgNotify(inviterRoleId, 141865,  null);
			return;
		}
		mkdb.Procedure createTeamP = new mkdb.Procedure()
		{

			@Override
			protected boolean process()
			{
				Team team = null;
				//lock start 
				Long teamId = xtable.Roleid2teamid.select(inviterRoleId);
				//先验证队伍是否为空,来决定是否锁teamlock
				if(teamId != null)
				{//锁teamlock
					team = new Team(teamId,false);
					if(!team.isTeamLeader(inviterRoleId))
						return true;//验证该角色是否还是队伍的队长
				}
				
				// 按顺序锁rolelock和 teamlock
				Long inviterTeamId = null;
				Long invitedTeamId = null;
				if (inviterRoleId < invitedRoleId)
				{
					inviterTeamId = xtable.Roleid2teamid.get(inviterRoleId);
					invitedTeamId = xtable.Roleid2teamid.get(invitedRoleId);
				} else
				{
					invitedTeamId = xtable.Roleid2teamid.get(invitedRoleId);
					inviterTeamId = xtable.Roleid2teamid.get(inviterRoleId);
				}
				//lock end
				
				now = System.currentTimeMillis();
				
				if(!checkOnline(invitedRoleId, inviterRoleId))
				{
					//返回消息被邀请的玩家不在线(illegal)
					TeamManager.logger.debug("FAIL:被邀请的玩家不在线,RoleId: "+invitedRoleId);
				}
				else if(!checkInviterStatus(inviterRoleId))
				{
					//邀请者处于不能组队的状态 
					MessageMgr.psendMsgNotify(inviterRoleId, 141618, null);
//					psend(inviterRoleId, new STeamError(TeamError.SelfInUnteamState));
					TeamManager.logger.debug("FAIL:邀请者处于不能组队的状态,RoleId: "+inviterRoleId);
				}
				else if(!checkInvitedStatus(invitedRoleId))
				{
					//被邀请者处于不能组队的状态 
					MessageMgr.psendMsgNotify(inviterRoleId, TeamManager.ERROR_MSG_OBJECT_CANT_IN_TEAM, null);
//					psend(invitedRoleId, new STeamError(TeamError.SelfInUnteamState));
					TeamManager.logger.debug("FAIL:被邀请者处于不能组队的状态,RoleId: "+invitedRoleId);
				}
				else if(!checkInvitedTeamFuctionEnable(invitedRoleId))
				{
					//被邀请者的组队功能没有打开
//					psend(inviterRoleId, new STeamError(TeamError.ObjectTeamFunctionClose));
					MessageMgr.psendMsgNotify(inviterRoleId, 141201, null);
					TeamManager.logger.debug("FAIL:被邀请者的组队功能没有打开,invitedRoleId: "+invitedRoleId);
				}
				else if(!checkInvitedInNoTeam(invitedTeamId))
				{
					//被邀请者在队伍中
					//psend(inviterRoleId, new STeamError(TeamError.ObjectInTeam));
					TeamManager.logger.debug("FAIL:被邀请者在队伍中,invitedRoleId: "+invitedRoleId);
					fire.pb.talk.MessageMgr.psendMsgNotify(inviterRoleId, 141191, null);
				}
				else if(!checkNotBeingInvited(invitedRoleId))
				{
					//被邀请者正在被其他人邀请中 
//					psend(inviterRoleId, new STeamError(TeamError.BeingInvited));
					fire.pb.talk.MessageMgr.psendMsgNotify(inviterRoleId, 141202, null);
					TeamManager.logger.debug("FAIL:被邀请者正在被其他人邀请中,invitedRoleId: "+invitedRoleId);
				}
				else if(!checkNotInvitedIn30s(invitedRoleId, inviterRoleId, inviterTeamId))
				{
					//被邀请者30秒内曾经被队伍或者个人邀请过
					//psend(inviterRoleId, new STeamError(TeamError.InvitedIn30s));
					TeamManager.logger.debug("FAIL:被邀请者30秒内曾经被队伍或者个人邀请过,invitedRoleId: "+invitedRoleId);
					fire.pb.talk.MessageMgr.psendMsgNotify(inviterRoleId, 141050, null);
				}
				else if(team != null)
				{
					//Team team = new Team(inviterTeamId,false);
					//来自队伍的邀请
					if(!checkInviterIsLeader(inviterRoleId, team))
					{
						//邀请者不是队长(illegal)
						TeamManager.logger.debug("FAIL:邀请者不是队长,RoleId: "+inviterRoleId);
					}
					else if(!checkTeamNotFull(team))
					{
						//邀请队伍满人
						psendWhileCommit(inviterRoleId, new STeamError(TeamError.TeamFull));
						TeamManager.logger.debug("FAIL:邀请队伍满人,TeamId: "+inviterTeamId);
					}
					else if(!checkTeamFilter(team, invitedRoleId))
					{
						TeamManager.logger.debug("FAIL:TeamFilter否决,TeamId: "+inviterTeamId);
					}
					else if(!checkTeamInviteNotFull(team))
					{
						//邀请队伍的正在邀请人数达到4个，不能再邀请更多
						psend(inviterRoleId, new STeamError(TeamError.InviteingsFull));
						TeamManager.logger.debug("FAIL:邀请队伍的正在邀请人数达到4个，不能再邀请更多,TeamId: "+inviterTeamId);
					}
					else if(!checkTeamLeaderState(inviterRoleId))
					{
						//邀请队伍的正在邀请人数达到4个，不能再邀请更多
						//psend(inviterRoleId, new STeamError(TeamError.InviteingsFull));
						TeamManager.logger.debug("FAIL:队长当前状态不能邀请。");
					}
					else{
						if(!checkMap()){
							return false;
						}
						
						TeamManager.logger.debug("SUCC:满足条件，可以发出队伍邀请 "+inviterTeamId);
						// 满足条件，可以发出队伍邀请
						SInviteJoinTeam snd = new SInviteJoinTeam();
						snd.op = 0;
						snd.invitername = xtable.Properties.get(inviterRoleId).getRolename();
						snd.inviterlevel = xtable.Properties.get(inviterRoleId).getLevel();
						// 保存邀请,这里不用定时器，可以被动方式清除
						//保存邀请到队伍
						team.getTeamInfo().getInvitingids().put(invitedRoleId,now);
						//保存邀请到被邀请表，先看被邀请者的邀请信息是否已存在
						xbean.InviteInfo inviteInfo = xtable.Teaminvite.get(invitedRoleId);
						if(inviteInfo == null)
						{
							inviteInfo = xbean.Pod.newInviteInfo();
							xtable.Teaminvite.add(invitedRoleId, inviteInfo);
						}
						inviteInfo.setBeinginvited(true);
						inviteInfo.getInviting().setTeamid(inviterTeamId);
						inviteInfo.getInviting().setRoleid(inviterRoleId);
						inviteInfo.getInviting().setInvitetime(now);						
						inviteInfo.getInvited().add(inviteInfo.getInviting().copy());
						psendWhileCommit(inviterRoleId, new SInviteJoinSucc(invitedRoleId));
						MessageMgr.psendMsgNotify(inviterRoleId, 142358, null);
						// 发送邀请
						mkdb.Procedure.psendWhileCommit(invitedRoleId, snd);
					}
			
				} else
				{
					//来自个人的邀请
					if(!checkSingleInviteNotFull(inviterRoleId))
					{
						//邀请者正在邀请人数达到4个，不能再邀请更多
						psend(inviterRoleId, new STeamError(TeamError.InviteingsFull));
						TeamManager.logger.debug("FAIL:邀请者正在邀请人数达到4个，不能再邀请更 "+inviterRoleId);
					}
					else if(!checkTeamFilter(inviterRoleId, invitedRoleId))
					{
						TeamManager.logger.debug("FAIL:TeamFilter否决,TeamId: "+inviterTeamId);
					}
					else
					{
						
						if(!checkMap()){
							return false;
						}
						
						TeamManager.logger.debug("SUCC:满足条件，可以发出个人邀请 "+inviterRoleId);
						// 满足条件，可以发出个人邀请
						xbean.Properties inviterProperty = xtable.Properties.get(inviterRoleId);
						SInviteJoinTeam snd = new SInviteJoinTeam();
						snd.op = 0;
						snd.invitername = inviterProperty.getRolename();
						snd.inviterlevel = xtable.Properties.get(inviterRoleId).getLevel();
						// 保存邀请,这里不用定时器，可以被动方式清除
						//保存邀请到个人邀请表
						xbean.SingleInvitings singleInvitings = xtable.Singleinviting.get(inviterRoleId);
						if(singleInvitings == null)
						{
							singleInvitings = xbean.Pod.newSingleInvitings();
							xtable.Singleinviting.add(inviterRoleId, singleInvitings);
						}
						singleInvitings.getInvitingids().put(invitedRoleId, now);
						//保存邀请到被邀请表，先看被邀请者的邀请信息是否已存在
						xbean.InviteInfo inviteInfo = xtable.Teaminvite.get(invitedRoleId);
						if(inviteInfo == null)
						{
							inviteInfo = xbean.Pod.newInviteInfo();
							xtable.Teaminvite.add(invitedRoleId, inviteInfo);
						}
						inviteInfo.setBeinginvited(true);
						if(inviterTeamId == null)
							inviterTeamId = -1L;
						inviteInfo.getInviting().setTeamid(inviterTeamId);
						inviteInfo.getInviting().setRoleid(inviterRoleId);
						inviteInfo.getInviting().setInvitetime(now);						
						inviteInfo.getInvited().add(inviteInfo.getInviting().copy());
						
						// 发送邀请
						psendWhileCommit(inviterRoleId, new SInviteJoinSucc(invitedRoleId));
						MessageMgr.psendMsgNotify(inviterRoleId, 142358, null);
						mkdb.Procedure.psendWhileCommit(invitedRoleId, snd);
					}
				}
					
				return true;
			}
			
			private boolean checkMap(){

				//发起人
				final fire.pb.map.Role  invitMaprole = fire.pb.map.RoleManager.getInstance().getRoleByID(inviterRoleId);
				final fire.pb.map.Role  desMaprole = fire.pb.map.RoleManager.getInstance().getRoleByID(invitedRoleId);
				if(invitMaprole == null || desMaprole == null){
					return true;
				}
				int srcMapId = invitMaprole.getMapId();
				MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(srcMapId);
				
				int desMapId = desMaprole.getMapId();
				MapConfig descfg = ConfigManager.getInstance().getConf(MapConfig.class).get(desMapId);
				
				//在安全地图 不校验组队
				if(cfg == null || descfg == null){
					return true;
				}
				if(cfg.getSafemap() == descfg.getSafemap() && cfg.getSafemap() == 1){
					return true;
				}
				
				
				return true;	
			}
		};
		createTeamP.submit();
	}
	
	// 邀请者和被邀请者同时在线?只能在Procedure中被调用
	private boolean checkOnline(long invitedRoleId, long inviterRoleId)
	{
		if(StateCommon.isOnline(invitedRoleId))
			return true;
		else
		{
			//对方不在线
			MessageMgr.sendMsgNotify(inviterRoleId, 141701, null);
			return false;
		}
	}	
	
	//邀请者处于可以组队的状态？（非跑商、飞行、单人任务等）
	private boolean checkInviterStatus(long inviterRoleId)
	{
		BuffAgent buffagent = new BuffRoleImpl(inviterRoleId,true);
		if(!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM))
		{
			TeamManager.logger.info("玩家(roleId=" + inviterRoleId+")处于不能组队的状态");
			return false;
		}
		return true;
	}
	
	//被邀请者处于可以组队的状态？（非跑商、飞行、单人任务等）
	private boolean checkInvitedStatus(long invitedRoleId)
	{
		BuffAgent buffagent = new BuffRoleImpl(invitedRoleId,true);
		if(!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM))
		{
			TeamManager.logger.info("玩家(roleId=" + invitedRoleId+")处于不能组队的状态");
			return false;
		}
		return true;
	}
	
	// 被邀请者组队开关已打开?只能在Procedure中被调用
	private boolean checkInvitedTeamFuctionEnable(long invitedRoleId)
	{
		//组队开关
//		if(fire.pb.SystemSettingConfig.checkRoleSetting(invitedRoleId, fire.pb.SysSetType.AcceptTeam)<=0)
//			return false;
//		else
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
	
	// 被邀请者没有正在被邀请？只能在Procedure中被调用
	private boolean checkNotBeingInvited(long invitedRoleId)
	{
		xbean.InviteInfo inviteInfo = xtable.Teaminvite.get(invitedRoleId);
		
		if (inviteInfo == null)//如果没有记录，表示从未被邀请过
			return true;
		if(!inviteInfo.getBeinginvited())
		{
			//如果正在邀请位为false，肯定不处于被邀请状态，清除过期邀请
			cleanTimeoutInvites(inviteInfo.getInvited());
			return true;
		}
		else if ((now - inviteInfo.getInviting().getInvitetime()) > TeamManager.MAX_INVITE_TIMEOUT)
		{
			//处于邀请状态，但是已经超时，需要更新被邀请者状态为不处于被邀请状态，同时清除过期邀请
			inviteInfo.setBeinginvited(false);
			cleanTimeoutInvites(inviteInfo.getInvited());
			return true;
		}
		else
			return false;
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

	// 这个邀请没有被队伍或者个人在30秒内邀请过？只能在Procedure中被调用
	private boolean checkNotInvitedIn30s(long invitedRoleId, long inviterRoleId, Long inviterTeamId)
	{
		xbean.InviteInfo inviteInfo = xtable.Teaminvite.get(invitedRoleId);
		if(inviteInfo!=null)
		{
			java.util.List<xbean.TeamInvite> timeoutList = new java.util.ArrayList<xbean.TeamInvite>();
			boolean result = true;
			for (xbean.TeamInvite invited : inviteInfo.getInvited())
			{
				if ((now - invited.getInvitetime()) < TeamManager.MAX_INVITE_TIMEOUT)
				{
					if (!result)
						continue;// 如果已经找到（result == false），就没必要进行后面的判断了
					else if (invited.getRoleid() == inviterRoleId)
						result = false;// roleid相等
					else if (inviterTeamId != null && invited.getTeamid() == inviterTeamId)
						result = false;// inviterTeamId存在并且teamId相等
				} else
					timeoutList.add(invited);// 超过30秒的过会一起删除
			}
			inviteInfo.getInvited().removeAll(timeoutList);
			return result;
		}
		else
		{
			return true;
		}
	}
	

	// 邀请者是队长？只能在Procedure中被调用
	private boolean checkInviterIsLeader(long inviterRoleId , Team team)
	{
		if(team.getTeamInfo().getTeamleaderid() == inviterRoleId)
			return true;
		else
			return false;
	}

	// 邀请者队伍人数<5? 只能在Procedure中被调用
	private boolean checkTeamNotFull(Team team)
	{
		if (team.getTeamInfo().getMembers().size() < 4)
			return true;
		else
			return false;
	}
	

	private boolean checkTeamFilter(Team team, long roleId)
	{
		TeamFilter filter = team.getFilter();
		if(filter == null)
			return true;
		return filter.checkInviteJoin(team.getTeamLeaderId(), roleId);
	}
	
	private boolean checkTeamFilter(long inviterId, long roleId)
	{
		TeamFilter filter = TeamManager.getActiveFilter(inviterId);
		if(filter == null)
			return true;
		return filter.checkInviteJoin(inviterId, roleId);
	}
	
	// 邀请者队伍的邀请次数<4? 只能在Procedure中被调用
	private boolean checkTeamInviteNotFull(Team team)
	{
		//先清理过期邀请
		cleanTimoutInvitings(team.getTeamInfo().getInvitingids());
		if (team.getTeamInfo().getInvitingids().size() < 4)
			return true;
		else
			return false;
	}
	
	// 邀请者状态不允许? 只能在Procedure中被调用
	private boolean checkTeamLeaderState(long inviterRoleId)
	{
		BuffAgent buffagent = new BuffRoleImpl(inviterRoleId);
		return buffagent.canAddBuff(OperateType.TEAM_INVITE);
	}
	
	// 邀请者的邀请次数<4? 只能在Procedure中被调用
	private boolean checkSingleInviteNotFull(long inviterRoleId)
	{
		xbean.SingleInvitings singleInvitings = xtable.Singleinviting.get(inviterRoleId);
		//先清理过期邀请
		if(singleInvitings == null)
			return true;
		cleanTimoutInvitings(singleInvitings.getInvitingids());
		if (singleInvitings.getInvitingids().size() < 4)
			return true;
		else
			return false;
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

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794493;

	public int getType() {
		return 794493;
	}

	public long roleid; // 被邀请者角色ID

	public CFInviteJoinTeam() {
	}

	public CFInviteJoinTeam(long _roleid_) {
		this.roleid = _roleid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CFInviteJoinTeam) {
			CFInviteJoinTeam _o_ = (CFInviteJoinTeam)_o1_;
			if (roleid != _o_.roleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CFInviteJoinTeam _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


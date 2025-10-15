
package fire.pb.team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.msp.team.GNotifyTeamChange;
import fire.msp.team.TeamChangeType;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.battle.battleflag.SModifyBattleFlag;
import fire.pb.battle.battleflag.SSetCommander;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.RoleManager;
import fire.pb.mission.SUseMissionItemFail;
import fire.pb.pet.PShowPetOffProc;
import fire.pb.scene.Scene;
import fire.pb.talk.ChatChannel;
import fire.pb.talk.DisplayInfo;
import fire.pb.talk.MessageMgr;
import mkdb.Transaction;

public class Team {

	private xbean.TeamInfo teamInfo;

	private boolean readonly;

	public final long teamId;

	public final static int MAX_RETURN_SCALE_LENGTH = 30 * 16;

	public final static int MAX_RETURN_SCALE_SQUARE = MAX_RETURN_SCALE_LENGTH * MAX_RETURN_SCALE_LENGTH;
	
	/**
	 * 构造函数。
	 * 
	 * @lock teamlock
	 * @param teamId
	 */
	Team(long teamId) {

		this(teamId, false);
	}

	/**
	 * 构造函数。
	 * 
	 * @lock readonly=true 无锁；reandonly=false锁teamlock
	 * @param teamId
	 * @param readonly
	 *            如果为true,那么只能读数据，不能修改数据
	 */
	Team(long teamId, boolean readonly) {

		this.teamId = teamId;
		this.readonly = readonly;
		if (readonly)
			teamInfo = xtable.Team.select(teamId);
		else
			teamInfo = xtable.Team.get(teamId);
		if (teamInfo == null)
			throw new IllegalArgumentException("错误的teamId：" + teamId);
		if(teamInfo.getCommanderroleid()==0)
			teamInfo.setCommanderroleid(teamInfo.getTeamleaderid());
	}
	
	public Team(long teamId, xbean.TeamInfo teamInfo, boolean readonly)
	{
		this.teamId = teamId;
		this.teamInfo = teamInfo;
		this.readonly = readonly;
		if(teamInfo.getCommanderroleid()==0)
			teamInfo.setCommanderroleid(teamInfo.getTeamleaderid());
	}
	
	public void SetCommanderRoleId(long roleid)
	{
		if(!isInTeam(roleid))
			return;
		
		if(roleid == teamInfo.getCommanderroleid())			
			return;
		
		xbean.Properties commanderprop = xtable.Properties.select(roleid);
		if (commanderprop != null)
		{	
			java.util.List<String> params = new java.util.ArrayList<String>();
			params.add(commanderprop.getRolename());		

			MessageMgr.sendMsgNotify(teamInfo.getTeamleaderid(), 180044, null);		
			if(teamInfo.getCommanderroleid() != teamInfo.getTeamleaderid())
				MessageMgr.sendMsgNotify(teamInfo.getCommanderroleid(), 180045, null);		

			teamInfo.setCommanderroleid(roleid);		
			SSetCommander send = new SSetCommander(roleid);
			mkdb.Procedure.psend(teamInfo.getTeamleaderid(), send);
			
			if(roleid == teamInfo.getTeamleaderid())
				MessageMgr.sendMsgNotify(teamInfo.getTeamleaderid(), 180042, null);		
			else
				MessageMgr.sendMsgNotify(teamInfo.getTeamleaderid(), 180043, params);		
				
			String msg1 = "您已经被队长委任成指挥"; 
			String msg2 = "成功委任"+commanderprop.getRolename()+"为指挥";
			final java.util.ArrayList<fire.pb.talk.DisplayInfo> showinfos  = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
			for (xbean.TeamMember member : teamInfo.getMembers()) {
				mkdb.Procedure.psend(member.getRoleid(), send);
				if(roleid == member.getRoleid())
				{
					MessageMgr.sendMsgNotify(member.getRoleid(), 180042, null);
					ChatChannel.getInstance().process(member.getRoleid(), fire.pb.talk.ChannelType.CHANNEL_TEAM,
							msg1, "指挥", showinfos, 0);
				}
				else
				{
					MessageMgr.sendMsgNotify(member.getRoleid(), 180043, params);
					ChatChannel.getInstance().process(member.getRoleid(), fire.pb.talk.ChannelType.CHANNEL_TEAM,
							msg2, "指挥", showinfos, 0);
				}
			}
		

		}	
	}
	
	public long  GetCommanderRoleId()
	{
		return teamInfo.getCommanderroleid();
	}

	public xbean.TeamInfo getTeamInfo() {

		return teamInfo;
	}

	public boolean isTeamLeader(long roleId) {

		if (roleId == teamInfo.getTeamleaderid())
			return true;
		else
			return false;
	}

	public boolean isTeamMember(long roleId) {

		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getRoleid() == roleId)
				return true;
		}
		return false;
	}

	/**
	 * 是不是正常队员，不包括队长
	 * 队长用isTeamLeader判断
	 * @param roleId
	 * @return
	 */
	public boolean isNormalMember(long roleId) {

		return isMemberState(roleId, TeamMemberState.eTeamNormal);
	}

	/**
	 * 是否是暂离队员
	 * ！注意：这里包括了归队中队员，因为归队中队员很大程度上与暂离的处理时一致的
	 * @param roleId
	 * @return
	 */
	public boolean isAbsentMember(long roleId) {

		return (isMemberState(roleId, TeamMemberState.eTeamAbsent) || isMemberState(roleId, TeamMemberState.eTeamReturn));
	}
	
	/**
	 * 是否是归队中队员
	 * @param roleId
	 * @return
	 */
	public boolean isReturnMember(long roleId) {

		return isMemberState(roleId, TeamMemberState.eTeamReturn);
	}

	
	/**
	 * 是否是掉线队员
	 * @param roleId
	 * @return
	 */
	public boolean isOfflineMember(long roleId) {

		return isMemberState(roleId, TeamMemberState.eTeamFallline);
	}

	public boolean isMemberState(long roleId, int memberState) {

		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getRoleid() == roleId) {
				if (member.getState() == memberState)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	/**
	 * 获取队伍中所有队员ID，包括队长
	 * 获取的ID保持队伍的顺序
	 * @return
	 */
	public List<Long> getAllMemberIds() {

		List<Long> ids = new ArrayList<Long>();
		ids.add(teamInfo.getTeamleaderid());
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			ids.add(member.getRoleid());
		}
		return ids;
	}
	
	/**
	 * 获取队伍中所有队员ID，包括队长
	 * 
	 * @return
	 */
	public Set<Long> getAllMemberIdSet() {

		Set<Long> ids = new HashSet<Long>();
		ids.add(teamInfo.getTeamleaderid());
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			ids.add(member.getRoleid());
		}
		return ids;
	}

	/**
	 * 获取所有在线的队伍成员Id（包括队长）
	 * 
	 * @return List<Long> 在线的队伍成员Id
	 */
	public List<Long> getOnlineMemberIds() {

		List<Long> result = new ArrayList<Long>();
		result.add(teamInfo.getTeamleaderid());
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getState() != TeamMemberState.eTeamFallline)
				result.add(member.getRoleid());
		}
		return result;
	}
	
	/**
	 * 获取所有正常状态的队伍成员Id（包括队长）。 为进入战斗时提供
	 * 
	 * @return List<Long> 正常状态的队伍成员Id
	 */
	public List<Long> getNormalMemberIds() {

		List<Long> result = new ArrayList<Long>();
		result.add(teamInfo.getTeamleaderid());
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getState() == TeamMemberState.eTeamNormal)
				result.add(member.getRoleid());
		}
		return result;
	}

	/**
	 * 获取所有能战斗状态的队伍成员Id（包括队长）。 为进入战斗时提供
	 * 
	 * @return List<Long> 正常状态的队伍成员Id
	 */
	public List<Long> getFighterMemberIds() {

		List<Long> result = new ArrayList<Long>();
		result.add(teamInfo.getTeamleaderid());
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getState() == TeamMemberState.eTeamNormal /*|| member.getState() == TeamMemberState.eTeamFallline*/)
				result.add(member.getRoleid());
		}
		return result;
	}	
	
	/**
	 * 获取所有离线队伍成员Id。 为进入战斗时提供
	 * 
	 * @return List<Long> 离线的队伍成员Id
	 */
	public List<Long> getOfflineMemberIds() {

		List<Long> result = new ArrayList<Long>();
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getState() == TeamMemberState.eTeamFallline)
				result.add(member.getRoleid());
		}
		return result;
	}

	/**
	 * 获取所有归队中队伍成员Id（不包括队长）。 为进入战斗时提供
	 * 
	 * @return List<Long> 归队中成员Id
	 */
	public List<Long> getReturningMemberIds() {

		List<Long> result = new ArrayList<Long>();
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getState() == TeamMemberState.eTeamReturn)
				result.add(member.getRoleid());
		}
		return result;
	}

	/**
	 * 获取所有暂离状态的队伍成员Id（肯定没有队长）。
	 * ！注意：这里包括了归队中队员，因为大部分的处理情况，暂离和归队中是一致的
	 * @return List<Long> 暂离状态的队伍成员Id
	 */
	public List<Long> getAbsentMemberIds() {

		List<Long> result = new ArrayList<Long>();
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getState() == TeamMemberState.eTeamAbsent || member.getState() == TeamMemberState.eTeamReturn)
				result.add(member.getRoleid());
		}
		return result;
	}
	
	public void setformLevel(){
		
		
		
	}

	/**
	 * 添加新队员（包括广播队员信息、广播排序）。 只能在Procedure中被调用！
	 * 
	 * @lock: 队长和新队员的rolelock
	 * @param newMemberRoleId
	 */
	public boolean addNewMemberWithSP(long newMemberRoleId) {

		if (readonly)
			return false;
		// 加入新队员
		// xbean.TeamInfo teamInfo = xtable.Team.get(teamId);
		if(!validAddNewMemeber(newMemberRoleId))
			return false;
		
		// 在队伍表中添加一个队员
		xbean.TeamMember member = xbean.Pod.newTeamMember();
		member.setRoleid(newMemberRoleId);
		member.setState(fire.pb.team.TeamMemberState.eTeamAbsent);
		teamInfo.getMembers().add(member);
		xtable.Roleid2teamid.add(newMemberRoleId, teamId);
				
		// 在队伍信息中删除该队员的邀请信息
		teamInfo.getInvitingids().remove(newMemberRoleId);
		// 在邀请表中删除该玩家的邀请信息
		xtable.Teaminvite.remove(newMemberRoleId);
		// 删除申请
		removeTeamApplyWithSendProtocol(newMemberRoleId);
		// 计算新队员的状态（暂离，归队中，正常）
		
		int memberstate = calculateMemberState(newMemberRoleId);
		member.setState(memberstate);

		/*
		if (memberstate == TeamMemberState.eTeamReturn)
		{
			xdb.Procedure.psendWhileCommit(newMemberRoleId, 160249, null);
		}
		*/
		
		new PShowPetOffProc(newMemberRoleId).call();//从召集令的“等待中角色状态”退出
		
		// 向所有队员（包括新队员）群发添加队员消息（包括排序）
		broadcastAddNewMember(newMemberRoleId);
		//发送队伍战斗指挥者
		SSetCommander send = new SSetCommander(teamInfo.getCommanderroleid());
		mkdb.Procedure.psend(newMemberRoleId, send);
		
		long leaderid = teamInfo.getTeamleaderid();
		if (leaderid != newMemberRoleId)
		{
			xbean.Properties leaderprop = xtable.Properties.select(leaderid);
			if (leaderprop != null)
			{
				java.util.List<String> params = new java.util.ArrayList<String>();
				params.add(leaderprop.getRolename());		
				MessageMgr.sendMsgNotify(newMemberRoleId, 141203, params);		
			}	
		}
		
		int changetype = (member.getState() == TeamMemberState.eTeamNormal)? TeamChangeType.ADD_NORMAL_MEMBER : TeamChangeType.ADD_ABSENT_MEMBER;
		GsClient.tSendWhileCommit(new GNotifyTeamChange(changetype, teamId, newMemberRoleId, 0));//通知地图模块
		// 通知地图模块队伍更新
		OnTeamStructChange(TeamManager.STRUCT_CHANGE_NEW_MEMBER, newMemberRoleId, 0);
		
		mkdb.Procedure.psendWhileCommit(newMemberRoleId, new SSetTeamFormation(teamInfo.getFormation(),getFormLevel(), (byte)0));
		TeamManager.logger.debug("队伍加入新队员,RoleId: " + newMemberRoleId + " ;TeamId: " + teamId);
		
		//如果组队满了的话，如果在匹配表里那么删除 by changhao
		if (teamInfo.getMembers().size() >= TeamManager.MAX_MEMBER_COUNT)
		{
			TeamManager.getInstance().delTeamMatch(getTeamLeaderId());
		}
		
		TeamManager.getInstance().delTeamMatch(newMemberRoleId);
		
		TeamManager.getInstance().sendCurTeamMatchStateByRoleId(getTeamLeaderId(), newMemberRoleId);		
						
		return true;
	}
	
	public int getFormLevel(){
		return teamInfo.getFormationlevel();
	}
	
	private boolean validAddNewMemeber(long newmemId)
	{
		BuffAgent buffagent = new BuffRoleImpl(newmemId,true);
		if(!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM))
		{
			TeamManager.logger.info("玩家(roleId=" + newmemId+")处于不能组队的状态");
			return false;
		}
		xbean.TeamFilter xfilter = xtable.Teamfilters.get(teamId);
		if(xfilter!= null)
			if(!xfilter.getFilter().checkEnterTeam(getTeamLeaderId(), newmemId))
				return false;
		return true;
	}
	
	public TeamFilter getFilter()
	{
		if(Transaction.current() == null)
			return null;
		xbean.TeamFilter xfilter = xtable.Teamfilters.get(teamId);
		if(xfilter == null)
			return null;
		return xfilter.getFilter();
	}



	/**
	 * 获得队伍的群发列表（不包括掉线的成员）
	 * 
	 * @return Set<Long> 队伍所有成员的IDs
	 */
	public Set<Long> getTeamBroadcastSet() {

		Set<Long> roleids = new HashSet<Long>();
		roleids.add(teamInfo.getTeamleaderid());
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getState() != TeamMemberState.eTeamFallline)
				roleids.add(member.getRoleid());
		}
		return roleids;
	}

	/**
	 * 删除一个非队长队员（包括群发删除队员协议） 只能在Procedure中被调用
	 * 
	 * @return false队伍中没有该队员
	 */
	public boolean removeTeamMemberWithSP(long leverRoleId, boolean active) {

		if (readonly)
			return false;
		Set<Long> roleIds = getTeamBroadcastSet();
		int i;
		for (i = 0; i < teamInfo.getMembers().size(); i++) {
			if (teamInfo.getMembers().get(i).getRoleid() == leverRoleId) {
				break;
			}
		}
		if (i >= teamInfo.getMembers().size()) {// 队伍中没找到该成员
			return false;
		} else {
			teamInfo.getMembers().remove(i);
			if(leverRoleId == teamInfo.getCommanderroleid())
				SetCommanderRoleId(teamInfo.getTeamleaderid());
		}
		
		if(teamInfo.getSwitchleaderid() == leverRoleId)
		{
			teamInfo.setSwitchleaderid(0);
			mkdb.Procedure.psendWhileCommit(teamInfo.getTeamleaderid(), new STeamError(TeamError.RefuseChangeLeader));
		}
		
		xtable.Roleid2teamid.remove(leverRoleId);
		SRemoveTeamMember sRemoveTeamMember = new SRemoveTeamMember();
		sRemoveTeamMember.memberids.add(leverRoleId);
		roleIds.remove(leverRoleId);
		if (roleIds.size() != 0)
			mkdb.Procedure.psendWhileCommit(roleIds, sRemoveTeamMember);
		// 离队的队员需要发解散队伍协议
		SDismissTeam sDismissTeam = new SDismissTeam();
		mkdb.Procedure.psendWhileCommit(leverRoleId, sDismissTeam);
		OnTeamStructChange(TeamManager.STRUCT_CHANGE_REMOVE_MEMBER, leverRoleId, 0);
		
		return true;
	}

	/**
	 * 解散队伍。请提前按从小到大锁rolelock，然后在调用。 只能在Peocedure中调用
	 * ！注意：这个方法不能被直接调用，解散队伍请使用PDismissTeam过程
	 * 
	 * @lock:队长和所有队员的rolelock
	 */
	public void dismissTeam() {
		
		Set<Long> roleIds = new HashSet<Long>();// 群发列表
		long leaderid = teamInfo.getTeamleaderid();
		
		TeamManager.getInstance().delTeamMatch(leaderid); //删除队长对应的队伍匹配 by changhao
		
		xtable.Roleid2teamid.remove(teamInfo.getTeamleaderid());
		if (StateCommon.isOnline(teamInfo.getTeamleaderid()))
			roleIds.add(teamInfo.getTeamleaderid());
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			xtable.Roleid2teamid.remove(member.getRoleid());
			
			if (xtable.Roleonoffstate.get(member.getRoleid()) != null && xtable.Roleonoffstate.get(member.getRoleid()) != TeamMemberState.eTeamFallline)
				roleIds.add(member.getRoleid());
		}
		xtable.Team.remove(teamId);// 删除表记录
		xtable.Teamfilters.remove(teamId);
		
		SDismissTeam sDismissTeam = new SDismissTeam();
		if (roleIds.size() != 0)
			mkdb.Procedure.psendWhileCommit(roleIds, sDismissTeam);// 群发解散队伍消息
		//notify scene
		GsClient.tSendWhileCommit(new GNotifyTeamChange(TeamChangeType.DISMISS, teamId, leaderid, 0));//通知地图模块
	}

	/**
	 * 查看该队员是否处于可以归队的范围内（与队长在10*10范围） 只能在Procedure中调用
	 * 
	 * @lock：队长和该队员的rolelock
	 * @return true=在范围内; false=在范围外
	 */
	public boolean isMemberInReturnScale(long memberRoleId) {

		long leaderRoleId = teamInfo.getTeamleaderid();
		fire.pb.map.Role leaderRole = RoleManager.getInstance().getRoleByID(leaderRoleId);
		fire.pb.map.Role memberRole = RoleManager.getInstance().getRoleByID(memberRoleId);

		if (leaderRole.getScene() != memberRole.getScene())
			return false;
		// Mapid相同
		MapConfig mapcfg = ConfigManager.getInstance().getConf(MapConfig.class).get((int)leaderRole.getScene());
		if(mapcfg == null)
			return false;
		
		if(mapcfg.visibletype == Scene.VISIBLE_SINGLE)//自己可见的场景，不能归队
			return false;
		
		if(memberRole.getPos().getZ() != leaderRole.getPos().getZ())
			return false;//不在同一层
		//验证归队直线上是否有阻挡点
		/*
		List<Position> poses = new LinkedList<Position>();
		poses.add(memberRole.getPos());
		poses.add(leaderRole.getPos());
		if(!MapUtil.checkWay((int)leaderRole.getScene(), poses, memberRole.getPos().getWalkBlockType()))
			return false;//归队时之间有阻挡点
		*/
		
		/*
		//距离是否合法
		int minusX2 = (leaderRole.getPos().getX() - memberRole.getPos().getX()) * (leaderRole.getPos().getX() - memberRole.getPos().getX());
		int minusY2 = (leaderRole.getPos().getY() - memberRole.getPos().getY()) * (leaderRole.getPos().getY() - memberRole.getPos().getY());
		if (minusX2 <= MAX_RETURN_SCALE_SQUARE && minusY2 <= MAX_RETURN_SCALE_SQUARE)
			return true;// 距离在5格之内
		*/
		
		return true;
	}

	/**
	 * 重新排列队员顺序，并向所有队员发顺序更新协议 如果顺序没有改变则不发协议 只能在Procedure中调用
	 * 
	 */
	public void updateMemberSequenceWithSendProtocol() {

		Set<Long> roleids = new HashSet<Long>();

		if (sequenceMembersByStatus(teamInfo.getMembers()))// 排序，这里可能重复排序了（为了保证正确，发送前还是排一次序）
		{
			SMemberSequence sMemberSequence = new SMemberSequence();
			sMemberSequence.teammemeberlist.add(teamInfo.getTeamleaderid());
			roleids.add(teamInfo.getTeamleaderid());
			for (xbean.TeamMember member : teamInfo.getMembers()) {
				sMemberSequence.teammemeberlist.add(member.getRoleid());
				roleids.add(member.getRoleid());
			}
			// 将队伍成员顺序发送到所有成员
			mkdb.Procedure.psendWhileCommit(roleids, sMemberSequence);
		}
	}

	/**
	 * 主动交换队长，原队长跟新队长必须都处于正常状态 主动交换队长后，原队长作为队员也处于正常状态，其他人不变 包括群发交换队长协议
	 * 只能在Procedure中被调用
	 * 
	 * @lock: 原队长和新队长的rolelock
	 * 
	 * @return true表示交换队长成功，fasle表示队伍中已经没有其他在线成员
	 */
	public boolean switchTeamLeaderWithSP(long newLeaderId) {

		BuffAgent buffagent = new BuffRoleImpl(newLeaderId);
		if(!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM_LEADER))
			return false;
		
		teamInfo.setSwitchleaderid(-1);// 不管是主动还是被动交换队长后，都需要清这个状态
		long oldLeaderId = teamInfo.getTeamleaderid();

		// 前队长构建新的队员
		xbean.TeamMember newMember = xbean.Pod.newTeamMember();
		newMember.setRoleid(oldLeaderId);
		newMember.setState(TeamMemberState.eTeamNormal);

		// 添加新队长,原队长放在新队长处
		teamInfo.setTeamleaderid(newLeaderId);
		for (int i = 0; i < teamInfo.getMembers().size(); i++) {
			if (teamInfo.getMembers().get(i).getRoleid() == newLeaderId) {
				teamInfo.getMembers().set(i, newMember);
				break;
			}
		}
		if(oldLeaderId == teamInfo.getCommanderroleid())
			SetCommanderRoleId(newLeaderId);
		// 换队长协议
		SSetTeamLeader sSetTeamLeader = new SSetTeamLeader();
		sSetTeamLeader.roleid = newLeaderId;
		// 群发交换队长信息
		mkdb.Procedure.psendWhileCommit(getTeamBroadcastSet(), sSetTeamLeader);
		// 发送给新队长申请列表
		refreshAllAppliersWithSendProtocol(newLeaderId);
		// 发送新队长的默认阵型
		PropRole role = new PropRole(newLeaderId, true);
		int formId = role.getDealutFormId();
		int formLevel = role.getFormLevel(formId);
		
		teamInfo.setFormation(formId);
		teamInfo.setFormationlevel(formLevel);
		changeFormationWithSP(formId, formLevel ,false);
		// 将队伍等级更改为默认
		/*
		if (teamInfo.getMinlevel() != 1 || teamInfo.getMaxlevel() != 155) {
			teamInfo.setMinlevel(1);
			teamInfo.setMaxlevel(155);
			SSetTeamLevel sSetTeamLevel = new SSetTeamLevel();
			sSetTeamLevel.minlevel = 1;
			sSetTeamLevel.maxlevel = 155;
			mkdb.Procedure.psendWhileCommit(getTeamBroadcastSet(), sSetTeamLevel);// 群发队伍等级需求变更
		}
		*/
		mkdb.Procedure.pexecuteWhileCommit(new PShowPetOffProc(oldLeaderId));
		OnTeamStructChange(TeamManager.STRUCT_CHANGE_SWITCH_LEADER, newLeaderId, oldLeaderId);
		
		teamInfo.setOnekeytimestamp(0);
		return true;
	}
	
	/**
	 * 被动交换队长（由下线、逃离战斗等触发），只发送更改状态协议 ，不发送设置队长协议 可能失败，没有其他在线成员，外面需要判断是解散队伍还是不换队长
	 * 
	 * @return true表示交换队长成功，fasle表示队伍中已经没有其他在线成员，交换队长失败
	 */
	private boolean passiveSwitchLeader() {

		if (teamInfo.getMembers().size() == 0 || teamInfo.getMembers().get(0).getState() == TeamMemberState.eTeamFallline)
			return false;// 第一个队员处于离线状态，后面的都是离线状态,无法交换队长
		
		long newLeaderId = 0 ;
		int newLeaderMemberSeq = 0;
		for(xbean.TeamMember xtm : teamInfo.getMembers())
		{
			if(xtm.getState() !=  TeamMemberState.eTeamFallline)
			{
				BuffAgent buffagent = new BuffRoleImpl(xtm.getRoleid());
				if(buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM_LEADER))
				{
					newLeaderId = xtm.getRoleid();
					break;
				}
			}
			newLeaderMemberSeq++;
		}
		if(newLeaderId == 0)
			return false;
		
		teamInfo.setSwitchleaderid(-1);// 不管是主动还是被动交换队长后，都需要清这个状态
		
		PropRole prole = new PropRole(newLeaderId, true);
		int formLevel = prole.getFormLevel(prole.getDealutFormId());
		teamInfo.setFormation(prole.getDealutFormId());
		teamInfo.setFormationlevel(formLevel);
		long oldLeaderId = teamInfo.getTeamleaderid();
		
		// 前队长构建新的队员
		xbean.TeamMember newMember = xbean.Pod.newTeamMember();
		newMember.setRoleid(oldLeaderId);

		// 添加新队长,原队长放在新队长处
		teamInfo.setTeamleaderid(newLeaderId);
		teamInfo.getMembers().set(newLeaderMemberSeq, newMember);
		if(oldLeaderId == teamInfo.getCommanderroleid())
			SetCommanderRoleId(newLeaderId);
		return true;
	}

	public boolean changeFormationWithSP(int newForm, int formLevel, boolean msg) {

		teamInfo.setFormation(newForm);
		teamInfo.setFormationlevel(formLevel);
		mkdb.Procedure.psendWhileCommit(getTeamBroadcastSet(), new SSetTeamFormation(newForm, formLevel, (byte)0));
		return true;
	}
	
	/**
	 * 是否所有队员都暂离（不包括队长) by changhao
	 * 
	 * 
	 */
	public boolean allMemberAbsent()
	{
		for(xbean.TeamMember xtm : teamInfo.getMembers())
		{
			if(xtm.getState() !=  TeamMemberState.eTeamAbsent)
			{
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 被动交换队长 可能失败，没有其他在线成员，外面需要判断是解散队伍还是不换队长 (包括群发交换队长协议,群发当前顺序)
	 * 
	 * @return true表示交换队长成功，fasle表示队伍中已经没有其他可以做队长的队员，交换队长失败
	 */
	public boolean passiveSwitchLeaderWithSP(int leaderNewState) {

		long oldleader = teamInfo.getTeamleaderid();
		if (!passiveSwitchLeader()) //
		{
			if (leaderNewState == -1 || leaderNewState == TeamMemberState.eTeamFallline) //没有队员交换直接解散 by changhao
			{
				TeamManager.getInstance().delTeamMatch(oldleader);				
			}
			
			return false;
		}
		// 群发交换队长协议
		SSetTeamLeader sSetTeamLeader = new SSetTeamLeader();
		sSetTeamLeader.roleid = teamInfo.getTeamleaderid();
		mkdb.Procedure.psendWhileCommit(getTeamBroadcastSet(), sSetTeamLeader);
		OnTeamStructChange(TeamManager.STRUCT_CHANGE_PASSIVE_SWITCH_LEADER, teamInfo.getTeamleaderid(), oldleader);
		
		// 发送给新队长申请者列表
		refreshAllAppliersWithSendProtocol(teamInfo.getTeamleaderid());
		// 发老队长的新状态
		if (leaderNewState >= 0)
			setTeamMemberState(oldleader, leaderNewState);
		mkdb.Procedure.pexecuteWhileCommit(new PShowPetOffProc(oldleader));
		// 设置所有归队中状态为暂离状态
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getState() == TeamMemberState.eTeamReturn)
				setTeamMemberState(member.getRoleid(), TeamMemberState.eTeamAbsent);
		}
		// 群发当前队员顺序
		updateMemberSequenceWithSendProtocol();// 可能要重排序
		// 发送新阵型
		PropRole prole = new PropRole(teamInfo.getTeamleaderid(), true);
		
		int formId = prole.getDealutFormId();
		int level = prole.getFormLevel(formId);
		teamInfo.setFormation(formId);
		teamInfo.setFormationlevel(level);
		
		mkdb.Procedure.psendWhileCommit(getTeamBroadcastSet(), new SSetTeamFormation(formId,level, (byte)0));
		// 将队伍等级更改为默认
		if (teamInfo.getMinlevel() != 1 || teamInfo.getMaxlevel() != 155) {
			teamInfo.setMinlevel(1);
			teamInfo.setMaxlevel(155);
			SSetTeamLevel sSetTeamLevel = new SSetTeamLevel();
			sSetTeamLevel.minlevel = 1;
			sSetTeamLevel.maxlevel = 155;
			mkdb.Procedure.psendWhileCommit(getTeamBroadcastSet(), sSetTeamLevel);// 群发队伍等级需求变更
		}
		
		//TeamManager.getInstance().swapRoleidTeamMatch(teamInfo.getTeamleaderid(), oldleader, teamId); //队长更换交换匹配 by changhao
		TeamManager.getInstance().delTeamMatch(oldleader); //交换队长停止自动匹配 by changhao
		
		TeamManager.logger.debug("队伍：" + teamId + " 被动交换队长，新队长ID：" + teamInfo.getTeamleaderid());
		return true;
	}

	/**
	 * 删除一条申请入队信息 只能在Procedure中调用
	 * 
	 * @return true表示删除申请入队信息成功，fasle表示没有该申请
	 */
	public boolean removeTeamApplyWithSendProtocol(long applierRoleId) {

		Long time = teamInfo.getApplierids().remove(applierRoleId);
		if (time != null)
		{
			SRemoveTeamApply sRemoveTeamApply = new SRemoveTeamApply();
			sRemoveTeamApply.applyids.add(applierRoleId);
			mkdb.Procedure.psendWhileCommit(teamInfo.getTeamleaderid(), sRemoveTeamApply);
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 删除超时申请 只能在Procedure中调用
	 */
	public void removeTimeoutTeamApplys() {

		long now = System.currentTimeMillis();
		List<Long> rmroleIds = new ArrayList<Long>();
		for (Long applierRoleId : teamInfo.getApplierids().keySet()) {
			if ((now - teamInfo.getApplierids().get(applierRoleId)) > TeamManager.MAX_ARRLY_TIMEOUT)
				rmroleIds.add(applierRoleId);
		}
		for(long rmroleId :rmroleIds)
			teamInfo.getApplierids().remove(rmroleId);
	}

	/**
	 * 队员下线，更新队伍信息，提供给下线模块用 只能在Procedure中调用
	 * 
	 * @lock: 角色的rolelock
	 * 
	 * @return true表示成员下线更新队伍信息成功，fasle表示没有该成员
	 */
	public boolean roleOffline(long roleId) {

		if (roleId == teamInfo.getTeamleaderid()) {
			// 队长下线，被动交换队长
			if (passiveSwitchLeaderWithSP(TeamMemberState.eTeamFallline)) {

			} else {
				// 交换不成功，解散队伍
				PDisMissTeam pDisMissTeam = new PDisMissTeam(teamId,PDisMissTeam.REASON_SYSTEM);
				mkdb.Procedure.pexecute(pDisMissTeam);
			}
			return true;
		} 
		else 
		{
			// 队员下线，先更新状态，然后更新顺序
			boolean isMemberExist = setTeamMemberStateWithSP(roleId, TeamMemberState.eTeamFallline);
			if (isMemberExist) {
//				updateMemberSequenceWithSendProtocol();
				return true;
			}
		}
		return false;// 角色不在队伍中
	}
	/**
	 * 设置队伍的状态和玩法ID
	 * @param teamstate
	 */
	public void setTeamState(int teamstate, int smapId)
	{
		teamInfo.setState(teamstate);
		teamInfo.setSmapid(smapId);
		mkdb.Procedure.psendWhileCommit(getTeamBroadcastSet(),new SSetTeamState(teamstate,smapId));
	}
	/**
	 * 队员上线，更新队伍信息，提供给上线模块用 只能在Procedure中调用
	 * 
	 * @lock: 角色的rolelock
	 * 
	 * @return true表示成员上线更新队伍信息成功，fasle表示没有该成员
	 */
	public boolean roleOnline(long roleId)
	{
		if (!isInTeam(roleId))
			return false;// 角色不在队伍中
		
		// 队员上线，先更新状态，然后更新顺序
		// 建立队伍信息发给上线队员
		SCreateTeam sCreateTeam = new SCreateTeam();
		sCreateTeam.teamid = teamId;
		sCreateTeam.formation = teamInfo.getFormation();
		sCreateTeam.teamstate = teamInfo.getState();
		sCreateTeam.smapid = teamInfo.getSmapid();
		mkdb.Procedure.psendWhileCommit(roleId, sCreateTeam);

		// 所有队员信息(包括新队员和队长的信息)，需要发给上线队员
		SAddTeamMember sAddTeamMemeber = new SAddTeamMember();
		sAddTeamMemeber.memberlist.add(getTeamMemeberBasic(teamInfo.getTeamleaderid()));
		for (xbean.TeamMember member : teamInfo.getMembers())
		{
			sAddTeamMemeber.memberlist.add(getTeamMemeberBasic(member.getRoleid()));
		}
		mkdb.Procedure.psendWhileCommit(roleId, sAddTeamMemeber);
		// 变更状态，并群发
		//如果玩家真离线上限改为暂离状态
		int teamstate = getTeamMemberState(roleId);
		if (teamstate == TeamMemberState.eTeamFallline)
		{
			if (xtable.Roleid2battleid.get(roleId) == null)
			{
				setTeamMemberStateWithSP(roleId, TeamMemberState.eTeamAbsent);				
			}
			else
			{
				setTeamMemberStateWithSP(roleId, TeamMemberState.eTeamReturn);					
			}
		}
		
		// 自己的更新给其他人
		updateTeamMemberBasic2Others(roleId);
		mkdb.Procedure.psendWhileCommit(roleId, new SSetTeamFormation(teamInfo.getFormation(), getFormLevel(), (byte) 0));
		// 是队长才发
		if (teamInfo.getTeamleaderid() == roleId)
		{
			mkdb.Procedure.pexecuteWhileCommit(new PSendTeamApplierids(teamInfo.getTeamleaderid()));
		}
		
		SSetCommander send2 = new SSetCommander(GetCommanderRoleId());
		mkdb.Procedure.psend(roleId, send2);
		
		TeamManager.getInstance().sendCurTeamSetInfo(roleId);
		
		TeamManager.getInstance().sendCurTeamMatchStateByRoleId(teamInfo.getTeamleaderid(), roleId);
		
		return true;
	}

	// 将队员按状态排序
	private boolean sequenceMembersByStatus(List<xbean.TeamMember> members) {

		boolean sequenceChanged = false;
		for (int i = 0; i < members.size(); i++) {
			for (int j = i; j > 0; j--) {
				xbean.TeamMember frontMember = members.get(j - 1);
				xbean.TeamMember backMember = members.get(j);

				if (frontMember.getState() == TeamMemberState.eTeamNormal)
					break;// 前面的是正常状态，不换位置
				else if ((frontMember.getState() == TeamMemberState.eTeamReturn || frontMember.getState() == TeamMemberState.eTeamAbsent)
						&& backMember.getState() != TeamMemberState.eTeamNormal)
					break;// 前面是归队中/暂离状态，后面不是正常状态，不换位置
				else if (frontMember.getState() == TeamMemberState.eTeamFallline && backMember.getState() == TeamMemberState.eTeamFallline)
					break;// 前面是离线状态，后面也是离线状态，不换位置
				else {
					members.set(j - 1, backMember.copy());
					members.set(j, frontMember.copy());
					sequenceChanged = true;
				}
			}
		}
		return sequenceChanged;
	}

	/**
	 * 计算一个队员的状态 只能在Procedure中调用
	 * 
	 * @lock: 队长和队员的rolelock
	 * @param long memberRoleId 成员角色ID
	 * @return TeamMemberState
	 */
	private int calculateMemberState(long memberRoleId) {

		if (isMemberInReturnScale(memberRoleId)) {
			// 距离在5格之内，可以变状态为正常或回归中
			// TODO 还需要判断队长的状态，战斗中需要进入归队中状态
			BuffAgent brole = new BuffRoleImpl(memberRoleId);
			if(fire.pb.buff.Module.existState(teamInfo.getTeamleaderid(), BuffConstant.StateType.STATE_BATTLE_FIGHTER)||
					fire.pb.buff.Module.existState(teamInfo.getTeamleaderid(), BuffConstant.StateType.STATE_REPLAY)||
					fire.pb.buff.Module.existState(teamInfo.getTeamleaderid(), BuffConstant.StateType.STATE_BATTLE_WATCHER))
				return TeamMemberState.eTeamReturn;
			if(brole.canAddBuff(BuffConstant.StateType.STATE_TEAM_MEMBER_NORMAL))
				return TeamMemberState.eTeamNormal;
			else
				return TeamMemberState.eTeamAbsent;
		} else
			return TeamMemberState.eTeamAbsent;
	}

	// 广播添加新队员的协议，带队员排序。只能在Procedure中被调用！
	private void broadcastAddNewMember(long newMemberRoleId) {

		// xbean.TeamInfo team = xtable.Team.get(inviterTeamId);
		// 新成员的信息
		SAddTeamMember sAddTeamMemeber = new SAddTeamMember();
		sAddTeamMemeber.memberlist.add(getTeamMemeberBasic(newMemberRoleId));// 先放入新队员的信息
		Set<Long> roleids = new HashSet<Long>();// 老队员ids
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			roleids.add(member.getRoleid());
		}
		roleids.add(teamInfo.getTeamleaderid());// 添加队长id
		roleids.remove(newMemberRoleId);// 去掉新队员的id(如果是新建队伍时，队长就是新队员)

		

		// ！将新成员的信息广播到所有老成员
		if (roleids.size() != 0)// 新建队伍时没有老成员
		{
			mkdb.Procedure.psendWhileCommit(roleids, sAddTeamMemeber);
		}

		// 建立队伍信息发给新队员
		SCreateTeam sCreateTeam = new SCreateTeam();
		sCreateTeam.teamid = teamId;
		sCreateTeam.formation = teamInfo.getFormation();
		sCreateTeam.teamstate = teamInfo.getState();
		sCreateTeam.smapid = teamInfo.getSmapid();
		mkdb.Procedure.psendWhileCommit(newMemberRoleId, sCreateTeam);

		// 所有队员信息(包括新队员和队长的信息)，需要发给新队员
		SAddTeamMember sAddTeamMemeber2 = new SAddTeamMember();
		sAddTeamMemeber2.memberlist.add(getTeamMemeberBasic(teamInfo.getTeamleaderid()));
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			sAddTeamMemeber2.memberlist.add(getTeamMemeberBasic(member.getRoleid()));
		}
		// ！将当前所有队员的信息发给新队员
		mkdb.Procedure.psendWhileCommit(newMemberRoleId, sAddTeamMemeber2);
		// ！广播队员顺序
		if (roleids.size() != 0)// 新建队伍时不用对队长发顺序
			updateMemberSequenceWithSendProtocol();
	}

	/**
	 * 获取一个角色的TeamMemberBasic（协议定义的队伍成员结构） 只能在Procedure中调用
	 * 
	 * @lock: 角色的rolelock
	 * @param long memberRoleId 成员角色ID
	 * @return TeamMemberBasic
	 */
	public TeamMemberBasic getTeamMemeberBasic(long memberRoleId) {

		TeamMemberBasic memberBasic = new TeamMemberBasic();
		memberBasic.roleid = memberRoleId;
		memberBasic.state = getTeamMemberState(memberRoleId);
		if(memberBasic.state == TeamMemberState.eTeamReturn)
			memberBasic.state = TeamMemberState.eTeamAbsent;
		xbean.Properties newProperty = xtable.Properties.select(memberRoleId);
		if (newProperty != null) {
			//memberBasic.face = newProperty.getFaction();
			memberBasic.level = newProperty.getLevel();
			memberBasic.rolename = newProperty.getRolename();
			memberBasic.school = newProperty.getSchool();
			memberBasic.shape = newProperty.getShape();
			//memberBasic.model = fire.pb.map.Role.getPlayerModel(memberRoleId);
			fire.pb.map.Role.getPlayerComponents(memberRoleId, memberBasic.components);
			// memberBasic.title = newProperty.getTitle();
		}
		//if (memberBasic.state != TeamMemberState.eTeamFallline) {
		final fire.pb.effect.Role calAttr = new fire.pb.effect.RoleImpl(memberRoleId, true);
		memberBasic.hp = calAttr.getHp();
		memberBasic.mp = calAttr.getMp();
		memberBasic.maxhp = calAttr.getMaxHp();
		memberBasic.maxmp = calAttr.getMaxMp();
		memberBasic.hugindex = getHugIndex(memberRoleId);
		fire.pb.map.Role role = RoleManager.getInstance().getRoleByID(memberRoleId);
		if (role != null)
		{
			memberBasic.sceneid = role.getScene();
			memberBasic.pos.x = role.getPos().getX();
			memberBasic.pos.y = role.getPos().getY();
		} else
		{
			memberBasic.sceneid = 0;
			memberBasic.pos.x = 0;
			memberBasic.pos.y = 0;
		}
		
		return memberBasic;
	}
	
	/**
	 * 获取一个角色的TeamMemberSimple（协议定义的队伍成员结构） 只能在Procedure中调用 简单的成员数据  by changhao
	 * 
	 * @lock: 角色的rolelock
	 * @param long memberRoleId 成员角色ID
	 * @return TeamMemberBasic
	 */
	public TeamMemberSimple getTeamMemeberSimple(long memberRoleId) {

		TeamMemberSimple member = new TeamMemberSimple();
		member.roleid = memberRoleId;
		xbean.Properties newProperty = xtable.Properties.select(memberRoleId);
		if (newProperty != null) {
			member.level = newProperty.getLevel();
			member.rolename = newProperty.getRolename();
			member.school = newProperty.getSchool();
			member.shape = newProperty.getShape();
		}
		
		return member;
	}
	
	public byte getHugIndex(long roleId)
	{
		List<Long> rids = getNormalMemberIds();
		if(!rids.contains(roleId))
			return (byte)0;
		
		for(Iterator<Map.Entry<Long, Long>>it = getTeamInfo().getHugs().entrySet().iterator(); it.hasNext();)
		{
			Map.Entry<Long, Long> entry = it.next();
			if(!rids.contains(entry.getKey())
					|| !rids.contains(entry.getValue()))
			{
				it.remove();
				continue;
			}
			if(entry.getKey() == roleId)
			{
				int index = rids.indexOf(entry.getValue().longValue());
				if(index < 0)
					return (byte)0;
				return (byte)(index + 1);
			}
			if(entry.getValue() == roleId)
			{
				int index = rids.indexOf(entry.getKey().longValue());
				if(index < 0)
					return (byte)0;
				return (byte)(index + 1);
			}
		}
		return (byte)0;
	}
	
	public boolean canHug(long roleid1, long roleid2)
	{
		xbean.Properties prop1 = xtable.Properties.select(roleid1);
		if(isHugging(roleid1))
		{
			//TODO 某人正在拥抱
			return false;
		}
		if(prop1.getSex() == 2 && isTeamLeader(roleid1))
		{
			//TODO 队长不能是女的
			return false;
		}
		if(!isTeamLeader(roleid1) && !isNormalMember(roleid1))
		{
			//不是归队中
			MessageMgr.psendMsgNotify(roleid1, 144618 , null);
			return false;
		}
		
		xbean.Properties prop2 = xtable.Properties.select(roleid2);
		if(isHugging(roleid2))
		{
			//某人正在拥抱
			MessageMgr.psendMsgNotify(roleid1, 144621 , null);
			return false;
		}
		if(prop2.getSex() == 2 && isTeamLeader(roleid2))
		{
			//队长不能是女的
			MessageMgr.psendMsgNotify(roleid1, 144622 , null);
			return false;
		}
		if(!isTeamLeader(roleid2) && !isNormalMember(roleid2))
		{
			//不是归队中
			MessageMgr.psendMsgNotify(roleid1, 144618 , null);
			return false;
		}
		
		
		if(prop1.getSex() == prop2.getSex())
		{
			//同性不能抱
			MessageMgr.psendMsgNotify(roleid1, 144619 , null);
			return false;
		}
		
		return true;
	}
	
	
	public boolean isHugging(long roleId)
	{
		for(Map.Entry<Long, Long> entry : getTeamInfo().getHugs().entrySet())
		{
			if(entry.getKey() == roleId
					||entry.getValue() == roleId)
				return true;
		}
		return false;
	}
	
	/**
	 * 获取一个成员的当前状态
	 * ！注意：如果是队长，返回的是正常状态
	 * 
	 * @param memberRoleId
	 * @return TeamMemberState(-1为该队伍中无该角色)
	 */
	public int getTeamMemberState(long memberRoleId) {

		if (teamInfo.getTeamleaderid() == memberRoleId)
			return TeamMemberState.eTeamNormal;
		else
			for (xbean.TeamMember member : teamInfo.getMembers()) {
				if (member.getRoleid() == memberRoleId)
					return member.getState();
			}
		return -1;
	}


	/**
	 * 设置队员状态，队长不会被设置状态，设置队长时返回false 只能在Procedure中被调用 包括群发队员更新状态协议
	 * 
	 * @param memberRoleId
	 * @param newState
	 * @return true= 更新成功， false=未找到该队员；或者状态一致，未更新
	 */
	public boolean setTeamMemberState(long memberRoleId, int newState) {

		xbean.TeamMember teammember = null;
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			if (member.getRoleid() == memberRoleId && member.getState() != newState) {
				teammember = member;
			}
		}
		if (teammember != null) {
			TeamManager.logger.debug("设置队员新状态：" + newState);
			BuffAgent buffagent = new BuffRoleImpl(memberRoleId);
			switch (newState)
			{
			case TeamMemberState.eTeamNormal:
				if (!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM_MEMBER_NORMAL))
					return false;
				break;
			case TeamMemberState.eTeamAbsent:
				if (!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM_MEMBER_ABSENT))
					return false;
				break;
			case TeamMemberState.eTeamReturn:
				if (!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM_MEMBER_RETURN))
					return false;
				break;
			case TeamMemberState.eTeamFallline:
				if(memberRoleId == teamInfo.getCommanderroleid())			
					SetCommanderRoleId(teamInfo.getTeamleaderid());
				break;
			}
			teammember.setState(newState);
			if(newState != TeamMemberState.eTeamReturn)
			{//归队中不用发送任何提示
				SUpdateMemberState sUpdateMemberState = new SUpdateMemberState();
				sUpdateMemberState.roleid = memberRoleId;
				sUpdateMemberState.state = newState;
				mkdb.Procedure.psendWhileCommit(getTeamBroadcastSet(), sUpdateMemberState);
			}
			OnTeamStructChange(newState, memberRoleId, 0);
			return true;
		} else
			return false;
	}
	
	/**
	 * 设置队员状态，队长不会被设置状态，设置队长时返回false 只能在Procedure中被调用 包括群发队员更新状态协议
	 * 包括群发重新排序协议
	 * @param memberRoleId
	 * @param newState
	 * @return true= 更新成功， false=未找到该队员；或者状态一致，未更新
	 */
	public boolean setTeamMemberStateWithSP(long memberRoleId, int newState) {

		boolean sucess = setTeamMemberState(memberRoleId, newState);
		if(sucess)
		{
			updateMemberSequenceWithSendProtocol();
			return true;
		}
		else
			return false;
	}

	/**
	 * 按从小到大顺序获取队伍中所有角色ID的rolelock id
	 * 
	 * @return 从小到大有序的role id
	 */
	public Long[] getMembersSequenceRolelocks() {

		Long[] memberIds = new Long[teamInfo.getMembers().size() + 1];
		memberIds[0] = teamInfo.getTeamleaderid();
		for (int i = 1; i < memberIds.length; i++) {
			memberIds[i] = teamInfo.getMembers().get(i - 1).getRoleid();
		}
		return TeamManager.getSequenceRolelocks(memberIds);
	}

	/**
	 * 判断角色是否在队伍中
	 * 
	 * @return true = 在队伍中（队长或队员），false = 不在队伍中
	 */
	public boolean isInTeam(long roleId) {

		if (teamInfo.getTeamleaderid() == roleId)
			return true;
		else {
			for (xbean.TeamMember member : teamInfo.getMembers()) {
				if (member.getRoleid() == roleId)
					return true;
			}
		}
		return false;
	}

	/**
	 * 将当前的申请列表发到某个队员，用于换队长时发给新队长申请列表
	 * 
	 * @param roleId
	 *            想要发送到的队员ID
	 * 
	 */
	public void refreshAllAppliersWithSendProtocol(long roleId)
	{
		mkdb.Procedure.pexecuteWhileCommit(new PSendAndModifyTeamApplierids(roleId));
	}

	/**
	 * 检查队伍申请列表未满（15个） 可能会发送SRemoveApply协议删除过期的申请
	 */
	public boolean isApplyListFull() {

		// 先清除过期申请
		removeTimeoutTeamApplys();
		if (teamInfo.getApplierids().size() < TeamManager.MAX_TEAM_APPLIER_COUNT)
			return false;
		else
			return true;
	}
	
	/**
	 * 交换队员，没有任何验证，直接交换
	 */
	public void switchTeamMemberWithSP(int index1, int index2)
	{
		long roleId1 = getTeamInfo().getMembers().get(index1-1).getRoleid();
		long roleId2 = getTeamInfo().getMembers().get(index2-1).getRoleid();
		getTeamInfo().getMembers().get(index2-1).setRoleid(roleId1);
		getTeamInfo().getMembers().get(index1-1).setRoleid(roleId2);
		
		//将新顺序广播到所有队员
		Set<Long> roleids = new HashSet<Long>();
		SMemberSequence sMemberSequence = new SMemberSequence();
		sMemberSequence.teammemeberlist.add(getTeamInfo().getTeamleaderid());
		roleids.add(getTeamInfo().getTeamleaderid());
		for (xbean.TeamMember member : getTeamInfo().getMembers())
		{
			sMemberSequence.teammemeberlist.add(member.getRoleid());
			roleids.add(member.getRoleid());
		}
		// 将队伍成员顺序发送到所有成员
		mkdb.Procedure.psendWhileCommit(roleids, sMemberSequence);
		OnTeamStructChange(TeamManager.STRUCT_CHANGE_SWITCH_MEMBER, roleId1, roleId2);
	}

	/**
	 * 队伍结构发生变化
	 * 
	 * @param reason
	 *            参考TeamManager.STRUCT_CHANGE_XXX，其中1-4对应成员状态变化
	 * @param roleid
	 *            不同情况下对应的角色Id.如果是换队长,涉及到两个id,则此id为新队长的roleid
	 * @param oldRoleid
	 *            如果是换队长,涉及到两个id,则此id为原队长的roleid.其他情况为0
	 */
	private void OnTeamStructChange(final int reason, final long newRoleId, final long oldRoleid) {

		if(readonly)
			throw new RuntimeException("只读的情况下不能引起队伍结构变化！！");
		
		// 更新地图模块
		updateTeamToMap(reason,newRoleId,oldRoleid);

		// 取消召集令的询问中队伍状态
		long oldLeaderId = oldRoleid;
		if(oldLeaderId == 0)
			oldLeaderId = getTeamLeaderId();
	}

	private void updateTeamToMap(final int reason, final long newRoleId, final long oldRoleid)
	{
		GNotifyTeamChange gchange = new GNotifyTeamChange();
		gchange.teamid = teamId;
		gchange.roleid = newRoleId;
		gchange.roleid2 = oldRoleid;
		switch(reason)
		{
		case TeamManager.STRUCT_CHANGE_SWITCH_LEADER:
		case TeamManager.STRUCT_CHANGE_PASSIVE_SWITCH_LEADER:
			gchange.changetype = TeamChangeType.SWITCH_LEADER;
			break;
		case TeamManager.STRUCT_CHANGE_REMOVE_MEMBER:
			gchange.changetype = TeamChangeType.REMOVE_MEMBER;
			break;
		case TeamManager.STRUCT_CHANGE_SWITCH_MEMBER:
			gchange.changetype = TeamChangeType.SWITCH_MEMBER;
			break;
		case TeamManager.STRUCT_CHANGE_MEMBER_STATE_NORMAL:
			gchange.changetype = TeamChangeType.CHANGE_MEMBER_NORMAL;
			break;
		case TeamManager.STRUCT_CHANGE_MEMBER_STATE_ABSENT:
			gchange.changetype = TeamChangeType.CHANGE_MEMBER_ABSENT;
			break;
		case TeamManager.STRUCT_CHANGE_MEMBER_STATE_OFFLINE:
			gchange.changetype = TeamChangeType.MEMBER_OFFLINE;
			break;
		default:
			break;
		}
		if(gchange.changetype != 0)
			GsClient.tSendWhileCommit(gchange);
		
		
		/*GUpdateTeam updateTeam = new GUpdateTeam();
		updateTeam.sceneteam.teamid = teamId;
		updateTeam.sceneteam.leaderid = teamInfo.getTeamleaderid();
		updateTeam.newmemberid.put(teamInfo.getTeamleaderid(), TeamMemberState.eTeamNormal);
		
		StringBuilder sb = new StringBuilder("updateTeamToMap : teamid = ").append(teamId);
		
		for (xbean.TeamMember member : teamInfo.getMembers()) {
			int teamstate = (member.getState() == TeamMemberState.eTeamReturn)? TeamMemberState.eTeamAbsent : member.getState();
			SceneTeamMember stm = new SceneTeamMember( member.getRoleid(), teamstate);
			updateTeam.sceneteam.memebers.add(stm);
			updateTeam.newmemberid.put(member.getRoleid(), teamstate);
			sb.append("; roleid=").append(stm.roleid).append("; state=").append(stm.state);
		}
		TeamManager.logger.debug(sb.toString());
//		GsClient.pSendWhileCommit(updateTeam);
		GsClient.tSendWhileCommit(updateTeam);*/
//		SceneTeamManager.getInstance().onUpdateTeam(mapTeam, newMemberIds);
	}
	
	public void notifyHpMpChange(long roleId)
	{
		Set<Long> roleIds = getAllMemberIdSet();
		xbean.Properties prop = xtable.Properties.select(roleId);
		if(prop == null)
			return;
		
		SUpdateMemberHPMP update = new SUpdateMemberHPMP(roleId,prop.getHp(),prop.getMp());
		gnet.link.Onlines.getInstance().send(roleIds, update);
	}

	public long getTeamLeaderId()
	{
		return teamInfo.getTeamleaderid();
	}
	
	public static int TEAM_VOLUME = 5;
	
	public void updateTeamMemberBasic2Others(long memberRoleId)
	{
		TeamMemberBasic memberbasic = getTeamMemeberBasic(memberRoleId);
		if(memberbasic == null)
			return;
		Set<Long> roleids = getTeamBroadcastSet();
		roleids.remove(memberRoleId);
		if(Transaction.current()!= null)
			mkdb.Procedure.psendWhileCommit(roleids,new SUpdateTeamMemberBasic(memberbasic));
		else
			gnet.link.Onlines.getInstance().send(roleids,new SUpdateTeamMemberBasic(memberbasic));
	}
	
	public void updateTeamMemberComponents2Others(long memberRoleId) {
		Set<Long> roleids = getTeamBroadcastSet();
		roleids.remove(memberRoleId);
		SUpdateTeamMemberComponent send = new SUpdateTeamMemberComponent();
		send.memberid = memberRoleId;
		fire.pb.map.Role.getPlayerComponents( memberRoleId, send.components);
		if(Transaction.current()!= null)
			mkdb.Procedure.psendWhileCommit( roleids, send);
		else
			gnet.link.Onlines.getInstance().send( roleids, send);
		
	}
	
	public long getTeamId(){
		return teamId;
	}
}

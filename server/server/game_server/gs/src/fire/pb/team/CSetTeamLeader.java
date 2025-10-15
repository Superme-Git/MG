
package fire.pb.team;
import fire.pb.StateCommon;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetTeamLeader__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 设置队长
 * @author changhao
 *
 */
public class CSetTeamLeader extends __CSetTeamLeader__ {
	private long now = 0L;//procedure开始时保存一个当前时间，保证此procedure中时间的统一性
	
	Team team;
	@Override
	protected void process() {
		// protocol handle
		TeamManager.logger.debug("Enter: " + this.getClass());
		final long oldLeaderRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (oldLeaderRoleId < 0)
			return;
		final long newLeaderRoleId = roleid;
		mkdb.Procedure setTeamLeaderP = new mkdb.Procedure()
		{

			@Override
			protected boolean process()
			{
				//lock start 
				Long teamId = xtable.Roleid2teamid.select(oldLeaderRoleId);
				//先验证队伍是否为空
				if(teamId != null)
					team = new Team(teamId,false);
				else
					return true;
				if(!team.isTeamLeader(oldLeaderRoleId))
					return true;//验证原队长是否还是队伍的队长
				if(!team.isInTeam(newLeaderRoleId))
					return true;//验证新队长是否在队伍中
				Long[] roleids = new Long[2];
				if(oldLeaderRoleId < newLeaderRoleId)
				{
					roleids[0] = oldLeaderRoleId;
					roleids[1] = newLeaderRoleId;
				}
				else
				{
					roleids[0] = newLeaderRoleId;
					roleids[1] = oldLeaderRoleId;
				}
				this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK,(Object[])roleids));
				//lock end

				now = System.currentTimeMillis();

				if (checkPvP(oldLeaderRoleId, newLeaderRoleId) != 0) {
					return true;
				}

				if(!checkLeaderOnline(oldLeaderRoleId))
				{
					//申请者不在线（illegal）
					TeamManager.logger.debug("FAIL:申请者不在线,LeaderID: " +oldLeaderRoleId);
				}
				else if(!checkTeamStatusValid(team))
				{
					//队伍处于不可以换队长的状态（战斗中不能换队长）(illegal)
					TeamManager.logger.debug("FAIL:队伍处于不可以换队长的状态（例如飞行、战斗）,teamId: " +teamId);
				}
				else if(!checkTeamNotInSwitchStatus(team))
				{
					//队伍处于更换队长申请状态
					//psend(oldLeaderRoleId, new STeamError(TeamError.InChangeLeaderStatus));
					fire.pb.talk.MessageMgr.psendMsgNotify(oldLeaderRoleId, 141210, null);
					TeamManager.logger.debug("FAIL:队伍处于更换队长申请状态,teamId: " +teamId);
				}
				else if(!checkTeamNoSuccSwitchIn2min(team))
				{
					//队伍2分钟内成功更换过队长
					//psend(oldLeaderRoleId, new STeamError(TeamError.ChangeLeaderInCD));
					fire.pb.talk.MessageMgr.psendMsgNotify(oldLeaderRoleId, 141209, null);
					TeamManager.logger.debug("FAIL:队伍2分钟内成功更换过队长,teamId: " +teamId);
				}
				else if(!checkNewLeaderNormal(team, newLeaderRoleId))
				{
					//新队长不处于正常状态(暂离、离线等状态)（illgal）
					psend(newLeaderRoleId, new STeamError(TeamError.MembersNotNormal));
					TeamManager.logger.debug("FAIL:新队长不处于正常状态,newLeaderRoleId: " +newLeaderRoleId);
				}
				else if(StateCommon.isTrusteeshipState(newLeaderRoleId)) //如果在新队长在托管状态中 by changhao
				{
					fire.pb.talk.MessageMgr.psendMsgNotify(oldLeaderRoleId, 160408, null);
				}
				else
				{
					BuffAgent buffagent = new BuffRoleImpl(newLeaderRoleId);
					if (!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM_LEADER))
					{
						// 新队长处于不能当队长的状态
						TeamManager.logger.debug("FAIL:新队长处于不能当队长的状态,newLeaderRoleId: " + newLeaderRoleId);
					} else
					{
						TeamManager.logger.debug("SUCC:可以发出更换队长邀请,teamId: " + teamId);
						team.getTeamInfo().setSwitchleaderid(newLeaderRoleId);
						team.getTeamInfo().setSwitchleadertime(now);
						SAskforSetLeader sAskforSetLeader = new SAskforSetLeader();
						sAskforSetLeader.leaderid = oldLeaderRoleId;
						
						TeamManager.getInstance().delTeamMatch(oldLeaderRoleId); //交换队长离开匹配 by changhao
						
						psendWhileCommit(oldLeaderRoleId, new SRequestSetLeaderSucc(newLeaderRoleId));
						psendWhileCommit(newLeaderRoleId, sAskforSetLeader);
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(oldLeaderRoleId, 141251, null);
					}
				}

				return true;
			}
		};
		setTeamLeaderP.submit();
		
	}

	// 检测PVP
	private static int checkPvP(long oldLeaderRoleId, long newLeaderRoleId) {
		// 重新设置队长
		return fire.pb.battle.pvp.PvPTeamHandle.onSetTeamLeader(oldLeaderRoleId, newLeaderRoleId);
	}

//	// 申请者是一个队伍的队长？只能在Procedure中被调用
//	private boolean checkOldLeaderInTeam(long leaderRoleId,Team team)
//	{
//		if(team.getTeamInfo().getTeamleaderid() == leaderRoleId)
//			return true;
//		else
//			return false;
//	}
	
	// 申请者在线?只能在Procedure中被调用
	private boolean checkLeaderOnline(long leaderRoleId)
	{
		if(StateCommon.isOnline(leaderRoleId))
			return true;
		else
			return false;
	}
	
	//队伍处于可以换队长的状态？（飞行，战斗中不能换队长，还有其他状态吗？）
	private boolean checkTeamStatusValid(Team team)
	{
		BuffAgent buffagent = new BuffRoleImpl(team.getTeamLeaderId());
		int conflictId = buffagent.checkCanAddBuff(BuffConstant.StateType.STATE_TEAM_LEADER);
		return conflictId == 0;
	}
	
	//队伍不处于更换队长申请状态？
	private boolean checkTeamNotInSwitchStatus(Team team)
	{
		if(team.getTeamInfo().getSwitchleaderid() == -1)
			return true;
		else if((now - team.getTeamInfo().getSwitchleadertime()) > TeamManager.MAX_INVITE_TIMEOUT )
		{
			team.getTeamInfo().setSwitchleaderid(-1);
			return true;
		}
		else
			return false;
	}
	
	//队伍2分钟内未更换过队长？
	private boolean checkTeamNoSuccSwitchIn2min(Team team)
	{
		if((now - team.getTeamInfo().getSuccessswitchtime()) > TeamManager.MIN_SUCCESS_SWITCH_LEADER_PERIOD )
			return true;
		else
			return false;
	}
	
	//新队长处于正常状态？只能在Procedure中被调用
	private boolean checkNewLeaderNormal(Team team, long memberRoleId)
	{
		for(xbean.TeamMember member: team.getTeamInfo().getMembers())
		{
			if(member.getRoleid() == memberRoleId)
			{
				if(member.getState() == TeamMemberState.eTeamAbsent)
					return false;
				else
					return true;
			}
		}
		return false;
	}
	
	
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794444;

	public int getType() {
		return 794444;
	}

	public long roleid;

	public CSetTeamLeader() {
	}

	public CSetTeamLeader(long _roleid_) {
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
		if (_o1_ instanceof CSetTeamLeader) {
			CSetTeamLeader _o_ = (CSetTeamLeader)_o1_;
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

	public int compareTo(CSetTeamLeader _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


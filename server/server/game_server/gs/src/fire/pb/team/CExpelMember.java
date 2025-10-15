
package fire.pb.team;
import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.OperateType;
import fire.pb.event.LeaveTeamSpecialQuestEvent;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CExpelMember__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 请离队员
 * @author changhao
 *
 */
public class CExpelMember extends __CExpelMember__ {
	
	Team team;
	
	@Override
	protected void process() {
		// protocol handle
		TeamManager.logger.debug("Enter: " + this.getClass());
		final long leaderRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (leaderRoleId < 0)
			return;
		final long expeledRoleId = roleid;
		mkdb.Procedure expelMemberP = new mkdb.Procedure()
		{

			@Override
			protected boolean process()
			{
				//lock start 
				Long teamId = xtable.Roleid2teamid.select(leaderRoleId);
				//先验证队伍是否为空
				if(teamId != null)
					team = new Team(teamId,false);
				else
					return true;
				if(!team.isTeamLeader(leaderRoleId))
					return true;//验证该角色是否还是队伍的队长
				Long[] roleids = new Long[2];
				if(leaderRoleId < expeledRoleId)
				{
					roleids[0] = leaderRoleId;
					roleids[1] = expeledRoleId;
				}
				else
				{
					roleids[0] = expeledRoleId;
					roleids[1] = leaderRoleId;
				}
				this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK,(Object[])roleids));
				//lock end

				if (checkPvP(leaderRoleId, expeledRoleId) != 0) {
					return true;
				}

				if(!checkLeaderInTeam(leaderRoleId, team))
				{
					//踢人者不在队伍中或者不是队长(illegal)
					TeamManager.logger.debug("FAIL:踢人者不在队伍中或者不是队长,踢人者Id: "+ leaderRoleId);
				}
				else if(!checkLeaderOnline(leaderRoleId))
				{
					//TODO 踢人者不在线(illegal)
					TeamManager.logger.debug("FAIL:踢人者不在线,踢人者Id: "+ leaderRoleId);
				}
				else if(!checkExpeledIsMember(team, expeledRoleId))
				{
					//TODO 被踢者不是踢人者队伍成员(illegal)
					TeamManager.logger.debug("FAIL:被踢者不是踢人者队伍成员,被踢者Id: "+ expeledRoleId);
				}
				else if(!checkTeamLeaderState(leaderRoleId))
				{
					//TODO 队伍状态不允许(illegal)
					TeamManager.logger.debug("FAIL:队伍状态不允许,被踢者Id: "+ expeledRoleId);
				}
				else
				{
					team.removeTeamMemberWithSP(expeledRoleId,false);
					
					//向被离队者发送消息
					PropRole prole = new PropRole(team.getTeamInfo().getTeamleaderid(), true);
					List<String> name = new ArrayList<String>();
					name.add(prole.getName());
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(expeledRoleId, 140641, name);
					PropRole expelrole = new PropRole(expeledRoleId, true);
					List<String> expelname = new ArrayList<String>();
					expelname.add(expelrole.getName());
					for(long memberId : team.getAllMemberIds())
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(memberId,141208,expelname);
					TeamManager.logger.debug("SUCC:队伍踢人,被踢者Id: "+ expeledRoleId);
				}
	//			FactionPatrol.setRoleTaskFailed(expeledRoleId);  //被踢出队伍后，有帮派四方巡视任务的角色要记任务失败
				fire.pb.event.Poster.getPoster().dispatchEvent(new LeaveTeamSpecialQuestEvent(expeledRoleId));
				return true;
			}
		};
		expelMemberP.submit();
		
	}

	// 检测PVP
	private static int checkPvP(long leaderRoleId, long expeledRoleId) {
		// 请离队员
		return fire.pb.battle.pvp.PvPTeamHandle.onExpelMember(leaderRoleId, expeledRoleId);
	}
	
	// 踢人者是一个队伍的队长？?只能在Procedure中被调用
	private boolean checkLeaderInTeam(long leaderRoleId,Team team)
	{
		//xbean.TeamInfo team = xtable.Team.get(teamId);
		if(team.getTeamInfo() != null && team.getTeamInfo().getTeamleaderid() == leaderRoleId)
			return true;
		else
			return false;

	}
	
	// 踢人者在线?只能在Procedure中被调用
	private boolean checkLeaderOnline(long leaderRoleId)
	{
		if(StateCommon.isOnline(leaderRoleId))
			return true;
		else
			return false;
	}
	
	//被踢者是队长队伍的队员？只能在Procedure中被调用
	private boolean checkExpeledIsMember(Team team, long expeledRoleId)
	{
		//xbean.TeamInfo team = xtable.Team.get(teamId);
		for(xbean.TeamMember member: team.getTeamInfo().getMembers())
		{
			if(member.getRoleid() == expeledRoleId)
				return true;
		}
		return false;
	}
	
	// 邀请者状态不允许? 只能在Procedure中被调用
	private boolean checkTeamLeaderState(long roleId)
	{
		BuffAgent buffagent = new BuffRoleImpl(roleId);
		return buffagent.canAddBuff(OperateType.TEAM_EXPEL_MEMBER);
	}
	
	
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794442;

	public int getType() {
		return 794442;
	}

	public long roleid;

	public CExpelMember() {
	}

	public CExpelMember(long _roleid_) {
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
		if (_o1_ instanceof CExpelMember) {
			CExpelMember _o_ = (CExpelMember)_o1_;
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

	public int compareTo(CExpelMember _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


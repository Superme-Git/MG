
package fire.pb.team;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;



// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestTeamMatch__ extends mkio.Protocol { }

/** 最新加入功能 by changhao
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 加入匹配
 * @author changhao
 *
 */
public class CRequestTeamMatch extends __CRequestTeamMatch__ {
	@Override
	protected void process() {
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure requestteammatch = new mkdb.Procedure()
		{
			@Override
			/*加锁顺序 team->roleid->match by changhao*/
			protected boolean process()
			{	
				if (!checkLevel()) //检查等级 by changhao
				{
					psend(roleid, new STeamError(TeamError.LevelError));
					TeamManager.logger.debug("FAIL:CRequestTeamMatch匹配等级设置错误 "+roleid);
					return true;					
				}
				
				if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
				{
					DSTeamMatchInfo config = ConfigManager.getInstance().getConf(DSTeamMatchInfo.class).get(targetid);
					if (config == null)
					{
						psend(roleid, new STeamError(TeamError.NoTarget));
						TeamManager.logger.debug("FAIL:CRequestTeamMatch:目标ID错误 "+roleid);
						return true;					
					}						
				}
				else
				{
					STeamMatchInfo config = ConfigManager.getInstance().getConf(STeamMatchInfo.class).get(targetid);
					if (config == null)
					{
						psend(roleid, new STeamError(TeamError.NoTarget));
						TeamManager.logger.debug("FAIL:CRequestTeamMatch:目标ID错误 "+roleid);
						return true;					
					}					
				}
				
				Long clanfightid = xtable.Roleid2clanfightid.select(roleid);
				if (clanfightid != null)//公会战场中不能自动匹配 by changhao
				{ 
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 410040, null);
					return true;
				}
								
				Long teamid = xtable.Roleid2teamid.select(roleid);
				
				Team team = null;
				
				final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
				if(teamid != null)
				{
					team = TeamManager.getTeamByTeamID(teamid);	//有队先把队伍锁住 by changhao
					
					if (!team.isTeamLeader(roleid))
					{
						psend(roleid, new STeamError(TeamError.SelfNOtLeader));
						TeamManager.logger.debug("FAIL:CRequestTeamMatch:不是队长不能自动匹配 "+roleid);
						return true;						
					}
					else if (team.getTeamInfo().getMembers().size() >= TeamManager.MAX_MEMBER_COUNT)
					{
						psend(roleid, new STeamError(TeamError.TeamFull));
						TeamManager.logger.debug("FAIL:CRequestTeamMatch:队伍满了 "+roleid);
						return true;						
					}
					
					team.getTeamInfo().setTargetid(targetid);
					team.getTeamInfo().setMinlevel(levelmin);
					team.getTeamInfo().setMaxlevel(levelmax);
					
					//锁所有人  by changhao
					roleids.addAll(team.getAllMemberIds());
					this.lock(xtable.Locks.ROLELOCK, roleids);
				}
				else
				{
					roleids.add(roleid);
				}
				
				Long roleidteamId = xtable.Roleid2teamid.get(roleid);
				if (teamid != roleidteamId)
				{
					psend(roleid, new STeamError(TeamError.UnkownError));
					TeamManager.logger.debug("FAIL:CRequestTeamMatch:队伍ID有变化 "+roleid);
					return true;
				}
				
				if (!checkTeamState(team, roleid)) //检查队伍状态 by changhao
				{
					psend(roleid, new STeamError(TeamError.TeamStateError));
					TeamManager.logger.debug("FAIL:CRequestTeamMatch组队匹配客户端服务器不同步 "+roleid);
					return true;
				}
				
				if (!checkTeamMemberNum(team)) //检查队伍人数 by changhao
				{
					psend(roleid, new STeamError(TeamError.TeamEnoughFull));
					TeamManager.logger.debug("FAIL:CRequestTeamMatch队伍已经组满 "+roleid);
					return true;												
				}
				
				if (!checkFaction(roleid)) //检查门派 by changhao
				{
					psend(roleid, new STeamError(TeamError.NoFaction));
					TeamManager.logger.debug("FAIL:CRequestTeamMatch没有工会 "+roleid);
					return true;					
				}
				
				if (!checkActiveTime()) //检查活动开放时间 by changhao
				{
					psend(roleid, new STeamError(TeamError.ActiveNotOpen));
					TeamManager.logger.debug("FAIL:CRequestTeamMatch活动未开放 "+roleid);
					return true;						
				}
				
				/*if (fire.pb.buff.Module.existState(roleid,
						BuffConstant.StateType.STATE_INSTANCE_ZONE)) {
					psend(roleid, new STeamError(TeamError.SelfInUnteamState));
					TeamManager.logger.debug("FAIL:在副本中啊 "+roleid);
					return true;
				}*/

				if (checkPvP(roleid) != 0) {
					return true;
				}

				addTeamMatch(roleid, roleidteamId); //加一个匹配到表 by changhao
								
				SRequestTeamMatch msg = new SRequestTeamMatch();
				msg.levelmin = levelmin;
				msg.levelmax = levelmax;
				msg.targetid = targetid;
				msg.typematch = typematch;
				
				for (Long roleid : roleids)
				{
					mkdb.Procedure.psendWhileCommit(roleid, msg);					
				}
				
				return true;
			}
			
		};
		requestteammatch.submit();	
	}
	
	private void addTeamMatch(long roleid, Long teamid)
	{
		xbean.TeamMatch teammatch = xbean.Pod.newTeamMatch();
		
		teammatch.setRoleid(roleid);
		teammatch.setLevelmin(this.levelmin);
		teammatch.setLevelmax(this.levelmax);
		teammatch.setTargetid(this.targetid);
		teammatch.setMatchtype(typematch);
		teammatch.setOnekeytimestamp(0);
		teammatch.setTimestamp(0);
		
		TeamManager.getInstance().addTeamMatch(teammatch, teamid);			
	}
	
	private boolean checkTeamState(Team team, long roleid) //检查组队状态和客户端是否匹配 by changhao
	{
		if (this.typematch == 0 && team == null)
		{			
			return true;
		}
		
		if (this.typematch == 1 && team != null)
		{
			return team.isTeamLeader(roleid);
			
		}
		
		return false;
	}
	
	private boolean checkLevel() //检查等级相关错误 by changhao
	{
		if (this.levelmin > this.levelmax)
		{
			return false;
		}
		
		if (this.levelmin <= 0 || this.levelmax <= 0)
		{
			return false;
		}
		
		//取表数据检查是否范围内 by changhao
		
		return true;
	}
	
	private boolean checkTeamMemberNum(Team team) //检查人数 by changhao
	{
		return true;	
	}
	
	private boolean checkFaction(long roleid) //检查公会相关 by changhao
	{
		return true;
	}
	
	private boolean checkActiveTime() //检查活动时间 by changhao
	{
		
		return true;
	}

	// 检测PVP
	private static int checkPvP(long roleId) {
		// 请求自动匹配
		return fire.pb.battle.pvp.PvPTeamHandle.onRequestTeamMatch(roleId);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794494;

	public int getType() {
		return 794494;
	}

	public int typematch; // 类型为0是个人组队匹配1是队伍匹配 by changhao
	public int targetid; // 目标id by changhao
	public int levelmin; // 等级下限 如果是个人匹配这个忽略 by changhao
	public int levelmax; // 等级上限 如果是个人匹配这个忽略 by changhao

	public CRequestTeamMatch() {
	}

	public CRequestTeamMatch(int _typematch_, int _targetid_, int _levelmin_, int _levelmax_) {
		this.typematch = _typematch_;
		this.targetid = _targetid_;
		this.levelmin = _levelmin_;
		this.levelmax = _levelmax_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(typematch);
		_os_.marshal(targetid);
		_os_.marshal(levelmin);
		_os_.marshal(levelmax);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		typematch = _os_.unmarshal_int();
		targetid = _os_.unmarshal_int();
		levelmin = _os_.unmarshal_int();
		levelmax = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestTeamMatch) {
			CRequestTeamMatch _o_ = (CRequestTeamMatch)_o1_;
			if (typematch != _o_.typematch) return false;
			if (targetid != _o_.targetid) return false;
			if (levelmin != _o_.levelmin) return false;
			if (levelmax != _o_.levelmax) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += typematch;
		_h_ += targetid;
		_h_ += levelmin;
		_h_ += levelmax;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(typematch).append(",");
		_sb_.append(targetid).append(",");
		_sb_.append(levelmin).append(",");
		_sb_.append(levelmax).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestTeamMatch _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = typematch - _o_.typematch;
		if (0 != _c_) return _c_;
		_c_ = targetid - _o_.targetid;
		if (0 != _c_) return _c_;
		_c_ = levelmin - _o_.levelmin;
		if (0 != _c_) return _c_;
		_c_ = levelmax - _o_.levelmax;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


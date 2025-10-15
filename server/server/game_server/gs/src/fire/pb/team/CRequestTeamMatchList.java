
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestTeamMatchList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 请求匹配链表
 * @author changhao
 *
 */
public class CRequestTeamMatchList extends __CRequestTeamMatchList__ {
	@Override
	protected void process() {
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure requestteammatchlist = new mkdb.Procedure()
		{
			@Override
			protected boolean process() //role->teammatch by changhao
			{
				SRequestTeamMatchList msg = new SRequestTeamMatchList();
				xbean.Properties prop = xtable.Properties.select(roleid);
				if (prop == null)
				{
					msg.targetid = targetid;
					msg.ret = 0;
					
					mkdb.Procedure.psendWhileCommit(roleid, msg);
					return true;
				}
				
				int level = prop.getLevel();
				
				xbean.ETeamMatch ematch = xtable.Targetid2teammatch.select(0);
				
				if (ematch == null)
				{
					msg.targetid = targetid;
					msg.ret = 0;
					
					mkdb.Procedure.psendWhileCommit(roleid, msg);
					
					return true;
				}
				
				if (num <= 0) //数量错误 by changhao
				{
					msg.targetid = targetid;
					msg.ret = 2;
					
					mkdb.Procedure.psendWhileCommit(roleid, msg);
					return true;
				}
					
				int curnum = 0;
			
				for (java.util.Map.Entry<Long, xbean.TeamMatch> e : ematch.getTeamid2matchdata().entrySet())
				{
					xbean.TeamMatch teammatch = e.getValue();
					if (targetid == 0 || (teammatch.getTargetid() == targetid && teammatch.getMatchtype() == 1)) //是队伍匹配 by changhao
					{
						Long teamid = xtable.Roleid2teamid.select(teammatch.getRoleid());
						if (teamid != null && teamid > startteamid) //从开始取 by changhao
						{
							Team team = new Team(teamid, true);
							
							if (!checkTeamFull(team.getTeamInfo())) //队伍满了 by changhao
							{
								continue;
							}
							
							if (!checkLevel(teammatch, level)) //等级不符合 by changhao
							{
								continue;								
							}
									
							if (curnum >= num || curnum >= TeamManager.MAX_MATCH_LIST_COUNT) //最大数量限制 by changhao
							{
								break;
							}
														
							fire.pb.team.TeamInfoBasicWithMembers t = new fire.pb.team.TeamInfoBasicWithMembers();
							t.teaminfobasic = newTeamInfoBasic(teamid, teammatch, team.getTeamInfo());
							
							boolean ok = team.getTeamInfo().getApplierids().containsKey(roleid);
							if (ok == true)
							{
								t.status = 1;
							}
						
							t.memberlist.add(team.getTeamMemeberSimple(team.getTeamInfo().getTeamleaderid()));
							
							for (xbean.TeamMember e1 : team.getTeamInfo().getMembers())
							{
								t.memberlist.add(team.getTeamMemeberSimple(e1.getRoleid()));								
							}
							
							msg.teamlist.add(t);
							
							curnum ++;
						}
					}
				}
			/*	
				if (!msg.teamlist.isEmpty())
				{
					tmp = msg.teamlist.get(0);
				}
				
				if (tmp != null)
				for (int i = curnum; i < num ; i ++)
				{
					fire.pb.team.TeamInfoBasic tmp1 = new fire.pb.team.TeamInfoBasic();
					tmp1 = tmp;
					tmp1.teamid = tmp.teamid ++;
					msg.teamlist.add(tmp1);
				}
			*/
				msg.targetid = targetid;
				msg.ret = 0;
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);				
				
				return true;
			}		
		};
		requestteammatchlist.submit();			
	}
	
	public static fire.pb.team.TeamInfoBasic tmp;
	
	public boolean checkLevel(xbean.TeamMatch teammatch, int level)
	{		
		if (level >= teammatch.getLevelmin() && level <= teammatch.getLevelmax())
		{
			return true;
		}
		
		return false;
	}
	
	public boolean checkTeamFull(xbean.TeamInfo teaminfo)
	{
		if (teaminfo.getMembers().size() >= TeamManager.MAX_MEMBER_COUNT)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 必须在Procedure里执行 创建一个队伍的基本信息 by changhao
	 * leaderid 必须是队伍的队长 by changhao
	 */		
	public TeamInfoBasic newTeamInfoBasic(long teamid, xbean.TeamMatch teammatch, xbean.TeamInfo teaminfo)
	{	
		xbean.Properties leaderprop = xtable.Properties.select(teaminfo.getTeamleaderid());
		TeamInfoBasic teamInfoBasic = new TeamInfoBasic();
		teamInfoBasic.leaderid = teaminfo.getTeamleaderid();
		teamInfoBasic.leaderlevel = leaderprop.getLevel();
		teamInfoBasic.leadername = leaderprop.getRolename();
		teamInfoBasic.leaderschool = leaderprop.getSchool();
		teamInfoBasic.minlevel = teammatch.getLevelmin();
		teamInfoBasic.maxlevel = teammatch.getLevelmax();
		teamInfoBasic.membernum = teaminfo.getMembers().size() + 1;
		teamInfoBasic.teamid = teamid;
		teamInfoBasic.membermaxnum = TeamManager.MAX_MEMBER_COUNT + 1; //表还没有要在表里取 by changhao 
		teamInfoBasic.targetid = teammatch.getTargetid();
		
		return teamInfoBasic;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794509;

	public int getType() {
		return 794509;
	}

	public int targetid; // 目标ID by changhao
	public long startteamid; // 起始队伍ID （0的话就从头取）by changhao
	public int num; // 取起始队伍id后面的num个数据  by changhao

	public CRequestTeamMatchList() {
	}

	public CRequestTeamMatchList(int _targetid_, long _startteamid_, int _num_) {
		this.targetid = _targetid_;
		this.startteamid = _startteamid_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(targetid);
		_os_.marshal(startteamid);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		targetid = _os_.unmarshal_int();
		startteamid = _os_.unmarshal_long();
		num = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestTeamMatchList) {
			CRequestTeamMatchList _o_ = (CRequestTeamMatchList)_o1_;
			if (targetid != _o_.targetid) return false;
			if (startteamid != _o_.startteamid) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += targetid;
		_h_ += (int)startteamid;
		_h_ += num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(targetid).append(",");
		_sb_.append(startteamid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestTeamMatchList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = targetid - _o_.targetid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(startteamid - _o_.startteamid);
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


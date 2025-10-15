
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestClanFightTeamList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 请求公会战力队伍信息 by changhao
 *
 */
public class CRequestClanFightTeamList extends __CRequestClanFightTeamList__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
		{
			return;
		}
		
		mkdb.Procedure r = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				if (num > 20)
				{
					return false;
				}
				
				int side = 0;
				Long c1 = xtable.Roleid2clanfightid.select(roleid); //检测是否在战场中 by changhao
				if (c1 == null)
				{
					return false;
				}
			
				fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(c1, true);
				if (bf != null)
				{
					Long clanid = xtable.Roleidclan.select(roleid);
					if (clanid == null)
					{
						return false;
					}
					
					if (clanid == bf.getClanfightBean().getClanid1())
					{
						side = 0;
					}
					else if (clanid == bf.getClanfightBean().getClanid2())
					{
						side = 1;
					}
					else
					{
						return false;
					}
					
					SRequestClanFightTeamList msg = new SRequestClanFightTeamList();
					java.util.Map<Long, Integer> v = bf.getClanroleidsByWhich(side);
					java.util.List<Long> teamlist = new java.util.ArrayList<Long>();
					
					if (v != null)
					{
						for (Long e : v.keySet())
						{
							Long teamid = xtable.Roleid2teamid.select(e);
							if (teamid != null)
							{
								teamlist.add(teamid);
							}
						}
						
						teamlist.sort(new java.util.Comparator<Long>(){
							@Override
							public int compare(Long o1, Long o2)
							{
								if (o1 > o2)
								{
									return 1;
								}
								else
								{
									return -1;
								}
							}
						});						
						
						int curnum = 0;
						java.util.Set<Long> teamidset = new java.util.HashSet<Long>();
						for (Long teamid : teamlist)
						{	
							if (teamid != null)
							{
								if (start != 0 && teamid <= start)
								{
									continue;
								}
								
								xbean.TeamInfo teaminfo = xtable.Team.select(teamid);
								if (teaminfo != null && !teamidset.contains(teamid)) //没满的才发送过去 by changhao
								{
									fire.pb.team.TeamInfoBasicWithMembers t = new fire.pb.team.TeamInfoBasicWithMembers();
									t.teaminfobasic = newTeamInfoBasic(teamid, teaminfo);
									msg.teamlist.add(t);
									
									t.memberlist.add(getTeamMemeberSimple(teaminfo.getTeamleaderid()));
									
									for (xbean.TeamMember e1 : teaminfo.getMembers())
									{
										t.memberlist.add(getTeamMemeberSimple(e1.getRoleid()));								
									}
									
									curnum ++;
									
									teamidset.add(teamid);
									if (curnum >= num)
									{
										break;
									}	
								}								
							}
						}						
					}
					
					msg.isfresh = isfresh;
					if (msg.teamlist.size() == 0)
					{
						msg.ret = -1;
					}
					
					mkdb.Procedure.psendWhileCommit(roleid, msg);
				}
				
				return true;
			}
		};
		
		r.submit();	
	}
	
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
	
	public TeamInfoBasic newTeamInfoBasic(long teamid, xbean.TeamInfo teaminfo)
	{	
		xbean.Properties leaderprop = xtable.Properties.select(teaminfo.getTeamleaderid());
		TeamInfoBasic teamInfoBasic = new TeamInfoBasic();
		teamInfoBasic.leaderid = teaminfo.getTeamleaderid();
		teamInfoBasic.leaderlevel = leaderprop.getLevel();
		teamInfoBasic.leadername = leaderprop.getRolename();
		teamInfoBasic.leaderschool = leaderprop.getSchool();
		teamInfoBasic.minlevel = 0;
		teamInfoBasic.maxlevel = 0;
		teamInfoBasic.membernum = teaminfo.getMembers().size() + 1;
		teamInfoBasic.teamid = teamid;
		teamInfoBasic.membermaxnum = TeamManager.MAX_MEMBER_COUNT + 1; //表还没有要在表里取 by changhao 
		teamInfoBasic.targetid = 0;
		
		return teamInfoBasic;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794557;

	public int getType() {
		return 794557;
	}

	public int isfresh; // 客户端用 0刷新 1不刷新  by qyl
	public long start; // 起始teamid by changhao
	public int num; // 请求的数量 by changhao

	public CRequestClanFightTeamList() {
	}

	public CRequestClanFightTeamList(int _isfresh_, long _start_, int _num_) {
		this.isfresh = _isfresh_;
		this.start = _start_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(isfresh);
		_os_.marshal(start);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		isfresh = _os_.unmarshal_int();
		start = _os_.unmarshal_long();
		num = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestClanFightTeamList) {
			CRequestClanFightTeamList _o_ = (CRequestClanFightTeamList)_o1_;
			if (isfresh != _o_.isfresh) return false;
			if (start != _o_.start) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += isfresh;
		_h_ += (int)start;
		_h_ += num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(isfresh).append(",");
		_sb_.append(start).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestClanFightTeamList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = isfresh - _o_.isfresh;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(start - _o_.start);
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


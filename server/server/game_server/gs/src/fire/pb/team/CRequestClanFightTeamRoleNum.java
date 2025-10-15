
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestClanFightTeamRoleNum__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
/***
 * 请求公会战力角色和队伍数量 by changhao
 *
 */
public class CRequestClanFightTeamRoleNum extends __CRequestClanFightTeamRoleNum__ {
	@Override
	protected void process() {
		// protocol handle
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
					
					SRequestClanFightTeamRoleNum msg = new SRequestClanFightTeamRoleNum();
					java.util.Map<Long, Integer> v = bf.getClanroleidsByWhich(side);
					if (v != null)
					{
						java.util.Set<Long> teamidset = new java.util.HashSet<Long>();
						for (Long e : v.keySet())
						{
							Long teamid = xtable.Roleid2teamid.select(e);
							if (teamid != null)
							{
								if (!teamidset.contains(teamid))
								{
									msg.teamnum ++;
								}
								
								teamidset.add(teamid);
							}
							else
							{
								msg.rolenum ++;
							}
						}						
					}
					
					mkdb.Procedure.psendWhileCommit(roleid, msg);
				}
				
				return true;
			}
		};
		
		r.submit();	
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794561;

	public int getType() {
		return 794561;
	}


	public CRequestClanFightTeamRoleNum() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestClanFightTeamRoleNum) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestClanFightTeamRoleNum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


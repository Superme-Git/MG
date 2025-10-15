
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestClanFightRoleList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 请求公会战里角色信息 by changhao
 *
 */
public class CRequestClanFightRoleList extends __CRequestClanFightRoleList__ {
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
					
					SRequestClanFightRoleList msg = new SRequestClanFightRoleList();
					java.util.Map<Long, Integer> v = bf.getClanroleidsByWhich(side);
					
					java.util.List<Long> sortdata = new java.util.ArrayList<Long>();
					if (v != null)
					{
						sortdata.addAll(v.keySet());
					}
					
					sortdata.sort(new java.util.Comparator<Long>(){
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
					
					if (v != null)
					{
						int curnum = 0;
						for (Long e : sortdata)
						{
							if (start != 0 && e <= start)
							{
								continue;
							}
							
							Long teamid = xtable.Roleid2teamid.select(e);
							if (teamid == null)
							{
								xbean.Properties prop = xtable.Properties.select(e);
								if (prop != null)
								{
									fire.pb.team.RoleSimapleInfo info = new fire.pb.team.RoleSimapleInfo();
									info.roleid = e;
									info.rolename = prop.getRolename();
									info.level = prop.getLevel();
									info.schoold = prop.getSchool();
									info.shape = prop.getShape();
									msg.rolelist.add(info);
									
									curnum ++;
									if (curnum >= num)
									{
										break;
									}	
								}								
							}
						}						
					}
					
					msg.isfresh = isfresh;
					if (msg.rolelist.size() == 0)
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

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794559;

	public int getType() {
		return 794559;
	}

	public int isfresh; // 客户端用 0刷新 1不刷新  by qyl
	public long start; // 起始roleid by changhao
	public int num; // 请求的数量 by changhao

	public CRequestClanFightRoleList() {
	}

	public CRequestClanFightRoleList(int _isfresh_, long _start_, int _num_) {
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
		if (_o1_ instanceof CRequestClanFightRoleList) {
			CRequestClanFightRoleList _o_ = (CRequestClanFightRoleList)_o1_;
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

	public int compareTo(CRequestClanFightRoleList _o_) {
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



package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetClanFightList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 得到帮会战的对战表 by changhao
 */
public class CGetClanFightList extends __CGetClanFightList__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		mkdb.Procedure getclanfightlist = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				long cur = System.currentTimeMillis();
				
				if (which != 0 && which != 1)
				{
					return false;
				}
				
				if (which == 0)
				{
					long mon1 = fire.pb.activity.clanfight.ActivityClanFightManager.GetMonday1500ByTime(cur);
					if (cur < mon1)
					{
						return false;
					}
				}
				
				if (which == 1)
				{
					long wed1 = fire.pb.activity.clanfight.ActivityClanFightManager.GetWednesday1500ByTime(cur);
					if (cur < wed1)
					{
						return false;
					}
				}
				
				long createtime = which == 0 ? fire.pb.activity.clanfight.ActivityClanFightManager.GetMonday1500ByTime(cur) : fire.pb.activity.clanfight.ActivityClanFightManager.GetWednesday1500ByTime(cur);
				
				int curweek = fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().curweek.get();
				SGetClanFightList msg = new SGetClanFightList();
				
				if (curweek == -1)
				{
					msg.curweek = -1;
					mkdb.Procedure.psendWhileCommit(roleid, msg);					
					
					return true;
				}
				
				int curweekvalue = curweek % 8;
				
				msg.curweek = curweekvalue;
				
				int  result = 0;
				xbean.ClanFights clanfights = xtable.Clanfights.select(createtime);
				if (clanfights != null)
				{
					for (xbean.ClanFight c : clanfights.getClan1vschan2().values())
					{
						fire.pb.clan.fight.ClanFight f = new fire.pb.clan.fight.ClanFight();
						f.clanid1 = c.getClanindex1();
						f.clanname1 = c.getClanname1();
						f.clanid2 = c.getClanindex2();
						f.clanname2 = c.getClanname2();
						f.winner = c.getWinner();
						
						if (f.winner != -1)
						{
							result = 1;
						}
						
						msg.clanfightlist.add(f);
					}
				}
				
				msg.over = result;
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);
				
				return true;
			}
		};
		
		getclanfightlist.submit();			
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808532;

	public int getType() {
		return 808532;
	}

	public int whichweek; // 历史那一周(0-7周)(如果填-1表示当前周) by changhao
	public int which; // 0是周一那场，1是周三那场 by changhao

	public CGetClanFightList() {
	}

	public CGetClanFightList(int _whichweek_, int _which_) {
		this.whichweek = _whichweek_;
		this.which = _which_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(whichweek);
		_os_.marshal(which);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		whichweek = _os_.unmarshal_int();
		which = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetClanFightList) {
			CGetClanFightList _o_ = (CGetClanFightList)_o1_;
			if (whichweek != _o_.whichweek) return false;
			if (which != _o_.which) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += whichweek;
		_h_ += which;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(whichweek).append(",");
		_sb_.append(which).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetClanFightList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = whichweek - _o_.whichweek;
		if (0 != _c_) return _c_;
		_c_ = which - _o_.which;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


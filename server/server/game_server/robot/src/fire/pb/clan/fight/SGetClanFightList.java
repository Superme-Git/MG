
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetClanFightList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetClanFightList extends __SGetClanFightList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808533;

	public int getType() {
		return 808533;
	}

	public java.util.LinkedList<fire.pb.clan.fight.ClanFight> clanfightlist; // 当前这轮的对阵信息链表 by changhao
	public int curweek; // 当前轮数(从0开始, 返回-1表示还没开始或已经过了8轮) by changhao
	public int over; // 0是没结束1是已结束 by changhao

	public SGetClanFightList() {
		clanfightlist = new java.util.LinkedList<fire.pb.clan.fight.ClanFight>();
	}

	public SGetClanFightList(java.util.LinkedList<fire.pb.clan.fight.ClanFight> _clanfightlist_, int _curweek_, int _over_) {
		this.clanfightlist = _clanfightlist_;
		this.curweek = _curweek_;
		this.over = _over_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.fight.ClanFight _v_ : clanfightlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(clanfightlist.size());
		for (fire.pb.clan.fight.ClanFight _v_ : clanfightlist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(curweek);
		_os_.marshal(over);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.fight.ClanFight _v_ = new fire.pb.clan.fight.ClanFight();
			_v_.unmarshal(_os_);
			clanfightlist.add(_v_);
		}
		curweek = _os_.unmarshal_int();
		over = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetClanFightList) {
			SGetClanFightList _o_ = (SGetClanFightList)_o1_;
			if (!clanfightlist.equals(_o_.clanfightlist)) return false;
			if (curweek != _o_.curweek) return false;
			if (over != _o_.over) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += clanfightlist.hashCode();
		_h_ += curweek;
		_h_ += over;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanfightlist).append(",");
		_sb_.append(curweek).append(",");
		_sb_.append(over).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


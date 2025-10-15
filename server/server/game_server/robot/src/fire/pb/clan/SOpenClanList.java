
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOpenClanList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOpenClanList extends __SOpenClanList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808448;

	public int getType() {
		return 808448;
	}

	public int currpage; // 当前页
	public java.util.ArrayList<fire.pb.clan.ClanSummaryInfo> clanlist;

	public SOpenClanList() {
		clanlist = new java.util.ArrayList<fire.pb.clan.ClanSummaryInfo>();
	}

	public SOpenClanList(int _currpage_, java.util.ArrayList<fire.pb.clan.ClanSummaryInfo> _clanlist_) {
		this.currpage = _currpage_;
		this.clanlist = _clanlist_;
	}

	public final boolean _validator_() {
		if (currpage < 1) return false;
		for (fire.pb.clan.ClanSummaryInfo _v_ : clanlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(currpage);
		_os_.compact_uint32(clanlist.size());
		for (fire.pb.clan.ClanSummaryInfo _v_ : clanlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		currpage = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.ClanSummaryInfo _v_ = new fire.pb.clan.ClanSummaryInfo();
			_v_.unmarshal(_os_);
			clanlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOpenClanList) {
			SOpenClanList _o_ = (SOpenClanList)_o1_;
			if (currpage != _o_.currpage) return false;
			if (!clanlist.equals(_o_.clanlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += currpage;
		_h_ += clanlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(currpage).append(",");
		_sb_.append(clanlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


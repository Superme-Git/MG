
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestRuneInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestRuneInfo extends __SRequestRuneInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808508;

	public int getType() {
		return 808508;
	}

	public int requestnum; // 请求次数
	public int useenergy; // 消耗活力
	public java.util.ArrayList<fire.pb.clan.RuneInfo> runeinfolist;

	public SRequestRuneInfo() {
		runeinfolist = new java.util.ArrayList<fire.pb.clan.RuneInfo>();
	}

	public SRequestRuneInfo(int _requestnum_, int _useenergy_, java.util.ArrayList<fire.pb.clan.RuneInfo> _runeinfolist_) {
		this.requestnum = _requestnum_;
		this.useenergy = _useenergy_;
		this.runeinfolist = _runeinfolist_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.RuneInfo _v_ : runeinfolist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(requestnum);
		_os_.marshal(useenergy);
		_os_.compact_uint32(runeinfolist.size());
		for (fire.pb.clan.RuneInfo _v_ : runeinfolist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		requestnum = _os_.unmarshal_int();
		useenergy = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.RuneInfo _v_ = new fire.pb.clan.RuneInfo();
			_v_.unmarshal(_os_);
			runeinfolist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestRuneInfo) {
			SRequestRuneInfo _o_ = (SRequestRuneInfo)_o1_;
			if (requestnum != _o_.requestnum) return false;
			if (useenergy != _o_.useenergy) return false;
			if (!runeinfolist.equals(_o_.runeinfolist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += requestnum;
		_h_ += useenergy;
		_h_ += runeinfolist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(requestnum).append(",");
		_sb_.append(useenergy).append(",");
		_sb_.append(runeinfolist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


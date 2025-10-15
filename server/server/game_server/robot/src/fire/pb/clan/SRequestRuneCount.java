
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestRuneCount__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestRuneCount extends __SRequestRuneCount__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808514;

	public int getType() {
		return 808514;
	}

	public java.util.ArrayList<fire.pb.clan.RuneCountInfo> runecountinfolist;

	public SRequestRuneCount() {
		runecountinfolist = new java.util.ArrayList<fire.pb.clan.RuneCountInfo>();
	}

	public SRequestRuneCount(java.util.ArrayList<fire.pb.clan.RuneCountInfo> _runecountinfolist_) {
		this.runecountinfolist = _runecountinfolist_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.RuneCountInfo _v_ : runecountinfolist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(runecountinfolist.size());
		for (fire.pb.clan.RuneCountInfo _v_ : runecountinfolist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.RuneCountInfo _v_ = new fire.pb.clan.RuneCountInfo();
			_v_.unmarshal(_os_);
			runecountinfolist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestRuneCount) {
			SRequestRuneCount _o_ = (SRequestRuneCount)_o1_;
			if (!runecountinfolist.equals(_o_.runecountinfolist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += runecountinfolist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(runecountinfolist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


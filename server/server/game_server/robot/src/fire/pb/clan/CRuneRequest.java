
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRuneRequest__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRuneRequest extends __CRuneRequest__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808511;

	public int getType() {
		return 808511;
	}

	public java.util.ArrayList<fire.pb.clan.RuneRequestInfo> runerequestinfolist;

	public CRuneRequest() {
		runerequestinfolist = new java.util.ArrayList<fire.pb.clan.RuneRequestInfo>();
	}

	public CRuneRequest(java.util.ArrayList<fire.pb.clan.RuneRequestInfo> _runerequestinfolist_) {
		this.runerequestinfolist = _runerequestinfolist_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.RuneRequestInfo _v_ : runerequestinfolist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(runerequestinfolist.size());
		for (fire.pb.clan.RuneRequestInfo _v_ : runerequestinfolist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.RuneRequestInfo _v_ = new fire.pb.clan.RuneRequestInfo();
			_v_.unmarshal(_os_);
			runerequestinfolist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRuneRequest) {
			CRuneRequest _o_ = (CRuneRequest)_o1_;
			if (!runerequestinfolist.equals(_o_.runerequestinfolist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += runerequestinfolist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(runerequestinfolist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


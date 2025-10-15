
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAcceptOrRefuseApply__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAcceptOrRefuseApply extends __CAcceptOrRefuseApply__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808457;

	public int getType() {
		return 808457;
	}

	public long applyroleid;
	public byte accept; // 0拒绝,1接受

	public CAcceptOrRefuseApply() {
	}

	public CAcceptOrRefuseApply(long _applyroleid_, byte _accept_) {
		this.applyroleid = _applyroleid_;
		this.accept = _accept_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(applyroleid);
		_os_.marshal(accept);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		applyroleid = _os_.unmarshal_long();
		accept = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAcceptOrRefuseApply) {
			CAcceptOrRefuseApply _o_ = (CAcceptOrRefuseApply)_o1_;
			if (applyroleid != _o_.applyroleid) return false;
			if (accept != _o_.accept) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)applyroleid;
		_h_ += (int)accept;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(applyroleid).append(",");
		_sb_.append(accept).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAcceptOrRefuseApply _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(applyroleid - _o_.applyroleid);
		if (0 != _c_) return _c_;
		_c_ = accept - _o_.accept;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


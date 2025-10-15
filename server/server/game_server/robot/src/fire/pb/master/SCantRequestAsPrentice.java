
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SCantRequestAsPrentice__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SCantRequestAsPrentice extends __SCantRequestAsPrentice__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816466;

	public int getType() {
		return 816466;
	}

	public final static int Mask_Word = 0; // 屏蔽字

	public long roleid; // 请求师父的id
	public int reason; // 失败原因

	public SCantRequestAsPrentice() {
	}

	public SCantRequestAsPrentice(long _roleid_, int _reason_) {
		this.roleid = _roleid_;
		this.reason = _reason_;
	}

	public final boolean _validator_() {
		if (reason < 0 || reason > 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(reason);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		reason = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SCantRequestAsPrentice) {
			SCantRequestAsPrentice _o_ = (SCantRequestAsPrentice)_o1_;
			if (roleid != _o_.roleid) return false;
			if (reason != _o_.reason) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += reason;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(reason).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SCantRequestAsPrentice _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = reason - _o_.reason;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


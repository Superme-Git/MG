
package fire.pb.lock;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateLockInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateLockInfo extends __SUpdateLockInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818946;

	public int getType() {
		return 818946;
	}

	public int status; // 安全锁状态0未设置安全锁1未解锁2已解锁

	public SUpdateLockInfo() {
	}

	public SUpdateLockInfo(int _status_) {
		this.status = _status_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(status);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		status = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateLockInfo) {
			SUpdateLockInfo _o_ = (SUpdateLockInfo)_o1_;
			if (status != _o_.status) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += status;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(status).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SUpdateLockInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


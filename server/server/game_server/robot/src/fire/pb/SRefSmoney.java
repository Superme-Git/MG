
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefSmoney__ extends mkio.Protocol { }

/** 通知客户端刷新储备金
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefSmoney extends __SRefSmoney__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786450;

	public int getType() {
		return 786450;
	}

	public long smoney;

	public SRefSmoney() {
	}

	public SRefSmoney(long _smoney_) {
		this.smoney = _smoney_;
	}

	public final boolean _validator_() {
		if (smoney < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(smoney);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		smoney = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefSmoney) {
			SRefSmoney _o_ = (SRefSmoney)_o1_;
			if (smoney != _o_.smoney) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)smoney;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(smoney).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefSmoney _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(smoney - _o_.smoney);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


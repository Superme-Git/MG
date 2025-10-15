
package fire.pb.fushi.payday;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SQuerySubscribeInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SQuerySubscribeInfo extends __SQuerySubscribeInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812598;

	public int getType() {
		return 812598;
	}

	public long subscribetime; // 最近一次订阅时间
	public long expiretime; // 过期时间

	public SQuerySubscribeInfo() {
	}

	public SQuerySubscribeInfo(long _subscribetime_, long _expiretime_) {
		this.subscribetime = _subscribetime_;
		this.expiretime = _expiretime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(subscribetime);
		_os_.marshal(expiretime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		subscribetime = _os_.unmarshal_long();
		expiretime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SQuerySubscribeInfo) {
			SQuerySubscribeInfo _o_ = (SQuerySubscribeInfo)_o1_;
			if (subscribetime != _o_.subscribetime) return false;
			if (expiretime != _o_.expiretime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)subscribetime;
		_h_ += (int)expiretime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(subscribetime).append(",");
		_sb_.append(expiretime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SQuerySubscribeInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(subscribetime - _o_.subscribetime);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(expiretime - _o_.expiretime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


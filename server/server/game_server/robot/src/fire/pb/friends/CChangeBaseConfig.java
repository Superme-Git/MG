
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CChangeBaseConfig__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CChangeBaseConfig extends __CChangeBaseConfig__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806441;

	public int getType() {
		return 806441;
	}

	public byte refusestrangermsg; // 0=个拒绝陌生人消息 1=接收

	public CChangeBaseConfig() {
	}

	public CChangeBaseConfig(byte _refusestrangermsg_) {
		this.refusestrangermsg = _refusestrangermsg_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(refusestrangermsg);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		refusestrangermsg = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CChangeBaseConfig) {
			CChangeBaseConfig _o_ = (CChangeBaseConfig)_o1_;
			if (refusestrangermsg != _o_.refusestrangermsg) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)refusestrangermsg;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(refusestrangermsg).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CChangeBaseConfig _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = refusestrangermsg - _o_.refusestrangermsg;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


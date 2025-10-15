
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBindTel__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBindTel extends __SBindTel__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786555;

	public int getType() {
		return 786555;
	}

	public byte status;
	public long bindteltime;

	public SBindTel() {
	}

	public SBindTel(byte _status_, long _bindteltime_) {
		this.status = _status_;
		this.bindteltime = _bindteltime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(status);
		_os_.marshal(bindteltime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		status = _os_.unmarshal_byte();
		bindteltime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBindTel) {
			SBindTel _o_ = (SBindTel)_o1_;
			if (status != _o_.status) return false;
			if (bindteltime != _o_.bindteltime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)status;
		_h_ += (int)bindteltime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(status).append(",");
		_sb_.append(bindteltime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBindTel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(bindteltime - _o_.bindteltime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


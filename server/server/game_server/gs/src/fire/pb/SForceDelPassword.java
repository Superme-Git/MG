
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SForceDelPassword__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SForceDelPassword extends __SForceDelPassword__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786573;

	public int getType() {
		return 786573;
	}

	public long starttimepoint;
	public long finishtimepoint;

	public SForceDelPassword() {
	}

	public SForceDelPassword(long _starttimepoint_, long _finishtimepoint_) {
		this.starttimepoint = _starttimepoint_;
		this.finishtimepoint = _finishtimepoint_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(starttimepoint);
		_os_.marshal(finishtimepoint);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		starttimepoint = _os_.unmarshal_long();
		finishtimepoint = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SForceDelPassword) {
			SForceDelPassword _o_ = (SForceDelPassword)_o1_;
			if (starttimepoint != _o_.starttimepoint) return false;
			if (finishtimepoint != _o_.finishtimepoint) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)starttimepoint;
		_h_ += (int)finishtimepoint;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(starttimepoint).append(",");
		_sb_.append(finishtimepoint).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SForceDelPassword _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(starttimepoint - _o_.starttimepoint);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(finishtimepoint - _o_.finishtimepoint);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


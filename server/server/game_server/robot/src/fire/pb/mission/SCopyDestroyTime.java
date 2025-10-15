
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SCopyDestroyTime__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SCopyDestroyTime extends __SCopyDestroyTime__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805471;

	public int getType() {
		return 805471;
	}

	public long destroytime;

	public SCopyDestroyTime() {
	}

	public SCopyDestroyTime(long _destroytime_) {
		this.destroytime = _destroytime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(destroytime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		destroytime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SCopyDestroyTime) {
			SCopyDestroyTime _o_ = (SCopyDestroyTime)_o1_;
			if (destroytime != _o_.destroytime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)destroytime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(destroytime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SCopyDestroyTime _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(destroytime - _o_.destroytime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


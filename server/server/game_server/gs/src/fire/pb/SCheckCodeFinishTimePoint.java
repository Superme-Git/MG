
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SCheckCodeFinishTimePoint__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SCheckCodeFinishTimePoint extends __SCheckCodeFinishTimePoint__ {
	@Override
	protected void process() {
		
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786571;

	public int getType() {
		return 786571;
	}

	public byte checkcodetype; // 2 道具安全锁 3藏宝阁
	public long finishtimepoint;

	public SCheckCodeFinishTimePoint() {
	}

	public SCheckCodeFinishTimePoint(byte _checkcodetype_, long _finishtimepoint_) {
		this.checkcodetype = _checkcodetype_;
		this.finishtimepoint = _finishtimepoint_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(checkcodetype);
		_os_.marshal(finishtimepoint);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		checkcodetype = _os_.unmarshal_byte();
		finishtimepoint = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SCheckCodeFinishTimePoint) {
			SCheckCodeFinishTimePoint _o_ = (SCheckCodeFinishTimePoint)_o1_;
			if (checkcodetype != _o_.checkcodetype) return false;
			if (finishtimepoint != _o_.finishtimepoint) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)checkcodetype;
		_h_ += (int)finishtimepoint;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(checkcodetype).append(",");
		_sb_.append(finishtimepoint).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SCheckCodeFinishTimePoint _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = checkcodetype - _o_.checkcodetype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(finishtimepoint - _o_.finishtimepoint);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}



package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqPointSchemeTime__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqPointSchemeTime extends __SReqPointSchemeTime__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786542;

	public int getType() {
		return 786542;
	}

	public int schemetimes; // 切换加点方案次数

	public SReqPointSchemeTime() {
	}

	public SReqPointSchemeTime(int _schemetimes_) {
		this.schemetimes = _schemetimes_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(schemetimes);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		schemetimes = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqPointSchemeTime) {
			SReqPointSchemeTime _o_ = (SReqPointSchemeTime)_o1_;
			if (schemetimes != _o_.schemetimes) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += schemetimes;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(schemetimes).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SReqPointSchemeTime _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = schemetimes - _o_.schemetimes;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


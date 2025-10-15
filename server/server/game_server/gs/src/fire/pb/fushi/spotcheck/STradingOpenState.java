
package fire.pb.fushi.spotcheck;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STradingOpenState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STradingOpenState extends __STradingOpenState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812646;

	public int getType() {
		return 812646;
	}

	public byte openstate; // 0关闭   1开启

	public STradingOpenState() {
	}

	public STradingOpenState(byte _openstate_) {
		this.openstate = _openstate_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(openstate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		openstate = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STradingOpenState) {
			STradingOpenState _o_ = (STradingOpenState)_o1_;
			if (openstate != _o_.openstate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)openstate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(openstate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(STradingOpenState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = openstate - _o_.openstate;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


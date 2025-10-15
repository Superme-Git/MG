
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SDeadLess20__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SDeadLess20 extends __SDeadLess20__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793563;

	public int getType() {
		return 793563;
	}

	public int eventtype; // 类型：0为死亡：1为逃跑

	public SDeadLess20() {
	}

	public SDeadLess20(int _eventtype_) {
		this.eventtype = _eventtype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(eventtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		eventtype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SDeadLess20) {
			SDeadLess20 _o_ = (SDeadLess20)_o1_;
			if (eventtype != _o_.eventtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += eventtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(eventtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SDeadLess20 _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = eventtype - _o_.eventtype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


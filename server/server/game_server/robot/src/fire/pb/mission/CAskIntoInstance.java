
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAskIntoInstance__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAskIntoInstance extends __CAskIntoInstance__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805539;

	public int getType() {
		return 805539;
	}

	public short answer; // 1表示同意, 0表示不同意
	public int insttype; // 0 日常副本; 1时光之穴

	public CAskIntoInstance() {
	}

	public CAskIntoInstance(short _answer_, int _insttype_) {
		this.answer = _answer_;
		this.insttype = _insttype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(answer);
		_os_.marshal(insttype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		answer = _os_.unmarshal_short();
		insttype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAskIntoInstance) {
			CAskIntoInstance _o_ = (CAskIntoInstance)_o1_;
			if (answer != _o_.answer) return false;
			if (insttype != _o_.insttype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += answer;
		_h_ += insttype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(answer).append(",");
		_sb_.append(insttype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAskIntoInstance _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = answer - _o_.answer;
		if (0 != _c_) return _c_;
		_c_ = insttype - _o_.insttype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


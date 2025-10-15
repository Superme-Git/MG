
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendServerMultiExp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendServerMultiExp extends __SSendServerMultiExp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786502;

	public int getType() {
		return 786502;
	}

	public int addvalue; // 0为没有多倍，1-3为多1到3倍（即2-4倍）

	public SSendServerMultiExp() {
	}

	public SSendServerMultiExp(int _addvalue_) {
		this.addvalue = _addvalue_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(addvalue);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		addvalue = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendServerMultiExp) {
			SSendServerMultiExp _o_ = (SSendServerMultiExp)_o1_;
			if (addvalue != _o_.addvalue) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += addvalue;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(addvalue).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendServerMultiExp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = addvalue - _o_.addvalue;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


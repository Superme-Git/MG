
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SImpExamHelp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SImpExamHelp extends __SImpExamHelp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795468;

	public int getType() {
		return 795468;
	}

	public byte helpcnt;

	public SImpExamHelp() {
	}

	public SImpExamHelp(byte _helpcnt_) {
		this.helpcnt = _helpcnt_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(helpcnt);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		helpcnt = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SImpExamHelp) {
			SImpExamHelp _o_ = (SImpExamHelp)_o1_;
			if (helpcnt != _o_.helpcnt) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)helpcnt;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(helpcnt).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SImpExamHelp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = helpcnt - _o_.helpcnt;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


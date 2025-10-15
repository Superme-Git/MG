
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CDefineTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CDefineTeam extends __CDefineTeam__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805548;

	public int getType() {
		return 805548;
	}

	public short answer; // 1表示同意, 0表示不同意

	public CDefineTeam() {
	}

	public CDefineTeam(short _answer_) {
		this.answer = _answer_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(answer);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		answer = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CDefineTeam) {
			CDefineTeam _o_ = (CDefineTeam)_o1_;
			if (answer != _o_.answer) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += answer;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(answer).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CDefineTeam _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = answer - _o_.answer;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


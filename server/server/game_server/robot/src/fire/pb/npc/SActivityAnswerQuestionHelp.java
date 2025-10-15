
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SActivityAnswerQuestionHelp__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SActivityAnswerQuestionHelp extends __SActivityAnswerQuestionHelp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795535;

	public int getType() {
		return 795535;
	}

	public int helpnum;

	public SActivityAnswerQuestionHelp() {
	}

	public SActivityAnswerQuestionHelp(int _helpnum_) {
		this.helpnum = _helpnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(helpnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		helpnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SActivityAnswerQuestionHelp) {
			SActivityAnswerQuestionHelp _o_ = (SActivityAnswerQuestionHelp)_o1_;
			if (helpnum != _o_.helpnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += helpnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(helpnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SActivityAnswerQuestionHelp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = helpnum - _o_.helpnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


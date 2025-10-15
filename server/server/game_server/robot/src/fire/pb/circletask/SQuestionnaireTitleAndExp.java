
package fire.pb.circletask;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SQuestionnaireTitleAndExp__ extends mkio.Protocol { }

/** 问卷调查获得称号和经验
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SQuestionnaireTitleAndExp extends __SQuestionnaireTitleAndExp__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807445;

	public int getType() {
		return 807445;
	}

	public int title;
	public long exp;

	public SQuestionnaireTitleAndExp() {
	}

	public SQuestionnaireTitleAndExp(int _title_, long _exp_) {
		this.title = _title_;
		this.exp = _exp_;
	}

	public final boolean _validator_() {
		if (title < 0) return false;
		if (exp < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(title);
		_os_.marshal(exp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		title = _os_.unmarshal_int();
		exp = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SQuestionnaireTitleAndExp) {
			SQuestionnaireTitleAndExp _o_ = (SQuestionnaireTitleAndExp)_o1_;
			if (title != _o_.title) return false;
			if (exp != _o_.exp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += title;
		_h_ += (int)exp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(title).append(",");
		_sb_.append(exp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SQuestionnaireTitleAndExp _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = title - _o_.title;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(exp - _o_.exp);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


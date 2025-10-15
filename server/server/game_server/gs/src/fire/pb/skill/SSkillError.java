
package fire.pb.skill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSkillError__ extends mkio.Protocol { }

/** 职业心法
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSkillError extends __SSkillError__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800436;

	public int getType() {
		return 800436;
	}

	public int skillerror; // 参考SkillError的bean定义

	public SSkillError() {
	}

	public SSkillError(int _skillerror_) {
		this.skillerror = _skillerror_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(skillerror);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		skillerror = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSkillError) {
			SSkillError _o_ = (SSkillError)_o1_;
			if (skillerror != _o_.skillerror) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += skillerror;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skillerror).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSkillError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = skillerror - _o_.skillerror;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


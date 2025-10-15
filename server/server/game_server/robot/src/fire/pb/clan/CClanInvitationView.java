
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CClanInvitationView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CClanInvitationView extends __CClanInvitationView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808520;

	public int getType() {
		return 808520;
	}

	public int type_level; // 等级删选  -1表示所有
	public int type_school; // 职业删选  -1表示所有
	public int type_sex; // 性别删选  1男  2女  -1表示所有

	public CClanInvitationView() {
	}

	public CClanInvitationView(int _type_level_, int _type_school_, int _type_sex_) {
		this.type_level = _type_level_;
		this.type_school = _type_school_;
		this.type_sex = _type_sex_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(type_level);
		_os_.marshal(type_school);
		_os_.marshal(type_sex);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		type_level = _os_.unmarshal_int();
		type_school = _os_.unmarshal_int();
		type_sex = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CClanInvitationView) {
			CClanInvitationView _o_ = (CClanInvitationView)_o1_;
			if (type_level != _o_.type_level) return false;
			if (type_school != _o_.type_school) return false;
			if (type_sex != _o_.type_sex) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += type_level;
		_h_ += type_school;
		_h_ += type_sex;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(type_level).append(",");
		_sb_.append(type_school).append(",");
		_sb_.append(type_sex).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CClanInvitationView _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = type_level - _o_.type_level;
		if (0 != _c_) return _c_;
		_c_ = type_school - _o_.type_school;
		if (0 != _c_) return _c_;
		_c_ = type_sex - _o_.type_sex;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


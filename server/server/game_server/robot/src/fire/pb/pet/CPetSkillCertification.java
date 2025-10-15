
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPetSkillCertification__ extends mkio.Protocol { }

/** 客户端请求宠物技能认证
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPetSkillCertification extends __CPetSkillCertification__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788519;

	public int getType() {
		return 788519;
	}

	public int petkey; // 宠物key
	public int skillid; // 技能ID
	public int isconfirm; // 1:确定/0:取消

	public CPetSkillCertification() {
	}

	public CPetSkillCertification(int _petkey_, int _skillid_, int _isconfirm_) {
		this.petkey = _petkey_;
		this.skillid = _skillid_;
		this.isconfirm = _isconfirm_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		if (skillid < 1) return false;
		if (isconfirm < 0 || isconfirm > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(skillid);
		_os_.marshal(isconfirm);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		skillid = _os_.unmarshal_int();
		isconfirm = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPetSkillCertification) {
			CPetSkillCertification _o_ = (CPetSkillCertification)_o1_;
			if (petkey != _o_.petkey) return false;
			if (skillid != _o_.skillid) return false;
			if (isconfirm != _o_.isconfirm) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += skillid;
		_h_ += isconfirm;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(skillid).append(",");
		_sb_.append(isconfirm).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPetSkillCertification _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = skillid - _o_.skillid;
		if (0 != _c_) return _c_;
		_c_ = isconfirm - _o_.isconfirm;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


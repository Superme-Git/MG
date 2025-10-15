
package fire.pb.skill;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CUseSceneSkill__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CUseSceneSkill extends __CUseSceneSkill__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 800452;

	public int getType() {
		return 800452;
	}

	public int skillid; // 技能ID
	public int aimtype; // 技能使用目标类型 对自己角色使用==1，对自己战斗宠物使用==2，对正常队友角色使用==3
	public long aimid; // 技能使用目标ID，为队友角色ID

	public CUseSceneSkill() {
	}

	public CUseSceneSkill(int _skillid_, int _aimtype_, long _aimid_) {
		this.skillid = _skillid_;
		this.aimtype = _aimtype_;
		this.aimid = _aimid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(skillid);
		_os_.marshal(aimtype);
		_os_.marshal(aimid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		skillid = _os_.unmarshal_int();
		aimtype = _os_.unmarshal_int();
		aimid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CUseSceneSkill) {
			CUseSceneSkill _o_ = (CUseSceneSkill)_o1_;
			if (skillid != _o_.skillid) return false;
			if (aimtype != _o_.aimtype) return false;
			if (aimid != _o_.aimid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += skillid;
		_h_ += aimtype;
		_h_ += (int)aimid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skillid).append(",");
		_sb_.append(aimtype).append(",");
		_sb_.append(aimid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CUseSceneSkill _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = skillid - _o_.skillid;
		if (0 != _c_) return _c_;
		_c_ = aimtype - _o_.aimtype;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(aimid - _o_.aimid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


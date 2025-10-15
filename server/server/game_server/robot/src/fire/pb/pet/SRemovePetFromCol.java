
package fire.pb.pet;

import robot.LoginRole;
import robot.LoginRoleMgr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRemovePetFromCol__ extends mkio.Protocol { }

/** 通知客户端删除指定宠物栏上的指定key对应的宠物
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRemovePetFromCol extends __SRemovePetFromCol__ {
	@Override
	protected void process() {
		// protocol handle
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if(role!= null){
			role.getRoleBag().removePet(petkey);
			role.onRoleAddProtocol(this);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788445;

	public int getType() {
		return 788445;
	}

	public int columnid; // 宠物栏id
	public int petkey; // 要删除的宠物key

	public SRemovePetFromCol() {
	}

	public SRemovePetFromCol(int _columnid_, int _petkey_) {
		this.columnid = _columnid_;
		this.petkey = _petkey_;
	}

	public final boolean _validator_() {
		if (columnid < 1) return false;
		if (petkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(columnid);
		_os_.marshal(petkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		columnid = _os_.unmarshal_int();
		petkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRemovePetFromCol) {
			SRemovePetFromCol _o_ = (SRemovePetFromCol)_o1_;
			if (columnid != _o_.columnid) return false;
			if (petkey != _o_.petkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += columnid;
		_h_ += petkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(columnid).append(",");
		_sb_.append(petkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRemovePetFromCol _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = columnid - _o_.columnid;
		if (0 != _c_) return _c_;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


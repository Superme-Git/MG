
package fire.pb.pet;

import robot.LoginRole;
import robot.LoginRoleMgr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAddPetToColumn__ extends mkio.Protocol { }

/** 通知客户端添加宠物
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAddPetToColumn extends __SAddPetToColumn__ {
	@Override
	protected void process() {
		// protocol handle
		LoginRole role = LoginRoleMgr.findLoginRole(this);
		if(role!= null){
			role.getRoleBag().addPet(petdata);
			role.onRoleAddProtocol(this);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788444;

	public int getType() {
		return 788444;
	}

	public int columnid; // 角色宠物栏1, 仓库宠物栏2
	public fire.pb.Pet petdata; // 宠物详细数据

	public SAddPetToColumn() {
		petdata = new fire.pb.Pet();
	}

	public SAddPetToColumn(int _columnid_, fire.pb.Pet _petdata_) {
		this.columnid = _columnid_;
		this.petdata = _petdata_;
	}

	public final boolean _validator_() {
		if (columnid < 1) return false;
		if (!petdata._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(columnid);
		_os_.marshal(petdata);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		columnid = _os_.unmarshal_int();
		petdata.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAddPetToColumn) {
			SAddPetToColumn _o_ = (SAddPetToColumn)_o1_;
			if (columnid != _o_.columnid) return false;
			if (!petdata.equals(_o_.petdata)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += columnid;
		_h_ += petdata.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(columnid).append(",");
		_sb_.append(petdata).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


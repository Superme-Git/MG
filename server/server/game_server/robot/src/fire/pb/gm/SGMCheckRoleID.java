
package fire.pb.gm;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGMCheckRoleID__ extends mkio.Protocol { }

/** GM 检测的角色ID正确时，服务器回复 GM 角色ID检测的请求， 角色ID不正确，服务器只发送提示消息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGMCheckRoleID extends __SGMCheckRoleID__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 791435;

	public int getType() {
		return 791435;
	}

	public fire.pb.move.RoleSimpleInfo roleinfo;

	public SGMCheckRoleID() {
		roleinfo = new fire.pb.move.RoleSimpleInfo();
	}

	public SGMCheckRoleID(fire.pb.move.RoleSimpleInfo _roleinfo_) {
		this.roleinfo = _roleinfo_;
	}

	public final boolean _validator_() {
		if (!roleinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleinfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleinfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGMCheckRoleID) {
			SGMCheckRoleID _o_ = (SGMCheckRoleID)_o1_;
			if (!roleinfo.equals(_o_.roleinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += roleinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


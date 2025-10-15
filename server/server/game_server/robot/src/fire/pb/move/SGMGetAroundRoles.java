
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGMGetAroundRoles__ extends mkio.Protocol { }

/** 服务器回复GM请求周围所有玩家信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGMGetAroundRoles extends __SGMGetAroundRoles__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790470;

	public int getType() {
		return 790470;
	}

	public java.util.ArrayList<fire.pb.move.RoleSimpleInfo> roles;

	public SGMGetAroundRoles() {
		roles = new java.util.ArrayList<fire.pb.move.RoleSimpleInfo>();
	}

	public SGMGetAroundRoles(java.util.ArrayList<fire.pb.move.RoleSimpleInfo> _roles_) {
		this.roles = _roles_;
	}

	public final boolean _validator_() {
		for (fire.pb.move.RoleSimpleInfo _v_ : roles)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(roles.size());
		for (fire.pb.move.RoleSimpleInfo _v_ : roles) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.RoleSimpleInfo _v_ = new fire.pb.move.RoleSimpleInfo();
			_v_.unmarshal(_os_);
			roles.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGMGetAroundRoles) {
			SGMGetAroundRoles _o_ = (SGMGetAroundRoles)_o1_;
			if (!roles.equals(_o_.roles)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += roles.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roles).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


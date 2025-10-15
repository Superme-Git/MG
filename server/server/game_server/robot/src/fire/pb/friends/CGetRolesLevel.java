
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetRolesLevel__ extends mkio.Protocol { }

/** 获取一批角色等级
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetRolesLevel extends __CGetRolesLevel__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806645;

	public int getType() {
		return 806645;
	}

	public java.util.ArrayList<Long> roles; // 一批角色id
	public int gettype;

	public CGetRolesLevel() {
		roles = new java.util.ArrayList<Long>();
	}

	public CGetRolesLevel(java.util.ArrayList<Long> _roles_, int _gettype_) {
		this.roles = _roles_;
		this.gettype = _gettype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(roles.size());
		for (Long _v_ : roles) {
			_os_.marshal(_v_);
		}
		_os_.marshal(gettype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			roles.add(_v_);
		}
		gettype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetRolesLevel) {
			CGetRolesLevel _o_ = (CGetRolesLevel)_o1_;
			if (!roles.equals(_o_.roles)) return false;
			if (gettype != _o_.gettype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += roles.hashCode();
		_h_ += gettype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roles).append(",");
		_sb_.append(gettype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


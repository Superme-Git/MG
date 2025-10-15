
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __PassportGetRoleListRep__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class PassportGetRoleListRep extends __PassportGetRoleListRep__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 215;

	public int getType() {
		return 215;
	}

	public int xid;
	public int retcode; // 0成功，1没有角色,2网络通信错误
	public java.util.ArrayList<gnet.RoleSimpleInfo> rolelist; // 角色列表

	public PassportGetRoleListRep() {
		retcode = 0;
		rolelist = new java.util.ArrayList<gnet.RoleSimpleInfo>();
	}

	public PassportGetRoleListRep(int _xid_, int _retcode_, java.util.ArrayList<gnet.RoleSimpleInfo> _rolelist_) {
		this.xid = _xid_;
		this.retcode = _retcode_;
		this.rolelist = _rolelist_;
	}

	public final boolean _validator_() {
		for (gnet.RoleSimpleInfo _v_ : rolelist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(xid);
		_os_.marshal(retcode);
		_os_.compact_uint32(rolelist.size());
		for (gnet.RoleSimpleInfo _v_ : rolelist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		xid = _os_.unmarshal_int();
		retcode = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			gnet.RoleSimpleInfo _v_ = new gnet.RoleSimpleInfo();
			_v_.unmarshal(_os_);
			rolelist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PassportGetRoleListRep) {
			PassportGetRoleListRep _o_ = (PassportGetRoleListRep)_o1_;
			if (xid != _o_.xid) return false;
			if (retcode != _o_.retcode) return false;
			if (!rolelist.equals(_o_.rolelist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += xid;
		_h_ += retcode;
		_h_ += rolelist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(xid).append(",");
		_sb_.append(retcode).append(",");
		_sb_.append(rolelist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


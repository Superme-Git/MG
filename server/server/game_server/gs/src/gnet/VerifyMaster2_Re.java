
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __VerifyMaster2_Re__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class VerifyMaster2_Re extends __VerifyMaster2_Re__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 605;

	public int getType() {
		return 605;
	}

	public com.locojoy.base.Octets rolename;
	public int ret;

	public VerifyMaster2_Re() {
		rolename = new com.locojoy.base.Octets();
		ret = -1;
	}

	public VerifyMaster2_Re(com.locojoy.base.Octets _rolename_, int _ret_) {
		this.rolename = _rolename_;
		this.ret = _ret_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rolename);
		_os_.marshal(ret);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolename = _os_.unmarshal_Octets();
		ret = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof VerifyMaster2_Re) {
			VerifyMaster2_Re _o_ = (VerifyMaster2_Re)_o1_;
			if (!rolename.equals(_o_.rolename)) return false;
			if (ret != _o_.ret) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolename.hashCode();
		_h_ += ret;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("B").append(rolename.size()).append(",");
		_sb_.append(ret).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


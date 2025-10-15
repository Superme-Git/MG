
package fire.pb.hook;

import gnet.link.Onlines;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CClientLockScreen__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CClientLockScreen extends __CClientLockScreen__ {
	@Override
	protected void process() {
		// protocol handle
		gnet.link.Role role = Onlines.getInstance().find(this);
		if(role == null)
			return;
		//判断是否能下线，并正常下线。下线完毕后，再发SReturnRoleList消息
		new fire.pb.state.PRoleLockScreen(role.getRoleid(),(lock!=0)?Boolean.TRUE:Boolean.FALSE).submit();		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810344;

	public int getType() {
		return 810344;
	}

	public byte lock;

	public CClientLockScreen() {
	}

	public CClientLockScreen(byte _lock_) {
		this.lock = _lock_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(lock);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		lock = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CClientLockScreen) {
			CClientLockScreen _o_ = (CClientLockScreen)_o1_;
			if (lock != _o_.lock) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)lock;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(lock).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CClientLockScreen _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = lock - _o_.lock;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


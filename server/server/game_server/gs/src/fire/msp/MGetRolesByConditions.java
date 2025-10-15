
package fire.msp;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MGetRolesByConditions__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MGetRolesByConditions extends __MGetRolesByConditions__ {
	@Override
	protected void process() {
		// protocol handle
		new PGetRolesCallBack().submit();
	}
	
	class PGetRolesCallBack extends mkdb.Procedure
	{
		
		@Override
		protected boolean process()
		{
			xbean.GetRolesCallBackInst inst = xtable.Getrolescallback.get(callbackid);
			xtable.Getrolescallback.remove(callbackid);
			IGetRolesCallBack callback = inst.getCallback();
			callback.process(roleids);
			return true;
		}
		
	}
	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720908;

	public int getType() {
		return 720908;
	}

	public long callbackid; // 回调Id
	public java.util.LinkedList<Long> roleids; // 符合条件的roleId

	public MGetRolesByConditions() {
		roleids = new java.util.LinkedList<Long>();
	}

	public MGetRolesByConditions(long _callbackid_, java.util.LinkedList<Long> _roleids_) {
		this.callbackid = _callbackid_;
		this.roleids = _roleids_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(callbackid);
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		callbackid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MGetRolesByConditions) {
			MGetRolesByConditions _o_ = (MGetRolesByConditions)_o1_;
			if (callbackid != _o_.callbackid) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)callbackid;
		_h_ += roleids.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(callbackid).append(",");
		_sb_.append(roleids).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


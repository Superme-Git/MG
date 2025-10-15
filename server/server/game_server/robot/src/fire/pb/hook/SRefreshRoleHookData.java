
package fire.pb.hook;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshRoleHookData__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshRoleHookData extends __SRefreshRoleHookData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810333;

	public int getType() {
		return 810333;
	}

	public fire.pb.hook.HookData rolehookdata;

	public SRefreshRoleHookData() {
		rolehookdata = new fire.pb.hook.HookData();
	}

	public SRefreshRoleHookData(fire.pb.hook.HookData _rolehookdata_) {
		this.rolehookdata = _rolehookdata_;
	}

	public final boolean _validator_() {
		if (!rolehookdata._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rolehookdata);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolehookdata.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshRoleHookData) {
			SRefreshRoleHookData _o_ = (SRefreshRoleHookData)_o1_;
			if (!rolehookdata.equals(_o_.rolehookdata)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolehookdata.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolehookdata).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


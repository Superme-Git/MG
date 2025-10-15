
package fire.pb.hook;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshRoleHookExpData__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshRoleHookExpData extends __SRefreshRoleHookExpData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810340;

	public int getType() {
		return 810340;
	}

	public fire.pb.hook.HookExpData rolehookexpdata;

	public SRefreshRoleHookExpData() {
		rolehookexpdata = new fire.pb.hook.HookExpData();
	}

	public SRefreshRoleHookExpData(fire.pb.hook.HookExpData _rolehookexpdata_) {
		this.rolehookexpdata = _rolehookexpdata_;
	}

	public final boolean _validator_() {
		if (!rolehookexpdata._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rolehookexpdata);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolehookexpdata.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshRoleHookExpData) {
			SRefreshRoleHookExpData _o_ = (SRefreshRoleHookExpData)_o1_;
			if (!rolehookexpdata.equals(_o_.rolehookexpdata)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolehookexpdata.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolehookexpdata).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshRoleHookExpData _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = rolehookexpdata.compareTo(_o_.rolehookexpdata);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


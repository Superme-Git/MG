
package fire.pb.hook;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshRoleHookBattleData__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshRoleHookBattleData extends __SRefreshRoleHookBattleData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810339;

	public int getType() {
		return 810339;
	}

	public fire.pb.hook.HookBattleData rolehookbattledata;

	public SRefreshRoleHookBattleData() {
		rolehookbattledata = new fire.pb.hook.HookBattleData();
	}

	public SRefreshRoleHookBattleData(fire.pb.hook.HookBattleData _rolehookbattledata_) {
		this.rolehookbattledata = _rolehookbattledata_;
	}

	public final boolean _validator_() {
		if (!rolehookbattledata._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rolehookbattledata);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolehookbattledata.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshRoleHookBattleData) {
			SRefreshRoleHookBattleData _o_ = (SRefreshRoleHookBattleData)_o1_;
			if (!rolehookbattledata.equals(_o_.rolehookbattledata)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolehookbattledata.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolehookbattledata).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshRoleHookBattleData _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = rolehookbattledata.compareTo(_o_.rolehookbattledata);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


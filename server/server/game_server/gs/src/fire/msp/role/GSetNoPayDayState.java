
package fire.msp.role;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSetNoPayDayState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSetNoPayDayState extends __GSetNoPayDayState__ {
	@Override
	protected void process() {
		// protocol handle
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(role != null)
			role.setNoPayDay(ok == 1);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730901;

	public int getType() {
		return 730901;
	}

	public long roleid; // roleid
	public byte ok; // 1没支付0是支付 by changhao

	public GSetNoPayDayState() {
	}

	public GSetNoPayDayState(long _roleid_, byte _ok_) {
		this.roleid = _roleid_;
		this.ok = _ok_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(ok);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		ok = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSetNoPayDayState) {
			GSetNoPayDayState _o_ = (GSetNoPayDayState)_o1_;
			if (roleid != _o_.roleid) return false;
			if (ok != _o_.ok) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)ok;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(ok).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSetNoPayDayState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = ok - _o_.ok;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


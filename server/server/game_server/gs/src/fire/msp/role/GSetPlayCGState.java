
package fire.msp.role;

import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSetPlayCGState__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSetPlayCGState extends __GSetPlayCGState__ {
	@Override
	protected void process() {
		// protocol handle
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(role != null)
			role.setPlayingCGState(isplaying == 1);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730899;

	public int getType() {
		return 730899;
	}

	public long roleid; // roleid
	public byte isplaying; // 1正在播放动画，0停止播放动画

	public GSetPlayCGState() {
	}

	public GSetPlayCGState(long _roleid_, byte _isplaying_) {
		this.roleid = _roleid_;
		this.isplaying = _isplaying_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(isplaying);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		isplaying = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSetPlayCGState) {
			GSetPlayCGState _o_ = (GSetPlayCGState)_o1_;
			if (roleid != _o_.roleid) return false;
			if (isplaying != _o_.isplaying) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)isplaying;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(isplaying).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSetPlayCGState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = isplaying - _o_.isplaying;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


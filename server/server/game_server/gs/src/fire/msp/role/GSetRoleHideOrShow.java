
package fire.msp.role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSetRoleHideOrShow__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSetRoleHideOrShow extends __GSetRoleHideOrShow__ {
	@Override
	protected void process() {
		if (hide==1){
			fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
			role.notifyAroundForgetMe();
			role.setVisible(false);
			fire.pb.move.SHideRole shr=new fire.pb.move.SHideRole(1);
	        gnet.link.Onlines.getInstance().send(roleid, shr);
		}else {
			fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
			role.setVisible(true);
			role.notifyAroundSeeMe();
			fire.pb.move.SHideRole shr=new fire.pb.move.SHideRole(0);
		    gnet.link.Onlines.getInstance().send(roleid, shr);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730904;

	public int getType() {
		return 730904;
	}

	public long roleid; // roleid
	public int hide; // 1表示hide,0表示show

	public GSetRoleHideOrShow() {
	}

	public GSetRoleHideOrShow(long _roleid_, int _hide_) {
		this.roleid = _roleid_;
		this.hide = _hide_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(hide);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		hide = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSetRoleHideOrShow) {
			GSetRoleHideOrShow _o_ = (GSetRoleHideOrShow)_o1_;
			if (roleid != _o_.roleid) return false;
			if (hide != _o_.hide) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += hide;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(hide).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSetRoleHideOrShow _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = hide - _o_.hide;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


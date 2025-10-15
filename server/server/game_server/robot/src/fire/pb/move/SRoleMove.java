
package fire.pb.move;

import robot.LoginUI;
import mkio.ILoginIns;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRoleMove__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRoleMove extends __SRoleMove__ {
	@Override
	protected void process() {
		/**
		 * 不用理会服务器通知的别人移动，只管自己即可
		 */
		ILoginIns ins = (ILoginIns)this.getContext();
		LoginUI ui = (LoginUI)ins.getLoginUI();
		if(ui.getLoginRole() != null){
			ui.getLoginRole().onRoleMove( this);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790434;

	public int getType() {
		return 790434;
	}

	public long roleid; // 角色ID
	public fire.pb.move.Pos destpos; // 到达点

	public SRoleMove() {
		destpos = new fire.pb.move.Pos();
	}

	public SRoleMove(long _roleid_, fire.pb.move.Pos _destpos_) {
		this.roleid = _roleid_;
		this.destpos = _destpos_;
	}

	public final boolean _validator_() {
		if (!destpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(destpos);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		destpos.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRoleMove) {
			SRoleMove _o_ = (SRoleMove)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!destpos.equals(_o_.destpos)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += destpos.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(destpos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRoleMove _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = destpos.compareTo(_o_.destpos);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


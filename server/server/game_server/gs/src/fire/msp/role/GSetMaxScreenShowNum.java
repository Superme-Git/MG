
package fire.msp.role;


import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.Position;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSetMaxScreenShowNum__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSetMaxScreenShowNum extends __GSetMaxScreenShowNum__ {
	@Override
	protected void process() {
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(role == null)
			return;
		
		role.setMaxShowNum(maxscreenshownum);
		
		Position pos = role.getPos();
		role.updatePositionForce(pos);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730913;

	public int getType() {
		return 730913;
	}

	public long roleid;
	public int maxscreenshownum;

	public GSetMaxScreenShowNum() {
	}

	public GSetMaxScreenShowNum(long _roleid_, int _maxscreenshownum_) {
		this.roleid = _roleid_;
		this.maxscreenshownum = _maxscreenshownum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(maxscreenshownum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		maxscreenshownum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSetMaxScreenShowNum) {
			GSetMaxScreenShowNum _o_ = (GSetMaxScreenShowNum)_o1_;
			if (roleid != _o_.roleid) return false;
			if (maxscreenshownum != _o_.maxscreenshownum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += maxscreenshownum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(maxscreenshownum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSetMaxScreenShowNum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = maxscreenshownum - _o_.maxscreenshownum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


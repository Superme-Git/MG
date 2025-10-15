
package fire.msp.role;

import fire.pb.scene.manager.RoleManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSetProgressbarInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSetProgressbarInfo extends __GSetProgressbarInfo__ {
	@Override
	protected void process() {
		fire.pb.scene.movable.Role mrole = RoleManager.getInstance().getRoleByID(roleid);
		if (mrole == null)
			return;
		if (progresson==0) 
		    mrole.setProgressing(false);
		else {
			mrole.setProgressing(true);
		}

	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730898;

	public int getType() {
		return 730898;
	}

	public long roleid; // roleid
	public int progresson; // 1表示在读进度条,0表示结束或打断

	public GSetProgressbarInfo() {
	}

	public GSetProgressbarInfo(long _roleid_, int _progresson_) {
		this.roleid = _roleid_;
		this.progresson = _progresson_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(progresson);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		progresson = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSetProgressbarInfo) {
			GSetProgressbarInfo _o_ = (GSetProgressbarInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (progresson != _o_.progresson) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += progresson;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(progresson).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSetProgressbarInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = progresson - _o_.progresson;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


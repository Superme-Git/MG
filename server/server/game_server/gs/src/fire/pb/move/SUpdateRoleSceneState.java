
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateRoleSceneState__ extends mkio.Protocol { }

/** 服务器刷新一个玩家的场景状态
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateRoleSceneState extends __SUpdateRoleSceneState__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790471;

	public int getType() {
		return 790471;
	}

	public long roleid;
	public int scenestate;

	public SUpdateRoleSceneState() {
	}

	public SUpdateRoleSceneState(long _roleid_, int _scenestate_) {
		this.roleid = _roleid_;
		this.scenestate = _scenestate_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(scenestate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		scenestate = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateRoleSceneState) {
			SUpdateRoleSceneState _o_ = (SUpdateRoleSceneState)_o1_;
			if (roleid != _o_.roleid) return false;
			if (scenestate != _o_.scenestate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += scenestate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(scenestate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SUpdateRoleSceneState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = scenestate - _o_.scenestate;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


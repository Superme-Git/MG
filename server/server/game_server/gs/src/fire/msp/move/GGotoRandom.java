
package fire.msp.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GGotoRandom__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GGotoRandom extends __GGotoRandom__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if(null == role) return;
		
		role.justGotoRandom(sceneid,gototype);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724901;

	public int getType() {
		return 724901;
	}

	public long roleid;
	public long sceneid; // 场景id
	public int gototype; // 跳转类型

	public GGotoRandom() {
	}

	public GGotoRandom(long _roleid_, long _sceneid_, int _gototype_) {
		this.roleid = _roleid_;
		this.sceneid = _sceneid_;
		this.gototype = _gototype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(sceneid);
		_os_.marshal(gototype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		sceneid = _os_.unmarshal_long();
		gototype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GGotoRandom) {
			GGotoRandom _o_ = (GGotoRandom)_o1_;
			if (roleid != _o_.roleid) return false;
			if (sceneid != _o_.sceneid) return false;
			if (gototype != _o_.gototype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)sceneid;
		_h_ += gototype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(gototype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GGotoRandom _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = gototype - _o_.gototype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


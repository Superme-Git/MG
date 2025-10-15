
package fire.msp.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GGotoRandomInARegion__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GGotoRandomInARegion extends __GGotoRandomInARegion__ {
	@Override
	protected void process() {
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if(null == role) return;
		
		role.justGotoRandom(sceneid, xpos, ypos, lenx, leny,gototype);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724911;

	public int getType() {
		return 724911;
	}

	public long roleid;
	public long sceneid; // 场景id
	public int xpos; // left top point
	public int ypos;
	public int lenx; // 矩形的宽
	public int leny; // 矩形的高
	public int gototype; // 跳转类型

	public GGotoRandomInARegion() {
	}

	public GGotoRandomInARegion(long _roleid_, long _sceneid_, int _xpos_, int _ypos_, int _lenx_, int _leny_, int _gototype_) {
		this.roleid = _roleid_;
		this.sceneid = _sceneid_;
		this.xpos = _xpos_;
		this.ypos = _ypos_;
		this.lenx = _lenx_;
		this.leny = _leny_;
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
		_os_.marshal(xpos);
		_os_.marshal(ypos);
		_os_.marshal(lenx);
		_os_.marshal(leny);
		_os_.marshal(gototype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		sceneid = _os_.unmarshal_long();
		xpos = _os_.unmarshal_int();
		ypos = _os_.unmarshal_int();
		lenx = _os_.unmarshal_int();
		leny = _os_.unmarshal_int();
		gototype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GGotoRandomInARegion) {
			GGotoRandomInARegion _o_ = (GGotoRandomInARegion)_o1_;
			if (roleid != _o_.roleid) return false;
			if (sceneid != _o_.sceneid) return false;
			if (xpos != _o_.xpos) return false;
			if (ypos != _o_.ypos) return false;
			if (lenx != _o_.lenx) return false;
			if (leny != _o_.leny) return false;
			if (gototype != _o_.gototype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)sceneid;
		_h_ += xpos;
		_h_ += ypos;
		_h_ += lenx;
		_h_ += leny;
		_h_ += gototype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(xpos).append(",");
		_sb_.append(ypos).append(",");
		_sb_.append(lenx).append(",");
		_sb_.append(leny).append(",");
		_sb_.append(gototype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GGotoRandomInARegion _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = xpos - _o_.xpos;
		if (0 != _c_) return _c_;
		_c_ = ypos - _o_.ypos;
		if (0 != _c_) return _c_;
		_c_ = lenx - _o_.lenx;
		if (0 != _c_) return _c_;
		_c_ = leny - _o_.leny;
		if (0 != _c_) return _c_;
		_c_ = gototype - _o_.gototype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


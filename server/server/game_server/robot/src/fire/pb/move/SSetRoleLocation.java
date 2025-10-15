
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSetRoleLocation__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSetRoleLocation extends __SSetRoleLocation__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790436;

	public int getType() {
		return 790436;
	}

	public long roleid;
	public fire.pb.move.Pos position; // 人物当前点
	public byte locz; // 人物当前点z坐标

	public SSetRoleLocation() {
		position = new fire.pb.move.Pos();
	}

	public SSetRoleLocation(long _roleid_, fire.pb.move.Pos _position_, byte _locz_) {
		this.roleid = _roleid_;
		this.position = _position_;
		this.locz = _locz_;
	}

	public final boolean _validator_() {
		if (!position._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(position);
		_os_.marshal(locz);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		position.unmarshal(_os_);
		locz = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSetRoleLocation) {
			SSetRoleLocation _o_ = (SSetRoleLocation)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!position.equals(_o_.position)) return false;
			if (locz != _o_.locz) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += position.hashCode();
		_h_ += (int)locz;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(position).append(",");
		_sb_.append(locz).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSetRoleLocation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = position.compareTo(_o_.position);
		if (0 != _c_) return _c_;
		_c_ = locz - _o_.locz;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


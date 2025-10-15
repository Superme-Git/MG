
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqUseColor__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqUseColor extends __SReqUseColor__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786539;

	public int getType() {
		return 786539;
	}

	public fire.pb.RoleColorType rolecolorinfo; // 染色信息

	public SReqUseColor() {
		rolecolorinfo = new fire.pb.RoleColorType();
	}

	public SReqUseColor(fire.pb.RoleColorType _rolecolorinfo_) {
		this.rolecolorinfo = _rolecolorinfo_;
	}

	public final boolean _validator_() {
		if (!rolecolorinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rolecolorinfo);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolecolorinfo.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqUseColor) {
			SReqUseColor _o_ = (SReqUseColor)_o1_;
			if (!rolecolorinfo.equals(_o_.rolecolorinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolecolorinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolecolorinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SReqUseColor _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = rolecolorinfo.compareTo(_o_.rolecolorinfo);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


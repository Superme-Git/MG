
package fire.pb.school.change;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CChangeSchool__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CChangeSchool extends __CChangeSchool__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;

		PChangeSchool proc = new PChangeSchool(roleId, newshape, newschool);
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810485;

	public int getType() {
		return 810485;
	}

	public int newshape; // 新造型
	public int newschool; // 新职业

	public CChangeSchool() {
	}

	public CChangeSchool(int _newshape_, int _newschool_) {
		this.newshape = _newshape_;
		this.newschool = _newschool_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(newshape);
		_os_.marshal(newschool);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		newshape = _os_.unmarshal_int();
		newschool = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CChangeSchool) {
			CChangeSchool _o_ = (CChangeSchool)_o1_;
			if (newshape != _o_.newshape) return false;
			if (newschool != _o_.newschool) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += newshape;
		_h_ += newschool;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(newshape).append(",");
		_sb_.append(newschool).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CChangeSchool _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = newshape - _o_.newshape;
		if (0 != _c_) return _c_;
		_c_ = newschool - _o_.newschool;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


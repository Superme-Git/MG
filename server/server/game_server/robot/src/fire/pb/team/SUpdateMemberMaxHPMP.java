
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateMemberMaxHPMP__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateMemberMaxHPMP extends __SUpdateMemberMaxHPMP__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794461;

	public int getType() {
		return 794461;
	}

	public long roleid;
	public int maxhp;
	public int maxmp;

	public SUpdateMemberMaxHPMP() {
	}

	public SUpdateMemberMaxHPMP(long _roleid_, int _maxhp_, int _maxmp_) {
		this.roleid = _roleid_;
		this.maxhp = _maxhp_;
		this.maxmp = _maxmp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(maxhp);
		_os_.marshal(maxmp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		maxhp = _os_.unmarshal_int();
		maxmp = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateMemberMaxHPMP) {
			SUpdateMemberMaxHPMP _o_ = (SUpdateMemberMaxHPMP)_o1_;
			if (roleid != _o_.roleid) return false;
			if (maxhp != _o_.maxhp) return false;
			if (maxmp != _o_.maxmp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += maxhp;
		_h_ += maxmp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(maxhp).append(",");
		_sb_.append(maxmp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SUpdateMemberMaxHPMP _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = maxhp - _o_.maxhp;
		if (0 != _c_) return _c_;
		_c_ = maxmp - _o_.maxmp;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


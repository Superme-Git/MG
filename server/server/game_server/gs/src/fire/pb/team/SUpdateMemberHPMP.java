
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateMemberHPMP__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateMemberHPMP extends __SUpdateMemberHPMP__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794460;

	public int getType() {
		return 794460;
	}

	public long roleid;
	public int hp;
	public int mp;

	public SUpdateMemberHPMP() {
	}

	public SUpdateMemberHPMP(long _roleid_, int _hp_, int _mp_) {
		this.roleid = _roleid_;
		this.hp = _hp_;
		this.mp = _mp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(hp);
		_os_.marshal(mp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		hp = _os_.unmarshal_int();
		mp = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateMemberHPMP) {
			SUpdateMemberHPMP _o_ = (SUpdateMemberHPMP)_o1_;
			if (roleid != _o_.roleid) return false;
			if (hp != _o_.hp) return false;
			if (mp != _o_.mp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += hp;
		_h_ += mp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(hp).append(",");
		_sb_.append(mp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SUpdateMemberHPMP _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = hp - _o_.hp;
		if (0 != _c_) return _c_;
		_c_ = mp - _o_.mp;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


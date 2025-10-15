
package fire.pb.school.change;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SChangeSchoolExtInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SChangeSchoolExtInfo extends __SChangeSchoolExtInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810488;

	public int getType() {
		return 810488;
	}

	public int remainchangeweaponcount; // 剩余转换武器的次数
	public int remainchangegemcount; // 剩余转换宝石的次数

	public SChangeSchoolExtInfo() {
	}

	public SChangeSchoolExtInfo(int _remainchangeweaponcount_, int _remainchangegemcount_) {
		this.remainchangeweaponcount = _remainchangeweaponcount_;
		this.remainchangegemcount = _remainchangegemcount_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(remainchangeweaponcount);
		_os_.marshal(remainchangegemcount);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		remainchangeweaponcount = _os_.unmarshal_int();
		remainchangegemcount = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SChangeSchoolExtInfo) {
			SChangeSchoolExtInfo _o_ = (SChangeSchoolExtInfo)_o1_;
			if (remainchangeweaponcount != _o_.remainchangeweaponcount) return false;
			if (remainchangegemcount != _o_.remainchangegemcount) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += remainchangeweaponcount;
		_h_ += remainchangegemcount;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(remainchangeweaponcount).append(",");
		_sb_.append(remainchangegemcount).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SChangeSchoolExtInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = remainchangeweaponcount - _o_.remainchangeweaponcount;
		if (0 != _c_) return _c_;
		_c_ = remainchangegemcount - _o_.remainchangegemcount;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


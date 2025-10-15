
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetBindTel__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetBindTel extends __SGetBindTel__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786559;

	public int getType() {
		return 786559;
	}

	public long tel;
	public long createdate;
	public byte isfistloginofday;
	public byte isgetbindtelaward;
	public byte isbindtelagain;
	public long bindteltime;

	public SGetBindTel() {
	}

	public SGetBindTel(long _tel_, long _createdate_, byte _isfistloginofday_, byte _isgetbindtelaward_, byte _isbindtelagain_, long _bindteltime_) {
		this.tel = _tel_;
		this.createdate = _createdate_;
		this.isfistloginofday = _isfistloginofday_;
		this.isgetbindtelaward = _isgetbindtelaward_;
		this.isbindtelagain = _isbindtelagain_;
		this.bindteltime = _bindteltime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(tel);
		_os_.marshal(createdate);
		_os_.marshal(isfistloginofday);
		_os_.marshal(isgetbindtelaward);
		_os_.marshal(isbindtelagain);
		_os_.marshal(bindteltime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tel = _os_.unmarshal_long();
		createdate = _os_.unmarshal_long();
		isfistloginofday = _os_.unmarshal_byte();
		isgetbindtelaward = _os_.unmarshal_byte();
		isbindtelagain = _os_.unmarshal_byte();
		bindteltime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetBindTel) {
			SGetBindTel _o_ = (SGetBindTel)_o1_;
			if (tel != _o_.tel) return false;
			if (createdate != _o_.createdate) return false;
			if (isfistloginofday != _o_.isfistloginofday) return false;
			if (isgetbindtelaward != _o_.isgetbindtelaward) return false;
			if (isbindtelagain != _o_.isbindtelagain) return false;
			if (bindteltime != _o_.bindteltime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)tel;
		_h_ += (int)createdate;
		_h_ += (int)isfistloginofday;
		_h_ += (int)isgetbindtelaward;
		_h_ += (int)isbindtelagain;
		_h_ += (int)bindteltime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tel).append(",");
		_sb_.append(createdate).append(",");
		_sb_.append(isfistloginofday).append(",");
		_sb_.append(isgetbindtelaward).append(",");
		_sb_.append(isbindtelagain).append(",");
		_sb_.append(bindteltime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SGetBindTel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(tel - _o_.tel);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(createdate - _o_.createdate);
		if (0 != _c_) return _c_;
		_c_ = isfistloginofday - _o_.isfistloginofday;
		if (0 != _c_) return _c_;
		_c_ = isgetbindtelaward - _o_.isgetbindtelaward;
		if (0 != _c_) return _c_;
		_c_ = isbindtelagain - _o_.isbindtelagain;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(bindteltime - _o_.bindteltime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


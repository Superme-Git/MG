package fire.pb;

import fire.pb.tel.utils.TelBindUtils;
import fire.pb.tel.utils.TelState;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetCheckCode__ extends mkio.Protocol { }

/** 手机绑定
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CGetCheckCode extends __CGetCheckCode__ {

	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0) {
			return;
		}
		TelBindUtils.telProcess(roleid, tel,TelState.GET_BINDTEL);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786553;

	public int getType() {
		return 786553;
	}

	public long tel;

	public CGetCheckCode() {
	}

	public CGetCheckCode(long _tel_) {
		this.tel = _tel_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(tel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tel = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetCheckCode) {
			CGetCheckCode _o_ = (CGetCheckCode)_o1_;
			if (tel != _o_.tel) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)tel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetCheckCode _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(tel - _o_.tel);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}

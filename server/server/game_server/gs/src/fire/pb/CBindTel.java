package fire.pb;

import fire.pb.tel.utils.TelBindUtils;
import fire.pb.tel.utils.TelState;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBindTel__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CBindTel extends __CBindTel__ {

	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0) {
			return;
		}
		TelBindUtils.telProcess(roleid, tel, code,TelState.BINDTEL);
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786554;

	public int getType() {
		return 786554;
	}

	public long tel;
	public java.lang.String code;

	public CBindTel() {
		code = "";
	}

	public CBindTel(long _tel_, java.lang.String _code_) {
		this.tel = _tel_;
		this.code = _code_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(tel);
		_os_.marshal(code, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tel = _os_.unmarshal_long();
		code = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CBindTel) {
			CBindTel _o_ = (CBindTel)_o1_;
			if (tel != _o_.tel) return false;
			if (!code.equals(_o_.code)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)tel;
		_h_ += code.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tel).append(",");
		_sb_.append("T").append(code.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}

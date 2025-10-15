
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestImpeachMentView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestImpeachMentView extends __CRequestImpeachMentView__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0){
			return;
		}
		new PRequestImpeachMent(roleid,cmdtype).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808526;

	public int getType() {
		return 808526;
	}

	public byte cmdtype; // 0请求弹劾界面   1发起弹劾   2响应弹劾

	public CRequestImpeachMentView() {
	}

	public CRequestImpeachMentView(byte _cmdtype_) {
		this.cmdtype = _cmdtype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(cmdtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		cmdtype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestImpeachMentView) {
			CRequestImpeachMentView _o_ = (CRequestImpeachMentView)_o1_;
			if (cmdtype != _o_.cmdtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)cmdtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(cmdtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestImpeachMentView _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = cmdtype - _o_.cmdtype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


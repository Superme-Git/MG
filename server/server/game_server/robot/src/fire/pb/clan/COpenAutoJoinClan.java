
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __COpenAutoJoinClan__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class COpenAutoJoinClan extends __COpenAutoJoinClan__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808498;

	public int getType() {
		return 808498;
	}

	public int autostate; // 开启状态：0关闭  1开启
	public short requestlevel; // 申请公会等级限制

	public COpenAutoJoinClan() {
	}

	public COpenAutoJoinClan(int _autostate_, short _requestlevel_) {
		this.autostate = _autostate_;
		this.requestlevel = _requestlevel_;
	}

	public final boolean _validator_() {
		if (autostate < 0 || autostate > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(autostate);
		_os_.marshal(requestlevel);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		autostate = _os_.unmarshal_int();
		requestlevel = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof COpenAutoJoinClan) {
			COpenAutoJoinClan _o_ = (COpenAutoJoinClan)_o1_;
			if (autostate != _o_.autostate) return false;
			if (requestlevel != _o_.requestlevel) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += autostate;
		_h_ += requestlevel;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(autostate).append(",");
		_sb_.append(requestlevel).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(COpenAutoJoinClan _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = autostate - _o_.autostate;
		if (0 != _c_) return _c_;
		_c_ = requestlevel - _o_.requestlevel;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


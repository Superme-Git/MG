
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGoldOrderDownBlackMarket__ extends mkio.Protocol { }

/** 删除订单
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGoldOrderDownBlackMarket extends __SGoldOrderDownBlackMarket__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810677;

	public int getType() {
		return 810677;
	}

	public long pid; // 订单编号

	public SGoldOrderDownBlackMarket() {
	}

	public SGoldOrderDownBlackMarket(long _pid_) {
		this.pid = _pid_;
	}

	public final boolean _validator_() {
		if (pid < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(pid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGoldOrderDownBlackMarket) {
			SGoldOrderDownBlackMarket _o_ = (SGoldOrderDownBlackMarket)_o1_;
			if (pid != _o_.pid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)pid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SGoldOrderDownBlackMarket _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(pid - _o_.pid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


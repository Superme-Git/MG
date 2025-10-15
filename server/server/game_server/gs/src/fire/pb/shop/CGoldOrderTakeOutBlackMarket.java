
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGoldOrderTakeOutBlackMarket__ extends mkio.Protocol { }

/** 金币订单取出金币
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGoldOrderTakeOutBlackMarket extends __CGoldOrderTakeOutBlackMarket__ {
	@Override
	protected void process() {
		// protocol handle
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				return fire.pb.blackmarket.srv.BlackMarketManager.getInstance().goldOrderTakeOut(roleId, pid);
			}
			
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810672;

	public int getType() {
		return 810672;
	}

	public long pid; // 订单编号

	public CGoldOrderTakeOutBlackMarket() {
	}

	public CGoldOrderTakeOutBlackMarket(long _pid_) {
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
		if (_o1_ instanceof CGoldOrderTakeOutBlackMarket) {
			CGoldOrderTakeOutBlackMarket _o_ = (CGoldOrderTakeOutBlackMarket)_o1_;
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

	public int compareTo(CGoldOrderTakeOutBlackMarket _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(pid - _o_.pid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


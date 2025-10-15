
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGoldOrderUpBlackMarket__ extends mkio.Protocol { }

/** 金币订单上架
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGoldOrderUpBlackMarket extends __CGoldOrderUpBlackMarket__ {
	@Override
	protected void process() {
		// protocol handle
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				return fire.pb.blackmarket.srv.BlackMarketManager.getInstance().goldOrderUp(roleId, goldnumber, rmb);
			}
			
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810670;

	public int getType() {
		return 810670;
	}

	public long goldnumber; // 金币数量, 整数
	public long rmb; // 出售价格, 整数, 单位人民币分

	public CGoldOrderUpBlackMarket() {
	}

	public CGoldOrderUpBlackMarket(long _goldnumber_, long _rmb_) {
		this.goldnumber = _goldnumber_;
		this.rmb = _rmb_;
	}

	public final boolean _validator_() {
		if (goldnumber < 0) return false;
		if (rmb < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(goldnumber);
		_os_.marshal(rmb);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		goldnumber = _os_.unmarshal_long();
		rmb = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGoldOrderUpBlackMarket) {
			CGoldOrderUpBlackMarket _o_ = (CGoldOrderUpBlackMarket)_o1_;
			if (goldnumber != _o_.goldnumber) return false;
			if (rmb != _o_.rmb) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)goldnumber;
		_h_ += (int)rmb;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goldnumber).append(",");
		_sb_.append(rmb).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGoldOrderUpBlackMarket _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(goldnumber - _o_.goldnumber);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(rmb - _o_.rmb);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


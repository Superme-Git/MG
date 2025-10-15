
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGoldOrderUpBlackMarket__ extends mkio.Protocol { }

/** 添加新订单
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGoldOrderUpBlackMarket extends __SGoldOrderUpBlackMarket__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810675;

	public int getType() {
		return 810675;
	}

	public fire.pb.shop.GoldOrder order; // 新订单

	public SGoldOrderUpBlackMarket() {
		order = new fire.pb.shop.GoldOrder();
	}

	public SGoldOrderUpBlackMarket(fire.pb.shop.GoldOrder _order_) {
		this.order = _order_;
	}

	public final boolean _validator_() {
		if (!order._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(order);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		order.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGoldOrderUpBlackMarket) {
			SGoldOrderUpBlackMarket _o_ = (SGoldOrderUpBlackMarket)_o1_;
			if (!order.equals(_o_.order)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += order.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(order).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SGoldOrderUpBlackMarket _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = order.compareTo(_o_.order);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


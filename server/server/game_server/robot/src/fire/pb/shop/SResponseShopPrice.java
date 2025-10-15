
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SResponseShopPrice__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SResponseShopPrice extends __SResponseShopPrice__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810636;

	public int getType() {
		return 810636;
	}

	public long shopid; // 商店序号
	public java.util.ArrayList<fire.pb.shop.Goods> goodslist; // 返回的商品集合

	public SResponseShopPrice() {
		goodslist = new java.util.ArrayList<fire.pb.shop.Goods>();
	}

	public SResponseShopPrice(long _shopid_, java.util.ArrayList<fire.pb.shop.Goods> _goodslist_) {
		this.shopid = _shopid_;
		this.goodslist = _goodslist_;
	}

	public final boolean _validator_() {
		for (fire.pb.shop.Goods _v_ : goodslist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(shopid);
		_os_.compact_uint32(goodslist.size());
		for (fire.pb.shop.Goods _v_ : goodslist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		shopid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.Goods _v_ = new fire.pb.shop.Goods();
			_v_.unmarshal(_os_);
			goodslist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SResponseShopPrice) {
			SResponseShopPrice _o_ = (SResponseShopPrice)_o1_;
			if (shopid != _o_.shopid) return false;
			if (!goodslist.equals(_o_.goodslist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)shopid;
		_h_ += goodslist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shopid).append(",");
		_sb_.append(goodslist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


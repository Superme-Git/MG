
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STempMarketContainer__ extends mkio.Protocol { }

/** 拍卖临时背包,合服后浏览道具
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STempMarketContainer extends __STempMarketContainer__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810667;

	public int getType() {
		return 810667;
	}

	public java.util.ArrayList<fire.pb.shop.TempMarketContainerGoods> goodslist;

	public STempMarketContainer() {
		goodslist = new java.util.ArrayList<fire.pb.shop.TempMarketContainerGoods>();
	}

	public STempMarketContainer(java.util.ArrayList<fire.pb.shop.TempMarketContainerGoods> _goodslist_) {
		this.goodslist = _goodslist_;
	}

	public final boolean _validator_() {
		for (fire.pb.shop.TempMarketContainerGoods _v_ : goodslist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(goodslist.size());
		for (fire.pb.shop.TempMarketContainerGoods _v_ : goodslist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.shop.TempMarketContainerGoods _v_ = new fire.pb.shop.TempMarketContainerGoods();
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
		if (_o1_ instanceof STempMarketContainer) {
			STempMarketContainer _o_ = (STempMarketContainer)_o1_;
			if (!goodslist.equals(_o_.goodslist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += goodslist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goodslist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}



package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetMarketUpPrice__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetMarketUpPrice extends __SGetMarketUpPrice__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810652;

	public int getType() {
		return 810652;
	}

	public int containertype; // 1背包 2宠物栏 3拍卖宠物栏 4拍卖背包
	public int price; // 推荐价格
	public int stallprice; // 摊位费
	public java.util.ArrayList<Integer> recommendations; // 推荐价格

	public SGetMarketUpPrice() {
		recommendations = new java.util.ArrayList<Integer>();
	}

	public SGetMarketUpPrice(int _containertype_, int _price_, int _stallprice_, java.util.ArrayList<Integer> _recommendations_) {
		this.containertype = _containertype_;
		this.price = _price_;
		this.stallprice = _stallprice_;
		this.recommendations = _recommendations_;
	}

	public final boolean _validator_() {
		if (containertype < 1 || containertype > 4) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(containertype);
		_os_.marshal(price);
		_os_.marshal(stallprice);
		_os_.compact_uint32(recommendations.size());
		for (Integer _v_ : recommendations) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		containertype = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		stallprice = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			recommendations.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetMarketUpPrice) {
			SGetMarketUpPrice _o_ = (SGetMarketUpPrice)_o1_;
			if (containertype != _o_.containertype) return false;
			if (price != _o_.price) return false;
			if (stallprice != _o_.stallprice) return false;
			if (!recommendations.equals(_o_.recommendations)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += containertype;
		_h_ += price;
		_h_ += stallprice;
		_h_ += recommendations.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(containertype).append(",");
		_sb_.append(price).append(",");
		_sb_.append(stallprice).append(",");
		_sb_.append(recommendations).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


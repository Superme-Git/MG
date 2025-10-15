
package fire.pb.shop;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 从服务器获得限购或限售商品的可购买或可售卖次数
*/
public class GoodsLimit implements Marshal , Comparable<GoodsLimit>{
	public int goodsid; // 商品id
	public int number; // 可购买或可售卖数量

	public GoodsLimit() {
	}

	public GoodsLimit(int _goodsid_, int _number_) {
		this.goodsid = _goodsid_;
		this.number = _number_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(goodsid);
		_os_.marshal(number);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		goodsid = _os_.unmarshal_int();
		number = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GoodsLimit) {
			GoodsLimit _o_ = (GoodsLimit)_o1_;
			if (goodsid != _o_.goodsid) return false;
			if (number != _o_.number) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += goodsid;
		_h_ += number;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goodsid).append(",");
		_sb_.append(number).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GoodsLimit _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = goodsid - _o_.goodsid;
		if (0 != _c_) return _c_;
		_c_ = number - _o_.number;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


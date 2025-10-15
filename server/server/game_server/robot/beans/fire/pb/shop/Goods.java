
package fire.pb.shop;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 请求服务器商店商品价格,只有NPCMT3买卖物品表.xlsx中,价格是否波动这列为1的时候,服务器才会返回价格
*/
public class Goods implements Marshal , Comparable<Goods>{
	public int goodsid; // 商品id
	public int price; // 商品价格
	public int priorperiodprice; // 商品上一次价格

	public Goods() {
	}

	public Goods(int _goodsid_, int _price_, int _priorperiodprice_) {
		this.goodsid = _goodsid_;
		this.price = _price_;
		this.priorperiodprice = _priorperiodprice_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(goodsid);
		_os_.marshal(price);
		_os_.marshal(priorperiodprice);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		goodsid = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		priorperiodprice = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Goods) {
			Goods _o_ = (Goods)_o1_;
			if (goodsid != _o_.goodsid) return false;
			if (price != _o_.price) return false;
			if (priorperiodprice != _o_.priorperiodprice) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += goodsid;
		_h_ += price;
		_h_ += priorperiodprice;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goodsid).append(",");
		_sb_.append(price).append(",");
		_sb_.append(priorperiodprice).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(Goods _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = goodsid - _o_.goodsid;
		if (0 != _c_) return _c_;
		_c_ = price - _o_.price;
		if (0 != _c_) return _c_;
		_c_ = priorperiodprice - _o_.priorperiodprice;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


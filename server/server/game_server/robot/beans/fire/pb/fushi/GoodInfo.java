
package fire.pb.fushi;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class GoodInfo implements Marshal , Comparable<GoodInfo>{
	public int goodid; // 商品id
	public int price; // rmb价格
	public int fushi; // 可获得的符石
	public int present; // 额外赠送的符石
	public int beishu; // 该项倍数

	public GoodInfo() {
	}

	public GoodInfo(int _goodid_, int _price_, int _fushi_, int _present_, int _beishu_) {
		this.goodid = _goodid_;
		this.price = _price_;
		this.fushi = _fushi_;
		this.present = _present_;
		this.beishu = _beishu_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(goodid);
		_os_.marshal(price);
		_os_.marshal(fushi);
		_os_.marshal(present);
		_os_.marshal(beishu);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		goodid = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		fushi = _os_.unmarshal_int();
		present = _os_.unmarshal_int();
		beishu = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GoodInfo) {
			GoodInfo _o_ = (GoodInfo)_o1_;
			if (goodid != _o_.goodid) return false;
			if (price != _o_.price) return false;
			if (fushi != _o_.fushi) return false;
			if (present != _o_.present) return false;
			if (beishu != _o_.beishu) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += goodid;
		_h_ += price;
		_h_ += fushi;
		_h_ += present;
		_h_ += beishu;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goodid).append(",");
		_sb_.append(price).append(",");
		_sb_.append(fushi).append(",");
		_sb_.append(present).append(",");
		_sb_.append(beishu).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GoodInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = goodid - _o_.goodid;
		if (0 != _c_) return _c_;
		_c_ = price - _o_.price;
		if (0 != _c_) return _c_;
		_c_ = fushi - _o_.fushi;
		if (0 != _c_) return _c_;
		_c_ = present - _o_.present;
		if (0 != _c_) return _c_;
		_c_ = beishu - _o_.beishu;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


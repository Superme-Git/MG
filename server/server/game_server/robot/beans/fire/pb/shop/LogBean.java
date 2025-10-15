
package fire.pb.shop;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class LogBean implements Marshal , Comparable<LogBean>{
	public int itemid; // 道具id
	public int num; // 数量
	public int price; // 价格
	public int level; // 等级或品质

	public LogBean() {
	}

	public LogBean(int _itemid_, int _num_, int _price_, int _level_) {
		this.itemid = _itemid_;
		this.num = _num_;
		this.price = _price_;
		this.level = _level_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(itemid);
		_os_.marshal(num);
		_os_.marshal(price);
		_os_.marshal(level);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof LogBean) {
			LogBean _o_ = (LogBean)_o1_;
			if (itemid != _o_.itemid) return false;
			if (num != _o_.num) return false;
			if (price != _o_.price) return false;
			if (level != _o_.level) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemid;
		_h_ += num;
		_h_ += price;
		_h_ += level;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(price).append(",");
		_sb_.append(level).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(LogBean _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		_c_ = price - _o_.price;
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


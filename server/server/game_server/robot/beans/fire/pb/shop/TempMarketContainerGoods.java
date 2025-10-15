
package fire.pb.shop;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 摆摊浏览
*/
public class TempMarketContainerGoods implements Marshal , Comparable<TempMarketContainerGoods>{
	public int itemid; // 道具id
	public int num; // 数量
	public int key; // itemkey
	public int itemtype; // 物品类型 1道具, 2宠物
	public int level; // 等级或品质

	public TempMarketContainerGoods() {
	}

	public TempMarketContainerGoods(int _itemid_, int _num_, int _key_, int _itemtype_, int _level_) {
		this.itemid = _itemid_;
		this.num = _num_;
		this.key = _key_;
		this.itemtype = _itemtype_;
		this.level = _level_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(itemid);
		_os_.marshal(num);
		_os_.marshal(key);
		_os_.marshal(itemtype);
		_os_.marshal(level);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		key = _os_.unmarshal_int();
		itemtype = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TempMarketContainerGoods) {
			TempMarketContainerGoods _o_ = (TempMarketContainerGoods)_o1_;
			if (itemid != _o_.itemid) return false;
			if (num != _o_.num) return false;
			if (key != _o_.key) return false;
			if (itemtype != _o_.itemtype) return false;
			if (level != _o_.level) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemid;
		_h_ += num;
		_h_ += key;
		_h_ += itemtype;
		_h_ += level;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(key).append(",");
		_sb_.append(itemtype).append(",");
		_sb_.append(level).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(TempMarketContainerGoods _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		_c_ = itemtype - _o_.itemtype;
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


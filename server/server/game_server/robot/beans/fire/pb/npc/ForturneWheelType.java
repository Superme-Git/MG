
package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 幸运大转盘奖励类型
*/
public class ForturneWheelType implements Marshal , Comparable<ForturneWheelType>{
	public int itemtype; // 1为物品,2为经验,3为金钱
	public int id; // 物品的id,经验和金钱的id为0
	public long num; // 金钱或经验的数量,物品的数量
	public int times; // 真正的倍数值*10

	public ForturneWheelType() {
	}

	public ForturneWheelType(int _itemtype_, int _id_, long _num_, int _times_) {
		this.itemtype = _itemtype_;
		this.id = _id_;
		this.num = _num_;
		this.times = _times_;
	}

	public final boolean _validator_() {
		if (itemtype < 1) return false;
		if (id < 0) return false;
		if (num < 0) return false;
		if (times < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(itemtype);
		_os_.marshal(id);
		_os_.marshal(num);
		_os_.marshal(times);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemtype = _os_.unmarshal_int();
		id = _os_.unmarshal_int();
		num = _os_.unmarshal_long();
		times = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ForturneWheelType) {
			ForturneWheelType _o_ = (ForturneWheelType)_o1_;
			if (itemtype != _o_.itemtype) return false;
			if (id != _o_.id) return false;
			if (num != _o_.num) return false;
			if (times != _o_.times) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemtype;
		_h_ += id;
		_h_ += (int)num;
		_h_ += times;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemtype).append(",");
		_sb_.append(id).append(",");
		_sb_.append(num).append(",");
		_sb_.append(times).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ForturneWheelType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemtype - _o_.itemtype;
		if (0 != _c_) return _c_;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(num - _o_.num);
		if (0 != _c_) return _c_;
		_c_ = times - _o_.times;
		if (0 != _c_) return _c_;
		return _c_;
	}

}



package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class Item implements Marshal , Comparable<Item>{
	public final static int BIND = 0x00000001; // 不可交易给玩家，不可卖店
	public final static int FUSHI = 0x0000002; // 用符石购买而来
	public final static int ONSTALL = 0x0000004; // 摆摊出售中
	public final static int ONCOFCSELL = 0x0000008; // 在商会中上架
	public final static int CANNOTONSTALL = 0x10; // 不能卖店
	public final static int LOCK = 0x0000020; // 锁定
	public final static int TIMEOUT = 0x0000040; // 时效物品

	public int id; // 编号
	public int flags; // 标志，叠加的时候，flags 也 OR 叠加。
	public int key; // 背包属性，key
	public int position; // 背包属性，位置
	public int number; // 背包属性，数量
	public long timeout; // 到期时间。如果为0，代表没有时间限制
	public int isnew; // 1为新物品,0为旧物品
	public long loseeffecttime; // 失效时间
	public long markettime; // 摆摊道具冻结时间

	public Item() {
	}

	public Item(int _id_, int _flags_, int _key_, int _position_, int _number_, long _timeout_, int _isnew_, long _loseeffecttime_, long _markettime_) {
		this.id = _id_;
		this.flags = _flags_;
		this.key = _key_;
		this.position = _position_;
		this.number = _number_;
		this.timeout = _timeout_;
		this.isnew = _isnew_;
		this.loseeffecttime = _loseeffecttime_;
		this.markettime = _markettime_;
	}

	public final boolean _validator_() {
		if (id < 1) return false;
		if (key < 1) return false;
		if (position < 0) return false;
		if (number < 1) return false;
		if (isnew < 0 || isnew > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(flags);
		_os_.marshal(key);
		_os_.marshal(position);
		_os_.marshal(number);
		_os_.marshal(timeout);
		_os_.marshal(isnew);
		_os_.marshal(loseeffecttime);
		_os_.marshal(markettime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		flags = _os_.unmarshal_int();
		key = _os_.unmarshal_int();
		position = _os_.unmarshal_int();
		number = _os_.unmarshal_int();
		timeout = _os_.unmarshal_long();
		isnew = _os_.unmarshal_int();
		loseeffecttime = _os_.unmarshal_long();
		markettime = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Item) {
			Item _o_ = (Item)_o1_;
			if (id != _o_.id) return false;
			if (flags != _o_.flags) return false;
			if (key != _o_.key) return false;
			if (position != _o_.position) return false;
			if (number != _o_.number) return false;
			if (timeout != _o_.timeout) return false;
			if (isnew != _o_.isnew) return false;
			if (loseeffecttime != _o_.loseeffecttime) return false;
			if (markettime != _o_.markettime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += flags;
		_h_ += key;
		_h_ += position;
		_h_ += number;
		_h_ += (int)timeout;
		_h_ += isnew;
		_h_ += (int)loseeffecttime;
		_h_ += (int)markettime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(flags).append(",");
		_sb_.append(key).append(",");
		_sb_.append(position).append(",");
		_sb_.append(number).append(",");
		_sb_.append(timeout).append(",");
		_sb_.append(isnew).append(",");
		_sb_.append(loseeffecttime).append(",");
		_sb_.append(markettime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(Item _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = flags - _o_.flags;
		if (0 != _c_) return _c_;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		_c_ = position - _o_.position;
		if (0 != _c_) return _c_;
		_c_ = number - _o_.number;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(timeout - _o_.timeout);
		if (0 != _c_) return _c_;
		_c_ = isnew - _o_.isnew;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(loseeffecttime - _o_.loseeffecttime);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(markettime - _o_.markettime);
		if (0 != _c_) return _c_;
		return _c_;
	}

}


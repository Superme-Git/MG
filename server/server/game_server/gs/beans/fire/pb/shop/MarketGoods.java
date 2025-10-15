
package fire.pb.shop;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 摆摊浏览
*/
public class MarketGoods implements Marshal , Comparable<MarketGoods>{
	public long id; // 唯一id(是我服务器数据库的id,传给前端的目的是在购买物品时给我返回)
	public long saleroleid; // 卖家角色id
	public int itemid; // 道具id
	public int num; // 数量
	public int noticenum; // 关注数
	public int key; // itemkey
	public int price; // 价格
	public long showtime; // 公示时间
	public long expiretime; // 过期时间
	public int itemtype; // 物品类型 1道具, 2宠物
	public int level; // 等级或品质
	public int attentionnumber; // 关注数量

	public MarketGoods() {
	}

	public MarketGoods(long _id_, long _saleroleid_, int _itemid_, int _num_, int _noticenum_, int _key_, int _price_, long _showtime_, long _expiretime_, int _itemtype_, int _level_, int _attentionnumber_) {
		this.id = _id_;
		this.saleroleid = _saleroleid_;
		this.itemid = _itemid_;
		this.num = _num_;
		this.noticenum = _noticenum_;
		this.key = _key_;
		this.price = _price_;
		this.showtime = _showtime_;
		this.expiretime = _expiretime_;
		this.itemtype = _itemtype_;
		this.level = _level_;
		this.attentionnumber = _attentionnumber_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(saleroleid);
		_os_.marshal(itemid);
		_os_.marshal(num);
		_os_.marshal(noticenum);
		_os_.marshal(key);
		_os_.marshal(price);
		_os_.marshal(showtime);
		_os_.marshal(expiretime);
		_os_.marshal(itemtype);
		_os_.marshal(level);
		_os_.marshal(attentionnumber);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_long();
		saleroleid = _os_.unmarshal_long();
		itemid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		noticenum = _os_.unmarshal_int();
		key = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		showtime = _os_.unmarshal_long();
		expiretime = _os_.unmarshal_long();
		itemtype = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		attentionnumber = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MarketGoods) {
			MarketGoods _o_ = (MarketGoods)_o1_;
			if (id != _o_.id) return false;
			if (saleroleid != _o_.saleroleid) return false;
			if (itemid != _o_.itemid) return false;
			if (num != _o_.num) return false;
			if (noticenum != _o_.noticenum) return false;
			if (key != _o_.key) return false;
			if (price != _o_.price) return false;
			if (showtime != _o_.showtime) return false;
			if (expiretime != _o_.expiretime) return false;
			if (itemtype != _o_.itemtype) return false;
			if (level != _o_.level) return false;
			if (attentionnumber != _o_.attentionnumber) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)id;
		_h_ += (int)saleroleid;
		_h_ += itemid;
		_h_ += num;
		_h_ += noticenum;
		_h_ += key;
		_h_ += price;
		_h_ += (int)showtime;
		_h_ += (int)expiretime;
		_h_ += itemtype;
		_h_ += level;
		_h_ += attentionnumber;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(saleroleid).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(noticenum).append(",");
		_sb_.append(key).append(",");
		_sb_.append(price).append(",");
		_sb_.append(showtime).append(",");
		_sb_.append(expiretime).append(",");
		_sb_.append(itemtype).append(",");
		_sb_.append(level).append(",");
		_sb_.append(attentionnumber).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MarketGoods _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(id - _o_.id);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(saleroleid - _o_.saleroleid);
		if (0 != _c_) return _c_;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		_c_ = noticenum - _o_.noticenum;
		if (0 != _c_) return _c_;
		_c_ = key - _o_.key;
		if (0 != _c_) return _c_;
		_c_ = price - _o_.price;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(showtime - _o_.showtime);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(expiretime - _o_.expiretime);
		if (0 != _c_) return _c_;
		_c_ = itemtype - _o_.itemtype;
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		_c_ = attentionnumber - _o_.attentionnumber;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


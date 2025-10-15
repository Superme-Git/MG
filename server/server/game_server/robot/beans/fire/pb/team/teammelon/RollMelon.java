
package fire.pb.team.teammelon;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 8090-8099
*/
public class RollMelon implements Marshal {
	public long melonid; // 分赃ID by changhao
	public int itemid; // 道具表id by changhao
	public int itemnum; // 道具数量 by changhao
	public com.locojoy.base.Octets itemdata; // 道具的数据 by changhao

	public RollMelon() {
		itemdata = new com.locojoy.base.Octets();
	}

	public RollMelon(long _melonid_, int _itemid_, int _itemnum_, com.locojoy.base.Octets _itemdata_) {
		this.melonid = _melonid_;
		this.itemid = _itemid_;
		this.itemnum = _itemnum_;
		this.itemdata = _itemdata_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(melonid);
		_os_.marshal(itemid);
		_os_.marshal(itemnum);
		_os_.marshal(itemdata);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		melonid = _os_.unmarshal_long();
		itemid = _os_.unmarshal_int();
		itemnum = _os_.unmarshal_int();
		itemdata = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RollMelon) {
			RollMelon _o_ = (RollMelon)_o1_;
			if (melonid != _o_.melonid) return false;
			if (itemid != _o_.itemid) return false;
			if (itemnum != _o_.itemnum) return false;
			if (!itemdata.equals(_o_.itemdata)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)melonid;
		_h_ += itemid;
		_h_ += itemnum;
		_h_ += itemdata.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(melonid).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(itemnum).append(",");
		_sb_.append("B").append(itemdata.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}


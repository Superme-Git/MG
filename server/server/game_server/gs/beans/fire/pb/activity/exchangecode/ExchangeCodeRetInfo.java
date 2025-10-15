
package fire.pb.activity.exchangecode;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ExchangeCodeRetInfo implements Marshal {
	public int itemtype; // 类型Id
	public long itemid; // 道具Id
	public int itemcount; // 道具数目
	public java.lang.String preinfos; // 预留信息

	public ExchangeCodeRetInfo() {
		preinfos = "";
	}

	public ExchangeCodeRetInfo(int _itemtype_, long _itemid_, int _itemcount_, java.lang.String _preinfos_) {
		this.itemtype = _itemtype_;
		this.itemid = _itemid_;
		this.itemcount = _itemcount_;
		this.preinfos = _preinfos_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(itemtype);
		_os_.marshal(itemid);
		_os_.marshal(itemcount);
		_os_.marshal(preinfos, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemtype = _os_.unmarshal_int();
		itemid = _os_.unmarshal_long();
		itemcount = _os_.unmarshal_int();
		preinfos = _os_.unmarshal_String("UTF-16LE");
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ExchangeCodeRetInfo) {
			ExchangeCodeRetInfo _o_ = (ExchangeCodeRetInfo)_o1_;
			if (itemtype != _o_.itemtype) return false;
			if (itemid != _o_.itemid) return false;
			if (itemcount != _o_.itemcount) return false;
			if (!preinfos.equals(_o_.preinfos)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemtype;
		_h_ += (int)itemid;
		_h_ += itemcount;
		_h_ += preinfos.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemtype).append(",");
		_sb_.append(itemid).append(",");
		_sb_.append(itemcount).append(",");
		_sb_.append("T").append(preinfos.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}


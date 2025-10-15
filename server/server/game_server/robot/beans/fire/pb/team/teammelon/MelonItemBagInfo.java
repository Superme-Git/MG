
package fire.pb.team.teammelon;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class MelonItemBagInfo implements Marshal , Comparable<MelonItemBagInfo>{
	public int itemkey;
	public int bagid;

	public MelonItemBagInfo() {
	}

	public MelonItemBagInfo(int _itemkey_, int _bagid_) {
		this.itemkey = _itemkey_;
		this.bagid = _bagid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(itemkey);
		_os_.marshal(bagid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemkey = _os_.unmarshal_int();
		bagid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MelonItemBagInfo) {
			MelonItemBagInfo _o_ = (MelonItemBagInfo)_o1_;
			if (itemkey != _o_.itemkey) return false;
			if (bagid != _o_.bagid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemkey;
		_h_ += bagid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemkey).append(",");
		_sb_.append(bagid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MelonItemBagInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemkey - _o_.itemkey;
		if (0 != _c_) return _c_;
		_c_ = bagid - _o_.bagid;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


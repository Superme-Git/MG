
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RuneRequestInfo implements Marshal , Comparable<RuneRequestInfo>{
	public int itemid; // 物品id

	public RuneRequestInfo() {
	}

	public RuneRequestInfo(int _itemid_) {
		this.itemid = _itemid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(itemid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RuneRequestInfo) {
			RuneRequestInfo _o_ = (RuneRequestInfo)_o1_;
			if (itemid != _o_.itemid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(RuneRequestInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemid - _o_.itemid;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


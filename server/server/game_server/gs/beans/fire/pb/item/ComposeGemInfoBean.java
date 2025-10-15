
package fire.pb.item;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 合成宝石
*/
public class ComposeGemInfoBean implements Marshal , Comparable<ComposeGemInfoBean>{
	public int itemidorgoodid; // 如果是背包里的,就是itemId,如果是商店里的,就是goodid
	public int num; // 数量

	public ComposeGemInfoBean() {
	}

	public ComposeGemInfoBean(int _itemidorgoodid_, int _num_) {
		this.itemidorgoodid = _itemidorgoodid_;
		this.num = _num_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(itemidorgoodid);
		_os_.marshal(num);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemidorgoodid = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ComposeGemInfoBean) {
			ComposeGemInfoBean _o_ = (ComposeGemInfoBean)_o1_;
			if (itemidorgoodid != _o_.itemidorgoodid) return false;
			if (num != _o_.num) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemidorgoodid;
		_h_ += num;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemidorgoodid).append(",");
		_sb_.append(num).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ComposeGemInfoBean _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = itemidorgoodid - _o_.itemidorgoodid;
		if (0 != _c_) return _c_;
		_c_ = num - _o_.num;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


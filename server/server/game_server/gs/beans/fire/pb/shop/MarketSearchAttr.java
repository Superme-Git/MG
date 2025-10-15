
package fire.pb.shop;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 摆摊珍品装备搜索-基础属性结构
*/
public class MarketSearchAttr implements Marshal , Comparable<MarketSearchAttr>{
	public int attrid; // 基础属性id
	public int attrval; // 属性最小值

	public MarketSearchAttr() {
	}

	public MarketSearchAttr(int _attrid_, int _attrval_) {
		this.attrid = _attrid_;
		this.attrval = _attrval_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(attrid);
		_os_.marshal(attrval);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		attrid = _os_.unmarshal_int();
		attrval = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MarketSearchAttr) {
			MarketSearchAttr _o_ = (MarketSearchAttr)_o1_;
			if (attrid != _o_.attrid) return false;
			if (attrval != _o_.attrval) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += attrid;
		_h_ += attrval;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(attrid).append(",");
		_sb_.append(attrval).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MarketSearchAttr _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = attrid - _o_.attrid;
		if (0 != _c_) return _c_;
		_c_ = attrval - _o_.attrval;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


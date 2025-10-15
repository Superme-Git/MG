
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 所有需要增长的key又不想用xdb自增长的
*/
public class KeyCounterIndex implements Marshal , Comparable<KeyCounterIndex>{
	public final static int FACTION_KEY = 1000; // 公会
	public final static int FAMILY_KEY = 2000;
	public final static int ITEM_KEY = 3000; // 物品


	public KeyCounterIndex() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof KeyCounterIndex) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(KeyCounterIndex _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}



package fire.pb.fushi.redpack;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RedPackState implements Marshal , Comparable<RedPackState>{
	public final static int STATE_CANGET = 0; // 可以抢红包
	public final static int STATE_HAVE = 1; // 已经领取红包
	public final static int STATE_NONE = 2; // 红包已经抢光


	public RedPackState() {
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
		if (_o1_ instanceof RedPackState) {
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

	public int compareTo(RedPackState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


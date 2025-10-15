
package fire.pb.hook;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class HookFreeDpointResEnum implements Marshal , Comparable<HookFreeDpointResEnum>{
	public final static int FREEDPOINTSUCC = 0; // 领取成功
	public final static int FREEDPOINTFAIL = 1; // 领取失败


	public HookFreeDpointResEnum() {
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
		if (_o1_ instanceof HookFreeDpointResEnum) {
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

	public int compareTo(HookFreeDpointResEnum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


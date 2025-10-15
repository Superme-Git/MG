
package fire.pb.hook;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class HookMsg2ClientEnum implements Marshal , Comparable<HookMsg2ClientEnum>{
	public final static int GETDPOINTSUCC = 150151; // 领取成功
	public final static int GETDPOINTLIMIT = 150152; // 领取上限
	public final static int FREEDPOINTSUCC = 150153; // 冻结成功
	public final static int OFFLINEEXP = 150156; // 离线经验


	public HookMsg2ClientEnum() {
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
		if (_o1_ instanceof HookMsg2ClientEnum) {
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

	public int compareTo(HookMsg2ClientEnum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}



package fire.pb.hook;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class HookDataUpdateEnum implements Marshal , Comparable<HookDataUpdateEnum>{
	public final static int HOOKDATA_CANGETDPOINT = 0;
	public final static int HOOKDATA_GETDPOINT = 1;
	public final static int HOOKDATA_ISAUTOBATTLE = 2;
	public final static int HOOKDATA_CHAROPTYPE = 3;
	public final static int HOOKDATA_CHAROPID = 4;
	public final static int HOOKDATA_PETOPTYPE = 5;
	public final static int HOOKDATA_PETOPID = 6;
	public final static int HOOKDATA_OFFLINEEXP = 7;


	public HookDataUpdateEnum() {
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
		if (_o1_ instanceof HookDataUpdateEnum) {
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

	public int compareTo(HookDataUpdateEnum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}



package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class MissionFinTypes implements Marshal , Comparable<MissionFinTypes>{
	public final static int NUL = 0;
	public final static int MONEY = 1;
	public final static int ITEM = 2;
	public final static int PET = 3;
	public final static int COUNT = 4;
	public final static int ITEMCOUNT = 5;
	public final static int AREA = 6;
	public final static int QUESTION = 7;
	public final static int LEVEL = 8; // 等级
	public final static int OTHER = 9; // 其他


	public MissionFinTypes() {
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
		if (_o1_ instanceof MissionFinTypes) {
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

	public int compareTo(MissionFinTypes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}



package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TransmitTypes implements Marshal , Comparable<TransmitTypes>{
	public final static int impexamsystem = 4; // 智慧试炼考试系统拉人
	public final static int winnercall = 5; // 冠军试炼拉人
	public final static int backschoolwhile20lv = 6; // 20级时师傅拉你回职业
	public final static int singlepvp = 10; // 单人pvp拉人
	public final static int pvp3 = 15; // pvp3拉人
	public final static int pvp5 = 16; // pvp5拉人


	public TransmitTypes() {
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
		if (_o1_ instanceof TransmitTypes) {
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

	public int compareTo(TransmitTypes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


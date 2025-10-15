
package fire.pb.pet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class PetTypeEnum implements Marshal , Comparable<PetTypeEnum>{
	public final static int WILD = 1; // 野生
	public final static int BABY = 2; // 宝宝
	public final static int VARIATION = 3; // 变异
	public final static int SACREDANIMAL = 4; // 神兽


	public PetTypeEnum() {
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
		if (_o1_ instanceof PetTypeEnum) {
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

	public int compareTo(PetTypeEnum _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


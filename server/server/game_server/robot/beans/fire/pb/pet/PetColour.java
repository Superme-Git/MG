
package fire.pb.pet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class PetColour implements Marshal , Comparable<PetColour>{
	public final static int WHITE = 1;
	public final static int GREEN = 2;
	public final static int BLUE = 3;
	public final static int PURPLE = 4;
	public final static int ORANGE = 5;
	public final static int DARKGOLD = 6;


	public PetColour() {
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
		if (_o1_ instanceof PetColour) {
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

	public int compareTo(PetColour _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


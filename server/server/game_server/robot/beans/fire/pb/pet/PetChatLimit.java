
package fire.pb.pet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 宠物战斗中闲话的ID 范围
*/
public class PetChatLimit implements Marshal , Comparable<PetChatLimit>{
	public final static int Self20percentBegin = 0;
	public final static int Self20percentEnd = 1;
	public final static int Master20percentBegin = 2;
	public final static int Master20percentEnd = 3;


	public PetChatLimit() {
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
		if (_o1_ instanceof PetChatLimit) {
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

	public int compareTo(PetChatLimit _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


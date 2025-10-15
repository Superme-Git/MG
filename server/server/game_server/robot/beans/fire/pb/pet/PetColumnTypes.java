
package fire.pb.pet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class PetColumnTypes implements Marshal , Comparable<PetColumnTypes>{
	public final static int PET = 1; // 人物身上的宠物栏
	public final static int DEPOT = 2; // 人物的仓库宠物栏
	public final static int XUNBAOSELLPET = 3; // 找宝网寄售宠物栏
	public final static int XUNBAOBUYPET = 4; // 找宝网买入宠物栏
	public final static int MARKETPET = 5; // 找宝网买入宠物栏


	public PetColumnTypes() {
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
		if (_o1_ instanceof PetColumnTypes) {
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

	public int compareTo(PetColumnTypes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


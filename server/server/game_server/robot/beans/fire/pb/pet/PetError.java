
package fire.pb.pet;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class PetError implements Marshal , Comparable<PetError>{
	public final static int UnkownError = -1; // 未知错误
	public final static int KeyNotFound = -2; // key错误。找不到对应宠物
	public final static int PetcolumnFull = -3; // 添加宠物，或者存取宠物时，宠物栏已经满了
	public final static int WrongDstCol = -4; // 同背包内宠物不能移动.
	public final static int ShowPetCantMoveErr = -5; // 展示宠物不能入仓
	public final static int FightPetCantMoveErr = -6; // 参战宠物不能入仓
	public final static int PetNameOverLen = -7; // 宠物名称过长
	public final static int PetNameShotLen = -8; // 宠物名称过短
	public final static int PetNameInvalid = -9; // 宠物名字非法
	public final static int ShowPetCantFree = -10; // 展示的宠物不能放生
	public final static int FightPetCantFree = -11; // 正在参战的宠物不能放生
	public final static int WrongFreeCode = -12; // 错误的放生随机码


	public PetError() {
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
		if (_o1_ instanceof PetError) {
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

	public int compareTo(PetError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


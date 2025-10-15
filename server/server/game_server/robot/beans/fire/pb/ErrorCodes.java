
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 错误代码，oyjx
*/
public class ErrorCodes implements Marshal , Comparable<ErrorCodes>{
	public final static int NoError = 0; // 没有错误
	public final static int AddItemToBagException = 1; // 往背包添加物品出错
	public final static int NotEnoughMoney = 2; // 金钱不足
	public final static int EquipPosNotSuit = 3; // 装备位置不符合
	public final static int EquipLevelNotSuit = 4; // 装备等级不符合
	public final static int EquipSexNotSuit = 5; // 装备性别不符合


	public ErrorCodes() {
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
		if (_o1_ instanceof ErrorCodes) {
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

	public int compareTo(ErrorCodes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


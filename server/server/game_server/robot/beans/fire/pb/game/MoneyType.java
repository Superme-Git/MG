
package fire.pb.game;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 货币类型
*/
public class MoneyType implements Marshal , Comparable<MoneyType>{
	public final static int MoneyType_None = 0; // 无效类型
	public final static int MoneyType_SilverCoin = 1; // 银币
	public final static int MoneyType_GoldCoin = 2; // 金币
	public final static int MoneyType_HearthStone = 3; // 符石
	public final static int MoneyType_ProfContribute = 4; // 职业贡献
	public final static int MoneyType_RongYu = 5; // 荣誉值
	public final static int MoneyType_FactionContribute = 6; // 公会贡献
	public final static int MoneyType_ShengWang = 7; // 声望
	public final static int MoneyType_FestivalPoint = 8; // 节日积分
	public final static int MoneyType_GoodTeacherVal = 9; // 良师值
	public final static int MoneyType_RoleExp = 10; // 角色经验
	public final static int MoneyType_Activity = 11; // 活跃度
	public final static int MoneyType_Energy = 12; // 活力
	public final static int MoneyType_EreditPoint = 13; // 信用点
	public final static int MoneyType_Item = 99; // 道具
	public final static int MoneyType_Num = 15; // 类型数量


	public MoneyType() {
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
		if (_o1_ instanceof MoneyType) {
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

	public int compareTo(MoneyType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


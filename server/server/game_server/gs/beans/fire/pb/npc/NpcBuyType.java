
package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** Npc购买类型
*/
public class NpcBuyType implements Marshal , Comparable<NpcBuyType>{
	public final static int COMMON_MONEY_BUY = 0;
	public final static int COMMON_SMONEY_BUY = 1;
	public final static int FAMILY_SELL = 2;
	public final static int SKILL_SELL = 3;
	public final static int BUY_AND_USE = 6; // 用符石购买物品，购买后直接使用，如果购买多个，扣除一个后其余的放入背包
	public final static int MONEY_BUY_AND_USE = 7; // 用现金购买物品，购买后直接使用


	public NpcBuyType() {
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
		if (_o1_ instanceof NpcBuyType) {
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

	public int compareTo(NpcBuyType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


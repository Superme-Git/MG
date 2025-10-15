
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class OperationType implements Marshal , Comparable<OperationType>{
	public final static int ACTION_ATTACK = 1; // 攻击
	public final static int ACTION_SKILL = 2; // 使用技能
	public final static int ACTION_USEITEM = 3; // 使用物品
	public final static int ACTION_DEFEND = 4; // 防御
	public final static int ACTION_PROTECT = 5; // 保护
	public final static int ACTION_SUMMON = 6; // 召唤宠物
	public final static int ACTION_WITHDRAW = 7; // 召还宠物
	public final static int ACTION_CATHCH = 8; // 捕捉
	public final static int ACTION_ESCAPE = 9; // 逃跑
	public final static int ACTION_REST = 10; // 休息
	public final static int ACTION_SPECIAL_SKILL = 11; // 特殊技能
	public final static int ACTION_SUMMON_INSTANT = 12; // 瞬时召唤
	public final static int ACTION_ESCAPE_INSTANT = 13; // 瞬时逃跑
	public final static int ACTION_FAILURE = 14; // 操作失败
	public final static int ACTION_BATTLE_END = 15; // 战斗结束,只有AI怪的AI指令有这个Action
	public final static int ACTION_ENVIRONMENTDEMO = 16; // 不带施法者的Demo，attackID填0
	public final static int ACTION_ENVIRONMENTCHANGE = 17; // 战场环境改变 operateid填战场环境id
	public final static int ACTION_ROUNDENDDEMO = 18; // 回合末结算demo
	public final static int ACTION_UNIQUE_SKILL = 19; // 绝技
	public final static int ACTION_FAILURE_NO_WONDER = 20; // 操作失败_不带叹号的！


	public OperationType() {
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
		if (_o1_ instanceof OperationType) {
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

	public int compareTo(OperationType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


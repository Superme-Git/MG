
package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SubmitType implements Marshal , Comparable<SubmitType>{
	public final static int ITEM = 1; // 给予npc，任务相关的物品
	public final static int PET = 2; // 给予npc,任务相关的宠物
	public final static int MONEY = 3; // 给予npc,任务相关的金钱
	public final static int FACTION_MONEY_BOX = 4; // 给予npc,公会银箱
	public final static int FAMILY_MONEY_BOX = 5; // 给予npc,银箱
	public final static int INSTANCE_ZONE_ITEM = 13; // 副本玩法内的提交物品
	public final static int GUILD_ZONE_ITEM = 22; // 副本玩法内的提交物品


	public SubmitType() {
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
		if (_o1_ instanceof SubmitType) {
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

	public int compareTo(SubmitType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


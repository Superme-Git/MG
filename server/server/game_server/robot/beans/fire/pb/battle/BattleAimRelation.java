
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class BattleAimRelation implements Marshal , Comparable<BattleAimRelation>{
	public final static int SELF = 1; // 战斗中自己（可以是人、宠、怪、NPC）
	public final static int SELF_PET = 2; // 自己的宠物
	public final static int FRIEND_ROLE = 4; // 友方角色
	public final static int FRIEND_PET = 8; // 友方的宠物
	public final static int FRIEND_NPC = 16; // 友方NPC
	public final static int ENERMY_ROLE = 32; // 敌人角色
	public final static int ENERMY_PET = 64; // 敌人宠物
	public final static int ENERMY_NPC = 128; // 敌人NPC
	public final static int ENERMY_MONSTER = 256; // 敌人怪物
	public final static int COUPLE = 512; // 夫妻
	public final static int BROTHERS = 1024; // 结交
	public final static int MASTER_STUDENT = 2048; // 师徒
	public final static int FRIEND_MONSTER = 4096; // 友方怪物


	public BattleAimRelation() {
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
		if (_o1_ instanceof BattleAimRelation) {
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

	public int compareTo(BattleAimRelation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


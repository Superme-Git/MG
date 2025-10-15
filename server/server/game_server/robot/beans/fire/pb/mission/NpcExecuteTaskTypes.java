
package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class NpcExecuteTaskTypes implements Marshal , Comparable<NpcExecuteTaskTypes>{
	public final static int NPC_TALK = 10; // 点击npc，与npc对话
	public final static int GIVE_MONEY = 11; // 给予金钱
	public final static int GIVE_ITEM = 12; // 给予道具
	public final static int GIVE_PET = 13; // 给予宠物
	public final static int ANSWER_QUESTION = 17; // 答题
	public final static int START_BATTLE = 40; // 开始战斗


	public NpcExecuteTaskTypes() {
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
		if (_o1_ instanceof NpcExecuteTaskTypes) {
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

	public int compareTo(NpcExecuteTaskTypes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


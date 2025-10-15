
package fire.pb.npc;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class BattleToNpcError implements Marshal , Comparable<BattleToNpcError>{
	public final static int NoTeam = -1; // 未组队
	public final static int LeaderLevelIsNotEnough = -2; // 队长级别不够
	public final static int TeamMemberIsNotEnough = -3; // 队员人数不够
	public final static int NpcOnBattle = -4; // npc正在战斗


	public BattleToNpcError() {
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
		if (_o1_ instanceof BattleToNpcError) {
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

	public int compareTo(BattleToNpcError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


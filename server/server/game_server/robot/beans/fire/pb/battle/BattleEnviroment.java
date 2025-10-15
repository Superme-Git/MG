
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class BattleEnviroment implements Marshal , Comparable<BattleEnviroment>{
	public final static int DAY = 1001; // 昼
	public final static int NIGHT = 1002; // 夜
	public final static int WARM = 1003; // 暖
	public final static int COLD = 1004; // 寒
	public final static int ORCHID = 1005; // 兰
	public final static int THUNDER = 1006; // 雷


	public BattleEnviroment() {
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
		if (_o1_ instanceof BattleEnviroment) {
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

	public int compareTo(BattleEnviroment _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


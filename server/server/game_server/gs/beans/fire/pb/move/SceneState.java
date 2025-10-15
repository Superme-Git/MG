
package fire.pb.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class SceneState implements Marshal , Comparable<SceneState>{
	public final static int BATTLE_FIGHTER = 1; // 1:战斗状态
	public final static int BATTLE_WATCHER = 2; // 2:观战状态
	public final static int BATTLE_REPLAY = 4; // 4:观看录像
	public final static int PLAY_CG = 8; // 8:动画播放
	public final static int NO_PAY_DAY = 16; // 16没有支付日卡 by changhao
	public final static int GM_FLAG = 2097152; // 22:场景中显示gm图标


	public SceneState() {
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
		if (_o1_ instanceof SceneState) {
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

	public int compareTo(SceneState _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


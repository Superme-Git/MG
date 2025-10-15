
package fire.pb.talk;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class FunModelType implements Marshal , Comparable<FunModelType>{
	public final static int FUN_TASKITEM = 1; // 任务求助道具功能
	public final static int FUN_DIELIVE_CLAN = 2; // 生死战公会频道分享
	public final static int FUN_DIELIVE_WORLD = 3; // 生死战世界频道分享
	public final static int QIU_ZHU = 4; // 求助


	public FunModelType() {
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
		if (_o1_ instanceof FunModelType) {
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

	public int compareTo(FunModelType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


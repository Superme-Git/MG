
package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class MissionStatus implements Marshal , Comparable<MissionStatus>{
	public final static int ABANDON = -2; // 放弃
	public final static int UNACCEPT = -1;
	public final static int COMMITED = 1; // 已提交
	public final static int FAILED = 2; // 任务执行失败
	public final static int FINISHED = 3; // 完成
	public final static int PROCESSING = 4; // 进行中


	public MissionStatus() {
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
		if (_o1_ instanceof MissionStatus) {
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

	public int compareTo(MissionStatus _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}


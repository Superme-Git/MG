
package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TrackedMission implements Marshal , Comparable<TrackedMission>{
	public long acceptdate; // 接收时间

	public TrackedMission() {
	}

	public TrackedMission(long _acceptdate_) {
		this.acceptdate = _acceptdate_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(acceptdate);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		acceptdate = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TrackedMission) {
			TrackedMission _o_ = (TrackedMission)_o1_;
			if (acceptdate != _o_.acceptdate) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)acceptdate;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(acceptdate).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(TrackedMission _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(acceptdate - _o_.acceptdate);
		if (0 != _c_) return _c_;
		return _c_;
	}

}



package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** ping值单元
*/
public class PingStatEntry implements Marshal , Comparable<PingStatEntry>{
	public short _max;
	public short _min;
	public short _avg;

	public PingStatEntry() {
	}

	public PingStatEntry(short __max_, short __min_, short __avg_) {
		this._max = __max_;
		this._min = __min_;
		this._avg = __avg_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(_max);
		_os_.marshal(_min);
		_os_.marshal(_avg);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_max = _os_.unmarshal_short();
		_min = _os_.unmarshal_short();
		_avg = _os_.unmarshal_short();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PingStatEntry) {
			PingStatEntry _o_ = (PingStatEntry)_o1_;
			if (_max != _o_._max) return false;
			if (_min != _o_._min) return false;
			if (_avg != _o_._avg) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += _max;
		_h_ += _min;
		_h_ += _avg;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(_max).append(",");
		_sb_.append(_min).append(",");
		_sb_.append(_avg).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(PingStatEntry _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = _max - _o_._max;
		if (0 != _c_) return _c_;
		_c_ = _min - _o_._min;
		if (0 != _c_) return _c_;
		_c_ = _avg - _o_._avg;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


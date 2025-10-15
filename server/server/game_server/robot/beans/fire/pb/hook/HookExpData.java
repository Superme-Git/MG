
package fire.pb.hook;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class HookExpData implements Marshal , Comparable<HookExpData>{
	public short cangetdpoint; // 可领取双倍点数
	public short getdpoint; // 已领取双倍点数
	public long offlineexp; // 离线经验

	public HookExpData() {
	}

	public HookExpData(short _cangetdpoint_, short _getdpoint_, long _offlineexp_) {
		this.cangetdpoint = _cangetdpoint_;
		this.getdpoint = _getdpoint_;
		this.offlineexp = _offlineexp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(cangetdpoint);
		_os_.marshal(getdpoint);
		_os_.marshal(offlineexp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		cangetdpoint = _os_.unmarshal_short();
		getdpoint = _os_.unmarshal_short();
		offlineexp = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof HookExpData) {
			HookExpData _o_ = (HookExpData)_o1_;
			if (cangetdpoint != _o_.cangetdpoint) return false;
			if (getdpoint != _o_.getdpoint) return false;
			if (offlineexp != _o_.offlineexp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += cangetdpoint;
		_h_ += getdpoint;
		_h_ += (int)offlineexp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(cangetdpoint).append(",");
		_sb_.append(getdpoint).append(",");
		_sb_.append(offlineexp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(HookExpData _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = cangetdpoint - _o_.cangetdpoint;
		if (0 != _c_) return _c_;
		_c_ = getdpoint - _o_.getdpoint;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(offlineexp - _o_.offlineexp);
		if (0 != _c_) return _c_;
		return _c_;
	}

}


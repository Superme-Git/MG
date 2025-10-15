
package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class InstanceTimes implements Marshal , Comparable<InstanceTimes>{
	public int instanceid; // 副本id
	public int finishedtimes; // 完成的次数
	public int totaltimes; // 总次数

	public InstanceTimes() {
	}

	public InstanceTimes(int _instanceid_, int _finishedtimes_, int _totaltimes_) {
		this.instanceid = _instanceid_;
		this.finishedtimes = _finishedtimes_;
		this.totaltimes = _totaltimes_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(instanceid);
		_os_.marshal(finishedtimes);
		_os_.marshal(totaltimes);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		instanceid = _os_.unmarshal_int();
		finishedtimes = _os_.unmarshal_int();
		totaltimes = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof InstanceTimes) {
			InstanceTimes _o_ = (InstanceTimes)_o1_;
			if (instanceid != _o_.instanceid) return false;
			if (finishedtimes != _o_.finishedtimes) return false;
			if (totaltimes != _o_.totaltimes) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += instanceid;
		_h_ += finishedtimes;
		_h_ += totaltimes;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instanceid).append(",");
		_sb_.append(finishedtimes).append(",");
		_sb_.append(totaltimes).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(InstanceTimes _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = instanceid - _o_.instanceid;
		if (0 != _c_) return _c_;
		_c_ = finishedtimes - _o_.finishedtimes;
		if (0 != _c_) return _c_;
		_c_ = totaltimes - _o_.totaltimes;
		if (0 != _c_) return _c_;
		return _c_;
	}

}



package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 副本任务 进入副本
*/
public class InstanceInfo implements Marshal , Comparable<InstanceInfo>{
	public int id; // 任务id
	public int instanceid; // 副本id
	public int state; // 0未完成, 2已经完成 完成状态 (角色本身副本状态)
	public int instancestate; // 0未开启, 1开启 (服务器副本状态)
	public long starttime; // 开启时间
	public long endtime; // 结束时间
	public int finishedtimes; // 完成的次数
	public int totaltimes; // 总次数

	public InstanceInfo() {
	}

	public InstanceInfo(int _id_, int _instanceid_, int _state_, int _instancestate_, long _starttime_, long _endtime_, int _finishedtimes_, int _totaltimes_) {
		this.id = _id_;
		this.instanceid = _instanceid_;
		this.state = _state_;
		this.instancestate = _instancestate_;
		this.starttime = _starttime_;
		this.endtime = _endtime_;
		this.finishedtimes = _finishedtimes_;
		this.totaltimes = _totaltimes_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(instanceid);
		_os_.marshal(state);
		_os_.marshal(instancestate);
		_os_.marshal(starttime);
		_os_.marshal(endtime);
		_os_.marshal(finishedtimes);
		_os_.marshal(totaltimes);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		instanceid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		instancestate = _os_.unmarshal_int();
		starttime = _os_.unmarshal_long();
		endtime = _os_.unmarshal_long();
		finishedtimes = _os_.unmarshal_int();
		totaltimes = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof InstanceInfo) {
			InstanceInfo _o_ = (InstanceInfo)_o1_;
			if (id != _o_.id) return false;
			if (instanceid != _o_.instanceid) return false;
			if (state != _o_.state) return false;
			if (instancestate != _o_.instancestate) return false;
			if (starttime != _o_.starttime) return false;
			if (endtime != _o_.endtime) return false;
			if (finishedtimes != _o_.finishedtimes) return false;
			if (totaltimes != _o_.totaltimes) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += instanceid;
		_h_ += state;
		_h_ += instancestate;
		_h_ += (int)starttime;
		_h_ += (int)endtime;
		_h_ += finishedtimes;
		_h_ += totaltimes;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(instanceid).append(",");
		_sb_.append(state).append(",");
		_sb_.append(instancestate).append(",");
		_sb_.append(starttime).append(",");
		_sb_.append(endtime).append(",");
		_sb_.append(finishedtimes).append(",");
		_sb_.append(totaltimes).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(InstanceInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = instanceid - _o_.instanceid;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		_c_ = instancestate - _o_.instancestate;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(starttime - _o_.starttime);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(endtime - _o_.endtime);
		if (0 != _c_) return _c_;
		_c_ = finishedtimes - _o_.finishedtimes;
		if (0 != _c_) return _c_;
		_c_ = totaltimes - _o_.totaltimes;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


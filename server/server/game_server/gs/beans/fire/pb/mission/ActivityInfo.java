
package fire.pb.mission;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ActivityInfo implements Marshal , Comparable<ActivityInfo>{
	public int activityid; // 活动id
	public int state; // 角色相对活动状态, 0不可进入,1可进入
	public int activitystate; // 状态, 0未开启,1开启
	public int finishtimes; // 完成次数
	public int nextid; // 下周要开启的活动id
	public int nextnextid; // 下下周要开启的活动id

	public ActivityInfo() {
	}

	public ActivityInfo(int _activityid_, int _state_, int _activitystate_, int _finishtimes_, int _nextid_, int _nextnextid_) {
		this.activityid = _activityid_;
		this.state = _state_;
		this.activitystate = _activitystate_;
		this.finishtimes = _finishtimes_;
		this.nextid = _nextid_;
		this.nextnextid = _nextnextid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(activityid);
		_os_.marshal(state);
		_os_.marshal(activitystate);
		_os_.marshal(finishtimes);
		_os_.marshal(nextid);
		_os_.marshal(nextnextid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		activityid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		activitystate = _os_.unmarshal_int();
		finishtimes = _os_.unmarshal_int();
		nextid = _os_.unmarshal_int();
		nextnextid = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ActivityInfo) {
			ActivityInfo _o_ = (ActivityInfo)_o1_;
			if (activityid != _o_.activityid) return false;
			if (state != _o_.state) return false;
			if (activitystate != _o_.activitystate) return false;
			if (finishtimes != _o_.finishtimes) return false;
			if (nextid != _o_.nextid) return false;
			if (nextnextid != _o_.nextnextid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += activityid;
		_h_ += state;
		_h_ += activitystate;
		_h_ += finishtimes;
		_h_ += nextid;
		_h_ += nextnextid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(activityid).append(",");
		_sb_.append(state).append(",");
		_sb_.append(activitystate).append(",");
		_sb_.append(finishtimes).append(",");
		_sb_.append(nextid).append(",");
		_sb_.append(nextnextid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(ActivityInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = activityid - _o_.activityid;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		_c_ = activitystate - _o_.activitystate;
		if (0 != _c_) return _c_;
		_c_ = finishtimes - _o_.finishtimes;
		if (0 != _c_) return _c_;
		_c_ = nextid - _o_.nextid;
		if (0 != _c_) return _c_;
		_c_ = nextnextid - _o_.nextnextid;
		if (0 != _c_) return _c_;
		return _c_;
	}

}


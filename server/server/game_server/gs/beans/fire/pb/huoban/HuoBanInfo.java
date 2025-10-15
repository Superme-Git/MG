
package fire.pb.huoban;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class HuoBanInfo implements Marshal , Comparable<HuoBanInfo>{
	public final static int white = 1; // 白
	public final static int green = 2; // 绿
	public final static int blue = 3; // 蓝
	public final static int purple = 4; // 紫
	public final static int orange = 5; // 橙
	public final static int golden = 6; // 金
	public final static int pink = 7; // 粉色
	public final static int red = 8; // 红色

	public int huobanid; // 伙伴id
	public int infight; // 是否参战,1为参战
	public int weekfree; // 本周是否免费 0不免费; 1本周免费
	public long state; // 是否解锁, 0为未解锁; 1为永久使用; 大于10为有多少秒的剩余时间,如 134 表示为免费134 - 10 = 124秒

	public HuoBanInfo() {
	}

	public HuoBanInfo(int _huobanid_, int _infight_, int _weekfree_, long _state_) {
		this.huobanid = _huobanid_;
		this.infight = _infight_;
		this.weekfree = _weekfree_;
		this.state = _state_;
	}

	public final boolean _validator_() {
		if (weekfree < 0 || weekfree > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(huobanid);
		_os_.marshal(infight);
		_os_.marshal(weekfree);
		_os_.marshal(state);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		huobanid = _os_.unmarshal_int();
		infight = _os_.unmarshal_int();
		weekfree = _os_.unmarshal_int();
		state = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof HuoBanInfo) {
			HuoBanInfo _o_ = (HuoBanInfo)_o1_;
			if (huobanid != _o_.huobanid) return false;
			if (infight != _o_.infight) return false;
			if (weekfree != _o_.weekfree) return false;
			if (state != _o_.state) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += huobanid;
		_h_ += infight;
		_h_ += weekfree;
		_h_ += (int)state;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huobanid).append(",");
		_sb_.append(infight).append(",");
		_sb_.append(weekfree).append(",");
		_sb_.append(state).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(HuoBanInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = huobanid - _o_.huobanid;
		if (0 != _c_) return _c_;
		_c_ = infight - _o_.infight;
		if (0 != _c_) return _c_;
		_c_ = weekfree - _o_.weekfree;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(state - _o_.state);
		if (0 != _c_) return _c_;
		return _c_;
	}

}


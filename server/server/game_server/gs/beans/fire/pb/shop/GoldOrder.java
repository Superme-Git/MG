
package fire.pb.shop;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** 金币订单
*/
public class GoldOrder implements Marshal , Comparable<GoldOrder>{
	public long pid; // 订单编号
	public long number; // 金币数量, 整数
	public long price; // 出售价格, 整数, 单位人民币分
	public int publicity; // 公示时间, 整数, 单位小时.如果不需要公示传0
	public int locktime; // 锁定订单时间,单位秒.锁定时不能下架
	public int state; // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
	public long time; // 订单创建时间,单位毫秒

	public GoldOrder() {
		pid = 0;
		number = 0;
		price = 0;
		publicity = 0;
		locktime = 0;
		state = 0;
		time = 0;
	}

	public GoldOrder(long _pid_, long _number_, long _price_, int _publicity_, int _locktime_, int _state_, long _time_) {
		this.pid = _pid_;
		this.number = _number_;
		this.price = _price_;
		this.publicity = _publicity_;
		this.locktime = _locktime_;
		this.state = _state_;
		this.time = _time_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(pid);
		_os_.marshal(number);
		_os_.marshal(price);
		_os_.marshal(publicity);
		_os_.marshal(locktime);
		_os_.marshal(state);
		_os_.marshal(time);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pid = _os_.unmarshal_long();
		number = _os_.unmarshal_long();
		price = _os_.unmarshal_long();
		publicity = _os_.unmarshal_int();
		locktime = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		time = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GoldOrder) {
			GoldOrder _o_ = (GoldOrder)_o1_;
			if (pid != _o_.pid) return false;
			if (number != _o_.number) return false;
			if (price != _o_.price) return false;
			if (publicity != _o_.publicity) return false;
			if (locktime != _o_.locktime) return false;
			if (state != _o_.state) return false;
			if (time != _o_.time) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)pid;
		_h_ += (int)number;
		_h_ += (int)price;
		_h_ += publicity;
		_h_ += locktime;
		_h_ += state;
		_h_ += (int)time;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pid).append(",");
		_sb_.append(number).append(",");
		_sb_.append(price).append(",");
		_sb_.append(publicity).append(",");
		_sb_.append(locktime).append(",");
		_sb_.append(state).append(",");
		_sb_.append(time).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GoldOrder _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(pid - _o_.pid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(number - _o_.number);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(price - _o_.price);
		if (0 != _c_) return _c_;
		_c_ = publicity - _o_.publicity;
		if (0 != _c_) return _c_;
		_c_ = locktime - _o_.locktime;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(time - _o_.time);
		if (0 != _c_) return _c_;
		return _c_;
	}

}


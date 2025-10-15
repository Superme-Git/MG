
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendQueueInfo__ extends mkio.Protocol { }

/** 服务器：发送排队信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendQueueInfo extends __SSendQueueInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786480;

	public int getType() {
		return 786480;
	}

	public int order; // 排位 ，等于0时为在0位次，等于-1时表示排队人数已满
	public int queuelength; // 队列总长度
	public int minutes; // 剩余时间

	public SSendQueueInfo() {
	}

	public SSendQueueInfo(int _order_, int _queuelength_, int _minutes_) {
		this.order = _order_;
		this.queuelength = _queuelength_;
		this.minutes = _minutes_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(order);
		_os_.marshal(queuelength);
		_os_.marshal(minutes);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		order = _os_.unmarshal_int();
		queuelength = _os_.unmarshal_int();
		minutes = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendQueueInfo) {
			SSendQueueInfo _o_ = (SSendQueueInfo)_o1_;
			if (order != _o_.order) return false;
			if (queuelength != _o_.queuelength) return false;
			if (minutes != _o_.minutes) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += order;
		_h_ += queuelength;
		_h_ += minutes;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(order).append(",");
		_sb_.append(queuelength).append(",");
		_sb_.append(minutes).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendQueueInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = order - _o_.order;
		if (0 != _c_) return _c_;
		_c_ = queuelength - _o_.queuelength;
		if (0 != _c_) return _c_;
		_c_ = minutes - _o_.minutes;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


package fire.pb;

import robot.QueueUtils;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendSlowQueueInfo__ extends mkio.Protocol { }

/** 服务器：发送缓慢进入排队信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class SSendSlowQueueInfo extends __SSendSlowQueueInfo__ {

	@Override
	protected void process() {
	
		QueueUtils.getInstance().setQueueInfo(this, second,order,queuelength);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786484;

	public int getType() {
		return 786484;
	}

	public int order; // 排位
	public int queuelength; // 总排队人数
	public int second; // 剩余时间(秒)

	public SSendSlowQueueInfo() {
	}

	public SSendSlowQueueInfo(int _order_, int _queuelength_, int _second_) {
		this.order = _order_;
		this.queuelength = _queuelength_;
		this.second = _second_;
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
		_os_.marshal(second);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		order = _os_.unmarshal_int();
		queuelength = _os_.unmarshal_int();
		second = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendSlowQueueInfo) {
			SSendSlowQueueInfo _o_ = (SSendSlowQueueInfo)_o1_;
			if (order != _o_.order) return false;
			if (queuelength != _o_.queuelength) return false;
			if (second != _o_.second) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += order;
		_h_ += queuelength;
		_h_ += second;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(order).append(",");
		_sb_.append(queuelength).append(",");
		_sb_.append(second).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendSlowQueueInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = order - _o_.order;
		if (0 != _c_) return _c_;
		_c_ = queuelength - _o_.queuelength;
		if (0 != _c_) return _c_;
		_c_ = second - _o_.second;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}

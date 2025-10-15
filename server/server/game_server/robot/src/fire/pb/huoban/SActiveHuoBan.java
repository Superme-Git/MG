
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SActiveHuoBan__ extends mkio.Protocol { }

/** 解锁伙伴返回
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SActiveHuoBan extends __SActiveHuoBan__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818841;

	public int getType() {
		return 818841;
	}

	public int huobanid; // 伙伴ID
	public int state; // 0为未解锁; 1为永久使用; 2为本周免费; 大于10为有多少分钟的剩余时间,如 134 表示为免费134 - 10 = 124分钟

	public SActiveHuoBan() {
	}

	public SActiveHuoBan(int _huobanid_, int _state_) {
		this.huobanid = _huobanid_;
		this.state = _state_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(huobanid);
		_os_.marshal(state);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		huobanid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SActiveHuoBan) {
			SActiveHuoBan _o_ = (SActiveHuoBan)_o1_;
			if (huobanid != _o_.huobanid) return false;
			if (state != _o_.state) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += huobanid;
		_h_ += state;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huobanid).append(",");
		_sb_.append(state).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SActiveHuoBan _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = huobanid - _o_.huobanid;
		if (0 != _c_) return _c_;
		_c_ = state - _o_.state;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}



package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SClanFightOver__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SClanFightOver extends __SClanFightOver__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808543;

	public int getType() {
		return 808543;
	}

	public int status; // -1是没结束0是第一个赢了1是第2个赢了 by changhao
	public long overtimestamp; // 结束时服务器的时间戳 by changhao

	public SClanFightOver() {
	}

	public SClanFightOver(int _status_, long _overtimestamp_) {
		this.status = _status_;
		this.overtimestamp = _overtimestamp_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(status);
		_os_.marshal(overtimestamp);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		status = _os_.unmarshal_int();
		overtimestamp = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SClanFightOver) {
			SClanFightOver _o_ = (SClanFightOver)_o1_;
			if (status != _o_.status) return false;
			if (overtimestamp != _o_.overtimestamp) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += status;
		_h_ += (int)overtimestamp;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(status).append(",");
		_sb_.append(overtimestamp).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SClanFightOver _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(overtimestamp - _o_.overtimestamp);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


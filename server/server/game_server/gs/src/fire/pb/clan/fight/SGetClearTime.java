
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetClearTime__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetClearTime extends __SGetClearTime__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808546;

	public int getType() {
		return 808546;
	}

	public long cleartime; // 清除时间戳 by changhao

	public SGetClearTime() {
	}

	public SGetClearTime(long _cleartime_) {
		this.cleartime = _cleartime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(cleartime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		cleartime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetClearTime) {
			SGetClearTime _o_ = (SGetClearTime)_o1_;
			if (cleartime != _o_.cleartime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)cleartime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(cleartime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SGetClearTime _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(cleartime - _o_.cleartime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


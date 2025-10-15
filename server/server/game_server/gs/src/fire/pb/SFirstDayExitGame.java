
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SFirstDayExitGame__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SFirstDayExitGame extends __SFirstDayExitGame__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786489;

	public int getType() {
		return 786489;
	}

	public int firstdayleftsecond; // 今天还剩多少秒

	public SFirstDayExitGame() {
	}

	public SFirstDayExitGame(int _firstdayleftsecond_) {
		this.firstdayleftsecond = _firstdayleftsecond_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(firstdayleftsecond);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		firstdayleftsecond = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SFirstDayExitGame) {
			SFirstDayExitGame _o_ = (SFirstDayExitGame)_o1_;
			if (firstdayleftsecond != _o_.firstdayleftsecond) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += firstdayleftsecond;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(firstdayleftsecond).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SFirstDayExitGame _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = firstdayleftsecond - _o_.firstdayleftsecond;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


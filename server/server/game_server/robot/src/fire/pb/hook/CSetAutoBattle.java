
package fire.pb.hook;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetAutoBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetAutoBattle extends __CSetAutoBattle__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810334;

	public int getType() {
		return 810334;
	}

	public byte isautobattle; // 设置是否自动战斗

	public CSetAutoBattle() {
	}

	public CSetAutoBattle(byte _isautobattle_) {
		this.isautobattle = _isautobattle_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(isautobattle);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		isautobattle = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetAutoBattle) {
			CSetAutoBattle _o_ = (CSetAutoBattle)_o1_;
			if (isautobattle != _o_.isautobattle) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)isautobattle;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(isautobattle).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetAutoBattle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = isautobattle - _o_.isautobattle;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


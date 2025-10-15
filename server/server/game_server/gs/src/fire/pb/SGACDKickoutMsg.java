
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGACDKickoutMsg__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGACDKickoutMsg extends __SGACDKickoutMsg__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786501;

	public int getType() {
		return 786501;
	}

	public java.lang.String reason;
	public long endtime;

	public SGACDKickoutMsg() {
		reason = "";
	}

	public SGACDKickoutMsg(java.lang.String _reason_, long _endtime_) {
		this.reason = _reason_;
		this.endtime = _endtime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(reason, "UTF-16LE");
		_os_.marshal(endtime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		reason = _os_.unmarshal_String("UTF-16LE");
		endtime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGACDKickoutMsg) {
			SGACDKickoutMsg _o_ = (SGACDKickoutMsg)_o1_;
			if (!reason.equals(_o_.reason)) return false;
			if (endtime != _o_.endtime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += reason.hashCode();
		_h_ += (int)endtime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(reason.length()).append(",");
		_sb_.append(endtime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


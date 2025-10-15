
package fire.pb.game;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRoleAccusation__ extends mkio.Protocol { }

/** 举报
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRoleAccusation extends __SRoleAccusation__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810371;

	public int getType() {
		return 810371;
	}

	public byte isbereported; // 1为被举报 0为撤销举报

	public SRoleAccusation() {
	}

	public SRoleAccusation(byte _isbereported_) {
		this.isbereported = _isbereported_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(isbereported);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		isbereported = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRoleAccusation) {
			SRoleAccusation _o_ = (SRoleAccusation)_o1_;
			if (isbereported != _o_.isbereported) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)isbereported;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(isbereported).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRoleAccusation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = isbereported - _o_.isbereported;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


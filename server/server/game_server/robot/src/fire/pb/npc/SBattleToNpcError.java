
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBattleToNpcError__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBattleToNpcError extends __SBattleToNpcError__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795453;

	public int getType() {
		return 795453;
	}

	public int battleerror; // 错误代号

	public SBattleToNpcError() {
	}

	public SBattleToNpcError(int _battleerror_) {
		this.battleerror = _battleerror_;
	}

	public final boolean _validator_() {
		if (battleerror >= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(battleerror);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		battleerror = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBattleToNpcError) {
			SBattleToNpcError _o_ = (SBattleToNpcError)_o1_;
			if (battleerror != _o_.battleerror) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += battleerror;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(battleerror).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBattleToNpcError _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = battleerror - _o_.battleerror;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


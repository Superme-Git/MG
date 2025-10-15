
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendRoundPlayEnd__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendRoundPlayEnd extends __SSendRoundPlayEnd__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793462;

	public int getType() {
		return 793462;
	}

	public int fighterid; // 角色的fighterId

	public SSendRoundPlayEnd() {
	}

	public SSendRoundPlayEnd(int _fighterid_) {
		this.fighterid = _fighterid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(fighterid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		fighterid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendRoundPlayEnd) {
			SSendRoundPlayEnd _o_ = (SSendRoundPlayEnd)_o1_;
			if (fighterid != _o_.fighterid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += fighterid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(fighterid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSendRoundPlayEnd _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = fighterid - _o_.fighterid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


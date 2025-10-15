
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBattleFieldStatistics__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBattleFieldStatistics extends __SBattleFieldStatistics__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808535;

	public int getType() {
		return 808535;
	}

	public int clanscore1; // 公会1积分 by changhao
	public int clanscroe2; // 公会2积分 by changhao

	public SBattleFieldStatistics() {
	}

	public SBattleFieldStatistics(int _clanscore1_, int _clanscroe2_) {
		this.clanscore1 = _clanscore1_;
		this.clanscroe2 = _clanscroe2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(clanscore1);
		_os_.marshal(clanscroe2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanscore1 = _os_.unmarshal_int();
		clanscroe2 = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBattleFieldStatistics) {
			SBattleFieldStatistics _o_ = (SBattleFieldStatistics)_o1_;
			if (clanscore1 != _o_.clanscore1) return false;
			if (clanscroe2 != _o_.clanscroe2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += clanscore1;
		_h_ += clanscroe2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanscore1).append(",");
		_sb_.append(clanscroe2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBattleFieldStatistics _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = clanscore1 - _o_.clanscore1;
		if (0 != _c_) return _c_;
		_c_ = clanscroe2 - _o_.clanscroe2;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}



package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBattleFieldScore__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBattleFieldScore extends __SBattleFieldScore__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808536;

	public int getType() {
		return 808536;
	}

	public int clanscore1; // 公会1积分 by changhao
	public int clanscroe2; // 公会2积分 by changhao
	public int myscore; // 我的积分 by changhao
	public int myrank; // 我的排名(从0开始) by changhao

	public SBattleFieldScore() {
	}

	public SBattleFieldScore(int _clanscore1_, int _clanscroe2_, int _myscore_, int _myrank_) {
		this.clanscore1 = _clanscore1_;
		this.clanscroe2 = _clanscroe2_;
		this.myscore = _myscore_;
		this.myrank = _myrank_;
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
		_os_.marshal(myscore);
		_os_.marshal(myrank);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanscore1 = _os_.unmarshal_int();
		clanscroe2 = _os_.unmarshal_int();
		myscore = _os_.unmarshal_int();
		myrank = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBattleFieldScore) {
			SBattleFieldScore _o_ = (SBattleFieldScore)_o1_;
			if (clanscore1 != _o_.clanscore1) return false;
			if (clanscroe2 != _o_.clanscroe2) return false;
			if (myscore != _o_.myscore) return false;
			if (myrank != _o_.myrank) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += clanscore1;
		_h_ += clanscroe2;
		_h_ += myscore;
		_h_ += myrank;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanscore1).append(",");
		_sb_.append(clanscroe2).append(",");
		_sb_.append(myscore).append(",");
		_sb_.append(myrank).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SBattleFieldScore _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = clanscore1 - _o_.clanscore1;
		if (0 != _c_) return _c_;
		_c_ = clanscroe2 - _o_.clanscroe2;
		if (0 != _c_) return _c_;
		_c_ = myscore - _o_.myscore;
		if (0 != _c_) return _c_;
		_c_ = myrank - _o_.myrank;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


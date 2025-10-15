
package fire.pb.battle.pvp5;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP5MyInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP5MyInfo extends __SPvP5MyInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793664;

	public int getType() {
		return 793664;
	}

	public byte firstwin; // 首胜。0：不可领取，1：可领取，2：已领取
	public byte fivefight; // 五战。0：不可领取，1：可领取，2：已领取
	public byte battlenum; // 本场已经战斗的次数
	public byte winnum; // 本场胜利次数
	public byte combowinnum; // 本场连胜
	public int score; // 积分
	public byte camp; // 所属阵营(0 or 1)
	public long waitstarttime; // 等待的起始时间点(单位:秒, -1:不在等待中)

	public SPvP5MyInfo() {
	}

	public SPvP5MyInfo(byte _firstwin_, byte _fivefight_, byte _battlenum_, byte _winnum_, byte _combowinnum_, int _score_, byte _camp_, long _waitstarttime_) {
		this.firstwin = _firstwin_;
		this.fivefight = _fivefight_;
		this.battlenum = _battlenum_;
		this.winnum = _winnum_;
		this.combowinnum = _combowinnum_;
		this.score = _score_;
		this.camp = _camp_;
		this.waitstarttime = _waitstarttime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(firstwin);
		_os_.marshal(fivefight);
		_os_.marshal(battlenum);
		_os_.marshal(winnum);
		_os_.marshal(combowinnum);
		_os_.marshal(score);
		_os_.marshal(camp);
		_os_.marshal(waitstarttime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		firstwin = _os_.unmarshal_byte();
		fivefight = _os_.unmarshal_byte();
		battlenum = _os_.unmarshal_byte();
		winnum = _os_.unmarshal_byte();
		combowinnum = _os_.unmarshal_byte();
		score = _os_.unmarshal_int();
		camp = _os_.unmarshal_byte();
		waitstarttime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP5MyInfo) {
			SPvP5MyInfo _o_ = (SPvP5MyInfo)_o1_;
			if (firstwin != _o_.firstwin) return false;
			if (fivefight != _o_.fivefight) return false;
			if (battlenum != _o_.battlenum) return false;
			if (winnum != _o_.winnum) return false;
			if (combowinnum != _o_.combowinnum) return false;
			if (score != _o_.score) return false;
			if (camp != _o_.camp) return false;
			if (waitstarttime != _o_.waitstarttime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)firstwin;
		_h_ += (int)fivefight;
		_h_ += (int)battlenum;
		_h_ += (int)winnum;
		_h_ += (int)combowinnum;
		_h_ += score;
		_h_ += (int)camp;
		_h_ += (int)waitstarttime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(firstwin).append(",");
		_sb_.append(fivefight).append(",");
		_sb_.append(battlenum).append(",");
		_sb_.append(winnum).append(",");
		_sb_.append(combowinnum).append(",");
		_sb_.append(score).append(",");
		_sb_.append(camp).append(",");
		_sb_.append(waitstarttime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPvP5MyInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = firstwin - _o_.firstwin;
		if (0 != _c_) return _c_;
		_c_ = fivefight - _o_.fivefight;
		if (0 != _c_) return _c_;
		_c_ = battlenum - _o_.battlenum;
		if (0 != _c_) return _c_;
		_c_ = winnum - _o_.winnum;
		if (0 != _c_) return _c_;
		_c_ = combowinnum - _o_.combowinnum;
		if (0 != _c_) return _c_;
		_c_ = score - _o_.score;
		if (0 != _c_) return _c_;
		_c_ = camp - _o_.camp;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(waitstarttime - _o_.waitstarttime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


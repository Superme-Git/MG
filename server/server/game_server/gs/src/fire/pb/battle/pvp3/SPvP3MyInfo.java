
package fire.pb.battle.pvp3;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP3MyInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP3MyInfo extends __SPvP3MyInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793634;

	public int getType() {
		return 793634;
	}

	public byte firstwin; // 首胜。0：不可领取，1：可领取，2：已领取
	public byte tenfight; // 十战。0：不可领取，1：可领取，2：已领取
	public byte eightwin; // 五胜。0：不可领取，1：可领取，2：已领取
	public byte battlenum; // 本场已经战斗的次数
	public byte winnum; // 本场胜利次数
	public short combowinnum; // 本场连胜
	public int score; // 积分
	public byte ready; // 1：准备中，0：未准备

	public SPvP3MyInfo() {
	}

	public SPvP3MyInfo(byte _firstwin_, byte _tenfight_, byte _eightwin_, byte _battlenum_, byte _winnum_, short _combowinnum_, int _score_, byte _ready_) {
		this.firstwin = _firstwin_;
		this.tenfight = _tenfight_;
		this.eightwin = _eightwin_;
		this.battlenum = _battlenum_;
		this.winnum = _winnum_;
		this.combowinnum = _combowinnum_;
		this.score = _score_;
		this.ready = _ready_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(firstwin);
		_os_.marshal(tenfight);
		_os_.marshal(eightwin);
		_os_.marshal(battlenum);
		_os_.marshal(winnum);
		_os_.marshal(combowinnum);
		_os_.marshal(score);
		_os_.marshal(ready);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		firstwin = _os_.unmarshal_byte();
		tenfight = _os_.unmarshal_byte();
		eightwin = _os_.unmarshal_byte();
		battlenum = _os_.unmarshal_byte();
		winnum = _os_.unmarshal_byte();
		combowinnum = _os_.unmarshal_short();
		score = _os_.unmarshal_int();
		ready = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP3MyInfo) {
			SPvP3MyInfo _o_ = (SPvP3MyInfo)_o1_;
			if (firstwin != _o_.firstwin) return false;
			if (tenfight != _o_.tenfight) return false;
			if (eightwin != _o_.eightwin) return false;
			if (battlenum != _o_.battlenum) return false;
			if (winnum != _o_.winnum) return false;
			if (combowinnum != _o_.combowinnum) return false;
			if (score != _o_.score) return false;
			if (ready != _o_.ready) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)firstwin;
		_h_ += (int)tenfight;
		_h_ += (int)eightwin;
		_h_ += (int)battlenum;
		_h_ += (int)winnum;
		_h_ += combowinnum;
		_h_ += score;
		_h_ += (int)ready;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(firstwin).append(",");
		_sb_.append(tenfight).append(",");
		_sb_.append(eightwin).append(",");
		_sb_.append(battlenum).append(",");
		_sb_.append(winnum).append(",");
		_sb_.append(combowinnum).append(",");
		_sb_.append(score).append(",");
		_sb_.append(ready).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPvP3MyInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = firstwin - _o_.firstwin;
		if (0 != _c_) return _c_;
		_c_ = tenfight - _o_.tenfight;
		if (0 != _c_) return _c_;
		_c_ = eightwin - _o_.eightwin;
		if (0 != _c_) return _c_;
		_c_ = battlenum - _o_.battlenum;
		if (0 != _c_) return _c_;
		_c_ = winnum - _o_.winnum;
		if (0 != _c_) return _c_;
		_c_ = combowinnum - _o_.combowinnum;
		if (0 != _c_) return _c_;
		_c_ = score - _o_.score;
		if (0 != _c_) return _c_;
		_c_ = ready - _o_.ready;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


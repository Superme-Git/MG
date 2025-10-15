
package fire.pb.battle.pvp1;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP1MyInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP1MyInfo extends __SPvP1MyInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793534;

	public int getType() {
		return 793534;
	}

	public byte firstwin; // 首胜。0：不可领取，1：可领取，2：已领取
	public byte tenfight; // 十战。0：不可领取，1：可领取，2：已领取
	public byte tencombowin; // 十连胜。0：不可领取，1：可领取，2：已领取
	public byte battlenum; // 本场已经战斗的次数
	public int score; // 本赛季积分
	public byte winnum; // 本场胜利次数
	public short combowinnum; // 本场连胜
	public byte formation; // 当前光环
	public byte ready; // 1：准备中，0：未准备
	public int jieci; // 届次
	public int changci; // 场次

	public SPvP1MyInfo() {
	}

	public SPvP1MyInfo(byte _firstwin_, byte _tenfight_, byte _tencombowin_, byte _battlenum_, int _score_, byte _winnum_, short _combowinnum_, byte _formation_, byte _ready_, int _jieci_, int _changci_) {
		this.firstwin = _firstwin_;
		this.tenfight = _tenfight_;
		this.tencombowin = _tencombowin_;
		this.battlenum = _battlenum_;
		this.score = _score_;
		this.winnum = _winnum_;
		this.combowinnum = _combowinnum_;
		this.formation = _formation_;
		this.ready = _ready_;
		this.jieci = _jieci_;
		this.changci = _changci_;
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
		_os_.marshal(tencombowin);
		_os_.marshal(battlenum);
		_os_.marshal(score);
		_os_.marshal(winnum);
		_os_.marshal(combowinnum);
		_os_.marshal(formation);
		_os_.marshal(ready);
		_os_.marshal(jieci);
		_os_.marshal(changci);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		firstwin = _os_.unmarshal_byte();
		tenfight = _os_.unmarshal_byte();
		tencombowin = _os_.unmarshal_byte();
		battlenum = _os_.unmarshal_byte();
		score = _os_.unmarshal_int();
		winnum = _os_.unmarshal_byte();
		combowinnum = _os_.unmarshal_short();
		formation = _os_.unmarshal_byte();
		ready = _os_.unmarshal_byte();
		jieci = _os_.unmarshal_int();
		changci = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP1MyInfo) {
			SPvP1MyInfo _o_ = (SPvP1MyInfo)_o1_;
			if (firstwin != _o_.firstwin) return false;
			if (tenfight != _o_.tenfight) return false;
			if (tencombowin != _o_.tencombowin) return false;
			if (battlenum != _o_.battlenum) return false;
			if (score != _o_.score) return false;
			if (winnum != _o_.winnum) return false;
			if (combowinnum != _o_.combowinnum) return false;
			if (formation != _o_.formation) return false;
			if (ready != _o_.ready) return false;
			if (jieci != _o_.jieci) return false;
			if (changci != _o_.changci) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)firstwin;
		_h_ += (int)tenfight;
		_h_ += (int)tencombowin;
		_h_ += (int)battlenum;
		_h_ += score;
		_h_ += (int)winnum;
		_h_ += combowinnum;
		_h_ += (int)formation;
		_h_ += (int)ready;
		_h_ += jieci;
		_h_ += changci;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(firstwin).append(",");
		_sb_.append(tenfight).append(",");
		_sb_.append(tencombowin).append(",");
		_sb_.append(battlenum).append(",");
		_sb_.append(score).append(",");
		_sb_.append(winnum).append(",");
		_sb_.append(combowinnum).append(",");
		_sb_.append(formation).append(",");
		_sb_.append(ready).append(",");
		_sb_.append(jieci).append(",");
		_sb_.append(changci).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SPvP1MyInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = firstwin - _o_.firstwin;
		if (0 != _c_) return _c_;
		_c_ = tenfight - _o_.tenfight;
		if (0 != _c_) return _c_;
		_c_ = tencombowin - _o_.tencombowin;
		if (0 != _c_) return _c_;
		_c_ = battlenum - _o_.battlenum;
		if (0 != _c_) return _c_;
		_c_ = score - _o_.score;
		if (0 != _c_) return _c_;
		_c_ = winnum - _o_.winnum;
		if (0 != _c_) return _c_;
		_c_ = combowinnum - _o_.combowinnum;
		if (0 != _c_) return _c_;
		_c_ = formation - _o_.formation;
		if (0 != _c_) return _c_;
		_c_ = ready - _o_.ready;
		if (0 != _c_) return _c_;
		_c_ = jieci - _o_.jieci;
		if (0 != _c_) return _c_;
		_c_ = changci - _o_.changci;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


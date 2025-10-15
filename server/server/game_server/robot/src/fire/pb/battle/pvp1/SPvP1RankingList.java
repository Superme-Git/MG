
package fire.pb.battle.pvp1;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP1RankingList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP1RankingList extends __SPvP1RankingList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793536;

	public int getType() {
		return 793536;
	}

	public java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleScore> rolescores; // 一页积分榜的玩家信息
	public java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleScoreMid> rolescores3; // 我的前一名,我,我的后一名3名玩家信息
	public java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleWin> rolewins; // 连胜榜

	public SPvP1RankingList() {
		rolescores = new java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleScore>();
		rolescores3 = new java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleScoreMid>();
		rolewins = new java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleWin>();
	}

	public SPvP1RankingList(java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleScore> _rolescores_, java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleScoreMid> _rolescores3_, java.util.LinkedList<fire.pb.battle.pvp1.PvP1RoleSingleWin> _rolewins_) {
		this.rolescores = _rolescores_;
		this.rolescores3 = _rolescores3_;
		this.rolewins = _rolewins_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.pvp1.PvP1RoleSingleScore _v_ : rolescores)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.pvp1.PvP1RoleSingleScoreMid _v_ : rolescores3)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.pvp1.PvP1RoleSingleWin _v_ : rolewins)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(rolescores.size());
		for (fire.pb.battle.pvp1.PvP1RoleSingleScore _v_ : rolescores) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(rolescores3.size());
		for (fire.pb.battle.pvp1.PvP1RoleSingleScoreMid _v_ : rolescores3) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(rolewins.size());
		for (fire.pb.battle.pvp1.PvP1RoleSingleWin _v_ : rolewins) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.pvp1.PvP1RoleSingleScore _v_ = new fire.pb.battle.pvp1.PvP1RoleSingleScore();
			_v_.unmarshal(_os_);
			rolescores.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.pvp1.PvP1RoleSingleScoreMid _v_ = new fire.pb.battle.pvp1.PvP1RoleSingleScoreMid();
			_v_.unmarshal(_os_);
			rolescores3.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.pvp1.PvP1RoleSingleWin _v_ = new fire.pb.battle.pvp1.PvP1RoleSingleWin();
			_v_.unmarshal(_os_);
			rolewins.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP1RankingList) {
			SPvP1RankingList _o_ = (SPvP1RankingList)_o1_;
			if (!rolescores.equals(_o_.rolescores)) return false;
			if (!rolescores3.equals(_o_.rolescores3)) return false;
			if (!rolewins.equals(_o_.rolewins)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolescores.hashCode();
		_h_ += rolescores3.hashCode();
		_h_ += rolewins.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolescores).append(",");
		_sb_.append(rolescores3).append(",");
		_sb_.append(rolewins).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


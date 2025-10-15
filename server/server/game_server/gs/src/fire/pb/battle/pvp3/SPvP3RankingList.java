
package fire.pb.battle.pvp3;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP3RankingList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP3RankingList extends __SPvP3RankingList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793636;

	public int getType() {
		return 793636;
	}

	public byte history; // 0：当前排行，1：历史排行
	public java.util.LinkedList<fire.pb.battle.pvp3.PvP3RoleSingleScore> rolescores; // 积分榜
	public java.util.LinkedList<fire.pb.battle.pvp3.PvP3RoleSingleScoreMid> myscore; // 我自己的排名信息

	public SPvP3RankingList() {
		rolescores = new java.util.LinkedList<fire.pb.battle.pvp3.PvP3RoleSingleScore>();
		myscore = new java.util.LinkedList<fire.pb.battle.pvp3.PvP3RoleSingleScoreMid>();
	}

	public SPvP3RankingList(byte _history_, java.util.LinkedList<fire.pb.battle.pvp3.PvP3RoleSingleScore> _rolescores_, java.util.LinkedList<fire.pb.battle.pvp3.PvP3RoleSingleScoreMid> _myscore_) {
		this.history = _history_;
		this.rolescores = _rolescores_;
		this.myscore = _myscore_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.pvp3.PvP3RoleSingleScore _v_ : rolescores)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.pvp3.PvP3RoleSingleScoreMid _v_ : myscore)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(history);
		_os_.compact_uint32(rolescores.size());
		for (fire.pb.battle.pvp3.PvP3RoleSingleScore _v_ : rolescores) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(myscore.size());
		for (fire.pb.battle.pvp3.PvP3RoleSingleScoreMid _v_ : myscore) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		history = _os_.unmarshal_byte();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.pvp3.PvP3RoleSingleScore _v_ = new fire.pb.battle.pvp3.PvP3RoleSingleScore();
			_v_.unmarshal(_os_);
			rolescores.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.pvp3.PvP3RoleSingleScoreMid _v_ = new fire.pb.battle.pvp3.PvP3RoleSingleScoreMid();
			_v_.unmarshal(_os_);
			myscore.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP3RankingList) {
			SPvP3RankingList _o_ = (SPvP3RankingList)_o1_;
			if (history != _o_.history) return false;
			if (!rolescores.equals(_o_.rolescores)) return false;
			if (!myscore.equals(_o_.myscore)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)history;
		_h_ += rolescores.hashCode();
		_h_ += myscore.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(history).append(",");
		_sb_.append(rolescores).append(",");
		_sb_.append(myscore).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


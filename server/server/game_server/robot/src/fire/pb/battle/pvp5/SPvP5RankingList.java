
package fire.pb.battle.pvp5;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SPvP5RankingList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SPvP5RankingList extends __SPvP5RankingList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793666;

	public int getType() {
		return 793666;
	}

	public java.util.LinkedList<fire.pb.battle.pvp5.PvP5RoleSingleScore> rolescores1; // 积分榜1
	public java.util.LinkedList<fire.pb.battle.pvp5.PvP5RoleSingleScore> rolescores2; // 积分榜2
	public fire.pb.battle.pvp5.PvP5RoleSingleScoreMid myscore; // 我自己的排名信息

	public SPvP5RankingList() {
		rolescores1 = new java.util.LinkedList<fire.pb.battle.pvp5.PvP5RoleSingleScore>();
		rolescores2 = new java.util.LinkedList<fire.pb.battle.pvp5.PvP5RoleSingleScore>();
		myscore = new fire.pb.battle.pvp5.PvP5RoleSingleScoreMid();
	}

	public SPvP5RankingList(java.util.LinkedList<fire.pb.battle.pvp5.PvP5RoleSingleScore> _rolescores1_, java.util.LinkedList<fire.pb.battle.pvp5.PvP5RoleSingleScore> _rolescores2_, fire.pb.battle.pvp5.PvP5RoleSingleScoreMid _myscore_) {
		this.rolescores1 = _rolescores1_;
		this.rolescores2 = _rolescores2_;
		this.myscore = _myscore_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.pvp5.PvP5RoleSingleScore _v_ : rolescores1)
			if (!_v_._validator_()) return false;
		for (fire.pb.battle.pvp5.PvP5RoleSingleScore _v_ : rolescores2)
			if (!_v_._validator_()) return false;
		if (!myscore._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(rolescores1.size());
		for (fire.pb.battle.pvp5.PvP5RoleSingleScore _v_ : rolescores1) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(rolescores2.size());
		for (fire.pb.battle.pvp5.PvP5RoleSingleScore _v_ : rolescores2) {
			_os_.marshal(_v_);
		}
		_os_.marshal(myscore);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.pvp5.PvP5RoleSingleScore _v_ = new fire.pb.battle.pvp5.PvP5RoleSingleScore();
			_v_.unmarshal(_os_);
			rolescores1.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.pvp5.PvP5RoleSingleScore _v_ = new fire.pb.battle.pvp5.PvP5RoleSingleScore();
			_v_.unmarshal(_os_);
			rolescores2.add(_v_);
		}
		myscore.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SPvP5RankingList) {
			SPvP5RankingList _o_ = (SPvP5RankingList)_o1_;
			if (!rolescores1.equals(_o_.rolescores1)) return false;
			if (!rolescores2.equals(_o_.rolescores2)) return false;
			if (!myscore.equals(_o_.myscore)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolescores1.hashCode();
		_h_ += rolescores2.hashCode();
		_h_ += myscore.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolescores1).append(",");
		_sb_.append(rolescores2).append(",");
		_sb_.append(myscore).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


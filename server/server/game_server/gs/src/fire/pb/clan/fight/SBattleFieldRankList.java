
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBattleFieldRankList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBattleFieldRankList extends __SBattleFieldRankList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808539;

	public int getType() {
		return 808539;
	}

	public int clanscore1; // 公会1积分 by changhao
	public int clanscroe2; // 公会2积分 by changhao
	public java.util.LinkedList<fire.pb.clan.fight.RoleBattleFieldRank> ranklist1; // 战场积分排名list  by changhao
	public java.util.LinkedList<fire.pb.clan.fight.RoleBattleFieldRank> ranklist2; // 战场积分排名list  by changhao
	public int myscore; // 我的积分 by changhao
	public int myrank; // 我的排名(从0开始) by changhao

	public SBattleFieldRankList() {
		ranklist1 = new java.util.LinkedList<fire.pb.clan.fight.RoleBattleFieldRank>();
		ranklist2 = new java.util.LinkedList<fire.pb.clan.fight.RoleBattleFieldRank>();
	}

	public SBattleFieldRankList(int _clanscore1_, int _clanscroe2_, java.util.LinkedList<fire.pb.clan.fight.RoleBattleFieldRank> _ranklist1_, java.util.LinkedList<fire.pb.clan.fight.RoleBattleFieldRank> _ranklist2_, int _myscore_, int _myrank_) {
		this.clanscore1 = _clanscore1_;
		this.clanscroe2 = _clanscroe2_;
		this.ranklist1 = _ranklist1_;
		this.ranklist2 = _ranklist2_;
		this.myscore = _myscore_;
		this.myrank = _myrank_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.fight.RoleBattleFieldRank _v_ : ranklist1)
			if (!_v_._validator_()) return false;
		for (fire.pb.clan.fight.RoleBattleFieldRank _v_ : ranklist2)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(clanscore1);
		_os_.marshal(clanscroe2);
		_os_.compact_uint32(ranklist1.size());
		for (fire.pb.clan.fight.RoleBattleFieldRank _v_ : ranklist1) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(ranklist2.size());
		for (fire.pb.clan.fight.RoleBattleFieldRank _v_ : ranklist2) {
			_os_.marshal(_v_);
		}
		_os_.marshal(myscore);
		_os_.marshal(myrank);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanscore1 = _os_.unmarshal_int();
		clanscroe2 = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.fight.RoleBattleFieldRank _v_ = new fire.pb.clan.fight.RoleBattleFieldRank();
			_v_.unmarshal(_os_);
			ranklist1.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.fight.RoleBattleFieldRank _v_ = new fire.pb.clan.fight.RoleBattleFieldRank();
			_v_.unmarshal(_os_);
			ranklist2.add(_v_);
		}
		myscore = _os_.unmarshal_int();
		myrank = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBattleFieldRankList) {
			SBattleFieldRankList _o_ = (SBattleFieldRankList)_o1_;
			if (clanscore1 != _o_.clanscore1) return false;
			if (clanscroe2 != _o_.clanscroe2) return false;
			if (!ranklist1.equals(_o_.ranklist1)) return false;
			if (!ranklist2.equals(_o_.ranklist2)) return false;
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
		_h_ += ranklist1.hashCode();
		_h_ += ranklist2.hashCode();
		_h_ += myscore;
		_h_ += myrank;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanscore1).append(",");
		_sb_.append(clanscroe2).append(",");
		_sb_.append(ranklist1).append(",");
		_sb_.append(ranklist2).append(",");
		_sb_.append(myscore).append(",");
		_sb_.append(myrank).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


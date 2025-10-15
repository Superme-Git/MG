
package fire.pb.ranklist.getrankinfo;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRankGetInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 获得排行榜数据
 * @author changhao
 *
 */
public class CRankGetInfo extends __CRankGetInfo__ {
	@Override
	protected void process() {
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		switch(ranktype){
		case fire.pb.ranklist.RankType.ROLE_ZONGHE_RANK: //综合,职业排名 by changhao
		case fire.pb.ranklist.RankType.PROFESSION_WARRIOR_RANK:
		case fire.pb.ranklist.RankType.PROFESSION_MAGIC_RANK:
		case fire.pb.ranklist.RankType.PROFESSION_PRIEST_RANK:
		case fire.pb.ranklist.RankType.PROFESSION_PALADIN_RANK:
		case fire.pb.ranklist.RankType.PROFESSION_HUNTER_RANK:
		case fire.pb.ranklist.RankType.PROFESSION_DRUID_RANK:
		case fire.pb.ranklist.RankType.PROFESSION_ROGUE_RANK:
		case fire.pb.ranklist.RankType.PROFESSION_SAMAN_RANK:
		case fire.pb.ranklist.RankType.PROFESSION_WARLOCK_RANK:
		case fire.pb.ranklist.RankType.ROLE_RANK:
		case fire.pb.ranklist.RankType.PVP5_LAST_GRADE1:
		case fire.pb.ranklist.RankType.PVP5_LAST_GRADE2:
		case fire.pb.ranklist.RankType.PVP5_LAST_GRADE3:
		case fire.pb.ranklist.RankType.PVP5_HISTORY_GRADE1:
		case fire.pb.ranklist.RankType.PVP5_HISTORY_GRADE2:
		case fire.pb.ranklist.RankType.PVP5_HISTORY_GRADE3:
			new PRankGetRoleInfo(rank,roleId,ranktype, id).submit();
			break;
		case fire.pb.ranklist.RankType.PET_GRADE_RANK:
			new PGetPetData(rank,roleId,0, id).submit();
			break;
		case fire.pb.ranklist.RankType.LEVEL_RANK:
		case fire.pb.ranklist.RankType.RED_PACK_1:
		case fire.pb.ranklist.RankType.RED_PACK_2:
		case fire.pb.ranklist.RankType.FLOWER_RECEIVE:
		case fire.pb.ranklist.RankType.FLOWER_GIVE:
			new PRoleInfoGet(rank,roleId, id, ranktype).submit();
			break;
			
		case fire.pb.ranklist.RankType.FACTION_RANK_LEVEL:
			
			new fire.pb.ranklist.getrankinfo.PFactionRankInfo(rank, fire.pb.ranklist.RankType.FACTION_RANK_LEVEL, roleId, id).submit();
			
			break;
			
		case fire.pb.ranklist.RankType.FACTION_ZONGHE:
		
			new fire.pb.ranklist.getrankinfo.PFactionRankInfo(rank, fire.pb.ranklist.RankType.FACTION_ZONGHE, roleId, id).submit();
			
			break;
			
		case fire.pb.ranklist.RankType.FACTION_COPY:
			
			new fire.pb.ranklist.getrankinfo.PFactionRankInfo(rank, fire.pb.ranklist.RankType.FACTION_COPY, roleId, id).submit();
			
			break;
		}
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810256;

	public int getType() {
		return 810256;
	}

	public int ranktype; // 排行榜类型
	public int rank; // 排名
	public long id;

	public CRankGetInfo() {
	}

	public CRankGetInfo(int _ranktype_, int _rank_, long _id_) {
		this.ranktype = _ranktype_;
		this.rank = _rank_;
		this.id = _id_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ranktype);
		_os_.marshal(rank);
		_os_.marshal(id);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ranktype = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		id = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRankGetInfo) {
			CRankGetInfo _o_ = (CRankGetInfo)_o1_;
			if (ranktype != _o_.ranktype) return false;
			if (rank != _o_.rank) return false;
			if (id != _o_.id) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += ranktype;
		_h_ += rank;
		_h_ += (int)id;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ranktype).append(",");
		_sb_.append(rank).append(",");
		_sb_.append(id).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRankGetInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = ranktype - _o_.ranktype;
		if (0 != _c_) return _c_;
		_c_ = rank - _o_.rank;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(id - _o_.id);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


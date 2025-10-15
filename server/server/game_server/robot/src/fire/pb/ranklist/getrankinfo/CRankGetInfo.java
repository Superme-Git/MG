
package fire.pb.ranklist.getrankinfo;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRankGetInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRankGetInfo extends __CRankGetInfo__ {
	@Override
	protected void process() {
		// protocol handle
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


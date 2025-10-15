
package fire.pb.ranklist.getrankinfo;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRankRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRankRoleInfo extends __SRankRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810257;

	public int getType() {
		return 810257;
	}

	public long roleid; // 角色Id
	public java.lang.String rolename; // 角色名
	public int shape; // 角色造型
	public int level; // 角色等级
	public int zonghescore; // 综合战力
	public int petscore; // 宠物评分
	public int camp; // 阵营
	public int school; // 职业
	public java.lang.String factionname; // 公会名称
	public int rank; // 排名

	public SRankRoleInfo() {
		rolename = "";
		factionname = "";
	}

	public SRankRoleInfo(long _roleid_, java.lang.String _rolename_, int _shape_, int _level_, int _zonghescore_, int _petscore_, int _camp_, int _school_, java.lang.String _factionname_, int _rank_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.shape = _shape_;
		this.level = _level_;
		this.zonghescore = _zonghescore_;
		this.petscore = _petscore_;
		this.camp = _camp_;
		this.school = _school_;
		this.factionname = _factionname_;
		this.rank = _rank_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(shape);
		_os_.marshal(level);
		_os_.marshal(zonghescore);
		_os_.marshal(petscore);
		_os_.marshal(camp);
		_os_.marshal(school);
		_os_.marshal(factionname, "UTF-16LE");
		_os_.marshal(rank);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		zonghescore = _os_.unmarshal_int();
		petscore = _os_.unmarshal_int();
		camp = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		factionname = _os_.unmarshal_String("UTF-16LE");
		rank = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRankRoleInfo) {
			SRankRoleInfo _o_ = (SRankRoleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (shape != _o_.shape) return false;
			if (level != _o_.level) return false;
			if (zonghescore != _o_.zonghescore) return false;
			if (petscore != _o_.petscore) return false;
			if (camp != _o_.camp) return false;
			if (school != _o_.school) return false;
			if (!factionname.equals(_o_.factionname)) return false;
			if (rank != _o_.rank) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += shape;
		_h_ += level;
		_h_ += zonghescore;
		_h_ += petscore;
		_h_ += camp;
		_h_ += school;
		_h_ += factionname.hashCode();
		_h_ += rank;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(level).append(",");
		_sb_.append(zonghescore).append(",");
		_sb_.append(petscore).append(",");
		_sb_.append(camp).append(",");
		_sb_.append(school).append(",");
		_sb_.append("T").append(factionname.length()).append(",");
		_sb_.append(rank).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


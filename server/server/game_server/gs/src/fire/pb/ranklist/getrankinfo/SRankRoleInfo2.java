
package fire.pb.ranklist.getrankinfo;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRankRoleInfo2__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRankRoleInfo2 extends __SRankRoleInfo2__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810261;

	public int getType() {
		return 810261;
	}

	public long roleid;
	public int ranktype; // 请求的类型 by changhao
	public java.lang.String rolename;
	public int shape;
	public int school;
	public int level;
	public fire.pb.Bag baginfo;
	public java.util.HashMap<Integer,com.locojoy.base.Octets> tips;
	public int footlogoid;
	public int rank;
	public int totalscore; // 总评分 by changhao
	public int rolescore; // 只是人物评分 by changhao
	public int petscore; // 宠物评分 by changhao
	public int manypetscore; // 多宠物评分 by changhao
	public int skillscore; // 技能评分 by changhao
	public int levelscore; // 等级评分 by changhao
	public int xiulianscore; // 修炼评分 by changha
	public int equipscore; // 装备评分
	public java.util.HashMap<Byte,Integer> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装
	public java.lang.String factionname; // 公会名称

	public SRankRoleInfo2() {
		rolename = "";
		baginfo = new fire.pb.Bag();
		tips = new java.util.HashMap<Integer,com.locojoy.base.Octets>();
		components = new java.util.HashMap<Byte,Integer>();
		factionname = "";
	}

	public SRankRoleInfo2(long _roleid_, int _ranktype_, java.lang.String _rolename_, int _shape_, int _school_, int _level_, fire.pb.Bag _baginfo_, java.util.HashMap<Integer,com.locojoy.base.Octets> _tips_, int _footlogoid_, int _rank_, int _totalscore_, int _rolescore_, int _petscore_, int _manypetscore_, int _skillscore_, int _levelscore_, int _xiulianscore_, int _equipscore_, java.util.HashMap<Byte,Integer> _components_, java.lang.String _factionname_) {
		this.roleid = _roleid_;
		this.ranktype = _ranktype_;
		this.rolename = _rolename_;
		this.shape = _shape_;
		this.school = _school_;
		this.level = _level_;
		this.baginfo = _baginfo_;
		this.tips = _tips_;
		this.footlogoid = _footlogoid_;
		this.rank = _rank_;
		this.totalscore = _totalscore_;
		this.rolescore = _rolescore_;
		this.petscore = _petscore_;
		this.manypetscore = _manypetscore_;
		this.skillscore = _skillscore_;
		this.levelscore = _levelscore_;
		this.xiulianscore = _xiulianscore_;
		this.equipscore = _equipscore_;
		this.components = _components_;
		this.factionname = _factionname_;
	}

	public final boolean _validator_() {
		if (roleid < 1) return false;
		if (!baginfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(ranktype);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(shape);
		_os_.marshal(school);
		_os_.marshal(level);
		_os_.marshal(baginfo);
		_os_.compact_uint32(tips.size());
		for (java.util.Map.Entry<Integer, com.locojoy.base.Octets> _e_ : tips.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(footlogoid);
		_os_.marshal(rank);
		_os_.marshal(totalscore);
		_os_.marshal(rolescore);
		_os_.marshal(petscore);
		_os_.marshal(manypetscore);
		_os_.marshal(skillscore);
		_os_.marshal(levelscore);
		_os_.marshal(xiulianscore);
		_os_.marshal(equipscore);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(factionname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		ranktype = _os_.unmarshal_int();
		rolename = _os_.unmarshal_String("UTF-16LE");
		shape = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		baginfo.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			tips.put(_k_, _v_);
		}
		footlogoid = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		totalscore = _os_.unmarshal_int();
		rolescore = _os_.unmarshal_int();
		petscore = _os_.unmarshal_int();
		manypetscore = _os_.unmarshal_int();
		skillscore = _os_.unmarshal_int();
		levelscore = _os_.unmarshal_int();
		xiulianscore = _os_.unmarshal_int();
		equipscore = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		factionname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRankRoleInfo2) {
			SRankRoleInfo2 _o_ = (SRankRoleInfo2)_o1_;
			if (roleid != _o_.roleid) return false;
			if (ranktype != _o_.ranktype) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (shape != _o_.shape) return false;
			if (school != _o_.school) return false;
			if (level != _o_.level) return false;
			if (!baginfo.equals(_o_.baginfo)) return false;
			if (!tips.equals(_o_.tips)) return false;
			if (footlogoid != _o_.footlogoid) return false;
			if (rank != _o_.rank) return false;
			if (totalscore != _o_.totalscore) return false;
			if (rolescore != _o_.rolescore) return false;
			if (petscore != _o_.petscore) return false;
			if (manypetscore != _o_.manypetscore) return false;
			if (skillscore != _o_.skillscore) return false;
			if (levelscore != _o_.levelscore) return false;
			if (xiulianscore != _o_.xiulianscore) return false;
			if (equipscore != _o_.equipscore) return false;
			if (!components.equals(_o_.components)) return false;
			if (!factionname.equals(_o_.factionname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += ranktype;
		_h_ += rolename.hashCode();
		_h_ += shape;
		_h_ += school;
		_h_ += level;
		_h_ += baginfo.hashCode();
		_h_ += tips.hashCode();
		_h_ += footlogoid;
		_h_ += rank;
		_h_ += totalscore;
		_h_ += rolescore;
		_h_ += petscore;
		_h_ += manypetscore;
		_h_ += skillscore;
		_h_ += levelscore;
		_h_ += xiulianscore;
		_h_ += equipscore;
		_h_ += components.hashCode();
		_h_ += factionname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(ranktype).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(school).append(",");
		_sb_.append(level).append(",");
		_sb_.append(baginfo).append(",");
		_sb_.append(tips).append(",");
		_sb_.append(footlogoid).append(",");
		_sb_.append(rank).append(",");
		_sb_.append(totalscore).append(",");
		_sb_.append(rolescore).append(",");
		_sb_.append(petscore).append(",");
		_sb_.append(manypetscore).append(",");
		_sb_.append(skillscore).append(",");
		_sb_.append(levelscore).append(",");
		_sb_.append(xiulianscore).append(",");
		_sb_.append(equipscore).append(",");
		_sb_.append(components).append(",");
		_sb_.append("T").append(factionname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


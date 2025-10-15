
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

/** mydata
*/
public class RoleDetail implements Marshal {
	public long roleid; // ID
	public java.lang.String rolename; // 名称
	public int level; // 等级
	public int school; // 职业
	public int shape; // 角色:玩家所属的系统角色的id
	public int title; // 当前称谓
	public long lastlogin; // 上次登录时间
	public int hp; // 气血
	public int uplimithp; // 当前气血上限
	public int maxhp; // 最大气血
	public int mp; // 法力
	public int magicattack; // 法术攻击
	public int maxmp; // 最大法力
	public int magicdef; // 法术防御
	public int sp; // 怒气
	public int seal; // 加强封印
	public int maxsp; // 最大怒气
	public int hit; // 命中
	public int damage; // 伤害
	public int heal_critc_level; // 疗暴等级
	public int defend; // 防御
	public int phy_critc_level; // 外功暴击等级
	public int speed; // 速度
	public int magic_critc_level; // 内功暴击等级
	public int dodge; // 躲避
	public int anti_phy_critc_level; // 外功抗性等级
	public int medical; // 治疗强度
	public int unseal; // 封印抗性
	public int anti_critc_level; // 暴击抗性等级
	public float phy_critc_pct; // 外功暴击程度
	public float magic_critc_pct; // 内功暴击程度
	public float heal_critc_pct; // 疗暴程度
	public int anti_magic_critc_level; // 内功抗性等级
	public int energy; // 活力
	public int enlimit; // 活力上限
	public fire.pb.RoleBasicFightProperties bfp;
	public java.util.HashMap<Integer,Integer> point; // 潜能
	public int pointscheme; // 当前加点方案
	public int schemechanges; // 方案切换次数
	public int schoolvalue; // 职业贡献值
	public int reputation; // 声望值
	public long exp; // 经验
	public long nexp; // 升级 需要经验
	public int showpet; // 当前展示的宠物
	public int petmaxnum; // 可携带的最大宠物数量
	public java.util.ArrayList<fire.pb.Pet> pets;
	public java.util.HashMap<Integer,Integer> sysconfigmap;
	public java.util.HashMap<Integer,Integer> lineconfigmap;
	public java.util.HashMap<Integer,fire.pb.title.TitleInfo> titles; // 称谓信息
	public java.util.HashMap<Integer,fire.pb.FormBean> learnedformsmap;
	public java.util.HashMap<Byte,Integer> components; // 角色装备信息 0脱下
	public int activeness; // 活跃度值
	public int factionvalue; // 公会贡献度
	public long masterid;
	public byte isprotected; // 是否处于财产保护状态，1 = 是 ； 0 = 否
	public byte wrongpwdtimes; // 24小时内输错密码的次数
	public int petindex; // 当前战斗宠物
	public int kongzhijiacheng; // 控制加成
	public int kongzhimianyi; // 控制免疫
	public int zhiliaojiashen; // 治疗加深
	public int wulidikang; // 物理抵抗
	public int fashudikang; // 法术抵抗
	public int fashuchuantou; // 法术穿透
	public int wulichuantou; // 物理穿透
	public java.util.HashMap<Integer,fire.pb.Bag> baginfo; // key是bagid,value是背包的详细信息
	public long rolecreatetime; // 角色创建时间
	public java.util.HashMap<Integer,java.lang.String> depotnameinfo; // key是depotid,value是depot名(如果是默认的就不存了)

	public RoleDetail() {
		rolename = "";
		bfp = new fire.pb.RoleBasicFightProperties();
		point = new java.util.HashMap<Integer,Integer>();
		pets = new java.util.ArrayList<fire.pb.Pet>();
		sysconfigmap = new java.util.HashMap<Integer,Integer>();
		lineconfigmap = new java.util.HashMap<Integer,Integer>();
		titles = new java.util.HashMap<Integer,fire.pb.title.TitleInfo>();
		learnedformsmap = new java.util.HashMap<Integer,fire.pb.FormBean>();
		components = new java.util.HashMap<Byte,Integer>();
		baginfo = new java.util.HashMap<Integer,fire.pb.Bag>();
		depotnameinfo = new java.util.HashMap<Integer,java.lang.String>();
	}

	public RoleDetail(long _roleid_, java.lang.String _rolename_, int _level_, int _school_, int _shape_, int _title_, long _lastlogin_, int _hp_, int _uplimithp_, int _maxhp_, int _mp_, int _magicattack_, int _maxmp_, int _magicdef_, int _sp_, int _seal_, int _maxsp_, int _hit_, int _damage_, int _heal_critc_level_, int _defend_, int _phy_critc_level_, int _speed_, int _magic_critc_level_, int _dodge_, int _anti_phy_critc_level_, int _medical_, int _unseal_, int _anti_critc_level_, float _phy_critc_pct_, float _magic_critc_pct_, float _heal_critc_pct_, int _anti_magic_critc_level_, int _energy_, int _enlimit_, fire.pb.RoleBasicFightProperties _bfp_, java.util.HashMap<Integer,Integer> _point_, int _pointscheme_, int _schemechanges_, int _schoolvalue_, int _reputation_, long _exp_, long _nexp_, int _showpet_, int _petmaxnum_, java.util.ArrayList<fire.pb.Pet> _pets_, java.util.HashMap<Integer,Integer> _sysconfigmap_, java.util.HashMap<Integer,Integer> _lineconfigmap_, java.util.HashMap<Integer,fire.pb.title.TitleInfo> _titles_, java.util.HashMap<Integer,fire.pb.FormBean> _learnedformsmap_, java.util.HashMap<Byte,Integer> _components_, int _activeness_, int _factionvalue_, long _masterid_, byte _isprotected_, byte _wrongpwdtimes_, int _petindex_, int _kongzhijiacheng_, int _kongzhimianyi_, int _zhiliaojiashen_, int _wulidikang_, int _fashudikang_, int _fashuchuantou_, int _wulichuantou_, java.util.HashMap<Integer,fire.pb.Bag> _baginfo_, long _rolecreatetime_, java.util.HashMap<Integer,java.lang.String> _depotnameinfo_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.level = _level_;
		this.school = _school_;
		this.shape = _shape_;
		this.title = _title_;
		this.lastlogin = _lastlogin_;
		this.hp = _hp_;
		this.uplimithp = _uplimithp_;
		this.maxhp = _maxhp_;
		this.mp = _mp_;
		this.magicattack = _magicattack_;
		this.maxmp = _maxmp_;
		this.magicdef = _magicdef_;
		this.sp = _sp_;
		this.seal = _seal_;
		this.maxsp = _maxsp_;
		this.hit = _hit_;
		this.damage = _damage_;
		this.heal_critc_level = _heal_critc_level_;
		this.defend = _defend_;
		this.phy_critc_level = _phy_critc_level_;
		this.speed = _speed_;
		this.magic_critc_level = _magic_critc_level_;
		this.dodge = _dodge_;
		this.anti_phy_critc_level = _anti_phy_critc_level_;
		this.medical = _medical_;
		this.unseal = _unseal_;
		this.anti_critc_level = _anti_critc_level_;
		this.phy_critc_pct = _phy_critc_pct_;
		this.magic_critc_pct = _magic_critc_pct_;
		this.heal_critc_pct = _heal_critc_pct_;
		this.anti_magic_critc_level = _anti_magic_critc_level_;
		this.energy = _energy_;
		this.enlimit = _enlimit_;
		this.bfp = _bfp_;
		this.point = _point_;
		this.pointscheme = _pointscheme_;
		this.schemechanges = _schemechanges_;
		this.schoolvalue = _schoolvalue_;
		this.reputation = _reputation_;
		this.exp = _exp_;
		this.nexp = _nexp_;
		this.showpet = _showpet_;
		this.petmaxnum = _petmaxnum_;
		this.pets = _pets_;
		this.sysconfigmap = _sysconfigmap_;
		this.lineconfigmap = _lineconfigmap_;
		this.titles = _titles_;
		this.learnedformsmap = _learnedformsmap_;
		this.components = _components_;
		this.activeness = _activeness_;
		this.factionvalue = _factionvalue_;
		this.masterid = _masterid_;
		this.isprotected = _isprotected_;
		this.wrongpwdtimes = _wrongpwdtimes_;
		this.petindex = _petindex_;
		this.kongzhijiacheng = _kongzhijiacheng_;
		this.kongzhimianyi = _kongzhimianyi_;
		this.zhiliaojiashen = _zhiliaojiashen_;
		this.wulidikang = _wulidikang_;
		this.fashudikang = _fashudikang_;
		this.fashuchuantou = _fashuchuantou_;
		this.wulichuantou = _wulichuantou_;
		this.baginfo = _baginfo_;
		this.rolecreatetime = _rolecreatetime_;
		this.depotnameinfo = _depotnameinfo_;
	}

	public final boolean _validator_() {
		if (roleid < 1) return false;
		if (level < 1) return false;
		if (title < -1) return false;
		if (!bfp._validator_()) return false;
		for (fire.pb.Pet _v_ : pets)
			if (!_v_._validator_()) return false;
		for (java.util.Map.Entry<Integer, fire.pb.title.TitleInfo> _e_ : titles.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		for (java.util.Map.Entry<Integer, fire.pb.FormBean> _e_ : learnedformsmap.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		for (java.util.Map.Entry<Integer, fire.pb.Bag> _e_ : baginfo.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(shape);
		_os_.marshal(title);
		_os_.marshal(lastlogin);
		_os_.marshal(hp);
		_os_.marshal(uplimithp);
		_os_.marshal(maxhp);
		_os_.marshal(mp);
		_os_.marshal(magicattack);
		_os_.marshal(maxmp);
		_os_.marshal(magicdef);
		_os_.marshal(sp);
		_os_.marshal(seal);
		_os_.marshal(maxsp);
		_os_.marshal(hit);
		_os_.marshal(damage);
		_os_.marshal(heal_critc_level);
		_os_.marshal(defend);
		_os_.marshal(phy_critc_level);
		_os_.marshal(speed);
		_os_.marshal(magic_critc_level);
		_os_.marshal(dodge);
		_os_.marshal(anti_phy_critc_level);
		_os_.marshal(medical);
		_os_.marshal(unseal);
		_os_.marshal(anti_critc_level);
		_os_.marshal(phy_critc_pct);
		_os_.marshal(magic_critc_pct);
		_os_.marshal(heal_critc_pct);
		_os_.marshal(anti_magic_critc_level);
		_os_.marshal(energy);
		_os_.marshal(enlimit);
		_os_.marshal(bfp);
		_os_.compact_uint32(point.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : point.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(pointscheme);
		_os_.marshal(schemechanges);
		_os_.marshal(schoolvalue);
		_os_.marshal(reputation);
		_os_.marshal(exp);
		_os_.marshal(nexp);
		_os_.marshal(showpet);
		_os_.marshal(petmaxnum);
		_os_.compact_uint32(pets.size());
		for (fire.pb.Pet _v_ : pets) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(sysconfigmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : sysconfigmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(lineconfigmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : lineconfigmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(titles.size());
		for (java.util.Map.Entry<Integer, fire.pb.title.TitleInfo> _e_ : titles.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(learnedformsmap.size());
		for (java.util.Map.Entry<Integer, fire.pb.FormBean> _e_ : learnedformsmap.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(activeness);
		_os_.marshal(factionvalue);
		_os_.marshal(masterid);
		_os_.marshal(isprotected);
		_os_.marshal(wrongpwdtimes);
		_os_.marshal(petindex);
		_os_.marshal(kongzhijiacheng);
		_os_.marshal(kongzhimianyi);
		_os_.marshal(zhiliaojiashen);
		_os_.marshal(wulidikang);
		_os_.marshal(fashudikang);
		_os_.marshal(fashuchuantou);
		_os_.marshal(wulichuantou);
		_os_.compact_uint32(baginfo.size());
		for (java.util.Map.Entry<Integer, fire.pb.Bag> _e_ : baginfo.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(rolecreatetime);
		_os_.compact_uint32(depotnameinfo.size());
		for (java.util.Map.Entry<Integer, java.lang.String> _e_ : depotnameinfo.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue(), "UTF-16LE");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		title = _os_.unmarshal_int();
		lastlogin = _os_.unmarshal_long();
		hp = _os_.unmarshal_int();
		uplimithp = _os_.unmarshal_int();
		maxhp = _os_.unmarshal_int();
		mp = _os_.unmarshal_int();
		magicattack = _os_.unmarshal_int();
		maxmp = _os_.unmarshal_int();
		magicdef = _os_.unmarshal_int();
		sp = _os_.unmarshal_int();
		seal = _os_.unmarshal_int();
		maxsp = _os_.unmarshal_int();
		hit = _os_.unmarshal_int();
		damage = _os_.unmarshal_int();
		heal_critc_level = _os_.unmarshal_int();
		defend = _os_.unmarshal_int();
		phy_critc_level = _os_.unmarshal_int();
		speed = _os_.unmarshal_int();
		magic_critc_level = _os_.unmarshal_int();
		dodge = _os_.unmarshal_int();
		anti_phy_critc_level = _os_.unmarshal_int();
		medical = _os_.unmarshal_int();
		unseal = _os_.unmarshal_int();
		anti_critc_level = _os_.unmarshal_int();
		phy_critc_pct = _os_.unmarshal_float();
		magic_critc_pct = _os_.unmarshal_float();
		heal_critc_pct = _os_.unmarshal_float();
		anti_magic_critc_level = _os_.unmarshal_int();
		energy = _os_.unmarshal_int();
		enlimit = _os_.unmarshal_int();
		bfp.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			point.put(_k_, _v_);
		}
		pointscheme = _os_.unmarshal_int();
		schemechanges = _os_.unmarshal_int();
		schoolvalue = _os_.unmarshal_int();
		reputation = _os_.unmarshal_int();
		exp = _os_.unmarshal_long();
		nexp = _os_.unmarshal_long();
		showpet = _os_.unmarshal_int();
		petmaxnum = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.Pet _v_ = new fire.pb.Pet();
			_v_.unmarshal(_os_);
			pets.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			sysconfigmap.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			lineconfigmap.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.title.TitleInfo _v_ = new fire.pb.title.TitleInfo();
			_v_.unmarshal(_os_);
			titles.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.FormBean _v_ = new fire.pb.FormBean();
			_v_.unmarshal(_os_);
			learnedformsmap.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		activeness = _os_.unmarshal_int();
		factionvalue = _os_.unmarshal_int();
		masterid = _os_.unmarshal_long();
		isprotected = _os_.unmarshal_byte();
		wrongpwdtimes = _os_.unmarshal_byte();
		petindex = _os_.unmarshal_int();
		kongzhijiacheng = _os_.unmarshal_int();
		kongzhimianyi = _os_.unmarshal_int();
		zhiliaojiashen = _os_.unmarshal_int();
		wulidikang = _os_.unmarshal_int();
		fashudikang = _os_.unmarshal_int();
		fashuchuantou = _os_.unmarshal_int();
		wulichuantou = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.Bag _v_ = new fire.pb.Bag();
			_v_.unmarshal(_os_);
			baginfo.put(_k_, _v_);
		}
		rolecreatetime = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			java.lang.String _v_;
			_v_ = _os_.unmarshal_String("UTF-16LE");
			depotnameinfo.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleDetail) {
			RoleDetail _o_ = (RoleDetail)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (shape != _o_.shape) return false;
			if (title != _o_.title) return false;
			if (lastlogin != _o_.lastlogin) return false;
			if (hp != _o_.hp) return false;
			if (uplimithp != _o_.uplimithp) return false;
			if (maxhp != _o_.maxhp) return false;
			if (mp != _o_.mp) return false;
			if (magicattack != _o_.magicattack) return false;
			if (maxmp != _o_.maxmp) return false;
			if (magicdef != _o_.magicdef) return false;
			if (sp != _o_.sp) return false;
			if (seal != _o_.seal) return false;
			if (maxsp != _o_.maxsp) return false;
			if (hit != _o_.hit) return false;
			if (damage != _o_.damage) return false;
			if (heal_critc_level != _o_.heal_critc_level) return false;
			if (defend != _o_.defend) return false;
			if (phy_critc_level != _o_.phy_critc_level) return false;
			if (speed != _o_.speed) return false;
			if (magic_critc_level != _o_.magic_critc_level) return false;
			if (dodge != _o_.dodge) return false;
			if (anti_phy_critc_level != _o_.anti_phy_critc_level) return false;
			if (medical != _o_.medical) return false;
			if (unseal != _o_.unseal) return false;
			if (anti_critc_level != _o_.anti_critc_level) return false;
			if (phy_critc_pct != _o_.phy_critc_pct) return false;
			if (magic_critc_pct != _o_.magic_critc_pct) return false;
			if (heal_critc_pct != _o_.heal_critc_pct) return false;
			if (anti_magic_critc_level != _o_.anti_magic_critc_level) return false;
			if (energy != _o_.energy) return false;
			if (enlimit != _o_.enlimit) return false;
			if (!bfp.equals(_o_.bfp)) return false;
			if (!point.equals(_o_.point)) return false;
			if (pointscheme != _o_.pointscheme) return false;
			if (schemechanges != _o_.schemechanges) return false;
			if (schoolvalue != _o_.schoolvalue) return false;
			if (reputation != _o_.reputation) return false;
			if (exp != _o_.exp) return false;
			if (nexp != _o_.nexp) return false;
			if (showpet != _o_.showpet) return false;
			if (petmaxnum != _o_.petmaxnum) return false;
			if (!pets.equals(_o_.pets)) return false;
			if (!sysconfigmap.equals(_o_.sysconfigmap)) return false;
			if (!lineconfigmap.equals(_o_.lineconfigmap)) return false;
			if (!titles.equals(_o_.titles)) return false;
			if (!learnedformsmap.equals(_o_.learnedformsmap)) return false;
			if (!components.equals(_o_.components)) return false;
			if (activeness != _o_.activeness) return false;
			if (factionvalue != _o_.factionvalue) return false;
			if (masterid != _o_.masterid) return false;
			if (isprotected != _o_.isprotected) return false;
			if (wrongpwdtimes != _o_.wrongpwdtimes) return false;
			if (petindex != _o_.petindex) return false;
			if (kongzhijiacheng != _o_.kongzhijiacheng) return false;
			if (kongzhimianyi != _o_.kongzhimianyi) return false;
			if (zhiliaojiashen != _o_.zhiliaojiashen) return false;
			if (wulidikang != _o_.wulidikang) return false;
			if (fashudikang != _o_.fashudikang) return false;
			if (fashuchuantou != _o_.fashuchuantou) return false;
			if (wulichuantou != _o_.wulichuantou) return false;
			if (!baginfo.equals(_o_.baginfo)) return false;
			if (rolecreatetime != _o_.rolecreatetime) return false;
			if (!depotnameinfo.equals(_o_.depotnameinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += level;
		_h_ += school;
		_h_ += shape;
		_h_ += title;
		_h_ += (int)lastlogin;
		_h_ += hp;
		_h_ += uplimithp;
		_h_ += maxhp;
		_h_ += mp;
		_h_ += magicattack;
		_h_ += maxmp;
		_h_ += magicdef;
		_h_ += sp;
		_h_ += seal;
		_h_ += maxsp;
		_h_ += hit;
		_h_ += damage;
		_h_ += heal_critc_level;
		_h_ += defend;
		_h_ += phy_critc_level;
		_h_ += speed;
		_h_ += magic_critc_level;
		_h_ += dodge;
		_h_ += anti_phy_critc_level;
		_h_ += medical;
		_h_ += unseal;
		_h_ += anti_critc_level;
		_h_ += Float.floatToIntBits(phy_critc_pct);
		_h_ += Float.floatToIntBits(magic_critc_pct);
		_h_ += Float.floatToIntBits(heal_critc_pct);
		_h_ += anti_magic_critc_level;
		_h_ += energy;
		_h_ += enlimit;
		_h_ += bfp.hashCode();
		_h_ += point.hashCode();
		_h_ += pointscheme;
		_h_ += schemechanges;
		_h_ += schoolvalue;
		_h_ += reputation;
		_h_ += (int)exp;
		_h_ += (int)nexp;
		_h_ += showpet;
		_h_ += petmaxnum;
		_h_ += pets.hashCode();
		_h_ += sysconfigmap.hashCode();
		_h_ += lineconfigmap.hashCode();
		_h_ += titles.hashCode();
		_h_ += learnedformsmap.hashCode();
		_h_ += components.hashCode();
		_h_ += activeness;
		_h_ += factionvalue;
		_h_ += (int)masterid;
		_h_ += (int)isprotected;
		_h_ += (int)wrongpwdtimes;
		_h_ += petindex;
		_h_ += kongzhijiacheng;
		_h_ += kongzhimianyi;
		_h_ += zhiliaojiashen;
		_h_ += wulidikang;
		_h_ += fashudikang;
		_h_ += fashuchuantou;
		_h_ += wulichuantou;
		_h_ += baginfo.hashCode();
		_h_ += (int)rolecreatetime;
		_h_ += depotnameinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(school).append(",");
		_sb_.append(shape).append(",");
		_sb_.append(title).append(",");
		_sb_.append(lastlogin).append(",");
		_sb_.append(hp).append(",");
		_sb_.append(uplimithp).append(",");
		_sb_.append(maxhp).append(",");
		_sb_.append(mp).append(",");
		_sb_.append(magicattack).append(",");
		_sb_.append(maxmp).append(",");
		_sb_.append(magicdef).append(",");
		_sb_.append(sp).append(",");
		_sb_.append(seal).append(",");
		_sb_.append(maxsp).append(",");
		_sb_.append(hit).append(",");
		_sb_.append(damage).append(",");
		_sb_.append(heal_critc_level).append(",");
		_sb_.append(defend).append(",");
		_sb_.append(phy_critc_level).append(",");
		_sb_.append(speed).append(",");
		_sb_.append(magic_critc_level).append(",");
		_sb_.append(dodge).append(",");
		_sb_.append(anti_phy_critc_level).append(",");
		_sb_.append(medical).append(",");
		_sb_.append(unseal).append(",");
		_sb_.append(anti_critc_level).append(",");
		_sb_.append(phy_critc_pct).append(",");
		_sb_.append(magic_critc_pct).append(",");
		_sb_.append(heal_critc_pct).append(",");
		_sb_.append(anti_magic_critc_level).append(",");
		_sb_.append(energy).append(",");
		_sb_.append(enlimit).append(",");
		_sb_.append(bfp).append(",");
		_sb_.append(point).append(",");
		_sb_.append(pointscheme).append(",");
		_sb_.append(schemechanges).append(",");
		_sb_.append(schoolvalue).append(",");
		_sb_.append(reputation).append(",");
		_sb_.append(exp).append(",");
		_sb_.append(nexp).append(",");
		_sb_.append(showpet).append(",");
		_sb_.append(petmaxnum).append(",");
		_sb_.append(pets).append(",");
		_sb_.append(sysconfigmap).append(",");
		_sb_.append(lineconfigmap).append(",");
		_sb_.append(titles).append(",");
		_sb_.append(learnedformsmap).append(",");
		_sb_.append(components).append(",");
		_sb_.append(activeness).append(",");
		_sb_.append(factionvalue).append(",");
		_sb_.append(masterid).append(",");
		_sb_.append(isprotected).append(",");
		_sb_.append(wrongpwdtimes).append(",");
		_sb_.append(petindex).append(",");
		_sb_.append(kongzhijiacheng).append(",");
		_sb_.append(kongzhimianyi).append(",");
		_sb_.append(zhiliaojiashen).append(",");
		_sb_.append(wulidikang).append(",");
		_sb_.append(fashudikang).append(",");
		_sb_.append(fashuchuantou).append(",");
		_sb_.append(wulichuantou).append(",");
		_sb_.append(baginfo).append(",");
		_sb_.append(rolecreatetime).append(",");
		_sb_.append(depotnameinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}


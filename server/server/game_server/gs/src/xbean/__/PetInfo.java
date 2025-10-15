
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PetInfo extends mkdb.XBean implements xbean.PetInfo {
	private int id; // 宠物ID
	private int key; // 在宠物栏、仓库、商会柜台里的key
	private long uniqid; // 宠物唯一ID
	private String name; // 名称
	private int level; // 等级    	*short够了
	private xbean.BasicFightProperties initbfp; // 初始的基础战斗属性
	private xbean.BasicFightProperties bfp; // 基础战斗属性
	private int hp; // 当前气血
	private int mp; // 当前法力
	private int growrate; // 成长率 		*short够了
	private int life; // 寿命
	private long exp; // 当前经验
	private int bornattackapt; // 出生时攻击资质	*short够了
	private int borndefendapt; // 出生时防御资质	*short够了
	private int bornphyforceapt; // 出生时体力资质	*short够了
	private int bornmagicapt; // 出生时法力资质	*short够了
	private int bornspeedapt; // 出生时速度资质	*short够了
	private int borndodgeapt; // 出生时躲闪资质	*short够了
	private int kind; // 宠物类型 野生、宝宝	*byte够了
	private java.util.HashMap<Integer, Float> effects; // key = effect type id
	private java.util.HashMap<Integer, Float> finalattrs; // key = attr type
	private xbean.BuffAgent buffpet; // buff
	private boolean islocked; // 是否锁定
	private boolean isbinded; // 是否绑定
	private long timeout; // 时间锁的到期时间，为0则只是普通锁
	private int petscore; // 宠物评分
	private int petbasescore; // 宠物一级属性评分
	private long ownerid; // 宠物的主人
	private java.util.LinkedList<xbean.PetSkill> skills; // 宠物拥有的战斗技能
	private int point; // 潜能点
	private int autoaddcons; // 加点方案-升级自动加体质
	private int autoaddiq; // 加点方案-升级自动加智力
	private int autoaddstr; // 加点方案-升级自动加力量
	private int autoaddendu; // 加点方案-升级自动加耐力
	private int autoaddagi; // 加点方案-升级自动加敏捷
	private int pointresetcount; // 宠物加点的重置次数 *short够了
	private int aptaddcount; // 资质培养次数 *byte够了
	private int growrateaddcount; // 成长率培养次数 *byte够了
	private int washcount; // 洗练次数 *short够了
	private int treasure; // 0表示不是珍品;1表示是珍品
	private int petdye1; // 宠物染色信息部位1
	private int petdye2; // 宠物染色信息部位2
	private int shenshouinccount; // 神兽养成次数
	private long marketfreezeexpire; // 摆摊冻结截止时间,默认0不冻结

	@Override
	public void _reset_unsafe_() {
		id = 0;
		key = 0;
		uniqid = 0L;
		name = "";
		level = 0;
		initbfp._reset_unsafe_();
		bfp._reset_unsafe_();
		hp = 0;
		mp = 0;
		growrate = 0;
		life = 0;
		exp = 0L;
		bornattackapt = 0;
		borndefendapt = 0;
		bornphyforceapt = 0;
		bornmagicapt = 0;
		bornspeedapt = 0;
		borndodgeapt = 0;
		kind = 0;
		effects.clear();
		finalattrs.clear();
		buffpet._reset_unsafe_();
		islocked = false;
		isbinded = false;
		timeout = 0;
		petscore = 0;
		petbasescore = 0;
		ownerid = 0L;
		skills.clear();
		point = 0;
		autoaddcons = 0;
		autoaddiq = 0;
		autoaddstr = 0;
		autoaddendu = 0;
		autoaddagi = 0;
		pointresetcount = 0;
		aptaddcount = 0;
		growrateaddcount = 0;
		washcount = 0;
		treasure = 0;
		petdye1 = 0;
		petdye2 = 0;
		shenshouinccount = 0;
		marketfreezeexpire = 0;
	}

	PetInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		name = "";
		initbfp = new BasicFightProperties(0, this, "initbfp");
		bfp = new BasicFightProperties(0, this, "bfp");
		effects = new java.util.HashMap<Integer, Float>();
		finalattrs = new java.util.HashMap<Integer, Float>();
		buffpet = new BuffAgent(0, this, "buffpet");
		islocked = false;
		isbinded = false;
		timeout = 0;
		skills = new java.util.LinkedList<xbean.PetSkill>();
		treasure = 0;
		petdye1 = 0;
		petdye2 = 0;
		shenshouinccount = 0;
		marketfreezeexpire = 0;
	}

	public PetInfo() {
		this(0, null, null);
	}

	public PetInfo(PetInfo _o_) {
		this(_o_, null, null);
	}

	PetInfo(xbean.PetInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PetInfo) assign((PetInfo)_o1_);
		else if (_o1_ instanceof PetInfo.Data) assign((PetInfo.Data)_o1_);
		else if (_o1_ instanceof PetInfo.Const) assign(((PetInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PetInfo _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		key = _o_.key;
		uniqid = _o_.uniqid;
		name = _o_.name;
		level = _o_.level;
		initbfp = new BasicFightProperties(_o_.initbfp, this, "initbfp");
		bfp = new BasicFightProperties(_o_.bfp, this, "bfp");
		hp = _o_.hp;
		mp = _o_.mp;
		growrate = _o_.growrate;
		life = _o_.life;
		exp = _o_.exp;
		bornattackapt = _o_.bornattackapt;
		borndefendapt = _o_.borndefendapt;
		bornphyforceapt = _o_.bornphyforceapt;
		bornmagicapt = _o_.bornmagicapt;
		bornspeedapt = _o_.bornspeedapt;
		borndodgeapt = _o_.borndodgeapt;
		kind = _o_.kind;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		buffpet = new BuffAgent(_o_.buffpet, this, "buffpet");
		islocked = _o_.islocked;
		isbinded = _o_.isbinded;
		timeout = _o_.timeout;
		petscore = _o_.petscore;
		petbasescore = _o_.petbasescore;
		ownerid = _o_.ownerid;
		skills = new java.util.LinkedList<xbean.PetSkill>();
		for (xbean.PetSkill _v_ : _o_.skills)
			skills.add(new PetSkill(_v_, this, "skills"));
		point = _o_.point;
		autoaddcons = _o_.autoaddcons;
		autoaddiq = _o_.autoaddiq;
		autoaddstr = _o_.autoaddstr;
		autoaddendu = _o_.autoaddendu;
		autoaddagi = _o_.autoaddagi;
		pointresetcount = _o_.pointresetcount;
		aptaddcount = _o_.aptaddcount;
		growrateaddcount = _o_.growrateaddcount;
		washcount = _o_.washcount;
		treasure = _o_.treasure;
		petdye1 = _o_.petdye1;
		petdye2 = _o_.petdye2;
		shenshouinccount = _o_.shenshouinccount;
		marketfreezeexpire = _o_.marketfreezeexpire;
	}

	private void assign(PetInfo.Data _o_) {
		id = _o_.id;
		key = _o_.key;
		uniqid = _o_.uniqid;
		name = _o_.name;
		level = _o_.level;
		initbfp = new BasicFightProperties(_o_.initbfp, this, "initbfp");
		bfp = new BasicFightProperties(_o_.bfp, this, "bfp");
		hp = _o_.hp;
		mp = _o_.mp;
		growrate = _o_.growrate;
		life = _o_.life;
		exp = _o_.exp;
		bornattackapt = _o_.bornattackapt;
		borndefendapt = _o_.borndefendapt;
		bornphyforceapt = _o_.bornphyforceapt;
		bornmagicapt = _o_.bornmagicapt;
		bornspeedapt = _o_.bornspeedapt;
		borndodgeapt = _o_.borndodgeapt;
		kind = _o_.kind;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		buffpet = new BuffAgent(_o_.buffpet, this, "buffpet");
		islocked = _o_.islocked;
		isbinded = _o_.isbinded;
		timeout = _o_.timeout;
		petscore = _o_.petscore;
		petbasescore = _o_.petbasescore;
		ownerid = _o_.ownerid;
		skills = new java.util.LinkedList<xbean.PetSkill>();
		for (xbean.PetSkill _v_ : _o_.skills)
			skills.add(new PetSkill(_v_, this, "skills"));
		point = _o_.point;
		autoaddcons = _o_.autoaddcons;
		autoaddiq = _o_.autoaddiq;
		autoaddstr = _o_.autoaddstr;
		autoaddendu = _o_.autoaddendu;
		autoaddagi = _o_.autoaddagi;
		pointresetcount = _o_.pointresetcount;
		aptaddcount = _o_.aptaddcount;
		growrateaddcount = _o_.growrateaddcount;
		washcount = _o_.washcount;
		treasure = _o_.treasure;
		petdye1 = _o_.petdye1;
		petdye2 = _o_.petdye2;
		shenshouinccount = _o_.shenshouinccount;
		marketfreezeexpire = _o_.marketfreezeexpire;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(key);
		_os_.marshal(uniqid);
		_os_.marshal(name, mkdb.Const.IO_CHARSET);
		_os_.marshal(level);
		initbfp.marshal(_os_);
		bfp.marshal(_os_);
		_os_.marshal(hp);
		_os_.marshal(mp);
		_os_.marshal(growrate);
		_os_.marshal(life);
		_os_.marshal(exp);
		_os_.marshal(bornattackapt);
		_os_.marshal(borndefendapt);
		_os_.marshal(bornphyforceapt);
		_os_.marshal(bornmagicapt);
		_os_.marshal(bornspeedapt);
		_os_.marshal(borndodgeapt);
		_os_.marshal(kind);
		_os_.compact_uint32(effects.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : effects.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(finalattrs.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : finalattrs.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		buffpet.marshal(_os_);
		_os_.marshal(islocked);
		_os_.marshal(isbinded);
		_os_.marshal(timeout);
		_os_.marshal(petscore);
		_os_.marshal(petbasescore);
		_os_.marshal(ownerid);
		_os_.compact_uint32(skills.size());
		for (xbean.PetSkill _v_ : skills) {
			_v_.marshal(_os_);
		}
		_os_.marshal(point);
		_os_.marshal(autoaddcons);
		_os_.marshal(autoaddiq);
		_os_.marshal(autoaddstr);
		_os_.marshal(autoaddendu);
		_os_.marshal(autoaddagi);
		_os_.marshal(pointresetcount);
		_os_.marshal(aptaddcount);
		_os_.marshal(growrateaddcount);
		_os_.marshal(washcount);
		_os_.marshal(treasure);
		_os_.marshal(petdye1);
		_os_.marshal(petdye2);
		_os_.marshal(shenshouinccount);
		_os_.marshal(marketfreezeexpire);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		key = _os_.unmarshal_int();
		uniqid = _os_.unmarshal_long();
		name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		level = _os_.unmarshal_int();
		initbfp.unmarshal(_os_);
		bfp.unmarshal(_os_);
		hp = _os_.unmarshal_int();
		mp = _os_.unmarshal_int();
		growrate = _os_.unmarshal_int();
		life = _os_.unmarshal_int();
		exp = _os_.unmarshal_long();
		bornattackapt = _os_.unmarshal_int();
		borndefendapt = _os_.unmarshal_int();
		bornphyforceapt = _os_.unmarshal_int();
		bornmagicapt = _os_.unmarshal_int();
		bornspeedapt = _os_.unmarshal_int();
		borndodgeapt = _os_.unmarshal_int();
		kind = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				effects = new java.util.HashMap<Integer, Float>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				float _v_ = 0.0f;
				_v_ = _os_.unmarshal_float();
				effects.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				finalattrs = new java.util.HashMap<Integer, Float>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				float _v_ = 0.0f;
				_v_ = _os_.unmarshal_float();
				finalattrs.put(_k_, _v_);
			}
		}
		buffpet.unmarshal(_os_);
		islocked = _os_.unmarshal_boolean();
		isbinded = _os_.unmarshal_boolean();
		timeout = _os_.unmarshal_long();
		petscore = _os_.unmarshal_int();
		petbasescore = _os_.unmarshal_int();
		ownerid = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.PetSkill _v_ = new PetSkill(0, this, "skills");
			_v_.unmarshal(_os_);
			skills.add(_v_);
		}
		point = _os_.unmarshal_int();
		autoaddcons = _os_.unmarshal_int();
		autoaddiq = _os_.unmarshal_int();
		autoaddstr = _os_.unmarshal_int();
		autoaddendu = _os_.unmarshal_int();
		autoaddagi = _os_.unmarshal_int();
		pointresetcount = _os_.unmarshal_int();
		aptaddcount = _os_.unmarshal_int();
		growrateaddcount = _os_.unmarshal_int();
		washcount = _os_.unmarshal_int();
		treasure = _os_.unmarshal_int();
		petdye1 = _os_.unmarshal_int();
		petdye2 = _os_.unmarshal_int();
		shenshouinccount = _os_.unmarshal_int();
		marketfreezeexpire = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.PetInfo copy() {
		_xdb_verify_unsafe_();
		return new PetInfo(this);
	}

	@Override
	public xbean.PetInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetInfo toBean() {
		_xdb_verify_unsafe_();
		return new PetInfo(this); // same as copy()
	}

	@Override
	public xbean.PetInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 宠物ID
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getKey() { // 在宠物栏、仓库、商会柜台里的key
		_xdb_verify_unsafe_();
		return key;
	}

	@Override
	public long getUniqid() { // 宠物唯一ID
		_xdb_verify_unsafe_();
		return uniqid;
	}

	@Override
	public String getName() { // 名称
		_xdb_verify_unsafe_();
		return name;
	}

	@Override
	public com.locojoy.base.Octets getNameOctets() { // 名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getLevel() { // 等级    	*short够了
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public xbean.BasicFightProperties getInitbfp() { // 初始的基础战斗属性
		_xdb_verify_unsafe_();
		return initbfp;
	}

	@Override
	public xbean.BasicFightProperties getBfp() { // 基础战斗属性
		_xdb_verify_unsafe_();
		return bfp;
	}

	@Override
	public int getHp() { // 当前气血
		_xdb_verify_unsafe_();
		return hp;
	}

	@Override
	public int getMp() { // 当前法力
		_xdb_verify_unsafe_();
		return mp;
	}

	@Override
	public int getGrowrate() { // 成长率 		*short够了
		_xdb_verify_unsafe_();
		return growrate;
	}

	@Override
	public int getLife() { // 寿命
		_xdb_verify_unsafe_();
		return life;
	}

	@Override
	public long getExp() { // 当前经验
		_xdb_verify_unsafe_();
		return exp;
	}

	@Override
	public int getBornattackapt() { // 出生时攻击资质	*short够了
		_xdb_verify_unsafe_();
		return bornattackapt;
	}

	@Override
	public int getBorndefendapt() { // 出生时防御资质	*short够了
		_xdb_verify_unsafe_();
		return borndefendapt;
	}

	@Override
	public int getBornphyforceapt() { // 出生时体力资质	*short够了
		_xdb_verify_unsafe_();
		return bornphyforceapt;
	}

	@Override
	public int getBornmagicapt() { // 出生时法力资质	*short够了
		_xdb_verify_unsafe_();
		return bornmagicapt;
	}

	@Override
	public int getBornspeedapt() { // 出生时速度资质	*short够了
		_xdb_verify_unsafe_();
		return bornspeedapt;
	}

	@Override
	public int getBorndodgeapt() { // 出生时躲闪资质	*short够了
		_xdb_verify_unsafe_();
		return borndodgeapt;
	}

	@Override
	public int getKind() { // 宠物类型 野生、宝宝	*byte够了
		_xdb_verify_unsafe_();
		return kind;
	}

	@Override
	public java.util.Map<Integer, Float> getEffects() { // key = effect type id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "effects"), effects);
	}

	@Override
	public java.util.Map<Integer, Float> getEffectsAsData() { // key = effect type id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Float> effects;
		PetInfo _o_ = this;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		return effects;
	}

	@Override
	public java.util.Map<Integer, Float> getFinalattrs() { // key = attr type
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "finalattrs"), finalattrs);
	}

	@Override
	public java.util.Map<Integer, Float> getFinalattrsAsData() { // key = attr type
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Float> finalattrs;
		PetInfo _o_ = this;
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		return finalattrs;
	}

	@Override
	public xbean.BuffAgent getBuffpet() { // buff
		_xdb_verify_unsafe_();
		return buffpet;
	}

	@Override
	public boolean getIslocked() { // 是否锁定
		_xdb_verify_unsafe_();
		return islocked;
	}

	@Override
	public boolean getIsbinded() { // 是否绑定
		_xdb_verify_unsafe_();
		return isbinded;
	}

	@Override
	public long getTimeout() { // 时间锁的到期时间，为0则只是普通锁
		_xdb_verify_unsafe_();
		return timeout;
	}

	@Override
	public int getPetscore() { // 宠物评分
		_xdb_verify_unsafe_();
		return petscore;
	}

	@Override
	public int getPetbasescore() { // 宠物一级属性评分
		_xdb_verify_unsafe_();
		return petbasescore;
	}

	@Override
	public long getOwnerid() { // 宠物的主人
		_xdb_verify_unsafe_();
		return ownerid;
	}

	@Override
	public java.util.List<xbean.PetSkill> getSkills() { // 宠物拥有的战斗技能
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "skills"), skills);
	}

	public java.util.List<xbean.PetSkill> getSkillsAsData() { // 宠物拥有的战斗技能
		_xdb_verify_unsafe_();
		java.util.List<xbean.PetSkill> skills;
		PetInfo _o_ = this;
		skills = new java.util.LinkedList<xbean.PetSkill>();
		for (xbean.PetSkill _v_ : _o_.skills)
			skills.add(new PetSkill.Data(_v_));
		return skills;
	}

	@Override
	public int getPoint() { // 潜能点
		_xdb_verify_unsafe_();
		return point;
	}

	@Override
	public int getAutoaddcons() { // 加点方案-升级自动加体质
		_xdb_verify_unsafe_();
		return autoaddcons;
	}

	@Override
	public int getAutoaddiq() { // 加点方案-升级自动加智力
		_xdb_verify_unsafe_();
		return autoaddiq;
	}

	@Override
	public int getAutoaddstr() { // 加点方案-升级自动加力量
		_xdb_verify_unsafe_();
		return autoaddstr;
	}

	@Override
	public int getAutoaddendu() { // 加点方案-升级自动加耐力
		_xdb_verify_unsafe_();
		return autoaddendu;
	}

	@Override
	public int getAutoaddagi() { // 加点方案-升级自动加敏捷
		_xdb_verify_unsafe_();
		return autoaddagi;
	}

	@Override
	public int getPointresetcount() { // 宠物加点的重置次数 *short够了
		_xdb_verify_unsafe_();
		return pointresetcount;
	}

	@Override
	public int getAptaddcount() { // 资质培养次数 *byte够了
		_xdb_verify_unsafe_();
		return aptaddcount;
	}

	@Override
	public int getGrowrateaddcount() { // 成长率培养次数 *byte够了
		_xdb_verify_unsafe_();
		return growrateaddcount;
	}

	@Override
	public int getWashcount() { // 洗练次数 *short够了
		_xdb_verify_unsafe_();
		return washcount;
	}

	@Override
	public int getTreasure() { // 0表示不是珍品;1表示是珍品
		_xdb_verify_unsafe_();
		return treasure;
	}

	@Override
	public int getPetdye1() { // 宠物染色信息部位1
		_xdb_verify_unsafe_();
		return petdye1;
	}

	@Override
	public int getPetdye2() { // 宠物染色信息部位2
		_xdb_verify_unsafe_();
		return petdye2;
	}

	@Override
	public int getShenshouinccount() { // 神兽养成次数
		_xdb_verify_unsafe_();
		return shenshouinccount;
	}

	@Override
	public long getMarketfreezeexpire() { // 摆摊冻结截止时间,默认0不冻结
		_xdb_verify_unsafe_();
		return marketfreezeexpire;
	}

	@Override
	public void setId(int _v_) { // 宠物ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setKey(int _v_) { // 在宠物栏、仓库、商会柜台里的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "key") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, key) {
					public void rollback() { key = _xdb_saved; }
				};}});
		key = _v_;
	}

	@Override
	public void setUniqid(long _v_) { // 宠物唯一ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "uniqid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, uniqid) {
					public void rollback() { uniqid = _xdb_saved; }
				};}});
		uniqid = _v_;
	}

	@Override
	public void setName(String _v_) { // 名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "name") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, name) {
					public void rollback() { name = _xdb_saved; }
				};}});
		name = _v_;
	}

	@Override
	public void setNameOctets(com.locojoy.base.Octets _v_) { // 名称
		_xdb_verify_unsafe_();
		this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setLevel(int _v_) { // 等级    	*short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setHp(int _v_) { // 当前气血
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, hp) {
					public void rollback() { hp = _xdb_saved; }
				};}});
		hp = _v_;
	}

	@Override
	public void setMp(int _v_) { // 当前法力
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, mp) {
					public void rollback() { mp = _xdb_saved; }
				};}});
		mp = _v_;
	}

	@Override
	public void setGrowrate(int _v_) { // 成长率 		*short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "growrate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, growrate) {
					public void rollback() { growrate = _xdb_saved; }
				};}});
		growrate = _v_;
	}

	@Override
	public void setLife(int _v_) { // 寿命
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "life") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, life) {
					public void rollback() { life = _xdb_saved; }
				};}});
		life = _v_;
	}

	@Override
	public void setExp(long _v_) { // 当前经验
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "exp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, exp) {
					public void rollback() { exp = _xdb_saved; }
				};}});
		exp = _v_;
	}

	@Override
	public void setBornattackapt(int _v_) { // 出生时攻击资质	*short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bornattackapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bornattackapt) {
					public void rollback() { bornattackapt = _xdb_saved; }
				};}});
		bornattackapt = _v_;
	}

	@Override
	public void setBorndefendapt(int _v_) { // 出生时防御资质	*short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "borndefendapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, borndefendapt) {
					public void rollback() { borndefendapt = _xdb_saved; }
				};}});
		borndefendapt = _v_;
	}

	@Override
	public void setBornphyforceapt(int _v_) { // 出生时体力资质	*short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bornphyforceapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bornphyforceapt) {
					public void rollback() { bornphyforceapt = _xdb_saved; }
				};}});
		bornphyforceapt = _v_;
	}

	@Override
	public void setBornmagicapt(int _v_) { // 出生时法力资质	*short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bornmagicapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bornmagicapt) {
					public void rollback() { bornmagicapt = _xdb_saved; }
				};}});
		bornmagicapt = _v_;
	}

	@Override
	public void setBornspeedapt(int _v_) { // 出生时速度资质	*short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bornspeedapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bornspeedapt) {
					public void rollback() { bornspeedapt = _xdb_saved; }
				};}});
		bornspeedapt = _v_;
	}

	@Override
	public void setBorndodgeapt(int _v_) { // 出生时躲闪资质	*short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "borndodgeapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, borndodgeapt) {
					public void rollback() { borndodgeapt = _xdb_saved; }
				};}});
		borndodgeapt = _v_;
	}

	@Override
	public void setKind(int _v_) { // 宠物类型 野生、宝宝	*byte够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "kind") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, kind) {
					public void rollback() { kind = _xdb_saved; }
				};}});
		kind = _v_;
	}

	@Override
	public void setIslocked(boolean _v_) { // 是否锁定
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "islocked") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, islocked) {
					public void rollback() { islocked = _xdb_saved; }
				};}});
		islocked = _v_;
	}

	@Override
	public void setIsbinded(boolean _v_) { // 是否绑定
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isbinded") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, isbinded) {
					public void rollback() { isbinded = _xdb_saved; }
				};}});
		isbinded = _v_;
	}

	@Override
	public void setTimeout(long _v_) { // 时间锁的到期时间，为0则只是普通锁
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "timeout") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, timeout) {
					public void rollback() { timeout = _xdb_saved; }
				};}});
		timeout = _v_;
	}

	@Override
	public void setPetscore(int _v_) { // 宠物评分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, petscore) {
					public void rollback() { petscore = _xdb_saved; }
				};}});
		petscore = _v_;
	}

	@Override
	public void setPetbasescore(int _v_) { // 宠物一级属性评分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petbasescore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, petbasescore) {
					public void rollback() { petbasescore = _xdb_saved; }
				};}});
		petbasescore = _v_;
	}

	@Override
	public void setOwnerid(long _v_) { // 宠物的主人
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ownerid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, ownerid) {
					public void rollback() { ownerid = _xdb_saved; }
				};}});
		ownerid = _v_;
	}

	@Override
	public void setPoint(int _v_) { // 潜能点
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "point") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, point) {
					public void rollback() { point = _xdb_saved; }
				};}});
		point = _v_;
	}

	@Override
	public void setAutoaddcons(int _v_) { // 加点方案-升级自动加体质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autoaddcons") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autoaddcons) {
					public void rollback() { autoaddcons = _xdb_saved; }
				};}});
		autoaddcons = _v_;
	}

	@Override
	public void setAutoaddiq(int _v_) { // 加点方案-升级自动加智力
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autoaddiq") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autoaddiq) {
					public void rollback() { autoaddiq = _xdb_saved; }
				};}});
		autoaddiq = _v_;
	}

	@Override
	public void setAutoaddstr(int _v_) { // 加点方案-升级自动加力量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autoaddstr") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autoaddstr) {
					public void rollback() { autoaddstr = _xdb_saved; }
				};}});
		autoaddstr = _v_;
	}

	@Override
	public void setAutoaddendu(int _v_) { // 加点方案-升级自动加耐力
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autoaddendu") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autoaddendu) {
					public void rollback() { autoaddendu = _xdb_saved; }
				};}});
		autoaddendu = _v_;
	}

	@Override
	public void setAutoaddagi(int _v_) { // 加点方案-升级自动加敏捷
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autoaddagi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autoaddagi) {
					public void rollback() { autoaddagi = _xdb_saved; }
				};}});
		autoaddagi = _v_;
	}

	@Override
	public void setPointresetcount(int _v_) { // 宠物加点的重置次数 *short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "pointresetcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, pointresetcount) {
					public void rollback() { pointresetcount = _xdb_saved; }
				};}});
		pointresetcount = _v_;
	}

	@Override
	public void setAptaddcount(int _v_) { // 资质培养次数 *byte够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "aptaddcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, aptaddcount) {
					public void rollback() { aptaddcount = _xdb_saved; }
				};}});
		aptaddcount = _v_;
	}

	@Override
	public void setGrowrateaddcount(int _v_) { // 成长率培养次数 *byte够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "growrateaddcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, growrateaddcount) {
					public void rollback() { growrateaddcount = _xdb_saved; }
				};}});
		growrateaddcount = _v_;
	}

	@Override
	public void setWashcount(int _v_) { // 洗练次数 *short够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "washcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, washcount) {
					public void rollback() { washcount = _xdb_saved; }
				};}});
		washcount = _v_;
	}

	@Override
	public void setTreasure(int _v_) { // 0表示不是珍品;1表示是珍品
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "treasure") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, treasure) {
					public void rollback() { treasure = _xdb_saved; }
				};}});
		treasure = _v_;
	}

	@Override
	public void setPetdye1(int _v_) { // 宠物染色信息部位1
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petdye1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, petdye1) {
					public void rollback() { petdye1 = _xdb_saved; }
				};}});
		petdye1 = _v_;
	}

	@Override
	public void setPetdye2(int _v_) { // 宠物染色信息部位2
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petdye2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, petdye2) {
					public void rollback() { petdye2 = _xdb_saved; }
				};}});
		petdye2 = _v_;
	}

	@Override
	public void setShenshouinccount(int _v_) { // 神兽养成次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shenshouinccount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shenshouinccount) {
					public void rollback() { shenshouinccount = _xdb_saved; }
				};}});
		shenshouinccount = _v_;
	}

	@Override
	public void setMarketfreezeexpire(long _v_) { // 摆摊冻结截止时间,默认0不冻结
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "marketfreezeexpire") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, marketfreezeexpire) {
					public void rollback() { marketfreezeexpire = _xdb_saved; }
				};}});
		marketfreezeexpire = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PetInfo _o_ = null;
		if ( _o1_ instanceof PetInfo ) _o_ = (PetInfo)_o1_;
		else if ( _o1_ instanceof PetInfo.Const ) _o_ = ((PetInfo.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (key != _o_.key) return false;
		if (uniqid != _o_.uniqid) return false;
		if (!name.equals(_o_.name)) return false;
		if (level != _o_.level) return false;
		if (!initbfp.equals(_o_.initbfp)) return false;
		if (!bfp.equals(_o_.bfp)) return false;
		if (hp != _o_.hp) return false;
		if (mp != _o_.mp) return false;
		if (growrate != _o_.growrate) return false;
		if (life != _o_.life) return false;
		if (exp != _o_.exp) return false;
		if (bornattackapt != _o_.bornattackapt) return false;
		if (borndefendapt != _o_.borndefendapt) return false;
		if (bornphyforceapt != _o_.bornphyforceapt) return false;
		if (bornmagicapt != _o_.bornmagicapt) return false;
		if (bornspeedapt != _o_.bornspeedapt) return false;
		if (borndodgeapt != _o_.borndodgeapt) return false;
		if (kind != _o_.kind) return false;
		if (!effects.equals(_o_.effects)) return false;
		if (!finalattrs.equals(_o_.finalattrs)) return false;
		if (!buffpet.equals(_o_.buffpet)) return false;
		if (islocked != _o_.islocked) return false;
		if (isbinded != _o_.isbinded) return false;
		if (timeout != _o_.timeout) return false;
		if (petscore != _o_.petscore) return false;
		if (petbasescore != _o_.petbasescore) return false;
		if (ownerid != _o_.ownerid) return false;
		if (!skills.equals(_o_.skills)) return false;
		if (point != _o_.point) return false;
		if (autoaddcons != _o_.autoaddcons) return false;
		if (autoaddiq != _o_.autoaddiq) return false;
		if (autoaddstr != _o_.autoaddstr) return false;
		if (autoaddendu != _o_.autoaddendu) return false;
		if (autoaddagi != _o_.autoaddagi) return false;
		if (pointresetcount != _o_.pointresetcount) return false;
		if (aptaddcount != _o_.aptaddcount) return false;
		if (growrateaddcount != _o_.growrateaddcount) return false;
		if (washcount != _o_.washcount) return false;
		if (treasure != _o_.treasure) return false;
		if (petdye1 != _o_.petdye1) return false;
		if (petdye2 != _o_.petdye2) return false;
		if (shenshouinccount != _o_.shenshouinccount) return false;
		if (marketfreezeexpire != _o_.marketfreezeexpire) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += key;
		_h_ += uniqid;
		_h_ += name.hashCode();
		_h_ += level;
		_h_ += initbfp.hashCode();
		_h_ += bfp.hashCode();
		_h_ += hp;
		_h_ += mp;
		_h_ += growrate;
		_h_ += life;
		_h_ += exp;
		_h_ += bornattackapt;
		_h_ += borndefendapt;
		_h_ += bornphyforceapt;
		_h_ += bornmagicapt;
		_h_ += bornspeedapt;
		_h_ += borndodgeapt;
		_h_ += kind;
		_h_ += effects.hashCode();
		_h_ += finalattrs.hashCode();
		_h_ += buffpet.hashCode();
		_h_ += islocked ? 1231 : 1237;
		_h_ += isbinded ? 1231 : 1237;
		_h_ += timeout;
		_h_ += petscore;
		_h_ += petbasescore;
		_h_ += ownerid;
		_h_ += skills.hashCode();
		_h_ += point;
		_h_ += autoaddcons;
		_h_ += autoaddiq;
		_h_ += autoaddstr;
		_h_ += autoaddendu;
		_h_ += autoaddagi;
		_h_ += pointresetcount;
		_h_ += aptaddcount;
		_h_ += growrateaddcount;
		_h_ += washcount;
		_h_ += treasure;
		_h_ += petdye1;
		_h_ += petdye2;
		_h_ += shenshouinccount;
		_h_ += marketfreezeexpire;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(key);
		_sb_.append(",");
		_sb_.append(uniqid);
		_sb_.append(",");
		_sb_.append("'").append(name).append("'");
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(initbfp);
		_sb_.append(",");
		_sb_.append(bfp);
		_sb_.append(",");
		_sb_.append(hp);
		_sb_.append(",");
		_sb_.append(mp);
		_sb_.append(",");
		_sb_.append(growrate);
		_sb_.append(",");
		_sb_.append(life);
		_sb_.append(",");
		_sb_.append(exp);
		_sb_.append(",");
		_sb_.append(bornattackapt);
		_sb_.append(",");
		_sb_.append(borndefendapt);
		_sb_.append(",");
		_sb_.append(bornphyforceapt);
		_sb_.append(",");
		_sb_.append(bornmagicapt);
		_sb_.append(",");
		_sb_.append(bornspeedapt);
		_sb_.append(",");
		_sb_.append(borndodgeapt);
		_sb_.append(",");
		_sb_.append(kind);
		_sb_.append(",");
		_sb_.append(effects);
		_sb_.append(",");
		_sb_.append(finalattrs);
		_sb_.append(",");
		_sb_.append(buffpet);
		_sb_.append(",");
		_sb_.append(islocked);
		_sb_.append(",");
		_sb_.append(isbinded);
		_sb_.append(",");
		_sb_.append(timeout);
		_sb_.append(",");
		_sb_.append(petscore);
		_sb_.append(",");
		_sb_.append(petbasescore);
		_sb_.append(",");
		_sb_.append(ownerid);
		_sb_.append(",");
		_sb_.append(skills);
		_sb_.append(",");
		_sb_.append(point);
		_sb_.append(",");
		_sb_.append(autoaddcons);
		_sb_.append(",");
		_sb_.append(autoaddiq);
		_sb_.append(",");
		_sb_.append(autoaddstr);
		_sb_.append(",");
		_sb_.append(autoaddendu);
		_sb_.append(",");
		_sb_.append(autoaddagi);
		_sb_.append(",");
		_sb_.append(pointresetcount);
		_sb_.append(",");
		_sb_.append(aptaddcount);
		_sb_.append(",");
		_sb_.append(growrateaddcount);
		_sb_.append(",");
		_sb_.append(washcount);
		_sb_.append(",");
		_sb_.append(treasure);
		_sb_.append(",");
		_sb_.append(petdye1);
		_sb_.append(",");
		_sb_.append(petdye2);
		_sb_.append(",");
		_sb_.append(shenshouinccount);
		_sb_.append(",");
		_sb_.append(marketfreezeexpire);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("key"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uniqid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("name"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("initbfp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bfp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("growrate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("life"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("exp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bornattackapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("borndefendapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bornphyforceapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bornmagicapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bornspeedapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("borndodgeapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("kind"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("effects"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("finalattrs"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buffpet"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("islocked"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isbinded"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("timeout"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petbasescore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ownerid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skills"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("point"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autoaddcons"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autoaddiq"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autoaddstr"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autoaddendu"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autoaddagi"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("pointresetcount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("aptaddcount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("growrateaddcount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("washcount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("treasure"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petdye1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petdye2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shenshouinccount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("marketfreezeexpire"));
		return lb;
	}

	private class Const implements xbean.PetInfo {
		PetInfo nThis() {
			return PetInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PetInfo copy() {
			return PetInfo.this.copy();
		}

		@Override
		public xbean.PetInfo toData() {
			return PetInfo.this.toData();
		}

		public xbean.PetInfo toBean() {
			return PetInfo.this.toBean();
		}

		@Override
		public xbean.PetInfo toDataIf() {
			return PetInfo.this.toDataIf();
		}

		public xbean.PetInfo toBeanIf() {
			return PetInfo.this.toBeanIf();
		}

		@Override
		public int getId() { // 宠物ID
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getKey() { // 在宠物栏、仓库、商会柜台里的key
			_xdb_verify_unsafe_();
			return key;
		}

		@Override
		public long getUniqid() { // 宠物唯一ID
			_xdb_verify_unsafe_();
			return uniqid;
		}

		@Override
		public String getName() { // 名称
			_xdb_verify_unsafe_();
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 名称
			_xdb_verify_unsafe_();
			return PetInfo.this.getNameOctets();
		}

		@Override
		public int getLevel() { // 等级    	*short够了
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public xbean.BasicFightProperties getInitbfp() { // 初始的基础战斗属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(initbfp);
		}

		@Override
		public xbean.BasicFightProperties getBfp() { // 基础战斗属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(bfp);
		}

		@Override
		public int getHp() { // 当前气血
			_xdb_verify_unsafe_();
			return hp;
		}

		@Override
		public int getMp() { // 当前法力
			_xdb_verify_unsafe_();
			return mp;
		}

		@Override
		public int getGrowrate() { // 成长率 		*short够了
			_xdb_verify_unsafe_();
			return growrate;
		}

		@Override
		public int getLife() { // 寿命
			_xdb_verify_unsafe_();
			return life;
		}

		@Override
		public long getExp() { // 当前经验
			_xdb_verify_unsafe_();
			return exp;
		}

		@Override
		public int getBornattackapt() { // 出生时攻击资质	*short够了
			_xdb_verify_unsafe_();
			return bornattackapt;
		}

		@Override
		public int getBorndefendapt() { // 出生时防御资质	*short够了
			_xdb_verify_unsafe_();
			return borndefendapt;
		}

		@Override
		public int getBornphyforceapt() { // 出生时体力资质	*short够了
			_xdb_verify_unsafe_();
			return bornphyforceapt;
		}

		@Override
		public int getBornmagicapt() { // 出生时法力资质	*short够了
			_xdb_verify_unsafe_();
			return bornmagicapt;
		}

		@Override
		public int getBornspeedapt() { // 出生时速度资质	*short够了
			_xdb_verify_unsafe_();
			return bornspeedapt;
		}

		@Override
		public int getBorndodgeapt() { // 出生时躲闪资质	*short够了
			_xdb_verify_unsafe_();
			return borndodgeapt;
		}

		@Override
		public int getKind() { // 宠物类型 野生、宝宝	*byte够了
			_xdb_verify_unsafe_();
			return kind;
		}

		@Override
		public java.util.Map<Integer, Float> getEffects() { // key = effect type id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(effects);
		}

		@Override
		public java.util.Map<Integer, Float> getEffectsAsData() { // key = effect type id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Float> effects;
			PetInfo _o_ = PetInfo.this;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrs() { // key = attr type
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(finalattrs);
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrsAsData() { // key = attr type
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Float> finalattrs;
			PetInfo _o_ = PetInfo.this;
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			return finalattrs;
		}

		@Override
		public xbean.BuffAgent getBuffpet() { // buff
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(buffpet);
		}

		@Override
		public boolean getIslocked() { // 是否锁定
			_xdb_verify_unsafe_();
			return islocked;
		}

		@Override
		public boolean getIsbinded() { // 是否绑定
			_xdb_verify_unsafe_();
			return isbinded;
		}

		@Override
		public long getTimeout() { // 时间锁的到期时间，为0则只是普通锁
			_xdb_verify_unsafe_();
			return timeout;
		}

		@Override
		public int getPetscore() { // 宠物评分
			_xdb_verify_unsafe_();
			return petscore;
		}

		@Override
		public int getPetbasescore() { // 宠物一级属性评分
			_xdb_verify_unsafe_();
			return petbasescore;
		}

		@Override
		public long getOwnerid() { // 宠物的主人
			_xdb_verify_unsafe_();
			return ownerid;
		}

		@Override
		public java.util.List<xbean.PetSkill> getSkills() { // 宠物拥有的战斗技能
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(skills);
		}

		public java.util.List<xbean.PetSkill> getSkillsAsData() { // 宠物拥有的战斗技能
			_xdb_verify_unsafe_();
			java.util.List<xbean.PetSkill> skills;
			PetInfo _o_ = PetInfo.this;
		skills = new java.util.LinkedList<xbean.PetSkill>();
		for (xbean.PetSkill _v_ : _o_.skills)
			skills.add(new PetSkill.Data(_v_));
			return skills;
		}

		@Override
		public int getPoint() { // 潜能点
			_xdb_verify_unsafe_();
			return point;
		}

		@Override
		public int getAutoaddcons() { // 加点方案-升级自动加体质
			_xdb_verify_unsafe_();
			return autoaddcons;
		}

		@Override
		public int getAutoaddiq() { // 加点方案-升级自动加智力
			_xdb_verify_unsafe_();
			return autoaddiq;
		}

		@Override
		public int getAutoaddstr() { // 加点方案-升级自动加力量
			_xdb_verify_unsafe_();
			return autoaddstr;
		}

		@Override
		public int getAutoaddendu() { // 加点方案-升级自动加耐力
			_xdb_verify_unsafe_();
			return autoaddendu;
		}

		@Override
		public int getAutoaddagi() { // 加点方案-升级自动加敏捷
			_xdb_verify_unsafe_();
			return autoaddagi;
		}

		@Override
		public int getPointresetcount() { // 宠物加点的重置次数 *short够了
			_xdb_verify_unsafe_();
			return pointresetcount;
		}

		@Override
		public int getAptaddcount() { // 资质培养次数 *byte够了
			_xdb_verify_unsafe_();
			return aptaddcount;
		}

		@Override
		public int getGrowrateaddcount() { // 成长率培养次数 *byte够了
			_xdb_verify_unsafe_();
			return growrateaddcount;
		}

		@Override
		public int getWashcount() { // 洗练次数 *short够了
			_xdb_verify_unsafe_();
			return washcount;
		}

		@Override
		public int getTreasure() { // 0表示不是珍品;1表示是珍品
			_xdb_verify_unsafe_();
			return treasure;
		}

		@Override
		public int getPetdye1() { // 宠物染色信息部位1
			_xdb_verify_unsafe_();
			return petdye1;
		}

		@Override
		public int getPetdye2() { // 宠物染色信息部位2
			_xdb_verify_unsafe_();
			return petdye2;
		}

		@Override
		public int getShenshouinccount() { // 神兽养成次数
			_xdb_verify_unsafe_();
			return shenshouinccount;
		}

		@Override
		public long getMarketfreezeexpire() { // 摆摊冻结截止时间,默认0不冻结
			_xdb_verify_unsafe_();
			return marketfreezeexpire;
		}

		@Override
		public void setId(int _v_) { // 宠物ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setKey(int _v_) { // 在宠物栏、仓库、商会柜台里的key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUniqid(long _v_) { // 宠物唯一ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setName(String _v_) { // 名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级    	*short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHp(int _v_) { // 当前气血
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMp(int _v_) { // 当前法力
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGrowrate(int _v_) { // 成长率 		*short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLife(int _v_) { // 寿命
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExp(long _v_) { // 当前经验
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBornattackapt(int _v_) { // 出生时攻击资质	*short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBorndefendapt(int _v_) { // 出生时防御资质	*short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBornphyforceapt(int _v_) { // 出生时体力资质	*short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBornmagicapt(int _v_) { // 出生时法力资质	*short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBornspeedapt(int _v_) { // 出生时速度资质	*short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBorndodgeapt(int _v_) { // 出生时躲闪资质	*short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setKind(int _v_) { // 宠物类型 野生、宝宝	*byte够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIslocked(boolean _v_) { // 是否锁定
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsbinded(boolean _v_) { // 是否绑定
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimeout(long _v_) { // 时间锁的到期时间，为0则只是普通锁
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetscore(int _v_) { // 宠物评分
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetbasescore(int _v_) { // 宠物一级属性评分
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOwnerid(long _v_) { // 宠物的主人
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPoint(int _v_) { // 潜能点
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutoaddcons(int _v_) { // 加点方案-升级自动加体质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutoaddiq(int _v_) { // 加点方案-升级自动加智力
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutoaddstr(int _v_) { // 加点方案-升级自动加力量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutoaddendu(int _v_) { // 加点方案-升级自动加耐力
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutoaddagi(int _v_) { // 加点方案-升级自动加敏捷
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPointresetcount(int _v_) { // 宠物加点的重置次数 *short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAptaddcount(int _v_) { // 资质培养次数 *byte够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGrowrateaddcount(int _v_) { // 成长率培养次数 *byte够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWashcount(int _v_) { // 洗练次数 *short够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTreasure(int _v_) { // 0表示不是珍品;1表示是珍品
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetdye1(int _v_) { // 宠物染色信息部位1
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetdye2(int _v_) { // 宠物染色信息部位2
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShenshouinccount(int _v_) { // 神兽养成次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMarketfreezeexpire(long _v_) { // 摆摊冻结截止时间,默认0不冻结
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean toConst() {
			_xdb_verify_unsafe_();
			return this;
		}

		@Override
		public boolean isConst() {
			_xdb_verify_unsafe_();
			return true;
		}

		@Override
		public boolean isData() {
			return PetInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PetInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PetInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PetInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PetInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PetInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PetInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PetInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return PetInfo.this.toString();
		}

	}

	public static final class Data implements xbean.PetInfo {
		private int id; // 宠物ID
		private int key; // 在宠物栏、仓库、商会柜台里的key
		private long uniqid; // 宠物唯一ID
		private String name; // 名称
		private int level; // 等级    	*short够了
		private xbean.BasicFightProperties initbfp; // 初始的基础战斗属性
		private xbean.BasicFightProperties bfp; // 基础战斗属性
		private int hp; // 当前气血
		private int mp; // 当前法力
		private int growrate; // 成长率 		*short够了
		private int life; // 寿命
		private long exp; // 当前经验
		private int bornattackapt; // 出生时攻击资质	*short够了
		private int borndefendapt; // 出生时防御资质	*short够了
		private int bornphyforceapt; // 出生时体力资质	*short够了
		private int bornmagicapt; // 出生时法力资质	*short够了
		private int bornspeedapt; // 出生时速度资质	*short够了
		private int borndodgeapt; // 出生时躲闪资质	*short够了
		private int kind; // 宠物类型 野生、宝宝	*byte够了
		private java.util.HashMap<Integer, Float> effects; // key = effect type id
		private java.util.HashMap<Integer, Float> finalattrs; // key = attr type
		private xbean.BuffAgent buffpet; // buff
		private boolean islocked; // 是否锁定
		private boolean isbinded; // 是否绑定
		private long timeout; // 时间锁的到期时间，为0则只是普通锁
		private int petscore; // 宠物评分
		private int petbasescore; // 宠物一级属性评分
		private long ownerid; // 宠物的主人
		private java.util.LinkedList<xbean.PetSkill> skills; // 宠物拥有的战斗技能
		private int point; // 潜能点
		private int autoaddcons; // 加点方案-升级自动加体质
		private int autoaddiq; // 加点方案-升级自动加智力
		private int autoaddstr; // 加点方案-升级自动加力量
		private int autoaddendu; // 加点方案-升级自动加耐力
		private int autoaddagi; // 加点方案-升级自动加敏捷
		private int pointresetcount; // 宠物加点的重置次数 *short够了
		private int aptaddcount; // 资质培养次数 *byte够了
		private int growrateaddcount; // 成长率培养次数 *byte够了
		private int washcount; // 洗练次数 *short够了
		private int treasure; // 0表示不是珍品;1表示是珍品
		private int petdye1; // 宠物染色信息部位1
		private int petdye2; // 宠物染色信息部位2
		private int shenshouinccount; // 神兽养成次数
		private long marketfreezeexpire; // 摆摊冻结截止时间,默认0不冻结

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			name = "";
			initbfp = new BasicFightProperties.Data();
			bfp = new BasicFightProperties.Data();
			effects = new java.util.HashMap<Integer, Float>();
			finalattrs = new java.util.HashMap<Integer, Float>();
			buffpet = new BuffAgent.Data();
			islocked = false;
			isbinded = false;
			timeout = 0;
			skills = new java.util.LinkedList<xbean.PetSkill>();
			treasure = 0;
			petdye1 = 0;
			petdye2 = 0;
			shenshouinccount = 0;
			marketfreezeexpire = 0;
		}

		Data(xbean.PetInfo _o1_) {
			if (_o1_ instanceof PetInfo) assign((PetInfo)_o1_);
			else if (_o1_ instanceof PetInfo.Data) assign((PetInfo.Data)_o1_);
			else if (_o1_ instanceof PetInfo.Const) assign(((PetInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PetInfo _o_) {
			id = _o_.id;
			key = _o_.key;
			uniqid = _o_.uniqid;
			name = _o_.name;
			level = _o_.level;
			initbfp = new BasicFightProperties.Data(_o_.initbfp);
			bfp = new BasicFightProperties.Data(_o_.bfp);
			hp = _o_.hp;
			mp = _o_.mp;
			growrate = _o_.growrate;
			life = _o_.life;
			exp = _o_.exp;
			bornattackapt = _o_.bornattackapt;
			borndefendapt = _o_.borndefendapt;
			bornphyforceapt = _o_.bornphyforceapt;
			bornmagicapt = _o_.bornmagicapt;
			bornspeedapt = _o_.bornspeedapt;
			borndodgeapt = _o_.borndodgeapt;
			kind = _o_.kind;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			buffpet = new BuffAgent.Data(_o_.buffpet);
			islocked = _o_.islocked;
			isbinded = _o_.isbinded;
			timeout = _o_.timeout;
			petscore = _o_.petscore;
			petbasescore = _o_.petbasescore;
			ownerid = _o_.ownerid;
			skills = new java.util.LinkedList<xbean.PetSkill>();
			for (xbean.PetSkill _v_ : _o_.skills)
				skills.add(new PetSkill.Data(_v_));
			point = _o_.point;
			autoaddcons = _o_.autoaddcons;
			autoaddiq = _o_.autoaddiq;
			autoaddstr = _o_.autoaddstr;
			autoaddendu = _o_.autoaddendu;
			autoaddagi = _o_.autoaddagi;
			pointresetcount = _o_.pointresetcount;
			aptaddcount = _o_.aptaddcount;
			growrateaddcount = _o_.growrateaddcount;
			washcount = _o_.washcount;
			treasure = _o_.treasure;
			petdye1 = _o_.petdye1;
			petdye2 = _o_.petdye2;
			shenshouinccount = _o_.shenshouinccount;
			marketfreezeexpire = _o_.marketfreezeexpire;
		}

		private void assign(PetInfo.Data _o_) {
			id = _o_.id;
			key = _o_.key;
			uniqid = _o_.uniqid;
			name = _o_.name;
			level = _o_.level;
			initbfp = new BasicFightProperties.Data(_o_.initbfp);
			bfp = new BasicFightProperties.Data(_o_.bfp);
			hp = _o_.hp;
			mp = _o_.mp;
			growrate = _o_.growrate;
			life = _o_.life;
			exp = _o_.exp;
			bornattackapt = _o_.bornattackapt;
			borndefendapt = _o_.borndefendapt;
			bornphyforceapt = _o_.bornphyforceapt;
			bornmagicapt = _o_.bornmagicapt;
			bornspeedapt = _o_.bornspeedapt;
			borndodgeapt = _o_.borndodgeapt;
			kind = _o_.kind;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			buffpet = new BuffAgent.Data(_o_.buffpet);
			islocked = _o_.islocked;
			isbinded = _o_.isbinded;
			timeout = _o_.timeout;
			petscore = _o_.petscore;
			petbasescore = _o_.petbasescore;
			ownerid = _o_.ownerid;
			skills = new java.util.LinkedList<xbean.PetSkill>();
			for (xbean.PetSkill _v_ : _o_.skills)
				skills.add(new PetSkill.Data(_v_));
			point = _o_.point;
			autoaddcons = _o_.autoaddcons;
			autoaddiq = _o_.autoaddiq;
			autoaddstr = _o_.autoaddstr;
			autoaddendu = _o_.autoaddendu;
			autoaddagi = _o_.autoaddagi;
			pointresetcount = _o_.pointresetcount;
			aptaddcount = _o_.aptaddcount;
			growrateaddcount = _o_.growrateaddcount;
			washcount = _o_.washcount;
			treasure = _o_.treasure;
			petdye1 = _o_.petdye1;
			petdye2 = _o_.petdye2;
			shenshouinccount = _o_.shenshouinccount;
			marketfreezeexpire = _o_.marketfreezeexpire;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(key);
			_os_.marshal(uniqid);
			_os_.marshal(name, mkdb.Const.IO_CHARSET);
			_os_.marshal(level);
			initbfp.marshal(_os_);
			bfp.marshal(_os_);
			_os_.marshal(hp);
			_os_.marshal(mp);
			_os_.marshal(growrate);
			_os_.marshal(life);
			_os_.marshal(exp);
			_os_.marshal(bornattackapt);
			_os_.marshal(borndefendapt);
			_os_.marshal(bornphyforceapt);
			_os_.marshal(bornmagicapt);
			_os_.marshal(bornspeedapt);
			_os_.marshal(borndodgeapt);
			_os_.marshal(kind);
			_os_.compact_uint32(effects.size());
			for (java.util.Map.Entry<Integer, Float> _e_ : effects.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(finalattrs.size());
			for (java.util.Map.Entry<Integer, Float> _e_ : finalattrs.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			buffpet.marshal(_os_);
			_os_.marshal(islocked);
			_os_.marshal(isbinded);
			_os_.marshal(timeout);
			_os_.marshal(petscore);
			_os_.marshal(petbasescore);
			_os_.marshal(ownerid);
			_os_.compact_uint32(skills.size());
			for (xbean.PetSkill _v_ : skills) {
				_v_.marshal(_os_);
			}
			_os_.marshal(point);
			_os_.marshal(autoaddcons);
			_os_.marshal(autoaddiq);
			_os_.marshal(autoaddstr);
			_os_.marshal(autoaddendu);
			_os_.marshal(autoaddagi);
			_os_.marshal(pointresetcount);
			_os_.marshal(aptaddcount);
			_os_.marshal(growrateaddcount);
			_os_.marshal(washcount);
			_os_.marshal(treasure);
			_os_.marshal(petdye1);
			_os_.marshal(petdye2);
			_os_.marshal(shenshouinccount);
			_os_.marshal(marketfreezeexpire);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			key = _os_.unmarshal_int();
			uniqid = _os_.unmarshal_long();
			name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			level = _os_.unmarshal_int();
			initbfp.unmarshal(_os_);
			bfp.unmarshal(_os_);
			hp = _os_.unmarshal_int();
			mp = _os_.unmarshal_int();
			growrate = _os_.unmarshal_int();
			life = _os_.unmarshal_int();
			exp = _os_.unmarshal_long();
			bornattackapt = _os_.unmarshal_int();
			borndefendapt = _os_.unmarshal_int();
			bornphyforceapt = _os_.unmarshal_int();
			bornmagicapt = _os_.unmarshal_int();
			bornspeedapt = _os_.unmarshal_int();
			borndodgeapt = _os_.unmarshal_int();
			kind = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					effects = new java.util.HashMap<Integer, Float>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					float _v_ = 0.0f;
					_v_ = _os_.unmarshal_float();
					effects.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					finalattrs = new java.util.HashMap<Integer, Float>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					float _v_ = 0.0f;
					_v_ = _os_.unmarshal_float();
					finalattrs.put(_k_, _v_);
				}
			}
			buffpet.unmarshal(_os_);
			islocked = _os_.unmarshal_boolean();
			isbinded = _os_.unmarshal_boolean();
			timeout = _os_.unmarshal_long();
			petscore = _os_.unmarshal_int();
			petbasescore = _os_.unmarshal_int();
			ownerid = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.PetSkill _v_ = xbean.Pod.newPetSkillData();
				_v_.unmarshal(_os_);
				skills.add(_v_);
			}
			point = _os_.unmarshal_int();
			autoaddcons = _os_.unmarshal_int();
			autoaddiq = _os_.unmarshal_int();
			autoaddstr = _os_.unmarshal_int();
			autoaddendu = _os_.unmarshal_int();
			autoaddagi = _os_.unmarshal_int();
			pointresetcount = _os_.unmarshal_int();
			aptaddcount = _os_.unmarshal_int();
			growrateaddcount = _os_.unmarshal_int();
			washcount = _os_.unmarshal_int();
			treasure = _os_.unmarshal_int();
			petdye1 = _os_.unmarshal_int();
			petdye2 = _os_.unmarshal_int();
			shenshouinccount = _os_.unmarshal_int();
			marketfreezeexpire = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.PetInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.PetInfo toData() {
			return new Data(this);
		}

		public xbean.PetInfo toBean() {
			return new PetInfo(this, null, null);
		}

		@Override
		public xbean.PetInfo toDataIf() {
			return this;
		}

		public xbean.PetInfo toBeanIf() {
			return new PetInfo(this, null, null);
		}

		// mkdb.Bean interface. Data Unsupported
		public boolean xdbManaged() { throw new UnsupportedOperationException(); }
		public mkdb.Bean xdbParent() { throw new UnsupportedOperationException(); }
		public String xdbVarname()  { throw new UnsupportedOperationException(); }
		public Long    xdbObjId()   { throw new UnsupportedOperationException(); }
		public mkdb.Bean toConst()   { throw new UnsupportedOperationException(); }
		public boolean isConst()    { return false; }
		public boolean isData()     { return true; }

		@Override
		public int getId() { // 宠物ID
			return id;
		}

		@Override
		public int getKey() { // 在宠物栏、仓库、商会柜台里的key
			return key;
		}

		@Override
		public long getUniqid() { // 宠物唯一ID
			return uniqid;
		}

		@Override
		public String getName() { // 名称
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 名称
			return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getLevel() { // 等级    	*short够了
			return level;
		}

		@Override
		public xbean.BasicFightProperties getInitbfp() { // 初始的基础战斗属性
			return initbfp;
		}

		@Override
		public xbean.BasicFightProperties getBfp() { // 基础战斗属性
			return bfp;
		}

		@Override
		public int getHp() { // 当前气血
			return hp;
		}

		@Override
		public int getMp() { // 当前法力
			return mp;
		}

		@Override
		public int getGrowrate() { // 成长率 		*short够了
			return growrate;
		}

		@Override
		public int getLife() { // 寿命
			return life;
		}

		@Override
		public long getExp() { // 当前经验
			return exp;
		}

		@Override
		public int getBornattackapt() { // 出生时攻击资质	*short够了
			return bornattackapt;
		}

		@Override
		public int getBorndefendapt() { // 出生时防御资质	*short够了
			return borndefendapt;
		}

		@Override
		public int getBornphyforceapt() { // 出生时体力资质	*short够了
			return bornphyforceapt;
		}

		@Override
		public int getBornmagicapt() { // 出生时法力资质	*short够了
			return bornmagicapt;
		}

		@Override
		public int getBornspeedapt() { // 出生时速度资质	*short够了
			return bornspeedapt;
		}

		@Override
		public int getBorndodgeapt() { // 出生时躲闪资质	*short够了
			return borndodgeapt;
		}

		@Override
		public int getKind() { // 宠物类型 野生、宝宝	*byte够了
			return kind;
		}

		@Override
		public java.util.Map<Integer, Float> getEffects() { // key = effect type id
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getEffectsAsData() { // key = effect type id
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrs() { // key = attr type
			return finalattrs;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrsAsData() { // key = attr type
			return finalattrs;
		}

		@Override
		public xbean.BuffAgent getBuffpet() { // buff
			return buffpet;
		}

		@Override
		public boolean getIslocked() { // 是否锁定
			return islocked;
		}

		@Override
		public boolean getIsbinded() { // 是否绑定
			return isbinded;
		}

		@Override
		public long getTimeout() { // 时间锁的到期时间，为0则只是普通锁
			return timeout;
		}

		@Override
		public int getPetscore() { // 宠物评分
			return petscore;
		}

		@Override
		public int getPetbasescore() { // 宠物一级属性评分
			return petbasescore;
		}

		@Override
		public long getOwnerid() { // 宠物的主人
			return ownerid;
		}

		@Override
		public java.util.List<xbean.PetSkill> getSkills() { // 宠物拥有的战斗技能
			return skills;
		}

		@Override
		public java.util.List<xbean.PetSkill> getSkillsAsData() { // 宠物拥有的战斗技能
			return skills;
		}

		@Override
		public int getPoint() { // 潜能点
			return point;
		}

		@Override
		public int getAutoaddcons() { // 加点方案-升级自动加体质
			return autoaddcons;
		}

		@Override
		public int getAutoaddiq() { // 加点方案-升级自动加智力
			return autoaddiq;
		}

		@Override
		public int getAutoaddstr() { // 加点方案-升级自动加力量
			return autoaddstr;
		}

		@Override
		public int getAutoaddendu() { // 加点方案-升级自动加耐力
			return autoaddendu;
		}

		@Override
		public int getAutoaddagi() { // 加点方案-升级自动加敏捷
			return autoaddagi;
		}

		@Override
		public int getPointresetcount() { // 宠物加点的重置次数 *short够了
			return pointresetcount;
		}

		@Override
		public int getAptaddcount() { // 资质培养次数 *byte够了
			return aptaddcount;
		}

		@Override
		public int getGrowrateaddcount() { // 成长率培养次数 *byte够了
			return growrateaddcount;
		}

		@Override
		public int getWashcount() { // 洗练次数 *short够了
			return washcount;
		}

		@Override
		public int getTreasure() { // 0表示不是珍品;1表示是珍品
			return treasure;
		}

		@Override
		public int getPetdye1() { // 宠物染色信息部位1
			return petdye1;
		}

		@Override
		public int getPetdye2() { // 宠物染色信息部位2
			return petdye2;
		}

		@Override
		public int getShenshouinccount() { // 神兽养成次数
			return shenshouinccount;
		}

		@Override
		public long getMarketfreezeexpire() { // 摆摊冻结截止时间,默认0不冻结
			return marketfreezeexpire;
		}

		@Override
		public void setId(int _v_) { // 宠物ID
			id = _v_;
		}

		@Override
		public void setKey(int _v_) { // 在宠物栏、仓库、商会柜台里的key
			key = _v_;
		}

		@Override
		public void setUniqid(long _v_) { // 宠物唯一ID
			uniqid = _v_;
		}

		@Override
		public void setName(String _v_) { // 名称
			if (null == _v_)
				throw new NullPointerException();
			name = _v_;
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 名称
			this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setLevel(int _v_) { // 等级    	*short够了
			level = _v_;
		}

		@Override
		public void setHp(int _v_) { // 当前气血
			hp = _v_;
		}

		@Override
		public void setMp(int _v_) { // 当前法力
			mp = _v_;
		}

		@Override
		public void setGrowrate(int _v_) { // 成长率 		*short够了
			growrate = _v_;
		}

		@Override
		public void setLife(int _v_) { // 寿命
			life = _v_;
		}

		@Override
		public void setExp(long _v_) { // 当前经验
			exp = _v_;
		}

		@Override
		public void setBornattackapt(int _v_) { // 出生时攻击资质	*short够了
			bornattackapt = _v_;
		}

		@Override
		public void setBorndefendapt(int _v_) { // 出生时防御资质	*short够了
			borndefendapt = _v_;
		}

		@Override
		public void setBornphyforceapt(int _v_) { // 出生时体力资质	*short够了
			bornphyforceapt = _v_;
		}

		@Override
		public void setBornmagicapt(int _v_) { // 出生时法力资质	*short够了
			bornmagicapt = _v_;
		}

		@Override
		public void setBornspeedapt(int _v_) { // 出生时速度资质	*short够了
			bornspeedapt = _v_;
		}

		@Override
		public void setBorndodgeapt(int _v_) { // 出生时躲闪资质	*short够了
			borndodgeapt = _v_;
		}

		@Override
		public void setKind(int _v_) { // 宠物类型 野生、宝宝	*byte够了
			kind = _v_;
		}

		@Override
		public void setIslocked(boolean _v_) { // 是否锁定
			islocked = _v_;
		}

		@Override
		public void setIsbinded(boolean _v_) { // 是否绑定
			isbinded = _v_;
		}

		@Override
		public void setTimeout(long _v_) { // 时间锁的到期时间，为0则只是普通锁
			timeout = _v_;
		}

		@Override
		public void setPetscore(int _v_) { // 宠物评分
			petscore = _v_;
		}

		@Override
		public void setPetbasescore(int _v_) { // 宠物一级属性评分
			petbasescore = _v_;
		}

		@Override
		public void setOwnerid(long _v_) { // 宠物的主人
			ownerid = _v_;
		}

		@Override
		public void setPoint(int _v_) { // 潜能点
			point = _v_;
		}

		@Override
		public void setAutoaddcons(int _v_) { // 加点方案-升级自动加体质
			autoaddcons = _v_;
		}

		@Override
		public void setAutoaddiq(int _v_) { // 加点方案-升级自动加智力
			autoaddiq = _v_;
		}

		@Override
		public void setAutoaddstr(int _v_) { // 加点方案-升级自动加力量
			autoaddstr = _v_;
		}

		@Override
		public void setAutoaddendu(int _v_) { // 加点方案-升级自动加耐力
			autoaddendu = _v_;
		}

		@Override
		public void setAutoaddagi(int _v_) { // 加点方案-升级自动加敏捷
			autoaddagi = _v_;
		}

		@Override
		public void setPointresetcount(int _v_) { // 宠物加点的重置次数 *short够了
			pointresetcount = _v_;
		}

		@Override
		public void setAptaddcount(int _v_) { // 资质培养次数 *byte够了
			aptaddcount = _v_;
		}

		@Override
		public void setGrowrateaddcount(int _v_) { // 成长率培养次数 *byte够了
			growrateaddcount = _v_;
		}

		@Override
		public void setWashcount(int _v_) { // 洗练次数 *short够了
			washcount = _v_;
		}

		@Override
		public void setTreasure(int _v_) { // 0表示不是珍品;1表示是珍品
			treasure = _v_;
		}

		@Override
		public void setPetdye1(int _v_) { // 宠物染色信息部位1
			petdye1 = _v_;
		}

		@Override
		public void setPetdye2(int _v_) { // 宠物染色信息部位2
			petdye2 = _v_;
		}

		@Override
		public void setShenshouinccount(int _v_) { // 神兽养成次数
			shenshouinccount = _v_;
		}

		@Override
		public void setMarketfreezeexpire(long _v_) { // 摆摊冻结截止时间,默认0不冻结
			marketfreezeexpire = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PetInfo.Data)) return false;
			PetInfo.Data _o_ = (PetInfo.Data) _o1_;
			if (id != _o_.id) return false;
			if (key != _o_.key) return false;
			if (uniqid != _o_.uniqid) return false;
			if (!name.equals(_o_.name)) return false;
			if (level != _o_.level) return false;
			if (!initbfp.equals(_o_.initbfp)) return false;
			if (!bfp.equals(_o_.bfp)) return false;
			if (hp != _o_.hp) return false;
			if (mp != _o_.mp) return false;
			if (growrate != _o_.growrate) return false;
			if (life != _o_.life) return false;
			if (exp != _o_.exp) return false;
			if (bornattackapt != _o_.bornattackapt) return false;
			if (borndefendapt != _o_.borndefendapt) return false;
			if (bornphyforceapt != _o_.bornphyforceapt) return false;
			if (bornmagicapt != _o_.bornmagicapt) return false;
			if (bornspeedapt != _o_.bornspeedapt) return false;
			if (borndodgeapt != _o_.borndodgeapt) return false;
			if (kind != _o_.kind) return false;
			if (!effects.equals(_o_.effects)) return false;
			if (!finalattrs.equals(_o_.finalattrs)) return false;
			if (!buffpet.equals(_o_.buffpet)) return false;
			if (islocked != _o_.islocked) return false;
			if (isbinded != _o_.isbinded) return false;
			if (timeout != _o_.timeout) return false;
			if (petscore != _o_.petscore) return false;
			if (petbasescore != _o_.petbasescore) return false;
			if (ownerid != _o_.ownerid) return false;
			if (!skills.equals(_o_.skills)) return false;
			if (point != _o_.point) return false;
			if (autoaddcons != _o_.autoaddcons) return false;
			if (autoaddiq != _o_.autoaddiq) return false;
			if (autoaddstr != _o_.autoaddstr) return false;
			if (autoaddendu != _o_.autoaddendu) return false;
			if (autoaddagi != _o_.autoaddagi) return false;
			if (pointresetcount != _o_.pointresetcount) return false;
			if (aptaddcount != _o_.aptaddcount) return false;
			if (growrateaddcount != _o_.growrateaddcount) return false;
			if (washcount != _o_.washcount) return false;
			if (treasure != _o_.treasure) return false;
			if (petdye1 != _o_.petdye1) return false;
			if (petdye2 != _o_.petdye2) return false;
			if (shenshouinccount != _o_.shenshouinccount) return false;
			if (marketfreezeexpire != _o_.marketfreezeexpire) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += key;
			_h_ += uniqid;
			_h_ += name.hashCode();
			_h_ += level;
			_h_ += initbfp.hashCode();
			_h_ += bfp.hashCode();
			_h_ += hp;
			_h_ += mp;
			_h_ += growrate;
			_h_ += life;
			_h_ += exp;
			_h_ += bornattackapt;
			_h_ += borndefendapt;
			_h_ += bornphyforceapt;
			_h_ += bornmagicapt;
			_h_ += bornspeedapt;
			_h_ += borndodgeapt;
			_h_ += kind;
			_h_ += effects.hashCode();
			_h_ += finalattrs.hashCode();
			_h_ += buffpet.hashCode();
			_h_ += islocked ? 1231 : 1237;
			_h_ += isbinded ? 1231 : 1237;
			_h_ += timeout;
			_h_ += petscore;
			_h_ += petbasescore;
			_h_ += ownerid;
			_h_ += skills.hashCode();
			_h_ += point;
			_h_ += autoaddcons;
			_h_ += autoaddiq;
			_h_ += autoaddstr;
			_h_ += autoaddendu;
			_h_ += autoaddagi;
			_h_ += pointresetcount;
			_h_ += aptaddcount;
			_h_ += growrateaddcount;
			_h_ += washcount;
			_h_ += treasure;
			_h_ += petdye1;
			_h_ += petdye2;
			_h_ += shenshouinccount;
			_h_ += marketfreezeexpire;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(key);
			_sb_.append(",");
			_sb_.append(uniqid);
			_sb_.append(",");
			_sb_.append("'").append(name).append("'");
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(initbfp);
			_sb_.append(",");
			_sb_.append(bfp);
			_sb_.append(",");
			_sb_.append(hp);
			_sb_.append(",");
			_sb_.append(mp);
			_sb_.append(",");
			_sb_.append(growrate);
			_sb_.append(",");
			_sb_.append(life);
			_sb_.append(",");
			_sb_.append(exp);
			_sb_.append(",");
			_sb_.append(bornattackapt);
			_sb_.append(",");
			_sb_.append(borndefendapt);
			_sb_.append(",");
			_sb_.append(bornphyforceapt);
			_sb_.append(",");
			_sb_.append(bornmagicapt);
			_sb_.append(",");
			_sb_.append(bornspeedapt);
			_sb_.append(",");
			_sb_.append(borndodgeapt);
			_sb_.append(",");
			_sb_.append(kind);
			_sb_.append(",");
			_sb_.append(effects);
			_sb_.append(",");
			_sb_.append(finalattrs);
			_sb_.append(",");
			_sb_.append(buffpet);
			_sb_.append(",");
			_sb_.append(islocked);
			_sb_.append(",");
			_sb_.append(isbinded);
			_sb_.append(",");
			_sb_.append(timeout);
			_sb_.append(",");
			_sb_.append(petscore);
			_sb_.append(",");
			_sb_.append(petbasescore);
			_sb_.append(",");
			_sb_.append(ownerid);
			_sb_.append(",");
			_sb_.append(skills);
			_sb_.append(",");
			_sb_.append(point);
			_sb_.append(",");
			_sb_.append(autoaddcons);
			_sb_.append(",");
			_sb_.append(autoaddiq);
			_sb_.append(",");
			_sb_.append(autoaddstr);
			_sb_.append(",");
			_sb_.append(autoaddendu);
			_sb_.append(",");
			_sb_.append(autoaddagi);
			_sb_.append(",");
			_sb_.append(pointresetcount);
			_sb_.append(",");
			_sb_.append(aptaddcount);
			_sb_.append(",");
			_sb_.append(growrateaddcount);
			_sb_.append(",");
			_sb_.append(washcount);
			_sb_.append(",");
			_sb_.append(treasure);
			_sb_.append(",");
			_sb_.append(petdye1);
			_sb_.append(",");
			_sb_.append(petdye2);
			_sb_.append(",");
			_sb_.append(shenshouinccount);
			_sb_.append(",");
			_sb_.append(marketfreezeexpire);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

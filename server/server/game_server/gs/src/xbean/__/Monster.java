
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Monster extends mkdb.XBean implements xbean.Monster {
	private int monsterid; // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
	private int shape; // 造型Id
	private int school; // 职业
	private int level; // 等级
	private float bodysize; // 体型大小
	private int hp; // 血
	private int uplimithp; // 当前气血上限
	private int mp; // 蓝
	private int sp; // 怒
	private xbean.BasicFightProperties bfp; // 基础战斗属性
	private java.util.HashMap<Integer, Float> effects; // 效果 key = effect type id
	private java.util.HashMap<Integer, Float> finalattrs; // 最终属性 key = attr type
	private xbean.BuffAgent buffagent; // buff代理
	private java.util.LinkedList<xbean.BattleAI> ais; // 怪物ai
	private int fightnpctype; // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
	private int monstertype; // 怪物类型,普通,精英,宝宝,boss,公会boss
	private int specialtype; // 1控制,2治疗,3惧控,4惧疗,5隐身
	private java.util.LinkedList<xbean.MonsterSkill> skills; // value= MonsterSkill
	private java.util.LinkedList<Integer> immunebuffs; // 该怪物免疫的buffId
	private java.util.HashMap<Integer, Integer> extradata; // 记录一些额外数据

	@Override
	public void _reset_unsafe_() {
		monsterid = 0;
		shape = 0;
		school = 0;
		level = 1;
		bodysize = 1;
		hp = 0;
		uplimithp = 0;
		mp = 0;
		sp = 0;
		bfp._reset_unsafe_();
		effects.clear();
		finalattrs.clear();
		buffagent._reset_unsafe_();
		ais.clear();
		fightnpctype = 0;
		monstertype = 0;
		specialtype = 0;
		skills.clear();
		immunebuffs.clear();
		extradata.clear();
	}

	Monster(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		level = 1;
		bodysize = 1;
		bfp = new BasicFightProperties(0, this, "bfp");
		effects = new java.util.HashMap<Integer, Float>();
		finalattrs = new java.util.HashMap<Integer, Float>();
		buffagent = new BuffAgent(0, this, "buffagent");
		ais = new java.util.LinkedList<xbean.BattleAI>();
		skills = new java.util.LinkedList<xbean.MonsterSkill>();
		immunebuffs = new java.util.LinkedList<Integer>();
		extradata = new java.util.HashMap<Integer, Integer>();
	}

	public Monster() {
		this(0, null, null);
	}

	public Monster(Monster _o_) {
		this(_o_, null, null);
	}

	Monster(xbean.Monster _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Monster) assign((Monster)_o1_);
		else if (_o1_ instanceof Monster.Data) assign((Monster.Data)_o1_);
		else if (_o1_ instanceof Monster.Const) assign(((Monster.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Monster _o_) {
		_o_._xdb_verify_unsafe_();
		monsterid = _o_.monsterid;
		shape = _o_.shape;
		school = _o_.school;
		level = _o_.level;
		bodysize = _o_.bodysize;
		hp = _o_.hp;
		uplimithp = _o_.uplimithp;
		mp = _o_.mp;
		sp = _o_.sp;
		bfp = new BasicFightProperties(_o_.bfp, this, "bfp");
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		buffagent = new BuffAgent(_o_.buffagent, this, "buffagent");
		ais = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.ais)
			ais.add(new BattleAI(_v_, this, "ais"));
		fightnpctype = _o_.fightnpctype;
		monstertype = _o_.monstertype;
		specialtype = _o_.specialtype;
		skills = new java.util.LinkedList<xbean.MonsterSkill>();
		for (xbean.MonsterSkill _v_ : _o_.skills)
			skills.add(new MonsterSkill(_v_, this, "skills"));
		immunebuffs = new java.util.LinkedList<Integer>();
		immunebuffs.addAll(_o_.immunebuffs);
		extradata = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extradata.entrySet())
			extradata.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(Monster.Data _o_) {
		monsterid = _o_.monsterid;
		shape = _o_.shape;
		school = _o_.school;
		level = _o_.level;
		bodysize = _o_.bodysize;
		hp = _o_.hp;
		uplimithp = _o_.uplimithp;
		mp = _o_.mp;
		sp = _o_.sp;
		bfp = new BasicFightProperties(_o_.bfp, this, "bfp");
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		buffagent = new BuffAgent(_o_.buffagent, this, "buffagent");
		ais = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.ais)
			ais.add(new BattleAI(_v_, this, "ais"));
		fightnpctype = _o_.fightnpctype;
		monstertype = _o_.monstertype;
		specialtype = _o_.specialtype;
		skills = new java.util.LinkedList<xbean.MonsterSkill>();
		for (xbean.MonsterSkill _v_ : _o_.skills)
			skills.add(new MonsterSkill(_v_, this, "skills"));
		immunebuffs = new java.util.LinkedList<Integer>();
		immunebuffs.addAll(_o_.immunebuffs);
		extradata = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extradata.entrySet())
			extradata.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(monsterid);
		_os_.marshal(shape);
		_os_.marshal(school);
		_os_.marshal(level);
		_os_.marshal(bodysize);
		_os_.marshal(hp);
		_os_.marshal(uplimithp);
		_os_.marshal(mp);
		_os_.marshal(sp);
		bfp.marshal(_os_);
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
		buffagent.marshal(_os_);
		_os_.compact_uint32(ais.size());
		for (xbean.BattleAI _v_ : ais) {
			_v_.marshal(_os_);
		}
		_os_.marshal(fightnpctype);
		_os_.marshal(monstertype);
		_os_.marshal(specialtype);
		_os_.compact_uint32(skills.size());
		for (xbean.MonsterSkill _v_ : skills) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(immunebuffs.size());
		for (Integer _v_ : immunebuffs) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(extradata.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : extradata.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		monsterid = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		bodysize = _os_.unmarshal_float();
		hp = _os_.unmarshal_int();
		uplimithp = _os_.unmarshal_int();
		mp = _os_.unmarshal_int();
		sp = _os_.unmarshal_int();
		bfp.unmarshal(_os_);
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
		buffagent.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.BattleAI _v_ = new BattleAI(0, this, "ais");
			_v_.unmarshal(_os_);
			ais.add(_v_);
		}
		fightnpctype = _os_.unmarshal_int();
		monstertype = _os_.unmarshal_int();
		specialtype = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.MonsterSkill _v_ = new MonsterSkill(0, this, "skills");
			_v_.unmarshal(_os_);
			skills.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			immunebuffs.add(_v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				extradata = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				extradata.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.Monster copy() {
		_xdb_verify_unsafe_();
		return new Monster(this);
	}

	@Override
	public xbean.Monster toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Monster toBean() {
		_xdb_verify_unsafe_();
		return new Monster(this); // same as copy()
	}

	@Override
	public xbean.Monster toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Monster toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getMonsterid() { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
		_xdb_verify_unsafe_();
		return monsterid;
	}

	@Override
	public int getShape() { // 造型Id
		_xdb_verify_unsafe_();
		return shape;
	}

	@Override
	public int getSchool() { // 职业
		_xdb_verify_unsafe_();
		return school;
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public float getBodysize() { // 体型大小
		_xdb_verify_unsafe_();
		return bodysize;
	}

	@Override
	public int getHp() { // 血
		_xdb_verify_unsafe_();
		return hp;
	}

	@Override
	public int getUplimithp() { // 当前气血上限
		_xdb_verify_unsafe_();
		return uplimithp;
	}

	@Override
	public int getMp() { // 蓝
		_xdb_verify_unsafe_();
		return mp;
	}

	@Override
	public int getSp() { // 怒
		_xdb_verify_unsafe_();
		return sp;
	}

	@Override
	public xbean.BasicFightProperties getBfp() { // 基础战斗属性
		_xdb_verify_unsafe_();
		return bfp;
	}

	@Override
	public java.util.Map<Integer, Float> getEffects() { // 效果 key = effect type id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "effects"), effects);
	}

	@Override
	public java.util.Map<Integer, Float> getEffectsAsData() { // 效果 key = effect type id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Float> effects;
		Monster _o_ = this;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		return effects;
	}

	@Override
	public java.util.Map<Integer, Float> getFinalattrs() { // 最终属性 key = attr type
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "finalattrs"), finalattrs);
	}

	@Override
	public java.util.Map<Integer, Float> getFinalattrsAsData() { // 最终属性 key = attr type
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Float> finalattrs;
		Monster _o_ = this;
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		return finalattrs;
	}

	@Override
	public xbean.BuffAgent getBuffagent() { // buff代理
		_xdb_verify_unsafe_();
		return buffagent;
	}

	@Override
	public java.util.List<xbean.BattleAI> getAis() { // 怪物ai
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "ais"), ais);
	}

	public java.util.List<xbean.BattleAI> getAisAsData() { // 怪物ai
		_xdb_verify_unsafe_();
		java.util.List<xbean.BattleAI> ais;
		Monster _o_ = this;
		ais = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.ais)
			ais.add(new BattleAI.Data(_v_));
		return ais;
	}

	@Override
	public int getFightnpctype() { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
		_xdb_verify_unsafe_();
		return fightnpctype;
	}

	@Override
	public int getMonstertype() { // 怪物类型,普通,精英,宝宝,boss,公会boss
		_xdb_verify_unsafe_();
		return monstertype;
	}

	@Override
	public int getSpecialtype() { // 1控制,2治疗,3惧控,4惧疗,5隐身
		_xdb_verify_unsafe_();
		return specialtype;
	}

	@Override
	public java.util.List<xbean.MonsterSkill> getSkills() { // value= MonsterSkill
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "skills"), skills);
	}

	public java.util.List<xbean.MonsterSkill> getSkillsAsData() { // value= MonsterSkill
		_xdb_verify_unsafe_();
		java.util.List<xbean.MonsterSkill> skills;
		Monster _o_ = this;
		skills = new java.util.LinkedList<xbean.MonsterSkill>();
		for (xbean.MonsterSkill _v_ : _o_.skills)
			skills.add(new MonsterSkill.Data(_v_));
		return skills;
	}

	@Override
	public java.util.List<Integer> getImmunebuffs() { // 该怪物免疫的buffId
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "immunebuffs"), immunebuffs);
	}

	public java.util.List<Integer> getImmunebuffsAsData() { // 该怪物免疫的buffId
		_xdb_verify_unsafe_();
		java.util.List<Integer> immunebuffs;
		Monster _o_ = this;
		immunebuffs = new java.util.LinkedList<Integer>();
		immunebuffs.addAll(_o_.immunebuffs);
		return immunebuffs;
	}

	@Override
	public java.util.Map<Integer, Integer> getExtradata() { // 记录一些额外数据
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "extradata"), extradata);
	}

	@Override
	public java.util.Map<Integer, Integer> getExtradataAsData() { // 记录一些额外数据
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> extradata;
		Monster _o_ = this;
		extradata = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extradata.entrySet())
			extradata.put(_e_.getKey(), _e_.getValue());
		return extradata;
	}

	@Override
	public void setMonsterid(int _v_) { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "monsterid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, monsterid) {
					public void rollback() { monsterid = _xdb_saved; }
				};}});
		monsterid = _v_;
	}

	@Override
	public void setShape(int _v_) { // 造型Id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shape") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shape) {
					public void rollback() { shape = _xdb_saved; }
				};}});
		shape = _v_;
	}

	@Override
	public void setSchool(int _v_) { // 职业
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "school") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, school) {
					public void rollback() { school = _xdb_saved; }
				};}});
		school = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setBodysize(float _v_) { // 体型大小
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bodysize") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, bodysize) {
					public void rollback() { bodysize = _xdb_saved; }
				};}});
		bodysize = _v_;
	}

	@Override
	public void setHp(int _v_) { // 血
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, hp) {
					public void rollback() { hp = _xdb_saved; }
				};}});
		hp = _v_;
	}

	@Override
	public void setUplimithp(int _v_) { // 当前气血上限
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "uplimithp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, uplimithp) {
					public void rollback() { uplimithp = _xdb_saved; }
				};}});
		uplimithp = _v_;
	}

	@Override
	public void setMp(int _v_) { // 蓝
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, mp) {
					public void rollback() { mp = _xdb_saved; }
				};}});
		mp = _v_;
	}

	@Override
	public void setSp(int _v_) { // 怒
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sp) {
					public void rollback() { sp = _xdb_saved; }
				};}});
		sp = _v_;
	}

	@Override
	public void setFightnpctype(int _v_) { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fightnpctype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fightnpctype) {
					public void rollback() { fightnpctype = _xdb_saved; }
				};}});
		fightnpctype = _v_;
	}

	@Override
	public void setMonstertype(int _v_) { // 怪物类型,普通,精英,宝宝,boss,公会boss
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "monstertype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, monstertype) {
					public void rollback() { monstertype = _xdb_saved; }
				};}});
		monstertype = _v_;
	}

	@Override
	public void setSpecialtype(int _v_) { // 1控制,2治疗,3惧控,4惧疗,5隐身
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "specialtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, specialtype) {
					public void rollback() { specialtype = _xdb_saved; }
				};}});
		specialtype = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Monster _o_ = null;
		if ( _o1_ instanceof Monster ) _o_ = (Monster)_o1_;
		else if ( _o1_ instanceof Monster.Const ) _o_ = ((Monster.Const)_o1_).nThis();
		else return false;
		if (monsterid != _o_.monsterid) return false;
		if (shape != _o_.shape) return false;
		if (school != _o_.school) return false;
		if (level != _o_.level) return false;
		if (bodysize != _o_.bodysize) return false;
		if (hp != _o_.hp) return false;
		if (uplimithp != _o_.uplimithp) return false;
		if (mp != _o_.mp) return false;
		if (sp != _o_.sp) return false;
		if (!bfp.equals(_o_.bfp)) return false;
		if (!effects.equals(_o_.effects)) return false;
		if (!finalattrs.equals(_o_.finalattrs)) return false;
		if (!buffagent.equals(_o_.buffagent)) return false;
		if (!ais.equals(_o_.ais)) return false;
		if (fightnpctype != _o_.fightnpctype) return false;
		if (monstertype != _o_.monstertype) return false;
		if (specialtype != _o_.specialtype) return false;
		if (!skills.equals(_o_.skills)) return false;
		if (!immunebuffs.equals(_o_.immunebuffs)) return false;
		if (!extradata.equals(_o_.extradata)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += monsterid;
		_h_ += shape;
		_h_ += school;
		_h_ += level;
		_h_ += bodysize;
		_h_ += hp;
		_h_ += uplimithp;
		_h_ += mp;
		_h_ += sp;
		_h_ += bfp.hashCode();
		_h_ += effects.hashCode();
		_h_ += finalattrs.hashCode();
		_h_ += buffagent.hashCode();
		_h_ += ais.hashCode();
		_h_ += fightnpctype;
		_h_ += monstertype;
		_h_ += specialtype;
		_h_ += skills.hashCode();
		_h_ += immunebuffs.hashCode();
		_h_ += extradata.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(monsterid);
		_sb_.append(",");
		_sb_.append(shape);
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(bodysize);
		_sb_.append(",");
		_sb_.append(hp);
		_sb_.append(",");
		_sb_.append(uplimithp);
		_sb_.append(",");
		_sb_.append(mp);
		_sb_.append(",");
		_sb_.append(sp);
		_sb_.append(",");
		_sb_.append(bfp);
		_sb_.append(",");
		_sb_.append(effects);
		_sb_.append(",");
		_sb_.append(finalattrs);
		_sb_.append(",");
		_sb_.append(buffagent);
		_sb_.append(",");
		_sb_.append(ais);
		_sb_.append(",");
		_sb_.append(fightnpctype);
		_sb_.append(",");
		_sb_.append(monstertype);
		_sb_.append(",");
		_sb_.append(specialtype);
		_sb_.append(",");
		_sb_.append(skills);
		_sb_.append(",");
		_sb_.append(immunebuffs);
		_sb_.append(",");
		_sb_.append(extradata);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monsterid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shape"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bodysize"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uplimithp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bfp"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("effects"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("finalattrs"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buffagent"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ais"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fightnpctype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monstertype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("specialtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skills"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("immunebuffs"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("extradata"));
		return lb;
	}

	private class Const implements xbean.Monster {
		Monster nThis() {
			return Monster.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Monster copy() {
			return Monster.this.copy();
		}

		@Override
		public xbean.Monster toData() {
			return Monster.this.toData();
		}

		public xbean.Monster toBean() {
			return Monster.this.toBean();
		}

		@Override
		public xbean.Monster toDataIf() {
			return Monster.this.toDataIf();
		}

		public xbean.Monster toBeanIf() {
			return Monster.this.toBeanIf();
		}

		@Override
		public int getMonsterid() { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
			_xdb_verify_unsafe_();
			return monsterid;
		}

		@Override
		public int getShape() { // 造型Id
			_xdb_verify_unsafe_();
			return shape;
		}

		@Override
		public int getSchool() { // 职业
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public float getBodysize() { // 体型大小
			_xdb_verify_unsafe_();
			return bodysize;
		}

		@Override
		public int getHp() { // 血
			_xdb_verify_unsafe_();
			return hp;
		}

		@Override
		public int getUplimithp() { // 当前气血上限
			_xdb_verify_unsafe_();
			return uplimithp;
		}

		@Override
		public int getMp() { // 蓝
			_xdb_verify_unsafe_();
			return mp;
		}

		@Override
		public int getSp() { // 怒
			_xdb_verify_unsafe_();
			return sp;
		}

		@Override
		public xbean.BasicFightProperties getBfp() { // 基础战斗属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(bfp);
		}

		@Override
		public java.util.Map<Integer, Float> getEffects() { // 效果 key = effect type id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(effects);
		}

		@Override
		public java.util.Map<Integer, Float> getEffectsAsData() { // 效果 key = effect type id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Float> effects;
			Monster _o_ = Monster.this;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrs() { // 最终属性 key = attr type
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(finalattrs);
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrsAsData() { // 最终属性 key = attr type
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Float> finalattrs;
			Monster _o_ = Monster.this;
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			return finalattrs;
		}

		@Override
		public xbean.BuffAgent getBuffagent() { // buff代理
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(buffagent);
		}

		@Override
		public java.util.List<xbean.BattleAI> getAis() { // 怪物ai
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(ais);
		}

		public java.util.List<xbean.BattleAI> getAisAsData() { // 怪物ai
			_xdb_verify_unsafe_();
			java.util.List<xbean.BattleAI> ais;
			Monster _o_ = Monster.this;
		ais = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.ais)
			ais.add(new BattleAI.Data(_v_));
			return ais;
		}

		@Override
		public int getFightnpctype() { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
			_xdb_verify_unsafe_();
			return fightnpctype;
		}

		@Override
		public int getMonstertype() { // 怪物类型,普通,精英,宝宝,boss,公会boss
			_xdb_verify_unsafe_();
			return monstertype;
		}

		@Override
		public int getSpecialtype() { // 1控制,2治疗,3惧控,4惧疗,5隐身
			_xdb_verify_unsafe_();
			return specialtype;
		}

		@Override
		public java.util.List<xbean.MonsterSkill> getSkills() { // value= MonsterSkill
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(skills);
		}

		public java.util.List<xbean.MonsterSkill> getSkillsAsData() { // value= MonsterSkill
			_xdb_verify_unsafe_();
			java.util.List<xbean.MonsterSkill> skills;
			Monster _o_ = Monster.this;
		skills = new java.util.LinkedList<xbean.MonsterSkill>();
		for (xbean.MonsterSkill _v_ : _o_.skills)
			skills.add(new MonsterSkill.Data(_v_));
			return skills;
		}

		@Override
		public java.util.List<Integer> getImmunebuffs() { // 该怪物免疫的buffId
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(immunebuffs);
		}

		public java.util.List<Integer> getImmunebuffsAsData() { // 该怪物免疫的buffId
			_xdb_verify_unsafe_();
			java.util.List<Integer> immunebuffs;
			Monster _o_ = Monster.this;
		immunebuffs = new java.util.LinkedList<Integer>();
		immunebuffs.addAll(_o_.immunebuffs);
			return immunebuffs;
		}

		@Override
		public java.util.Map<Integer, Integer> getExtradata() { // 记录一些额外数据
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(extradata);
		}

		@Override
		public java.util.Map<Integer, Integer> getExtradataAsData() { // 记录一些额外数据
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> extradata;
			Monster _o_ = Monster.this;
			extradata = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extradata.entrySet())
				extradata.put(_e_.getKey(), _e_.getValue());
			return extradata;
		}

		@Override
		public void setMonsterid(int _v_) { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShape(int _v_) { // 造型Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 职业
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBodysize(float _v_) { // 体型大小
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHp(int _v_) { // 血
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUplimithp(int _v_) { // 当前气血上限
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMp(int _v_) { // 蓝
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSp(int _v_) { // 怒
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFightnpctype(int _v_) { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMonstertype(int _v_) { // 怪物类型,普通,精英,宝宝,boss,公会boss
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSpecialtype(int _v_) { // 1控制,2治疗,3惧控,4惧疗,5隐身
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
			return Monster.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Monster.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Monster.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Monster.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Monster.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Monster.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Monster.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Monster.this.hashCode();
		}

		@Override
		public String toString() {
			return Monster.this.toString();
		}

	}

	public static final class Data implements xbean.Monster {
		private int monsterid; // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
		private int shape; // 造型Id
		private int school; // 职业
		private int level; // 等级
		private float bodysize; // 体型大小
		private int hp; // 血
		private int uplimithp; // 当前气血上限
		private int mp; // 蓝
		private int sp; // 怒
		private xbean.BasicFightProperties bfp; // 基础战斗属性
		private java.util.HashMap<Integer, Float> effects; // 效果 key = effect type id
		private java.util.HashMap<Integer, Float> finalattrs; // 最终属性 key = attr type
		private xbean.BuffAgent buffagent; // buff代理
		private java.util.LinkedList<xbean.BattleAI> ais; // 怪物ai
		private int fightnpctype; // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
		private int monstertype; // 怪物类型,普通,精英,宝宝,boss,公会boss
		private int specialtype; // 1控制,2治疗,3惧控,4惧疗,5隐身
		private java.util.LinkedList<xbean.MonsterSkill> skills; // value= MonsterSkill
		private java.util.LinkedList<Integer> immunebuffs; // 该怪物免疫的buffId
		private java.util.HashMap<Integer, Integer> extradata; // 记录一些额外数据

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			level = 1;
			bodysize = 1;
			bfp = new BasicFightProperties.Data();
			effects = new java.util.HashMap<Integer, Float>();
			finalattrs = new java.util.HashMap<Integer, Float>();
			buffagent = new BuffAgent.Data();
			ais = new java.util.LinkedList<xbean.BattleAI>();
			skills = new java.util.LinkedList<xbean.MonsterSkill>();
			immunebuffs = new java.util.LinkedList<Integer>();
			extradata = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.Monster _o1_) {
			if (_o1_ instanceof Monster) assign((Monster)_o1_);
			else if (_o1_ instanceof Monster.Data) assign((Monster.Data)_o1_);
			else if (_o1_ instanceof Monster.Const) assign(((Monster.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Monster _o_) {
			monsterid = _o_.monsterid;
			shape = _o_.shape;
			school = _o_.school;
			level = _o_.level;
			bodysize = _o_.bodysize;
			hp = _o_.hp;
			uplimithp = _o_.uplimithp;
			mp = _o_.mp;
			sp = _o_.sp;
			bfp = new BasicFightProperties.Data(_o_.bfp);
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			buffagent = new BuffAgent.Data(_o_.buffagent);
			ais = new java.util.LinkedList<xbean.BattleAI>();
			for (xbean.BattleAI _v_ : _o_.ais)
				ais.add(new BattleAI.Data(_v_));
			fightnpctype = _o_.fightnpctype;
			monstertype = _o_.monstertype;
			specialtype = _o_.specialtype;
			skills = new java.util.LinkedList<xbean.MonsterSkill>();
			for (xbean.MonsterSkill _v_ : _o_.skills)
				skills.add(new MonsterSkill.Data(_v_));
			immunebuffs = new java.util.LinkedList<Integer>();
			immunebuffs.addAll(_o_.immunebuffs);
			extradata = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extradata.entrySet())
				extradata.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(Monster.Data _o_) {
			monsterid = _o_.monsterid;
			shape = _o_.shape;
			school = _o_.school;
			level = _o_.level;
			bodysize = _o_.bodysize;
			hp = _o_.hp;
			uplimithp = _o_.uplimithp;
			mp = _o_.mp;
			sp = _o_.sp;
			bfp = new BasicFightProperties.Data(_o_.bfp);
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			buffagent = new BuffAgent.Data(_o_.buffagent);
			ais = new java.util.LinkedList<xbean.BattleAI>();
			for (xbean.BattleAI _v_ : _o_.ais)
				ais.add(new BattleAI.Data(_v_));
			fightnpctype = _o_.fightnpctype;
			monstertype = _o_.monstertype;
			specialtype = _o_.specialtype;
			skills = new java.util.LinkedList<xbean.MonsterSkill>();
			for (xbean.MonsterSkill _v_ : _o_.skills)
				skills.add(new MonsterSkill.Data(_v_));
			immunebuffs = new java.util.LinkedList<Integer>();
			immunebuffs.addAll(_o_.immunebuffs);
			extradata = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.extradata.entrySet())
				extradata.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(monsterid);
			_os_.marshal(shape);
			_os_.marshal(school);
			_os_.marshal(level);
			_os_.marshal(bodysize);
			_os_.marshal(hp);
			_os_.marshal(uplimithp);
			_os_.marshal(mp);
			_os_.marshal(sp);
			bfp.marshal(_os_);
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
			buffagent.marshal(_os_);
			_os_.compact_uint32(ais.size());
			for (xbean.BattleAI _v_ : ais) {
				_v_.marshal(_os_);
			}
			_os_.marshal(fightnpctype);
			_os_.marshal(monstertype);
			_os_.marshal(specialtype);
			_os_.compact_uint32(skills.size());
			for (xbean.MonsterSkill _v_ : skills) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(immunebuffs.size());
			for (Integer _v_ : immunebuffs) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(extradata.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : extradata.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			monsterid = _os_.unmarshal_int();
			shape = _os_.unmarshal_int();
			school = _os_.unmarshal_int();
			level = _os_.unmarshal_int();
			bodysize = _os_.unmarshal_float();
			hp = _os_.unmarshal_int();
			uplimithp = _os_.unmarshal_int();
			mp = _os_.unmarshal_int();
			sp = _os_.unmarshal_int();
			bfp.unmarshal(_os_);
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
			buffagent.unmarshal(_os_);
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.BattleAI _v_ = xbean.Pod.newBattleAIData();
				_v_.unmarshal(_os_);
				ais.add(_v_);
			}
			fightnpctype = _os_.unmarshal_int();
			monstertype = _os_.unmarshal_int();
			specialtype = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.MonsterSkill _v_ = xbean.Pod.newMonsterSkillData();
				_v_.unmarshal(_os_);
				skills.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				immunebuffs.add(_v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					extradata = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					extradata.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.Monster copy() {
			return new Data(this);
		}

		@Override
		public xbean.Monster toData() {
			return new Data(this);
		}

		public xbean.Monster toBean() {
			return new Monster(this, null, null);
		}

		@Override
		public xbean.Monster toDataIf() {
			return this;
		}

		public xbean.Monster toBeanIf() {
			return new Monster(this, null, null);
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
		public int getMonsterid() { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
			return monsterid;
		}

		@Override
		public int getShape() { // 造型Id
			return shape;
		}

		@Override
		public int getSchool() { // 职业
			return school;
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public float getBodysize() { // 体型大小
			return bodysize;
		}

		@Override
		public int getHp() { // 血
			return hp;
		}

		@Override
		public int getUplimithp() { // 当前气血上限
			return uplimithp;
		}

		@Override
		public int getMp() { // 蓝
			return mp;
		}

		@Override
		public int getSp() { // 怒
			return sp;
		}

		@Override
		public xbean.BasicFightProperties getBfp() { // 基础战斗属性
			return bfp;
		}

		@Override
		public java.util.Map<Integer, Float> getEffects() { // 效果 key = effect type id
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getEffectsAsData() { // 效果 key = effect type id
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrs() { // 最终属性 key = attr type
			return finalattrs;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrsAsData() { // 最终属性 key = attr type
			return finalattrs;
		}

		@Override
		public xbean.BuffAgent getBuffagent() { // buff代理
			return buffagent;
		}

		@Override
		public java.util.List<xbean.BattleAI> getAis() { // 怪物ai
			return ais;
		}

		@Override
		public java.util.List<xbean.BattleAI> getAisAsData() { // 怪物ai
			return ais;
		}

		@Override
		public int getFightnpctype() { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
			return fightnpctype;
		}

		@Override
		public int getMonstertype() { // 怪物类型,普通,精英,宝宝,boss,公会boss
			return monstertype;
		}

		@Override
		public int getSpecialtype() { // 1控制,2治疗,3惧控,4惧疗,5隐身
			return specialtype;
		}

		@Override
		public java.util.List<xbean.MonsterSkill> getSkills() { // value= MonsterSkill
			return skills;
		}

		@Override
		public java.util.List<xbean.MonsterSkill> getSkillsAsData() { // value= MonsterSkill
			return skills;
		}

		@Override
		public java.util.List<Integer> getImmunebuffs() { // 该怪物免疫的buffId
			return immunebuffs;
		}

		@Override
		public java.util.List<Integer> getImmunebuffsAsData() { // 该怪物免疫的buffId
			return immunebuffs;
		}

		@Override
		public java.util.Map<Integer, Integer> getExtradata() { // 记录一些额外数据
			return extradata;
		}

		@Override
		public java.util.Map<Integer, Integer> getExtradataAsData() { // 记录一些额外数据
			return extradata;
		}

		@Override
		public void setMonsterid(int _v_) { // 怪物Id，此Id为在战斗NPC表中配的Id，为monster的baseId
			monsterid = _v_;
		}

		@Override
		public void setShape(int _v_) { // 造型Id
			shape = _v_;
		}

		@Override
		public void setSchool(int _v_) { // 职业
			school = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setBodysize(float _v_) { // 体型大小
			bodysize = _v_;
		}

		@Override
		public void setHp(int _v_) { // 血
			hp = _v_;
		}

		@Override
		public void setUplimithp(int _v_) { // 当前气血上限
			uplimithp = _v_;
		}

		@Override
		public void setMp(int _v_) { // 蓝
			mp = _v_;
		}

		@Override
		public void setSp(int _v_) { // 怒
			sp = _v_;
		}

		@Override
		public void setFightnpctype(int _v_) { // 战斗单位类型,主角（玩家控制）,宠物,伙伴（玩家自带助战）,暗雷野怪（20000~23999）,战斗npc（24000以上）,系统安排的助战
			fightnpctype = _v_;
		}

		@Override
		public void setMonstertype(int _v_) { // 怪物类型,普通,精英,宝宝,boss,公会boss
			monstertype = _v_;
		}

		@Override
		public void setSpecialtype(int _v_) { // 1控制,2治疗,3惧控,4惧疗,5隐身
			specialtype = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Monster.Data)) return false;
			Monster.Data _o_ = (Monster.Data) _o1_;
			if (monsterid != _o_.monsterid) return false;
			if (shape != _o_.shape) return false;
			if (school != _o_.school) return false;
			if (level != _o_.level) return false;
			if (bodysize != _o_.bodysize) return false;
			if (hp != _o_.hp) return false;
			if (uplimithp != _o_.uplimithp) return false;
			if (mp != _o_.mp) return false;
			if (sp != _o_.sp) return false;
			if (!bfp.equals(_o_.bfp)) return false;
			if (!effects.equals(_o_.effects)) return false;
			if (!finalattrs.equals(_o_.finalattrs)) return false;
			if (!buffagent.equals(_o_.buffagent)) return false;
			if (!ais.equals(_o_.ais)) return false;
			if (fightnpctype != _o_.fightnpctype) return false;
			if (monstertype != _o_.monstertype) return false;
			if (specialtype != _o_.specialtype) return false;
			if (!skills.equals(_o_.skills)) return false;
			if (!immunebuffs.equals(_o_.immunebuffs)) return false;
			if (!extradata.equals(_o_.extradata)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += monsterid;
			_h_ += shape;
			_h_ += school;
			_h_ += level;
			_h_ += bodysize;
			_h_ += hp;
			_h_ += uplimithp;
			_h_ += mp;
			_h_ += sp;
			_h_ += bfp.hashCode();
			_h_ += effects.hashCode();
			_h_ += finalattrs.hashCode();
			_h_ += buffagent.hashCode();
			_h_ += ais.hashCode();
			_h_ += fightnpctype;
			_h_ += monstertype;
			_h_ += specialtype;
			_h_ += skills.hashCode();
			_h_ += immunebuffs.hashCode();
			_h_ += extradata.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(monsterid);
			_sb_.append(",");
			_sb_.append(shape);
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(bodysize);
			_sb_.append(",");
			_sb_.append(hp);
			_sb_.append(",");
			_sb_.append(uplimithp);
			_sb_.append(",");
			_sb_.append(mp);
			_sb_.append(",");
			_sb_.append(sp);
			_sb_.append(",");
			_sb_.append(bfp);
			_sb_.append(",");
			_sb_.append(effects);
			_sb_.append(",");
			_sb_.append(finalattrs);
			_sb_.append(",");
			_sb_.append(buffagent);
			_sb_.append(",");
			_sb_.append(ais);
			_sb_.append(",");
			_sb_.append(fightnpctype);
			_sb_.append(",");
			_sb_.append(monstertype);
			_sb_.append(",");
			_sb_.append(specialtype);
			_sb_.append(",");
			_sb_.append(skills);
			_sb_.append(",");
			_sb_.append(immunebuffs);
			_sb_.append(",");
			_sb_.append(extradata);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

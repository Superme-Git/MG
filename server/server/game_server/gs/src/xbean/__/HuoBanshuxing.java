
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HuoBanshuxing extends mkdb.XBean implements xbean.HuoBanshuxing {
	private String huobanname; // 名字
	private int level; // 等级
	private int school; // 职业
	private int shape; // 造型Id
	private float bodytype; // 大小
	private java.util.HashMap<Integer, Float> effects; // key = effect type id
	private java.util.HashMap<Integer, Float> finalattrs; // key = attr type
	private java.util.ArrayList<Integer> skills; // huoban拥有的战斗技能
	private int skillmaster; // 精通技能
	private java.util.ArrayList<Integer> battleai; // huoban拥有的战斗ai
	private float levelfactor; // 技能等级系数
	private int levelconstant; // 技能等级常数

	@Override
	public void _reset_unsafe_() {
		huobanname = "";
		level = 0;
		school = 0;
		shape = 0;
		bodytype = 0.0f;
		effects.clear();
		finalattrs.clear();
		skills.clear();
		skillmaster = 0;
		battleai.clear();
		levelfactor = 0.0f;
		levelconstant = 0;
	}

	HuoBanshuxing(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		huobanname = "";
		effects = new java.util.HashMap<Integer, Float>();
		finalattrs = new java.util.HashMap<Integer, Float>();
		skills = new java.util.ArrayList<Integer>();
		battleai = new java.util.ArrayList<Integer>();
	}

	public HuoBanshuxing() {
		this(0, null, null);
	}

	public HuoBanshuxing(HuoBanshuxing _o_) {
		this(_o_, null, null);
	}

	HuoBanshuxing(xbean.HuoBanshuxing _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HuoBanshuxing) assign((HuoBanshuxing)_o1_);
		else if (_o1_ instanceof HuoBanshuxing.Data) assign((HuoBanshuxing.Data)_o1_);
		else if (_o1_ instanceof HuoBanshuxing.Const) assign(((HuoBanshuxing.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HuoBanshuxing _o_) {
		_o_._xdb_verify_unsafe_();
		huobanname = _o_.huobanname;
		level = _o_.level;
		school = _o_.school;
		shape = _o_.shape;
		bodytype = _o_.bodytype;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		skills = new java.util.ArrayList<Integer>();
		skills.addAll(_o_.skills);
		skillmaster = _o_.skillmaster;
		battleai = new java.util.ArrayList<Integer>();
		battleai.addAll(_o_.battleai);
		levelfactor = _o_.levelfactor;
		levelconstant = _o_.levelconstant;
	}

	private void assign(HuoBanshuxing.Data _o_) {
		huobanname = _o_.huobanname;
		level = _o_.level;
		school = _o_.school;
		shape = _o_.shape;
		bodytype = _o_.bodytype;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		skills = new java.util.ArrayList<Integer>();
		skills.addAll(_o_.skills);
		skillmaster = _o_.skillmaster;
		battleai = new java.util.ArrayList<Integer>();
		battleai.addAll(_o_.battleai);
		levelfactor = _o_.levelfactor;
		levelconstant = _o_.levelconstant;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(huobanname, mkdb.Const.IO_CHARSET);
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(shape);
		_os_.marshal(bodytype);
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
		_os_.compact_uint32(skills.size());
		for (Integer _v_ : skills) {
			_os_.marshal(_v_);
		}
		_os_.marshal(skillmaster);
		_os_.compact_uint32(battleai.size());
		for (Integer _v_ : battleai) {
			_os_.marshal(_v_);
		}
		_os_.marshal(levelfactor);
		_os_.marshal(levelconstant);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		huobanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		bodytype = _os_.unmarshal_float();
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
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			skills.add(_v_);
		}
		skillmaster = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			battleai.add(_v_);
		}
		levelfactor = _os_.unmarshal_float();
		levelconstant = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.HuoBanshuxing copy() {
		_xdb_verify_unsafe_();
		return new HuoBanshuxing(this);
	}

	@Override
	public xbean.HuoBanshuxing toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanshuxing toBean() {
		_xdb_verify_unsafe_();
		return new HuoBanshuxing(this); // same as copy()
	}

	@Override
	public xbean.HuoBanshuxing toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanshuxing toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getHuobanname() { // 名字
		_xdb_verify_unsafe_();
		return huobanname;
	}

	@Override
	public com.locojoy.base.Octets getHuobannameOctets() { // 名字
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getHuobanname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getSchool() { // 职业
		_xdb_verify_unsafe_();
		return school;
	}

	@Override
	public int getShape() { // 造型Id
		_xdb_verify_unsafe_();
		return shape;
	}

	@Override
	public float getBodytype() { // 大小
		_xdb_verify_unsafe_();
		return bodytype;
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
		HuoBanshuxing _o_ = this;
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
		HuoBanshuxing _o_ = this;
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		return finalattrs;
	}

	@Override
	public java.util.List<Integer> getSkills() { // huoban拥有的战斗技能
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "skills"), skills);
	}

	public java.util.List<Integer> getSkillsAsData() { // huoban拥有的战斗技能
		_xdb_verify_unsafe_();
		java.util.List<Integer> skills;
		HuoBanshuxing _o_ = this;
		skills = new java.util.ArrayList<Integer>();
		skills.addAll(_o_.skills);
		return skills;
	}

	@Override
	public int getSkillmaster() { // 精通技能
		_xdb_verify_unsafe_();
		return skillmaster;
	}

	@Override
	public java.util.List<Integer> getBattleai() { // huoban拥有的战斗ai
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "battleai"), battleai);
	}

	public java.util.List<Integer> getBattleaiAsData() { // huoban拥有的战斗ai
		_xdb_verify_unsafe_();
		java.util.List<Integer> battleai;
		HuoBanshuxing _o_ = this;
		battleai = new java.util.ArrayList<Integer>();
		battleai.addAll(_o_.battleai);
		return battleai;
	}

	@Override
	public float getLevelfactor() { // 技能等级系数
		_xdb_verify_unsafe_();
		return levelfactor;
	}

	@Override
	public int getLevelconstant() { // 技能等级常数
		_xdb_verify_unsafe_();
		return levelconstant;
	}

	@Override
	public void setHuobanname(String _v_) { // 名字
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "huobanname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, huobanname) {
					public void rollback() { huobanname = _xdb_saved; }
				};}});
		huobanname = _v_;
	}

	@Override
	public void setHuobannameOctets(com.locojoy.base.Octets _v_) { // 名字
		_xdb_verify_unsafe_();
		this.setHuobanname(_v_.getString(mkdb.Const.IO_CHARSET));
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
	public void setBodytype(float _v_) { // 大小
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bodytype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, bodytype) {
					public void rollback() { bodytype = _xdb_saved; }
				};}});
		bodytype = _v_;
	}

	@Override
	public void setSkillmaster(int _v_) { // 精通技能
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skillmaster") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skillmaster) {
					public void rollback() { skillmaster = _xdb_saved; }
				};}});
		skillmaster = _v_;
	}

	@Override
	public void setLevelfactor(float _v_) { // 技能等级系数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "levelfactor") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, levelfactor) {
					public void rollback() { levelfactor = _xdb_saved; }
				};}});
		levelfactor = _v_;
	}

	@Override
	public void setLevelconstant(int _v_) { // 技能等级常数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "levelconstant") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, levelconstant) {
					public void rollback() { levelconstant = _xdb_saved; }
				};}});
		levelconstant = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HuoBanshuxing _o_ = null;
		if ( _o1_ instanceof HuoBanshuxing ) _o_ = (HuoBanshuxing)_o1_;
		else if ( _o1_ instanceof HuoBanshuxing.Const ) _o_ = ((HuoBanshuxing.Const)_o1_).nThis();
		else return false;
		if (!huobanname.equals(_o_.huobanname)) return false;
		if (level != _o_.level) return false;
		if (school != _o_.school) return false;
		if (shape != _o_.shape) return false;
		if (bodytype != _o_.bodytype) return false;
		if (!effects.equals(_o_.effects)) return false;
		if (!finalattrs.equals(_o_.finalattrs)) return false;
		if (!skills.equals(_o_.skills)) return false;
		if (skillmaster != _o_.skillmaster) return false;
		if (!battleai.equals(_o_.battleai)) return false;
		if (levelfactor != _o_.levelfactor) return false;
		if (levelconstant != _o_.levelconstant) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += huobanname.hashCode();
		_h_ += level;
		_h_ += school;
		_h_ += shape;
		_h_ += bodytype;
		_h_ += effects.hashCode();
		_h_ += finalattrs.hashCode();
		_h_ += skills.hashCode();
		_h_ += skillmaster;
		_h_ += battleai.hashCode();
		_h_ += levelfactor;
		_h_ += levelconstant;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(huobanname).append("'");
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(",");
		_sb_.append(shape);
		_sb_.append(",");
		_sb_.append(bodytype);
		_sb_.append(",");
		_sb_.append(effects);
		_sb_.append(",");
		_sb_.append(finalattrs);
		_sb_.append(",");
		_sb_.append(skills);
		_sb_.append(",");
		_sb_.append(skillmaster);
		_sb_.append(",");
		_sb_.append(battleai);
		_sb_.append(",");
		_sb_.append(levelfactor);
		_sb_.append(",");
		_sb_.append(levelconstant);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("huobanname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shape"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bodytype"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("effects"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("finalattrs"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skills"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skillmaster"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleai"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("levelfactor"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("levelconstant"));
		return lb;
	}

	private class Const implements xbean.HuoBanshuxing {
		HuoBanshuxing nThis() {
			return HuoBanshuxing.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HuoBanshuxing copy() {
			return HuoBanshuxing.this.copy();
		}

		@Override
		public xbean.HuoBanshuxing toData() {
			return HuoBanshuxing.this.toData();
		}

		public xbean.HuoBanshuxing toBean() {
			return HuoBanshuxing.this.toBean();
		}

		@Override
		public xbean.HuoBanshuxing toDataIf() {
			return HuoBanshuxing.this.toDataIf();
		}

		public xbean.HuoBanshuxing toBeanIf() {
			return HuoBanshuxing.this.toBeanIf();
		}

		@Override
		public String getHuobanname() { // 名字
			_xdb_verify_unsafe_();
			return huobanname;
		}

		@Override
		public com.locojoy.base.Octets getHuobannameOctets() { // 名字
			_xdb_verify_unsafe_();
			return HuoBanshuxing.this.getHuobannameOctets();
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getSchool() { // 职业
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public int getShape() { // 造型Id
			_xdb_verify_unsafe_();
			return shape;
		}

		@Override
		public float getBodytype() { // 大小
			_xdb_verify_unsafe_();
			return bodytype;
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
			HuoBanshuxing _o_ = HuoBanshuxing.this;
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
			HuoBanshuxing _o_ = HuoBanshuxing.this;
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			return finalattrs;
		}

		@Override
		public java.util.List<Integer> getSkills() { // huoban拥有的战斗技能
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(skills);
		}

		public java.util.List<Integer> getSkillsAsData() { // huoban拥有的战斗技能
			_xdb_verify_unsafe_();
			java.util.List<Integer> skills;
			HuoBanshuxing _o_ = HuoBanshuxing.this;
		skills = new java.util.ArrayList<Integer>();
		skills.addAll(_o_.skills);
			return skills;
		}

		@Override
		public int getSkillmaster() { // 精通技能
			_xdb_verify_unsafe_();
			return skillmaster;
		}

		@Override
		public java.util.List<Integer> getBattleai() { // huoban拥有的战斗ai
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(battleai);
		}

		public java.util.List<Integer> getBattleaiAsData() { // huoban拥有的战斗ai
			_xdb_verify_unsafe_();
			java.util.List<Integer> battleai;
			HuoBanshuxing _o_ = HuoBanshuxing.this;
		battleai = new java.util.ArrayList<Integer>();
		battleai.addAll(_o_.battleai);
			return battleai;
		}

		@Override
		public float getLevelfactor() { // 技能等级系数
			_xdb_verify_unsafe_();
			return levelfactor;
		}

		@Override
		public int getLevelconstant() { // 技能等级常数
			_xdb_verify_unsafe_();
			return levelconstant;
		}

		@Override
		public void setHuobanname(String _v_) { // 名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHuobannameOctets(com.locojoy.base.Octets _v_) { // 名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 职业
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShape(int _v_) { // 造型Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBodytype(float _v_) { // 大小
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSkillmaster(int _v_) { // 精通技能
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevelfactor(float _v_) { // 技能等级系数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevelconstant(int _v_) { // 技能等级常数
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
			return HuoBanshuxing.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HuoBanshuxing.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HuoBanshuxing.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HuoBanshuxing.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HuoBanshuxing.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HuoBanshuxing.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HuoBanshuxing.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HuoBanshuxing.this.hashCode();
		}

		@Override
		public String toString() {
			return HuoBanshuxing.this.toString();
		}

	}

	public static final class Data implements xbean.HuoBanshuxing {
		private String huobanname; // 名字
		private int level; // 等级
		private int school; // 职业
		private int shape; // 造型Id
		private float bodytype; // 大小
		private java.util.HashMap<Integer, Float> effects; // key = effect type id
		private java.util.HashMap<Integer, Float> finalattrs; // key = attr type
		private java.util.ArrayList<Integer> skills; // huoban拥有的战斗技能
		private int skillmaster; // 精通技能
		private java.util.ArrayList<Integer> battleai; // huoban拥有的战斗ai
		private float levelfactor; // 技能等级系数
		private int levelconstant; // 技能等级常数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			huobanname = "";
			effects = new java.util.HashMap<Integer, Float>();
			finalattrs = new java.util.HashMap<Integer, Float>();
			skills = new java.util.ArrayList<Integer>();
			battleai = new java.util.ArrayList<Integer>();
		}

		Data(xbean.HuoBanshuxing _o1_) {
			if (_o1_ instanceof HuoBanshuxing) assign((HuoBanshuxing)_o1_);
			else if (_o1_ instanceof HuoBanshuxing.Data) assign((HuoBanshuxing.Data)_o1_);
			else if (_o1_ instanceof HuoBanshuxing.Const) assign(((HuoBanshuxing.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HuoBanshuxing _o_) {
			huobanname = _o_.huobanname;
			level = _o_.level;
			school = _o_.school;
			shape = _o_.shape;
			bodytype = _o_.bodytype;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			skills = new java.util.ArrayList<Integer>();
			skills.addAll(_o_.skills);
			skillmaster = _o_.skillmaster;
			battleai = new java.util.ArrayList<Integer>();
			battleai.addAll(_o_.battleai);
			levelfactor = _o_.levelfactor;
			levelconstant = _o_.levelconstant;
		}

		private void assign(HuoBanshuxing.Data _o_) {
			huobanname = _o_.huobanname;
			level = _o_.level;
			school = _o_.school;
			shape = _o_.shape;
			bodytype = _o_.bodytype;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			skills = new java.util.ArrayList<Integer>();
			skills.addAll(_o_.skills);
			skillmaster = _o_.skillmaster;
			battleai = new java.util.ArrayList<Integer>();
			battleai.addAll(_o_.battleai);
			levelfactor = _o_.levelfactor;
			levelconstant = _o_.levelconstant;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(huobanname, mkdb.Const.IO_CHARSET);
			_os_.marshal(level);
			_os_.marshal(school);
			_os_.marshal(shape);
			_os_.marshal(bodytype);
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
			_os_.compact_uint32(skills.size());
			for (Integer _v_ : skills) {
				_os_.marshal(_v_);
			}
			_os_.marshal(skillmaster);
			_os_.compact_uint32(battleai.size());
			for (Integer _v_ : battleai) {
				_os_.marshal(_v_);
			}
			_os_.marshal(levelfactor);
			_os_.marshal(levelconstant);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			huobanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			level = _os_.unmarshal_int();
			school = _os_.unmarshal_int();
			shape = _os_.unmarshal_int();
			bodytype = _os_.unmarshal_float();
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
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				skills.add(_v_);
			}
			skillmaster = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				battleai.add(_v_);
			}
			levelfactor = _os_.unmarshal_float();
			levelconstant = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.HuoBanshuxing copy() {
			return new Data(this);
		}

		@Override
		public xbean.HuoBanshuxing toData() {
			return new Data(this);
		}

		public xbean.HuoBanshuxing toBean() {
			return new HuoBanshuxing(this, null, null);
		}

		@Override
		public xbean.HuoBanshuxing toDataIf() {
			return this;
		}

		public xbean.HuoBanshuxing toBeanIf() {
			return new HuoBanshuxing(this, null, null);
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
		public String getHuobanname() { // 名字
			return huobanname;
		}

		@Override
		public com.locojoy.base.Octets getHuobannameOctets() { // 名字
			return com.locojoy.base.Octets.wrap(getHuobanname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public int getSchool() { // 职业
			return school;
		}

		@Override
		public int getShape() { // 造型Id
			return shape;
		}

		@Override
		public float getBodytype() { // 大小
			return bodytype;
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
		public java.util.List<Integer> getSkills() { // huoban拥有的战斗技能
			return skills;
		}

		@Override
		public java.util.List<Integer> getSkillsAsData() { // huoban拥有的战斗技能
			return skills;
		}

		@Override
		public int getSkillmaster() { // 精通技能
			return skillmaster;
		}

		@Override
		public java.util.List<Integer> getBattleai() { // huoban拥有的战斗ai
			return battleai;
		}

		@Override
		public java.util.List<Integer> getBattleaiAsData() { // huoban拥有的战斗ai
			return battleai;
		}

		@Override
		public float getLevelfactor() { // 技能等级系数
			return levelfactor;
		}

		@Override
		public int getLevelconstant() { // 技能等级常数
			return levelconstant;
		}

		@Override
		public void setHuobanname(String _v_) { // 名字
			if (null == _v_)
				throw new NullPointerException();
			huobanname = _v_;
		}

		@Override
		public void setHuobannameOctets(com.locojoy.base.Octets _v_) { // 名字
			this.setHuobanname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setSchool(int _v_) { // 职业
			school = _v_;
		}

		@Override
		public void setShape(int _v_) { // 造型Id
			shape = _v_;
		}

		@Override
		public void setBodytype(float _v_) { // 大小
			bodytype = _v_;
		}

		@Override
		public void setSkillmaster(int _v_) { // 精通技能
			skillmaster = _v_;
		}

		@Override
		public void setLevelfactor(float _v_) { // 技能等级系数
			levelfactor = _v_;
		}

		@Override
		public void setLevelconstant(int _v_) { // 技能等级常数
			levelconstant = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HuoBanshuxing.Data)) return false;
			HuoBanshuxing.Data _o_ = (HuoBanshuxing.Data) _o1_;
			if (!huobanname.equals(_o_.huobanname)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (shape != _o_.shape) return false;
			if (bodytype != _o_.bodytype) return false;
			if (!effects.equals(_o_.effects)) return false;
			if (!finalattrs.equals(_o_.finalattrs)) return false;
			if (!skills.equals(_o_.skills)) return false;
			if (skillmaster != _o_.skillmaster) return false;
			if (!battleai.equals(_o_.battleai)) return false;
			if (levelfactor != _o_.levelfactor) return false;
			if (levelconstant != _o_.levelconstant) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += huobanname.hashCode();
			_h_ += level;
			_h_ += school;
			_h_ += shape;
			_h_ += bodytype;
			_h_ += effects.hashCode();
			_h_ += finalattrs.hashCode();
			_h_ += skills.hashCode();
			_h_ += skillmaster;
			_h_ += battleai.hashCode();
			_h_ += levelfactor;
			_h_ += levelconstant;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(huobanname).append("'");
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(",");
			_sb_.append(shape);
			_sb_.append(",");
			_sb_.append(bodytype);
			_sb_.append(",");
			_sb_.append(effects);
			_sb_.append(",");
			_sb_.append(finalattrs);
			_sb_.append(",");
			_sb_.append(skills);
			_sb_.append(",");
			_sb_.append(skillmaster);
			_sb_.append(",");
			_sb_.append(battleai);
			_sb_.append(",");
			_sb_.append(levelfactor);
			_sb_.append(",");
			_sb_.append(levelconstant);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

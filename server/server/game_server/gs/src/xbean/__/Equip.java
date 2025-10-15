
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Equip extends mkdb.XBean implements xbean.Equip {
	private int equiplevel; // 装备等级
	private int repairtimes; // 修理失败次数
	private int endure; // 耐久
	private int curmaxendure; // 当前最大耐久
	private String producer; // 制造者
	private int extendure; // 当值为100时,减少1
	private int maxendure; // 
	private int skill; // 技能
	private int effect; // 特效
	private int treasure; // 0表示不是珍品;1表示是珍品
	private java.util.HashMap<Integer, Integer> attr; // 基础属性
	private java.util.HashMap<Integer, Integer> addattr; // 附加属性
	private java.util.HashMap<Integer, xbean.EnhancementData> enhancement; // 附魔属性 by changhao
	private int equipscore; // 装备评分
	private java.util.LinkedList<Integer> diamonds; // 宝石列表

	@Override
	public void _reset_unsafe_() {
		equiplevel = 0;
		repairtimes = 0;
		endure = 0;
		curmaxendure = 0;
		producer = "";
		extendure = 0;
		maxendure = 0;
		skill = 0;
		effect = 0;
		treasure = 0;
		attr.clear();
		addattr.clear();
		enhancement.clear();
		equipscore = 0;
		diamonds.clear();
	}

	Equip(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		producer = "";
		extendure = 0;
		skill = 0;
		effect = 0;
		treasure = 0;
		attr = new java.util.HashMap<Integer, Integer>();
		addattr = new java.util.HashMap<Integer, Integer>();
		enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>();
		diamonds = new java.util.LinkedList<Integer>();
	}

	public Equip() {
		this(0, null, null);
	}

	public Equip(Equip _o_) {
		this(_o_, null, null);
	}

	Equip(xbean.Equip _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Equip) assign((Equip)_o1_);
		else if (_o1_ instanceof Equip.Data) assign((Equip.Data)_o1_);
		else if (_o1_ instanceof Equip.Const) assign(((Equip.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Equip _o_) {
		_o_._xdb_verify_unsafe_();
		equiplevel = _o_.equiplevel;
		repairtimes = _o_.repairtimes;
		endure = _o_.endure;
		curmaxendure = _o_.curmaxendure;
		producer = _o_.producer;
		extendure = _o_.extendure;
		maxendure = _o_.maxendure;
		skill = _o_.skill;
		effect = _o_.effect;
		treasure = _o_.treasure;
		attr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
			attr.put(_e_.getKey(), _e_.getValue());
		addattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
			addattr.put(_e_.getKey(), _e_.getValue());
		enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>();
		for (java.util.Map.Entry<Integer, xbean.EnhancementData> _e_ : _o_.enhancement.entrySet())
			enhancement.put(_e_.getKey(), new EnhancementData(_e_.getValue(), this, "enhancement"));
		equipscore = _o_.equipscore;
		diamonds = new java.util.LinkedList<Integer>();
		diamonds.addAll(_o_.diamonds);
	}

	private void assign(Equip.Data _o_) {
		equiplevel = _o_.equiplevel;
		repairtimes = _o_.repairtimes;
		endure = _o_.endure;
		curmaxendure = _o_.curmaxendure;
		producer = _o_.producer;
		extendure = _o_.extendure;
		maxendure = _o_.maxendure;
		skill = _o_.skill;
		effect = _o_.effect;
		treasure = _o_.treasure;
		attr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
			attr.put(_e_.getKey(), _e_.getValue());
		addattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
			addattr.put(_e_.getKey(), _e_.getValue());
		enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>();
		for (java.util.Map.Entry<Integer, xbean.EnhancementData> _e_ : _o_.enhancement.entrySet())
			enhancement.put(_e_.getKey(), new EnhancementData(_e_.getValue(), this, "enhancement"));
		equipscore = _o_.equipscore;
		diamonds = new java.util.LinkedList<Integer>();
		diamonds.addAll(_o_.diamonds);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(equiplevel);
		_os_.marshal(repairtimes);
		_os_.marshal(endure);
		_os_.marshal(curmaxendure);
		_os_.marshal(producer, mkdb.Const.IO_CHARSET);
		_os_.marshal(extendure);
		_os_.marshal(maxendure);
		_os_.marshal(skill);
		_os_.marshal(effect);
		_os_.marshal(treasure);
		_os_.compact_uint32(attr.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : attr.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(addattr.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : addattr.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(enhancement.size());
		for (java.util.Map.Entry<Integer, xbean.EnhancementData> _e_ : enhancement.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(equipscore);
		_os_.compact_uint32(diamonds.size());
		for (Integer _v_ : diamonds) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		equiplevel = _os_.unmarshal_int();
		repairtimes = _os_.unmarshal_int();
		endure = _os_.unmarshal_int();
		curmaxendure = _os_.unmarshal_int();
		producer = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		extendure = _os_.unmarshal_int();
		maxendure = _os_.unmarshal_int();
		skill = _os_.unmarshal_int();
		effect = _os_.unmarshal_int();
		treasure = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				attr = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				attr.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				addattr = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				addattr.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.EnhancementData _v_ = new EnhancementData(0, this, "enhancement");
				_v_.unmarshal(_os_);
				enhancement.put(_k_, _v_);
			}
		}
		equipscore = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			diamonds.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.Equip copy() {
		_xdb_verify_unsafe_();
		return new Equip(this);
	}

	@Override
	public xbean.Equip toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Equip toBean() {
		_xdb_verify_unsafe_();
		return new Equip(this); // same as copy()
	}

	@Override
	public xbean.Equip toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Equip toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getEquiplevel() { // 装备等级
		_xdb_verify_unsafe_();
		return equiplevel;
	}

	@Override
	public int getRepairtimes() { // 修理失败次数
		_xdb_verify_unsafe_();
		return repairtimes;
	}

	@Override
	public int getEndure() { // 耐久
		_xdb_verify_unsafe_();
		return endure;
	}

	@Override
	public int getCurmaxendure() { // 当前最大耐久
		_xdb_verify_unsafe_();
		return curmaxendure;
	}

	@Override
	public String getProducer() { // 制造者
		_xdb_verify_unsafe_();
		return producer;
	}

	@Override
	public com.locojoy.base.Octets getProducerOctets() { // 制造者
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getProducer(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getExtendure() { // 当值为100时,减少1
		_xdb_verify_unsafe_();
		return extendure;
	}

	@Override
	public int getMaxendure() { // 
		_xdb_verify_unsafe_();
		return maxendure;
	}

	@Override
	public int getSkill() { // 技能
		_xdb_verify_unsafe_();
		return skill;
	}

	@Override
	public int getEffect() { // 特效
		_xdb_verify_unsafe_();
		return effect;
	}

	@Override
	public int getTreasure() { // 0表示不是珍品;1表示是珍品
		_xdb_verify_unsafe_();
		return treasure;
	}

	@Override
	public java.util.Map<Integer, Integer> getAttr() { // 基础属性
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "attr"), attr);
	}

	@Override
	public java.util.Map<Integer, Integer> getAttrAsData() { // 基础属性
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> attr;
		Equip _o_ = this;
		attr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
			attr.put(_e_.getKey(), _e_.getValue());
		return attr;
	}

	@Override
	public java.util.Map<Integer, Integer> getAddattr() { // 附加属性
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "addattr"), addattr);
	}

	@Override
	public java.util.Map<Integer, Integer> getAddattrAsData() { // 附加属性
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> addattr;
		Equip _o_ = this;
		addattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
			addattr.put(_e_.getKey(), _e_.getValue());
		return addattr;
	}

	@Override
	public java.util.Map<Integer, xbean.EnhancementData> getEnhancement() { // 附魔属性 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "enhancement"), enhancement);
	}

	@Override
	public java.util.Map<Integer, xbean.EnhancementData> getEnhancementAsData() { // 附魔属性 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.EnhancementData> enhancement;
		Equip _o_ = this;
		enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>();
		for (java.util.Map.Entry<Integer, xbean.EnhancementData> _e_ : _o_.enhancement.entrySet())
			enhancement.put(_e_.getKey(), new EnhancementData.Data(_e_.getValue()));
		return enhancement;
	}

	@Override
	public int getEquipscore() { // 装备评分
		_xdb_verify_unsafe_();
		return equipscore;
	}

	@Override
	public java.util.List<Integer> getDiamonds() { // 宝石列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "diamonds"), diamonds);
	}

	public java.util.List<Integer> getDiamondsAsData() { // 宝石列表
		_xdb_verify_unsafe_();
		java.util.List<Integer> diamonds;
		Equip _o_ = this;
		diamonds = new java.util.LinkedList<Integer>();
		diamonds.addAll(_o_.diamonds);
		return diamonds;
	}

	@Override
	public void setEquiplevel(int _v_) { // 装备等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "equiplevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, equiplevel) {
					public void rollback() { equiplevel = _xdb_saved; }
				};}});
		equiplevel = _v_;
	}

	@Override
	public void setRepairtimes(int _v_) { // 修理失败次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "repairtimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, repairtimes) {
					public void rollback() { repairtimes = _xdb_saved; }
				};}});
		repairtimes = _v_;
	}

	@Override
	public void setEndure(int _v_) { // 耐久
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "endure") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, endure) {
					public void rollback() { endure = _xdb_saved; }
				};}});
		endure = _v_;
	}

	@Override
	public void setCurmaxendure(int _v_) { // 当前最大耐久
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "curmaxendure") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, curmaxendure) {
					public void rollback() { curmaxendure = _xdb_saved; }
				};}});
		curmaxendure = _v_;
	}

	@Override
	public void setProducer(String _v_) { // 制造者
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "producer") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, producer) {
					public void rollback() { producer = _xdb_saved; }
				};}});
		producer = _v_;
	}

	@Override
	public void setProducerOctets(com.locojoy.base.Octets _v_) { // 制造者
		_xdb_verify_unsafe_();
		this.setProducer(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setExtendure(int _v_) { // 当值为100时,减少1
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "extendure") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, extendure) {
					public void rollback() { extendure = _xdb_saved; }
				};}});
		extendure = _v_;
	}

	@Override
	public void setMaxendure(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxendure") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxendure) {
					public void rollback() { maxendure = _xdb_saved; }
				};}});
		maxendure = _v_;
	}

	@Override
	public void setSkill(int _v_) { // 技能
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skill") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skill) {
					public void rollback() { skill = _xdb_saved; }
				};}});
		skill = _v_;
	}

	@Override
	public void setEffect(int _v_) { // 特效
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "effect") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, effect) {
					public void rollback() { effect = _xdb_saved; }
				};}});
		effect = _v_;
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
	public void setEquipscore(int _v_) { // 装备评分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "equipscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, equipscore) {
					public void rollback() { equipscore = _xdb_saved; }
				};}});
		equipscore = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Equip _o_ = null;
		if ( _o1_ instanceof Equip ) _o_ = (Equip)_o1_;
		else if ( _o1_ instanceof Equip.Const ) _o_ = ((Equip.Const)_o1_).nThis();
		else return false;
		if (equiplevel != _o_.equiplevel) return false;
		if (repairtimes != _o_.repairtimes) return false;
		if (endure != _o_.endure) return false;
		if (curmaxendure != _o_.curmaxendure) return false;
		if (!producer.equals(_o_.producer)) return false;
		if (extendure != _o_.extendure) return false;
		if (maxendure != _o_.maxendure) return false;
		if (skill != _o_.skill) return false;
		if (effect != _o_.effect) return false;
		if (treasure != _o_.treasure) return false;
		if (!attr.equals(_o_.attr)) return false;
		if (!addattr.equals(_o_.addattr)) return false;
		if (!enhancement.equals(_o_.enhancement)) return false;
		if (equipscore != _o_.equipscore) return false;
		if (!diamonds.equals(_o_.diamonds)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += equiplevel;
		_h_ += repairtimes;
		_h_ += endure;
		_h_ += curmaxendure;
		_h_ += producer.hashCode();
		_h_ += extendure;
		_h_ += maxendure;
		_h_ += skill;
		_h_ += effect;
		_h_ += treasure;
		_h_ += attr.hashCode();
		_h_ += addattr.hashCode();
		_h_ += enhancement.hashCode();
		_h_ += equipscore;
		_h_ += diamonds.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(equiplevel);
		_sb_.append(",");
		_sb_.append(repairtimes);
		_sb_.append(",");
		_sb_.append(endure);
		_sb_.append(",");
		_sb_.append(curmaxendure);
		_sb_.append(",");
		_sb_.append("'").append(producer).append("'");
		_sb_.append(",");
		_sb_.append(extendure);
		_sb_.append(",");
		_sb_.append(maxendure);
		_sb_.append(",");
		_sb_.append(skill);
		_sb_.append(",");
		_sb_.append(effect);
		_sb_.append(",");
		_sb_.append(treasure);
		_sb_.append(",");
		_sb_.append(attr);
		_sb_.append(",");
		_sb_.append(addattr);
		_sb_.append(",");
		_sb_.append(enhancement);
		_sb_.append(",");
		_sb_.append(equipscore);
		_sb_.append(",");
		_sb_.append(diamonds);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("equiplevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("repairtimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endure"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("curmaxendure"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("producer"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("extendure"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxendure"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skill"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("effect"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("treasure"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("attr"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("addattr"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("enhancement"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("equipscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("diamonds"));
		return lb;
	}

	private class Const implements xbean.Equip {
		Equip nThis() {
			return Equip.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Equip copy() {
			return Equip.this.copy();
		}

		@Override
		public xbean.Equip toData() {
			return Equip.this.toData();
		}

		public xbean.Equip toBean() {
			return Equip.this.toBean();
		}

		@Override
		public xbean.Equip toDataIf() {
			return Equip.this.toDataIf();
		}

		public xbean.Equip toBeanIf() {
			return Equip.this.toBeanIf();
		}

		@Override
		public int getEquiplevel() { // 装备等级
			_xdb_verify_unsafe_();
			return equiplevel;
		}

		@Override
		public int getRepairtimes() { // 修理失败次数
			_xdb_verify_unsafe_();
			return repairtimes;
		}

		@Override
		public int getEndure() { // 耐久
			_xdb_verify_unsafe_();
			return endure;
		}

		@Override
		public int getCurmaxendure() { // 当前最大耐久
			_xdb_verify_unsafe_();
			return curmaxendure;
		}

		@Override
		public String getProducer() { // 制造者
			_xdb_verify_unsafe_();
			return producer;
		}

		@Override
		public com.locojoy.base.Octets getProducerOctets() { // 制造者
			_xdb_verify_unsafe_();
			return Equip.this.getProducerOctets();
		}

		@Override
		public int getExtendure() { // 当值为100时,减少1
			_xdb_verify_unsafe_();
			return extendure;
		}

		@Override
		public int getMaxendure() { // 
			_xdb_verify_unsafe_();
			return maxendure;
		}

		@Override
		public int getSkill() { // 技能
			_xdb_verify_unsafe_();
			return skill;
		}

		@Override
		public int getEffect() { // 特效
			_xdb_verify_unsafe_();
			return effect;
		}

		@Override
		public int getTreasure() { // 0表示不是珍品;1表示是珍品
			_xdb_verify_unsafe_();
			return treasure;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttr() { // 基础属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(attr);
		}

		@Override
		public java.util.Map<Integer, Integer> getAttrAsData() { // 基础属性
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> attr;
			Equip _o_ = Equip.this;
			attr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
				attr.put(_e_.getKey(), _e_.getValue());
			return attr;
		}

		@Override
		public java.util.Map<Integer, Integer> getAddattr() { // 附加属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(addattr);
		}

		@Override
		public java.util.Map<Integer, Integer> getAddattrAsData() { // 附加属性
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> addattr;
			Equip _o_ = Equip.this;
			addattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
				addattr.put(_e_.getKey(), _e_.getValue());
			return addattr;
		}

		@Override
		public java.util.Map<Integer, xbean.EnhancementData> getEnhancement() { // 附魔属性 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(enhancement);
		}

		@Override
		public java.util.Map<Integer, xbean.EnhancementData> getEnhancementAsData() { // 附魔属性 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.EnhancementData> enhancement;
			Equip _o_ = Equip.this;
			enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>();
			for (java.util.Map.Entry<Integer, xbean.EnhancementData> _e_ : _o_.enhancement.entrySet())
				enhancement.put(_e_.getKey(), new EnhancementData.Data(_e_.getValue()));
			return enhancement;
		}

		@Override
		public int getEquipscore() { // 装备评分
			_xdb_verify_unsafe_();
			return equipscore;
		}

		@Override
		public java.util.List<Integer> getDiamonds() { // 宝石列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(diamonds);
		}

		public java.util.List<Integer> getDiamondsAsData() { // 宝石列表
			_xdb_verify_unsafe_();
			java.util.List<Integer> diamonds;
			Equip _o_ = Equip.this;
		diamonds = new java.util.LinkedList<Integer>();
		diamonds.addAll(_o_.diamonds);
			return diamonds;
		}

		@Override
		public void setEquiplevel(int _v_) { // 装备等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRepairtimes(int _v_) { // 修理失败次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEndure(int _v_) { // 耐久
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurmaxendure(int _v_) { // 当前最大耐久
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setProducer(String _v_) { // 制造者
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setProducerOctets(com.locojoy.base.Octets _v_) { // 制造者
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExtendure(int _v_) { // 当值为100时,减少1
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMaxendure(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSkill(int _v_) { // 技能
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEffect(int _v_) { // 特效
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTreasure(int _v_) { // 0表示不是珍品;1表示是珍品
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEquipscore(int _v_) { // 装备评分
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
			return Equip.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Equip.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Equip.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Equip.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Equip.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Equip.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Equip.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Equip.this.hashCode();
		}

		@Override
		public String toString() {
			return Equip.this.toString();
		}

	}

	public static final class Data implements xbean.Equip {
		private int equiplevel; // 装备等级
		private int repairtimes; // 修理失败次数
		private int endure; // 耐久
		private int curmaxendure; // 当前最大耐久
		private String producer; // 制造者
		private int extendure; // 当值为100时,减少1
		private int maxendure; // 
		private int skill; // 技能
		private int effect; // 特效
		private int treasure; // 0表示不是珍品;1表示是珍品
		private java.util.HashMap<Integer, Integer> attr; // 基础属性
		private java.util.HashMap<Integer, Integer> addattr; // 附加属性
		private java.util.HashMap<Integer, xbean.EnhancementData> enhancement; // 附魔属性 by changhao
		private int equipscore; // 装备评分
		private java.util.LinkedList<Integer> diamonds; // 宝石列表

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			producer = "";
			extendure = 0;
			skill = 0;
			effect = 0;
			treasure = 0;
			attr = new java.util.HashMap<Integer, Integer>();
			addattr = new java.util.HashMap<Integer, Integer>();
			enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>();
			diamonds = new java.util.LinkedList<Integer>();
		}

		Data(xbean.Equip _o1_) {
			if (_o1_ instanceof Equip) assign((Equip)_o1_);
			else if (_o1_ instanceof Equip.Data) assign((Equip.Data)_o1_);
			else if (_o1_ instanceof Equip.Const) assign(((Equip.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Equip _o_) {
			equiplevel = _o_.equiplevel;
			repairtimes = _o_.repairtimes;
			endure = _o_.endure;
			curmaxendure = _o_.curmaxendure;
			producer = _o_.producer;
			extendure = _o_.extendure;
			maxendure = _o_.maxendure;
			skill = _o_.skill;
			effect = _o_.effect;
			treasure = _o_.treasure;
			attr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
				attr.put(_e_.getKey(), _e_.getValue());
			addattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
				addattr.put(_e_.getKey(), _e_.getValue());
			enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>();
			for (java.util.Map.Entry<Integer, xbean.EnhancementData> _e_ : _o_.enhancement.entrySet())
				enhancement.put(_e_.getKey(), new EnhancementData.Data(_e_.getValue()));
			equipscore = _o_.equipscore;
			diamonds = new java.util.LinkedList<Integer>();
			diamonds.addAll(_o_.diamonds);
		}

		private void assign(Equip.Data _o_) {
			equiplevel = _o_.equiplevel;
			repairtimes = _o_.repairtimes;
			endure = _o_.endure;
			curmaxendure = _o_.curmaxendure;
			producer = _o_.producer;
			extendure = _o_.extendure;
			maxendure = _o_.maxendure;
			skill = _o_.skill;
			effect = _o_.effect;
			treasure = _o_.treasure;
			attr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attr.entrySet())
				attr.put(_e_.getKey(), _e_.getValue());
			addattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.addattr.entrySet())
				addattr.put(_e_.getKey(), _e_.getValue());
			enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>();
			for (java.util.Map.Entry<Integer, xbean.EnhancementData> _e_ : _o_.enhancement.entrySet())
				enhancement.put(_e_.getKey(), new EnhancementData.Data(_e_.getValue()));
			equipscore = _o_.equipscore;
			diamonds = new java.util.LinkedList<Integer>();
			diamonds.addAll(_o_.diamonds);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(equiplevel);
			_os_.marshal(repairtimes);
			_os_.marshal(endure);
			_os_.marshal(curmaxendure);
			_os_.marshal(producer, mkdb.Const.IO_CHARSET);
			_os_.marshal(extendure);
			_os_.marshal(maxendure);
			_os_.marshal(skill);
			_os_.marshal(effect);
			_os_.marshal(treasure);
			_os_.compact_uint32(attr.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : attr.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(addattr.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : addattr.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(enhancement.size());
			for (java.util.Map.Entry<Integer, xbean.EnhancementData> _e_ : enhancement.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(equipscore);
			_os_.compact_uint32(diamonds.size());
			for (Integer _v_ : diamonds) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			equiplevel = _os_.unmarshal_int();
			repairtimes = _os_.unmarshal_int();
			endure = _os_.unmarshal_int();
			curmaxendure = _os_.unmarshal_int();
			producer = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			extendure = _os_.unmarshal_int();
			maxendure = _os_.unmarshal_int();
			skill = _os_.unmarshal_int();
			effect = _os_.unmarshal_int();
			treasure = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					attr = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					attr.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					addattr = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					addattr.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					enhancement = new java.util.HashMap<Integer, xbean.EnhancementData>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.EnhancementData _v_ = xbean.Pod.newEnhancementDataData();
					_v_.unmarshal(_os_);
					enhancement.put(_k_, _v_);
				}
			}
			equipscore = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				diamonds.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.Equip copy() {
			return new Data(this);
		}

		@Override
		public xbean.Equip toData() {
			return new Data(this);
		}

		public xbean.Equip toBean() {
			return new Equip(this, null, null);
		}

		@Override
		public xbean.Equip toDataIf() {
			return this;
		}

		public xbean.Equip toBeanIf() {
			return new Equip(this, null, null);
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
		public int getEquiplevel() { // 装备等级
			return equiplevel;
		}

		@Override
		public int getRepairtimes() { // 修理失败次数
			return repairtimes;
		}

		@Override
		public int getEndure() { // 耐久
			return endure;
		}

		@Override
		public int getCurmaxendure() { // 当前最大耐久
			return curmaxendure;
		}

		@Override
		public String getProducer() { // 制造者
			return producer;
		}

		@Override
		public com.locojoy.base.Octets getProducerOctets() { // 制造者
			return com.locojoy.base.Octets.wrap(getProducer(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getExtendure() { // 当值为100时,减少1
			return extendure;
		}

		@Override
		public int getMaxendure() { // 
			return maxendure;
		}

		@Override
		public int getSkill() { // 技能
			return skill;
		}

		@Override
		public int getEffect() { // 特效
			return effect;
		}

		@Override
		public int getTreasure() { // 0表示不是珍品;1表示是珍品
			return treasure;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttr() { // 基础属性
			return attr;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttrAsData() { // 基础属性
			return attr;
		}

		@Override
		public java.util.Map<Integer, Integer> getAddattr() { // 附加属性
			return addattr;
		}

		@Override
		public java.util.Map<Integer, Integer> getAddattrAsData() { // 附加属性
			return addattr;
		}

		@Override
		public java.util.Map<Integer, xbean.EnhancementData> getEnhancement() { // 附魔属性 by changhao
			return enhancement;
		}

		@Override
		public java.util.Map<Integer, xbean.EnhancementData> getEnhancementAsData() { // 附魔属性 by changhao
			return enhancement;
		}

		@Override
		public int getEquipscore() { // 装备评分
			return equipscore;
		}

		@Override
		public java.util.List<Integer> getDiamonds() { // 宝石列表
			return diamonds;
		}

		@Override
		public java.util.List<Integer> getDiamondsAsData() { // 宝石列表
			return diamonds;
		}

		@Override
		public void setEquiplevel(int _v_) { // 装备等级
			equiplevel = _v_;
		}

		@Override
		public void setRepairtimes(int _v_) { // 修理失败次数
			repairtimes = _v_;
		}

		@Override
		public void setEndure(int _v_) { // 耐久
			endure = _v_;
		}

		@Override
		public void setCurmaxendure(int _v_) { // 当前最大耐久
			curmaxendure = _v_;
		}

		@Override
		public void setProducer(String _v_) { // 制造者
			if (null == _v_)
				throw new NullPointerException();
			producer = _v_;
		}

		@Override
		public void setProducerOctets(com.locojoy.base.Octets _v_) { // 制造者
			this.setProducer(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setExtendure(int _v_) { // 当值为100时,减少1
			extendure = _v_;
		}

		@Override
		public void setMaxendure(int _v_) { // 
			maxendure = _v_;
		}

		@Override
		public void setSkill(int _v_) { // 技能
			skill = _v_;
		}

		@Override
		public void setEffect(int _v_) { // 特效
			effect = _v_;
		}

		@Override
		public void setTreasure(int _v_) { // 0表示不是珍品;1表示是珍品
			treasure = _v_;
		}

		@Override
		public void setEquipscore(int _v_) { // 装备评分
			equipscore = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Equip.Data)) return false;
			Equip.Data _o_ = (Equip.Data) _o1_;
			if (equiplevel != _o_.equiplevel) return false;
			if (repairtimes != _o_.repairtimes) return false;
			if (endure != _o_.endure) return false;
			if (curmaxendure != _o_.curmaxendure) return false;
			if (!producer.equals(_o_.producer)) return false;
			if (extendure != _o_.extendure) return false;
			if (maxendure != _o_.maxendure) return false;
			if (skill != _o_.skill) return false;
			if (effect != _o_.effect) return false;
			if (treasure != _o_.treasure) return false;
			if (!attr.equals(_o_.attr)) return false;
			if (!addattr.equals(_o_.addattr)) return false;
			if (!enhancement.equals(_o_.enhancement)) return false;
			if (equipscore != _o_.equipscore) return false;
			if (!diamonds.equals(_o_.diamonds)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += equiplevel;
			_h_ += repairtimes;
			_h_ += endure;
			_h_ += curmaxendure;
			_h_ += producer.hashCode();
			_h_ += extendure;
			_h_ += maxendure;
			_h_ += skill;
			_h_ += effect;
			_h_ += treasure;
			_h_ += attr.hashCode();
			_h_ += addattr.hashCode();
			_h_ += enhancement.hashCode();
			_h_ += equipscore;
			_h_ += diamonds.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(equiplevel);
			_sb_.append(",");
			_sb_.append(repairtimes);
			_sb_.append(",");
			_sb_.append(endure);
			_sb_.append(",");
			_sb_.append(curmaxendure);
			_sb_.append(",");
			_sb_.append("'").append(producer).append("'");
			_sb_.append(",");
			_sb_.append(extendure);
			_sb_.append(",");
			_sb_.append(maxendure);
			_sb_.append(",");
			_sb_.append(skill);
			_sb_.append(",");
			_sb_.append(effect);
			_sb_.append(",");
			_sb_.append(treasure);
			_sb_.append(",");
			_sb_.append(attr);
			_sb_.append(",");
			_sb_.append(addattr);
			_sb_.append(",");
			_sb_.append(enhancement);
			_sb_.append(",");
			_sb_.append(equipscore);
			_sb_.append(",");
			_sb_.append(diamonds);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

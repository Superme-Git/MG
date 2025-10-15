
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Buff extends mkdb.XBean implements xbean.Buff {
	private int index; // buff类型Id，一种类型的buff只能有一个
	private long impacttime; // buff attach时的时间，用于计算剩余时间和检测到期
	private long remaintime; // 计时buff总持续时间（period时的period）
	private int round; // 计数buff剩余回合（period时的count）
	private long value; // buff的剩余量（period时的initDelay）
	private java.util.HashMap<Integer, Float> effects; // key = effect type id
	private int fighterkey; // 当在战斗中添加的buff时，记录战斗者key

	@Override
	public void _reset_unsafe_() {
		index = 0;
		impacttime = 0;
		remaintime = 0;
		round = 0;
		value = 0;
		effects.clear();
		fighterkey = 0;
	}

	Buff(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		impacttime = 0;
		remaintime = 0;
		round = 0;
		value = 0;
		effects = new java.util.HashMap<Integer, Float>();
	}

	public Buff() {
		this(0, null, null);
	}

	public Buff(Buff _o_) {
		this(_o_, null, null);
	}

	Buff(xbean.Buff _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Buff) assign((Buff)_o1_);
		else if (_o1_ instanceof Buff.Data) assign((Buff.Data)_o1_);
		else if (_o1_ instanceof Buff.Const) assign(((Buff.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Buff _o_) {
		_o_._xdb_verify_unsafe_();
		index = _o_.index;
		impacttime = _o_.impacttime;
		remaintime = _o_.remaintime;
		round = _o_.round;
		value = _o_.value;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		fighterkey = _o_.fighterkey;
	}

	private void assign(Buff.Data _o_) {
		index = _o_.index;
		impacttime = _o_.impacttime;
		remaintime = _o_.remaintime;
		round = _o_.round;
		value = _o_.value;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		fighterkey = _o_.fighterkey;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(index);
		_os_.marshal(impacttime);
		_os_.marshal(remaintime);
		_os_.marshal(round);
		_os_.marshal(value);
		_os_.compact_uint32(effects.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : effects.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(fighterkey);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		index = _os_.unmarshal_int();
		impacttime = _os_.unmarshal_long();
		remaintime = _os_.unmarshal_long();
		round = _os_.unmarshal_int();
		value = _os_.unmarshal_long();
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
		fighterkey = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.Buff copy() {
		_xdb_verify_unsafe_();
		return new Buff(this);
	}

	@Override
	public xbean.Buff toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Buff toBean() {
		_xdb_verify_unsafe_();
		return new Buff(this); // same as copy()
	}

	@Override
	public xbean.Buff toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Buff toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getIndex() { // buff类型Id，一种类型的buff只能有一个
		_xdb_verify_unsafe_();
		return index;
	}

	@Override
	public long getImpacttime() { // buff attach时的时间，用于计算剩余时间和检测到期
		_xdb_verify_unsafe_();
		return impacttime;
	}

	@Override
	public long getRemaintime() { // 计时buff总持续时间（period时的period）
		_xdb_verify_unsafe_();
		return remaintime;
	}

	@Override
	public int getRound() { // 计数buff剩余回合（period时的count）
		_xdb_verify_unsafe_();
		return round;
	}

	@Override
	public long getValue() { // buff的剩余量（period时的initDelay）
		_xdb_verify_unsafe_();
		return value;
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
		Buff _o_ = this;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		return effects;
	}

	@Override
	public int getFighterkey() { // 当在战斗中添加的buff时，记录战斗者key
		_xdb_verify_unsafe_();
		return fighterkey;
	}

	@Override
	public void setIndex(int _v_) { // buff类型Id，一种类型的buff只能有一个
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "index") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, index) {
					public void rollback() { index = _xdb_saved; }
				};}});
		index = _v_;
	}

	@Override
	public void setImpacttime(long _v_) { // buff attach时的时间，用于计算剩余时间和检测到期
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "impacttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, impacttime) {
					public void rollback() { impacttime = _xdb_saved; }
				};}});
		impacttime = _v_;
	}

	@Override
	public void setRemaintime(long _v_) { // 计时buff总持续时间（period时的period）
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "remaintime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, remaintime) {
					public void rollback() { remaintime = _xdb_saved; }
				};}});
		remaintime = _v_;
	}

	@Override
	public void setRound(int _v_) { // 计数buff剩余回合（period时的count）
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "round") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, round) {
					public void rollback() { round = _xdb_saved; }
				};}});
		round = _v_;
	}

	@Override
	public void setValue(long _v_) { // buff的剩余量（period时的initDelay）
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "value") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, value) {
					public void rollback() { value = _xdb_saved; }
				};}});
		value = _v_;
	}

	@Override
	public void setFighterkey(int _v_) { // 当在战斗中添加的buff时，记录战斗者key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fighterkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fighterkey) {
					public void rollback() { fighterkey = _xdb_saved; }
				};}});
		fighterkey = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Buff _o_ = null;
		if ( _o1_ instanceof Buff ) _o_ = (Buff)_o1_;
		else if ( _o1_ instanceof Buff.Const ) _o_ = ((Buff.Const)_o1_).nThis();
		else return false;
		if (index != _o_.index) return false;
		if (impacttime != _o_.impacttime) return false;
		if (remaintime != _o_.remaintime) return false;
		if (round != _o_.round) return false;
		if (value != _o_.value) return false;
		if (!effects.equals(_o_.effects)) return false;
		if (fighterkey != _o_.fighterkey) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += index;
		_h_ += impacttime;
		_h_ += remaintime;
		_h_ += round;
		_h_ += value;
		_h_ += effects.hashCode();
		_h_ += fighterkey;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(index);
		_sb_.append(",");
		_sb_.append(impacttime);
		_sb_.append(",");
		_sb_.append(remaintime);
		_sb_.append(",");
		_sb_.append(round);
		_sb_.append(",");
		_sb_.append(value);
		_sb_.append(",");
		_sb_.append(effects);
		_sb_.append(",");
		_sb_.append(fighterkey);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("index"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("impacttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("remaintime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("round"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("value"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("effects"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fighterkey"));
		return lb;
	}

	private class Const implements xbean.Buff {
		Buff nThis() {
			return Buff.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Buff copy() {
			return Buff.this.copy();
		}

		@Override
		public xbean.Buff toData() {
			return Buff.this.toData();
		}

		public xbean.Buff toBean() {
			return Buff.this.toBean();
		}

		@Override
		public xbean.Buff toDataIf() {
			return Buff.this.toDataIf();
		}

		public xbean.Buff toBeanIf() {
			return Buff.this.toBeanIf();
		}

		@Override
		public int getIndex() { // buff类型Id，一种类型的buff只能有一个
			_xdb_verify_unsafe_();
			return index;
		}

		@Override
		public long getImpacttime() { // buff attach时的时间，用于计算剩余时间和检测到期
			_xdb_verify_unsafe_();
			return impacttime;
		}

		@Override
		public long getRemaintime() { // 计时buff总持续时间（period时的period）
			_xdb_verify_unsafe_();
			return remaintime;
		}

		@Override
		public int getRound() { // 计数buff剩余回合（period时的count）
			_xdb_verify_unsafe_();
			return round;
		}

		@Override
		public long getValue() { // buff的剩余量（period时的initDelay）
			_xdb_verify_unsafe_();
			return value;
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
			Buff _o_ = Buff.this;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			return effects;
		}

		@Override
		public int getFighterkey() { // 当在战斗中添加的buff时，记录战斗者key
			_xdb_verify_unsafe_();
			return fighterkey;
		}

		@Override
		public void setIndex(int _v_) { // buff类型Id，一种类型的buff只能有一个
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setImpacttime(long _v_) { // buff attach时的时间，用于计算剩余时间和检测到期
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRemaintime(long _v_) { // 计时buff总持续时间（period时的period）
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRound(int _v_) { // 计数buff剩余回合（period时的count）
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue(long _v_) { // buff的剩余量（period时的initDelay）
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFighterkey(int _v_) { // 当在战斗中添加的buff时，记录战斗者key
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
			return Buff.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Buff.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Buff.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Buff.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Buff.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Buff.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Buff.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Buff.this.hashCode();
		}

		@Override
		public String toString() {
			return Buff.this.toString();
		}

	}

	public static final class Data implements xbean.Buff {
		private int index; // buff类型Id，一种类型的buff只能有一个
		private long impacttime; // buff attach时的时间，用于计算剩余时间和检测到期
		private long remaintime; // 计时buff总持续时间（period时的period）
		private int round; // 计数buff剩余回合（period时的count）
		private long value; // buff的剩余量（period时的initDelay）
		private java.util.HashMap<Integer, Float> effects; // key = effect type id
		private int fighterkey; // 当在战斗中添加的buff时，记录战斗者key

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			impacttime = 0;
			remaintime = 0;
			round = 0;
			value = 0;
			effects = new java.util.HashMap<Integer, Float>();
		}

		Data(xbean.Buff _o1_) {
			if (_o1_ instanceof Buff) assign((Buff)_o1_);
			else if (_o1_ instanceof Buff.Data) assign((Buff.Data)_o1_);
			else if (_o1_ instanceof Buff.Const) assign(((Buff.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Buff _o_) {
			index = _o_.index;
			impacttime = _o_.impacttime;
			remaintime = _o_.remaintime;
			round = _o_.round;
			value = _o_.value;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			fighterkey = _o_.fighterkey;
		}

		private void assign(Buff.Data _o_) {
			index = _o_.index;
			impacttime = _o_.impacttime;
			remaintime = _o_.remaintime;
			round = _o_.round;
			value = _o_.value;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			fighterkey = _o_.fighterkey;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(index);
			_os_.marshal(impacttime);
			_os_.marshal(remaintime);
			_os_.marshal(round);
			_os_.marshal(value);
			_os_.compact_uint32(effects.size());
			for (java.util.Map.Entry<Integer, Float> _e_ : effects.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(fighterkey);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			index = _os_.unmarshal_int();
			impacttime = _os_.unmarshal_long();
			remaintime = _os_.unmarshal_long();
			round = _os_.unmarshal_int();
			value = _os_.unmarshal_long();
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
			fighterkey = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.Buff copy() {
			return new Data(this);
		}

		@Override
		public xbean.Buff toData() {
			return new Data(this);
		}

		public xbean.Buff toBean() {
			return new Buff(this, null, null);
		}

		@Override
		public xbean.Buff toDataIf() {
			return this;
		}

		public xbean.Buff toBeanIf() {
			return new Buff(this, null, null);
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
		public int getIndex() { // buff类型Id，一种类型的buff只能有一个
			return index;
		}

		@Override
		public long getImpacttime() { // buff attach时的时间，用于计算剩余时间和检测到期
			return impacttime;
		}

		@Override
		public long getRemaintime() { // 计时buff总持续时间（period时的period）
			return remaintime;
		}

		@Override
		public int getRound() { // 计数buff剩余回合（period时的count）
			return round;
		}

		@Override
		public long getValue() { // buff的剩余量（period时的initDelay）
			return value;
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
		public int getFighterkey() { // 当在战斗中添加的buff时，记录战斗者key
			return fighterkey;
		}

		@Override
		public void setIndex(int _v_) { // buff类型Id，一种类型的buff只能有一个
			index = _v_;
		}

		@Override
		public void setImpacttime(long _v_) { // buff attach时的时间，用于计算剩余时间和检测到期
			impacttime = _v_;
		}

		@Override
		public void setRemaintime(long _v_) { // 计时buff总持续时间（period时的period）
			remaintime = _v_;
		}

		@Override
		public void setRound(int _v_) { // 计数buff剩余回合（period时的count）
			round = _v_;
		}

		@Override
		public void setValue(long _v_) { // buff的剩余量（period时的initDelay）
			value = _v_;
		}

		@Override
		public void setFighterkey(int _v_) { // 当在战斗中添加的buff时，记录战斗者key
			fighterkey = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Buff.Data)) return false;
			Buff.Data _o_ = (Buff.Data) _o1_;
			if (index != _o_.index) return false;
			if (impacttime != _o_.impacttime) return false;
			if (remaintime != _o_.remaintime) return false;
			if (round != _o_.round) return false;
			if (value != _o_.value) return false;
			if (!effects.equals(_o_.effects)) return false;
			if (fighterkey != _o_.fighterkey) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += index;
			_h_ += impacttime;
			_h_ += remaintime;
			_h_ += round;
			_h_ += value;
			_h_ += effects.hashCode();
			_h_ += fighterkey;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(index);
			_sb_.append(",");
			_sb_.append(impacttime);
			_sb_.append(",");
			_sb_.append(remaintime);
			_sb_.append(",");
			_sb_.append(round);
			_sb_.append(",");
			_sb_.append(value);
			_sb_.append(",");
			_sb_.append(effects);
			_sb_.append(",");
			_sb_.append(fighterkey);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

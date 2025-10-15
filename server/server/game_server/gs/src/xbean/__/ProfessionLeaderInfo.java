
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ProfessionLeaderInfo extends mkdb.XBean implements xbean.ProfessionLeaderInfo {
	private long roleid; // 职业领袖对应的role的id
	private int refreshtimes; // 每天刷新职业领袖能力不超过5次
	private long lastrefreshtime; // 上次刷新的时间
	private java.util.ArrayList<xbean.Monster> monsterbean; // 血,魔和其他攻击属性都从Monster中获取,其实本应不用vector,但生成的代码没有set function
	private java.util.HashMap<Integer, Integer> shapecomponent; // 

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		refreshtimes = 0;
		lastrefreshtime = 0L;
		monsterbean.clear();
		shapecomponent.clear();
	}

	ProfessionLeaderInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		monsterbean = new java.util.ArrayList<xbean.Monster>();
		shapecomponent = new java.util.HashMap<Integer, Integer>();
	}

	public ProfessionLeaderInfo() {
		this(0, null, null);
	}

	public ProfessionLeaderInfo(ProfessionLeaderInfo _o_) {
		this(_o_, null, null);
	}

	ProfessionLeaderInfo(xbean.ProfessionLeaderInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ProfessionLeaderInfo) assign((ProfessionLeaderInfo)_o1_);
		else if (_o1_ instanceof ProfessionLeaderInfo.Data) assign((ProfessionLeaderInfo.Data)_o1_);
		else if (_o1_ instanceof ProfessionLeaderInfo.Const) assign(((ProfessionLeaderInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ProfessionLeaderInfo _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		refreshtimes = _o_.refreshtimes;
		lastrefreshtime = _o_.lastrefreshtime;
		monsterbean = new java.util.ArrayList<xbean.Monster>();
		for (xbean.Monster _v_ : _o_.monsterbean)
			monsterbean.add(new Monster(_v_, this, "monsterbean"));
		shapecomponent = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.shapecomponent.entrySet())
			shapecomponent.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(ProfessionLeaderInfo.Data _o_) {
		roleid = _o_.roleid;
		refreshtimes = _o_.refreshtimes;
		lastrefreshtime = _o_.lastrefreshtime;
		monsterbean = new java.util.ArrayList<xbean.Monster>();
		for (xbean.Monster _v_ : _o_.monsterbean)
			monsterbean.add(new Monster(_v_, this, "monsterbean"));
		shapecomponent = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.shapecomponent.entrySet())
			shapecomponent.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(refreshtimes);
		_os_.marshal(lastrefreshtime);
		_os_.compact_uint32(monsterbean.size());
		for (xbean.Monster _v_ : monsterbean) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(shapecomponent.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : shapecomponent.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		refreshtimes = _os_.unmarshal_int();
		lastrefreshtime = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.Monster _v_ = new Monster(0, this, "monsterbean");
			_v_.unmarshal(_os_);
			monsterbean.add(_v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				shapecomponent = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				shapecomponent.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ProfessionLeaderInfo copy() {
		_xdb_verify_unsafe_();
		return new ProfessionLeaderInfo(this);
	}

	@Override
	public xbean.ProfessionLeaderInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProfessionLeaderInfo toBean() {
		_xdb_verify_unsafe_();
		return new ProfessionLeaderInfo(this); // same as copy()
	}

	@Override
	public xbean.ProfessionLeaderInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProfessionLeaderInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 职业领袖对应的role的id
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getRefreshtimes() { // 每天刷新职业领袖能力不超过5次
		_xdb_verify_unsafe_();
		return refreshtimes;
	}

	@Override
	public long getLastrefreshtime() { // 上次刷新的时间
		_xdb_verify_unsafe_();
		return lastrefreshtime;
	}

	@Override
	public java.util.List<xbean.Monster> getMonsterbean() { // 血,魔和其他攻击属性都从Monster中获取,其实本应不用vector,但生成的代码没有set function
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "monsterbean"), monsterbean);
	}

	public java.util.List<xbean.Monster> getMonsterbeanAsData() { // 血,魔和其他攻击属性都从Monster中获取,其实本应不用vector,但生成的代码没有set function
		_xdb_verify_unsafe_();
		java.util.List<xbean.Monster> monsterbean;
		ProfessionLeaderInfo _o_ = this;
		monsterbean = new java.util.ArrayList<xbean.Monster>();
		for (xbean.Monster _v_ : _o_.monsterbean)
			monsterbean.add(new Monster.Data(_v_));
		return monsterbean;
	}

	@Override
	public java.util.Map<Integer, Integer> getShapecomponent() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "shapecomponent"), shapecomponent);
	}

	@Override
	public java.util.Map<Integer, Integer> getShapecomponentAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> shapecomponent;
		ProfessionLeaderInfo _o_ = this;
		shapecomponent = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.shapecomponent.entrySet())
			shapecomponent.put(_e_.getKey(), _e_.getValue());
		return shapecomponent;
	}

	@Override
	public void setRoleid(long _v_) { // 职业领袖对应的role的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setRefreshtimes(int _v_) { // 每天刷新职业领袖能力不超过5次
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "refreshtimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, refreshtimes) {
					public void rollback() { refreshtimes = _xdb_saved; }
				};}});
		refreshtimes = _v_;
	}

	@Override
	public void setLastrefreshtime(long _v_) { // 上次刷新的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastrefreshtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastrefreshtime) {
					public void rollback() { lastrefreshtime = _xdb_saved; }
				};}});
		lastrefreshtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ProfessionLeaderInfo _o_ = null;
		if ( _o1_ instanceof ProfessionLeaderInfo ) _o_ = (ProfessionLeaderInfo)_o1_;
		else if ( _o1_ instanceof ProfessionLeaderInfo.Const ) _o_ = ((ProfessionLeaderInfo.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (refreshtimes != _o_.refreshtimes) return false;
		if (lastrefreshtime != _o_.lastrefreshtime) return false;
		if (!monsterbean.equals(_o_.monsterbean)) return false;
		if (!shapecomponent.equals(_o_.shapecomponent)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += refreshtimes;
		_h_ += lastrefreshtime;
		_h_ += monsterbean.hashCode();
		_h_ += shapecomponent.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(refreshtimes);
		_sb_.append(",");
		_sb_.append(lastrefreshtime);
		_sb_.append(",");
		_sb_.append(monsterbean);
		_sb_.append(",");
		_sb_.append(shapecomponent);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("refreshtimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastrefreshtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monsterbean"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("shapecomponent"));
		return lb;
	}

	private class Const implements xbean.ProfessionLeaderInfo {
		ProfessionLeaderInfo nThis() {
			return ProfessionLeaderInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ProfessionLeaderInfo copy() {
			return ProfessionLeaderInfo.this.copy();
		}

		@Override
		public xbean.ProfessionLeaderInfo toData() {
			return ProfessionLeaderInfo.this.toData();
		}

		public xbean.ProfessionLeaderInfo toBean() {
			return ProfessionLeaderInfo.this.toBean();
		}

		@Override
		public xbean.ProfessionLeaderInfo toDataIf() {
			return ProfessionLeaderInfo.this.toDataIf();
		}

		public xbean.ProfessionLeaderInfo toBeanIf() {
			return ProfessionLeaderInfo.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 职业领袖对应的role的id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getRefreshtimes() { // 每天刷新职业领袖能力不超过5次
			_xdb_verify_unsafe_();
			return refreshtimes;
		}

		@Override
		public long getLastrefreshtime() { // 上次刷新的时间
			_xdb_verify_unsafe_();
			return lastrefreshtime;
		}

		@Override
		public java.util.List<xbean.Monster> getMonsterbean() { // 血,魔和其他攻击属性都从Monster中获取,其实本应不用vector,但生成的代码没有set function
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(monsterbean);
		}

		public java.util.List<xbean.Monster> getMonsterbeanAsData() { // 血,魔和其他攻击属性都从Monster中获取,其实本应不用vector,但生成的代码没有set function
			_xdb_verify_unsafe_();
			java.util.List<xbean.Monster> monsterbean;
			ProfessionLeaderInfo _o_ = ProfessionLeaderInfo.this;
		monsterbean = new java.util.ArrayList<xbean.Monster>();
		for (xbean.Monster _v_ : _o_.monsterbean)
			monsterbean.add(new Monster.Data(_v_));
			return monsterbean;
		}

		@Override
		public java.util.Map<Integer, Integer> getShapecomponent() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(shapecomponent);
		}

		@Override
		public java.util.Map<Integer, Integer> getShapecomponentAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> shapecomponent;
			ProfessionLeaderInfo _o_ = ProfessionLeaderInfo.this;
			shapecomponent = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.shapecomponent.entrySet())
				shapecomponent.put(_e_.getKey(), _e_.getValue());
			return shapecomponent;
		}

		@Override
		public void setRoleid(long _v_) { // 职业领袖对应的role的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRefreshtimes(int _v_) { // 每天刷新职业领袖能力不超过5次
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastrefreshtime(long _v_) { // 上次刷新的时间
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
			return ProfessionLeaderInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ProfessionLeaderInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ProfessionLeaderInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ProfessionLeaderInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ProfessionLeaderInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ProfessionLeaderInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ProfessionLeaderInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ProfessionLeaderInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ProfessionLeaderInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ProfessionLeaderInfo {
		private long roleid; // 职业领袖对应的role的id
		private int refreshtimes; // 每天刷新职业领袖能力不超过5次
		private long lastrefreshtime; // 上次刷新的时间
		private java.util.ArrayList<xbean.Monster> monsterbean; // 血,魔和其他攻击属性都从Monster中获取,其实本应不用vector,但生成的代码没有set function
		private java.util.HashMap<Integer, Integer> shapecomponent; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			monsterbean = new java.util.ArrayList<xbean.Monster>();
			shapecomponent = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.ProfessionLeaderInfo _o1_) {
			if (_o1_ instanceof ProfessionLeaderInfo) assign((ProfessionLeaderInfo)_o1_);
			else if (_o1_ instanceof ProfessionLeaderInfo.Data) assign((ProfessionLeaderInfo.Data)_o1_);
			else if (_o1_ instanceof ProfessionLeaderInfo.Const) assign(((ProfessionLeaderInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ProfessionLeaderInfo _o_) {
			roleid = _o_.roleid;
			refreshtimes = _o_.refreshtimes;
			lastrefreshtime = _o_.lastrefreshtime;
			monsterbean = new java.util.ArrayList<xbean.Monster>();
			for (xbean.Monster _v_ : _o_.monsterbean)
				monsterbean.add(new Monster.Data(_v_));
			shapecomponent = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.shapecomponent.entrySet())
				shapecomponent.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(ProfessionLeaderInfo.Data _o_) {
			roleid = _o_.roleid;
			refreshtimes = _o_.refreshtimes;
			lastrefreshtime = _o_.lastrefreshtime;
			monsterbean = new java.util.ArrayList<xbean.Monster>();
			for (xbean.Monster _v_ : _o_.monsterbean)
				monsterbean.add(new Monster.Data(_v_));
			shapecomponent = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.shapecomponent.entrySet())
				shapecomponent.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(refreshtimes);
			_os_.marshal(lastrefreshtime);
			_os_.compact_uint32(monsterbean.size());
			for (xbean.Monster _v_ : monsterbean) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(shapecomponent.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : shapecomponent.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			refreshtimes = _os_.unmarshal_int();
			lastrefreshtime = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.Monster _v_ = xbean.Pod.newMonsterData();
				_v_.unmarshal(_os_);
				monsterbean.add(_v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					shapecomponent = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					shapecomponent.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ProfessionLeaderInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ProfessionLeaderInfo toData() {
			return new Data(this);
		}

		public xbean.ProfessionLeaderInfo toBean() {
			return new ProfessionLeaderInfo(this, null, null);
		}

		@Override
		public xbean.ProfessionLeaderInfo toDataIf() {
			return this;
		}

		public xbean.ProfessionLeaderInfo toBeanIf() {
			return new ProfessionLeaderInfo(this, null, null);
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
		public long getRoleid() { // 职业领袖对应的role的id
			return roleid;
		}

		@Override
		public int getRefreshtimes() { // 每天刷新职业领袖能力不超过5次
			return refreshtimes;
		}

		@Override
		public long getLastrefreshtime() { // 上次刷新的时间
			return lastrefreshtime;
		}

		@Override
		public java.util.List<xbean.Monster> getMonsterbean() { // 血,魔和其他攻击属性都从Monster中获取,其实本应不用vector,但生成的代码没有set function
			return monsterbean;
		}

		@Override
		public java.util.List<xbean.Monster> getMonsterbeanAsData() { // 血,魔和其他攻击属性都从Monster中获取,其实本应不用vector,但生成的代码没有set function
			return monsterbean;
		}

		@Override
		public java.util.Map<Integer, Integer> getShapecomponent() { // 
			return shapecomponent;
		}

		@Override
		public java.util.Map<Integer, Integer> getShapecomponentAsData() { // 
			return shapecomponent;
		}

		@Override
		public void setRoleid(long _v_) { // 职业领袖对应的role的id
			roleid = _v_;
		}

		@Override
		public void setRefreshtimes(int _v_) { // 每天刷新职业领袖能力不超过5次
			refreshtimes = _v_;
		}

		@Override
		public void setLastrefreshtime(long _v_) { // 上次刷新的时间
			lastrefreshtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ProfessionLeaderInfo.Data)) return false;
			ProfessionLeaderInfo.Data _o_ = (ProfessionLeaderInfo.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (refreshtimes != _o_.refreshtimes) return false;
			if (lastrefreshtime != _o_.lastrefreshtime) return false;
			if (!monsterbean.equals(_o_.monsterbean)) return false;
			if (!shapecomponent.equals(_o_.shapecomponent)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += refreshtimes;
			_h_ += lastrefreshtime;
			_h_ += monsterbean.hashCode();
			_h_ += shapecomponent.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(refreshtimes);
			_sb_.append(",");
			_sb_.append(lastrefreshtime);
			_sb_.append(",");
			_sb_.append(monsterbean);
			_sb_.append(",");
			_sb_.append(shapecomponent);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CurrApprent extends mkdb.XBean implements xbean.CurrApprent {
	private int titleid; // 徒弟的当前称谓
	private int level; // 拜师时候的等级
	private xbean.HasApprent apprentinfo; // 徒弟的基本信息
	private java.util.HashMap<Integer, xbean.ApprentceChieve> achievement; // 徒弟的各项成就
	private int mastercomment; // 师傅对徒弟的评价
	private int apprentcomment; // 徒弟对师傅的评价
	private long baishitime; // 

	@Override
	public void _reset_unsafe_() {
		titleid = 0;
		level = 0;
		apprentinfo._reset_unsafe_();
		achievement.clear();
		mastercomment = 0;
		apprentcomment = 0;
		baishitime = 0L;
	}

	CurrApprent(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		apprentinfo = new HasApprent(0, this, "apprentinfo");
		achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>();
	}

	public CurrApprent() {
		this(0, null, null);
	}

	public CurrApprent(CurrApprent _o_) {
		this(_o_, null, null);
	}

	CurrApprent(xbean.CurrApprent _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CurrApprent) assign((CurrApprent)_o1_);
		else if (_o1_ instanceof CurrApprent.Data) assign((CurrApprent.Data)_o1_);
		else if (_o1_ instanceof CurrApprent.Const) assign(((CurrApprent.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CurrApprent _o_) {
		_o_._xdb_verify_unsafe_();
		titleid = _o_.titleid;
		level = _o_.level;
		apprentinfo = new HasApprent(_o_.apprentinfo, this, "apprentinfo");
		achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>();
		for (java.util.Map.Entry<Integer, xbean.ApprentceChieve> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), new ApprentceChieve(_e_.getValue(), this, "achievement"));
		mastercomment = _o_.mastercomment;
		apprentcomment = _o_.apprentcomment;
		baishitime = _o_.baishitime;
	}

	private void assign(CurrApprent.Data _o_) {
		titleid = _o_.titleid;
		level = _o_.level;
		apprentinfo = new HasApprent(_o_.apprentinfo, this, "apprentinfo");
		achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>();
		for (java.util.Map.Entry<Integer, xbean.ApprentceChieve> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), new ApprentceChieve(_e_.getValue(), this, "achievement"));
		mastercomment = _o_.mastercomment;
		apprentcomment = _o_.apprentcomment;
		baishitime = _o_.baishitime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(titleid);
		_os_.marshal(level);
		apprentinfo.marshal(_os_);
		_os_.compact_uint32(achievement.size());
		for (java.util.Map.Entry<Integer, xbean.ApprentceChieve> _e_ : achievement.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(mastercomment);
		_os_.marshal(apprentcomment);
		_os_.marshal(baishitime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		titleid = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		apprentinfo.unmarshal(_os_);
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.ApprentceChieve _v_ = new ApprentceChieve(0, this, "achievement");
				_v_.unmarshal(_os_);
				achievement.put(_k_, _v_);
			}
		}
		mastercomment = _os_.unmarshal_int();
		apprentcomment = _os_.unmarshal_int();
		baishitime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.CurrApprent copy() {
		_xdb_verify_unsafe_();
		return new CurrApprent(this);
	}

	@Override
	public xbean.CurrApprent toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CurrApprent toBean() {
		_xdb_verify_unsafe_();
		return new CurrApprent(this); // same as copy()
	}

	@Override
	public xbean.CurrApprent toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CurrApprent toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getTitleid() { // 徒弟的当前称谓
		_xdb_verify_unsafe_();
		return titleid;
	}

	@Override
	public int getLevel() { // 拜师时候的等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public xbean.HasApprent getApprentinfo() { // 徒弟的基本信息
		_xdb_verify_unsafe_();
		return apprentinfo;
	}

	@Override
	public java.util.Map<Integer, xbean.ApprentceChieve> getAchievement() { // 徒弟的各项成就
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "achievement"), achievement);
	}

	@Override
	public java.util.Map<Integer, xbean.ApprentceChieve> getAchievementAsData() { // 徒弟的各项成就
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.ApprentceChieve> achievement;
		CurrApprent _o_ = this;
		achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>();
		for (java.util.Map.Entry<Integer, xbean.ApprentceChieve> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), new ApprentceChieve.Data(_e_.getValue()));
		return achievement;
	}

	@Override
	public int getMastercomment() { // 师傅对徒弟的评价
		_xdb_verify_unsafe_();
		return mastercomment;
	}

	@Override
	public int getApprentcomment() { // 徒弟对师傅的评价
		_xdb_verify_unsafe_();
		return apprentcomment;
	}

	@Override
	public long getBaishitime() { // 
		_xdb_verify_unsafe_();
		return baishitime;
	}

	@Override
	public void setTitleid(int _v_) { // 徒弟的当前称谓
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "titleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, titleid) {
					public void rollback() { titleid = _xdb_saved; }
				};}});
		titleid = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 拜师时候的等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setMastercomment(int _v_) { // 师傅对徒弟的评价
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mastercomment") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, mastercomment) {
					public void rollback() { mastercomment = _xdb_saved; }
				};}});
		mastercomment = _v_;
	}

	@Override
	public void setApprentcomment(int _v_) { // 徒弟对师傅的评价
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "apprentcomment") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, apprentcomment) {
					public void rollback() { apprentcomment = _xdb_saved; }
				};}});
		apprentcomment = _v_;
	}

	@Override
	public void setBaishitime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "baishitime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, baishitime) {
					public void rollback() { baishitime = _xdb_saved; }
				};}});
		baishitime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CurrApprent _o_ = null;
		if ( _o1_ instanceof CurrApprent ) _o_ = (CurrApprent)_o1_;
		else if ( _o1_ instanceof CurrApprent.Const ) _o_ = ((CurrApprent.Const)_o1_).nThis();
		else return false;
		if (titleid != _o_.titleid) return false;
		if (level != _o_.level) return false;
		if (!apprentinfo.equals(_o_.apprentinfo)) return false;
		if (!achievement.equals(_o_.achievement)) return false;
		if (mastercomment != _o_.mastercomment) return false;
		if (apprentcomment != _o_.apprentcomment) return false;
		if (baishitime != _o_.baishitime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += titleid;
		_h_ += level;
		_h_ += apprentinfo.hashCode();
		_h_ += achievement.hashCode();
		_h_ += mastercomment;
		_h_ += apprentcomment;
		_h_ += baishitime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(titleid);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(apprentinfo);
		_sb_.append(",");
		_sb_.append(achievement);
		_sb_.append(",");
		_sb_.append(mastercomment);
		_sb_.append(",");
		_sb_.append(apprentcomment);
		_sb_.append(",");
		_sb_.append(baishitime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("titleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("apprentinfo"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("achievement"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mastercomment"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("apprentcomment"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("baishitime"));
		return lb;
	}

	private class Const implements xbean.CurrApprent {
		CurrApprent nThis() {
			return CurrApprent.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CurrApprent copy() {
			return CurrApprent.this.copy();
		}

		@Override
		public xbean.CurrApprent toData() {
			return CurrApprent.this.toData();
		}

		public xbean.CurrApprent toBean() {
			return CurrApprent.this.toBean();
		}

		@Override
		public xbean.CurrApprent toDataIf() {
			return CurrApprent.this.toDataIf();
		}

		public xbean.CurrApprent toBeanIf() {
			return CurrApprent.this.toBeanIf();
		}

		@Override
		public int getTitleid() { // 徒弟的当前称谓
			_xdb_verify_unsafe_();
			return titleid;
		}

		@Override
		public int getLevel() { // 拜师时候的等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public xbean.HasApprent getApprentinfo() { // 徒弟的基本信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(apprentinfo);
		}

		@Override
		public java.util.Map<Integer, xbean.ApprentceChieve> getAchievement() { // 徒弟的各项成就
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(achievement);
		}

		@Override
		public java.util.Map<Integer, xbean.ApprentceChieve> getAchievementAsData() { // 徒弟的各项成就
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.ApprentceChieve> achievement;
			CurrApprent _o_ = CurrApprent.this;
			achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>();
			for (java.util.Map.Entry<Integer, xbean.ApprentceChieve> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), new ApprentceChieve.Data(_e_.getValue()));
			return achievement;
		}

		@Override
		public int getMastercomment() { // 师傅对徒弟的评价
			_xdb_verify_unsafe_();
			return mastercomment;
		}

		@Override
		public int getApprentcomment() { // 徒弟对师傅的评价
			_xdb_verify_unsafe_();
			return apprentcomment;
		}

		@Override
		public long getBaishitime() { // 
			_xdb_verify_unsafe_();
			return baishitime;
		}

		@Override
		public void setTitleid(int _v_) { // 徒弟的当前称谓
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 拜师时候的等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMastercomment(int _v_) { // 师傅对徒弟的评价
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setApprentcomment(int _v_) { // 徒弟对师傅的评价
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBaishitime(long _v_) { // 
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
			return CurrApprent.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CurrApprent.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CurrApprent.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CurrApprent.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CurrApprent.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CurrApprent.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CurrApprent.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CurrApprent.this.hashCode();
		}

		@Override
		public String toString() {
			return CurrApprent.this.toString();
		}

	}

	public static final class Data implements xbean.CurrApprent {
		private int titleid; // 徒弟的当前称谓
		private int level; // 拜师时候的等级
		private xbean.HasApprent apprentinfo; // 徒弟的基本信息
		private java.util.HashMap<Integer, xbean.ApprentceChieve> achievement; // 徒弟的各项成就
		private int mastercomment; // 师傅对徒弟的评价
		private int apprentcomment; // 徒弟对师傅的评价
		private long baishitime; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			apprentinfo = new HasApprent.Data();
			achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>();
		}

		Data(xbean.CurrApprent _o1_) {
			if (_o1_ instanceof CurrApprent) assign((CurrApprent)_o1_);
			else if (_o1_ instanceof CurrApprent.Data) assign((CurrApprent.Data)_o1_);
			else if (_o1_ instanceof CurrApprent.Const) assign(((CurrApprent.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CurrApprent _o_) {
			titleid = _o_.titleid;
			level = _o_.level;
			apprentinfo = new HasApprent.Data(_o_.apprentinfo);
			achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>();
			for (java.util.Map.Entry<Integer, xbean.ApprentceChieve> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), new ApprentceChieve.Data(_e_.getValue()));
			mastercomment = _o_.mastercomment;
			apprentcomment = _o_.apprentcomment;
			baishitime = _o_.baishitime;
		}

		private void assign(CurrApprent.Data _o_) {
			titleid = _o_.titleid;
			level = _o_.level;
			apprentinfo = new HasApprent.Data(_o_.apprentinfo);
			achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>();
			for (java.util.Map.Entry<Integer, xbean.ApprentceChieve> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), new ApprentceChieve.Data(_e_.getValue()));
			mastercomment = _o_.mastercomment;
			apprentcomment = _o_.apprentcomment;
			baishitime = _o_.baishitime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(titleid);
			_os_.marshal(level);
			apprentinfo.marshal(_os_);
			_os_.compact_uint32(achievement.size());
			for (java.util.Map.Entry<Integer, xbean.ApprentceChieve> _e_ : achievement.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(mastercomment);
			_os_.marshal(apprentcomment);
			_os_.marshal(baishitime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			titleid = _os_.unmarshal_int();
			level = _os_.unmarshal_int();
			apprentinfo.unmarshal(_os_);
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					achievement = new java.util.HashMap<Integer, xbean.ApprentceChieve>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.ApprentceChieve _v_ = xbean.Pod.newApprentceChieveData();
					_v_.unmarshal(_os_);
					achievement.put(_k_, _v_);
				}
			}
			mastercomment = _os_.unmarshal_int();
			apprentcomment = _os_.unmarshal_int();
			baishitime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.CurrApprent copy() {
			return new Data(this);
		}

		@Override
		public xbean.CurrApprent toData() {
			return new Data(this);
		}

		public xbean.CurrApprent toBean() {
			return new CurrApprent(this, null, null);
		}

		@Override
		public xbean.CurrApprent toDataIf() {
			return this;
		}

		public xbean.CurrApprent toBeanIf() {
			return new CurrApprent(this, null, null);
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
		public int getTitleid() { // 徒弟的当前称谓
			return titleid;
		}

		@Override
		public int getLevel() { // 拜师时候的等级
			return level;
		}

		@Override
		public xbean.HasApprent getApprentinfo() { // 徒弟的基本信息
			return apprentinfo;
		}

		@Override
		public java.util.Map<Integer, xbean.ApprentceChieve> getAchievement() { // 徒弟的各项成就
			return achievement;
		}

		@Override
		public java.util.Map<Integer, xbean.ApprentceChieve> getAchievementAsData() { // 徒弟的各项成就
			return achievement;
		}

		@Override
		public int getMastercomment() { // 师傅对徒弟的评价
			return mastercomment;
		}

		@Override
		public int getApprentcomment() { // 徒弟对师傅的评价
			return apprentcomment;
		}

		@Override
		public long getBaishitime() { // 
			return baishitime;
		}

		@Override
		public void setTitleid(int _v_) { // 徒弟的当前称谓
			titleid = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 拜师时候的等级
			level = _v_;
		}

		@Override
		public void setMastercomment(int _v_) { // 师傅对徒弟的评价
			mastercomment = _v_;
		}

		@Override
		public void setApprentcomment(int _v_) { // 徒弟对师傅的评价
			apprentcomment = _v_;
		}

		@Override
		public void setBaishitime(long _v_) { // 
			baishitime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CurrApprent.Data)) return false;
			CurrApprent.Data _o_ = (CurrApprent.Data) _o1_;
			if (titleid != _o_.titleid) return false;
			if (level != _o_.level) return false;
			if (!apprentinfo.equals(_o_.apprentinfo)) return false;
			if (!achievement.equals(_o_.achievement)) return false;
			if (mastercomment != _o_.mastercomment) return false;
			if (apprentcomment != _o_.apprentcomment) return false;
			if (baishitime != _o_.baishitime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += titleid;
			_h_ += level;
			_h_ += apprentinfo.hashCode();
			_h_ += achievement.hashCode();
			_h_ += mastercomment;
			_h_ += apprentcomment;
			_h_ += baishitime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(titleid);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(apprentinfo);
			_sb_.append(",");
			_sb_.append(achievement);
			_sb_.append(",");
			_sb_.append(mastercomment);
			_sb_.append(",");
			_sb_.append(apprentcomment);
			_sb_.append(",");
			_sb_.append(baishitime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

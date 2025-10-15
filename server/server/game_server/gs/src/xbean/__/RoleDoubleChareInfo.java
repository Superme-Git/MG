
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleDoubleChareInfo extends mkdb.XBean implements xbean.RoleDoubleChareInfo {
	private long activetime; // 活动开始时间
	private java.util.HashMap<Integer, Integer> achievement; // value=1 表示充值过  商品的id -> flag
	private int flag; // 0=没有初始化过  1=初始化过 2=网页充值临时初始化

	@Override
	public void _reset_unsafe_() {
		activetime = 0L;
		achievement.clear();
		flag = 0;
	}

	RoleDoubleChareInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		achievement = new java.util.HashMap<Integer, Integer>();
	}

	public RoleDoubleChareInfo() {
		this(0, null, null);
	}

	public RoleDoubleChareInfo(RoleDoubleChareInfo _o_) {
		this(_o_, null, null);
	}

	RoleDoubleChareInfo(xbean.RoleDoubleChareInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleDoubleChareInfo) assign((RoleDoubleChareInfo)_o1_);
		else if (_o1_ instanceof RoleDoubleChareInfo.Data) assign((RoleDoubleChareInfo.Data)_o1_);
		else if (_o1_ instanceof RoleDoubleChareInfo.Const) assign(((RoleDoubleChareInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleDoubleChareInfo _o_) {
		_o_._xdb_verify_unsafe_();
		activetime = _o_.activetime;
		achievement = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), _e_.getValue());
		flag = _o_.flag;
	}

	private void assign(RoleDoubleChareInfo.Data _o_) {
		activetime = _o_.activetime;
		achievement = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), _e_.getValue());
		flag = _o_.flag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(activetime);
		_os_.compact_uint32(achievement.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : achievement.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(flag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		activetime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				achievement = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				achievement.put(_k_, _v_);
			}
		}
		flag = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RoleDoubleChareInfo copy() {
		_xdb_verify_unsafe_();
		return new RoleDoubleChareInfo(this);
	}

	@Override
	public xbean.RoleDoubleChareInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleDoubleChareInfo toBean() {
		_xdb_verify_unsafe_();
		return new RoleDoubleChareInfo(this); // same as copy()
	}

	@Override
	public xbean.RoleDoubleChareInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleDoubleChareInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getActivetime() { // 活动开始时间
		_xdb_verify_unsafe_();
		return activetime;
	}

	@Override
	public java.util.Map<Integer, Integer> getAchievement() { // value=1 表示充值过  商品的id -> flag
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "achievement"), achievement);
	}

	@Override
	public java.util.Map<Integer, Integer> getAchievementAsData() { // value=1 表示充值过  商品的id -> flag
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> achievement;
		RoleDoubleChareInfo _o_ = this;
		achievement = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), _e_.getValue());
		return achievement;
	}

	@Override
	public int getFlag() { // 0=没有初始化过  1=初始化过 2=网页充值临时初始化
		_xdb_verify_unsafe_();
		return flag;
	}

	@Override
	public void setActivetime(long _v_) { // 活动开始时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, activetime) {
					public void rollback() { activetime = _xdb_saved; }
				};}});
		activetime = _v_;
	}

	@Override
	public void setFlag(int _v_) { // 0=没有初始化过  1=初始化过 2=网页充值临时初始化
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "flag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, flag) {
					public void rollback() { flag = _xdb_saved; }
				};}});
		flag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleDoubleChareInfo _o_ = null;
		if ( _o1_ instanceof RoleDoubleChareInfo ) _o_ = (RoleDoubleChareInfo)_o1_;
		else if ( _o1_ instanceof RoleDoubleChareInfo.Const ) _o_ = ((RoleDoubleChareInfo.Const)_o1_).nThis();
		else return false;
		if (activetime != _o_.activetime) return false;
		if (!achievement.equals(_o_.achievement)) return false;
		if (flag != _o_.flag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += activetime;
		_h_ += achievement.hashCode();
		_h_ += flag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(activetime);
		_sb_.append(",");
		_sb_.append(achievement);
		_sb_.append(",");
		_sb_.append(flag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activetime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("achievement"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("flag"));
		return lb;
	}

	private class Const implements xbean.RoleDoubleChareInfo {
		RoleDoubleChareInfo nThis() {
			return RoleDoubleChareInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleDoubleChareInfo copy() {
			return RoleDoubleChareInfo.this.copy();
		}

		@Override
		public xbean.RoleDoubleChareInfo toData() {
			return RoleDoubleChareInfo.this.toData();
		}

		public xbean.RoleDoubleChareInfo toBean() {
			return RoleDoubleChareInfo.this.toBean();
		}

		@Override
		public xbean.RoleDoubleChareInfo toDataIf() {
			return RoleDoubleChareInfo.this.toDataIf();
		}

		public xbean.RoleDoubleChareInfo toBeanIf() {
			return RoleDoubleChareInfo.this.toBeanIf();
		}

		@Override
		public long getActivetime() { // 活动开始时间
			_xdb_verify_unsafe_();
			return activetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getAchievement() { // value=1 表示充值过  商品的id -> flag
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(achievement);
		}

		@Override
		public java.util.Map<Integer, Integer> getAchievementAsData() { // value=1 表示充值过  商品的id -> flag
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> achievement;
			RoleDoubleChareInfo _o_ = RoleDoubleChareInfo.this;
			achievement = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), _e_.getValue());
			return achievement;
		}

		@Override
		public int getFlag() { // 0=没有初始化过  1=初始化过 2=网页充值临时初始化
			_xdb_verify_unsafe_();
			return flag;
		}

		@Override
		public void setActivetime(long _v_) { // 活动开始时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlag(int _v_) { // 0=没有初始化过  1=初始化过 2=网页充值临时初始化
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
			return RoleDoubleChareInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleDoubleChareInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleDoubleChareInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleDoubleChareInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleDoubleChareInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleDoubleChareInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleDoubleChareInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleDoubleChareInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleDoubleChareInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RoleDoubleChareInfo {
		private long activetime; // 活动开始时间
		private java.util.HashMap<Integer, Integer> achievement; // value=1 表示充值过  商品的id -> flag
		private int flag; // 0=没有初始化过  1=初始化过 2=网页充值临时初始化

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			achievement = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.RoleDoubleChareInfo _o1_) {
			if (_o1_ instanceof RoleDoubleChareInfo) assign((RoleDoubleChareInfo)_o1_);
			else if (_o1_ instanceof RoleDoubleChareInfo.Data) assign((RoleDoubleChareInfo.Data)_o1_);
			else if (_o1_ instanceof RoleDoubleChareInfo.Const) assign(((RoleDoubleChareInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleDoubleChareInfo _o_) {
			activetime = _o_.activetime;
			achievement = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), _e_.getValue());
			flag = _o_.flag;
		}

		private void assign(RoleDoubleChareInfo.Data _o_) {
			activetime = _o_.activetime;
			achievement = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), _e_.getValue());
			flag = _o_.flag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(activetime);
			_os_.compact_uint32(achievement.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : achievement.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(flag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			activetime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					achievement = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					achievement.put(_k_, _v_);
				}
			}
			flag = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RoleDoubleChareInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleDoubleChareInfo toData() {
			return new Data(this);
		}

		public xbean.RoleDoubleChareInfo toBean() {
			return new RoleDoubleChareInfo(this, null, null);
		}

		@Override
		public xbean.RoleDoubleChareInfo toDataIf() {
			return this;
		}

		public xbean.RoleDoubleChareInfo toBeanIf() {
			return new RoleDoubleChareInfo(this, null, null);
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
		public long getActivetime() { // 活动开始时间
			return activetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getAchievement() { // value=1 表示充值过  商品的id -> flag
			return achievement;
		}

		@Override
		public java.util.Map<Integer, Integer> getAchievementAsData() { // value=1 表示充值过  商品的id -> flag
			return achievement;
		}

		@Override
		public int getFlag() { // 0=没有初始化过  1=初始化过 2=网页充值临时初始化
			return flag;
		}

		@Override
		public void setActivetime(long _v_) { // 活动开始时间
			activetime = _v_;
		}

		@Override
		public void setFlag(int _v_) { // 0=没有初始化过  1=初始化过 2=网页充值临时初始化
			flag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleDoubleChareInfo.Data)) return false;
			RoleDoubleChareInfo.Data _o_ = (RoleDoubleChareInfo.Data) _o1_;
			if (activetime != _o_.activetime) return false;
			if (!achievement.equals(_o_.achievement)) return false;
			if (flag != _o_.flag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += activetime;
			_h_ += achievement.hashCode();
			_h_ += flag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(activetime);
			_sb_.append(",");
			_sb_.append(achievement);
			_sb_.append(",");
			_sb_.append(flag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

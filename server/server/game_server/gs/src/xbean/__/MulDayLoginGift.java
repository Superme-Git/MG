
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MulDayLoginGift extends mkdb.XBean implements xbean.MulDayLoginGift {
	private int logindays; // 累计登录天数
	private long logintime; // 更新时间
	private java.util.HashMap<Integer, Long> rewardmap; // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))

	@Override
	public void _reset_unsafe_() {
		logindays = 0;
		logintime = 0L;
		rewardmap.clear();
	}

	MulDayLoginGift(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rewardmap = new java.util.HashMap<Integer, Long>();
	}

	public MulDayLoginGift() {
		this(0, null, null);
	}

	public MulDayLoginGift(MulDayLoginGift _o_) {
		this(_o_, null, null);
	}

	MulDayLoginGift(xbean.MulDayLoginGift _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MulDayLoginGift) assign((MulDayLoginGift)_o1_);
		else if (_o1_ instanceof MulDayLoginGift.Data) assign((MulDayLoginGift.Data)_o1_);
		else if (_o1_ instanceof MulDayLoginGift.Const) assign(((MulDayLoginGift.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MulDayLoginGift _o_) {
		_o_._xdb_verify_unsafe_();
		logindays = _o_.logindays;
		logintime = _o_.logintime;
		rewardmap = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
			rewardmap.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(MulDayLoginGift.Data _o_) {
		logindays = _o_.logindays;
		logintime = _o_.logintime;
		rewardmap = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
			rewardmap.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(logindays);
		_os_.marshal(logintime);
		_os_.compact_uint32(rewardmap.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : rewardmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		logindays = _os_.unmarshal_int();
		logintime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				rewardmap = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				rewardmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.MulDayLoginGift copy() {
		_xdb_verify_unsafe_();
		return new MulDayLoginGift(this);
	}

	@Override
	public xbean.MulDayLoginGift toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MulDayLoginGift toBean() {
		_xdb_verify_unsafe_();
		return new MulDayLoginGift(this); // same as copy()
	}

	@Override
	public xbean.MulDayLoginGift toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MulDayLoginGift toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getLogindays() { // 累计登录天数
		_xdb_verify_unsafe_();
		return logindays;
	}

	@Override
	public long getLogintime() { // 更新时间
		_xdb_verify_unsafe_();
		return logintime;
	}

	@Override
	public java.util.Map<Integer, Long> getRewardmap() { // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "rewardmap"), rewardmap);
	}

	@Override
	public java.util.Map<Integer, Long> getRewardmapAsData() { // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> rewardmap;
		MulDayLoginGift _o_ = this;
		rewardmap = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
			rewardmap.put(_e_.getKey(), _e_.getValue());
		return rewardmap;
	}

	@Override
	public void setLogindays(int _v_) { // 累计登录天数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "logindays") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, logindays) {
					public void rollback() { logindays = _xdb_saved; }
				};}});
		logindays = _v_;
	}

	@Override
	public void setLogintime(long _v_) { // 更新时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "logintime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, logintime) {
					public void rollback() { logintime = _xdb_saved; }
				};}});
		logintime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MulDayLoginGift _o_ = null;
		if ( _o1_ instanceof MulDayLoginGift ) _o_ = (MulDayLoginGift)_o1_;
		else if ( _o1_ instanceof MulDayLoginGift.Const ) _o_ = ((MulDayLoginGift.Const)_o1_).nThis();
		else return false;
		if (logindays != _o_.logindays) return false;
		if (logintime != _o_.logintime) return false;
		if (!rewardmap.equals(_o_.rewardmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += logindays;
		_h_ += logintime;
		_h_ += rewardmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(logindays);
		_sb_.append(",");
		_sb_.append(logintime);
		_sb_.append(",");
		_sb_.append(rewardmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("logindays"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("logintime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("rewardmap"));
		return lb;
	}

	private class Const implements xbean.MulDayLoginGift {
		MulDayLoginGift nThis() {
			return MulDayLoginGift.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MulDayLoginGift copy() {
			return MulDayLoginGift.this.copy();
		}

		@Override
		public xbean.MulDayLoginGift toData() {
			return MulDayLoginGift.this.toData();
		}

		public xbean.MulDayLoginGift toBean() {
			return MulDayLoginGift.this.toBean();
		}

		@Override
		public xbean.MulDayLoginGift toDataIf() {
			return MulDayLoginGift.this.toDataIf();
		}

		public xbean.MulDayLoginGift toBeanIf() {
			return MulDayLoginGift.this.toBeanIf();
		}

		@Override
		public int getLogindays() { // 累计登录天数
			_xdb_verify_unsafe_();
			return logindays;
		}

		@Override
		public long getLogintime() { // 更新时间
			_xdb_verify_unsafe_();
			return logintime;
		}

		@Override
		public java.util.Map<Integer, Long> getRewardmap() { // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(rewardmap);
		}

		@Override
		public java.util.Map<Integer, Long> getRewardmapAsData() { // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> rewardmap;
			MulDayLoginGift _o_ = MulDayLoginGift.this;
			rewardmap = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
				rewardmap.put(_e_.getKey(), _e_.getValue());
			return rewardmap;
		}

		@Override
		public void setLogindays(int _v_) { // 累计登录天数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLogintime(long _v_) { // 更新时间
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
			return MulDayLoginGift.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MulDayLoginGift.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MulDayLoginGift.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MulDayLoginGift.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MulDayLoginGift.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MulDayLoginGift.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MulDayLoginGift.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MulDayLoginGift.this.hashCode();
		}

		@Override
		public String toString() {
			return MulDayLoginGift.this.toString();
		}

	}

	public static final class Data implements xbean.MulDayLoginGift {
		private int logindays; // 累计登录天数
		private long logintime; // 更新时间
		private java.util.HashMap<Integer, Long> rewardmap; // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rewardmap = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.MulDayLoginGift _o1_) {
			if (_o1_ instanceof MulDayLoginGift) assign((MulDayLoginGift)_o1_);
			else if (_o1_ instanceof MulDayLoginGift.Data) assign((MulDayLoginGift.Data)_o1_);
			else if (_o1_ instanceof MulDayLoginGift.Const) assign(((MulDayLoginGift.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MulDayLoginGift _o_) {
			logindays = _o_.logindays;
			logintime = _o_.logintime;
			rewardmap = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
				rewardmap.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(MulDayLoginGift.Data _o_) {
			logindays = _o_.logindays;
			logintime = _o_.logintime;
			rewardmap = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.rewardmap.entrySet())
				rewardmap.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(logindays);
			_os_.marshal(logintime);
			_os_.compact_uint32(rewardmap.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : rewardmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			logindays = _os_.unmarshal_int();
			logintime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					rewardmap = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					rewardmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.MulDayLoginGift copy() {
			return new Data(this);
		}

		@Override
		public xbean.MulDayLoginGift toData() {
			return new Data(this);
		}

		public xbean.MulDayLoginGift toBean() {
			return new MulDayLoginGift(this, null, null);
		}

		@Override
		public xbean.MulDayLoginGift toDataIf() {
			return this;
		}

		public xbean.MulDayLoginGift toBeanIf() {
			return new MulDayLoginGift(this, null, null);
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
		public int getLogindays() { // 累计登录天数
			return logindays;
		}

		@Override
		public long getLogintime() { // 更新时间
			return logintime;
		}

		@Override
		public java.util.Map<Integer, Long> getRewardmap() { // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))
			return rewardmap;
		}

		@Override
		public java.util.Map<Integer, Long> getRewardmapAsData() { // 七日登录奖励(key-奖励ID，val-领取时间(0表示未领取))
			return rewardmap;
		}

		@Override
		public void setLogindays(int _v_) { // 累计登录天数
			logindays = _v_;
		}

		@Override
		public void setLogintime(long _v_) { // 更新时间
			logintime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MulDayLoginGift.Data)) return false;
			MulDayLoginGift.Data _o_ = (MulDayLoginGift.Data) _o1_;
			if (logindays != _o_.logindays) return false;
			if (logintime != _o_.logintime) return false;
			if (!rewardmap.equals(_o_.rewardmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += logindays;
			_h_ += logintime;
			_h_ += rewardmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(logindays);
			_sb_.append(",");
			_sb_.append(logintime);
			_sb_.append(",");
			_sb_.append(rewardmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

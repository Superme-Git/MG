
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleQuitStatistics extends mkdb.XBean implements xbean.RoleQuitStatistics {
	private long money; // 
	private int exp; // 
	private java.util.HashMap<Integer, Long> currency; // 
	private long lastcountdate; // 最近统计钱,储备金,经验的时间
	private int lastrewardidx; // 上次领取奖励序号
	private long rewarddate; // 上次领取上线奖励

	@Override
	public void _reset_unsafe_() {
		money = 0L;
		exp = 0;
		currency.clear();
		lastcountdate = 0L;
		lastrewardidx = 0;
		rewarddate = 0L;
	}

	RoleQuitStatistics(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		currency = new java.util.HashMap<Integer, Long>();
	}

	public RoleQuitStatistics() {
		this(0, null, null);
	}

	public RoleQuitStatistics(RoleQuitStatistics _o_) {
		this(_o_, null, null);
	}

	RoleQuitStatistics(xbean.RoleQuitStatistics _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleQuitStatistics) assign((RoleQuitStatistics)_o1_);
		else if (_o1_ instanceof RoleQuitStatistics.Data) assign((RoleQuitStatistics.Data)_o1_);
		else if (_o1_ instanceof RoleQuitStatistics.Const) assign(((RoleQuitStatistics.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleQuitStatistics _o_) {
		_o_._xdb_verify_unsafe_();
		money = _o_.money;
		exp = _o_.exp;
		currency = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
			currency.put(_e_.getKey(), _e_.getValue());
		lastcountdate = _o_.lastcountdate;
		lastrewardidx = _o_.lastrewardidx;
		rewarddate = _o_.rewarddate;
	}

	private void assign(RoleQuitStatistics.Data _o_) {
		money = _o_.money;
		exp = _o_.exp;
		currency = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
			currency.put(_e_.getKey(), _e_.getValue());
		lastcountdate = _o_.lastcountdate;
		lastrewardidx = _o_.lastrewardidx;
		rewarddate = _o_.rewarddate;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(money);
		_os_.marshal(exp);
		_os_.compact_uint32(currency.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : currency.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(lastcountdate);
		_os_.marshal(lastrewardidx);
		_os_.marshal(rewarddate);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		money = _os_.unmarshal_long();
		exp = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				currency = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				currency.put(_k_, _v_);
			}
		}
		lastcountdate = _os_.unmarshal_long();
		lastrewardidx = _os_.unmarshal_int();
		rewarddate = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleQuitStatistics copy() {
		_xdb_verify_unsafe_();
		return new RoleQuitStatistics(this);
	}

	@Override
	public xbean.RoleQuitStatistics toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleQuitStatistics toBean() {
		_xdb_verify_unsafe_();
		return new RoleQuitStatistics(this); // same as copy()
	}

	@Override
	public xbean.RoleQuitStatistics toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleQuitStatistics toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getMoney() { // 
		_xdb_verify_unsafe_();
		return money;
	}

	@Override
	public int getExp() { // 
		_xdb_verify_unsafe_();
		return exp;
	}

	@Override
	public java.util.Map<Integer, Long> getCurrency() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "currency"), currency);
	}

	@Override
	public java.util.Map<Integer, Long> getCurrencyAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> currency;
		RoleQuitStatistics _o_ = this;
		currency = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
			currency.put(_e_.getKey(), _e_.getValue());
		return currency;
	}

	@Override
	public long getLastcountdate() { // 最近统计钱,储备金,经验的时间
		_xdb_verify_unsafe_();
		return lastcountdate;
	}

	@Override
	public int getLastrewardidx() { // 上次领取奖励序号
		_xdb_verify_unsafe_();
		return lastrewardidx;
	}

	@Override
	public long getRewarddate() { // 上次领取上线奖励
		_xdb_verify_unsafe_();
		return rewarddate;
	}

	@Override
	public void setMoney(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "money") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, money) {
					public void rollback() { money = _xdb_saved; }
				};}});
		money = _v_;
	}

	@Override
	public void setExp(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "exp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, exp) {
					public void rollback() { exp = _xdb_saved; }
				};}});
		exp = _v_;
	}

	@Override
	public void setLastcountdate(long _v_) { // 最近统计钱,储备金,经验的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastcountdate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastcountdate) {
					public void rollback() { lastcountdate = _xdb_saved; }
				};}});
		lastcountdate = _v_;
	}

	@Override
	public void setLastrewardidx(int _v_) { // 上次领取奖励序号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastrewardidx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastrewardidx) {
					public void rollback() { lastrewardidx = _xdb_saved; }
				};}});
		lastrewardidx = _v_;
	}

	@Override
	public void setRewarddate(long _v_) { // 上次领取上线奖励
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rewarddate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, rewarddate) {
					public void rollback() { rewarddate = _xdb_saved; }
				};}});
		rewarddate = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleQuitStatistics _o_ = null;
		if ( _o1_ instanceof RoleQuitStatistics ) _o_ = (RoleQuitStatistics)_o1_;
		else if ( _o1_ instanceof RoleQuitStatistics.Const ) _o_ = ((RoleQuitStatistics.Const)_o1_).nThis();
		else return false;
		if (money != _o_.money) return false;
		if (exp != _o_.exp) return false;
		if (!currency.equals(_o_.currency)) return false;
		if (lastcountdate != _o_.lastcountdate) return false;
		if (lastrewardidx != _o_.lastrewardidx) return false;
		if (rewarddate != _o_.rewarddate) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += money;
		_h_ += exp;
		_h_ += currency.hashCode();
		_h_ += lastcountdate;
		_h_ += lastrewardidx;
		_h_ += rewarddate;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(money);
		_sb_.append(",");
		_sb_.append(exp);
		_sb_.append(",");
		_sb_.append(currency);
		_sb_.append(",");
		_sb_.append(lastcountdate);
		_sb_.append(",");
		_sb_.append(lastrewardidx);
		_sb_.append(",");
		_sb_.append(rewarddate);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("money"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("exp"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("currency"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastcountdate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastrewardidx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rewarddate"));
		return lb;
	}

	private class Const implements xbean.RoleQuitStatistics {
		RoleQuitStatistics nThis() {
			return RoleQuitStatistics.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleQuitStatistics copy() {
			return RoleQuitStatistics.this.copy();
		}

		@Override
		public xbean.RoleQuitStatistics toData() {
			return RoleQuitStatistics.this.toData();
		}

		public xbean.RoleQuitStatistics toBean() {
			return RoleQuitStatistics.this.toBean();
		}

		@Override
		public xbean.RoleQuitStatistics toDataIf() {
			return RoleQuitStatistics.this.toDataIf();
		}

		public xbean.RoleQuitStatistics toBeanIf() {
			return RoleQuitStatistics.this.toBeanIf();
		}

		@Override
		public long getMoney() { // 
			_xdb_verify_unsafe_();
			return money;
		}

		@Override
		public int getExp() { // 
			_xdb_verify_unsafe_();
			return exp;
		}

		@Override
		public java.util.Map<Integer, Long> getCurrency() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(currency);
		}

		@Override
		public java.util.Map<Integer, Long> getCurrencyAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> currency;
			RoleQuitStatistics _o_ = RoleQuitStatistics.this;
			currency = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
				currency.put(_e_.getKey(), _e_.getValue());
			return currency;
		}

		@Override
		public long getLastcountdate() { // 最近统计钱,储备金,经验的时间
			_xdb_verify_unsafe_();
			return lastcountdate;
		}

		@Override
		public int getLastrewardidx() { // 上次领取奖励序号
			_xdb_verify_unsafe_();
			return lastrewardidx;
		}

		@Override
		public long getRewarddate() { // 上次领取上线奖励
			_xdb_verify_unsafe_();
			return rewarddate;
		}

		@Override
		public void setMoney(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExp(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastcountdate(long _v_) { // 最近统计钱,储备金,经验的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastrewardidx(int _v_) { // 上次领取奖励序号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRewarddate(long _v_) { // 上次领取上线奖励
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
			return RoleQuitStatistics.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleQuitStatistics.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleQuitStatistics.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleQuitStatistics.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleQuitStatistics.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleQuitStatistics.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleQuitStatistics.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleQuitStatistics.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleQuitStatistics.this.toString();
		}

	}

	public static final class Data implements xbean.RoleQuitStatistics {
		private long money; // 
		private int exp; // 
		private java.util.HashMap<Integer, Long> currency; // 
		private long lastcountdate; // 最近统计钱,储备金,经验的时间
		private int lastrewardidx; // 上次领取奖励序号
		private long rewarddate; // 上次领取上线奖励

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			currency = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.RoleQuitStatistics _o1_) {
			if (_o1_ instanceof RoleQuitStatistics) assign((RoleQuitStatistics)_o1_);
			else if (_o1_ instanceof RoleQuitStatistics.Data) assign((RoleQuitStatistics.Data)_o1_);
			else if (_o1_ instanceof RoleQuitStatistics.Const) assign(((RoleQuitStatistics.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleQuitStatistics _o_) {
			money = _o_.money;
			exp = _o_.exp;
			currency = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
				currency.put(_e_.getKey(), _e_.getValue());
			lastcountdate = _o_.lastcountdate;
			lastrewardidx = _o_.lastrewardidx;
			rewarddate = _o_.rewarddate;
		}

		private void assign(RoleQuitStatistics.Data _o_) {
			money = _o_.money;
			exp = _o_.exp;
			currency = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
				currency.put(_e_.getKey(), _e_.getValue());
			lastcountdate = _o_.lastcountdate;
			lastrewardidx = _o_.lastrewardidx;
			rewarddate = _o_.rewarddate;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(money);
			_os_.marshal(exp);
			_os_.compact_uint32(currency.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : currency.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(lastcountdate);
			_os_.marshal(lastrewardidx);
			_os_.marshal(rewarddate);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			money = _os_.unmarshal_long();
			exp = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					currency = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					currency.put(_k_, _v_);
				}
			}
			lastcountdate = _os_.unmarshal_long();
			lastrewardidx = _os_.unmarshal_int();
			rewarddate = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleQuitStatistics copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleQuitStatistics toData() {
			return new Data(this);
		}

		public xbean.RoleQuitStatistics toBean() {
			return new RoleQuitStatistics(this, null, null);
		}

		@Override
		public xbean.RoleQuitStatistics toDataIf() {
			return this;
		}

		public xbean.RoleQuitStatistics toBeanIf() {
			return new RoleQuitStatistics(this, null, null);
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
		public long getMoney() { // 
			return money;
		}

		@Override
		public int getExp() { // 
			return exp;
		}

		@Override
		public java.util.Map<Integer, Long> getCurrency() { // 
			return currency;
		}

		@Override
		public java.util.Map<Integer, Long> getCurrencyAsData() { // 
			return currency;
		}

		@Override
		public long getLastcountdate() { // 最近统计钱,储备金,经验的时间
			return lastcountdate;
		}

		@Override
		public int getLastrewardidx() { // 上次领取奖励序号
			return lastrewardidx;
		}

		@Override
		public long getRewarddate() { // 上次领取上线奖励
			return rewarddate;
		}

		@Override
		public void setMoney(long _v_) { // 
			money = _v_;
		}

		@Override
		public void setExp(int _v_) { // 
			exp = _v_;
		}

		@Override
		public void setLastcountdate(long _v_) { // 最近统计钱,储备金,经验的时间
			lastcountdate = _v_;
		}

		@Override
		public void setLastrewardidx(int _v_) { // 上次领取奖励序号
			lastrewardidx = _v_;
		}

		@Override
		public void setRewarddate(long _v_) { // 上次领取上线奖励
			rewarddate = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleQuitStatistics.Data)) return false;
			RoleQuitStatistics.Data _o_ = (RoleQuitStatistics.Data) _o1_;
			if (money != _o_.money) return false;
			if (exp != _o_.exp) return false;
			if (!currency.equals(_o_.currency)) return false;
			if (lastcountdate != _o_.lastcountdate) return false;
			if (lastrewardidx != _o_.lastrewardidx) return false;
			if (rewarddate != _o_.rewarddate) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += money;
			_h_ += exp;
			_h_ += currency.hashCode();
			_h_ += lastcountdate;
			_h_ += lastrewardidx;
			_h_ += rewarddate;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(money);
			_sb_.append(",");
			_sb_.append(exp);
			_sb_.append(",");
			_sb_.append(currency);
			_sb_.append(",");
			_sb_.append(lastcountdate);
			_sb_.append(",");
			_sb_.append(lastrewardidx);
			_sb_.append(",");
			_sb_.append(rewarddate);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

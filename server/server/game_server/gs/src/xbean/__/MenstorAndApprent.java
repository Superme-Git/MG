
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MenstorAndApprent extends mkdb.XBean implements xbean.MenstorAndApprent {
	private int flag; // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
	private int menstortitleid; // 为师时的titleId 表apprents 为自己的徒弟信息表
	private long firstbaishitime; // 第一个出师徒弟的拜师时间
	private long firstapprentid; // 第一个出师徒弟的id
	private long shidezhi; // 师傅的师德值
	private long menstorid; // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
	private long bashitime; // 罢师时间
	private int bashiflag; // 1=师傅7天没登录罢师 2=强行罢师
	private int eqipmentscore; // 拜师时候的装备评分
	private java.util.HashMap<Integer, Integer> achievement; // 每达成一项就要记录一项
	private long baishitime; // 拜师时间
	private long lastgivefushitime; // 上次获得符石的时间
	private long fushi; // 获得符石数量

	@Override
	public void _reset_unsafe_() {
		flag = 0;
		menstortitleid = 0;
		firstbaishitime = 0L;
		firstapprentid = 0L;
		shidezhi = 0L;
		menstorid = 0L;
		bashitime = 0L;
		bashiflag = 0;
		eqipmentscore = 0;
		achievement.clear();
		baishitime = 0L;
		lastgivefushitime = 0L;
		fushi = 0L;
	}

	MenstorAndApprent(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		achievement = new java.util.HashMap<Integer, Integer>();
	}

	public MenstorAndApprent() {
		this(0, null, null);
	}

	public MenstorAndApprent(MenstorAndApprent _o_) {
		this(_o_, null, null);
	}

	MenstorAndApprent(xbean.MenstorAndApprent _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MenstorAndApprent) assign((MenstorAndApprent)_o1_);
		else if (_o1_ instanceof MenstorAndApprent.Data) assign((MenstorAndApprent.Data)_o1_);
		else if (_o1_ instanceof MenstorAndApprent.Const) assign(((MenstorAndApprent.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MenstorAndApprent _o_) {
		_o_._xdb_verify_unsafe_();
		flag = _o_.flag;
		menstortitleid = _o_.menstortitleid;
		firstbaishitime = _o_.firstbaishitime;
		firstapprentid = _o_.firstapprentid;
		shidezhi = _o_.shidezhi;
		menstorid = _o_.menstorid;
		bashitime = _o_.bashitime;
		bashiflag = _o_.bashiflag;
		eqipmentscore = _o_.eqipmentscore;
		achievement = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), _e_.getValue());
		baishitime = _o_.baishitime;
		lastgivefushitime = _o_.lastgivefushitime;
		fushi = _o_.fushi;
	}

	private void assign(MenstorAndApprent.Data _o_) {
		flag = _o_.flag;
		menstortitleid = _o_.menstortitleid;
		firstbaishitime = _o_.firstbaishitime;
		firstapprentid = _o_.firstapprentid;
		shidezhi = _o_.shidezhi;
		menstorid = _o_.menstorid;
		bashitime = _o_.bashitime;
		bashiflag = _o_.bashiflag;
		eqipmentscore = _o_.eqipmentscore;
		achievement = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), _e_.getValue());
		baishitime = _o_.baishitime;
		lastgivefushitime = _o_.lastgivefushitime;
		fushi = _o_.fushi;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(flag);
		_os_.marshal(menstortitleid);
		_os_.marshal(firstbaishitime);
		_os_.marshal(firstapprentid);
		_os_.marshal(shidezhi);
		_os_.marshal(menstorid);
		_os_.marshal(bashitime);
		_os_.marshal(bashiflag);
		_os_.marshal(eqipmentscore);
		_os_.compact_uint32(achievement.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : achievement.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(baishitime);
		_os_.marshal(lastgivefushitime);
		_os_.marshal(fushi);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		flag = _os_.unmarshal_int();
		menstortitleid = _os_.unmarshal_int();
		firstbaishitime = _os_.unmarshal_long();
		firstapprentid = _os_.unmarshal_long();
		shidezhi = _os_.unmarshal_long();
		menstorid = _os_.unmarshal_long();
		bashitime = _os_.unmarshal_long();
		bashiflag = _os_.unmarshal_int();
		eqipmentscore = _os_.unmarshal_int();
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
		baishitime = _os_.unmarshal_long();
		lastgivefushitime = _os_.unmarshal_long();
		fushi = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.MenstorAndApprent copy() {
		_xdb_verify_unsafe_();
		return new MenstorAndApprent(this);
	}

	@Override
	public xbean.MenstorAndApprent toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MenstorAndApprent toBean() {
		_xdb_verify_unsafe_();
		return new MenstorAndApprent(this); // same as copy()
	}

	@Override
	public xbean.MenstorAndApprent toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MenstorAndApprent toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getFlag() { // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
		_xdb_verify_unsafe_();
		return flag;
	}

	@Override
	public int getMenstortitleid() { // 为师时的titleId 表apprents 为自己的徒弟信息表
		_xdb_verify_unsafe_();
		return menstortitleid;
	}

	@Override
	public long getFirstbaishitime() { // 第一个出师徒弟的拜师时间
		_xdb_verify_unsafe_();
		return firstbaishitime;
	}

	@Override
	public long getFirstapprentid() { // 第一个出师徒弟的id
		_xdb_verify_unsafe_();
		return firstapprentid;
	}

	@Override
	public long getShidezhi() { // 师傅的师德值
		_xdb_verify_unsafe_();
		return shidezhi;
	}

	@Override
	public long getMenstorid() { // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
		_xdb_verify_unsafe_();
		return menstorid;
	}

	@Override
	public long getBashitime() { // 罢师时间
		_xdb_verify_unsafe_();
		return bashitime;
	}

	@Override
	public int getBashiflag() { // 1=师傅7天没登录罢师 2=强行罢师
		_xdb_verify_unsafe_();
		return bashiflag;
	}

	@Override
	public int getEqipmentscore() { // 拜师时候的装备评分
		_xdb_verify_unsafe_();
		return eqipmentscore;
	}

	@Override
	public java.util.Map<Integer, Integer> getAchievement() { // 每达成一项就要记录一项
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "achievement"), achievement);
	}

	@Override
	public java.util.Map<Integer, Integer> getAchievementAsData() { // 每达成一项就要记录一项
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> achievement;
		MenstorAndApprent _o_ = this;
		achievement = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
			achievement.put(_e_.getKey(), _e_.getValue());
		return achievement;
	}

	@Override
	public long getBaishitime() { // 拜师时间
		_xdb_verify_unsafe_();
		return baishitime;
	}

	@Override
	public long getLastgivefushitime() { // 上次获得符石的时间
		_xdb_verify_unsafe_();
		return lastgivefushitime;
	}

	@Override
	public long getFushi() { // 获得符石数量
		_xdb_verify_unsafe_();
		return fushi;
	}

	@Override
	public void setFlag(int _v_) { // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "flag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, flag) {
					public void rollback() { flag = _xdb_saved; }
				};}});
		flag = _v_;
	}

	@Override
	public void setMenstortitleid(int _v_) { // 为师时的titleId 表apprents 为自己的徒弟信息表
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "menstortitleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, menstortitleid) {
					public void rollback() { menstortitleid = _xdb_saved; }
				};}});
		menstortitleid = _v_;
	}

	@Override
	public void setFirstbaishitime(long _v_) { // 第一个出师徒弟的拜师时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstbaishitime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, firstbaishitime) {
					public void rollback() { firstbaishitime = _xdb_saved; }
				};}});
		firstbaishitime = _v_;
	}

	@Override
	public void setFirstapprentid(long _v_) { // 第一个出师徒弟的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstapprentid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, firstapprentid) {
					public void rollback() { firstapprentid = _xdb_saved; }
				};}});
		firstapprentid = _v_;
	}

	@Override
	public void setShidezhi(long _v_) { // 师傅的师德值
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shidezhi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, shidezhi) {
					public void rollback() { shidezhi = _xdb_saved; }
				};}});
		shidezhi = _v_;
	}

	@Override
	public void setMenstorid(long _v_) { // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "menstorid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, menstorid) {
					public void rollback() { menstorid = _xdb_saved; }
				};}});
		menstorid = _v_;
	}

	@Override
	public void setBashitime(long _v_) { // 罢师时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bashitime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bashitime) {
					public void rollback() { bashitime = _xdb_saved; }
				};}});
		bashitime = _v_;
	}

	@Override
	public void setBashiflag(int _v_) { // 1=师傅7天没登录罢师 2=强行罢师
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bashiflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bashiflag) {
					public void rollback() { bashiflag = _xdb_saved; }
				};}});
		bashiflag = _v_;
	}

	@Override
	public void setEqipmentscore(int _v_) { // 拜师时候的装备评分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "eqipmentscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, eqipmentscore) {
					public void rollback() { eqipmentscore = _xdb_saved; }
				};}});
		eqipmentscore = _v_;
	}

	@Override
	public void setBaishitime(long _v_) { // 拜师时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "baishitime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, baishitime) {
					public void rollback() { baishitime = _xdb_saved; }
				};}});
		baishitime = _v_;
	}

	@Override
	public void setLastgivefushitime(long _v_) { // 上次获得符石的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastgivefushitime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastgivefushitime) {
					public void rollback() { lastgivefushitime = _xdb_saved; }
				};}});
		lastgivefushitime = _v_;
	}

	@Override
	public void setFushi(long _v_) { // 获得符石数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fushi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fushi) {
					public void rollback() { fushi = _xdb_saved; }
				};}});
		fushi = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MenstorAndApprent _o_ = null;
		if ( _o1_ instanceof MenstorAndApprent ) _o_ = (MenstorAndApprent)_o1_;
		else if ( _o1_ instanceof MenstorAndApprent.Const ) _o_ = ((MenstorAndApprent.Const)_o1_).nThis();
		else return false;
		if (flag != _o_.flag) return false;
		if (menstortitleid != _o_.menstortitleid) return false;
		if (firstbaishitime != _o_.firstbaishitime) return false;
		if (firstapprentid != _o_.firstapprentid) return false;
		if (shidezhi != _o_.shidezhi) return false;
		if (menstorid != _o_.menstorid) return false;
		if (bashitime != _o_.bashitime) return false;
		if (bashiflag != _o_.bashiflag) return false;
		if (eqipmentscore != _o_.eqipmentscore) return false;
		if (!achievement.equals(_o_.achievement)) return false;
		if (baishitime != _o_.baishitime) return false;
		if (lastgivefushitime != _o_.lastgivefushitime) return false;
		if (fushi != _o_.fushi) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += flag;
		_h_ += menstortitleid;
		_h_ += firstbaishitime;
		_h_ += firstapprentid;
		_h_ += shidezhi;
		_h_ += menstorid;
		_h_ += bashitime;
		_h_ += bashiflag;
		_h_ += eqipmentscore;
		_h_ += achievement.hashCode();
		_h_ += baishitime;
		_h_ += lastgivefushitime;
		_h_ += fushi;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(flag);
		_sb_.append(",");
		_sb_.append(menstortitleid);
		_sb_.append(",");
		_sb_.append(firstbaishitime);
		_sb_.append(",");
		_sb_.append(firstapprentid);
		_sb_.append(",");
		_sb_.append(shidezhi);
		_sb_.append(",");
		_sb_.append(menstorid);
		_sb_.append(",");
		_sb_.append(bashitime);
		_sb_.append(",");
		_sb_.append(bashiflag);
		_sb_.append(",");
		_sb_.append(eqipmentscore);
		_sb_.append(",");
		_sb_.append(achievement);
		_sb_.append(",");
		_sb_.append(baishitime);
		_sb_.append(",");
		_sb_.append(lastgivefushitime);
		_sb_.append(",");
		_sb_.append(fushi);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("flag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("menstortitleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstbaishitime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstapprentid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shidezhi"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("menstorid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bashitime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bashiflag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("eqipmentscore"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("achievement"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("baishitime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastgivefushitime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fushi"));
		return lb;
	}

	private class Const implements xbean.MenstorAndApprent {
		MenstorAndApprent nThis() {
			return MenstorAndApprent.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MenstorAndApprent copy() {
			return MenstorAndApprent.this.copy();
		}

		@Override
		public xbean.MenstorAndApprent toData() {
			return MenstorAndApprent.this.toData();
		}

		public xbean.MenstorAndApprent toBean() {
			return MenstorAndApprent.this.toBean();
		}

		@Override
		public xbean.MenstorAndApprent toDataIf() {
			return MenstorAndApprent.this.toDataIf();
		}

		public xbean.MenstorAndApprent toBeanIf() {
			return MenstorAndApprent.this.toBeanIf();
		}

		@Override
		public int getFlag() { // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
			_xdb_verify_unsafe_();
			return flag;
		}

		@Override
		public int getMenstortitleid() { // 为师时的titleId 表apprents 为自己的徒弟信息表
			_xdb_verify_unsafe_();
			return menstortitleid;
		}

		@Override
		public long getFirstbaishitime() { // 第一个出师徒弟的拜师时间
			_xdb_verify_unsafe_();
			return firstbaishitime;
		}

		@Override
		public long getFirstapprentid() { // 第一个出师徒弟的id
			_xdb_verify_unsafe_();
			return firstapprentid;
		}

		@Override
		public long getShidezhi() { // 师傅的师德值
			_xdb_verify_unsafe_();
			return shidezhi;
		}

		@Override
		public long getMenstorid() { // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
			_xdb_verify_unsafe_();
			return menstorid;
		}

		@Override
		public long getBashitime() { // 罢师时间
			_xdb_verify_unsafe_();
			return bashitime;
		}

		@Override
		public int getBashiflag() { // 1=师傅7天没登录罢师 2=强行罢师
			_xdb_verify_unsafe_();
			return bashiflag;
		}

		@Override
		public int getEqipmentscore() { // 拜师时候的装备评分
			_xdb_verify_unsafe_();
			return eqipmentscore;
		}

		@Override
		public java.util.Map<Integer, Integer> getAchievement() { // 每达成一项就要记录一项
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(achievement);
		}

		@Override
		public java.util.Map<Integer, Integer> getAchievementAsData() { // 每达成一项就要记录一项
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> achievement;
			MenstorAndApprent _o_ = MenstorAndApprent.this;
			achievement = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), _e_.getValue());
			return achievement;
		}

		@Override
		public long getBaishitime() { // 拜师时间
			_xdb_verify_unsafe_();
			return baishitime;
		}

		@Override
		public long getLastgivefushitime() { // 上次获得符石的时间
			_xdb_verify_unsafe_();
			return lastgivefushitime;
		}

		@Override
		public long getFushi() { // 获得符石数量
			_xdb_verify_unsafe_();
			return fushi;
		}

		@Override
		public void setFlag(int _v_) { // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMenstortitleid(int _v_) { // 为师时的titleId 表apprents 为自己的徒弟信息表
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstbaishitime(long _v_) { // 第一个出师徒弟的拜师时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstapprentid(long _v_) { // 第一个出师徒弟的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShidezhi(long _v_) { // 师傅的师德值
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMenstorid(long _v_) { // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBashitime(long _v_) { // 罢师时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBashiflag(int _v_) { // 1=师傅7天没登录罢师 2=强行罢师
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEqipmentscore(int _v_) { // 拜师时候的装备评分
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBaishitime(long _v_) { // 拜师时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastgivefushitime(long _v_) { // 上次获得符石的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFushi(long _v_) { // 获得符石数量
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
			return MenstorAndApprent.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MenstorAndApprent.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MenstorAndApprent.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MenstorAndApprent.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MenstorAndApprent.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MenstorAndApprent.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MenstorAndApprent.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MenstorAndApprent.this.hashCode();
		}

		@Override
		public String toString() {
			return MenstorAndApprent.this.toString();
		}

	}

	public static final class Data implements xbean.MenstorAndApprent {
		private int flag; // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
		private int menstortitleid; // 为师时的titleId 表apprents 为自己的徒弟信息表
		private long firstbaishitime; // 第一个出师徒弟的拜师时间
		private long firstapprentid; // 第一个出师徒弟的id
		private long shidezhi; // 师傅的师德值
		private long menstorid; // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
		private long bashitime; // 罢师时间
		private int bashiflag; // 1=师傅7天没登录罢师 2=强行罢师
		private int eqipmentscore; // 拜师时候的装备评分
		private java.util.HashMap<Integer, Integer> achievement; // 每达成一项就要记录一项
		private long baishitime; // 拜师时间
		private long lastgivefushitime; // 上次获得符石的时间
		private long fushi; // 获得符石数量

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			achievement = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.MenstorAndApprent _o1_) {
			if (_o1_ instanceof MenstorAndApprent) assign((MenstorAndApprent)_o1_);
			else if (_o1_ instanceof MenstorAndApprent.Data) assign((MenstorAndApprent.Data)_o1_);
			else if (_o1_ instanceof MenstorAndApprent.Const) assign(((MenstorAndApprent.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MenstorAndApprent _o_) {
			flag = _o_.flag;
			menstortitleid = _o_.menstortitleid;
			firstbaishitime = _o_.firstbaishitime;
			firstapprentid = _o_.firstapprentid;
			shidezhi = _o_.shidezhi;
			menstorid = _o_.menstorid;
			bashitime = _o_.bashitime;
			bashiflag = _o_.bashiflag;
			eqipmentscore = _o_.eqipmentscore;
			achievement = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), _e_.getValue());
			baishitime = _o_.baishitime;
			lastgivefushitime = _o_.lastgivefushitime;
			fushi = _o_.fushi;
		}

		private void assign(MenstorAndApprent.Data _o_) {
			flag = _o_.flag;
			menstortitleid = _o_.menstortitleid;
			firstbaishitime = _o_.firstbaishitime;
			firstapprentid = _o_.firstapprentid;
			shidezhi = _o_.shidezhi;
			menstorid = _o_.menstorid;
			bashitime = _o_.bashitime;
			bashiflag = _o_.bashiflag;
			eqipmentscore = _o_.eqipmentscore;
			achievement = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.achievement.entrySet())
				achievement.put(_e_.getKey(), _e_.getValue());
			baishitime = _o_.baishitime;
			lastgivefushitime = _o_.lastgivefushitime;
			fushi = _o_.fushi;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(flag);
			_os_.marshal(menstortitleid);
			_os_.marshal(firstbaishitime);
			_os_.marshal(firstapprentid);
			_os_.marshal(shidezhi);
			_os_.marshal(menstorid);
			_os_.marshal(bashitime);
			_os_.marshal(bashiflag);
			_os_.marshal(eqipmentscore);
			_os_.compact_uint32(achievement.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : achievement.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(baishitime);
			_os_.marshal(lastgivefushitime);
			_os_.marshal(fushi);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			flag = _os_.unmarshal_int();
			menstortitleid = _os_.unmarshal_int();
			firstbaishitime = _os_.unmarshal_long();
			firstapprentid = _os_.unmarshal_long();
			shidezhi = _os_.unmarshal_long();
			menstorid = _os_.unmarshal_long();
			bashitime = _os_.unmarshal_long();
			bashiflag = _os_.unmarshal_int();
			eqipmentscore = _os_.unmarshal_int();
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
			baishitime = _os_.unmarshal_long();
			lastgivefushitime = _os_.unmarshal_long();
			fushi = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.MenstorAndApprent copy() {
			return new Data(this);
		}

		@Override
		public xbean.MenstorAndApprent toData() {
			return new Data(this);
		}

		public xbean.MenstorAndApprent toBean() {
			return new MenstorAndApprent(this, null, null);
		}

		@Override
		public xbean.MenstorAndApprent toDataIf() {
			return this;
		}

		public xbean.MenstorAndApprent toBeanIf() {
			return new MenstorAndApprent(this, null, null);
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
		public int getFlag() { // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
			return flag;
		}

		@Override
		public int getMenstortitleid() { // 为师时的titleId 表apprents 为自己的徒弟信息表
			return menstortitleid;
		}

		@Override
		public long getFirstbaishitime() { // 第一个出师徒弟的拜师时间
			return firstbaishitime;
		}

		@Override
		public long getFirstapprentid() { // 第一个出师徒弟的id
			return firstapprentid;
		}

		@Override
		public long getShidezhi() { // 师傅的师德值
			return shidezhi;
		}

		@Override
		public long getMenstorid() { // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
			return menstorid;
		}

		@Override
		public long getBashitime() { // 罢师时间
			return bashitime;
		}

		@Override
		public int getBashiflag() { // 1=师傅7天没登录罢师 2=强行罢师
			return bashiflag;
		}

		@Override
		public int getEqipmentscore() { // 拜师时候的装备评分
			return eqipmentscore;
		}

		@Override
		public java.util.Map<Integer, Integer> getAchievement() { // 每达成一项就要记录一项
			return achievement;
		}

		@Override
		public java.util.Map<Integer, Integer> getAchievementAsData() { // 每达成一项就要记录一项
			return achievement;
		}

		@Override
		public long getBaishitime() { // 拜师时间
			return baishitime;
		}

		@Override
		public long getLastgivefushitime() { // 上次获得符石的时间
			return lastgivefushitime;
		}

		@Override
		public long getFushi() { // 获得符石数量
			return fushi;
		}

		@Override
		public void setFlag(int _v_) { // 0=什么也不是 1=当前阶段是徒弟 2=当前阶段是师傅
			flag = _v_;
		}

		@Override
		public void setMenstortitleid(int _v_) { // 为师时的titleId 表apprents 为自己的徒弟信息表
			menstortitleid = _v_;
		}

		@Override
		public void setFirstbaishitime(long _v_) { // 第一个出师徒弟的拜师时间
			firstbaishitime = _v_;
		}

		@Override
		public void setFirstapprentid(long _v_) { // 第一个出师徒弟的id
			firstapprentid = _v_;
		}

		@Override
		public void setShidezhi(long _v_) { // 师傅的师德值
			shidezhi = _v_;
		}

		@Override
		public void setMenstorid(long _v_) { // 师傅的角色id 也是apprents表key 表示自己的师兄弟信息
			menstorid = _v_;
		}

		@Override
		public void setBashitime(long _v_) { // 罢师时间
			bashitime = _v_;
		}

		@Override
		public void setBashiflag(int _v_) { // 1=师傅7天没登录罢师 2=强行罢师
			bashiflag = _v_;
		}

		@Override
		public void setEqipmentscore(int _v_) { // 拜师时候的装备评分
			eqipmentscore = _v_;
		}

		@Override
		public void setBaishitime(long _v_) { // 拜师时间
			baishitime = _v_;
		}

		@Override
		public void setLastgivefushitime(long _v_) { // 上次获得符石的时间
			lastgivefushitime = _v_;
		}

		@Override
		public void setFushi(long _v_) { // 获得符石数量
			fushi = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MenstorAndApprent.Data)) return false;
			MenstorAndApprent.Data _o_ = (MenstorAndApprent.Data) _o1_;
			if (flag != _o_.flag) return false;
			if (menstortitleid != _o_.menstortitleid) return false;
			if (firstbaishitime != _o_.firstbaishitime) return false;
			if (firstapprentid != _o_.firstapprentid) return false;
			if (shidezhi != _o_.shidezhi) return false;
			if (menstorid != _o_.menstorid) return false;
			if (bashitime != _o_.bashitime) return false;
			if (bashiflag != _o_.bashiflag) return false;
			if (eqipmentscore != _o_.eqipmentscore) return false;
			if (!achievement.equals(_o_.achievement)) return false;
			if (baishitime != _o_.baishitime) return false;
			if (lastgivefushitime != _o_.lastgivefushitime) return false;
			if (fushi != _o_.fushi) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += flag;
			_h_ += menstortitleid;
			_h_ += firstbaishitime;
			_h_ += firstapprentid;
			_h_ += shidezhi;
			_h_ += menstorid;
			_h_ += bashitime;
			_h_ += bashiflag;
			_h_ += eqipmentscore;
			_h_ += achievement.hashCode();
			_h_ += baishitime;
			_h_ += lastgivefushitime;
			_h_ += fushi;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(flag);
			_sb_.append(",");
			_sb_.append(menstortitleid);
			_sb_.append(",");
			_sb_.append(firstbaishitime);
			_sb_.append(",");
			_sb_.append(firstapprentid);
			_sb_.append(",");
			_sb_.append(shidezhi);
			_sb_.append(",");
			_sb_.append(menstorid);
			_sb_.append(",");
			_sb_.append(bashitime);
			_sb_.append(",");
			_sb_.append(bashiflag);
			_sb_.append(",");
			_sb_.append(eqipmentscore);
			_sb_.append(",");
			_sb_.append(achievement);
			_sb_.append(",");
			_sb_.append(baishitime);
			_sb_.append(",");
			_sb_.append(lastgivefushitime);
			_sb_.append(",");
			_sb_.append(fushi);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

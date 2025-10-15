
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanMemberInfo extends mkdb.XBean implements xbean.ClanMemberInfo {
	private int clanposition; // 职位信息
	private boolean isbannedtalk; // 是否被禁言
	private long bannedtalktime; // 被禁言的时间
	private String salutation; // 公会中的称谓
	private long jointime; // 加入公会时间
	private int ownbonus; // 可领取分红
	private long bonusgettime; // 领取分红时间
	private int tasknum; // 公会任务次数
	private int weekclanpoint; // 周公会贡献度by hzl
	private int preweekclanpoint; // 上周周公会贡献度by hzl
	private int clanhelpnum; // 公会本周援助次数
	private short claninstnum; // 参加公会副本次数
	private long claninstnumtime; // 记录参加公会副本次数时间
	private short clanfightnum; // 参加公会战次数
	private long clanfightnumtime; // 记录参加公会战次数时间

	@Override
	public void _reset_unsafe_() {
		clanposition = 0;
		isbannedtalk = false;
		bannedtalktime = 0;
		salutation = "";
		jointime = 0L;
		ownbonus = 0;
		bonusgettime = 0L;
		tasknum = 0;
		weekclanpoint = 0;
		preweekclanpoint = 0;
		clanhelpnum = 0;
		claninstnum = 0;
		claninstnumtime = 0L;
		clanfightnum = 0;
		clanfightnumtime = 0L;
	}

	ClanMemberInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		isbannedtalk = false;
		bannedtalktime = 0;
		salutation = "";
		ownbonus = 0;
		weekclanpoint = 0;
		preweekclanpoint = 0;
	}

	public ClanMemberInfo() {
		this(0, null, null);
	}

	public ClanMemberInfo(ClanMemberInfo _o_) {
		this(_o_, null, null);
	}

	ClanMemberInfo(xbean.ClanMemberInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanMemberInfo) assign((ClanMemberInfo)_o1_);
		else if (_o1_ instanceof ClanMemberInfo.Data) assign((ClanMemberInfo.Data)_o1_);
		else if (_o1_ instanceof ClanMemberInfo.Const) assign(((ClanMemberInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanMemberInfo _o_) {
		_o_._xdb_verify_unsafe_();
		clanposition = _o_.clanposition;
		isbannedtalk = _o_.isbannedtalk;
		bannedtalktime = _o_.bannedtalktime;
		salutation = _o_.salutation;
		jointime = _o_.jointime;
		ownbonus = _o_.ownbonus;
		bonusgettime = _o_.bonusgettime;
		tasknum = _o_.tasknum;
		weekclanpoint = _o_.weekclanpoint;
		preweekclanpoint = _o_.preweekclanpoint;
		clanhelpnum = _o_.clanhelpnum;
		claninstnum = _o_.claninstnum;
		claninstnumtime = _o_.claninstnumtime;
		clanfightnum = _o_.clanfightnum;
		clanfightnumtime = _o_.clanfightnumtime;
	}

	private void assign(ClanMemberInfo.Data _o_) {
		clanposition = _o_.clanposition;
		isbannedtalk = _o_.isbannedtalk;
		bannedtalktime = _o_.bannedtalktime;
		salutation = _o_.salutation;
		jointime = _o_.jointime;
		ownbonus = _o_.ownbonus;
		bonusgettime = _o_.bonusgettime;
		tasknum = _o_.tasknum;
		weekclanpoint = _o_.weekclanpoint;
		preweekclanpoint = _o_.preweekclanpoint;
		clanhelpnum = _o_.clanhelpnum;
		claninstnum = _o_.claninstnum;
		claninstnumtime = _o_.claninstnumtime;
		clanfightnum = _o_.clanfightnum;
		clanfightnumtime = _o_.clanfightnumtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(clanposition);
		_os_.marshal(isbannedtalk);
		_os_.marshal(bannedtalktime);
		_os_.marshal(salutation, mkdb.Const.IO_CHARSET);
		_os_.marshal(jointime);
		_os_.marshal(ownbonus);
		_os_.marshal(bonusgettime);
		_os_.marshal(tasknum);
		_os_.marshal(weekclanpoint);
		_os_.marshal(preweekclanpoint);
		_os_.marshal(clanhelpnum);
		_os_.marshal(claninstnum);
		_os_.marshal(claninstnumtime);
		_os_.marshal(clanfightnum);
		_os_.marshal(clanfightnumtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		clanposition = _os_.unmarshal_int();
		isbannedtalk = _os_.unmarshal_boolean();
		bannedtalktime = _os_.unmarshal_long();
		salutation = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		jointime = _os_.unmarshal_long();
		ownbonus = _os_.unmarshal_int();
		bonusgettime = _os_.unmarshal_long();
		tasknum = _os_.unmarshal_int();
		weekclanpoint = _os_.unmarshal_int();
		preweekclanpoint = _os_.unmarshal_int();
		clanhelpnum = _os_.unmarshal_int();
		claninstnum = _os_.unmarshal_short();
		claninstnumtime = _os_.unmarshal_long();
		clanfightnum = _os_.unmarshal_short();
		clanfightnumtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ClanMemberInfo copy() {
		_xdb_verify_unsafe_();
		return new ClanMemberInfo(this);
	}

	@Override
	public xbean.ClanMemberInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanMemberInfo toBean() {
		_xdb_verify_unsafe_();
		return new ClanMemberInfo(this); // same as copy()
	}

	@Override
	public xbean.ClanMemberInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanMemberInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getClanposition() { // 职位信息
		_xdb_verify_unsafe_();
		return clanposition;
	}

	@Override
	public boolean getIsbannedtalk() { // 是否被禁言
		_xdb_verify_unsafe_();
		return isbannedtalk;
	}

	@Override
	public long getBannedtalktime() { // 被禁言的时间
		_xdb_verify_unsafe_();
		return bannedtalktime;
	}

	@Override
	public String getSalutation() { // 公会中的称谓
		_xdb_verify_unsafe_();
		return salutation;
	}

	@Override
	public com.locojoy.base.Octets getSalutationOctets() { // 公会中的称谓
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getSalutation(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getJointime() { // 加入公会时间
		_xdb_verify_unsafe_();
		return jointime;
	}

	@Override
	public int getOwnbonus() { // 可领取分红
		_xdb_verify_unsafe_();
		return ownbonus;
	}

	@Override
	public long getBonusgettime() { // 领取分红时间
		_xdb_verify_unsafe_();
		return bonusgettime;
	}

	@Override
	public int getTasknum() { // 公会任务次数
		_xdb_verify_unsafe_();
		return tasknum;
	}

	@Override
	public int getWeekclanpoint() { // 周公会贡献度by hzl
		_xdb_verify_unsafe_();
		return weekclanpoint;
	}

	@Override
	public int getPreweekclanpoint() { // 上周周公会贡献度by hzl
		_xdb_verify_unsafe_();
		return preweekclanpoint;
	}

	@Override
	public int getClanhelpnum() { // 公会本周援助次数
		_xdb_verify_unsafe_();
		return clanhelpnum;
	}

	@Override
	public short getClaninstnum() { // 参加公会副本次数
		_xdb_verify_unsafe_();
		return claninstnum;
	}

	@Override
	public long getClaninstnumtime() { // 记录参加公会副本次数时间
		_xdb_verify_unsafe_();
		return claninstnumtime;
	}

	@Override
	public short getClanfightnum() { // 参加公会战次数
		_xdb_verify_unsafe_();
		return clanfightnum;
	}

	@Override
	public long getClanfightnumtime() { // 记录参加公会战次数时间
		_xdb_verify_unsafe_();
		return clanfightnumtime;
	}

	@Override
	public void setClanposition(int _v_) { // 职位信息
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanposition") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanposition) {
					public void rollback() { clanposition = _xdb_saved; }
				};}});
		clanposition = _v_;
	}

	@Override
	public void setIsbannedtalk(boolean _v_) { // 是否被禁言
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isbannedtalk") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, isbannedtalk) {
					public void rollback() { isbannedtalk = _xdb_saved; }
				};}});
		isbannedtalk = _v_;
	}

	@Override
	public void setBannedtalktime(long _v_) { // 被禁言的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bannedtalktime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bannedtalktime) {
					public void rollback() { bannedtalktime = _xdb_saved; }
				};}});
		bannedtalktime = _v_;
	}

	@Override
	public void setSalutation(String _v_) { // 公会中的称谓
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "salutation") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, salutation) {
					public void rollback() { salutation = _xdb_saved; }
				};}});
		salutation = _v_;
	}

	@Override
	public void setSalutationOctets(com.locojoy.base.Octets _v_) { // 公会中的称谓
		_xdb_verify_unsafe_();
		this.setSalutation(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setJointime(long _v_) { // 加入公会时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "jointime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, jointime) {
					public void rollback() { jointime = _xdb_saved; }
				};}});
		jointime = _v_;
	}

	@Override
	public void setOwnbonus(int _v_) { // 可领取分红
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ownbonus") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, ownbonus) {
					public void rollback() { ownbonus = _xdb_saved; }
				};}});
		ownbonus = _v_;
	}

	@Override
	public void setBonusgettime(long _v_) { // 领取分红时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bonusgettime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bonusgettime) {
					public void rollback() { bonusgettime = _xdb_saved; }
				};}});
		bonusgettime = _v_;
	}

	@Override
	public void setTasknum(int _v_) { // 公会任务次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tasknum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, tasknum) {
					public void rollback() { tasknum = _xdb_saved; }
				};}});
		tasknum = _v_;
	}

	@Override
	public void setWeekclanpoint(int _v_) { // 周公会贡献度by hzl
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "weekclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, weekclanpoint) {
					public void rollback() { weekclanpoint = _xdb_saved; }
				};}});
		weekclanpoint = _v_;
	}

	@Override
	public void setPreweekclanpoint(int _v_) { // 上周周公会贡献度by hzl
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "preweekclanpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, preweekclanpoint) {
					public void rollback() { preweekclanpoint = _xdb_saved; }
				};}});
		preweekclanpoint = _v_;
	}

	@Override
	public void setClanhelpnum(int _v_) { // 公会本周援助次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanhelpnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanhelpnum) {
					public void rollback() { clanhelpnum = _xdb_saved; }
				};}});
		clanhelpnum = _v_;
	}

	@Override
	public void setClaninstnum(short _v_) { // 参加公会副本次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "claninstnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, claninstnum) {
					public void rollback() { claninstnum = _xdb_saved; }
				};}});
		claninstnum = _v_;
	}

	@Override
	public void setClaninstnumtime(long _v_) { // 记录参加公会副本次数时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "claninstnumtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, claninstnumtime) {
					public void rollback() { claninstnumtime = _xdb_saved; }
				};}});
		claninstnumtime = _v_;
	}

	@Override
	public void setClanfightnum(short _v_) { // 参加公会战次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanfightnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, clanfightnum) {
					public void rollback() { clanfightnum = _xdb_saved; }
				};}});
		clanfightnum = _v_;
	}

	@Override
	public void setClanfightnumtime(long _v_) { // 记录参加公会战次数时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanfightnumtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clanfightnumtime) {
					public void rollback() { clanfightnumtime = _xdb_saved; }
				};}});
		clanfightnumtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanMemberInfo _o_ = null;
		if ( _o1_ instanceof ClanMemberInfo ) _o_ = (ClanMemberInfo)_o1_;
		else if ( _o1_ instanceof ClanMemberInfo.Const ) _o_ = ((ClanMemberInfo.Const)_o1_).nThis();
		else return false;
		if (clanposition != _o_.clanposition) return false;
		if (isbannedtalk != _o_.isbannedtalk) return false;
		if (bannedtalktime != _o_.bannedtalktime) return false;
		if (!salutation.equals(_o_.salutation)) return false;
		if (jointime != _o_.jointime) return false;
		if (ownbonus != _o_.ownbonus) return false;
		if (bonusgettime != _o_.bonusgettime) return false;
		if (tasknum != _o_.tasknum) return false;
		if (weekclanpoint != _o_.weekclanpoint) return false;
		if (preweekclanpoint != _o_.preweekclanpoint) return false;
		if (clanhelpnum != _o_.clanhelpnum) return false;
		if (claninstnum != _o_.claninstnum) return false;
		if (claninstnumtime != _o_.claninstnumtime) return false;
		if (clanfightnum != _o_.clanfightnum) return false;
		if (clanfightnumtime != _o_.clanfightnumtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += clanposition;
		_h_ += isbannedtalk ? 1231 : 1237;
		_h_ += bannedtalktime;
		_h_ += salutation.hashCode();
		_h_ += jointime;
		_h_ += ownbonus;
		_h_ += bonusgettime;
		_h_ += tasknum;
		_h_ += weekclanpoint;
		_h_ += preweekclanpoint;
		_h_ += clanhelpnum;
		_h_ += claninstnum;
		_h_ += claninstnumtime;
		_h_ += clanfightnum;
		_h_ += clanfightnumtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanposition);
		_sb_.append(",");
		_sb_.append(isbannedtalk);
		_sb_.append(",");
		_sb_.append(bannedtalktime);
		_sb_.append(",");
		_sb_.append("'").append(salutation).append("'");
		_sb_.append(",");
		_sb_.append(jointime);
		_sb_.append(",");
		_sb_.append(ownbonus);
		_sb_.append(",");
		_sb_.append(bonusgettime);
		_sb_.append(",");
		_sb_.append(tasknum);
		_sb_.append(",");
		_sb_.append(weekclanpoint);
		_sb_.append(",");
		_sb_.append(preweekclanpoint);
		_sb_.append(",");
		_sb_.append(clanhelpnum);
		_sb_.append(",");
		_sb_.append(claninstnum);
		_sb_.append(",");
		_sb_.append(claninstnumtime);
		_sb_.append(",");
		_sb_.append(clanfightnum);
		_sb_.append(",");
		_sb_.append(clanfightnumtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanposition"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isbannedtalk"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bannedtalktime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("salutation"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("jointime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ownbonus"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bonusgettime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tasknum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("weekclanpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("preweekclanpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanhelpnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("claninstnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("claninstnumtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanfightnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanfightnumtime"));
		return lb;
	}

	private class Const implements xbean.ClanMemberInfo {
		ClanMemberInfo nThis() {
			return ClanMemberInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanMemberInfo copy() {
			return ClanMemberInfo.this.copy();
		}

		@Override
		public xbean.ClanMemberInfo toData() {
			return ClanMemberInfo.this.toData();
		}

		public xbean.ClanMemberInfo toBean() {
			return ClanMemberInfo.this.toBean();
		}

		@Override
		public xbean.ClanMemberInfo toDataIf() {
			return ClanMemberInfo.this.toDataIf();
		}

		public xbean.ClanMemberInfo toBeanIf() {
			return ClanMemberInfo.this.toBeanIf();
		}

		@Override
		public int getClanposition() { // 职位信息
			_xdb_verify_unsafe_();
			return clanposition;
		}

		@Override
		public boolean getIsbannedtalk() { // 是否被禁言
			_xdb_verify_unsafe_();
			return isbannedtalk;
		}

		@Override
		public long getBannedtalktime() { // 被禁言的时间
			_xdb_verify_unsafe_();
			return bannedtalktime;
		}

		@Override
		public String getSalutation() { // 公会中的称谓
			_xdb_verify_unsafe_();
			return salutation;
		}

		@Override
		public com.locojoy.base.Octets getSalutationOctets() { // 公会中的称谓
			_xdb_verify_unsafe_();
			return ClanMemberInfo.this.getSalutationOctets();
		}

		@Override
		public long getJointime() { // 加入公会时间
			_xdb_verify_unsafe_();
			return jointime;
		}

		@Override
		public int getOwnbonus() { // 可领取分红
			_xdb_verify_unsafe_();
			return ownbonus;
		}

		@Override
		public long getBonusgettime() { // 领取分红时间
			_xdb_verify_unsafe_();
			return bonusgettime;
		}

		@Override
		public int getTasknum() { // 公会任务次数
			_xdb_verify_unsafe_();
			return tasknum;
		}

		@Override
		public int getWeekclanpoint() { // 周公会贡献度by hzl
			_xdb_verify_unsafe_();
			return weekclanpoint;
		}

		@Override
		public int getPreweekclanpoint() { // 上周周公会贡献度by hzl
			_xdb_verify_unsafe_();
			return preweekclanpoint;
		}

		@Override
		public int getClanhelpnum() { // 公会本周援助次数
			_xdb_verify_unsafe_();
			return clanhelpnum;
		}

		@Override
		public short getClaninstnum() { // 参加公会副本次数
			_xdb_verify_unsafe_();
			return claninstnum;
		}

		@Override
		public long getClaninstnumtime() { // 记录参加公会副本次数时间
			_xdb_verify_unsafe_();
			return claninstnumtime;
		}

		@Override
		public short getClanfightnum() { // 参加公会战次数
			_xdb_verify_unsafe_();
			return clanfightnum;
		}

		@Override
		public long getClanfightnumtime() { // 记录参加公会战次数时间
			_xdb_verify_unsafe_();
			return clanfightnumtime;
		}

		@Override
		public void setClanposition(int _v_) { // 职位信息
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsbannedtalk(boolean _v_) { // 是否被禁言
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBannedtalktime(long _v_) { // 被禁言的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSalutation(String _v_) { // 公会中的称谓
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSalutationOctets(com.locojoy.base.Octets _v_) { // 公会中的称谓
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setJointime(long _v_) { // 加入公会时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOwnbonus(int _v_) { // 可领取分红
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBonusgettime(long _v_) { // 领取分红时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTasknum(int _v_) { // 公会任务次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWeekclanpoint(int _v_) { // 周公会贡献度by hzl
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPreweekclanpoint(int _v_) { // 上周周公会贡献度by hzl
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanhelpnum(int _v_) { // 公会本周援助次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClaninstnum(short _v_) { // 参加公会副本次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClaninstnumtime(long _v_) { // 记录参加公会副本次数时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanfightnum(short _v_) { // 参加公会战次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanfightnumtime(long _v_) { // 记录参加公会战次数时间
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
			return ClanMemberInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanMemberInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanMemberInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanMemberInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanMemberInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanMemberInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanMemberInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanMemberInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanMemberInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ClanMemberInfo {
		private int clanposition; // 职位信息
		private boolean isbannedtalk; // 是否被禁言
		private long bannedtalktime; // 被禁言的时间
		private String salutation; // 公会中的称谓
		private long jointime; // 加入公会时间
		private int ownbonus; // 可领取分红
		private long bonusgettime; // 领取分红时间
		private int tasknum; // 公会任务次数
		private int weekclanpoint; // 周公会贡献度by hzl
		private int preweekclanpoint; // 上周周公会贡献度by hzl
		private int clanhelpnum; // 公会本周援助次数
		private short claninstnum; // 参加公会副本次数
		private long claninstnumtime; // 记录参加公会副本次数时间
		private short clanfightnum; // 参加公会战次数
		private long clanfightnumtime; // 记录参加公会战次数时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			isbannedtalk = false;
			bannedtalktime = 0;
			salutation = "";
			ownbonus = 0;
			weekclanpoint = 0;
			preweekclanpoint = 0;
		}

		Data(xbean.ClanMemberInfo _o1_) {
			if (_o1_ instanceof ClanMemberInfo) assign((ClanMemberInfo)_o1_);
			else if (_o1_ instanceof ClanMemberInfo.Data) assign((ClanMemberInfo.Data)_o1_);
			else if (_o1_ instanceof ClanMemberInfo.Const) assign(((ClanMemberInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanMemberInfo _o_) {
			clanposition = _o_.clanposition;
			isbannedtalk = _o_.isbannedtalk;
			bannedtalktime = _o_.bannedtalktime;
			salutation = _o_.salutation;
			jointime = _o_.jointime;
			ownbonus = _o_.ownbonus;
			bonusgettime = _o_.bonusgettime;
			tasknum = _o_.tasknum;
			weekclanpoint = _o_.weekclanpoint;
			preweekclanpoint = _o_.preweekclanpoint;
			clanhelpnum = _o_.clanhelpnum;
			claninstnum = _o_.claninstnum;
			claninstnumtime = _o_.claninstnumtime;
			clanfightnum = _o_.clanfightnum;
			clanfightnumtime = _o_.clanfightnumtime;
		}

		private void assign(ClanMemberInfo.Data _o_) {
			clanposition = _o_.clanposition;
			isbannedtalk = _o_.isbannedtalk;
			bannedtalktime = _o_.bannedtalktime;
			salutation = _o_.salutation;
			jointime = _o_.jointime;
			ownbonus = _o_.ownbonus;
			bonusgettime = _o_.bonusgettime;
			tasknum = _o_.tasknum;
			weekclanpoint = _o_.weekclanpoint;
			preweekclanpoint = _o_.preweekclanpoint;
			clanhelpnum = _o_.clanhelpnum;
			claninstnum = _o_.claninstnum;
			claninstnumtime = _o_.claninstnumtime;
			clanfightnum = _o_.clanfightnum;
			clanfightnumtime = _o_.clanfightnumtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(clanposition);
			_os_.marshal(isbannedtalk);
			_os_.marshal(bannedtalktime);
			_os_.marshal(salutation, mkdb.Const.IO_CHARSET);
			_os_.marshal(jointime);
			_os_.marshal(ownbonus);
			_os_.marshal(bonusgettime);
			_os_.marshal(tasknum);
			_os_.marshal(weekclanpoint);
			_os_.marshal(preweekclanpoint);
			_os_.marshal(clanhelpnum);
			_os_.marshal(claninstnum);
			_os_.marshal(claninstnumtime);
			_os_.marshal(clanfightnum);
			_os_.marshal(clanfightnumtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			clanposition = _os_.unmarshal_int();
			isbannedtalk = _os_.unmarshal_boolean();
			bannedtalktime = _os_.unmarshal_long();
			salutation = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			jointime = _os_.unmarshal_long();
			ownbonus = _os_.unmarshal_int();
			bonusgettime = _os_.unmarshal_long();
			tasknum = _os_.unmarshal_int();
			weekclanpoint = _os_.unmarshal_int();
			preweekclanpoint = _os_.unmarshal_int();
			clanhelpnum = _os_.unmarshal_int();
			claninstnum = _os_.unmarshal_short();
			claninstnumtime = _os_.unmarshal_long();
			clanfightnum = _os_.unmarshal_short();
			clanfightnumtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ClanMemberInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanMemberInfo toData() {
			return new Data(this);
		}

		public xbean.ClanMemberInfo toBean() {
			return new ClanMemberInfo(this, null, null);
		}

		@Override
		public xbean.ClanMemberInfo toDataIf() {
			return this;
		}

		public xbean.ClanMemberInfo toBeanIf() {
			return new ClanMemberInfo(this, null, null);
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
		public int getClanposition() { // 职位信息
			return clanposition;
		}

		@Override
		public boolean getIsbannedtalk() { // 是否被禁言
			return isbannedtalk;
		}

		@Override
		public long getBannedtalktime() { // 被禁言的时间
			return bannedtalktime;
		}

		@Override
		public String getSalutation() { // 公会中的称谓
			return salutation;
		}

		@Override
		public com.locojoy.base.Octets getSalutationOctets() { // 公会中的称谓
			return com.locojoy.base.Octets.wrap(getSalutation(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getJointime() { // 加入公会时间
			return jointime;
		}

		@Override
		public int getOwnbonus() { // 可领取分红
			return ownbonus;
		}

		@Override
		public long getBonusgettime() { // 领取分红时间
			return bonusgettime;
		}

		@Override
		public int getTasknum() { // 公会任务次数
			return tasknum;
		}

		@Override
		public int getWeekclanpoint() { // 周公会贡献度by hzl
			return weekclanpoint;
		}

		@Override
		public int getPreweekclanpoint() { // 上周周公会贡献度by hzl
			return preweekclanpoint;
		}

		@Override
		public int getClanhelpnum() { // 公会本周援助次数
			return clanhelpnum;
		}

		@Override
		public short getClaninstnum() { // 参加公会副本次数
			return claninstnum;
		}

		@Override
		public long getClaninstnumtime() { // 记录参加公会副本次数时间
			return claninstnumtime;
		}

		@Override
		public short getClanfightnum() { // 参加公会战次数
			return clanfightnum;
		}

		@Override
		public long getClanfightnumtime() { // 记录参加公会战次数时间
			return clanfightnumtime;
		}

		@Override
		public void setClanposition(int _v_) { // 职位信息
			clanposition = _v_;
		}

		@Override
		public void setIsbannedtalk(boolean _v_) { // 是否被禁言
			isbannedtalk = _v_;
		}

		@Override
		public void setBannedtalktime(long _v_) { // 被禁言的时间
			bannedtalktime = _v_;
		}

		@Override
		public void setSalutation(String _v_) { // 公会中的称谓
			if (null == _v_)
				throw new NullPointerException();
			salutation = _v_;
		}

		@Override
		public void setSalutationOctets(com.locojoy.base.Octets _v_) { // 公会中的称谓
			this.setSalutation(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setJointime(long _v_) { // 加入公会时间
			jointime = _v_;
		}

		@Override
		public void setOwnbonus(int _v_) { // 可领取分红
			ownbonus = _v_;
		}

		@Override
		public void setBonusgettime(long _v_) { // 领取分红时间
			bonusgettime = _v_;
		}

		@Override
		public void setTasknum(int _v_) { // 公会任务次数
			tasknum = _v_;
		}

		@Override
		public void setWeekclanpoint(int _v_) { // 周公会贡献度by hzl
			weekclanpoint = _v_;
		}

		@Override
		public void setPreweekclanpoint(int _v_) { // 上周周公会贡献度by hzl
			preweekclanpoint = _v_;
		}

		@Override
		public void setClanhelpnum(int _v_) { // 公会本周援助次数
			clanhelpnum = _v_;
		}

		@Override
		public void setClaninstnum(short _v_) { // 参加公会副本次数
			claninstnum = _v_;
		}

		@Override
		public void setClaninstnumtime(long _v_) { // 记录参加公会副本次数时间
			claninstnumtime = _v_;
		}

		@Override
		public void setClanfightnum(short _v_) { // 参加公会战次数
			clanfightnum = _v_;
		}

		@Override
		public void setClanfightnumtime(long _v_) { // 记录参加公会战次数时间
			clanfightnumtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanMemberInfo.Data)) return false;
			ClanMemberInfo.Data _o_ = (ClanMemberInfo.Data) _o1_;
			if (clanposition != _o_.clanposition) return false;
			if (isbannedtalk != _o_.isbannedtalk) return false;
			if (bannedtalktime != _o_.bannedtalktime) return false;
			if (!salutation.equals(_o_.salutation)) return false;
			if (jointime != _o_.jointime) return false;
			if (ownbonus != _o_.ownbonus) return false;
			if (bonusgettime != _o_.bonusgettime) return false;
			if (tasknum != _o_.tasknum) return false;
			if (weekclanpoint != _o_.weekclanpoint) return false;
			if (preweekclanpoint != _o_.preweekclanpoint) return false;
			if (clanhelpnum != _o_.clanhelpnum) return false;
			if (claninstnum != _o_.claninstnum) return false;
			if (claninstnumtime != _o_.claninstnumtime) return false;
			if (clanfightnum != _o_.clanfightnum) return false;
			if (clanfightnumtime != _o_.clanfightnumtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += clanposition;
			_h_ += isbannedtalk ? 1231 : 1237;
			_h_ += bannedtalktime;
			_h_ += salutation.hashCode();
			_h_ += jointime;
			_h_ += ownbonus;
			_h_ += bonusgettime;
			_h_ += tasknum;
			_h_ += weekclanpoint;
			_h_ += preweekclanpoint;
			_h_ += clanhelpnum;
			_h_ += claninstnum;
			_h_ += claninstnumtime;
			_h_ += clanfightnum;
			_h_ += clanfightnumtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(clanposition);
			_sb_.append(",");
			_sb_.append(isbannedtalk);
			_sb_.append(",");
			_sb_.append(bannedtalktime);
			_sb_.append(",");
			_sb_.append("'").append(salutation).append("'");
			_sb_.append(",");
			_sb_.append(jointime);
			_sb_.append(",");
			_sb_.append(ownbonus);
			_sb_.append(",");
			_sb_.append(bonusgettime);
			_sb_.append(",");
			_sb_.append(tasknum);
			_sb_.append(",");
			_sb_.append(weekclanpoint);
			_sb_.append(",");
			_sb_.append(preweekclanpoint);
			_sb_.append(",");
			_sb_.append(clanhelpnum);
			_sb_.append(",");
			_sb_.append(claninstnum);
			_sb_.append(",");
			_sb_.append(claninstnumtime);
			_sb_.append(",");
			_sb_.append(clanfightnum);
			_sb_.append(",");
			_sb_.append(clanfightnumtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

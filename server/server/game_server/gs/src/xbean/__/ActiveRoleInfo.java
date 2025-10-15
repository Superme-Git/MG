
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ActiveRoleInfo extends mkdb.XBean implements xbean.ActiveRoleInfo {
	private int createtime; // 创建时间 当前天
	private long enterworldtime; // 最近一次登录游戏的时间
	private int dayonline; // 在线时长 当前天 分钟为单位
	private int dayonlinetime; // 在线时长 当前天 分钟为单位
	private int weekonline; // 在线时长 当前周 分钟为单位
	private int weekonlinetime; // 在线时长 当前周 分钟为单位
	private int lasttimeactiveday1; // 最近两次活跃的 当前天 day1比day2新,下同
	private int lasttimeactiveday1onlinetime; // 最近一次日活跃的在线时长
	private int lasttimeactiveday2; // 
	private int lasttimeactiveweek1; // 最近两次活跃的 当前周 week1比week2新,下同
	private int lasttimeactiveweek1onlinetime; // 最近一次周活跃的在线时长
	private int lasttimeactiveweek2; // 
	private int firsttimeactiveday; // 第一次活跃的 当前天
	private int firsttimeactiveweek; // 第一次活跃的 当前周
	private long dealmoneyinplatform; // 交易平台挂单游戏币
	private long tmpmoneyinplatform; // 交易平台暂存游戏币
	private long moneyincofc; // 商会资金

	@Override
	public void _reset_unsafe_() {
		createtime = 0;
		enterworldtime = 0L;
		dayonline = 0;
		dayonlinetime = 0;
		weekonline = 0;
		weekonlinetime = 0;
		lasttimeactiveday1 = 0;
		lasttimeactiveday1onlinetime = 0;
		lasttimeactiveday2 = 0;
		lasttimeactiveweek1 = 0;
		lasttimeactiveweek1onlinetime = 0;
		lasttimeactiveweek2 = 0;
		firsttimeactiveday = 0;
		firsttimeactiveweek = 0;
		dealmoneyinplatform = 0L;
		tmpmoneyinplatform = 0L;
		moneyincofc = 0L;
	}

	ActiveRoleInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ActiveRoleInfo() {
		this(0, null, null);
	}

	public ActiveRoleInfo(ActiveRoleInfo _o_) {
		this(_o_, null, null);
	}

	ActiveRoleInfo(xbean.ActiveRoleInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ActiveRoleInfo) assign((ActiveRoleInfo)_o1_);
		else if (_o1_ instanceof ActiveRoleInfo.Data) assign((ActiveRoleInfo.Data)_o1_);
		else if (_o1_ instanceof ActiveRoleInfo.Const) assign(((ActiveRoleInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ActiveRoleInfo _o_) {
		_o_._xdb_verify_unsafe_();
		createtime = _o_.createtime;
		enterworldtime = _o_.enterworldtime;
		dayonline = _o_.dayonline;
		dayonlinetime = _o_.dayonlinetime;
		weekonline = _o_.weekonline;
		weekonlinetime = _o_.weekonlinetime;
		lasttimeactiveday1 = _o_.lasttimeactiveday1;
		lasttimeactiveday1onlinetime = _o_.lasttimeactiveday1onlinetime;
		lasttimeactiveday2 = _o_.lasttimeactiveday2;
		lasttimeactiveweek1 = _o_.lasttimeactiveweek1;
		lasttimeactiveweek1onlinetime = _o_.lasttimeactiveweek1onlinetime;
		lasttimeactiveweek2 = _o_.lasttimeactiveweek2;
		firsttimeactiveday = _o_.firsttimeactiveday;
		firsttimeactiveweek = _o_.firsttimeactiveweek;
		dealmoneyinplatform = _o_.dealmoneyinplatform;
		tmpmoneyinplatform = _o_.tmpmoneyinplatform;
		moneyincofc = _o_.moneyincofc;
	}

	private void assign(ActiveRoleInfo.Data _o_) {
		createtime = _o_.createtime;
		enterworldtime = _o_.enterworldtime;
		dayonline = _o_.dayonline;
		dayonlinetime = _o_.dayonlinetime;
		weekonline = _o_.weekonline;
		weekonlinetime = _o_.weekonlinetime;
		lasttimeactiveday1 = _o_.lasttimeactiveday1;
		lasttimeactiveday1onlinetime = _o_.lasttimeactiveday1onlinetime;
		lasttimeactiveday2 = _o_.lasttimeactiveday2;
		lasttimeactiveweek1 = _o_.lasttimeactiveweek1;
		lasttimeactiveweek1onlinetime = _o_.lasttimeactiveweek1onlinetime;
		lasttimeactiveweek2 = _o_.lasttimeactiveweek2;
		firsttimeactiveday = _o_.firsttimeactiveday;
		firsttimeactiveweek = _o_.firsttimeactiveweek;
		dealmoneyinplatform = _o_.dealmoneyinplatform;
		tmpmoneyinplatform = _o_.tmpmoneyinplatform;
		moneyincofc = _o_.moneyincofc;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(createtime);
		_os_.marshal(enterworldtime);
		_os_.marshal(dayonline);
		_os_.marshal(dayonlinetime);
		_os_.marshal(weekonline);
		_os_.marshal(weekonlinetime);
		_os_.marshal(lasttimeactiveday1);
		_os_.marshal(lasttimeactiveday1onlinetime);
		_os_.marshal(lasttimeactiveday2);
		_os_.marshal(lasttimeactiveweek1);
		_os_.marshal(lasttimeactiveweek1onlinetime);
		_os_.marshal(lasttimeactiveweek2);
		_os_.marshal(firsttimeactiveday);
		_os_.marshal(firsttimeactiveweek);
		_os_.marshal(dealmoneyinplatform);
		_os_.marshal(tmpmoneyinplatform);
		_os_.marshal(moneyincofc);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		createtime = _os_.unmarshal_int();
		enterworldtime = _os_.unmarshal_long();
		dayonline = _os_.unmarshal_int();
		dayonlinetime = _os_.unmarshal_int();
		weekonline = _os_.unmarshal_int();
		weekonlinetime = _os_.unmarshal_int();
		lasttimeactiveday1 = _os_.unmarshal_int();
		lasttimeactiveday1onlinetime = _os_.unmarshal_int();
		lasttimeactiveday2 = _os_.unmarshal_int();
		lasttimeactiveweek1 = _os_.unmarshal_int();
		lasttimeactiveweek1onlinetime = _os_.unmarshal_int();
		lasttimeactiveweek2 = _os_.unmarshal_int();
		firsttimeactiveday = _os_.unmarshal_int();
		firsttimeactiveweek = _os_.unmarshal_int();
		dealmoneyinplatform = _os_.unmarshal_long();
		tmpmoneyinplatform = _os_.unmarshal_long();
		moneyincofc = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ActiveRoleInfo copy() {
		_xdb_verify_unsafe_();
		return new ActiveRoleInfo(this);
	}

	@Override
	public xbean.ActiveRoleInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActiveRoleInfo toBean() {
		_xdb_verify_unsafe_();
		return new ActiveRoleInfo(this); // same as copy()
	}

	@Override
	public xbean.ActiveRoleInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActiveRoleInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getCreatetime() { // 创建时间 当前天
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public long getEnterworldtime() { // 最近一次登录游戏的时间
		_xdb_verify_unsafe_();
		return enterworldtime;
	}

	@Override
	public int getDayonline() { // 在线时长 当前天 分钟为单位
		_xdb_verify_unsafe_();
		return dayonline;
	}

	@Override
	public int getDayonlinetime() { // 在线时长 当前天 分钟为单位
		_xdb_verify_unsafe_();
		return dayonlinetime;
	}

	@Override
	public int getWeekonline() { // 在线时长 当前周 分钟为单位
		_xdb_verify_unsafe_();
		return weekonline;
	}

	@Override
	public int getWeekonlinetime() { // 在线时长 当前周 分钟为单位
		_xdb_verify_unsafe_();
		return weekonlinetime;
	}

	@Override
	public int getLasttimeactiveday1() { // 最近两次活跃的 当前天 day1比day2新,下同
		_xdb_verify_unsafe_();
		return lasttimeactiveday1;
	}

	@Override
	public int getLasttimeactiveday1onlinetime() { // 最近一次日活跃的在线时长
		_xdb_verify_unsafe_();
		return lasttimeactiveday1onlinetime;
	}

	@Override
	public int getLasttimeactiveday2() { // 
		_xdb_verify_unsafe_();
		return lasttimeactiveday2;
	}

	@Override
	public int getLasttimeactiveweek1() { // 最近两次活跃的 当前周 week1比week2新,下同
		_xdb_verify_unsafe_();
		return lasttimeactiveweek1;
	}

	@Override
	public int getLasttimeactiveweek1onlinetime() { // 最近一次周活跃的在线时长
		_xdb_verify_unsafe_();
		return lasttimeactiveweek1onlinetime;
	}

	@Override
	public int getLasttimeactiveweek2() { // 
		_xdb_verify_unsafe_();
		return lasttimeactiveweek2;
	}

	@Override
	public int getFirsttimeactiveday() { // 第一次活跃的 当前天
		_xdb_verify_unsafe_();
		return firsttimeactiveday;
	}

	@Override
	public int getFirsttimeactiveweek() { // 第一次活跃的 当前周
		_xdb_verify_unsafe_();
		return firsttimeactiveweek;
	}

	@Override
	public long getDealmoneyinplatform() { // 交易平台挂单游戏币
		_xdb_verify_unsafe_();
		return dealmoneyinplatform;
	}

	@Override
	public long getTmpmoneyinplatform() { // 交易平台暂存游戏币
		_xdb_verify_unsafe_();
		return tmpmoneyinplatform;
	}

	@Override
	public long getMoneyincofc() { // 商会资金
		_xdb_verify_unsafe_();
		return moneyincofc;
	}

	@Override
	public void setCreatetime(int _v_) { // 创建时间 当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public void setEnterworldtime(long _v_) { // 最近一次登录游戏的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "enterworldtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, enterworldtime) {
					public void rollback() { enterworldtime = _xdb_saved; }
				};}});
		enterworldtime = _v_;
	}

	@Override
	public void setDayonline(int _v_) { // 在线时长 当前天 分钟为单位
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dayonline") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dayonline) {
					public void rollback() { dayonline = _xdb_saved; }
				};}});
		dayonline = _v_;
	}

	@Override
	public void setDayonlinetime(int _v_) { // 在线时长 当前天 分钟为单位
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dayonlinetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dayonlinetime) {
					public void rollback() { dayonlinetime = _xdb_saved; }
				};}});
		dayonlinetime = _v_;
	}

	@Override
	public void setWeekonline(int _v_) { // 在线时长 当前周 分钟为单位
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "weekonline") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, weekonline) {
					public void rollback() { weekonline = _xdb_saved; }
				};}});
		weekonline = _v_;
	}

	@Override
	public void setWeekonlinetime(int _v_) { // 在线时长 当前周 分钟为单位
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "weekonlinetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, weekonlinetime) {
					public void rollback() { weekonlinetime = _xdb_saved; }
				};}});
		weekonlinetime = _v_;
	}

	@Override
	public void setLasttimeactiveday1(int _v_) { // 最近两次活跃的 当前天 day1比day2新,下同
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveday1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveday1) {
					public void rollback() { lasttimeactiveday1 = _xdb_saved; }
				};}});
		lasttimeactiveday1 = _v_;
	}

	@Override
	public void setLasttimeactiveday1onlinetime(int _v_) { // 最近一次日活跃的在线时长
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveday1onlinetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveday1onlinetime) {
					public void rollback() { lasttimeactiveday1onlinetime = _xdb_saved; }
				};}});
		lasttimeactiveday1onlinetime = _v_;
	}

	@Override
	public void setLasttimeactiveday2(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveday2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveday2) {
					public void rollback() { lasttimeactiveday2 = _xdb_saved; }
				};}});
		lasttimeactiveday2 = _v_;
	}

	@Override
	public void setLasttimeactiveweek1(int _v_) { // 最近两次活跃的 当前周 week1比week2新,下同
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveweek1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveweek1) {
					public void rollback() { lasttimeactiveweek1 = _xdb_saved; }
				};}});
		lasttimeactiveweek1 = _v_;
	}

	@Override
	public void setLasttimeactiveweek1onlinetime(int _v_) { // 最近一次周活跃的在线时长
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveweek1onlinetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveweek1onlinetime) {
					public void rollback() { lasttimeactiveweek1onlinetime = _xdb_saved; }
				};}});
		lasttimeactiveweek1onlinetime = _v_;
	}

	@Override
	public void setLasttimeactiveweek2(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttimeactiveweek2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttimeactiveweek2) {
					public void rollback() { lasttimeactiveweek2 = _xdb_saved; }
				};}});
		lasttimeactiveweek2 = _v_;
	}

	@Override
	public void setFirsttimeactiveday(int _v_) { // 第一次活跃的 当前天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firsttimeactiveday") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firsttimeactiveday) {
					public void rollback() { firsttimeactiveday = _xdb_saved; }
				};}});
		firsttimeactiveday = _v_;
	}

	@Override
	public void setFirsttimeactiveweek(int _v_) { // 第一次活跃的 当前周
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firsttimeactiveweek") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firsttimeactiveweek) {
					public void rollback() { firsttimeactiveweek = _xdb_saved; }
				};}});
		firsttimeactiveweek = _v_;
	}

	@Override
	public void setDealmoneyinplatform(long _v_) { // 交易平台挂单游戏币
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dealmoneyinplatform") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, dealmoneyinplatform) {
					public void rollback() { dealmoneyinplatform = _xdb_saved; }
				};}});
		dealmoneyinplatform = _v_;
	}

	@Override
	public void setTmpmoneyinplatform(long _v_) { // 交易平台暂存游戏币
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tmpmoneyinplatform") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, tmpmoneyinplatform) {
					public void rollback() { tmpmoneyinplatform = _xdb_saved; }
				};}});
		tmpmoneyinplatform = _v_;
	}

	@Override
	public void setMoneyincofc(long _v_) { // 商会资金
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "moneyincofc") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, moneyincofc) {
					public void rollback() { moneyincofc = _xdb_saved; }
				};}});
		moneyincofc = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ActiveRoleInfo _o_ = null;
		if ( _o1_ instanceof ActiveRoleInfo ) _o_ = (ActiveRoleInfo)_o1_;
		else if ( _o1_ instanceof ActiveRoleInfo.Const ) _o_ = ((ActiveRoleInfo.Const)_o1_).nThis();
		else return false;
		if (createtime != _o_.createtime) return false;
		if (enterworldtime != _o_.enterworldtime) return false;
		if (dayonline != _o_.dayonline) return false;
		if (dayonlinetime != _o_.dayonlinetime) return false;
		if (weekonline != _o_.weekonline) return false;
		if (weekonlinetime != _o_.weekonlinetime) return false;
		if (lasttimeactiveday1 != _o_.lasttimeactiveday1) return false;
		if (lasttimeactiveday1onlinetime != _o_.lasttimeactiveday1onlinetime) return false;
		if (lasttimeactiveday2 != _o_.lasttimeactiveday2) return false;
		if (lasttimeactiveweek1 != _o_.lasttimeactiveweek1) return false;
		if (lasttimeactiveweek1onlinetime != _o_.lasttimeactiveweek1onlinetime) return false;
		if (lasttimeactiveweek2 != _o_.lasttimeactiveweek2) return false;
		if (firsttimeactiveday != _o_.firsttimeactiveday) return false;
		if (firsttimeactiveweek != _o_.firsttimeactiveweek) return false;
		if (dealmoneyinplatform != _o_.dealmoneyinplatform) return false;
		if (tmpmoneyinplatform != _o_.tmpmoneyinplatform) return false;
		if (moneyincofc != _o_.moneyincofc) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += createtime;
		_h_ += enterworldtime;
		_h_ += dayonline;
		_h_ += dayonlinetime;
		_h_ += weekonline;
		_h_ += weekonlinetime;
		_h_ += lasttimeactiveday1;
		_h_ += lasttimeactiveday1onlinetime;
		_h_ += lasttimeactiveday2;
		_h_ += lasttimeactiveweek1;
		_h_ += lasttimeactiveweek1onlinetime;
		_h_ += lasttimeactiveweek2;
		_h_ += firsttimeactiveday;
		_h_ += firsttimeactiveweek;
		_h_ += dealmoneyinplatform;
		_h_ += tmpmoneyinplatform;
		_h_ += moneyincofc;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append(enterworldtime);
		_sb_.append(",");
		_sb_.append(dayonline);
		_sb_.append(",");
		_sb_.append(dayonlinetime);
		_sb_.append(",");
		_sb_.append(weekonline);
		_sb_.append(",");
		_sb_.append(weekonlinetime);
		_sb_.append(",");
		_sb_.append(lasttimeactiveday1);
		_sb_.append(",");
		_sb_.append(lasttimeactiveday1onlinetime);
		_sb_.append(",");
		_sb_.append(lasttimeactiveday2);
		_sb_.append(",");
		_sb_.append(lasttimeactiveweek1);
		_sb_.append(",");
		_sb_.append(lasttimeactiveweek1onlinetime);
		_sb_.append(",");
		_sb_.append(lasttimeactiveweek2);
		_sb_.append(",");
		_sb_.append(firsttimeactiveday);
		_sb_.append(",");
		_sb_.append(firsttimeactiveweek);
		_sb_.append(",");
		_sb_.append(dealmoneyinplatform);
		_sb_.append(",");
		_sb_.append(tmpmoneyinplatform);
		_sb_.append(",");
		_sb_.append(moneyincofc);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("enterworldtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dayonline"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dayonlinetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("weekonline"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("weekonlinetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveday1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveday1onlinetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveday2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveweek1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveweek1onlinetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttimeactiveweek2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firsttimeactiveday"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firsttimeactiveweek"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dealmoneyinplatform"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tmpmoneyinplatform"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("moneyincofc"));
		return lb;
	}

	private class Const implements xbean.ActiveRoleInfo {
		ActiveRoleInfo nThis() {
			return ActiveRoleInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ActiveRoleInfo copy() {
			return ActiveRoleInfo.this.copy();
		}

		@Override
		public xbean.ActiveRoleInfo toData() {
			return ActiveRoleInfo.this.toData();
		}

		public xbean.ActiveRoleInfo toBean() {
			return ActiveRoleInfo.this.toBean();
		}

		@Override
		public xbean.ActiveRoleInfo toDataIf() {
			return ActiveRoleInfo.this.toDataIf();
		}

		public xbean.ActiveRoleInfo toBeanIf() {
			return ActiveRoleInfo.this.toBeanIf();
		}

		@Override
		public int getCreatetime() { // 创建时间 当前天
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public long getEnterworldtime() { // 最近一次登录游戏的时间
			_xdb_verify_unsafe_();
			return enterworldtime;
		}

		@Override
		public int getDayonline() { // 在线时长 当前天 分钟为单位
			_xdb_verify_unsafe_();
			return dayonline;
		}

		@Override
		public int getDayonlinetime() { // 在线时长 当前天 分钟为单位
			_xdb_verify_unsafe_();
			return dayonlinetime;
		}

		@Override
		public int getWeekonline() { // 在线时长 当前周 分钟为单位
			_xdb_verify_unsafe_();
			return weekonline;
		}

		@Override
		public int getWeekonlinetime() { // 在线时长 当前周 分钟为单位
			_xdb_verify_unsafe_();
			return weekonlinetime;
		}

		@Override
		public int getLasttimeactiveday1() { // 最近两次活跃的 当前天 day1比day2新,下同
			_xdb_verify_unsafe_();
			return lasttimeactiveday1;
		}

		@Override
		public int getLasttimeactiveday1onlinetime() { // 最近一次日活跃的在线时长
			_xdb_verify_unsafe_();
			return lasttimeactiveday1onlinetime;
		}

		@Override
		public int getLasttimeactiveday2() { // 
			_xdb_verify_unsafe_();
			return lasttimeactiveday2;
		}

		@Override
		public int getLasttimeactiveweek1() { // 最近两次活跃的 当前周 week1比week2新,下同
			_xdb_verify_unsafe_();
			return lasttimeactiveweek1;
		}

		@Override
		public int getLasttimeactiveweek1onlinetime() { // 最近一次周活跃的在线时长
			_xdb_verify_unsafe_();
			return lasttimeactiveweek1onlinetime;
		}

		@Override
		public int getLasttimeactiveweek2() { // 
			_xdb_verify_unsafe_();
			return lasttimeactiveweek2;
		}

		@Override
		public int getFirsttimeactiveday() { // 第一次活跃的 当前天
			_xdb_verify_unsafe_();
			return firsttimeactiveday;
		}

		@Override
		public int getFirsttimeactiveweek() { // 第一次活跃的 当前周
			_xdb_verify_unsafe_();
			return firsttimeactiveweek;
		}

		@Override
		public long getDealmoneyinplatform() { // 交易平台挂单游戏币
			_xdb_verify_unsafe_();
			return dealmoneyinplatform;
		}

		@Override
		public long getTmpmoneyinplatform() { // 交易平台暂存游戏币
			_xdb_verify_unsafe_();
			return tmpmoneyinplatform;
		}

		@Override
		public long getMoneyincofc() { // 商会资金
			_xdb_verify_unsafe_();
			return moneyincofc;
		}

		@Override
		public void setCreatetime(int _v_) { // 创建时间 当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEnterworldtime(long _v_) { // 最近一次登录游戏的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDayonline(int _v_) { // 在线时长 当前天 分钟为单位
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDayonlinetime(int _v_) { // 在线时长 当前天 分钟为单位
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWeekonline(int _v_) { // 在线时长 当前周 分钟为单位
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWeekonlinetime(int _v_) { // 在线时长 当前周 分钟为单位
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveday1(int _v_) { // 最近两次活跃的 当前天 day1比day2新,下同
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveday1onlinetime(int _v_) { // 最近一次日活跃的在线时长
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveday2(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveweek1(int _v_) { // 最近两次活跃的 当前周 week1比week2新,下同
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveweek1onlinetime(int _v_) { // 最近一次周活跃的在线时长
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttimeactiveweek2(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirsttimeactiveday(int _v_) { // 第一次活跃的 当前天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirsttimeactiveweek(int _v_) { // 第一次活跃的 当前周
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDealmoneyinplatform(long _v_) { // 交易平台挂单游戏币
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTmpmoneyinplatform(long _v_) { // 交易平台暂存游戏币
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMoneyincofc(long _v_) { // 商会资金
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
			return ActiveRoleInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ActiveRoleInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ActiveRoleInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ActiveRoleInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ActiveRoleInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ActiveRoleInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ActiveRoleInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ActiveRoleInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ActiveRoleInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ActiveRoleInfo {
		private int createtime; // 创建时间 当前天
		private long enterworldtime; // 最近一次登录游戏的时间
		private int dayonline; // 在线时长 当前天 分钟为单位
		private int dayonlinetime; // 在线时长 当前天 分钟为单位
		private int weekonline; // 在线时长 当前周 分钟为单位
		private int weekonlinetime; // 在线时长 当前周 分钟为单位
		private int lasttimeactiveday1; // 最近两次活跃的 当前天 day1比day2新,下同
		private int lasttimeactiveday1onlinetime; // 最近一次日活跃的在线时长
		private int lasttimeactiveday2; // 
		private int lasttimeactiveweek1; // 最近两次活跃的 当前周 week1比week2新,下同
		private int lasttimeactiveweek1onlinetime; // 最近一次周活跃的在线时长
		private int lasttimeactiveweek2; // 
		private int firsttimeactiveday; // 第一次活跃的 当前天
		private int firsttimeactiveweek; // 第一次活跃的 当前周
		private long dealmoneyinplatform; // 交易平台挂单游戏币
		private long tmpmoneyinplatform; // 交易平台暂存游戏币
		private long moneyincofc; // 商会资金

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ActiveRoleInfo _o1_) {
			if (_o1_ instanceof ActiveRoleInfo) assign((ActiveRoleInfo)_o1_);
			else if (_o1_ instanceof ActiveRoleInfo.Data) assign((ActiveRoleInfo.Data)_o1_);
			else if (_o1_ instanceof ActiveRoleInfo.Const) assign(((ActiveRoleInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ActiveRoleInfo _o_) {
			createtime = _o_.createtime;
			enterworldtime = _o_.enterworldtime;
			dayonline = _o_.dayonline;
			dayonlinetime = _o_.dayonlinetime;
			weekonline = _o_.weekonline;
			weekonlinetime = _o_.weekonlinetime;
			lasttimeactiveday1 = _o_.lasttimeactiveday1;
			lasttimeactiveday1onlinetime = _o_.lasttimeactiveday1onlinetime;
			lasttimeactiveday2 = _o_.lasttimeactiveday2;
			lasttimeactiveweek1 = _o_.lasttimeactiveweek1;
			lasttimeactiveweek1onlinetime = _o_.lasttimeactiveweek1onlinetime;
			lasttimeactiveweek2 = _o_.lasttimeactiveweek2;
			firsttimeactiveday = _o_.firsttimeactiveday;
			firsttimeactiveweek = _o_.firsttimeactiveweek;
			dealmoneyinplatform = _o_.dealmoneyinplatform;
			tmpmoneyinplatform = _o_.tmpmoneyinplatform;
			moneyincofc = _o_.moneyincofc;
		}

		private void assign(ActiveRoleInfo.Data _o_) {
			createtime = _o_.createtime;
			enterworldtime = _o_.enterworldtime;
			dayonline = _o_.dayonline;
			dayonlinetime = _o_.dayonlinetime;
			weekonline = _o_.weekonline;
			weekonlinetime = _o_.weekonlinetime;
			lasttimeactiveday1 = _o_.lasttimeactiveday1;
			lasttimeactiveday1onlinetime = _o_.lasttimeactiveday1onlinetime;
			lasttimeactiveday2 = _o_.lasttimeactiveday2;
			lasttimeactiveweek1 = _o_.lasttimeactiveweek1;
			lasttimeactiveweek1onlinetime = _o_.lasttimeactiveweek1onlinetime;
			lasttimeactiveweek2 = _o_.lasttimeactiveweek2;
			firsttimeactiveday = _o_.firsttimeactiveday;
			firsttimeactiveweek = _o_.firsttimeactiveweek;
			dealmoneyinplatform = _o_.dealmoneyinplatform;
			tmpmoneyinplatform = _o_.tmpmoneyinplatform;
			moneyincofc = _o_.moneyincofc;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(createtime);
			_os_.marshal(enterworldtime);
			_os_.marshal(dayonline);
			_os_.marshal(dayonlinetime);
			_os_.marshal(weekonline);
			_os_.marshal(weekonlinetime);
			_os_.marshal(lasttimeactiveday1);
			_os_.marshal(lasttimeactiveday1onlinetime);
			_os_.marshal(lasttimeactiveday2);
			_os_.marshal(lasttimeactiveweek1);
			_os_.marshal(lasttimeactiveweek1onlinetime);
			_os_.marshal(lasttimeactiveweek2);
			_os_.marshal(firsttimeactiveday);
			_os_.marshal(firsttimeactiveweek);
			_os_.marshal(dealmoneyinplatform);
			_os_.marshal(tmpmoneyinplatform);
			_os_.marshal(moneyincofc);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			createtime = _os_.unmarshal_int();
			enterworldtime = _os_.unmarshal_long();
			dayonline = _os_.unmarshal_int();
			dayonlinetime = _os_.unmarshal_int();
			weekonline = _os_.unmarshal_int();
			weekonlinetime = _os_.unmarshal_int();
			lasttimeactiveday1 = _os_.unmarshal_int();
			lasttimeactiveday1onlinetime = _os_.unmarshal_int();
			lasttimeactiveday2 = _os_.unmarshal_int();
			lasttimeactiveweek1 = _os_.unmarshal_int();
			lasttimeactiveweek1onlinetime = _os_.unmarshal_int();
			lasttimeactiveweek2 = _os_.unmarshal_int();
			firsttimeactiveday = _os_.unmarshal_int();
			firsttimeactiveweek = _os_.unmarshal_int();
			dealmoneyinplatform = _os_.unmarshal_long();
			tmpmoneyinplatform = _os_.unmarshal_long();
			moneyincofc = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ActiveRoleInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ActiveRoleInfo toData() {
			return new Data(this);
		}

		public xbean.ActiveRoleInfo toBean() {
			return new ActiveRoleInfo(this, null, null);
		}

		@Override
		public xbean.ActiveRoleInfo toDataIf() {
			return this;
		}

		public xbean.ActiveRoleInfo toBeanIf() {
			return new ActiveRoleInfo(this, null, null);
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
		public int getCreatetime() { // 创建时间 当前天
			return createtime;
		}

		@Override
		public long getEnterworldtime() { // 最近一次登录游戏的时间
			return enterworldtime;
		}

		@Override
		public int getDayonline() { // 在线时长 当前天 分钟为单位
			return dayonline;
		}

		@Override
		public int getDayonlinetime() { // 在线时长 当前天 分钟为单位
			return dayonlinetime;
		}

		@Override
		public int getWeekonline() { // 在线时长 当前周 分钟为单位
			return weekonline;
		}

		@Override
		public int getWeekonlinetime() { // 在线时长 当前周 分钟为单位
			return weekonlinetime;
		}

		@Override
		public int getLasttimeactiveday1() { // 最近两次活跃的 当前天 day1比day2新,下同
			return lasttimeactiveday1;
		}

		@Override
		public int getLasttimeactiveday1onlinetime() { // 最近一次日活跃的在线时长
			return lasttimeactiveday1onlinetime;
		}

		@Override
		public int getLasttimeactiveday2() { // 
			return lasttimeactiveday2;
		}

		@Override
		public int getLasttimeactiveweek1() { // 最近两次活跃的 当前周 week1比week2新,下同
			return lasttimeactiveweek1;
		}

		@Override
		public int getLasttimeactiveweek1onlinetime() { // 最近一次周活跃的在线时长
			return lasttimeactiveweek1onlinetime;
		}

		@Override
		public int getLasttimeactiveweek2() { // 
			return lasttimeactiveweek2;
		}

		@Override
		public int getFirsttimeactiveday() { // 第一次活跃的 当前天
			return firsttimeactiveday;
		}

		@Override
		public int getFirsttimeactiveweek() { // 第一次活跃的 当前周
			return firsttimeactiveweek;
		}

		@Override
		public long getDealmoneyinplatform() { // 交易平台挂单游戏币
			return dealmoneyinplatform;
		}

		@Override
		public long getTmpmoneyinplatform() { // 交易平台暂存游戏币
			return tmpmoneyinplatform;
		}

		@Override
		public long getMoneyincofc() { // 商会资金
			return moneyincofc;
		}

		@Override
		public void setCreatetime(int _v_) { // 创建时间 当前天
			createtime = _v_;
		}

		@Override
		public void setEnterworldtime(long _v_) { // 最近一次登录游戏的时间
			enterworldtime = _v_;
		}

		@Override
		public void setDayonline(int _v_) { // 在线时长 当前天 分钟为单位
			dayonline = _v_;
		}

		@Override
		public void setDayonlinetime(int _v_) { // 在线时长 当前天 分钟为单位
			dayonlinetime = _v_;
		}

		@Override
		public void setWeekonline(int _v_) { // 在线时长 当前周 分钟为单位
			weekonline = _v_;
		}

		@Override
		public void setWeekonlinetime(int _v_) { // 在线时长 当前周 分钟为单位
			weekonlinetime = _v_;
		}

		@Override
		public void setLasttimeactiveday1(int _v_) { // 最近两次活跃的 当前天 day1比day2新,下同
			lasttimeactiveday1 = _v_;
		}

		@Override
		public void setLasttimeactiveday1onlinetime(int _v_) { // 最近一次日活跃的在线时长
			lasttimeactiveday1onlinetime = _v_;
		}

		@Override
		public void setLasttimeactiveday2(int _v_) { // 
			lasttimeactiveday2 = _v_;
		}

		@Override
		public void setLasttimeactiveweek1(int _v_) { // 最近两次活跃的 当前周 week1比week2新,下同
			lasttimeactiveweek1 = _v_;
		}

		@Override
		public void setLasttimeactiveweek1onlinetime(int _v_) { // 最近一次周活跃的在线时长
			lasttimeactiveweek1onlinetime = _v_;
		}

		@Override
		public void setLasttimeactiveweek2(int _v_) { // 
			lasttimeactiveweek2 = _v_;
		}

		@Override
		public void setFirsttimeactiveday(int _v_) { // 第一次活跃的 当前天
			firsttimeactiveday = _v_;
		}

		@Override
		public void setFirsttimeactiveweek(int _v_) { // 第一次活跃的 当前周
			firsttimeactiveweek = _v_;
		}

		@Override
		public void setDealmoneyinplatform(long _v_) { // 交易平台挂单游戏币
			dealmoneyinplatform = _v_;
		}

		@Override
		public void setTmpmoneyinplatform(long _v_) { // 交易平台暂存游戏币
			tmpmoneyinplatform = _v_;
		}

		@Override
		public void setMoneyincofc(long _v_) { // 商会资金
			moneyincofc = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ActiveRoleInfo.Data)) return false;
			ActiveRoleInfo.Data _o_ = (ActiveRoleInfo.Data) _o1_;
			if (createtime != _o_.createtime) return false;
			if (enterworldtime != _o_.enterworldtime) return false;
			if (dayonline != _o_.dayonline) return false;
			if (dayonlinetime != _o_.dayonlinetime) return false;
			if (weekonline != _o_.weekonline) return false;
			if (weekonlinetime != _o_.weekonlinetime) return false;
			if (lasttimeactiveday1 != _o_.lasttimeactiveday1) return false;
			if (lasttimeactiveday1onlinetime != _o_.lasttimeactiveday1onlinetime) return false;
			if (lasttimeactiveday2 != _o_.lasttimeactiveday2) return false;
			if (lasttimeactiveweek1 != _o_.lasttimeactiveweek1) return false;
			if (lasttimeactiveweek1onlinetime != _o_.lasttimeactiveweek1onlinetime) return false;
			if (lasttimeactiveweek2 != _o_.lasttimeactiveweek2) return false;
			if (firsttimeactiveday != _o_.firsttimeactiveday) return false;
			if (firsttimeactiveweek != _o_.firsttimeactiveweek) return false;
			if (dealmoneyinplatform != _o_.dealmoneyinplatform) return false;
			if (tmpmoneyinplatform != _o_.tmpmoneyinplatform) return false;
			if (moneyincofc != _o_.moneyincofc) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += createtime;
			_h_ += enterworldtime;
			_h_ += dayonline;
			_h_ += dayonlinetime;
			_h_ += weekonline;
			_h_ += weekonlinetime;
			_h_ += lasttimeactiveday1;
			_h_ += lasttimeactiveday1onlinetime;
			_h_ += lasttimeactiveday2;
			_h_ += lasttimeactiveweek1;
			_h_ += lasttimeactiveweek1onlinetime;
			_h_ += lasttimeactiveweek2;
			_h_ += firsttimeactiveday;
			_h_ += firsttimeactiveweek;
			_h_ += dealmoneyinplatform;
			_h_ += tmpmoneyinplatform;
			_h_ += moneyincofc;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append(enterworldtime);
			_sb_.append(",");
			_sb_.append(dayonline);
			_sb_.append(",");
			_sb_.append(dayonlinetime);
			_sb_.append(",");
			_sb_.append(weekonline);
			_sb_.append(",");
			_sb_.append(weekonlinetime);
			_sb_.append(",");
			_sb_.append(lasttimeactiveday1);
			_sb_.append(",");
			_sb_.append(lasttimeactiveday1onlinetime);
			_sb_.append(",");
			_sb_.append(lasttimeactiveday2);
			_sb_.append(",");
			_sb_.append(lasttimeactiveweek1);
			_sb_.append(",");
			_sb_.append(lasttimeactiveweek1onlinetime);
			_sb_.append(",");
			_sb_.append(lasttimeactiveweek2);
			_sb_.append(",");
			_sb_.append(firsttimeactiveday);
			_sb_.append(",");
			_sb_.append(firsttimeactiveweek);
			_sb_.append(",");
			_sb_.append(dealmoneyinplatform);
			_sb_.append(",");
			_sb_.append(tmpmoneyinplatform);
			_sb_.append(",");
			_sb_.append(moneyincofc);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

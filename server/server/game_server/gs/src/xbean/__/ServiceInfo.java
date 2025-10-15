
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ServiceInfo extends mkdb.XBean implements xbean.ServiceInfo {
	private int year; // 年
	private int weeks; // 当前开服第几周(今年的第几周)
	private int activerecommend; // 当前推荐活动
	private long updatetime; // 上次更新天数的时间
	private int days; // 累计开服天数

	@Override
	public void _reset_unsafe_() {
		year = 0;
		weeks = 0;
		activerecommend = 1;
		updatetime = 0L;
		days = 0;
	}

	ServiceInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		activerecommend = 1;
	}

	public ServiceInfo() {
		this(0, null, null);
	}

	public ServiceInfo(ServiceInfo _o_) {
		this(_o_, null, null);
	}

	ServiceInfo(xbean.ServiceInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ServiceInfo) assign((ServiceInfo)_o1_);
		else if (_o1_ instanceof ServiceInfo.Data) assign((ServiceInfo.Data)_o1_);
		else if (_o1_ instanceof ServiceInfo.Const) assign(((ServiceInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ServiceInfo _o_) {
		_o_._xdb_verify_unsafe_();
		year = _o_.year;
		weeks = _o_.weeks;
		activerecommend = _o_.activerecommend;
		updatetime = _o_.updatetime;
		days = _o_.days;
	}

	private void assign(ServiceInfo.Data _o_) {
		year = _o_.year;
		weeks = _o_.weeks;
		activerecommend = _o_.activerecommend;
		updatetime = _o_.updatetime;
		days = _o_.days;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(year);
		_os_.marshal(weeks);
		_os_.marshal(activerecommend);
		_os_.marshal(updatetime);
		_os_.marshal(days);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		year = _os_.unmarshal_int();
		weeks = _os_.unmarshal_int();
		activerecommend = _os_.unmarshal_int();
		updatetime = _os_.unmarshal_long();
		days = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ServiceInfo copy() {
		_xdb_verify_unsafe_();
		return new ServiceInfo(this);
	}

	@Override
	public xbean.ServiceInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ServiceInfo toBean() {
		_xdb_verify_unsafe_();
		return new ServiceInfo(this); // same as copy()
	}

	@Override
	public xbean.ServiceInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ServiceInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getYear() { // 年
		_xdb_verify_unsafe_();
		return year;
	}

	@Override
	public int getWeeks() { // 当前开服第几周(今年的第几周)
		_xdb_verify_unsafe_();
		return weeks;
	}

	@Override
	public int getActiverecommend() { // 当前推荐活动
		_xdb_verify_unsafe_();
		return activerecommend;
	}

	@Override
	public long getUpdatetime() { // 上次更新天数的时间
		_xdb_verify_unsafe_();
		return updatetime;
	}

	@Override
	public int getDays() { // 累计开服天数
		_xdb_verify_unsafe_();
		return days;
	}

	@Override
	public void setYear(int _v_) { // 年
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "year") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, year) {
					public void rollback() { year = _xdb_saved; }
				};}});
		year = _v_;
	}

	@Override
	public void setWeeks(int _v_) { // 当前开服第几周(今年的第几周)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "weeks") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, weeks) {
					public void rollback() { weeks = _xdb_saved; }
				};}});
		weeks = _v_;
	}

	@Override
	public void setActiverecommend(int _v_) { // 当前推荐活动
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activerecommend") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, activerecommend) {
					public void rollback() { activerecommend = _xdb_saved; }
				};}});
		activerecommend = _v_;
	}

	@Override
	public void setUpdatetime(long _v_) { // 上次更新天数的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "updatetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, updatetime) {
					public void rollback() { updatetime = _xdb_saved; }
				};}});
		updatetime = _v_;
	}

	@Override
	public void setDays(int _v_) { // 累计开服天数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "days") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, days) {
					public void rollback() { days = _xdb_saved; }
				};}});
		days = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ServiceInfo _o_ = null;
		if ( _o1_ instanceof ServiceInfo ) _o_ = (ServiceInfo)_o1_;
		else if ( _o1_ instanceof ServiceInfo.Const ) _o_ = ((ServiceInfo.Const)_o1_).nThis();
		else return false;
		if (year != _o_.year) return false;
		if (weeks != _o_.weeks) return false;
		if (activerecommend != _o_.activerecommend) return false;
		if (updatetime != _o_.updatetime) return false;
		if (days != _o_.days) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += year;
		_h_ += weeks;
		_h_ += activerecommend;
		_h_ += updatetime;
		_h_ += days;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(year);
		_sb_.append(",");
		_sb_.append(weeks);
		_sb_.append(",");
		_sb_.append(activerecommend);
		_sb_.append(",");
		_sb_.append(updatetime);
		_sb_.append(",");
		_sb_.append(days);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("year"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("weeks"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activerecommend"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("updatetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("days"));
		return lb;
	}

	private class Const implements xbean.ServiceInfo {
		ServiceInfo nThis() {
			return ServiceInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ServiceInfo copy() {
			return ServiceInfo.this.copy();
		}

		@Override
		public xbean.ServiceInfo toData() {
			return ServiceInfo.this.toData();
		}

		public xbean.ServiceInfo toBean() {
			return ServiceInfo.this.toBean();
		}

		@Override
		public xbean.ServiceInfo toDataIf() {
			return ServiceInfo.this.toDataIf();
		}

		public xbean.ServiceInfo toBeanIf() {
			return ServiceInfo.this.toBeanIf();
		}

		@Override
		public int getYear() { // 年
			_xdb_verify_unsafe_();
			return year;
		}

		@Override
		public int getWeeks() { // 当前开服第几周(今年的第几周)
			_xdb_verify_unsafe_();
			return weeks;
		}

		@Override
		public int getActiverecommend() { // 当前推荐活动
			_xdb_verify_unsafe_();
			return activerecommend;
		}

		@Override
		public long getUpdatetime() { // 上次更新天数的时间
			_xdb_verify_unsafe_();
			return updatetime;
		}

		@Override
		public int getDays() { // 累计开服天数
			_xdb_verify_unsafe_();
			return days;
		}

		@Override
		public void setYear(int _v_) { // 年
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWeeks(int _v_) { // 当前开服第几周(今年的第几周)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActiverecommend(int _v_) { // 当前推荐活动
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUpdatetime(long _v_) { // 上次更新天数的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDays(int _v_) { // 累计开服天数
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
			return ServiceInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ServiceInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ServiceInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ServiceInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ServiceInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ServiceInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ServiceInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ServiceInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ServiceInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ServiceInfo {
		private int year; // 年
		private int weeks; // 当前开服第几周(今年的第几周)
		private int activerecommend; // 当前推荐活动
		private long updatetime; // 上次更新天数的时间
		private int days; // 累计开服天数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			activerecommend = 1;
		}

		Data(xbean.ServiceInfo _o1_) {
			if (_o1_ instanceof ServiceInfo) assign((ServiceInfo)_o1_);
			else if (_o1_ instanceof ServiceInfo.Data) assign((ServiceInfo.Data)_o1_);
			else if (_o1_ instanceof ServiceInfo.Const) assign(((ServiceInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ServiceInfo _o_) {
			year = _o_.year;
			weeks = _o_.weeks;
			activerecommend = _o_.activerecommend;
			updatetime = _o_.updatetime;
			days = _o_.days;
		}

		private void assign(ServiceInfo.Data _o_) {
			year = _o_.year;
			weeks = _o_.weeks;
			activerecommend = _o_.activerecommend;
			updatetime = _o_.updatetime;
			days = _o_.days;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(year);
			_os_.marshal(weeks);
			_os_.marshal(activerecommend);
			_os_.marshal(updatetime);
			_os_.marshal(days);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			year = _os_.unmarshal_int();
			weeks = _os_.unmarshal_int();
			activerecommend = _os_.unmarshal_int();
			updatetime = _os_.unmarshal_long();
			days = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ServiceInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ServiceInfo toData() {
			return new Data(this);
		}

		public xbean.ServiceInfo toBean() {
			return new ServiceInfo(this, null, null);
		}

		@Override
		public xbean.ServiceInfo toDataIf() {
			return this;
		}

		public xbean.ServiceInfo toBeanIf() {
			return new ServiceInfo(this, null, null);
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
		public int getYear() { // 年
			return year;
		}

		@Override
		public int getWeeks() { // 当前开服第几周(今年的第几周)
			return weeks;
		}

		@Override
		public int getActiverecommend() { // 当前推荐活动
			return activerecommend;
		}

		@Override
		public long getUpdatetime() { // 上次更新天数的时间
			return updatetime;
		}

		@Override
		public int getDays() { // 累计开服天数
			return days;
		}

		@Override
		public void setYear(int _v_) { // 年
			year = _v_;
		}

		@Override
		public void setWeeks(int _v_) { // 当前开服第几周(今年的第几周)
			weeks = _v_;
		}

		@Override
		public void setActiverecommend(int _v_) { // 当前推荐活动
			activerecommend = _v_;
		}

		@Override
		public void setUpdatetime(long _v_) { // 上次更新天数的时间
			updatetime = _v_;
		}

		@Override
		public void setDays(int _v_) { // 累计开服天数
			days = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ServiceInfo.Data)) return false;
			ServiceInfo.Data _o_ = (ServiceInfo.Data) _o1_;
			if (year != _o_.year) return false;
			if (weeks != _o_.weeks) return false;
			if (activerecommend != _o_.activerecommend) return false;
			if (updatetime != _o_.updatetime) return false;
			if (days != _o_.days) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += year;
			_h_ += weeks;
			_h_ += activerecommend;
			_h_ += updatetime;
			_h_ += days;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(year);
			_sb_.append(",");
			_sb_.append(weeks);
			_sb_.append(",");
			_sb_.append(activerecommend);
			_sb_.append(",");
			_sb_.append(updatetime);
			_sb_.append(",");
			_sb_.append(days);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

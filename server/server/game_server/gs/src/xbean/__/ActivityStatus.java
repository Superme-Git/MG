
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ActivityStatus extends mkdb.XBean implements xbean.ActivityStatus {
	private int id; // 活动的id
	private int baseid; // 活动的baseid
	private long inistarttime; // 活动预计的开始的时间(填在表里的时间)
	private long iniendtime; // 活动预计的结束的时间(填在表里的时间)
	private long laststarttime; // 最近开始的时间
	private long lastendtime; // 最近结束的时间
	private long costtime; // 活动已经过去了多少时间

	@Override
	public void _reset_unsafe_() {
		id = 0;
		baseid = 0;
		inistarttime = 0L;
		iniendtime = 0L;
		laststarttime = 0L;
		lastendtime = 0L;
		costtime = 0L;
	}

	ActivityStatus(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ActivityStatus() {
		this(0, null, null);
	}

	public ActivityStatus(ActivityStatus _o_) {
		this(_o_, null, null);
	}

	ActivityStatus(xbean.ActivityStatus _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ActivityStatus) assign((ActivityStatus)_o1_);
		else if (_o1_ instanceof ActivityStatus.Data) assign((ActivityStatus.Data)_o1_);
		else if (_o1_ instanceof ActivityStatus.Const) assign(((ActivityStatus.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ActivityStatus _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		baseid = _o_.baseid;
		inistarttime = _o_.inistarttime;
		iniendtime = _o_.iniendtime;
		laststarttime = _o_.laststarttime;
		lastendtime = _o_.lastendtime;
		costtime = _o_.costtime;
	}

	private void assign(ActivityStatus.Data _o_) {
		id = _o_.id;
		baseid = _o_.baseid;
		inistarttime = _o_.inistarttime;
		iniendtime = _o_.iniendtime;
		laststarttime = _o_.laststarttime;
		lastendtime = _o_.lastendtime;
		costtime = _o_.costtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(baseid);
		_os_.marshal(inistarttime);
		_os_.marshal(iniendtime);
		_os_.marshal(laststarttime);
		_os_.marshal(lastendtime);
		_os_.marshal(costtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		baseid = _os_.unmarshal_int();
		inistarttime = _os_.unmarshal_long();
		iniendtime = _os_.unmarshal_long();
		laststarttime = _os_.unmarshal_long();
		lastendtime = _os_.unmarshal_long();
		costtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ActivityStatus copy() {
		_xdb_verify_unsafe_();
		return new ActivityStatus(this);
	}

	@Override
	public xbean.ActivityStatus toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActivityStatus toBean() {
		_xdb_verify_unsafe_();
		return new ActivityStatus(this); // same as copy()
	}

	@Override
	public xbean.ActivityStatus toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActivityStatus toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 活动的id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getBaseid() { // 活动的baseid
		_xdb_verify_unsafe_();
		return baseid;
	}

	@Override
	public long getInistarttime() { // 活动预计的开始的时间(填在表里的时间)
		_xdb_verify_unsafe_();
		return inistarttime;
	}

	@Override
	public long getIniendtime() { // 活动预计的结束的时间(填在表里的时间)
		_xdb_verify_unsafe_();
		return iniendtime;
	}

	@Override
	public long getLaststarttime() { // 最近开始的时间
		_xdb_verify_unsafe_();
		return laststarttime;
	}

	@Override
	public long getLastendtime() { // 最近结束的时间
		_xdb_verify_unsafe_();
		return lastendtime;
	}

	@Override
	public long getCosttime() { // 活动已经过去了多少时间
		_xdb_verify_unsafe_();
		return costtime;
	}

	@Override
	public void setId(int _v_) { // 活动的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setBaseid(int _v_) { // 活动的baseid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "baseid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, baseid) {
					public void rollback() { baseid = _xdb_saved; }
				};}});
		baseid = _v_;
	}

	@Override
	public void setInistarttime(long _v_) { // 活动预计的开始的时间(填在表里的时间)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "inistarttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, inistarttime) {
					public void rollback() { inistarttime = _xdb_saved; }
				};}});
		inistarttime = _v_;
	}

	@Override
	public void setIniendtime(long _v_) { // 活动预计的结束的时间(填在表里的时间)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "iniendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, iniendtime) {
					public void rollback() { iniendtime = _xdb_saved; }
				};}});
		iniendtime = _v_;
	}

	@Override
	public void setLaststarttime(long _v_) { // 最近开始的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "laststarttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, laststarttime) {
					public void rollback() { laststarttime = _xdb_saved; }
				};}});
		laststarttime = _v_;
	}

	@Override
	public void setLastendtime(long _v_) { // 最近结束的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastendtime) {
					public void rollback() { lastendtime = _xdb_saved; }
				};}});
		lastendtime = _v_;
	}

	@Override
	public void setCosttime(long _v_) { // 活动已经过去了多少时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "costtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, costtime) {
					public void rollback() { costtime = _xdb_saved; }
				};}});
		costtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ActivityStatus _o_ = null;
		if ( _o1_ instanceof ActivityStatus ) _o_ = (ActivityStatus)_o1_;
		else if ( _o1_ instanceof ActivityStatus.Const ) _o_ = ((ActivityStatus.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (baseid != _o_.baseid) return false;
		if (inistarttime != _o_.inistarttime) return false;
		if (iniendtime != _o_.iniendtime) return false;
		if (laststarttime != _o_.laststarttime) return false;
		if (lastendtime != _o_.lastendtime) return false;
		if (costtime != _o_.costtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += baseid;
		_h_ += inistarttime;
		_h_ += iniendtime;
		_h_ += laststarttime;
		_h_ += lastendtime;
		_h_ += costtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(baseid);
		_sb_.append(",");
		_sb_.append(inistarttime);
		_sb_.append(",");
		_sb_.append(iniendtime);
		_sb_.append(",");
		_sb_.append(laststarttime);
		_sb_.append(",");
		_sb_.append(lastendtime);
		_sb_.append(",");
		_sb_.append(costtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("baseid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inistarttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("iniendtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("laststarttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastendtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("costtime"));
		return lb;
	}

	private class Const implements xbean.ActivityStatus {
		ActivityStatus nThis() {
			return ActivityStatus.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ActivityStatus copy() {
			return ActivityStatus.this.copy();
		}

		@Override
		public xbean.ActivityStatus toData() {
			return ActivityStatus.this.toData();
		}

		public xbean.ActivityStatus toBean() {
			return ActivityStatus.this.toBean();
		}

		@Override
		public xbean.ActivityStatus toDataIf() {
			return ActivityStatus.this.toDataIf();
		}

		public xbean.ActivityStatus toBeanIf() {
			return ActivityStatus.this.toBeanIf();
		}

		@Override
		public int getId() { // 活动的id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getBaseid() { // 活动的baseid
			_xdb_verify_unsafe_();
			return baseid;
		}

		@Override
		public long getInistarttime() { // 活动预计的开始的时间(填在表里的时间)
			_xdb_verify_unsafe_();
			return inistarttime;
		}

		@Override
		public long getIniendtime() { // 活动预计的结束的时间(填在表里的时间)
			_xdb_verify_unsafe_();
			return iniendtime;
		}

		@Override
		public long getLaststarttime() { // 最近开始的时间
			_xdb_verify_unsafe_();
			return laststarttime;
		}

		@Override
		public long getLastendtime() { // 最近结束的时间
			_xdb_verify_unsafe_();
			return lastendtime;
		}

		@Override
		public long getCosttime() { // 活动已经过去了多少时间
			_xdb_verify_unsafe_();
			return costtime;
		}

		@Override
		public void setId(int _v_) { // 活动的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBaseid(int _v_) { // 活动的baseid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInistarttime(long _v_) { // 活动预计的开始的时间(填在表里的时间)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIniendtime(long _v_) { // 活动预计的结束的时间(填在表里的时间)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLaststarttime(long _v_) { // 最近开始的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastendtime(long _v_) { // 最近结束的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCosttime(long _v_) { // 活动已经过去了多少时间
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
			return ActivityStatus.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ActivityStatus.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ActivityStatus.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ActivityStatus.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ActivityStatus.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ActivityStatus.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ActivityStatus.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ActivityStatus.this.hashCode();
		}

		@Override
		public String toString() {
			return ActivityStatus.this.toString();
		}

	}

	public static final class Data implements xbean.ActivityStatus {
		private int id; // 活动的id
		private int baseid; // 活动的baseid
		private long inistarttime; // 活动预计的开始的时间(填在表里的时间)
		private long iniendtime; // 活动预计的结束的时间(填在表里的时间)
		private long laststarttime; // 最近开始的时间
		private long lastendtime; // 最近结束的时间
		private long costtime; // 活动已经过去了多少时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ActivityStatus _o1_) {
			if (_o1_ instanceof ActivityStatus) assign((ActivityStatus)_o1_);
			else if (_o1_ instanceof ActivityStatus.Data) assign((ActivityStatus.Data)_o1_);
			else if (_o1_ instanceof ActivityStatus.Const) assign(((ActivityStatus.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ActivityStatus _o_) {
			id = _o_.id;
			baseid = _o_.baseid;
			inistarttime = _o_.inistarttime;
			iniendtime = _o_.iniendtime;
			laststarttime = _o_.laststarttime;
			lastendtime = _o_.lastendtime;
			costtime = _o_.costtime;
		}

		private void assign(ActivityStatus.Data _o_) {
			id = _o_.id;
			baseid = _o_.baseid;
			inistarttime = _o_.inistarttime;
			iniendtime = _o_.iniendtime;
			laststarttime = _o_.laststarttime;
			lastendtime = _o_.lastendtime;
			costtime = _o_.costtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(baseid);
			_os_.marshal(inistarttime);
			_os_.marshal(iniendtime);
			_os_.marshal(laststarttime);
			_os_.marshal(lastendtime);
			_os_.marshal(costtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			baseid = _os_.unmarshal_int();
			inistarttime = _os_.unmarshal_long();
			iniendtime = _os_.unmarshal_long();
			laststarttime = _os_.unmarshal_long();
			lastendtime = _os_.unmarshal_long();
			costtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ActivityStatus copy() {
			return new Data(this);
		}

		@Override
		public xbean.ActivityStatus toData() {
			return new Data(this);
		}

		public xbean.ActivityStatus toBean() {
			return new ActivityStatus(this, null, null);
		}

		@Override
		public xbean.ActivityStatus toDataIf() {
			return this;
		}

		public xbean.ActivityStatus toBeanIf() {
			return new ActivityStatus(this, null, null);
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
		public int getId() { // 活动的id
			return id;
		}

		@Override
		public int getBaseid() { // 活动的baseid
			return baseid;
		}

		@Override
		public long getInistarttime() { // 活动预计的开始的时间(填在表里的时间)
			return inistarttime;
		}

		@Override
		public long getIniendtime() { // 活动预计的结束的时间(填在表里的时间)
			return iniendtime;
		}

		@Override
		public long getLaststarttime() { // 最近开始的时间
			return laststarttime;
		}

		@Override
		public long getLastendtime() { // 最近结束的时间
			return lastendtime;
		}

		@Override
		public long getCosttime() { // 活动已经过去了多少时间
			return costtime;
		}

		@Override
		public void setId(int _v_) { // 活动的id
			id = _v_;
		}

		@Override
		public void setBaseid(int _v_) { // 活动的baseid
			baseid = _v_;
		}

		@Override
		public void setInistarttime(long _v_) { // 活动预计的开始的时间(填在表里的时间)
			inistarttime = _v_;
		}

		@Override
		public void setIniendtime(long _v_) { // 活动预计的结束的时间(填在表里的时间)
			iniendtime = _v_;
		}

		@Override
		public void setLaststarttime(long _v_) { // 最近开始的时间
			laststarttime = _v_;
		}

		@Override
		public void setLastendtime(long _v_) { // 最近结束的时间
			lastendtime = _v_;
		}

		@Override
		public void setCosttime(long _v_) { // 活动已经过去了多少时间
			costtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ActivityStatus.Data)) return false;
			ActivityStatus.Data _o_ = (ActivityStatus.Data) _o1_;
			if (id != _o_.id) return false;
			if (baseid != _o_.baseid) return false;
			if (inistarttime != _o_.inistarttime) return false;
			if (iniendtime != _o_.iniendtime) return false;
			if (laststarttime != _o_.laststarttime) return false;
			if (lastendtime != _o_.lastendtime) return false;
			if (costtime != _o_.costtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += baseid;
			_h_ += inistarttime;
			_h_ += iniendtime;
			_h_ += laststarttime;
			_h_ += lastendtime;
			_h_ += costtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(baseid);
			_sb_.append(",");
			_sb_.append(inistarttime);
			_sb_.append(",");
			_sb_.append(iniendtime);
			_sb_.append(",");
			_sb_.append(laststarttime);
			_sb_.append(",");
			_sb_.append(lastendtime);
			_sb_.append(",");
			_sb_.append(costtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

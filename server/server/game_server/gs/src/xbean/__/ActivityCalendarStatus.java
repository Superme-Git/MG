
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ActivityCalendarStatus extends mkdb.XBean implements xbean.ActivityCalendarStatus {
	private int week; // 该记录是今年的第几周的
	private java.util.HashMap<Integer, xbean.DayFinishTask> daystatus; // 

	@Override
	public void _reset_unsafe_() {
		week = 0;
		daystatus.clear();
	}

	ActivityCalendarStatus(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>();
	}

	public ActivityCalendarStatus() {
		this(0, null, null);
	}

	public ActivityCalendarStatus(ActivityCalendarStatus _o_) {
		this(_o_, null, null);
	}

	ActivityCalendarStatus(xbean.ActivityCalendarStatus _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ActivityCalendarStatus) assign((ActivityCalendarStatus)_o1_);
		else if (_o1_ instanceof ActivityCalendarStatus.Data) assign((ActivityCalendarStatus.Data)_o1_);
		else if (_o1_ instanceof ActivityCalendarStatus.Const) assign(((ActivityCalendarStatus.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ActivityCalendarStatus _o_) {
		_o_._xdb_verify_unsafe_();
		week = _o_.week;
		daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>();
		for (java.util.Map.Entry<Integer, xbean.DayFinishTask> _e_ : _o_.daystatus.entrySet())
			daystatus.put(_e_.getKey(), new DayFinishTask(_e_.getValue(), this, "daystatus"));
	}

	private void assign(ActivityCalendarStatus.Data _o_) {
		week = _o_.week;
		daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>();
		for (java.util.Map.Entry<Integer, xbean.DayFinishTask> _e_ : _o_.daystatus.entrySet())
			daystatus.put(_e_.getKey(), new DayFinishTask(_e_.getValue(), this, "daystatus"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(week);
		_os_.compact_uint32(daystatus.size());
		for (java.util.Map.Entry<Integer, xbean.DayFinishTask> _e_ : daystatus.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		week = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.DayFinishTask _v_ = new DayFinishTask(0, this, "daystatus");
				_v_.unmarshal(_os_);
				daystatus.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ActivityCalendarStatus copy() {
		_xdb_verify_unsafe_();
		return new ActivityCalendarStatus(this);
	}

	@Override
	public xbean.ActivityCalendarStatus toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActivityCalendarStatus toBean() {
		_xdb_verify_unsafe_();
		return new ActivityCalendarStatus(this); // same as copy()
	}

	@Override
	public xbean.ActivityCalendarStatus toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActivityCalendarStatus toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getWeek() { // 该记录是今年的第几周的
		_xdb_verify_unsafe_();
		return week;
	}

	@Override
	public java.util.Map<Integer, xbean.DayFinishTask> getDaystatus() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "daystatus"), daystatus);
	}

	@Override
	public java.util.Map<Integer, xbean.DayFinishTask> getDaystatusAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.DayFinishTask> daystatus;
		ActivityCalendarStatus _o_ = this;
		daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>();
		for (java.util.Map.Entry<Integer, xbean.DayFinishTask> _e_ : _o_.daystatus.entrySet())
			daystatus.put(_e_.getKey(), new DayFinishTask.Data(_e_.getValue()));
		return daystatus;
	}

	@Override
	public void setWeek(int _v_) { // 该记录是今年的第几周的
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "week") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, week) {
					public void rollback() { week = _xdb_saved; }
				};}});
		week = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ActivityCalendarStatus _o_ = null;
		if ( _o1_ instanceof ActivityCalendarStatus ) _o_ = (ActivityCalendarStatus)_o1_;
		else if ( _o1_ instanceof ActivityCalendarStatus.Const ) _o_ = ((ActivityCalendarStatus.Const)_o1_).nThis();
		else return false;
		if (week != _o_.week) return false;
		if (!daystatus.equals(_o_.daystatus)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += week;
		_h_ += daystatus.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(week);
		_sb_.append(",");
		_sb_.append(daystatus);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("week"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("daystatus"));
		return lb;
	}

	private class Const implements xbean.ActivityCalendarStatus {
		ActivityCalendarStatus nThis() {
			return ActivityCalendarStatus.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ActivityCalendarStatus copy() {
			return ActivityCalendarStatus.this.copy();
		}

		@Override
		public xbean.ActivityCalendarStatus toData() {
			return ActivityCalendarStatus.this.toData();
		}

		public xbean.ActivityCalendarStatus toBean() {
			return ActivityCalendarStatus.this.toBean();
		}

		@Override
		public xbean.ActivityCalendarStatus toDataIf() {
			return ActivityCalendarStatus.this.toDataIf();
		}

		public xbean.ActivityCalendarStatus toBeanIf() {
			return ActivityCalendarStatus.this.toBeanIf();
		}

		@Override
		public int getWeek() { // 该记录是今年的第几周的
			_xdb_verify_unsafe_();
			return week;
		}

		@Override
		public java.util.Map<Integer, xbean.DayFinishTask> getDaystatus() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(daystatus);
		}

		@Override
		public java.util.Map<Integer, xbean.DayFinishTask> getDaystatusAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.DayFinishTask> daystatus;
			ActivityCalendarStatus _o_ = ActivityCalendarStatus.this;
			daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>();
			for (java.util.Map.Entry<Integer, xbean.DayFinishTask> _e_ : _o_.daystatus.entrySet())
				daystatus.put(_e_.getKey(), new DayFinishTask.Data(_e_.getValue()));
			return daystatus;
		}

		@Override
		public void setWeek(int _v_) { // 该记录是今年的第几周的
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
			return ActivityCalendarStatus.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ActivityCalendarStatus.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ActivityCalendarStatus.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ActivityCalendarStatus.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ActivityCalendarStatus.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ActivityCalendarStatus.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ActivityCalendarStatus.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ActivityCalendarStatus.this.hashCode();
		}

		@Override
		public String toString() {
			return ActivityCalendarStatus.this.toString();
		}

	}

	public static final class Data implements xbean.ActivityCalendarStatus {
		private int week; // 该记录是今年的第几周的
		private java.util.HashMap<Integer, xbean.DayFinishTask> daystatus; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>();
		}

		Data(xbean.ActivityCalendarStatus _o1_) {
			if (_o1_ instanceof ActivityCalendarStatus) assign((ActivityCalendarStatus)_o1_);
			else if (_o1_ instanceof ActivityCalendarStatus.Data) assign((ActivityCalendarStatus.Data)_o1_);
			else if (_o1_ instanceof ActivityCalendarStatus.Const) assign(((ActivityCalendarStatus.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ActivityCalendarStatus _o_) {
			week = _o_.week;
			daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>();
			for (java.util.Map.Entry<Integer, xbean.DayFinishTask> _e_ : _o_.daystatus.entrySet())
				daystatus.put(_e_.getKey(), new DayFinishTask.Data(_e_.getValue()));
		}

		private void assign(ActivityCalendarStatus.Data _o_) {
			week = _o_.week;
			daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>();
			for (java.util.Map.Entry<Integer, xbean.DayFinishTask> _e_ : _o_.daystatus.entrySet())
				daystatus.put(_e_.getKey(), new DayFinishTask.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(week);
			_os_.compact_uint32(daystatus.size());
			for (java.util.Map.Entry<Integer, xbean.DayFinishTask> _e_ : daystatus.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			week = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					daystatus = new java.util.HashMap<Integer, xbean.DayFinishTask>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.DayFinishTask _v_ = xbean.Pod.newDayFinishTaskData();
					_v_.unmarshal(_os_);
					daystatus.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ActivityCalendarStatus copy() {
			return new Data(this);
		}

		@Override
		public xbean.ActivityCalendarStatus toData() {
			return new Data(this);
		}

		public xbean.ActivityCalendarStatus toBean() {
			return new ActivityCalendarStatus(this, null, null);
		}

		@Override
		public xbean.ActivityCalendarStatus toDataIf() {
			return this;
		}

		public xbean.ActivityCalendarStatus toBeanIf() {
			return new ActivityCalendarStatus(this, null, null);
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
		public int getWeek() { // 该记录是今年的第几周的
			return week;
		}

		@Override
		public java.util.Map<Integer, xbean.DayFinishTask> getDaystatus() { // 
			return daystatus;
		}

		@Override
		public java.util.Map<Integer, xbean.DayFinishTask> getDaystatusAsData() { // 
			return daystatus;
		}

		@Override
		public void setWeek(int _v_) { // 该记录是今年的第几周的
			week = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ActivityCalendarStatus.Data)) return false;
			ActivityCalendarStatus.Data _o_ = (ActivityCalendarStatus.Data) _o1_;
			if (week != _o_.week) return false;
			if (!daystatus.equals(_o_.daystatus)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += week;
			_h_ += daystatus.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(week);
			_sb_.append(",");
			_sb_.append(daystatus);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

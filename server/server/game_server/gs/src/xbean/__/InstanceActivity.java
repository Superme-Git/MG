
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceActivity extends mkdb.XBean implements xbean.InstanceActivity {
	private int instanceid; // 副本id,跟任务配置.xlsx中的id对应
	private int state; // 状态,0=未开始；1=已开始；2=已结束
	private int baseid; // 活动基础id
	private long starttime; // 开始时间
	private long endtime; // 结束时间

	@Override
	public void _reset_unsafe_() {
		instanceid = 0;
		state = 0;
		baseid = 0;
		starttime = 0;
		endtime = 0;
	}

	InstanceActivity(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		starttime = 0;
		endtime = 0;
	}

	public InstanceActivity() {
		this(0, null, null);
	}

	public InstanceActivity(InstanceActivity _o_) {
		this(_o_, null, null);
	}

	InstanceActivity(xbean.InstanceActivity _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceActivity) assign((InstanceActivity)_o1_);
		else if (_o1_ instanceof InstanceActivity.Data) assign((InstanceActivity.Data)_o1_);
		else if (_o1_ instanceof InstanceActivity.Const) assign(((InstanceActivity.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceActivity _o_) {
		_o_._xdb_verify_unsafe_();
		instanceid = _o_.instanceid;
		state = _o_.state;
		baseid = _o_.baseid;
		starttime = _o_.starttime;
		endtime = _o_.endtime;
	}

	private void assign(InstanceActivity.Data _o_) {
		instanceid = _o_.instanceid;
		state = _o_.state;
		baseid = _o_.baseid;
		starttime = _o_.starttime;
		endtime = _o_.endtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(instanceid);
		_os_.marshal(state);
		_os_.marshal(baseid);
		_os_.marshal(starttime);
		_os_.marshal(endtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		instanceid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		baseid = _os_.unmarshal_int();
		starttime = _os_.unmarshal_long();
		endtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.InstanceActivity copy() {
		_xdb_verify_unsafe_();
		return new InstanceActivity(this);
	}

	@Override
	public xbean.InstanceActivity toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceActivity toBean() {
		_xdb_verify_unsafe_();
		return new InstanceActivity(this); // same as copy()
	}

	@Override
	public xbean.InstanceActivity toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceActivity toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getInstanceid() { // 副本id,跟任务配置.xlsx中的id对应
		_xdb_verify_unsafe_();
		return instanceid;
	}

	@Override
	public int getState() { // 状态,0=未开始；1=已开始；2=已结束
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public int getBaseid() { // 活动基础id
		_xdb_verify_unsafe_();
		return baseid;
	}

	@Override
	public long getStarttime() { // 开始时间
		_xdb_verify_unsafe_();
		return starttime;
	}

	@Override
	public long getEndtime() { // 结束时间
		_xdb_verify_unsafe_();
		return endtime;
	}

	@Override
	public void setInstanceid(int _v_) { // 副本id,跟任务配置.xlsx中的id对应
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instanceid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, instanceid) {
					public void rollback() { instanceid = _xdb_saved; }
				};}});
		instanceid = _v_;
	}

	@Override
	public void setState(int _v_) { // 状态,0=未开始；1=已开始；2=已结束
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setBaseid(int _v_) { // 活动基础id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "baseid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, baseid) {
					public void rollback() { baseid = _xdb_saved; }
				};}});
		baseid = _v_;
	}

	@Override
	public void setStarttime(long _v_) { // 开始时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "starttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, starttime) {
					public void rollback() { starttime = _xdb_saved; }
				};}});
		starttime = _v_;
	}

	@Override
	public void setEndtime(long _v_) { // 结束时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "endtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, endtime) {
					public void rollback() { endtime = _xdb_saved; }
				};}});
		endtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceActivity _o_ = null;
		if ( _o1_ instanceof InstanceActivity ) _o_ = (InstanceActivity)_o1_;
		else if ( _o1_ instanceof InstanceActivity.Const ) _o_ = ((InstanceActivity.Const)_o1_).nThis();
		else return false;
		if (instanceid != _o_.instanceid) return false;
		if (state != _o_.state) return false;
		if (baseid != _o_.baseid) return false;
		if (starttime != _o_.starttime) return false;
		if (endtime != _o_.endtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += instanceid;
		_h_ += state;
		_h_ += baseid;
		_h_ += starttime;
		_h_ += endtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instanceid);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(baseid);
		_sb_.append(",");
		_sb_.append(starttime);
		_sb_.append(",");
		_sb_.append(endtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instanceid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("baseid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("starttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endtime"));
		return lb;
	}

	private class Const implements xbean.InstanceActivity {
		InstanceActivity nThis() {
			return InstanceActivity.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceActivity copy() {
			return InstanceActivity.this.copy();
		}

		@Override
		public xbean.InstanceActivity toData() {
			return InstanceActivity.this.toData();
		}

		public xbean.InstanceActivity toBean() {
			return InstanceActivity.this.toBean();
		}

		@Override
		public xbean.InstanceActivity toDataIf() {
			return InstanceActivity.this.toDataIf();
		}

		public xbean.InstanceActivity toBeanIf() {
			return InstanceActivity.this.toBeanIf();
		}

		@Override
		public int getInstanceid() { // 副本id,跟任务配置.xlsx中的id对应
			_xdb_verify_unsafe_();
			return instanceid;
		}

		@Override
		public int getState() { // 状态,0=未开始；1=已开始；2=已结束
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public int getBaseid() { // 活动基础id
			_xdb_verify_unsafe_();
			return baseid;
		}

		@Override
		public long getStarttime() { // 开始时间
			_xdb_verify_unsafe_();
			return starttime;
		}

		@Override
		public long getEndtime() { // 结束时间
			_xdb_verify_unsafe_();
			return endtime;
		}

		@Override
		public void setInstanceid(int _v_) { // 副本id,跟任务配置.xlsx中的id对应
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 状态,0=未开始；1=已开始；2=已结束
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBaseid(int _v_) { // 活动基础id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStarttime(long _v_) { // 开始时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEndtime(long _v_) { // 结束时间
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
			return InstanceActivity.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceActivity.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceActivity.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceActivity.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceActivity.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceActivity.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceActivity.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceActivity.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceActivity.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceActivity {
		private int instanceid; // 副本id,跟任务配置.xlsx中的id对应
		private int state; // 状态,0=未开始；1=已开始；2=已结束
		private int baseid; // 活动基础id
		private long starttime; // 开始时间
		private long endtime; // 结束时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			starttime = 0;
			endtime = 0;
		}

		Data(xbean.InstanceActivity _o1_) {
			if (_o1_ instanceof InstanceActivity) assign((InstanceActivity)_o1_);
			else if (_o1_ instanceof InstanceActivity.Data) assign((InstanceActivity.Data)_o1_);
			else if (_o1_ instanceof InstanceActivity.Const) assign(((InstanceActivity.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceActivity _o_) {
			instanceid = _o_.instanceid;
			state = _o_.state;
			baseid = _o_.baseid;
			starttime = _o_.starttime;
			endtime = _o_.endtime;
		}

		private void assign(InstanceActivity.Data _o_) {
			instanceid = _o_.instanceid;
			state = _o_.state;
			baseid = _o_.baseid;
			starttime = _o_.starttime;
			endtime = _o_.endtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(instanceid);
			_os_.marshal(state);
			_os_.marshal(baseid);
			_os_.marshal(starttime);
			_os_.marshal(endtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			instanceid = _os_.unmarshal_int();
			state = _os_.unmarshal_int();
			baseid = _os_.unmarshal_int();
			starttime = _os_.unmarshal_long();
			endtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.InstanceActivity copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceActivity toData() {
			return new Data(this);
		}

		public xbean.InstanceActivity toBean() {
			return new InstanceActivity(this, null, null);
		}

		@Override
		public xbean.InstanceActivity toDataIf() {
			return this;
		}

		public xbean.InstanceActivity toBeanIf() {
			return new InstanceActivity(this, null, null);
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
		public int getInstanceid() { // 副本id,跟任务配置.xlsx中的id对应
			return instanceid;
		}

		@Override
		public int getState() { // 状态,0=未开始；1=已开始；2=已结束
			return state;
		}

		@Override
		public int getBaseid() { // 活动基础id
			return baseid;
		}

		@Override
		public long getStarttime() { // 开始时间
			return starttime;
		}

		@Override
		public long getEndtime() { // 结束时间
			return endtime;
		}

		@Override
		public void setInstanceid(int _v_) { // 副本id,跟任务配置.xlsx中的id对应
			instanceid = _v_;
		}

		@Override
		public void setState(int _v_) { // 状态,0=未开始；1=已开始；2=已结束
			state = _v_;
		}

		@Override
		public void setBaseid(int _v_) { // 活动基础id
			baseid = _v_;
		}

		@Override
		public void setStarttime(long _v_) { // 开始时间
			starttime = _v_;
		}

		@Override
		public void setEndtime(long _v_) { // 结束时间
			endtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceActivity.Data)) return false;
			InstanceActivity.Data _o_ = (InstanceActivity.Data) _o1_;
			if (instanceid != _o_.instanceid) return false;
			if (state != _o_.state) return false;
			if (baseid != _o_.baseid) return false;
			if (starttime != _o_.starttime) return false;
			if (endtime != _o_.endtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += instanceid;
			_h_ += state;
			_h_ += baseid;
			_h_ += starttime;
			_h_ += endtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(instanceid);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(baseid);
			_sb_.append(",");
			_sb_.append(starttime);
			_sb_.append(",");
			_sb_.append(endtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

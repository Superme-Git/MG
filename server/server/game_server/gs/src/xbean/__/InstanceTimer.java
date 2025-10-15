
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceTimer extends mkdb.XBean implements xbean.InstanceTimer {
	private int state; // 状态 0=未开始；1=已开始；2=已结束
	private long starttime; // 开始时间
	private long endtime; // 结束时间
	private long futurekey; // 在timerfutures中的key

	@Override
	public void _reset_unsafe_() {
		state = 0;
		starttime = 0L;
		endtime = 0L;
		futurekey = 0L;
	}

	InstanceTimer(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public InstanceTimer() {
		this(0, null, null);
	}

	public InstanceTimer(InstanceTimer _o_) {
		this(_o_, null, null);
	}

	InstanceTimer(xbean.InstanceTimer _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceTimer) assign((InstanceTimer)_o1_);
		else if (_o1_ instanceof InstanceTimer.Data) assign((InstanceTimer.Data)_o1_);
		else if (_o1_ instanceof InstanceTimer.Const) assign(((InstanceTimer.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceTimer _o_) {
		_o_._xdb_verify_unsafe_();
		state = _o_.state;
		starttime = _o_.starttime;
		endtime = _o_.endtime;
		futurekey = _o_.futurekey;
	}

	private void assign(InstanceTimer.Data _o_) {
		state = _o_.state;
		starttime = _o_.starttime;
		endtime = _o_.endtime;
		futurekey = _o_.futurekey;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(state);
		_os_.marshal(starttime);
		_os_.marshal(endtime);
		_os_.marshal(futurekey);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		state = _os_.unmarshal_int();
		starttime = _os_.unmarshal_long();
		endtime = _os_.unmarshal_long();
		futurekey = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.InstanceTimer copy() {
		_xdb_verify_unsafe_();
		return new InstanceTimer(this);
	}

	@Override
	public xbean.InstanceTimer toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceTimer toBean() {
		_xdb_verify_unsafe_();
		return new InstanceTimer(this); // same as copy()
	}

	@Override
	public xbean.InstanceTimer toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceTimer toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getState() { // 状态 0=未开始；1=已开始；2=已结束
		_xdb_verify_unsafe_();
		return state;
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
	public long getFuturekey() { // 在timerfutures中的key
		_xdb_verify_unsafe_();
		return futurekey;
	}

	@Override
	public void setState(int _v_) { // 状态 0=未开始；1=已开始；2=已结束
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
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
	public void setFuturekey(long _v_) { // 在timerfutures中的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "futurekey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, futurekey) {
					public void rollback() { futurekey = _xdb_saved; }
				};}});
		futurekey = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceTimer _o_ = null;
		if ( _o1_ instanceof InstanceTimer ) _o_ = (InstanceTimer)_o1_;
		else if ( _o1_ instanceof InstanceTimer.Const ) _o_ = ((InstanceTimer.Const)_o1_).nThis();
		else return false;
		if (state != _o_.state) return false;
		if (starttime != _o_.starttime) return false;
		if (endtime != _o_.endtime) return false;
		if (futurekey != _o_.futurekey) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += state;
		_h_ += starttime;
		_h_ += endtime;
		_h_ += futurekey;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(starttime);
		_sb_.append(",");
		_sb_.append(endtime);
		_sb_.append(",");
		_sb_.append(futurekey);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("starttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("futurekey"));
		return lb;
	}

	private class Const implements xbean.InstanceTimer {
		InstanceTimer nThis() {
			return InstanceTimer.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceTimer copy() {
			return InstanceTimer.this.copy();
		}

		@Override
		public xbean.InstanceTimer toData() {
			return InstanceTimer.this.toData();
		}

		public xbean.InstanceTimer toBean() {
			return InstanceTimer.this.toBean();
		}

		@Override
		public xbean.InstanceTimer toDataIf() {
			return InstanceTimer.this.toDataIf();
		}

		public xbean.InstanceTimer toBeanIf() {
			return InstanceTimer.this.toBeanIf();
		}

		@Override
		public int getState() { // 状态 0=未开始；1=已开始；2=已结束
			_xdb_verify_unsafe_();
			return state;
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
		public long getFuturekey() { // 在timerfutures中的key
			_xdb_verify_unsafe_();
			return futurekey;
		}

		@Override
		public void setState(int _v_) { // 状态 0=未开始；1=已开始；2=已结束
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
		public void setFuturekey(long _v_) { // 在timerfutures中的key
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
			return InstanceTimer.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceTimer.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceTimer.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceTimer.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceTimer.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceTimer.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceTimer.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceTimer.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceTimer.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceTimer {
		private int state; // 状态 0=未开始；1=已开始；2=已结束
		private long starttime; // 开始时间
		private long endtime; // 结束时间
		private long futurekey; // 在timerfutures中的key

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.InstanceTimer _o1_) {
			if (_o1_ instanceof InstanceTimer) assign((InstanceTimer)_o1_);
			else if (_o1_ instanceof InstanceTimer.Data) assign((InstanceTimer.Data)_o1_);
			else if (_o1_ instanceof InstanceTimer.Const) assign(((InstanceTimer.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceTimer _o_) {
			state = _o_.state;
			starttime = _o_.starttime;
			endtime = _o_.endtime;
			futurekey = _o_.futurekey;
		}

		private void assign(InstanceTimer.Data _o_) {
			state = _o_.state;
			starttime = _o_.starttime;
			endtime = _o_.endtime;
			futurekey = _o_.futurekey;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(state);
			_os_.marshal(starttime);
			_os_.marshal(endtime);
			_os_.marshal(futurekey);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			state = _os_.unmarshal_int();
			starttime = _os_.unmarshal_long();
			endtime = _os_.unmarshal_long();
			futurekey = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.InstanceTimer copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceTimer toData() {
			return new Data(this);
		}

		public xbean.InstanceTimer toBean() {
			return new InstanceTimer(this, null, null);
		}

		@Override
		public xbean.InstanceTimer toDataIf() {
			return this;
		}

		public xbean.InstanceTimer toBeanIf() {
			return new InstanceTimer(this, null, null);
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
		public int getState() { // 状态 0=未开始；1=已开始；2=已结束
			return state;
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
		public long getFuturekey() { // 在timerfutures中的key
			return futurekey;
		}

		@Override
		public void setState(int _v_) { // 状态 0=未开始；1=已开始；2=已结束
			state = _v_;
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
		public void setFuturekey(long _v_) { // 在timerfutures中的key
			futurekey = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceTimer.Data)) return false;
			InstanceTimer.Data _o_ = (InstanceTimer.Data) _o1_;
			if (state != _o_.state) return false;
			if (starttime != _o_.starttime) return false;
			if (endtime != _o_.endtime) return false;
			if (futurekey != _o_.futurekey) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += state;
			_h_ += starttime;
			_h_ += endtime;
			_h_ += futurekey;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(starttime);
			_sb_.append(",");
			_sb_.append(endtime);
			_sb_.append(",");
			_sb_.append(futurekey);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

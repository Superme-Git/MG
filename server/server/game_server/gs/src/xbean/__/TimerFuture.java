
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TimerFuture extends mkdb.XBean implements xbean.TimerFuture {
	private java.util.concurrent.ScheduledFuture<?> future; // 

	@Override
	public void _reset_unsafe_() {
		future = null;
	}

	TimerFuture(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		future = null;
	}

	public TimerFuture() {
		this(0, null, null);
	}

	public TimerFuture(TimerFuture _o_) {
		this(_o_, null, null);
	}

	TimerFuture(xbean.TimerFuture _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public xbean.TimerFuture copy() {
		_xdb_verify_unsafe_();
		return new TimerFuture(this);
	}

	@Override
	public xbean.TimerFuture toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TimerFuture toBean() {
		_xdb_verify_unsafe_();
		return new TimerFuture(this); // same as copy()
	}

	@Override
	public xbean.TimerFuture toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TimerFuture toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.concurrent.ScheduledFuture<?> getFuture() { // 
		_xdb_verify_unsafe_();
		return future;
	}

	@Override
	public void setFuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "future") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<java.util.concurrent.ScheduledFuture<?>>(this, future) {
					public void rollback() { future = _xdb_saved; }
			}; }});
		future = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TimerFuture _o_ = null;
		if ( _o1_ instanceof TimerFuture ) _o_ = (TimerFuture)_o1_;
		else if ( _o1_ instanceof TimerFuture.Const ) _o_ = ((TimerFuture.Const)_o1_).nThis();
		else return false;
		if ((null == future && null != _o_.future) || (null != future && null == _o_.future) || (null != future && null != _o_.future && !future.equals(_o_.future))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (future == null ? 0 : future.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(future);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("future"));
		return lb;
	}

	private class Const implements xbean.TimerFuture {
		TimerFuture nThis() {
			return TimerFuture.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TimerFuture copy() {
			return TimerFuture.this.copy();
		}

		@Override
		public xbean.TimerFuture toData() {
			return TimerFuture.this.toData();
		}

		public xbean.TimerFuture toBean() {
			return TimerFuture.this.toBean();
		}

		@Override
		public xbean.TimerFuture toDataIf() {
			return TimerFuture.this.toDataIf();
		}

		public xbean.TimerFuture toBeanIf() {
			return TimerFuture.this.toBeanIf();
		}

		@Override
		public java.util.concurrent.ScheduledFuture<?> getFuture() { // 
			_xdb_verify_unsafe_();
			return future;
		}

		@Override
		public void setFuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
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
			return TimerFuture.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TimerFuture.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TimerFuture.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TimerFuture.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TimerFuture.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TimerFuture.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TimerFuture.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TimerFuture.this.hashCode();
		}

		@Override
		public String toString() {
			return TimerFuture.this.toString();
		}

	}

	public static final class Data implements xbean.TimerFuture {
		private java.util.concurrent.ScheduledFuture<?> future; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			future = null;
		}

		Data(xbean.TimerFuture _o1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TimerFuture copy() {
			return new Data(this);
		}

		@Override
		public xbean.TimerFuture toData() {
			return new Data(this);
		}

		public xbean.TimerFuture toBean() {
			return new TimerFuture(this, null, null);
		}

		@Override
		public xbean.TimerFuture toDataIf() {
			return this;
		}

		public xbean.TimerFuture toBeanIf() {
			return new TimerFuture(this, null, null);
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
		public java.util.concurrent.ScheduledFuture<?> getFuture() { // 
			return future;
		}

		@Override
		public void setFuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
			future = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TimerFuture.Data)) return false;
			TimerFuture.Data _o_ = (TimerFuture.Data) _o1_;
			if ((null == future && null != _o_.future) || (null != future && null == _o_.future) || (null != future && null != _o_.future && !future.equals(_o_.future))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (future == null ? 0 : future.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(future);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

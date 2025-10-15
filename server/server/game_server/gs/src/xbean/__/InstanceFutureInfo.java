
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceFutureInfo extends mkdb.XBean implements xbean.InstanceFutureInfo {
	private java.util.concurrent.ScheduledFuture<?> timeoutfuture; // 

	@Override
	public void _reset_unsafe_() {
		timeoutfuture = null;
	}

	InstanceFutureInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		timeoutfuture = null;
	}

	public InstanceFutureInfo() {
		this(0, null, null);
	}

	public InstanceFutureInfo(InstanceFutureInfo _o_) {
		this(_o_, null, null);
	}

	InstanceFutureInfo(xbean.InstanceFutureInfo _o1_, mkdb.XBean _xp_, String _vn_) {
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
	public xbean.InstanceFutureInfo copy() {
		_xdb_verify_unsafe_();
		return new InstanceFutureInfo(this);
	}

	@Override
	public xbean.InstanceFutureInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceFutureInfo toBean() {
		_xdb_verify_unsafe_();
		return new InstanceFutureInfo(this); // same as copy()
	}

	@Override
	public xbean.InstanceFutureInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceFutureInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.concurrent.ScheduledFuture<?> getTimeoutfuture() { // 
		_xdb_verify_unsafe_();
		return timeoutfuture;
	}

	@Override
	public void setTimeoutfuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "timeoutfuture") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<java.util.concurrent.ScheduledFuture<?>>(this, timeoutfuture) {
					public void rollback() { timeoutfuture = _xdb_saved; }
			}; }});
		timeoutfuture = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceFutureInfo _o_ = null;
		if ( _o1_ instanceof InstanceFutureInfo ) _o_ = (InstanceFutureInfo)_o1_;
		else if ( _o1_ instanceof InstanceFutureInfo.Const ) _o_ = ((InstanceFutureInfo.Const)_o1_).nThis();
		else return false;
		if ((null == timeoutfuture && null != _o_.timeoutfuture) || (null != timeoutfuture && null == _o_.timeoutfuture) || (null != timeoutfuture && null != _o_.timeoutfuture && !timeoutfuture.equals(_o_.timeoutfuture))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (timeoutfuture == null ? 0 : timeoutfuture.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(timeoutfuture);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("timeoutfuture"));
		return lb;
	}

	private class Const implements xbean.InstanceFutureInfo {
		InstanceFutureInfo nThis() {
			return InstanceFutureInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceFutureInfo copy() {
			return InstanceFutureInfo.this.copy();
		}

		@Override
		public xbean.InstanceFutureInfo toData() {
			return InstanceFutureInfo.this.toData();
		}

		public xbean.InstanceFutureInfo toBean() {
			return InstanceFutureInfo.this.toBean();
		}

		@Override
		public xbean.InstanceFutureInfo toDataIf() {
			return InstanceFutureInfo.this.toDataIf();
		}

		public xbean.InstanceFutureInfo toBeanIf() {
			return InstanceFutureInfo.this.toBeanIf();
		}

		@Override
		public java.util.concurrent.ScheduledFuture<?> getTimeoutfuture() { // 
			_xdb_verify_unsafe_();
			return timeoutfuture;
		}

		@Override
		public void setTimeoutfuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
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
			return InstanceFutureInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceFutureInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceFutureInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceFutureInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceFutureInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceFutureInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceFutureInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceFutureInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceFutureInfo.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceFutureInfo {
		private java.util.concurrent.ScheduledFuture<?> timeoutfuture; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			timeoutfuture = null;
		}

		Data(xbean.InstanceFutureInfo _o1_) {
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
		public xbean.InstanceFutureInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceFutureInfo toData() {
			return new Data(this);
		}

		public xbean.InstanceFutureInfo toBean() {
			return new InstanceFutureInfo(this, null, null);
		}

		@Override
		public xbean.InstanceFutureInfo toDataIf() {
			return this;
		}

		public xbean.InstanceFutureInfo toBeanIf() {
			return new InstanceFutureInfo(this, null, null);
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
		public java.util.concurrent.ScheduledFuture<?> getTimeoutfuture() { // 
			return timeoutfuture;
		}

		@Override
		public void setTimeoutfuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
			timeoutfuture = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceFutureInfo.Data)) return false;
			InstanceFutureInfo.Data _o_ = (InstanceFutureInfo.Data) _o1_;
			if ((null == timeoutfuture && null != _o_.timeoutfuture) || (null != timeoutfuture && null == _o_.timeoutfuture) || (null != timeoutfuture && null != _o_.timeoutfuture && !timeoutfuture.equals(_o_.timeoutfuture))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (timeoutfuture == null ? 0 : timeoutfuture.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(timeoutfuture);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

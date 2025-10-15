
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class OfflineFuture extends mkdb.XBean implements xbean.OfflineFuture {
	private java.util.concurrent.ScheduledFuture<?> timefuture; // 人物下线后的计时任务

	@Override
	public void _reset_unsafe_() {
		timefuture = null;
	}

	OfflineFuture(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		timefuture = null;
	}

	public OfflineFuture() {
		this(0, null, null);
	}

	public OfflineFuture(OfflineFuture _o_) {
		this(_o_, null, null);
	}

	OfflineFuture(xbean.OfflineFuture _o1_, mkdb.XBean _xp_, String _vn_) {
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
	public xbean.OfflineFuture copy() {
		_xdb_verify_unsafe_();
		return new OfflineFuture(this);
	}

	@Override
	public xbean.OfflineFuture toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OfflineFuture toBean() {
		_xdb_verify_unsafe_();
		return new OfflineFuture(this); // same as copy()
	}

	@Override
	public xbean.OfflineFuture toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OfflineFuture toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.concurrent.ScheduledFuture<?> getTimefuture() { // 人物下线后的计时任务
		_xdb_verify_unsafe_();
		return timefuture;
	}

	@Override
	public void setTimefuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 人物下线后的计时任务
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "timefuture") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<java.util.concurrent.ScheduledFuture<?>>(this, timefuture) {
					public void rollback() { timefuture = _xdb_saved; }
			}; }});
		timefuture = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		OfflineFuture _o_ = null;
		if ( _o1_ instanceof OfflineFuture ) _o_ = (OfflineFuture)_o1_;
		else if ( _o1_ instanceof OfflineFuture.Const ) _o_ = ((OfflineFuture.Const)_o1_).nThis();
		else return false;
		if ((null == timefuture && null != _o_.timefuture) || (null != timefuture && null == _o_.timefuture) || (null != timefuture && null != _o_.timefuture && !timefuture.equals(_o_.timefuture))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (timefuture == null ? 0 : timefuture.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(timefuture);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("timefuture"));
		return lb;
	}

	private class Const implements xbean.OfflineFuture {
		OfflineFuture nThis() {
			return OfflineFuture.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.OfflineFuture copy() {
			return OfflineFuture.this.copy();
		}

		@Override
		public xbean.OfflineFuture toData() {
			return OfflineFuture.this.toData();
		}

		public xbean.OfflineFuture toBean() {
			return OfflineFuture.this.toBean();
		}

		@Override
		public xbean.OfflineFuture toDataIf() {
			return OfflineFuture.this.toDataIf();
		}

		public xbean.OfflineFuture toBeanIf() {
			return OfflineFuture.this.toBeanIf();
		}

		@Override
		public java.util.concurrent.ScheduledFuture<?> getTimefuture() { // 人物下线后的计时任务
			_xdb_verify_unsafe_();
			return timefuture;
		}

		@Override
		public void setTimefuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 人物下线后的计时任务
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
			return OfflineFuture.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return OfflineFuture.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return OfflineFuture.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return OfflineFuture.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return OfflineFuture.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return OfflineFuture.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return OfflineFuture.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return OfflineFuture.this.hashCode();
		}

		@Override
		public String toString() {
			return OfflineFuture.this.toString();
		}

	}

	public static final class Data implements xbean.OfflineFuture {
		private java.util.concurrent.ScheduledFuture<?> timefuture; // 人物下线后的计时任务

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			timefuture = null;
		}

		Data(xbean.OfflineFuture _o1_) {
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
		public xbean.OfflineFuture copy() {
			return new Data(this);
		}

		@Override
		public xbean.OfflineFuture toData() {
			return new Data(this);
		}

		public xbean.OfflineFuture toBean() {
			return new OfflineFuture(this, null, null);
		}

		@Override
		public xbean.OfflineFuture toDataIf() {
			return this;
		}

		public xbean.OfflineFuture toBeanIf() {
			return new OfflineFuture(this, null, null);
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
		public java.util.concurrent.ScheduledFuture<?> getTimefuture() { // 人物下线后的计时任务
			return timefuture;
		}

		@Override
		public void setTimefuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 人物下线后的计时任务
			timefuture = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof OfflineFuture.Data)) return false;
			OfflineFuture.Data _o_ = (OfflineFuture.Data) _o1_;
			if ((null == timefuture && null != _o_.timefuture) || (null != timefuture && null == _o_.timefuture) || (null != timefuture && null != _o_.timefuture && !timefuture.equals(_o_.timefuture))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (timefuture == null ? 0 : timefuture.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(timefuture);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

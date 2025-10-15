
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class OnlinerefreshFuture extends mkdb.XBean implements xbean.OnlinerefreshFuture {
	private java.util.concurrent.ScheduledFuture<?> furvec; // 

	@Override
	public void _reset_unsafe_() {
		furvec = null;
	}

	OnlinerefreshFuture(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		furvec = null;
	}

	public OnlinerefreshFuture() {
		this(0, null, null);
	}

	public OnlinerefreshFuture(OnlinerefreshFuture _o_) {
		this(_o_, null, null);
	}

	OnlinerefreshFuture(xbean.OnlinerefreshFuture _o1_, mkdb.XBean _xp_, String _vn_) {
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
	public xbean.OnlinerefreshFuture copy() {
		_xdb_verify_unsafe_();
		return new OnlinerefreshFuture(this);
	}

	@Override
	public xbean.OnlinerefreshFuture toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OnlinerefreshFuture toBean() {
		_xdb_verify_unsafe_();
		return new OnlinerefreshFuture(this); // same as copy()
	}

	@Override
	public xbean.OnlinerefreshFuture toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OnlinerefreshFuture toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.concurrent.ScheduledFuture<?> getFurvec() { // 
		_xdb_verify_unsafe_();
		return furvec;
	}

	@Override
	public void setFurvec(java.util.concurrent.ScheduledFuture<?> _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "furvec") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<java.util.concurrent.ScheduledFuture<?>>(this, furvec) {
					public void rollback() { furvec = _xdb_saved; }
			}; }});
		furvec = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		OnlinerefreshFuture _o_ = null;
		if ( _o1_ instanceof OnlinerefreshFuture ) _o_ = (OnlinerefreshFuture)_o1_;
		else if ( _o1_ instanceof OnlinerefreshFuture.Const ) _o_ = ((OnlinerefreshFuture.Const)_o1_).nThis();
		else return false;
		if ((null == furvec && null != _o_.furvec) || (null != furvec && null == _o_.furvec) || (null != furvec && null != _o_.furvec && !furvec.equals(_o_.furvec))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (furvec == null ? 0 : furvec.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(furvec);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("furvec"));
		return lb;
	}

	private class Const implements xbean.OnlinerefreshFuture {
		OnlinerefreshFuture nThis() {
			return OnlinerefreshFuture.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.OnlinerefreshFuture copy() {
			return OnlinerefreshFuture.this.copy();
		}

		@Override
		public xbean.OnlinerefreshFuture toData() {
			return OnlinerefreshFuture.this.toData();
		}

		public xbean.OnlinerefreshFuture toBean() {
			return OnlinerefreshFuture.this.toBean();
		}

		@Override
		public xbean.OnlinerefreshFuture toDataIf() {
			return OnlinerefreshFuture.this.toDataIf();
		}

		public xbean.OnlinerefreshFuture toBeanIf() {
			return OnlinerefreshFuture.this.toBeanIf();
		}

		@Override
		public java.util.concurrent.ScheduledFuture<?> getFurvec() { // 
			_xdb_verify_unsafe_();
			return furvec;
		}

		@Override
		public void setFurvec(java.util.concurrent.ScheduledFuture<?> _v_) { // 
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
			return OnlinerefreshFuture.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return OnlinerefreshFuture.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return OnlinerefreshFuture.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return OnlinerefreshFuture.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return OnlinerefreshFuture.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return OnlinerefreshFuture.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return OnlinerefreshFuture.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return OnlinerefreshFuture.this.hashCode();
		}

		@Override
		public String toString() {
			return OnlinerefreshFuture.this.toString();
		}

	}

	public static final class Data implements xbean.OnlinerefreshFuture {
		private java.util.concurrent.ScheduledFuture<?> furvec; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			furvec = null;
		}

		Data(xbean.OnlinerefreshFuture _o1_) {
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
		public xbean.OnlinerefreshFuture copy() {
			return new Data(this);
		}

		@Override
		public xbean.OnlinerefreshFuture toData() {
			return new Data(this);
		}

		public xbean.OnlinerefreshFuture toBean() {
			return new OnlinerefreshFuture(this, null, null);
		}

		@Override
		public xbean.OnlinerefreshFuture toDataIf() {
			return this;
		}

		public xbean.OnlinerefreshFuture toBeanIf() {
			return new OnlinerefreshFuture(this, null, null);
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
		public java.util.concurrent.ScheduledFuture<?> getFurvec() { // 
			return furvec;
		}

		@Override
		public void setFurvec(java.util.concurrent.ScheduledFuture<?> _v_) { // 
			furvec = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof OnlinerefreshFuture.Data)) return false;
			OnlinerefreshFuture.Data _o_ = (OnlinerefreshFuture.Data) _o1_;
			if ((null == furvec && null != _o_.furvec) || (null != furvec && null == _o_.furvec) || (null != furvec && null != _o_.furvec && !furvec.equals(_o_.furvec))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (furvec == null ? 0 : furvec.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(furvec);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AnYeLegendFuture extends mkdb.XBean implements xbean.AnYeLegendFuture {
	private java.util.concurrent.ScheduledFuture<?> legendfuture; // 

	@Override
	public void _reset_unsafe_() {
		legendfuture = null;
	}

	AnYeLegendFuture(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		legendfuture = null;
	}

	public AnYeLegendFuture() {
		this(0, null, null);
	}

	public AnYeLegendFuture(AnYeLegendFuture _o_) {
		this(_o_, null, null);
	}

	AnYeLegendFuture(xbean.AnYeLegendFuture _o1_, mkdb.XBean _xp_, String _vn_) {
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
	public xbean.AnYeLegendFuture copy() {
		_xdb_verify_unsafe_();
		return new AnYeLegendFuture(this);
	}

	@Override
	public xbean.AnYeLegendFuture toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AnYeLegendFuture toBean() {
		_xdb_verify_unsafe_();
		return new AnYeLegendFuture(this); // same as copy()
	}

	@Override
	public xbean.AnYeLegendFuture toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AnYeLegendFuture toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.concurrent.ScheduledFuture<?> getLegendfuture() { // 
		_xdb_verify_unsafe_();
		return legendfuture;
	}

	@Override
	public void setLegendfuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "legendfuture") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<java.util.concurrent.ScheduledFuture<?>>(this, legendfuture) {
					public void rollback() { legendfuture = _xdb_saved; }
			}; }});
		legendfuture = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AnYeLegendFuture _o_ = null;
		if ( _o1_ instanceof AnYeLegendFuture ) _o_ = (AnYeLegendFuture)_o1_;
		else if ( _o1_ instanceof AnYeLegendFuture.Const ) _o_ = ((AnYeLegendFuture.Const)_o1_).nThis();
		else return false;
		if ((null == legendfuture && null != _o_.legendfuture) || (null != legendfuture && null == _o_.legendfuture) || (null != legendfuture && null != _o_.legendfuture && !legendfuture.equals(_o_.legendfuture))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (legendfuture == null ? 0 : legendfuture.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(legendfuture);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("legendfuture"));
		return lb;
	}

	private class Const implements xbean.AnYeLegendFuture {
		AnYeLegendFuture nThis() {
			return AnYeLegendFuture.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AnYeLegendFuture copy() {
			return AnYeLegendFuture.this.copy();
		}

		@Override
		public xbean.AnYeLegendFuture toData() {
			return AnYeLegendFuture.this.toData();
		}

		public xbean.AnYeLegendFuture toBean() {
			return AnYeLegendFuture.this.toBean();
		}

		@Override
		public xbean.AnYeLegendFuture toDataIf() {
			return AnYeLegendFuture.this.toDataIf();
		}

		public xbean.AnYeLegendFuture toBeanIf() {
			return AnYeLegendFuture.this.toBeanIf();
		}

		@Override
		public java.util.concurrent.ScheduledFuture<?> getLegendfuture() { // 
			_xdb_verify_unsafe_();
			return legendfuture;
		}

		@Override
		public void setLegendfuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
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
			return AnYeLegendFuture.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AnYeLegendFuture.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AnYeLegendFuture.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AnYeLegendFuture.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AnYeLegendFuture.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AnYeLegendFuture.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AnYeLegendFuture.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AnYeLegendFuture.this.hashCode();
		}

		@Override
		public String toString() {
			return AnYeLegendFuture.this.toString();
		}

	}

	public static final class Data implements xbean.AnYeLegendFuture {
		private java.util.concurrent.ScheduledFuture<?> legendfuture; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			legendfuture = null;
		}

		Data(xbean.AnYeLegendFuture _o1_) {
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
		public xbean.AnYeLegendFuture copy() {
			return new Data(this);
		}

		@Override
		public xbean.AnYeLegendFuture toData() {
			return new Data(this);
		}

		public xbean.AnYeLegendFuture toBean() {
			return new AnYeLegendFuture(this, null, null);
		}

		@Override
		public xbean.AnYeLegendFuture toDataIf() {
			return this;
		}

		public xbean.AnYeLegendFuture toBeanIf() {
			return new AnYeLegendFuture(this, null, null);
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
		public java.util.concurrent.ScheduledFuture<?> getLegendfuture() { // 
			return legendfuture;
		}

		@Override
		public void setLegendfuture(java.util.concurrent.ScheduledFuture<?> _v_) { // 
			legendfuture = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AnYeLegendFuture.Data)) return false;
			AnYeLegendFuture.Data _o_ = (AnYeLegendFuture.Data) _o1_;
			if ((null == legendfuture && null != _o_.legendfuture) || (null != legendfuture && null == _o_.legendfuture) || (null != legendfuture && null != _o_.legendfuture && !legendfuture.equals(_o_.legendfuture))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (legendfuture == null ? 0 : legendfuture.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(legendfuture);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

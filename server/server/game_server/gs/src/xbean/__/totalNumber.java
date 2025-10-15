
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class totalNumber extends mkdb.XBean implements xbean.totalNumber {
	private long totalnumber; // 

	@Override
	public void _reset_unsafe_() {
		totalnumber = 0L;
	}

	totalNumber(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public totalNumber() {
		this(0, null, null);
	}

	public totalNumber(totalNumber _o_) {
		this(_o_, null, null);
	}

	totalNumber(xbean.totalNumber _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof totalNumber) assign((totalNumber)_o1_);
		else if (_o1_ instanceof totalNumber.Data) assign((totalNumber.Data)_o1_);
		else if (_o1_ instanceof totalNumber.Const) assign(((totalNumber.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(totalNumber _o_) {
		_o_._xdb_verify_unsafe_();
		totalnumber = _o_.totalnumber;
	}

	private void assign(totalNumber.Data _o_) {
		totalnumber = _o_.totalnumber;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(totalnumber);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		totalnumber = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.totalNumber copy() {
		_xdb_verify_unsafe_();
		return new totalNumber(this);
	}

	@Override
	public xbean.totalNumber toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.totalNumber toBean() {
		_xdb_verify_unsafe_();
		return new totalNumber(this); // same as copy()
	}

	@Override
	public xbean.totalNumber toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.totalNumber toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTotalnumber() { // 
		_xdb_verify_unsafe_();
		return totalnumber;
	}

	@Override
	public void setTotalnumber(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalnumber") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, totalnumber) {
					public void rollback() { totalnumber = _xdb_saved; }
				};}});
		totalnumber = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		totalNumber _o_ = null;
		if ( _o1_ instanceof totalNumber ) _o_ = (totalNumber)_o1_;
		else if ( _o1_ instanceof totalNumber.Const ) _o_ = ((totalNumber.Const)_o1_).nThis();
		else return false;
		if (totalnumber != _o_.totalnumber) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += totalnumber;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(totalnumber);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalnumber"));
		return lb;
	}

	private class Const implements xbean.totalNumber {
		totalNumber nThis() {
			return totalNumber.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.totalNumber copy() {
			return totalNumber.this.copy();
		}

		@Override
		public xbean.totalNumber toData() {
			return totalNumber.this.toData();
		}

		public xbean.totalNumber toBean() {
			return totalNumber.this.toBean();
		}

		@Override
		public xbean.totalNumber toDataIf() {
			return totalNumber.this.toDataIf();
		}

		public xbean.totalNumber toBeanIf() {
			return totalNumber.this.toBeanIf();
		}

		@Override
		public long getTotalnumber() { // 
			_xdb_verify_unsafe_();
			return totalnumber;
		}

		@Override
		public void setTotalnumber(long _v_) { // 
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
			return totalNumber.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return totalNumber.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return totalNumber.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return totalNumber.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return totalNumber.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return totalNumber.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return totalNumber.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return totalNumber.this.hashCode();
		}

		@Override
		public String toString() {
			return totalNumber.this.toString();
		}

	}

	public static final class Data implements xbean.totalNumber {
		private long totalnumber; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.totalNumber _o1_) {
			if (_o1_ instanceof totalNumber) assign((totalNumber)_o1_);
			else if (_o1_ instanceof totalNumber.Data) assign((totalNumber.Data)_o1_);
			else if (_o1_ instanceof totalNumber.Const) assign(((totalNumber.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(totalNumber _o_) {
			totalnumber = _o_.totalnumber;
		}

		private void assign(totalNumber.Data _o_) {
			totalnumber = _o_.totalnumber;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(totalnumber);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			totalnumber = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.totalNumber copy() {
			return new Data(this);
		}

		@Override
		public xbean.totalNumber toData() {
			return new Data(this);
		}

		public xbean.totalNumber toBean() {
			return new totalNumber(this, null, null);
		}

		@Override
		public xbean.totalNumber toDataIf() {
			return this;
		}

		public xbean.totalNumber toBeanIf() {
			return new totalNumber(this, null, null);
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
		public long getTotalnumber() { // 
			return totalnumber;
		}

		@Override
		public void setTotalnumber(long _v_) { // 
			totalnumber = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof totalNumber.Data)) return false;
			totalNumber.Data _o_ = (totalNumber.Data) _o1_;
			if (totalnumber != _o_.totalnumber) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += totalnumber;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(totalnumber);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

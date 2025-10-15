
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TransfromByItemData extends mkdb.XBean implements xbean.TransfromByItemData {
	private int useitemid; // 
	private int transformid; // 
	private long validdate; // 

	@Override
	public void _reset_unsafe_() {
		useitemid = 0;
		transformid = 0;
		validdate = 0L;
	}

	TransfromByItemData(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public TransfromByItemData() {
		this(0, null, null);
	}

	public TransfromByItemData(TransfromByItemData _o_) {
		this(_o_, null, null);
	}

	TransfromByItemData(xbean.TransfromByItemData _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TransfromByItemData) assign((TransfromByItemData)_o1_);
		else if (_o1_ instanceof TransfromByItemData.Data) assign((TransfromByItemData.Data)_o1_);
		else if (_o1_ instanceof TransfromByItemData.Const) assign(((TransfromByItemData.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TransfromByItemData _o_) {
		_o_._xdb_verify_unsafe_();
		useitemid = _o_.useitemid;
		transformid = _o_.transformid;
		validdate = _o_.validdate;
	}

	private void assign(TransfromByItemData.Data _o_) {
		useitemid = _o_.useitemid;
		transformid = _o_.transformid;
		validdate = _o_.validdate;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(useitemid);
		_os_.marshal(transformid);
		_os_.marshal(validdate);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		useitemid = _os_.unmarshal_int();
		transformid = _os_.unmarshal_int();
		validdate = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.TransfromByItemData copy() {
		_xdb_verify_unsafe_();
		return new TransfromByItemData(this);
	}

	@Override
	public xbean.TransfromByItemData toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TransfromByItemData toBean() {
		_xdb_verify_unsafe_();
		return new TransfromByItemData(this); // same as copy()
	}

	@Override
	public xbean.TransfromByItemData toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TransfromByItemData toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getUseitemid() { // 
		_xdb_verify_unsafe_();
		return useitemid;
	}

	@Override
	public int getTransformid() { // 
		_xdb_verify_unsafe_();
		return transformid;
	}

	@Override
	public long getValiddate() { // 
		_xdb_verify_unsafe_();
		return validdate;
	}

	@Override
	public void setUseitemid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "useitemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, useitemid) {
					public void rollback() { useitemid = _xdb_saved; }
				};}});
		useitemid = _v_;
	}

	@Override
	public void setTransformid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "transformid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, transformid) {
					public void rollback() { transformid = _xdb_saved; }
				};}});
		transformid = _v_;
	}

	@Override
	public void setValiddate(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "validdate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, validdate) {
					public void rollback() { validdate = _xdb_saved; }
				};}});
		validdate = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TransfromByItemData _o_ = null;
		if ( _o1_ instanceof TransfromByItemData ) _o_ = (TransfromByItemData)_o1_;
		else if ( _o1_ instanceof TransfromByItemData.Const ) _o_ = ((TransfromByItemData.Const)_o1_).nThis();
		else return false;
		if (useitemid != _o_.useitemid) return false;
		if (transformid != _o_.transformid) return false;
		if (validdate != _o_.validdate) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += useitemid;
		_h_ += transformid;
		_h_ += validdate;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(useitemid);
		_sb_.append(",");
		_sb_.append(transformid);
		_sb_.append(",");
		_sb_.append(validdate);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("useitemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("transformid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("validdate"));
		return lb;
	}

	private class Const implements xbean.TransfromByItemData {
		TransfromByItemData nThis() {
			return TransfromByItemData.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TransfromByItemData copy() {
			return TransfromByItemData.this.copy();
		}

		@Override
		public xbean.TransfromByItemData toData() {
			return TransfromByItemData.this.toData();
		}

		public xbean.TransfromByItemData toBean() {
			return TransfromByItemData.this.toBean();
		}

		@Override
		public xbean.TransfromByItemData toDataIf() {
			return TransfromByItemData.this.toDataIf();
		}

		public xbean.TransfromByItemData toBeanIf() {
			return TransfromByItemData.this.toBeanIf();
		}

		@Override
		public int getUseitemid() { // 
			_xdb_verify_unsafe_();
			return useitemid;
		}

		@Override
		public int getTransformid() { // 
			_xdb_verify_unsafe_();
			return transformid;
		}

		@Override
		public long getValiddate() { // 
			_xdb_verify_unsafe_();
			return validdate;
		}

		@Override
		public void setUseitemid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTransformid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValiddate(long _v_) { // 
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
			return TransfromByItemData.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TransfromByItemData.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TransfromByItemData.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TransfromByItemData.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TransfromByItemData.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TransfromByItemData.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TransfromByItemData.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TransfromByItemData.this.hashCode();
		}

		@Override
		public String toString() {
			return TransfromByItemData.this.toString();
		}

	}

	public static final class Data implements xbean.TransfromByItemData {
		private int useitemid; // 
		private int transformid; // 
		private long validdate; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.TransfromByItemData _o1_) {
			if (_o1_ instanceof TransfromByItemData) assign((TransfromByItemData)_o1_);
			else if (_o1_ instanceof TransfromByItemData.Data) assign((TransfromByItemData.Data)_o1_);
			else if (_o1_ instanceof TransfromByItemData.Const) assign(((TransfromByItemData.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TransfromByItemData _o_) {
			useitemid = _o_.useitemid;
			transformid = _o_.transformid;
			validdate = _o_.validdate;
		}

		private void assign(TransfromByItemData.Data _o_) {
			useitemid = _o_.useitemid;
			transformid = _o_.transformid;
			validdate = _o_.validdate;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(useitemid);
			_os_.marshal(transformid);
			_os_.marshal(validdate);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			useitemid = _os_.unmarshal_int();
			transformid = _os_.unmarshal_int();
			validdate = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.TransfromByItemData copy() {
			return new Data(this);
		}

		@Override
		public xbean.TransfromByItemData toData() {
			return new Data(this);
		}

		public xbean.TransfromByItemData toBean() {
			return new TransfromByItemData(this, null, null);
		}

		@Override
		public xbean.TransfromByItemData toDataIf() {
			return this;
		}

		public xbean.TransfromByItemData toBeanIf() {
			return new TransfromByItemData(this, null, null);
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
		public int getUseitemid() { // 
			return useitemid;
		}

		@Override
		public int getTransformid() { // 
			return transformid;
		}

		@Override
		public long getValiddate() { // 
			return validdate;
		}

		@Override
		public void setUseitemid(int _v_) { // 
			useitemid = _v_;
		}

		@Override
		public void setTransformid(int _v_) { // 
			transformid = _v_;
		}

		@Override
		public void setValiddate(long _v_) { // 
			validdate = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TransfromByItemData.Data)) return false;
			TransfromByItemData.Data _o_ = (TransfromByItemData.Data) _o1_;
			if (useitemid != _o_.useitemid) return false;
			if (transformid != _o_.transformid) return false;
			if (validdate != _o_.validdate) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += useitemid;
			_h_ += transformid;
			_h_ += validdate;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(useitemid);
			_sb_.append(",");
			_sb_.append(transformid);
			_sb_.append(",");
			_sb_.append(validdate);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

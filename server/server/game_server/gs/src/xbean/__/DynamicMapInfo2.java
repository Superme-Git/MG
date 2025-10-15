
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class DynamicMapInfo2 extends mkdb.XBean implements xbean.DynamicMapInfo2 {
	private int basemapid; // 

	@Override
	public void _reset_unsafe_() {
		basemapid = 0;
	}

	DynamicMapInfo2(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public DynamicMapInfo2() {
		this(0, null, null);
	}

	public DynamicMapInfo2(DynamicMapInfo2 _o_) {
		this(_o_, null, null);
	}

	DynamicMapInfo2(xbean.DynamicMapInfo2 _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof DynamicMapInfo2) assign((DynamicMapInfo2)_o1_);
		else if (_o1_ instanceof DynamicMapInfo2.Data) assign((DynamicMapInfo2.Data)_o1_);
		else if (_o1_ instanceof DynamicMapInfo2.Const) assign(((DynamicMapInfo2.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(DynamicMapInfo2 _o_) {
		_o_._xdb_verify_unsafe_();
		basemapid = _o_.basemapid;
	}

	private void assign(DynamicMapInfo2.Data _o_) {
		basemapid = _o_.basemapid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(basemapid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		basemapid = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.DynamicMapInfo2 copy() {
		_xdb_verify_unsafe_();
		return new DynamicMapInfo2(this);
	}

	@Override
	public xbean.DynamicMapInfo2 toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DynamicMapInfo2 toBean() {
		_xdb_verify_unsafe_();
		return new DynamicMapInfo2(this); // same as copy()
	}

	@Override
	public xbean.DynamicMapInfo2 toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DynamicMapInfo2 toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBasemapid() { // 
		_xdb_verify_unsafe_();
		return basemapid;
	}

	@Override
	public void setBasemapid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "basemapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, basemapid) {
					public void rollback() { basemapid = _xdb_saved; }
				};}});
		basemapid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		DynamicMapInfo2 _o_ = null;
		if ( _o1_ instanceof DynamicMapInfo2 ) _o_ = (DynamicMapInfo2)_o1_;
		else if ( _o1_ instanceof DynamicMapInfo2.Const ) _o_ = ((DynamicMapInfo2.Const)_o1_).nThis();
		else return false;
		if (basemapid != _o_.basemapid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += basemapid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(basemapid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("basemapid"));
		return lb;
	}

	private class Const implements xbean.DynamicMapInfo2 {
		DynamicMapInfo2 nThis() {
			return DynamicMapInfo2.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.DynamicMapInfo2 copy() {
			return DynamicMapInfo2.this.copy();
		}

		@Override
		public xbean.DynamicMapInfo2 toData() {
			return DynamicMapInfo2.this.toData();
		}

		public xbean.DynamicMapInfo2 toBean() {
			return DynamicMapInfo2.this.toBean();
		}

		@Override
		public xbean.DynamicMapInfo2 toDataIf() {
			return DynamicMapInfo2.this.toDataIf();
		}

		public xbean.DynamicMapInfo2 toBeanIf() {
			return DynamicMapInfo2.this.toBeanIf();
		}

		@Override
		public int getBasemapid() { // 
			_xdb_verify_unsafe_();
			return basemapid;
		}

		@Override
		public void setBasemapid(int _v_) { // 
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
			return DynamicMapInfo2.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return DynamicMapInfo2.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return DynamicMapInfo2.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return DynamicMapInfo2.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return DynamicMapInfo2.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return DynamicMapInfo2.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return DynamicMapInfo2.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return DynamicMapInfo2.this.hashCode();
		}

		@Override
		public String toString() {
			return DynamicMapInfo2.this.toString();
		}

	}

	public static final class Data implements xbean.DynamicMapInfo2 {
		private int basemapid; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.DynamicMapInfo2 _o1_) {
			if (_o1_ instanceof DynamicMapInfo2) assign((DynamicMapInfo2)_o1_);
			else if (_o1_ instanceof DynamicMapInfo2.Data) assign((DynamicMapInfo2.Data)_o1_);
			else if (_o1_ instanceof DynamicMapInfo2.Const) assign(((DynamicMapInfo2.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(DynamicMapInfo2 _o_) {
			basemapid = _o_.basemapid;
		}

		private void assign(DynamicMapInfo2.Data _o_) {
			basemapid = _o_.basemapid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(basemapid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			basemapid = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.DynamicMapInfo2 copy() {
			return new Data(this);
		}

		@Override
		public xbean.DynamicMapInfo2 toData() {
			return new Data(this);
		}

		public xbean.DynamicMapInfo2 toBean() {
			return new DynamicMapInfo2(this, null, null);
		}

		@Override
		public xbean.DynamicMapInfo2 toDataIf() {
			return this;
		}

		public xbean.DynamicMapInfo2 toBeanIf() {
			return new DynamicMapInfo2(this, null, null);
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
		public int getBasemapid() { // 
			return basemapid;
		}

		@Override
		public void setBasemapid(int _v_) { // 
			basemapid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof DynamicMapInfo2.Data)) return false;
			DynamicMapInfo2.Data _o_ = (DynamicMapInfo2.Data) _o1_;
			if (basemapid != _o_.basemapid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += basemapid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(basemapid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

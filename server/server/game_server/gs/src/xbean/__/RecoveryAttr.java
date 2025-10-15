
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RecoveryAttr extends mkdb.XBean implements xbean.RecoveryAttr {
	private int quality; // 

	@Override
	public void _reset_unsafe_() {
		quality = 0;
	}

	RecoveryAttr(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RecoveryAttr() {
		this(0, null, null);
	}

	public RecoveryAttr(RecoveryAttr _o_) {
		this(_o_, null, null);
	}

	RecoveryAttr(xbean.RecoveryAttr _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RecoveryAttr) assign((RecoveryAttr)_o1_);
		else if (_o1_ instanceof RecoveryAttr.Data) assign((RecoveryAttr.Data)_o1_);
		else if (_o1_ instanceof RecoveryAttr.Const) assign(((RecoveryAttr.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RecoveryAttr _o_) {
		_o_._xdb_verify_unsafe_();
		quality = _o_.quality;
	}

	private void assign(RecoveryAttr.Data _o_) {
		quality = _o_.quality;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(quality);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		quality = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RecoveryAttr copy() {
		_xdb_verify_unsafe_();
		return new RecoveryAttr(this);
	}

	@Override
	public xbean.RecoveryAttr toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RecoveryAttr toBean() {
		_xdb_verify_unsafe_();
		return new RecoveryAttr(this); // same as copy()
	}

	@Override
	public xbean.RecoveryAttr toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RecoveryAttr toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getQuality() { // 
		_xdb_verify_unsafe_();
		return quality;
	}

	@Override
	public void setQuality(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "quality") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, quality) {
					public void rollback() { quality = _xdb_saved; }
				};}});
		quality = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RecoveryAttr _o_ = null;
		if ( _o1_ instanceof RecoveryAttr ) _o_ = (RecoveryAttr)_o1_;
		else if ( _o1_ instanceof RecoveryAttr.Const ) _o_ = ((RecoveryAttr.Const)_o1_).nThis();
		else return false;
		if (quality != _o_.quality) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += quality;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(quality);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("quality"));
		return lb;
	}

	private class Const implements xbean.RecoveryAttr {
		RecoveryAttr nThis() {
			return RecoveryAttr.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RecoveryAttr copy() {
			return RecoveryAttr.this.copy();
		}

		@Override
		public xbean.RecoveryAttr toData() {
			return RecoveryAttr.this.toData();
		}

		public xbean.RecoveryAttr toBean() {
			return RecoveryAttr.this.toBean();
		}

		@Override
		public xbean.RecoveryAttr toDataIf() {
			return RecoveryAttr.this.toDataIf();
		}

		public xbean.RecoveryAttr toBeanIf() {
			return RecoveryAttr.this.toBeanIf();
		}

		@Override
		public int getQuality() { // 
			_xdb_verify_unsafe_();
			return quality;
		}

		@Override
		public void setQuality(int _v_) { // 
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
			return RecoveryAttr.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RecoveryAttr.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RecoveryAttr.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RecoveryAttr.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RecoveryAttr.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RecoveryAttr.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RecoveryAttr.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RecoveryAttr.this.hashCode();
		}

		@Override
		public String toString() {
			return RecoveryAttr.this.toString();
		}

	}

	public static final class Data implements xbean.RecoveryAttr {
		private int quality; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RecoveryAttr _o1_) {
			if (_o1_ instanceof RecoveryAttr) assign((RecoveryAttr)_o1_);
			else if (_o1_ instanceof RecoveryAttr.Data) assign((RecoveryAttr.Data)_o1_);
			else if (_o1_ instanceof RecoveryAttr.Const) assign(((RecoveryAttr.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RecoveryAttr _o_) {
			quality = _o_.quality;
		}

		private void assign(RecoveryAttr.Data _o_) {
			quality = _o_.quality;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(quality);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			quality = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RecoveryAttr copy() {
			return new Data(this);
		}

		@Override
		public xbean.RecoveryAttr toData() {
			return new Data(this);
		}

		public xbean.RecoveryAttr toBean() {
			return new RecoveryAttr(this, null, null);
		}

		@Override
		public xbean.RecoveryAttr toDataIf() {
			return this;
		}

		public xbean.RecoveryAttr toBeanIf() {
			return new RecoveryAttr(this, null, null);
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
		public int getQuality() { // 
			return quality;
		}

		@Override
		public void setQuality(int _v_) { // 
			quality = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RecoveryAttr.Data)) return false;
			RecoveryAttr.Data _o_ = (RecoveryAttr.Data) _o1_;
			if (quality != _o_.quality) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += quality;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(quality);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

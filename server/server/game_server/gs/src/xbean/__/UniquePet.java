
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class UniquePet extends mkdb.XBean implements xbean.UniquePet {
	private long roleid; // 所属角色

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
	}

	UniquePet(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public UniquePet() {
		this(0, null, null);
	}

	public UniquePet(UniquePet _o_) {
		this(_o_, null, null);
	}

	UniquePet(xbean.UniquePet _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof UniquePet) assign((UniquePet)_o1_);
		else if (_o1_ instanceof UniquePet.Data) assign((UniquePet.Data)_o1_);
		else if (_o1_ instanceof UniquePet.Const) assign(((UniquePet.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(UniquePet _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
	}

	private void assign(UniquePet.Data _o_) {
		roleid = _o_.roleid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.UniquePet copy() {
		_xdb_verify_unsafe_();
		return new UniquePet(this);
	}

	@Override
	public xbean.UniquePet toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.UniquePet toBean() {
		_xdb_verify_unsafe_();
		return new UniquePet(this); // same as copy()
	}

	@Override
	public xbean.UniquePet toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.UniquePet toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 所属角色
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public void setRoleid(long _v_) { // 所属角色
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		UniquePet _o_ = null;
		if ( _o1_ instanceof UniquePet ) _o_ = (UniquePet)_o1_;
		else if ( _o1_ instanceof UniquePet.Const ) _o_ = ((UniquePet.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		return lb;
	}

	private class Const implements xbean.UniquePet {
		UniquePet nThis() {
			return UniquePet.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.UniquePet copy() {
			return UniquePet.this.copy();
		}

		@Override
		public xbean.UniquePet toData() {
			return UniquePet.this.toData();
		}

		public xbean.UniquePet toBean() {
			return UniquePet.this.toBean();
		}

		@Override
		public xbean.UniquePet toDataIf() {
			return UniquePet.this.toDataIf();
		}

		public xbean.UniquePet toBeanIf() {
			return UniquePet.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 所属角色
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public void setRoleid(long _v_) { // 所属角色
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
			return UniquePet.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return UniquePet.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return UniquePet.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return UniquePet.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return UniquePet.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return UniquePet.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return UniquePet.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return UniquePet.this.hashCode();
		}

		@Override
		public String toString() {
			return UniquePet.this.toString();
		}

	}

	public static final class Data implements xbean.UniquePet {
		private long roleid; // 所属角色

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.UniquePet _o1_) {
			if (_o1_ instanceof UniquePet) assign((UniquePet)_o1_);
			else if (_o1_ instanceof UniquePet.Data) assign((UniquePet.Data)_o1_);
			else if (_o1_ instanceof UniquePet.Const) assign(((UniquePet.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(UniquePet _o_) {
			roleid = _o_.roleid;
		}

		private void assign(UniquePet.Data _o_) {
			roleid = _o_.roleid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.UniquePet copy() {
			return new Data(this);
		}

		@Override
		public xbean.UniquePet toData() {
			return new Data(this);
		}

		public xbean.UniquePet toBean() {
			return new UniquePet(this, null, null);
		}

		@Override
		public xbean.UniquePet toDataIf() {
			return this;
		}

		public xbean.UniquePet toBeanIf() {
			return new UniquePet(this, null, null);
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
		public long getRoleid() { // 所属角色
			return roleid;
		}

		@Override
		public void setRoleid(long _v_) { // 所属角色
			roleid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof UniquePet.Data)) return false;
			UniquePet.Data _o_ = (UniquePet.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

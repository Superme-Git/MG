
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AttentionRole extends mkdb.XBean implements xbean.AttentionRole {
	private mkdb.util.SetX<Long> roleids; // 角色id

	@Override
	public void _reset_unsafe_() {
		roleids.clear();
	}

	AttentionRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		roleids = new mkdb.util.SetX<Long>();
	}

	public AttentionRole() {
		this(0, null, null);
	}

	public AttentionRole(AttentionRole _o_) {
		this(_o_, null, null);
	}

	AttentionRole(xbean.AttentionRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AttentionRole) assign((AttentionRole)_o1_);
		else if (_o1_ instanceof AttentionRole.Data) assign((AttentionRole.Data)_o1_);
		else if (_o1_ instanceof AttentionRole.Const) assign(((AttentionRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AttentionRole _o_) {
		_o_._xdb_verify_unsafe_();
		roleids = new mkdb.util.SetX<Long>();
		roleids.addAll(_o_.roleids);
	}

	private void assign(AttentionRole.Data _o_) {
		roleids = new mkdb.util.SetX<Long>();
		roleids.addAll(_o_.roleids);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.AttentionRole copy() {
		_xdb_verify_unsafe_();
		return new AttentionRole(this);
	}

	@Override
	public xbean.AttentionRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AttentionRole toBean() {
		_xdb_verify_unsafe_();
		return new AttentionRole(this); // same as copy()
	}

	@Override
	public xbean.AttentionRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AttentionRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Set<Long> getRoleids() { // 角色id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logSet(new mkdb.LogKey(this, "roleids"), roleids);
	}

	public java.util.Set<Long> getRoleidsAsData() { // 角色id
		_xdb_verify_unsafe_();
		java.util.Set<Long> roleids;
		AttentionRole _o_ = this;
		roleids = new mkdb.util.SetX<Long>();
		roleids.addAll(_o_.roleids);
		return roleids;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AttentionRole _o_ = null;
		if ( _o1_ instanceof AttentionRole ) _o_ = (AttentionRole)_o1_;
		else if ( _o1_ instanceof AttentionRole.Const ) _o_ = ((AttentionRole.Const)_o1_).nThis();
		else return false;
		if (!roleids.equals(_o_.roleids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableSet().setVarName("roleids"));
		return lb;
	}

	private class Const implements xbean.AttentionRole {
		AttentionRole nThis() {
			return AttentionRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AttentionRole copy() {
			return AttentionRole.this.copy();
		}

		@Override
		public xbean.AttentionRole toData() {
			return AttentionRole.this.toData();
		}

		public xbean.AttentionRole toBean() {
			return AttentionRole.this.toBean();
		}

		@Override
		public xbean.AttentionRole toDataIf() {
			return AttentionRole.this.toDataIf();
		}

		public xbean.AttentionRole toBeanIf() {
			return AttentionRole.this.toBeanIf();
		}

		@Override
		public java.util.Set<Long> getRoleids() { // 角色id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constSet(roleids);
		}

		public java.util.Set<Long> getRoleidsAsData() { // 角色id
			_xdb_verify_unsafe_();
			java.util.Set<Long> roleids;
			AttentionRole _o_ = AttentionRole.this;
		roleids = new mkdb.util.SetX<Long>();
		roleids.addAll(_o_.roleids);
			return roleids;
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
			return AttentionRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AttentionRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AttentionRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AttentionRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AttentionRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AttentionRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AttentionRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AttentionRole.this.hashCode();
		}

		@Override
		public String toString() {
			return AttentionRole.this.toString();
		}

	}

	public static final class Data implements xbean.AttentionRole {
		private java.util.HashSet<Long> roleids; // 角色id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			roleids = new java.util.HashSet<Long>();
		}

		Data(xbean.AttentionRole _o1_) {
			if (_o1_ instanceof AttentionRole) assign((AttentionRole)_o1_);
			else if (_o1_ instanceof AttentionRole.Data) assign((AttentionRole.Data)_o1_);
			else if (_o1_ instanceof AttentionRole.Const) assign(((AttentionRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AttentionRole _o_) {
			roleids = new java.util.HashSet<Long>();
			roleids.addAll(_o_.roleids);
		}

		private void assign(AttentionRole.Data _o_) {
			roleids = new java.util.HashSet<Long>();
			roleids.addAll(_o_.roleids);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(roleids.size());
			for (Long _v_ : roleids) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				roleids.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.AttentionRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.AttentionRole toData() {
			return new Data(this);
		}

		public xbean.AttentionRole toBean() {
			return new AttentionRole(this, null, null);
		}

		@Override
		public xbean.AttentionRole toDataIf() {
			return this;
		}

		public xbean.AttentionRole toBeanIf() {
			return new AttentionRole(this, null, null);
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
		public java.util.Set<Long> getRoleids() { // 角色id
			return roleids;
		}

		@Override
		public java.util.Set<Long> getRoleidsAsData() { // 角色id
			return roleids;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AttentionRole.Data)) return false;
			AttentionRole.Data _o_ = (AttentionRole.Data) _o1_;
			if (!roleids.equals(_o_.roleids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

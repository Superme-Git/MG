
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ServerRoles extends mkdb.XBean implements xbean.ServerRoles {
	private int rolenum; // 
	private long createtime; // 

	@Override
	public void _reset_unsafe_() {
		rolenum = 0;
		createtime = 0L;
	}

	ServerRoles(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ServerRoles() {
		this(0, null, null);
	}

	public ServerRoles(ServerRoles _o_) {
		this(_o_, null, null);
	}

	ServerRoles(xbean.ServerRoles _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ServerRoles) assign((ServerRoles)_o1_);
		else if (_o1_ instanceof ServerRoles.Data) assign((ServerRoles.Data)_o1_);
		else if (_o1_ instanceof ServerRoles.Const) assign(((ServerRoles.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ServerRoles _o_) {
		_o_._xdb_verify_unsafe_();
		rolenum = _o_.rolenum;
		createtime = _o_.createtime;
	}

	private void assign(ServerRoles.Data _o_) {
		rolenum = _o_.rolenum;
		createtime = _o_.createtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(rolenum);
		_os_.marshal(createtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		rolenum = _os_.unmarshal_int();
		createtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ServerRoles copy() {
		_xdb_verify_unsafe_();
		return new ServerRoles(this);
	}

	@Override
	public xbean.ServerRoles toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ServerRoles toBean() {
		_xdb_verify_unsafe_();
		return new ServerRoles(this); // same as copy()
	}

	@Override
	public xbean.ServerRoles toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ServerRoles toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getRolenum() { // 
		_xdb_verify_unsafe_();
		return rolenum;
	}

	@Override
	public long getCreatetime() { // 
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public void setRolenum(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rolenum) {
					public void rollback() { rolenum = _xdb_saved; }
				};}});
		rolenum = _v_;
	}

	@Override
	public void setCreatetime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ServerRoles _o_ = null;
		if ( _o1_ instanceof ServerRoles ) _o_ = (ServerRoles)_o1_;
		else if ( _o1_ instanceof ServerRoles.Const ) _o_ = ((ServerRoles.Const)_o1_).nThis();
		else return false;
		if (rolenum != _o_.rolenum) return false;
		if (createtime != _o_.createtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rolenum;
		_h_ += createtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolenum);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		return lb;
	}

	private class Const implements xbean.ServerRoles {
		ServerRoles nThis() {
			return ServerRoles.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ServerRoles copy() {
			return ServerRoles.this.copy();
		}

		@Override
		public xbean.ServerRoles toData() {
			return ServerRoles.this.toData();
		}

		public xbean.ServerRoles toBean() {
			return ServerRoles.this.toBean();
		}

		@Override
		public xbean.ServerRoles toDataIf() {
			return ServerRoles.this.toDataIf();
		}

		public xbean.ServerRoles toBeanIf() {
			return ServerRoles.this.toBeanIf();
		}

		@Override
		public int getRolenum() { // 
			_xdb_verify_unsafe_();
			return rolenum;
		}

		@Override
		public long getCreatetime() { // 
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public void setRolenum(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 
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
			return ServerRoles.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ServerRoles.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ServerRoles.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ServerRoles.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ServerRoles.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ServerRoles.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ServerRoles.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ServerRoles.this.hashCode();
		}

		@Override
		public String toString() {
			return ServerRoles.this.toString();
		}

	}

	public static final class Data implements xbean.ServerRoles {
		private int rolenum; // 
		private long createtime; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ServerRoles _o1_) {
			if (_o1_ instanceof ServerRoles) assign((ServerRoles)_o1_);
			else if (_o1_ instanceof ServerRoles.Data) assign((ServerRoles.Data)_o1_);
			else if (_o1_ instanceof ServerRoles.Const) assign(((ServerRoles.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ServerRoles _o_) {
			rolenum = _o_.rolenum;
			createtime = _o_.createtime;
		}

		private void assign(ServerRoles.Data _o_) {
			rolenum = _o_.rolenum;
			createtime = _o_.createtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(rolenum);
			_os_.marshal(createtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			rolenum = _os_.unmarshal_int();
			createtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ServerRoles copy() {
			return new Data(this);
		}

		@Override
		public xbean.ServerRoles toData() {
			return new Data(this);
		}

		public xbean.ServerRoles toBean() {
			return new ServerRoles(this, null, null);
		}

		@Override
		public xbean.ServerRoles toDataIf() {
			return this;
		}

		public xbean.ServerRoles toBeanIf() {
			return new ServerRoles(this, null, null);
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
		public int getRolenum() { // 
			return rolenum;
		}

		@Override
		public long getCreatetime() { // 
			return createtime;
		}

		@Override
		public void setRolenum(int _v_) { // 
			rolenum = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 
			createtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ServerRoles.Data)) return false;
			ServerRoles.Data _o_ = (ServerRoles.Data) _o1_;
			if (rolenum != _o_.rolenum) return false;
			if (createtime != _o_.createtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rolenum;
			_h_ += createtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rolenum);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

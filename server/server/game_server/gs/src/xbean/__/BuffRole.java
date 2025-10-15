
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BuffRole extends mkdb.XBean implements xbean.BuffRole {
	private long roleid; // 
	private xbean.BuffAgent buffagent; // 

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		buffagent._reset_unsafe_();
	}

	BuffRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		buffagent = new BuffAgent(0, this, "buffagent");
	}

	public BuffRole() {
		this(0, null, null);
	}

	public BuffRole(BuffRole _o_) {
		this(_o_, null, null);
	}

	BuffRole(xbean.BuffRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BuffRole) assign((BuffRole)_o1_);
		else if (_o1_ instanceof BuffRole.Data) assign((BuffRole.Data)_o1_);
		else if (_o1_ instanceof BuffRole.Const) assign(((BuffRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BuffRole _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		buffagent = new BuffAgent(_o_.buffagent, this, "buffagent");
	}

	private void assign(BuffRole.Data _o_) {
		roleid = _o_.roleid;
		buffagent = new BuffAgent(_o_.buffagent, this, "buffagent");
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		buffagent.marshal(_os_);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		buffagent.unmarshal(_os_);
		return _os_;
	}

	@Override
	public xbean.BuffRole copy() {
		_xdb_verify_unsafe_();
		return new BuffRole(this);
	}

	@Override
	public xbean.BuffRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BuffRole toBean() {
		_xdb_verify_unsafe_();
		return new BuffRole(this); // same as copy()
	}

	@Override
	public xbean.BuffRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BuffRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public xbean.BuffAgent getBuffagent() { // 
		_xdb_verify_unsafe_();
		return buffagent;
	}

	@Override
	public void setRoleid(long _v_) { // 
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
		BuffRole _o_ = null;
		if ( _o1_ instanceof BuffRole ) _o_ = (BuffRole)_o1_;
		else if ( _o1_ instanceof BuffRole.Const ) _o_ = ((BuffRole.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!buffagent.equals(_o_.buffagent)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += buffagent.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(buffagent);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buffagent"));
		return lb;
	}

	private class Const implements xbean.BuffRole {
		BuffRole nThis() {
			return BuffRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BuffRole copy() {
			return BuffRole.this.copy();
		}

		@Override
		public xbean.BuffRole toData() {
			return BuffRole.this.toData();
		}

		public xbean.BuffRole toBean() {
			return BuffRole.this.toBean();
		}

		@Override
		public xbean.BuffRole toDataIf() {
			return BuffRole.this.toDataIf();
		}

		public xbean.BuffRole toBeanIf() {
			return BuffRole.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public xbean.BuffAgent getBuffagent() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(buffagent);
		}

		@Override
		public void setRoleid(long _v_) { // 
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
			return BuffRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BuffRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BuffRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BuffRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BuffRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BuffRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BuffRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BuffRole.this.hashCode();
		}

		@Override
		public String toString() {
			return BuffRole.this.toString();
		}

	}

	public static final class Data implements xbean.BuffRole {
		private long roleid; // 
		private xbean.BuffAgent buffagent; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			buffagent = new BuffAgent.Data();
		}

		Data(xbean.BuffRole _o1_) {
			if (_o1_ instanceof BuffRole) assign((BuffRole)_o1_);
			else if (_o1_ instanceof BuffRole.Data) assign((BuffRole.Data)_o1_);
			else if (_o1_ instanceof BuffRole.Const) assign(((BuffRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BuffRole _o_) {
			roleid = _o_.roleid;
			buffagent = new BuffAgent.Data(_o_.buffagent);
		}

		private void assign(BuffRole.Data _o_) {
			roleid = _o_.roleid;
			buffagent = new BuffAgent.Data(_o_.buffagent);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			buffagent.marshal(_os_);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			buffagent.unmarshal(_os_);
			return _os_;
		}

		@Override
		public xbean.BuffRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.BuffRole toData() {
			return new Data(this);
		}

		public xbean.BuffRole toBean() {
			return new BuffRole(this, null, null);
		}

		@Override
		public xbean.BuffRole toDataIf() {
			return this;
		}

		public xbean.BuffRole toBeanIf() {
			return new BuffRole(this, null, null);
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
		public long getRoleid() { // 
			return roleid;
		}

		@Override
		public xbean.BuffAgent getBuffagent() { // 
			return buffagent;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BuffRole.Data)) return false;
			BuffRole.Data _o_ = (BuffRole.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!buffagent.equals(_o_.buffagent)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += buffagent.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(buffagent);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

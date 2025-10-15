
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TeamMember extends mkdb.XBean implements xbean.TeamMember {
	private long roleid; // 
	private int state; // 

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		state = 0;
	}

	TeamMember(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public TeamMember() {
		this(0, null, null);
	}

	public TeamMember(TeamMember _o_) {
		this(_o_, null, null);
	}

	TeamMember(xbean.TeamMember _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TeamMember) assign((TeamMember)_o1_);
		else if (_o1_ instanceof TeamMember.Data) assign((TeamMember.Data)_o1_);
		else if (_o1_ instanceof TeamMember.Const) assign(((TeamMember.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TeamMember _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		state = _o_.state;
	}

	private void assign(TeamMember.Data _o_) {
		roleid = _o_.roleid;
		state = _o_.state;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(state);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		state = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.TeamMember copy() {
		_xdb_verify_unsafe_();
		return new TeamMember(this);
	}

	@Override
	public xbean.TeamMember toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamMember toBean() {
		_xdb_verify_unsafe_();
		return new TeamMember(this); // same as copy()
	}

	@Override
	public xbean.TeamMember toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamMember toBeanIf() {
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
	public int getState() { // 
		_xdb_verify_unsafe_();
		return state;
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
	public void setState(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TeamMember _o_ = null;
		if ( _o1_ instanceof TeamMember ) _o_ = (TeamMember)_o1_;
		else if ( _o1_ instanceof TeamMember.Const ) _o_ = ((TeamMember.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (state != _o_.state) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += state;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		return lb;
	}

	private class Const implements xbean.TeamMember {
		TeamMember nThis() {
			return TeamMember.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TeamMember copy() {
			return TeamMember.this.copy();
		}

		@Override
		public xbean.TeamMember toData() {
			return TeamMember.this.toData();
		}

		public xbean.TeamMember toBean() {
			return TeamMember.this.toBean();
		}

		@Override
		public xbean.TeamMember toDataIf() {
			return TeamMember.this.toDataIf();
		}

		public xbean.TeamMember toBeanIf() {
			return TeamMember.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getState() { // 
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 
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
			return TeamMember.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TeamMember.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TeamMember.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TeamMember.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TeamMember.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TeamMember.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TeamMember.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TeamMember.this.hashCode();
		}

		@Override
		public String toString() {
			return TeamMember.this.toString();
		}

	}

	public static final class Data implements xbean.TeamMember {
		private long roleid; // 
		private int state; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.TeamMember _o1_) {
			if (_o1_ instanceof TeamMember) assign((TeamMember)_o1_);
			else if (_o1_ instanceof TeamMember.Data) assign((TeamMember.Data)_o1_);
			else if (_o1_ instanceof TeamMember.Const) assign(((TeamMember.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TeamMember _o_) {
			roleid = _o_.roleid;
			state = _o_.state;
		}

		private void assign(TeamMember.Data _o_) {
			roleid = _o_.roleid;
			state = _o_.state;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(state);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			state = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.TeamMember copy() {
			return new Data(this);
		}

		@Override
		public xbean.TeamMember toData() {
			return new Data(this);
		}

		public xbean.TeamMember toBean() {
			return new TeamMember(this, null, null);
		}

		@Override
		public xbean.TeamMember toDataIf() {
			return this;
		}

		public xbean.TeamMember toBeanIf() {
			return new TeamMember(this, null, null);
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
		public int getState() { // 
			return state;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setState(int _v_) { // 
			state = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TeamMember.Data)) return false;
			TeamMember.Data _o_ = (TeamMember.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (state != _o_.state) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += state;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

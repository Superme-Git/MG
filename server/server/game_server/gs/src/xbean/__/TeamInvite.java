
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TeamInvite extends mkdb.XBean implements xbean.TeamInvite {
	private long teamid; // 
	private long roleid; // 
	private long invitetime; // 

	@Override
	public void _reset_unsafe_() {
		teamid = 0L;
		roleid = 0L;
		invitetime = 0L;
	}

	TeamInvite(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public TeamInvite() {
		this(0, null, null);
	}

	public TeamInvite(TeamInvite _o_) {
		this(_o_, null, null);
	}

	TeamInvite(xbean.TeamInvite _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TeamInvite) assign((TeamInvite)_o1_);
		else if (_o1_ instanceof TeamInvite.Data) assign((TeamInvite.Data)_o1_);
		else if (_o1_ instanceof TeamInvite.Const) assign(((TeamInvite.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TeamInvite _o_) {
		_o_._xdb_verify_unsafe_();
		teamid = _o_.teamid;
		roleid = _o_.roleid;
		invitetime = _o_.invitetime;
	}

	private void assign(TeamInvite.Data _o_) {
		teamid = _o_.teamid;
		roleid = _o_.roleid;
		invitetime = _o_.invitetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(teamid);
		_os_.marshal(roleid);
		_os_.marshal(invitetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		teamid = _os_.unmarshal_long();
		roleid = _os_.unmarshal_long();
		invitetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.TeamInvite copy() {
		_xdb_verify_unsafe_();
		return new TeamInvite(this);
	}

	@Override
	public xbean.TeamInvite toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamInvite toBean() {
		_xdb_verify_unsafe_();
		return new TeamInvite(this); // same as copy()
	}

	@Override
	public xbean.TeamInvite toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamInvite toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTeamid() { // 
		_xdb_verify_unsafe_();
		return teamid;
	}

	@Override
	public long getRoleid() { // 
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public long getInvitetime() { // 
		_xdb_verify_unsafe_();
		return invitetime;
	}

	@Override
	public void setTeamid(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "teamid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, teamid) {
					public void rollback() { teamid = _xdb_saved; }
				};}});
		teamid = _v_;
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
	public void setInvitetime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "invitetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, invitetime) {
					public void rollback() { invitetime = _xdb_saved; }
				};}});
		invitetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TeamInvite _o_ = null;
		if ( _o1_ instanceof TeamInvite ) _o_ = (TeamInvite)_o1_;
		else if ( _o1_ instanceof TeamInvite.Const ) _o_ = ((TeamInvite.Const)_o1_).nThis();
		else return false;
		if (teamid != _o_.teamid) return false;
		if (roleid != _o_.roleid) return false;
		if (invitetime != _o_.invitetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += teamid;
		_h_ += roleid;
		_h_ += invitetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamid);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(invitetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("teamid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("invitetime"));
		return lb;
	}

	private class Const implements xbean.TeamInvite {
		TeamInvite nThis() {
			return TeamInvite.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TeamInvite copy() {
			return TeamInvite.this.copy();
		}

		@Override
		public xbean.TeamInvite toData() {
			return TeamInvite.this.toData();
		}

		public xbean.TeamInvite toBean() {
			return TeamInvite.this.toBean();
		}

		@Override
		public xbean.TeamInvite toDataIf() {
			return TeamInvite.this.toDataIf();
		}

		public xbean.TeamInvite toBeanIf() {
			return TeamInvite.this.toBeanIf();
		}

		@Override
		public long getTeamid() { // 
			_xdb_verify_unsafe_();
			return teamid;
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getInvitetime() { // 
			_xdb_verify_unsafe_();
			return invitetime;
		}

		@Override
		public void setTeamid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInvitetime(long _v_) { // 
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
			return TeamInvite.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TeamInvite.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TeamInvite.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TeamInvite.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TeamInvite.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TeamInvite.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TeamInvite.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TeamInvite.this.hashCode();
		}

		@Override
		public String toString() {
			return TeamInvite.this.toString();
		}

	}

	public static final class Data implements xbean.TeamInvite {
		private long teamid; // 
		private long roleid; // 
		private long invitetime; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.TeamInvite _o1_) {
			if (_o1_ instanceof TeamInvite) assign((TeamInvite)_o1_);
			else if (_o1_ instanceof TeamInvite.Data) assign((TeamInvite.Data)_o1_);
			else if (_o1_ instanceof TeamInvite.Const) assign(((TeamInvite.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TeamInvite _o_) {
			teamid = _o_.teamid;
			roleid = _o_.roleid;
			invitetime = _o_.invitetime;
		}

		private void assign(TeamInvite.Data _o_) {
			teamid = _o_.teamid;
			roleid = _o_.roleid;
			invitetime = _o_.invitetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(teamid);
			_os_.marshal(roleid);
			_os_.marshal(invitetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			teamid = _os_.unmarshal_long();
			roleid = _os_.unmarshal_long();
			invitetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.TeamInvite copy() {
			return new Data(this);
		}

		@Override
		public xbean.TeamInvite toData() {
			return new Data(this);
		}

		public xbean.TeamInvite toBean() {
			return new TeamInvite(this, null, null);
		}

		@Override
		public xbean.TeamInvite toDataIf() {
			return this;
		}

		public xbean.TeamInvite toBeanIf() {
			return new TeamInvite(this, null, null);
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
		public long getTeamid() { // 
			return teamid;
		}

		@Override
		public long getRoleid() { // 
			return roleid;
		}

		@Override
		public long getInvitetime() { // 
			return invitetime;
		}

		@Override
		public void setTeamid(long _v_) { // 
			teamid = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setInvitetime(long _v_) { // 
			invitetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TeamInvite.Data)) return false;
			TeamInvite.Data _o_ = (TeamInvite.Data) _o1_;
			if (teamid != _o_.teamid) return false;
			if (roleid != _o_.roleid) return false;
			if (invitetime != _o_.invitetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += teamid;
			_h_ += roleid;
			_h_ += invitetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(teamid);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(invitetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

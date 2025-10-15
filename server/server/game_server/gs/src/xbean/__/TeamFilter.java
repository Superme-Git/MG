
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TeamFilter extends mkdb.XBean implements xbean.TeamFilter {
	private fire.pb.team.TeamFilter filter; // 

	@Override
	public void _reset_unsafe_() {
		filter = null;
	}

	TeamFilter(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		filter = null;
	}

	public TeamFilter() {
		this(0, null, null);
	}

	public TeamFilter(TeamFilter _o_) {
		this(_o_, null, null);
	}

	TeamFilter(xbean.TeamFilter _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public xbean.TeamFilter copy() {
		_xdb_verify_unsafe_();
		return new TeamFilter(this);
	}

	@Override
	public xbean.TeamFilter toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamFilter toBean() {
		_xdb_verify_unsafe_();
		return new TeamFilter(this); // same as copy()
	}

	@Override
	public xbean.TeamFilter toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamFilter toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public fire.pb.team.TeamFilter getFilter() { // 
		_xdb_verify_unsafe_();
		return filter;
	}

	@Override
	public void setFilter(fire.pb.team.TeamFilter _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "filter") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<fire.pb.team.TeamFilter>(this, filter) {
					public void rollback() { filter = _xdb_saved; }
			}; }});
		filter = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TeamFilter _o_ = null;
		if ( _o1_ instanceof TeamFilter ) _o_ = (TeamFilter)_o1_;
		else if ( _o1_ instanceof TeamFilter.Const ) _o_ = ((TeamFilter.Const)_o1_).nThis();
		else return false;
		if ((null == filter && null != _o_.filter) || (null != filter && null == _o_.filter) || (null != filter && null != _o_.filter && !filter.equals(_o_.filter))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (filter == null ? 0 : filter.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(filter);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("filter"));
		return lb;
	}

	private class Const implements xbean.TeamFilter {
		TeamFilter nThis() {
			return TeamFilter.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TeamFilter copy() {
			return TeamFilter.this.copy();
		}

		@Override
		public xbean.TeamFilter toData() {
			return TeamFilter.this.toData();
		}

		public xbean.TeamFilter toBean() {
			return TeamFilter.this.toBean();
		}

		@Override
		public xbean.TeamFilter toDataIf() {
			return TeamFilter.this.toDataIf();
		}

		public xbean.TeamFilter toBeanIf() {
			return TeamFilter.this.toBeanIf();
		}

		@Override
		public fire.pb.team.TeamFilter getFilter() { // 
			_xdb_verify_unsafe_();
			return filter;
		}

		@Override
		public void setFilter(fire.pb.team.TeamFilter _v_) { // 
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
			return TeamFilter.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TeamFilter.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TeamFilter.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TeamFilter.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TeamFilter.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TeamFilter.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TeamFilter.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TeamFilter.this.hashCode();
		}

		@Override
		public String toString() {
			return TeamFilter.this.toString();
		}

	}

	public static final class Data implements xbean.TeamFilter {
		private fire.pb.team.TeamFilter filter; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			filter = null;
		}

		Data(xbean.TeamFilter _o1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TeamFilter copy() {
			return new Data(this);
		}

		@Override
		public xbean.TeamFilter toData() {
			return new Data(this);
		}

		public xbean.TeamFilter toBean() {
			return new TeamFilter(this, null, null);
		}

		@Override
		public xbean.TeamFilter toDataIf() {
			return this;
		}

		public xbean.TeamFilter toBeanIf() {
			return new TeamFilter(this, null, null);
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
		public fire.pb.team.TeamFilter getFilter() { // 
			return filter;
		}

		@Override
		public void setFilter(fire.pb.team.TeamFilter _v_) { // 
			filter = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TeamFilter.Data)) return false;
			TeamFilter.Data _o_ = (TeamFilter.Data) _o1_;
			if ((null == filter && null != _o_.filter) || (null != filter && null == _o_.filter) || (null != filter && null != _o_.filter && !filter.equals(_o_.filter))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (filter == null ? 0 : filter.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(filter);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

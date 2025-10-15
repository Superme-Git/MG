
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CommitedScenarioMission extends mkdb.XBean implements xbean.CommitedScenarioMission {
	private java.util.LinkedList<Integer> committed; // 

	@Override
	public void _reset_unsafe_() {
		committed.clear();
	}

	CommitedScenarioMission(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		committed = new java.util.LinkedList<Integer>();
	}

	public CommitedScenarioMission() {
		this(0, null, null);
	}

	public CommitedScenarioMission(CommitedScenarioMission _o_) {
		this(_o_, null, null);
	}

	CommitedScenarioMission(xbean.CommitedScenarioMission _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CommitedScenarioMission) assign((CommitedScenarioMission)_o1_);
		else if (_o1_ instanceof CommitedScenarioMission.Data) assign((CommitedScenarioMission.Data)_o1_);
		else if (_o1_ instanceof CommitedScenarioMission.Const) assign(((CommitedScenarioMission.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CommitedScenarioMission _o_) {
		_o_._xdb_verify_unsafe_();
		committed = new java.util.LinkedList<Integer>();
		committed.addAll(_o_.committed);
	}

	private void assign(CommitedScenarioMission.Data _o_) {
		committed = new java.util.LinkedList<Integer>();
		committed.addAll(_o_.committed);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(committed.size());
		for (Integer _v_ : committed) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			committed.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.CommitedScenarioMission copy() {
		_xdb_verify_unsafe_();
		return new CommitedScenarioMission(this);
	}

	@Override
	public xbean.CommitedScenarioMission toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CommitedScenarioMission toBean() {
		_xdb_verify_unsafe_();
		return new CommitedScenarioMission(this); // same as copy()
	}

	@Override
	public xbean.CommitedScenarioMission toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CommitedScenarioMission toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Integer> getCommitted() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "committed"), committed);
	}

	public java.util.List<Integer> getCommittedAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Integer> committed;
		CommitedScenarioMission _o_ = this;
		committed = new java.util.LinkedList<Integer>();
		committed.addAll(_o_.committed);
		return committed;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CommitedScenarioMission _o_ = null;
		if ( _o1_ instanceof CommitedScenarioMission ) _o_ = (CommitedScenarioMission)_o1_;
		else if ( _o1_ instanceof CommitedScenarioMission.Const ) _o_ = ((CommitedScenarioMission.Const)_o1_).nThis();
		else return false;
		if (!committed.equals(_o_.committed)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += committed.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(committed);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("committed"));
		return lb;
	}

	private class Const implements xbean.CommitedScenarioMission {
		CommitedScenarioMission nThis() {
			return CommitedScenarioMission.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CommitedScenarioMission copy() {
			return CommitedScenarioMission.this.copy();
		}

		@Override
		public xbean.CommitedScenarioMission toData() {
			return CommitedScenarioMission.this.toData();
		}

		public xbean.CommitedScenarioMission toBean() {
			return CommitedScenarioMission.this.toBean();
		}

		@Override
		public xbean.CommitedScenarioMission toDataIf() {
			return CommitedScenarioMission.this.toDataIf();
		}

		public xbean.CommitedScenarioMission toBeanIf() {
			return CommitedScenarioMission.this.toBeanIf();
		}

		@Override
		public java.util.List<Integer> getCommitted() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(committed);
		}

		public java.util.List<Integer> getCommittedAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Integer> committed;
			CommitedScenarioMission _o_ = CommitedScenarioMission.this;
		committed = new java.util.LinkedList<Integer>();
		committed.addAll(_o_.committed);
			return committed;
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
			return CommitedScenarioMission.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CommitedScenarioMission.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CommitedScenarioMission.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CommitedScenarioMission.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CommitedScenarioMission.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CommitedScenarioMission.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CommitedScenarioMission.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CommitedScenarioMission.this.hashCode();
		}

		@Override
		public String toString() {
			return CommitedScenarioMission.this.toString();
		}

	}

	public static final class Data implements xbean.CommitedScenarioMission {
		private java.util.LinkedList<Integer> committed; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			committed = new java.util.LinkedList<Integer>();
		}

		Data(xbean.CommitedScenarioMission _o1_) {
			if (_o1_ instanceof CommitedScenarioMission) assign((CommitedScenarioMission)_o1_);
			else if (_o1_ instanceof CommitedScenarioMission.Data) assign((CommitedScenarioMission.Data)_o1_);
			else if (_o1_ instanceof CommitedScenarioMission.Const) assign(((CommitedScenarioMission.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CommitedScenarioMission _o_) {
			committed = new java.util.LinkedList<Integer>();
			committed.addAll(_o_.committed);
		}

		private void assign(CommitedScenarioMission.Data _o_) {
			committed = new java.util.LinkedList<Integer>();
			committed.addAll(_o_.committed);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(committed.size());
			for (Integer _v_ : committed) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				committed.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.CommitedScenarioMission copy() {
			return new Data(this);
		}

		@Override
		public xbean.CommitedScenarioMission toData() {
			return new Data(this);
		}

		public xbean.CommitedScenarioMission toBean() {
			return new CommitedScenarioMission(this, null, null);
		}

		@Override
		public xbean.CommitedScenarioMission toDataIf() {
			return this;
		}

		public xbean.CommitedScenarioMission toBeanIf() {
			return new CommitedScenarioMission(this, null, null);
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
		public java.util.List<Integer> getCommitted() { // 
			return committed;
		}

		@Override
		public java.util.List<Integer> getCommittedAsData() { // 
			return committed;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CommitedScenarioMission.Data)) return false;
			CommitedScenarioMission.Data _o_ = (CommitedScenarioMission.Data) _o1_;
			if (!committed.equals(_o_.committed)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += committed.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(committed);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ProfessionLeaderCand extends mkdb.XBean implements xbean.ProfessionLeaderCand {
	private java.util.ArrayList<Long> candidatelist; // 职业领袖候选人list,该表每周清除一次

	@Override
	public void _reset_unsafe_() {
		candidatelist.clear();
	}

	ProfessionLeaderCand(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		candidatelist = new java.util.ArrayList<Long>();
	}

	public ProfessionLeaderCand() {
		this(0, null, null);
	}

	public ProfessionLeaderCand(ProfessionLeaderCand _o_) {
		this(_o_, null, null);
	}

	ProfessionLeaderCand(xbean.ProfessionLeaderCand _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ProfessionLeaderCand) assign((ProfessionLeaderCand)_o1_);
		else if (_o1_ instanceof ProfessionLeaderCand.Data) assign((ProfessionLeaderCand.Data)_o1_);
		else if (_o1_ instanceof ProfessionLeaderCand.Const) assign(((ProfessionLeaderCand.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ProfessionLeaderCand _o_) {
		_o_._xdb_verify_unsafe_();
		candidatelist = new java.util.ArrayList<Long>();
		candidatelist.addAll(_o_.candidatelist);
	}

	private void assign(ProfessionLeaderCand.Data _o_) {
		candidatelist = new java.util.ArrayList<Long>();
		candidatelist.addAll(_o_.candidatelist);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(candidatelist.size());
		for (Long _v_ : candidatelist) {
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
			candidatelist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ProfessionLeaderCand copy() {
		_xdb_verify_unsafe_();
		return new ProfessionLeaderCand(this);
	}

	@Override
	public xbean.ProfessionLeaderCand toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProfessionLeaderCand toBean() {
		_xdb_verify_unsafe_();
		return new ProfessionLeaderCand(this); // same as copy()
	}

	@Override
	public xbean.ProfessionLeaderCand toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProfessionLeaderCand toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Long> getCandidatelist() { // 职业领袖候选人list,该表每周清除一次
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "candidatelist"), candidatelist);
	}

	public java.util.List<Long> getCandidatelistAsData() { // 职业领袖候选人list,该表每周清除一次
		_xdb_verify_unsafe_();
		java.util.List<Long> candidatelist;
		ProfessionLeaderCand _o_ = this;
		candidatelist = new java.util.ArrayList<Long>();
		candidatelist.addAll(_o_.candidatelist);
		return candidatelist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ProfessionLeaderCand _o_ = null;
		if ( _o1_ instanceof ProfessionLeaderCand ) _o_ = (ProfessionLeaderCand)_o1_;
		else if ( _o1_ instanceof ProfessionLeaderCand.Const ) _o_ = ((ProfessionLeaderCand.Const)_o1_).nThis();
		else return false;
		if (!candidatelist.equals(_o_.candidatelist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += candidatelist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(candidatelist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("candidatelist"));
		return lb;
	}

	private class Const implements xbean.ProfessionLeaderCand {
		ProfessionLeaderCand nThis() {
			return ProfessionLeaderCand.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ProfessionLeaderCand copy() {
			return ProfessionLeaderCand.this.copy();
		}

		@Override
		public xbean.ProfessionLeaderCand toData() {
			return ProfessionLeaderCand.this.toData();
		}

		public xbean.ProfessionLeaderCand toBean() {
			return ProfessionLeaderCand.this.toBean();
		}

		@Override
		public xbean.ProfessionLeaderCand toDataIf() {
			return ProfessionLeaderCand.this.toDataIf();
		}

		public xbean.ProfessionLeaderCand toBeanIf() {
			return ProfessionLeaderCand.this.toBeanIf();
		}

		@Override
		public java.util.List<Long> getCandidatelist() { // 职业领袖候选人list,该表每周清除一次
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(candidatelist);
		}

		public java.util.List<Long> getCandidatelistAsData() { // 职业领袖候选人list,该表每周清除一次
			_xdb_verify_unsafe_();
			java.util.List<Long> candidatelist;
			ProfessionLeaderCand _o_ = ProfessionLeaderCand.this;
		candidatelist = new java.util.ArrayList<Long>();
		candidatelist.addAll(_o_.candidatelist);
			return candidatelist;
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
			return ProfessionLeaderCand.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ProfessionLeaderCand.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ProfessionLeaderCand.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ProfessionLeaderCand.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ProfessionLeaderCand.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ProfessionLeaderCand.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ProfessionLeaderCand.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ProfessionLeaderCand.this.hashCode();
		}

		@Override
		public String toString() {
			return ProfessionLeaderCand.this.toString();
		}

	}

	public static final class Data implements xbean.ProfessionLeaderCand {
		private java.util.ArrayList<Long> candidatelist; // 职业领袖候选人list,该表每周清除一次

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			candidatelist = new java.util.ArrayList<Long>();
		}

		Data(xbean.ProfessionLeaderCand _o1_) {
			if (_o1_ instanceof ProfessionLeaderCand) assign((ProfessionLeaderCand)_o1_);
			else if (_o1_ instanceof ProfessionLeaderCand.Data) assign((ProfessionLeaderCand.Data)_o1_);
			else if (_o1_ instanceof ProfessionLeaderCand.Const) assign(((ProfessionLeaderCand.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ProfessionLeaderCand _o_) {
			candidatelist = new java.util.ArrayList<Long>();
			candidatelist.addAll(_o_.candidatelist);
		}

		private void assign(ProfessionLeaderCand.Data _o_) {
			candidatelist = new java.util.ArrayList<Long>();
			candidatelist.addAll(_o_.candidatelist);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(candidatelist.size());
			for (Long _v_ : candidatelist) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				candidatelist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ProfessionLeaderCand copy() {
			return new Data(this);
		}

		@Override
		public xbean.ProfessionLeaderCand toData() {
			return new Data(this);
		}

		public xbean.ProfessionLeaderCand toBean() {
			return new ProfessionLeaderCand(this, null, null);
		}

		@Override
		public xbean.ProfessionLeaderCand toDataIf() {
			return this;
		}

		public xbean.ProfessionLeaderCand toBeanIf() {
			return new ProfessionLeaderCand(this, null, null);
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
		public java.util.List<Long> getCandidatelist() { // 职业领袖候选人list,该表每周清除一次
			return candidatelist;
		}

		@Override
		public java.util.List<Long> getCandidatelistAsData() { // 职业领袖候选人list,该表每周清除一次
			return candidatelist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ProfessionLeaderCand.Data)) return false;
			ProfessionLeaderCand.Data _o_ = (ProfessionLeaderCand.Data) _o1_;
			if (!candidatelist.equals(_o_.candidatelist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += candidatelist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(candidatelist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

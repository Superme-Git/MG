
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ProfessionLeaderVoteInfo extends mkdb.XBean implements xbean.ProfessionLeaderVoteInfo {
	private long votetime; // role最近一次投票的时间
	private long challengetime; // role最近一次挑战成功的时间

	@Override
	public void _reset_unsafe_() {
		votetime = 0L;
		challengetime = 0L;
	}

	ProfessionLeaderVoteInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ProfessionLeaderVoteInfo() {
		this(0, null, null);
	}

	public ProfessionLeaderVoteInfo(ProfessionLeaderVoteInfo _o_) {
		this(_o_, null, null);
	}

	ProfessionLeaderVoteInfo(xbean.ProfessionLeaderVoteInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ProfessionLeaderVoteInfo) assign((ProfessionLeaderVoteInfo)_o1_);
		else if (_o1_ instanceof ProfessionLeaderVoteInfo.Data) assign((ProfessionLeaderVoteInfo.Data)_o1_);
		else if (_o1_ instanceof ProfessionLeaderVoteInfo.Const) assign(((ProfessionLeaderVoteInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ProfessionLeaderVoteInfo _o_) {
		_o_._xdb_verify_unsafe_();
		votetime = _o_.votetime;
		challengetime = _o_.challengetime;
	}

	private void assign(ProfessionLeaderVoteInfo.Data _o_) {
		votetime = _o_.votetime;
		challengetime = _o_.challengetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(votetime);
		_os_.marshal(challengetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		votetime = _os_.unmarshal_long();
		challengetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ProfessionLeaderVoteInfo copy() {
		_xdb_verify_unsafe_();
		return new ProfessionLeaderVoteInfo(this);
	}

	@Override
	public xbean.ProfessionLeaderVoteInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProfessionLeaderVoteInfo toBean() {
		_xdb_verify_unsafe_();
		return new ProfessionLeaderVoteInfo(this); // same as copy()
	}

	@Override
	public xbean.ProfessionLeaderVoteInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProfessionLeaderVoteInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getVotetime() { // role最近一次投票的时间
		_xdb_verify_unsafe_();
		return votetime;
	}

	@Override
	public long getChallengetime() { // role最近一次挑战成功的时间
		_xdb_verify_unsafe_();
		return challengetime;
	}

	@Override
	public void setVotetime(long _v_) { // role最近一次投票的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "votetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, votetime) {
					public void rollback() { votetime = _xdb_saved; }
				};}});
		votetime = _v_;
	}

	@Override
	public void setChallengetime(long _v_) { // role最近一次挑战成功的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "challengetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, challengetime) {
					public void rollback() { challengetime = _xdb_saved; }
				};}});
		challengetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ProfessionLeaderVoteInfo _o_ = null;
		if ( _o1_ instanceof ProfessionLeaderVoteInfo ) _o_ = (ProfessionLeaderVoteInfo)_o1_;
		else if ( _o1_ instanceof ProfessionLeaderVoteInfo.Const ) _o_ = ((ProfessionLeaderVoteInfo.Const)_o1_).nThis();
		else return false;
		if (votetime != _o_.votetime) return false;
		if (challengetime != _o_.challengetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += votetime;
		_h_ += challengetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(votetime);
		_sb_.append(",");
		_sb_.append(challengetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("votetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("challengetime"));
		return lb;
	}

	private class Const implements xbean.ProfessionLeaderVoteInfo {
		ProfessionLeaderVoteInfo nThis() {
			return ProfessionLeaderVoteInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ProfessionLeaderVoteInfo copy() {
			return ProfessionLeaderVoteInfo.this.copy();
		}

		@Override
		public xbean.ProfessionLeaderVoteInfo toData() {
			return ProfessionLeaderVoteInfo.this.toData();
		}

		public xbean.ProfessionLeaderVoteInfo toBean() {
			return ProfessionLeaderVoteInfo.this.toBean();
		}

		@Override
		public xbean.ProfessionLeaderVoteInfo toDataIf() {
			return ProfessionLeaderVoteInfo.this.toDataIf();
		}

		public xbean.ProfessionLeaderVoteInfo toBeanIf() {
			return ProfessionLeaderVoteInfo.this.toBeanIf();
		}

		@Override
		public long getVotetime() { // role最近一次投票的时间
			_xdb_verify_unsafe_();
			return votetime;
		}

		@Override
		public long getChallengetime() { // role最近一次挑战成功的时间
			_xdb_verify_unsafe_();
			return challengetime;
		}

		@Override
		public void setVotetime(long _v_) { // role最近一次投票的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChallengetime(long _v_) { // role最近一次挑战成功的时间
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
			return ProfessionLeaderVoteInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ProfessionLeaderVoteInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ProfessionLeaderVoteInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ProfessionLeaderVoteInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ProfessionLeaderVoteInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ProfessionLeaderVoteInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ProfessionLeaderVoteInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ProfessionLeaderVoteInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ProfessionLeaderVoteInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ProfessionLeaderVoteInfo {
		private long votetime; // role最近一次投票的时间
		private long challengetime; // role最近一次挑战成功的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ProfessionLeaderVoteInfo _o1_) {
			if (_o1_ instanceof ProfessionLeaderVoteInfo) assign((ProfessionLeaderVoteInfo)_o1_);
			else if (_o1_ instanceof ProfessionLeaderVoteInfo.Data) assign((ProfessionLeaderVoteInfo.Data)_o1_);
			else if (_o1_ instanceof ProfessionLeaderVoteInfo.Const) assign(((ProfessionLeaderVoteInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ProfessionLeaderVoteInfo _o_) {
			votetime = _o_.votetime;
			challengetime = _o_.challengetime;
		}

		private void assign(ProfessionLeaderVoteInfo.Data _o_) {
			votetime = _o_.votetime;
			challengetime = _o_.challengetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(votetime);
			_os_.marshal(challengetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			votetime = _os_.unmarshal_long();
			challengetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ProfessionLeaderVoteInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ProfessionLeaderVoteInfo toData() {
			return new Data(this);
		}

		public xbean.ProfessionLeaderVoteInfo toBean() {
			return new ProfessionLeaderVoteInfo(this, null, null);
		}

		@Override
		public xbean.ProfessionLeaderVoteInfo toDataIf() {
			return this;
		}

		public xbean.ProfessionLeaderVoteInfo toBeanIf() {
			return new ProfessionLeaderVoteInfo(this, null, null);
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
		public long getVotetime() { // role最近一次投票的时间
			return votetime;
		}

		@Override
		public long getChallengetime() { // role最近一次挑战成功的时间
			return challengetime;
		}

		@Override
		public void setVotetime(long _v_) { // role最近一次投票的时间
			votetime = _v_;
		}

		@Override
		public void setChallengetime(long _v_) { // role最近一次挑战成功的时间
			challengetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ProfessionLeaderVoteInfo.Data)) return false;
			ProfessionLeaderVoteInfo.Data _o_ = (ProfessionLeaderVoteInfo.Data) _o1_;
			if (votetime != _o_.votetime) return false;
			if (challengetime != _o_.challengetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += votetime;
			_h_ += challengetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(votetime);
			_sb_.append(",");
			_sb_.append(challengetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

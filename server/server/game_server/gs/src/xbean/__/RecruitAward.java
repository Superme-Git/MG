
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RecruitAward extends mkdb.XBean implements xbean.RecruitAward {
	private int getnum; // 领取次数
	private long gettime; // 上次领取的时间

	@Override
	public void _reset_unsafe_() {
		getnum = 0;
		gettime = 0L;
	}

	RecruitAward(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RecruitAward() {
		this(0, null, null);
	}

	public RecruitAward(RecruitAward _o_) {
		this(_o_, null, null);
	}

	RecruitAward(xbean.RecruitAward _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RecruitAward) assign((RecruitAward)_o1_);
		else if (_o1_ instanceof RecruitAward.Data) assign((RecruitAward.Data)_o1_);
		else if (_o1_ instanceof RecruitAward.Const) assign(((RecruitAward.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RecruitAward _o_) {
		_o_._xdb_verify_unsafe_();
		getnum = _o_.getnum;
		gettime = _o_.gettime;
	}

	private void assign(RecruitAward.Data _o_) {
		getnum = _o_.getnum;
		gettime = _o_.gettime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(getnum);
		_os_.marshal(gettime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		getnum = _os_.unmarshal_int();
		gettime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RecruitAward copy() {
		_xdb_verify_unsafe_();
		return new RecruitAward(this);
	}

	@Override
	public xbean.RecruitAward toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RecruitAward toBean() {
		_xdb_verify_unsafe_();
		return new RecruitAward(this); // same as copy()
	}

	@Override
	public xbean.RecruitAward toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RecruitAward toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getGetnum() { // 领取次数
		_xdb_verify_unsafe_();
		return getnum;
	}

	@Override
	public long getGettime() { // 上次领取的时间
		_xdb_verify_unsafe_();
		return gettime;
	}

	@Override
	public void setGetnum(int _v_) { // 领取次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "getnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, getnum) {
					public void rollback() { getnum = _xdb_saved; }
				};}});
		getnum = _v_;
	}

	@Override
	public void setGettime(long _v_) { // 上次领取的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "gettime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, gettime) {
					public void rollback() { gettime = _xdb_saved; }
				};}});
		gettime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RecruitAward _o_ = null;
		if ( _o1_ instanceof RecruitAward ) _o_ = (RecruitAward)_o1_;
		else if ( _o1_ instanceof RecruitAward.Const ) _o_ = ((RecruitAward.Const)_o1_).nThis();
		else return false;
		if (getnum != _o_.getnum) return false;
		if (gettime != _o_.gettime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += getnum;
		_h_ += gettime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(getnum);
		_sb_.append(",");
		_sb_.append(gettime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("getnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("gettime"));
		return lb;
	}

	private class Const implements xbean.RecruitAward {
		RecruitAward nThis() {
			return RecruitAward.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RecruitAward copy() {
			return RecruitAward.this.copy();
		}

		@Override
		public xbean.RecruitAward toData() {
			return RecruitAward.this.toData();
		}

		public xbean.RecruitAward toBean() {
			return RecruitAward.this.toBean();
		}

		@Override
		public xbean.RecruitAward toDataIf() {
			return RecruitAward.this.toDataIf();
		}

		public xbean.RecruitAward toBeanIf() {
			return RecruitAward.this.toBeanIf();
		}

		@Override
		public int getGetnum() { // 领取次数
			_xdb_verify_unsafe_();
			return getnum;
		}

		@Override
		public long getGettime() { // 上次领取的时间
			_xdb_verify_unsafe_();
			return gettime;
		}

		@Override
		public void setGetnum(int _v_) { // 领取次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGettime(long _v_) { // 上次领取的时间
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
			return RecruitAward.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RecruitAward.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RecruitAward.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RecruitAward.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RecruitAward.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RecruitAward.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RecruitAward.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RecruitAward.this.hashCode();
		}

		@Override
		public String toString() {
			return RecruitAward.this.toString();
		}

	}

	public static final class Data implements xbean.RecruitAward {
		private int getnum; // 领取次数
		private long gettime; // 上次领取的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RecruitAward _o1_) {
			if (_o1_ instanceof RecruitAward) assign((RecruitAward)_o1_);
			else if (_o1_ instanceof RecruitAward.Data) assign((RecruitAward.Data)_o1_);
			else if (_o1_ instanceof RecruitAward.Const) assign(((RecruitAward.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RecruitAward _o_) {
			getnum = _o_.getnum;
			gettime = _o_.gettime;
		}

		private void assign(RecruitAward.Data _o_) {
			getnum = _o_.getnum;
			gettime = _o_.gettime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(getnum);
			_os_.marshal(gettime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			getnum = _os_.unmarshal_int();
			gettime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RecruitAward copy() {
			return new Data(this);
		}

		@Override
		public xbean.RecruitAward toData() {
			return new Data(this);
		}

		public xbean.RecruitAward toBean() {
			return new RecruitAward(this, null, null);
		}

		@Override
		public xbean.RecruitAward toDataIf() {
			return this;
		}

		public xbean.RecruitAward toBeanIf() {
			return new RecruitAward(this, null, null);
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
		public int getGetnum() { // 领取次数
			return getnum;
		}

		@Override
		public long getGettime() { // 上次领取的时间
			return gettime;
		}

		@Override
		public void setGetnum(int _v_) { // 领取次数
			getnum = _v_;
		}

		@Override
		public void setGettime(long _v_) { // 上次领取的时间
			gettime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RecruitAward.Data)) return false;
			RecruitAward.Data _o_ = (RecruitAward.Data) _o1_;
			if (getnum != _o_.getnum) return false;
			if (gettime != _o_.gettime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += getnum;
			_h_ += gettime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(getnum);
			_sb_.append(",");
			_sb_.append(gettime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

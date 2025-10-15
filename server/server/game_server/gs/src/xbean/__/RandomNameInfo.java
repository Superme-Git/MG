
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RandomNameInfo extends mkdb.XBean implements xbean.RandomNameInfo {
	private long randomtime; // 

	@Override
	public void _reset_unsafe_() {
		randomtime = 0L;
	}

	RandomNameInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RandomNameInfo() {
		this(0, null, null);
	}

	public RandomNameInfo(RandomNameInfo _o_) {
		this(_o_, null, null);
	}

	RandomNameInfo(xbean.RandomNameInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RandomNameInfo) assign((RandomNameInfo)_o1_);
		else if (_o1_ instanceof RandomNameInfo.Data) assign((RandomNameInfo.Data)_o1_);
		else if (_o1_ instanceof RandomNameInfo.Const) assign(((RandomNameInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RandomNameInfo _o_) {
		_o_._xdb_verify_unsafe_();
		randomtime = _o_.randomtime;
	}

	private void assign(RandomNameInfo.Data _o_) {
		randomtime = _o_.randomtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(randomtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		randomtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RandomNameInfo copy() {
		_xdb_verify_unsafe_();
		return new RandomNameInfo(this);
	}

	@Override
	public xbean.RandomNameInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RandomNameInfo toBean() {
		_xdb_verify_unsafe_();
		return new RandomNameInfo(this); // same as copy()
	}

	@Override
	public xbean.RandomNameInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RandomNameInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRandomtime() { // 
		_xdb_verify_unsafe_();
		return randomtime;
	}

	@Override
	public void setRandomtime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "randomtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, randomtime) {
					public void rollback() { randomtime = _xdb_saved; }
				};}});
		randomtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RandomNameInfo _o_ = null;
		if ( _o1_ instanceof RandomNameInfo ) _o_ = (RandomNameInfo)_o1_;
		else if ( _o1_ instanceof RandomNameInfo.Const ) _o_ = ((RandomNameInfo.Const)_o1_).nThis();
		else return false;
		if (randomtime != _o_.randomtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += randomtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(randomtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("randomtime"));
		return lb;
	}

	private class Const implements xbean.RandomNameInfo {
		RandomNameInfo nThis() {
			return RandomNameInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RandomNameInfo copy() {
			return RandomNameInfo.this.copy();
		}

		@Override
		public xbean.RandomNameInfo toData() {
			return RandomNameInfo.this.toData();
		}

		public xbean.RandomNameInfo toBean() {
			return RandomNameInfo.this.toBean();
		}

		@Override
		public xbean.RandomNameInfo toDataIf() {
			return RandomNameInfo.this.toDataIf();
		}

		public xbean.RandomNameInfo toBeanIf() {
			return RandomNameInfo.this.toBeanIf();
		}

		@Override
		public long getRandomtime() { // 
			_xdb_verify_unsafe_();
			return randomtime;
		}

		@Override
		public void setRandomtime(long _v_) { // 
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
			return RandomNameInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RandomNameInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RandomNameInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RandomNameInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RandomNameInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RandomNameInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RandomNameInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RandomNameInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RandomNameInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RandomNameInfo {
		private long randomtime; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RandomNameInfo _o1_) {
			if (_o1_ instanceof RandomNameInfo) assign((RandomNameInfo)_o1_);
			else if (_o1_ instanceof RandomNameInfo.Data) assign((RandomNameInfo.Data)_o1_);
			else if (_o1_ instanceof RandomNameInfo.Const) assign(((RandomNameInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RandomNameInfo _o_) {
			randomtime = _o_.randomtime;
		}

		private void assign(RandomNameInfo.Data _o_) {
			randomtime = _o_.randomtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(randomtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			randomtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RandomNameInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RandomNameInfo toData() {
			return new Data(this);
		}

		public xbean.RandomNameInfo toBean() {
			return new RandomNameInfo(this, null, null);
		}

		@Override
		public xbean.RandomNameInfo toDataIf() {
			return this;
		}

		public xbean.RandomNameInfo toBeanIf() {
			return new RandomNameInfo(this, null, null);
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
		public long getRandomtime() { // 
			return randomtime;
		}

		@Override
		public void setRandomtime(long _v_) { // 
			randomtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RandomNameInfo.Data)) return false;
			RandomNameInfo.Data _o_ = (RandomNameInfo.Data) _o1_;
			if (randomtime != _o_.randomtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += randomtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(randomtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

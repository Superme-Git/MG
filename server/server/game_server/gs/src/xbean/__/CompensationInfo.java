
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CompensationInfo extends mkdb.XBean implements xbean.CompensationInfo {
	private long receiveawardtime; // 领取奖励的时间
	private int readflag; // 是否已读 0=未读 1=已读

	@Override
	public void _reset_unsafe_() {
		receiveawardtime = 0L;
		readflag = 0;
	}

	CompensationInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public CompensationInfo() {
		this(0, null, null);
	}

	public CompensationInfo(CompensationInfo _o_) {
		this(_o_, null, null);
	}

	CompensationInfo(xbean.CompensationInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CompensationInfo) assign((CompensationInfo)_o1_);
		else if (_o1_ instanceof CompensationInfo.Data) assign((CompensationInfo.Data)_o1_);
		else if (_o1_ instanceof CompensationInfo.Const) assign(((CompensationInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CompensationInfo _o_) {
		_o_._xdb_verify_unsafe_();
		receiveawardtime = _o_.receiveawardtime;
		readflag = _o_.readflag;
	}

	private void assign(CompensationInfo.Data _o_) {
		receiveawardtime = _o_.receiveawardtime;
		readflag = _o_.readflag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(receiveawardtime);
		_os_.marshal(readflag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		receiveawardtime = _os_.unmarshal_long();
		readflag = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.CompensationInfo copy() {
		_xdb_verify_unsafe_();
		return new CompensationInfo(this);
	}

	@Override
	public xbean.CompensationInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CompensationInfo toBean() {
		_xdb_verify_unsafe_();
		return new CompensationInfo(this); // same as copy()
	}

	@Override
	public xbean.CompensationInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CompensationInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getReceiveawardtime() { // 领取奖励的时间
		_xdb_verify_unsafe_();
		return receiveawardtime;
	}

	@Override
	public int getReadflag() { // 是否已读 0=未读 1=已读
		_xdb_verify_unsafe_();
		return readflag;
	}

	@Override
	public void setReceiveawardtime(long _v_) { // 领取奖励的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receiveawardtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, receiveawardtime) {
					public void rollback() { receiveawardtime = _xdb_saved; }
				};}});
		receiveawardtime = _v_;
	}

	@Override
	public void setReadflag(int _v_) { // 是否已读 0=未读 1=已读
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "readflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, readflag) {
					public void rollback() { readflag = _xdb_saved; }
				};}});
		readflag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CompensationInfo _o_ = null;
		if ( _o1_ instanceof CompensationInfo ) _o_ = (CompensationInfo)_o1_;
		else if ( _o1_ instanceof CompensationInfo.Const ) _o_ = ((CompensationInfo.Const)_o1_).nThis();
		else return false;
		if (receiveawardtime != _o_.receiveawardtime) return false;
		if (readflag != _o_.readflag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += receiveawardtime;
		_h_ += readflag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(receiveawardtime);
		_sb_.append(",");
		_sb_.append(readflag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receiveawardtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("readflag"));
		return lb;
	}

	private class Const implements xbean.CompensationInfo {
		CompensationInfo nThis() {
			return CompensationInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CompensationInfo copy() {
			return CompensationInfo.this.copy();
		}

		@Override
		public xbean.CompensationInfo toData() {
			return CompensationInfo.this.toData();
		}

		public xbean.CompensationInfo toBean() {
			return CompensationInfo.this.toBean();
		}

		@Override
		public xbean.CompensationInfo toDataIf() {
			return CompensationInfo.this.toDataIf();
		}

		public xbean.CompensationInfo toBeanIf() {
			return CompensationInfo.this.toBeanIf();
		}

		@Override
		public long getReceiveawardtime() { // 领取奖励的时间
			_xdb_verify_unsafe_();
			return receiveawardtime;
		}

		@Override
		public int getReadflag() { // 是否已读 0=未读 1=已读
			_xdb_verify_unsafe_();
			return readflag;
		}

		@Override
		public void setReceiveawardtime(long _v_) { // 领取奖励的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReadflag(int _v_) { // 是否已读 0=未读 1=已读
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
			return CompensationInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CompensationInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CompensationInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CompensationInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CompensationInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CompensationInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CompensationInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CompensationInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return CompensationInfo.this.toString();
		}

	}

	public static final class Data implements xbean.CompensationInfo {
		private long receiveawardtime; // 领取奖励的时间
		private int readflag; // 是否已读 0=未读 1=已读

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.CompensationInfo _o1_) {
			if (_o1_ instanceof CompensationInfo) assign((CompensationInfo)_o1_);
			else if (_o1_ instanceof CompensationInfo.Data) assign((CompensationInfo.Data)_o1_);
			else if (_o1_ instanceof CompensationInfo.Const) assign(((CompensationInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CompensationInfo _o_) {
			receiveawardtime = _o_.receiveawardtime;
			readflag = _o_.readflag;
		}

		private void assign(CompensationInfo.Data _o_) {
			receiveawardtime = _o_.receiveawardtime;
			readflag = _o_.readflag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(receiveawardtime);
			_os_.marshal(readflag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			receiveawardtime = _os_.unmarshal_long();
			readflag = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.CompensationInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.CompensationInfo toData() {
			return new Data(this);
		}

		public xbean.CompensationInfo toBean() {
			return new CompensationInfo(this, null, null);
		}

		@Override
		public xbean.CompensationInfo toDataIf() {
			return this;
		}

		public xbean.CompensationInfo toBeanIf() {
			return new CompensationInfo(this, null, null);
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
		public long getReceiveawardtime() { // 领取奖励的时间
			return receiveawardtime;
		}

		@Override
		public int getReadflag() { // 是否已读 0=未读 1=已读
			return readflag;
		}

		@Override
		public void setReceiveawardtime(long _v_) { // 领取奖励的时间
			receiveawardtime = _v_;
		}

		@Override
		public void setReadflag(int _v_) { // 是否已读 0=未读 1=已读
			readflag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CompensationInfo.Data)) return false;
			CompensationInfo.Data _o_ = (CompensationInfo.Data) _o1_;
			if (receiveawardtime != _o_.receiveawardtime) return false;
			if (readflag != _o_.readflag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += receiveawardtime;
			_h_ += readflag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(receiveawardtime);
			_sb_.append(",");
			_sb_.append(readflag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

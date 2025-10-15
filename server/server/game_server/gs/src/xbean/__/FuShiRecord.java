
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FuShiRecord extends mkdb.XBean implements xbean.FuShiRecord {
	private long addtime; // 增加的时间
	private int currentnum; // 当前数量

	@Override
	public void _reset_unsafe_() {
		addtime = 0L;
		currentnum = 0;
	}

	FuShiRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public FuShiRecord() {
		this(0, null, null);
	}

	public FuShiRecord(FuShiRecord _o_) {
		this(_o_, null, null);
	}

	FuShiRecord(xbean.FuShiRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FuShiRecord) assign((FuShiRecord)_o1_);
		else if (_o1_ instanceof FuShiRecord.Data) assign((FuShiRecord.Data)_o1_);
		else if (_o1_ instanceof FuShiRecord.Const) assign(((FuShiRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FuShiRecord _o_) {
		_o_._xdb_verify_unsafe_();
		addtime = _o_.addtime;
		currentnum = _o_.currentnum;
	}

	private void assign(FuShiRecord.Data _o_) {
		addtime = _o_.addtime;
		currentnum = _o_.currentnum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(addtime);
		_os_.marshal(currentnum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		addtime = _os_.unmarshal_long();
		currentnum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.FuShiRecord copy() {
		_xdb_verify_unsafe_();
		return new FuShiRecord(this);
	}

	@Override
	public xbean.FuShiRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FuShiRecord toBean() {
		_xdb_verify_unsafe_();
		return new FuShiRecord(this); // same as copy()
	}

	@Override
	public xbean.FuShiRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FuShiRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getAddtime() { // 增加的时间
		_xdb_verify_unsafe_();
		return addtime;
	}

	@Override
	public int getCurrentnum() { // 当前数量
		_xdb_verify_unsafe_();
		return currentnum;
	}

	@Override
	public void setAddtime(long _v_) { // 增加的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "addtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, addtime) {
					public void rollback() { addtime = _xdb_saved; }
				};}});
		addtime = _v_;
	}

	@Override
	public void setCurrentnum(int _v_) { // 当前数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currentnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currentnum) {
					public void rollback() { currentnum = _xdb_saved; }
				};}});
		currentnum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FuShiRecord _o_ = null;
		if ( _o1_ instanceof FuShiRecord ) _o_ = (FuShiRecord)_o1_;
		else if ( _o1_ instanceof FuShiRecord.Const ) _o_ = ((FuShiRecord.Const)_o1_).nThis();
		else return false;
		if (addtime != _o_.addtime) return false;
		if (currentnum != _o_.currentnum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += addtime;
		_h_ += currentnum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(addtime);
		_sb_.append(",");
		_sb_.append(currentnum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("addtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currentnum"));
		return lb;
	}

	private class Const implements xbean.FuShiRecord {
		FuShiRecord nThis() {
			return FuShiRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FuShiRecord copy() {
			return FuShiRecord.this.copy();
		}

		@Override
		public xbean.FuShiRecord toData() {
			return FuShiRecord.this.toData();
		}

		public xbean.FuShiRecord toBean() {
			return FuShiRecord.this.toBean();
		}

		@Override
		public xbean.FuShiRecord toDataIf() {
			return FuShiRecord.this.toDataIf();
		}

		public xbean.FuShiRecord toBeanIf() {
			return FuShiRecord.this.toBeanIf();
		}

		@Override
		public long getAddtime() { // 增加的时间
			_xdb_verify_unsafe_();
			return addtime;
		}

		@Override
		public int getCurrentnum() { // 当前数量
			_xdb_verify_unsafe_();
			return currentnum;
		}

		@Override
		public void setAddtime(long _v_) { // 增加的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentnum(int _v_) { // 当前数量
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
			return FuShiRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FuShiRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FuShiRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FuShiRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FuShiRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FuShiRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FuShiRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FuShiRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return FuShiRecord.this.toString();
		}

	}

	public static final class Data implements xbean.FuShiRecord {
		private long addtime; // 增加的时间
		private int currentnum; // 当前数量

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.FuShiRecord _o1_) {
			if (_o1_ instanceof FuShiRecord) assign((FuShiRecord)_o1_);
			else if (_o1_ instanceof FuShiRecord.Data) assign((FuShiRecord.Data)_o1_);
			else if (_o1_ instanceof FuShiRecord.Const) assign(((FuShiRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FuShiRecord _o_) {
			addtime = _o_.addtime;
			currentnum = _o_.currentnum;
		}

		private void assign(FuShiRecord.Data _o_) {
			addtime = _o_.addtime;
			currentnum = _o_.currentnum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(addtime);
			_os_.marshal(currentnum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			addtime = _os_.unmarshal_long();
			currentnum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.FuShiRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.FuShiRecord toData() {
			return new Data(this);
		}

		public xbean.FuShiRecord toBean() {
			return new FuShiRecord(this, null, null);
		}

		@Override
		public xbean.FuShiRecord toDataIf() {
			return this;
		}

		public xbean.FuShiRecord toBeanIf() {
			return new FuShiRecord(this, null, null);
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
		public long getAddtime() { // 增加的时间
			return addtime;
		}

		@Override
		public int getCurrentnum() { // 当前数量
			return currentnum;
		}

		@Override
		public void setAddtime(long _v_) { // 增加的时间
			addtime = _v_;
		}

		@Override
		public void setCurrentnum(int _v_) { // 当前数量
			currentnum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FuShiRecord.Data)) return false;
			FuShiRecord.Data _o_ = (FuShiRecord.Data) _o1_;
			if (addtime != _o_.addtime) return false;
			if (currentnum != _o_.currentnum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += addtime;
			_h_ += currentnum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(addtime);
			_sb_.append(",");
			_sb_.append(currentnum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

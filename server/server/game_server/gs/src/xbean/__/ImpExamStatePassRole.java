
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ImpExamStatePassRole extends mkdb.XBean implements xbean.ImpExamStatePassRole {
	private long roleid; // 
	private int accrightnum; // 累积答对次数(vill---prov--state)
	private long stateexamtime; // 通过stateexam所用的时间

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		accrightnum = 0;
		stateexamtime = 0L;
	}

	ImpExamStatePassRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ImpExamStatePassRole() {
		this(0, null, null);
	}

	public ImpExamStatePassRole(ImpExamStatePassRole _o_) {
		this(_o_, null, null);
	}

	ImpExamStatePassRole(xbean.ImpExamStatePassRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ImpExamStatePassRole) assign((ImpExamStatePassRole)_o1_);
		else if (_o1_ instanceof ImpExamStatePassRole.Data) assign((ImpExamStatePassRole.Data)_o1_);
		else if (_o1_ instanceof ImpExamStatePassRole.Const) assign(((ImpExamStatePassRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ImpExamStatePassRole _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		accrightnum = _o_.accrightnum;
		stateexamtime = _o_.stateexamtime;
	}

	private void assign(ImpExamStatePassRole.Data _o_) {
		roleid = _o_.roleid;
		accrightnum = _o_.accrightnum;
		stateexamtime = _o_.stateexamtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(accrightnum);
		_os_.marshal(stateexamtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		accrightnum = _os_.unmarshal_int();
		stateexamtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ImpExamStatePassRole copy() {
		_xdb_verify_unsafe_();
		return new ImpExamStatePassRole(this);
	}

	@Override
	public xbean.ImpExamStatePassRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ImpExamStatePassRole toBean() {
		_xdb_verify_unsafe_();
		return new ImpExamStatePassRole(this); // same as copy()
	}

	@Override
	public xbean.ImpExamStatePassRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ImpExamStatePassRole toBeanIf() {
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
	public int getAccrightnum() { // 累积答对次数(vill---prov--state)
		_xdb_verify_unsafe_();
		return accrightnum;
	}

	@Override
	public long getStateexamtime() { // 通过stateexam所用的时间
		_xdb_verify_unsafe_();
		return stateexamtime;
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
	public void setAccrightnum(int _v_) { // 累积答对次数(vill---prov--state)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "accrightnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, accrightnum) {
					public void rollback() { accrightnum = _xdb_saved; }
				};}});
		accrightnum = _v_;
	}

	@Override
	public void setStateexamtime(long _v_) { // 通过stateexam所用的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "stateexamtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, stateexamtime) {
					public void rollback() { stateexamtime = _xdb_saved; }
				};}});
		stateexamtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ImpExamStatePassRole _o_ = null;
		if ( _o1_ instanceof ImpExamStatePassRole ) _o_ = (ImpExamStatePassRole)_o1_;
		else if ( _o1_ instanceof ImpExamStatePassRole.Const ) _o_ = ((ImpExamStatePassRole.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (accrightnum != _o_.accrightnum) return false;
		if (stateexamtime != _o_.stateexamtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += accrightnum;
		_h_ += stateexamtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(accrightnum);
		_sb_.append(",");
		_sb_.append(stateexamtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("accrightnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("stateexamtime"));
		return lb;
	}

	private class Const implements xbean.ImpExamStatePassRole {
		ImpExamStatePassRole nThis() {
			return ImpExamStatePassRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ImpExamStatePassRole copy() {
			return ImpExamStatePassRole.this.copy();
		}

		@Override
		public xbean.ImpExamStatePassRole toData() {
			return ImpExamStatePassRole.this.toData();
		}

		public xbean.ImpExamStatePassRole toBean() {
			return ImpExamStatePassRole.this.toBean();
		}

		@Override
		public xbean.ImpExamStatePassRole toDataIf() {
			return ImpExamStatePassRole.this.toDataIf();
		}

		public xbean.ImpExamStatePassRole toBeanIf() {
			return ImpExamStatePassRole.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getAccrightnum() { // 累积答对次数(vill---prov--state)
			_xdb_verify_unsafe_();
			return accrightnum;
		}

		@Override
		public long getStateexamtime() { // 通过stateexam所用的时间
			_xdb_verify_unsafe_();
			return stateexamtime;
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAccrightnum(int _v_) { // 累积答对次数(vill---prov--state)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStateexamtime(long _v_) { // 通过stateexam所用的时间
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
			return ImpExamStatePassRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ImpExamStatePassRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ImpExamStatePassRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ImpExamStatePassRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ImpExamStatePassRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ImpExamStatePassRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ImpExamStatePassRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ImpExamStatePassRole.this.hashCode();
		}

		@Override
		public String toString() {
			return ImpExamStatePassRole.this.toString();
		}

	}

	public static final class Data implements xbean.ImpExamStatePassRole {
		private long roleid; // 
		private int accrightnum; // 累积答对次数(vill---prov--state)
		private long stateexamtime; // 通过stateexam所用的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ImpExamStatePassRole _o1_) {
			if (_o1_ instanceof ImpExamStatePassRole) assign((ImpExamStatePassRole)_o1_);
			else if (_o1_ instanceof ImpExamStatePassRole.Data) assign((ImpExamStatePassRole.Data)_o1_);
			else if (_o1_ instanceof ImpExamStatePassRole.Const) assign(((ImpExamStatePassRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ImpExamStatePassRole _o_) {
			roleid = _o_.roleid;
			accrightnum = _o_.accrightnum;
			stateexamtime = _o_.stateexamtime;
		}

		private void assign(ImpExamStatePassRole.Data _o_) {
			roleid = _o_.roleid;
			accrightnum = _o_.accrightnum;
			stateexamtime = _o_.stateexamtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(accrightnum);
			_os_.marshal(stateexamtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			accrightnum = _os_.unmarshal_int();
			stateexamtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ImpExamStatePassRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.ImpExamStatePassRole toData() {
			return new Data(this);
		}

		public xbean.ImpExamStatePassRole toBean() {
			return new ImpExamStatePassRole(this, null, null);
		}

		@Override
		public xbean.ImpExamStatePassRole toDataIf() {
			return this;
		}

		public xbean.ImpExamStatePassRole toBeanIf() {
			return new ImpExamStatePassRole(this, null, null);
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
		public int getAccrightnum() { // 累积答对次数(vill---prov--state)
			return accrightnum;
		}

		@Override
		public long getStateexamtime() { // 通过stateexam所用的时间
			return stateexamtime;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setAccrightnum(int _v_) { // 累积答对次数(vill---prov--state)
			accrightnum = _v_;
		}

		@Override
		public void setStateexamtime(long _v_) { // 通过stateexam所用的时间
			stateexamtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ImpExamStatePassRole.Data)) return false;
			ImpExamStatePassRole.Data _o_ = (ImpExamStatePassRole.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (accrightnum != _o_.accrightnum) return false;
			if (stateexamtime != _o_.stateexamtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += accrightnum;
			_h_ += stateexamtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(accrightnum);
			_sb_.append(",");
			_sb_.append(stateexamtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

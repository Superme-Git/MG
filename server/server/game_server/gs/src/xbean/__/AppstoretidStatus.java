
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AppstoretidStatus extends mkdb.XBean implements xbean.AppstoretidStatus {
	private long chargesn; // 
	private int status; // 0处理中,1充值成功,2充值失败

	@Override
	public void _reset_unsafe_() {
		chargesn = 0L;
		status = 0;
	}

	AppstoretidStatus(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public AppstoretidStatus() {
		this(0, null, null);
	}

	public AppstoretidStatus(AppstoretidStatus _o_) {
		this(_o_, null, null);
	}

	AppstoretidStatus(xbean.AppstoretidStatus _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AppstoretidStatus) assign((AppstoretidStatus)_o1_);
		else if (_o1_ instanceof AppstoretidStatus.Data) assign((AppstoretidStatus.Data)_o1_);
		else if (_o1_ instanceof AppstoretidStatus.Const) assign(((AppstoretidStatus.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AppstoretidStatus _o_) {
		_o_._xdb_verify_unsafe_();
		chargesn = _o_.chargesn;
		status = _o_.status;
	}

	private void assign(AppstoretidStatus.Data _o_) {
		chargesn = _o_.chargesn;
		status = _o_.status;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(chargesn);
		_os_.marshal(status);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		chargesn = _os_.unmarshal_long();
		status = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.AppstoretidStatus copy() {
		_xdb_verify_unsafe_();
		return new AppstoretidStatus(this);
	}

	@Override
	public xbean.AppstoretidStatus toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AppstoretidStatus toBean() {
		_xdb_verify_unsafe_();
		return new AppstoretidStatus(this); // same as copy()
	}

	@Override
	public xbean.AppstoretidStatus toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AppstoretidStatus toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getChargesn() { // 
		_xdb_verify_unsafe_();
		return chargesn;
	}

	@Override
	public int getStatus() { // 0处理中,1充值成功,2充值失败
		_xdb_verify_unsafe_();
		return status;
	}

	@Override
	public void setChargesn(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargesn") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, chargesn) {
					public void rollback() { chargesn = _xdb_saved; }
				};}});
		chargesn = _v_;
	}

	@Override
	public void setStatus(int _v_) { // 0处理中,1充值成功,2充值失败
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "status") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, status) {
					public void rollback() { status = _xdb_saved; }
				};}});
		status = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AppstoretidStatus _o_ = null;
		if ( _o1_ instanceof AppstoretidStatus ) _o_ = (AppstoretidStatus)_o1_;
		else if ( _o1_ instanceof AppstoretidStatus.Const ) _o_ = ((AppstoretidStatus.Const)_o1_).nThis();
		else return false;
		if (chargesn != _o_.chargesn) return false;
		if (status != _o_.status) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += chargesn;
		_h_ += status;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(chargesn);
		_sb_.append(",");
		_sb_.append(status);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargesn"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("status"));
		return lb;
	}

	private class Const implements xbean.AppstoretidStatus {
		AppstoretidStatus nThis() {
			return AppstoretidStatus.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AppstoretidStatus copy() {
			return AppstoretidStatus.this.copy();
		}

		@Override
		public xbean.AppstoretidStatus toData() {
			return AppstoretidStatus.this.toData();
		}

		public xbean.AppstoretidStatus toBean() {
			return AppstoretidStatus.this.toBean();
		}

		@Override
		public xbean.AppstoretidStatus toDataIf() {
			return AppstoretidStatus.this.toDataIf();
		}

		public xbean.AppstoretidStatus toBeanIf() {
			return AppstoretidStatus.this.toBeanIf();
		}

		@Override
		public long getChargesn() { // 
			_xdb_verify_unsafe_();
			return chargesn;
		}

		@Override
		public int getStatus() { // 0处理中,1充值成功,2充值失败
			_xdb_verify_unsafe_();
			return status;
		}

		@Override
		public void setChargesn(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStatus(int _v_) { // 0处理中,1充值成功,2充值失败
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
			return AppstoretidStatus.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AppstoretidStatus.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AppstoretidStatus.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AppstoretidStatus.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AppstoretidStatus.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AppstoretidStatus.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AppstoretidStatus.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AppstoretidStatus.this.hashCode();
		}

		@Override
		public String toString() {
			return AppstoretidStatus.this.toString();
		}

	}

	public static final class Data implements xbean.AppstoretidStatus {
		private long chargesn; // 
		private int status; // 0处理中,1充值成功,2充值失败

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.AppstoretidStatus _o1_) {
			if (_o1_ instanceof AppstoretidStatus) assign((AppstoretidStatus)_o1_);
			else if (_o1_ instanceof AppstoretidStatus.Data) assign((AppstoretidStatus.Data)_o1_);
			else if (_o1_ instanceof AppstoretidStatus.Const) assign(((AppstoretidStatus.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AppstoretidStatus _o_) {
			chargesn = _o_.chargesn;
			status = _o_.status;
		}

		private void assign(AppstoretidStatus.Data _o_) {
			chargesn = _o_.chargesn;
			status = _o_.status;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(chargesn);
			_os_.marshal(status);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			chargesn = _os_.unmarshal_long();
			status = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.AppstoretidStatus copy() {
			return new Data(this);
		}

		@Override
		public xbean.AppstoretidStatus toData() {
			return new Data(this);
		}

		public xbean.AppstoretidStatus toBean() {
			return new AppstoretidStatus(this, null, null);
		}

		@Override
		public xbean.AppstoretidStatus toDataIf() {
			return this;
		}

		public xbean.AppstoretidStatus toBeanIf() {
			return new AppstoretidStatus(this, null, null);
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
		public long getChargesn() { // 
			return chargesn;
		}

		@Override
		public int getStatus() { // 0处理中,1充值成功,2充值失败
			return status;
		}

		@Override
		public void setChargesn(long _v_) { // 
			chargesn = _v_;
		}

		@Override
		public void setStatus(int _v_) { // 0处理中,1充值成功,2充值失败
			status = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AppstoretidStatus.Data)) return false;
			AppstoretidStatus.Data _o_ = (AppstoretidStatus.Data) _o1_;
			if (chargesn != _o_.chargesn) return false;
			if (status != _o_.status) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += chargesn;
			_h_ += status;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(chargesn);
			_sb_.append(",");
			_sb_.append(status);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

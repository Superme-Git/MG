
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class forbidinfo extends mkdb.XBean implements xbean.forbidinfo {
	private long forbidtime; // 
	private String reason; // 

	@Override
	public void _reset_unsafe_() {
		forbidtime = 0L;
		reason = "";
	}

	forbidinfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		reason = "";
	}

	public forbidinfo() {
		this(0, null, null);
	}

	public forbidinfo(forbidinfo _o_) {
		this(_o_, null, null);
	}

	forbidinfo(xbean.forbidinfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof forbidinfo) assign((forbidinfo)_o1_);
		else if (_o1_ instanceof forbidinfo.Data) assign((forbidinfo.Data)_o1_);
		else if (_o1_ instanceof forbidinfo.Const) assign(((forbidinfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(forbidinfo _o_) {
		_o_._xdb_verify_unsafe_();
		forbidtime = _o_.forbidtime;
		reason = _o_.reason;
	}

	private void assign(forbidinfo.Data _o_) {
		forbidtime = _o_.forbidtime;
		reason = _o_.reason;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(forbidtime);
		_os_.marshal(reason, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		forbidtime = _os_.unmarshal_long();
		reason = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.forbidinfo copy() {
		_xdb_verify_unsafe_();
		return new forbidinfo(this);
	}

	@Override
	public xbean.forbidinfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.forbidinfo toBean() {
		_xdb_verify_unsafe_();
		return new forbidinfo(this); // same as copy()
	}

	@Override
	public xbean.forbidinfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.forbidinfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getForbidtime() { // 
		_xdb_verify_unsafe_();
		return forbidtime;
	}

	@Override
	public String getReason() { // 
		_xdb_verify_unsafe_();
		return reason;
	}

	@Override
	public com.locojoy.base.Octets getReasonOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getReason(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setForbidtime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "forbidtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, forbidtime) {
					public void rollback() { forbidtime = _xdb_saved; }
				};}});
		forbidtime = _v_;
	}

	@Override
	public void setReason(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "reason") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, reason) {
					public void rollback() { reason = _xdb_saved; }
				};}});
		reason = _v_;
	}

	@Override
	public void setReasonOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setReason(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		forbidinfo _o_ = null;
		if ( _o1_ instanceof forbidinfo ) _o_ = (forbidinfo)_o1_;
		else if ( _o1_ instanceof forbidinfo.Const ) _o_ = ((forbidinfo.Const)_o1_).nThis();
		else return false;
		if (forbidtime != _o_.forbidtime) return false;
		if (!reason.equals(_o_.reason)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += forbidtime;
		_h_ += reason.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(forbidtime);
		_sb_.append(",");
		_sb_.append("'").append(reason).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("forbidtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("reason"));
		return lb;
	}

	private class Const implements xbean.forbidinfo {
		forbidinfo nThis() {
			return forbidinfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.forbidinfo copy() {
			return forbidinfo.this.copy();
		}

		@Override
		public xbean.forbidinfo toData() {
			return forbidinfo.this.toData();
		}

		public xbean.forbidinfo toBean() {
			return forbidinfo.this.toBean();
		}

		@Override
		public xbean.forbidinfo toDataIf() {
			return forbidinfo.this.toDataIf();
		}

		public xbean.forbidinfo toBeanIf() {
			return forbidinfo.this.toBeanIf();
		}

		@Override
		public long getForbidtime() { // 
			_xdb_verify_unsafe_();
			return forbidtime;
		}

		@Override
		public String getReason() { // 
			_xdb_verify_unsafe_();
			return reason;
		}

		@Override
		public com.locojoy.base.Octets getReasonOctets() { // 
			_xdb_verify_unsafe_();
			return forbidinfo.this.getReasonOctets();
		}

		@Override
		public void setForbidtime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReason(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReasonOctets(com.locojoy.base.Octets _v_) { // 
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
			return forbidinfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return forbidinfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return forbidinfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return forbidinfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return forbidinfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return forbidinfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return forbidinfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return forbidinfo.this.hashCode();
		}

		@Override
		public String toString() {
			return forbidinfo.this.toString();
		}

	}

	public static final class Data implements xbean.forbidinfo {
		private long forbidtime; // 
		private String reason; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			reason = "";
		}

		Data(xbean.forbidinfo _o1_) {
			if (_o1_ instanceof forbidinfo) assign((forbidinfo)_o1_);
			else if (_o1_ instanceof forbidinfo.Data) assign((forbidinfo.Data)_o1_);
			else if (_o1_ instanceof forbidinfo.Const) assign(((forbidinfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(forbidinfo _o_) {
			forbidtime = _o_.forbidtime;
			reason = _o_.reason;
		}

		private void assign(forbidinfo.Data _o_) {
			forbidtime = _o_.forbidtime;
			reason = _o_.reason;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(forbidtime);
			_os_.marshal(reason, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			forbidtime = _os_.unmarshal_long();
			reason = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.forbidinfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.forbidinfo toData() {
			return new Data(this);
		}

		public xbean.forbidinfo toBean() {
			return new forbidinfo(this, null, null);
		}

		@Override
		public xbean.forbidinfo toDataIf() {
			return this;
		}

		public xbean.forbidinfo toBeanIf() {
			return new forbidinfo(this, null, null);
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
		public long getForbidtime() { // 
			return forbidtime;
		}

		@Override
		public String getReason() { // 
			return reason;
		}

		@Override
		public com.locojoy.base.Octets getReasonOctets() { // 
			return com.locojoy.base.Octets.wrap(getReason(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setForbidtime(long _v_) { // 
			forbidtime = _v_;
		}

		@Override
		public void setReason(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			reason = _v_;
		}

		@Override
		public void setReasonOctets(com.locojoy.base.Octets _v_) { // 
			this.setReason(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof forbidinfo.Data)) return false;
			forbidinfo.Data _o_ = (forbidinfo.Data) _o1_;
			if (forbidtime != _o_.forbidtime) return false;
			if (!reason.equals(_o_.reason)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += forbidtime;
			_h_ += reason.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(forbidtime);
			_sb_.append(",");
			_sb_.append("'").append(reason).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

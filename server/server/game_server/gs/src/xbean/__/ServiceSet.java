
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ServiceSet extends mkdb.XBean implements xbean.ServiceSet {
	private int bindtelagain; // 重新绑定手机的次数
	private long bindtelagaintime; // 重新绑定手机的时间

	@Override
	public void _reset_unsafe_() {
		bindtelagain = 0;
		bindtelagaintime = 0L;
	}

	ServiceSet(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ServiceSet() {
		this(0, null, null);
	}

	public ServiceSet(ServiceSet _o_) {
		this(_o_, null, null);
	}

	ServiceSet(xbean.ServiceSet _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ServiceSet) assign((ServiceSet)_o1_);
		else if (_o1_ instanceof ServiceSet.Data) assign((ServiceSet.Data)_o1_);
		else if (_o1_ instanceof ServiceSet.Const) assign(((ServiceSet.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ServiceSet _o_) {
		_o_._xdb_verify_unsafe_();
		bindtelagain = _o_.bindtelagain;
		bindtelagaintime = _o_.bindtelagaintime;
	}

	private void assign(ServiceSet.Data _o_) {
		bindtelagain = _o_.bindtelagain;
		bindtelagaintime = _o_.bindtelagaintime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(bindtelagain);
		_os_.marshal(bindtelagaintime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		bindtelagain = _os_.unmarshal_int();
		bindtelagaintime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ServiceSet copy() {
		_xdb_verify_unsafe_();
		return new ServiceSet(this);
	}

	@Override
	public xbean.ServiceSet toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ServiceSet toBean() {
		_xdb_verify_unsafe_();
		return new ServiceSet(this); // same as copy()
	}

	@Override
	public xbean.ServiceSet toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ServiceSet toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBindtelagain() { // 重新绑定手机的次数
		_xdb_verify_unsafe_();
		return bindtelagain;
	}

	@Override
	public long getBindtelagaintime() { // 重新绑定手机的时间
		_xdb_verify_unsafe_();
		return bindtelagaintime;
	}

	@Override
	public void setBindtelagain(int _v_) { // 重新绑定手机的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bindtelagain") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bindtelagain) {
					public void rollback() { bindtelagain = _xdb_saved; }
				};}});
		bindtelagain = _v_;
	}

	@Override
	public void setBindtelagaintime(long _v_) { // 重新绑定手机的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bindtelagaintime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bindtelagaintime) {
					public void rollback() { bindtelagaintime = _xdb_saved; }
				};}});
		bindtelagaintime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ServiceSet _o_ = null;
		if ( _o1_ instanceof ServiceSet ) _o_ = (ServiceSet)_o1_;
		else if ( _o1_ instanceof ServiceSet.Const ) _o_ = ((ServiceSet.Const)_o1_).nThis();
		else return false;
		if (bindtelagain != _o_.bindtelagain) return false;
		if (bindtelagaintime != _o_.bindtelagaintime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += bindtelagain;
		_h_ += bindtelagaintime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(bindtelagain);
		_sb_.append(",");
		_sb_.append(bindtelagaintime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bindtelagain"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bindtelagaintime"));
		return lb;
	}

	private class Const implements xbean.ServiceSet {
		ServiceSet nThis() {
			return ServiceSet.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ServiceSet copy() {
			return ServiceSet.this.copy();
		}

		@Override
		public xbean.ServiceSet toData() {
			return ServiceSet.this.toData();
		}

		public xbean.ServiceSet toBean() {
			return ServiceSet.this.toBean();
		}

		@Override
		public xbean.ServiceSet toDataIf() {
			return ServiceSet.this.toDataIf();
		}

		public xbean.ServiceSet toBeanIf() {
			return ServiceSet.this.toBeanIf();
		}

		@Override
		public int getBindtelagain() { // 重新绑定手机的次数
			_xdb_verify_unsafe_();
			return bindtelagain;
		}

		@Override
		public long getBindtelagaintime() { // 重新绑定手机的时间
			_xdb_verify_unsafe_();
			return bindtelagaintime;
		}

		@Override
		public void setBindtelagain(int _v_) { // 重新绑定手机的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBindtelagaintime(long _v_) { // 重新绑定手机的时间
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
			return ServiceSet.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ServiceSet.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ServiceSet.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ServiceSet.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ServiceSet.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ServiceSet.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ServiceSet.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ServiceSet.this.hashCode();
		}

		@Override
		public String toString() {
			return ServiceSet.this.toString();
		}

	}

	public static final class Data implements xbean.ServiceSet {
		private int bindtelagain; // 重新绑定手机的次数
		private long bindtelagaintime; // 重新绑定手机的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ServiceSet _o1_) {
			if (_o1_ instanceof ServiceSet) assign((ServiceSet)_o1_);
			else if (_o1_ instanceof ServiceSet.Data) assign((ServiceSet.Data)_o1_);
			else if (_o1_ instanceof ServiceSet.Const) assign(((ServiceSet.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ServiceSet _o_) {
			bindtelagain = _o_.bindtelagain;
			bindtelagaintime = _o_.bindtelagaintime;
		}

		private void assign(ServiceSet.Data _o_) {
			bindtelagain = _o_.bindtelagain;
			bindtelagaintime = _o_.bindtelagaintime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(bindtelagain);
			_os_.marshal(bindtelagaintime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			bindtelagain = _os_.unmarshal_int();
			bindtelagaintime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ServiceSet copy() {
			return new Data(this);
		}

		@Override
		public xbean.ServiceSet toData() {
			return new Data(this);
		}

		public xbean.ServiceSet toBean() {
			return new ServiceSet(this, null, null);
		}

		@Override
		public xbean.ServiceSet toDataIf() {
			return this;
		}

		public xbean.ServiceSet toBeanIf() {
			return new ServiceSet(this, null, null);
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
		public int getBindtelagain() { // 重新绑定手机的次数
			return bindtelagain;
		}

		@Override
		public long getBindtelagaintime() { // 重新绑定手机的时间
			return bindtelagaintime;
		}

		@Override
		public void setBindtelagain(int _v_) { // 重新绑定手机的次数
			bindtelagain = _v_;
		}

		@Override
		public void setBindtelagaintime(long _v_) { // 重新绑定手机的时间
			bindtelagaintime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ServiceSet.Data)) return false;
			ServiceSet.Data _o_ = (ServiceSet.Data) _o1_;
			if (bindtelagain != _o_.bindtelagain) return false;
			if (bindtelagaintime != _o_.bindtelagaintime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += bindtelagain;
			_h_ += bindtelagaintime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(bindtelagain);
			_sb_.append(",");
			_sb_.append(bindtelagaintime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

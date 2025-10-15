
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class subscription extends mkdb.XBean implements xbean.subscription {
	private long expiretime; // 订阅过期时间
	private long subscribetime; // 上次订阅开始时间

	@Override
	public void _reset_unsafe_() {
		expiretime = 0;
		subscribetime = 0;
	}

	subscription(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		expiretime = 0;
		subscribetime = 0;
	}

	public subscription() {
		this(0, null, null);
	}

	public subscription(subscription _o_) {
		this(_o_, null, null);
	}

	subscription(xbean.subscription _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof subscription) assign((subscription)_o1_);
		else if (_o1_ instanceof subscription.Data) assign((subscription.Data)_o1_);
		else if (_o1_ instanceof subscription.Const) assign(((subscription.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(subscription _o_) {
		_o_._xdb_verify_unsafe_();
		expiretime = _o_.expiretime;
		subscribetime = _o_.subscribetime;
	}

	private void assign(subscription.Data _o_) {
		expiretime = _o_.expiretime;
		subscribetime = _o_.subscribetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(expiretime);
		_os_.marshal(subscribetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		expiretime = _os_.unmarshal_long();
		subscribetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.subscription copy() {
		_xdb_verify_unsafe_();
		return new subscription(this);
	}

	@Override
	public xbean.subscription toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.subscription toBean() {
		_xdb_verify_unsafe_();
		return new subscription(this); // same as copy()
	}

	@Override
	public xbean.subscription toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.subscription toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getExpiretime() { // 订阅过期时间
		_xdb_verify_unsafe_();
		return expiretime;
	}

	@Override
	public long getSubscribetime() { // 上次订阅开始时间
		_xdb_verify_unsafe_();
		return subscribetime;
	}

	@Override
	public void setExpiretime(long _v_) { // 订阅过期时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expiretime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expiretime) {
					public void rollback() { expiretime = _xdb_saved; }
				};}});
		expiretime = _v_;
	}

	@Override
	public void setSubscribetime(long _v_) { // 上次订阅开始时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "subscribetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, subscribetime) {
					public void rollback() { subscribetime = _xdb_saved; }
				};}});
		subscribetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		subscription _o_ = null;
		if ( _o1_ instanceof subscription ) _o_ = (subscription)_o1_;
		else if ( _o1_ instanceof subscription.Const ) _o_ = ((subscription.Const)_o1_).nThis();
		else return false;
		if (expiretime != _o_.expiretime) return false;
		if (subscribetime != _o_.subscribetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += expiretime;
		_h_ += subscribetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(expiretime);
		_sb_.append(",");
		_sb_.append(subscribetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expiretime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("subscribetime"));
		return lb;
	}

	private class Const implements xbean.subscription {
		subscription nThis() {
			return subscription.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.subscription copy() {
			return subscription.this.copy();
		}

		@Override
		public xbean.subscription toData() {
			return subscription.this.toData();
		}

		public xbean.subscription toBean() {
			return subscription.this.toBean();
		}

		@Override
		public xbean.subscription toDataIf() {
			return subscription.this.toDataIf();
		}

		public xbean.subscription toBeanIf() {
			return subscription.this.toBeanIf();
		}

		@Override
		public long getExpiretime() { // 订阅过期时间
			_xdb_verify_unsafe_();
			return expiretime;
		}

		@Override
		public long getSubscribetime() { // 上次订阅开始时间
			_xdb_verify_unsafe_();
			return subscribetime;
		}

		@Override
		public void setExpiretime(long _v_) { // 订阅过期时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSubscribetime(long _v_) { // 上次订阅开始时间
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
			return subscription.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return subscription.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return subscription.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return subscription.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return subscription.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return subscription.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return subscription.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return subscription.this.hashCode();
		}

		@Override
		public String toString() {
			return subscription.this.toString();
		}

	}

	public static final class Data implements xbean.subscription {
		private long expiretime; // 订阅过期时间
		private long subscribetime; // 上次订阅开始时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			expiretime = 0;
			subscribetime = 0;
		}

		Data(xbean.subscription _o1_) {
			if (_o1_ instanceof subscription) assign((subscription)_o1_);
			else if (_o1_ instanceof subscription.Data) assign((subscription.Data)_o1_);
			else if (_o1_ instanceof subscription.Const) assign(((subscription.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(subscription _o_) {
			expiretime = _o_.expiretime;
			subscribetime = _o_.subscribetime;
		}

		private void assign(subscription.Data _o_) {
			expiretime = _o_.expiretime;
			subscribetime = _o_.subscribetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(expiretime);
			_os_.marshal(subscribetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			expiretime = _os_.unmarshal_long();
			subscribetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.subscription copy() {
			return new Data(this);
		}

		@Override
		public xbean.subscription toData() {
			return new Data(this);
		}

		public xbean.subscription toBean() {
			return new subscription(this, null, null);
		}

		@Override
		public xbean.subscription toDataIf() {
			return this;
		}

		public xbean.subscription toBeanIf() {
			return new subscription(this, null, null);
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
		public long getExpiretime() { // 订阅过期时间
			return expiretime;
		}

		@Override
		public long getSubscribetime() { // 上次订阅开始时间
			return subscribetime;
		}

		@Override
		public void setExpiretime(long _v_) { // 订阅过期时间
			expiretime = _v_;
		}

		@Override
		public void setSubscribetime(long _v_) { // 上次订阅开始时间
			subscribetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof subscription.Data)) return false;
			subscription.Data _o_ = (subscription.Data) _o1_;
			if (expiretime != _o_.expiretime) return false;
			if (subscribetime != _o_.subscribetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += expiretime;
			_h_ += subscribetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(expiretime);
			_sb_.append(",");
			_sb_.append(subscribetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

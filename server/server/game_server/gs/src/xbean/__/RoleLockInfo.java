
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleLockInfo extends mkdb.XBean implements xbean.RoleLockInfo {
	private String password; // 安全锁密码,为空表示没有设置安全锁
	private long unlocktime; // 开始解锁的时间,为0表示没解锁
	private long forceunlocktime; // 开始强行解锁的时间,为0表示没有申请强制解锁
	private int errortimes; // 连续输错密码的次数
	private long fullerrortime; // 连续输错密码达上限的时间

	@Override
	public void _reset_unsafe_() {
		password = "";
		unlocktime = 0L;
		forceunlocktime = 0L;
		errortimes = 0;
		fullerrortime = 0L;
	}

	RoleLockInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		password = "";
	}

	public RoleLockInfo() {
		this(0, null, null);
	}

	public RoleLockInfo(RoleLockInfo _o_) {
		this(_o_, null, null);
	}

	RoleLockInfo(xbean.RoleLockInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleLockInfo) assign((RoleLockInfo)_o1_);
		else if (_o1_ instanceof RoleLockInfo.Data) assign((RoleLockInfo.Data)_o1_);
		else if (_o1_ instanceof RoleLockInfo.Const) assign(((RoleLockInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleLockInfo _o_) {
		_o_._xdb_verify_unsafe_();
		password = _o_.password;
		unlocktime = _o_.unlocktime;
		forceunlocktime = _o_.forceunlocktime;
		errortimes = _o_.errortimes;
		fullerrortime = _o_.fullerrortime;
	}

	private void assign(RoleLockInfo.Data _o_) {
		password = _o_.password;
		unlocktime = _o_.unlocktime;
		forceunlocktime = _o_.forceunlocktime;
		errortimes = _o_.errortimes;
		fullerrortime = _o_.fullerrortime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(password, mkdb.Const.IO_CHARSET);
		_os_.marshal(unlocktime);
		_os_.marshal(forceunlocktime);
		_os_.marshal(errortimes);
		_os_.marshal(fullerrortime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		password = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		unlocktime = _os_.unmarshal_long();
		forceunlocktime = _os_.unmarshal_long();
		errortimes = _os_.unmarshal_int();
		fullerrortime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleLockInfo copy() {
		_xdb_verify_unsafe_();
		return new RoleLockInfo(this);
	}

	@Override
	public xbean.RoleLockInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleLockInfo toBean() {
		_xdb_verify_unsafe_();
		return new RoleLockInfo(this); // same as copy()
	}

	@Override
	public xbean.RoleLockInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleLockInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getPassword() { // 安全锁密码,为空表示没有设置安全锁
		_xdb_verify_unsafe_();
		return password;
	}

	@Override
	public com.locojoy.base.Octets getPasswordOctets() { // 安全锁密码,为空表示没有设置安全锁
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPassword(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getUnlocktime() { // 开始解锁的时间,为0表示没解锁
		_xdb_verify_unsafe_();
		return unlocktime;
	}

	@Override
	public long getForceunlocktime() { // 开始强行解锁的时间,为0表示没有申请强制解锁
		_xdb_verify_unsafe_();
		return forceunlocktime;
	}

	@Override
	public int getErrortimes() { // 连续输错密码的次数
		_xdb_verify_unsafe_();
		return errortimes;
	}

	@Override
	public long getFullerrortime() { // 连续输错密码达上限的时间
		_xdb_verify_unsafe_();
		return fullerrortime;
	}

	@Override
	public void setPassword(String _v_) { // 安全锁密码,为空表示没有设置安全锁
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "password") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, password) {
					public void rollback() { password = _xdb_saved; }
				};}});
		password = _v_;
	}

	@Override
	public void setPasswordOctets(com.locojoy.base.Octets _v_) { // 安全锁密码,为空表示没有设置安全锁
		_xdb_verify_unsafe_();
		this.setPassword(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setUnlocktime(long _v_) { // 开始解锁的时间,为0表示没解锁
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "unlocktime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, unlocktime) {
					public void rollback() { unlocktime = _xdb_saved; }
				};}});
		unlocktime = _v_;
	}

	@Override
	public void setForceunlocktime(long _v_) { // 开始强行解锁的时间,为0表示没有申请强制解锁
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "forceunlocktime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, forceunlocktime) {
					public void rollback() { forceunlocktime = _xdb_saved; }
				};}});
		forceunlocktime = _v_;
	}

	@Override
	public void setErrortimes(int _v_) { // 连续输错密码的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "errortimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, errortimes) {
					public void rollback() { errortimes = _xdb_saved; }
				};}});
		errortimes = _v_;
	}

	@Override
	public void setFullerrortime(long _v_) { // 连续输错密码达上限的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fullerrortime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fullerrortime) {
					public void rollback() { fullerrortime = _xdb_saved; }
				};}});
		fullerrortime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleLockInfo _o_ = null;
		if ( _o1_ instanceof RoleLockInfo ) _o_ = (RoleLockInfo)_o1_;
		else if ( _o1_ instanceof RoleLockInfo.Const ) _o_ = ((RoleLockInfo.Const)_o1_).nThis();
		else return false;
		if (!password.equals(_o_.password)) return false;
		if (unlocktime != _o_.unlocktime) return false;
		if (forceunlocktime != _o_.forceunlocktime) return false;
		if (errortimes != _o_.errortimes) return false;
		if (fullerrortime != _o_.fullerrortime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += password.hashCode();
		_h_ += unlocktime;
		_h_ += forceunlocktime;
		_h_ += errortimes;
		_h_ += fullerrortime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(password).append("'");
		_sb_.append(",");
		_sb_.append(unlocktime);
		_sb_.append(",");
		_sb_.append(forceunlocktime);
		_sb_.append(",");
		_sb_.append(errortimes);
		_sb_.append(",");
		_sb_.append(fullerrortime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("password"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("unlocktime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("forceunlocktime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("errortimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fullerrortime"));
		return lb;
	}

	private class Const implements xbean.RoleLockInfo {
		RoleLockInfo nThis() {
			return RoleLockInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleLockInfo copy() {
			return RoleLockInfo.this.copy();
		}

		@Override
		public xbean.RoleLockInfo toData() {
			return RoleLockInfo.this.toData();
		}

		public xbean.RoleLockInfo toBean() {
			return RoleLockInfo.this.toBean();
		}

		@Override
		public xbean.RoleLockInfo toDataIf() {
			return RoleLockInfo.this.toDataIf();
		}

		public xbean.RoleLockInfo toBeanIf() {
			return RoleLockInfo.this.toBeanIf();
		}

		@Override
		public String getPassword() { // 安全锁密码,为空表示没有设置安全锁
			_xdb_verify_unsafe_();
			return password;
		}

		@Override
		public com.locojoy.base.Octets getPasswordOctets() { // 安全锁密码,为空表示没有设置安全锁
			_xdb_verify_unsafe_();
			return RoleLockInfo.this.getPasswordOctets();
		}

		@Override
		public long getUnlocktime() { // 开始解锁的时间,为0表示没解锁
			_xdb_verify_unsafe_();
			return unlocktime;
		}

		@Override
		public long getForceunlocktime() { // 开始强行解锁的时间,为0表示没有申请强制解锁
			_xdb_verify_unsafe_();
			return forceunlocktime;
		}

		@Override
		public int getErrortimes() { // 连续输错密码的次数
			_xdb_verify_unsafe_();
			return errortimes;
		}

		@Override
		public long getFullerrortime() { // 连续输错密码达上限的时间
			_xdb_verify_unsafe_();
			return fullerrortime;
		}

		@Override
		public void setPassword(String _v_) { // 安全锁密码,为空表示没有设置安全锁
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPasswordOctets(com.locojoy.base.Octets _v_) { // 安全锁密码,为空表示没有设置安全锁
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUnlocktime(long _v_) { // 开始解锁的时间,为0表示没解锁
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setForceunlocktime(long _v_) { // 开始强行解锁的时间,为0表示没有申请强制解锁
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setErrortimes(int _v_) { // 连续输错密码的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFullerrortime(long _v_) { // 连续输错密码达上限的时间
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
			return RoleLockInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleLockInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleLockInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleLockInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleLockInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleLockInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleLockInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleLockInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleLockInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RoleLockInfo {
		private String password; // 安全锁密码,为空表示没有设置安全锁
		private long unlocktime; // 开始解锁的时间,为0表示没解锁
		private long forceunlocktime; // 开始强行解锁的时间,为0表示没有申请强制解锁
		private int errortimes; // 连续输错密码的次数
		private long fullerrortime; // 连续输错密码达上限的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			password = "";
		}

		Data(xbean.RoleLockInfo _o1_) {
			if (_o1_ instanceof RoleLockInfo) assign((RoleLockInfo)_o1_);
			else if (_o1_ instanceof RoleLockInfo.Data) assign((RoleLockInfo.Data)_o1_);
			else if (_o1_ instanceof RoleLockInfo.Const) assign(((RoleLockInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleLockInfo _o_) {
			password = _o_.password;
			unlocktime = _o_.unlocktime;
			forceunlocktime = _o_.forceunlocktime;
			errortimes = _o_.errortimes;
			fullerrortime = _o_.fullerrortime;
		}

		private void assign(RoleLockInfo.Data _o_) {
			password = _o_.password;
			unlocktime = _o_.unlocktime;
			forceunlocktime = _o_.forceunlocktime;
			errortimes = _o_.errortimes;
			fullerrortime = _o_.fullerrortime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(password, mkdb.Const.IO_CHARSET);
			_os_.marshal(unlocktime);
			_os_.marshal(forceunlocktime);
			_os_.marshal(errortimes);
			_os_.marshal(fullerrortime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			password = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			unlocktime = _os_.unmarshal_long();
			forceunlocktime = _os_.unmarshal_long();
			errortimes = _os_.unmarshal_int();
			fullerrortime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleLockInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleLockInfo toData() {
			return new Data(this);
		}

		public xbean.RoleLockInfo toBean() {
			return new RoleLockInfo(this, null, null);
		}

		@Override
		public xbean.RoleLockInfo toDataIf() {
			return this;
		}

		public xbean.RoleLockInfo toBeanIf() {
			return new RoleLockInfo(this, null, null);
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
		public String getPassword() { // 安全锁密码,为空表示没有设置安全锁
			return password;
		}

		@Override
		public com.locojoy.base.Octets getPasswordOctets() { // 安全锁密码,为空表示没有设置安全锁
			return com.locojoy.base.Octets.wrap(getPassword(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getUnlocktime() { // 开始解锁的时间,为0表示没解锁
			return unlocktime;
		}

		@Override
		public long getForceunlocktime() { // 开始强行解锁的时间,为0表示没有申请强制解锁
			return forceunlocktime;
		}

		@Override
		public int getErrortimes() { // 连续输错密码的次数
			return errortimes;
		}

		@Override
		public long getFullerrortime() { // 连续输错密码达上限的时间
			return fullerrortime;
		}

		@Override
		public void setPassword(String _v_) { // 安全锁密码,为空表示没有设置安全锁
			if (null == _v_)
				throw new NullPointerException();
			password = _v_;
		}

		@Override
		public void setPasswordOctets(com.locojoy.base.Octets _v_) { // 安全锁密码,为空表示没有设置安全锁
			this.setPassword(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setUnlocktime(long _v_) { // 开始解锁的时间,为0表示没解锁
			unlocktime = _v_;
		}

		@Override
		public void setForceunlocktime(long _v_) { // 开始强行解锁的时间,为0表示没有申请强制解锁
			forceunlocktime = _v_;
		}

		@Override
		public void setErrortimes(int _v_) { // 连续输错密码的次数
			errortimes = _v_;
		}

		@Override
		public void setFullerrortime(long _v_) { // 连续输错密码达上限的时间
			fullerrortime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleLockInfo.Data)) return false;
			RoleLockInfo.Data _o_ = (RoleLockInfo.Data) _o1_;
			if (!password.equals(_o_.password)) return false;
			if (unlocktime != _o_.unlocktime) return false;
			if (forceunlocktime != _o_.forceunlocktime) return false;
			if (errortimes != _o_.errortimes) return false;
			if (fullerrortime != _o_.fullerrortime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += password.hashCode();
			_h_ += unlocktime;
			_h_ += forceunlocktime;
			_h_ += errortimes;
			_h_ += fullerrortime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(password).append("'");
			_sb_.append(",");
			_sb_.append(unlocktime);
			_sb_.append(",");
			_sb_.append(forceunlocktime);
			_sb_.append(",");
			_sb_.append(errortimes);
			_sb_.append(",");
			_sb_.append(fullerrortime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

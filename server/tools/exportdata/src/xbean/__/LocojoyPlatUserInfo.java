
package xbean.__;

import com.goldhuman.Common.Marshal.OctetsStream;
import com.goldhuman.Common.Marshal.MarshalException;

public final class LocojoyPlatUserInfo extends xdb.XBean implements xbean.LocojoyPlatUserInfo {
	private int userid; // 游戏里用的userid
	private long userinfoid; // users表里的key

	@Override
	public void _reset_unsafe_() {
		userid = 0;
		userinfoid = 0L;
	}

	LocojoyPlatUserInfo(int __, xdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public LocojoyPlatUserInfo() {
		this(0, null, null);
	}

	public LocojoyPlatUserInfo(LocojoyPlatUserInfo _o_) {
		this(_o_, null, null);
	}

	LocojoyPlatUserInfo(xbean.LocojoyPlatUserInfo _o1_, xdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LocojoyPlatUserInfo) assign((LocojoyPlatUserInfo)_o1_);
		else if (_o1_ instanceof LocojoyPlatUserInfo.Data) assign((LocojoyPlatUserInfo.Data)_o1_);
		else if (_o1_ instanceof LocojoyPlatUserInfo.Const) assign(((LocojoyPlatUserInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LocojoyPlatUserInfo _o_) {
		_o_._xdb_verify_unsafe_();
		userid = _o_.userid;
		userinfoid = _o_.userinfoid;
	}

	private void assign(LocojoyPlatUserInfo.Data _o_) {
		userid = _o_.userid;
		userinfoid = _o_.userinfoid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(userid);
		_os_.marshal(userinfoid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		userid = _os_.unmarshal_int();
		userinfoid = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.LocojoyPlatUserInfo copy() {
		_xdb_verify_unsafe_();
		return new LocojoyPlatUserInfo(this);
	}

	@Override
	public xbean.LocojoyPlatUserInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LocojoyPlatUserInfo toBean() {
		_xdb_verify_unsafe_();
		return new LocojoyPlatUserInfo(this); // same as copy()
	}

	@Override
	public xbean.LocojoyPlatUserInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LocojoyPlatUserInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public xdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getUserid() { // 游戏里用的userid
		_xdb_verify_unsafe_();
		return userid;
	}

	@Override
	public long getUserinfoid() { // users表里的key
		_xdb_verify_unsafe_();
		return userinfoid;
	}

	@Override
	public void setUserid(int _v_) { // 游戏里用的userid
		_xdb_verify_unsafe_();
		xdb.Logs.logIf(new xdb.LogKey(this, "userid") {
			protected xdb.Log create() {
				return new xdb.logs.LogInt(this, userid) {
					public void rollback() { userid = _xdb_saved; }
				};}});
		userid = _v_;
	}

	@Override
	public void setUserinfoid(long _v_) { // users表里的key
		_xdb_verify_unsafe_();
		xdb.Logs.logIf(new xdb.LogKey(this, "userinfoid") {
			protected xdb.Log create() {
				return new xdb.logs.LogLong(this, userinfoid) {
					public void rollback() { userinfoid = _xdb_saved; }
				};}});
		userinfoid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LocojoyPlatUserInfo _o_ = null;
		if ( _o1_ instanceof LocojoyPlatUserInfo ) _o_ = (LocojoyPlatUserInfo)_o1_;
		else if ( _o1_ instanceof LocojoyPlatUserInfo.Const ) _o_ = ((LocojoyPlatUserInfo.Const)_o1_).nThis();
		else return false;
		if (userid != _o_.userid) return false;
		if (userinfoid != _o_.userinfoid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += userid;
		_h_ += userinfoid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid);
		_sb_.append(",");
		_sb_.append(userinfoid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public xdb.logs.Listenable newListenable() {
		xdb.logs.ListenableBean lb = new xdb.logs.ListenableBean();
		lb.add(new xdb.logs.ListenableChanged().setVarName("userid"));
		lb.add(new xdb.logs.ListenableChanged().setVarName("userinfoid"));
		return lb;
	}

	private class Const implements xbean.LocojoyPlatUserInfo {
		LocojoyPlatUserInfo nThis() {
			return LocojoyPlatUserInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LocojoyPlatUserInfo copy() {
			return LocojoyPlatUserInfo.this.copy();
		}

		@Override
		public xbean.LocojoyPlatUserInfo toData() {
			return LocojoyPlatUserInfo.this.toData();
		}

		public xbean.LocojoyPlatUserInfo toBean() {
			return LocojoyPlatUserInfo.this.toBean();
		}

		@Override
		public xbean.LocojoyPlatUserInfo toDataIf() {
			return LocojoyPlatUserInfo.this.toDataIf();
		}

		public xbean.LocojoyPlatUserInfo toBeanIf() {
			return LocojoyPlatUserInfo.this.toBeanIf();
		}

		@Override
		public int getUserid() { // 游戏里用的userid
			_xdb_verify_unsafe_();
			return userid;
		}

		@Override
		public long getUserinfoid() { // users表里的key
			_xdb_verify_unsafe_();
			return userinfoid;
		}

		@Override
		public void setUserid(int _v_) { // 游戏里用的userid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUserinfoid(long _v_) { // users表里的key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public xdb.Bean toConst() {
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
			return LocojoyPlatUserInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LocojoyPlatUserInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public xdb.Bean xdbParent() {
			return LocojoyPlatUserInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LocojoyPlatUserInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LocojoyPlatUserInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LocojoyPlatUserInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LocojoyPlatUserInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LocojoyPlatUserInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return LocojoyPlatUserInfo.this.toString();
		}

	}

	public static final class Data implements xbean.LocojoyPlatUserInfo {
		private int userid; // 游戏里用的userid
		private long userinfoid; // users表里的key

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.LocojoyPlatUserInfo _o1_) {
			if (_o1_ instanceof LocojoyPlatUserInfo) assign((LocojoyPlatUserInfo)_o1_);
			else if (_o1_ instanceof LocojoyPlatUserInfo.Data) assign((LocojoyPlatUserInfo.Data)_o1_);
			else if (_o1_ instanceof LocojoyPlatUserInfo.Const) assign(((LocojoyPlatUserInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LocojoyPlatUserInfo _o_) {
			userid = _o_.userid;
			userinfoid = _o_.userinfoid;
		}

		private void assign(LocojoyPlatUserInfo.Data _o_) {
			userid = _o_.userid;
			userinfoid = _o_.userinfoid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(userid);
			_os_.marshal(userinfoid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			userid = _os_.unmarshal_int();
			userinfoid = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.LocojoyPlatUserInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.LocojoyPlatUserInfo toData() {
			return new Data(this);
		}

		public xbean.LocojoyPlatUserInfo toBean() {
			return new LocojoyPlatUserInfo(this, null, null);
		}

		@Override
		public xbean.LocojoyPlatUserInfo toDataIf() {
			return this;
		}

		public xbean.LocojoyPlatUserInfo toBeanIf() {
			return new LocojoyPlatUserInfo(this, null, null);
		}

		// xdb.Bean interface. Data Unsupported
		public boolean xdbManaged() { throw new UnsupportedOperationException(); }
		public xdb.Bean xdbParent() { throw new UnsupportedOperationException(); }
		public String xdbVarname()  { throw new UnsupportedOperationException(); }
		public Long    xdbObjId()   { throw new UnsupportedOperationException(); }
		public xdb.Bean toConst()   { throw new UnsupportedOperationException(); }
		public boolean isConst()    { return false; }
		public boolean isData()     { return true; }

		@Override
		public int getUserid() { // 游戏里用的userid
			return userid;
		}

		@Override
		public long getUserinfoid() { // users表里的key
			return userinfoid;
		}

		@Override
		public void setUserid(int _v_) { // 游戏里用的userid
			userid = _v_;
		}

		@Override
		public void setUserinfoid(long _v_) { // users表里的key
			userinfoid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LocojoyPlatUserInfo.Data)) return false;
			LocojoyPlatUserInfo.Data _o_ = (LocojoyPlatUserInfo.Data) _o1_;
			if (userid != _o_.userid) return false;
			if (userinfoid != _o_.userinfoid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += userid;
			_h_ += userinfoid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(userid);
			_sb_.append(",");
			_sb_.append(userinfoid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

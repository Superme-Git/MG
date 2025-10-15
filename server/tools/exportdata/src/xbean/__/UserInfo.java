
package xbean.__;

import com.goldhuman.Common.Marshal.OctetsStream;
import com.goldhuman.Common.Marshal.MarshalException;

public final class UserInfo extends xdb.XBean implements xbean.UserInfo {
	private String plat; // 
	private String uid; // 实际就是username，account，platformUID

	@Override
	public void _reset_unsafe_() {
		plat = "";
		uid = "";
	}

	UserInfo(int __, xdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		plat = "";
		uid = "";
	}

	public UserInfo() {
		this(0, null, null);
	}

	public UserInfo(UserInfo _o_) {
		this(_o_, null, null);
	}

	UserInfo(xbean.UserInfo _o1_, xdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof UserInfo) assign((UserInfo)_o1_);
		else if (_o1_ instanceof UserInfo.Data) assign((UserInfo.Data)_o1_);
		else if (_o1_ instanceof UserInfo.Const) assign(((UserInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(UserInfo _o_) {
		_o_._xdb_verify_unsafe_();
		plat = _o_.plat;
		uid = _o_.uid;
	}

	private void assign(UserInfo.Data _o_) {
		plat = _o_.plat;
		uid = _o_.uid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(plat, xdb.Const.IO_CHARSET);
		_os_.marshal(uid, xdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		plat = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
		uid = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.UserInfo copy() {
		_xdb_verify_unsafe_();
		return new UserInfo(this);
	}

	@Override
	public xbean.UserInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.UserInfo toBean() {
		_xdb_verify_unsafe_();
		return new UserInfo(this); // same as copy()
	}

	@Override
	public xbean.UserInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.UserInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public xdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getPlat() { // 
		_xdb_verify_unsafe_();
		return plat;
	}

	@Override
	public com.goldhuman.Common.Octets getPlatOctets() { // 
		_xdb_verify_unsafe_();
		return com.goldhuman.Common.Octets.wrap(getPlat(), xdb.Const.IO_CHARSET);
	}

	@Override
	public String getUid() { // 实际就是username，account，platformUID
		_xdb_verify_unsafe_();
		return uid;
	}

	@Override
	public com.goldhuman.Common.Octets getUidOctets() { // 实际就是username，account，platformUID
		_xdb_verify_unsafe_();
		return com.goldhuman.Common.Octets.wrap(getUid(), xdb.Const.IO_CHARSET);
	}

	@Override
	public void setPlat(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		xdb.Logs.logIf(new xdb.LogKey(this, "plat") {
			protected xdb.Log create() {
				return new xdb.logs.LogString(this, plat) {
					public void rollback() { plat = _xdb_saved; }
				};}});
		plat = _v_;
	}

	@Override
	public void setPlatOctets(com.goldhuman.Common.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setPlat(_v_.getString(xdb.Const.IO_CHARSET));
	}

	@Override
	public void setUid(String _v_) { // 实际就是username，account，platformUID
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		xdb.Logs.logIf(new xdb.LogKey(this, "uid") {
			protected xdb.Log create() {
				return new xdb.logs.LogString(this, uid) {
					public void rollback() { uid = _xdb_saved; }
				};}});
		uid = _v_;
	}

	@Override
	public void setUidOctets(com.goldhuman.Common.Octets _v_) { // 实际就是username，account，platformUID
		_xdb_verify_unsafe_();
		this.setUid(_v_.getString(xdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		UserInfo _o_ = null;
		if ( _o1_ instanceof UserInfo ) _o_ = (UserInfo)_o1_;
		else if ( _o1_ instanceof UserInfo.Const ) _o_ = ((UserInfo.Const)_o1_).nThis();
		else return false;
		if (!plat.equals(_o_.plat)) return false;
		if (!uid.equals(_o_.uid)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += plat.hashCode();
		_h_ += uid.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(plat).append("'");
		_sb_.append(",");
		_sb_.append("'").append(uid).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public xdb.logs.Listenable newListenable() {
		xdb.logs.ListenableBean lb = new xdb.logs.ListenableBean();
		lb.add(new xdb.logs.ListenableChanged().setVarName("plat"));
		lb.add(new xdb.logs.ListenableChanged().setVarName("uid"));
		return lb;
	}

	private class Const implements xbean.UserInfo {
		UserInfo nThis() {
			return UserInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.UserInfo copy() {
			return UserInfo.this.copy();
		}

		@Override
		public xbean.UserInfo toData() {
			return UserInfo.this.toData();
		}

		public xbean.UserInfo toBean() {
			return UserInfo.this.toBean();
		}

		@Override
		public xbean.UserInfo toDataIf() {
			return UserInfo.this.toDataIf();
		}

		public xbean.UserInfo toBeanIf() {
			return UserInfo.this.toBeanIf();
		}

		@Override
		public String getPlat() { // 
			_xdb_verify_unsafe_();
			return plat;
		}

		@Override
		public com.goldhuman.Common.Octets getPlatOctets() { // 
			_xdb_verify_unsafe_();
			return UserInfo.this.getPlatOctets();
		}

		@Override
		public String getUid() { // 实际就是username，account，platformUID
			_xdb_verify_unsafe_();
			return uid;
		}

		@Override
		public com.goldhuman.Common.Octets getUidOctets() { // 实际就是username，account，platformUID
			_xdb_verify_unsafe_();
			return UserInfo.this.getUidOctets();
		}

		@Override
		public void setPlat(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatOctets(com.goldhuman.Common.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUid(String _v_) { // 实际就是username，account，platformUID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUidOctets(com.goldhuman.Common.Octets _v_) { // 实际就是username，account，platformUID
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
			return UserInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return UserInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public xdb.Bean xdbParent() {
			return UserInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return UserInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return UserInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return UserInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return UserInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return UserInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return UserInfo.this.toString();
		}

	}

	public static final class Data implements xbean.UserInfo {
		private String plat; // 
		private String uid; // 实际就是username，account，platformUID

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			plat = "";
			uid = "";
		}

		Data(xbean.UserInfo _o1_) {
			if (_o1_ instanceof UserInfo) assign((UserInfo)_o1_);
			else if (_o1_ instanceof UserInfo.Data) assign((UserInfo.Data)_o1_);
			else if (_o1_ instanceof UserInfo.Const) assign(((UserInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(UserInfo _o_) {
			plat = _o_.plat;
			uid = _o_.uid;
		}

		private void assign(UserInfo.Data _o_) {
			plat = _o_.plat;
			uid = _o_.uid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(plat, xdb.Const.IO_CHARSET);
			_os_.marshal(uid, xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			plat = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			uid = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.UserInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.UserInfo toData() {
			return new Data(this);
		}

		public xbean.UserInfo toBean() {
			return new UserInfo(this, null, null);
		}

		@Override
		public xbean.UserInfo toDataIf() {
			return this;
		}

		public xbean.UserInfo toBeanIf() {
			return new UserInfo(this, null, null);
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
		public String getPlat() { // 
			return plat;
		}

		@Override
		public com.goldhuman.Common.Octets getPlatOctets() { // 
			return com.goldhuman.Common.Octets.wrap(getPlat(), xdb.Const.IO_CHARSET);
		}

		@Override
		public String getUid() { // 实际就是username，account，platformUID
			return uid;
		}

		@Override
		public com.goldhuman.Common.Octets getUidOctets() { // 实际就是username，account，platformUID
			return com.goldhuman.Common.Octets.wrap(getUid(), xdb.Const.IO_CHARSET);
		}

		@Override
		public void setPlat(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			plat = _v_;
		}

		@Override
		public void setPlatOctets(com.goldhuman.Common.Octets _v_) { // 
			this.setPlat(_v_.getString(xdb.Const.IO_CHARSET));
		}

		@Override
		public void setUid(String _v_) { // 实际就是username，account，platformUID
			if (null == _v_)
				throw new NullPointerException();
			uid = _v_;
		}

		@Override
		public void setUidOctets(com.goldhuman.Common.Octets _v_) { // 实际就是username，account，platformUID
			this.setUid(_v_.getString(xdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof UserInfo.Data)) return false;
			UserInfo.Data _o_ = (UserInfo.Data) _o1_;
			if (!plat.equals(_o_.plat)) return false;
			if (!uid.equals(_o_.uid)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += plat.hashCode();
			_h_ += uid.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(plat).append("'");
			_sb_.append(",");
			_sb_.append("'").append(uid).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

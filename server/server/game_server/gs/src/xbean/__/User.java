
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class User extends mkdb.XBean implements xbean.User {
	private long prevloginroleid; // 上一次登录的角色
	private java.util.ArrayList<Long> idlist; // 用户的角色列表 value是roleid
	private long createtime; // 帐号第一次进入游戏的时间
	private int isfirst; // 是否第一次登陆0第一次   1不是第一次

	@Override
	public void _reset_unsafe_() {
		prevloginroleid = 0L;
		idlist.clear();
		createtime = 0L;
		isfirst = 0;
	}

	User(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		idlist = new java.util.ArrayList<Long>();
		isfirst = 0;
	}

	public User() {
		this(0, null, null);
	}

	public User(User _o_) {
		this(_o_, null, null);
	}

	User(xbean.User _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof User) assign((User)_o1_);
		else if (_o1_ instanceof User.Data) assign((User.Data)_o1_);
		else if (_o1_ instanceof User.Const) assign(((User.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(User _o_) {
		_o_._xdb_verify_unsafe_();
		prevloginroleid = _o_.prevloginroleid;
		idlist = new java.util.ArrayList<Long>();
		idlist.addAll(_o_.idlist);
		createtime = _o_.createtime;
		isfirst = _o_.isfirst;
	}

	private void assign(User.Data _o_) {
		prevloginroleid = _o_.prevloginroleid;
		idlist = new java.util.ArrayList<Long>();
		idlist.addAll(_o_.idlist);
		createtime = _o_.createtime;
		isfirst = _o_.isfirst;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(prevloginroleid);
		_os_.compact_uint32(idlist.size());
		for (Long _v_ : idlist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(createtime);
		_os_.marshal(isfirst);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		prevloginroleid = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			idlist.add(_v_);
		}
		createtime = _os_.unmarshal_long();
		isfirst = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.User copy() {
		_xdb_verify_unsafe_();
		return new User(this);
	}

	@Override
	public xbean.User toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.User toBean() {
		_xdb_verify_unsafe_();
		return new User(this); // same as copy()
	}

	@Override
	public xbean.User toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.User toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getPrevloginroleid() { // 上一次登录的角色
		_xdb_verify_unsafe_();
		return prevloginroleid;
	}

	@Override
	public java.util.List<Long> getIdlist() { // 用户的角色列表 value是roleid
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "idlist"), idlist);
	}

	public java.util.List<Long> getIdlistAsData() { // 用户的角色列表 value是roleid
		_xdb_verify_unsafe_();
		java.util.List<Long> idlist;
		User _o_ = this;
		idlist = new java.util.ArrayList<Long>();
		idlist.addAll(_o_.idlist);
		return idlist;
	}

	@Override
	public long getCreatetime() { // 帐号第一次进入游戏的时间
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public int getIsfirst() { // 是否第一次登陆0第一次   1不是第一次
		_xdb_verify_unsafe_();
		return isfirst;
	}

	@Override
	public void setPrevloginroleid(long _v_) { // 上一次登录的角色
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "prevloginroleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, prevloginroleid) {
					public void rollback() { prevloginroleid = _xdb_saved; }
				};}});
		prevloginroleid = _v_;
	}

	@Override
	public void setCreatetime(long _v_) { // 帐号第一次进入游戏的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public void setIsfirst(int _v_) { // 是否第一次登陆0第一次   1不是第一次
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isfirst") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, isfirst) {
					public void rollback() { isfirst = _xdb_saved; }
				};}});
		isfirst = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		User _o_ = null;
		if ( _o1_ instanceof User ) _o_ = (User)_o1_;
		else if ( _o1_ instanceof User.Const ) _o_ = ((User.Const)_o1_).nThis();
		else return false;
		if (prevloginroleid != _o_.prevloginroleid) return false;
		if (!idlist.equals(_o_.idlist)) return false;
		if (createtime != _o_.createtime) return false;
		if (isfirst != _o_.isfirst) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += prevloginroleid;
		_h_ += idlist.hashCode();
		_h_ += createtime;
		_h_ += isfirst;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prevloginroleid);
		_sb_.append(",");
		_sb_.append(idlist);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append(isfirst);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("prevloginroleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("idlist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isfirst"));
		return lb;
	}

	private class Const implements xbean.User {
		User nThis() {
			return User.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.User copy() {
			return User.this.copy();
		}

		@Override
		public xbean.User toData() {
			return User.this.toData();
		}

		public xbean.User toBean() {
			return User.this.toBean();
		}

		@Override
		public xbean.User toDataIf() {
			return User.this.toDataIf();
		}

		public xbean.User toBeanIf() {
			return User.this.toBeanIf();
		}

		@Override
		public long getPrevloginroleid() { // 上一次登录的角色
			_xdb_verify_unsafe_();
			return prevloginroleid;
		}

		@Override
		public java.util.List<Long> getIdlist() { // 用户的角色列表 value是roleid
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(idlist);
		}

		public java.util.List<Long> getIdlistAsData() { // 用户的角色列表 value是roleid
			_xdb_verify_unsafe_();
			java.util.List<Long> idlist;
			User _o_ = User.this;
		idlist = new java.util.ArrayList<Long>();
		idlist.addAll(_o_.idlist);
			return idlist;
		}

		@Override
		public long getCreatetime() { // 帐号第一次进入游戏的时间
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public int getIsfirst() { // 是否第一次登陆0第一次   1不是第一次
			_xdb_verify_unsafe_();
			return isfirst;
		}

		@Override
		public void setPrevloginroleid(long _v_) { // 上一次登录的角色
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 帐号第一次进入游戏的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsfirst(int _v_) { // 是否第一次登陆0第一次   1不是第一次
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
			return User.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return User.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return User.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return User.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return User.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return User.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return User.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return User.this.hashCode();
		}

		@Override
		public String toString() {
			return User.this.toString();
		}

	}

	public static final class Data implements xbean.User {
		private long prevloginroleid; // 上一次登录的角色
		private java.util.ArrayList<Long> idlist; // 用户的角色列表 value是roleid
		private long createtime; // 帐号第一次进入游戏的时间
		private int isfirst; // 是否第一次登陆0第一次   1不是第一次

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			idlist = new java.util.ArrayList<Long>();
			isfirst = 0;
		}

		Data(xbean.User _o1_) {
			if (_o1_ instanceof User) assign((User)_o1_);
			else if (_o1_ instanceof User.Data) assign((User.Data)_o1_);
			else if (_o1_ instanceof User.Const) assign(((User.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(User _o_) {
			prevloginroleid = _o_.prevloginroleid;
			idlist = new java.util.ArrayList<Long>();
			idlist.addAll(_o_.idlist);
			createtime = _o_.createtime;
			isfirst = _o_.isfirst;
		}

		private void assign(User.Data _o_) {
			prevloginroleid = _o_.prevloginroleid;
			idlist = new java.util.ArrayList<Long>();
			idlist.addAll(_o_.idlist);
			createtime = _o_.createtime;
			isfirst = _o_.isfirst;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(prevloginroleid);
			_os_.compact_uint32(idlist.size());
			for (Long _v_ : idlist) {
				_os_.marshal(_v_);
			}
			_os_.marshal(createtime);
			_os_.marshal(isfirst);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			prevloginroleid = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				idlist.add(_v_);
			}
			createtime = _os_.unmarshal_long();
			isfirst = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.User copy() {
			return new Data(this);
		}

		@Override
		public xbean.User toData() {
			return new Data(this);
		}

		public xbean.User toBean() {
			return new User(this, null, null);
		}

		@Override
		public xbean.User toDataIf() {
			return this;
		}

		public xbean.User toBeanIf() {
			return new User(this, null, null);
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
		public long getPrevloginroleid() { // 上一次登录的角色
			return prevloginroleid;
		}

		@Override
		public java.util.List<Long> getIdlist() { // 用户的角色列表 value是roleid
			return idlist;
		}

		@Override
		public java.util.List<Long> getIdlistAsData() { // 用户的角色列表 value是roleid
			return idlist;
		}

		@Override
		public long getCreatetime() { // 帐号第一次进入游戏的时间
			return createtime;
		}

		@Override
		public int getIsfirst() { // 是否第一次登陆0第一次   1不是第一次
			return isfirst;
		}

		@Override
		public void setPrevloginroleid(long _v_) { // 上一次登录的角色
			prevloginroleid = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 帐号第一次进入游戏的时间
			createtime = _v_;
		}

		@Override
		public void setIsfirst(int _v_) { // 是否第一次登陆0第一次   1不是第一次
			isfirst = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof User.Data)) return false;
			User.Data _o_ = (User.Data) _o1_;
			if (prevloginroleid != _o_.prevloginroleid) return false;
			if (!idlist.equals(_o_.idlist)) return false;
			if (createtime != _o_.createtime) return false;
			if (isfirst != _o_.isfirst) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += prevloginroleid;
			_h_ += idlist.hashCode();
			_h_ += createtime;
			_h_ += isfirst;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(prevloginroleid);
			_sb_.append(",");
			_sb_.append(idlist);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append(isfirst);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

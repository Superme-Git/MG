
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class UserPunish extends mkdb.XBean implements xbean.UserPunish {
	private int userid; // 用户id
	private java.util.LinkedList<xbean.PunishRecord> records; // 封禁记录
	private long releasetime; // 解封的时间,gacd用
	private int waiguatimes; // 使用外挂的次数,gacd用
	private long sendmsgtime; // 解封后发消息给玩家的时间,如果没有发过消息,为0

	@Override
	public void _reset_unsafe_() {
		userid = 0;
		records.clear();
		releasetime = 0L;
		waiguatimes = 0;
		sendmsgtime = 0L;
	}

	UserPunish(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.PunishRecord>();
	}

	public UserPunish() {
		this(0, null, null);
	}

	public UserPunish(UserPunish _o_) {
		this(_o_, null, null);
	}

	UserPunish(xbean.UserPunish _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof UserPunish) assign((UserPunish)_o1_);
		else if (_o1_ instanceof UserPunish.Data) assign((UserPunish.Data)_o1_);
		else if (_o1_ instanceof UserPunish.Const) assign(((UserPunish.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(UserPunish _o_) {
		_o_._xdb_verify_unsafe_();
		userid = _o_.userid;
		records = new java.util.LinkedList<xbean.PunishRecord>();
		for (xbean.PunishRecord _v_ : _o_.records)
			records.add(new PunishRecord(_v_, this, "records"));
		releasetime = _o_.releasetime;
		waiguatimes = _o_.waiguatimes;
		sendmsgtime = _o_.sendmsgtime;
	}

	private void assign(UserPunish.Data _o_) {
		userid = _o_.userid;
		records = new java.util.LinkedList<xbean.PunishRecord>();
		for (xbean.PunishRecord _v_ : _o_.records)
			records.add(new PunishRecord(_v_, this, "records"));
		releasetime = _o_.releasetime;
		waiguatimes = _o_.waiguatimes;
		sendmsgtime = _o_.sendmsgtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(userid);
		_os_.compact_uint32(records.size());
		for (xbean.PunishRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		_os_.marshal(releasetime);
		_os_.marshal(waiguatimes);
		_os_.marshal(sendmsgtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		userid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.PunishRecord _v_ = new PunishRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		releasetime = _os_.unmarshal_long();
		waiguatimes = _os_.unmarshal_int();
		sendmsgtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.UserPunish copy() {
		_xdb_verify_unsafe_();
		return new UserPunish(this);
	}

	@Override
	public xbean.UserPunish toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.UserPunish toBean() {
		_xdb_verify_unsafe_();
		return new UserPunish(this); // same as copy()
	}

	@Override
	public xbean.UserPunish toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.UserPunish toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getUserid() { // 用户id
		_xdb_verify_unsafe_();
		return userid;
	}

	@Override
	public java.util.List<xbean.PunishRecord> getRecords() { // 封禁记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.PunishRecord> getRecordsAsData() { // 封禁记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.PunishRecord> records;
		UserPunish _o_ = this;
		records = new java.util.LinkedList<xbean.PunishRecord>();
		for (xbean.PunishRecord _v_ : _o_.records)
			records.add(new PunishRecord.Data(_v_));
		return records;
	}

	@Override
	public long getReleasetime() { // 解封的时间,gacd用
		_xdb_verify_unsafe_();
		return releasetime;
	}

	@Override
	public int getWaiguatimes() { // 使用外挂的次数,gacd用
		_xdb_verify_unsafe_();
		return waiguatimes;
	}

	@Override
	public long getSendmsgtime() { // 解封后发消息给玩家的时间,如果没有发过消息,为0
		_xdb_verify_unsafe_();
		return sendmsgtime;
	}

	@Override
	public void setUserid(int _v_) { // 用户id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "userid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, userid) {
					public void rollback() { userid = _xdb_saved; }
				};}});
		userid = _v_;
	}

	@Override
	public void setReleasetime(long _v_) { // 解封的时间,gacd用
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "releasetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, releasetime) {
					public void rollback() { releasetime = _xdb_saved; }
				};}});
		releasetime = _v_;
	}

	@Override
	public void setWaiguatimes(int _v_) { // 使用外挂的次数,gacd用
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "waiguatimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, waiguatimes) {
					public void rollback() { waiguatimes = _xdb_saved; }
				};}});
		waiguatimes = _v_;
	}

	@Override
	public void setSendmsgtime(long _v_) { // 解封后发消息给玩家的时间,如果没有发过消息,为0
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendmsgtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sendmsgtime) {
					public void rollback() { sendmsgtime = _xdb_saved; }
				};}});
		sendmsgtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		UserPunish _o_ = null;
		if ( _o1_ instanceof UserPunish ) _o_ = (UserPunish)_o1_;
		else if ( _o1_ instanceof UserPunish.Const ) _o_ = ((UserPunish.Const)_o1_).nThis();
		else return false;
		if (userid != _o_.userid) return false;
		if (!records.equals(_o_.records)) return false;
		if (releasetime != _o_.releasetime) return false;
		if (waiguatimes != _o_.waiguatimes) return false;
		if (sendmsgtime != _o_.sendmsgtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += userid;
		_h_ += records.hashCode();
		_h_ += releasetime;
		_h_ += waiguatimes;
		_h_ += sendmsgtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid);
		_sb_.append(",");
		_sb_.append(records);
		_sb_.append(",");
		_sb_.append(releasetime);
		_sb_.append(",");
		_sb_.append(waiguatimes);
		_sb_.append(",");
		_sb_.append(sendmsgtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("userid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("records"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("releasetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("waiguatimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendmsgtime"));
		return lb;
	}

	private class Const implements xbean.UserPunish {
		UserPunish nThis() {
			return UserPunish.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.UserPunish copy() {
			return UserPunish.this.copy();
		}

		@Override
		public xbean.UserPunish toData() {
			return UserPunish.this.toData();
		}

		public xbean.UserPunish toBean() {
			return UserPunish.this.toBean();
		}

		@Override
		public xbean.UserPunish toDataIf() {
			return UserPunish.this.toDataIf();
		}

		public xbean.UserPunish toBeanIf() {
			return UserPunish.this.toBeanIf();
		}

		@Override
		public int getUserid() { // 用户id
			_xdb_verify_unsafe_();
			return userid;
		}

		@Override
		public java.util.List<xbean.PunishRecord> getRecords() { // 封禁记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.PunishRecord> getRecordsAsData() { // 封禁记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.PunishRecord> records;
			UserPunish _o_ = UserPunish.this;
		records = new java.util.LinkedList<xbean.PunishRecord>();
		for (xbean.PunishRecord _v_ : _o_.records)
			records.add(new PunishRecord.Data(_v_));
			return records;
		}

		@Override
		public long getReleasetime() { // 解封的时间,gacd用
			_xdb_verify_unsafe_();
			return releasetime;
		}

		@Override
		public int getWaiguatimes() { // 使用外挂的次数,gacd用
			_xdb_verify_unsafe_();
			return waiguatimes;
		}

		@Override
		public long getSendmsgtime() { // 解封后发消息给玩家的时间,如果没有发过消息,为0
			_xdb_verify_unsafe_();
			return sendmsgtime;
		}

		@Override
		public void setUserid(int _v_) { // 用户id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReleasetime(long _v_) { // 解封的时间,gacd用
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWaiguatimes(int _v_) { // 使用外挂的次数,gacd用
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSendmsgtime(long _v_) { // 解封后发消息给玩家的时间,如果没有发过消息,为0
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
			return UserPunish.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return UserPunish.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return UserPunish.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return UserPunish.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return UserPunish.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return UserPunish.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return UserPunish.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return UserPunish.this.hashCode();
		}

		@Override
		public String toString() {
			return UserPunish.this.toString();
		}

	}

	public static final class Data implements xbean.UserPunish {
		private int userid; // 用户id
		private java.util.LinkedList<xbean.PunishRecord> records; // 封禁记录
		private long releasetime; // 解封的时间,gacd用
		private int waiguatimes; // 使用外挂的次数,gacd用
		private long sendmsgtime; // 解封后发消息给玩家的时间,如果没有发过消息,为0

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.PunishRecord>();
		}

		Data(xbean.UserPunish _o1_) {
			if (_o1_ instanceof UserPunish) assign((UserPunish)_o1_);
			else if (_o1_ instanceof UserPunish.Data) assign((UserPunish.Data)_o1_);
			else if (_o1_ instanceof UserPunish.Const) assign(((UserPunish.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(UserPunish _o_) {
			userid = _o_.userid;
			records = new java.util.LinkedList<xbean.PunishRecord>();
			for (xbean.PunishRecord _v_ : _o_.records)
				records.add(new PunishRecord.Data(_v_));
			releasetime = _o_.releasetime;
			waiguatimes = _o_.waiguatimes;
			sendmsgtime = _o_.sendmsgtime;
		}

		private void assign(UserPunish.Data _o_) {
			userid = _o_.userid;
			records = new java.util.LinkedList<xbean.PunishRecord>();
			for (xbean.PunishRecord _v_ : _o_.records)
				records.add(new PunishRecord.Data(_v_));
			releasetime = _o_.releasetime;
			waiguatimes = _o_.waiguatimes;
			sendmsgtime = _o_.sendmsgtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(userid);
			_os_.compact_uint32(records.size());
			for (xbean.PunishRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			_os_.marshal(releasetime);
			_os_.marshal(waiguatimes);
			_os_.marshal(sendmsgtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			userid = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.PunishRecord _v_ = xbean.Pod.newPunishRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			releasetime = _os_.unmarshal_long();
			waiguatimes = _os_.unmarshal_int();
			sendmsgtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.UserPunish copy() {
			return new Data(this);
		}

		@Override
		public xbean.UserPunish toData() {
			return new Data(this);
		}

		public xbean.UserPunish toBean() {
			return new UserPunish(this, null, null);
		}

		@Override
		public xbean.UserPunish toDataIf() {
			return this;
		}

		public xbean.UserPunish toBeanIf() {
			return new UserPunish(this, null, null);
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
		public int getUserid() { // 用户id
			return userid;
		}

		@Override
		public java.util.List<xbean.PunishRecord> getRecords() { // 封禁记录
			return records;
		}

		@Override
		public java.util.List<xbean.PunishRecord> getRecordsAsData() { // 封禁记录
			return records;
		}

		@Override
		public long getReleasetime() { // 解封的时间,gacd用
			return releasetime;
		}

		@Override
		public int getWaiguatimes() { // 使用外挂的次数,gacd用
			return waiguatimes;
		}

		@Override
		public long getSendmsgtime() { // 解封后发消息给玩家的时间,如果没有发过消息,为0
			return sendmsgtime;
		}

		@Override
		public void setUserid(int _v_) { // 用户id
			userid = _v_;
		}

		@Override
		public void setReleasetime(long _v_) { // 解封的时间,gacd用
			releasetime = _v_;
		}

		@Override
		public void setWaiguatimes(int _v_) { // 使用外挂的次数,gacd用
			waiguatimes = _v_;
		}

		@Override
		public void setSendmsgtime(long _v_) { // 解封后发消息给玩家的时间,如果没有发过消息,为0
			sendmsgtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof UserPunish.Data)) return false;
			UserPunish.Data _o_ = (UserPunish.Data) _o1_;
			if (userid != _o_.userid) return false;
			if (!records.equals(_o_.records)) return false;
			if (releasetime != _o_.releasetime) return false;
			if (waiguatimes != _o_.waiguatimes) return false;
			if (sendmsgtime != _o_.sendmsgtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += userid;
			_h_ += records.hashCode();
			_h_ += releasetime;
			_h_ += waiguatimes;
			_h_ += sendmsgtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(userid);
			_sb_.append(",");
			_sb_.append(records);
			_sb_.append(",");
			_sb_.append(releasetime);
			_sb_.append(",");
			_sb_.append(waiguatimes);
			_sb_.append(",");
			_sb_.append(sendmsgtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PunishRecord extends mkdb.XBean implements xbean.PunishRecord {
	private int userid; // 用户id
	private long roleid; // 角色id，roleid为0时是对整个账户的处罚
	private int type; // 惩罚类型，参考下面
	private long forbidtime; // 封禁的持续时间
	private int gmuserid; // 封禁操作的GM用户id
	private long optime; // 操作时间
	private String reason; // 原因

	@Override
	public void _reset_unsafe_() {
		userid = 0;
		roleid = 0L;
		type = 0;
		forbidtime = 0L;
		gmuserid = 0;
		optime = 0;
		reason = "";
	}

	PunishRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		optime = 0;
		reason = "";
	}

	public PunishRecord() {
		this(0, null, null);
	}

	public PunishRecord(PunishRecord _o_) {
		this(_o_, null, null);
	}

	PunishRecord(xbean.PunishRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PunishRecord) assign((PunishRecord)_o1_);
		else if (_o1_ instanceof PunishRecord.Data) assign((PunishRecord.Data)_o1_);
		else if (_o1_ instanceof PunishRecord.Const) assign(((PunishRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PunishRecord _o_) {
		_o_._xdb_verify_unsafe_();
		userid = _o_.userid;
		roleid = _o_.roleid;
		type = _o_.type;
		forbidtime = _o_.forbidtime;
		gmuserid = _o_.gmuserid;
		optime = _o_.optime;
		reason = _o_.reason;
	}

	private void assign(PunishRecord.Data _o_) {
		userid = _o_.userid;
		roleid = _o_.roleid;
		type = _o_.type;
		forbidtime = _o_.forbidtime;
		gmuserid = _o_.gmuserid;
		optime = _o_.optime;
		reason = _o_.reason;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(userid);
		_os_.marshal(roleid);
		_os_.marshal(type);
		_os_.marshal(forbidtime);
		_os_.marshal(gmuserid);
		_os_.marshal(optime);
		_os_.marshal(reason, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		userid = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		type = _os_.unmarshal_int();
		forbidtime = _os_.unmarshal_long();
		gmuserid = _os_.unmarshal_int();
		optime = _os_.unmarshal_long();
		reason = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.PunishRecord copy() {
		_xdb_verify_unsafe_();
		return new PunishRecord(this);
	}

	@Override
	public xbean.PunishRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PunishRecord toBean() {
		_xdb_verify_unsafe_();
		return new PunishRecord(this); // same as copy()
	}

	@Override
	public xbean.PunishRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PunishRecord toBeanIf() {
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
	public long getRoleid() { // 角色id，roleid为0时是对整个账户的处罚
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getType() { // 惩罚类型，参考下面
		_xdb_verify_unsafe_();
		return type;
	}

	@Override
	public long getForbidtime() { // 封禁的持续时间
		_xdb_verify_unsafe_();
		return forbidtime;
	}

	@Override
	public int getGmuserid() { // 封禁操作的GM用户id
		_xdb_verify_unsafe_();
		return gmuserid;
	}

	@Override
	public long getOptime() { // 操作时间
		_xdb_verify_unsafe_();
		return optime;
	}

	@Override
	public String getReason() { // 原因
		_xdb_verify_unsafe_();
		return reason;
	}

	@Override
	public com.locojoy.base.Octets getReasonOctets() { // 原因
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getReason(), mkdb.Const.IO_CHARSET);
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
	public void setRoleid(long _v_) { // 角色id，roleid为0时是对整个账户的处罚
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setType(int _v_) { // 惩罚类型，参考下面
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "type") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, type) {
					public void rollback() { type = _xdb_saved; }
				};}});
		type = _v_;
	}

	@Override
	public void setForbidtime(long _v_) { // 封禁的持续时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "forbidtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, forbidtime) {
					public void rollback() { forbidtime = _xdb_saved; }
				};}});
		forbidtime = _v_;
	}

	@Override
	public void setGmuserid(int _v_) { // 封禁操作的GM用户id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "gmuserid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, gmuserid) {
					public void rollback() { gmuserid = _xdb_saved; }
				};}});
		gmuserid = _v_;
	}

	@Override
	public void setOptime(long _v_) { // 操作时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "optime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, optime) {
					public void rollback() { optime = _xdb_saved; }
				};}});
		optime = _v_;
	}

	@Override
	public void setReason(String _v_) { // 原因
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
	public void setReasonOctets(com.locojoy.base.Octets _v_) { // 原因
		_xdb_verify_unsafe_();
		this.setReason(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PunishRecord _o_ = null;
		if ( _o1_ instanceof PunishRecord ) _o_ = (PunishRecord)_o1_;
		else if ( _o1_ instanceof PunishRecord.Const ) _o_ = ((PunishRecord.Const)_o1_).nThis();
		else return false;
		if (userid != _o_.userid) return false;
		if (roleid != _o_.roleid) return false;
		if (type != _o_.type) return false;
		if (forbidtime != _o_.forbidtime) return false;
		if (gmuserid != _o_.gmuserid) return false;
		if (optime != _o_.optime) return false;
		if (!reason.equals(_o_.reason)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += userid;
		_h_ += roleid;
		_h_ += type;
		_h_ += forbidtime;
		_h_ += gmuserid;
		_h_ += optime;
		_h_ += reason.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(type);
		_sb_.append(",");
		_sb_.append(forbidtime);
		_sb_.append(",");
		_sb_.append(gmuserid);
		_sb_.append(",");
		_sb_.append(optime);
		_sb_.append(",");
		_sb_.append("'").append(reason).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("userid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("type"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("forbidtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("gmuserid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("optime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("reason"));
		return lb;
	}

	private class Const implements xbean.PunishRecord {
		PunishRecord nThis() {
			return PunishRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PunishRecord copy() {
			return PunishRecord.this.copy();
		}

		@Override
		public xbean.PunishRecord toData() {
			return PunishRecord.this.toData();
		}

		public xbean.PunishRecord toBean() {
			return PunishRecord.this.toBean();
		}

		@Override
		public xbean.PunishRecord toDataIf() {
			return PunishRecord.this.toDataIf();
		}

		public xbean.PunishRecord toBeanIf() {
			return PunishRecord.this.toBeanIf();
		}

		@Override
		public int getUserid() { // 用户id
			_xdb_verify_unsafe_();
			return userid;
		}

		@Override
		public long getRoleid() { // 角色id，roleid为0时是对整个账户的处罚
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getType() { // 惩罚类型，参考下面
			_xdb_verify_unsafe_();
			return type;
		}

		@Override
		public long getForbidtime() { // 封禁的持续时间
			_xdb_verify_unsafe_();
			return forbidtime;
		}

		@Override
		public int getGmuserid() { // 封禁操作的GM用户id
			_xdb_verify_unsafe_();
			return gmuserid;
		}

		@Override
		public long getOptime() { // 操作时间
			_xdb_verify_unsafe_();
			return optime;
		}

		@Override
		public String getReason() { // 原因
			_xdb_verify_unsafe_();
			return reason;
		}

		@Override
		public com.locojoy.base.Octets getReasonOctets() { // 原因
			_xdb_verify_unsafe_();
			return PunishRecord.this.getReasonOctets();
		}

		@Override
		public void setUserid(int _v_) { // 用户id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 角色id，roleid为0时是对整个账户的处罚
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setType(int _v_) { // 惩罚类型，参考下面
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setForbidtime(long _v_) { // 封禁的持续时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGmuserid(int _v_) { // 封禁操作的GM用户id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOptime(long _v_) { // 操作时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReason(String _v_) { // 原因
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReasonOctets(com.locojoy.base.Octets _v_) { // 原因
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
			return PunishRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PunishRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PunishRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PunishRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PunishRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PunishRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PunishRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PunishRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return PunishRecord.this.toString();
		}

	}

	public static final class Data implements xbean.PunishRecord {
		private int userid; // 用户id
		private long roleid; // 角色id，roleid为0时是对整个账户的处罚
		private int type; // 惩罚类型，参考下面
		private long forbidtime; // 封禁的持续时间
		private int gmuserid; // 封禁操作的GM用户id
		private long optime; // 操作时间
		private String reason; // 原因

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			optime = 0;
			reason = "";
		}

		Data(xbean.PunishRecord _o1_) {
			if (_o1_ instanceof PunishRecord) assign((PunishRecord)_o1_);
			else if (_o1_ instanceof PunishRecord.Data) assign((PunishRecord.Data)_o1_);
			else if (_o1_ instanceof PunishRecord.Const) assign(((PunishRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PunishRecord _o_) {
			userid = _o_.userid;
			roleid = _o_.roleid;
			type = _o_.type;
			forbidtime = _o_.forbidtime;
			gmuserid = _o_.gmuserid;
			optime = _o_.optime;
			reason = _o_.reason;
		}

		private void assign(PunishRecord.Data _o_) {
			userid = _o_.userid;
			roleid = _o_.roleid;
			type = _o_.type;
			forbidtime = _o_.forbidtime;
			gmuserid = _o_.gmuserid;
			optime = _o_.optime;
			reason = _o_.reason;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(userid);
			_os_.marshal(roleid);
			_os_.marshal(type);
			_os_.marshal(forbidtime);
			_os_.marshal(gmuserid);
			_os_.marshal(optime);
			_os_.marshal(reason, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			userid = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			type = _os_.unmarshal_int();
			forbidtime = _os_.unmarshal_long();
			gmuserid = _os_.unmarshal_int();
			optime = _os_.unmarshal_long();
			reason = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.PunishRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.PunishRecord toData() {
			return new Data(this);
		}

		public xbean.PunishRecord toBean() {
			return new PunishRecord(this, null, null);
		}

		@Override
		public xbean.PunishRecord toDataIf() {
			return this;
		}

		public xbean.PunishRecord toBeanIf() {
			return new PunishRecord(this, null, null);
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
		public long getRoleid() { // 角色id，roleid为0时是对整个账户的处罚
			return roleid;
		}

		@Override
		public int getType() { // 惩罚类型，参考下面
			return type;
		}

		@Override
		public long getForbidtime() { // 封禁的持续时间
			return forbidtime;
		}

		@Override
		public int getGmuserid() { // 封禁操作的GM用户id
			return gmuserid;
		}

		@Override
		public long getOptime() { // 操作时间
			return optime;
		}

		@Override
		public String getReason() { // 原因
			return reason;
		}

		@Override
		public com.locojoy.base.Octets getReasonOctets() { // 原因
			return com.locojoy.base.Octets.wrap(getReason(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setUserid(int _v_) { // 用户id
			userid = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 角色id，roleid为0时是对整个账户的处罚
			roleid = _v_;
		}

		@Override
		public void setType(int _v_) { // 惩罚类型，参考下面
			type = _v_;
		}

		@Override
		public void setForbidtime(long _v_) { // 封禁的持续时间
			forbidtime = _v_;
		}

		@Override
		public void setGmuserid(int _v_) { // 封禁操作的GM用户id
			gmuserid = _v_;
		}

		@Override
		public void setOptime(long _v_) { // 操作时间
			optime = _v_;
		}

		@Override
		public void setReason(String _v_) { // 原因
			if (null == _v_)
				throw new NullPointerException();
			reason = _v_;
		}

		@Override
		public void setReasonOctets(com.locojoy.base.Octets _v_) { // 原因
			this.setReason(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PunishRecord.Data)) return false;
			PunishRecord.Data _o_ = (PunishRecord.Data) _o1_;
			if (userid != _o_.userid) return false;
			if (roleid != _o_.roleid) return false;
			if (type != _o_.type) return false;
			if (forbidtime != _o_.forbidtime) return false;
			if (gmuserid != _o_.gmuserid) return false;
			if (optime != _o_.optime) return false;
			if (!reason.equals(_o_.reason)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += userid;
			_h_ += roleid;
			_h_ += type;
			_h_ += forbidtime;
			_h_ += gmuserid;
			_h_ += optime;
			_h_ += reason.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(userid);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(type);
			_sb_.append(",");
			_sb_.append(forbidtime);
			_sb_.append(",");
			_sb_.append(gmuserid);
			_sb_.append(",");
			_sb_.append(optime);
			_sb_.append(",");
			_sb_.append("'").append(reason).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

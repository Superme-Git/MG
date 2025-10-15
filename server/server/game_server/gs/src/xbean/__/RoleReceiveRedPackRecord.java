
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleReceiveRedPackRecord extends mkdb.XBean implements xbean.RoleReceiveRedPackRecord {
	private int modeltype; // 红包类型
	private long sendroleid; // 发送红包角色Id
	private String redpackid; // 红包Id
	private long receivegold; // 收红包金币数量
	private long receivefushi; // 收红包符石数量
	private long receivetime; // 领取时间

	@Override
	public void _reset_unsafe_() {
		modeltype = 0;
		sendroleid = 0L;
		redpackid = "";
		receivegold = 0L;
		receivefushi = 0L;
		receivetime = 0L;
	}

	RoleReceiveRedPackRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		redpackid = "";
	}

	public RoleReceiveRedPackRecord() {
		this(0, null, null);
	}

	public RoleReceiveRedPackRecord(RoleReceiveRedPackRecord _o_) {
		this(_o_, null, null);
	}

	RoleReceiveRedPackRecord(xbean.RoleReceiveRedPackRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleReceiveRedPackRecord) assign((RoleReceiveRedPackRecord)_o1_);
		else if (_o1_ instanceof RoleReceiveRedPackRecord.Data) assign((RoleReceiveRedPackRecord.Data)_o1_);
		else if (_o1_ instanceof RoleReceiveRedPackRecord.Const) assign(((RoleReceiveRedPackRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleReceiveRedPackRecord _o_) {
		_o_._xdb_verify_unsafe_();
		modeltype = _o_.modeltype;
		sendroleid = _o_.sendroleid;
		redpackid = _o_.redpackid;
		receivegold = _o_.receivegold;
		receivefushi = _o_.receivefushi;
		receivetime = _o_.receivetime;
	}

	private void assign(RoleReceiveRedPackRecord.Data _o_) {
		modeltype = _o_.modeltype;
		sendroleid = _o_.sendroleid;
		redpackid = _o_.redpackid;
		receivegold = _o_.receivegold;
		receivefushi = _o_.receivefushi;
		receivetime = _o_.receivetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(modeltype);
		_os_.marshal(sendroleid);
		_os_.marshal(redpackid, mkdb.Const.IO_CHARSET);
		_os_.marshal(receivegold);
		_os_.marshal(receivefushi);
		_os_.marshal(receivetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		modeltype = _os_.unmarshal_int();
		sendroleid = _os_.unmarshal_long();
		redpackid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		receivegold = _os_.unmarshal_long();
		receivefushi = _os_.unmarshal_long();
		receivetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleReceiveRedPackRecord copy() {
		_xdb_verify_unsafe_();
		return new RoleReceiveRedPackRecord(this);
	}

	@Override
	public xbean.RoleReceiveRedPackRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleReceiveRedPackRecord toBean() {
		_xdb_verify_unsafe_();
		return new RoleReceiveRedPackRecord(this); // same as copy()
	}

	@Override
	public xbean.RoleReceiveRedPackRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleReceiveRedPackRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getModeltype() { // 红包类型
		_xdb_verify_unsafe_();
		return modeltype;
	}

	@Override
	public long getSendroleid() { // 发送红包角色Id
		_xdb_verify_unsafe_();
		return sendroleid;
	}

	@Override
	public String getRedpackid() { // 红包Id
		_xdb_verify_unsafe_();
		return redpackid;
	}

	@Override
	public com.locojoy.base.Octets getRedpackidOctets() { // 红包Id
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRedpackid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getReceivegold() { // 收红包金币数量
		_xdb_verify_unsafe_();
		return receivegold;
	}

	@Override
	public long getReceivefushi() { // 收红包符石数量
		_xdb_verify_unsafe_();
		return receivefushi;
	}

	@Override
	public long getReceivetime() { // 领取时间
		_xdb_verify_unsafe_();
		return receivetime;
	}

	@Override
	public void setModeltype(int _v_) { // 红包类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "modeltype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, modeltype) {
					public void rollback() { modeltype = _xdb_saved; }
				};}});
		modeltype = _v_;
	}

	@Override
	public void setSendroleid(long _v_) { // 发送红包角色Id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendroleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sendroleid) {
					public void rollback() { sendroleid = _xdb_saved; }
				};}});
		sendroleid = _v_;
	}

	@Override
	public void setRedpackid(String _v_) { // 红包Id
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, redpackid) {
					public void rollback() { redpackid = _xdb_saved; }
				};}});
		redpackid = _v_;
	}

	@Override
	public void setRedpackidOctets(com.locojoy.base.Octets _v_) { // 红包Id
		_xdb_verify_unsafe_();
		this.setRedpackid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setReceivegold(long _v_) { // 收红包金币数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receivegold") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, receivegold) {
					public void rollback() { receivegold = _xdb_saved; }
				};}});
		receivegold = _v_;
	}

	@Override
	public void setReceivefushi(long _v_) { // 收红包符石数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receivefushi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, receivefushi) {
					public void rollback() { receivefushi = _xdb_saved; }
				};}});
		receivefushi = _v_;
	}

	@Override
	public void setReceivetime(long _v_) { // 领取时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receivetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, receivetime) {
					public void rollback() { receivetime = _xdb_saved; }
				};}});
		receivetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleReceiveRedPackRecord _o_ = null;
		if ( _o1_ instanceof RoleReceiveRedPackRecord ) _o_ = (RoleReceiveRedPackRecord)_o1_;
		else if ( _o1_ instanceof RoleReceiveRedPackRecord.Const ) _o_ = ((RoleReceiveRedPackRecord.Const)_o1_).nThis();
		else return false;
		if (modeltype != _o_.modeltype) return false;
		if (sendroleid != _o_.sendroleid) return false;
		if (!redpackid.equals(_o_.redpackid)) return false;
		if (receivegold != _o_.receivegold) return false;
		if (receivefushi != _o_.receivefushi) return false;
		if (receivetime != _o_.receivetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += sendroleid;
		_h_ += redpackid.hashCode();
		_h_ += receivegold;
		_h_ += receivefushi;
		_h_ += receivetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype);
		_sb_.append(",");
		_sb_.append(sendroleid);
		_sb_.append(",");
		_sb_.append("'").append(redpackid).append("'");
		_sb_.append(",");
		_sb_.append(receivegold);
		_sb_.append(",");
		_sb_.append(receivefushi);
		_sb_.append(",");
		_sb_.append(receivetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("modeltype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendroleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receivegold"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receivefushi"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receivetime"));
		return lb;
	}

	private class Const implements xbean.RoleReceiveRedPackRecord {
		RoleReceiveRedPackRecord nThis() {
			return RoleReceiveRedPackRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleReceiveRedPackRecord copy() {
			return RoleReceiveRedPackRecord.this.copy();
		}

		@Override
		public xbean.RoleReceiveRedPackRecord toData() {
			return RoleReceiveRedPackRecord.this.toData();
		}

		public xbean.RoleReceiveRedPackRecord toBean() {
			return RoleReceiveRedPackRecord.this.toBean();
		}

		@Override
		public xbean.RoleReceiveRedPackRecord toDataIf() {
			return RoleReceiveRedPackRecord.this.toDataIf();
		}

		public xbean.RoleReceiveRedPackRecord toBeanIf() {
			return RoleReceiveRedPackRecord.this.toBeanIf();
		}

		@Override
		public int getModeltype() { // 红包类型
			_xdb_verify_unsafe_();
			return modeltype;
		}

		@Override
		public long getSendroleid() { // 发送红包角色Id
			_xdb_verify_unsafe_();
			return sendroleid;
		}

		@Override
		public String getRedpackid() { // 红包Id
			_xdb_verify_unsafe_();
			return redpackid;
		}

		@Override
		public com.locojoy.base.Octets getRedpackidOctets() { // 红包Id
			_xdb_verify_unsafe_();
			return RoleReceiveRedPackRecord.this.getRedpackidOctets();
		}

		@Override
		public long getReceivegold() { // 收红包金币数量
			_xdb_verify_unsafe_();
			return receivegold;
		}

		@Override
		public long getReceivefushi() { // 收红包符石数量
			_xdb_verify_unsafe_();
			return receivefushi;
		}

		@Override
		public long getReceivetime() { // 领取时间
			_xdb_verify_unsafe_();
			return receivetime;
		}

		@Override
		public void setModeltype(int _v_) { // 红包类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSendroleid(long _v_) { // 发送红包角色Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackid(String _v_) { // 红包Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackidOctets(com.locojoy.base.Octets _v_) { // 红包Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceivegold(long _v_) { // 收红包金币数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceivefushi(long _v_) { // 收红包符石数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceivetime(long _v_) { // 领取时间
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
			return RoleReceiveRedPackRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleReceiveRedPackRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleReceiveRedPackRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleReceiveRedPackRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleReceiveRedPackRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleReceiveRedPackRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleReceiveRedPackRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleReceiveRedPackRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleReceiveRedPackRecord.this.toString();
		}

	}

	public static final class Data implements xbean.RoleReceiveRedPackRecord {
		private int modeltype; // 红包类型
		private long sendroleid; // 发送红包角色Id
		private String redpackid; // 红包Id
		private long receivegold; // 收红包金币数量
		private long receivefushi; // 收红包符石数量
		private long receivetime; // 领取时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			redpackid = "";
		}

		Data(xbean.RoleReceiveRedPackRecord _o1_) {
			if (_o1_ instanceof RoleReceiveRedPackRecord) assign((RoleReceiveRedPackRecord)_o1_);
			else if (_o1_ instanceof RoleReceiveRedPackRecord.Data) assign((RoleReceiveRedPackRecord.Data)_o1_);
			else if (_o1_ instanceof RoleReceiveRedPackRecord.Const) assign(((RoleReceiveRedPackRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleReceiveRedPackRecord _o_) {
			modeltype = _o_.modeltype;
			sendroleid = _o_.sendroleid;
			redpackid = _o_.redpackid;
			receivegold = _o_.receivegold;
			receivefushi = _o_.receivefushi;
			receivetime = _o_.receivetime;
		}

		private void assign(RoleReceiveRedPackRecord.Data _o_) {
			modeltype = _o_.modeltype;
			sendroleid = _o_.sendroleid;
			redpackid = _o_.redpackid;
			receivegold = _o_.receivegold;
			receivefushi = _o_.receivefushi;
			receivetime = _o_.receivetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(modeltype);
			_os_.marshal(sendroleid);
			_os_.marshal(redpackid, mkdb.Const.IO_CHARSET);
			_os_.marshal(receivegold);
			_os_.marshal(receivefushi);
			_os_.marshal(receivetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			modeltype = _os_.unmarshal_int();
			sendroleid = _os_.unmarshal_long();
			redpackid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			receivegold = _os_.unmarshal_long();
			receivefushi = _os_.unmarshal_long();
			receivetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleReceiveRedPackRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleReceiveRedPackRecord toData() {
			return new Data(this);
		}

		public xbean.RoleReceiveRedPackRecord toBean() {
			return new RoleReceiveRedPackRecord(this, null, null);
		}

		@Override
		public xbean.RoleReceiveRedPackRecord toDataIf() {
			return this;
		}

		public xbean.RoleReceiveRedPackRecord toBeanIf() {
			return new RoleReceiveRedPackRecord(this, null, null);
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
		public int getModeltype() { // 红包类型
			return modeltype;
		}

		@Override
		public long getSendroleid() { // 发送红包角色Id
			return sendroleid;
		}

		@Override
		public String getRedpackid() { // 红包Id
			return redpackid;
		}

		@Override
		public com.locojoy.base.Octets getRedpackidOctets() { // 红包Id
			return com.locojoy.base.Octets.wrap(getRedpackid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getReceivegold() { // 收红包金币数量
			return receivegold;
		}

		@Override
		public long getReceivefushi() { // 收红包符石数量
			return receivefushi;
		}

		@Override
		public long getReceivetime() { // 领取时间
			return receivetime;
		}

		@Override
		public void setModeltype(int _v_) { // 红包类型
			modeltype = _v_;
		}

		@Override
		public void setSendroleid(long _v_) { // 发送红包角色Id
			sendroleid = _v_;
		}

		@Override
		public void setRedpackid(String _v_) { // 红包Id
			if (null == _v_)
				throw new NullPointerException();
			redpackid = _v_;
		}

		@Override
		public void setRedpackidOctets(com.locojoy.base.Octets _v_) { // 红包Id
			this.setRedpackid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setReceivegold(long _v_) { // 收红包金币数量
			receivegold = _v_;
		}

		@Override
		public void setReceivefushi(long _v_) { // 收红包符石数量
			receivefushi = _v_;
		}

		@Override
		public void setReceivetime(long _v_) { // 领取时间
			receivetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleReceiveRedPackRecord.Data)) return false;
			RoleReceiveRedPackRecord.Data _o_ = (RoleReceiveRedPackRecord.Data) _o1_;
			if (modeltype != _o_.modeltype) return false;
			if (sendroleid != _o_.sendroleid) return false;
			if (!redpackid.equals(_o_.redpackid)) return false;
			if (receivegold != _o_.receivegold) return false;
			if (receivefushi != _o_.receivefushi) return false;
			if (receivetime != _o_.receivetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += modeltype;
			_h_ += sendroleid;
			_h_ += redpackid.hashCode();
			_h_ += receivegold;
			_h_ += receivefushi;
			_h_ += receivetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(modeltype);
			_sb_.append(",");
			_sb_.append(sendroleid);
			_sb_.append(",");
			_sb_.append("'").append(redpackid).append("'");
			_sb_.append(",");
			_sb_.append(receivegold);
			_sb_.append(",");
			_sb_.append(receivefushi);
			_sb_.append(",");
			_sb_.append(receivetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

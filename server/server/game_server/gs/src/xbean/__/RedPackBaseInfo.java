
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RedPackBaseInfo extends mkdb.XBean implements xbean.RedPackBaseInfo {
	private long roleid; // 角色Id
	private String redpackid; // 红包Id
	private long sendtime; // 发送时间

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		redpackid = "";
		sendtime = 0L;
	}

	RedPackBaseInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		redpackid = "";
	}

	public RedPackBaseInfo() {
		this(0, null, null);
	}

	public RedPackBaseInfo(RedPackBaseInfo _o_) {
		this(_o_, null, null);
	}

	RedPackBaseInfo(xbean.RedPackBaseInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RedPackBaseInfo) assign((RedPackBaseInfo)_o1_);
		else if (_o1_ instanceof RedPackBaseInfo.Data) assign((RedPackBaseInfo.Data)_o1_);
		else if (_o1_ instanceof RedPackBaseInfo.Const) assign(((RedPackBaseInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RedPackBaseInfo _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		redpackid = _o_.redpackid;
		sendtime = _o_.sendtime;
	}

	private void assign(RedPackBaseInfo.Data _o_) {
		roleid = _o_.roleid;
		redpackid = _o_.redpackid;
		sendtime = _o_.sendtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(redpackid, mkdb.Const.IO_CHARSET);
		_os_.marshal(sendtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		redpackid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		sendtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RedPackBaseInfo copy() {
		_xdb_verify_unsafe_();
		return new RedPackBaseInfo(this);
	}

	@Override
	public xbean.RedPackBaseInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackBaseInfo toBean() {
		_xdb_verify_unsafe_();
		return new RedPackBaseInfo(this); // same as copy()
	}

	@Override
	public xbean.RedPackBaseInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackBaseInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 角色Id
		_xdb_verify_unsafe_();
		return roleid;
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
	public long getSendtime() { // 发送时间
		_xdb_verify_unsafe_();
		return sendtime;
	}

	@Override
	public void setRoleid(long _v_) { // 角色Id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
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
	public void setSendtime(long _v_) { // 发送时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sendtime) {
					public void rollback() { sendtime = _xdb_saved; }
				};}});
		sendtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RedPackBaseInfo _o_ = null;
		if ( _o1_ instanceof RedPackBaseInfo ) _o_ = (RedPackBaseInfo)_o1_;
		else if ( _o1_ instanceof RedPackBaseInfo.Const ) _o_ = ((RedPackBaseInfo.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!redpackid.equals(_o_.redpackid)) return false;
		if (sendtime != _o_.sendtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += redpackid.hashCode();
		_h_ += sendtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append("'").append(redpackid).append("'");
		_sb_.append(",");
		_sb_.append(sendtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendtime"));
		return lb;
	}

	private class Const implements xbean.RedPackBaseInfo {
		RedPackBaseInfo nThis() {
			return RedPackBaseInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RedPackBaseInfo copy() {
			return RedPackBaseInfo.this.copy();
		}

		@Override
		public xbean.RedPackBaseInfo toData() {
			return RedPackBaseInfo.this.toData();
		}

		public xbean.RedPackBaseInfo toBean() {
			return RedPackBaseInfo.this.toBean();
		}

		@Override
		public xbean.RedPackBaseInfo toDataIf() {
			return RedPackBaseInfo.this.toDataIf();
		}

		public xbean.RedPackBaseInfo toBeanIf() {
			return RedPackBaseInfo.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 角色Id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getRedpackid() { // 红包Id
			_xdb_verify_unsafe_();
			return redpackid;
		}

		@Override
		public com.locojoy.base.Octets getRedpackidOctets() { // 红包Id
			_xdb_verify_unsafe_();
			return RedPackBaseInfo.this.getRedpackidOctets();
		}

		@Override
		public long getSendtime() { // 发送时间
			_xdb_verify_unsafe_();
			return sendtime;
		}

		@Override
		public void setRoleid(long _v_) { // 角色Id
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
		public void setSendtime(long _v_) { // 发送时间
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
			return RedPackBaseInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RedPackBaseInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RedPackBaseInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RedPackBaseInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RedPackBaseInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RedPackBaseInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RedPackBaseInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RedPackBaseInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RedPackBaseInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RedPackBaseInfo {
		private long roleid; // 角色Id
		private String redpackid; // 红包Id
		private long sendtime; // 发送时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			redpackid = "";
		}

		Data(xbean.RedPackBaseInfo _o1_) {
			if (_o1_ instanceof RedPackBaseInfo) assign((RedPackBaseInfo)_o1_);
			else if (_o1_ instanceof RedPackBaseInfo.Data) assign((RedPackBaseInfo.Data)_o1_);
			else if (_o1_ instanceof RedPackBaseInfo.Const) assign(((RedPackBaseInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RedPackBaseInfo _o_) {
			roleid = _o_.roleid;
			redpackid = _o_.redpackid;
			sendtime = _o_.sendtime;
		}

		private void assign(RedPackBaseInfo.Data _o_) {
			roleid = _o_.roleid;
			redpackid = _o_.redpackid;
			sendtime = _o_.sendtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(redpackid, mkdb.Const.IO_CHARSET);
			_os_.marshal(sendtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			redpackid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			sendtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RedPackBaseInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RedPackBaseInfo toData() {
			return new Data(this);
		}

		public xbean.RedPackBaseInfo toBean() {
			return new RedPackBaseInfo(this, null, null);
		}

		@Override
		public xbean.RedPackBaseInfo toDataIf() {
			return this;
		}

		public xbean.RedPackBaseInfo toBeanIf() {
			return new RedPackBaseInfo(this, null, null);
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
		public long getRoleid() { // 角色Id
			return roleid;
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
		public long getSendtime() { // 发送时间
			return sendtime;
		}

		@Override
		public void setRoleid(long _v_) { // 角色Id
			roleid = _v_;
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
		public void setSendtime(long _v_) { // 发送时间
			sendtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RedPackBaseInfo.Data)) return false;
			RedPackBaseInfo.Data _o_ = (RedPackBaseInfo.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!redpackid.equals(_o_.redpackid)) return false;
			if (sendtime != _o_.sendtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += redpackid.hashCode();
			_h_ += sendtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append("'").append(redpackid).append("'");
			_sb_.append(",");
			_sb_.append(sendtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

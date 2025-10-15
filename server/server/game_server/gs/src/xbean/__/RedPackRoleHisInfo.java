
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RedPackRoleHisInfo extends mkdb.XBean implements xbean.RedPackRoleHisInfo {
	private long roleid; // 角色Id
	private int redpackmoney; // 红包金额
	private long receivetime; // 领取时间

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		redpackmoney = 0;
		receivetime = 0L;
	}

	RedPackRoleHisInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RedPackRoleHisInfo() {
		this(0, null, null);
	}

	public RedPackRoleHisInfo(RedPackRoleHisInfo _o_) {
		this(_o_, null, null);
	}

	RedPackRoleHisInfo(xbean.RedPackRoleHisInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RedPackRoleHisInfo) assign((RedPackRoleHisInfo)_o1_);
		else if (_o1_ instanceof RedPackRoleHisInfo.Data) assign((RedPackRoleHisInfo.Data)_o1_);
		else if (_o1_ instanceof RedPackRoleHisInfo.Const) assign(((RedPackRoleHisInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RedPackRoleHisInfo _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		redpackmoney = _o_.redpackmoney;
		receivetime = _o_.receivetime;
	}

	private void assign(RedPackRoleHisInfo.Data _o_) {
		roleid = _o_.roleid;
		redpackmoney = _o_.redpackmoney;
		receivetime = _o_.receivetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(redpackmoney);
		_os_.marshal(receivetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		redpackmoney = _os_.unmarshal_int();
		receivetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RedPackRoleHisInfo copy() {
		_xdb_verify_unsafe_();
		return new RedPackRoleHisInfo(this);
	}

	@Override
	public xbean.RedPackRoleHisInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackRoleHisInfo toBean() {
		_xdb_verify_unsafe_();
		return new RedPackRoleHisInfo(this); // same as copy()
	}

	@Override
	public xbean.RedPackRoleHisInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackRoleHisInfo toBeanIf() {
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
	public int getRedpackmoney() { // 红包金额
		_xdb_verify_unsafe_();
		return redpackmoney;
	}

	@Override
	public long getReceivetime() { // 领取时间
		_xdb_verify_unsafe_();
		return receivetime;
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
	public void setRedpackmoney(int _v_) { // 红包金额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackmoney") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpackmoney) {
					public void rollback() { redpackmoney = _xdb_saved; }
				};}});
		redpackmoney = _v_;
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
		RedPackRoleHisInfo _o_ = null;
		if ( _o1_ instanceof RedPackRoleHisInfo ) _o_ = (RedPackRoleHisInfo)_o1_;
		else if ( _o1_ instanceof RedPackRoleHisInfo.Const ) _o_ = ((RedPackRoleHisInfo.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (redpackmoney != _o_.redpackmoney) return false;
		if (receivetime != _o_.receivetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += redpackmoney;
		_h_ += receivetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(redpackmoney);
		_sb_.append(",");
		_sb_.append(receivetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackmoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receivetime"));
		return lb;
	}

	private class Const implements xbean.RedPackRoleHisInfo {
		RedPackRoleHisInfo nThis() {
			return RedPackRoleHisInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RedPackRoleHisInfo copy() {
			return RedPackRoleHisInfo.this.copy();
		}

		@Override
		public xbean.RedPackRoleHisInfo toData() {
			return RedPackRoleHisInfo.this.toData();
		}

		public xbean.RedPackRoleHisInfo toBean() {
			return RedPackRoleHisInfo.this.toBean();
		}

		@Override
		public xbean.RedPackRoleHisInfo toDataIf() {
			return RedPackRoleHisInfo.this.toDataIf();
		}

		public xbean.RedPackRoleHisInfo toBeanIf() {
			return RedPackRoleHisInfo.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 角色Id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getRedpackmoney() { // 红包金额
			_xdb_verify_unsafe_();
			return redpackmoney;
		}

		@Override
		public long getReceivetime() { // 领取时间
			_xdb_verify_unsafe_();
			return receivetime;
		}

		@Override
		public void setRoleid(long _v_) { // 角色Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackmoney(int _v_) { // 红包金额
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
			return RedPackRoleHisInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RedPackRoleHisInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RedPackRoleHisInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RedPackRoleHisInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RedPackRoleHisInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RedPackRoleHisInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RedPackRoleHisInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RedPackRoleHisInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RedPackRoleHisInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RedPackRoleHisInfo {
		private long roleid; // 角色Id
		private int redpackmoney; // 红包金额
		private long receivetime; // 领取时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RedPackRoleHisInfo _o1_) {
			if (_o1_ instanceof RedPackRoleHisInfo) assign((RedPackRoleHisInfo)_o1_);
			else if (_o1_ instanceof RedPackRoleHisInfo.Data) assign((RedPackRoleHisInfo.Data)_o1_);
			else if (_o1_ instanceof RedPackRoleHisInfo.Const) assign(((RedPackRoleHisInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RedPackRoleHisInfo _o_) {
			roleid = _o_.roleid;
			redpackmoney = _o_.redpackmoney;
			receivetime = _o_.receivetime;
		}

		private void assign(RedPackRoleHisInfo.Data _o_) {
			roleid = _o_.roleid;
			redpackmoney = _o_.redpackmoney;
			receivetime = _o_.receivetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(redpackmoney);
			_os_.marshal(receivetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			redpackmoney = _os_.unmarshal_int();
			receivetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RedPackRoleHisInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RedPackRoleHisInfo toData() {
			return new Data(this);
		}

		public xbean.RedPackRoleHisInfo toBean() {
			return new RedPackRoleHisInfo(this, null, null);
		}

		@Override
		public xbean.RedPackRoleHisInfo toDataIf() {
			return this;
		}

		public xbean.RedPackRoleHisInfo toBeanIf() {
			return new RedPackRoleHisInfo(this, null, null);
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
		public int getRedpackmoney() { // 红包金额
			return redpackmoney;
		}

		@Override
		public long getReceivetime() { // 领取时间
			return receivetime;
		}

		@Override
		public void setRoleid(long _v_) { // 角色Id
			roleid = _v_;
		}

		@Override
		public void setRedpackmoney(int _v_) { // 红包金额
			redpackmoney = _v_;
		}

		@Override
		public void setReceivetime(long _v_) { // 领取时间
			receivetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RedPackRoleHisInfo.Data)) return false;
			RedPackRoleHisInfo.Data _o_ = (RedPackRoleHisInfo.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (redpackmoney != _o_.redpackmoney) return false;
			if (receivetime != _o_.receivetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += redpackmoney;
			_h_ += receivetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(redpackmoney);
			_sb_.append(",");
			_sb_.append(receivetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

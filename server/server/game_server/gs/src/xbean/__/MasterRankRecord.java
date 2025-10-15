
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MasterRankRecord extends mkdb.XBean implements xbean.MasterRankRecord {
	private long roleid; // 人物ID
	private String rolename; // 人物名称
	private long shidezhi; // 师德值

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		rolename = "";
		shidezhi = 0L;
	}

	MasterRankRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolename = "";
	}

	public MasterRankRecord() {
		this(0, null, null);
	}

	public MasterRankRecord(MasterRankRecord _o_) {
		this(_o_, null, null);
	}

	MasterRankRecord(xbean.MasterRankRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MasterRankRecord) assign((MasterRankRecord)_o1_);
		else if (_o1_ instanceof MasterRankRecord.Data) assign((MasterRankRecord.Data)_o1_);
		else if (_o1_ instanceof MasterRankRecord.Const) assign(((MasterRankRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MasterRankRecord _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		shidezhi = _o_.shidezhi;
	}

	private void assign(MasterRankRecord.Data _o_) {
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		shidezhi = _o_.shidezhi;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
		_os_.marshal(shidezhi);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		shidezhi = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.MasterRankRecord copy() {
		_xdb_verify_unsafe_();
		return new MasterRankRecord(this);
	}

	@Override
	public xbean.MasterRankRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MasterRankRecord toBean() {
		_xdb_verify_unsafe_();
		return new MasterRankRecord(this); // same as copy()
	}

	@Override
	public xbean.MasterRankRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MasterRankRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 人物ID
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public String getRolename() { // 人物名称
		_xdb_verify_unsafe_();
		return rolename;
	}

	@Override
	public com.locojoy.base.Octets getRolenameOctets() { // 人物名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getShidezhi() { // 师德值
		_xdb_verify_unsafe_();
		return shidezhi;
	}

	@Override
	public void setRoleid(long _v_) { // 人物ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setRolename(String _v_) { // 人物名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolename") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, rolename) {
					public void rollback() { rolename = _xdb_saved; }
				};}});
		rolename = _v_;
	}

	@Override
	public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 人物名称
		_xdb_verify_unsafe_();
		this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setShidezhi(long _v_) { // 师德值
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shidezhi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, shidezhi) {
					public void rollback() { shidezhi = _xdb_saved; }
				};}});
		shidezhi = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MasterRankRecord _o_ = null;
		if ( _o1_ instanceof MasterRankRecord ) _o_ = (MasterRankRecord)_o1_;
		else if ( _o1_ instanceof MasterRankRecord.Const ) _o_ = ((MasterRankRecord.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!rolename.equals(_o_.rolename)) return false;
		if (shidezhi != _o_.shidezhi) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += rolename.hashCode();
		_h_ += shidezhi;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append("'").append(rolename).append("'");
		_sb_.append(",");
		_sb_.append(shidezhi);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolename"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shidezhi"));
		return lb;
	}

	private class Const implements xbean.MasterRankRecord {
		MasterRankRecord nThis() {
			return MasterRankRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MasterRankRecord copy() {
			return MasterRankRecord.this.copy();
		}

		@Override
		public xbean.MasterRankRecord toData() {
			return MasterRankRecord.this.toData();
		}

		public xbean.MasterRankRecord toBean() {
			return MasterRankRecord.this.toBean();
		}

		@Override
		public xbean.MasterRankRecord toDataIf() {
			return MasterRankRecord.this.toDataIf();
		}

		public xbean.MasterRankRecord toBeanIf() {
			return MasterRankRecord.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 人物ID
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getRolename() { // 人物名称
			_xdb_verify_unsafe_();
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 人物名称
			_xdb_verify_unsafe_();
			return MasterRankRecord.this.getRolenameOctets();
		}

		@Override
		public long getShidezhi() { // 师德值
			_xdb_verify_unsafe_();
			return shidezhi;
		}

		@Override
		public void setRoleid(long _v_) { // 人物ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolename(String _v_) { // 人物名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 人物名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShidezhi(long _v_) { // 师德值
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
			return MasterRankRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MasterRankRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MasterRankRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MasterRankRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MasterRankRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MasterRankRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MasterRankRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MasterRankRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return MasterRankRecord.this.toString();
		}

	}

	public static final class Data implements xbean.MasterRankRecord {
		private long roleid; // 人物ID
		private String rolename; // 人物名称
		private long shidezhi; // 师德值

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolename = "";
		}

		Data(xbean.MasterRankRecord _o1_) {
			if (_o1_ instanceof MasterRankRecord) assign((MasterRankRecord)_o1_);
			else if (_o1_ instanceof MasterRankRecord.Data) assign((MasterRankRecord.Data)_o1_);
			else if (_o1_ instanceof MasterRankRecord.Const) assign(((MasterRankRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MasterRankRecord _o_) {
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			shidezhi = _o_.shidezhi;
		}

		private void assign(MasterRankRecord.Data _o_) {
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			shidezhi = _o_.shidezhi;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
			_os_.marshal(shidezhi);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			shidezhi = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.MasterRankRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.MasterRankRecord toData() {
			return new Data(this);
		}

		public xbean.MasterRankRecord toBean() {
			return new MasterRankRecord(this, null, null);
		}

		@Override
		public xbean.MasterRankRecord toDataIf() {
			return this;
		}

		public xbean.MasterRankRecord toBeanIf() {
			return new MasterRankRecord(this, null, null);
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
		public long getRoleid() { // 人物ID
			return roleid;
		}

		@Override
		public String getRolename() { // 人物名称
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 人物名称
			return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getShidezhi() { // 师德值
			return shidezhi;
		}

		@Override
		public void setRoleid(long _v_) { // 人物ID
			roleid = _v_;
		}

		@Override
		public void setRolename(String _v_) { // 人物名称
			if (null == _v_)
				throw new NullPointerException();
			rolename = _v_;
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 人物名称
			this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setShidezhi(long _v_) { // 师德值
			shidezhi = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MasterRankRecord.Data)) return false;
			MasterRankRecord.Data _o_ = (MasterRankRecord.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (shidezhi != _o_.shidezhi) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += rolename.hashCode();
			_h_ += shidezhi;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append("'").append(rolename).append("'");
			_sb_.append(",");
			_sb_.append(shidezhi);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

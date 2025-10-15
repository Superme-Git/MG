
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MarshalRoleLevelRecord extends mkdb.XBean implements xbean.MarshalRoleLevelRecord {
	private long roleid; // roleid
	private String rolename; // 名字
	private int level; // 等级
	private int school; // 职业id
	private int rank; // 排名

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		rolename = "";
		level = 0;
		school = 0;
		rank = 0;
	}

	MarshalRoleLevelRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolename = "";
	}

	public MarshalRoleLevelRecord() {
		this(0, null, null);
	}

	public MarshalRoleLevelRecord(MarshalRoleLevelRecord _o_) {
		this(_o_, null, null);
	}

	MarshalRoleLevelRecord(xbean.MarshalRoleLevelRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MarshalRoleLevelRecord) assign((MarshalRoleLevelRecord)_o1_);
		else if (_o1_ instanceof MarshalRoleLevelRecord.Data) assign((MarshalRoleLevelRecord.Data)_o1_);
		else if (_o1_ instanceof MarshalRoleLevelRecord.Const) assign(((MarshalRoleLevelRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MarshalRoleLevelRecord _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		level = _o_.level;
		school = _o_.school;
		rank = _o_.rank;
	}

	private void assign(MarshalRoleLevelRecord.Data _o_) {
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		level = _o_.level;
		school = _o_.school;
		rank = _o_.rank;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(rank);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MarshalRoleLevelRecord copy() {
		_xdb_verify_unsafe_();
		return new MarshalRoleLevelRecord(this);
	}

	@Override
	public xbean.MarshalRoleLevelRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalRoleLevelRecord toBean() {
		_xdb_verify_unsafe_();
		return new MarshalRoleLevelRecord(this); // same as copy()
	}

	@Override
	public xbean.MarshalRoleLevelRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalRoleLevelRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // roleid
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public String getRolename() { // 名字
		_xdb_verify_unsafe_();
		return rolename;
	}

	@Override
	public com.locojoy.base.Octets getRolenameOctets() { // 名字
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getSchool() { // 职业id
		_xdb_verify_unsafe_();
		return school;
	}

	@Override
	public int getRank() { // 排名
		_xdb_verify_unsafe_();
		return rank;
	}

	@Override
	public void setRoleid(long _v_) { // roleid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setRolename(String _v_) { // 名字
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
	public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 名字
		_xdb_verify_unsafe_();
		this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setLevel(int _v_) { // 等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setSchool(int _v_) { // 职业id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "school") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, school) {
					public void rollback() { school = _xdb_saved; }
				};}});
		school = _v_;
	}

	@Override
	public void setRank(int _v_) { // 排名
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rank") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rank) {
					public void rollback() { rank = _xdb_saved; }
				};}});
		rank = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MarshalRoleLevelRecord _o_ = null;
		if ( _o1_ instanceof MarshalRoleLevelRecord ) _o_ = (MarshalRoleLevelRecord)_o1_;
		else if ( _o1_ instanceof MarshalRoleLevelRecord.Const ) _o_ = ((MarshalRoleLevelRecord.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!rolename.equals(_o_.rolename)) return false;
		if (level != _o_.level) return false;
		if (school != _o_.school) return false;
		if (rank != _o_.rank) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += rolename.hashCode();
		_h_ += level;
		_h_ += school;
		_h_ += rank;
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
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(",");
		_sb_.append(rank);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolename"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rank"));
		return lb;
	}

	private class Const implements xbean.MarshalRoleLevelRecord {
		MarshalRoleLevelRecord nThis() {
			return MarshalRoleLevelRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MarshalRoleLevelRecord copy() {
			return MarshalRoleLevelRecord.this.copy();
		}

		@Override
		public xbean.MarshalRoleLevelRecord toData() {
			return MarshalRoleLevelRecord.this.toData();
		}

		public xbean.MarshalRoleLevelRecord toBean() {
			return MarshalRoleLevelRecord.this.toBean();
		}

		@Override
		public xbean.MarshalRoleLevelRecord toDataIf() {
			return MarshalRoleLevelRecord.this.toDataIf();
		}

		public xbean.MarshalRoleLevelRecord toBeanIf() {
			return MarshalRoleLevelRecord.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // roleid
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getRolename() { // 名字
			_xdb_verify_unsafe_();
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 名字
			_xdb_verify_unsafe_();
			return MarshalRoleLevelRecord.this.getRolenameOctets();
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getSchool() { // 职业id
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public int getRank() { // 排名
			_xdb_verify_unsafe_();
			return rank;
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolename(String _v_) { // 名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 职业id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRank(int _v_) { // 排名
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
			return MarshalRoleLevelRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MarshalRoleLevelRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MarshalRoleLevelRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MarshalRoleLevelRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MarshalRoleLevelRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MarshalRoleLevelRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MarshalRoleLevelRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MarshalRoleLevelRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return MarshalRoleLevelRecord.this.toString();
		}

	}

	public static final class Data implements xbean.MarshalRoleLevelRecord {
		private long roleid; // roleid
		private String rolename; // 名字
		private int level; // 等级
		private int school; // 职业id
		private int rank; // 排名

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolename = "";
		}

		Data(xbean.MarshalRoleLevelRecord _o1_) {
			if (_o1_ instanceof MarshalRoleLevelRecord) assign((MarshalRoleLevelRecord)_o1_);
			else if (_o1_ instanceof MarshalRoleLevelRecord.Data) assign((MarshalRoleLevelRecord.Data)_o1_);
			else if (_o1_ instanceof MarshalRoleLevelRecord.Const) assign(((MarshalRoleLevelRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MarshalRoleLevelRecord _o_) {
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			level = _o_.level;
			school = _o_.school;
			rank = _o_.rank;
		}

		private void assign(MarshalRoleLevelRecord.Data _o_) {
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			level = _o_.level;
			school = _o_.school;
			rank = _o_.rank;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
			_os_.marshal(level);
			_os_.marshal(school);
			_os_.marshal(rank);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			level = _os_.unmarshal_int();
			school = _os_.unmarshal_int();
			rank = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MarshalRoleLevelRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.MarshalRoleLevelRecord toData() {
			return new Data(this);
		}

		public xbean.MarshalRoleLevelRecord toBean() {
			return new MarshalRoleLevelRecord(this, null, null);
		}

		@Override
		public xbean.MarshalRoleLevelRecord toDataIf() {
			return this;
		}

		public xbean.MarshalRoleLevelRecord toBeanIf() {
			return new MarshalRoleLevelRecord(this, null, null);
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
		public long getRoleid() { // roleid
			return roleid;
		}

		@Override
		public String getRolename() { // 名字
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 名字
			return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public int getSchool() { // 职业id
			return school;
		}

		@Override
		public int getRank() { // 排名
			return rank;
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			roleid = _v_;
		}

		@Override
		public void setRolename(String _v_) { // 名字
			if (null == _v_)
				throw new NullPointerException();
			rolename = _v_;
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 名字
			this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setSchool(int _v_) { // 职业id
			school = _v_;
		}

		@Override
		public void setRank(int _v_) { // 排名
			rank = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MarshalRoleLevelRecord.Data)) return false;
			MarshalRoleLevelRecord.Data _o_ = (MarshalRoleLevelRecord.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (rank != _o_.rank) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += rolename.hashCode();
			_h_ += level;
			_h_ += school;
			_h_ += rank;
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
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(",");
			_sb_.append(rank);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

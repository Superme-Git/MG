
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LDTeamRoleInfoDes extends mkdb.XBean implements xbean.LDTeamRoleInfoDes {
	private long roleid; // 角色Id
	private String rolename; // 角色名
	private int shape; // 角色造型
	private int level; // 角色等级
	private int school; // 职业

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		rolename = "";
		shape = 0;
		level = 0;
		school = 0;
	}

	LDTeamRoleInfoDes(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolename = "";
	}

	public LDTeamRoleInfoDes() {
		this(0, null, null);
	}

	public LDTeamRoleInfoDes(LDTeamRoleInfoDes _o_) {
		this(_o_, null, null);
	}

	LDTeamRoleInfoDes(xbean.LDTeamRoleInfoDes _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LDTeamRoleInfoDes) assign((LDTeamRoleInfoDes)_o1_);
		else if (_o1_ instanceof LDTeamRoleInfoDes.Data) assign((LDTeamRoleInfoDes.Data)_o1_);
		else if (_o1_ instanceof LDTeamRoleInfoDes.Const) assign(((LDTeamRoleInfoDes.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LDTeamRoleInfoDes _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		shape = _o_.shape;
		level = _o_.level;
		school = _o_.school;
	}

	private void assign(LDTeamRoleInfoDes.Data _o_) {
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		shape = _o_.shape;
		level = _o_.level;
		school = _o_.school;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
		_os_.marshal(shape);
		_os_.marshal(level);
		_os_.marshal(school);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		shape = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.LDTeamRoleInfoDes copy() {
		_xdb_verify_unsafe_();
		return new LDTeamRoleInfoDes(this);
	}

	@Override
	public xbean.LDTeamRoleInfoDes toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDTeamRoleInfoDes toBean() {
		_xdb_verify_unsafe_();
		return new LDTeamRoleInfoDes(this); // same as copy()
	}

	@Override
	public xbean.LDTeamRoleInfoDes toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDTeamRoleInfoDes toBeanIf() {
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
	public String getRolename() { // 角色名
		_xdb_verify_unsafe_();
		return rolename;
	}

	@Override
	public com.locojoy.base.Octets getRolenameOctets() { // 角色名
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getShape() { // 角色造型
		_xdb_verify_unsafe_();
		return shape;
	}

	@Override
	public int getLevel() { // 角色等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getSchool() { // 职业
		_xdb_verify_unsafe_();
		return school;
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
	public void setRolename(String _v_) { // 角色名
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
	public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 角色名
		_xdb_verify_unsafe_();
		this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setShape(int _v_) { // 角色造型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shape") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shape) {
					public void rollback() { shape = _xdb_saved; }
				};}});
		shape = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 角色等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setSchool(int _v_) { // 职业
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "school") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, school) {
					public void rollback() { school = _xdb_saved; }
				};}});
		school = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LDTeamRoleInfoDes _o_ = null;
		if ( _o1_ instanceof LDTeamRoleInfoDes ) _o_ = (LDTeamRoleInfoDes)_o1_;
		else if ( _o1_ instanceof LDTeamRoleInfoDes.Const ) _o_ = ((LDTeamRoleInfoDes.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!rolename.equals(_o_.rolename)) return false;
		if (shape != _o_.shape) return false;
		if (level != _o_.level) return false;
		if (school != _o_.school) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += rolename.hashCode();
		_h_ += shape;
		_h_ += level;
		_h_ += school;
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
		_sb_.append(shape);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolename"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shape"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		return lb;
	}

	private class Const implements xbean.LDTeamRoleInfoDes {
		LDTeamRoleInfoDes nThis() {
			return LDTeamRoleInfoDes.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LDTeamRoleInfoDes copy() {
			return LDTeamRoleInfoDes.this.copy();
		}

		@Override
		public xbean.LDTeamRoleInfoDes toData() {
			return LDTeamRoleInfoDes.this.toData();
		}

		public xbean.LDTeamRoleInfoDes toBean() {
			return LDTeamRoleInfoDes.this.toBean();
		}

		@Override
		public xbean.LDTeamRoleInfoDes toDataIf() {
			return LDTeamRoleInfoDes.this.toDataIf();
		}

		public xbean.LDTeamRoleInfoDes toBeanIf() {
			return LDTeamRoleInfoDes.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 角色Id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getRolename() { // 角色名
			_xdb_verify_unsafe_();
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 角色名
			_xdb_verify_unsafe_();
			return LDTeamRoleInfoDes.this.getRolenameOctets();
		}

		@Override
		public int getShape() { // 角色造型
			_xdb_verify_unsafe_();
			return shape;
		}

		@Override
		public int getLevel() { // 角色等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getSchool() { // 职业
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public void setRoleid(long _v_) { // 角色Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolename(String _v_) { // 角色名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 角色名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShape(int _v_) { // 角色造型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 角色等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 职业
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
			return LDTeamRoleInfoDes.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LDTeamRoleInfoDes.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LDTeamRoleInfoDes.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LDTeamRoleInfoDes.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LDTeamRoleInfoDes.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LDTeamRoleInfoDes.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LDTeamRoleInfoDes.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LDTeamRoleInfoDes.this.hashCode();
		}

		@Override
		public String toString() {
			return LDTeamRoleInfoDes.this.toString();
		}

	}

	public static final class Data implements xbean.LDTeamRoleInfoDes {
		private long roleid; // 角色Id
		private String rolename; // 角色名
		private int shape; // 角色造型
		private int level; // 角色等级
		private int school; // 职业

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolename = "";
		}

		Data(xbean.LDTeamRoleInfoDes _o1_) {
			if (_o1_ instanceof LDTeamRoleInfoDes) assign((LDTeamRoleInfoDes)_o1_);
			else if (_o1_ instanceof LDTeamRoleInfoDes.Data) assign((LDTeamRoleInfoDes.Data)_o1_);
			else if (_o1_ instanceof LDTeamRoleInfoDes.Const) assign(((LDTeamRoleInfoDes.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LDTeamRoleInfoDes _o_) {
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			shape = _o_.shape;
			level = _o_.level;
			school = _o_.school;
		}

		private void assign(LDTeamRoleInfoDes.Data _o_) {
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			shape = _o_.shape;
			level = _o_.level;
			school = _o_.school;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
			_os_.marshal(shape);
			_os_.marshal(level);
			_os_.marshal(school);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			shape = _os_.unmarshal_int();
			level = _os_.unmarshal_int();
			school = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.LDTeamRoleInfoDes copy() {
			return new Data(this);
		}

		@Override
		public xbean.LDTeamRoleInfoDes toData() {
			return new Data(this);
		}

		public xbean.LDTeamRoleInfoDes toBean() {
			return new LDTeamRoleInfoDes(this, null, null);
		}

		@Override
		public xbean.LDTeamRoleInfoDes toDataIf() {
			return this;
		}

		public xbean.LDTeamRoleInfoDes toBeanIf() {
			return new LDTeamRoleInfoDes(this, null, null);
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
		public String getRolename() { // 角色名
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 角色名
			return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getShape() { // 角色造型
			return shape;
		}

		@Override
		public int getLevel() { // 角色等级
			return level;
		}

		@Override
		public int getSchool() { // 职业
			return school;
		}

		@Override
		public void setRoleid(long _v_) { // 角色Id
			roleid = _v_;
		}

		@Override
		public void setRolename(String _v_) { // 角色名
			if (null == _v_)
				throw new NullPointerException();
			rolename = _v_;
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 角色名
			this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setShape(int _v_) { // 角色造型
			shape = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 角色等级
			level = _v_;
		}

		@Override
		public void setSchool(int _v_) { // 职业
			school = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LDTeamRoleInfoDes.Data)) return false;
			LDTeamRoleInfoDes.Data _o_ = (LDTeamRoleInfoDes.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (shape != _o_.shape) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += rolename.hashCode();
			_h_ += shape;
			_h_ += level;
			_h_ += school;
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
			_sb_.append(shape);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

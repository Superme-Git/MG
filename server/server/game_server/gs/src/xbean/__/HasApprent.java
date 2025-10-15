
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HasApprent extends mkdb.XBean implements xbean.HasApprent {
	private String name; // 
	private int level; // 
	private int school; // 
	private long roleid; // 
	private int camp; // 
	private int shap; // 

	@Override
	public void _reset_unsafe_() {
		name = "";
		level = 0;
		school = 0;
		roleid = 0L;
		camp = 0;
		shap = 0;
	}

	HasApprent(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		name = "";
	}

	public HasApprent() {
		this(0, null, null);
	}

	public HasApprent(HasApprent _o_) {
		this(_o_, null, null);
	}

	HasApprent(xbean.HasApprent _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HasApprent) assign((HasApprent)_o1_);
		else if (_o1_ instanceof HasApprent.Data) assign((HasApprent.Data)_o1_);
		else if (_o1_ instanceof HasApprent.Const) assign(((HasApprent.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HasApprent _o_) {
		_o_._xdb_verify_unsafe_();
		name = _o_.name;
		level = _o_.level;
		school = _o_.school;
		roleid = _o_.roleid;
		camp = _o_.camp;
		shap = _o_.shap;
	}

	private void assign(HasApprent.Data _o_) {
		name = _o_.name;
		level = _o_.level;
		school = _o_.school;
		roleid = _o_.roleid;
		camp = _o_.camp;
		shap = _o_.shap;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(name, mkdb.Const.IO_CHARSET);
		_os_.marshal(level);
		_os_.marshal(school);
		_os_.marshal(roleid);
		_os_.marshal(camp);
		_os_.marshal(shap);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		level = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		camp = _os_.unmarshal_int();
		shap = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.HasApprent copy() {
		_xdb_verify_unsafe_();
		return new HasApprent(this);
	}

	@Override
	public xbean.HasApprent toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HasApprent toBean() {
		_xdb_verify_unsafe_();
		return new HasApprent(this); // same as copy()
	}

	@Override
	public xbean.HasApprent toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HasApprent toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getName() { // 
		_xdb_verify_unsafe_();
		return name;
	}

	@Override
	public com.locojoy.base.Octets getNameOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getLevel() { // 
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getSchool() { // 
		_xdb_verify_unsafe_();
		return school;
	}

	@Override
	public long getRoleid() { // 
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getCamp() { // 
		_xdb_verify_unsafe_();
		return camp;
	}

	@Override
	public int getShap() { // 
		_xdb_verify_unsafe_();
		return shap;
	}

	@Override
	public void setName(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "name") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, name) {
					public void rollback() { name = _xdb_saved; }
				};}});
		name = _v_;
	}

	@Override
	public void setNameOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setLevel(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setSchool(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "school") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, school) {
					public void rollback() { school = _xdb_saved; }
				};}});
		school = _v_;
	}

	@Override
	public void setRoleid(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setCamp(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "camp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, camp) {
					public void rollback() { camp = _xdb_saved; }
				};}});
		camp = _v_;
	}

	@Override
	public void setShap(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shap") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shap) {
					public void rollback() { shap = _xdb_saved; }
				};}});
		shap = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HasApprent _o_ = null;
		if ( _o1_ instanceof HasApprent ) _o_ = (HasApprent)_o1_;
		else if ( _o1_ instanceof HasApprent.Const ) _o_ = ((HasApprent.Const)_o1_).nThis();
		else return false;
		if (!name.equals(_o_.name)) return false;
		if (level != _o_.level) return false;
		if (school != _o_.school) return false;
		if (roleid != _o_.roleid) return false;
		if (camp != _o_.camp) return false;
		if (shap != _o_.shap) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += name.hashCode();
		_h_ += level;
		_h_ += school;
		_h_ += roleid;
		_h_ += camp;
		_h_ += shap;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(name).append("'");
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(camp);
		_sb_.append(",");
		_sb_.append(shap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("name"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("camp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shap"));
		return lb;
	}

	private class Const implements xbean.HasApprent {
		HasApprent nThis() {
			return HasApprent.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HasApprent copy() {
			return HasApprent.this.copy();
		}

		@Override
		public xbean.HasApprent toData() {
			return HasApprent.this.toData();
		}

		public xbean.HasApprent toBean() {
			return HasApprent.this.toBean();
		}

		@Override
		public xbean.HasApprent toDataIf() {
			return HasApprent.this.toDataIf();
		}

		public xbean.HasApprent toBeanIf() {
			return HasApprent.this.toBeanIf();
		}

		@Override
		public String getName() { // 
			_xdb_verify_unsafe_();
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 
			_xdb_verify_unsafe_();
			return HasApprent.this.getNameOctets();
		}

		@Override
		public int getLevel() { // 
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getSchool() { // 
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getCamp() { // 
			_xdb_verify_unsafe_();
			return camp;
		}

		@Override
		public int getShap() { // 
			_xdb_verify_unsafe_();
			return shap;
		}

		@Override
		public void setName(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCamp(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShap(int _v_) { // 
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
			return HasApprent.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HasApprent.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HasApprent.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HasApprent.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HasApprent.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HasApprent.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HasApprent.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HasApprent.this.hashCode();
		}

		@Override
		public String toString() {
			return HasApprent.this.toString();
		}

	}

	public static final class Data implements xbean.HasApprent {
		private String name; // 
		private int level; // 
		private int school; // 
		private long roleid; // 
		private int camp; // 
		private int shap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			name = "";
		}

		Data(xbean.HasApprent _o1_) {
			if (_o1_ instanceof HasApprent) assign((HasApprent)_o1_);
			else if (_o1_ instanceof HasApprent.Data) assign((HasApprent.Data)_o1_);
			else if (_o1_ instanceof HasApprent.Const) assign(((HasApprent.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HasApprent _o_) {
			name = _o_.name;
			level = _o_.level;
			school = _o_.school;
			roleid = _o_.roleid;
			camp = _o_.camp;
			shap = _o_.shap;
		}

		private void assign(HasApprent.Data _o_) {
			name = _o_.name;
			level = _o_.level;
			school = _o_.school;
			roleid = _o_.roleid;
			camp = _o_.camp;
			shap = _o_.shap;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(name, mkdb.Const.IO_CHARSET);
			_os_.marshal(level);
			_os_.marshal(school);
			_os_.marshal(roleid);
			_os_.marshal(camp);
			_os_.marshal(shap);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			level = _os_.unmarshal_int();
			school = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			camp = _os_.unmarshal_int();
			shap = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.HasApprent copy() {
			return new Data(this);
		}

		@Override
		public xbean.HasApprent toData() {
			return new Data(this);
		}

		public xbean.HasApprent toBean() {
			return new HasApprent(this, null, null);
		}

		@Override
		public xbean.HasApprent toDataIf() {
			return this;
		}

		public xbean.HasApprent toBeanIf() {
			return new HasApprent(this, null, null);
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
		public String getName() { // 
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 
			return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getLevel() { // 
			return level;
		}

		@Override
		public int getSchool() { // 
			return school;
		}

		@Override
		public long getRoleid() { // 
			return roleid;
		}

		@Override
		public int getCamp() { // 
			return camp;
		}

		@Override
		public int getShap() { // 
			return shap;
		}

		@Override
		public void setName(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			name = _v_;
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 
			this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setLevel(int _v_) { // 
			level = _v_;
		}

		@Override
		public void setSchool(int _v_) { // 
			school = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setCamp(int _v_) { // 
			camp = _v_;
		}

		@Override
		public void setShap(int _v_) { // 
			shap = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HasApprent.Data)) return false;
			HasApprent.Data _o_ = (HasApprent.Data) _o1_;
			if (!name.equals(_o_.name)) return false;
			if (level != _o_.level) return false;
			if (school != _o_.school) return false;
			if (roleid != _o_.roleid) return false;
			if (camp != _o_.camp) return false;
			if (shap != _o_.shap) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += name.hashCode();
			_h_ += level;
			_h_ += school;
			_h_ += roleid;
			_h_ += camp;
			_h_ += shap;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(name).append("'");
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(camp);
			_sb_.append(",");
			_sb_.append(shap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

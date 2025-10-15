
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MarshalReceFlowerRecord extends mkdb.XBean implements xbean.MarshalReceFlowerRecord {
	private long roleid; // roleid by changhao
	private String name; // 玩家名字 by changhao
	private int school; // 玩家职业 by changhao
	private long num; // 收花 by changhao

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		name = "";
		school = 0;
		num = 0L;
	}

	MarshalReceFlowerRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		name = "";
	}

	public MarshalReceFlowerRecord() {
		this(0, null, null);
	}

	public MarshalReceFlowerRecord(MarshalReceFlowerRecord _o_) {
		this(_o_, null, null);
	}

	MarshalReceFlowerRecord(xbean.MarshalReceFlowerRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MarshalReceFlowerRecord) assign((MarshalReceFlowerRecord)_o1_);
		else if (_o1_ instanceof MarshalReceFlowerRecord.Data) assign((MarshalReceFlowerRecord.Data)_o1_);
		else if (_o1_ instanceof MarshalReceFlowerRecord.Const) assign(((MarshalReceFlowerRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MarshalReceFlowerRecord _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		name = _o_.name;
		school = _o_.school;
		num = _o_.num;
	}

	private void assign(MarshalReceFlowerRecord.Data _o_) {
		roleid = _o_.roleid;
		name = _o_.name;
		school = _o_.school;
		num = _o_.num;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(name, mkdb.Const.IO_CHARSET);
		_os_.marshal(school);
		_os_.marshal(num);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		school = _os_.unmarshal_int();
		num = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.MarshalReceFlowerRecord copy() {
		_xdb_verify_unsafe_();
		return new MarshalReceFlowerRecord(this);
	}

	@Override
	public xbean.MarshalReceFlowerRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalReceFlowerRecord toBean() {
		_xdb_verify_unsafe_();
		return new MarshalReceFlowerRecord(this); // same as copy()
	}

	@Override
	public xbean.MarshalReceFlowerRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalReceFlowerRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // roleid by changhao
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public String getName() { // 玩家名字 by changhao
		_xdb_verify_unsafe_();
		return name;
	}

	@Override
	public com.locojoy.base.Octets getNameOctets() { // 玩家名字 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getSchool() { // 玩家职业 by changhao
		_xdb_verify_unsafe_();
		return school;
	}

	@Override
	public long getNum() { // 收花 by changhao
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public void setRoleid(long _v_) { // roleid by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setName(String _v_) { // 玩家名字 by changhao
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
	public void setNameOctets(com.locojoy.base.Octets _v_) { // 玩家名字 by changhao
		_xdb_verify_unsafe_();
		this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setSchool(int _v_) { // 玩家职业 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "school") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, school) {
					public void rollback() { school = _xdb_saved; }
				};}});
		school = _v_;
	}

	@Override
	public void setNum(long _v_) { // 收花 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "num") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, num) {
					public void rollback() { num = _xdb_saved; }
				};}});
		num = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MarshalReceFlowerRecord _o_ = null;
		if ( _o1_ instanceof MarshalReceFlowerRecord ) _o_ = (MarshalReceFlowerRecord)_o1_;
		else if ( _o1_ instanceof MarshalReceFlowerRecord.Const ) _o_ = ((MarshalReceFlowerRecord.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!name.equals(_o_.name)) return false;
		if (school != _o_.school) return false;
		if (num != _o_.num) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += name.hashCode();
		_h_ += school;
		_h_ += num;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append("'").append(name).append("'");
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(",");
		_sb_.append(num);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("name"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		return lb;
	}

	private class Const implements xbean.MarshalReceFlowerRecord {
		MarshalReceFlowerRecord nThis() {
			return MarshalReceFlowerRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MarshalReceFlowerRecord copy() {
			return MarshalReceFlowerRecord.this.copy();
		}

		@Override
		public xbean.MarshalReceFlowerRecord toData() {
			return MarshalReceFlowerRecord.this.toData();
		}

		public xbean.MarshalReceFlowerRecord toBean() {
			return MarshalReceFlowerRecord.this.toBean();
		}

		@Override
		public xbean.MarshalReceFlowerRecord toDataIf() {
			return MarshalReceFlowerRecord.this.toDataIf();
		}

		public xbean.MarshalReceFlowerRecord toBeanIf() {
			return MarshalReceFlowerRecord.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // roleid by changhao
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getName() { // 玩家名字 by changhao
			_xdb_verify_unsafe_();
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 玩家名字 by changhao
			_xdb_verify_unsafe_();
			return MarshalReceFlowerRecord.this.getNameOctets();
		}

		@Override
		public int getSchool() { // 玩家职业 by changhao
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public long getNum() { // 收花 by changhao
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public void setRoleid(long _v_) { // roleid by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setName(String _v_) { // 玩家名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 玩家名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 玩家职业 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNum(long _v_) { // 收花 by changhao
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
			return MarshalReceFlowerRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MarshalReceFlowerRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MarshalReceFlowerRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MarshalReceFlowerRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MarshalReceFlowerRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MarshalReceFlowerRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MarshalReceFlowerRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MarshalReceFlowerRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return MarshalReceFlowerRecord.this.toString();
		}

	}

	public static final class Data implements xbean.MarshalReceFlowerRecord {
		private long roleid; // roleid by changhao
		private String name; // 玩家名字 by changhao
		private int school; // 玩家职业 by changhao
		private long num; // 收花 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			name = "";
		}

		Data(xbean.MarshalReceFlowerRecord _o1_) {
			if (_o1_ instanceof MarshalReceFlowerRecord) assign((MarshalReceFlowerRecord)_o1_);
			else if (_o1_ instanceof MarshalReceFlowerRecord.Data) assign((MarshalReceFlowerRecord.Data)_o1_);
			else if (_o1_ instanceof MarshalReceFlowerRecord.Const) assign(((MarshalReceFlowerRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MarshalReceFlowerRecord _o_) {
			roleid = _o_.roleid;
			name = _o_.name;
			school = _o_.school;
			num = _o_.num;
		}

		private void assign(MarshalReceFlowerRecord.Data _o_) {
			roleid = _o_.roleid;
			name = _o_.name;
			school = _o_.school;
			num = _o_.num;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(name, mkdb.Const.IO_CHARSET);
			_os_.marshal(school);
			_os_.marshal(num);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			school = _os_.unmarshal_int();
			num = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.MarshalReceFlowerRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.MarshalReceFlowerRecord toData() {
			return new Data(this);
		}

		public xbean.MarshalReceFlowerRecord toBean() {
			return new MarshalReceFlowerRecord(this, null, null);
		}

		@Override
		public xbean.MarshalReceFlowerRecord toDataIf() {
			return this;
		}

		public xbean.MarshalReceFlowerRecord toBeanIf() {
			return new MarshalReceFlowerRecord(this, null, null);
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
		public long getRoleid() { // roleid by changhao
			return roleid;
		}

		@Override
		public String getName() { // 玩家名字 by changhao
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 玩家名字 by changhao
			return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getSchool() { // 玩家职业 by changhao
			return school;
		}

		@Override
		public long getNum() { // 收花 by changhao
			return num;
		}

		@Override
		public void setRoleid(long _v_) { // roleid by changhao
			roleid = _v_;
		}

		@Override
		public void setName(String _v_) { // 玩家名字 by changhao
			if (null == _v_)
				throw new NullPointerException();
			name = _v_;
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 玩家名字 by changhao
			this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setSchool(int _v_) { // 玩家职业 by changhao
			school = _v_;
		}

		@Override
		public void setNum(long _v_) { // 收花 by changhao
			num = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MarshalReceFlowerRecord.Data)) return false;
			MarshalReceFlowerRecord.Data _o_ = (MarshalReceFlowerRecord.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!name.equals(_o_.name)) return false;
			if (school != _o_.school) return false;
			if (num != _o_.num) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += name.hashCode();
			_h_ += school;
			_h_ += num;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append("'").append(name).append("'");
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(",");
			_sb_.append(num);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

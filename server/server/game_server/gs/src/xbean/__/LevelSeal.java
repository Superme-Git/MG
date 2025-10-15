
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LevelSeal extends mkdb.XBean implements xbean.LevelSeal {
	private int level; // 到达等级
	private int rolenum; // 已经到达这个等级的人数

	@Override
	public void _reset_unsafe_() {
		level = 0;
		rolenum = 0;
	}

	LevelSeal(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public LevelSeal() {
		this(0, null, null);
	}

	public LevelSeal(LevelSeal _o_) {
		this(_o_, null, null);
	}

	LevelSeal(xbean.LevelSeal _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LevelSeal) assign((LevelSeal)_o1_);
		else if (_o1_ instanceof LevelSeal.Data) assign((LevelSeal.Data)_o1_);
		else if (_o1_ instanceof LevelSeal.Const) assign(((LevelSeal.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LevelSeal _o_) {
		_o_._xdb_verify_unsafe_();
		level = _o_.level;
		rolenum = _o_.rolenum;
	}

	private void assign(LevelSeal.Data _o_) {
		level = _o_.level;
		rolenum = _o_.rolenum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(level);
		_os_.marshal(rolenum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		level = _os_.unmarshal_int();
		rolenum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.LevelSeal copy() {
		_xdb_verify_unsafe_();
		return new LevelSeal(this);
	}

	@Override
	public xbean.LevelSeal toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LevelSeal toBean() {
		_xdb_verify_unsafe_();
		return new LevelSeal(this); // same as copy()
	}

	@Override
	public xbean.LevelSeal toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LevelSeal toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getLevel() { // 到达等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getRolenum() { // 已经到达这个等级的人数
		_xdb_verify_unsafe_();
		return rolenum;
	}

	@Override
	public void setLevel(int _v_) { // 到达等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setRolenum(int _v_) { // 已经到达这个等级的人数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rolenum) {
					public void rollback() { rolenum = _xdb_saved; }
				};}});
		rolenum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LevelSeal _o_ = null;
		if ( _o1_ instanceof LevelSeal ) _o_ = (LevelSeal)_o1_;
		else if ( _o1_ instanceof LevelSeal.Const ) _o_ = ((LevelSeal.Const)_o1_).nThis();
		else return false;
		if (level != _o_.level) return false;
		if (rolenum != _o_.rolenum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += level;
		_h_ += rolenum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(rolenum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolenum"));
		return lb;
	}

	private class Const implements xbean.LevelSeal {
		LevelSeal nThis() {
			return LevelSeal.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LevelSeal copy() {
			return LevelSeal.this.copy();
		}

		@Override
		public xbean.LevelSeal toData() {
			return LevelSeal.this.toData();
		}

		public xbean.LevelSeal toBean() {
			return LevelSeal.this.toBean();
		}

		@Override
		public xbean.LevelSeal toDataIf() {
			return LevelSeal.this.toDataIf();
		}

		public xbean.LevelSeal toBeanIf() {
			return LevelSeal.this.toBeanIf();
		}

		@Override
		public int getLevel() { // 到达等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getRolenum() { // 已经到达这个等级的人数
			_xdb_verify_unsafe_();
			return rolenum;
		}

		@Override
		public void setLevel(int _v_) { // 到达等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolenum(int _v_) { // 已经到达这个等级的人数
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
			return LevelSeal.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LevelSeal.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LevelSeal.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LevelSeal.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LevelSeal.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LevelSeal.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LevelSeal.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LevelSeal.this.hashCode();
		}

		@Override
		public String toString() {
			return LevelSeal.this.toString();
		}

	}

	public static final class Data implements xbean.LevelSeal {
		private int level; // 到达等级
		private int rolenum; // 已经到达这个等级的人数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.LevelSeal _o1_) {
			if (_o1_ instanceof LevelSeal) assign((LevelSeal)_o1_);
			else if (_o1_ instanceof LevelSeal.Data) assign((LevelSeal.Data)_o1_);
			else if (_o1_ instanceof LevelSeal.Const) assign(((LevelSeal.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LevelSeal _o_) {
			level = _o_.level;
			rolenum = _o_.rolenum;
		}

		private void assign(LevelSeal.Data _o_) {
			level = _o_.level;
			rolenum = _o_.rolenum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(level);
			_os_.marshal(rolenum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			level = _os_.unmarshal_int();
			rolenum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.LevelSeal copy() {
			return new Data(this);
		}

		@Override
		public xbean.LevelSeal toData() {
			return new Data(this);
		}

		public xbean.LevelSeal toBean() {
			return new LevelSeal(this, null, null);
		}

		@Override
		public xbean.LevelSeal toDataIf() {
			return this;
		}

		public xbean.LevelSeal toBeanIf() {
			return new LevelSeal(this, null, null);
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
		public int getLevel() { // 到达等级
			return level;
		}

		@Override
		public int getRolenum() { // 已经到达这个等级的人数
			return rolenum;
		}

		@Override
		public void setLevel(int _v_) { // 到达等级
			level = _v_;
		}

		@Override
		public void setRolenum(int _v_) { // 已经到达这个等级的人数
			rolenum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LevelSeal.Data)) return false;
			LevelSeal.Data _o_ = (LevelSeal.Data) _o1_;
			if (level != _o_.level) return false;
			if (rolenum != _o_.rolenum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += level;
			_h_ += rolenum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(rolenum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

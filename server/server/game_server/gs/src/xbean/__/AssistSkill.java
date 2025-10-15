
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AssistSkill extends mkdb.XBean implements xbean.AssistSkill {
	private int level; // 
	private long exp; // 

	@Override
	public void _reset_unsafe_() {
		level = 0;
		exp = 0L;
	}

	AssistSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public AssistSkill() {
		this(0, null, null);
	}

	public AssistSkill(AssistSkill _o_) {
		this(_o_, null, null);
	}

	AssistSkill(xbean.AssistSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AssistSkill) assign((AssistSkill)_o1_);
		else if (_o1_ instanceof AssistSkill.Data) assign((AssistSkill.Data)_o1_);
		else if (_o1_ instanceof AssistSkill.Const) assign(((AssistSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AssistSkill _o_) {
		_o_._xdb_verify_unsafe_();
		level = _o_.level;
		exp = _o_.exp;
	}

	private void assign(AssistSkill.Data _o_) {
		level = _o_.level;
		exp = _o_.exp;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(level);
		_os_.marshal(exp);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		level = _os_.unmarshal_int();
		exp = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.AssistSkill copy() {
		_xdb_verify_unsafe_();
		return new AssistSkill(this);
	}

	@Override
	public xbean.AssistSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AssistSkill toBean() {
		_xdb_verify_unsafe_();
		return new AssistSkill(this); // same as copy()
	}

	@Override
	public xbean.AssistSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AssistSkill toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getLevel() { // 
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public long getExp() { // 
		_xdb_verify_unsafe_();
		return exp;
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
	public void setExp(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "exp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, exp) {
					public void rollback() { exp = _xdb_saved; }
				};}});
		exp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AssistSkill _o_ = null;
		if ( _o1_ instanceof AssistSkill ) _o_ = (AssistSkill)_o1_;
		else if ( _o1_ instanceof AssistSkill.Const ) _o_ = ((AssistSkill.Const)_o1_).nThis();
		else return false;
		if (level != _o_.level) return false;
		if (exp != _o_.exp) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += level;
		_h_ += exp;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(exp);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("exp"));
		return lb;
	}

	private class Const implements xbean.AssistSkill {
		AssistSkill nThis() {
			return AssistSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AssistSkill copy() {
			return AssistSkill.this.copy();
		}

		@Override
		public xbean.AssistSkill toData() {
			return AssistSkill.this.toData();
		}

		public xbean.AssistSkill toBean() {
			return AssistSkill.this.toBean();
		}

		@Override
		public xbean.AssistSkill toDataIf() {
			return AssistSkill.this.toDataIf();
		}

		public xbean.AssistSkill toBeanIf() {
			return AssistSkill.this.toBeanIf();
		}

		@Override
		public int getLevel() { // 
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public long getExp() { // 
			_xdb_verify_unsafe_();
			return exp;
		}

		@Override
		public void setLevel(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExp(long _v_) { // 
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
			return AssistSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AssistSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AssistSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AssistSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AssistSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AssistSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AssistSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AssistSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return AssistSkill.this.toString();
		}

	}

	public static final class Data implements xbean.AssistSkill {
		private int level; // 
		private long exp; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.AssistSkill _o1_) {
			if (_o1_ instanceof AssistSkill) assign((AssistSkill)_o1_);
			else if (_o1_ instanceof AssistSkill.Data) assign((AssistSkill.Data)_o1_);
			else if (_o1_ instanceof AssistSkill.Const) assign(((AssistSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AssistSkill _o_) {
			level = _o_.level;
			exp = _o_.exp;
		}

		private void assign(AssistSkill.Data _o_) {
			level = _o_.level;
			exp = _o_.exp;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(level);
			_os_.marshal(exp);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			level = _os_.unmarshal_int();
			exp = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.AssistSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.AssistSkill toData() {
			return new Data(this);
		}

		public xbean.AssistSkill toBean() {
			return new AssistSkill(this, null, null);
		}

		@Override
		public xbean.AssistSkill toDataIf() {
			return this;
		}

		public xbean.AssistSkill toBeanIf() {
			return new AssistSkill(this, null, null);
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
		public int getLevel() { // 
			return level;
		}

		@Override
		public long getExp() { // 
			return exp;
		}

		@Override
		public void setLevel(int _v_) { // 
			level = _v_;
		}

		@Override
		public void setExp(long _v_) { // 
			exp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AssistSkill.Data)) return false;
			AssistSkill.Data _o_ = (AssistSkill.Data) _o1_;
			if (level != _o_.level) return false;
			if (exp != _o_.exp) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += level;
			_h_ += exp;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(exp);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

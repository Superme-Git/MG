
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PracticeSkill extends mkdb.XBean implements xbean.PracticeSkill {
	private int level; // 
	private int exp; // 

	@Override
	public void _reset_unsafe_() {
		level = 0;
		exp = 0;
	}

	PracticeSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public PracticeSkill() {
		this(0, null, null);
	}

	public PracticeSkill(PracticeSkill _o_) {
		this(_o_, null, null);
	}

	PracticeSkill(xbean.PracticeSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PracticeSkill) assign((PracticeSkill)_o1_);
		else if (_o1_ instanceof PracticeSkill.Data) assign((PracticeSkill.Data)_o1_);
		else if (_o1_ instanceof PracticeSkill.Const) assign(((PracticeSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PracticeSkill _o_) {
		_o_._xdb_verify_unsafe_();
		level = _o_.level;
		exp = _o_.exp;
	}

	private void assign(PracticeSkill.Data _o_) {
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
		exp = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.PracticeSkill copy() {
		_xdb_verify_unsafe_();
		return new PracticeSkill(this);
	}

	@Override
	public xbean.PracticeSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PracticeSkill toBean() {
		_xdb_verify_unsafe_();
		return new PracticeSkill(this); // same as copy()
	}

	@Override
	public xbean.PracticeSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PracticeSkill toBeanIf() {
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
	public int getExp() { // 
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
	public void setExp(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "exp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, exp) {
					public void rollback() { exp = _xdb_saved; }
				};}});
		exp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PracticeSkill _o_ = null;
		if ( _o1_ instanceof PracticeSkill ) _o_ = (PracticeSkill)_o1_;
		else if ( _o1_ instanceof PracticeSkill.Const ) _o_ = ((PracticeSkill.Const)_o1_).nThis();
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

	private class Const implements xbean.PracticeSkill {
		PracticeSkill nThis() {
			return PracticeSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PracticeSkill copy() {
			return PracticeSkill.this.copy();
		}

		@Override
		public xbean.PracticeSkill toData() {
			return PracticeSkill.this.toData();
		}

		public xbean.PracticeSkill toBean() {
			return PracticeSkill.this.toBean();
		}

		@Override
		public xbean.PracticeSkill toDataIf() {
			return PracticeSkill.this.toDataIf();
		}

		public xbean.PracticeSkill toBeanIf() {
			return PracticeSkill.this.toBeanIf();
		}

		@Override
		public int getLevel() { // 
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getExp() { // 
			_xdb_verify_unsafe_();
			return exp;
		}

		@Override
		public void setLevel(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExp(int _v_) { // 
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
			return PracticeSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PracticeSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PracticeSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PracticeSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PracticeSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PracticeSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PracticeSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PracticeSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return PracticeSkill.this.toString();
		}

	}

	public static final class Data implements xbean.PracticeSkill {
		private int level; // 
		private int exp; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.PracticeSkill _o1_) {
			if (_o1_ instanceof PracticeSkill) assign((PracticeSkill)_o1_);
			else if (_o1_ instanceof PracticeSkill.Data) assign((PracticeSkill.Data)_o1_);
			else if (_o1_ instanceof PracticeSkill.Const) assign(((PracticeSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PracticeSkill _o_) {
			level = _o_.level;
			exp = _o_.exp;
		}

		private void assign(PracticeSkill.Data _o_) {
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
			exp = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.PracticeSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.PracticeSkill toData() {
			return new Data(this);
		}

		public xbean.PracticeSkill toBean() {
			return new PracticeSkill(this, null, null);
		}

		@Override
		public xbean.PracticeSkill toDataIf() {
			return this;
		}

		public xbean.PracticeSkill toBeanIf() {
			return new PracticeSkill(this, null, null);
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
		public int getExp() { // 
			return exp;
		}

		@Override
		public void setLevel(int _v_) { // 
			level = _v_;
		}

		@Override
		public void setExp(int _v_) { // 
			exp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PracticeSkill.Data)) return false;
			PracticeSkill.Data _o_ = (PracticeSkill.Data) _o1_;
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


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ParticleSkill extends mkdb.XBean implements xbean.ParticleSkill {
	private int level; // 
	private int exp; // 

	@Override
	public void _reset_unsafe_() {
		level = 0;
		exp = 0;
	}

	ParticleSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ParticleSkill() {
		this(0, null, null);
	}

	public ParticleSkill(ParticleSkill _o_) {
		this(_o_, null, null);
	}

	ParticleSkill(xbean.ParticleSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ParticleSkill) assign((ParticleSkill)_o1_);
		else if (_o1_ instanceof ParticleSkill.Data) assign((ParticleSkill.Data)_o1_);
		else if (_o1_ instanceof ParticleSkill.Const) assign(((ParticleSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ParticleSkill _o_) {
		_o_._xdb_verify_unsafe_();
		level = _o_.level;
		exp = _o_.exp;
	}

	private void assign(ParticleSkill.Data _o_) {
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
	public xbean.ParticleSkill copy() {
		_xdb_verify_unsafe_();
		return new ParticleSkill(this);
	}

	@Override
	public xbean.ParticleSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ParticleSkill toBean() {
		_xdb_verify_unsafe_();
		return new ParticleSkill(this); // same as copy()
	}

	@Override
	public xbean.ParticleSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ParticleSkill toBeanIf() {
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
		ParticleSkill _o_ = null;
		if ( _o1_ instanceof ParticleSkill ) _o_ = (ParticleSkill)_o1_;
		else if ( _o1_ instanceof ParticleSkill.Const ) _o_ = ((ParticleSkill.Const)_o1_).nThis();
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

	private class Const implements xbean.ParticleSkill {
		ParticleSkill nThis() {
			return ParticleSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ParticleSkill copy() {
			return ParticleSkill.this.copy();
		}

		@Override
		public xbean.ParticleSkill toData() {
			return ParticleSkill.this.toData();
		}

		public xbean.ParticleSkill toBean() {
			return ParticleSkill.this.toBean();
		}

		@Override
		public xbean.ParticleSkill toDataIf() {
			return ParticleSkill.this.toDataIf();
		}

		public xbean.ParticleSkill toBeanIf() {
			return ParticleSkill.this.toBeanIf();
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
			return ParticleSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ParticleSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ParticleSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ParticleSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ParticleSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ParticleSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ParticleSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ParticleSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return ParticleSkill.this.toString();
		}

	}

	public static final class Data implements xbean.ParticleSkill {
		private int level; // 
		private int exp; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ParticleSkill _o1_) {
			if (_o1_ instanceof ParticleSkill) assign((ParticleSkill)_o1_);
			else if (_o1_ instanceof ParticleSkill.Data) assign((ParticleSkill.Data)_o1_);
			else if (_o1_ instanceof ParticleSkill.Const) assign(((ParticleSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ParticleSkill _o_) {
			level = _o_.level;
			exp = _o_.exp;
		}

		private void assign(ParticleSkill.Data _o_) {
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
		public xbean.ParticleSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.ParticleSkill toData() {
			return new Data(this);
		}

		public xbean.ParticleSkill toBean() {
			return new ParticleSkill(this, null, null);
		}

		@Override
		public xbean.ParticleSkill toDataIf() {
			return this;
		}

		public xbean.ParticleSkill toBeanIf() {
			return new ParticleSkill(this, null, null);
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
			if (!(_o1_ instanceof ParticleSkill.Data)) return false;
			ParticleSkill.Data _o_ = (ParticleSkill.Data) _o1_;
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

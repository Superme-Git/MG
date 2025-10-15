
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SpecialSkill extends mkdb.XBean implements xbean.SpecialSkill {
	private int skillid; // 
	private int equiptype; // 

	@Override
	public void _reset_unsafe_() {
		skillid = 0;
		equiptype = 0;
	}

	SpecialSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public SpecialSkill() {
		this(0, null, null);
	}

	public SpecialSkill(SpecialSkill _o_) {
		this(_o_, null, null);
	}

	SpecialSkill(xbean.SpecialSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SpecialSkill) assign((SpecialSkill)_o1_);
		else if (_o1_ instanceof SpecialSkill.Data) assign((SpecialSkill.Data)_o1_);
		else if (_o1_ instanceof SpecialSkill.Const) assign(((SpecialSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SpecialSkill _o_) {
		_o_._xdb_verify_unsafe_();
		skillid = _o_.skillid;
		equiptype = _o_.equiptype;
	}

	private void assign(SpecialSkill.Data _o_) {
		skillid = _o_.skillid;
		equiptype = _o_.equiptype;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(skillid);
		_os_.marshal(equiptype);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		skillid = _os_.unmarshal_int();
		equiptype = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.SpecialSkill copy() {
		_xdb_verify_unsafe_();
		return new SpecialSkill(this);
	}

	@Override
	public xbean.SpecialSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SpecialSkill toBean() {
		_xdb_verify_unsafe_();
		return new SpecialSkill(this); // same as copy()
	}

	@Override
	public xbean.SpecialSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SpecialSkill toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getSkillid() { // 
		_xdb_verify_unsafe_();
		return skillid;
	}

	@Override
	public int getEquiptype() { // 
		_xdb_verify_unsafe_();
		return equiptype;
	}

	@Override
	public void setSkillid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skillid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skillid) {
					public void rollback() { skillid = _xdb_saved; }
				};}});
		skillid = _v_;
	}

	@Override
	public void setEquiptype(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "equiptype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, equiptype) {
					public void rollback() { equiptype = _xdb_saved; }
				};}});
		equiptype = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SpecialSkill _o_ = null;
		if ( _o1_ instanceof SpecialSkill ) _o_ = (SpecialSkill)_o1_;
		else if ( _o1_ instanceof SpecialSkill.Const ) _o_ = ((SpecialSkill.Const)_o1_).nThis();
		else return false;
		if (skillid != _o_.skillid) return false;
		if (equiptype != _o_.equiptype) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += skillid;
		_h_ += equiptype;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skillid);
		_sb_.append(",");
		_sb_.append(equiptype);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skillid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("equiptype"));
		return lb;
	}

	private class Const implements xbean.SpecialSkill {
		SpecialSkill nThis() {
			return SpecialSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SpecialSkill copy() {
			return SpecialSkill.this.copy();
		}

		@Override
		public xbean.SpecialSkill toData() {
			return SpecialSkill.this.toData();
		}

		public xbean.SpecialSkill toBean() {
			return SpecialSkill.this.toBean();
		}

		@Override
		public xbean.SpecialSkill toDataIf() {
			return SpecialSkill.this.toDataIf();
		}

		public xbean.SpecialSkill toBeanIf() {
			return SpecialSkill.this.toBeanIf();
		}

		@Override
		public int getSkillid() { // 
			_xdb_verify_unsafe_();
			return skillid;
		}

		@Override
		public int getEquiptype() { // 
			_xdb_verify_unsafe_();
			return equiptype;
		}

		@Override
		public void setSkillid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEquiptype(int _v_) { // 
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
			return SpecialSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SpecialSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SpecialSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SpecialSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SpecialSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SpecialSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SpecialSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SpecialSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return SpecialSkill.this.toString();
		}

	}

	public static final class Data implements xbean.SpecialSkill {
		private int skillid; // 
		private int equiptype; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.SpecialSkill _o1_) {
			if (_o1_ instanceof SpecialSkill) assign((SpecialSkill)_o1_);
			else if (_o1_ instanceof SpecialSkill.Data) assign((SpecialSkill.Data)_o1_);
			else if (_o1_ instanceof SpecialSkill.Const) assign(((SpecialSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SpecialSkill _o_) {
			skillid = _o_.skillid;
			equiptype = _o_.equiptype;
		}

		private void assign(SpecialSkill.Data _o_) {
			skillid = _o_.skillid;
			equiptype = _o_.equiptype;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(skillid);
			_os_.marshal(equiptype);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			skillid = _os_.unmarshal_int();
			equiptype = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.SpecialSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.SpecialSkill toData() {
			return new Data(this);
		}

		public xbean.SpecialSkill toBean() {
			return new SpecialSkill(this, null, null);
		}

		@Override
		public xbean.SpecialSkill toDataIf() {
			return this;
		}

		public xbean.SpecialSkill toBeanIf() {
			return new SpecialSkill(this, null, null);
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
		public int getSkillid() { // 
			return skillid;
		}

		@Override
		public int getEquiptype() { // 
			return equiptype;
		}

		@Override
		public void setSkillid(int _v_) { // 
			skillid = _v_;
		}

		@Override
		public void setEquiptype(int _v_) { // 
			equiptype = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SpecialSkill.Data)) return false;
			SpecialSkill.Data _o_ = (SpecialSkill.Data) _o1_;
			if (skillid != _o_.skillid) return false;
			if (equiptype != _o_.equiptype) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += skillid;
			_h_ += equiptype;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(skillid);
			_sb_.append(",");
			_sb_.append(equiptype);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

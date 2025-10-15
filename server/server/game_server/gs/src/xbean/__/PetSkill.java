
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PetSkill extends mkdb.XBean implements xbean.PetSkill {
	private int skillid; // 技能id
	private int skilltype; // 0=先天技能  1=后天通过技能书打上去的技能
	private int certification; // 0=未认证 1=认证

	@Override
	public void _reset_unsafe_() {
		skillid = 0;
		skilltype = 0;
		certification = 0;
	}

	PetSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public PetSkill() {
		this(0, null, null);
	}

	public PetSkill(PetSkill _o_) {
		this(_o_, null, null);
	}

	PetSkill(xbean.PetSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PetSkill) assign((PetSkill)_o1_);
		else if (_o1_ instanceof PetSkill.Data) assign((PetSkill.Data)_o1_);
		else if (_o1_ instanceof PetSkill.Const) assign(((PetSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PetSkill _o_) {
		_o_._xdb_verify_unsafe_();
		skillid = _o_.skillid;
		skilltype = _o_.skilltype;
		certification = _o_.certification;
	}

	private void assign(PetSkill.Data _o_) {
		skillid = _o_.skillid;
		skilltype = _o_.skilltype;
		certification = _o_.certification;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(skillid);
		_os_.marshal(skilltype);
		_os_.marshal(certification);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		skillid = _os_.unmarshal_int();
		skilltype = _os_.unmarshal_int();
		certification = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.PetSkill copy() {
		_xdb_verify_unsafe_();
		return new PetSkill(this);
	}

	@Override
	public xbean.PetSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetSkill toBean() {
		_xdb_verify_unsafe_();
		return new PetSkill(this); // same as copy()
	}

	@Override
	public xbean.PetSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetSkill toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getSkillid() { // 技能id
		_xdb_verify_unsafe_();
		return skillid;
	}

	@Override
	public int getSkilltype() { // 0=先天技能  1=后天通过技能书打上去的技能
		_xdb_verify_unsafe_();
		return skilltype;
	}

	@Override
	public int getCertification() { // 0=未认证 1=认证
		_xdb_verify_unsafe_();
		return certification;
	}

	@Override
	public void setSkillid(int _v_) { // 技能id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skillid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skillid) {
					public void rollback() { skillid = _xdb_saved; }
				};}});
		skillid = _v_;
	}

	@Override
	public void setSkilltype(int _v_) { // 0=先天技能  1=后天通过技能书打上去的技能
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skilltype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skilltype) {
					public void rollback() { skilltype = _xdb_saved; }
				};}});
		skilltype = _v_;
	}

	@Override
	public void setCertification(int _v_) { // 0=未认证 1=认证
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "certification") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, certification) {
					public void rollback() { certification = _xdb_saved; }
				};}});
		certification = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PetSkill _o_ = null;
		if ( _o1_ instanceof PetSkill ) _o_ = (PetSkill)_o1_;
		else if ( _o1_ instanceof PetSkill.Const ) _o_ = ((PetSkill.Const)_o1_).nThis();
		else return false;
		if (skillid != _o_.skillid) return false;
		if (skilltype != _o_.skilltype) return false;
		if (certification != _o_.certification) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += skillid;
		_h_ += skilltype;
		_h_ += certification;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(skillid);
		_sb_.append(",");
		_sb_.append(skilltype);
		_sb_.append(",");
		_sb_.append(certification);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skillid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skilltype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("certification"));
		return lb;
	}

	private class Const implements xbean.PetSkill {
		PetSkill nThis() {
			return PetSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PetSkill copy() {
			return PetSkill.this.copy();
		}

		@Override
		public xbean.PetSkill toData() {
			return PetSkill.this.toData();
		}

		public xbean.PetSkill toBean() {
			return PetSkill.this.toBean();
		}

		@Override
		public xbean.PetSkill toDataIf() {
			return PetSkill.this.toDataIf();
		}

		public xbean.PetSkill toBeanIf() {
			return PetSkill.this.toBeanIf();
		}

		@Override
		public int getSkillid() { // 技能id
			_xdb_verify_unsafe_();
			return skillid;
		}

		@Override
		public int getSkilltype() { // 0=先天技能  1=后天通过技能书打上去的技能
			_xdb_verify_unsafe_();
			return skilltype;
		}

		@Override
		public int getCertification() { // 0=未认证 1=认证
			_xdb_verify_unsafe_();
			return certification;
		}

		@Override
		public void setSkillid(int _v_) { // 技能id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSkilltype(int _v_) { // 0=先天技能  1=后天通过技能书打上去的技能
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCertification(int _v_) { // 0=未认证 1=认证
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
			return PetSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PetSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PetSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PetSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PetSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PetSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PetSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PetSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return PetSkill.this.toString();
		}

	}

	public static final class Data implements xbean.PetSkill {
		private int skillid; // 技能id
		private int skilltype; // 0=先天技能  1=后天通过技能书打上去的技能
		private int certification; // 0=未认证 1=认证

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.PetSkill _o1_) {
			if (_o1_ instanceof PetSkill) assign((PetSkill)_o1_);
			else if (_o1_ instanceof PetSkill.Data) assign((PetSkill.Data)_o1_);
			else if (_o1_ instanceof PetSkill.Const) assign(((PetSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PetSkill _o_) {
			skillid = _o_.skillid;
			skilltype = _o_.skilltype;
			certification = _o_.certification;
		}

		private void assign(PetSkill.Data _o_) {
			skillid = _o_.skillid;
			skilltype = _o_.skilltype;
			certification = _o_.certification;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(skillid);
			_os_.marshal(skilltype);
			_os_.marshal(certification);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			skillid = _os_.unmarshal_int();
			skilltype = _os_.unmarshal_int();
			certification = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.PetSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.PetSkill toData() {
			return new Data(this);
		}

		public xbean.PetSkill toBean() {
			return new PetSkill(this, null, null);
		}

		@Override
		public xbean.PetSkill toDataIf() {
			return this;
		}

		public xbean.PetSkill toBeanIf() {
			return new PetSkill(this, null, null);
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
		public int getSkillid() { // 技能id
			return skillid;
		}

		@Override
		public int getSkilltype() { // 0=先天技能  1=后天通过技能书打上去的技能
			return skilltype;
		}

		@Override
		public int getCertification() { // 0=未认证 1=认证
			return certification;
		}

		@Override
		public void setSkillid(int _v_) { // 技能id
			skillid = _v_;
		}

		@Override
		public void setSkilltype(int _v_) { // 0=先天技能  1=后天通过技能书打上去的技能
			skilltype = _v_;
		}

		@Override
		public void setCertification(int _v_) { // 0=未认证 1=认证
			certification = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PetSkill.Data)) return false;
			PetSkill.Data _o_ = (PetSkill.Data) _o1_;
			if (skillid != _o_.skillid) return false;
			if (skilltype != _o_.skilltype) return false;
			if (certification != _o_.certification) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += skillid;
			_h_ += skilltype;
			_h_ += certification;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(skillid);
			_sb_.append(",");
			_sb_.append(skilltype);
			_sb_.append(",");
			_sb_.append(certification);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanSkill extends mkdb.XBean implements xbean.ClanSkill {
	private int clanskillid; // 技能id
	private int clanskilllevel; // 技能当前等级
	private int clanskillexp; // 技能当前经验

	@Override
	public void _reset_unsafe_() {
		clanskillid = 0;
		clanskilllevel = 0;
		clanskillexp = 0;
	}

	ClanSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ClanSkill() {
		this(0, null, null);
	}

	public ClanSkill(ClanSkill _o_) {
		this(_o_, null, null);
	}

	ClanSkill(xbean.ClanSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanSkill) assign((ClanSkill)_o1_);
		else if (_o1_ instanceof ClanSkill.Data) assign((ClanSkill.Data)_o1_);
		else if (_o1_ instanceof ClanSkill.Const) assign(((ClanSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanSkill _o_) {
		_o_._xdb_verify_unsafe_();
		clanskillid = _o_.clanskillid;
		clanskilllevel = _o_.clanskilllevel;
		clanskillexp = _o_.clanskillexp;
	}

	private void assign(ClanSkill.Data _o_) {
		clanskillid = _o_.clanskillid;
		clanskilllevel = _o_.clanskilllevel;
		clanskillexp = _o_.clanskillexp;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(clanskillid);
		_os_.marshal(clanskilllevel);
		_os_.marshal(clanskillexp);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		clanskillid = _os_.unmarshal_int();
		clanskilllevel = _os_.unmarshal_int();
		clanskillexp = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ClanSkill copy() {
		_xdb_verify_unsafe_();
		return new ClanSkill(this);
	}

	@Override
	public xbean.ClanSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanSkill toBean() {
		_xdb_verify_unsafe_();
		return new ClanSkill(this); // same as copy()
	}

	@Override
	public xbean.ClanSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanSkill toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getClanskillid() { // 技能id
		_xdb_verify_unsafe_();
		return clanskillid;
	}

	@Override
	public int getClanskilllevel() { // 技能当前等级
		_xdb_verify_unsafe_();
		return clanskilllevel;
	}

	@Override
	public int getClanskillexp() { // 技能当前经验
		_xdb_verify_unsafe_();
		return clanskillexp;
	}

	@Override
	public void setClanskillid(int _v_) { // 技能id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanskillid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanskillid) {
					public void rollback() { clanskillid = _xdb_saved; }
				};}});
		clanskillid = _v_;
	}

	@Override
	public void setClanskilllevel(int _v_) { // 技能当前等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanskilllevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanskilllevel) {
					public void rollback() { clanskilllevel = _xdb_saved; }
				};}});
		clanskilllevel = _v_;
	}

	@Override
	public void setClanskillexp(int _v_) { // 技能当前经验
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanskillexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanskillexp) {
					public void rollback() { clanskillexp = _xdb_saved; }
				};}});
		clanskillexp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanSkill _o_ = null;
		if ( _o1_ instanceof ClanSkill ) _o_ = (ClanSkill)_o1_;
		else if ( _o1_ instanceof ClanSkill.Const ) _o_ = ((ClanSkill.Const)_o1_).nThis();
		else return false;
		if (clanskillid != _o_.clanskillid) return false;
		if (clanskilllevel != _o_.clanskilllevel) return false;
		if (clanskillexp != _o_.clanskillexp) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += clanskillid;
		_h_ += clanskilllevel;
		_h_ += clanskillexp;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanskillid);
		_sb_.append(",");
		_sb_.append(clanskilllevel);
		_sb_.append(",");
		_sb_.append(clanskillexp);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanskillid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanskilllevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanskillexp"));
		return lb;
	}

	private class Const implements xbean.ClanSkill {
		ClanSkill nThis() {
			return ClanSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanSkill copy() {
			return ClanSkill.this.copy();
		}

		@Override
		public xbean.ClanSkill toData() {
			return ClanSkill.this.toData();
		}

		public xbean.ClanSkill toBean() {
			return ClanSkill.this.toBean();
		}

		@Override
		public xbean.ClanSkill toDataIf() {
			return ClanSkill.this.toDataIf();
		}

		public xbean.ClanSkill toBeanIf() {
			return ClanSkill.this.toBeanIf();
		}

		@Override
		public int getClanskillid() { // 技能id
			_xdb_verify_unsafe_();
			return clanskillid;
		}

		@Override
		public int getClanskilllevel() { // 技能当前等级
			_xdb_verify_unsafe_();
			return clanskilllevel;
		}

		@Override
		public int getClanskillexp() { // 技能当前经验
			_xdb_verify_unsafe_();
			return clanskillexp;
		}

		@Override
		public void setClanskillid(int _v_) { // 技能id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanskilllevel(int _v_) { // 技能当前等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanskillexp(int _v_) { // 技能当前经验
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
			return ClanSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanSkill.this.toString();
		}

	}

	public static final class Data implements xbean.ClanSkill {
		private int clanskillid; // 技能id
		private int clanskilllevel; // 技能当前等级
		private int clanskillexp; // 技能当前经验

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ClanSkill _o1_) {
			if (_o1_ instanceof ClanSkill) assign((ClanSkill)_o1_);
			else if (_o1_ instanceof ClanSkill.Data) assign((ClanSkill.Data)_o1_);
			else if (_o1_ instanceof ClanSkill.Const) assign(((ClanSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanSkill _o_) {
			clanskillid = _o_.clanskillid;
			clanskilllevel = _o_.clanskilllevel;
			clanskillexp = _o_.clanskillexp;
		}

		private void assign(ClanSkill.Data _o_) {
			clanskillid = _o_.clanskillid;
			clanskilllevel = _o_.clanskilllevel;
			clanskillexp = _o_.clanskillexp;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(clanskillid);
			_os_.marshal(clanskilllevel);
			_os_.marshal(clanskillexp);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			clanskillid = _os_.unmarshal_int();
			clanskilllevel = _os_.unmarshal_int();
			clanskillexp = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ClanSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanSkill toData() {
			return new Data(this);
		}

		public xbean.ClanSkill toBean() {
			return new ClanSkill(this, null, null);
		}

		@Override
		public xbean.ClanSkill toDataIf() {
			return this;
		}

		public xbean.ClanSkill toBeanIf() {
			return new ClanSkill(this, null, null);
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
		public int getClanskillid() { // 技能id
			return clanskillid;
		}

		@Override
		public int getClanskilllevel() { // 技能当前等级
			return clanskilllevel;
		}

		@Override
		public int getClanskillexp() { // 技能当前经验
			return clanskillexp;
		}

		@Override
		public void setClanskillid(int _v_) { // 技能id
			clanskillid = _v_;
		}

		@Override
		public void setClanskilllevel(int _v_) { // 技能当前等级
			clanskilllevel = _v_;
		}

		@Override
		public void setClanskillexp(int _v_) { // 技能当前经验
			clanskillexp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanSkill.Data)) return false;
			ClanSkill.Data _o_ = (ClanSkill.Data) _o1_;
			if (clanskillid != _o_.clanskillid) return false;
			if (clanskilllevel != _o_.clanskilllevel) return false;
			if (clanskillexp != _o_.clanskillexp) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += clanskillid;
			_h_ += clanskilllevel;
			_h_ += clanskillexp;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(clanskillid);
			_sb_.append(",");
			_sb_.append(clanskilllevel);
			_sb_.append(",");
			_sb_.append(clanskillexp);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MonsterSkill extends mkdb.XBean implements xbean.MonsterSkill {
	private int id; // 
	private int skilllevel; // 
	private int castrate; // 以千为底

	@Override
	public void _reset_unsafe_() {
		id = 0;
		skilllevel = 0;
		castrate = 0;
	}

	MonsterSkill(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public MonsterSkill() {
		this(0, null, null);
	}

	public MonsterSkill(MonsterSkill _o_) {
		this(_o_, null, null);
	}

	MonsterSkill(xbean.MonsterSkill _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MonsterSkill) assign((MonsterSkill)_o1_);
		else if (_o1_ instanceof MonsterSkill.Data) assign((MonsterSkill.Data)_o1_);
		else if (_o1_ instanceof MonsterSkill.Const) assign(((MonsterSkill.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MonsterSkill _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		skilllevel = _o_.skilllevel;
		castrate = _o_.castrate;
	}

	private void assign(MonsterSkill.Data _o_) {
		id = _o_.id;
		skilllevel = _o_.skilllevel;
		castrate = _o_.castrate;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(skilllevel);
		_os_.marshal(castrate);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		skilllevel = _os_.unmarshal_int();
		castrate = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MonsterSkill copy() {
		_xdb_verify_unsafe_();
		return new MonsterSkill(this);
	}

	@Override
	public xbean.MonsterSkill toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MonsterSkill toBean() {
		_xdb_verify_unsafe_();
		return new MonsterSkill(this); // same as copy()
	}

	@Override
	public xbean.MonsterSkill toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MonsterSkill toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getSkilllevel() { // 
		_xdb_verify_unsafe_();
		return skilllevel;
	}

	@Override
	public int getCastrate() { // 以千为底
		_xdb_verify_unsafe_();
		return castrate;
	}

	@Override
	public void setId(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setSkilllevel(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skilllevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skilllevel) {
					public void rollback() { skilllevel = _xdb_saved; }
				};}});
		skilllevel = _v_;
	}

	@Override
	public void setCastrate(int _v_) { // 以千为底
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "castrate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, castrate) {
					public void rollback() { castrate = _xdb_saved; }
				};}});
		castrate = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MonsterSkill _o_ = null;
		if ( _o1_ instanceof MonsterSkill ) _o_ = (MonsterSkill)_o1_;
		else if ( _o1_ instanceof MonsterSkill.Const ) _o_ = ((MonsterSkill.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (skilllevel != _o_.skilllevel) return false;
		if (castrate != _o_.castrate) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += skilllevel;
		_h_ += castrate;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(skilllevel);
		_sb_.append(",");
		_sb_.append(castrate);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skilllevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("castrate"));
		return lb;
	}

	private class Const implements xbean.MonsterSkill {
		MonsterSkill nThis() {
			return MonsterSkill.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MonsterSkill copy() {
			return MonsterSkill.this.copy();
		}

		@Override
		public xbean.MonsterSkill toData() {
			return MonsterSkill.this.toData();
		}

		public xbean.MonsterSkill toBean() {
			return MonsterSkill.this.toBean();
		}

		@Override
		public xbean.MonsterSkill toDataIf() {
			return MonsterSkill.this.toDataIf();
		}

		public xbean.MonsterSkill toBeanIf() {
			return MonsterSkill.this.toBeanIf();
		}

		@Override
		public int getId() { // 
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getSkilllevel() { // 
			_xdb_verify_unsafe_();
			return skilllevel;
		}

		@Override
		public int getCastrate() { // 以千为底
			_xdb_verify_unsafe_();
			return castrate;
		}

		@Override
		public void setId(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSkilllevel(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCastrate(int _v_) { // 以千为底
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
			return MonsterSkill.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MonsterSkill.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MonsterSkill.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MonsterSkill.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MonsterSkill.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MonsterSkill.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MonsterSkill.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MonsterSkill.this.hashCode();
		}

		@Override
		public String toString() {
			return MonsterSkill.this.toString();
		}

	}

	public static final class Data implements xbean.MonsterSkill {
		private int id; // 
		private int skilllevel; // 
		private int castrate; // 以千为底

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.MonsterSkill _o1_) {
			if (_o1_ instanceof MonsterSkill) assign((MonsterSkill)_o1_);
			else if (_o1_ instanceof MonsterSkill.Data) assign((MonsterSkill.Data)_o1_);
			else if (_o1_ instanceof MonsterSkill.Const) assign(((MonsterSkill.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MonsterSkill _o_) {
			id = _o_.id;
			skilllevel = _o_.skilllevel;
			castrate = _o_.castrate;
		}

		private void assign(MonsterSkill.Data _o_) {
			id = _o_.id;
			skilllevel = _o_.skilllevel;
			castrate = _o_.castrate;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(skilllevel);
			_os_.marshal(castrate);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			skilllevel = _os_.unmarshal_int();
			castrate = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MonsterSkill copy() {
			return new Data(this);
		}

		@Override
		public xbean.MonsterSkill toData() {
			return new Data(this);
		}

		public xbean.MonsterSkill toBean() {
			return new MonsterSkill(this, null, null);
		}

		@Override
		public xbean.MonsterSkill toDataIf() {
			return this;
		}

		public xbean.MonsterSkill toBeanIf() {
			return new MonsterSkill(this, null, null);
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
		public int getId() { // 
			return id;
		}

		@Override
		public int getSkilllevel() { // 
			return skilllevel;
		}

		@Override
		public int getCastrate() { // 以千为底
			return castrate;
		}

		@Override
		public void setId(int _v_) { // 
			id = _v_;
		}

		@Override
		public void setSkilllevel(int _v_) { // 
			skilllevel = _v_;
		}

		@Override
		public void setCastrate(int _v_) { // 以千为底
			castrate = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MonsterSkill.Data)) return false;
			MonsterSkill.Data _o_ = (MonsterSkill.Data) _o1_;
			if (id != _o_.id) return false;
			if (skilllevel != _o_.skilllevel) return false;
			if (castrate != _o_.castrate) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += skilllevel;
			_h_ += castrate;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(skilllevel);
			_sb_.append(",");
			_sb_.append(castrate);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

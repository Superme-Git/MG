
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BattleFieldScroeRank extends mkdb.XBean implements xbean.BattleFieldScroeRank {
	private long roleid; // id by changhao
	private int scroe; // 积分 by changhao

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		scroe = 0;
	}

	BattleFieldScroeRank(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public BattleFieldScroeRank() {
		this(0, null, null);
	}

	public BattleFieldScroeRank(BattleFieldScroeRank _o_) {
		this(_o_, null, null);
	}

	BattleFieldScroeRank(xbean.BattleFieldScroeRank _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BattleFieldScroeRank) assign((BattleFieldScroeRank)_o1_);
		else if (_o1_ instanceof BattleFieldScroeRank.Data) assign((BattleFieldScroeRank.Data)_o1_);
		else if (_o1_ instanceof BattleFieldScroeRank.Const) assign(((BattleFieldScroeRank.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BattleFieldScroeRank _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		scroe = _o_.scroe;
	}

	private void assign(BattleFieldScroeRank.Data _o_) {
		roleid = _o_.roleid;
		scroe = _o_.scroe;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(scroe);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		scroe = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.BattleFieldScroeRank copy() {
		_xdb_verify_unsafe_();
		return new BattleFieldScroeRank(this);
	}

	@Override
	public xbean.BattleFieldScroeRank toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleFieldScroeRank toBean() {
		_xdb_verify_unsafe_();
		return new BattleFieldScroeRank(this); // same as copy()
	}

	@Override
	public xbean.BattleFieldScroeRank toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleFieldScroeRank toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // id by changhao
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getScroe() { // 积分 by changhao
		_xdb_verify_unsafe_();
		return scroe;
	}

	@Override
	public void setRoleid(long _v_) { // id by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setScroe(int _v_) { // 积分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "scroe") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, scroe) {
					public void rollback() { scroe = _xdb_saved; }
				};}});
		scroe = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BattleFieldScroeRank _o_ = null;
		if ( _o1_ instanceof BattleFieldScroeRank ) _o_ = (BattleFieldScroeRank)_o1_;
		else if ( _o1_ instanceof BattleFieldScroeRank.Const ) _o_ = ((BattleFieldScroeRank.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (scroe != _o_.scroe) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += scroe;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(scroe);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("scroe"));
		return lb;
	}

	private class Const implements xbean.BattleFieldScroeRank {
		BattleFieldScroeRank nThis() {
			return BattleFieldScroeRank.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BattleFieldScroeRank copy() {
			return BattleFieldScroeRank.this.copy();
		}

		@Override
		public xbean.BattleFieldScroeRank toData() {
			return BattleFieldScroeRank.this.toData();
		}

		public xbean.BattleFieldScroeRank toBean() {
			return BattleFieldScroeRank.this.toBean();
		}

		@Override
		public xbean.BattleFieldScroeRank toDataIf() {
			return BattleFieldScroeRank.this.toDataIf();
		}

		public xbean.BattleFieldScroeRank toBeanIf() {
			return BattleFieldScroeRank.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // id by changhao
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getScroe() { // 积分 by changhao
			_xdb_verify_unsafe_();
			return scroe;
		}

		@Override
		public void setRoleid(long _v_) { // id by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setScroe(int _v_) { // 积分 by changhao
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
			return BattleFieldScroeRank.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BattleFieldScroeRank.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BattleFieldScroeRank.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BattleFieldScroeRank.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BattleFieldScroeRank.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BattleFieldScroeRank.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BattleFieldScroeRank.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BattleFieldScroeRank.this.hashCode();
		}

		@Override
		public String toString() {
			return BattleFieldScroeRank.this.toString();
		}

	}

	public static final class Data implements xbean.BattleFieldScroeRank {
		private long roleid; // id by changhao
		private int scroe; // 积分 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.BattleFieldScroeRank _o1_) {
			if (_o1_ instanceof BattleFieldScroeRank) assign((BattleFieldScroeRank)_o1_);
			else if (_o1_ instanceof BattleFieldScroeRank.Data) assign((BattleFieldScroeRank.Data)_o1_);
			else if (_o1_ instanceof BattleFieldScroeRank.Const) assign(((BattleFieldScroeRank.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BattleFieldScroeRank _o_) {
			roleid = _o_.roleid;
			scroe = _o_.scroe;
		}

		private void assign(BattleFieldScroeRank.Data _o_) {
			roleid = _o_.roleid;
			scroe = _o_.scroe;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(scroe);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			scroe = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.BattleFieldScroeRank copy() {
			return new Data(this);
		}

		@Override
		public xbean.BattleFieldScroeRank toData() {
			return new Data(this);
		}

		public xbean.BattleFieldScroeRank toBean() {
			return new BattleFieldScroeRank(this, null, null);
		}

		@Override
		public xbean.BattleFieldScroeRank toDataIf() {
			return this;
		}

		public xbean.BattleFieldScroeRank toBeanIf() {
			return new BattleFieldScroeRank(this, null, null);
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
		public long getRoleid() { // id by changhao
			return roleid;
		}

		@Override
		public int getScroe() { // 积分 by changhao
			return scroe;
		}

		@Override
		public void setRoleid(long _v_) { // id by changhao
			roleid = _v_;
		}

		@Override
		public void setScroe(int _v_) { // 积分 by changhao
			scroe = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BattleFieldScroeRank.Data)) return false;
			BattleFieldScroeRank.Data _o_ = (BattleFieldScroeRank.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (scroe != _o_.scroe) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += scroe;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(scroe);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

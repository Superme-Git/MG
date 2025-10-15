
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BattleEndProcedure extends mkdb.XBean implements xbean.BattleEndProcedure {
	private int fighterid; // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
	private mkdb.Procedure endprocedure; // 战斗者出战斗时执行的Procedure

	@Override
	public void _reset_unsafe_() {
		fighterid = 0;
		endprocedure = null;
	}

	BattleEndProcedure(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		endprocedure = null;
	}

	public BattleEndProcedure() {
		this(0, null, null);
	}

	public BattleEndProcedure(BattleEndProcedure _o_) {
		this(_o_, null, null);
	}

	BattleEndProcedure(xbean.BattleEndProcedure _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public xbean.BattleEndProcedure copy() {
		_xdb_verify_unsafe_();
		return new BattleEndProcedure(this);
	}

	@Override
	public xbean.BattleEndProcedure toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleEndProcedure toBean() {
		_xdb_verify_unsafe_();
		return new BattleEndProcedure(this); // same as copy()
	}

	@Override
	public xbean.BattleEndProcedure toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleEndProcedure toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getFighterid() { // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
		_xdb_verify_unsafe_();
		return fighterid;
	}

	@Override
	public mkdb.Procedure getEndprocedure() { // 战斗者出战斗时执行的Procedure
		_xdb_verify_unsafe_();
		return endprocedure;
	}

	@Override
	public void setFighterid(int _v_) { // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fighterid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fighterid) {
					public void rollback() { fighterid = _xdb_saved; }
				};}});
		fighterid = _v_;
	}

	@Override
	public void setEndprocedure(mkdb.Procedure _v_) { // 战斗者出战斗时执行的Procedure
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "endprocedure") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<mkdb.Procedure>(this, endprocedure) {
					public void rollback() { endprocedure = _xdb_saved; }
			}; }});
		endprocedure = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BattleEndProcedure _o_ = null;
		if ( _o1_ instanceof BattleEndProcedure ) _o_ = (BattleEndProcedure)_o1_;
		else if ( _o1_ instanceof BattleEndProcedure.Const ) _o_ = ((BattleEndProcedure.Const)_o1_).nThis();
		else return false;
		if (fighterid != _o_.fighterid) return false;
		if ((null == endprocedure && null != _o_.endprocedure) || (null != endprocedure && null == _o_.endprocedure) || (null != endprocedure && null != _o_.endprocedure && !endprocedure.equals(_o_.endprocedure))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += fighterid;
		_h_ += (endprocedure == null ? 0 : endprocedure.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(fighterid);
		_sb_.append(",");
		_sb_.append(endprocedure);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fighterid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endprocedure"));
		return lb;
	}

	private class Const implements xbean.BattleEndProcedure {
		BattleEndProcedure nThis() {
			return BattleEndProcedure.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BattleEndProcedure copy() {
			return BattleEndProcedure.this.copy();
		}

		@Override
		public xbean.BattleEndProcedure toData() {
			return BattleEndProcedure.this.toData();
		}

		public xbean.BattleEndProcedure toBean() {
			return BattleEndProcedure.this.toBean();
		}

		@Override
		public xbean.BattleEndProcedure toDataIf() {
			return BattleEndProcedure.this.toDataIf();
		}

		public xbean.BattleEndProcedure toBeanIf() {
			return BattleEndProcedure.this.toBeanIf();
		}

		@Override
		public int getFighterid() { // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
			_xdb_verify_unsafe_();
			return fighterid;
		}

		@Override
		public mkdb.Procedure getEndprocedure() { // 战斗者出战斗时执行的Procedure
			_xdb_verify_unsafe_();
			return endprocedure;
		}

		@Override
		public void setFighterid(int _v_) { // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEndprocedure(mkdb.Procedure _v_) { // 战斗者出战斗时执行的Procedure
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
			return BattleEndProcedure.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BattleEndProcedure.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BattleEndProcedure.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BattleEndProcedure.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BattleEndProcedure.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BattleEndProcedure.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BattleEndProcedure.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BattleEndProcedure.this.hashCode();
		}

		@Override
		public String toString() {
			return BattleEndProcedure.this.toString();
		}

	}

	public static final class Data implements xbean.BattleEndProcedure {
		private int fighterid; // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
		private mkdb.Procedure endprocedure; // 战斗者出战斗时执行的Procedure

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			endprocedure = null;
		}

		Data(xbean.BattleEndProcedure _o1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BattleEndProcedure copy() {
			return new Data(this);
		}

		@Override
		public xbean.BattleEndProcedure toData() {
			return new Data(this);
		}

		public xbean.BattleEndProcedure toBean() {
			return new BattleEndProcedure(this, null, null);
		}

		@Override
		public xbean.BattleEndProcedure toDataIf() {
			return this;
		}

		public xbean.BattleEndProcedure toBeanIf() {
			return new BattleEndProcedure(this, null, null);
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
		public int getFighterid() { // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
			return fighterid;
		}

		@Override
		public mkdb.Procedure getEndprocedure() { // 战斗者出战斗时执行的Procedure
			return endprocedure;
		}

		@Override
		public void setFighterid(int _v_) { // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
			fighterid = _v_;
		}

		@Override
		public void setEndprocedure(mkdb.Procedure _v_) { // 战斗者出战斗时执行的Procedure
			endprocedure = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BattleEndProcedure.Data)) return false;
			BattleEndProcedure.Data _o_ = (BattleEndProcedure.Data) _o1_;
			if (fighterid != _o_.fighterid) return false;
			if ((null == endprocedure && null != _o_.endprocedure) || (null != endprocedure && null == _o_.endprocedure) || (null != endprocedure && null != _o_.endprocedure && !endprocedure.equals(_o_.endprocedure))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += fighterid;
			_h_ += (endprocedure == null ? 0 : endprocedure.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(fighterid);
			_sb_.append(",");
			_sb_.append(endprocedure);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

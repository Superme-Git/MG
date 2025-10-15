
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceTurnGroup extends mkdb.XBean implements xbean.InstanceTurnGroup {
	private int turngroup; // 轮换组id
	private int turnindex; // 轮换组当前id
	private int turntype; // 轮换类型

	@Override
	public void _reset_unsafe_() {
		turngroup = 0;
		turnindex = 0;
		turntype = 0;
	}

	InstanceTurnGroup(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public InstanceTurnGroup() {
		this(0, null, null);
	}

	public InstanceTurnGroup(InstanceTurnGroup _o_) {
		this(_o_, null, null);
	}

	InstanceTurnGroup(xbean.InstanceTurnGroup _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceTurnGroup) assign((InstanceTurnGroup)_o1_);
		else if (_o1_ instanceof InstanceTurnGroup.Data) assign((InstanceTurnGroup.Data)_o1_);
		else if (_o1_ instanceof InstanceTurnGroup.Const) assign(((InstanceTurnGroup.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceTurnGroup _o_) {
		_o_._xdb_verify_unsafe_();
		turngroup = _o_.turngroup;
		turnindex = _o_.turnindex;
		turntype = _o_.turntype;
	}

	private void assign(InstanceTurnGroup.Data _o_) {
		turngroup = _o_.turngroup;
		turnindex = _o_.turnindex;
		turntype = _o_.turntype;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(turngroup);
		_os_.marshal(turnindex);
		_os_.marshal(turntype);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		turngroup = _os_.unmarshal_int();
		turnindex = _os_.unmarshal_int();
		turntype = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.InstanceTurnGroup copy() {
		_xdb_verify_unsafe_();
		return new InstanceTurnGroup(this);
	}

	@Override
	public xbean.InstanceTurnGroup toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceTurnGroup toBean() {
		_xdb_verify_unsafe_();
		return new InstanceTurnGroup(this); // same as copy()
	}

	@Override
	public xbean.InstanceTurnGroup toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceTurnGroup toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getTurngroup() { // 轮换组id
		_xdb_verify_unsafe_();
		return turngroup;
	}

	@Override
	public int getTurnindex() { // 轮换组当前id
		_xdb_verify_unsafe_();
		return turnindex;
	}

	@Override
	public int getTurntype() { // 轮换类型
		_xdb_verify_unsafe_();
		return turntype;
	}

	@Override
	public void setTurngroup(int _v_) { // 轮换组id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "turngroup") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, turngroup) {
					public void rollback() { turngroup = _xdb_saved; }
				};}});
		turngroup = _v_;
	}

	@Override
	public void setTurnindex(int _v_) { // 轮换组当前id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "turnindex") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, turnindex) {
					public void rollback() { turnindex = _xdb_saved; }
				};}});
		turnindex = _v_;
	}

	@Override
	public void setTurntype(int _v_) { // 轮换类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "turntype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, turntype) {
					public void rollback() { turntype = _xdb_saved; }
				};}});
		turntype = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceTurnGroup _o_ = null;
		if ( _o1_ instanceof InstanceTurnGroup ) _o_ = (InstanceTurnGroup)_o1_;
		else if ( _o1_ instanceof InstanceTurnGroup.Const ) _o_ = ((InstanceTurnGroup.Const)_o1_).nThis();
		else return false;
		if (turngroup != _o_.turngroup) return false;
		if (turnindex != _o_.turnindex) return false;
		if (turntype != _o_.turntype) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += turngroup;
		_h_ += turnindex;
		_h_ += turntype;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(turngroup);
		_sb_.append(",");
		_sb_.append(turnindex);
		_sb_.append(",");
		_sb_.append(turntype);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("turngroup"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("turnindex"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("turntype"));
		return lb;
	}

	private class Const implements xbean.InstanceTurnGroup {
		InstanceTurnGroup nThis() {
			return InstanceTurnGroup.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceTurnGroup copy() {
			return InstanceTurnGroup.this.copy();
		}

		@Override
		public xbean.InstanceTurnGroup toData() {
			return InstanceTurnGroup.this.toData();
		}

		public xbean.InstanceTurnGroup toBean() {
			return InstanceTurnGroup.this.toBean();
		}

		@Override
		public xbean.InstanceTurnGroup toDataIf() {
			return InstanceTurnGroup.this.toDataIf();
		}

		public xbean.InstanceTurnGroup toBeanIf() {
			return InstanceTurnGroup.this.toBeanIf();
		}

		@Override
		public int getTurngroup() { // 轮换组id
			_xdb_verify_unsafe_();
			return turngroup;
		}

		@Override
		public int getTurnindex() { // 轮换组当前id
			_xdb_verify_unsafe_();
			return turnindex;
		}

		@Override
		public int getTurntype() { // 轮换类型
			_xdb_verify_unsafe_();
			return turntype;
		}

		@Override
		public void setTurngroup(int _v_) { // 轮换组id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTurnindex(int _v_) { // 轮换组当前id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTurntype(int _v_) { // 轮换类型
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
			return InstanceTurnGroup.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceTurnGroup.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceTurnGroup.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceTurnGroup.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceTurnGroup.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceTurnGroup.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceTurnGroup.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceTurnGroup.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceTurnGroup.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceTurnGroup {
		private int turngroup; // 轮换组id
		private int turnindex; // 轮换组当前id
		private int turntype; // 轮换类型

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.InstanceTurnGroup _o1_) {
			if (_o1_ instanceof InstanceTurnGroup) assign((InstanceTurnGroup)_o1_);
			else if (_o1_ instanceof InstanceTurnGroup.Data) assign((InstanceTurnGroup.Data)_o1_);
			else if (_o1_ instanceof InstanceTurnGroup.Const) assign(((InstanceTurnGroup.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceTurnGroup _o_) {
			turngroup = _o_.turngroup;
			turnindex = _o_.turnindex;
			turntype = _o_.turntype;
		}

		private void assign(InstanceTurnGroup.Data _o_) {
			turngroup = _o_.turngroup;
			turnindex = _o_.turnindex;
			turntype = _o_.turntype;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(turngroup);
			_os_.marshal(turnindex);
			_os_.marshal(turntype);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			turngroup = _os_.unmarshal_int();
			turnindex = _os_.unmarshal_int();
			turntype = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.InstanceTurnGroup copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceTurnGroup toData() {
			return new Data(this);
		}

		public xbean.InstanceTurnGroup toBean() {
			return new InstanceTurnGroup(this, null, null);
		}

		@Override
		public xbean.InstanceTurnGroup toDataIf() {
			return this;
		}

		public xbean.InstanceTurnGroup toBeanIf() {
			return new InstanceTurnGroup(this, null, null);
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
		public int getTurngroup() { // 轮换组id
			return turngroup;
		}

		@Override
		public int getTurnindex() { // 轮换组当前id
			return turnindex;
		}

		@Override
		public int getTurntype() { // 轮换类型
			return turntype;
		}

		@Override
		public void setTurngroup(int _v_) { // 轮换组id
			turngroup = _v_;
		}

		@Override
		public void setTurnindex(int _v_) { // 轮换组当前id
			turnindex = _v_;
		}

		@Override
		public void setTurntype(int _v_) { // 轮换类型
			turntype = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceTurnGroup.Data)) return false;
			InstanceTurnGroup.Data _o_ = (InstanceTurnGroup.Data) _o1_;
			if (turngroup != _o_.turngroup) return false;
			if (turnindex != _o_.turnindex) return false;
			if (turntype != _o_.turntype) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += turngroup;
			_h_ += turnindex;
			_h_ += turntype;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(turngroup);
			_sb_.append(",");
			_sb_.append(turnindex);
			_sb_.append(",");
			_sb_.append(turntype);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleApplyClan extends mkdb.XBean implements xbean.RoleApplyClan {
	private long clankey; // 公会key
	private int state; // 申请状态
	private long applytiem; // 申请时间

	@Override
	public void _reset_unsafe_() {
		clankey = 0L;
		state = 0;
		applytiem = 0L;
	}

	RoleApplyClan(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RoleApplyClan() {
		this(0, null, null);
	}

	public RoleApplyClan(RoleApplyClan _o_) {
		this(_o_, null, null);
	}

	RoleApplyClan(xbean.RoleApplyClan _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleApplyClan) assign((RoleApplyClan)_o1_);
		else if (_o1_ instanceof RoleApplyClan.Data) assign((RoleApplyClan.Data)_o1_);
		else if (_o1_ instanceof RoleApplyClan.Const) assign(((RoleApplyClan.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleApplyClan _o_) {
		_o_._xdb_verify_unsafe_();
		clankey = _o_.clankey;
		state = _o_.state;
		applytiem = _o_.applytiem;
	}

	private void assign(RoleApplyClan.Data _o_) {
		clankey = _o_.clankey;
		state = _o_.state;
		applytiem = _o_.applytiem;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(clankey);
		_os_.marshal(state);
		_os_.marshal(applytiem);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		clankey = _os_.unmarshal_long();
		state = _os_.unmarshal_int();
		applytiem = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleApplyClan copy() {
		_xdb_verify_unsafe_();
		return new RoleApplyClan(this);
	}

	@Override
	public xbean.RoleApplyClan toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleApplyClan toBean() {
		_xdb_verify_unsafe_();
		return new RoleApplyClan(this); // same as copy()
	}

	@Override
	public xbean.RoleApplyClan toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleApplyClan toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getClankey() { // 公会key
		_xdb_verify_unsafe_();
		return clankey;
	}

	@Override
	public int getState() { // 申请状态
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public long getApplytiem() { // 申请时间
		_xdb_verify_unsafe_();
		return applytiem;
	}

	@Override
	public void setClankey(long _v_) { // 公会key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clankey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clankey) {
					public void rollback() { clankey = _xdb_saved; }
				};}});
		clankey = _v_;
	}

	@Override
	public void setState(int _v_) { // 申请状态
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setApplytiem(long _v_) { // 申请时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "applytiem") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, applytiem) {
					public void rollback() { applytiem = _xdb_saved; }
				};}});
		applytiem = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleApplyClan _o_ = null;
		if ( _o1_ instanceof RoleApplyClan ) _o_ = (RoleApplyClan)_o1_;
		else if ( _o1_ instanceof RoleApplyClan.Const ) _o_ = ((RoleApplyClan.Const)_o1_).nThis();
		else return false;
		if (clankey != _o_.clankey) return false;
		if (state != _o_.state) return false;
		if (applytiem != _o_.applytiem) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += clankey;
		_h_ += state;
		_h_ += applytiem;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clankey);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(applytiem);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clankey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("applytiem"));
		return lb;
	}

	private class Const implements xbean.RoleApplyClan {
		RoleApplyClan nThis() {
			return RoleApplyClan.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleApplyClan copy() {
			return RoleApplyClan.this.copy();
		}

		@Override
		public xbean.RoleApplyClan toData() {
			return RoleApplyClan.this.toData();
		}

		public xbean.RoleApplyClan toBean() {
			return RoleApplyClan.this.toBean();
		}

		@Override
		public xbean.RoleApplyClan toDataIf() {
			return RoleApplyClan.this.toDataIf();
		}

		public xbean.RoleApplyClan toBeanIf() {
			return RoleApplyClan.this.toBeanIf();
		}

		@Override
		public long getClankey() { // 公会key
			_xdb_verify_unsafe_();
			return clankey;
		}

		@Override
		public int getState() { // 申请状态
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public long getApplytiem() { // 申请时间
			_xdb_verify_unsafe_();
			return applytiem;
		}

		@Override
		public void setClankey(long _v_) { // 公会key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 申请状态
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setApplytiem(long _v_) { // 申请时间
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
			return RoleApplyClan.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleApplyClan.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleApplyClan.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleApplyClan.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleApplyClan.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleApplyClan.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleApplyClan.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleApplyClan.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleApplyClan.this.toString();
		}

	}

	public static final class Data implements xbean.RoleApplyClan {
		private long clankey; // 公会key
		private int state; // 申请状态
		private long applytiem; // 申请时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RoleApplyClan _o1_) {
			if (_o1_ instanceof RoleApplyClan) assign((RoleApplyClan)_o1_);
			else if (_o1_ instanceof RoleApplyClan.Data) assign((RoleApplyClan.Data)_o1_);
			else if (_o1_ instanceof RoleApplyClan.Const) assign(((RoleApplyClan.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleApplyClan _o_) {
			clankey = _o_.clankey;
			state = _o_.state;
			applytiem = _o_.applytiem;
		}

		private void assign(RoleApplyClan.Data _o_) {
			clankey = _o_.clankey;
			state = _o_.state;
			applytiem = _o_.applytiem;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(clankey);
			_os_.marshal(state);
			_os_.marshal(applytiem);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			clankey = _os_.unmarshal_long();
			state = _os_.unmarshal_int();
			applytiem = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleApplyClan copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleApplyClan toData() {
			return new Data(this);
		}

		public xbean.RoleApplyClan toBean() {
			return new RoleApplyClan(this, null, null);
		}

		@Override
		public xbean.RoleApplyClan toDataIf() {
			return this;
		}

		public xbean.RoleApplyClan toBeanIf() {
			return new RoleApplyClan(this, null, null);
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
		public long getClankey() { // 公会key
			return clankey;
		}

		@Override
		public int getState() { // 申请状态
			return state;
		}

		@Override
		public long getApplytiem() { // 申请时间
			return applytiem;
		}

		@Override
		public void setClankey(long _v_) { // 公会key
			clankey = _v_;
		}

		@Override
		public void setState(int _v_) { // 申请状态
			state = _v_;
		}

		@Override
		public void setApplytiem(long _v_) { // 申请时间
			applytiem = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleApplyClan.Data)) return false;
			RoleApplyClan.Data _o_ = (RoleApplyClan.Data) _o1_;
			if (clankey != _o_.clankey) return false;
			if (state != _o_.state) return false;
			if (applytiem != _o_.applytiem) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += clankey;
			_h_ += state;
			_h_ += applytiem;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(clankey);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(applytiem);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BestowNpcInfo extends mkdb.XBean implements xbean.BestowNpcInfo {
	private long roleid; // 宝箱刷新的角色

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
	}

	BestowNpcInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public BestowNpcInfo() {
		this(0, null, null);
	}

	public BestowNpcInfo(BestowNpcInfo _o_) {
		this(_o_, null, null);
	}

	BestowNpcInfo(xbean.BestowNpcInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BestowNpcInfo) assign((BestowNpcInfo)_o1_);
		else if (_o1_ instanceof BestowNpcInfo.Data) assign((BestowNpcInfo.Data)_o1_);
		else if (_o1_ instanceof BestowNpcInfo.Const) assign(((BestowNpcInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BestowNpcInfo _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
	}

	private void assign(BestowNpcInfo.Data _o_) {
		roleid = _o_.roleid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.BestowNpcInfo copy() {
		_xdb_verify_unsafe_();
		return new BestowNpcInfo(this);
	}

	@Override
	public xbean.BestowNpcInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BestowNpcInfo toBean() {
		_xdb_verify_unsafe_();
		return new BestowNpcInfo(this); // same as copy()
	}

	@Override
	public xbean.BestowNpcInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BestowNpcInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 宝箱刷新的角色
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public void setRoleid(long _v_) { // 宝箱刷新的角色
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BestowNpcInfo _o_ = null;
		if ( _o1_ instanceof BestowNpcInfo ) _o_ = (BestowNpcInfo)_o1_;
		else if ( _o1_ instanceof BestowNpcInfo.Const ) _o_ = ((BestowNpcInfo.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		return lb;
	}

	private class Const implements xbean.BestowNpcInfo {
		BestowNpcInfo nThis() {
			return BestowNpcInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BestowNpcInfo copy() {
			return BestowNpcInfo.this.copy();
		}

		@Override
		public xbean.BestowNpcInfo toData() {
			return BestowNpcInfo.this.toData();
		}

		public xbean.BestowNpcInfo toBean() {
			return BestowNpcInfo.this.toBean();
		}

		@Override
		public xbean.BestowNpcInfo toDataIf() {
			return BestowNpcInfo.this.toDataIf();
		}

		public xbean.BestowNpcInfo toBeanIf() {
			return BestowNpcInfo.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 宝箱刷新的角色
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public void setRoleid(long _v_) { // 宝箱刷新的角色
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
			return BestowNpcInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BestowNpcInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BestowNpcInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BestowNpcInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BestowNpcInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BestowNpcInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BestowNpcInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BestowNpcInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return BestowNpcInfo.this.toString();
		}

	}

	public static final class Data implements xbean.BestowNpcInfo {
		private long roleid; // 宝箱刷新的角色

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.BestowNpcInfo _o1_) {
			if (_o1_ instanceof BestowNpcInfo) assign((BestowNpcInfo)_o1_);
			else if (_o1_ instanceof BestowNpcInfo.Data) assign((BestowNpcInfo.Data)_o1_);
			else if (_o1_ instanceof BestowNpcInfo.Const) assign(((BestowNpcInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BestowNpcInfo _o_) {
			roleid = _o_.roleid;
		}

		private void assign(BestowNpcInfo.Data _o_) {
			roleid = _o_.roleid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.BestowNpcInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.BestowNpcInfo toData() {
			return new Data(this);
		}

		public xbean.BestowNpcInfo toBean() {
			return new BestowNpcInfo(this, null, null);
		}

		@Override
		public xbean.BestowNpcInfo toDataIf() {
			return this;
		}

		public xbean.BestowNpcInfo toBeanIf() {
			return new BestowNpcInfo(this, null, null);
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
		public long getRoleid() { // 宝箱刷新的角色
			return roleid;
		}

		@Override
		public void setRoleid(long _v_) { // 宝箱刷新的角色
			roleid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BestowNpcInfo.Data)) return false;
			BestowNpcInfo.Data _o_ = (BestowNpcInfo.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

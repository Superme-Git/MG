
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PresellItem extends mkdb.XBean implements xbean.PresellItem {
	private java.util.ArrayList<Long> roleid; // 参与预售玩家
	private long expiretime; // 到期时间

	@Override
	public void _reset_unsafe_() {
		roleid.clear();
		expiretime = 0;
	}

	PresellItem(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		roleid = new java.util.ArrayList<Long>();
		expiretime = 0;
	}

	public PresellItem() {
		this(0, null, null);
	}

	public PresellItem(PresellItem _o_) {
		this(_o_, null, null);
	}

	PresellItem(xbean.PresellItem _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PresellItem) assign((PresellItem)_o1_);
		else if (_o1_ instanceof PresellItem.Data) assign((PresellItem.Data)_o1_);
		else if (_o1_ instanceof PresellItem.Const) assign(((PresellItem.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PresellItem _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = new java.util.ArrayList<Long>();
		roleid.addAll(_o_.roleid);
		expiretime = _o_.expiretime;
	}

	private void assign(PresellItem.Data _o_) {
		roleid = new java.util.ArrayList<Long>();
		roleid.addAll(_o_.roleid);
		expiretime = _o_.expiretime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(roleid.size());
		for (Long _v_ : roleid) {
			_os_.marshal(_v_);
		}
		_os_.marshal(expiretime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			roleid.add(_v_);
		}
		expiretime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.PresellItem copy() {
		_xdb_verify_unsafe_();
		return new PresellItem(this);
	}

	@Override
	public xbean.PresellItem toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PresellItem toBean() {
		_xdb_verify_unsafe_();
		return new PresellItem(this); // same as copy()
	}

	@Override
	public xbean.PresellItem toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PresellItem toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Long> getRoleid() { // 参与预售玩家
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "roleid"), roleid);
	}

	public java.util.List<Long> getRoleidAsData() { // 参与预售玩家
		_xdb_verify_unsafe_();
		java.util.List<Long> roleid;
		PresellItem _o_ = this;
		roleid = new java.util.ArrayList<Long>();
		roleid.addAll(_o_.roleid);
		return roleid;
	}

	@Override
	public long getExpiretime() { // 到期时间
		_xdb_verify_unsafe_();
		return expiretime;
	}

	@Override
	public void setExpiretime(long _v_) { // 到期时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expiretime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expiretime) {
					public void rollback() { expiretime = _xdb_saved; }
				};}});
		expiretime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PresellItem _o_ = null;
		if ( _o1_ instanceof PresellItem ) _o_ = (PresellItem)_o1_;
		else if ( _o1_ instanceof PresellItem.Const ) _o_ = ((PresellItem.Const)_o1_).nThis();
		else return false;
		if (!roleid.equals(_o_.roleid)) return false;
		if (expiretime != _o_.expiretime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid.hashCode();
		_h_ += expiretime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(expiretime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expiretime"));
		return lb;
	}

	private class Const implements xbean.PresellItem {
		PresellItem nThis() {
			return PresellItem.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PresellItem copy() {
			return PresellItem.this.copy();
		}

		@Override
		public xbean.PresellItem toData() {
			return PresellItem.this.toData();
		}

		public xbean.PresellItem toBean() {
			return PresellItem.this.toBean();
		}

		@Override
		public xbean.PresellItem toDataIf() {
			return PresellItem.this.toDataIf();
		}

		public xbean.PresellItem toBeanIf() {
			return PresellItem.this.toBeanIf();
		}

		@Override
		public java.util.List<Long> getRoleid() { // 参与预售玩家
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(roleid);
		}

		public java.util.List<Long> getRoleidAsData() { // 参与预售玩家
			_xdb_verify_unsafe_();
			java.util.List<Long> roleid;
			PresellItem _o_ = PresellItem.this;
		roleid = new java.util.ArrayList<Long>();
		roleid.addAll(_o_.roleid);
			return roleid;
		}

		@Override
		public long getExpiretime() { // 到期时间
			_xdb_verify_unsafe_();
			return expiretime;
		}

		@Override
		public void setExpiretime(long _v_) { // 到期时间
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
			return PresellItem.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PresellItem.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PresellItem.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PresellItem.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PresellItem.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PresellItem.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PresellItem.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PresellItem.this.hashCode();
		}

		@Override
		public String toString() {
			return PresellItem.this.toString();
		}

	}

	public static final class Data implements xbean.PresellItem {
		private java.util.ArrayList<Long> roleid; // 参与预售玩家
		private long expiretime; // 到期时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			roleid = new java.util.ArrayList<Long>();
			expiretime = 0;
		}

		Data(xbean.PresellItem _o1_) {
			if (_o1_ instanceof PresellItem) assign((PresellItem)_o1_);
			else if (_o1_ instanceof PresellItem.Data) assign((PresellItem.Data)_o1_);
			else if (_o1_ instanceof PresellItem.Const) assign(((PresellItem.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PresellItem _o_) {
			roleid = new java.util.ArrayList<Long>();
			roleid.addAll(_o_.roleid);
			expiretime = _o_.expiretime;
		}

		private void assign(PresellItem.Data _o_) {
			roleid = new java.util.ArrayList<Long>();
			roleid.addAll(_o_.roleid);
			expiretime = _o_.expiretime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(roleid.size());
			for (Long _v_ : roleid) {
				_os_.marshal(_v_);
			}
			_os_.marshal(expiretime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				roleid.add(_v_);
			}
			expiretime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.PresellItem copy() {
			return new Data(this);
		}

		@Override
		public xbean.PresellItem toData() {
			return new Data(this);
		}

		public xbean.PresellItem toBean() {
			return new PresellItem(this, null, null);
		}

		@Override
		public xbean.PresellItem toDataIf() {
			return this;
		}

		public xbean.PresellItem toBeanIf() {
			return new PresellItem(this, null, null);
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
		public java.util.List<Long> getRoleid() { // 参与预售玩家
			return roleid;
		}

		@Override
		public java.util.List<Long> getRoleidAsData() { // 参与预售玩家
			return roleid;
		}

		@Override
		public long getExpiretime() { // 到期时间
			return expiretime;
		}

		@Override
		public void setExpiretime(long _v_) { // 到期时间
			expiretime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PresellItem.Data)) return false;
			PresellItem.Data _o_ = (PresellItem.Data) _o1_;
			if (!roleid.equals(_o_.roleid)) return false;
			if (expiretime != _o_.expiretime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid.hashCode();
			_h_ += expiretime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(expiretime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

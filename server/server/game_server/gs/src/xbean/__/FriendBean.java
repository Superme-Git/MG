
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FriendBean extends mkdb.XBean implements xbean.FriendBean {
	private int friendlydegrees; // 好友度

	@Override
	public void _reset_unsafe_() {
		friendlydegrees = 0;
	}

	FriendBean(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public FriendBean() {
		this(0, null, null);
	}

	public FriendBean(FriendBean _o_) {
		this(_o_, null, null);
	}

	FriendBean(xbean.FriendBean _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FriendBean) assign((FriendBean)_o1_);
		else if (_o1_ instanceof FriendBean.Data) assign((FriendBean.Data)_o1_);
		else if (_o1_ instanceof FriendBean.Const) assign(((FriendBean.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FriendBean _o_) {
		_o_._xdb_verify_unsafe_();
		friendlydegrees = _o_.friendlydegrees;
	}

	private void assign(FriendBean.Data _o_) {
		friendlydegrees = _o_.friendlydegrees;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(friendlydegrees);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		friendlydegrees = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.FriendBean copy() {
		_xdb_verify_unsafe_();
		return new FriendBean(this);
	}

	@Override
	public xbean.FriendBean toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FriendBean toBean() {
		_xdb_verify_unsafe_();
		return new FriendBean(this); // same as copy()
	}

	@Override
	public xbean.FriendBean toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FriendBean toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getFriendlydegrees() { // 好友度
		_xdb_verify_unsafe_();
		return friendlydegrees;
	}

	@Override
	public void setFriendlydegrees(int _v_) { // 好友度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "friendlydegrees") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, friendlydegrees) {
					public void rollback() { friendlydegrees = _xdb_saved; }
				};}});
		friendlydegrees = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FriendBean _o_ = null;
		if ( _o1_ instanceof FriendBean ) _o_ = (FriendBean)_o1_;
		else if ( _o1_ instanceof FriendBean.Const ) _o_ = ((FriendBean.Const)_o1_).nThis();
		else return false;
		if (friendlydegrees != _o_.friendlydegrees) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += friendlydegrees;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friendlydegrees);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("friendlydegrees"));
		return lb;
	}

	private class Const implements xbean.FriendBean {
		FriendBean nThis() {
			return FriendBean.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FriendBean copy() {
			return FriendBean.this.copy();
		}

		@Override
		public xbean.FriendBean toData() {
			return FriendBean.this.toData();
		}

		public xbean.FriendBean toBean() {
			return FriendBean.this.toBean();
		}

		@Override
		public xbean.FriendBean toDataIf() {
			return FriendBean.this.toDataIf();
		}

		public xbean.FriendBean toBeanIf() {
			return FriendBean.this.toBeanIf();
		}

		@Override
		public int getFriendlydegrees() { // 好友度
			_xdb_verify_unsafe_();
			return friendlydegrees;
		}

		@Override
		public void setFriendlydegrees(int _v_) { // 好友度
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
			return FriendBean.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FriendBean.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FriendBean.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FriendBean.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FriendBean.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FriendBean.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FriendBean.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FriendBean.this.hashCode();
		}

		@Override
		public String toString() {
			return FriendBean.this.toString();
		}

	}

	public static final class Data implements xbean.FriendBean {
		private int friendlydegrees; // 好友度

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.FriendBean _o1_) {
			if (_o1_ instanceof FriendBean) assign((FriendBean)_o1_);
			else if (_o1_ instanceof FriendBean.Data) assign((FriendBean.Data)_o1_);
			else if (_o1_ instanceof FriendBean.Const) assign(((FriendBean.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FriendBean _o_) {
			friendlydegrees = _o_.friendlydegrees;
		}

		private void assign(FriendBean.Data _o_) {
			friendlydegrees = _o_.friendlydegrees;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(friendlydegrees);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			friendlydegrees = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.FriendBean copy() {
			return new Data(this);
		}

		@Override
		public xbean.FriendBean toData() {
			return new Data(this);
		}

		public xbean.FriendBean toBean() {
			return new FriendBean(this, null, null);
		}

		@Override
		public xbean.FriendBean toDataIf() {
			return this;
		}

		public xbean.FriendBean toBeanIf() {
			return new FriendBean(this, null, null);
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
		public int getFriendlydegrees() { // 好友度
			return friendlydegrees;
		}

		@Override
		public void setFriendlydegrees(int _v_) { // 好友度
			friendlydegrees = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FriendBean.Data)) return false;
			FriendBean.Data _o_ = (FriendBean.Data) _o1_;
			if (friendlydegrees != _o_.friendlydegrees) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += friendlydegrees;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(friendlydegrees);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

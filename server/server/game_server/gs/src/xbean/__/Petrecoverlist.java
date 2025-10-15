
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Petrecoverlist extends mkdb.XBean implements xbean.Petrecoverlist {
	private java.util.LinkedList<Long> uniqids; // 宠物唯一id列表

	@Override
	public void _reset_unsafe_() {
		uniqids.clear();
	}

	Petrecoverlist(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		uniqids = new java.util.LinkedList<Long>();
	}

	public Petrecoverlist() {
		this(0, null, null);
	}

	public Petrecoverlist(Petrecoverlist _o_) {
		this(_o_, null, null);
	}

	Petrecoverlist(xbean.Petrecoverlist _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Petrecoverlist) assign((Petrecoverlist)_o1_);
		else if (_o1_ instanceof Petrecoverlist.Data) assign((Petrecoverlist.Data)_o1_);
		else if (_o1_ instanceof Petrecoverlist.Const) assign(((Petrecoverlist.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Petrecoverlist _o_) {
		_o_._xdb_verify_unsafe_();
		uniqids = new java.util.LinkedList<Long>();
		uniqids.addAll(_o_.uniqids);
	}

	private void assign(Petrecoverlist.Data _o_) {
		uniqids = new java.util.LinkedList<Long>();
		uniqids.addAll(_o_.uniqids);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(uniqids.size());
		for (Long _v_ : uniqids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			uniqids.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.Petrecoverlist copy() {
		_xdb_verify_unsafe_();
		return new Petrecoverlist(this);
	}

	@Override
	public xbean.Petrecoverlist toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Petrecoverlist toBean() {
		_xdb_verify_unsafe_();
		return new Petrecoverlist(this); // same as copy()
	}

	@Override
	public xbean.Petrecoverlist toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Petrecoverlist toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Long> getUniqids() { // 宠物唯一id列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "uniqids"), uniqids);
	}

	public java.util.List<Long> getUniqidsAsData() { // 宠物唯一id列表
		_xdb_verify_unsafe_();
		java.util.List<Long> uniqids;
		Petrecoverlist _o_ = this;
		uniqids = new java.util.LinkedList<Long>();
		uniqids.addAll(_o_.uniqids);
		return uniqids;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Petrecoverlist _o_ = null;
		if ( _o1_ instanceof Petrecoverlist ) _o_ = (Petrecoverlist)_o1_;
		else if ( _o1_ instanceof Petrecoverlist.Const ) _o_ = ((Petrecoverlist.Const)_o1_).nThis();
		else return false;
		if (!uniqids.equals(_o_.uniqids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += uniqids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(uniqids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uniqids"));
		return lb;
	}

	private class Const implements xbean.Petrecoverlist {
		Petrecoverlist nThis() {
			return Petrecoverlist.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Petrecoverlist copy() {
			return Petrecoverlist.this.copy();
		}

		@Override
		public xbean.Petrecoverlist toData() {
			return Petrecoverlist.this.toData();
		}

		public xbean.Petrecoverlist toBean() {
			return Petrecoverlist.this.toBean();
		}

		@Override
		public xbean.Petrecoverlist toDataIf() {
			return Petrecoverlist.this.toDataIf();
		}

		public xbean.Petrecoverlist toBeanIf() {
			return Petrecoverlist.this.toBeanIf();
		}

		@Override
		public java.util.List<Long> getUniqids() { // 宠物唯一id列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(uniqids);
		}

		public java.util.List<Long> getUniqidsAsData() { // 宠物唯一id列表
			_xdb_verify_unsafe_();
			java.util.List<Long> uniqids;
			Petrecoverlist _o_ = Petrecoverlist.this;
		uniqids = new java.util.LinkedList<Long>();
		uniqids.addAll(_o_.uniqids);
			return uniqids;
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
			return Petrecoverlist.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Petrecoverlist.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Petrecoverlist.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Petrecoverlist.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Petrecoverlist.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Petrecoverlist.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Petrecoverlist.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Petrecoverlist.this.hashCode();
		}

		@Override
		public String toString() {
			return Petrecoverlist.this.toString();
		}

	}

	public static final class Data implements xbean.Petrecoverlist {
		private java.util.LinkedList<Long> uniqids; // 宠物唯一id列表

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			uniqids = new java.util.LinkedList<Long>();
		}

		Data(xbean.Petrecoverlist _o1_) {
			if (_o1_ instanceof Petrecoverlist) assign((Petrecoverlist)_o1_);
			else if (_o1_ instanceof Petrecoverlist.Data) assign((Petrecoverlist.Data)_o1_);
			else if (_o1_ instanceof Petrecoverlist.Const) assign(((Petrecoverlist.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Petrecoverlist _o_) {
			uniqids = new java.util.LinkedList<Long>();
			uniqids.addAll(_o_.uniqids);
		}

		private void assign(Petrecoverlist.Data _o_) {
			uniqids = new java.util.LinkedList<Long>();
			uniqids.addAll(_o_.uniqids);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(uniqids.size());
			for (Long _v_ : uniqids) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				uniqids.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.Petrecoverlist copy() {
			return new Data(this);
		}

		@Override
		public xbean.Petrecoverlist toData() {
			return new Data(this);
		}

		public xbean.Petrecoverlist toBean() {
			return new Petrecoverlist(this, null, null);
		}

		@Override
		public xbean.Petrecoverlist toDataIf() {
			return this;
		}

		public xbean.Petrecoverlist toBeanIf() {
			return new Petrecoverlist(this, null, null);
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
		public java.util.List<Long> getUniqids() { // 宠物唯一id列表
			return uniqids;
		}

		@Override
		public java.util.List<Long> getUniqidsAsData() { // 宠物唯一id列表
			return uniqids;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Petrecoverlist.Data)) return false;
			Petrecoverlist.Data _o_ = (Petrecoverlist.Data) _o1_;
			if (!uniqids.equals(_o_.uniqids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += uniqids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(uniqids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

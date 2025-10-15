
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Itemrecoverlist extends mkdb.XBean implements xbean.Itemrecoverlist {
	private java.util.LinkedList<Long> uniqids; // 物品唯一id列表

	@Override
	public void _reset_unsafe_() {
		uniqids.clear();
	}

	Itemrecoverlist(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		uniqids = new java.util.LinkedList<Long>();
	}

	public Itemrecoverlist() {
		this(0, null, null);
	}

	public Itemrecoverlist(Itemrecoverlist _o_) {
		this(_o_, null, null);
	}

	Itemrecoverlist(xbean.Itemrecoverlist _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Itemrecoverlist) assign((Itemrecoverlist)_o1_);
		else if (_o1_ instanceof Itemrecoverlist.Data) assign((Itemrecoverlist.Data)_o1_);
		else if (_o1_ instanceof Itemrecoverlist.Const) assign(((Itemrecoverlist.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Itemrecoverlist _o_) {
		_o_._xdb_verify_unsafe_();
		uniqids = new java.util.LinkedList<Long>();
		uniqids.addAll(_o_.uniqids);
	}

	private void assign(Itemrecoverlist.Data _o_) {
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
	public xbean.Itemrecoverlist copy() {
		_xdb_verify_unsafe_();
		return new Itemrecoverlist(this);
	}

	@Override
	public xbean.Itemrecoverlist toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Itemrecoverlist toBean() {
		_xdb_verify_unsafe_();
		return new Itemrecoverlist(this); // same as copy()
	}

	@Override
	public xbean.Itemrecoverlist toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Itemrecoverlist toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Long> getUniqids() { // 物品唯一id列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "uniqids"), uniqids);
	}

	public java.util.List<Long> getUniqidsAsData() { // 物品唯一id列表
		_xdb_verify_unsafe_();
		java.util.List<Long> uniqids;
		Itemrecoverlist _o_ = this;
		uniqids = new java.util.LinkedList<Long>();
		uniqids.addAll(_o_.uniqids);
		return uniqids;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Itemrecoverlist _o_ = null;
		if ( _o1_ instanceof Itemrecoverlist ) _o_ = (Itemrecoverlist)_o1_;
		else if ( _o1_ instanceof Itemrecoverlist.Const ) _o_ = ((Itemrecoverlist.Const)_o1_).nThis();
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

	private class Const implements xbean.Itemrecoverlist {
		Itemrecoverlist nThis() {
			return Itemrecoverlist.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Itemrecoverlist copy() {
			return Itemrecoverlist.this.copy();
		}

		@Override
		public xbean.Itemrecoverlist toData() {
			return Itemrecoverlist.this.toData();
		}

		public xbean.Itemrecoverlist toBean() {
			return Itemrecoverlist.this.toBean();
		}

		@Override
		public xbean.Itemrecoverlist toDataIf() {
			return Itemrecoverlist.this.toDataIf();
		}

		public xbean.Itemrecoverlist toBeanIf() {
			return Itemrecoverlist.this.toBeanIf();
		}

		@Override
		public java.util.List<Long> getUniqids() { // 物品唯一id列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(uniqids);
		}

		public java.util.List<Long> getUniqidsAsData() { // 物品唯一id列表
			_xdb_verify_unsafe_();
			java.util.List<Long> uniqids;
			Itemrecoverlist _o_ = Itemrecoverlist.this;
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
			return Itemrecoverlist.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Itemrecoverlist.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Itemrecoverlist.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Itemrecoverlist.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Itemrecoverlist.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Itemrecoverlist.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Itemrecoverlist.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Itemrecoverlist.this.hashCode();
		}

		@Override
		public String toString() {
			return Itemrecoverlist.this.toString();
		}

	}

	public static final class Data implements xbean.Itemrecoverlist {
		private java.util.LinkedList<Long> uniqids; // 物品唯一id列表

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			uniqids = new java.util.LinkedList<Long>();
		}

		Data(xbean.Itemrecoverlist _o1_) {
			if (_o1_ instanceof Itemrecoverlist) assign((Itemrecoverlist)_o1_);
			else if (_o1_ instanceof Itemrecoverlist.Data) assign((Itemrecoverlist.Data)_o1_);
			else if (_o1_ instanceof Itemrecoverlist.Const) assign(((Itemrecoverlist.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Itemrecoverlist _o_) {
			uniqids = new java.util.LinkedList<Long>();
			uniqids.addAll(_o_.uniqids);
		}

		private void assign(Itemrecoverlist.Data _o_) {
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
		public xbean.Itemrecoverlist copy() {
			return new Data(this);
		}

		@Override
		public xbean.Itemrecoverlist toData() {
			return new Data(this);
		}

		public xbean.Itemrecoverlist toBean() {
			return new Itemrecoverlist(this, null, null);
		}

		@Override
		public xbean.Itemrecoverlist toDataIf() {
			return this;
		}

		public xbean.Itemrecoverlist toBeanIf() {
			return new Itemrecoverlist(this, null, null);
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
		public java.util.List<Long> getUniqids() { // 物品唯一id列表
			return uniqids;
		}

		@Override
		public java.util.List<Long> getUniqidsAsData() { // 物品唯一id列表
			return uniqids;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Itemrecoverlist.Data)) return false;
			Itemrecoverlist.Data _o_ = (Itemrecoverlist.Data) _o1_;
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

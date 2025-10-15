
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ItemRecycleIndex extends mkdb.XBean implements xbean.ItemRecycleIndex {
	private mkdb.util.SetX<Long> dayrecycle; // 

	@Override
	public void _reset_unsafe_() {
		dayrecycle.clear();
	}

	ItemRecycleIndex(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		dayrecycle = new mkdb.util.SetX<Long>();
	}

	public ItemRecycleIndex() {
		this(0, null, null);
	}

	public ItemRecycleIndex(ItemRecycleIndex _o_) {
		this(_o_, null, null);
	}

	ItemRecycleIndex(xbean.ItemRecycleIndex _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ItemRecycleIndex) assign((ItemRecycleIndex)_o1_);
		else if (_o1_ instanceof ItemRecycleIndex.Data) assign((ItemRecycleIndex.Data)_o1_);
		else if (_o1_ instanceof ItemRecycleIndex.Const) assign(((ItemRecycleIndex.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ItemRecycleIndex _o_) {
		_o_._xdb_verify_unsafe_();
		dayrecycle = new mkdb.util.SetX<Long>();
		dayrecycle.addAll(_o_.dayrecycle);
	}

	private void assign(ItemRecycleIndex.Data _o_) {
		dayrecycle = new mkdb.util.SetX<Long>();
		dayrecycle.addAll(_o_.dayrecycle);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(dayrecycle.size());
		for (Long _v_ : dayrecycle) {
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
			dayrecycle.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ItemRecycleIndex copy() {
		_xdb_verify_unsafe_();
		return new ItemRecycleIndex(this);
	}

	@Override
	public xbean.ItemRecycleIndex toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ItemRecycleIndex toBean() {
		_xdb_verify_unsafe_();
		return new ItemRecycleIndex(this); // same as copy()
	}

	@Override
	public xbean.ItemRecycleIndex toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ItemRecycleIndex toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Set<Long> getDayrecycle() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logSet(new mkdb.LogKey(this, "dayrecycle"), dayrecycle);
	}

	public java.util.Set<Long> getDayrecycleAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Set<Long> dayrecycle;
		ItemRecycleIndex _o_ = this;
		dayrecycle = new mkdb.util.SetX<Long>();
		dayrecycle.addAll(_o_.dayrecycle);
		return dayrecycle;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ItemRecycleIndex _o_ = null;
		if ( _o1_ instanceof ItemRecycleIndex ) _o_ = (ItemRecycleIndex)_o1_;
		else if ( _o1_ instanceof ItemRecycleIndex.Const ) _o_ = ((ItemRecycleIndex.Const)_o1_).nThis();
		else return false;
		if (!dayrecycle.equals(_o_.dayrecycle)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += dayrecycle.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(dayrecycle);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableSet().setVarName("dayrecycle"));
		return lb;
	}

	private class Const implements xbean.ItemRecycleIndex {
		ItemRecycleIndex nThis() {
			return ItemRecycleIndex.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ItemRecycleIndex copy() {
			return ItemRecycleIndex.this.copy();
		}

		@Override
		public xbean.ItemRecycleIndex toData() {
			return ItemRecycleIndex.this.toData();
		}

		public xbean.ItemRecycleIndex toBean() {
			return ItemRecycleIndex.this.toBean();
		}

		@Override
		public xbean.ItemRecycleIndex toDataIf() {
			return ItemRecycleIndex.this.toDataIf();
		}

		public xbean.ItemRecycleIndex toBeanIf() {
			return ItemRecycleIndex.this.toBeanIf();
		}

		@Override
		public java.util.Set<Long> getDayrecycle() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constSet(dayrecycle);
		}

		public java.util.Set<Long> getDayrecycleAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Set<Long> dayrecycle;
			ItemRecycleIndex _o_ = ItemRecycleIndex.this;
		dayrecycle = new mkdb.util.SetX<Long>();
		dayrecycle.addAll(_o_.dayrecycle);
			return dayrecycle;
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
			return ItemRecycleIndex.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ItemRecycleIndex.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ItemRecycleIndex.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ItemRecycleIndex.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ItemRecycleIndex.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ItemRecycleIndex.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ItemRecycleIndex.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ItemRecycleIndex.this.hashCode();
		}

		@Override
		public String toString() {
			return ItemRecycleIndex.this.toString();
		}

	}

	public static final class Data implements xbean.ItemRecycleIndex {
		private java.util.HashSet<Long> dayrecycle; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			dayrecycle = new java.util.HashSet<Long>();
		}

		Data(xbean.ItemRecycleIndex _o1_) {
			if (_o1_ instanceof ItemRecycleIndex) assign((ItemRecycleIndex)_o1_);
			else if (_o1_ instanceof ItemRecycleIndex.Data) assign((ItemRecycleIndex.Data)_o1_);
			else if (_o1_ instanceof ItemRecycleIndex.Const) assign(((ItemRecycleIndex.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ItemRecycleIndex _o_) {
			dayrecycle = new java.util.HashSet<Long>();
			dayrecycle.addAll(_o_.dayrecycle);
		}

		private void assign(ItemRecycleIndex.Data _o_) {
			dayrecycle = new java.util.HashSet<Long>();
			dayrecycle.addAll(_o_.dayrecycle);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(dayrecycle.size());
			for (Long _v_ : dayrecycle) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				dayrecycle.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ItemRecycleIndex copy() {
			return new Data(this);
		}

		@Override
		public xbean.ItemRecycleIndex toData() {
			return new Data(this);
		}

		public xbean.ItemRecycleIndex toBean() {
			return new ItemRecycleIndex(this, null, null);
		}

		@Override
		public xbean.ItemRecycleIndex toDataIf() {
			return this;
		}

		public xbean.ItemRecycleIndex toBeanIf() {
			return new ItemRecycleIndex(this, null, null);
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
		public java.util.Set<Long> getDayrecycle() { // 
			return dayrecycle;
		}

		@Override
		public java.util.Set<Long> getDayrecycleAsData() { // 
			return dayrecycle;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ItemRecycleIndex.Data)) return false;
			ItemRecycleIndex.Data _o_ = (ItemRecycleIndex.Data) _o1_;
			if (!dayrecycle.equals(_o_.dayrecycle)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += dayrecycle.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(dayrecycle);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

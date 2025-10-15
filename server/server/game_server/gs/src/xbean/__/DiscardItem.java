
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class DiscardItem extends mkdb.XBean implements xbean.DiscardItem {
	private xbean.Item item; // 物品固有属性
	private long deletedate; // 删除日期

	@Override
	public void _reset_unsafe_() {
		item._reset_unsafe_();
		deletedate = 0L;
	}

	DiscardItem(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		item = new Item(0, this, "item");
	}

	public DiscardItem() {
		this(0, null, null);
	}

	public DiscardItem(DiscardItem _o_) {
		this(_o_, null, null);
	}

	DiscardItem(xbean.DiscardItem _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof DiscardItem) assign((DiscardItem)_o1_);
		else if (_o1_ instanceof DiscardItem.Data) assign((DiscardItem.Data)_o1_);
		else if (_o1_ instanceof DiscardItem.Const) assign(((DiscardItem.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(DiscardItem _o_) {
		_o_._xdb_verify_unsafe_();
		item = new Item(_o_.item, this, "item");
		deletedate = _o_.deletedate;
	}

	private void assign(DiscardItem.Data _o_) {
		item = new Item(_o_.item, this, "item");
		deletedate = _o_.deletedate;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		item.marshal(_os_);
		_os_.marshal(deletedate);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		item.unmarshal(_os_);
		deletedate = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.DiscardItem copy() {
		_xdb_verify_unsafe_();
		return new DiscardItem(this);
	}

	@Override
	public xbean.DiscardItem toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DiscardItem toBean() {
		_xdb_verify_unsafe_();
		return new DiscardItem(this); // same as copy()
	}

	@Override
	public xbean.DiscardItem toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DiscardItem toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public xbean.Item getItem() { // 物品固有属性
		_xdb_verify_unsafe_();
		return item;
	}

	@Override
	public long getDeletedate() { // 删除日期
		_xdb_verify_unsafe_();
		return deletedate;
	}

	@Override
	public void setDeletedate(long _v_) { // 删除日期
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "deletedate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, deletedate) {
					public void rollback() { deletedate = _xdb_saved; }
				};}});
		deletedate = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		DiscardItem _o_ = null;
		if ( _o1_ instanceof DiscardItem ) _o_ = (DiscardItem)_o1_;
		else if ( _o1_ instanceof DiscardItem.Const ) _o_ = ((DiscardItem.Const)_o1_).nThis();
		else return false;
		if (!item.equals(_o_.item)) return false;
		if (deletedate != _o_.deletedate) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += item.hashCode();
		_h_ += deletedate;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(item);
		_sb_.append(",");
		_sb_.append(deletedate);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("item"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("deletedate"));
		return lb;
	}

	private class Const implements xbean.DiscardItem {
		DiscardItem nThis() {
			return DiscardItem.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.DiscardItem copy() {
			return DiscardItem.this.copy();
		}

		@Override
		public xbean.DiscardItem toData() {
			return DiscardItem.this.toData();
		}

		public xbean.DiscardItem toBean() {
			return DiscardItem.this.toBean();
		}

		@Override
		public xbean.DiscardItem toDataIf() {
			return DiscardItem.this.toDataIf();
		}

		public xbean.DiscardItem toBeanIf() {
			return DiscardItem.this.toBeanIf();
		}

		@Override
		public xbean.Item getItem() { // 物品固有属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(item);
		}

		@Override
		public long getDeletedate() { // 删除日期
			_xdb_verify_unsafe_();
			return deletedate;
		}

		@Override
		public void setDeletedate(long _v_) { // 删除日期
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
			return DiscardItem.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return DiscardItem.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return DiscardItem.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return DiscardItem.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return DiscardItem.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return DiscardItem.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return DiscardItem.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return DiscardItem.this.hashCode();
		}

		@Override
		public String toString() {
			return DiscardItem.this.toString();
		}

	}

	public static final class Data implements xbean.DiscardItem {
		private xbean.Item item; // 物品固有属性
		private long deletedate; // 删除日期

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			item = new Item.Data();
		}

		Data(xbean.DiscardItem _o1_) {
			if (_o1_ instanceof DiscardItem) assign((DiscardItem)_o1_);
			else if (_o1_ instanceof DiscardItem.Data) assign((DiscardItem.Data)_o1_);
			else if (_o1_ instanceof DiscardItem.Const) assign(((DiscardItem.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(DiscardItem _o_) {
			item = new Item.Data(_o_.item);
			deletedate = _o_.deletedate;
		}

		private void assign(DiscardItem.Data _o_) {
			item = new Item.Data(_o_.item);
			deletedate = _o_.deletedate;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			item.marshal(_os_);
			_os_.marshal(deletedate);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			item.unmarshal(_os_);
			deletedate = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.DiscardItem copy() {
			return new Data(this);
		}

		@Override
		public xbean.DiscardItem toData() {
			return new Data(this);
		}

		public xbean.DiscardItem toBean() {
			return new DiscardItem(this, null, null);
		}

		@Override
		public xbean.DiscardItem toDataIf() {
			return this;
		}

		public xbean.DiscardItem toBeanIf() {
			return new DiscardItem(this, null, null);
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
		public xbean.Item getItem() { // 物品固有属性
			return item;
		}

		@Override
		public long getDeletedate() { // 删除日期
			return deletedate;
		}

		@Override
		public void setDeletedate(long _v_) { // 删除日期
			deletedate = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof DiscardItem.Data)) return false;
			DiscardItem.Data _o_ = (DiscardItem.Data) _o1_;
			if (!item.equals(_o_.item)) return false;
			if (deletedate != _o_.deletedate) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += item.hashCode();
			_h_ += deletedate;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(item);
			_sb_.append(",");
			_sb_.append(deletedate);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

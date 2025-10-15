
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ItemUse extends mkdb.XBean implements xbean.ItemUse {
	private java.util.HashMap<Integer, xbean.ItemUseCount> iteminfo; // 每个道具的使用次数

	@Override
	public void _reset_unsafe_() {
		iteminfo.clear();
	}

	ItemUse(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>();
	}

	public ItemUse() {
		this(0, null, null);
	}

	public ItemUse(ItemUse _o_) {
		this(_o_, null, null);
	}

	ItemUse(xbean.ItemUse _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ItemUse) assign((ItemUse)_o1_);
		else if (_o1_ instanceof ItemUse.Data) assign((ItemUse.Data)_o1_);
		else if (_o1_ instanceof ItemUse.Const) assign(((ItemUse.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ItemUse _o_) {
		_o_._xdb_verify_unsafe_();
		iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>();
		for (java.util.Map.Entry<Integer, xbean.ItemUseCount> _e_ : _o_.iteminfo.entrySet())
			iteminfo.put(_e_.getKey(), new ItemUseCount(_e_.getValue(), this, "iteminfo"));
	}

	private void assign(ItemUse.Data _o_) {
		iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>();
		for (java.util.Map.Entry<Integer, xbean.ItemUseCount> _e_ : _o_.iteminfo.entrySet())
			iteminfo.put(_e_.getKey(), new ItemUseCount(_e_.getValue(), this, "iteminfo"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(iteminfo.size());
		for (java.util.Map.Entry<Integer, xbean.ItemUseCount> _e_ : iteminfo.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.ItemUseCount _v_ = new ItemUseCount(0, this, "iteminfo");
				_v_.unmarshal(_os_);
				iteminfo.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ItemUse copy() {
		_xdb_verify_unsafe_();
		return new ItemUse(this);
	}

	@Override
	public xbean.ItemUse toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ItemUse toBean() {
		_xdb_verify_unsafe_();
		return new ItemUse(this); // same as copy()
	}

	@Override
	public xbean.ItemUse toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ItemUse toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.ItemUseCount> getIteminfo() { // 每个道具的使用次数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "iteminfo"), iteminfo);
	}

	@Override
	public java.util.Map<Integer, xbean.ItemUseCount> getIteminfoAsData() { // 每个道具的使用次数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.ItemUseCount> iteminfo;
		ItemUse _o_ = this;
		iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>();
		for (java.util.Map.Entry<Integer, xbean.ItemUseCount> _e_ : _o_.iteminfo.entrySet())
			iteminfo.put(_e_.getKey(), new ItemUseCount.Data(_e_.getValue()));
		return iteminfo;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ItemUse _o_ = null;
		if ( _o1_ instanceof ItemUse ) _o_ = (ItemUse)_o1_;
		else if ( _o1_ instanceof ItemUse.Const ) _o_ = ((ItemUse.Const)_o1_).nThis();
		else return false;
		if (!iteminfo.equals(_o_.iteminfo)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += iteminfo.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(iteminfo);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("iteminfo"));
		return lb;
	}

	private class Const implements xbean.ItemUse {
		ItemUse nThis() {
			return ItemUse.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ItemUse copy() {
			return ItemUse.this.copy();
		}

		@Override
		public xbean.ItemUse toData() {
			return ItemUse.this.toData();
		}

		public xbean.ItemUse toBean() {
			return ItemUse.this.toBean();
		}

		@Override
		public xbean.ItemUse toDataIf() {
			return ItemUse.this.toDataIf();
		}

		public xbean.ItemUse toBeanIf() {
			return ItemUse.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.ItemUseCount> getIteminfo() { // 每个道具的使用次数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(iteminfo);
		}

		@Override
		public java.util.Map<Integer, xbean.ItemUseCount> getIteminfoAsData() { // 每个道具的使用次数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.ItemUseCount> iteminfo;
			ItemUse _o_ = ItemUse.this;
			iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>();
			for (java.util.Map.Entry<Integer, xbean.ItemUseCount> _e_ : _o_.iteminfo.entrySet())
				iteminfo.put(_e_.getKey(), new ItemUseCount.Data(_e_.getValue()));
			return iteminfo;
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
			return ItemUse.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ItemUse.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ItemUse.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ItemUse.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ItemUse.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ItemUse.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ItemUse.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ItemUse.this.hashCode();
		}

		@Override
		public String toString() {
			return ItemUse.this.toString();
		}

	}

	public static final class Data implements xbean.ItemUse {
		private java.util.HashMap<Integer, xbean.ItemUseCount> iteminfo; // 每个道具的使用次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>();
		}

		Data(xbean.ItemUse _o1_) {
			if (_o1_ instanceof ItemUse) assign((ItemUse)_o1_);
			else if (_o1_ instanceof ItemUse.Data) assign((ItemUse.Data)_o1_);
			else if (_o1_ instanceof ItemUse.Const) assign(((ItemUse.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ItemUse _o_) {
			iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>();
			for (java.util.Map.Entry<Integer, xbean.ItemUseCount> _e_ : _o_.iteminfo.entrySet())
				iteminfo.put(_e_.getKey(), new ItemUseCount.Data(_e_.getValue()));
		}

		private void assign(ItemUse.Data _o_) {
			iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>();
			for (java.util.Map.Entry<Integer, xbean.ItemUseCount> _e_ : _o_.iteminfo.entrySet())
				iteminfo.put(_e_.getKey(), new ItemUseCount.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(iteminfo.size());
			for (java.util.Map.Entry<Integer, xbean.ItemUseCount> _e_ : iteminfo.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					iteminfo = new java.util.HashMap<Integer, xbean.ItemUseCount>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.ItemUseCount _v_ = xbean.Pod.newItemUseCountData();
					_v_.unmarshal(_os_);
					iteminfo.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ItemUse copy() {
			return new Data(this);
		}

		@Override
		public xbean.ItemUse toData() {
			return new Data(this);
		}

		public xbean.ItemUse toBean() {
			return new ItemUse(this, null, null);
		}

		@Override
		public xbean.ItemUse toDataIf() {
			return this;
		}

		public xbean.ItemUse toBeanIf() {
			return new ItemUse(this, null, null);
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
		public java.util.Map<Integer, xbean.ItemUseCount> getIteminfo() { // 每个道具的使用次数
			return iteminfo;
		}

		@Override
		public java.util.Map<Integer, xbean.ItemUseCount> getIteminfoAsData() { // 每个道具的使用次数
			return iteminfo;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ItemUse.Data)) return false;
			ItemUse.Data _o_ = (ItemUse.Data) _o1_;
			if (!iteminfo.equals(_o_.iteminfo)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += iteminfo.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(iteminfo);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

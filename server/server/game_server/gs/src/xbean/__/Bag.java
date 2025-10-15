
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Bag extends mkdb.XBean implements xbean.Bag {
	private java.util.HashMap<Integer, Long> currency; // 货币, key 为枚举值, value为货币值
	private int capacity; // 
	private int nextid; // 
	private java.util.HashMap<Integer, xbean.Item> items; // 
	private int locked; // 0 没有锁。1 有锁

	@Override
	public void _reset_unsafe_() {
		currency.clear();
		capacity = 0;
		nextid = 0;
		items.clear();
		locked = 0;
	}

	Bag(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		currency = new java.util.HashMap<Integer, Long>();
		items = new java.util.HashMap<Integer, xbean.Item>();
		locked = 0;
	}

	public Bag() {
		this(0, null, null);
	}

	public Bag(Bag _o_) {
		this(_o_, null, null);
	}

	Bag(xbean.Bag _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Bag) assign((Bag)_o1_);
		else if (_o1_ instanceof Bag.Data) assign((Bag.Data)_o1_);
		else if (_o1_ instanceof Bag.Const) assign(((Bag.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Bag _o_) {
		_o_._xdb_verify_unsafe_();
		currency = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
			currency.put(_e_.getKey(), _e_.getValue());
		capacity = _o_.capacity;
		nextid = _o_.nextid;
		items = new java.util.HashMap<Integer, xbean.Item>();
		for (java.util.Map.Entry<Integer, xbean.Item> _e_ : _o_.items.entrySet())
			items.put(_e_.getKey(), new Item(_e_.getValue(), this, "items"));
		locked = _o_.locked;
	}

	private void assign(Bag.Data _o_) {
		currency = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
			currency.put(_e_.getKey(), _e_.getValue());
		capacity = _o_.capacity;
		nextid = _o_.nextid;
		items = new java.util.HashMap<Integer, xbean.Item>();
		for (java.util.Map.Entry<Integer, xbean.Item> _e_ : _o_.items.entrySet())
			items.put(_e_.getKey(), new Item(_e_.getValue(), this, "items"));
		locked = _o_.locked;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(currency.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : currency.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(capacity);
		_os_.marshal(nextid);
		_os_.compact_uint32(items.size());
		for (java.util.Map.Entry<Integer, xbean.Item> _e_ : items.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(locked);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				currency = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				currency.put(_k_, _v_);
			}
		}
		capacity = _os_.unmarshal_int();
		nextid = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				items = new java.util.HashMap<Integer, xbean.Item>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Item _v_ = new Item(0, this, "items");
				_v_.unmarshal(_os_);
				items.put(_k_, _v_);
			}
		}
		locked = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.Bag copy() {
		_xdb_verify_unsafe_();
		return new Bag(this);
	}

	@Override
	public xbean.Bag toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Bag toBean() {
		_xdb_verify_unsafe_();
		return new Bag(this); // same as copy()
	}

	@Override
	public xbean.Bag toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Bag toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getCurrency() { // 货币, key 为枚举值, value为货币值
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "currency"), currency);
	}

	@Override
	public java.util.Map<Integer, Long> getCurrencyAsData() { // 货币, key 为枚举值, value为货币值
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> currency;
		Bag _o_ = this;
		currency = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
			currency.put(_e_.getKey(), _e_.getValue());
		return currency;
	}

	@Override
	public int getCapacity() { // 
		_xdb_verify_unsafe_();
		return capacity;
	}

	@Override
	public int getNextid() { // 
		_xdb_verify_unsafe_();
		return nextid;
	}

	@Override
	public java.util.Map<Integer, xbean.Item> getItems() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "items"), items);
	}

	@Override
	public java.util.Map<Integer, xbean.Item> getItemsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.Item> items;
		Bag _o_ = this;
		items = new java.util.HashMap<Integer, xbean.Item>();
		for (java.util.Map.Entry<Integer, xbean.Item> _e_ : _o_.items.entrySet())
			items.put(_e_.getKey(), new Item.Data(_e_.getValue()));
		return items;
	}

	@Override
	public int getLocked() { // 0 没有锁。1 有锁
		_xdb_verify_unsafe_();
		return locked;
	}

	@Override
	public void setCapacity(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "capacity") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, capacity) {
					public void rollback() { capacity = _xdb_saved; }
				};}});
		capacity = _v_;
	}

	@Override
	public void setNextid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "nextid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, nextid) {
					public void rollback() { nextid = _xdb_saved; }
				};}});
		nextid = _v_;
	}

	@Override
	public void setLocked(int _v_) { // 0 没有锁。1 有锁
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "locked") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, locked) {
					public void rollback() { locked = _xdb_saved; }
				};}});
		locked = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Bag _o_ = null;
		if ( _o1_ instanceof Bag ) _o_ = (Bag)_o1_;
		else if ( _o1_ instanceof Bag.Const ) _o_ = ((Bag.Const)_o1_).nThis();
		else return false;
		if (!currency.equals(_o_.currency)) return false;
		if (capacity != _o_.capacity) return false;
		if (nextid != _o_.nextid) return false;
		if (!items.equals(_o_.items)) return false;
		if (locked != _o_.locked) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += currency.hashCode();
		_h_ += capacity;
		_h_ += nextid;
		_h_ += items.hashCode();
		_h_ += locked;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(currency);
		_sb_.append(",");
		_sb_.append(capacity);
		_sb_.append(",");
		_sb_.append(nextid);
		_sb_.append(",");
		_sb_.append(items);
		_sb_.append(",");
		_sb_.append(locked);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("currency"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("capacity"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("nextid"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("items"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("locked"));
		return lb;
	}

	private class Const implements xbean.Bag {
		Bag nThis() {
			return Bag.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Bag copy() {
			return Bag.this.copy();
		}

		@Override
		public xbean.Bag toData() {
			return Bag.this.toData();
		}

		public xbean.Bag toBean() {
			return Bag.this.toBean();
		}

		@Override
		public xbean.Bag toDataIf() {
			return Bag.this.toDataIf();
		}

		public xbean.Bag toBeanIf() {
			return Bag.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getCurrency() { // 货币, key 为枚举值, value为货币值
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(currency);
		}

		@Override
		public java.util.Map<Integer, Long> getCurrencyAsData() { // 货币, key 为枚举值, value为货币值
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> currency;
			Bag _o_ = Bag.this;
			currency = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
				currency.put(_e_.getKey(), _e_.getValue());
			return currency;
		}

		@Override
		public int getCapacity() { // 
			_xdb_verify_unsafe_();
			return capacity;
		}

		@Override
		public int getNextid() { // 
			_xdb_verify_unsafe_();
			return nextid;
		}

		@Override
		public java.util.Map<Integer, xbean.Item> getItems() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(items);
		}

		@Override
		public java.util.Map<Integer, xbean.Item> getItemsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.Item> items;
			Bag _o_ = Bag.this;
			items = new java.util.HashMap<Integer, xbean.Item>();
			for (java.util.Map.Entry<Integer, xbean.Item> _e_ : _o_.items.entrySet())
				items.put(_e_.getKey(), new Item.Data(_e_.getValue()));
			return items;
		}

		@Override
		public int getLocked() { // 0 没有锁。1 有锁
			_xdb_verify_unsafe_();
			return locked;
		}

		@Override
		public void setCapacity(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNextid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLocked(int _v_) { // 0 没有锁。1 有锁
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
			return Bag.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Bag.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Bag.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Bag.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Bag.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Bag.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Bag.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Bag.this.hashCode();
		}

		@Override
		public String toString() {
			return Bag.this.toString();
		}

	}

	public static final class Data implements xbean.Bag {
		private java.util.HashMap<Integer, Long> currency; // 货币, key 为枚举值, value为货币值
		private int capacity; // 
		private int nextid; // 
		private java.util.HashMap<Integer, xbean.Item> items; // 
		private int locked; // 0 没有锁。1 有锁

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			currency = new java.util.HashMap<Integer, Long>();
			items = new java.util.HashMap<Integer, xbean.Item>();
			locked = 0;
		}

		Data(xbean.Bag _o1_) {
			if (_o1_ instanceof Bag) assign((Bag)_o1_);
			else if (_o1_ instanceof Bag.Data) assign((Bag.Data)_o1_);
			else if (_o1_ instanceof Bag.Const) assign(((Bag.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Bag _o_) {
			currency = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
				currency.put(_e_.getKey(), _e_.getValue());
			capacity = _o_.capacity;
			nextid = _o_.nextid;
			items = new java.util.HashMap<Integer, xbean.Item>();
			for (java.util.Map.Entry<Integer, xbean.Item> _e_ : _o_.items.entrySet())
				items.put(_e_.getKey(), new Item.Data(_e_.getValue()));
			locked = _o_.locked;
		}

		private void assign(Bag.Data _o_) {
			currency = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.currency.entrySet())
				currency.put(_e_.getKey(), _e_.getValue());
			capacity = _o_.capacity;
			nextid = _o_.nextid;
			items = new java.util.HashMap<Integer, xbean.Item>();
			for (java.util.Map.Entry<Integer, xbean.Item> _e_ : _o_.items.entrySet())
				items.put(_e_.getKey(), new Item.Data(_e_.getValue()));
			locked = _o_.locked;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(currency.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : currency.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(capacity);
			_os_.marshal(nextid);
			_os_.compact_uint32(items.size());
			for (java.util.Map.Entry<Integer, xbean.Item> _e_ : items.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(locked);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					currency = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					currency.put(_k_, _v_);
				}
			}
			capacity = _os_.unmarshal_int();
			nextid = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					items = new java.util.HashMap<Integer, xbean.Item>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.Item _v_ = xbean.Pod.newItemData();
					_v_.unmarshal(_os_);
					items.put(_k_, _v_);
				}
			}
			locked = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.Bag copy() {
			return new Data(this);
		}

		@Override
		public xbean.Bag toData() {
			return new Data(this);
		}

		public xbean.Bag toBean() {
			return new Bag(this, null, null);
		}

		@Override
		public xbean.Bag toDataIf() {
			return this;
		}

		public xbean.Bag toBeanIf() {
			return new Bag(this, null, null);
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
		public java.util.Map<Integer, Long> getCurrency() { // 货币, key 为枚举值, value为货币值
			return currency;
		}

		@Override
		public java.util.Map<Integer, Long> getCurrencyAsData() { // 货币, key 为枚举值, value为货币值
			return currency;
		}

		@Override
		public int getCapacity() { // 
			return capacity;
		}

		@Override
		public int getNextid() { // 
			return nextid;
		}

		@Override
		public java.util.Map<Integer, xbean.Item> getItems() { // 
			return items;
		}

		@Override
		public java.util.Map<Integer, xbean.Item> getItemsAsData() { // 
			return items;
		}

		@Override
		public int getLocked() { // 0 没有锁。1 有锁
			return locked;
		}

		@Override
		public void setCapacity(int _v_) { // 
			capacity = _v_;
		}

		@Override
		public void setNextid(int _v_) { // 
			nextid = _v_;
		}

		@Override
		public void setLocked(int _v_) { // 0 没有锁。1 有锁
			locked = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Bag.Data)) return false;
			Bag.Data _o_ = (Bag.Data) _o1_;
			if (!currency.equals(_o_.currency)) return false;
			if (capacity != _o_.capacity) return false;
			if (nextid != _o_.nextid) return false;
			if (!items.equals(_o_.items)) return false;
			if (locked != _o_.locked) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += currency.hashCode();
			_h_ += capacity;
			_h_ += nextid;
			_h_ += items.hashCode();
			_h_ += locked;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(currency);
			_sb_.append(",");
			_sb_.append(capacity);
			_sb_.append(",");
			_sb_.append(nextid);
			_sb_.append(",");
			_sb_.append(items);
			_sb_.append(",");
			_sb_.append(locked);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

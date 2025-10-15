
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ItemUseCount extends mkdb.XBean implements xbean.ItemUseCount {
	private int itemid; // 道具id
	private int usetimes; // 使用次数
	private long lastusetime; // 上次使用时间

	@Override
	public void _reset_unsafe_() {
		itemid = 0;
		usetimes = 0;
		lastusetime = 0L;
	}

	ItemUseCount(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ItemUseCount() {
		this(0, null, null);
	}

	public ItemUseCount(ItemUseCount _o_) {
		this(_o_, null, null);
	}

	ItemUseCount(xbean.ItemUseCount _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ItemUseCount) assign((ItemUseCount)_o1_);
		else if (_o1_ instanceof ItemUseCount.Data) assign((ItemUseCount.Data)_o1_);
		else if (_o1_ instanceof ItemUseCount.Const) assign(((ItemUseCount.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ItemUseCount _o_) {
		_o_._xdb_verify_unsafe_();
		itemid = _o_.itemid;
		usetimes = _o_.usetimes;
		lastusetime = _o_.lastusetime;
	}

	private void assign(ItemUseCount.Data _o_) {
		itemid = _o_.itemid;
		usetimes = _o_.usetimes;
		lastusetime = _o_.lastusetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(itemid);
		_os_.marshal(usetimes);
		_os_.marshal(lastusetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		itemid = _os_.unmarshal_int();
		usetimes = _os_.unmarshal_int();
		lastusetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ItemUseCount copy() {
		_xdb_verify_unsafe_();
		return new ItemUseCount(this);
	}

	@Override
	public xbean.ItemUseCount toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ItemUseCount toBean() {
		_xdb_verify_unsafe_();
		return new ItemUseCount(this); // same as copy()
	}

	@Override
	public xbean.ItemUseCount toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ItemUseCount toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getItemid() { // 道具id
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public int getUsetimes() { // 使用次数
		_xdb_verify_unsafe_();
		return usetimes;
	}

	@Override
	public long getLastusetime() { // 上次使用时间
		_xdb_verify_unsafe_();
		return lastusetime;
	}

	@Override
	public void setItemid(int _v_) { // 道具id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setUsetimes(int _v_) { // 使用次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "usetimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, usetimes) {
					public void rollback() { usetimes = _xdb_saved; }
				};}});
		usetimes = _v_;
	}

	@Override
	public void setLastusetime(long _v_) { // 上次使用时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastusetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastusetime) {
					public void rollback() { lastusetime = _xdb_saved; }
				};}});
		lastusetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ItemUseCount _o_ = null;
		if ( _o1_ instanceof ItemUseCount ) _o_ = (ItemUseCount)_o1_;
		else if ( _o1_ instanceof ItemUseCount.Const ) _o_ = ((ItemUseCount.Const)_o1_).nThis();
		else return false;
		if (itemid != _o_.itemid) return false;
		if (usetimes != _o_.usetimes) return false;
		if (lastusetime != _o_.lastusetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += itemid;
		_h_ += usetimes;
		_h_ += lastusetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(usetimes);
		_sb_.append(",");
		_sb_.append(lastusetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("usetimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastusetime"));
		return lb;
	}

	private class Const implements xbean.ItemUseCount {
		ItemUseCount nThis() {
			return ItemUseCount.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ItemUseCount copy() {
			return ItemUseCount.this.copy();
		}

		@Override
		public xbean.ItemUseCount toData() {
			return ItemUseCount.this.toData();
		}

		public xbean.ItemUseCount toBean() {
			return ItemUseCount.this.toBean();
		}

		@Override
		public xbean.ItemUseCount toDataIf() {
			return ItemUseCount.this.toDataIf();
		}

		public xbean.ItemUseCount toBeanIf() {
			return ItemUseCount.this.toBeanIf();
		}

		@Override
		public int getItemid() { // 道具id
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public int getUsetimes() { // 使用次数
			_xdb_verify_unsafe_();
			return usetimes;
		}

		@Override
		public long getLastusetime() { // 上次使用时间
			_xdb_verify_unsafe_();
			return lastusetime;
		}

		@Override
		public void setItemid(int _v_) { // 道具id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUsetimes(int _v_) { // 使用次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastusetime(long _v_) { // 上次使用时间
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
			return ItemUseCount.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ItemUseCount.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ItemUseCount.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ItemUseCount.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ItemUseCount.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ItemUseCount.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ItemUseCount.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ItemUseCount.this.hashCode();
		}

		@Override
		public String toString() {
			return ItemUseCount.this.toString();
		}

	}

	public static final class Data implements xbean.ItemUseCount {
		private int itemid; // 道具id
		private int usetimes; // 使用次数
		private long lastusetime; // 上次使用时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ItemUseCount _o1_) {
			if (_o1_ instanceof ItemUseCount) assign((ItemUseCount)_o1_);
			else if (_o1_ instanceof ItemUseCount.Data) assign((ItemUseCount.Data)_o1_);
			else if (_o1_ instanceof ItemUseCount.Const) assign(((ItemUseCount.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ItemUseCount _o_) {
			itemid = _o_.itemid;
			usetimes = _o_.usetimes;
			lastusetime = _o_.lastusetime;
		}

		private void assign(ItemUseCount.Data _o_) {
			itemid = _o_.itemid;
			usetimes = _o_.usetimes;
			lastusetime = _o_.lastusetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(itemid);
			_os_.marshal(usetimes);
			_os_.marshal(lastusetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			itemid = _os_.unmarshal_int();
			usetimes = _os_.unmarshal_int();
			lastusetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ItemUseCount copy() {
			return new Data(this);
		}

		@Override
		public xbean.ItemUseCount toData() {
			return new Data(this);
		}

		public xbean.ItemUseCount toBean() {
			return new ItemUseCount(this, null, null);
		}

		@Override
		public xbean.ItemUseCount toDataIf() {
			return this;
		}

		public xbean.ItemUseCount toBeanIf() {
			return new ItemUseCount(this, null, null);
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
		public int getItemid() { // 道具id
			return itemid;
		}

		@Override
		public int getUsetimes() { // 使用次数
			return usetimes;
		}

		@Override
		public long getLastusetime() { // 上次使用时间
			return lastusetime;
		}

		@Override
		public void setItemid(int _v_) { // 道具id
			itemid = _v_;
		}

		@Override
		public void setUsetimes(int _v_) { // 使用次数
			usetimes = _v_;
		}

		@Override
		public void setLastusetime(long _v_) { // 上次使用时间
			lastusetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ItemUseCount.Data)) return false;
			ItemUseCount.Data _o_ = (ItemUseCount.Data) _o1_;
			if (itemid != _o_.itemid) return false;
			if (usetimes != _o_.usetimes) return false;
			if (lastusetime != _o_.lastusetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += itemid;
			_h_ += usetimes;
			_h_ += lastusetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(usetimes);
			_sb_.append(",");
			_sb_.append(lastusetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

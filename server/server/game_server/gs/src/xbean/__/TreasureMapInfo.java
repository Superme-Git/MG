
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TreasureMapInfo extends mkdb.XBean implements xbean.TreasureMapInfo {
	private int awardid; // 奖励id
	private int tableindex; // 奖励表里的唯一id
	private int itemid; // 使用的物品id
	private int itemkey; // 物品的key

	@Override
	public void _reset_unsafe_() {
		awardid = 0;
		tableindex = 0;
		itemid = 0;
		itemkey = 0;
	}

	TreasureMapInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public TreasureMapInfo() {
		this(0, null, null);
	}

	public TreasureMapInfo(TreasureMapInfo _o_) {
		this(_o_, null, null);
	}

	TreasureMapInfo(xbean.TreasureMapInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TreasureMapInfo) assign((TreasureMapInfo)_o1_);
		else if (_o1_ instanceof TreasureMapInfo.Data) assign((TreasureMapInfo.Data)_o1_);
		else if (_o1_ instanceof TreasureMapInfo.Const) assign(((TreasureMapInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TreasureMapInfo _o_) {
		_o_._xdb_verify_unsafe_();
		awardid = _o_.awardid;
		tableindex = _o_.tableindex;
		itemid = _o_.itemid;
		itemkey = _o_.itemkey;
	}

	private void assign(TreasureMapInfo.Data _o_) {
		awardid = _o_.awardid;
		tableindex = _o_.tableindex;
		itemid = _o_.itemid;
		itemkey = _o_.itemkey;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(awardid);
		_os_.marshal(tableindex);
		_os_.marshal(itemid);
		_os_.marshal(itemkey);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		awardid = _os_.unmarshal_int();
		tableindex = _os_.unmarshal_int();
		itemid = _os_.unmarshal_int();
		itemkey = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.TreasureMapInfo copy() {
		_xdb_verify_unsafe_();
		return new TreasureMapInfo(this);
	}

	@Override
	public xbean.TreasureMapInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TreasureMapInfo toBean() {
		_xdb_verify_unsafe_();
		return new TreasureMapInfo(this); // same as copy()
	}

	@Override
	public xbean.TreasureMapInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TreasureMapInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getAwardid() { // 奖励id
		_xdb_verify_unsafe_();
		return awardid;
	}

	@Override
	public int getTableindex() { // 奖励表里的唯一id
		_xdb_verify_unsafe_();
		return tableindex;
	}

	@Override
	public int getItemid() { // 使用的物品id
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public int getItemkey() { // 物品的key
		_xdb_verify_unsafe_();
		return itemkey;
	}

	@Override
	public void setAwardid(int _v_) { // 奖励id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "awardid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, awardid) {
					public void rollback() { awardid = _xdb_saved; }
				};}});
		awardid = _v_;
	}

	@Override
	public void setTableindex(int _v_) { // 奖励表里的唯一id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tableindex") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, tableindex) {
					public void rollback() { tableindex = _xdb_saved; }
				};}});
		tableindex = _v_;
	}

	@Override
	public void setItemid(int _v_) { // 使用的物品id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setItemkey(int _v_) { // 物品的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemkey) {
					public void rollback() { itemkey = _xdb_saved; }
				};}});
		itemkey = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TreasureMapInfo _o_ = null;
		if ( _o1_ instanceof TreasureMapInfo ) _o_ = (TreasureMapInfo)_o1_;
		else if ( _o1_ instanceof TreasureMapInfo.Const ) _o_ = ((TreasureMapInfo.Const)_o1_).nThis();
		else return false;
		if (awardid != _o_.awardid) return false;
		if (tableindex != _o_.tableindex) return false;
		if (itemid != _o_.itemid) return false;
		if (itemkey != _o_.itemkey) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += awardid;
		_h_ += tableindex;
		_h_ += itemid;
		_h_ += itemkey;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(awardid);
		_sb_.append(",");
		_sb_.append(tableindex);
		_sb_.append(",");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(itemkey);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awardid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tableindex"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemkey"));
		return lb;
	}

	private class Const implements xbean.TreasureMapInfo {
		TreasureMapInfo nThis() {
			return TreasureMapInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TreasureMapInfo copy() {
			return TreasureMapInfo.this.copy();
		}

		@Override
		public xbean.TreasureMapInfo toData() {
			return TreasureMapInfo.this.toData();
		}

		public xbean.TreasureMapInfo toBean() {
			return TreasureMapInfo.this.toBean();
		}

		@Override
		public xbean.TreasureMapInfo toDataIf() {
			return TreasureMapInfo.this.toDataIf();
		}

		public xbean.TreasureMapInfo toBeanIf() {
			return TreasureMapInfo.this.toBeanIf();
		}

		@Override
		public int getAwardid() { // 奖励id
			_xdb_verify_unsafe_();
			return awardid;
		}

		@Override
		public int getTableindex() { // 奖励表里的唯一id
			_xdb_verify_unsafe_();
			return tableindex;
		}

		@Override
		public int getItemid() { // 使用的物品id
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public int getItemkey() { // 物品的key
			_xdb_verify_unsafe_();
			return itemkey;
		}

		@Override
		public void setAwardid(int _v_) { // 奖励id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTableindex(int _v_) { // 奖励表里的唯一id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemid(int _v_) { // 使用的物品id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemkey(int _v_) { // 物品的key
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
			return TreasureMapInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TreasureMapInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TreasureMapInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TreasureMapInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TreasureMapInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TreasureMapInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TreasureMapInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TreasureMapInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return TreasureMapInfo.this.toString();
		}

	}

	public static final class Data implements xbean.TreasureMapInfo {
		private int awardid; // 奖励id
		private int tableindex; // 奖励表里的唯一id
		private int itemid; // 使用的物品id
		private int itemkey; // 物品的key

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.TreasureMapInfo _o1_) {
			if (_o1_ instanceof TreasureMapInfo) assign((TreasureMapInfo)_o1_);
			else if (_o1_ instanceof TreasureMapInfo.Data) assign((TreasureMapInfo.Data)_o1_);
			else if (_o1_ instanceof TreasureMapInfo.Const) assign(((TreasureMapInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TreasureMapInfo _o_) {
			awardid = _o_.awardid;
			tableindex = _o_.tableindex;
			itemid = _o_.itemid;
			itemkey = _o_.itemkey;
		}

		private void assign(TreasureMapInfo.Data _o_) {
			awardid = _o_.awardid;
			tableindex = _o_.tableindex;
			itemid = _o_.itemid;
			itemkey = _o_.itemkey;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(awardid);
			_os_.marshal(tableindex);
			_os_.marshal(itemid);
			_os_.marshal(itemkey);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			awardid = _os_.unmarshal_int();
			tableindex = _os_.unmarshal_int();
			itemid = _os_.unmarshal_int();
			itemkey = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.TreasureMapInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.TreasureMapInfo toData() {
			return new Data(this);
		}

		public xbean.TreasureMapInfo toBean() {
			return new TreasureMapInfo(this, null, null);
		}

		@Override
		public xbean.TreasureMapInfo toDataIf() {
			return this;
		}

		public xbean.TreasureMapInfo toBeanIf() {
			return new TreasureMapInfo(this, null, null);
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
		public int getAwardid() { // 奖励id
			return awardid;
		}

		@Override
		public int getTableindex() { // 奖励表里的唯一id
			return tableindex;
		}

		@Override
		public int getItemid() { // 使用的物品id
			return itemid;
		}

		@Override
		public int getItemkey() { // 物品的key
			return itemkey;
		}

		@Override
		public void setAwardid(int _v_) { // 奖励id
			awardid = _v_;
		}

		@Override
		public void setTableindex(int _v_) { // 奖励表里的唯一id
			tableindex = _v_;
		}

		@Override
		public void setItemid(int _v_) { // 使用的物品id
			itemid = _v_;
		}

		@Override
		public void setItemkey(int _v_) { // 物品的key
			itemkey = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TreasureMapInfo.Data)) return false;
			TreasureMapInfo.Data _o_ = (TreasureMapInfo.Data) _o1_;
			if (awardid != _o_.awardid) return false;
			if (tableindex != _o_.tableindex) return false;
			if (itemid != _o_.itemid) return false;
			if (itemkey != _o_.itemkey) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += awardid;
			_h_ += tableindex;
			_h_ += itemid;
			_h_ += itemkey;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(awardid);
			_sb_.append(",");
			_sb_.append(tableindex);
			_sb_.append(",");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(itemkey);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

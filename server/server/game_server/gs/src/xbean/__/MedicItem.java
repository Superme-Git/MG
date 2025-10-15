
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MedicItem extends mkdb.XBean implements xbean.MedicItem {
	private int itemid; // 药品id
	private int itemnum; // 药品数量

	@Override
	public void _reset_unsafe_() {
		itemid = 0;
		itemnum = 0;
	}

	MedicItem(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public MedicItem() {
		this(0, null, null);
	}

	public MedicItem(MedicItem _o_) {
		this(_o_, null, null);
	}

	MedicItem(xbean.MedicItem _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MedicItem) assign((MedicItem)_o1_);
		else if (_o1_ instanceof MedicItem.Data) assign((MedicItem.Data)_o1_);
		else if (_o1_ instanceof MedicItem.Const) assign(((MedicItem.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MedicItem _o_) {
		_o_._xdb_verify_unsafe_();
		itemid = _o_.itemid;
		itemnum = _o_.itemnum;
	}

	private void assign(MedicItem.Data _o_) {
		itemid = _o_.itemid;
		itemnum = _o_.itemnum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(itemid);
		_os_.marshal(itemnum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		itemid = _os_.unmarshal_int();
		itemnum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MedicItem copy() {
		_xdb_verify_unsafe_();
		return new MedicItem(this);
	}

	@Override
	public xbean.MedicItem toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MedicItem toBean() {
		_xdb_verify_unsafe_();
		return new MedicItem(this); // same as copy()
	}

	@Override
	public xbean.MedicItem toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MedicItem toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getItemid() { // 药品id
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public int getItemnum() { // 药品数量
		_xdb_verify_unsafe_();
		return itemnum;
	}

	@Override
	public void setItemid(int _v_) { // 药品id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setItemnum(int _v_) { // 药品数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemnum) {
					public void rollback() { itemnum = _xdb_saved; }
				};}});
		itemnum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MedicItem _o_ = null;
		if ( _o1_ instanceof MedicItem ) _o_ = (MedicItem)_o1_;
		else if ( _o1_ instanceof MedicItem.Const ) _o_ = ((MedicItem.Const)_o1_).nThis();
		else return false;
		if (itemid != _o_.itemid) return false;
		if (itemnum != _o_.itemnum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += itemid;
		_h_ += itemnum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(itemnum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemnum"));
		return lb;
	}

	private class Const implements xbean.MedicItem {
		MedicItem nThis() {
			return MedicItem.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MedicItem copy() {
			return MedicItem.this.copy();
		}

		@Override
		public xbean.MedicItem toData() {
			return MedicItem.this.toData();
		}

		public xbean.MedicItem toBean() {
			return MedicItem.this.toBean();
		}

		@Override
		public xbean.MedicItem toDataIf() {
			return MedicItem.this.toDataIf();
		}

		public xbean.MedicItem toBeanIf() {
			return MedicItem.this.toBeanIf();
		}

		@Override
		public int getItemid() { // 药品id
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public int getItemnum() { // 药品数量
			_xdb_verify_unsafe_();
			return itemnum;
		}

		@Override
		public void setItemid(int _v_) { // 药品id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemnum(int _v_) { // 药品数量
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
			return MedicItem.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MedicItem.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MedicItem.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MedicItem.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MedicItem.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MedicItem.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MedicItem.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MedicItem.this.hashCode();
		}

		@Override
		public String toString() {
			return MedicItem.this.toString();
		}

	}

	public static final class Data implements xbean.MedicItem {
		private int itemid; // 药品id
		private int itemnum; // 药品数量

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.MedicItem _o1_) {
			if (_o1_ instanceof MedicItem) assign((MedicItem)_o1_);
			else if (_o1_ instanceof MedicItem.Data) assign((MedicItem.Data)_o1_);
			else if (_o1_ instanceof MedicItem.Const) assign(((MedicItem.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MedicItem _o_) {
			itemid = _o_.itemid;
			itemnum = _o_.itemnum;
		}

		private void assign(MedicItem.Data _o_) {
			itemid = _o_.itemid;
			itemnum = _o_.itemnum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(itemid);
			_os_.marshal(itemnum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			itemid = _os_.unmarshal_int();
			itemnum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MedicItem copy() {
			return new Data(this);
		}

		@Override
		public xbean.MedicItem toData() {
			return new Data(this);
		}

		public xbean.MedicItem toBean() {
			return new MedicItem(this, null, null);
		}

		@Override
		public xbean.MedicItem toDataIf() {
			return this;
		}

		public xbean.MedicItem toBeanIf() {
			return new MedicItem(this, null, null);
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
		public int getItemid() { // 药品id
			return itemid;
		}

		@Override
		public int getItemnum() { // 药品数量
			return itemnum;
		}

		@Override
		public void setItemid(int _v_) { // 药品id
			itemid = _v_;
		}

		@Override
		public void setItemnum(int _v_) { // 药品数量
			itemnum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MedicItem.Data)) return false;
			MedicItem.Data _o_ = (MedicItem.Data) _o1_;
			if (itemid != _o_.itemid) return false;
			if (itemnum != _o_.itemnum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += itemid;
			_h_ += itemnum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(itemnum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

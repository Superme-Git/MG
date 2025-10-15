
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TraderWheel extends mkdb.XBean implements xbean.TraderWheel {
	private int boxtype; // 宝箱类型
	private int itemindex; // 物品列的索引

	@Override
	public void _reset_unsafe_() {
		boxtype = 0;
		itemindex = 0;
	}

	TraderWheel(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public TraderWheel() {
		this(0, null, null);
	}

	public TraderWheel(TraderWheel _o_) {
		this(_o_, null, null);
	}

	TraderWheel(xbean.TraderWheel _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TraderWheel) assign((TraderWheel)_o1_);
		else if (_o1_ instanceof TraderWheel.Data) assign((TraderWheel.Data)_o1_);
		else if (_o1_ instanceof TraderWheel.Const) assign(((TraderWheel.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TraderWheel _o_) {
		_o_._xdb_verify_unsafe_();
		boxtype = _o_.boxtype;
		itemindex = _o_.itemindex;
	}

	private void assign(TraderWheel.Data _o_) {
		boxtype = _o_.boxtype;
		itemindex = _o_.itemindex;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(boxtype);
		_os_.marshal(itemindex);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		boxtype = _os_.unmarshal_int();
		itemindex = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.TraderWheel copy() {
		_xdb_verify_unsafe_();
		return new TraderWheel(this);
	}

	@Override
	public xbean.TraderWheel toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TraderWheel toBean() {
		_xdb_verify_unsafe_();
		return new TraderWheel(this); // same as copy()
	}

	@Override
	public xbean.TraderWheel toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TraderWheel toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBoxtype() { // 宝箱类型
		_xdb_verify_unsafe_();
		return boxtype;
	}

	@Override
	public int getItemindex() { // 物品列的索引
		_xdb_verify_unsafe_();
		return itemindex;
	}

	@Override
	public void setBoxtype(int _v_) { // 宝箱类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "boxtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, boxtype) {
					public void rollback() { boxtype = _xdb_saved; }
				};}});
		boxtype = _v_;
	}

	@Override
	public void setItemindex(int _v_) { // 物品列的索引
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemindex") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemindex) {
					public void rollback() { itemindex = _xdb_saved; }
				};}});
		itemindex = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TraderWheel _o_ = null;
		if ( _o1_ instanceof TraderWheel ) _o_ = (TraderWheel)_o1_;
		else if ( _o1_ instanceof TraderWheel.Const ) _o_ = ((TraderWheel.Const)_o1_).nThis();
		else return false;
		if (boxtype != _o_.boxtype) return false;
		if (itemindex != _o_.itemindex) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += boxtype;
		_h_ += itemindex;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(boxtype);
		_sb_.append(",");
		_sb_.append(itemindex);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("boxtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemindex"));
		return lb;
	}

	private class Const implements xbean.TraderWheel {
		TraderWheel nThis() {
			return TraderWheel.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TraderWheel copy() {
			return TraderWheel.this.copy();
		}

		@Override
		public xbean.TraderWheel toData() {
			return TraderWheel.this.toData();
		}

		public xbean.TraderWheel toBean() {
			return TraderWheel.this.toBean();
		}

		@Override
		public xbean.TraderWheel toDataIf() {
			return TraderWheel.this.toDataIf();
		}

		public xbean.TraderWheel toBeanIf() {
			return TraderWheel.this.toBeanIf();
		}

		@Override
		public int getBoxtype() { // 宝箱类型
			_xdb_verify_unsafe_();
			return boxtype;
		}

		@Override
		public int getItemindex() { // 物品列的索引
			_xdb_verify_unsafe_();
			return itemindex;
		}

		@Override
		public void setBoxtype(int _v_) { // 宝箱类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemindex(int _v_) { // 物品列的索引
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
			return TraderWheel.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TraderWheel.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TraderWheel.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TraderWheel.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TraderWheel.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TraderWheel.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TraderWheel.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TraderWheel.this.hashCode();
		}

		@Override
		public String toString() {
			return TraderWheel.this.toString();
		}

	}

	public static final class Data implements xbean.TraderWheel {
		private int boxtype; // 宝箱类型
		private int itemindex; // 物品列的索引

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.TraderWheel _o1_) {
			if (_o1_ instanceof TraderWheel) assign((TraderWheel)_o1_);
			else if (_o1_ instanceof TraderWheel.Data) assign((TraderWheel.Data)_o1_);
			else if (_o1_ instanceof TraderWheel.Const) assign(((TraderWheel.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TraderWheel _o_) {
			boxtype = _o_.boxtype;
			itemindex = _o_.itemindex;
		}

		private void assign(TraderWheel.Data _o_) {
			boxtype = _o_.boxtype;
			itemindex = _o_.itemindex;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(boxtype);
			_os_.marshal(itemindex);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			boxtype = _os_.unmarshal_int();
			itemindex = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.TraderWheel copy() {
			return new Data(this);
		}

		@Override
		public xbean.TraderWheel toData() {
			return new Data(this);
		}

		public xbean.TraderWheel toBean() {
			return new TraderWheel(this, null, null);
		}

		@Override
		public xbean.TraderWheel toDataIf() {
			return this;
		}

		public xbean.TraderWheel toBeanIf() {
			return new TraderWheel(this, null, null);
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
		public int getBoxtype() { // 宝箱类型
			return boxtype;
		}

		@Override
		public int getItemindex() { // 物品列的索引
			return itemindex;
		}

		@Override
		public void setBoxtype(int _v_) { // 宝箱类型
			boxtype = _v_;
		}

		@Override
		public void setItemindex(int _v_) { // 物品列的索引
			itemindex = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TraderWheel.Data)) return false;
			TraderWheel.Data _o_ = (TraderWheel.Data) _o1_;
			if (boxtype != _o_.boxtype) return false;
			if (itemindex != _o_.itemindex) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += boxtype;
			_h_ += itemindex;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(boxtype);
			_sb_.append(",");
			_sb_.append(itemindex);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RollItem extends mkdb.XBean implements xbean.RollItem {
	private int itemid; // 道具表id by changhao
	private int itemnum; // 数量id by changhao
	private int countertype; // 
	private int xiangguanid; // 
	private int awardid; // 

	@Override
	public void _reset_unsafe_() {
		itemid = 0;
		itemnum = 0;
		countertype = 0;
		xiangguanid = 0;
		awardid = 0;
	}

	RollItem(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RollItem() {
		this(0, null, null);
	}

	public RollItem(RollItem _o_) {
		this(_o_, null, null);
	}

	RollItem(xbean.RollItem _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RollItem) assign((RollItem)_o1_);
		else if (_o1_ instanceof RollItem.Data) assign((RollItem.Data)_o1_);
		else if (_o1_ instanceof RollItem.Const) assign(((RollItem.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RollItem _o_) {
		_o_._xdb_verify_unsafe_();
		itemid = _o_.itemid;
		itemnum = _o_.itemnum;
		countertype = _o_.countertype;
		xiangguanid = _o_.xiangguanid;
		awardid = _o_.awardid;
	}

	private void assign(RollItem.Data _o_) {
		itemid = _o_.itemid;
		itemnum = _o_.itemnum;
		countertype = _o_.countertype;
		xiangguanid = _o_.xiangguanid;
		awardid = _o_.awardid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(itemid);
		_os_.marshal(itemnum);
		_os_.marshal(countertype);
		_os_.marshal(xiangguanid);
		_os_.marshal(awardid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		itemid = _os_.unmarshal_int();
		itemnum = _os_.unmarshal_int();
		countertype = _os_.unmarshal_int();
		xiangguanid = _os_.unmarshal_int();
		awardid = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RollItem copy() {
		_xdb_verify_unsafe_();
		return new RollItem(this);
	}

	@Override
	public xbean.RollItem toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RollItem toBean() {
		_xdb_verify_unsafe_();
		return new RollItem(this); // same as copy()
	}

	@Override
	public xbean.RollItem toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RollItem toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getItemid() { // 道具表id by changhao
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public int getItemnum() { // 数量id by changhao
		_xdb_verify_unsafe_();
		return itemnum;
	}

	@Override
	public int getCountertype() { // 
		_xdb_verify_unsafe_();
		return countertype;
	}

	@Override
	public int getXiangguanid() { // 
		_xdb_verify_unsafe_();
		return xiangguanid;
	}

	@Override
	public int getAwardid() { // 
		_xdb_verify_unsafe_();
		return awardid;
	}

	@Override
	public void setItemid(int _v_) { // 道具表id by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setItemnum(int _v_) { // 数量id by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemnum) {
					public void rollback() { itemnum = _xdb_saved; }
				};}});
		itemnum = _v_;
	}

	@Override
	public void setCountertype(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "countertype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, countertype) {
					public void rollback() { countertype = _xdb_saved; }
				};}});
		countertype = _v_;
	}

	@Override
	public void setXiangguanid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "xiangguanid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, xiangguanid) {
					public void rollback() { xiangguanid = _xdb_saved; }
				};}});
		xiangguanid = _v_;
	}

	@Override
	public void setAwardid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "awardid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, awardid) {
					public void rollback() { awardid = _xdb_saved; }
				};}});
		awardid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RollItem _o_ = null;
		if ( _o1_ instanceof RollItem ) _o_ = (RollItem)_o1_;
		else if ( _o1_ instanceof RollItem.Const ) _o_ = ((RollItem.Const)_o1_).nThis();
		else return false;
		if (itemid != _o_.itemid) return false;
		if (itemnum != _o_.itemnum) return false;
		if (countertype != _o_.countertype) return false;
		if (xiangguanid != _o_.xiangguanid) return false;
		if (awardid != _o_.awardid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += itemid;
		_h_ += itemnum;
		_h_ += countertype;
		_h_ += xiangguanid;
		_h_ += awardid;
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
		_sb_.append(",");
		_sb_.append(countertype);
		_sb_.append(",");
		_sb_.append(xiangguanid);
		_sb_.append(",");
		_sb_.append(awardid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("countertype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("xiangguanid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awardid"));
		return lb;
	}

	private class Const implements xbean.RollItem {
		RollItem nThis() {
			return RollItem.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RollItem copy() {
			return RollItem.this.copy();
		}

		@Override
		public xbean.RollItem toData() {
			return RollItem.this.toData();
		}

		public xbean.RollItem toBean() {
			return RollItem.this.toBean();
		}

		@Override
		public xbean.RollItem toDataIf() {
			return RollItem.this.toDataIf();
		}

		public xbean.RollItem toBeanIf() {
			return RollItem.this.toBeanIf();
		}

		@Override
		public int getItemid() { // 道具表id by changhao
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public int getItemnum() { // 数量id by changhao
			_xdb_verify_unsafe_();
			return itemnum;
		}

		@Override
		public int getCountertype() { // 
			_xdb_verify_unsafe_();
			return countertype;
		}

		@Override
		public int getXiangguanid() { // 
			_xdb_verify_unsafe_();
			return xiangguanid;
		}

		@Override
		public int getAwardid() { // 
			_xdb_verify_unsafe_();
			return awardid;
		}

		@Override
		public void setItemid(int _v_) { // 道具表id by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemnum(int _v_) { // 数量id by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCountertype(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setXiangguanid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAwardid(int _v_) { // 
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
			return RollItem.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RollItem.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RollItem.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RollItem.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RollItem.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RollItem.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RollItem.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RollItem.this.hashCode();
		}

		@Override
		public String toString() {
			return RollItem.this.toString();
		}

	}

	public static final class Data implements xbean.RollItem {
		private int itemid; // 道具表id by changhao
		private int itemnum; // 数量id by changhao
		private int countertype; // 
		private int xiangguanid; // 
		private int awardid; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RollItem _o1_) {
			if (_o1_ instanceof RollItem) assign((RollItem)_o1_);
			else if (_o1_ instanceof RollItem.Data) assign((RollItem.Data)_o1_);
			else if (_o1_ instanceof RollItem.Const) assign(((RollItem.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RollItem _o_) {
			itemid = _o_.itemid;
			itemnum = _o_.itemnum;
			countertype = _o_.countertype;
			xiangguanid = _o_.xiangguanid;
			awardid = _o_.awardid;
		}

		private void assign(RollItem.Data _o_) {
			itemid = _o_.itemid;
			itemnum = _o_.itemnum;
			countertype = _o_.countertype;
			xiangguanid = _o_.xiangguanid;
			awardid = _o_.awardid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(itemid);
			_os_.marshal(itemnum);
			_os_.marshal(countertype);
			_os_.marshal(xiangguanid);
			_os_.marshal(awardid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			itemid = _os_.unmarshal_int();
			itemnum = _os_.unmarshal_int();
			countertype = _os_.unmarshal_int();
			xiangguanid = _os_.unmarshal_int();
			awardid = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RollItem copy() {
			return new Data(this);
		}

		@Override
		public xbean.RollItem toData() {
			return new Data(this);
		}

		public xbean.RollItem toBean() {
			return new RollItem(this, null, null);
		}

		@Override
		public xbean.RollItem toDataIf() {
			return this;
		}

		public xbean.RollItem toBeanIf() {
			return new RollItem(this, null, null);
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
		public int getItemid() { // 道具表id by changhao
			return itemid;
		}

		@Override
		public int getItemnum() { // 数量id by changhao
			return itemnum;
		}

		@Override
		public int getCountertype() { // 
			return countertype;
		}

		@Override
		public int getXiangguanid() { // 
			return xiangguanid;
		}

		@Override
		public int getAwardid() { // 
			return awardid;
		}

		@Override
		public void setItemid(int _v_) { // 道具表id by changhao
			itemid = _v_;
		}

		@Override
		public void setItemnum(int _v_) { // 数量id by changhao
			itemnum = _v_;
		}

		@Override
		public void setCountertype(int _v_) { // 
			countertype = _v_;
		}

		@Override
		public void setXiangguanid(int _v_) { // 
			xiangguanid = _v_;
		}

		@Override
		public void setAwardid(int _v_) { // 
			awardid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RollItem.Data)) return false;
			RollItem.Data _o_ = (RollItem.Data) _o1_;
			if (itemid != _o_.itemid) return false;
			if (itemnum != _o_.itemnum) return false;
			if (countertype != _o_.countertype) return false;
			if (xiangguanid != _o_.xiangguanid) return false;
			if (awardid != _o_.awardid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += itemid;
			_h_ += itemnum;
			_h_ += countertype;
			_h_ += xiangguanid;
			_h_ += awardid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(itemnum);
			_sb_.append(",");
			_sb_.append(countertype);
			_sb_.append(",");
			_sb_.append(xiangguanid);
			_sb_.append(",");
			_sb_.append(awardid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

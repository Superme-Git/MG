
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SchoolWheel extends mkdb.XBean implements xbean.SchoolWheel {
	private int awardid; // 奖励id
	private int itemindex; // 物品列的索引

	@Override
	public void _reset_unsafe_() {
		awardid = 0;
		itemindex = 0;
	}

	SchoolWheel(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public SchoolWheel() {
		this(0, null, null);
	}

	public SchoolWheel(SchoolWheel _o_) {
		this(_o_, null, null);
	}

	SchoolWheel(xbean.SchoolWheel _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SchoolWheel) assign((SchoolWheel)_o1_);
		else if (_o1_ instanceof SchoolWheel.Data) assign((SchoolWheel.Data)_o1_);
		else if (_o1_ instanceof SchoolWheel.Const) assign(((SchoolWheel.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SchoolWheel _o_) {
		_o_._xdb_verify_unsafe_();
		awardid = _o_.awardid;
		itemindex = _o_.itemindex;
	}

	private void assign(SchoolWheel.Data _o_) {
		awardid = _o_.awardid;
		itemindex = _o_.itemindex;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(awardid);
		_os_.marshal(itemindex);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		awardid = _os_.unmarshal_int();
		itemindex = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.SchoolWheel copy() {
		_xdb_verify_unsafe_();
		return new SchoolWheel(this);
	}

	@Override
	public xbean.SchoolWheel toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SchoolWheel toBean() {
		_xdb_verify_unsafe_();
		return new SchoolWheel(this); // same as copy()
	}

	@Override
	public xbean.SchoolWheel toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SchoolWheel toBeanIf() {
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
	public int getItemindex() { // 物品列的索引
		_xdb_verify_unsafe_();
		return itemindex;
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
		SchoolWheel _o_ = null;
		if ( _o1_ instanceof SchoolWheel ) _o_ = (SchoolWheel)_o1_;
		else if ( _o1_ instanceof SchoolWheel.Const ) _o_ = ((SchoolWheel.Const)_o1_).nThis();
		else return false;
		if (awardid != _o_.awardid) return false;
		if (itemindex != _o_.itemindex) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += awardid;
		_h_ += itemindex;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(awardid);
		_sb_.append(",");
		_sb_.append(itemindex);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awardid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemindex"));
		return lb;
	}

	private class Const implements xbean.SchoolWheel {
		SchoolWheel nThis() {
			return SchoolWheel.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SchoolWheel copy() {
			return SchoolWheel.this.copy();
		}

		@Override
		public xbean.SchoolWheel toData() {
			return SchoolWheel.this.toData();
		}

		public xbean.SchoolWheel toBean() {
			return SchoolWheel.this.toBean();
		}

		@Override
		public xbean.SchoolWheel toDataIf() {
			return SchoolWheel.this.toDataIf();
		}

		public xbean.SchoolWheel toBeanIf() {
			return SchoolWheel.this.toBeanIf();
		}

		@Override
		public int getAwardid() { // 奖励id
			_xdb_verify_unsafe_();
			return awardid;
		}

		@Override
		public int getItemindex() { // 物品列的索引
			_xdb_verify_unsafe_();
			return itemindex;
		}

		@Override
		public void setAwardid(int _v_) { // 奖励id
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
			return SchoolWheel.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SchoolWheel.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SchoolWheel.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SchoolWheel.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SchoolWheel.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SchoolWheel.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SchoolWheel.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SchoolWheel.this.hashCode();
		}

		@Override
		public String toString() {
			return SchoolWheel.this.toString();
		}

	}

	public static final class Data implements xbean.SchoolWheel {
		private int awardid; // 奖励id
		private int itemindex; // 物品列的索引

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.SchoolWheel _o1_) {
			if (_o1_ instanceof SchoolWheel) assign((SchoolWheel)_o1_);
			else if (_o1_ instanceof SchoolWheel.Data) assign((SchoolWheel.Data)_o1_);
			else if (_o1_ instanceof SchoolWheel.Const) assign(((SchoolWheel.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SchoolWheel _o_) {
			awardid = _o_.awardid;
			itemindex = _o_.itemindex;
		}

		private void assign(SchoolWheel.Data _o_) {
			awardid = _o_.awardid;
			itemindex = _o_.itemindex;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(awardid);
			_os_.marshal(itemindex);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			awardid = _os_.unmarshal_int();
			itemindex = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.SchoolWheel copy() {
			return new Data(this);
		}

		@Override
		public xbean.SchoolWheel toData() {
			return new Data(this);
		}

		public xbean.SchoolWheel toBean() {
			return new SchoolWheel(this, null, null);
		}

		@Override
		public xbean.SchoolWheel toDataIf() {
			return this;
		}

		public xbean.SchoolWheel toBeanIf() {
			return new SchoolWheel(this, null, null);
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
		public int getItemindex() { // 物品列的索引
			return itemindex;
		}

		@Override
		public void setAwardid(int _v_) { // 奖励id
			awardid = _v_;
		}

		@Override
		public void setItemindex(int _v_) { // 物品列的索引
			itemindex = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SchoolWheel.Data)) return false;
			SchoolWheel.Data _o_ = (SchoolWheel.Data) _o1_;
			if (awardid != _o_.awardid) return false;
			if (itemindex != _o_.itemindex) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += awardid;
			_h_ += itemindex;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(awardid);
			_sb_.append(",");
			_sb_.append(itemindex);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

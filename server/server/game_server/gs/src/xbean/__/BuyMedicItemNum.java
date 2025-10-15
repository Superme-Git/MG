
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BuyMedicItemNum extends mkdb.XBean implements xbean.BuyMedicItemNum {
	private int buynum; // 药房道具购买数量
	private int impeachdaynum; // 发起弹劾次数

	@Override
	public void _reset_unsafe_() {
		buynum = 0;
		impeachdaynum = 0;
	}

	BuyMedicItemNum(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		buynum = 0;
		impeachdaynum = 0;
	}

	public BuyMedicItemNum() {
		this(0, null, null);
	}

	public BuyMedicItemNum(BuyMedicItemNum _o_) {
		this(_o_, null, null);
	}

	BuyMedicItemNum(xbean.BuyMedicItemNum _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BuyMedicItemNum) assign((BuyMedicItemNum)_o1_);
		else if (_o1_ instanceof BuyMedicItemNum.Data) assign((BuyMedicItemNum.Data)_o1_);
		else if (_o1_ instanceof BuyMedicItemNum.Const) assign(((BuyMedicItemNum.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BuyMedicItemNum _o_) {
		_o_._xdb_verify_unsafe_();
		buynum = _o_.buynum;
		impeachdaynum = _o_.impeachdaynum;
	}

	private void assign(BuyMedicItemNum.Data _o_) {
		buynum = _o_.buynum;
		impeachdaynum = _o_.impeachdaynum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(buynum);
		_os_.marshal(impeachdaynum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		buynum = _os_.unmarshal_int();
		impeachdaynum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.BuyMedicItemNum copy() {
		_xdb_verify_unsafe_();
		return new BuyMedicItemNum(this);
	}

	@Override
	public xbean.BuyMedicItemNum toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BuyMedicItemNum toBean() {
		_xdb_verify_unsafe_();
		return new BuyMedicItemNum(this); // same as copy()
	}

	@Override
	public xbean.BuyMedicItemNum toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BuyMedicItemNum toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBuynum() { // 药房道具购买数量
		_xdb_verify_unsafe_();
		return buynum;
	}

	@Override
	public int getImpeachdaynum() { // 发起弹劾次数
		_xdb_verify_unsafe_();
		return impeachdaynum;
	}

	@Override
	public void setBuynum(int _v_) { // 药房道具购买数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "buynum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, buynum) {
					public void rollback() { buynum = _xdb_saved; }
				};}});
		buynum = _v_;
	}

	@Override
	public void setImpeachdaynum(int _v_) { // 发起弹劾次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "impeachdaynum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, impeachdaynum) {
					public void rollback() { impeachdaynum = _xdb_saved; }
				};}});
		impeachdaynum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BuyMedicItemNum _o_ = null;
		if ( _o1_ instanceof BuyMedicItemNum ) _o_ = (BuyMedicItemNum)_o1_;
		else if ( _o1_ instanceof BuyMedicItemNum.Const ) _o_ = ((BuyMedicItemNum.Const)_o1_).nThis();
		else return false;
		if (buynum != _o_.buynum) return false;
		if (impeachdaynum != _o_.impeachdaynum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += buynum;
		_h_ += impeachdaynum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buynum);
		_sb_.append(",");
		_sb_.append(impeachdaynum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buynum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("impeachdaynum"));
		return lb;
	}

	private class Const implements xbean.BuyMedicItemNum {
		BuyMedicItemNum nThis() {
			return BuyMedicItemNum.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BuyMedicItemNum copy() {
			return BuyMedicItemNum.this.copy();
		}

		@Override
		public xbean.BuyMedicItemNum toData() {
			return BuyMedicItemNum.this.toData();
		}

		public xbean.BuyMedicItemNum toBean() {
			return BuyMedicItemNum.this.toBean();
		}

		@Override
		public xbean.BuyMedicItemNum toDataIf() {
			return BuyMedicItemNum.this.toDataIf();
		}

		public xbean.BuyMedicItemNum toBeanIf() {
			return BuyMedicItemNum.this.toBeanIf();
		}

		@Override
		public int getBuynum() { // 药房道具购买数量
			_xdb_verify_unsafe_();
			return buynum;
		}

		@Override
		public int getImpeachdaynum() { // 发起弹劾次数
			_xdb_verify_unsafe_();
			return impeachdaynum;
		}

		@Override
		public void setBuynum(int _v_) { // 药房道具购买数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setImpeachdaynum(int _v_) { // 发起弹劾次数
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
			return BuyMedicItemNum.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BuyMedicItemNum.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BuyMedicItemNum.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BuyMedicItemNum.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BuyMedicItemNum.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BuyMedicItemNum.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BuyMedicItemNum.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BuyMedicItemNum.this.hashCode();
		}

		@Override
		public String toString() {
			return BuyMedicItemNum.this.toString();
		}

	}

	public static final class Data implements xbean.BuyMedicItemNum {
		private int buynum; // 药房道具购买数量
		private int impeachdaynum; // 发起弹劾次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			buynum = 0;
			impeachdaynum = 0;
		}

		Data(xbean.BuyMedicItemNum _o1_) {
			if (_o1_ instanceof BuyMedicItemNum) assign((BuyMedicItemNum)_o1_);
			else if (_o1_ instanceof BuyMedicItemNum.Data) assign((BuyMedicItemNum.Data)_o1_);
			else if (_o1_ instanceof BuyMedicItemNum.Const) assign(((BuyMedicItemNum.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BuyMedicItemNum _o_) {
			buynum = _o_.buynum;
			impeachdaynum = _o_.impeachdaynum;
		}

		private void assign(BuyMedicItemNum.Data _o_) {
			buynum = _o_.buynum;
			impeachdaynum = _o_.impeachdaynum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(buynum);
			_os_.marshal(impeachdaynum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			buynum = _os_.unmarshal_int();
			impeachdaynum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.BuyMedicItemNum copy() {
			return new Data(this);
		}

		@Override
		public xbean.BuyMedicItemNum toData() {
			return new Data(this);
		}

		public xbean.BuyMedicItemNum toBean() {
			return new BuyMedicItemNum(this, null, null);
		}

		@Override
		public xbean.BuyMedicItemNum toDataIf() {
			return this;
		}

		public xbean.BuyMedicItemNum toBeanIf() {
			return new BuyMedicItemNum(this, null, null);
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
		public int getBuynum() { // 药房道具购买数量
			return buynum;
		}

		@Override
		public int getImpeachdaynum() { // 发起弹劾次数
			return impeachdaynum;
		}

		@Override
		public void setBuynum(int _v_) { // 药房道具购买数量
			buynum = _v_;
		}

		@Override
		public void setImpeachdaynum(int _v_) { // 发起弹劾次数
			impeachdaynum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BuyMedicItemNum.Data)) return false;
			BuyMedicItemNum.Data _o_ = (BuyMedicItemNum.Data) _o1_;
			if (buynum != _o_.buynum) return false;
			if (impeachdaynum != _o_.impeachdaynum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += buynum;
			_h_ += impeachdaynum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(buynum);
			_sb_.append(",");
			_sb_.append(impeachdaynum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

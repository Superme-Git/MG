
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleUpdateModelNum extends mkdb.XBean implements xbean.RoleUpdateModelNum {
	private int tradingbuyfushinum; // 每日交易所购买符石数量

	@Override
	public void _reset_unsafe_() {
		tradingbuyfushinum = 0;
	}

	RoleUpdateModelNum(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		tradingbuyfushinum = 0;
	}

	public RoleUpdateModelNum() {
		this(0, null, null);
	}

	public RoleUpdateModelNum(RoleUpdateModelNum _o_) {
		this(_o_, null, null);
	}

	RoleUpdateModelNum(xbean.RoleUpdateModelNum _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleUpdateModelNum) assign((RoleUpdateModelNum)_o1_);
		else if (_o1_ instanceof RoleUpdateModelNum.Data) assign((RoleUpdateModelNum.Data)_o1_);
		else if (_o1_ instanceof RoleUpdateModelNum.Const) assign(((RoleUpdateModelNum.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleUpdateModelNum _o_) {
		_o_._xdb_verify_unsafe_();
		tradingbuyfushinum = _o_.tradingbuyfushinum;
	}

	private void assign(RoleUpdateModelNum.Data _o_) {
		tradingbuyfushinum = _o_.tradingbuyfushinum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(tradingbuyfushinum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		tradingbuyfushinum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RoleUpdateModelNum copy() {
		_xdb_verify_unsafe_();
		return new RoleUpdateModelNum(this);
	}

	@Override
	public xbean.RoleUpdateModelNum toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleUpdateModelNum toBean() {
		_xdb_verify_unsafe_();
		return new RoleUpdateModelNum(this); // same as copy()
	}

	@Override
	public xbean.RoleUpdateModelNum toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleUpdateModelNum toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getTradingbuyfushinum() { // 每日交易所购买符石数量
		_xdb_verify_unsafe_();
		return tradingbuyfushinum;
	}

	@Override
	public void setTradingbuyfushinum(int _v_) { // 每日交易所购买符石数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tradingbuyfushinum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, tradingbuyfushinum) {
					public void rollback() { tradingbuyfushinum = _xdb_saved; }
				};}});
		tradingbuyfushinum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleUpdateModelNum _o_ = null;
		if ( _o1_ instanceof RoleUpdateModelNum ) _o_ = (RoleUpdateModelNum)_o1_;
		else if ( _o1_ instanceof RoleUpdateModelNum.Const ) _o_ = ((RoleUpdateModelNum.Const)_o1_).nThis();
		else return false;
		if (tradingbuyfushinum != _o_.tradingbuyfushinum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += tradingbuyfushinum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tradingbuyfushinum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tradingbuyfushinum"));
		return lb;
	}

	private class Const implements xbean.RoleUpdateModelNum {
		RoleUpdateModelNum nThis() {
			return RoleUpdateModelNum.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleUpdateModelNum copy() {
			return RoleUpdateModelNum.this.copy();
		}

		@Override
		public xbean.RoleUpdateModelNum toData() {
			return RoleUpdateModelNum.this.toData();
		}

		public xbean.RoleUpdateModelNum toBean() {
			return RoleUpdateModelNum.this.toBean();
		}

		@Override
		public xbean.RoleUpdateModelNum toDataIf() {
			return RoleUpdateModelNum.this.toDataIf();
		}

		public xbean.RoleUpdateModelNum toBeanIf() {
			return RoleUpdateModelNum.this.toBeanIf();
		}

		@Override
		public int getTradingbuyfushinum() { // 每日交易所购买符石数量
			_xdb_verify_unsafe_();
			return tradingbuyfushinum;
		}

		@Override
		public void setTradingbuyfushinum(int _v_) { // 每日交易所购买符石数量
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
			return RoleUpdateModelNum.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleUpdateModelNum.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleUpdateModelNum.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleUpdateModelNum.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleUpdateModelNum.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleUpdateModelNum.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleUpdateModelNum.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleUpdateModelNum.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleUpdateModelNum.this.toString();
		}

	}

	public static final class Data implements xbean.RoleUpdateModelNum {
		private int tradingbuyfushinum; // 每日交易所购买符石数量

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			tradingbuyfushinum = 0;
		}

		Data(xbean.RoleUpdateModelNum _o1_) {
			if (_o1_ instanceof RoleUpdateModelNum) assign((RoleUpdateModelNum)_o1_);
			else if (_o1_ instanceof RoleUpdateModelNum.Data) assign((RoleUpdateModelNum.Data)_o1_);
			else if (_o1_ instanceof RoleUpdateModelNum.Const) assign(((RoleUpdateModelNum.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleUpdateModelNum _o_) {
			tradingbuyfushinum = _o_.tradingbuyfushinum;
		}

		private void assign(RoleUpdateModelNum.Data _o_) {
			tradingbuyfushinum = _o_.tradingbuyfushinum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(tradingbuyfushinum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			tradingbuyfushinum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RoleUpdateModelNum copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleUpdateModelNum toData() {
			return new Data(this);
		}

		public xbean.RoleUpdateModelNum toBean() {
			return new RoleUpdateModelNum(this, null, null);
		}

		@Override
		public xbean.RoleUpdateModelNum toDataIf() {
			return this;
		}

		public xbean.RoleUpdateModelNum toBeanIf() {
			return new RoleUpdateModelNum(this, null, null);
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
		public int getTradingbuyfushinum() { // 每日交易所购买符石数量
			return tradingbuyfushinum;
		}

		@Override
		public void setTradingbuyfushinum(int _v_) { // 每日交易所购买符石数量
			tradingbuyfushinum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleUpdateModelNum.Data)) return false;
			RoleUpdateModelNum.Data _o_ = (RoleUpdateModelNum.Data) _o1_;
			if (tradingbuyfushinum != _o_.tradingbuyfushinum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += tradingbuyfushinum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(tradingbuyfushinum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

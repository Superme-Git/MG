
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class GoodsBusiness extends mkdb.XBean implements xbean.GoodsBusiness {
	private long buynum; // 本期购买的数量
	private long salenum; // 本期售卖的数量
	private long lastnum; // 上期成交量
	private int priorperiodprice; // 上期价格
	private int price; // 商品价格
	private int count; // 周期成交量小于等于零的次数,降价后次数清零重新计数

	@Override
	public void _reset_unsafe_() {
		buynum = 0L;
		salenum = 0L;
		lastnum = 0L;
		priorperiodprice = 0;
		price = 0;
		count = 0;
	}

	GoodsBusiness(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		priorperiodprice = 0;
	}

	public GoodsBusiness() {
		this(0, null, null);
	}

	public GoodsBusiness(GoodsBusiness _o_) {
		this(_o_, null, null);
	}

	GoodsBusiness(xbean.GoodsBusiness _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof GoodsBusiness) assign((GoodsBusiness)_o1_);
		else if (_o1_ instanceof GoodsBusiness.Data) assign((GoodsBusiness.Data)_o1_);
		else if (_o1_ instanceof GoodsBusiness.Const) assign(((GoodsBusiness.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(GoodsBusiness _o_) {
		_o_._xdb_verify_unsafe_();
		buynum = _o_.buynum;
		salenum = _o_.salenum;
		lastnum = _o_.lastnum;
		priorperiodprice = _o_.priorperiodprice;
		price = _o_.price;
		count = _o_.count;
	}

	private void assign(GoodsBusiness.Data _o_) {
		buynum = _o_.buynum;
		salenum = _o_.salenum;
		lastnum = _o_.lastnum;
		priorperiodprice = _o_.priorperiodprice;
		price = _o_.price;
		count = _o_.count;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(buynum);
		_os_.marshal(salenum);
		_os_.marshal(lastnum);
		_os_.marshal(priorperiodprice);
		_os_.marshal(price);
		_os_.marshal(count);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		buynum = _os_.unmarshal_long();
		salenum = _os_.unmarshal_long();
		lastnum = _os_.unmarshal_long();
		priorperiodprice = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		count = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.GoodsBusiness copy() {
		_xdb_verify_unsafe_();
		return new GoodsBusiness(this);
	}

	@Override
	public xbean.GoodsBusiness toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GoodsBusiness toBean() {
		_xdb_verify_unsafe_();
		return new GoodsBusiness(this); // same as copy()
	}

	@Override
	public xbean.GoodsBusiness toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GoodsBusiness toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getBuynum() { // 本期购买的数量
		_xdb_verify_unsafe_();
		return buynum;
	}

	@Override
	public long getSalenum() { // 本期售卖的数量
		_xdb_verify_unsafe_();
		return salenum;
	}

	@Override
	public long getLastnum() { // 上期成交量
		_xdb_verify_unsafe_();
		return lastnum;
	}

	@Override
	public int getPriorperiodprice() { // 上期价格
		_xdb_verify_unsafe_();
		return priorperiodprice;
	}

	@Override
	public int getPrice() { // 商品价格
		_xdb_verify_unsafe_();
		return price;
	}

	@Override
	public int getCount() { // 周期成交量小于等于零的次数,降价后次数清零重新计数
		_xdb_verify_unsafe_();
		return count;
	}

	@Override
	public void setBuynum(long _v_) { // 本期购买的数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "buynum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, buynum) {
					public void rollback() { buynum = _xdb_saved; }
				};}});
		buynum = _v_;
	}

	@Override
	public void setSalenum(long _v_) { // 本期售卖的数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "salenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, salenum) {
					public void rollback() { salenum = _xdb_saved; }
				};}});
		salenum = _v_;
	}

	@Override
	public void setLastnum(long _v_) { // 上期成交量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastnum) {
					public void rollback() { lastnum = _xdb_saved; }
				};}});
		lastnum = _v_;
	}

	@Override
	public void setPriorperiodprice(int _v_) { // 上期价格
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "priorperiodprice") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, priorperiodprice) {
					public void rollback() { priorperiodprice = _xdb_saved; }
				};}});
		priorperiodprice = _v_;
	}

	@Override
	public void setPrice(int _v_) { // 商品价格
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "price") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, price) {
					public void rollback() { price = _xdb_saved; }
				};}});
		price = _v_;
	}

	@Override
	public void setCount(int _v_) { // 周期成交量小于等于零的次数,降价后次数清零重新计数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "count") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, count) {
					public void rollback() { count = _xdb_saved; }
				};}});
		count = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		GoodsBusiness _o_ = null;
		if ( _o1_ instanceof GoodsBusiness ) _o_ = (GoodsBusiness)_o1_;
		else if ( _o1_ instanceof GoodsBusiness.Const ) _o_ = ((GoodsBusiness.Const)_o1_).nThis();
		else return false;
		if (buynum != _o_.buynum) return false;
		if (salenum != _o_.salenum) return false;
		if (lastnum != _o_.lastnum) return false;
		if (priorperiodprice != _o_.priorperiodprice) return false;
		if (price != _o_.price) return false;
		if (count != _o_.count) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += buynum;
		_h_ += salenum;
		_h_ += lastnum;
		_h_ += priorperiodprice;
		_h_ += price;
		_h_ += count;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buynum);
		_sb_.append(",");
		_sb_.append(salenum);
		_sb_.append(",");
		_sb_.append(lastnum);
		_sb_.append(",");
		_sb_.append(priorperiodprice);
		_sb_.append(",");
		_sb_.append(price);
		_sb_.append(",");
		_sb_.append(count);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buynum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("salenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("priorperiodprice"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("price"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("count"));
		return lb;
	}

	private class Const implements xbean.GoodsBusiness {
		GoodsBusiness nThis() {
			return GoodsBusiness.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.GoodsBusiness copy() {
			return GoodsBusiness.this.copy();
		}

		@Override
		public xbean.GoodsBusiness toData() {
			return GoodsBusiness.this.toData();
		}

		public xbean.GoodsBusiness toBean() {
			return GoodsBusiness.this.toBean();
		}

		@Override
		public xbean.GoodsBusiness toDataIf() {
			return GoodsBusiness.this.toDataIf();
		}

		public xbean.GoodsBusiness toBeanIf() {
			return GoodsBusiness.this.toBeanIf();
		}

		@Override
		public long getBuynum() { // 本期购买的数量
			_xdb_verify_unsafe_();
			return buynum;
		}

		@Override
		public long getSalenum() { // 本期售卖的数量
			_xdb_verify_unsafe_();
			return salenum;
		}

		@Override
		public long getLastnum() { // 上期成交量
			_xdb_verify_unsafe_();
			return lastnum;
		}

		@Override
		public int getPriorperiodprice() { // 上期价格
			_xdb_verify_unsafe_();
			return priorperiodprice;
		}

		@Override
		public int getPrice() { // 商品价格
			_xdb_verify_unsafe_();
			return price;
		}

		@Override
		public int getCount() { // 周期成交量小于等于零的次数,降价后次数清零重新计数
			_xdb_verify_unsafe_();
			return count;
		}

		@Override
		public void setBuynum(long _v_) { // 本期购买的数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSalenum(long _v_) { // 本期售卖的数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastnum(long _v_) { // 上期成交量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPriorperiodprice(int _v_) { // 上期价格
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPrice(int _v_) { // 商品价格
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCount(int _v_) { // 周期成交量小于等于零的次数,降价后次数清零重新计数
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
			return GoodsBusiness.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return GoodsBusiness.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return GoodsBusiness.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return GoodsBusiness.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return GoodsBusiness.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return GoodsBusiness.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return GoodsBusiness.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return GoodsBusiness.this.hashCode();
		}

		@Override
		public String toString() {
			return GoodsBusiness.this.toString();
		}

	}

	public static final class Data implements xbean.GoodsBusiness {
		private long buynum; // 本期购买的数量
		private long salenum; // 本期售卖的数量
		private long lastnum; // 上期成交量
		private int priorperiodprice; // 上期价格
		private int price; // 商品价格
		private int count; // 周期成交量小于等于零的次数,降价后次数清零重新计数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			priorperiodprice = 0;
		}

		Data(xbean.GoodsBusiness _o1_) {
			if (_o1_ instanceof GoodsBusiness) assign((GoodsBusiness)_o1_);
			else if (_o1_ instanceof GoodsBusiness.Data) assign((GoodsBusiness.Data)_o1_);
			else if (_o1_ instanceof GoodsBusiness.Const) assign(((GoodsBusiness.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(GoodsBusiness _o_) {
			buynum = _o_.buynum;
			salenum = _o_.salenum;
			lastnum = _o_.lastnum;
			priorperiodprice = _o_.priorperiodprice;
			price = _o_.price;
			count = _o_.count;
		}

		private void assign(GoodsBusiness.Data _o_) {
			buynum = _o_.buynum;
			salenum = _o_.salenum;
			lastnum = _o_.lastnum;
			priorperiodprice = _o_.priorperiodprice;
			price = _o_.price;
			count = _o_.count;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(buynum);
			_os_.marshal(salenum);
			_os_.marshal(lastnum);
			_os_.marshal(priorperiodprice);
			_os_.marshal(price);
			_os_.marshal(count);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			buynum = _os_.unmarshal_long();
			salenum = _os_.unmarshal_long();
			lastnum = _os_.unmarshal_long();
			priorperiodprice = _os_.unmarshal_int();
			price = _os_.unmarshal_int();
			count = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.GoodsBusiness copy() {
			return new Data(this);
		}

		@Override
		public xbean.GoodsBusiness toData() {
			return new Data(this);
		}

		public xbean.GoodsBusiness toBean() {
			return new GoodsBusiness(this, null, null);
		}

		@Override
		public xbean.GoodsBusiness toDataIf() {
			return this;
		}

		public xbean.GoodsBusiness toBeanIf() {
			return new GoodsBusiness(this, null, null);
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
		public long getBuynum() { // 本期购买的数量
			return buynum;
		}

		@Override
		public long getSalenum() { // 本期售卖的数量
			return salenum;
		}

		@Override
		public long getLastnum() { // 上期成交量
			return lastnum;
		}

		@Override
		public int getPriorperiodprice() { // 上期价格
			return priorperiodprice;
		}

		@Override
		public int getPrice() { // 商品价格
			return price;
		}

		@Override
		public int getCount() { // 周期成交量小于等于零的次数,降价后次数清零重新计数
			return count;
		}

		@Override
		public void setBuynum(long _v_) { // 本期购买的数量
			buynum = _v_;
		}

		@Override
		public void setSalenum(long _v_) { // 本期售卖的数量
			salenum = _v_;
		}

		@Override
		public void setLastnum(long _v_) { // 上期成交量
			lastnum = _v_;
		}

		@Override
		public void setPriorperiodprice(int _v_) { // 上期价格
			priorperiodprice = _v_;
		}

		@Override
		public void setPrice(int _v_) { // 商品价格
			price = _v_;
		}

		@Override
		public void setCount(int _v_) { // 周期成交量小于等于零的次数,降价后次数清零重新计数
			count = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof GoodsBusiness.Data)) return false;
			GoodsBusiness.Data _o_ = (GoodsBusiness.Data) _o1_;
			if (buynum != _o_.buynum) return false;
			if (salenum != _o_.salenum) return false;
			if (lastnum != _o_.lastnum) return false;
			if (priorperiodprice != _o_.priorperiodprice) return false;
			if (price != _o_.price) return false;
			if (count != _o_.count) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += buynum;
			_h_ += salenum;
			_h_ += lastnum;
			_h_ += priorperiodprice;
			_h_ += price;
			_h_ += count;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(buynum);
			_sb_.append(",");
			_sb_.append(salenum);
			_sb_.append(",");
			_sb_.append(lastnum);
			_sb_.append(",");
			_sb_.append(priorperiodprice);
			_sb_.append(",");
			_sb_.append(price);
			_sb_.append(",");
			_sb_.append(count);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

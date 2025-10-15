
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MarketFloatingGoods extends mkdb.XBean implements xbean.MarketFloatingGoods {
	private float floatingmin; // 价格下限
	private float floatingmax; // 价格上限
	private float floatingprice; // 浮动价格系数
	private int basehangordernum; // 基准挂单量
	private int basesalenum; // 基准成交量
	private int hangordernum; // 挂单量
	private int salenum; // 当期成交量
	private int priorperiodprice; // 上期价格
	private int price; // 当前价格
	private int totalmoney; // 本期售卖总金额

	@Override
	public void _reset_unsafe_() {
		floatingmin = 0;
		floatingmax = 0;
		floatingprice = 0;
		basehangordernum = 0;
		basesalenum = 0;
		hangordernum = 0;
		salenum = 0;
		priorperiodprice = 0;
		price = 0;
		totalmoney = 0;
	}

	MarketFloatingGoods(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		floatingmin = 0;
		floatingmax = 0;
		floatingprice = 0;
		basehangordernum = 0;
		basesalenum = 0;
		hangordernum = 0;
		salenum = 0;
		priorperiodprice = 0;
		price = 0;
		totalmoney = 0;
	}

	public MarketFloatingGoods() {
		this(0, null, null);
	}

	public MarketFloatingGoods(MarketFloatingGoods _o_) {
		this(_o_, null, null);
	}

	MarketFloatingGoods(xbean.MarketFloatingGoods _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MarketFloatingGoods) assign((MarketFloatingGoods)_o1_);
		else if (_o1_ instanceof MarketFloatingGoods.Data) assign((MarketFloatingGoods.Data)_o1_);
		else if (_o1_ instanceof MarketFloatingGoods.Const) assign(((MarketFloatingGoods.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MarketFloatingGoods _o_) {
		_o_._xdb_verify_unsafe_();
		floatingmin = _o_.floatingmin;
		floatingmax = _o_.floatingmax;
		floatingprice = _o_.floatingprice;
		basehangordernum = _o_.basehangordernum;
		basesalenum = _o_.basesalenum;
		hangordernum = _o_.hangordernum;
		salenum = _o_.salenum;
		priorperiodprice = _o_.priorperiodprice;
		price = _o_.price;
		totalmoney = _o_.totalmoney;
	}

	private void assign(MarketFloatingGoods.Data _o_) {
		floatingmin = _o_.floatingmin;
		floatingmax = _o_.floatingmax;
		floatingprice = _o_.floatingprice;
		basehangordernum = _o_.basehangordernum;
		basesalenum = _o_.basesalenum;
		hangordernum = _o_.hangordernum;
		salenum = _o_.salenum;
		priorperiodprice = _o_.priorperiodprice;
		price = _o_.price;
		totalmoney = _o_.totalmoney;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(floatingmin);
		_os_.marshal(floatingmax);
		_os_.marshal(floatingprice);
		_os_.marshal(basehangordernum);
		_os_.marshal(basesalenum);
		_os_.marshal(hangordernum);
		_os_.marshal(salenum);
		_os_.marshal(priorperiodprice);
		_os_.marshal(price);
		_os_.marshal(totalmoney);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		floatingmin = _os_.unmarshal_float();
		floatingmax = _os_.unmarshal_float();
		floatingprice = _os_.unmarshal_float();
		basehangordernum = _os_.unmarshal_int();
		basesalenum = _os_.unmarshal_int();
		hangordernum = _os_.unmarshal_int();
		salenum = _os_.unmarshal_int();
		priorperiodprice = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		totalmoney = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MarketFloatingGoods copy() {
		_xdb_verify_unsafe_();
		return new MarketFloatingGoods(this);
	}

	@Override
	public xbean.MarketFloatingGoods toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarketFloatingGoods toBean() {
		_xdb_verify_unsafe_();
		return new MarketFloatingGoods(this); // same as copy()
	}

	@Override
	public xbean.MarketFloatingGoods toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarketFloatingGoods toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public float getFloatingmin() { // 价格下限
		_xdb_verify_unsafe_();
		return floatingmin;
	}

	@Override
	public float getFloatingmax() { // 价格上限
		_xdb_verify_unsafe_();
		return floatingmax;
	}

	@Override
	public float getFloatingprice() { // 浮动价格系数
		_xdb_verify_unsafe_();
		return floatingprice;
	}

	@Override
	public int getBasehangordernum() { // 基准挂单量
		_xdb_verify_unsafe_();
		return basehangordernum;
	}

	@Override
	public int getBasesalenum() { // 基准成交量
		_xdb_verify_unsafe_();
		return basesalenum;
	}

	@Override
	public int getHangordernum() { // 挂单量
		_xdb_verify_unsafe_();
		return hangordernum;
	}

	@Override
	public int getSalenum() { // 当期成交量
		_xdb_verify_unsafe_();
		return salenum;
	}

	@Override
	public int getPriorperiodprice() { // 上期价格
		_xdb_verify_unsafe_();
		return priorperiodprice;
	}

	@Override
	public int getPrice() { // 当前价格
		_xdb_verify_unsafe_();
		return price;
	}

	@Override
	public int getTotalmoney() { // 本期售卖总金额
		_xdb_verify_unsafe_();
		return totalmoney;
	}

	@Override
	public void setFloatingmin(float _v_) { // 价格下限
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "floatingmin") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, floatingmin) {
					public void rollback() { floatingmin = _xdb_saved; }
				};}});
		floatingmin = _v_;
	}

	@Override
	public void setFloatingmax(float _v_) { // 价格上限
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "floatingmax") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, floatingmax) {
					public void rollback() { floatingmax = _xdb_saved; }
				};}});
		floatingmax = _v_;
	}

	@Override
	public void setFloatingprice(float _v_) { // 浮动价格系数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "floatingprice") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, floatingprice) {
					public void rollback() { floatingprice = _xdb_saved; }
				};}});
		floatingprice = _v_;
	}

	@Override
	public void setBasehangordernum(int _v_) { // 基准挂单量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "basehangordernum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, basehangordernum) {
					public void rollback() { basehangordernum = _xdb_saved; }
				};}});
		basehangordernum = _v_;
	}

	@Override
	public void setBasesalenum(int _v_) { // 基准成交量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "basesalenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, basesalenum) {
					public void rollback() { basesalenum = _xdb_saved; }
				};}});
		basesalenum = _v_;
	}

	@Override
	public void setHangordernum(int _v_) { // 挂单量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hangordernum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, hangordernum) {
					public void rollback() { hangordernum = _xdb_saved; }
				};}});
		hangordernum = _v_;
	}

	@Override
	public void setSalenum(int _v_) { // 当期成交量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "salenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, salenum) {
					public void rollback() { salenum = _xdb_saved; }
				};}});
		salenum = _v_;
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
	public void setPrice(int _v_) { // 当前价格
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "price") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, price) {
					public void rollback() { price = _xdb_saved; }
				};}});
		price = _v_;
	}

	@Override
	public void setTotalmoney(int _v_) { // 本期售卖总金额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalmoney") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalmoney) {
					public void rollback() { totalmoney = _xdb_saved; }
				};}});
		totalmoney = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MarketFloatingGoods _o_ = null;
		if ( _o1_ instanceof MarketFloatingGoods ) _o_ = (MarketFloatingGoods)_o1_;
		else if ( _o1_ instanceof MarketFloatingGoods.Const ) _o_ = ((MarketFloatingGoods.Const)_o1_).nThis();
		else return false;
		if (floatingmin != _o_.floatingmin) return false;
		if (floatingmax != _o_.floatingmax) return false;
		if (floatingprice != _o_.floatingprice) return false;
		if (basehangordernum != _o_.basehangordernum) return false;
		if (basesalenum != _o_.basesalenum) return false;
		if (hangordernum != _o_.hangordernum) return false;
		if (salenum != _o_.salenum) return false;
		if (priorperiodprice != _o_.priorperiodprice) return false;
		if (price != _o_.price) return false;
		if (totalmoney != _o_.totalmoney) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += floatingmin;
		_h_ += floatingmax;
		_h_ += floatingprice;
		_h_ += basehangordernum;
		_h_ += basesalenum;
		_h_ += hangordernum;
		_h_ += salenum;
		_h_ += priorperiodprice;
		_h_ += price;
		_h_ += totalmoney;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(floatingmin);
		_sb_.append(",");
		_sb_.append(floatingmax);
		_sb_.append(",");
		_sb_.append(floatingprice);
		_sb_.append(",");
		_sb_.append(basehangordernum);
		_sb_.append(",");
		_sb_.append(basesalenum);
		_sb_.append(",");
		_sb_.append(hangordernum);
		_sb_.append(",");
		_sb_.append(salenum);
		_sb_.append(",");
		_sb_.append(priorperiodprice);
		_sb_.append(",");
		_sb_.append(price);
		_sb_.append(",");
		_sb_.append(totalmoney);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("floatingmin"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("floatingmax"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("floatingprice"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("basehangordernum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("basesalenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hangordernum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("salenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("priorperiodprice"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("price"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalmoney"));
		return lb;
	}

	private class Const implements xbean.MarketFloatingGoods {
		MarketFloatingGoods nThis() {
			return MarketFloatingGoods.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MarketFloatingGoods copy() {
			return MarketFloatingGoods.this.copy();
		}

		@Override
		public xbean.MarketFloatingGoods toData() {
			return MarketFloatingGoods.this.toData();
		}

		public xbean.MarketFloatingGoods toBean() {
			return MarketFloatingGoods.this.toBean();
		}

		@Override
		public xbean.MarketFloatingGoods toDataIf() {
			return MarketFloatingGoods.this.toDataIf();
		}

		public xbean.MarketFloatingGoods toBeanIf() {
			return MarketFloatingGoods.this.toBeanIf();
		}

		@Override
		public float getFloatingmin() { // 价格下限
			_xdb_verify_unsafe_();
			return floatingmin;
		}

		@Override
		public float getFloatingmax() { // 价格上限
			_xdb_verify_unsafe_();
			return floatingmax;
		}

		@Override
		public float getFloatingprice() { // 浮动价格系数
			_xdb_verify_unsafe_();
			return floatingprice;
		}

		@Override
		public int getBasehangordernum() { // 基准挂单量
			_xdb_verify_unsafe_();
			return basehangordernum;
		}

		@Override
		public int getBasesalenum() { // 基准成交量
			_xdb_verify_unsafe_();
			return basesalenum;
		}

		@Override
		public int getHangordernum() { // 挂单量
			_xdb_verify_unsafe_();
			return hangordernum;
		}

		@Override
		public int getSalenum() { // 当期成交量
			_xdb_verify_unsafe_();
			return salenum;
		}

		@Override
		public int getPriorperiodprice() { // 上期价格
			_xdb_verify_unsafe_();
			return priorperiodprice;
		}

		@Override
		public int getPrice() { // 当前价格
			_xdb_verify_unsafe_();
			return price;
		}

		@Override
		public int getTotalmoney() { // 本期售卖总金额
			_xdb_verify_unsafe_();
			return totalmoney;
		}

		@Override
		public void setFloatingmin(float _v_) { // 价格下限
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFloatingmax(float _v_) { // 价格上限
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFloatingprice(float _v_) { // 浮动价格系数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBasehangordernum(int _v_) { // 基准挂单量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBasesalenum(int _v_) { // 基准成交量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHangordernum(int _v_) { // 挂单量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSalenum(int _v_) { // 当期成交量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPriorperiodprice(int _v_) { // 上期价格
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPrice(int _v_) { // 当前价格
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalmoney(int _v_) { // 本期售卖总金额
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
			return MarketFloatingGoods.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MarketFloatingGoods.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MarketFloatingGoods.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MarketFloatingGoods.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MarketFloatingGoods.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MarketFloatingGoods.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MarketFloatingGoods.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MarketFloatingGoods.this.hashCode();
		}

		@Override
		public String toString() {
			return MarketFloatingGoods.this.toString();
		}

	}

	public static final class Data implements xbean.MarketFloatingGoods {
		private float floatingmin; // 价格下限
		private float floatingmax; // 价格上限
		private float floatingprice; // 浮动价格系数
		private int basehangordernum; // 基准挂单量
		private int basesalenum; // 基准成交量
		private int hangordernum; // 挂单量
		private int salenum; // 当期成交量
		private int priorperiodprice; // 上期价格
		private int price; // 当前价格
		private int totalmoney; // 本期售卖总金额

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			floatingmin = 0;
			floatingmax = 0;
			floatingprice = 0;
			basehangordernum = 0;
			basesalenum = 0;
			hangordernum = 0;
			salenum = 0;
			priorperiodprice = 0;
			price = 0;
			totalmoney = 0;
		}

		Data(xbean.MarketFloatingGoods _o1_) {
			if (_o1_ instanceof MarketFloatingGoods) assign((MarketFloatingGoods)_o1_);
			else if (_o1_ instanceof MarketFloatingGoods.Data) assign((MarketFloatingGoods.Data)_o1_);
			else if (_o1_ instanceof MarketFloatingGoods.Const) assign(((MarketFloatingGoods.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MarketFloatingGoods _o_) {
			floatingmin = _o_.floatingmin;
			floatingmax = _o_.floatingmax;
			floatingprice = _o_.floatingprice;
			basehangordernum = _o_.basehangordernum;
			basesalenum = _o_.basesalenum;
			hangordernum = _o_.hangordernum;
			salenum = _o_.salenum;
			priorperiodprice = _o_.priorperiodprice;
			price = _o_.price;
			totalmoney = _o_.totalmoney;
		}

		private void assign(MarketFloatingGoods.Data _o_) {
			floatingmin = _o_.floatingmin;
			floatingmax = _o_.floatingmax;
			floatingprice = _o_.floatingprice;
			basehangordernum = _o_.basehangordernum;
			basesalenum = _o_.basesalenum;
			hangordernum = _o_.hangordernum;
			salenum = _o_.salenum;
			priorperiodprice = _o_.priorperiodprice;
			price = _o_.price;
			totalmoney = _o_.totalmoney;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(floatingmin);
			_os_.marshal(floatingmax);
			_os_.marshal(floatingprice);
			_os_.marshal(basehangordernum);
			_os_.marshal(basesalenum);
			_os_.marshal(hangordernum);
			_os_.marshal(salenum);
			_os_.marshal(priorperiodprice);
			_os_.marshal(price);
			_os_.marshal(totalmoney);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			floatingmin = _os_.unmarshal_float();
			floatingmax = _os_.unmarshal_float();
			floatingprice = _os_.unmarshal_float();
			basehangordernum = _os_.unmarshal_int();
			basesalenum = _os_.unmarshal_int();
			hangordernum = _os_.unmarshal_int();
			salenum = _os_.unmarshal_int();
			priorperiodprice = _os_.unmarshal_int();
			price = _os_.unmarshal_int();
			totalmoney = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MarketFloatingGoods copy() {
			return new Data(this);
		}

		@Override
		public xbean.MarketFloatingGoods toData() {
			return new Data(this);
		}

		public xbean.MarketFloatingGoods toBean() {
			return new MarketFloatingGoods(this, null, null);
		}

		@Override
		public xbean.MarketFloatingGoods toDataIf() {
			return this;
		}

		public xbean.MarketFloatingGoods toBeanIf() {
			return new MarketFloatingGoods(this, null, null);
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
		public float getFloatingmin() { // 价格下限
			return floatingmin;
		}

		@Override
		public float getFloatingmax() { // 价格上限
			return floatingmax;
		}

		@Override
		public float getFloatingprice() { // 浮动价格系数
			return floatingprice;
		}

		@Override
		public int getBasehangordernum() { // 基准挂单量
			return basehangordernum;
		}

		@Override
		public int getBasesalenum() { // 基准成交量
			return basesalenum;
		}

		@Override
		public int getHangordernum() { // 挂单量
			return hangordernum;
		}

		@Override
		public int getSalenum() { // 当期成交量
			return salenum;
		}

		@Override
		public int getPriorperiodprice() { // 上期价格
			return priorperiodprice;
		}

		@Override
		public int getPrice() { // 当前价格
			return price;
		}

		@Override
		public int getTotalmoney() { // 本期售卖总金额
			return totalmoney;
		}

		@Override
		public void setFloatingmin(float _v_) { // 价格下限
			floatingmin = _v_;
		}

		@Override
		public void setFloatingmax(float _v_) { // 价格上限
			floatingmax = _v_;
		}

		@Override
		public void setFloatingprice(float _v_) { // 浮动价格系数
			floatingprice = _v_;
		}

		@Override
		public void setBasehangordernum(int _v_) { // 基准挂单量
			basehangordernum = _v_;
		}

		@Override
		public void setBasesalenum(int _v_) { // 基准成交量
			basesalenum = _v_;
		}

		@Override
		public void setHangordernum(int _v_) { // 挂单量
			hangordernum = _v_;
		}

		@Override
		public void setSalenum(int _v_) { // 当期成交量
			salenum = _v_;
		}

		@Override
		public void setPriorperiodprice(int _v_) { // 上期价格
			priorperiodprice = _v_;
		}

		@Override
		public void setPrice(int _v_) { // 当前价格
			price = _v_;
		}

		@Override
		public void setTotalmoney(int _v_) { // 本期售卖总金额
			totalmoney = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MarketFloatingGoods.Data)) return false;
			MarketFloatingGoods.Data _o_ = (MarketFloatingGoods.Data) _o1_;
			if (floatingmin != _o_.floatingmin) return false;
			if (floatingmax != _o_.floatingmax) return false;
			if (floatingprice != _o_.floatingprice) return false;
			if (basehangordernum != _o_.basehangordernum) return false;
			if (basesalenum != _o_.basesalenum) return false;
			if (hangordernum != _o_.hangordernum) return false;
			if (salenum != _o_.salenum) return false;
			if (priorperiodprice != _o_.priorperiodprice) return false;
			if (price != _o_.price) return false;
			if (totalmoney != _o_.totalmoney) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += floatingmin;
			_h_ += floatingmax;
			_h_ += floatingprice;
			_h_ += basehangordernum;
			_h_ += basesalenum;
			_h_ += hangordernum;
			_h_ += salenum;
			_h_ += priorperiodprice;
			_h_ += price;
			_h_ += totalmoney;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(floatingmin);
			_sb_.append(",");
			_sb_.append(floatingmax);
			_sb_.append(",");
			_sb_.append(floatingprice);
			_sb_.append(",");
			_sb_.append(basehangordernum);
			_sb_.append(",");
			_sb_.append(basesalenum);
			_sb_.append(",");
			_sb_.append(hangordernum);
			_sb_.append(",");
			_sb_.append(salenum);
			_sb_.append(",");
			_sb_.append(priorperiodprice);
			_sb_.append(",");
			_sb_.append(price);
			_sb_.append(",");
			_sb_.append(totalmoney);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

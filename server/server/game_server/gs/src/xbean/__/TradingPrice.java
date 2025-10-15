
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TradingPrice extends mkdb.XBean implements xbean.TradingPrice {
	private String tradingid; // 订单号
	private int price; // 价格
	private int num; // 数量
	private long roleid; // 角色id
	private long createtime; // 挂单时间

	@Override
	public void _reset_unsafe_() {
		tradingid = "";
		price = 0;
		num = 0;
		roleid = 0L;
		createtime = 0L;
	}

	TradingPrice(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		tradingid = "";
	}

	public TradingPrice() {
		this(0, null, null);
	}

	public TradingPrice(TradingPrice _o_) {
		this(_o_, null, null);
	}

	TradingPrice(xbean.TradingPrice _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TradingPrice) assign((TradingPrice)_o1_);
		else if (_o1_ instanceof TradingPrice.Data) assign((TradingPrice.Data)_o1_);
		else if (_o1_ instanceof TradingPrice.Const) assign(((TradingPrice.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TradingPrice _o_) {
		_o_._xdb_verify_unsafe_();
		tradingid = _o_.tradingid;
		price = _o_.price;
		num = _o_.num;
		roleid = _o_.roleid;
		createtime = _o_.createtime;
	}

	private void assign(TradingPrice.Data _o_) {
		tradingid = _o_.tradingid;
		price = _o_.price;
		num = _o_.num;
		roleid = _o_.roleid;
		createtime = _o_.createtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(tradingid, mkdb.Const.IO_CHARSET);
		_os_.marshal(price);
		_os_.marshal(num);
		_os_.marshal(roleid);
		_os_.marshal(createtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		tradingid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		price = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		createtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.TradingPrice copy() {
		_xdb_verify_unsafe_();
		return new TradingPrice(this);
	}

	@Override
	public xbean.TradingPrice toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TradingPrice toBean() {
		_xdb_verify_unsafe_();
		return new TradingPrice(this); // same as copy()
	}

	@Override
	public xbean.TradingPrice toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TradingPrice toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getTradingid() { // 订单号
		_xdb_verify_unsafe_();
		return tradingid;
	}

	@Override
	public com.locojoy.base.Octets getTradingidOctets() { // 订单号
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getTradingid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getPrice() { // 价格
		_xdb_verify_unsafe_();
		return price;
	}

	@Override
	public int getNum() { // 数量
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public long getRoleid() { // 角色id
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public long getCreatetime() { // 挂单时间
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public void setTradingid(String _v_) { // 订单号
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tradingid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, tradingid) {
					public void rollback() { tradingid = _xdb_saved; }
				};}});
		tradingid = _v_;
	}

	@Override
	public void setTradingidOctets(com.locojoy.base.Octets _v_) { // 订单号
		_xdb_verify_unsafe_();
		this.setTradingid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPrice(int _v_) { // 价格
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "price") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, price) {
					public void rollback() { price = _xdb_saved; }
				};}});
		price = _v_;
	}

	@Override
	public void setNum(int _v_) { // 数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "num") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, num) {
					public void rollback() { num = _xdb_saved; }
				};}});
		num = _v_;
	}

	@Override
	public void setRoleid(long _v_) { // 角色id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setCreatetime(long _v_) { // 挂单时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TradingPrice _o_ = null;
		if ( _o1_ instanceof TradingPrice ) _o_ = (TradingPrice)_o1_;
		else if ( _o1_ instanceof TradingPrice.Const ) _o_ = ((TradingPrice.Const)_o1_).nThis();
		else return false;
		if (!tradingid.equals(_o_.tradingid)) return false;
		if (price != _o_.price) return false;
		if (num != _o_.num) return false;
		if (roleid != _o_.roleid) return false;
		if (createtime != _o_.createtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += tradingid.hashCode();
		_h_ += price;
		_h_ += num;
		_h_ += roleid;
		_h_ += createtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(tradingid).append("'");
		_sb_.append(",");
		_sb_.append(price);
		_sb_.append(",");
		_sb_.append(num);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tradingid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("price"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		return lb;
	}

	private class Const implements xbean.TradingPrice {
		TradingPrice nThis() {
			return TradingPrice.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TradingPrice copy() {
			return TradingPrice.this.copy();
		}

		@Override
		public xbean.TradingPrice toData() {
			return TradingPrice.this.toData();
		}

		public xbean.TradingPrice toBean() {
			return TradingPrice.this.toBean();
		}

		@Override
		public xbean.TradingPrice toDataIf() {
			return TradingPrice.this.toDataIf();
		}

		public xbean.TradingPrice toBeanIf() {
			return TradingPrice.this.toBeanIf();
		}

		@Override
		public String getTradingid() { // 订单号
			_xdb_verify_unsafe_();
			return tradingid;
		}

		@Override
		public com.locojoy.base.Octets getTradingidOctets() { // 订单号
			_xdb_verify_unsafe_();
			return TradingPrice.this.getTradingidOctets();
		}

		@Override
		public int getPrice() { // 价格
			_xdb_verify_unsafe_();
			return price;
		}

		@Override
		public int getNum() { // 数量
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public long getRoleid() { // 角色id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getCreatetime() { // 挂单时间
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public void setTradingid(String _v_) { // 订单号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTradingidOctets(com.locojoy.base.Octets _v_) { // 订单号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPrice(int _v_) { // 价格
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNum(int _v_) { // 数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 角色id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 挂单时间
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
			return TradingPrice.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TradingPrice.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TradingPrice.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TradingPrice.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TradingPrice.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TradingPrice.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TradingPrice.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TradingPrice.this.hashCode();
		}

		@Override
		public String toString() {
			return TradingPrice.this.toString();
		}

	}

	public static final class Data implements xbean.TradingPrice {
		private String tradingid; // 订单号
		private int price; // 价格
		private int num; // 数量
		private long roleid; // 角色id
		private long createtime; // 挂单时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			tradingid = "";
		}

		Data(xbean.TradingPrice _o1_) {
			if (_o1_ instanceof TradingPrice) assign((TradingPrice)_o1_);
			else if (_o1_ instanceof TradingPrice.Data) assign((TradingPrice.Data)_o1_);
			else if (_o1_ instanceof TradingPrice.Const) assign(((TradingPrice.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TradingPrice _o_) {
			tradingid = _o_.tradingid;
			price = _o_.price;
			num = _o_.num;
			roleid = _o_.roleid;
			createtime = _o_.createtime;
		}

		private void assign(TradingPrice.Data _o_) {
			tradingid = _o_.tradingid;
			price = _o_.price;
			num = _o_.num;
			roleid = _o_.roleid;
			createtime = _o_.createtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(tradingid, mkdb.Const.IO_CHARSET);
			_os_.marshal(price);
			_os_.marshal(num);
			_os_.marshal(roleid);
			_os_.marshal(createtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			tradingid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			price = _os_.unmarshal_int();
			num = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			createtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.TradingPrice copy() {
			return new Data(this);
		}

		@Override
		public xbean.TradingPrice toData() {
			return new Data(this);
		}

		public xbean.TradingPrice toBean() {
			return new TradingPrice(this, null, null);
		}

		@Override
		public xbean.TradingPrice toDataIf() {
			return this;
		}

		public xbean.TradingPrice toBeanIf() {
			return new TradingPrice(this, null, null);
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
		public String getTradingid() { // 订单号
			return tradingid;
		}

		@Override
		public com.locojoy.base.Octets getTradingidOctets() { // 订单号
			return com.locojoy.base.Octets.wrap(getTradingid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getPrice() { // 价格
			return price;
		}

		@Override
		public int getNum() { // 数量
			return num;
		}

		@Override
		public long getRoleid() { // 角色id
			return roleid;
		}

		@Override
		public long getCreatetime() { // 挂单时间
			return createtime;
		}

		@Override
		public void setTradingid(String _v_) { // 订单号
			if (null == _v_)
				throw new NullPointerException();
			tradingid = _v_;
		}

		@Override
		public void setTradingidOctets(com.locojoy.base.Octets _v_) { // 订单号
			this.setTradingid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPrice(int _v_) { // 价格
			price = _v_;
		}

		@Override
		public void setNum(int _v_) { // 数量
			num = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 角色id
			roleid = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 挂单时间
			createtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TradingPrice.Data)) return false;
			TradingPrice.Data _o_ = (TradingPrice.Data) _o1_;
			if (!tradingid.equals(_o_.tradingid)) return false;
			if (price != _o_.price) return false;
			if (num != _o_.num) return false;
			if (roleid != _o_.roleid) return false;
			if (createtime != _o_.createtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += tradingid.hashCode();
			_h_ += price;
			_h_ += num;
			_h_ += roleid;
			_h_ += createtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(tradingid).append("'");
			_sb_.append(",");
			_sb_.append(price);
			_sb_.append(",");
			_sb_.append(num);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

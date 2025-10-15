
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleTradingRecord extends mkdb.XBean implements xbean.RoleTradingRecord {
	private String tradingid; // 挂单号
	private long roleid; // 角色id
	private int tradingtype; // 交易类型  0买入  1出售
	private int curnum; // 当前数量
	private int allnum; // 总数量
	private int price; // 价格
	private long createtime; // 挂单时间
	private long tradingtime; // 交易时间
	private int state; // 状态    1表示已经退回或者交易完

	@Override
	public void _reset_unsafe_() {
		tradingid = "";
		roleid = 0L;
		tradingtype = 0;
		curnum = 0;
		allnum = 0;
		price = 0;
		createtime = 0L;
		tradingtime = 0L;
		state = 0;
	}

	RoleTradingRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		tradingid = "";
	}

	public RoleTradingRecord() {
		this(0, null, null);
	}

	public RoleTradingRecord(RoleTradingRecord _o_) {
		this(_o_, null, null);
	}

	RoleTradingRecord(xbean.RoleTradingRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleTradingRecord) assign((RoleTradingRecord)_o1_);
		else if (_o1_ instanceof RoleTradingRecord.Data) assign((RoleTradingRecord.Data)_o1_);
		else if (_o1_ instanceof RoleTradingRecord.Const) assign(((RoleTradingRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleTradingRecord _o_) {
		_o_._xdb_verify_unsafe_();
		tradingid = _o_.tradingid;
		roleid = _o_.roleid;
		tradingtype = _o_.tradingtype;
		curnum = _o_.curnum;
		allnum = _o_.allnum;
		price = _o_.price;
		createtime = _o_.createtime;
		tradingtime = _o_.tradingtime;
		state = _o_.state;
	}

	private void assign(RoleTradingRecord.Data _o_) {
		tradingid = _o_.tradingid;
		roleid = _o_.roleid;
		tradingtype = _o_.tradingtype;
		curnum = _o_.curnum;
		allnum = _o_.allnum;
		price = _o_.price;
		createtime = _o_.createtime;
		tradingtime = _o_.tradingtime;
		state = _o_.state;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(tradingid, mkdb.Const.IO_CHARSET);
		_os_.marshal(roleid);
		_os_.marshal(tradingtype);
		_os_.marshal(curnum);
		_os_.marshal(allnum);
		_os_.marshal(price);
		_os_.marshal(createtime);
		_os_.marshal(tradingtime);
		_os_.marshal(state);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		tradingid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		roleid = _os_.unmarshal_long();
		tradingtype = _os_.unmarshal_int();
		curnum = _os_.unmarshal_int();
		allnum = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		createtime = _os_.unmarshal_long();
		tradingtime = _os_.unmarshal_long();
		state = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RoleTradingRecord copy() {
		_xdb_verify_unsafe_();
		return new RoleTradingRecord(this);
	}

	@Override
	public xbean.RoleTradingRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleTradingRecord toBean() {
		_xdb_verify_unsafe_();
		return new RoleTradingRecord(this); // same as copy()
	}

	@Override
	public xbean.RoleTradingRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleTradingRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getTradingid() { // 挂单号
		_xdb_verify_unsafe_();
		return tradingid;
	}

	@Override
	public com.locojoy.base.Octets getTradingidOctets() { // 挂单号
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getTradingid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getRoleid() { // 角色id
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getTradingtype() { // 交易类型  0买入  1出售
		_xdb_verify_unsafe_();
		return tradingtype;
	}

	@Override
	public int getCurnum() { // 当前数量
		_xdb_verify_unsafe_();
		return curnum;
	}

	@Override
	public int getAllnum() { // 总数量
		_xdb_verify_unsafe_();
		return allnum;
	}

	@Override
	public int getPrice() { // 价格
		_xdb_verify_unsafe_();
		return price;
	}

	@Override
	public long getCreatetime() { // 挂单时间
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public long getTradingtime() { // 交易时间
		_xdb_verify_unsafe_();
		return tradingtime;
	}

	@Override
	public int getState() { // 状态    1表示已经退回或者交易完
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public void setTradingid(String _v_) { // 挂单号
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
	public void setTradingidOctets(com.locojoy.base.Octets _v_) { // 挂单号
		_xdb_verify_unsafe_();
		this.setTradingid(_v_.getString(mkdb.Const.IO_CHARSET));
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
	public void setTradingtype(int _v_) { // 交易类型  0买入  1出售
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tradingtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, tradingtype) {
					public void rollback() { tradingtype = _xdb_saved; }
				};}});
		tradingtype = _v_;
	}

	@Override
	public void setCurnum(int _v_) { // 当前数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "curnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, curnum) {
					public void rollback() { curnum = _xdb_saved; }
				};}});
		curnum = _v_;
	}

	@Override
	public void setAllnum(int _v_) { // 总数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "allnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, allnum) {
					public void rollback() { allnum = _xdb_saved; }
				};}});
		allnum = _v_;
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
	public void setTradingtime(long _v_) { // 交易时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tradingtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, tradingtime) {
					public void rollback() { tradingtime = _xdb_saved; }
				};}});
		tradingtime = _v_;
	}

	@Override
	public void setState(int _v_) { // 状态    1表示已经退回或者交易完
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleTradingRecord _o_ = null;
		if ( _o1_ instanceof RoleTradingRecord ) _o_ = (RoleTradingRecord)_o1_;
		else if ( _o1_ instanceof RoleTradingRecord.Const ) _o_ = ((RoleTradingRecord.Const)_o1_).nThis();
		else return false;
		if (!tradingid.equals(_o_.tradingid)) return false;
		if (roleid != _o_.roleid) return false;
		if (tradingtype != _o_.tradingtype) return false;
		if (curnum != _o_.curnum) return false;
		if (allnum != _o_.allnum) return false;
		if (price != _o_.price) return false;
		if (createtime != _o_.createtime) return false;
		if (tradingtime != _o_.tradingtime) return false;
		if (state != _o_.state) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += tradingid.hashCode();
		_h_ += roleid;
		_h_ += tradingtype;
		_h_ += curnum;
		_h_ += allnum;
		_h_ += price;
		_h_ += createtime;
		_h_ += tradingtime;
		_h_ += state;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(tradingid).append("'");
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(tradingtype);
		_sb_.append(",");
		_sb_.append(curnum);
		_sb_.append(",");
		_sb_.append(allnum);
		_sb_.append(",");
		_sb_.append(price);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append(tradingtime);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tradingid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tradingtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("curnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("allnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("price"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tradingtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		return lb;
	}

	private class Const implements xbean.RoleTradingRecord {
		RoleTradingRecord nThis() {
			return RoleTradingRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleTradingRecord copy() {
			return RoleTradingRecord.this.copy();
		}

		@Override
		public xbean.RoleTradingRecord toData() {
			return RoleTradingRecord.this.toData();
		}

		public xbean.RoleTradingRecord toBean() {
			return RoleTradingRecord.this.toBean();
		}

		@Override
		public xbean.RoleTradingRecord toDataIf() {
			return RoleTradingRecord.this.toDataIf();
		}

		public xbean.RoleTradingRecord toBeanIf() {
			return RoleTradingRecord.this.toBeanIf();
		}

		@Override
		public String getTradingid() { // 挂单号
			_xdb_verify_unsafe_();
			return tradingid;
		}

		@Override
		public com.locojoy.base.Octets getTradingidOctets() { // 挂单号
			_xdb_verify_unsafe_();
			return RoleTradingRecord.this.getTradingidOctets();
		}

		@Override
		public long getRoleid() { // 角色id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getTradingtype() { // 交易类型  0买入  1出售
			_xdb_verify_unsafe_();
			return tradingtype;
		}

		@Override
		public int getCurnum() { // 当前数量
			_xdb_verify_unsafe_();
			return curnum;
		}

		@Override
		public int getAllnum() { // 总数量
			_xdb_verify_unsafe_();
			return allnum;
		}

		@Override
		public int getPrice() { // 价格
			_xdb_verify_unsafe_();
			return price;
		}

		@Override
		public long getCreatetime() { // 挂单时间
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public long getTradingtime() { // 交易时间
			_xdb_verify_unsafe_();
			return tradingtime;
		}

		@Override
		public int getState() { // 状态    1表示已经退回或者交易完
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public void setTradingid(String _v_) { // 挂单号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTradingidOctets(com.locojoy.base.Octets _v_) { // 挂单号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 角色id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTradingtype(int _v_) { // 交易类型  0买入  1出售
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurnum(int _v_) { // 当前数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAllnum(int _v_) { // 总数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPrice(int _v_) { // 价格
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 挂单时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTradingtime(long _v_) { // 交易时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 状态    1表示已经退回或者交易完
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
			return RoleTradingRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleTradingRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleTradingRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleTradingRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleTradingRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleTradingRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleTradingRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleTradingRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleTradingRecord.this.toString();
		}

	}

	public static final class Data implements xbean.RoleTradingRecord {
		private String tradingid; // 挂单号
		private long roleid; // 角色id
		private int tradingtype; // 交易类型  0买入  1出售
		private int curnum; // 当前数量
		private int allnum; // 总数量
		private int price; // 价格
		private long createtime; // 挂单时间
		private long tradingtime; // 交易时间
		private int state; // 状态    1表示已经退回或者交易完

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			tradingid = "";
		}

		Data(xbean.RoleTradingRecord _o1_) {
			if (_o1_ instanceof RoleTradingRecord) assign((RoleTradingRecord)_o1_);
			else if (_o1_ instanceof RoleTradingRecord.Data) assign((RoleTradingRecord.Data)_o1_);
			else if (_o1_ instanceof RoleTradingRecord.Const) assign(((RoleTradingRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleTradingRecord _o_) {
			tradingid = _o_.tradingid;
			roleid = _o_.roleid;
			tradingtype = _o_.tradingtype;
			curnum = _o_.curnum;
			allnum = _o_.allnum;
			price = _o_.price;
			createtime = _o_.createtime;
			tradingtime = _o_.tradingtime;
			state = _o_.state;
		}

		private void assign(RoleTradingRecord.Data _o_) {
			tradingid = _o_.tradingid;
			roleid = _o_.roleid;
			tradingtype = _o_.tradingtype;
			curnum = _o_.curnum;
			allnum = _o_.allnum;
			price = _o_.price;
			createtime = _o_.createtime;
			tradingtime = _o_.tradingtime;
			state = _o_.state;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(tradingid, mkdb.Const.IO_CHARSET);
			_os_.marshal(roleid);
			_os_.marshal(tradingtype);
			_os_.marshal(curnum);
			_os_.marshal(allnum);
			_os_.marshal(price);
			_os_.marshal(createtime);
			_os_.marshal(tradingtime);
			_os_.marshal(state);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			tradingid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			roleid = _os_.unmarshal_long();
			tradingtype = _os_.unmarshal_int();
			curnum = _os_.unmarshal_int();
			allnum = _os_.unmarshal_int();
			price = _os_.unmarshal_int();
			createtime = _os_.unmarshal_long();
			tradingtime = _os_.unmarshal_long();
			state = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RoleTradingRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleTradingRecord toData() {
			return new Data(this);
		}

		public xbean.RoleTradingRecord toBean() {
			return new RoleTradingRecord(this, null, null);
		}

		@Override
		public xbean.RoleTradingRecord toDataIf() {
			return this;
		}

		public xbean.RoleTradingRecord toBeanIf() {
			return new RoleTradingRecord(this, null, null);
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
		public String getTradingid() { // 挂单号
			return tradingid;
		}

		@Override
		public com.locojoy.base.Octets getTradingidOctets() { // 挂单号
			return com.locojoy.base.Octets.wrap(getTradingid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getRoleid() { // 角色id
			return roleid;
		}

		@Override
		public int getTradingtype() { // 交易类型  0买入  1出售
			return tradingtype;
		}

		@Override
		public int getCurnum() { // 当前数量
			return curnum;
		}

		@Override
		public int getAllnum() { // 总数量
			return allnum;
		}

		@Override
		public int getPrice() { // 价格
			return price;
		}

		@Override
		public long getCreatetime() { // 挂单时间
			return createtime;
		}

		@Override
		public long getTradingtime() { // 交易时间
			return tradingtime;
		}

		@Override
		public int getState() { // 状态    1表示已经退回或者交易完
			return state;
		}

		@Override
		public void setTradingid(String _v_) { // 挂单号
			if (null == _v_)
				throw new NullPointerException();
			tradingid = _v_;
		}

		@Override
		public void setTradingidOctets(com.locojoy.base.Octets _v_) { // 挂单号
			this.setTradingid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setRoleid(long _v_) { // 角色id
			roleid = _v_;
		}

		@Override
		public void setTradingtype(int _v_) { // 交易类型  0买入  1出售
			tradingtype = _v_;
		}

		@Override
		public void setCurnum(int _v_) { // 当前数量
			curnum = _v_;
		}

		@Override
		public void setAllnum(int _v_) { // 总数量
			allnum = _v_;
		}

		@Override
		public void setPrice(int _v_) { // 价格
			price = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 挂单时间
			createtime = _v_;
		}

		@Override
		public void setTradingtime(long _v_) { // 交易时间
			tradingtime = _v_;
		}

		@Override
		public void setState(int _v_) { // 状态    1表示已经退回或者交易完
			state = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleTradingRecord.Data)) return false;
			RoleTradingRecord.Data _o_ = (RoleTradingRecord.Data) _o1_;
			if (!tradingid.equals(_o_.tradingid)) return false;
			if (roleid != _o_.roleid) return false;
			if (tradingtype != _o_.tradingtype) return false;
			if (curnum != _o_.curnum) return false;
			if (allnum != _o_.allnum) return false;
			if (price != _o_.price) return false;
			if (createtime != _o_.createtime) return false;
			if (tradingtime != _o_.tradingtime) return false;
			if (state != _o_.state) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += tradingid.hashCode();
			_h_ += roleid;
			_h_ += tradingtype;
			_h_ += curnum;
			_h_ += allnum;
			_h_ += price;
			_h_ += createtime;
			_h_ += tradingtime;
			_h_ += state;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(tradingid).append("'");
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(tradingtype);
			_sb_.append(",");
			_sb_.append(curnum);
			_sb_.append(",");
			_sb_.append(allnum);
			_sb_.append(",");
			_sb_.append(price);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append(tradingtime);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

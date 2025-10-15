
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class YybOrder extends mkdb.XBean implements xbean.YybOrder {
	private int userid; // 充值的userid
	private long roleid; // 充值的角色id
	private int status; // 0,新建订单; 1,订单完成; -1,订单错误
	private String platname; // weixin qq
	private int num; // 充值金额
	private int localsaveamt; // 本地累计充值金额
	private int localbalance; // 本地充值金额
	private int localgenbalance; // 本地赠送金额
	private long createtime; // 
	private int retrytimes; // 超过10次单子被标记为失败
	private int consumetype; // 

	@Override
	public void _reset_unsafe_() {
		userid = 0;
		roleid = 0L;
		status = 0;
		platname = "";
		num = 0;
		localsaveamt = 0;
		localbalance = 0;
		localgenbalance = 0;
		createtime = 0L;
		retrytimes = 10;
		consumetype = 0;
	}

	YybOrder(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		platname = "";
		retrytimes = 10;
	}

	public YybOrder() {
		this(0, null, null);
	}

	public YybOrder(YybOrder _o_) {
		this(_o_, null, null);
	}

	YybOrder(xbean.YybOrder _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof YybOrder) assign((YybOrder)_o1_);
		else if (_o1_ instanceof YybOrder.Data) assign((YybOrder.Data)_o1_);
		else if (_o1_ instanceof YybOrder.Const) assign(((YybOrder.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(YybOrder _o_) {
		_o_._xdb_verify_unsafe_();
		userid = _o_.userid;
		roleid = _o_.roleid;
		status = _o_.status;
		platname = _o_.platname;
		num = _o_.num;
		localsaveamt = _o_.localsaveamt;
		localbalance = _o_.localbalance;
		localgenbalance = _o_.localgenbalance;
		createtime = _o_.createtime;
		retrytimes = _o_.retrytimes;
		consumetype = _o_.consumetype;
	}

	private void assign(YybOrder.Data _o_) {
		userid = _o_.userid;
		roleid = _o_.roleid;
		status = _o_.status;
		platname = _o_.platname;
		num = _o_.num;
		localsaveamt = _o_.localsaveamt;
		localbalance = _o_.localbalance;
		localgenbalance = _o_.localgenbalance;
		createtime = _o_.createtime;
		retrytimes = _o_.retrytimes;
		consumetype = _o_.consumetype;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(userid);
		_os_.marshal(roleid);
		_os_.marshal(status);
		_os_.marshal(platname, mkdb.Const.IO_CHARSET);
		_os_.marshal(num);
		_os_.marshal(localsaveamt);
		_os_.marshal(localbalance);
		_os_.marshal(localgenbalance);
		_os_.marshal(createtime);
		_os_.marshal(retrytimes);
		_os_.marshal(consumetype);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		userid = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		status = _os_.unmarshal_int();
		platname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		num = _os_.unmarshal_int();
		localsaveamt = _os_.unmarshal_int();
		localbalance = _os_.unmarshal_int();
		localgenbalance = _os_.unmarshal_int();
		createtime = _os_.unmarshal_long();
		retrytimes = _os_.unmarshal_int();
		consumetype = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.YybOrder copy() {
		_xdb_verify_unsafe_();
		return new YybOrder(this);
	}

	@Override
	public xbean.YybOrder toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YybOrder toBean() {
		_xdb_verify_unsafe_();
		return new YybOrder(this); // same as copy()
	}

	@Override
	public xbean.YybOrder toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YybOrder toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getUserid() { // 充值的userid
		_xdb_verify_unsafe_();
		return userid;
	}

	@Override
	public long getRoleid() { // 充值的角色id
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getStatus() { // 0,新建订单; 1,订单完成; -1,订单错误
		_xdb_verify_unsafe_();
		return status;
	}

	@Override
	public String getPlatname() { // weixin qq
		_xdb_verify_unsafe_();
		return platname;
	}

	@Override
	public com.locojoy.base.Octets getPlatnameOctets() { // weixin qq
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlatname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getNum() { // 充值金额
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public int getLocalsaveamt() { // 本地累计充值金额
		_xdb_verify_unsafe_();
		return localsaveamt;
	}

	@Override
	public int getLocalbalance() { // 本地充值金额
		_xdb_verify_unsafe_();
		return localbalance;
	}

	@Override
	public int getLocalgenbalance() { // 本地赠送金额
		_xdb_verify_unsafe_();
		return localgenbalance;
	}

	@Override
	public long getCreatetime() { // 
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public int getRetrytimes() { // 超过10次单子被标记为失败
		_xdb_verify_unsafe_();
		return retrytimes;
	}

	@Override
	public int getConsumetype() { // 
		_xdb_verify_unsafe_();
		return consumetype;
	}

	@Override
	public void setUserid(int _v_) { // 充值的userid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "userid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, userid) {
					public void rollback() { userid = _xdb_saved; }
				};}});
		userid = _v_;
	}

	@Override
	public void setRoleid(long _v_) { // 充值的角色id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setStatus(int _v_) { // 0,新建订单; 1,订单完成; -1,订单错误
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "status") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, status) {
					public void rollback() { status = _xdb_saved; }
				};}});
		status = _v_;
	}

	@Override
	public void setPlatname(String _v_) { // weixin qq
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "platname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, platname) {
					public void rollback() { platname = _xdb_saved; }
				};}});
		platname = _v_;
	}

	@Override
	public void setPlatnameOctets(com.locojoy.base.Octets _v_) { // weixin qq
		_xdb_verify_unsafe_();
		this.setPlatname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setNum(int _v_) { // 充值金额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "num") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, num) {
					public void rollback() { num = _xdb_saved; }
				};}});
		num = _v_;
	}

	@Override
	public void setLocalsaveamt(int _v_) { // 本地累计充值金额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "localsaveamt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, localsaveamt) {
					public void rollback() { localsaveamt = _xdb_saved; }
				};}});
		localsaveamt = _v_;
	}

	@Override
	public void setLocalbalance(int _v_) { // 本地充值金额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "localbalance") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, localbalance) {
					public void rollback() { localbalance = _xdb_saved; }
				};}});
		localbalance = _v_;
	}

	@Override
	public void setLocalgenbalance(int _v_) { // 本地赠送金额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "localgenbalance") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, localgenbalance) {
					public void rollback() { localgenbalance = _xdb_saved; }
				};}});
		localgenbalance = _v_;
	}

	@Override
	public void setCreatetime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public void setRetrytimes(int _v_) { // 超过10次单子被标记为失败
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "retrytimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, retrytimes) {
					public void rollback() { retrytimes = _xdb_saved; }
				};}});
		retrytimes = _v_;
	}

	@Override
	public void setConsumetype(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "consumetype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, consumetype) {
					public void rollback() { consumetype = _xdb_saved; }
				};}});
		consumetype = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		YybOrder _o_ = null;
		if ( _o1_ instanceof YybOrder ) _o_ = (YybOrder)_o1_;
		else if ( _o1_ instanceof YybOrder.Const ) _o_ = ((YybOrder.Const)_o1_).nThis();
		else return false;
		if (userid != _o_.userid) return false;
		if (roleid != _o_.roleid) return false;
		if (status != _o_.status) return false;
		if (!platname.equals(_o_.platname)) return false;
		if (num != _o_.num) return false;
		if (localsaveamt != _o_.localsaveamt) return false;
		if (localbalance != _o_.localbalance) return false;
		if (localgenbalance != _o_.localgenbalance) return false;
		if (createtime != _o_.createtime) return false;
		if (retrytimes != _o_.retrytimes) return false;
		if (consumetype != _o_.consumetype) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += userid;
		_h_ += roleid;
		_h_ += status;
		_h_ += platname.hashCode();
		_h_ += num;
		_h_ += localsaveamt;
		_h_ += localbalance;
		_h_ += localgenbalance;
		_h_ += createtime;
		_h_ += retrytimes;
		_h_ += consumetype;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(status);
		_sb_.append(",");
		_sb_.append("'").append(platname).append("'");
		_sb_.append(",");
		_sb_.append(num);
		_sb_.append(",");
		_sb_.append(localsaveamt);
		_sb_.append(",");
		_sb_.append(localbalance);
		_sb_.append(",");
		_sb_.append(localgenbalance);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append(retrytimes);
		_sb_.append(",");
		_sb_.append(consumetype);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("userid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("status"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("platname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("localsaveamt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("localbalance"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("localgenbalance"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("retrytimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("consumetype"));
		return lb;
	}

	private class Const implements xbean.YybOrder {
		YybOrder nThis() {
			return YybOrder.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.YybOrder copy() {
			return YybOrder.this.copy();
		}

		@Override
		public xbean.YybOrder toData() {
			return YybOrder.this.toData();
		}

		public xbean.YybOrder toBean() {
			return YybOrder.this.toBean();
		}

		@Override
		public xbean.YybOrder toDataIf() {
			return YybOrder.this.toDataIf();
		}

		public xbean.YybOrder toBeanIf() {
			return YybOrder.this.toBeanIf();
		}

		@Override
		public int getUserid() { // 充值的userid
			_xdb_verify_unsafe_();
			return userid;
		}

		@Override
		public long getRoleid() { // 充值的角色id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getStatus() { // 0,新建订单; 1,订单完成; -1,订单错误
			_xdb_verify_unsafe_();
			return status;
		}

		@Override
		public String getPlatname() { // weixin qq
			_xdb_verify_unsafe_();
			return platname;
		}

		@Override
		public com.locojoy.base.Octets getPlatnameOctets() { // weixin qq
			_xdb_verify_unsafe_();
			return YybOrder.this.getPlatnameOctets();
		}

		@Override
		public int getNum() { // 充值金额
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public int getLocalsaveamt() { // 本地累计充值金额
			_xdb_verify_unsafe_();
			return localsaveamt;
		}

		@Override
		public int getLocalbalance() { // 本地充值金额
			_xdb_verify_unsafe_();
			return localbalance;
		}

		@Override
		public int getLocalgenbalance() { // 本地赠送金额
			_xdb_verify_unsafe_();
			return localgenbalance;
		}

		@Override
		public long getCreatetime() { // 
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public int getRetrytimes() { // 超过10次单子被标记为失败
			_xdb_verify_unsafe_();
			return retrytimes;
		}

		@Override
		public int getConsumetype() { // 
			_xdb_verify_unsafe_();
			return consumetype;
		}

		@Override
		public void setUserid(int _v_) { // 充值的userid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 充值的角色id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStatus(int _v_) { // 0,新建订单; 1,订单完成; -1,订单错误
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatname(String _v_) { // weixin qq
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatnameOctets(com.locojoy.base.Octets _v_) { // weixin qq
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNum(int _v_) { // 充值金额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLocalsaveamt(int _v_) { // 本地累计充值金额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLocalbalance(int _v_) { // 本地充值金额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLocalgenbalance(int _v_) { // 本地赠送金额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRetrytimes(int _v_) { // 超过10次单子被标记为失败
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setConsumetype(int _v_) { // 
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
			return YybOrder.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return YybOrder.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return YybOrder.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return YybOrder.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return YybOrder.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return YybOrder.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return YybOrder.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return YybOrder.this.hashCode();
		}

		@Override
		public String toString() {
			return YybOrder.this.toString();
		}

	}

	public static final class Data implements xbean.YybOrder {
		private int userid; // 充值的userid
		private long roleid; // 充值的角色id
		private int status; // 0,新建订单; 1,订单完成; -1,订单错误
		private String platname; // weixin qq
		private int num; // 充值金额
		private int localsaveamt; // 本地累计充值金额
		private int localbalance; // 本地充值金额
		private int localgenbalance; // 本地赠送金额
		private long createtime; // 
		private int retrytimes; // 超过10次单子被标记为失败
		private int consumetype; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			platname = "";
			retrytimes = 10;
		}

		Data(xbean.YybOrder _o1_) {
			if (_o1_ instanceof YybOrder) assign((YybOrder)_o1_);
			else if (_o1_ instanceof YybOrder.Data) assign((YybOrder.Data)_o1_);
			else if (_o1_ instanceof YybOrder.Const) assign(((YybOrder.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(YybOrder _o_) {
			userid = _o_.userid;
			roleid = _o_.roleid;
			status = _o_.status;
			platname = _o_.platname;
			num = _o_.num;
			localsaveamt = _o_.localsaveamt;
			localbalance = _o_.localbalance;
			localgenbalance = _o_.localgenbalance;
			createtime = _o_.createtime;
			retrytimes = _o_.retrytimes;
			consumetype = _o_.consumetype;
		}

		private void assign(YybOrder.Data _o_) {
			userid = _o_.userid;
			roleid = _o_.roleid;
			status = _o_.status;
			platname = _o_.platname;
			num = _o_.num;
			localsaveamt = _o_.localsaveamt;
			localbalance = _o_.localbalance;
			localgenbalance = _o_.localgenbalance;
			createtime = _o_.createtime;
			retrytimes = _o_.retrytimes;
			consumetype = _o_.consumetype;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(userid);
			_os_.marshal(roleid);
			_os_.marshal(status);
			_os_.marshal(platname, mkdb.Const.IO_CHARSET);
			_os_.marshal(num);
			_os_.marshal(localsaveamt);
			_os_.marshal(localbalance);
			_os_.marshal(localgenbalance);
			_os_.marshal(createtime);
			_os_.marshal(retrytimes);
			_os_.marshal(consumetype);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			userid = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			status = _os_.unmarshal_int();
			platname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			num = _os_.unmarshal_int();
			localsaveamt = _os_.unmarshal_int();
			localbalance = _os_.unmarshal_int();
			localgenbalance = _os_.unmarshal_int();
			createtime = _os_.unmarshal_long();
			retrytimes = _os_.unmarshal_int();
			consumetype = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.YybOrder copy() {
			return new Data(this);
		}

		@Override
		public xbean.YybOrder toData() {
			return new Data(this);
		}

		public xbean.YybOrder toBean() {
			return new YybOrder(this, null, null);
		}

		@Override
		public xbean.YybOrder toDataIf() {
			return this;
		}

		public xbean.YybOrder toBeanIf() {
			return new YybOrder(this, null, null);
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
		public int getUserid() { // 充值的userid
			return userid;
		}

		@Override
		public long getRoleid() { // 充值的角色id
			return roleid;
		}

		@Override
		public int getStatus() { // 0,新建订单; 1,订单完成; -1,订单错误
			return status;
		}

		@Override
		public String getPlatname() { // weixin qq
			return platname;
		}

		@Override
		public com.locojoy.base.Octets getPlatnameOctets() { // weixin qq
			return com.locojoy.base.Octets.wrap(getPlatname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getNum() { // 充值金额
			return num;
		}

		@Override
		public int getLocalsaveamt() { // 本地累计充值金额
			return localsaveamt;
		}

		@Override
		public int getLocalbalance() { // 本地充值金额
			return localbalance;
		}

		@Override
		public int getLocalgenbalance() { // 本地赠送金额
			return localgenbalance;
		}

		@Override
		public long getCreatetime() { // 
			return createtime;
		}

		@Override
		public int getRetrytimes() { // 超过10次单子被标记为失败
			return retrytimes;
		}

		@Override
		public int getConsumetype() { // 
			return consumetype;
		}

		@Override
		public void setUserid(int _v_) { // 充值的userid
			userid = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 充值的角色id
			roleid = _v_;
		}

		@Override
		public void setStatus(int _v_) { // 0,新建订单; 1,订单完成; -1,订单错误
			status = _v_;
		}

		@Override
		public void setPlatname(String _v_) { // weixin qq
			if (null == _v_)
				throw new NullPointerException();
			platname = _v_;
		}

		@Override
		public void setPlatnameOctets(com.locojoy.base.Octets _v_) { // weixin qq
			this.setPlatname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setNum(int _v_) { // 充值金额
			num = _v_;
		}

		@Override
		public void setLocalsaveamt(int _v_) { // 本地累计充值金额
			localsaveamt = _v_;
		}

		@Override
		public void setLocalbalance(int _v_) { // 本地充值金额
			localbalance = _v_;
		}

		@Override
		public void setLocalgenbalance(int _v_) { // 本地赠送金额
			localgenbalance = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 
			createtime = _v_;
		}

		@Override
		public void setRetrytimes(int _v_) { // 超过10次单子被标记为失败
			retrytimes = _v_;
		}

		@Override
		public void setConsumetype(int _v_) { // 
			consumetype = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof YybOrder.Data)) return false;
			YybOrder.Data _o_ = (YybOrder.Data) _o1_;
			if (userid != _o_.userid) return false;
			if (roleid != _o_.roleid) return false;
			if (status != _o_.status) return false;
			if (!platname.equals(_o_.platname)) return false;
			if (num != _o_.num) return false;
			if (localsaveamt != _o_.localsaveamt) return false;
			if (localbalance != _o_.localbalance) return false;
			if (localgenbalance != _o_.localgenbalance) return false;
			if (createtime != _o_.createtime) return false;
			if (retrytimes != _o_.retrytimes) return false;
			if (consumetype != _o_.consumetype) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += userid;
			_h_ += roleid;
			_h_ += status;
			_h_ += platname.hashCode();
			_h_ += num;
			_h_ += localsaveamt;
			_h_ += localbalance;
			_h_ += localgenbalance;
			_h_ += createtime;
			_h_ += retrytimes;
			_h_ += consumetype;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(userid);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(status);
			_sb_.append(",");
			_sb_.append("'").append(platname).append("'");
			_sb_.append(",");
			_sb_.append(num);
			_sb_.append(",");
			_sb_.append(localsaveamt);
			_sb_.append(",");
			_sb_.append(localbalance);
			_sb_.append(",");
			_sb_.append(localgenbalance);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append(retrytimes);
			_sb_.append(",");
			_sb_.append(consumetype);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ChargeOrder extends mkdb.XBean implements xbean.ChargeOrder {
	private int status; // 
	private String platformsn; // 充值的平台的sn
	private String plattype; // 来自哪个平台的充值
	private String platformuid; // 充值的平台的账号id
	private String receipt; // 只有苹果官方平台才有
	private String transcationid; // 只有苹果官方平台才有
	private long roleid; // 充值的角色id
	private int num; // 充值金额
	private int goodid; // 
	private int goodnum; // 
	private long createtime; // 
	private int userid; // 充值的userid
	private int retrytimes; // 超过10次单子被标记为失败

	@Override
	public void _reset_unsafe_() {
		status = 0;
		platformsn = "";
		plattype = "";
		platformuid = "";
		receipt = "";
		transcationid = "";
		roleid = 0L;
		num = 0;
		goodid = 0;
		goodnum = 0;
		createtime = 0L;
		userid = 0;
		retrytimes = 10;
	}

	ChargeOrder(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		platformsn = "";
		plattype = "";
		platformuid = "";
		receipt = "";
		transcationid = "";
		retrytimes = 10;
	}

	public ChargeOrder() {
		this(0, null, null);
	}

	public ChargeOrder(ChargeOrder _o_) {
		this(_o_, null, null);
	}

	ChargeOrder(xbean.ChargeOrder _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ChargeOrder) assign((ChargeOrder)_o1_);
		else if (_o1_ instanceof ChargeOrder.Data) assign((ChargeOrder.Data)_o1_);
		else if (_o1_ instanceof ChargeOrder.Const) assign(((ChargeOrder.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ChargeOrder _o_) {
		_o_._xdb_verify_unsafe_();
		status = _o_.status;
		platformsn = _o_.platformsn;
		plattype = _o_.plattype;
		platformuid = _o_.platformuid;
		receipt = _o_.receipt;
		transcationid = _o_.transcationid;
		roleid = _o_.roleid;
		num = _o_.num;
		goodid = _o_.goodid;
		goodnum = _o_.goodnum;
		createtime = _o_.createtime;
		userid = _o_.userid;
		retrytimes = _o_.retrytimes;
	}

	private void assign(ChargeOrder.Data _o_) {
		status = _o_.status;
		platformsn = _o_.platformsn;
		plattype = _o_.plattype;
		platformuid = _o_.platformuid;
		receipt = _o_.receipt;
		transcationid = _o_.transcationid;
		roleid = _o_.roleid;
		num = _o_.num;
		goodid = _o_.goodid;
		goodnum = _o_.goodnum;
		createtime = _o_.createtime;
		userid = _o_.userid;
		retrytimes = _o_.retrytimes;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(status);
		_os_.marshal(platformsn, mkdb.Const.IO_CHARSET);
		_os_.marshal(plattype, mkdb.Const.IO_CHARSET);
		_os_.marshal(platformuid, mkdb.Const.IO_CHARSET);
		_os_.marshal(receipt, mkdb.Const.IO_CHARSET);
		_os_.marshal(transcationid, mkdb.Const.IO_CHARSET);
		_os_.marshal(roleid);
		_os_.marshal(num);
		_os_.marshal(goodid);
		_os_.marshal(goodnum);
		_os_.marshal(createtime);
		_os_.marshal(userid);
		_os_.marshal(retrytimes);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		status = _os_.unmarshal_int();
		platformsn = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		plattype = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		platformuid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		receipt = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		transcationid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		roleid = _os_.unmarshal_long();
		num = _os_.unmarshal_int();
		goodid = _os_.unmarshal_int();
		goodnum = _os_.unmarshal_int();
		createtime = _os_.unmarshal_long();
		userid = _os_.unmarshal_int();
		retrytimes = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ChargeOrder copy() {
		_xdb_verify_unsafe_();
		return new ChargeOrder(this);
	}

	@Override
	public xbean.ChargeOrder toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChargeOrder toBean() {
		_xdb_verify_unsafe_();
		return new ChargeOrder(this); // same as copy()
	}

	@Override
	public xbean.ChargeOrder toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChargeOrder toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getStatus() { // 
		_xdb_verify_unsafe_();
		return status;
	}

	@Override
	public String getPlatformsn() { // 充值的平台的sn
		_xdb_verify_unsafe_();
		return platformsn;
	}

	@Override
	public com.locojoy.base.Octets getPlatformsnOctets() { // 充值的平台的sn
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlatformsn(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPlattype() { // 来自哪个平台的充值
		_xdb_verify_unsafe_();
		return plattype;
	}

	@Override
	public com.locojoy.base.Octets getPlattypeOctets() { // 来自哪个平台的充值
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlattype(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPlatformuid() { // 充值的平台的账号id
		_xdb_verify_unsafe_();
		return platformuid;
	}

	@Override
	public com.locojoy.base.Octets getPlatformuidOctets() { // 充值的平台的账号id
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlatformuid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getReceipt() { // 只有苹果官方平台才有
		_xdb_verify_unsafe_();
		return receipt;
	}

	@Override
	public com.locojoy.base.Octets getReceiptOctets() { // 只有苹果官方平台才有
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getReceipt(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getTranscationid() { // 只有苹果官方平台才有
		_xdb_verify_unsafe_();
		return transcationid;
	}

	@Override
	public com.locojoy.base.Octets getTranscationidOctets() { // 只有苹果官方平台才有
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getTranscationid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getRoleid() { // 充值的角色id
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getNum() { // 充值金额
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public int getGoodid() { // 
		_xdb_verify_unsafe_();
		return goodid;
	}

	@Override
	public int getGoodnum() { // 
		_xdb_verify_unsafe_();
		return goodnum;
	}

	@Override
	public long getCreatetime() { // 
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public int getUserid() { // 充值的userid
		_xdb_verify_unsafe_();
		return userid;
	}

	@Override
	public int getRetrytimes() { // 超过10次单子被标记为失败
		_xdb_verify_unsafe_();
		return retrytimes;
	}

	@Override
	public void setStatus(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "status") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, status) {
					public void rollback() { status = _xdb_saved; }
				};}});
		status = _v_;
	}

	@Override
	public void setPlatformsn(String _v_) { // 充值的平台的sn
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "platformsn") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, platformsn) {
					public void rollback() { platformsn = _xdb_saved; }
				};}});
		platformsn = _v_;
	}

	@Override
	public void setPlatformsnOctets(com.locojoy.base.Octets _v_) { // 充值的平台的sn
		_xdb_verify_unsafe_();
		this.setPlatformsn(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPlattype(String _v_) { // 来自哪个平台的充值
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "plattype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, plattype) {
					public void rollback() { plattype = _xdb_saved; }
				};}});
		plattype = _v_;
	}

	@Override
	public void setPlattypeOctets(com.locojoy.base.Octets _v_) { // 来自哪个平台的充值
		_xdb_verify_unsafe_();
		this.setPlattype(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPlatformuid(String _v_) { // 充值的平台的账号id
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "platformuid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, platformuid) {
					public void rollback() { platformuid = _xdb_saved; }
				};}});
		platformuid = _v_;
	}

	@Override
	public void setPlatformuidOctets(com.locojoy.base.Octets _v_) { // 充值的平台的账号id
		_xdb_verify_unsafe_();
		this.setPlatformuid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setReceipt(String _v_) { // 只有苹果官方平台才有
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receipt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, receipt) {
					public void rollback() { receipt = _xdb_saved; }
				};}});
		receipt = _v_;
	}

	@Override
	public void setReceiptOctets(com.locojoy.base.Octets _v_) { // 只有苹果官方平台才有
		_xdb_verify_unsafe_();
		this.setReceipt(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setTranscationid(String _v_) { // 只有苹果官方平台才有
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "transcationid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, transcationid) {
					public void rollback() { transcationid = _xdb_saved; }
				};}});
		transcationid = _v_;
	}

	@Override
	public void setTranscationidOctets(com.locojoy.base.Octets _v_) { // 只有苹果官方平台才有
		_xdb_verify_unsafe_();
		this.setTranscationid(_v_.getString(mkdb.Const.IO_CHARSET));
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
	public void setGoodid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "goodid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, goodid) {
					public void rollback() { goodid = _xdb_saved; }
				};}});
		goodid = _v_;
	}

	@Override
	public void setGoodnum(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "goodnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, goodnum) {
					public void rollback() { goodnum = _xdb_saved; }
				};}});
		goodnum = _v_;
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
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ChargeOrder _o_ = null;
		if ( _o1_ instanceof ChargeOrder ) _o_ = (ChargeOrder)_o1_;
		else if ( _o1_ instanceof ChargeOrder.Const ) _o_ = ((ChargeOrder.Const)_o1_).nThis();
		else return false;
		if (status != _o_.status) return false;
		if (!platformsn.equals(_o_.platformsn)) return false;
		if (!plattype.equals(_o_.plattype)) return false;
		if (!platformuid.equals(_o_.platformuid)) return false;
		if (!receipt.equals(_o_.receipt)) return false;
		if (!transcationid.equals(_o_.transcationid)) return false;
		if (roleid != _o_.roleid) return false;
		if (num != _o_.num) return false;
		if (goodid != _o_.goodid) return false;
		if (goodnum != _o_.goodnum) return false;
		if (createtime != _o_.createtime) return false;
		if (userid != _o_.userid) return false;
		if (retrytimes != _o_.retrytimes) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += status;
		_h_ += platformsn.hashCode();
		_h_ += plattype.hashCode();
		_h_ += platformuid.hashCode();
		_h_ += receipt.hashCode();
		_h_ += transcationid.hashCode();
		_h_ += roleid;
		_h_ += num;
		_h_ += goodid;
		_h_ += goodnum;
		_h_ += createtime;
		_h_ += userid;
		_h_ += retrytimes;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(status);
		_sb_.append(",");
		_sb_.append("'").append(platformsn).append("'");
		_sb_.append(",");
		_sb_.append("'").append(plattype).append("'");
		_sb_.append(",");
		_sb_.append("'").append(platformuid).append("'");
		_sb_.append(",");
		_sb_.append("'").append(receipt).append("'");
		_sb_.append(",");
		_sb_.append("'").append(transcationid).append("'");
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(num);
		_sb_.append(",");
		_sb_.append(goodid);
		_sb_.append(",");
		_sb_.append(goodnum);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append(userid);
		_sb_.append(",");
		_sb_.append(retrytimes);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("status"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("platformsn"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("plattype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("platformuid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receipt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("transcationid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("goodid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("goodnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("userid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("retrytimes"));
		return lb;
	}

	private class Const implements xbean.ChargeOrder {
		ChargeOrder nThis() {
			return ChargeOrder.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ChargeOrder copy() {
			return ChargeOrder.this.copy();
		}

		@Override
		public xbean.ChargeOrder toData() {
			return ChargeOrder.this.toData();
		}

		public xbean.ChargeOrder toBean() {
			return ChargeOrder.this.toBean();
		}

		@Override
		public xbean.ChargeOrder toDataIf() {
			return ChargeOrder.this.toDataIf();
		}

		public xbean.ChargeOrder toBeanIf() {
			return ChargeOrder.this.toBeanIf();
		}

		@Override
		public int getStatus() { // 
			_xdb_verify_unsafe_();
			return status;
		}

		@Override
		public String getPlatformsn() { // 充值的平台的sn
			_xdb_verify_unsafe_();
			return platformsn;
		}

		@Override
		public com.locojoy.base.Octets getPlatformsnOctets() { // 充值的平台的sn
			_xdb_verify_unsafe_();
			return ChargeOrder.this.getPlatformsnOctets();
		}

		@Override
		public String getPlattype() { // 来自哪个平台的充值
			_xdb_verify_unsafe_();
			return plattype;
		}

		@Override
		public com.locojoy.base.Octets getPlattypeOctets() { // 来自哪个平台的充值
			_xdb_verify_unsafe_();
			return ChargeOrder.this.getPlattypeOctets();
		}

		@Override
		public String getPlatformuid() { // 充值的平台的账号id
			_xdb_verify_unsafe_();
			return platformuid;
		}

		@Override
		public com.locojoy.base.Octets getPlatformuidOctets() { // 充值的平台的账号id
			_xdb_verify_unsafe_();
			return ChargeOrder.this.getPlatformuidOctets();
		}

		@Override
		public String getReceipt() { // 只有苹果官方平台才有
			_xdb_verify_unsafe_();
			return receipt;
		}

		@Override
		public com.locojoy.base.Octets getReceiptOctets() { // 只有苹果官方平台才有
			_xdb_verify_unsafe_();
			return ChargeOrder.this.getReceiptOctets();
		}

		@Override
		public String getTranscationid() { // 只有苹果官方平台才有
			_xdb_verify_unsafe_();
			return transcationid;
		}

		@Override
		public com.locojoy.base.Octets getTranscationidOctets() { // 只有苹果官方平台才有
			_xdb_verify_unsafe_();
			return ChargeOrder.this.getTranscationidOctets();
		}

		@Override
		public long getRoleid() { // 充值的角色id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getNum() { // 充值金额
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public int getGoodid() { // 
			_xdb_verify_unsafe_();
			return goodid;
		}

		@Override
		public int getGoodnum() { // 
			_xdb_verify_unsafe_();
			return goodnum;
		}

		@Override
		public long getCreatetime() { // 
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public int getUserid() { // 充值的userid
			_xdb_verify_unsafe_();
			return userid;
		}

		@Override
		public int getRetrytimes() { // 超过10次单子被标记为失败
			_xdb_verify_unsafe_();
			return retrytimes;
		}

		@Override
		public void setStatus(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformsn(String _v_) { // 充值的平台的sn
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformsnOctets(com.locojoy.base.Octets _v_) { // 充值的平台的sn
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlattype(String _v_) { // 来自哪个平台的充值
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlattypeOctets(com.locojoy.base.Octets _v_) { // 来自哪个平台的充值
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformuid(String _v_) { // 充值的平台的账号id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformuidOctets(com.locojoy.base.Octets _v_) { // 充值的平台的账号id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceipt(String _v_) { // 只有苹果官方平台才有
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceiptOctets(com.locojoy.base.Octets _v_) { // 只有苹果官方平台才有
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTranscationid(String _v_) { // 只有苹果官方平台才有
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTranscationidOctets(com.locojoy.base.Octets _v_) { // 只有苹果官方平台才有
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 充值的角色id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNum(int _v_) { // 充值金额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGoodid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGoodnum(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUserid(int _v_) { // 充值的userid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRetrytimes(int _v_) { // 超过10次单子被标记为失败
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
			return ChargeOrder.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ChargeOrder.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ChargeOrder.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ChargeOrder.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ChargeOrder.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ChargeOrder.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ChargeOrder.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ChargeOrder.this.hashCode();
		}

		@Override
		public String toString() {
			return ChargeOrder.this.toString();
		}

	}

	public static final class Data implements xbean.ChargeOrder {
		private int status; // 
		private String platformsn; // 充值的平台的sn
		private String plattype; // 来自哪个平台的充值
		private String platformuid; // 充值的平台的账号id
		private String receipt; // 只有苹果官方平台才有
		private String transcationid; // 只有苹果官方平台才有
		private long roleid; // 充值的角色id
		private int num; // 充值金额
		private int goodid; // 
		private int goodnum; // 
		private long createtime; // 
		private int userid; // 充值的userid
		private int retrytimes; // 超过10次单子被标记为失败

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			platformsn = "";
			plattype = "";
			platformuid = "";
			receipt = "";
			transcationid = "";
			retrytimes = 10;
		}

		Data(xbean.ChargeOrder _o1_) {
			if (_o1_ instanceof ChargeOrder) assign((ChargeOrder)_o1_);
			else if (_o1_ instanceof ChargeOrder.Data) assign((ChargeOrder.Data)_o1_);
			else if (_o1_ instanceof ChargeOrder.Const) assign(((ChargeOrder.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ChargeOrder _o_) {
			status = _o_.status;
			platformsn = _o_.platformsn;
			plattype = _o_.plattype;
			platformuid = _o_.platformuid;
			receipt = _o_.receipt;
			transcationid = _o_.transcationid;
			roleid = _o_.roleid;
			num = _o_.num;
			goodid = _o_.goodid;
			goodnum = _o_.goodnum;
			createtime = _o_.createtime;
			userid = _o_.userid;
			retrytimes = _o_.retrytimes;
		}

		private void assign(ChargeOrder.Data _o_) {
			status = _o_.status;
			platformsn = _o_.platformsn;
			plattype = _o_.plattype;
			platformuid = _o_.platformuid;
			receipt = _o_.receipt;
			transcationid = _o_.transcationid;
			roleid = _o_.roleid;
			num = _o_.num;
			goodid = _o_.goodid;
			goodnum = _o_.goodnum;
			createtime = _o_.createtime;
			userid = _o_.userid;
			retrytimes = _o_.retrytimes;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(status);
			_os_.marshal(platformsn, mkdb.Const.IO_CHARSET);
			_os_.marshal(plattype, mkdb.Const.IO_CHARSET);
			_os_.marshal(platformuid, mkdb.Const.IO_CHARSET);
			_os_.marshal(receipt, mkdb.Const.IO_CHARSET);
			_os_.marshal(transcationid, mkdb.Const.IO_CHARSET);
			_os_.marshal(roleid);
			_os_.marshal(num);
			_os_.marshal(goodid);
			_os_.marshal(goodnum);
			_os_.marshal(createtime);
			_os_.marshal(userid);
			_os_.marshal(retrytimes);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			status = _os_.unmarshal_int();
			platformsn = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			plattype = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			platformuid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			receipt = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			transcationid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			roleid = _os_.unmarshal_long();
			num = _os_.unmarshal_int();
			goodid = _os_.unmarshal_int();
			goodnum = _os_.unmarshal_int();
			createtime = _os_.unmarshal_long();
			userid = _os_.unmarshal_int();
			retrytimes = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ChargeOrder copy() {
			return new Data(this);
		}

		@Override
		public xbean.ChargeOrder toData() {
			return new Data(this);
		}

		public xbean.ChargeOrder toBean() {
			return new ChargeOrder(this, null, null);
		}

		@Override
		public xbean.ChargeOrder toDataIf() {
			return this;
		}

		public xbean.ChargeOrder toBeanIf() {
			return new ChargeOrder(this, null, null);
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
		public int getStatus() { // 
			return status;
		}

		@Override
		public String getPlatformsn() { // 充值的平台的sn
			return platformsn;
		}

		@Override
		public com.locojoy.base.Octets getPlatformsnOctets() { // 充值的平台的sn
			return com.locojoy.base.Octets.wrap(getPlatformsn(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPlattype() { // 来自哪个平台的充值
			return plattype;
		}

		@Override
		public com.locojoy.base.Octets getPlattypeOctets() { // 来自哪个平台的充值
			return com.locojoy.base.Octets.wrap(getPlattype(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPlatformuid() { // 充值的平台的账号id
			return platformuid;
		}

		@Override
		public com.locojoy.base.Octets getPlatformuidOctets() { // 充值的平台的账号id
			return com.locojoy.base.Octets.wrap(getPlatformuid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getReceipt() { // 只有苹果官方平台才有
			return receipt;
		}

		@Override
		public com.locojoy.base.Octets getReceiptOctets() { // 只有苹果官方平台才有
			return com.locojoy.base.Octets.wrap(getReceipt(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getTranscationid() { // 只有苹果官方平台才有
			return transcationid;
		}

		@Override
		public com.locojoy.base.Octets getTranscationidOctets() { // 只有苹果官方平台才有
			return com.locojoy.base.Octets.wrap(getTranscationid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getRoleid() { // 充值的角色id
			return roleid;
		}

		@Override
		public int getNum() { // 充值金额
			return num;
		}

		@Override
		public int getGoodid() { // 
			return goodid;
		}

		@Override
		public int getGoodnum() { // 
			return goodnum;
		}

		@Override
		public long getCreatetime() { // 
			return createtime;
		}

		@Override
		public int getUserid() { // 充值的userid
			return userid;
		}

		@Override
		public int getRetrytimes() { // 超过10次单子被标记为失败
			return retrytimes;
		}

		@Override
		public void setStatus(int _v_) { // 
			status = _v_;
		}

		@Override
		public void setPlatformsn(String _v_) { // 充值的平台的sn
			if (null == _v_)
				throw new NullPointerException();
			platformsn = _v_;
		}

		@Override
		public void setPlatformsnOctets(com.locojoy.base.Octets _v_) { // 充值的平台的sn
			this.setPlatformsn(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPlattype(String _v_) { // 来自哪个平台的充值
			if (null == _v_)
				throw new NullPointerException();
			plattype = _v_;
		}

		@Override
		public void setPlattypeOctets(com.locojoy.base.Octets _v_) { // 来自哪个平台的充值
			this.setPlattype(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPlatformuid(String _v_) { // 充值的平台的账号id
			if (null == _v_)
				throw new NullPointerException();
			platformuid = _v_;
		}

		@Override
		public void setPlatformuidOctets(com.locojoy.base.Octets _v_) { // 充值的平台的账号id
			this.setPlatformuid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setReceipt(String _v_) { // 只有苹果官方平台才有
			if (null == _v_)
				throw new NullPointerException();
			receipt = _v_;
		}

		@Override
		public void setReceiptOctets(com.locojoy.base.Octets _v_) { // 只有苹果官方平台才有
			this.setReceipt(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setTranscationid(String _v_) { // 只有苹果官方平台才有
			if (null == _v_)
				throw new NullPointerException();
			transcationid = _v_;
		}

		@Override
		public void setTranscationidOctets(com.locojoy.base.Octets _v_) { // 只有苹果官方平台才有
			this.setTranscationid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setRoleid(long _v_) { // 充值的角色id
			roleid = _v_;
		}

		@Override
		public void setNum(int _v_) { // 充值金额
			num = _v_;
		}

		@Override
		public void setGoodid(int _v_) { // 
			goodid = _v_;
		}

		@Override
		public void setGoodnum(int _v_) { // 
			goodnum = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 
			createtime = _v_;
		}

		@Override
		public void setUserid(int _v_) { // 充值的userid
			userid = _v_;
		}

		@Override
		public void setRetrytimes(int _v_) { // 超过10次单子被标记为失败
			retrytimes = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ChargeOrder.Data)) return false;
			ChargeOrder.Data _o_ = (ChargeOrder.Data) _o1_;
			if (status != _o_.status) return false;
			if (!platformsn.equals(_o_.platformsn)) return false;
			if (!plattype.equals(_o_.plattype)) return false;
			if (!platformuid.equals(_o_.platformuid)) return false;
			if (!receipt.equals(_o_.receipt)) return false;
			if (!transcationid.equals(_o_.transcationid)) return false;
			if (roleid != _o_.roleid) return false;
			if (num != _o_.num) return false;
			if (goodid != _o_.goodid) return false;
			if (goodnum != _o_.goodnum) return false;
			if (createtime != _o_.createtime) return false;
			if (userid != _o_.userid) return false;
			if (retrytimes != _o_.retrytimes) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += status;
			_h_ += platformsn.hashCode();
			_h_ += plattype.hashCode();
			_h_ += platformuid.hashCode();
			_h_ += receipt.hashCode();
			_h_ += transcationid.hashCode();
			_h_ += roleid;
			_h_ += num;
			_h_ += goodid;
			_h_ += goodnum;
			_h_ += createtime;
			_h_ += userid;
			_h_ += retrytimes;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(status);
			_sb_.append(",");
			_sb_.append("'").append(platformsn).append("'");
			_sb_.append(",");
			_sb_.append("'").append(plattype).append("'");
			_sb_.append(",");
			_sb_.append("'").append(platformuid).append("'");
			_sb_.append(",");
			_sb_.append("'").append(receipt).append("'");
			_sb_.append(",");
			_sb_.append("'").append(transcationid).append("'");
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(num);
			_sb_.append(",");
			_sb_.append(goodid);
			_sb_.append(",");
			_sb_.append(goodnum);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append(userid);
			_sb_.append(",");
			_sb_.append(retrytimes);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

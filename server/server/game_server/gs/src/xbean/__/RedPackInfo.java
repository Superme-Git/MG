
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RedPackInfo extends mkdb.XBean implements xbean.RedPackInfo {
	private String redpackid; // 红包Id
	private int modeltype; // 红包类型
	private long valuekey; // 工会或者队伍id
	private long sendroleid; // 角色Id
	private String redpackdes; // 红包寄语
	private int redpackstate; // 红包状态
	private int redpackallnum; // 红包总个数
	private int redpackreceivednum; // 红包已经领取个数
	private int redpackallmoney; // 红包总金额
	private int redpackreceiveallmoney; // 已经领取红包总金额  便于退回红包
	private long sendtime; // 发送时间
	private int backflag; // 0没退   1已经退钱了
	private java.util.ArrayList<xbean.RedPackRoleHisInfo> redpackrolehisinfolist; // 领取红包记录

	@Override
	public void _reset_unsafe_() {
		redpackid = "";
		modeltype = 0;
		valuekey = 0L;
		sendroleid = 0L;
		redpackdes = "";
		redpackstate = 0;
		redpackallnum = 0;
		redpackreceivednum = 0;
		redpackallmoney = 0;
		redpackreceiveallmoney = 0;
		sendtime = 0L;
		backflag = 0;
		redpackrolehisinfolist.clear();
	}

	RedPackInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		redpackid = "";
		redpackdes = "";
		redpackrolehisinfolist = new java.util.ArrayList<xbean.RedPackRoleHisInfo>();
	}

	public RedPackInfo() {
		this(0, null, null);
	}

	public RedPackInfo(RedPackInfo _o_) {
		this(_o_, null, null);
	}

	RedPackInfo(xbean.RedPackInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RedPackInfo) assign((RedPackInfo)_o1_);
		else if (_o1_ instanceof RedPackInfo.Data) assign((RedPackInfo.Data)_o1_);
		else if (_o1_ instanceof RedPackInfo.Const) assign(((RedPackInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RedPackInfo _o_) {
		_o_._xdb_verify_unsafe_();
		redpackid = _o_.redpackid;
		modeltype = _o_.modeltype;
		valuekey = _o_.valuekey;
		sendroleid = _o_.sendroleid;
		redpackdes = _o_.redpackdes;
		redpackstate = _o_.redpackstate;
		redpackallnum = _o_.redpackallnum;
		redpackreceivednum = _o_.redpackreceivednum;
		redpackallmoney = _o_.redpackallmoney;
		redpackreceiveallmoney = _o_.redpackreceiveallmoney;
		sendtime = _o_.sendtime;
		backflag = _o_.backflag;
		redpackrolehisinfolist = new java.util.ArrayList<xbean.RedPackRoleHisInfo>();
		for (xbean.RedPackRoleHisInfo _v_ : _o_.redpackrolehisinfolist)
			redpackrolehisinfolist.add(new RedPackRoleHisInfo(_v_, this, "redpackrolehisinfolist"));
	}

	private void assign(RedPackInfo.Data _o_) {
		redpackid = _o_.redpackid;
		modeltype = _o_.modeltype;
		valuekey = _o_.valuekey;
		sendroleid = _o_.sendroleid;
		redpackdes = _o_.redpackdes;
		redpackstate = _o_.redpackstate;
		redpackallnum = _o_.redpackallnum;
		redpackreceivednum = _o_.redpackreceivednum;
		redpackallmoney = _o_.redpackallmoney;
		redpackreceiveallmoney = _o_.redpackreceiveallmoney;
		sendtime = _o_.sendtime;
		backflag = _o_.backflag;
		redpackrolehisinfolist = new java.util.ArrayList<xbean.RedPackRoleHisInfo>();
		for (xbean.RedPackRoleHisInfo _v_ : _o_.redpackrolehisinfolist)
			redpackrolehisinfolist.add(new RedPackRoleHisInfo(_v_, this, "redpackrolehisinfolist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(redpackid, mkdb.Const.IO_CHARSET);
		_os_.marshal(modeltype);
		_os_.marshal(valuekey);
		_os_.marshal(sendroleid);
		_os_.marshal(redpackdes, mkdb.Const.IO_CHARSET);
		_os_.marshal(redpackstate);
		_os_.marshal(redpackallnum);
		_os_.marshal(redpackreceivednum);
		_os_.marshal(redpackallmoney);
		_os_.marshal(redpackreceiveallmoney);
		_os_.marshal(sendtime);
		_os_.marshal(backflag);
		_os_.compact_uint32(redpackrolehisinfolist.size());
		for (xbean.RedPackRoleHisInfo _v_ : redpackrolehisinfolist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		redpackid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		modeltype = _os_.unmarshal_int();
		valuekey = _os_.unmarshal_long();
		sendroleid = _os_.unmarshal_long();
		redpackdes = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		redpackstate = _os_.unmarshal_int();
		redpackallnum = _os_.unmarshal_int();
		redpackreceivednum = _os_.unmarshal_int();
		redpackallmoney = _os_.unmarshal_int();
		redpackreceiveallmoney = _os_.unmarshal_int();
		sendtime = _os_.unmarshal_long();
		backflag = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RedPackRoleHisInfo _v_ = new RedPackRoleHisInfo(0, this, "redpackrolehisinfolist");
			_v_.unmarshal(_os_);
			redpackrolehisinfolist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RedPackInfo copy() {
		_xdb_verify_unsafe_();
		return new RedPackInfo(this);
	}

	@Override
	public xbean.RedPackInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackInfo toBean() {
		_xdb_verify_unsafe_();
		return new RedPackInfo(this); // same as copy()
	}

	@Override
	public xbean.RedPackInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getRedpackid() { // 红包Id
		_xdb_verify_unsafe_();
		return redpackid;
	}

	@Override
	public com.locojoy.base.Octets getRedpackidOctets() { // 红包Id
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRedpackid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getModeltype() { // 红包类型
		_xdb_verify_unsafe_();
		return modeltype;
	}

	@Override
	public long getValuekey() { // 工会或者队伍id
		_xdb_verify_unsafe_();
		return valuekey;
	}

	@Override
	public long getSendroleid() { // 角色Id
		_xdb_verify_unsafe_();
		return sendroleid;
	}

	@Override
	public String getRedpackdes() { // 红包寄语
		_xdb_verify_unsafe_();
		return redpackdes;
	}

	@Override
	public com.locojoy.base.Octets getRedpackdesOctets() { // 红包寄语
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRedpackdes(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getRedpackstate() { // 红包状态
		_xdb_verify_unsafe_();
		return redpackstate;
	}

	@Override
	public int getRedpackallnum() { // 红包总个数
		_xdb_verify_unsafe_();
		return redpackallnum;
	}

	@Override
	public int getRedpackreceivednum() { // 红包已经领取个数
		_xdb_verify_unsafe_();
		return redpackreceivednum;
	}

	@Override
	public int getRedpackallmoney() { // 红包总金额
		_xdb_verify_unsafe_();
		return redpackallmoney;
	}

	@Override
	public int getRedpackreceiveallmoney() { // 已经领取红包总金额  便于退回红包
		_xdb_verify_unsafe_();
		return redpackreceiveallmoney;
	}

	@Override
	public long getSendtime() { // 发送时间
		_xdb_verify_unsafe_();
		return sendtime;
	}

	@Override
	public int getBackflag() { // 0没退   1已经退钱了
		_xdb_verify_unsafe_();
		return backflag;
	}

	@Override
	public java.util.List<xbean.RedPackRoleHisInfo> getRedpackrolehisinfolist() { // 领取红包记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "redpackrolehisinfolist"), redpackrolehisinfolist);
	}

	public java.util.List<xbean.RedPackRoleHisInfo> getRedpackrolehisinfolistAsData() { // 领取红包记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.RedPackRoleHisInfo> redpackrolehisinfolist;
		RedPackInfo _o_ = this;
		redpackrolehisinfolist = new java.util.ArrayList<xbean.RedPackRoleHisInfo>();
		for (xbean.RedPackRoleHisInfo _v_ : _o_.redpackrolehisinfolist)
			redpackrolehisinfolist.add(new RedPackRoleHisInfo.Data(_v_));
		return redpackrolehisinfolist;
	}

	@Override
	public void setRedpackid(String _v_) { // 红包Id
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, redpackid) {
					public void rollback() { redpackid = _xdb_saved; }
				};}});
		redpackid = _v_;
	}

	@Override
	public void setRedpackidOctets(com.locojoy.base.Octets _v_) { // 红包Id
		_xdb_verify_unsafe_();
		this.setRedpackid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setModeltype(int _v_) { // 红包类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "modeltype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, modeltype) {
					public void rollback() { modeltype = _xdb_saved; }
				};}});
		modeltype = _v_;
	}

	@Override
	public void setValuekey(long _v_) { // 工会或者队伍id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "valuekey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, valuekey) {
					public void rollback() { valuekey = _xdb_saved; }
				};}});
		valuekey = _v_;
	}

	@Override
	public void setSendroleid(long _v_) { // 角色Id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendroleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sendroleid) {
					public void rollback() { sendroleid = _xdb_saved; }
				};}});
		sendroleid = _v_;
	}

	@Override
	public void setRedpackdes(String _v_) { // 红包寄语
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackdes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, redpackdes) {
					public void rollback() { redpackdes = _xdb_saved; }
				};}});
		redpackdes = _v_;
	}

	@Override
	public void setRedpackdesOctets(com.locojoy.base.Octets _v_) { // 红包寄语
		_xdb_verify_unsafe_();
		this.setRedpackdes(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setRedpackstate(int _v_) { // 红包状态
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackstate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpackstate) {
					public void rollback() { redpackstate = _xdb_saved; }
				};}});
		redpackstate = _v_;
	}

	@Override
	public void setRedpackallnum(int _v_) { // 红包总个数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackallnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpackallnum) {
					public void rollback() { redpackallnum = _xdb_saved; }
				};}});
		redpackallnum = _v_;
	}

	@Override
	public void setRedpackreceivednum(int _v_) { // 红包已经领取个数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackreceivednum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpackreceivednum) {
					public void rollback() { redpackreceivednum = _xdb_saved; }
				};}});
		redpackreceivednum = _v_;
	}

	@Override
	public void setRedpackallmoney(int _v_) { // 红包总金额
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackallmoney") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpackallmoney) {
					public void rollback() { redpackallmoney = _xdb_saved; }
				};}});
		redpackallmoney = _v_;
	}

	@Override
	public void setRedpackreceiveallmoney(int _v_) { // 已经领取红包总金额  便于退回红包
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "redpackreceiveallmoney") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, redpackreceiveallmoney) {
					public void rollback() { redpackreceiveallmoney = _xdb_saved; }
				};}});
		redpackreceiveallmoney = _v_;
	}

	@Override
	public void setSendtime(long _v_) { // 发送时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sendtime) {
					public void rollback() { sendtime = _xdb_saved; }
				};}});
		sendtime = _v_;
	}

	@Override
	public void setBackflag(int _v_) { // 0没退   1已经退钱了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "backflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, backflag) {
					public void rollback() { backflag = _xdb_saved; }
				};}});
		backflag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RedPackInfo _o_ = null;
		if ( _o1_ instanceof RedPackInfo ) _o_ = (RedPackInfo)_o1_;
		else if ( _o1_ instanceof RedPackInfo.Const ) _o_ = ((RedPackInfo.Const)_o1_).nThis();
		else return false;
		if (!redpackid.equals(_o_.redpackid)) return false;
		if (modeltype != _o_.modeltype) return false;
		if (valuekey != _o_.valuekey) return false;
		if (sendroleid != _o_.sendroleid) return false;
		if (!redpackdes.equals(_o_.redpackdes)) return false;
		if (redpackstate != _o_.redpackstate) return false;
		if (redpackallnum != _o_.redpackallnum) return false;
		if (redpackreceivednum != _o_.redpackreceivednum) return false;
		if (redpackallmoney != _o_.redpackallmoney) return false;
		if (redpackreceiveallmoney != _o_.redpackreceiveallmoney) return false;
		if (sendtime != _o_.sendtime) return false;
		if (backflag != _o_.backflag) return false;
		if (!redpackrolehisinfolist.equals(_o_.redpackrolehisinfolist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += redpackid.hashCode();
		_h_ += modeltype;
		_h_ += valuekey;
		_h_ += sendroleid;
		_h_ += redpackdes.hashCode();
		_h_ += redpackstate;
		_h_ += redpackallnum;
		_h_ += redpackreceivednum;
		_h_ += redpackallmoney;
		_h_ += redpackreceiveallmoney;
		_h_ += sendtime;
		_h_ += backflag;
		_h_ += redpackrolehisinfolist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(redpackid).append("'");
		_sb_.append(",");
		_sb_.append(modeltype);
		_sb_.append(",");
		_sb_.append(valuekey);
		_sb_.append(",");
		_sb_.append(sendroleid);
		_sb_.append(",");
		_sb_.append("'").append(redpackdes).append("'");
		_sb_.append(",");
		_sb_.append(redpackstate);
		_sb_.append(",");
		_sb_.append(redpackallnum);
		_sb_.append(",");
		_sb_.append(redpackreceivednum);
		_sb_.append(",");
		_sb_.append(redpackallmoney);
		_sb_.append(",");
		_sb_.append(redpackreceiveallmoney);
		_sb_.append(",");
		_sb_.append(sendtime);
		_sb_.append(",");
		_sb_.append(backflag);
		_sb_.append(",");
		_sb_.append(redpackrolehisinfolist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("modeltype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("valuekey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendroleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackdes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackstate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackallnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackreceivednum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackallmoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackreceiveallmoney"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("backflag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("redpackrolehisinfolist"));
		return lb;
	}

	private class Const implements xbean.RedPackInfo {
		RedPackInfo nThis() {
			return RedPackInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RedPackInfo copy() {
			return RedPackInfo.this.copy();
		}

		@Override
		public xbean.RedPackInfo toData() {
			return RedPackInfo.this.toData();
		}

		public xbean.RedPackInfo toBean() {
			return RedPackInfo.this.toBean();
		}

		@Override
		public xbean.RedPackInfo toDataIf() {
			return RedPackInfo.this.toDataIf();
		}

		public xbean.RedPackInfo toBeanIf() {
			return RedPackInfo.this.toBeanIf();
		}

		@Override
		public String getRedpackid() { // 红包Id
			_xdb_verify_unsafe_();
			return redpackid;
		}

		@Override
		public com.locojoy.base.Octets getRedpackidOctets() { // 红包Id
			_xdb_verify_unsafe_();
			return RedPackInfo.this.getRedpackidOctets();
		}

		@Override
		public int getModeltype() { // 红包类型
			_xdb_verify_unsafe_();
			return modeltype;
		}

		@Override
		public long getValuekey() { // 工会或者队伍id
			_xdb_verify_unsafe_();
			return valuekey;
		}

		@Override
		public long getSendroleid() { // 角色Id
			_xdb_verify_unsafe_();
			return sendroleid;
		}

		@Override
		public String getRedpackdes() { // 红包寄语
			_xdb_verify_unsafe_();
			return redpackdes;
		}

		@Override
		public com.locojoy.base.Octets getRedpackdesOctets() { // 红包寄语
			_xdb_verify_unsafe_();
			return RedPackInfo.this.getRedpackdesOctets();
		}

		@Override
		public int getRedpackstate() { // 红包状态
			_xdb_verify_unsafe_();
			return redpackstate;
		}

		@Override
		public int getRedpackallnum() { // 红包总个数
			_xdb_verify_unsafe_();
			return redpackallnum;
		}

		@Override
		public int getRedpackreceivednum() { // 红包已经领取个数
			_xdb_verify_unsafe_();
			return redpackreceivednum;
		}

		@Override
		public int getRedpackallmoney() { // 红包总金额
			_xdb_verify_unsafe_();
			return redpackallmoney;
		}

		@Override
		public int getRedpackreceiveallmoney() { // 已经领取红包总金额  便于退回红包
			_xdb_verify_unsafe_();
			return redpackreceiveallmoney;
		}

		@Override
		public long getSendtime() { // 发送时间
			_xdb_verify_unsafe_();
			return sendtime;
		}

		@Override
		public int getBackflag() { // 0没退   1已经退钱了
			_xdb_verify_unsafe_();
			return backflag;
		}

		@Override
		public java.util.List<xbean.RedPackRoleHisInfo> getRedpackrolehisinfolist() { // 领取红包记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(redpackrolehisinfolist);
		}

		public java.util.List<xbean.RedPackRoleHisInfo> getRedpackrolehisinfolistAsData() { // 领取红包记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.RedPackRoleHisInfo> redpackrolehisinfolist;
			RedPackInfo _o_ = RedPackInfo.this;
		redpackrolehisinfolist = new java.util.ArrayList<xbean.RedPackRoleHisInfo>();
		for (xbean.RedPackRoleHisInfo _v_ : _o_.redpackrolehisinfolist)
			redpackrolehisinfolist.add(new RedPackRoleHisInfo.Data(_v_));
			return redpackrolehisinfolist;
		}

		@Override
		public void setRedpackid(String _v_) { // 红包Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackidOctets(com.locojoy.base.Octets _v_) { // 红包Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setModeltype(int _v_) { // 红包类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValuekey(long _v_) { // 工会或者队伍id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSendroleid(long _v_) { // 角色Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackdes(String _v_) { // 红包寄语
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackdesOctets(com.locojoy.base.Octets _v_) { // 红包寄语
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackstate(int _v_) { // 红包状态
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackallnum(int _v_) { // 红包总个数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackreceivednum(int _v_) { // 红包已经领取个数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackallmoney(int _v_) { // 红包总金额
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRedpackreceiveallmoney(int _v_) { // 已经领取红包总金额  便于退回红包
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSendtime(long _v_) { // 发送时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBackflag(int _v_) { // 0没退   1已经退钱了
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
			return RedPackInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RedPackInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RedPackInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RedPackInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RedPackInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RedPackInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RedPackInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RedPackInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RedPackInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RedPackInfo {
		private String redpackid; // 红包Id
		private int modeltype; // 红包类型
		private long valuekey; // 工会或者队伍id
		private long sendroleid; // 角色Id
		private String redpackdes; // 红包寄语
		private int redpackstate; // 红包状态
		private int redpackallnum; // 红包总个数
		private int redpackreceivednum; // 红包已经领取个数
		private int redpackallmoney; // 红包总金额
		private int redpackreceiveallmoney; // 已经领取红包总金额  便于退回红包
		private long sendtime; // 发送时间
		private int backflag; // 0没退   1已经退钱了
		private java.util.ArrayList<xbean.RedPackRoleHisInfo> redpackrolehisinfolist; // 领取红包记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			redpackid = "";
			redpackdes = "";
			redpackrolehisinfolist = new java.util.ArrayList<xbean.RedPackRoleHisInfo>();
		}

		Data(xbean.RedPackInfo _o1_) {
			if (_o1_ instanceof RedPackInfo) assign((RedPackInfo)_o1_);
			else if (_o1_ instanceof RedPackInfo.Data) assign((RedPackInfo.Data)_o1_);
			else if (_o1_ instanceof RedPackInfo.Const) assign(((RedPackInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RedPackInfo _o_) {
			redpackid = _o_.redpackid;
			modeltype = _o_.modeltype;
			valuekey = _o_.valuekey;
			sendroleid = _o_.sendroleid;
			redpackdes = _o_.redpackdes;
			redpackstate = _o_.redpackstate;
			redpackallnum = _o_.redpackallnum;
			redpackreceivednum = _o_.redpackreceivednum;
			redpackallmoney = _o_.redpackallmoney;
			redpackreceiveallmoney = _o_.redpackreceiveallmoney;
			sendtime = _o_.sendtime;
			backflag = _o_.backflag;
			redpackrolehisinfolist = new java.util.ArrayList<xbean.RedPackRoleHisInfo>();
			for (xbean.RedPackRoleHisInfo _v_ : _o_.redpackrolehisinfolist)
				redpackrolehisinfolist.add(new RedPackRoleHisInfo.Data(_v_));
		}

		private void assign(RedPackInfo.Data _o_) {
			redpackid = _o_.redpackid;
			modeltype = _o_.modeltype;
			valuekey = _o_.valuekey;
			sendroleid = _o_.sendroleid;
			redpackdes = _o_.redpackdes;
			redpackstate = _o_.redpackstate;
			redpackallnum = _o_.redpackallnum;
			redpackreceivednum = _o_.redpackreceivednum;
			redpackallmoney = _o_.redpackallmoney;
			redpackreceiveallmoney = _o_.redpackreceiveallmoney;
			sendtime = _o_.sendtime;
			backflag = _o_.backflag;
			redpackrolehisinfolist = new java.util.ArrayList<xbean.RedPackRoleHisInfo>();
			for (xbean.RedPackRoleHisInfo _v_ : _o_.redpackrolehisinfolist)
				redpackrolehisinfolist.add(new RedPackRoleHisInfo.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(redpackid, mkdb.Const.IO_CHARSET);
			_os_.marshal(modeltype);
			_os_.marshal(valuekey);
			_os_.marshal(sendroleid);
			_os_.marshal(redpackdes, mkdb.Const.IO_CHARSET);
			_os_.marshal(redpackstate);
			_os_.marshal(redpackallnum);
			_os_.marshal(redpackreceivednum);
			_os_.marshal(redpackallmoney);
			_os_.marshal(redpackreceiveallmoney);
			_os_.marshal(sendtime);
			_os_.marshal(backflag);
			_os_.compact_uint32(redpackrolehisinfolist.size());
			for (xbean.RedPackRoleHisInfo _v_ : redpackrolehisinfolist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			redpackid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			modeltype = _os_.unmarshal_int();
			valuekey = _os_.unmarshal_long();
			sendroleid = _os_.unmarshal_long();
			redpackdes = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			redpackstate = _os_.unmarshal_int();
			redpackallnum = _os_.unmarshal_int();
			redpackreceivednum = _os_.unmarshal_int();
			redpackallmoney = _os_.unmarshal_int();
			redpackreceiveallmoney = _os_.unmarshal_int();
			sendtime = _os_.unmarshal_long();
			backflag = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RedPackRoleHisInfo _v_ = xbean.Pod.newRedPackRoleHisInfoData();
				_v_.unmarshal(_os_);
				redpackrolehisinfolist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RedPackInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RedPackInfo toData() {
			return new Data(this);
		}

		public xbean.RedPackInfo toBean() {
			return new RedPackInfo(this, null, null);
		}

		@Override
		public xbean.RedPackInfo toDataIf() {
			return this;
		}

		public xbean.RedPackInfo toBeanIf() {
			return new RedPackInfo(this, null, null);
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
		public String getRedpackid() { // 红包Id
			return redpackid;
		}

		@Override
		public com.locojoy.base.Octets getRedpackidOctets() { // 红包Id
			return com.locojoy.base.Octets.wrap(getRedpackid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getModeltype() { // 红包类型
			return modeltype;
		}

		@Override
		public long getValuekey() { // 工会或者队伍id
			return valuekey;
		}

		@Override
		public long getSendroleid() { // 角色Id
			return sendroleid;
		}

		@Override
		public String getRedpackdes() { // 红包寄语
			return redpackdes;
		}

		@Override
		public com.locojoy.base.Octets getRedpackdesOctets() { // 红包寄语
			return com.locojoy.base.Octets.wrap(getRedpackdes(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getRedpackstate() { // 红包状态
			return redpackstate;
		}

		@Override
		public int getRedpackallnum() { // 红包总个数
			return redpackallnum;
		}

		@Override
		public int getRedpackreceivednum() { // 红包已经领取个数
			return redpackreceivednum;
		}

		@Override
		public int getRedpackallmoney() { // 红包总金额
			return redpackallmoney;
		}

		@Override
		public int getRedpackreceiveallmoney() { // 已经领取红包总金额  便于退回红包
			return redpackreceiveallmoney;
		}

		@Override
		public long getSendtime() { // 发送时间
			return sendtime;
		}

		@Override
		public int getBackflag() { // 0没退   1已经退钱了
			return backflag;
		}

		@Override
		public java.util.List<xbean.RedPackRoleHisInfo> getRedpackrolehisinfolist() { // 领取红包记录
			return redpackrolehisinfolist;
		}

		@Override
		public java.util.List<xbean.RedPackRoleHisInfo> getRedpackrolehisinfolistAsData() { // 领取红包记录
			return redpackrolehisinfolist;
		}

		@Override
		public void setRedpackid(String _v_) { // 红包Id
			if (null == _v_)
				throw new NullPointerException();
			redpackid = _v_;
		}

		@Override
		public void setRedpackidOctets(com.locojoy.base.Octets _v_) { // 红包Id
			this.setRedpackid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setModeltype(int _v_) { // 红包类型
			modeltype = _v_;
		}

		@Override
		public void setValuekey(long _v_) { // 工会或者队伍id
			valuekey = _v_;
		}

		@Override
		public void setSendroleid(long _v_) { // 角色Id
			sendroleid = _v_;
		}

		@Override
		public void setRedpackdes(String _v_) { // 红包寄语
			if (null == _v_)
				throw new NullPointerException();
			redpackdes = _v_;
		}

		@Override
		public void setRedpackdesOctets(com.locojoy.base.Octets _v_) { // 红包寄语
			this.setRedpackdes(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setRedpackstate(int _v_) { // 红包状态
			redpackstate = _v_;
		}

		@Override
		public void setRedpackallnum(int _v_) { // 红包总个数
			redpackallnum = _v_;
		}

		@Override
		public void setRedpackreceivednum(int _v_) { // 红包已经领取个数
			redpackreceivednum = _v_;
		}

		@Override
		public void setRedpackallmoney(int _v_) { // 红包总金额
			redpackallmoney = _v_;
		}

		@Override
		public void setRedpackreceiveallmoney(int _v_) { // 已经领取红包总金额  便于退回红包
			redpackreceiveallmoney = _v_;
		}

		@Override
		public void setSendtime(long _v_) { // 发送时间
			sendtime = _v_;
		}

		@Override
		public void setBackflag(int _v_) { // 0没退   1已经退钱了
			backflag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RedPackInfo.Data)) return false;
			RedPackInfo.Data _o_ = (RedPackInfo.Data) _o1_;
			if (!redpackid.equals(_o_.redpackid)) return false;
			if (modeltype != _o_.modeltype) return false;
			if (valuekey != _o_.valuekey) return false;
			if (sendroleid != _o_.sendroleid) return false;
			if (!redpackdes.equals(_o_.redpackdes)) return false;
			if (redpackstate != _o_.redpackstate) return false;
			if (redpackallnum != _o_.redpackallnum) return false;
			if (redpackreceivednum != _o_.redpackreceivednum) return false;
			if (redpackallmoney != _o_.redpackallmoney) return false;
			if (redpackreceiveallmoney != _o_.redpackreceiveallmoney) return false;
			if (sendtime != _o_.sendtime) return false;
			if (backflag != _o_.backflag) return false;
			if (!redpackrolehisinfolist.equals(_o_.redpackrolehisinfolist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += redpackid.hashCode();
			_h_ += modeltype;
			_h_ += valuekey;
			_h_ += sendroleid;
			_h_ += redpackdes.hashCode();
			_h_ += redpackstate;
			_h_ += redpackallnum;
			_h_ += redpackreceivednum;
			_h_ += redpackallmoney;
			_h_ += redpackreceiveallmoney;
			_h_ += sendtime;
			_h_ += backflag;
			_h_ += redpackrolehisinfolist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(redpackid).append("'");
			_sb_.append(",");
			_sb_.append(modeltype);
			_sb_.append(",");
			_sb_.append(valuekey);
			_sb_.append(",");
			_sb_.append(sendroleid);
			_sb_.append(",");
			_sb_.append("'").append(redpackdes).append("'");
			_sb_.append(",");
			_sb_.append(redpackstate);
			_sb_.append(",");
			_sb_.append(redpackallnum);
			_sb_.append(",");
			_sb_.append(redpackreceivednum);
			_sb_.append(",");
			_sb_.append(redpackallmoney);
			_sb_.append(",");
			_sb_.append(redpackreceiveallmoney);
			_sb_.append(",");
			_sb_.append(sendtime);
			_sb_.append(",");
			_sb_.append(backflag);
			_sb_.append(",");
			_sb_.append(redpackrolehisinfolist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

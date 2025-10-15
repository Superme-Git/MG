
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FriendGroups extends mkdb.XBean implements xbean.FriendGroups {
	private int refusestrangermsg; // 0=收听陌生人消息  1= 拒绝陌生人消息
	private java.util.HashMap<Long, xbean.FriendBean> friendmap; // 好友列表 key=roleid
	private xbean.FriendlyDegreesLimit friendlydegreeslimit; // 好友度每日限制
	private String sign; // 签名
	private java.util.ArrayList<xbean.OfflineMsg> offlinemsglist; // 角色收到的离线消息上线发送以后立刻清除

	@Override
	public void _reset_unsafe_() {
		refusestrangermsg = 0;
		friendmap.clear();
		friendlydegreeslimit._reset_unsafe_();
		sign = "";
		offlinemsglist.clear();
	}

	FriendGroups(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		friendmap = new java.util.HashMap<Long, xbean.FriendBean>();
		friendlydegreeslimit = new FriendlyDegreesLimit(0, this, "friendlydegreeslimit");
		sign = "";
		offlinemsglist = new java.util.ArrayList<xbean.OfflineMsg>();
	}

	public FriendGroups() {
		this(0, null, null);
	}

	public FriendGroups(FriendGroups _o_) {
		this(_o_, null, null);
	}

	FriendGroups(xbean.FriendGroups _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FriendGroups) assign((FriendGroups)_o1_);
		else if (_o1_ instanceof FriendGroups.Data) assign((FriendGroups.Data)_o1_);
		else if (_o1_ instanceof FriendGroups.Const) assign(((FriendGroups.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FriendGroups _o_) {
		_o_._xdb_verify_unsafe_();
		refusestrangermsg = _o_.refusestrangermsg;
		friendmap = new java.util.HashMap<Long, xbean.FriendBean>();
		for (java.util.Map.Entry<Long, xbean.FriendBean> _e_ : _o_.friendmap.entrySet())
			friendmap.put(_e_.getKey(), new FriendBean(_e_.getValue(), this, "friendmap"));
		friendlydegreeslimit = new FriendlyDegreesLimit(_o_.friendlydegreeslimit, this, "friendlydegreeslimit");
		sign = _o_.sign;
		offlinemsglist = new java.util.ArrayList<xbean.OfflineMsg>();
		for (xbean.OfflineMsg _v_ : _o_.offlinemsglist)
			offlinemsglist.add(new OfflineMsg(_v_, this, "offlinemsglist"));
	}

	private void assign(FriendGroups.Data _o_) {
		refusestrangermsg = _o_.refusestrangermsg;
		friendmap = new java.util.HashMap<Long, xbean.FriendBean>();
		for (java.util.Map.Entry<Long, xbean.FriendBean> _e_ : _o_.friendmap.entrySet())
			friendmap.put(_e_.getKey(), new FriendBean(_e_.getValue(), this, "friendmap"));
		friendlydegreeslimit = new FriendlyDegreesLimit(_o_.friendlydegreeslimit, this, "friendlydegreeslimit");
		sign = _o_.sign;
		offlinemsglist = new java.util.ArrayList<xbean.OfflineMsg>();
		for (xbean.OfflineMsg _v_ : _o_.offlinemsglist)
			offlinemsglist.add(new OfflineMsg(_v_, this, "offlinemsglist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(refusestrangermsg);
		_os_.compact_uint32(friendmap.size());
		for (java.util.Map.Entry<Long, xbean.FriendBean> _e_ : friendmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		friendlydegreeslimit.marshal(_os_);
		_os_.marshal(sign, mkdb.Const.IO_CHARSET);
		_os_.compact_uint32(offlinemsglist.size());
		for (xbean.OfflineMsg _v_ : offlinemsglist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		refusestrangermsg = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				friendmap = new java.util.HashMap<Long, xbean.FriendBean>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.FriendBean _v_ = new FriendBean(0, this, "friendmap");
				_v_.unmarshal(_os_);
				friendmap.put(_k_, _v_);
			}
		}
		friendlydegreeslimit.unmarshal(_os_);
		sign = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.OfflineMsg _v_ = new OfflineMsg(0, this, "offlinemsglist");
			_v_.unmarshal(_os_);
			offlinemsglist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.FriendGroups copy() {
		_xdb_verify_unsafe_();
		return new FriendGroups(this);
	}

	@Override
	public xbean.FriendGroups toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FriendGroups toBean() {
		_xdb_verify_unsafe_();
		return new FriendGroups(this); // same as copy()
	}

	@Override
	public xbean.FriendGroups toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FriendGroups toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getRefusestrangermsg() { // 0=收听陌生人消息  1= 拒绝陌生人消息
		_xdb_verify_unsafe_();
		return refusestrangermsg;
	}

	@Override
	public java.util.Map<Long, xbean.FriendBean> getFriendmap() { // 好友列表 key=roleid
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "friendmap"), friendmap);
	}

	@Override
	public java.util.Map<Long, xbean.FriendBean> getFriendmapAsData() { // 好友列表 key=roleid
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.FriendBean> friendmap;
		FriendGroups _o_ = this;
		friendmap = new java.util.HashMap<Long, xbean.FriendBean>();
		for (java.util.Map.Entry<Long, xbean.FriendBean> _e_ : _o_.friendmap.entrySet())
			friendmap.put(_e_.getKey(), new FriendBean.Data(_e_.getValue()));
		return friendmap;
	}

	@Override
	public xbean.FriendlyDegreesLimit getFriendlydegreeslimit() { // 好友度每日限制
		_xdb_verify_unsafe_();
		return friendlydegreeslimit;
	}

	@Override
	public String getSign() { // 签名
		_xdb_verify_unsafe_();
		return sign;
	}

	@Override
	public com.locojoy.base.Octets getSignOctets() { // 签名
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getSign(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public java.util.List<xbean.OfflineMsg> getOfflinemsglist() { // 角色收到的离线消息上线发送以后立刻清除
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "offlinemsglist"), offlinemsglist);
	}

	public java.util.List<xbean.OfflineMsg> getOfflinemsglistAsData() { // 角色收到的离线消息上线发送以后立刻清除
		_xdb_verify_unsafe_();
		java.util.List<xbean.OfflineMsg> offlinemsglist;
		FriendGroups _o_ = this;
		offlinemsglist = new java.util.ArrayList<xbean.OfflineMsg>();
		for (xbean.OfflineMsg _v_ : _o_.offlinemsglist)
			offlinemsglist.add(new OfflineMsg.Data(_v_));
		return offlinemsglist;
	}

	@Override
	public void setRefusestrangermsg(int _v_) { // 0=收听陌生人消息  1= 拒绝陌生人消息
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "refusestrangermsg") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, refusestrangermsg) {
					public void rollback() { refusestrangermsg = _xdb_saved; }
				};}});
		refusestrangermsg = _v_;
	}

	@Override
	public void setSign(String _v_) { // 签名
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sign") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, sign) {
					public void rollback() { sign = _xdb_saved; }
				};}});
		sign = _v_;
	}

	@Override
	public void setSignOctets(com.locojoy.base.Octets _v_) { // 签名
		_xdb_verify_unsafe_();
		this.setSign(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FriendGroups _o_ = null;
		if ( _o1_ instanceof FriendGroups ) _o_ = (FriendGroups)_o1_;
		else if ( _o1_ instanceof FriendGroups.Const ) _o_ = ((FriendGroups.Const)_o1_).nThis();
		else return false;
		if (refusestrangermsg != _o_.refusestrangermsg) return false;
		if (!friendmap.equals(_o_.friendmap)) return false;
		if (!friendlydegreeslimit.equals(_o_.friendlydegreeslimit)) return false;
		if (!sign.equals(_o_.sign)) return false;
		if (!offlinemsglist.equals(_o_.offlinemsglist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += refusestrangermsg;
		_h_ += friendmap.hashCode();
		_h_ += friendlydegreeslimit.hashCode();
		_h_ += sign.hashCode();
		_h_ += offlinemsglist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(refusestrangermsg);
		_sb_.append(",");
		_sb_.append(friendmap);
		_sb_.append(",");
		_sb_.append(friendlydegreeslimit);
		_sb_.append(",");
		_sb_.append("'").append(sign).append("'");
		_sb_.append(",");
		_sb_.append(offlinemsglist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("refusestrangermsg"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("friendmap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("friendlydegreeslimit"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sign"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("offlinemsglist"));
		return lb;
	}

	private class Const implements xbean.FriendGroups {
		FriendGroups nThis() {
			return FriendGroups.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FriendGroups copy() {
			return FriendGroups.this.copy();
		}

		@Override
		public xbean.FriendGroups toData() {
			return FriendGroups.this.toData();
		}

		public xbean.FriendGroups toBean() {
			return FriendGroups.this.toBean();
		}

		@Override
		public xbean.FriendGroups toDataIf() {
			return FriendGroups.this.toDataIf();
		}

		public xbean.FriendGroups toBeanIf() {
			return FriendGroups.this.toBeanIf();
		}

		@Override
		public int getRefusestrangermsg() { // 0=收听陌生人消息  1= 拒绝陌生人消息
			_xdb_verify_unsafe_();
			return refusestrangermsg;
		}

		@Override
		public java.util.Map<Long, xbean.FriendBean> getFriendmap() { // 好友列表 key=roleid
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(friendmap);
		}

		@Override
		public java.util.Map<Long, xbean.FriendBean> getFriendmapAsData() { // 好友列表 key=roleid
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.FriendBean> friendmap;
			FriendGroups _o_ = FriendGroups.this;
			friendmap = new java.util.HashMap<Long, xbean.FriendBean>();
			for (java.util.Map.Entry<Long, xbean.FriendBean> _e_ : _o_.friendmap.entrySet())
				friendmap.put(_e_.getKey(), new FriendBean.Data(_e_.getValue()));
			return friendmap;
		}

		@Override
		public xbean.FriendlyDegreesLimit getFriendlydegreeslimit() { // 好友度每日限制
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(friendlydegreeslimit);
		}

		@Override
		public String getSign() { // 签名
			_xdb_verify_unsafe_();
			return sign;
		}

		@Override
		public com.locojoy.base.Octets getSignOctets() { // 签名
			_xdb_verify_unsafe_();
			return FriendGroups.this.getSignOctets();
		}

		@Override
		public java.util.List<xbean.OfflineMsg> getOfflinemsglist() { // 角色收到的离线消息上线发送以后立刻清除
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(offlinemsglist);
		}

		public java.util.List<xbean.OfflineMsg> getOfflinemsglistAsData() { // 角色收到的离线消息上线发送以后立刻清除
			_xdb_verify_unsafe_();
			java.util.List<xbean.OfflineMsg> offlinemsglist;
			FriendGroups _o_ = FriendGroups.this;
		offlinemsglist = new java.util.ArrayList<xbean.OfflineMsg>();
		for (xbean.OfflineMsg _v_ : _o_.offlinemsglist)
			offlinemsglist.add(new OfflineMsg.Data(_v_));
			return offlinemsglist;
		}

		@Override
		public void setRefusestrangermsg(int _v_) { // 0=收听陌生人消息  1= 拒绝陌生人消息
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSign(String _v_) { // 签名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSignOctets(com.locojoy.base.Octets _v_) { // 签名
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
			return FriendGroups.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FriendGroups.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FriendGroups.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FriendGroups.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FriendGroups.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FriendGroups.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FriendGroups.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FriendGroups.this.hashCode();
		}

		@Override
		public String toString() {
			return FriendGroups.this.toString();
		}

	}

	public static final class Data implements xbean.FriendGroups {
		private int refusestrangermsg; // 0=收听陌生人消息  1= 拒绝陌生人消息
		private java.util.HashMap<Long, xbean.FriendBean> friendmap; // 好友列表 key=roleid
		private xbean.FriendlyDegreesLimit friendlydegreeslimit; // 好友度每日限制
		private String sign; // 签名
		private java.util.ArrayList<xbean.OfflineMsg> offlinemsglist; // 角色收到的离线消息上线发送以后立刻清除

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			friendmap = new java.util.HashMap<Long, xbean.FriendBean>();
			friendlydegreeslimit = new FriendlyDegreesLimit.Data();
			sign = "";
			offlinemsglist = new java.util.ArrayList<xbean.OfflineMsg>();
		}

		Data(xbean.FriendGroups _o1_) {
			if (_o1_ instanceof FriendGroups) assign((FriendGroups)_o1_);
			else if (_o1_ instanceof FriendGroups.Data) assign((FriendGroups.Data)_o1_);
			else if (_o1_ instanceof FriendGroups.Const) assign(((FriendGroups.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FriendGroups _o_) {
			refusestrangermsg = _o_.refusestrangermsg;
			friendmap = new java.util.HashMap<Long, xbean.FriendBean>();
			for (java.util.Map.Entry<Long, xbean.FriendBean> _e_ : _o_.friendmap.entrySet())
				friendmap.put(_e_.getKey(), new FriendBean.Data(_e_.getValue()));
			friendlydegreeslimit = new FriendlyDegreesLimit.Data(_o_.friendlydegreeslimit);
			sign = _o_.sign;
			offlinemsglist = new java.util.ArrayList<xbean.OfflineMsg>();
			for (xbean.OfflineMsg _v_ : _o_.offlinemsglist)
				offlinemsglist.add(new OfflineMsg.Data(_v_));
		}

		private void assign(FriendGroups.Data _o_) {
			refusestrangermsg = _o_.refusestrangermsg;
			friendmap = new java.util.HashMap<Long, xbean.FriendBean>();
			for (java.util.Map.Entry<Long, xbean.FriendBean> _e_ : _o_.friendmap.entrySet())
				friendmap.put(_e_.getKey(), new FriendBean.Data(_e_.getValue()));
			friendlydegreeslimit = new FriendlyDegreesLimit.Data(_o_.friendlydegreeslimit);
			sign = _o_.sign;
			offlinemsglist = new java.util.ArrayList<xbean.OfflineMsg>();
			for (xbean.OfflineMsg _v_ : _o_.offlinemsglist)
				offlinemsglist.add(new OfflineMsg.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(refusestrangermsg);
			_os_.compact_uint32(friendmap.size());
			for (java.util.Map.Entry<Long, xbean.FriendBean> _e_ : friendmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			friendlydegreeslimit.marshal(_os_);
			_os_.marshal(sign, mkdb.Const.IO_CHARSET);
			_os_.compact_uint32(offlinemsglist.size());
			for (xbean.OfflineMsg _v_ : offlinemsglist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			refusestrangermsg = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					friendmap = new java.util.HashMap<Long, xbean.FriendBean>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.FriendBean _v_ = xbean.Pod.newFriendBeanData();
					_v_.unmarshal(_os_);
					friendmap.put(_k_, _v_);
				}
			}
			friendlydegreeslimit.unmarshal(_os_);
			sign = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.OfflineMsg _v_ = xbean.Pod.newOfflineMsgData();
				_v_.unmarshal(_os_);
				offlinemsglist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.FriendGroups copy() {
			return new Data(this);
		}

		@Override
		public xbean.FriendGroups toData() {
			return new Data(this);
		}

		public xbean.FriendGroups toBean() {
			return new FriendGroups(this, null, null);
		}

		@Override
		public xbean.FriendGroups toDataIf() {
			return this;
		}

		public xbean.FriendGroups toBeanIf() {
			return new FriendGroups(this, null, null);
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
		public int getRefusestrangermsg() { // 0=收听陌生人消息  1= 拒绝陌生人消息
			return refusestrangermsg;
		}

		@Override
		public java.util.Map<Long, xbean.FriendBean> getFriendmap() { // 好友列表 key=roleid
			return friendmap;
		}

		@Override
		public java.util.Map<Long, xbean.FriendBean> getFriendmapAsData() { // 好友列表 key=roleid
			return friendmap;
		}

		@Override
		public xbean.FriendlyDegreesLimit getFriendlydegreeslimit() { // 好友度每日限制
			return friendlydegreeslimit;
		}

		@Override
		public String getSign() { // 签名
			return sign;
		}

		@Override
		public com.locojoy.base.Octets getSignOctets() { // 签名
			return com.locojoy.base.Octets.wrap(getSign(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public java.util.List<xbean.OfflineMsg> getOfflinemsglist() { // 角色收到的离线消息上线发送以后立刻清除
			return offlinemsglist;
		}

		@Override
		public java.util.List<xbean.OfflineMsg> getOfflinemsglistAsData() { // 角色收到的离线消息上线发送以后立刻清除
			return offlinemsglist;
		}

		@Override
		public void setRefusestrangermsg(int _v_) { // 0=收听陌生人消息  1= 拒绝陌生人消息
			refusestrangermsg = _v_;
		}

		@Override
		public void setSign(String _v_) { // 签名
			if (null == _v_)
				throw new NullPointerException();
			sign = _v_;
		}

		@Override
		public void setSignOctets(com.locojoy.base.Octets _v_) { // 签名
			this.setSign(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FriendGroups.Data)) return false;
			FriendGroups.Data _o_ = (FriendGroups.Data) _o1_;
			if (refusestrangermsg != _o_.refusestrangermsg) return false;
			if (!friendmap.equals(_o_.friendmap)) return false;
			if (!friendlydegreeslimit.equals(_o_.friendlydegreeslimit)) return false;
			if (!sign.equals(_o_.sign)) return false;
			if (!offlinemsglist.equals(_o_.offlinemsglist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += refusestrangermsg;
			_h_ += friendmap.hashCode();
			_h_ += friendlydegreeslimit.hashCode();
			_h_ += sign.hashCode();
			_h_ += offlinemsglist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(refusestrangermsg);
			_sb_.append(",");
			_sb_.append(friendmap);
			_sb_.append(",");
			_sb_.append(friendlydegreeslimit);
			_sb_.append(",");
			_sb_.append("'").append(sign).append("'");
			_sb_.append(",");
			_sb_.append(offlinemsglist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

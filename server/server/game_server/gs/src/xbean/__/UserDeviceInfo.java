
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class UserDeviceInfo extends mkdb.XBean implements xbean.UserDeviceInfo {
	private String telcooper; // 用户网络运营商
	private String chid; // 渠道ID
	private String plattype; // 平台ID(IOS,android,wp)
	private String deviid; // 玩家设备(mac/idfv)设备唯一标识
	private String imei; // 入网标识符
	private String ip; // 玩家设备IP
	private String netenvir; // 网络环境(如：3G,4G,wifi)
	private String osver; // 玩家系统版本(具体版本如ios9,android4.1)
	private String brtype; // 玩家手机类型(如联想，酷派)
	private String firstdeviid; // 玩家设备(mac/idfv) 注册设备唯一标识
	private String username; // 账号名称

	@Override
	public void _reset_unsafe_() {
		telcooper = "";
		chid = "";
		plattype = "";
		deviid = "";
		imei = "";
		ip = "";
		netenvir = "";
		osver = "";
		brtype = "";
		firstdeviid = "";
		username = "";
	}

	UserDeviceInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		telcooper = "";
		chid = "";
		plattype = "";
		deviid = "";
		imei = "";
		ip = "";
		netenvir = "";
		osver = "";
		brtype = "";
		firstdeviid = "";
		username = "";
	}

	public UserDeviceInfo() {
		this(0, null, null);
	}

	public UserDeviceInfo(UserDeviceInfo _o_) {
		this(_o_, null, null);
	}

	UserDeviceInfo(xbean.UserDeviceInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof UserDeviceInfo) assign((UserDeviceInfo)_o1_);
		else if (_o1_ instanceof UserDeviceInfo.Data) assign((UserDeviceInfo.Data)_o1_);
		else if (_o1_ instanceof UserDeviceInfo.Const) assign(((UserDeviceInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(UserDeviceInfo _o_) {
		_o_._xdb_verify_unsafe_();
		telcooper = _o_.telcooper;
		chid = _o_.chid;
		plattype = _o_.plattype;
		deviid = _o_.deviid;
		imei = _o_.imei;
		ip = _o_.ip;
		netenvir = _o_.netenvir;
		osver = _o_.osver;
		brtype = _o_.brtype;
		firstdeviid = _o_.firstdeviid;
		username = _o_.username;
	}

	private void assign(UserDeviceInfo.Data _o_) {
		telcooper = _o_.telcooper;
		chid = _o_.chid;
		plattype = _o_.plattype;
		deviid = _o_.deviid;
		imei = _o_.imei;
		ip = _o_.ip;
		netenvir = _o_.netenvir;
		osver = _o_.osver;
		brtype = _o_.brtype;
		firstdeviid = _o_.firstdeviid;
		username = _o_.username;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(telcooper, mkdb.Const.IO_CHARSET);
		_os_.marshal(chid, mkdb.Const.IO_CHARSET);
		_os_.marshal(plattype, mkdb.Const.IO_CHARSET);
		_os_.marshal(deviid, mkdb.Const.IO_CHARSET);
		_os_.marshal(imei, mkdb.Const.IO_CHARSET);
		_os_.marshal(ip, mkdb.Const.IO_CHARSET);
		_os_.marshal(netenvir, mkdb.Const.IO_CHARSET);
		_os_.marshal(osver, mkdb.Const.IO_CHARSET);
		_os_.marshal(brtype, mkdb.Const.IO_CHARSET);
		_os_.marshal(firstdeviid, mkdb.Const.IO_CHARSET);
		_os_.marshal(username, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		telcooper = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		chid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		plattype = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		deviid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		imei = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		ip = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		netenvir = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		osver = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		brtype = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		firstdeviid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		username = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.UserDeviceInfo copy() {
		_xdb_verify_unsafe_();
		return new UserDeviceInfo(this);
	}

	@Override
	public xbean.UserDeviceInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.UserDeviceInfo toBean() {
		_xdb_verify_unsafe_();
		return new UserDeviceInfo(this); // same as copy()
	}

	@Override
	public xbean.UserDeviceInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.UserDeviceInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getTelcooper() { // 用户网络运营商
		_xdb_verify_unsafe_();
		return telcooper;
	}

	@Override
	public com.locojoy.base.Octets getTelcooperOctets() { // 用户网络运营商
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getTelcooper(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getChid() { // 渠道ID
		_xdb_verify_unsafe_();
		return chid;
	}

	@Override
	public com.locojoy.base.Octets getChidOctets() { // 渠道ID
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getChid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPlattype() { // 平台ID(IOS,android,wp)
		_xdb_verify_unsafe_();
		return plattype;
	}

	@Override
	public com.locojoy.base.Octets getPlattypeOctets() { // 平台ID(IOS,android,wp)
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlattype(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getDeviid() { // 玩家设备(mac/idfv)设备唯一标识
		_xdb_verify_unsafe_();
		return deviid;
	}

	@Override
	public com.locojoy.base.Octets getDeviidOctets() { // 玩家设备(mac/idfv)设备唯一标识
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getDeviid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getImei() { // 入网标识符
		_xdb_verify_unsafe_();
		return imei;
	}

	@Override
	public com.locojoy.base.Octets getImeiOctets() { // 入网标识符
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getImei(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getIp() { // 玩家设备IP
		_xdb_verify_unsafe_();
		return ip;
	}

	@Override
	public com.locojoy.base.Octets getIpOctets() { // 玩家设备IP
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getIp(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getNetenvir() { // 网络环境(如：3G,4G,wifi)
		_xdb_verify_unsafe_();
		return netenvir;
	}

	@Override
	public com.locojoy.base.Octets getNetenvirOctets() { // 网络环境(如：3G,4G,wifi)
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getNetenvir(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getOsver() { // 玩家系统版本(具体版本如ios9,android4.1)
		_xdb_verify_unsafe_();
		return osver;
	}

	@Override
	public com.locojoy.base.Octets getOsverOctets() { // 玩家系统版本(具体版本如ios9,android4.1)
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getOsver(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getBrtype() { // 玩家手机类型(如联想，酷派)
		_xdb_verify_unsafe_();
		return brtype;
	}

	@Override
	public com.locojoy.base.Octets getBrtypeOctets() { // 玩家手机类型(如联想，酷派)
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getBrtype(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getFirstdeviid() { // 玩家设备(mac/idfv) 注册设备唯一标识
		_xdb_verify_unsafe_();
		return firstdeviid;
	}

	@Override
	public com.locojoy.base.Octets getFirstdeviidOctets() { // 玩家设备(mac/idfv) 注册设备唯一标识
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getFirstdeviid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getUsername() { // 账号名称
		_xdb_verify_unsafe_();
		return username;
	}

	@Override
	public com.locojoy.base.Octets getUsernameOctets() { // 账号名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getUsername(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setTelcooper(String _v_) { // 用户网络运营商
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "telcooper") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, telcooper) {
					public void rollback() { telcooper = _xdb_saved; }
				};}});
		telcooper = _v_;
	}

	@Override
	public void setTelcooperOctets(com.locojoy.base.Octets _v_) { // 用户网络运营商
		_xdb_verify_unsafe_();
		this.setTelcooper(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setChid(String _v_) { // 渠道ID
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, chid) {
					public void rollback() { chid = _xdb_saved; }
				};}});
		chid = _v_;
	}

	@Override
	public void setChidOctets(com.locojoy.base.Octets _v_) { // 渠道ID
		_xdb_verify_unsafe_();
		this.setChid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPlattype(String _v_) { // 平台ID(IOS,android,wp)
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
	public void setPlattypeOctets(com.locojoy.base.Octets _v_) { // 平台ID(IOS,android,wp)
		_xdb_verify_unsafe_();
		this.setPlattype(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setDeviid(String _v_) { // 玩家设备(mac/idfv)设备唯一标识
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "deviid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, deviid) {
					public void rollback() { deviid = _xdb_saved; }
				};}});
		deviid = _v_;
	}

	@Override
	public void setDeviidOctets(com.locojoy.base.Octets _v_) { // 玩家设备(mac/idfv)设备唯一标识
		_xdb_verify_unsafe_();
		this.setDeviid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setImei(String _v_) { // 入网标识符
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "imei") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, imei) {
					public void rollback() { imei = _xdb_saved; }
				};}});
		imei = _v_;
	}

	@Override
	public void setImeiOctets(com.locojoy.base.Octets _v_) { // 入网标识符
		_xdb_verify_unsafe_();
		this.setImei(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setIp(String _v_) { // 玩家设备IP
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ip") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, ip) {
					public void rollback() { ip = _xdb_saved; }
				};}});
		ip = _v_;
	}

	@Override
	public void setIpOctets(com.locojoy.base.Octets _v_) { // 玩家设备IP
		_xdb_verify_unsafe_();
		this.setIp(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setNetenvir(String _v_) { // 网络环境(如：3G,4G,wifi)
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "netenvir") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, netenvir) {
					public void rollback() { netenvir = _xdb_saved; }
				};}});
		netenvir = _v_;
	}

	@Override
	public void setNetenvirOctets(com.locojoy.base.Octets _v_) { // 网络环境(如：3G,4G,wifi)
		_xdb_verify_unsafe_();
		this.setNetenvir(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setOsver(String _v_) { // 玩家系统版本(具体版本如ios9,android4.1)
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "osver") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, osver) {
					public void rollback() { osver = _xdb_saved; }
				};}});
		osver = _v_;
	}

	@Override
	public void setOsverOctets(com.locojoy.base.Octets _v_) { // 玩家系统版本(具体版本如ios9,android4.1)
		_xdb_verify_unsafe_();
		this.setOsver(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setBrtype(String _v_) { // 玩家手机类型(如联想，酷派)
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "brtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, brtype) {
					public void rollback() { brtype = _xdb_saved; }
				};}});
		brtype = _v_;
	}

	@Override
	public void setBrtypeOctets(com.locojoy.base.Octets _v_) { // 玩家手机类型(如联想，酷派)
		_xdb_verify_unsafe_();
		this.setBrtype(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setFirstdeviid(String _v_) { // 玩家设备(mac/idfv) 注册设备唯一标识
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstdeviid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, firstdeviid) {
					public void rollback() { firstdeviid = _xdb_saved; }
				};}});
		firstdeviid = _v_;
	}

	@Override
	public void setFirstdeviidOctets(com.locojoy.base.Octets _v_) { // 玩家设备(mac/idfv) 注册设备唯一标识
		_xdb_verify_unsafe_();
		this.setFirstdeviid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setUsername(String _v_) { // 账号名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "username") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, username) {
					public void rollback() { username = _xdb_saved; }
				};}});
		username = _v_;
	}

	@Override
	public void setUsernameOctets(com.locojoy.base.Octets _v_) { // 账号名称
		_xdb_verify_unsafe_();
		this.setUsername(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		UserDeviceInfo _o_ = null;
		if ( _o1_ instanceof UserDeviceInfo ) _o_ = (UserDeviceInfo)_o1_;
		else if ( _o1_ instanceof UserDeviceInfo.Const ) _o_ = ((UserDeviceInfo.Const)_o1_).nThis();
		else return false;
		if (!telcooper.equals(_o_.telcooper)) return false;
		if (!chid.equals(_o_.chid)) return false;
		if (!plattype.equals(_o_.plattype)) return false;
		if (!deviid.equals(_o_.deviid)) return false;
		if (!imei.equals(_o_.imei)) return false;
		if (!ip.equals(_o_.ip)) return false;
		if (!netenvir.equals(_o_.netenvir)) return false;
		if (!osver.equals(_o_.osver)) return false;
		if (!brtype.equals(_o_.brtype)) return false;
		if (!firstdeviid.equals(_o_.firstdeviid)) return false;
		if (!username.equals(_o_.username)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += telcooper.hashCode();
		_h_ += chid.hashCode();
		_h_ += plattype.hashCode();
		_h_ += deviid.hashCode();
		_h_ += imei.hashCode();
		_h_ += ip.hashCode();
		_h_ += netenvir.hashCode();
		_h_ += osver.hashCode();
		_h_ += brtype.hashCode();
		_h_ += firstdeviid.hashCode();
		_h_ += username.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(telcooper).append("'");
		_sb_.append(",");
		_sb_.append("'").append(chid).append("'");
		_sb_.append(",");
		_sb_.append("'").append(plattype).append("'");
		_sb_.append(",");
		_sb_.append("'").append(deviid).append("'");
		_sb_.append(",");
		_sb_.append("'").append(imei).append("'");
		_sb_.append(",");
		_sb_.append("'").append(ip).append("'");
		_sb_.append(",");
		_sb_.append("'").append(netenvir).append("'");
		_sb_.append(",");
		_sb_.append("'").append(osver).append("'");
		_sb_.append(",");
		_sb_.append("'").append(brtype).append("'");
		_sb_.append(",");
		_sb_.append("'").append(firstdeviid).append("'");
		_sb_.append(",");
		_sb_.append("'").append(username).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("telcooper"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("plattype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("deviid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("imei"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ip"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("netenvir"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("osver"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("brtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstdeviid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("username"));
		return lb;
	}

	private class Const implements xbean.UserDeviceInfo {
		UserDeviceInfo nThis() {
			return UserDeviceInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.UserDeviceInfo copy() {
			return UserDeviceInfo.this.copy();
		}

		@Override
		public xbean.UserDeviceInfo toData() {
			return UserDeviceInfo.this.toData();
		}

		public xbean.UserDeviceInfo toBean() {
			return UserDeviceInfo.this.toBean();
		}

		@Override
		public xbean.UserDeviceInfo toDataIf() {
			return UserDeviceInfo.this.toDataIf();
		}

		public xbean.UserDeviceInfo toBeanIf() {
			return UserDeviceInfo.this.toBeanIf();
		}

		@Override
		public String getTelcooper() { // 用户网络运营商
			_xdb_verify_unsafe_();
			return telcooper;
		}

		@Override
		public com.locojoy.base.Octets getTelcooperOctets() { // 用户网络运营商
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getTelcooperOctets();
		}

		@Override
		public String getChid() { // 渠道ID
			_xdb_verify_unsafe_();
			return chid;
		}

		@Override
		public com.locojoy.base.Octets getChidOctets() { // 渠道ID
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getChidOctets();
		}

		@Override
		public String getPlattype() { // 平台ID(IOS,android,wp)
			_xdb_verify_unsafe_();
			return plattype;
		}

		@Override
		public com.locojoy.base.Octets getPlattypeOctets() { // 平台ID(IOS,android,wp)
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getPlattypeOctets();
		}

		@Override
		public String getDeviid() { // 玩家设备(mac/idfv)设备唯一标识
			_xdb_verify_unsafe_();
			return deviid;
		}

		@Override
		public com.locojoy.base.Octets getDeviidOctets() { // 玩家设备(mac/idfv)设备唯一标识
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getDeviidOctets();
		}

		@Override
		public String getImei() { // 入网标识符
			_xdb_verify_unsafe_();
			return imei;
		}

		@Override
		public com.locojoy.base.Octets getImeiOctets() { // 入网标识符
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getImeiOctets();
		}

		@Override
		public String getIp() { // 玩家设备IP
			_xdb_verify_unsafe_();
			return ip;
		}

		@Override
		public com.locojoy.base.Octets getIpOctets() { // 玩家设备IP
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getIpOctets();
		}

		@Override
		public String getNetenvir() { // 网络环境(如：3G,4G,wifi)
			_xdb_verify_unsafe_();
			return netenvir;
		}

		@Override
		public com.locojoy.base.Octets getNetenvirOctets() { // 网络环境(如：3G,4G,wifi)
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getNetenvirOctets();
		}

		@Override
		public String getOsver() { // 玩家系统版本(具体版本如ios9,android4.1)
			_xdb_verify_unsafe_();
			return osver;
		}

		@Override
		public com.locojoy.base.Octets getOsverOctets() { // 玩家系统版本(具体版本如ios9,android4.1)
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getOsverOctets();
		}

		@Override
		public String getBrtype() { // 玩家手机类型(如联想，酷派)
			_xdb_verify_unsafe_();
			return brtype;
		}

		@Override
		public com.locojoy.base.Octets getBrtypeOctets() { // 玩家手机类型(如联想，酷派)
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getBrtypeOctets();
		}

		@Override
		public String getFirstdeviid() { // 玩家设备(mac/idfv) 注册设备唯一标识
			_xdb_verify_unsafe_();
			return firstdeviid;
		}

		@Override
		public com.locojoy.base.Octets getFirstdeviidOctets() { // 玩家设备(mac/idfv) 注册设备唯一标识
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getFirstdeviidOctets();
		}

		@Override
		public String getUsername() { // 账号名称
			_xdb_verify_unsafe_();
			return username;
		}

		@Override
		public com.locojoy.base.Octets getUsernameOctets() { // 账号名称
			_xdb_verify_unsafe_();
			return UserDeviceInfo.this.getUsernameOctets();
		}

		@Override
		public void setTelcooper(String _v_) { // 用户网络运营商
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTelcooperOctets(com.locojoy.base.Octets _v_) { // 用户网络运营商
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChid(String _v_) { // 渠道ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChidOctets(com.locojoy.base.Octets _v_) { // 渠道ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlattype(String _v_) { // 平台ID(IOS,android,wp)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlattypeOctets(com.locojoy.base.Octets _v_) { // 平台ID(IOS,android,wp)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDeviid(String _v_) { // 玩家设备(mac/idfv)设备唯一标识
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDeviidOctets(com.locojoy.base.Octets _v_) { // 玩家设备(mac/idfv)设备唯一标识
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setImei(String _v_) { // 入网标识符
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setImeiOctets(com.locojoy.base.Octets _v_) { // 入网标识符
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIp(String _v_) { // 玩家设备IP
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIpOctets(com.locojoy.base.Octets _v_) { // 玩家设备IP
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNetenvir(String _v_) { // 网络环境(如：3G,4G,wifi)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNetenvirOctets(com.locojoy.base.Octets _v_) { // 网络环境(如：3G,4G,wifi)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOsver(String _v_) { // 玩家系统版本(具体版本如ios9,android4.1)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOsverOctets(com.locojoy.base.Octets _v_) { // 玩家系统版本(具体版本如ios9,android4.1)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBrtype(String _v_) { // 玩家手机类型(如联想，酷派)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBrtypeOctets(com.locojoy.base.Octets _v_) { // 玩家手机类型(如联想，酷派)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstdeviid(String _v_) { // 玩家设备(mac/idfv) 注册设备唯一标识
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstdeviidOctets(com.locojoy.base.Octets _v_) { // 玩家设备(mac/idfv) 注册设备唯一标识
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUsername(String _v_) { // 账号名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUsernameOctets(com.locojoy.base.Octets _v_) { // 账号名称
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
			return UserDeviceInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return UserDeviceInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return UserDeviceInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return UserDeviceInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return UserDeviceInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return UserDeviceInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return UserDeviceInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return UserDeviceInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return UserDeviceInfo.this.toString();
		}

	}

	public static final class Data implements xbean.UserDeviceInfo {
		private String telcooper; // 用户网络运营商
		private String chid; // 渠道ID
		private String plattype; // 平台ID(IOS,android,wp)
		private String deviid; // 玩家设备(mac/idfv)设备唯一标识
		private String imei; // 入网标识符
		private String ip; // 玩家设备IP
		private String netenvir; // 网络环境(如：3G,4G,wifi)
		private String osver; // 玩家系统版本(具体版本如ios9,android4.1)
		private String brtype; // 玩家手机类型(如联想，酷派)
		private String firstdeviid; // 玩家设备(mac/idfv) 注册设备唯一标识
		private String username; // 账号名称

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			telcooper = "";
			chid = "";
			plattype = "";
			deviid = "";
			imei = "";
			ip = "";
			netenvir = "";
			osver = "";
			brtype = "";
			firstdeviid = "";
			username = "";
		}

		Data(xbean.UserDeviceInfo _o1_) {
			if (_o1_ instanceof UserDeviceInfo) assign((UserDeviceInfo)_o1_);
			else if (_o1_ instanceof UserDeviceInfo.Data) assign((UserDeviceInfo.Data)_o1_);
			else if (_o1_ instanceof UserDeviceInfo.Const) assign(((UserDeviceInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(UserDeviceInfo _o_) {
			telcooper = _o_.telcooper;
			chid = _o_.chid;
			plattype = _o_.plattype;
			deviid = _o_.deviid;
			imei = _o_.imei;
			ip = _o_.ip;
			netenvir = _o_.netenvir;
			osver = _o_.osver;
			brtype = _o_.brtype;
			firstdeviid = _o_.firstdeviid;
			username = _o_.username;
		}

		private void assign(UserDeviceInfo.Data _o_) {
			telcooper = _o_.telcooper;
			chid = _o_.chid;
			plattype = _o_.plattype;
			deviid = _o_.deviid;
			imei = _o_.imei;
			ip = _o_.ip;
			netenvir = _o_.netenvir;
			osver = _o_.osver;
			brtype = _o_.brtype;
			firstdeviid = _o_.firstdeviid;
			username = _o_.username;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(telcooper, mkdb.Const.IO_CHARSET);
			_os_.marshal(chid, mkdb.Const.IO_CHARSET);
			_os_.marshal(plattype, mkdb.Const.IO_CHARSET);
			_os_.marshal(deviid, mkdb.Const.IO_CHARSET);
			_os_.marshal(imei, mkdb.Const.IO_CHARSET);
			_os_.marshal(ip, mkdb.Const.IO_CHARSET);
			_os_.marshal(netenvir, mkdb.Const.IO_CHARSET);
			_os_.marshal(osver, mkdb.Const.IO_CHARSET);
			_os_.marshal(brtype, mkdb.Const.IO_CHARSET);
			_os_.marshal(firstdeviid, mkdb.Const.IO_CHARSET);
			_os_.marshal(username, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			telcooper = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			chid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			plattype = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			deviid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			imei = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			ip = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			netenvir = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			osver = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			brtype = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			firstdeviid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			username = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.UserDeviceInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.UserDeviceInfo toData() {
			return new Data(this);
		}

		public xbean.UserDeviceInfo toBean() {
			return new UserDeviceInfo(this, null, null);
		}

		@Override
		public xbean.UserDeviceInfo toDataIf() {
			return this;
		}

		public xbean.UserDeviceInfo toBeanIf() {
			return new UserDeviceInfo(this, null, null);
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
		public String getTelcooper() { // 用户网络运营商
			return telcooper;
		}

		@Override
		public com.locojoy.base.Octets getTelcooperOctets() { // 用户网络运营商
			return com.locojoy.base.Octets.wrap(getTelcooper(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getChid() { // 渠道ID
			return chid;
		}

		@Override
		public com.locojoy.base.Octets getChidOctets() { // 渠道ID
			return com.locojoy.base.Octets.wrap(getChid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPlattype() { // 平台ID(IOS,android,wp)
			return plattype;
		}

		@Override
		public com.locojoy.base.Octets getPlattypeOctets() { // 平台ID(IOS,android,wp)
			return com.locojoy.base.Octets.wrap(getPlattype(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getDeviid() { // 玩家设备(mac/idfv)设备唯一标识
			return deviid;
		}

		@Override
		public com.locojoy.base.Octets getDeviidOctets() { // 玩家设备(mac/idfv)设备唯一标识
			return com.locojoy.base.Octets.wrap(getDeviid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getImei() { // 入网标识符
			return imei;
		}

		@Override
		public com.locojoy.base.Octets getImeiOctets() { // 入网标识符
			return com.locojoy.base.Octets.wrap(getImei(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getIp() { // 玩家设备IP
			return ip;
		}

		@Override
		public com.locojoy.base.Octets getIpOctets() { // 玩家设备IP
			return com.locojoy.base.Octets.wrap(getIp(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getNetenvir() { // 网络环境(如：3G,4G,wifi)
			return netenvir;
		}

		@Override
		public com.locojoy.base.Octets getNetenvirOctets() { // 网络环境(如：3G,4G,wifi)
			return com.locojoy.base.Octets.wrap(getNetenvir(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getOsver() { // 玩家系统版本(具体版本如ios9,android4.1)
			return osver;
		}

		@Override
		public com.locojoy.base.Octets getOsverOctets() { // 玩家系统版本(具体版本如ios9,android4.1)
			return com.locojoy.base.Octets.wrap(getOsver(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getBrtype() { // 玩家手机类型(如联想，酷派)
			return brtype;
		}

		@Override
		public com.locojoy.base.Octets getBrtypeOctets() { // 玩家手机类型(如联想，酷派)
			return com.locojoy.base.Octets.wrap(getBrtype(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getFirstdeviid() { // 玩家设备(mac/idfv) 注册设备唯一标识
			return firstdeviid;
		}

		@Override
		public com.locojoy.base.Octets getFirstdeviidOctets() { // 玩家设备(mac/idfv) 注册设备唯一标识
			return com.locojoy.base.Octets.wrap(getFirstdeviid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getUsername() { // 账号名称
			return username;
		}

		@Override
		public com.locojoy.base.Octets getUsernameOctets() { // 账号名称
			return com.locojoy.base.Octets.wrap(getUsername(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setTelcooper(String _v_) { // 用户网络运营商
			if (null == _v_)
				throw new NullPointerException();
			telcooper = _v_;
		}

		@Override
		public void setTelcooperOctets(com.locojoy.base.Octets _v_) { // 用户网络运营商
			this.setTelcooper(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setChid(String _v_) { // 渠道ID
			if (null == _v_)
				throw new NullPointerException();
			chid = _v_;
		}

		@Override
		public void setChidOctets(com.locojoy.base.Octets _v_) { // 渠道ID
			this.setChid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPlattype(String _v_) { // 平台ID(IOS,android,wp)
			if (null == _v_)
				throw new NullPointerException();
			plattype = _v_;
		}

		@Override
		public void setPlattypeOctets(com.locojoy.base.Octets _v_) { // 平台ID(IOS,android,wp)
			this.setPlattype(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setDeviid(String _v_) { // 玩家设备(mac/idfv)设备唯一标识
			if (null == _v_)
				throw new NullPointerException();
			deviid = _v_;
		}

		@Override
		public void setDeviidOctets(com.locojoy.base.Octets _v_) { // 玩家设备(mac/idfv)设备唯一标识
			this.setDeviid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setImei(String _v_) { // 入网标识符
			if (null == _v_)
				throw new NullPointerException();
			imei = _v_;
		}

		@Override
		public void setImeiOctets(com.locojoy.base.Octets _v_) { // 入网标识符
			this.setImei(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setIp(String _v_) { // 玩家设备IP
			if (null == _v_)
				throw new NullPointerException();
			ip = _v_;
		}

		@Override
		public void setIpOctets(com.locojoy.base.Octets _v_) { // 玩家设备IP
			this.setIp(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setNetenvir(String _v_) { // 网络环境(如：3G,4G,wifi)
			if (null == _v_)
				throw new NullPointerException();
			netenvir = _v_;
		}

		@Override
		public void setNetenvirOctets(com.locojoy.base.Octets _v_) { // 网络环境(如：3G,4G,wifi)
			this.setNetenvir(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setOsver(String _v_) { // 玩家系统版本(具体版本如ios9,android4.1)
			if (null == _v_)
				throw new NullPointerException();
			osver = _v_;
		}

		@Override
		public void setOsverOctets(com.locojoy.base.Octets _v_) { // 玩家系统版本(具体版本如ios9,android4.1)
			this.setOsver(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setBrtype(String _v_) { // 玩家手机类型(如联想，酷派)
			if (null == _v_)
				throw new NullPointerException();
			brtype = _v_;
		}

		@Override
		public void setBrtypeOctets(com.locojoy.base.Octets _v_) { // 玩家手机类型(如联想，酷派)
			this.setBrtype(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setFirstdeviid(String _v_) { // 玩家设备(mac/idfv) 注册设备唯一标识
			if (null == _v_)
				throw new NullPointerException();
			firstdeviid = _v_;
		}

		@Override
		public void setFirstdeviidOctets(com.locojoy.base.Octets _v_) { // 玩家设备(mac/idfv) 注册设备唯一标识
			this.setFirstdeviid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setUsername(String _v_) { // 账号名称
			if (null == _v_)
				throw new NullPointerException();
			username = _v_;
		}

		@Override
		public void setUsernameOctets(com.locojoy.base.Octets _v_) { // 账号名称
			this.setUsername(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof UserDeviceInfo.Data)) return false;
			UserDeviceInfo.Data _o_ = (UserDeviceInfo.Data) _o1_;
			if (!telcooper.equals(_o_.telcooper)) return false;
			if (!chid.equals(_o_.chid)) return false;
			if (!plattype.equals(_o_.plattype)) return false;
			if (!deviid.equals(_o_.deviid)) return false;
			if (!imei.equals(_o_.imei)) return false;
			if (!ip.equals(_o_.ip)) return false;
			if (!netenvir.equals(_o_.netenvir)) return false;
			if (!osver.equals(_o_.osver)) return false;
			if (!brtype.equals(_o_.brtype)) return false;
			if (!firstdeviid.equals(_o_.firstdeviid)) return false;
			if (!username.equals(_o_.username)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += telcooper.hashCode();
			_h_ += chid.hashCode();
			_h_ += plattype.hashCode();
			_h_ += deviid.hashCode();
			_h_ += imei.hashCode();
			_h_ += ip.hashCode();
			_h_ += netenvir.hashCode();
			_h_ += osver.hashCode();
			_h_ += brtype.hashCode();
			_h_ += firstdeviid.hashCode();
			_h_ += username.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(telcooper).append("'");
			_sb_.append(",");
			_sb_.append("'").append(chid).append("'");
			_sb_.append(",");
			_sb_.append("'").append(plattype).append("'");
			_sb_.append(",");
			_sb_.append("'").append(deviid).append("'");
			_sb_.append(",");
			_sb_.append("'").append(imei).append("'");
			_sb_.append(",");
			_sb_.append("'").append(ip).append("'");
			_sb_.append(",");
			_sb_.append("'").append(netenvir).append("'");
			_sb_.append(",");
			_sb_.append("'").append(osver).append("'");
			_sb_.append(",");
			_sb_.append("'").append(brtype).append("'");
			_sb_.append(",");
			_sb_.append("'").append(firstdeviid).append("'");
			_sb_.append(",");
			_sb_.append("'").append(username).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AUUserInfo extends mkdb.XBean implements xbean.AUUserInfo {
	private int retcode; // 
	private int func; // 
	private int funcparm; // 
	private int loginip; // 
	private int blisgm; // 
	private java.util.ArrayList<Integer> auth; // 
	private int algorithm; // 
	private String nickname; // 
	private String username; // 
	private String mac; // 
	private String os; // 
	private String platform; // 
	private String platname; // 平台名称 yingyongbao,360 等
	private String clientsource; // 
	private String idfa; // 

	@Override
	public void _reset_unsafe_() {
		retcode = 0;
		func = 0;
		funcparm = 0;
		loginip = 0;
		blisgm = 0;
		auth.clear();
		algorithm = 0;
		nickname = "";
		username = "";
		mac = "";
		os = "";
		platform = "";
		platname = "";
		clientsource = "";
		idfa = "";
	}

	AUUserInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		auth = new java.util.ArrayList<Integer>();
		nickname = "";
		username = "";
		mac = "";
		os = "";
		platform = "";
		platname = "";
		clientsource = "";
		idfa = "";
	}

	public AUUserInfo() {
		this(0, null, null);
	}

	public AUUserInfo(AUUserInfo _o_) {
		this(_o_, null, null);
	}

	AUUserInfo(xbean.AUUserInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AUUserInfo) assign((AUUserInfo)_o1_);
		else if (_o1_ instanceof AUUserInfo.Data) assign((AUUserInfo.Data)_o1_);
		else if (_o1_ instanceof AUUserInfo.Const) assign(((AUUserInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AUUserInfo _o_) {
		_o_._xdb_verify_unsafe_();
		retcode = _o_.retcode;
		func = _o_.func;
		funcparm = _o_.funcparm;
		loginip = _o_.loginip;
		blisgm = _o_.blisgm;
		auth = new java.util.ArrayList<Integer>();
		auth.addAll(_o_.auth);
		algorithm = _o_.algorithm;
		nickname = _o_.nickname;
		username = _o_.username;
		mac = _o_.mac;
		os = _o_.os;
		platform = _o_.platform;
		platname = _o_.platname;
		clientsource = _o_.clientsource;
		idfa = _o_.idfa;
	}

	private void assign(AUUserInfo.Data _o_) {
		retcode = _o_.retcode;
		func = _o_.func;
		funcparm = _o_.funcparm;
		loginip = _o_.loginip;
		blisgm = _o_.blisgm;
		auth = new java.util.ArrayList<Integer>();
		auth.addAll(_o_.auth);
		algorithm = _o_.algorithm;
		nickname = _o_.nickname;
		username = _o_.username;
		mac = _o_.mac;
		os = _o_.os;
		platform = _o_.platform;
		platname = _o_.platname;
		clientsource = _o_.clientsource;
		idfa = _o_.idfa;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(retcode);
		_os_.marshal(func);
		_os_.marshal(funcparm);
		_os_.marshal(loginip);
		_os_.marshal(blisgm);
		_os_.compact_uint32(auth.size());
		for (Integer _v_ : auth) {
			_os_.marshal(_v_);
		}
		_os_.marshal(algorithm);
		_os_.marshal(nickname, mkdb.Const.IO_CHARSET);
		_os_.marshal(username, mkdb.Const.IO_CHARSET);
		_os_.marshal(mac, mkdb.Const.IO_CHARSET);
		_os_.marshal(os, mkdb.Const.IO_CHARSET);
		_os_.marshal(platform, mkdb.Const.IO_CHARSET);
		_os_.marshal(platname, mkdb.Const.IO_CHARSET);
		_os_.marshal(clientsource, mkdb.Const.IO_CHARSET);
		_os_.marshal(idfa, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		retcode = _os_.unmarshal_int();
		func = _os_.unmarshal_int();
		funcparm = _os_.unmarshal_int();
		loginip = _os_.unmarshal_int();
		blisgm = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			auth.add(_v_);
		}
		algorithm = _os_.unmarshal_int();
		nickname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		username = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		mac = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		os = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		platform = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		platname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		clientsource = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		idfa = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.AUUserInfo copy() {
		_xdb_verify_unsafe_();
		return new AUUserInfo(this);
	}

	@Override
	public xbean.AUUserInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AUUserInfo toBean() {
		_xdb_verify_unsafe_();
		return new AUUserInfo(this); // same as copy()
	}

	@Override
	public xbean.AUUserInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AUUserInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getRetcode() { // 
		_xdb_verify_unsafe_();
		return retcode;
	}

	@Override
	public int getFunc() { // 
		_xdb_verify_unsafe_();
		return func;
	}

	@Override
	public int getFuncparm() { // 
		_xdb_verify_unsafe_();
		return funcparm;
	}

	@Override
	public int getLoginip() { // 
		_xdb_verify_unsafe_();
		return loginip;
	}

	@Override
	public int getBlisgm() { // 
		_xdb_verify_unsafe_();
		return blisgm;
	}

	@Override
	public java.util.List<Integer> getAuth() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "auth"), auth);
	}

	public java.util.List<Integer> getAuthAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Integer> auth;
		AUUserInfo _o_ = this;
		auth = new java.util.ArrayList<Integer>();
		auth.addAll(_o_.auth);
		return auth;
	}

	@Override
	public int getAlgorithm() { // 
		_xdb_verify_unsafe_();
		return algorithm;
	}

	@Override
	public String getNickname() { // 
		_xdb_verify_unsafe_();
		return nickname;
	}

	@Override
	public com.locojoy.base.Octets getNicknameOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getNickname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getUsername() { // 
		_xdb_verify_unsafe_();
		return username;
	}

	@Override
	public com.locojoy.base.Octets getUsernameOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getUsername(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getMac() { // 
		_xdb_verify_unsafe_();
		return mac;
	}

	@Override
	public com.locojoy.base.Octets getMacOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getMac(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getOs() { // 
		_xdb_verify_unsafe_();
		return os;
	}

	@Override
	public com.locojoy.base.Octets getOsOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getOs(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPlatform() { // 
		_xdb_verify_unsafe_();
		return platform;
	}

	@Override
	public com.locojoy.base.Octets getPlatformOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlatform(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPlatname() { // 平台名称 yingyongbao,360 等
		_xdb_verify_unsafe_();
		return platname;
	}

	@Override
	public com.locojoy.base.Octets getPlatnameOctets() { // 平台名称 yingyongbao,360 等
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlatname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getClientsource() { // 
		_xdb_verify_unsafe_();
		return clientsource;
	}

	@Override
	public com.locojoy.base.Octets getClientsourceOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClientsource(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getIdfa() { // 
		_xdb_verify_unsafe_();
		return idfa;
	}

	@Override
	public com.locojoy.base.Octets getIdfaOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getIdfa(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setRetcode(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "retcode") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, retcode) {
					public void rollback() { retcode = _xdb_saved; }
				};}});
		retcode = _v_;
	}

	@Override
	public void setFunc(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "func") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, func) {
					public void rollback() { func = _xdb_saved; }
				};}});
		func = _v_;
	}

	@Override
	public void setFuncparm(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "funcparm") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, funcparm) {
					public void rollback() { funcparm = _xdb_saved; }
				};}});
		funcparm = _v_;
	}

	@Override
	public void setLoginip(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "loginip") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, loginip) {
					public void rollback() { loginip = _xdb_saved; }
				};}});
		loginip = _v_;
	}

	@Override
	public void setBlisgm(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "blisgm") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, blisgm) {
					public void rollback() { blisgm = _xdb_saved; }
				};}});
		blisgm = _v_;
	}

	@Override
	public void setAlgorithm(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "algorithm") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, algorithm) {
					public void rollback() { algorithm = _xdb_saved; }
				};}});
		algorithm = _v_;
	}

	@Override
	public void setNickname(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "nickname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, nickname) {
					public void rollback() { nickname = _xdb_saved; }
				};}});
		nickname = _v_;
	}

	@Override
	public void setNicknameOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setNickname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setUsername(String _v_) { // 
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
	public void setUsernameOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setUsername(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setMac(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mac") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, mac) {
					public void rollback() { mac = _xdb_saved; }
				};}});
		mac = _v_;
	}

	@Override
	public void setMacOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setMac(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setOs(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "os") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, os) {
					public void rollback() { os = _xdb_saved; }
				};}});
		os = _v_;
	}

	@Override
	public void setOsOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setOs(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPlatform(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "platform") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, platform) {
					public void rollback() { platform = _xdb_saved; }
				};}});
		platform = _v_;
	}

	@Override
	public void setPlatformOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setPlatform(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPlatname(String _v_) { // 平台名称 yingyongbao,360 等
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
	public void setPlatnameOctets(com.locojoy.base.Octets _v_) { // 平台名称 yingyongbao,360 等
		_xdb_verify_unsafe_();
		this.setPlatname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setClientsource(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clientsource") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clientsource) {
					public void rollback() { clientsource = _xdb_saved; }
				};}});
		clientsource = _v_;
	}

	@Override
	public void setClientsourceOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setClientsource(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setIdfa(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "idfa") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, idfa) {
					public void rollback() { idfa = _xdb_saved; }
				};}});
		idfa = _v_;
	}

	@Override
	public void setIdfaOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setIdfa(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AUUserInfo _o_ = null;
		if ( _o1_ instanceof AUUserInfo ) _o_ = (AUUserInfo)_o1_;
		else if ( _o1_ instanceof AUUserInfo.Const ) _o_ = ((AUUserInfo.Const)_o1_).nThis();
		else return false;
		if (retcode != _o_.retcode) return false;
		if (func != _o_.func) return false;
		if (funcparm != _o_.funcparm) return false;
		if (loginip != _o_.loginip) return false;
		if (blisgm != _o_.blisgm) return false;
		if (!auth.equals(_o_.auth)) return false;
		if (algorithm != _o_.algorithm) return false;
		if (!nickname.equals(_o_.nickname)) return false;
		if (!username.equals(_o_.username)) return false;
		if (!mac.equals(_o_.mac)) return false;
		if (!os.equals(_o_.os)) return false;
		if (!platform.equals(_o_.platform)) return false;
		if (!platname.equals(_o_.platname)) return false;
		if (!clientsource.equals(_o_.clientsource)) return false;
		if (!idfa.equals(_o_.idfa)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += retcode;
		_h_ += func;
		_h_ += funcparm;
		_h_ += loginip;
		_h_ += blisgm;
		_h_ += auth.hashCode();
		_h_ += algorithm;
		_h_ += nickname.hashCode();
		_h_ += username.hashCode();
		_h_ += mac.hashCode();
		_h_ += os.hashCode();
		_h_ += platform.hashCode();
		_h_ += platname.hashCode();
		_h_ += clientsource.hashCode();
		_h_ += idfa.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(retcode);
		_sb_.append(",");
		_sb_.append(func);
		_sb_.append(",");
		_sb_.append(funcparm);
		_sb_.append(",");
		_sb_.append(loginip);
		_sb_.append(",");
		_sb_.append(blisgm);
		_sb_.append(",");
		_sb_.append(auth);
		_sb_.append(",");
		_sb_.append(algorithm);
		_sb_.append(",");
		_sb_.append("'").append(nickname).append("'");
		_sb_.append(",");
		_sb_.append("'").append(username).append("'");
		_sb_.append(",");
		_sb_.append("'").append(mac).append("'");
		_sb_.append(",");
		_sb_.append("'").append(os).append("'");
		_sb_.append(",");
		_sb_.append("'").append(platform).append("'");
		_sb_.append(",");
		_sb_.append("'").append(platname).append("'");
		_sb_.append(",");
		_sb_.append("'").append(clientsource).append("'");
		_sb_.append(",");
		_sb_.append("'").append(idfa).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("retcode"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("func"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("funcparm"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("loginip"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("blisgm"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("auth"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("algorithm"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("nickname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("username"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mac"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("os"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("platform"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("platname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clientsource"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("idfa"));
		return lb;
	}

	private class Const implements xbean.AUUserInfo {
		AUUserInfo nThis() {
			return AUUserInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AUUserInfo copy() {
			return AUUserInfo.this.copy();
		}

		@Override
		public xbean.AUUserInfo toData() {
			return AUUserInfo.this.toData();
		}

		public xbean.AUUserInfo toBean() {
			return AUUserInfo.this.toBean();
		}

		@Override
		public xbean.AUUserInfo toDataIf() {
			return AUUserInfo.this.toDataIf();
		}

		public xbean.AUUserInfo toBeanIf() {
			return AUUserInfo.this.toBeanIf();
		}

		@Override
		public int getRetcode() { // 
			_xdb_verify_unsafe_();
			return retcode;
		}

		@Override
		public int getFunc() { // 
			_xdb_verify_unsafe_();
			return func;
		}

		@Override
		public int getFuncparm() { // 
			_xdb_verify_unsafe_();
			return funcparm;
		}

		@Override
		public int getLoginip() { // 
			_xdb_verify_unsafe_();
			return loginip;
		}

		@Override
		public int getBlisgm() { // 
			_xdb_verify_unsafe_();
			return blisgm;
		}

		@Override
		public java.util.List<Integer> getAuth() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(auth);
		}

		public java.util.List<Integer> getAuthAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Integer> auth;
			AUUserInfo _o_ = AUUserInfo.this;
		auth = new java.util.ArrayList<Integer>();
		auth.addAll(_o_.auth);
			return auth;
		}

		@Override
		public int getAlgorithm() { // 
			_xdb_verify_unsafe_();
			return algorithm;
		}

		@Override
		public String getNickname() { // 
			_xdb_verify_unsafe_();
			return nickname;
		}

		@Override
		public com.locojoy.base.Octets getNicknameOctets() { // 
			_xdb_verify_unsafe_();
			return AUUserInfo.this.getNicknameOctets();
		}

		@Override
		public String getUsername() { // 
			_xdb_verify_unsafe_();
			return username;
		}

		@Override
		public com.locojoy.base.Octets getUsernameOctets() { // 
			_xdb_verify_unsafe_();
			return AUUserInfo.this.getUsernameOctets();
		}

		@Override
		public String getMac() { // 
			_xdb_verify_unsafe_();
			return mac;
		}

		@Override
		public com.locojoy.base.Octets getMacOctets() { // 
			_xdb_verify_unsafe_();
			return AUUserInfo.this.getMacOctets();
		}

		@Override
		public String getOs() { // 
			_xdb_verify_unsafe_();
			return os;
		}

		@Override
		public com.locojoy.base.Octets getOsOctets() { // 
			_xdb_verify_unsafe_();
			return AUUserInfo.this.getOsOctets();
		}

		@Override
		public String getPlatform() { // 
			_xdb_verify_unsafe_();
			return platform;
		}

		@Override
		public com.locojoy.base.Octets getPlatformOctets() { // 
			_xdb_verify_unsafe_();
			return AUUserInfo.this.getPlatformOctets();
		}

		@Override
		public String getPlatname() { // 平台名称 yingyongbao,360 等
			_xdb_verify_unsafe_();
			return platname;
		}

		@Override
		public com.locojoy.base.Octets getPlatnameOctets() { // 平台名称 yingyongbao,360 等
			_xdb_verify_unsafe_();
			return AUUserInfo.this.getPlatnameOctets();
		}

		@Override
		public String getClientsource() { // 
			_xdb_verify_unsafe_();
			return clientsource;
		}

		@Override
		public com.locojoy.base.Octets getClientsourceOctets() { // 
			_xdb_verify_unsafe_();
			return AUUserInfo.this.getClientsourceOctets();
		}

		@Override
		public String getIdfa() { // 
			_xdb_verify_unsafe_();
			return idfa;
		}

		@Override
		public com.locojoy.base.Octets getIdfaOctets() { // 
			_xdb_verify_unsafe_();
			return AUUserInfo.this.getIdfaOctets();
		}

		@Override
		public void setRetcode(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFunc(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFuncparm(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLoginip(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBlisgm(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAlgorithm(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNickname(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNicknameOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUsername(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUsernameOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMac(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMacOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOs(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOsOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatform(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatname(String _v_) { // 平台名称 yingyongbao,360 等
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatnameOctets(com.locojoy.base.Octets _v_) { // 平台名称 yingyongbao,360 等
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClientsource(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClientsourceOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIdfa(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIdfaOctets(com.locojoy.base.Octets _v_) { // 
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
			return AUUserInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AUUserInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AUUserInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AUUserInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AUUserInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AUUserInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AUUserInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AUUserInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return AUUserInfo.this.toString();
		}

	}

	public static final class Data implements xbean.AUUserInfo {
		private int retcode; // 
		private int func; // 
		private int funcparm; // 
		private int loginip; // 
		private int blisgm; // 
		private java.util.ArrayList<Integer> auth; // 
		private int algorithm; // 
		private String nickname; // 
		private String username; // 
		private String mac; // 
		private String os; // 
		private String platform; // 
		private String platname; // 平台名称 yingyongbao,360 等
		private String clientsource; // 
		private String idfa; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			auth = new java.util.ArrayList<Integer>();
			nickname = "";
			username = "";
			mac = "";
			os = "";
			platform = "";
			platname = "";
			clientsource = "";
			idfa = "";
		}

		Data(xbean.AUUserInfo _o1_) {
			if (_o1_ instanceof AUUserInfo) assign((AUUserInfo)_o1_);
			else if (_o1_ instanceof AUUserInfo.Data) assign((AUUserInfo.Data)_o1_);
			else if (_o1_ instanceof AUUserInfo.Const) assign(((AUUserInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AUUserInfo _o_) {
			retcode = _o_.retcode;
			func = _o_.func;
			funcparm = _o_.funcparm;
			loginip = _o_.loginip;
			blisgm = _o_.blisgm;
			auth = new java.util.ArrayList<Integer>();
			auth.addAll(_o_.auth);
			algorithm = _o_.algorithm;
			nickname = _o_.nickname;
			username = _o_.username;
			mac = _o_.mac;
			os = _o_.os;
			platform = _o_.platform;
			platname = _o_.platname;
			clientsource = _o_.clientsource;
			idfa = _o_.idfa;
		}

		private void assign(AUUserInfo.Data _o_) {
			retcode = _o_.retcode;
			func = _o_.func;
			funcparm = _o_.funcparm;
			loginip = _o_.loginip;
			blisgm = _o_.blisgm;
			auth = new java.util.ArrayList<Integer>();
			auth.addAll(_o_.auth);
			algorithm = _o_.algorithm;
			nickname = _o_.nickname;
			username = _o_.username;
			mac = _o_.mac;
			os = _o_.os;
			platform = _o_.platform;
			platname = _o_.platname;
			clientsource = _o_.clientsource;
			idfa = _o_.idfa;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(retcode);
			_os_.marshal(func);
			_os_.marshal(funcparm);
			_os_.marshal(loginip);
			_os_.marshal(blisgm);
			_os_.compact_uint32(auth.size());
			for (Integer _v_ : auth) {
				_os_.marshal(_v_);
			}
			_os_.marshal(algorithm);
			_os_.marshal(nickname, mkdb.Const.IO_CHARSET);
			_os_.marshal(username, mkdb.Const.IO_CHARSET);
			_os_.marshal(mac, mkdb.Const.IO_CHARSET);
			_os_.marshal(os, mkdb.Const.IO_CHARSET);
			_os_.marshal(platform, mkdb.Const.IO_CHARSET);
			_os_.marshal(platname, mkdb.Const.IO_CHARSET);
			_os_.marshal(clientsource, mkdb.Const.IO_CHARSET);
			_os_.marshal(idfa, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			retcode = _os_.unmarshal_int();
			func = _os_.unmarshal_int();
			funcparm = _os_.unmarshal_int();
			loginip = _os_.unmarshal_int();
			blisgm = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				auth.add(_v_);
			}
			algorithm = _os_.unmarshal_int();
			nickname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			username = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			mac = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			os = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			platform = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			platname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			clientsource = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			idfa = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.AUUserInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.AUUserInfo toData() {
			return new Data(this);
		}

		public xbean.AUUserInfo toBean() {
			return new AUUserInfo(this, null, null);
		}

		@Override
		public xbean.AUUserInfo toDataIf() {
			return this;
		}

		public xbean.AUUserInfo toBeanIf() {
			return new AUUserInfo(this, null, null);
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
		public int getRetcode() { // 
			return retcode;
		}

		@Override
		public int getFunc() { // 
			return func;
		}

		@Override
		public int getFuncparm() { // 
			return funcparm;
		}

		@Override
		public int getLoginip() { // 
			return loginip;
		}

		@Override
		public int getBlisgm() { // 
			return blisgm;
		}

		@Override
		public java.util.List<Integer> getAuth() { // 
			return auth;
		}

		@Override
		public java.util.List<Integer> getAuthAsData() { // 
			return auth;
		}

		@Override
		public int getAlgorithm() { // 
			return algorithm;
		}

		@Override
		public String getNickname() { // 
			return nickname;
		}

		@Override
		public com.locojoy.base.Octets getNicknameOctets() { // 
			return com.locojoy.base.Octets.wrap(getNickname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getUsername() { // 
			return username;
		}

		@Override
		public com.locojoy.base.Octets getUsernameOctets() { // 
			return com.locojoy.base.Octets.wrap(getUsername(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getMac() { // 
			return mac;
		}

		@Override
		public com.locojoy.base.Octets getMacOctets() { // 
			return com.locojoy.base.Octets.wrap(getMac(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getOs() { // 
			return os;
		}

		@Override
		public com.locojoy.base.Octets getOsOctets() { // 
			return com.locojoy.base.Octets.wrap(getOs(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPlatform() { // 
			return platform;
		}

		@Override
		public com.locojoy.base.Octets getPlatformOctets() { // 
			return com.locojoy.base.Octets.wrap(getPlatform(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPlatname() { // 平台名称 yingyongbao,360 等
			return platname;
		}

		@Override
		public com.locojoy.base.Octets getPlatnameOctets() { // 平台名称 yingyongbao,360 等
			return com.locojoy.base.Octets.wrap(getPlatname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getClientsource() { // 
			return clientsource;
		}

		@Override
		public com.locojoy.base.Octets getClientsourceOctets() { // 
			return com.locojoy.base.Octets.wrap(getClientsource(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getIdfa() { // 
			return idfa;
		}

		@Override
		public com.locojoy.base.Octets getIdfaOctets() { // 
			return com.locojoy.base.Octets.wrap(getIdfa(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setRetcode(int _v_) { // 
			retcode = _v_;
		}

		@Override
		public void setFunc(int _v_) { // 
			func = _v_;
		}

		@Override
		public void setFuncparm(int _v_) { // 
			funcparm = _v_;
		}

		@Override
		public void setLoginip(int _v_) { // 
			loginip = _v_;
		}

		@Override
		public void setBlisgm(int _v_) { // 
			blisgm = _v_;
		}

		@Override
		public void setAlgorithm(int _v_) { // 
			algorithm = _v_;
		}

		@Override
		public void setNickname(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			nickname = _v_;
		}

		@Override
		public void setNicknameOctets(com.locojoy.base.Octets _v_) { // 
			this.setNickname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setUsername(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			username = _v_;
		}

		@Override
		public void setUsernameOctets(com.locojoy.base.Octets _v_) { // 
			this.setUsername(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setMac(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			mac = _v_;
		}

		@Override
		public void setMacOctets(com.locojoy.base.Octets _v_) { // 
			this.setMac(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setOs(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			os = _v_;
		}

		@Override
		public void setOsOctets(com.locojoy.base.Octets _v_) { // 
			this.setOs(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPlatform(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			platform = _v_;
		}

		@Override
		public void setPlatformOctets(com.locojoy.base.Octets _v_) { // 
			this.setPlatform(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPlatname(String _v_) { // 平台名称 yingyongbao,360 等
			if (null == _v_)
				throw new NullPointerException();
			platname = _v_;
		}

		@Override
		public void setPlatnameOctets(com.locojoy.base.Octets _v_) { // 平台名称 yingyongbao,360 等
			this.setPlatname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setClientsource(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			clientsource = _v_;
		}

		@Override
		public void setClientsourceOctets(com.locojoy.base.Octets _v_) { // 
			this.setClientsource(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setIdfa(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			idfa = _v_;
		}

		@Override
		public void setIdfaOctets(com.locojoy.base.Octets _v_) { // 
			this.setIdfa(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AUUserInfo.Data)) return false;
			AUUserInfo.Data _o_ = (AUUserInfo.Data) _o1_;
			if (retcode != _o_.retcode) return false;
			if (func != _o_.func) return false;
			if (funcparm != _o_.funcparm) return false;
			if (loginip != _o_.loginip) return false;
			if (blisgm != _o_.blisgm) return false;
			if (!auth.equals(_o_.auth)) return false;
			if (algorithm != _o_.algorithm) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			if (!username.equals(_o_.username)) return false;
			if (!mac.equals(_o_.mac)) return false;
			if (!os.equals(_o_.os)) return false;
			if (!platform.equals(_o_.platform)) return false;
			if (!platname.equals(_o_.platname)) return false;
			if (!clientsource.equals(_o_.clientsource)) return false;
			if (!idfa.equals(_o_.idfa)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += retcode;
			_h_ += func;
			_h_ += funcparm;
			_h_ += loginip;
			_h_ += blisgm;
			_h_ += auth.hashCode();
			_h_ += algorithm;
			_h_ += nickname.hashCode();
			_h_ += username.hashCode();
			_h_ += mac.hashCode();
			_h_ += os.hashCode();
			_h_ += platform.hashCode();
			_h_ += platname.hashCode();
			_h_ += clientsource.hashCode();
			_h_ += idfa.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(retcode);
			_sb_.append(",");
			_sb_.append(func);
			_sb_.append(",");
			_sb_.append(funcparm);
			_sb_.append(",");
			_sb_.append(loginip);
			_sb_.append(",");
			_sb_.append(blisgm);
			_sb_.append(",");
			_sb_.append(auth);
			_sb_.append(",");
			_sb_.append(algorithm);
			_sb_.append(",");
			_sb_.append("'").append(nickname).append("'");
			_sb_.append(",");
			_sb_.append("'").append(username).append("'");
			_sb_.append(",");
			_sb_.append("'").append(mac).append("'");
			_sb_.append(",");
			_sb_.append("'").append(os).append("'");
			_sb_.append(",");
			_sb_.append("'").append(platform).append("'");
			_sb_.append(",");
			_sb_.append("'").append(platname).append("'");
			_sb_.append(",");
			_sb_.append("'").append(clientsource).append("'");
			_sb_.append(",");
			_sb_.append("'").append(idfa).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

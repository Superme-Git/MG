
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class YingYongBao extends mkdb.XBean implements xbean.YingYongBao {
	private String openid; // 
	private String openkey; // 
	private String paytoken; // 
	private String pf; // 
	private String pfkey; // 
	private String zoneid; // 
	private String platformname; // 手Q用户:qq 微信用户:weixin

	@Override
	public void _reset_unsafe_() {
		openid = "";
		openkey = "";
		paytoken = "";
		pf = "";
		pfkey = "";
		zoneid = "";
		platformname = "";
	}

	YingYongBao(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		openid = "";
		openkey = "";
		paytoken = "";
		pf = "";
		pfkey = "";
		zoneid = "";
		platformname = "";
	}

	public YingYongBao() {
		this(0, null, null);
	}

	public YingYongBao(YingYongBao _o_) {
		this(_o_, null, null);
	}

	YingYongBao(xbean.YingYongBao _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof YingYongBao) assign((YingYongBao)_o1_);
		else if (_o1_ instanceof YingYongBao.Data) assign((YingYongBao.Data)_o1_);
		else if (_o1_ instanceof YingYongBao.Const) assign(((YingYongBao.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(YingYongBao _o_) {
		_o_._xdb_verify_unsafe_();
		openid = _o_.openid;
		openkey = _o_.openkey;
		paytoken = _o_.paytoken;
		pf = _o_.pf;
		pfkey = _o_.pfkey;
		zoneid = _o_.zoneid;
		platformname = _o_.platformname;
	}

	private void assign(YingYongBao.Data _o_) {
		openid = _o_.openid;
		openkey = _o_.openkey;
		paytoken = _o_.paytoken;
		pf = _o_.pf;
		pfkey = _o_.pfkey;
		zoneid = _o_.zoneid;
		platformname = _o_.platformname;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(openid, mkdb.Const.IO_CHARSET);
		_os_.marshal(openkey, mkdb.Const.IO_CHARSET);
		_os_.marshal(paytoken, mkdb.Const.IO_CHARSET);
		_os_.marshal(pf, mkdb.Const.IO_CHARSET);
		_os_.marshal(pfkey, mkdb.Const.IO_CHARSET);
		_os_.marshal(zoneid, mkdb.Const.IO_CHARSET);
		_os_.marshal(platformname, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		openid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		openkey = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		paytoken = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		pf = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		pfkey = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		zoneid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		platformname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.YingYongBao copy() {
		_xdb_verify_unsafe_();
		return new YingYongBao(this);
	}

	@Override
	public xbean.YingYongBao toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YingYongBao toBean() {
		_xdb_verify_unsafe_();
		return new YingYongBao(this); // same as copy()
	}

	@Override
	public xbean.YingYongBao toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YingYongBao toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getOpenid() { // 
		_xdb_verify_unsafe_();
		return openid;
	}

	@Override
	public com.locojoy.base.Octets getOpenidOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getOpenid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getOpenkey() { // 
		_xdb_verify_unsafe_();
		return openkey;
	}

	@Override
	public com.locojoy.base.Octets getOpenkeyOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getOpenkey(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPaytoken() { // 
		_xdb_verify_unsafe_();
		return paytoken;
	}

	@Override
	public com.locojoy.base.Octets getPaytokenOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPaytoken(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPf() { // 
		_xdb_verify_unsafe_();
		return pf;
	}

	@Override
	public com.locojoy.base.Octets getPfOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPf(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPfkey() { // 
		_xdb_verify_unsafe_();
		return pfkey;
	}

	@Override
	public com.locojoy.base.Octets getPfkeyOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPfkey(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getZoneid() { // 
		_xdb_verify_unsafe_();
		return zoneid;
	}

	@Override
	public com.locojoy.base.Octets getZoneidOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getZoneid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getPlatformname() { // 手Q用户:qq 微信用户:weixin
		_xdb_verify_unsafe_();
		return platformname;
	}

	@Override
	public com.locojoy.base.Octets getPlatformnameOctets() { // 手Q用户:qq 微信用户:weixin
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlatformname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setOpenid(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "openid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, openid) {
					public void rollback() { openid = _xdb_saved; }
				};}});
		openid = _v_;
	}

	@Override
	public void setOpenidOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setOpenid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setOpenkey(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "openkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, openkey) {
					public void rollback() { openkey = _xdb_saved; }
				};}});
		openkey = _v_;
	}

	@Override
	public void setOpenkeyOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setOpenkey(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPaytoken(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "paytoken") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, paytoken) {
					public void rollback() { paytoken = _xdb_saved; }
				};}});
		paytoken = _v_;
	}

	@Override
	public void setPaytokenOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setPaytoken(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPf(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "pf") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, pf) {
					public void rollback() { pf = _xdb_saved; }
				};}});
		pf = _v_;
	}

	@Override
	public void setPfOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setPf(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPfkey(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "pfkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, pfkey) {
					public void rollback() { pfkey = _xdb_saved; }
				};}});
		pfkey = _v_;
	}

	@Override
	public void setPfkeyOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setPfkey(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setZoneid(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "zoneid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, zoneid) {
					public void rollback() { zoneid = _xdb_saved; }
				};}});
		zoneid = _v_;
	}

	@Override
	public void setZoneidOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setZoneid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPlatformname(String _v_) { // 手Q用户:qq 微信用户:weixin
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "platformname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, platformname) {
					public void rollback() { platformname = _xdb_saved; }
				};}});
		platformname = _v_;
	}

	@Override
	public void setPlatformnameOctets(com.locojoy.base.Octets _v_) { // 手Q用户:qq 微信用户:weixin
		_xdb_verify_unsafe_();
		this.setPlatformname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		YingYongBao _o_ = null;
		if ( _o1_ instanceof YingYongBao ) _o_ = (YingYongBao)_o1_;
		else if ( _o1_ instanceof YingYongBao.Const ) _o_ = ((YingYongBao.Const)_o1_).nThis();
		else return false;
		if (!openid.equals(_o_.openid)) return false;
		if (!openkey.equals(_o_.openkey)) return false;
		if (!paytoken.equals(_o_.paytoken)) return false;
		if (!pf.equals(_o_.pf)) return false;
		if (!pfkey.equals(_o_.pfkey)) return false;
		if (!zoneid.equals(_o_.zoneid)) return false;
		if (!platformname.equals(_o_.platformname)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += openid.hashCode();
		_h_ += openkey.hashCode();
		_h_ += paytoken.hashCode();
		_h_ += pf.hashCode();
		_h_ += pfkey.hashCode();
		_h_ += zoneid.hashCode();
		_h_ += platformname.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(openid).append("'");
		_sb_.append(",");
		_sb_.append("'").append(openkey).append("'");
		_sb_.append(",");
		_sb_.append("'").append(paytoken).append("'");
		_sb_.append(",");
		_sb_.append("'").append(pf).append("'");
		_sb_.append(",");
		_sb_.append("'").append(pfkey).append("'");
		_sb_.append(",");
		_sb_.append("'").append(zoneid).append("'");
		_sb_.append(",");
		_sb_.append("'").append(platformname).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("openid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("openkey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("paytoken"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("pf"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("pfkey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("zoneid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("platformname"));
		return lb;
	}

	private class Const implements xbean.YingYongBao {
		YingYongBao nThis() {
			return YingYongBao.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.YingYongBao copy() {
			return YingYongBao.this.copy();
		}

		@Override
		public xbean.YingYongBao toData() {
			return YingYongBao.this.toData();
		}

		public xbean.YingYongBao toBean() {
			return YingYongBao.this.toBean();
		}

		@Override
		public xbean.YingYongBao toDataIf() {
			return YingYongBao.this.toDataIf();
		}

		public xbean.YingYongBao toBeanIf() {
			return YingYongBao.this.toBeanIf();
		}

		@Override
		public String getOpenid() { // 
			_xdb_verify_unsafe_();
			return openid;
		}

		@Override
		public com.locojoy.base.Octets getOpenidOctets() { // 
			_xdb_verify_unsafe_();
			return YingYongBao.this.getOpenidOctets();
		}

		@Override
		public String getOpenkey() { // 
			_xdb_verify_unsafe_();
			return openkey;
		}

		@Override
		public com.locojoy.base.Octets getOpenkeyOctets() { // 
			_xdb_verify_unsafe_();
			return YingYongBao.this.getOpenkeyOctets();
		}

		@Override
		public String getPaytoken() { // 
			_xdb_verify_unsafe_();
			return paytoken;
		}

		@Override
		public com.locojoy.base.Octets getPaytokenOctets() { // 
			_xdb_verify_unsafe_();
			return YingYongBao.this.getPaytokenOctets();
		}

		@Override
		public String getPf() { // 
			_xdb_verify_unsafe_();
			return pf;
		}

		@Override
		public com.locojoy.base.Octets getPfOctets() { // 
			_xdb_verify_unsafe_();
			return YingYongBao.this.getPfOctets();
		}

		@Override
		public String getPfkey() { // 
			_xdb_verify_unsafe_();
			return pfkey;
		}

		@Override
		public com.locojoy.base.Octets getPfkeyOctets() { // 
			_xdb_verify_unsafe_();
			return YingYongBao.this.getPfkeyOctets();
		}

		@Override
		public String getZoneid() { // 
			_xdb_verify_unsafe_();
			return zoneid;
		}

		@Override
		public com.locojoy.base.Octets getZoneidOctets() { // 
			_xdb_verify_unsafe_();
			return YingYongBao.this.getZoneidOctets();
		}

		@Override
		public String getPlatformname() { // 手Q用户:qq 微信用户:weixin
			_xdb_verify_unsafe_();
			return platformname;
		}

		@Override
		public com.locojoy.base.Octets getPlatformnameOctets() { // 手Q用户:qq 微信用户:weixin
			_xdb_verify_unsafe_();
			return YingYongBao.this.getPlatformnameOctets();
		}

		@Override
		public void setOpenid(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOpenidOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOpenkey(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOpenkeyOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPaytoken(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPaytokenOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPf(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPfOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPfkey(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPfkeyOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setZoneid(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setZoneidOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformname(String _v_) { // 手Q用户:qq 微信用户:weixin
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformnameOctets(com.locojoy.base.Octets _v_) { // 手Q用户:qq 微信用户:weixin
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
			return YingYongBao.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return YingYongBao.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return YingYongBao.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return YingYongBao.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return YingYongBao.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return YingYongBao.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return YingYongBao.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return YingYongBao.this.hashCode();
		}

		@Override
		public String toString() {
			return YingYongBao.this.toString();
		}

	}

	public static final class Data implements xbean.YingYongBao {
		private String openid; // 
		private String openkey; // 
		private String paytoken; // 
		private String pf; // 
		private String pfkey; // 
		private String zoneid; // 
		private String platformname; // 手Q用户:qq 微信用户:weixin

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			openid = "";
			openkey = "";
			paytoken = "";
			pf = "";
			pfkey = "";
			zoneid = "";
			platformname = "";
		}

		Data(xbean.YingYongBao _o1_) {
			if (_o1_ instanceof YingYongBao) assign((YingYongBao)_o1_);
			else if (_o1_ instanceof YingYongBao.Data) assign((YingYongBao.Data)_o1_);
			else if (_o1_ instanceof YingYongBao.Const) assign(((YingYongBao.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(YingYongBao _o_) {
			openid = _o_.openid;
			openkey = _o_.openkey;
			paytoken = _o_.paytoken;
			pf = _o_.pf;
			pfkey = _o_.pfkey;
			zoneid = _o_.zoneid;
			platformname = _o_.platformname;
		}

		private void assign(YingYongBao.Data _o_) {
			openid = _o_.openid;
			openkey = _o_.openkey;
			paytoken = _o_.paytoken;
			pf = _o_.pf;
			pfkey = _o_.pfkey;
			zoneid = _o_.zoneid;
			platformname = _o_.platformname;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(openid, mkdb.Const.IO_CHARSET);
			_os_.marshal(openkey, mkdb.Const.IO_CHARSET);
			_os_.marshal(paytoken, mkdb.Const.IO_CHARSET);
			_os_.marshal(pf, mkdb.Const.IO_CHARSET);
			_os_.marshal(pfkey, mkdb.Const.IO_CHARSET);
			_os_.marshal(zoneid, mkdb.Const.IO_CHARSET);
			_os_.marshal(platformname, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			openid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			openkey = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			paytoken = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			pf = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			pfkey = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			zoneid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			platformname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.YingYongBao copy() {
			return new Data(this);
		}

		@Override
		public xbean.YingYongBao toData() {
			return new Data(this);
		}

		public xbean.YingYongBao toBean() {
			return new YingYongBao(this, null, null);
		}

		@Override
		public xbean.YingYongBao toDataIf() {
			return this;
		}

		public xbean.YingYongBao toBeanIf() {
			return new YingYongBao(this, null, null);
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
		public String getOpenid() { // 
			return openid;
		}

		@Override
		public com.locojoy.base.Octets getOpenidOctets() { // 
			return com.locojoy.base.Octets.wrap(getOpenid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getOpenkey() { // 
			return openkey;
		}

		@Override
		public com.locojoy.base.Octets getOpenkeyOctets() { // 
			return com.locojoy.base.Octets.wrap(getOpenkey(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPaytoken() { // 
			return paytoken;
		}

		@Override
		public com.locojoy.base.Octets getPaytokenOctets() { // 
			return com.locojoy.base.Octets.wrap(getPaytoken(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPf() { // 
			return pf;
		}

		@Override
		public com.locojoy.base.Octets getPfOctets() { // 
			return com.locojoy.base.Octets.wrap(getPf(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPfkey() { // 
			return pfkey;
		}

		@Override
		public com.locojoy.base.Octets getPfkeyOctets() { // 
			return com.locojoy.base.Octets.wrap(getPfkey(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getZoneid() { // 
			return zoneid;
		}

		@Override
		public com.locojoy.base.Octets getZoneidOctets() { // 
			return com.locojoy.base.Octets.wrap(getZoneid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getPlatformname() { // 手Q用户:qq 微信用户:weixin
			return platformname;
		}

		@Override
		public com.locojoy.base.Octets getPlatformnameOctets() { // 手Q用户:qq 微信用户:weixin
			return com.locojoy.base.Octets.wrap(getPlatformname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setOpenid(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			openid = _v_;
		}

		@Override
		public void setOpenidOctets(com.locojoy.base.Octets _v_) { // 
			this.setOpenid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setOpenkey(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			openkey = _v_;
		}

		@Override
		public void setOpenkeyOctets(com.locojoy.base.Octets _v_) { // 
			this.setOpenkey(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPaytoken(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			paytoken = _v_;
		}

		@Override
		public void setPaytokenOctets(com.locojoy.base.Octets _v_) { // 
			this.setPaytoken(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPf(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			pf = _v_;
		}

		@Override
		public void setPfOctets(com.locojoy.base.Octets _v_) { // 
			this.setPf(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPfkey(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			pfkey = _v_;
		}

		@Override
		public void setPfkeyOctets(com.locojoy.base.Octets _v_) { // 
			this.setPfkey(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setZoneid(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			zoneid = _v_;
		}

		@Override
		public void setZoneidOctets(com.locojoy.base.Octets _v_) { // 
			this.setZoneid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPlatformname(String _v_) { // 手Q用户:qq 微信用户:weixin
			if (null == _v_)
				throw new NullPointerException();
			platformname = _v_;
		}

		@Override
		public void setPlatformnameOctets(com.locojoy.base.Octets _v_) { // 手Q用户:qq 微信用户:weixin
			this.setPlatformname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof YingYongBao.Data)) return false;
			YingYongBao.Data _o_ = (YingYongBao.Data) _o1_;
			if (!openid.equals(_o_.openid)) return false;
			if (!openkey.equals(_o_.openkey)) return false;
			if (!paytoken.equals(_o_.paytoken)) return false;
			if (!pf.equals(_o_.pf)) return false;
			if (!pfkey.equals(_o_.pfkey)) return false;
			if (!zoneid.equals(_o_.zoneid)) return false;
			if (!platformname.equals(_o_.platformname)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += openid.hashCode();
			_h_ += openkey.hashCode();
			_h_ += paytoken.hashCode();
			_h_ += pf.hashCode();
			_h_ += pfkey.hashCode();
			_h_ += zoneid.hashCode();
			_h_ += platformname.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(openid).append("'");
			_sb_.append(",");
			_sb_.append("'").append(openkey).append("'");
			_sb_.append(",");
			_sb_.append("'").append(paytoken).append("'");
			_sb_.append(",");
			_sb_.append("'").append(pf).append("'");
			_sb_.append(",");
			_sb_.append("'").append(pfkey).append("'");
			_sb_.append(",");
			_sb_.append("'").append(zoneid).append("'");
			_sb_.append(",");
			_sb_.append("'").append(platformname).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

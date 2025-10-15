
package xbean.__;

import com.goldhuman.Common.Marshal.OctetsStream;
import com.goldhuman.Common.Marshal.MarshalException;

public final class LocojoyPlatOrderInfo extends xdb.XBean implements xbean.LocojoyPlatOrderInfo {
	private long createtime; // 
	private String orderserialplat; // 
	private String orderserialgame; // 
	private String username; // 
	private byte [] vars; // 

	@Override
	public void _reset_unsafe_() {
		createtime = 0L;
		orderserialplat = "";
		orderserialgame = "";
		username = "";
		vars = new byte[0];
	}

	LocojoyPlatOrderInfo(int __, xdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		orderserialplat = "";
		orderserialgame = "";
		username = "";
		vars = new byte[0];
	}

	public LocojoyPlatOrderInfo() {
		this(0, null, null);
	}

	public LocojoyPlatOrderInfo(LocojoyPlatOrderInfo _o_) {
		this(_o_, null, null);
	}

	LocojoyPlatOrderInfo(xbean.LocojoyPlatOrderInfo _o1_, xdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LocojoyPlatOrderInfo) assign((LocojoyPlatOrderInfo)_o1_);
		else if (_o1_ instanceof LocojoyPlatOrderInfo.Data) assign((LocojoyPlatOrderInfo.Data)_o1_);
		else if (_o1_ instanceof LocojoyPlatOrderInfo.Const) assign(((LocojoyPlatOrderInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LocojoyPlatOrderInfo _o_) {
		_o_._xdb_verify_unsafe_();
		createtime = _o_.createtime;
		orderserialplat = _o_.orderserialplat;
		orderserialgame = _o_.orderserialgame;
		username = _o_.username;
		vars = java.util.Arrays.copyOf(_o_.vars, _o_.vars.length);
	}

	private void assign(LocojoyPlatOrderInfo.Data _o_) {
		createtime = _o_.createtime;
		orderserialplat = _o_.orderserialplat;
		orderserialgame = _o_.orderserialgame;
		username = _o_.username;
		vars = java.util.Arrays.copyOf(_o_.vars, _o_.vars.length);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(createtime);
		_os_.marshal(orderserialplat, xdb.Const.IO_CHARSET);
		_os_.marshal(orderserialgame, xdb.Const.IO_CHARSET);
		_os_.marshal(username, xdb.Const.IO_CHARSET);
		_os_.marshal(vars);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		createtime = _os_.unmarshal_long();
		orderserialplat = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
		orderserialgame = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
		username = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
		vars = _os_.unmarshal_bytes();
		return _os_;
	}

	@Override
	public xbean.LocojoyPlatOrderInfo copy() {
		_xdb_verify_unsafe_();
		return new LocojoyPlatOrderInfo(this);
	}

	@Override
	public xbean.LocojoyPlatOrderInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LocojoyPlatOrderInfo toBean() {
		_xdb_verify_unsafe_();
		return new LocojoyPlatOrderInfo(this); // same as copy()
	}

	@Override
	public xbean.LocojoyPlatOrderInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LocojoyPlatOrderInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public xdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getCreatetime() { // 
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public String getOrderserialplat() { // 
		_xdb_verify_unsafe_();
		return orderserialplat;
	}

	@Override
	public com.goldhuman.Common.Octets getOrderserialplatOctets() { // 
		_xdb_verify_unsafe_();
		return com.goldhuman.Common.Octets.wrap(getOrderserialplat(), xdb.Const.IO_CHARSET);
	}

	@Override
	public String getOrderserialgame() { // 
		_xdb_verify_unsafe_();
		return orderserialgame;
	}

	@Override
	public com.goldhuman.Common.Octets getOrderserialgameOctets() { // 
		_xdb_verify_unsafe_();
		return com.goldhuman.Common.Octets.wrap(getOrderserialgame(), xdb.Const.IO_CHARSET);
	}

	@Override
	public String getUsername() { // 
		_xdb_verify_unsafe_();
		return username;
	}

	@Override
	public com.goldhuman.Common.Octets getUsernameOctets() { // 
		_xdb_verify_unsafe_();
		return com.goldhuman.Common.Octets.wrap(getUsername(), xdb.Const.IO_CHARSET);
	}

	@Override
	public <T extends com.goldhuman.Common.Marshal.Marshal> T getVars(T _v_) { // 
		_xdb_verify_unsafe_();
		try {
			_v_.unmarshal(OctetsStream.wrap(com.goldhuman.Common.Octets.wrap(vars)));
			return _v_;
		} catch (MarshalException _e_) {
			throw new xio.MarshalError();
		}
	}

	@Override
	public boolean isVarsEmpty() { // 
		_xdb_verify_unsafe_();
		return vars.length == 0;
	}

	@Override
	public byte[] getVarsCopy() { // 
		_xdb_verify_unsafe_();
		return java.util.Arrays.copyOf(vars, vars.length);
	}

	@Override
	public void setCreatetime(long _v_) { // 
		_xdb_verify_unsafe_();
		xdb.Logs.logIf(new xdb.LogKey(this, "createtime") {
			protected xdb.Log create() {
				return new xdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public void setOrderserialplat(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		xdb.Logs.logIf(new xdb.LogKey(this, "orderserialplat") {
			protected xdb.Log create() {
				return new xdb.logs.LogString(this, orderserialplat) {
					public void rollback() { orderserialplat = _xdb_saved; }
				};}});
		orderserialplat = _v_;
	}

	@Override
	public void setOrderserialplatOctets(com.goldhuman.Common.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setOrderserialplat(_v_.getString(xdb.Const.IO_CHARSET));
	}

	@Override
	public void setOrderserialgame(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		xdb.Logs.logIf(new xdb.LogKey(this, "orderserialgame") {
			protected xdb.Log create() {
				return new xdb.logs.LogString(this, orderserialgame) {
					public void rollback() { orderserialgame = _xdb_saved; }
				};}});
		orderserialgame = _v_;
	}

	@Override
	public void setOrderserialgameOctets(com.goldhuman.Common.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setOrderserialgame(_v_.getString(xdb.Const.IO_CHARSET));
	}

	@Override
	public void setUsername(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		xdb.Logs.logIf(new xdb.LogKey(this, "username") {
			protected xdb.Log create() {
				return new xdb.logs.LogString(this, username) {
					public void rollback() { username = _xdb_saved; }
				};}});
		username = _v_;
	}

	@Override
	public void setUsernameOctets(com.goldhuman.Common.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setUsername(_v_.getString(xdb.Const.IO_CHARSET));
	}

	@Override
	public void setVars(com.goldhuman.Common.Marshal.Marshal _v_) { // 
		_xdb_verify_unsafe_();
		xdb.Logs.logIf(new xdb.LogKey(this, "vars") {
			protected xdb.Log create() {
				return new xdb.logs.LogObject<byte []>(this, vars) {
					public void rollback() { vars = _xdb_saved; }
			}; }});
		vars = _v_.marshal(new OctetsStream()).getBytes();
	}

	@Override
	public void setVarsCopy(byte[] _v_) { // 
		_xdb_verify_unsafe_();
		xdb.Logs.logIf(new xdb.LogKey(this, "vars") {
			protected xdb.Log create() {
				return new xdb.logs.LogObject<byte []>(this, vars) {
					public void rollback() { vars = _xdb_saved; }
			}; }});
		vars = java.util.Arrays.copyOf(_v_, _v_.length);
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LocojoyPlatOrderInfo _o_ = null;
		if ( _o1_ instanceof LocojoyPlatOrderInfo ) _o_ = (LocojoyPlatOrderInfo)_o1_;
		else if ( _o1_ instanceof LocojoyPlatOrderInfo.Const ) _o_ = ((LocojoyPlatOrderInfo.Const)_o1_).nThis();
		else return false;
		if (createtime != _o_.createtime) return false;
		if (!orderserialplat.equals(_o_.orderserialplat)) return false;
		if (!orderserialgame.equals(_o_.orderserialgame)) return false;
		if (!username.equals(_o_.username)) return false;
		if (!java.util.Arrays.equals(vars, _o_.vars)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += createtime;
		_h_ += orderserialplat.hashCode();
		_h_ += orderserialgame.hashCode();
		_h_ += username.hashCode();
		_h_ += java.util.Arrays.hashCode(vars);
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append("'").append(orderserialplat).append("'");
		_sb_.append(",");
		_sb_.append("'").append(orderserialgame).append("'");
		_sb_.append(",");
		_sb_.append("'").append(username).append("'");
		_sb_.append(",");
		_sb_.append('B').append(vars.length);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public xdb.logs.Listenable newListenable() {
		xdb.logs.ListenableBean lb = new xdb.logs.ListenableBean();
		lb.add(new xdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new xdb.logs.ListenableChanged().setVarName("orderserialplat"));
		lb.add(new xdb.logs.ListenableChanged().setVarName("orderserialgame"));
		lb.add(new xdb.logs.ListenableChanged().setVarName("username"));
		lb.add(new xdb.logs.ListenableChanged().setVarName("vars"));
		return lb;
	}

	private class Const implements xbean.LocojoyPlatOrderInfo {
		LocojoyPlatOrderInfo nThis() {
			return LocojoyPlatOrderInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LocojoyPlatOrderInfo copy() {
			return LocojoyPlatOrderInfo.this.copy();
		}

		@Override
		public xbean.LocojoyPlatOrderInfo toData() {
			return LocojoyPlatOrderInfo.this.toData();
		}

		public xbean.LocojoyPlatOrderInfo toBean() {
			return LocojoyPlatOrderInfo.this.toBean();
		}

		@Override
		public xbean.LocojoyPlatOrderInfo toDataIf() {
			return LocojoyPlatOrderInfo.this.toDataIf();
		}

		public xbean.LocojoyPlatOrderInfo toBeanIf() {
			return LocojoyPlatOrderInfo.this.toBeanIf();
		}

		@Override
		public long getCreatetime() { // 
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public String getOrderserialplat() { // 
			_xdb_verify_unsafe_();
			return orderserialplat;
		}

		@Override
		public com.goldhuman.Common.Octets getOrderserialplatOctets() { // 
			_xdb_verify_unsafe_();
			return LocojoyPlatOrderInfo.this.getOrderserialplatOctets();
		}

		@Override
		public String getOrderserialgame() { // 
			_xdb_verify_unsafe_();
			return orderserialgame;
		}

		@Override
		public com.goldhuman.Common.Octets getOrderserialgameOctets() { // 
			_xdb_verify_unsafe_();
			return LocojoyPlatOrderInfo.this.getOrderserialgameOctets();
		}

		@Override
		public String getUsername() { // 
			_xdb_verify_unsafe_();
			return username;
		}

		@Override
		public com.goldhuman.Common.Octets getUsernameOctets() { // 
			_xdb_verify_unsafe_();
			return LocojoyPlatOrderInfo.this.getUsernameOctets();
		}

		@Override
		public <T extends com.goldhuman.Common.Marshal.Marshal> T getVars(T _v_) { // 
			_xdb_verify_unsafe_();
			return LocojoyPlatOrderInfo.this.getVars(_v_);
		}

		@Override
		public boolean isVarsEmpty() { // 
			_xdb_verify_unsafe_();
			return LocojoyPlatOrderInfo.this.isVarsEmpty();
		}

		@Override
		public byte[] getVarsCopy() { // 
			_xdb_verify_unsafe_();
			return LocojoyPlatOrderInfo.this.getVarsCopy();
		}

		@Override
		public void setCreatetime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOrderserialplat(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOrderserialplatOctets(com.goldhuman.Common.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOrderserialgame(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOrderserialgameOctets(com.goldhuman.Common.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUsername(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUsernameOctets(com.goldhuman.Common.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setVars(com.goldhuman.Common.Marshal.Marshal _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setVarsCopy(byte[] _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public xdb.Bean toConst() {
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
			return LocojoyPlatOrderInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LocojoyPlatOrderInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public xdb.Bean xdbParent() {
			return LocojoyPlatOrderInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LocojoyPlatOrderInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LocojoyPlatOrderInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LocojoyPlatOrderInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LocojoyPlatOrderInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LocojoyPlatOrderInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return LocojoyPlatOrderInfo.this.toString();
		}

	}

	public static final class Data implements xbean.LocojoyPlatOrderInfo {
		private long createtime; // 
		private String orderserialplat; // 
		private String orderserialgame; // 
		private String username; // 
		private byte [] vars; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			orderserialplat = "";
			orderserialgame = "";
			username = "";
			vars = new byte[0];
		}

		Data(xbean.LocojoyPlatOrderInfo _o1_) {
			if (_o1_ instanceof LocojoyPlatOrderInfo) assign((LocojoyPlatOrderInfo)_o1_);
			else if (_o1_ instanceof LocojoyPlatOrderInfo.Data) assign((LocojoyPlatOrderInfo.Data)_o1_);
			else if (_o1_ instanceof LocojoyPlatOrderInfo.Const) assign(((LocojoyPlatOrderInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LocojoyPlatOrderInfo _o_) {
			createtime = _o_.createtime;
			orderserialplat = _o_.orderserialplat;
			orderserialgame = _o_.orderserialgame;
			username = _o_.username;
			vars = java.util.Arrays.copyOf(_o_.vars, _o_.vars.length);
		}

		private void assign(LocojoyPlatOrderInfo.Data _o_) {
			createtime = _o_.createtime;
			orderserialplat = _o_.orderserialplat;
			orderserialgame = _o_.orderserialgame;
			username = _o_.username;
			vars = java.util.Arrays.copyOf(_o_.vars, _o_.vars.length);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(createtime);
			_os_.marshal(orderserialplat, xdb.Const.IO_CHARSET);
			_os_.marshal(orderserialgame, xdb.Const.IO_CHARSET);
			_os_.marshal(username, xdb.Const.IO_CHARSET);
			_os_.marshal(vars);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			createtime = _os_.unmarshal_long();
			orderserialplat = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			orderserialgame = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			username = _os_.unmarshal_String(xdb.Const.IO_CHARSET);
			vars = _os_.unmarshal_bytes();
			return _os_;
		}

		@Override
		public xbean.LocojoyPlatOrderInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.LocojoyPlatOrderInfo toData() {
			return new Data(this);
		}

		public xbean.LocojoyPlatOrderInfo toBean() {
			return new LocojoyPlatOrderInfo(this, null, null);
		}

		@Override
		public xbean.LocojoyPlatOrderInfo toDataIf() {
			return this;
		}

		public xbean.LocojoyPlatOrderInfo toBeanIf() {
			return new LocojoyPlatOrderInfo(this, null, null);
		}

		// xdb.Bean interface. Data Unsupported
		public boolean xdbManaged() { throw new UnsupportedOperationException(); }
		public xdb.Bean xdbParent() { throw new UnsupportedOperationException(); }
		public String xdbVarname()  { throw new UnsupportedOperationException(); }
		public Long    xdbObjId()   { throw new UnsupportedOperationException(); }
		public xdb.Bean toConst()   { throw new UnsupportedOperationException(); }
		public boolean isConst()    { return false; }
		public boolean isData()     { return true; }

		@Override
		public long getCreatetime() { // 
			return createtime;
		}

		@Override
		public String getOrderserialplat() { // 
			return orderserialplat;
		}

		@Override
		public com.goldhuman.Common.Octets getOrderserialplatOctets() { // 
			return com.goldhuman.Common.Octets.wrap(getOrderserialplat(), xdb.Const.IO_CHARSET);
		}

		@Override
		public String getOrderserialgame() { // 
			return orderserialgame;
		}

		@Override
		public com.goldhuman.Common.Octets getOrderserialgameOctets() { // 
			return com.goldhuman.Common.Octets.wrap(getOrderserialgame(), xdb.Const.IO_CHARSET);
		}

		@Override
		public String getUsername() { // 
			return username;
		}

		@Override
		public com.goldhuman.Common.Octets getUsernameOctets() { // 
			return com.goldhuman.Common.Octets.wrap(getUsername(), xdb.Const.IO_CHARSET);
		}

		@Override
		public <T extends com.goldhuman.Common.Marshal.Marshal> T getVars(T _v_) { // 
			try {
				_v_.unmarshal(OctetsStream.wrap(com.goldhuman.Common.Octets.wrap(vars)));
				return _v_;
			} catch (MarshalException _e_) {
				throw new xio.MarshalError();
			}
		}

		@Override
		public boolean isVarsEmpty() { // 
			return vars.length == 0;
		}

		@Override
		public byte[] getVarsCopy() { // 
			return java.util.Arrays.copyOf(vars, vars.length);
		}

		@Override
		public void setCreatetime(long _v_) { // 
			createtime = _v_;
		}

		@Override
		public void setOrderserialplat(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			orderserialplat = _v_;
		}

		@Override
		public void setOrderserialplatOctets(com.goldhuman.Common.Octets _v_) { // 
			this.setOrderserialplat(_v_.getString(xdb.Const.IO_CHARSET));
		}

		@Override
		public void setOrderserialgame(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			orderserialgame = _v_;
		}

		@Override
		public void setOrderserialgameOctets(com.goldhuman.Common.Octets _v_) { // 
			this.setOrderserialgame(_v_.getString(xdb.Const.IO_CHARSET));
		}

		@Override
		public void setUsername(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			username = _v_;
		}

		@Override
		public void setUsernameOctets(com.goldhuman.Common.Octets _v_) { // 
			this.setUsername(_v_.getString(xdb.Const.IO_CHARSET));
		}

		@Override
		public void setVars(com.goldhuman.Common.Marshal.Marshal _v_) { // 
			vars = _v_.marshal(new OctetsStream()).getBytes();
		}

		@Override
		public void setVarsCopy(byte[] _v_) { // 
			vars = java.util.Arrays.copyOf(_v_, _v_.length);
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LocojoyPlatOrderInfo.Data)) return false;
			LocojoyPlatOrderInfo.Data _o_ = (LocojoyPlatOrderInfo.Data) _o1_;
			if (createtime != _o_.createtime) return false;
			if (!orderserialplat.equals(_o_.orderserialplat)) return false;
			if (!orderserialgame.equals(_o_.orderserialgame)) return false;
			if (!username.equals(_o_.username)) return false;
			if (!java.util.Arrays.equals(vars, _o_.vars)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += createtime;
			_h_ += orderserialplat.hashCode();
			_h_ += orderserialgame.hashCode();
			_h_ += username.hashCode();
			_h_ += java.util.Arrays.hashCode(vars);
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append("'").append(orderserialplat).append("'");
			_sb_.append(",");
			_sb_.append("'").append(orderserialgame).append("'");
			_sb_.append(",");
			_sb_.append("'").append(username).append("'");
			_sb_.append(",");
			_sb_.append('B').append(vars.length);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class OfflineMsg extends mkdb.XBean implements xbean.OfflineMsg {
	private long roleid; // 
	private String content; // 
	private java.util.LinkedList<byte []> details; // 展示品信息
	private java.util.LinkedList<xbean.ShowInfoBean> showinfos; // 展示品信息
	private String sendtime; // 

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		content = "";
		details.clear();
		showinfos.clear();
		sendtime = "";
	}

	OfflineMsg(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		content = "";
		details = new java.util.LinkedList<byte []>();
		showinfos = new java.util.LinkedList<xbean.ShowInfoBean>();
		sendtime = "";
	}

	public OfflineMsg() {
		this(0, null, null);
	}

	public OfflineMsg(OfflineMsg _o_) {
		this(_o_, null, null);
	}

	OfflineMsg(xbean.OfflineMsg _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof OfflineMsg) assign((OfflineMsg)_o1_);
		else if (_o1_ instanceof OfflineMsg.Data) assign((OfflineMsg.Data)_o1_);
		else if (_o1_ instanceof OfflineMsg.Const) assign(((OfflineMsg.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(OfflineMsg _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		content = _o_.content;
		details = new java.util.LinkedList<byte []>();
		for (byte [] _v_ : _o_.details)
			details.add(java.util.Arrays.copyOf(_v_, _v_.length));
		showinfos = new java.util.LinkedList<xbean.ShowInfoBean>();
		for (xbean.ShowInfoBean _v_ : _o_.showinfos)
			showinfos.add(new ShowInfoBean(_v_, this, "showinfos"));
		sendtime = _o_.sendtime;
	}

	private void assign(OfflineMsg.Data _o_) {
		roleid = _o_.roleid;
		content = _o_.content;
		details = new java.util.LinkedList<byte []>();
		for (byte [] _v_ : _o_.details)
			details.add(java.util.Arrays.copyOf(_v_, _v_.length));
		showinfos = new java.util.LinkedList<xbean.ShowInfoBean>();
		for (xbean.ShowInfoBean _v_ : _o_.showinfos)
			showinfos.add(new ShowInfoBean(_v_, this, "showinfos"));
		sendtime = _o_.sendtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(content, mkdb.Const.IO_CHARSET);
		_os_.compact_uint32(details.size());
		for (byte [] _v_ : details) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(showinfos.size());
		for (xbean.ShowInfoBean _v_ : showinfos) {
			_v_.marshal(_os_);
		}
		_os_.marshal(sendtime, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		content = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte [] _v_ = new byte [0];
			_v_ = _os_.unmarshal_bytes();
			details.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ShowInfoBean _v_ = new ShowInfoBean(0, this, "showinfos");
			_v_.unmarshal(_os_);
			showinfos.add(_v_);
		}
		sendtime = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.OfflineMsg copy() {
		_xdb_verify_unsafe_();
		return new OfflineMsg(this);
	}

	@Override
	public xbean.OfflineMsg toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OfflineMsg toBean() {
		_xdb_verify_unsafe_();
		return new OfflineMsg(this); // same as copy()
	}

	@Override
	public xbean.OfflineMsg toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OfflineMsg toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public String getContent() { // 
		_xdb_verify_unsafe_();
		return content;
	}

	@Override
	public com.locojoy.base.Octets getContentOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getContent(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public java.util.List<byte []> getDetails() { // 展示品信息
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "details"), details);
	}

	public java.util.List<byte []> getDetailsAsData() { // 展示品信息
		_xdb_verify_unsafe_();
		java.util.List<byte []> details;
		OfflineMsg _o_ = this;
		details = new java.util.LinkedList<byte []>();
		for (byte [] _v_ : _o_.details)
			details.add(java.util.Arrays.copyOf(_v_, _v_.length));
		return details;
	}

	@Override
	public java.util.List<xbean.ShowInfoBean> getShowinfos() { // 展示品信息
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "showinfos"), showinfos);
	}

	public java.util.List<xbean.ShowInfoBean> getShowinfosAsData() { // 展示品信息
		_xdb_verify_unsafe_();
		java.util.List<xbean.ShowInfoBean> showinfos;
		OfflineMsg _o_ = this;
		showinfos = new java.util.LinkedList<xbean.ShowInfoBean>();
		for (xbean.ShowInfoBean _v_ : _o_.showinfos)
			showinfos.add(new ShowInfoBean.Data(_v_));
		return showinfos;
	}

	@Override
	public String getSendtime() { // 
		_xdb_verify_unsafe_();
		return sendtime;
	}

	@Override
	public com.locojoy.base.Octets getSendtimeOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getSendtime(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setRoleid(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setContent(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "content") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, content) {
					public void rollback() { content = _xdb_saved; }
				};}});
		content = _v_;
	}

	@Override
	public void setContentOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setContent(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setSendtime(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, sendtime) {
					public void rollback() { sendtime = _xdb_saved; }
				};}});
		sendtime = _v_;
	}

	@Override
	public void setSendtimeOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setSendtime(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		OfflineMsg _o_ = null;
		if ( _o1_ instanceof OfflineMsg ) _o_ = (OfflineMsg)_o1_;
		else if ( _o1_ instanceof OfflineMsg.Const ) _o_ = ((OfflineMsg.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!content.equals(_o_.content)) return false;
		if (!details.equals(_o_.details)) return false;
		if (!showinfos.equals(_o_.showinfos)) return false;
		if (!sendtime.equals(_o_.sendtime)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += content.hashCode();
		_h_ += details.hashCode();
		_h_ += showinfos.hashCode();
		_h_ += sendtime.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append("'").append(content).append("'");
		_sb_.append(",");
		_sb_.append(details);
		_sb_.append(",");
		_sb_.append(showinfos);
		_sb_.append(",");
		_sb_.append("'").append(sendtime).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("content"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("details"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("showinfos"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendtime"));
		return lb;
	}

	private class Const implements xbean.OfflineMsg {
		OfflineMsg nThis() {
			return OfflineMsg.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.OfflineMsg copy() {
			return OfflineMsg.this.copy();
		}

		@Override
		public xbean.OfflineMsg toData() {
			return OfflineMsg.this.toData();
		}

		public xbean.OfflineMsg toBean() {
			return OfflineMsg.this.toBean();
		}

		@Override
		public xbean.OfflineMsg toDataIf() {
			return OfflineMsg.this.toDataIf();
		}

		public xbean.OfflineMsg toBeanIf() {
			return OfflineMsg.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getContent() { // 
			_xdb_verify_unsafe_();
			return content;
		}

		@Override
		public com.locojoy.base.Octets getContentOctets() { // 
			_xdb_verify_unsafe_();
			return OfflineMsg.this.getContentOctets();
		}

		@Override
		public java.util.List<byte []> getDetails() { // 展示品信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(details);
		}

		public java.util.List<byte []> getDetailsAsData() { // 展示品信息
			_xdb_verify_unsafe_();
			java.util.List<byte []> details;
			OfflineMsg _o_ = OfflineMsg.this;
		details = new java.util.LinkedList<byte []>();
		for (byte [] _v_ : _o_.details)
			details.add(java.util.Arrays.copyOf(_v_, _v_.length));
			return details;
		}

		@Override
		public java.util.List<xbean.ShowInfoBean> getShowinfos() { // 展示品信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(showinfos);
		}

		public java.util.List<xbean.ShowInfoBean> getShowinfosAsData() { // 展示品信息
			_xdb_verify_unsafe_();
			java.util.List<xbean.ShowInfoBean> showinfos;
			OfflineMsg _o_ = OfflineMsg.this;
		showinfos = new java.util.LinkedList<xbean.ShowInfoBean>();
		for (xbean.ShowInfoBean _v_ : _o_.showinfos)
			showinfos.add(new ShowInfoBean.Data(_v_));
			return showinfos;
		}

		@Override
		public String getSendtime() { // 
			_xdb_verify_unsafe_();
			return sendtime;
		}

		@Override
		public com.locojoy.base.Octets getSendtimeOctets() { // 
			_xdb_verify_unsafe_();
			return OfflineMsg.this.getSendtimeOctets();
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContent(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContentOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSendtime(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSendtimeOctets(com.locojoy.base.Octets _v_) { // 
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
			return OfflineMsg.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return OfflineMsg.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return OfflineMsg.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return OfflineMsg.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return OfflineMsg.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return OfflineMsg.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return OfflineMsg.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return OfflineMsg.this.hashCode();
		}

		@Override
		public String toString() {
			return OfflineMsg.this.toString();
		}

	}

	public static final class Data implements xbean.OfflineMsg {
		private long roleid; // 
		private String content; // 
		private java.util.LinkedList<byte []> details; // 展示品信息
		private java.util.LinkedList<xbean.ShowInfoBean> showinfos; // 展示品信息
		private String sendtime; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			content = "";
			details = new java.util.LinkedList<byte []>();
			showinfos = new java.util.LinkedList<xbean.ShowInfoBean>();
			sendtime = "";
		}

		Data(xbean.OfflineMsg _o1_) {
			if (_o1_ instanceof OfflineMsg) assign((OfflineMsg)_o1_);
			else if (_o1_ instanceof OfflineMsg.Data) assign((OfflineMsg.Data)_o1_);
			else if (_o1_ instanceof OfflineMsg.Const) assign(((OfflineMsg.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(OfflineMsg _o_) {
			roleid = _o_.roleid;
			content = _o_.content;
			details = new java.util.LinkedList<byte []>();
			for (byte [] _v_ : _o_.details)
				details.add(java.util.Arrays.copyOf(_v_, _v_.length));
			showinfos = new java.util.LinkedList<xbean.ShowInfoBean>();
			for (xbean.ShowInfoBean _v_ : _o_.showinfos)
				showinfos.add(new ShowInfoBean.Data(_v_));
			sendtime = _o_.sendtime;
		}

		private void assign(OfflineMsg.Data _o_) {
			roleid = _o_.roleid;
			content = _o_.content;
			details = new java.util.LinkedList<byte []>();
			for (byte [] _v_ : _o_.details)
				details.add(java.util.Arrays.copyOf(_v_, _v_.length));
			showinfos = new java.util.LinkedList<xbean.ShowInfoBean>();
			for (xbean.ShowInfoBean _v_ : _o_.showinfos)
				showinfos.add(new ShowInfoBean.Data(_v_));
			sendtime = _o_.sendtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(content, mkdb.Const.IO_CHARSET);
			_os_.compact_uint32(details.size());
			for (byte [] _v_ : details) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(showinfos.size());
			for (xbean.ShowInfoBean _v_ : showinfos) {
				_v_.marshal(_os_);
			}
			_os_.marshal(sendtime, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			content = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				byte [] _v_ = new byte [0];
				_v_ = _os_.unmarshal_bytes();
				details.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ShowInfoBean _v_ = xbean.Pod.newShowInfoBeanData();
				_v_.unmarshal(_os_);
				showinfos.add(_v_);
			}
			sendtime = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.OfflineMsg copy() {
			return new Data(this);
		}

		@Override
		public xbean.OfflineMsg toData() {
			return new Data(this);
		}

		public xbean.OfflineMsg toBean() {
			return new OfflineMsg(this, null, null);
		}

		@Override
		public xbean.OfflineMsg toDataIf() {
			return this;
		}

		public xbean.OfflineMsg toBeanIf() {
			return new OfflineMsg(this, null, null);
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
		public long getRoleid() { // 
			return roleid;
		}

		@Override
		public String getContent() { // 
			return content;
		}

		@Override
		public com.locojoy.base.Octets getContentOctets() { // 
			return com.locojoy.base.Octets.wrap(getContent(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public java.util.List<byte []> getDetails() { // 展示品信息
			return details;
		}

		@Override
		public java.util.List<byte []> getDetailsAsData() { // 展示品信息
			return details;
		}

		@Override
		public java.util.List<xbean.ShowInfoBean> getShowinfos() { // 展示品信息
			return showinfos;
		}

		@Override
		public java.util.List<xbean.ShowInfoBean> getShowinfosAsData() { // 展示品信息
			return showinfos;
		}

		@Override
		public String getSendtime() { // 
			return sendtime;
		}

		@Override
		public com.locojoy.base.Octets getSendtimeOctets() { // 
			return com.locojoy.base.Octets.wrap(getSendtime(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setContent(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			content = _v_;
		}

		@Override
		public void setContentOctets(com.locojoy.base.Octets _v_) { // 
			this.setContent(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setSendtime(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			sendtime = _v_;
		}

		@Override
		public void setSendtimeOctets(com.locojoy.base.Octets _v_) { // 
			this.setSendtime(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof OfflineMsg.Data)) return false;
			OfflineMsg.Data _o_ = (OfflineMsg.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!content.equals(_o_.content)) return false;
			if (!details.equals(_o_.details)) return false;
			if (!showinfos.equals(_o_.showinfos)) return false;
			if (!sendtime.equals(_o_.sendtime)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += content.hashCode();
			_h_ += details.hashCode();
			_h_ += showinfos.hashCode();
			_h_ += sendtime.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append("'").append(content).append("'");
			_sb_.append(",");
			_sb_.append(details);
			_sb_.append(",");
			_sb_.append(showinfos);
			_sb_.append(",");
			_sb_.append("'").append(sendtime).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

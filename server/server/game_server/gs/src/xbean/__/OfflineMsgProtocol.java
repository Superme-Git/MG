
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class OfflineMsgProtocol extends mkdb.XBean implements xbean.OfflineMsgProtocol {
	private int protype; // 
	private byte [] content; // 
	private String proclassname; // 
	private long tick; // 插入到离线协议的时间

	@Override
	public void _reset_unsafe_() {
		protype = 0;
		content = new byte[0];
		proclassname = "";
		tick = 0L;
	}

	OfflineMsgProtocol(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		content = new byte[0];
		proclassname = "";
	}

	public OfflineMsgProtocol() {
		this(0, null, null);
	}

	public OfflineMsgProtocol(OfflineMsgProtocol _o_) {
		this(_o_, null, null);
	}

	OfflineMsgProtocol(xbean.OfflineMsgProtocol _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof OfflineMsgProtocol) assign((OfflineMsgProtocol)_o1_);
		else if (_o1_ instanceof OfflineMsgProtocol.Data) assign((OfflineMsgProtocol.Data)_o1_);
		else if (_o1_ instanceof OfflineMsgProtocol.Const) assign(((OfflineMsgProtocol.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(OfflineMsgProtocol _o_) {
		_o_._xdb_verify_unsafe_();
		protype = _o_.protype;
		content = java.util.Arrays.copyOf(_o_.content, _o_.content.length);
		proclassname = _o_.proclassname;
		tick = _o_.tick;
	}

	private void assign(OfflineMsgProtocol.Data _o_) {
		protype = _o_.protype;
		content = java.util.Arrays.copyOf(_o_.content, _o_.content.length);
		proclassname = _o_.proclassname;
		tick = _o_.tick;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(protype);
		_os_.marshal(content);
		_os_.marshal(proclassname, mkdb.Const.IO_CHARSET);
		_os_.marshal(tick);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		protype = _os_.unmarshal_int();
		content = _os_.unmarshal_bytes();
		proclassname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		tick = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.OfflineMsgProtocol copy() {
		_xdb_verify_unsafe_();
		return new OfflineMsgProtocol(this);
	}

	@Override
	public xbean.OfflineMsgProtocol toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OfflineMsgProtocol toBean() {
		_xdb_verify_unsafe_();
		return new OfflineMsgProtocol(this); // same as copy()
	}

	@Override
	public xbean.OfflineMsgProtocol toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OfflineMsgProtocol toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getProtype() { // 
		_xdb_verify_unsafe_();
		return protype;
	}

	@Override
	public <T extends com.locojoy.base.Marshal.Marshal> T getContent(T _v_) { // 
		_xdb_verify_unsafe_();
		try {
			_v_.unmarshal(OctetsStream.wrap(com.locojoy.base.Octets.wrap(content)));
			return _v_;
		} catch (MarshalException _e_) {
			throw new mkio.MarshalError();
		}
	}

	@Override
	public boolean isContentEmpty() { // 
		_xdb_verify_unsafe_();
		return content.length == 0;
	}

	@Override
	public byte[] getContentCopy() { // 
		_xdb_verify_unsafe_();
		return java.util.Arrays.copyOf(content, content.length);
	}

	@Override
	public String getProclassname() { // 
		_xdb_verify_unsafe_();
		return proclassname;
	}

	@Override
	public com.locojoy.base.Octets getProclassnameOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getProclassname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getTick() { // 插入到离线协议的时间
		_xdb_verify_unsafe_();
		return tick;
	}

	@Override
	public void setProtype(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "protype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, protype) {
					public void rollback() { protype = _xdb_saved; }
				};}});
		protype = _v_;
	}

	@Override
	public void setContent(com.locojoy.base.Marshal.Marshal _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "content") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<byte []>(this, content) {
					public void rollback() { content = _xdb_saved; }
			}; }});
		content = _v_.marshal(new OctetsStream()).getBytes();
	}

	@Override
	public void setContentCopy(byte[] _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "content") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<byte []>(this, content) {
					public void rollback() { content = _xdb_saved; }
			}; }});
		content = java.util.Arrays.copyOf(_v_, _v_.length);
	}

	@Override
	public void setProclassname(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "proclassname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, proclassname) {
					public void rollback() { proclassname = _xdb_saved; }
				};}});
		proclassname = _v_;
	}

	@Override
	public void setProclassnameOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setProclassname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setTick(long _v_) { // 插入到离线协议的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tick") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, tick) {
					public void rollback() { tick = _xdb_saved; }
				};}});
		tick = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		OfflineMsgProtocol _o_ = null;
		if ( _o1_ instanceof OfflineMsgProtocol ) _o_ = (OfflineMsgProtocol)_o1_;
		else if ( _o1_ instanceof OfflineMsgProtocol.Const ) _o_ = ((OfflineMsgProtocol.Const)_o1_).nThis();
		else return false;
		if (protype != _o_.protype) return false;
		if (!java.util.Arrays.equals(content, _o_.content)) return false;
		if (!proclassname.equals(_o_.proclassname)) return false;
		if (tick != _o_.tick) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += protype;
		_h_ += java.util.Arrays.hashCode(content);
		_h_ += proclassname.hashCode();
		_h_ += tick;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(protype);
		_sb_.append(",");
		_sb_.append('B').append(content.length);
		_sb_.append(",");
		_sb_.append("'").append(proclassname).append("'");
		_sb_.append(",");
		_sb_.append(tick);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("protype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("content"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("proclassname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tick"));
		return lb;
	}

	private class Const implements xbean.OfflineMsgProtocol {
		OfflineMsgProtocol nThis() {
			return OfflineMsgProtocol.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.OfflineMsgProtocol copy() {
			return OfflineMsgProtocol.this.copy();
		}

		@Override
		public xbean.OfflineMsgProtocol toData() {
			return OfflineMsgProtocol.this.toData();
		}

		public xbean.OfflineMsgProtocol toBean() {
			return OfflineMsgProtocol.this.toBean();
		}

		@Override
		public xbean.OfflineMsgProtocol toDataIf() {
			return OfflineMsgProtocol.this.toDataIf();
		}

		public xbean.OfflineMsgProtocol toBeanIf() {
			return OfflineMsgProtocol.this.toBeanIf();
		}

		@Override
		public int getProtype() { // 
			_xdb_verify_unsafe_();
			return protype;
		}

		@Override
		public <T extends com.locojoy.base.Marshal.Marshal> T getContent(T _v_) { // 
			_xdb_verify_unsafe_();
			return OfflineMsgProtocol.this.getContent(_v_);
		}

		@Override
		public boolean isContentEmpty() { // 
			_xdb_verify_unsafe_();
			return OfflineMsgProtocol.this.isContentEmpty();
		}

		@Override
		public byte[] getContentCopy() { // 
			_xdb_verify_unsafe_();
			return OfflineMsgProtocol.this.getContentCopy();
		}

		@Override
		public String getProclassname() { // 
			_xdb_verify_unsafe_();
			return proclassname;
		}

		@Override
		public com.locojoy.base.Octets getProclassnameOctets() { // 
			_xdb_verify_unsafe_();
			return OfflineMsgProtocol.this.getProclassnameOctets();
		}

		@Override
		public long getTick() { // 插入到离线协议的时间
			_xdb_verify_unsafe_();
			return tick;
		}

		@Override
		public void setProtype(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContent(com.locojoy.base.Marshal.Marshal _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setContentCopy(byte[] _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setProclassname(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setProclassnameOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTick(long _v_) { // 插入到离线协议的时间
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
			return OfflineMsgProtocol.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return OfflineMsgProtocol.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return OfflineMsgProtocol.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return OfflineMsgProtocol.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return OfflineMsgProtocol.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return OfflineMsgProtocol.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return OfflineMsgProtocol.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return OfflineMsgProtocol.this.hashCode();
		}

		@Override
		public String toString() {
			return OfflineMsgProtocol.this.toString();
		}

	}

	public static final class Data implements xbean.OfflineMsgProtocol {
		private int protype; // 
		private byte [] content; // 
		private String proclassname; // 
		private long tick; // 插入到离线协议的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			content = new byte[0];
			proclassname = "";
		}

		Data(xbean.OfflineMsgProtocol _o1_) {
			if (_o1_ instanceof OfflineMsgProtocol) assign((OfflineMsgProtocol)_o1_);
			else if (_o1_ instanceof OfflineMsgProtocol.Data) assign((OfflineMsgProtocol.Data)_o1_);
			else if (_o1_ instanceof OfflineMsgProtocol.Const) assign(((OfflineMsgProtocol.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(OfflineMsgProtocol _o_) {
			protype = _o_.protype;
			content = java.util.Arrays.copyOf(_o_.content, _o_.content.length);
			proclassname = _o_.proclassname;
			tick = _o_.tick;
		}

		private void assign(OfflineMsgProtocol.Data _o_) {
			protype = _o_.protype;
			content = java.util.Arrays.copyOf(_o_.content, _o_.content.length);
			proclassname = _o_.proclassname;
			tick = _o_.tick;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(protype);
			_os_.marshal(content);
			_os_.marshal(proclassname, mkdb.Const.IO_CHARSET);
			_os_.marshal(tick);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			protype = _os_.unmarshal_int();
			content = _os_.unmarshal_bytes();
			proclassname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			tick = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.OfflineMsgProtocol copy() {
			return new Data(this);
		}

		@Override
		public xbean.OfflineMsgProtocol toData() {
			return new Data(this);
		}

		public xbean.OfflineMsgProtocol toBean() {
			return new OfflineMsgProtocol(this, null, null);
		}

		@Override
		public xbean.OfflineMsgProtocol toDataIf() {
			return this;
		}

		public xbean.OfflineMsgProtocol toBeanIf() {
			return new OfflineMsgProtocol(this, null, null);
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
		public int getProtype() { // 
			return protype;
		}

		@Override
		public <T extends com.locojoy.base.Marshal.Marshal> T getContent(T _v_) { // 
			try {
				_v_.unmarshal(OctetsStream.wrap(com.locojoy.base.Octets.wrap(content)));
				return _v_;
			} catch (MarshalException _e_) {
				throw new mkio.MarshalError();
			}
		}

		@Override
		public boolean isContentEmpty() { // 
			return content.length == 0;
		}

		@Override
		public byte[] getContentCopy() { // 
			return java.util.Arrays.copyOf(content, content.length);
		}

		@Override
		public String getProclassname() { // 
			return proclassname;
		}

		@Override
		public com.locojoy.base.Octets getProclassnameOctets() { // 
			return com.locojoy.base.Octets.wrap(getProclassname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getTick() { // 插入到离线协议的时间
			return tick;
		}

		@Override
		public void setProtype(int _v_) { // 
			protype = _v_;
		}

		@Override
		public void setContent(com.locojoy.base.Marshal.Marshal _v_) { // 
			content = _v_.marshal(new OctetsStream()).getBytes();
		}

		@Override
		public void setContentCopy(byte[] _v_) { // 
			content = java.util.Arrays.copyOf(_v_, _v_.length);
		}

		@Override
		public void setProclassname(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			proclassname = _v_;
		}

		@Override
		public void setProclassnameOctets(com.locojoy.base.Octets _v_) { // 
			this.setProclassname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setTick(long _v_) { // 插入到离线协议的时间
			tick = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof OfflineMsgProtocol.Data)) return false;
			OfflineMsgProtocol.Data _o_ = (OfflineMsgProtocol.Data) _o1_;
			if (protype != _o_.protype) return false;
			if (!java.util.Arrays.equals(content, _o_.content)) return false;
			if (!proclassname.equals(_o_.proclassname)) return false;
			if (tick != _o_.tick) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += protype;
			_h_ += java.util.Arrays.hashCode(content);
			_h_ += proclassname.hashCode();
			_h_ += tick;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(protype);
			_sb_.append(",");
			_sb_.append('B').append(content.length);
			_sb_.append(",");
			_sb_.append("'").append(proclassname).append("'");
			_sb_.append(",");
			_sb_.append(tick);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

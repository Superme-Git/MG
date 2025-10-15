
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TicketItemExInfo extends mkdb.XBean implements xbean.TicketItemExInfo {
	private String ticketcode; // 

	@Override
	public void _reset_unsafe_() {
		ticketcode = "";
	}

	TicketItemExInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		ticketcode = "";
	}

	public TicketItemExInfo() {
		this(0, null, null);
	}

	public TicketItemExInfo(TicketItemExInfo _o_) {
		this(_o_, null, null);
	}

	TicketItemExInfo(xbean.TicketItemExInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TicketItemExInfo) assign((TicketItemExInfo)_o1_);
		else if (_o1_ instanceof TicketItemExInfo.Data) assign((TicketItemExInfo.Data)_o1_);
		else if (_o1_ instanceof TicketItemExInfo.Const) assign(((TicketItemExInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TicketItemExInfo _o_) {
		_o_._xdb_verify_unsafe_();
		ticketcode = _o_.ticketcode;
	}

	private void assign(TicketItemExInfo.Data _o_) {
		ticketcode = _o_.ticketcode;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(ticketcode, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		ticketcode = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.TicketItemExInfo copy() {
		_xdb_verify_unsafe_();
		return new TicketItemExInfo(this);
	}

	@Override
	public xbean.TicketItemExInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TicketItemExInfo toBean() {
		_xdb_verify_unsafe_();
		return new TicketItemExInfo(this); // same as copy()
	}

	@Override
	public xbean.TicketItemExInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TicketItemExInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getTicketcode() { // 
		_xdb_verify_unsafe_();
		return ticketcode;
	}

	@Override
	public com.locojoy.base.Octets getTicketcodeOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getTicketcode(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setTicketcode(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ticketcode") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, ticketcode) {
					public void rollback() { ticketcode = _xdb_saved; }
				};}});
		ticketcode = _v_;
	}

	@Override
	public void setTicketcodeOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setTicketcode(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TicketItemExInfo _o_ = null;
		if ( _o1_ instanceof TicketItemExInfo ) _o_ = (TicketItemExInfo)_o1_;
		else if ( _o1_ instanceof TicketItemExInfo.Const ) _o_ = ((TicketItemExInfo.Const)_o1_).nThis();
		else return false;
		if (!ticketcode.equals(_o_.ticketcode)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += ticketcode.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(ticketcode).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ticketcode"));
		return lb;
	}

	private class Const implements xbean.TicketItemExInfo {
		TicketItemExInfo nThis() {
			return TicketItemExInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TicketItemExInfo copy() {
			return TicketItemExInfo.this.copy();
		}

		@Override
		public xbean.TicketItemExInfo toData() {
			return TicketItemExInfo.this.toData();
		}

		public xbean.TicketItemExInfo toBean() {
			return TicketItemExInfo.this.toBean();
		}

		@Override
		public xbean.TicketItemExInfo toDataIf() {
			return TicketItemExInfo.this.toDataIf();
		}

		public xbean.TicketItemExInfo toBeanIf() {
			return TicketItemExInfo.this.toBeanIf();
		}

		@Override
		public String getTicketcode() { // 
			_xdb_verify_unsafe_();
			return ticketcode;
		}

		@Override
		public com.locojoy.base.Octets getTicketcodeOctets() { // 
			_xdb_verify_unsafe_();
			return TicketItemExInfo.this.getTicketcodeOctets();
		}

		@Override
		public void setTicketcode(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTicketcodeOctets(com.locojoy.base.Octets _v_) { // 
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
			return TicketItemExInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TicketItemExInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TicketItemExInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TicketItemExInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TicketItemExInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TicketItemExInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TicketItemExInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TicketItemExInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return TicketItemExInfo.this.toString();
		}

	}

	public static final class Data implements xbean.TicketItemExInfo {
		private String ticketcode; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			ticketcode = "";
		}

		Data(xbean.TicketItemExInfo _o1_) {
			if (_o1_ instanceof TicketItemExInfo) assign((TicketItemExInfo)_o1_);
			else if (_o1_ instanceof TicketItemExInfo.Data) assign((TicketItemExInfo.Data)_o1_);
			else if (_o1_ instanceof TicketItemExInfo.Const) assign(((TicketItemExInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TicketItemExInfo _o_) {
			ticketcode = _o_.ticketcode;
		}

		private void assign(TicketItemExInfo.Data _o_) {
			ticketcode = _o_.ticketcode;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(ticketcode, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			ticketcode = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.TicketItemExInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.TicketItemExInfo toData() {
			return new Data(this);
		}

		public xbean.TicketItemExInfo toBean() {
			return new TicketItemExInfo(this, null, null);
		}

		@Override
		public xbean.TicketItemExInfo toDataIf() {
			return this;
		}

		public xbean.TicketItemExInfo toBeanIf() {
			return new TicketItemExInfo(this, null, null);
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
		public String getTicketcode() { // 
			return ticketcode;
		}

		@Override
		public com.locojoy.base.Octets getTicketcodeOctets() { // 
			return com.locojoy.base.Octets.wrap(getTicketcode(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setTicketcode(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			ticketcode = _v_;
		}

		@Override
		public void setTicketcodeOctets(com.locojoy.base.Octets _v_) { // 
			this.setTicketcode(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TicketItemExInfo.Data)) return false;
			TicketItemExInfo.Data _o_ = (TicketItemExInfo.Data) _o1_;
			if (!ticketcode.equals(_o_.ticketcode)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += ticketcode.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(ticketcode).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

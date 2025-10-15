
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BroadcastMsg extends mkdb.XBean implements xbean.BroadcastMsg {
	private xbean.OfflineMsgProtocol msgprotocol; // 
	private long broadtime; // 
	private long broadendtime; // 如果默认是0，则没有广播的结束时间

	@Override
	public void _reset_unsafe_() {
		msgprotocol._reset_unsafe_();
		broadtime = 0L;
		broadendtime = 0L;
	}

	BroadcastMsg(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		msgprotocol = new OfflineMsgProtocol(0, this, "msgprotocol");
	}

	public BroadcastMsg() {
		this(0, null, null);
	}

	public BroadcastMsg(BroadcastMsg _o_) {
		this(_o_, null, null);
	}

	BroadcastMsg(xbean.BroadcastMsg _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BroadcastMsg) assign((BroadcastMsg)_o1_);
		else if (_o1_ instanceof BroadcastMsg.Data) assign((BroadcastMsg.Data)_o1_);
		else if (_o1_ instanceof BroadcastMsg.Const) assign(((BroadcastMsg.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BroadcastMsg _o_) {
		_o_._xdb_verify_unsafe_();
		msgprotocol = new OfflineMsgProtocol(_o_.msgprotocol, this, "msgprotocol");
		broadtime = _o_.broadtime;
		broadendtime = _o_.broadendtime;
	}

	private void assign(BroadcastMsg.Data _o_) {
		msgprotocol = new OfflineMsgProtocol(_o_.msgprotocol, this, "msgprotocol");
		broadtime = _o_.broadtime;
		broadendtime = _o_.broadendtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		msgprotocol.marshal(_os_);
		_os_.marshal(broadtime);
		_os_.marshal(broadendtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		msgprotocol.unmarshal(_os_);
		broadtime = _os_.unmarshal_long();
		broadendtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.BroadcastMsg copy() {
		_xdb_verify_unsafe_();
		return new BroadcastMsg(this);
	}

	@Override
	public xbean.BroadcastMsg toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BroadcastMsg toBean() {
		_xdb_verify_unsafe_();
		return new BroadcastMsg(this); // same as copy()
	}

	@Override
	public xbean.BroadcastMsg toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BroadcastMsg toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public xbean.OfflineMsgProtocol getMsgprotocol() { // 
		_xdb_verify_unsafe_();
		return msgprotocol;
	}

	@Override
	public long getBroadtime() { // 
		_xdb_verify_unsafe_();
		return broadtime;
	}

	@Override
	public long getBroadendtime() { // 如果默认是0，则没有广播的结束时间
		_xdb_verify_unsafe_();
		return broadendtime;
	}

	@Override
	public void setBroadtime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "broadtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, broadtime) {
					public void rollback() { broadtime = _xdb_saved; }
				};}});
		broadtime = _v_;
	}

	@Override
	public void setBroadendtime(long _v_) { // 如果默认是0，则没有广播的结束时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "broadendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, broadendtime) {
					public void rollback() { broadendtime = _xdb_saved; }
				};}});
		broadendtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BroadcastMsg _o_ = null;
		if ( _o1_ instanceof BroadcastMsg ) _o_ = (BroadcastMsg)_o1_;
		else if ( _o1_ instanceof BroadcastMsg.Const ) _o_ = ((BroadcastMsg.Const)_o1_).nThis();
		else return false;
		if (!msgprotocol.equals(_o_.msgprotocol)) return false;
		if (broadtime != _o_.broadtime) return false;
		if (broadendtime != _o_.broadendtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += msgprotocol.hashCode();
		_h_ += broadtime;
		_h_ += broadendtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(msgprotocol);
		_sb_.append(",");
		_sb_.append(broadtime);
		_sb_.append(",");
		_sb_.append(broadendtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("msgprotocol"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("broadtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("broadendtime"));
		return lb;
	}

	private class Const implements xbean.BroadcastMsg {
		BroadcastMsg nThis() {
			return BroadcastMsg.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BroadcastMsg copy() {
			return BroadcastMsg.this.copy();
		}

		@Override
		public xbean.BroadcastMsg toData() {
			return BroadcastMsg.this.toData();
		}

		public xbean.BroadcastMsg toBean() {
			return BroadcastMsg.this.toBean();
		}

		@Override
		public xbean.BroadcastMsg toDataIf() {
			return BroadcastMsg.this.toDataIf();
		}

		public xbean.BroadcastMsg toBeanIf() {
			return BroadcastMsg.this.toBeanIf();
		}

		@Override
		public xbean.OfflineMsgProtocol getMsgprotocol() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(msgprotocol);
		}

		@Override
		public long getBroadtime() { // 
			_xdb_verify_unsafe_();
			return broadtime;
		}

		@Override
		public long getBroadendtime() { // 如果默认是0，则没有广播的结束时间
			_xdb_verify_unsafe_();
			return broadendtime;
		}

		@Override
		public void setBroadtime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBroadendtime(long _v_) { // 如果默认是0，则没有广播的结束时间
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
			return BroadcastMsg.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BroadcastMsg.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BroadcastMsg.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BroadcastMsg.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BroadcastMsg.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BroadcastMsg.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BroadcastMsg.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BroadcastMsg.this.hashCode();
		}

		@Override
		public String toString() {
			return BroadcastMsg.this.toString();
		}

	}

	public static final class Data implements xbean.BroadcastMsg {
		private xbean.OfflineMsgProtocol msgprotocol; // 
		private long broadtime; // 
		private long broadendtime; // 如果默认是0，则没有广播的结束时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			msgprotocol = new OfflineMsgProtocol.Data();
		}

		Data(xbean.BroadcastMsg _o1_) {
			if (_o1_ instanceof BroadcastMsg) assign((BroadcastMsg)_o1_);
			else if (_o1_ instanceof BroadcastMsg.Data) assign((BroadcastMsg.Data)_o1_);
			else if (_o1_ instanceof BroadcastMsg.Const) assign(((BroadcastMsg.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BroadcastMsg _o_) {
			msgprotocol = new OfflineMsgProtocol.Data(_o_.msgprotocol);
			broadtime = _o_.broadtime;
			broadendtime = _o_.broadendtime;
		}

		private void assign(BroadcastMsg.Data _o_) {
			msgprotocol = new OfflineMsgProtocol.Data(_o_.msgprotocol);
			broadtime = _o_.broadtime;
			broadendtime = _o_.broadendtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			msgprotocol.marshal(_os_);
			_os_.marshal(broadtime);
			_os_.marshal(broadendtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			msgprotocol.unmarshal(_os_);
			broadtime = _os_.unmarshal_long();
			broadendtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.BroadcastMsg copy() {
			return new Data(this);
		}

		@Override
		public xbean.BroadcastMsg toData() {
			return new Data(this);
		}

		public xbean.BroadcastMsg toBean() {
			return new BroadcastMsg(this, null, null);
		}

		@Override
		public xbean.BroadcastMsg toDataIf() {
			return this;
		}

		public xbean.BroadcastMsg toBeanIf() {
			return new BroadcastMsg(this, null, null);
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
		public xbean.OfflineMsgProtocol getMsgprotocol() { // 
			return msgprotocol;
		}

		@Override
		public long getBroadtime() { // 
			return broadtime;
		}

		@Override
		public long getBroadendtime() { // 如果默认是0，则没有广播的结束时间
			return broadendtime;
		}

		@Override
		public void setBroadtime(long _v_) { // 
			broadtime = _v_;
		}

		@Override
		public void setBroadendtime(long _v_) { // 如果默认是0，则没有广播的结束时间
			broadendtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BroadcastMsg.Data)) return false;
			BroadcastMsg.Data _o_ = (BroadcastMsg.Data) _o1_;
			if (!msgprotocol.equals(_o_.msgprotocol)) return false;
			if (broadtime != _o_.broadtime) return false;
			if (broadendtime != _o_.broadendtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += msgprotocol.hashCode();
			_h_ += broadtime;
			_h_ += broadendtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(msgprotocol);
			_sb_.append(",");
			_sb_.append(broadtime);
			_sb_.append(",");
			_sb_.append(broadendtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

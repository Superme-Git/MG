
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class IdState extends mkdb.XBean implements xbean.IdState {
	private long nextid; // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
	private int localid; // ID分配的远端分组编号
	private String peerip; // ID分配的远端服务器地址
	private long time; // ID分配的时间

	@Override
	public void _reset_unsafe_() {
		nextid = 0L;
		localid = 0;
		peerip = "";
		time = 0L;
	}

	IdState(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		peerip = "";
	}

	public IdState() {
		this(0, null, null);
	}

	public IdState(IdState _o_) {
		this(_o_, null, null);
	}

	IdState(xbean.IdState _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof IdState) assign((IdState)_o1_);
		else if (_o1_ instanceof IdState.Data) assign((IdState.Data)_o1_);
		else if (_o1_ instanceof IdState.Const) assign(((IdState.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(IdState _o_) {
		_o_._xdb_verify_unsafe_();
		nextid = _o_.nextid;
		localid = _o_.localid;
		peerip = _o_.peerip;
		time = _o_.time;
	}

	private void assign(IdState.Data _o_) {
		nextid = _o_.nextid;
		localid = _o_.localid;
		peerip = _o_.peerip;
		time = _o_.time;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(nextid);
		_os_.marshal(localid);
		_os_.marshal(peerip, mkdb.Const.IO_CHARSET);
		_os_.marshal(time);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		nextid = _os_.unmarshal_long();
		localid = _os_.unmarshal_int();
		peerip = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		time = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.IdState copy() {
		_xdb_verify_unsafe_();
		return new IdState(this);
	}

	@Override
	public xbean.IdState toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.IdState toBean() {
		_xdb_verify_unsafe_();
		return new IdState(this); // same as copy()
	}

	@Override
	public xbean.IdState toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.IdState toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getNextid() { // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
		_xdb_verify_unsafe_();
		return nextid;
	}

	@Override
	public int getLocalid() { // ID分配的远端分组编号
		_xdb_verify_unsafe_();
		return localid;
	}

	@Override
	public String getPeerip() { // ID分配的远端服务器地址
		_xdb_verify_unsafe_();
		return peerip;
	}

	@Override
	public com.locojoy.base.Octets getPeeripOctets() { // ID分配的远端服务器地址
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPeerip(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getTime() { // ID分配的时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public void setNextid(long _v_) { // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "nextid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, nextid) {
					public void rollback() { nextid = _xdb_saved; }
				};}});
		nextid = _v_;
	}

	@Override
	public void setLocalid(int _v_) { // ID分配的远端分组编号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "localid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, localid) {
					public void rollback() { localid = _xdb_saved; }
				};}});
		localid = _v_;
	}

	@Override
	public void setPeerip(String _v_) { // ID分配的远端服务器地址
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "peerip") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, peerip) {
					public void rollback() { peerip = _xdb_saved; }
				};}});
		peerip = _v_;
	}

	@Override
	public void setPeeripOctets(com.locojoy.base.Octets _v_) { // ID分配的远端服务器地址
		_xdb_verify_unsafe_();
		this.setPeerip(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setTime(long _v_) { // ID分配的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		IdState _o_ = null;
		if ( _o1_ instanceof IdState ) _o_ = (IdState)_o1_;
		else if ( _o1_ instanceof IdState.Const ) _o_ = ((IdState.Const)_o1_).nThis();
		else return false;
		if (nextid != _o_.nextid) return false;
		if (localid != _o_.localid) return false;
		if (!peerip.equals(_o_.peerip)) return false;
		if (time != _o_.time) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += nextid;
		_h_ += localid;
		_h_ += peerip.hashCode();
		_h_ += time;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(nextid);
		_sb_.append(",");
		_sb_.append(localid);
		_sb_.append(",");
		_sb_.append("'").append(peerip).append("'");
		_sb_.append(",");
		_sb_.append(time);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("nextid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("localid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("peerip"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		return lb;
	}

	private class Const implements xbean.IdState {
		IdState nThis() {
			return IdState.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.IdState copy() {
			return IdState.this.copy();
		}

		@Override
		public xbean.IdState toData() {
			return IdState.this.toData();
		}

		public xbean.IdState toBean() {
			return IdState.this.toBean();
		}

		@Override
		public xbean.IdState toDataIf() {
			return IdState.this.toDataIf();
		}

		public xbean.IdState toBeanIf() {
			return IdState.this.toBeanIf();
		}

		@Override
		public long getNextid() { // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
			_xdb_verify_unsafe_();
			return nextid;
		}

		@Override
		public int getLocalid() { // ID分配的远端分组编号
			_xdb_verify_unsafe_();
			return localid;
		}

		@Override
		public String getPeerip() { // ID分配的远端服务器地址
			_xdb_verify_unsafe_();
			return peerip;
		}

		@Override
		public com.locojoy.base.Octets getPeeripOctets() { // ID分配的远端服务器地址
			_xdb_verify_unsafe_();
			return IdState.this.getPeeripOctets();
		}

		@Override
		public long getTime() { // ID分配的时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public void setNextid(long _v_) { // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLocalid(int _v_) { // ID分配的远端分组编号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPeerip(String _v_) { // ID分配的远端服务器地址
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPeeripOctets(com.locojoy.base.Octets _v_) { // ID分配的远端服务器地址
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // ID分配的时间
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
			return IdState.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return IdState.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return IdState.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return IdState.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return IdState.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return IdState.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return IdState.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return IdState.this.hashCode();
		}

		@Override
		public String toString() {
			return IdState.this.toString();
		}

	}

	public static final class Data implements xbean.IdState {
		private long nextid; // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
		private int localid; // ID分配的远端分组编号
		private String peerip; // ID分配的远端服务器地址
		private long time; // ID分配的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			peerip = "";
		}

		Data(xbean.IdState _o1_) {
			if (_o1_ instanceof IdState) assign((IdState)_o1_);
			else if (_o1_ instanceof IdState.Data) assign((IdState.Data)_o1_);
			else if (_o1_ instanceof IdState.Const) assign(((IdState.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(IdState _o_) {
			nextid = _o_.nextid;
			localid = _o_.localid;
			peerip = _o_.peerip;
			time = _o_.time;
		}

		private void assign(IdState.Data _o_) {
			nextid = _o_.nextid;
			localid = _o_.localid;
			peerip = _o_.peerip;
			time = _o_.time;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(nextid);
			_os_.marshal(localid);
			_os_.marshal(peerip, mkdb.Const.IO_CHARSET);
			_os_.marshal(time);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			nextid = _os_.unmarshal_long();
			localid = _os_.unmarshal_int();
			peerip = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			time = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.IdState copy() {
			return new Data(this);
		}

		@Override
		public xbean.IdState toData() {
			return new Data(this);
		}

		public xbean.IdState toBean() {
			return new IdState(this, null, null);
		}

		@Override
		public xbean.IdState toDataIf() {
			return this;
		}

		public xbean.IdState toBeanIf() {
			return new IdState(this, null, null);
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
		public long getNextid() { // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
			return nextid;
		}

		@Override
		public int getLocalid() { // ID分配的远端分组编号
			return localid;
		}

		@Override
		public String getPeerip() { // ID分配的远端服务器地址
			return peerip;
		}

		@Override
		public com.locojoy.base.Octets getPeeripOctets() { // ID分配的远端服务器地址
			return com.locojoy.base.Octets.wrap(getPeerip(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getTime() { // ID分配的时间
			return time;
		}

		@Override
		public void setNextid(long _v_) { // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
			nextid = _v_;
		}

		@Override
		public void setLocalid(int _v_) { // ID分配的远端分组编号
			localid = _v_;
		}

		@Override
		public void setPeerip(String _v_) { // ID分配的远端服务器地址
			if (null == _v_)
				throw new NullPointerException();
			peerip = _v_;
		}

		@Override
		public void setPeeripOctets(com.locojoy.base.Octets _v_) { // ID分配的远端服务器地址
			this.setPeerip(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setTime(long _v_) { // ID分配的时间
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof IdState.Data)) return false;
			IdState.Data _o_ = (IdState.Data) _o1_;
			if (nextid != _o_.nextid) return false;
			if (localid != _o_.localid) return false;
			if (!peerip.equals(_o_.peerip)) return false;
			if (time != _o_.time) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += nextid;
			_h_ += localid;
			_h_ += peerip.hashCode();
			_h_ += time;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(nextid);
			_sb_.append(",");
			_sb_.append(localid);
			_sb_.append(",");
			_sb_.append("'").append(peerip).append("'");
			_sb_.append(",");
			_sb_.append(time);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BroadcastMsgList extends mkdb.XBean implements xbean.BroadcastMsgList {
	private java.util.ArrayList<xbean.BroadcastMsg> msglist; // 

	@Override
	public void _reset_unsafe_() {
		msglist.clear();
	}

	BroadcastMsgList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		msglist = new java.util.ArrayList<xbean.BroadcastMsg>();
	}

	public BroadcastMsgList() {
		this(0, null, null);
	}

	public BroadcastMsgList(BroadcastMsgList _o_) {
		this(_o_, null, null);
	}

	BroadcastMsgList(xbean.BroadcastMsgList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BroadcastMsgList) assign((BroadcastMsgList)_o1_);
		else if (_o1_ instanceof BroadcastMsgList.Data) assign((BroadcastMsgList.Data)_o1_);
		else if (_o1_ instanceof BroadcastMsgList.Const) assign(((BroadcastMsgList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BroadcastMsgList _o_) {
		_o_._xdb_verify_unsafe_();
		msglist = new java.util.ArrayList<xbean.BroadcastMsg>();
		for (xbean.BroadcastMsg _v_ : _o_.msglist)
			msglist.add(new BroadcastMsg(_v_, this, "msglist"));
	}

	private void assign(BroadcastMsgList.Data _o_) {
		msglist = new java.util.ArrayList<xbean.BroadcastMsg>();
		for (xbean.BroadcastMsg _v_ : _o_.msglist)
			msglist.add(new BroadcastMsg(_v_, this, "msglist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(msglist.size());
		for (xbean.BroadcastMsg _v_ : msglist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.BroadcastMsg _v_ = new BroadcastMsg(0, this, "msglist");
			_v_.unmarshal(_os_);
			msglist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.BroadcastMsgList copy() {
		_xdb_verify_unsafe_();
		return new BroadcastMsgList(this);
	}

	@Override
	public xbean.BroadcastMsgList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BroadcastMsgList toBean() {
		_xdb_verify_unsafe_();
		return new BroadcastMsgList(this); // same as copy()
	}

	@Override
	public xbean.BroadcastMsgList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BroadcastMsgList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.BroadcastMsg> getMsglist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "msglist"), msglist);
	}

	public java.util.List<xbean.BroadcastMsg> getMsglistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.BroadcastMsg> msglist;
		BroadcastMsgList _o_ = this;
		msglist = new java.util.ArrayList<xbean.BroadcastMsg>();
		for (xbean.BroadcastMsg _v_ : _o_.msglist)
			msglist.add(new BroadcastMsg.Data(_v_));
		return msglist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BroadcastMsgList _o_ = null;
		if ( _o1_ instanceof BroadcastMsgList ) _o_ = (BroadcastMsgList)_o1_;
		else if ( _o1_ instanceof BroadcastMsgList.Const ) _o_ = ((BroadcastMsgList.Const)_o1_).nThis();
		else return false;
		if (!msglist.equals(_o_.msglist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += msglist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(msglist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("msglist"));
		return lb;
	}

	private class Const implements xbean.BroadcastMsgList {
		BroadcastMsgList nThis() {
			return BroadcastMsgList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BroadcastMsgList copy() {
			return BroadcastMsgList.this.copy();
		}

		@Override
		public xbean.BroadcastMsgList toData() {
			return BroadcastMsgList.this.toData();
		}

		public xbean.BroadcastMsgList toBean() {
			return BroadcastMsgList.this.toBean();
		}

		@Override
		public xbean.BroadcastMsgList toDataIf() {
			return BroadcastMsgList.this.toDataIf();
		}

		public xbean.BroadcastMsgList toBeanIf() {
			return BroadcastMsgList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.BroadcastMsg> getMsglist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(msglist);
		}

		public java.util.List<xbean.BroadcastMsg> getMsglistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.BroadcastMsg> msglist;
			BroadcastMsgList _o_ = BroadcastMsgList.this;
		msglist = new java.util.ArrayList<xbean.BroadcastMsg>();
		for (xbean.BroadcastMsg _v_ : _o_.msglist)
			msglist.add(new BroadcastMsg.Data(_v_));
			return msglist;
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
			return BroadcastMsgList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BroadcastMsgList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BroadcastMsgList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BroadcastMsgList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BroadcastMsgList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BroadcastMsgList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BroadcastMsgList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BroadcastMsgList.this.hashCode();
		}

		@Override
		public String toString() {
			return BroadcastMsgList.this.toString();
		}

	}

	public static final class Data implements xbean.BroadcastMsgList {
		private java.util.ArrayList<xbean.BroadcastMsg> msglist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			msglist = new java.util.ArrayList<xbean.BroadcastMsg>();
		}

		Data(xbean.BroadcastMsgList _o1_) {
			if (_o1_ instanceof BroadcastMsgList) assign((BroadcastMsgList)_o1_);
			else if (_o1_ instanceof BroadcastMsgList.Data) assign((BroadcastMsgList.Data)_o1_);
			else if (_o1_ instanceof BroadcastMsgList.Const) assign(((BroadcastMsgList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BroadcastMsgList _o_) {
			msglist = new java.util.ArrayList<xbean.BroadcastMsg>();
			for (xbean.BroadcastMsg _v_ : _o_.msglist)
				msglist.add(new BroadcastMsg.Data(_v_));
		}

		private void assign(BroadcastMsgList.Data _o_) {
			msglist = new java.util.ArrayList<xbean.BroadcastMsg>();
			for (xbean.BroadcastMsg _v_ : _o_.msglist)
				msglist.add(new BroadcastMsg.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(msglist.size());
			for (xbean.BroadcastMsg _v_ : msglist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.BroadcastMsg _v_ = xbean.Pod.newBroadcastMsgData();
				_v_.unmarshal(_os_);
				msglist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.BroadcastMsgList copy() {
			return new Data(this);
		}

		@Override
		public xbean.BroadcastMsgList toData() {
			return new Data(this);
		}

		public xbean.BroadcastMsgList toBean() {
			return new BroadcastMsgList(this, null, null);
		}

		@Override
		public xbean.BroadcastMsgList toDataIf() {
			return this;
		}

		public xbean.BroadcastMsgList toBeanIf() {
			return new BroadcastMsgList(this, null, null);
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
		public java.util.List<xbean.BroadcastMsg> getMsglist() { // 
			return msglist;
		}

		@Override
		public java.util.List<xbean.BroadcastMsg> getMsglistAsData() { // 
			return msglist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BroadcastMsgList.Data)) return false;
			BroadcastMsgList.Data _o_ = (BroadcastMsgList.Data) _o1_;
			if (!msglist.equals(_o_.msglist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += msglist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(msglist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

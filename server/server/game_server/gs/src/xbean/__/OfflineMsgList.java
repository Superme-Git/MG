
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class OfflineMsgList extends mkdb.XBean implements xbean.OfflineMsgList {
	private java.util.ArrayList<xbean.OfflineMsgProtocol> protocollist; // 

	@Override
	public void _reset_unsafe_() {
		protocollist.clear();
	}

	OfflineMsgList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		protocollist = new java.util.ArrayList<xbean.OfflineMsgProtocol>();
	}

	public OfflineMsgList() {
		this(0, null, null);
	}

	public OfflineMsgList(OfflineMsgList _o_) {
		this(_o_, null, null);
	}

	OfflineMsgList(xbean.OfflineMsgList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof OfflineMsgList) assign((OfflineMsgList)_o1_);
		else if (_o1_ instanceof OfflineMsgList.Data) assign((OfflineMsgList.Data)_o1_);
		else if (_o1_ instanceof OfflineMsgList.Const) assign(((OfflineMsgList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(OfflineMsgList _o_) {
		_o_._xdb_verify_unsafe_();
		protocollist = new java.util.ArrayList<xbean.OfflineMsgProtocol>();
		for (xbean.OfflineMsgProtocol _v_ : _o_.protocollist)
			protocollist.add(new OfflineMsgProtocol(_v_, this, "protocollist"));
	}

	private void assign(OfflineMsgList.Data _o_) {
		protocollist = new java.util.ArrayList<xbean.OfflineMsgProtocol>();
		for (xbean.OfflineMsgProtocol _v_ : _o_.protocollist)
			protocollist.add(new OfflineMsgProtocol(_v_, this, "protocollist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(protocollist.size());
		for (xbean.OfflineMsgProtocol _v_ : protocollist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.OfflineMsgProtocol _v_ = new OfflineMsgProtocol(0, this, "protocollist");
			_v_.unmarshal(_os_);
			protocollist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.OfflineMsgList copy() {
		_xdb_verify_unsafe_();
		return new OfflineMsgList(this);
	}

	@Override
	public xbean.OfflineMsgList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OfflineMsgList toBean() {
		_xdb_verify_unsafe_();
		return new OfflineMsgList(this); // same as copy()
	}

	@Override
	public xbean.OfflineMsgList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OfflineMsgList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.OfflineMsgProtocol> getProtocollist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "protocollist"), protocollist);
	}

	public java.util.List<xbean.OfflineMsgProtocol> getProtocollistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.OfflineMsgProtocol> protocollist;
		OfflineMsgList _o_ = this;
		protocollist = new java.util.ArrayList<xbean.OfflineMsgProtocol>();
		for (xbean.OfflineMsgProtocol _v_ : _o_.protocollist)
			protocollist.add(new OfflineMsgProtocol.Data(_v_));
		return protocollist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		OfflineMsgList _o_ = null;
		if ( _o1_ instanceof OfflineMsgList ) _o_ = (OfflineMsgList)_o1_;
		else if ( _o1_ instanceof OfflineMsgList.Const ) _o_ = ((OfflineMsgList.Const)_o1_).nThis();
		else return false;
		if (!protocollist.equals(_o_.protocollist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += protocollist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(protocollist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("protocollist"));
		return lb;
	}

	private class Const implements xbean.OfflineMsgList {
		OfflineMsgList nThis() {
			return OfflineMsgList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.OfflineMsgList copy() {
			return OfflineMsgList.this.copy();
		}

		@Override
		public xbean.OfflineMsgList toData() {
			return OfflineMsgList.this.toData();
		}

		public xbean.OfflineMsgList toBean() {
			return OfflineMsgList.this.toBean();
		}

		@Override
		public xbean.OfflineMsgList toDataIf() {
			return OfflineMsgList.this.toDataIf();
		}

		public xbean.OfflineMsgList toBeanIf() {
			return OfflineMsgList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.OfflineMsgProtocol> getProtocollist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(protocollist);
		}

		public java.util.List<xbean.OfflineMsgProtocol> getProtocollistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.OfflineMsgProtocol> protocollist;
			OfflineMsgList _o_ = OfflineMsgList.this;
		protocollist = new java.util.ArrayList<xbean.OfflineMsgProtocol>();
		for (xbean.OfflineMsgProtocol _v_ : _o_.protocollist)
			protocollist.add(new OfflineMsgProtocol.Data(_v_));
			return protocollist;
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
			return OfflineMsgList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return OfflineMsgList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return OfflineMsgList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return OfflineMsgList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return OfflineMsgList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return OfflineMsgList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return OfflineMsgList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return OfflineMsgList.this.hashCode();
		}

		@Override
		public String toString() {
			return OfflineMsgList.this.toString();
		}

	}

	public static final class Data implements xbean.OfflineMsgList {
		private java.util.ArrayList<xbean.OfflineMsgProtocol> protocollist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			protocollist = new java.util.ArrayList<xbean.OfflineMsgProtocol>();
		}

		Data(xbean.OfflineMsgList _o1_) {
			if (_o1_ instanceof OfflineMsgList) assign((OfflineMsgList)_o1_);
			else if (_o1_ instanceof OfflineMsgList.Data) assign((OfflineMsgList.Data)_o1_);
			else if (_o1_ instanceof OfflineMsgList.Const) assign(((OfflineMsgList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(OfflineMsgList _o_) {
			protocollist = new java.util.ArrayList<xbean.OfflineMsgProtocol>();
			for (xbean.OfflineMsgProtocol _v_ : _o_.protocollist)
				protocollist.add(new OfflineMsgProtocol.Data(_v_));
		}

		private void assign(OfflineMsgList.Data _o_) {
			protocollist = new java.util.ArrayList<xbean.OfflineMsgProtocol>();
			for (xbean.OfflineMsgProtocol _v_ : _o_.protocollist)
				protocollist.add(new OfflineMsgProtocol.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(protocollist.size());
			for (xbean.OfflineMsgProtocol _v_ : protocollist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.OfflineMsgProtocol _v_ = xbean.Pod.newOfflineMsgProtocolData();
				_v_.unmarshal(_os_);
				protocollist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.OfflineMsgList copy() {
			return new Data(this);
		}

		@Override
		public xbean.OfflineMsgList toData() {
			return new Data(this);
		}

		public xbean.OfflineMsgList toBean() {
			return new OfflineMsgList(this, null, null);
		}

		@Override
		public xbean.OfflineMsgList toDataIf() {
			return this;
		}

		public xbean.OfflineMsgList toBeanIf() {
			return new OfflineMsgList(this, null, null);
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
		public java.util.List<xbean.OfflineMsgProtocol> getProtocollist() { // 
			return protocollist;
		}

		@Override
		public java.util.List<xbean.OfflineMsgProtocol> getProtocollistAsData() { // 
			return protocollist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof OfflineMsgList.Data)) return false;
			OfflineMsgList.Data _o_ = (OfflineMsgList.Data) _o1_;
			if (!protocollist.equals(_o_.protocollist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += protocollist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(protocollist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

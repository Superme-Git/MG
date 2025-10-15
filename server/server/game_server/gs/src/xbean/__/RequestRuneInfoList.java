
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RequestRuneInfoList extends mkdb.XBean implements xbean.RequestRuneInfoList {
	private java.util.ArrayList<xbean.RequestRuneInfo> requestrunelists; // 

	@Override
	public void _reset_unsafe_() {
		requestrunelists.clear();
	}

	RequestRuneInfoList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		requestrunelists = new java.util.ArrayList<xbean.RequestRuneInfo>();
	}

	public RequestRuneInfoList() {
		this(0, null, null);
	}

	public RequestRuneInfoList(RequestRuneInfoList _o_) {
		this(_o_, null, null);
	}

	RequestRuneInfoList(xbean.RequestRuneInfoList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RequestRuneInfoList) assign((RequestRuneInfoList)_o1_);
		else if (_o1_ instanceof RequestRuneInfoList.Data) assign((RequestRuneInfoList.Data)_o1_);
		else if (_o1_ instanceof RequestRuneInfoList.Const) assign(((RequestRuneInfoList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RequestRuneInfoList _o_) {
		_o_._xdb_verify_unsafe_();
		requestrunelists = new java.util.ArrayList<xbean.RequestRuneInfo>();
		for (xbean.RequestRuneInfo _v_ : _o_.requestrunelists)
			requestrunelists.add(new RequestRuneInfo(_v_, this, "requestrunelists"));
	}

	private void assign(RequestRuneInfoList.Data _o_) {
		requestrunelists = new java.util.ArrayList<xbean.RequestRuneInfo>();
		for (xbean.RequestRuneInfo _v_ : _o_.requestrunelists)
			requestrunelists.add(new RequestRuneInfo(_v_, this, "requestrunelists"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(requestrunelists.size());
		for (xbean.RequestRuneInfo _v_ : requestrunelists) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RequestRuneInfo _v_ = new RequestRuneInfo(0, this, "requestrunelists");
			_v_.unmarshal(_os_);
			requestrunelists.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RequestRuneInfoList copy() {
		_xdb_verify_unsafe_();
		return new RequestRuneInfoList(this);
	}

	@Override
	public xbean.RequestRuneInfoList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RequestRuneInfoList toBean() {
		_xdb_verify_unsafe_();
		return new RequestRuneInfoList(this); // same as copy()
	}

	@Override
	public xbean.RequestRuneInfoList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RequestRuneInfoList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.RequestRuneInfo> getRequestrunelists() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "requestrunelists"), requestrunelists);
	}

	public java.util.List<xbean.RequestRuneInfo> getRequestrunelistsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.RequestRuneInfo> requestrunelists;
		RequestRuneInfoList _o_ = this;
		requestrunelists = new java.util.ArrayList<xbean.RequestRuneInfo>();
		for (xbean.RequestRuneInfo _v_ : _o_.requestrunelists)
			requestrunelists.add(new RequestRuneInfo.Data(_v_));
		return requestrunelists;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RequestRuneInfoList _o_ = null;
		if ( _o1_ instanceof RequestRuneInfoList ) _o_ = (RequestRuneInfoList)_o1_;
		else if ( _o1_ instanceof RequestRuneInfoList.Const ) _o_ = ((RequestRuneInfoList.Const)_o1_).nThis();
		else return false;
		if (!requestrunelists.equals(_o_.requestrunelists)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += requestrunelists.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(requestrunelists);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("requestrunelists"));
		return lb;
	}

	private class Const implements xbean.RequestRuneInfoList {
		RequestRuneInfoList nThis() {
			return RequestRuneInfoList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RequestRuneInfoList copy() {
			return RequestRuneInfoList.this.copy();
		}

		@Override
		public xbean.RequestRuneInfoList toData() {
			return RequestRuneInfoList.this.toData();
		}

		public xbean.RequestRuneInfoList toBean() {
			return RequestRuneInfoList.this.toBean();
		}

		@Override
		public xbean.RequestRuneInfoList toDataIf() {
			return RequestRuneInfoList.this.toDataIf();
		}

		public xbean.RequestRuneInfoList toBeanIf() {
			return RequestRuneInfoList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.RequestRuneInfo> getRequestrunelists() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(requestrunelists);
		}

		public java.util.List<xbean.RequestRuneInfo> getRequestrunelistsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.RequestRuneInfo> requestrunelists;
			RequestRuneInfoList _o_ = RequestRuneInfoList.this;
		requestrunelists = new java.util.ArrayList<xbean.RequestRuneInfo>();
		for (xbean.RequestRuneInfo _v_ : _o_.requestrunelists)
			requestrunelists.add(new RequestRuneInfo.Data(_v_));
			return requestrunelists;
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
			return RequestRuneInfoList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RequestRuneInfoList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RequestRuneInfoList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RequestRuneInfoList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RequestRuneInfoList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RequestRuneInfoList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RequestRuneInfoList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RequestRuneInfoList.this.hashCode();
		}

		@Override
		public String toString() {
			return RequestRuneInfoList.this.toString();
		}

	}

	public static final class Data implements xbean.RequestRuneInfoList {
		private java.util.ArrayList<xbean.RequestRuneInfo> requestrunelists; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			requestrunelists = new java.util.ArrayList<xbean.RequestRuneInfo>();
		}

		Data(xbean.RequestRuneInfoList _o1_) {
			if (_o1_ instanceof RequestRuneInfoList) assign((RequestRuneInfoList)_o1_);
			else if (_o1_ instanceof RequestRuneInfoList.Data) assign((RequestRuneInfoList.Data)_o1_);
			else if (_o1_ instanceof RequestRuneInfoList.Const) assign(((RequestRuneInfoList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RequestRuneInfoList _o_) {
			requestrunelists = new java.util.ArrayList<xbean.RequestRuneInfo>();
			for (xbean.RequestRuneInfo _v_ : _o_.requestrunelists)
				requestrunelists.add(new RequestRuneInfo.Data(_v_));
		}

		private void assign(RequestRuneInfoList.Data _o_) {
			requestrunelists = new java.util.ArrayList<xbean.RequestRuneInfo>();
			for (xbean.RequestRuneInfo _v_ : _o_.requestrunelists)
				requestrunelists.add(new RequestRuneInfo.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(requestrunelists.size());
			for (xbean.RequestRuneInfo _v_ : requestrunelists) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RequestRuneInfo _v_ = xbean.Pod.newRequestRuneInfoData();
				_v_.unmarshal(_os_);
				requestrunelists.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RequestRuneInfoList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RequestRuneInfoList toData() {
			return new Data(this);
		}

		public xbean.RequestRuneInfoList toBean() {
			return new RequestRuneInfoList(this, null, null);
		}

		@Override
		public xbean.RequestRuneInfoList toDataIf() {
			return this;
		}

		public xbean.RequestRuneInfoList toBeanIf() {
			return new RequestRuneInfoList(this, null, null);
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
		public java.util.List<xbean.RequestRuneInfo> getRequestrunelists() { // 
			return requestrunelists;
		}

		@Override
		public java.util.List<xbean.RequestRuneInfo> getRequestrunelistsAsData() { // 
			return requestrunelists;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RequestRuneInfoList.Data)) return false;
			RequestRuneInfoList.Data _o_ = (RequestRuneInfoList.Data) _o1_;
			if (!requestrunelists.equals(_o_.requestrunelists)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += requestrunelists.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(requestrunelists);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

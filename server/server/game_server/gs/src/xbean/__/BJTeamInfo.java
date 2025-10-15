
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BJTeamInfo extends mkdb.XBean implements xbean.BJTeamInfo {
	private int serviceid; // 
	private java.util.ArrayList<Long> bjdata; // 

	@Override
	public void _reset_unsafe_() {
		serviceid = 0;
		bjdata.clear();
	}

	BJTeamInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		bjdata = new java.util.ArrayList<Long>();
	}

	public BJTeamInfo() {
		this(0, null, null);
	}

	public BJTeamInfo(BJTeamInfo _o_) {
		this(_o_, null, null);
	}

	BJTeamInfo(xbean.BJTeamInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BJTeamInfo) assign((BJTeamInfo)_o1_);
		else if (_o1_ instanceof BJTeamInfo.Data) assign((BJTeamInfo.Data)_o1_);
		else if (_o1_ instanceof BJTeamInfo.Const) assign(((BJTeamInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BJTeamInfo _o_) {
		_o_._xdb_verify_unsafe_();
		serviceid = _o_.serviceid;
		bjdata = new java.util.ArrayList<Long>();
		bjdata.addAll(_o_.bjdata);
	}

	private void assign(BJTeamInfo.Data _o_) {
		serviceid = _o_.serviceid;
		bjdata = new java.util.ArrayList<Long>();
		bjdata.addAll(_o_.bjdata);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(serviceid);
		_os_.compact_uint32(bjdata.size());
		for (Long _v_ : bjdata) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		serviceid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			bjdata.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.BJTeamInfo copy() {
		_xdb_verify_unsafe_();
		return new BJTeamInfo(this);
	}

	@Override
	public xbean.BJTeamInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BJTeamInfo toBean() {
		_xdb_verify_unsafe_();
		return new BJTeamInfo(this); // same as copy()
	}

	@Override
	public xbean.BJTeamInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BJTeamInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getServiceid() { // 
		_xdb_verify_unsafe_();
		return serviceid;
	}

	@Override
	public java.util.List<Long> getBjdata() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "bjdata"), bjdata);
	}

	public java.util.List<Long> getBjdataAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Long> bjdata;
		BJTeamInfo _o_ = this;
		bjdata = new java.util.ArrayList<Long>();
		bjdata.addAll(_o_.bjdata);
		return bjdata;
	}

	@Override
	public void setServiceid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "serviceid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, serviceid) {
					public void rollback() { serviceid = _xdb_saved; }
				};}});
		serviceid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BJTeamInfo _o_ = null;
		if ( _o1_ instanceof BJTeamInfo ) _o_ = (BJTeamInfo)_o1_;
		else if ( _o1_ instanceof BJTeamInfo.Const ) _o_ = ((BJTeamInfo.Const)_o1_).nThis();
		else return false;
		if (serviceid != _o_.serviceid) return false;
		if (!bjdata.equals(_o_.bjdata)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += serviceid;
		_h_ += bjdata.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(serviceid);
		_sb_.append(",");
		_sb_.append(bjdata);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("serviceid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bjdata"));
		return lb;
	}

	private class Const implements xbean.BJTeamInfo {
		BJTeamInfo nThis() {
			return BJTeamInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BJTeamInfo copy() {
			return BJTeamInfo.this.copy();
		}

		@Override
		public xbean.BJTeamInfo toData() {
			return BJTeamInfo.this.toData();
		}

		public xbean.BJTeamInfo toBean() {
			return BJTeamInfo.this.toBean();
		}

		@Override
		public xbean.BJTeamInfo toDataIf() {
			return BJTeamInfo.this.toDataIf();
		}

		public xbean.BJTeamInfo toBeanIf() {
			return BJTeamInfo.this.toBeanIf();
		}

		@Override
		public int getServiceid() { // 
			_xdb_verify_unsafe_();
			return serviceid;
		}

		@Override
		public java.util.List<Long> getBjdata() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(bjdata);
		}

		public java.util.List<Long> getBjdataAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Long> bjdata;
			BJTeamInfo _o_ = BJTeamInfo.this;
		bjdata = new java.util.ArrayList<Long>();
		bjdata.addAll(_o_.bjdata);
			return bjdata;
		}

		@Override
		public void setServiceid(int _v_) { // 
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
			return BJTeamInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BJTeamInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BJTeamInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BJTeamInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BJTeamInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BJTeamInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BJTeamInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BJTeamInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return BJTeamInfo.this.toString();
		}

	}

	public static final class Data implements xbean.BJTeamInfo {
		private int serviceid; // 
		private java.util.ArrayList<Long> bjdata; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			bjdata = new java.util.ArrayList<Long>();
		}

		Data(xbean.BJTeamInfo _o1_) {
			if (_o1_ instanceof BJTeamInfo) assign((BJTeamInfo)_o1_);
			else if (_o1_ instanceof BJTeamInfo.Data) assign((BJTeamInfo.Data)_o1_);
			else if (_o1_ instanceof BJTeamInfo.Const) assign(((BJTeamInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BJTeamInfo _o_) {
			serviceid = _o_.serviceid;
			bjdata = new java.util.ArrayList<Long>();
			bjdata.addAll(_o_.bjdata);
		}

		private void assign(BJTeamInfo.Data _o_) {
			serviceid = _o_.serviceid;
			bjdata = new java.util.ArrayList<Long>();
			bjdata.addAll(_o_.bjdata);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(serviceid);
			_os_.compact_uint32(bjdata.size());
			for (Long _v_ : bjdata) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			serviceid = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				bjdata.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.BJTeamInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.BJTeamInfo toData() {
			return new Data(this);
		}

		public xbean.BJTeamInfo toBean() {
			return new BJTeamInfo(this, null, null);
		}

		@Override
		public xbean.BJTeamInfo toDataIf() {
			return this;
		}

		public xbean.BJTeamInfo toBeanIf() {
			return new BJTeamInfo(this, null, null);
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
		public int getServiceid() { // 
			return serviceid;
		}

		@Override
		public java.util.List<Long> getBjdata() { // 
			return bjdata;
		}

		@Override
		public java.util.List<Long> getBjdataAsData() { // 
			return bjdata;
		}

		@Override
		public void setServiceid(int _v_) { // 
			serviceid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BJTeamInfo.Data)) return false;
			BJTeamInfo.Data _o_ = (BJTeamInfo.Data) _o1_;
			if (serviceid != _o_.serviceid) return false;
			if (!bjdata.equals(_o_.bjdata)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += serviceid;
			_h_ += bjdata.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(serviceid);
			_sb_.append(",");
			_sb_.append(bjdata);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

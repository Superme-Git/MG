
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleTradingHisRecordList extends mkdb.XBean implements xbean.RoleTradingHisRecordList {
	private java.util.ArrayList<xbean.RoleTradingHisRecord> roletradinghisrecordlist; // 

	@Override
	public void _reset_unsafe_() {
		roletradinghisrecordlist.clear();
	}

	RoleTradingHisRecordList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		roletradinghisrecordlist = new java.util.ArrayList<xbean.RoleTradingHisRecord>();
	}

	public RoleTradingHisRecordList() {
		this(0, null, null);
	}

	public RoleTradingHisRecordList(RoleTradingHisRecordList _o_) {
		this(_o_, null, null);
	}

	RoleTradingHisRecordList(xbean.RoleTradingHisRecordList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleTradingHisRecordList) assign((RoleTradingHisRecordList)_o1_);
		else if (_o1_ instanceof RoleTradingHisRecordList.Data) assign((RoleTradingHisRecordList.Data)_o1_);
		else if (_o1_ instanceof RoleTradingHisRecordList.Const) assign(((RoleTradingHisRecordList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleTradingHisRecordList _o_) {
		_o_._xdb_verify_unsafe_();
		roletradinghisrecordlist = new java.util.ArrayList<xbean.RoleTradingHisRecord>();
		for (xbean.RoleTradingHisRecord _v_ : _o_.roletradinghisrecordlist)
			roletradinghisrecordlist.add(new RoleTradingHisRecord(_v_, this, "roletradinghisrecordlist"));
	}

	private void assign(RoleTradingHisRecordList.Data _o_) {
		roletradinghisrecordlist = new java.util.ArrayList<xbean.RoleTradingHisRecord>();
		for (xbean.RoleTradingHisRecord _v_ : _o_.roletradinghisrecordlist)
			roletradinghisrecordlist.add(new RoleTradingHisRecord(_v_, this, "roletradinghisrecordlist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(roletradinghisrecordlist.size());
		for (xbean.RoleTradingHisRecord _v_ : roletradinghisrecordlist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RoleTradingHisRecord _v_ = new RoleTradingHisRecord(0, this, "roletradinghisrecordlist");
			_v_.unmarshal(_os_);
			roletradinghisrecordlist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RoleTradingHisRecordList copy() {
		_xdb_verify_unsafe_();
		return new RoleTradingHisRecordList(this);
	}

	@Override
	public xbean.RoleTradingHisRecordList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleTradingHisRecordList toBean() {
		_xdb_verify_unsafe_();
		return new RoleTradingHisRecordList(this); // same as copy()
	}

	@Override
	public xbean.RoleTradingHisRecordList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleTradingHisRecordList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.RoleTradingHisRecord> getRoletradinghisrecordlist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "roletradinghisrecordlist"), roletradinghisrecordlist);
	}

	public java.util.List<xbean.RoleTradingHisRecord> getRoletradinghisrecordlistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.RoleTradingHisRecord> roletradinghisrecordlist;
		RoleTradingHisRecordList _o_ = this;
		roletradinghisrecordlist = new java.util.ArrayList<xbean.RoleTradingHisRecord>();
		for (xbean.RoleTradingHisRecord _v_ : _o_.roletradinghisrecordlist)
			roletradinghisrecordlist.add(new RoleTradingHisRecord.Data(_v_));
		return roletradinghisrecordlist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleTradingHisRecordList _o_ = null;
		if ( _o1_ instanceof RoleTradingHisRecordList ) _o_ = (RoleTradingHisRecordList)_o1_;
		else if ( _o1_ instanceof RoleTradingHisRecordList.Const ) _o_ = ((RoleTradingHisRecordList.Const)_o1_).nThis();
		else return false;
		if (!roletradinghisrecordlist.equals(_o_.roletradinghisrecordlist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roletradinghisrecordlist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roletradinghisrecordlist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roletradinghisrecordlist"));
		return lb;
	}

	private class Const implements xbean.RoleTradingHisRecordList {
		RoleTradingHisRecordList nThis() {
			return RoleTradingHisRecordList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleTradingHisRecordList copy() {
			return RoleTradingHisRecordList.this.copy();
		}

		@Override
		public xbean.RoleTradingHisRecordList toData() {
			return RoleTradingHisRecordList.this.toData();
		}

		public xbean.RoleTradingHisRecordList toBean() {
			return RoleTradingHisRecordList.this.toBean();
		}

		@Override
		public xbean.RoleTradingHisRecordList toDataIf() {
			return RoleTradingHisRecordList.this.toDataIf();
		}

		public xbean.RoleTradingHisRecordList toBeanIf() {
			return RoleTradingHisRecordList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.RoleTradingHisRecord> getRoletradinghisrecordlist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(roletradinghisrecordlist);
		}

		public java.util.List<xbean.RoleTradingHisRecord> getRoletradinghisrecordlistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.RoleTradingHisRecord> roletradinghisrecordlist;
			RoleTradingHisRecordList _o_ = RoleTradingHisRecordList.this;
		roletradinghisrecordlist = new java.util.ArrayList<xbean.RoleTradingHisRecord>();
		for (xbean.RoleTradingHisRecord _v_ : _o_.roletradinghisrecordlist)
			roletradinghisrecordlist.add(new RoleTradingHisRecord.Data(_v_));
			return roletradinghisrecordlist;
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
			return RoleTradingHisRecordList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleTradingHisRecordList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleTradingHisRecordList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleTradingHisRecordList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleTradingHisRecordList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleTradingHisRecordList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleTradingHisRecordList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleTradingHisRecordList.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleTradingHisRecordList.this.toString();
		}

	}

	public static final class Data implements xbean.RoleTradingHisRecordList {
		private java.util.ArrayList<xbean.RoleTradingHisRecord> roletradinghisrecordlist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			roletradinghisrecordlist = new java.util.ArrayList<xbean.RoleTradingHisRecord>();
		}

		Data(xbean.RoleTradingHisRecordList _o1_) {
			if (_o1_ instanceof RoleTradingHisRecordList) assign((RoleTradingHisRecordList)_o1_);
			else if (_o1_ instanceof RoleTradingHisRecordList.Data) assign((RoleTradingHisRecordList.Data)_o1_);
			else if (_o1_ instanceof RoleTradingHisRecordList.Const) assign(((RoleTradingHisRecordList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleTradingHisRecordList _o_) {
			roletradinghisrecordlist = new java.util.ArrayList<xbean.RoleTradingHisRecord>();
			for (xbean.RoleTradingHisRecord _v_ : _o_.roletradinghisrecordlist)
				roletradinghisrecordlist.add(new RoleTradingHisRecord.Data(_v_));
		}

		private void assign(RoleTradingHisRecordList.Data _o_) {
			roletradinghisrecordlist = new java.util.ArrayList<xbean.RoleTradingHisRecord>();
			for (xbean.RoleTradingHisRecord _v_ : _o_.roletradinghisrecordlist)
				roletradinghisrecordlist.add(new RoleTradingHisRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(roletradinghisrecordlist.size());
			for (xbean.RoleTradingHisRecord _v_ : roletradinghisrecordlist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RoleTradingHisRecord _v_ = xbean.Pod.newRoleTradingHisRecordData();
				_v_.unmarshal(_os_);
				roletradinghisrecordlist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RoleTradingHisRecordList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleTradingHisRecordList toData() {
			return new Data(this);
		}

		public xbean.RoleTradingHisRecordList toBean() {
			return new RoleTradingHisRecordList(this, null, null);
		}

		@Override
		public xbean.RoleTradingHisRecordList toDataIf() {
			return this;
		}

		public xbean.RoleTradingHisRecordList toBeanIf() {
			return new RoleTradingHisRecordList(this, null, null);
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
		public java.util.List<xbean.RoleTradingHisRecord> getRoletradinghisrecordlist() { // 
			return roletradinghisrecordlist;
		}

		@Override
		public java.util.List<xbean.RoleTradingHisRecord> getRoletradinghisrecordlistAsData() { // 
			return roletradinghisrecordlist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleTradingHisRecordList.Data)) return false;
			RoleTradingHisRecordList.Data _o_ = (RoleTradingHisRecordList.Data) _o1_;
			if (!roletradinghisrecordlist.equals(_o_.roletradinghisrecordlist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roletradinghisrecordlist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roletradinghisrecordlist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

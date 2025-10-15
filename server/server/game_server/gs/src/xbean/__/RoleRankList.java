
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleRankList extends mkdb.XBean implements xbean.RoleRankList {
	private java.util.LinkedList<xbean.RoleRankRecord> records; // 

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	RoleRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.RoleRankRecord>();
	}

	public RoleRankList() {
		this(0, null, null);
	}

	public RoleRankList(RoleRankList _o_) {
		this(_o_, null, null);
	}

	RoleRankList(xbean.RoleRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleRankList) assign((RoleRankList)_o1_);
		else if (_o1_ instanceof RoleRankList.Data) assign((RoleRankList.Data)_o1_);
		else if (_o1_ instanceof RoleRankList.Const) assign(((RoleRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.RoleRankRecord>();
		for (xbean.RoleRankRecord _v_ : _o_.records)
			records.add(new RoleRankRecord(_v_, this, "records"));
	}

	private void assign(RoleRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.RoleRankRecord>();
		for (xbean.RoleRankRecord _v_ : _o_.records)
			records.add(new RoleRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.RoleRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RoleRankRecord _v_ = new RoleRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RoleRankList copy() {
		_xdb_verify_unsafe_();
		return new RoleRankList(this);
	}

	@Override
	public xbean.RoleRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleRankList toBean() {
		_xdb_verify_unsafe_();
		return new RoleRankList(this); // same as copy()
	}

	@Override
	public xbean.RoleRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.RoleRankRecord> getRecords() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.RoleRankRecord> getRecordsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.RoleRankRecord> records;
		RoleRankList _o_ = this;
		records = new java.util.LinkedList<xbean.RoleRankRecord>();
		for (xbean.RoleRankRecord _v_ : _o_.records)
			records.add(new RoleRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleRankList _o_ = null;
		if ( _o1_ instanceof RoleRankList ) _o_ = (RoleRankList)_o1_;
		else if ( _o1_ instanceof RoleRankList.Const ) _o_ = ((RoleRankList.Const)_o1_).nThis();
		else return false;
		if (!records.equals(_o_.records)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += records.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(records);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("records"));
		return lb;
	}

	private class Const implements xbean.RoleRankList {
		RoleRankList nThis() {
			return RoleRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleRankList copy() {
			return RoleRankList.this.copy();
		}

		@Override
		public xbean.RoleRankList toData() {
			return RoleRankList.this.toData();
		}

		public xbean.RoleRankList toBean() {
			return RoleRankList.this.toBean();
		}

		@Override
		public xbean.RoleRankList toDataIf() {
			return RoleRankList.this.toDataIf();
		}

		public xbean.RoleRankList toBeanIf() {
			return RoleRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.RoleRankRecord> getRecords() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.RoleRankRecord> getRecordsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.RoleRankRecord> records;
			RoleRankList _o_ = RoleRankList.this;
		records = new java.util.LinkedList<xbean.RoleRankRecord>();
		for (xbean.RoleRankRecord _v_ : _o_.records)
			records.add(new RoleRankRecord.Data(_v_));
			return records;
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
			return RoleRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleRankList.this.toString();
		}

	}

	public static final class Data implements xbean.RoleRankList {
		private java.util.LinkedList<xbean.RoleRankRecord> records; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.RoleRankRecord>();
		}

		Data(xbean.RoleRankList _o1_) {
			if (_o1_ instanceof RoleRankList) assign((RoleRankList)_o1_);
			else if (_o1_ instanceof RoleRankList.Data) assign((RoleRankList.Data)_o1_);
			else if (_o1_ instanceof RoleRankList.Const) assign(((RoleRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleRankList _o_) {
			records = new java.util.LinkedList<xbean.RoleRankRecord>();
			for (xbean.RoleRankRecord _v_ : _o_.records)
				records.add(new RoleRankRecord.Data(_v_));
		}

		private void assign(RoleRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.RoleRankRecord>();
			for (xbean.RoleRankRecord _v_ : _o_.records)
				records.add(new RoleRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.RoleRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RoleRankRecord _v_ = xbean.Pod.newRoleRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RoleRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleRankList toData() {
			return new Data(this);
		}

		public xbean.RoleRankList toBean() {
			return new RoleRankList(this, null, null);
		}

		@Override
		public xbean.RoleRankList toDataIf() {
			return this;
		}

		public xbean.RoleRankList toBeanIf() {
			return new RoleRankList(this, null, null);
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
		public java.util.List<xbean.RoleRankRecord> getRecords() { // 
			return records;
		}

		@Override
		public java.util.List<xbean.RoleRankRecord> getRecordsAsData() { // 
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleRankList.Data)) return false;
			RoleRankList.Data _o_ = (RoleRankList.Data) _o1_;
			if (!records.equals(_o_.records)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += records.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(records);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

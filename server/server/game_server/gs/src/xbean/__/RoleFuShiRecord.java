
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleFuShiRecord extends mkdb.XBean implements xbean.RoleFuShiRecord {
	private java.util.ArrayList<xbean.FuShiRecord> records; // 

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	RoleFuShiRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.ArrayList<xbean.FuShiRecord>();
	}

	public RoleFuShiRecord() {
		this(0, null, null);
	}

	public RoleFuShiRecord(RoleFuShiRecord _o_) {
		this(_o_, null, null);
	}

	RoleFuShiRecord(xbean.RoleFuShiRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleFuShiRecord) assign((RoleFuShiRecord)_o1_);
		else if (_o1_ instanceof RoleFuShiRecord.Data) assign((RoleFuShiRecord.Data)_o1_);
		else if (_o1_ instanceof RoleFuShiRecord.Const) assign(((RoleFuShiRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleFuShiRecord _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.ArrayList<xbean.FuShiRecord>();
		for (xbean.FuShiRecord _v_ : _o_.records)
			records.add(new FuShiRecord(_v_, this, "records"));
	}

	private void assign(RoleFuShiRecord.Data _o_) {
		records = new java.util.ArrayList<xbean.FuShiRecord>();
		for (xbean.FuShiRecord _v_ : _o_.records)
			records.add(new FuShiRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.FuShiRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.FuShiRecord _v_ = new FuShiRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RoleFuShiRecord copy() {
		_xdb_verify_unsafe_();
		return new RoleFuShiRecord(this);
	}

	@Override
	public xbean.RoleFuShiRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleFuShiRecord toBean() {
		_xdb_verify_unsafe_();
		return new RoleFuShiRecord(this); // same as copy()
	}

	@Override
	public xbean.RoleFuShiRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleFuShiRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.FuShiRecord> getRecords() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.FuShiRecord> getRecordsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.FuShiRecord> records;
		RoleFuShiRecord _o_ = this;
		records = new java.util.ArrayList<xbean.FuShiRecord>();
		for (xbean.FuShiRecord _v_ : _o_.records)
			records.add(new FuShiRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleFuShiRecord _o_ = null;
		if ( _o1_ instanceof RoleFuShiRecord ) _o_ = (RoleFuShiRecord)_o1_;
		else if ( _o1_ instanceof RoleFuShiRecord.Const ) _o_ = ((RoleFuShiRecord.Const)_o1_).nThis();
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

	private class Const implements xbean.RoleFuShiRecord {
		RoleFuShiRecord nThis() {
			return RoleFuShiRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleFuShiRecord copy() {
			return RoleFuShiRecord.this.copy();
		}

		@Override
		public xbean.RoleFuShiRecord toData() {
			return RoleFuShiRecord.this.toData();
		}

		public xbean.RoleFuShiRecord toBean() {
			return RoleFuShiRecord.this.toBean();
		}

		@Override
		public xbean.RoleFuShiRecord toDataIf() {
			return RoleFuShiRecord.this.toDataIf();
		}

		public xbean.RoleFuShiRecord toBeanIf() {
			return RoleFuShiRecord.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.FuShiRecord> getRecords() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.FuShiRecord> getRecordsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.FuShiRecord> records;
			RoleFuShiRecord _o_ = RoleFuShiRecord.this;
		records = new java.util.ArrayList<xbean.FuShiRecord>();
		for (xbean.FuShiRecord _v_ : _o_.records)
			records.add(new FuShiRecord.Data(_v_));
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
			return RoleFuShiRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleFuShiRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleFuShiRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleFuShiRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleFuShiRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleFuShiRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleFuShiRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleFuShiRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleFuShiRecord.this.toString();
		}

	}

	public static final class Data implements xbean.RoleFuShiRecord {
		private java.util.ArrayList<xbean.FuShiRecord> records; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.ArrayList<xbean.FuShiRecord>();
		}

		Data(xbean.RoleFuShiRecord _o1_) {
			if (_o1_ instanceof RoleFuShiRecord) assign((RoleFuShiRecord)_o1_);
			else if (_o1_ instanceof RoleFuShiRecord.Data) assign((RoleFuShiRecord.Data)_o1_);
			else if (_o1_ instanceof RoleFuShiRecord.Const) assign(((RoleFuShiRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleFuShiRecord _o_) {
			records = new java.util.ArrayList<xbean.FuShiRecord>();
			for (xbean.FuShiRecord _v_ : _o_.records)
				records.add(new FuShiRecord.Data(_v_));
		}

		private void assign(RoleFuShiRecord.Data _o_) {
			records = new java.util.ArrayList<xbean.FuShiRecord>();
			for (xbean.FuShiRecord _v_ : _o_.records)
				records.add(new FuShiRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.FuShiRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.FuShiRecord _v_ = xbean.Pod.newFuShiRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RoleFuShiRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleFuShiRecord toData() {
			return new Data(this);
		}

		public xbean.RoleFuShiRecord toBean() {
			return new RoleFuShiRecord(this, null, null);
		}

		@Override
		public xbean.RoleFuShiRecord toDataIf() {
			return this;
		}

		public xbean.RoleFuShiRecord toBeanIf() {
			return new RoleFuShiRecord(this, null, null);
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
		public java.util.List<xbean.FuShiRecord> getRecords() { // 
			return records;
		}

		@Override
		public java.util.List<xbean.FuShiRecord> getRecordsAsData() { // 
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleFuShiRecord.Data)) return false;
			RoleFuShiRecord.Data _o_ = (RoleFuShiRecord.Data) _o1_;
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


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleProfessionRankList extends mkdb.XBean implements xbean.RoleProfessionRankList {
	private java.util.LinkedList<xbean.RoleProfessionRankRecord> records; // 

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	RoleProfessionRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.RoleProfessionRankRecord>();
	}

	public RoleProfessionRankList() {
		this(0, null, null);
	}

	public RoleProfessionRankList(RoleProfessionRankList _o_) {
		this(_o_, null, null);
	}

	RoleProfessionRankList(xbean.RoleProfessionRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleProfessionRankList) assign((RoleProfessionRankList)_o1_);
		else if (_o1_ instanceof RoleProfessionRankList.Data) assign((RoleProfessionRankList.Data)_o1_);
		else if (_o1_ instanceof RoleProfessionRankList.Const) assign(((RoleProfessionRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleProfessionRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.RoleProfessionRankRecord>();
		for (xbean.RoleProfessionRankRecord _v_ : _o_.records)
			records.add(new RoleProfessionRankRecord(_v_, this, "records"));
	}

	private void assign(RoleProfessionRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.RoleProfessionRankRecord>();
		for (xbean.RoleProfessionRankRecord _v_ : _o_.records)
			records.add(new RoleProfessionRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.RoleProfessionRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RoleProfessionRankRecord _v_ = new RoleProfessionRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RoleProfessionRankList copy() {
		_xdb_verify_unsafe_();
		return new RoleProfessionRankList(this);
	}

	@Override
	public xbean.RoleProfessionRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleProfessionRankList toBean() {
		_xdb_verify_unsafe_();
		return new RoleProfessionRankList(this); // same as copy()
	}

	@Override
	public xbean.RoleProfessionRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleProfessionRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.RoleProfessionRankRecord> getRecords() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.RoleProfessionRankRecord> getRecordsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.RoleProfessionRankRecord> records;
		RoleProfessionRankList _o_ = this;
		records = new java.util.LinkedList<xbean.RoleProfessionRankRecord>();
		for (xbean.RoleProfessionRankRecord _v_ : _o_.records)
			records.add(new RoleProfessionRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleProfessionRankList _o_ = null;
		if ( _o1_ instanceof RoleProfessionRankList ) _o_ = (RoleProfessionRankList)_o1_;
		else if ( _o1_ instanceof RoleProfessionRankList.Const ) _o_ = ((RoleProfessionRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.RoleProfessionRankList {
		RoleProfessionRankList nThis() {
			return RoleProfessionRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleProfessionRankList copy() {
			return RoleProfessionRankList.this.copy();
		}

		@Override
		public xbean.RoleProfessionRankList toData() {
			return RoleProfessionRankList.this.toData();
		}

		public xbean.RoleProfessionRankList toBean() {
			return RoleProfessionRankList.this.toBean();
		}

		@Override
		public xbean.RoleProfessionRankList toDataIf() {
			return RoleProfessionRankList.this.toDataIf();
		}

		public xbean.RoleProfessionRankList toBeanIf() {
			return RoleProfessionRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.RoleProfessionRankRecord> getRecords() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.RoleProfessionRankRecord> getRecordsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.RoleProfessionRankRecord> records;
			RoleProfessionRankList _o_ = RoleProfessionRankList.this;
		records = new java.util.LinkedList<xbean.RoleProfessionRankRecord>();
		for (xbean.RoleProfessionRankRecord _v_ : _o_.records)
			records.add(new RoleProfessionRankRecord.Data(_v_));
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
			return RoleProfessionRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleProfessionRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleProfessionRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleProfessionRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleProfessionRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleProfessionRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleProfessionRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleProfessionRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleProfessionRankList.this.toString();
		}

	}

	public static final class Data implements xbean.RoleProfessionRankList {
		private java.util.LinkedList<xbean.RoleProfessionRankRecord> records; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.RoleProfessionRankRecord>();
		}

		Data(xbean.RoleProfessionRankList _o1_) {
			if (_o1_ instanceof RoleProfessionRankList) assign((RoleProfessionRankList)_o1_);
			else if (_o1_ instanceof RoleProfessionRankList.Data) assign((RoleProfessionRankList.Data)_o1_);
			else if (_o1_ instanceof RoleProfessionRankList.Const) assign(((RoleProfessionRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleProfessionRankList _o_) {
			records = new java.util.LinkedList<xbean.RoleProfessionRankRecord>();
			for (xbean.RoleProfessionRankRecord _v_ : _o_.records)
				records.add(new RoleProfessionRankRecord.Data(_v_));
		}

		private void assign(RoleProfessionRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.RoleProfessionRankRecord>();
			for (xbean.RoleProfessionRankRecord _v_ : _o_.records)
				records.add(new RoleProfessionRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.RoleProfessionRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RoleProfessionRankRecord _v_ = xbean.Pod.newRoleProfessionRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RoleProfessionRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleProfessionRankList toData() {
			return new Data(this);
		}

		public xbean.RoleProfessionRankList toBean() {
			return new RoleProfessionRankList(this, null, null);
		}

		@Override
		public xbean.RoleProfessionRankList toDataIf() {
			return this;
		}

		public xbean.RoleProfessionRankList toBeanIf() {
			return new RoleProfessionRankList(this, null, null);
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
		public java.util.List<xbean.RoleProfessionRankRecord> getRecords() { // 
			return records;
		}

		@Override
		public java.util.List<xbean.RoleProfessionRankRecord> getRecordsAsData() { // 
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleProfessionRankList.Data)) return false;
			RoleProfessionRankList.Data _o_ = (RoleProfessionRankList.Data) _o1_;
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

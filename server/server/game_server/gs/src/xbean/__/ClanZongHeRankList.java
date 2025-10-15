
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanZongHeRankList extends mkdb.XBean implements xbean.ClanZongHeRankList {
	private java.util.LinkedList<xbean.ClanZongHeRankRecord> records; // 

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	ClanZongHeRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.ClanZongHeRankRecord>();
	}

	public ClanZongHeRankList() {
		this(0, null, null);
	}

	public ClanZongHeRankList(ClanZongHeRankList _o_) {
		this(_o_, null, null);
	}

	ClanZongHeRankList(xbean.ClanZongHeRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanZongHeRankList) assign((ClanZongHeRankList)_o1_);
		else if (_o1_ instanceof ClanZongHeRankList.Data) assign((ClanZongHeRankList.Data)_o1_);
		else if (_o1_ instanceof ClanZongHeRankList.Const) assign(((ClanZongHeRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanZongHeRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.ClanZongHeRankRecord>();
		for (xbean.ClanZongHeRankRecord _v_ : _o_.records)
			records.add(new ClanZongHeRankRecord(_v_, this, "records"));
	}

	private void assign(ClanZongHeRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.ClanZongHeRankRecord>();
		for (xbean.ClanZongHeRankRecord _v_ : _o_.records)
			records.add(new ClanZongHeRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.ClanZongHeRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ClanZongHeRankRecord _v_ = new ClanZongHeRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ClanZongHeRankList copy() {
		_xdb_verify_unsafe_();
		return new ClanZongHeRankList(this);
	}

	@Override
	public xbean.ClanZongHeRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanZongHeRankList toBean() {
		_xdb_verify_unsafe_();
		return new ClanZongHeRankList(this); // same as copy()
	}

	@Override
	public xbean.ClanZongHeRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanZongHeRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.ClanZongHeRankRecord> getRecords() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.ClanZongHeRankRecord> getRecordsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.ClanZongHeRankRecord> records;
		ClanZongHeRankList _o_ = this;
		records = new java.util.LinkedList<xbean.ClanZongHeRankRecord>();
		for (xbean.ClanZongHeRankRecord _v_ : _o_.records)
			records.add(new ClanZongHeRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanZongHeRankList _o_ = null;
		if ( _o1_ instanceof ClanZongHeRankList ) _o_ = (ClanZongHeRankList)_o1_;
		else if ( _o1_ instanceof ClanZongHeRankList.Const ) _o_ = ((ClanZongHeRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.ClanZongHeRankList {
		ClanZongHeRankList nThis() {
			return ClanZongHeRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanZongHeRankList copy() {
			return ClanZongHeRankList.this.copy();
		}

		@Override
		public xbean.ClanZongHeRankList toData() {
			return ClanZongHeRankList.this.toData();
		}

		public xbean.ClanZongHeRankList toBean() {
			return ClanZongHeRankList.this.toBean();
		}

		@Override
		public xbean.ClanZongHeRankList toDataIf() {
			return ClanZongHeRankList.this.toDataIf();
		}

		public xbean.ClanZongHeRankList toBeanIf() {
			return ClanZongHeRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.ClanZongHeRankRecord> getRecords() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.ClanZongHeRankRecord> getRecordsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.ClanZongHeRankRecord> records;
			ClanZongHeRankList _o_ = ClanZongHeRankList.this;
		records = new java.util.LinkedList<xbean.ClanZongHeRankRecord>();
		for (xbean.ClanZongHeRankRecord _v_ : _o_.records)
			records.add(new ClanZongHeRankRecord.Data(_v_));
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
			return ClanZongHeRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanZongHeRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanZongHeRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanZongHeRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanZongHeRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanZongHeRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanZongHeRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanZongHeRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanZongHeRankList.this.toString();
		}

	}

	public static final class Data implements xbean.ClanZongHeRankList {
		private java.util.LinkedList<xbean.ClanZongHeRankRecord> records; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.ClanZongHeRankRecord>();
		}

		Data(xbean.ClanZongHeRankList _o1_) {
			if (_o1_ instanceof ClanZongHeRankList) assign((ClanZongHeRankList)_o1_);
			else if (_o1_ instanceof ClanZongHeRankList.Data) assign((ClanZongHeRankList.Data)_o1_);
			else if (_o1_ instanceof ClanZongHeRankList.Const) assign(((ClanZongHeRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanZongHeRankList _o_) {
			records = new java.util.LinkedList<xbean.ClanZongHeRankRecord>();
			for (xbean.ClanZongHeRankRecord _v_ : _o_.records)
				records.add(new ClanZongHeRankRecord.Data(_v_));
		}

		private void assign(ClanZongHeRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.ClanZongHeRankRecord>();
			for (xbean.ClanZongHeRankRecord _v_ : _o_.records)
				records.add(new ClanZongHeRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.ClanZongHeRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ClanZongHeRankRecord _v_ = xbean.Pod.newClanZongHeRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ClanZongHeRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanZongHeRankList toData() {
			return new Data(this);
		}

		public xbean.ClanZongHeRankList toBean() {
			return new ClanZongHeRankList(this, null, null);
		}

		@Override
		public xbean.ClanZongHeRankList toDataIf() {
			return this;
		}

		public xbean.ClanZongHeRankList toBeanIf() {
			return new ClanZongHeRankList(this, null, null);
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
		public java.util.List<xbean.ClanZongHeRankRecord> getRecords() { // 
			return records;
		}

		@Override
		public java.util.List<xbean.ClanZongHeRankRecord> getRecordsAsData() { // 
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanZongHeRankList.Data)) return false;
			ClanZongHeRankList.Data _o_ = (ClanZongHeRankList.Data) _o1_;
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

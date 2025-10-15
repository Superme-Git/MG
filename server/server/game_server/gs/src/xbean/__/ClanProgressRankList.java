
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanProgressRankList extends mkdb.XBean implements xbean.ClanProgressRankList {
	private java.util.LinkedList<xbean.ClanProgressRankRecord> records; // 

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	ClanProgressRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.ClanProgressRankRecord>();
	}

	public ClanProgressRankList() {
		this(0, null, null);
	}

	public ClanProgressRankList(ClanProgressRankList _o_) {
		this(_o_, null, null);
	}

	ClanProgressRankList(xbean.ClanProgressRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanProgressRankList) assign((ClanProgressRankList)_o1_);
		else if (_o1_ instanceof ClanProgressRankList.Data) assign((ClanProgressRankList.Data)_o1_);
		else if (_o1_ instanceof ClanProgressRankList.Const) assign(((ClanProgressRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanProgressRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.ClanProgressRankRecord>();
		for (xbean.ClanProgressRankRecord _v_ : _o_.records)
			records.add(new ClanProgressRankRecord(_v_, this, "records"));
	}

	private void assign(ClanProgressRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.ClanProgressRankRecord>();
		for (xbean.ClanProgressRankRecord _v_ : _o_.records)
			records.add(new ClanProgressRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.ClanProgressRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ClanProgressRankRecord _v_ = new ClanProgressRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ClanProgressRankList copy() {
		_xdb_verify_unsafe_();
		return new ClanProgressRankList(this);
	}

	@Override
	public xbean.ClanProgressRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanProgressRankList toBean() {
		_xdb_verify_unsafe_();
		return new ClanProgressRankList(this); // same as copy()
	}

	@Override
	public xbean.ClanProgressRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanProgressRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.ClanProgressRankRecord> getRecords() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.ClanProgressRankRecord> getRecordsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.ClanProgressRankRecord> records;
		ClanProgressRankList _o_ = this;
		records = new java.util.LinkedList<xbean.ClanProgressRankRecord>();
		for (xbean.ClanProgressRankRecord _v_ : _o_.records)
			records.add(new ClanProgressRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanProgressRankList _o_ = null;
		if ( _o1_ instanceof ClanProgressRankList ) _o_ = (ClanProgressRankList)_o1_;
		else if ( _o1_ instanceof ClanProgressRankList.Const ) _o_ = ((ClanProgressRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.ClanProgressRankList {
		ClanProgressRankList nThis() {
			return ClanProgressRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanProgressRankList copy() {
			return ClanProgressRankList.this.copy();
		}

		@Override
		public xbean.ClanProgressRankList toData() {
			return ClanProgressRankList.this.toData();
		}

		public xbean.ClanProgressRankList toBean() {
			return ClanProgressRankList.this.toBean();
		}

		@Override
		public xbean.ClanProgressRankList toDataIf() {
			return ClanProgressRankList.this.toDataIf();
		}

		public xbean.ClanProgressRankList toBeanIf() {
			return ClanProgressRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.ClanProgressRankRecord> getRecords() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.ClanProgressRankRecord> getRecordsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.ClanProgressRankRecord> records;
			ClanProgressRankList _o_ = ClanProgressRankList.this;
		records = new java.util.LinkedList<xbean.ClanProgressRankRecord>();
		for (xbean.ClanProgressRankRecord _v_ : _o_.records)
			records.add(new ClanProgressRankRecord.Data(_v_));
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
			return ClanProgressRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanProgressRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanProgressRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanProgressRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanProgressRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanProgressRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanProgressRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanProgressRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanProgressRankList.this.toString();
		}

	}

	public static final class Data implements xbean.ClanProgressRankList {
		private java.util.LinkedList<xbean.ClanProgressRankRecord> records; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.ClanProgressRankRecord>();
		}

		Data(xbean.ClanProgressRankList _o1_) {
			if (_o1_ instanceof ClanProgressRankList) assign((ClanProgressRankList)_o1_);
			else if (_o1_ instanceof ClanProgressRankList.Data) assign((ClanProgressRankList.Data)_o1_);
			else if (_o1_ instanceof ClanProgressRankList.Const) assign(((ClanProgressRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanProgressRankList _o_) {
			records = new java.util.LinkedList<xbean.ClanProgressRankRecord>();
			for (xbean.ClanProgressRankRecord _v_ : _o_.records)
				records.add(new ClanProgressRankRecord.Data(_v_));
		}

		private void assign(ClanProgressRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.ClanProgressRankRecord>();
			for (xbean.ClanProgressRankRecord _v_ : _o_.records)
				records.add(new ClanProgressRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.ClanProgressRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ClanProgressRankRecord _v_ = xbean.Pod.newClanProgressRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ClanProgressRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanProgressRankList toData() {
			return new Data(this);
		}

		public xbean.ClanProgressRankList toBean() {
			return new ClanProgressRankList(this, null, null);
		}

		@Override
		public xbean.ClanProgressRankList toDataIf() {
			return this;
		}

		public xbean.ClanProgressRankList toBeanIf() {
			return new ClanProgressRankList(this, null, null);
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
		public java.util.List<xbean.ClanProgressRankRecord> getRecords() { // 
			return records;
		}

		@Override
		public java.util.List<xbean.ClanProgressRankRecord> getRecordsAsData() { // 
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanProgressRankList.Data)) return false;
			ClanProgressRankList.Data _o_ = (ClanProgressRankList.Data) _o1_;
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

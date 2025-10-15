
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanRankList extends mkdb.XBean implements xbean.ClanRankList {
	private java.util.LinkedList<xbean.ClanRankRecord> records; // 所有的记录

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	ClanRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.ClanRankRecord>();
	}

	public ClanRankList() {
		this(0, null, null);
	}

	public ClanRankList(ClanRankList _o_) {
		this(_o_, null, null);
	}

	ClanRankList(xbean.ClanRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanRankList) assign((ClanRankList)_o1_);
		else if (_o1_ instanceof ClanRankList.Data) assign((ClanRankList.Data)_o1_);
		else if (_o1_ instanceof ClanRankList.Const) assign(((ClanRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.ClanRankRecord>();
		for (xbean.ClanRankRecord _v_ : _o_.records)
			records.add(new ClanRankRecord(_v_, this, "records"));
	}

	private void assign(ClanRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.ClanRankRecord>();
		for (xbean.ClanRankRecord _v_ : _o_.records)
			records.add(new ClanRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.ClanRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ClanRankRecord _v_ = new ClanRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ClanRankList copy() {
		_xdb_verify_unsafe_();
		return new ClanRankList(this);
	}

	@Override
	public xbean.ClanRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanRankList toBean() {
		_xdb_verify_unsafe_();
		return new ClanRankList(this); // same as copy()
	}

	@Override
	public xbean.ClanRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.ClanRankRecord> getRecords() { // 所有的记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.ClanRankRecord> getRecordsAsData() { // 所有的记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.ClanRankRecord> records;
		ClanRankList _o_ = this;
		records = new java.util.LinkedList<xbean.ClanRankRecord>();
		for (xbean.ClanRankRecord _v_ : _o_.records)
			records.add(new ClanRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanRankList _o_ = null;
		if ( _o1_ instanceof ClanRankList ) _o_ = (ClanRankList)_o1_;
		else if ( _o1_ instanceof ClanRankList.Const ) _o_ = ((ClanRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.ClanRankList {
		ClanRankList nThis() {
			return ClanRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanRankList copy() {
			return ClanRankList.this.copy();
		}

		@Override
		public xbean.ClanRankList toData() {
			return ClanRankList.this.toData();
		}

		public xbean.ClanRankList toBean() {
			return ClanRankList.this.toBean();
		}

		@Override
		public xbean.ClanRankList toDataIf() {
			return ClanRankList.this.toDataIf();
		}

		public xbean.ClanRankList toBeanIf() {
			return ClanRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.ClanRankRecord> getRecords() { // 所有的记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.ClanRankRecord> getRecordsAsData() { // 所有的记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.ClanRankRecord> records;
			ClanRankList _o_ = ClanRankList.this;
		records = new java.util.LinkedList<xbean.ClanRankRecord>();
		for (xbean.ClanRankRecord _v_ : _o_.records)
			records.add(new ClanRankRecord.Data(_v_));
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
			return ClanRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanRankList.this.toString();
		}

	}

	public static final class Data implements xbean.ClanRankList {
		private java.util.LinkedList<xbean.ClanRankRecord> records; // 所有的记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.ClanRankRecord>();
		}

		Data(xbean.ClanRankList _o1_) {
			if (_o1_ instanceof ClanRankList) assign((ClanRankList)_o1_);
			else if (_o1_ instanceof ClanRankList.Data) assign((ClanRankList.Data)_o1_);
			else if (_o1_ instanceof ClanRankList.Const) assign(((ClanRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanRankList _o_) {
			records = new java.util.LinkedList<xbean.ClanRankRecord>();
			for (xbean.ClanRankRecord _v_ : _o_.records)
				records.add(new ClanRankRecord.Data(_v_));
		}

		private void assign(ClanRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.ClanRankRecord>();
			for (xbean.ClanRankRecord _v_ : _o_.records)
				records.add(new ClanRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.ClanRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ClanRankRecord _v_ = xbean.Pod.newClanRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ClanRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanRankList toData() {
			return new Data(this);
		}

		public xbean.ClanRankList toBean() {
			return new ClanRankList(this, null, null);
		}

		@Override
		public xbean.ClanRankList toDataIf() {
			return this;
		}

		public xbean.ClanRankList toBeanIf() {
			return new ClanRankList(this, null, null);
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
		public java.util.List<xbean.ClanRankRecord> getRecords() { // 所有的记录
			return records;
		}

		@Override
		public java.util.List<xbean.ClanRankRecord> getRecordsAsData() { // 所有的记录
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanRankList.Data)) return false;
			ClanRankList.Data _o_ = (ClanRankList.Data) _o1_;
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

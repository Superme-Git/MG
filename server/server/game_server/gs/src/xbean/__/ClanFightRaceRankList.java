
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanFightRaceRankList extends mkdb.XBean implements xbean.ClanFightRaceRankList {
	private java.util.LinkedList<xbean.ClanFightRaceRankRecord> records; // 送花记录 by changhao

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	ClanFightRaceRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.ClanFightRaceRankRecord>();
	}

	public ClanFightRaceRankList() {
		this(0, null, null);
	}

	public ClanFightRaceRankList(ClanFightRaceRankList _o_) {
		this(_o_, null, null);
	}

	ClanFightRaceRankList(xbean.ClanFightRaceRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanFightRaceRankList) assign((ClanFightRaceRankList)_o1_);
		else if (_o1_ instanceof ClanFightRaceRankList.Data) assign((ClanFightRaceRankList.Data)_o1_);
		else if (_o1_ instanceof ClanFightRaceRankList.Const) assign(((ClanFightRaceRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanFightRaceRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.ClanFightRaceRankRecord>();
		for (xbean.ClanFightRaceRankRecord _v_ : _o_.records)
			records.add(new ClanFightRaceRankRecord(_v_, this, "records"));
	}

	private void assign(ClanFightRaceRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.ClanFightRaceRankRecord>();
		for (xbean.ClanFightRaceRankRecord _v_ : _o_.records)
			records.add(new ClanFightRaceRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.ClanFightRaceRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ClanFightRaceRankRecord _v_ = new ClanFightRaceRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ClanFightRaceRankList copy() {
		_xdb_verify_unsafe_();
		return new ClanFightRaceRankList(this);
	}

	@Override
	public xbean.ClanFightRaceRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFightRaceRankList toBean() {
		_xdb_verify_unsafe_();
		return new ClanFightRaceRankList(this); // same as copy()
	}

	@Override
	public xbean.ClanFightRaceRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFightRaceRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.ClanFightRaceRankRecord> getRecords() { // 送花记录 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.ClanFightRaceRankRecord> getRecordsAsData() { // 送花记录 by changhao
		_xdb_verify_unsafe_();
		java.util.List<xbean.ClanFightRaceRankRecord> records;
		ClanFightRaceRankList _o_ = this;
		records = new java.util.LinkedList<xbean.ClanFightRaceRankRecord>();
		for (xbean.ClanFightRaceRankRecord _v_ : _o_.records)
			records.add(new ClanFightRaceRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanFightRaceRankList _o_ = null;
		if ( _o1_ instanceof ClanFightRaceRankList ) _o_ = (ClanFightRaceRankList)_o1_;
		else if ( _o1_ instanceof ClanFightRaceRankList.Const ) _o_ = ((ClanFightRaceRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.ClanFightRaceRankList {
		ClanFightRaceRankList nThis() {
			return ClanFightRaceRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanFightRaceRankList copy() {
			return ClanFightRaceRankList.this.copy();
		}

		@Override
		public xbean.ClanFightRaceRankList toData() {
			return ClanFightRaceRankList.this.toData();
		}

		public xbean.ClanFightRaceRankList toBean() {
			return ClanFightRaceRankList.this.toBean();
		}

		@Override
		public xbean.ClanFightRaceRankList toDataIf() {
			return ClanFightRaceRankList.this.toDataIf();
		}

		public xbean.ClanFightRaceRankList toBeanIf() {
			return ClanFightRaceRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.ClanFightRaceRankRecord> getRecords() { // 送花记录 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.ClanFightRaceRankRecord> getRecordsAsData() { // 送花记录 by changhao
			_xdb_verify_unsafe_();
			java.util.List<xbean.ClanFightRaceRankRecord> records;
			ClanFightRaceRankList _o_ = ClanFightRaceRankList.this;
		records = new java.util.LinkedList<xbean.ClanFightRaceRankRecord>();
		for (xbean.ClanFightRaceRankRecord _v_ : _o_.records)
			records.add(new ClanFightRaceRankRecord.Data(_v_));
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
			return ClanFightRaceRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanFightRaceRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanFightRaceRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanFightRaceRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanFightRaceRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanFightRaceRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanFightRaceRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanFightRaceRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanFightRaceRankList.this.toString();
		}

	}

	public static final class Data implements xbean.ClanFightRaceRankList {
		private java.util.LinkedList<xbean.ClanFightRaceRankRecord> records; // 送花记录 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.ClanFightRaceRankRecord>();
		}

		Data(xbean.ClanFightRaceRankList _o1_) {
			if (_o1_ instanceof ClanFightRaceRankList) assign((ClanFightRaceRankList)_o1_);
			else if (_o1_ instanceof ClanFightRaceRankList.Data) assign((ClanFightRaceRankList.Data)_o1_);
			else if (_o1_ instanceof ClanFightRaceRankList.Const) assign(((ClanFightRaceRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanFightRaceRankList _o_) {
			records = new java.util.LinkedList<xbean.ClanFightRaceRankRecord>();
			for (xbean.ClanFightRaceRankRecord _v_ : _o_.records)
				records.add(new ClanFightRaceRankRecord.Data(_v_));
		}

		private void assign(ClanFightRaceRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.ClanFightRaceRankRecord>();
			for (xbean.ClanFightRaceRankRecord _v_ : _o_.records)
				records.add(new ClanFightRaceRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.ClanFightRaceRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ClanFightRaceRankRecord _v_ = xbean.Pod.newClanFightRaceRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ClanFightRaceRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanFightRaceRankList toData() {
			return new Data(this);
		}

		public xbean.ClanFightRaceRankList toBean() {
			return new ClanFightRaceRankList(this, null, null);
		}

		@Override
		public xbean.ClanFightRaceRankList toDataIf() {
			return this;
		}

		public xbean.ClanFightRaceRankList toBeanIf() {
			return new ClanFightRaceRankList(this, null, null);
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
		public java.util.List<xbean.ClanFightRaceRankRecord> getRecords() { // 送花记录 by changhao
			return records;
		}

		@Override
		public java.util.List<xbean.ClanFightRaceRankRecord> getRecordsAsData() { // 送花记录 by changhao
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanFightRaceRankList.Data)) return false;
			ClanFightRaceRankList.Data _o_ = (ClanFightRaceRankList.Data) _o1_;
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

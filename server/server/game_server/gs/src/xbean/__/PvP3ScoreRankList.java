
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PvP3ScoreRankList extends mkdb.XBean implements xbean.PvP3ScoreRankList {
	private java.util.LinkedList<xbean.PvP3ScoreRecord> records; // 所有的记录

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	PvP3ScoreRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.PvP3ScoreRecord>();
	}

	public PvP3ScoreRankList() {
		this(0, null, null);
	}

	public PvP3ScoreRankList(PvP3ScoreRankList _o_) {
		this(_o_, null, null);
	}

	PvP3ScoreRankList(xbean.PvP3ScoreRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PvP3ScoreRankList) assign((PvP3ScoreRankList)_o1_);
		else if (_o1_ instanceof PvP3ScoreRankList.Data) assign((PvP3ScoreRankList.Data)_o1_);
		else if (_o1_ instanceof PvP3ScoreRankList.Const) assign(((PvP3ScoreRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PvP3ScoreRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.PvP3ScoreRecord>();
		for (xbean.PvP3ScoreRecord _v_ : _o_.records)
			records.add(new PvP3ScoreRecord(_v_, this, "records"));
	}

	private void assign(PvP3ScoreRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.PvP3ScoreRecord>();
		for (xbean.PvP3ScoreRecord _v_ : _o_.records)
			records.add(new PvP3ScoreRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.PvP3ScoreRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.PvP3ScoreRecord _v_ = new PvP3ScoreRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.PvP3ScoreRankList copy() {
		_xdb_verify_unsafe_();
		return new PvP3ScoreRankList(this);
	}

	@Override
	public xbean.PvP3ScoreRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP3ScoreRankList toBean() {
		_xdb_verify_unsafe_();
		return new PvP3ScoreRankList(this); // same as copy()
	}

	@Override
	public xbean.PvP3ScoreRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP3ScoreRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.PvP3ScoreRecord> getRecords() { // 所有的记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.PvP3ScoreRecord> getRecordsAsData() { // 所有的记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.PvP3ScoreRecord> records;
		PvP3ScoreRankList _o_ = this;
		records = new java.util.LinkedList<xbean.PvP3ScoreRecord>();
		for (xbean.PvP3ScoreRecord _v_ : _o_.records)
			records.add(new PvP3ScoreRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PvP3ScoreRankList _o_ = null;
		if ( _o1_ instanceof PvP3ScoreRankList ) _o_ = (PvP3ScoreRankList)_o1_;
		else if ( _o1_ instanceof PvP3ScoreRankList.Const ) _o_ = ((PvP3ScoreRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.PvP3ScoreRankList {
		PvP3ScoreRankList nThis() {
			return PvP3ScoreRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PvP3ScoreRankList copy() {
			return PvP3ScoreRankList.this.copy();
		}

		@Override
		public xbean.PvP3ScoreRankList toData() {
			return PvP3ScoreRankList.this.toData();
		}

		public xbean.PvP3ScoreRankList toBean() {
			return PvP3ScoreRankList.this.toBean();
		}

		@Override
		public xbean.PvP3ScoreRankList toDataIf() {
			return PvP3ScoreRankList.this.toDataIf();
		}

		public xbean.PvP3ScoreRankList toBeanIf() {
			return PvP3ScoreRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.PvP3ScoreRecord> getRecords() { // 所有的记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.PvP3ScoreRecord> getRecordsAsData() { // 所有的记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.PvP3ScoreRecord> records;
			PvP3ScoreRankList _o_ = PvP3ScoreRankList.this;
		records = new java.util.LinkedList<xbean.PvP3ScoreRecord>();
		for (xbean.PvP3ScoreRecord _v_ : _o_.records)
			records.add(new PvP3ScoreRecord.Data(_v_));
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
			return PvP3ScoreRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PvP3ScoreRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PvP3ScoreRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PvP3ScoreRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PvP3ScoreRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PvP3ScoreRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PvP3ScoreRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PvP3ScoreRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return PvP3ScoreRankList.this.toString();
		}

	}

	public static final class Data implements xbean.PvP3ScoreRankList {
		private java.util.LinkedList<xbean.PvP3ScoreRecord> records; // 所有的记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.PvP3ScoreRecord>();
		}

		Data(xbean.PvP3ScoreRankList _o1_) {
			if (_o1_ instanceof PvP3ScoreRankList) assign((PvP3ScoreRankList)_o1_);
			else if (_o1_ instanceof PvP3ScoreRankList.Data) assign((PvP3ScoreRankList.Data)_o1_);
			else if (_o1_ instanceof PvP3ScoreRankList.Const) assign(((PvP3ScoreRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PvP3ScoreRankList _o_) {
			records = new java.util.LinkedList<xbean.PvP3ScoreRecord>();
			for (xbean.PvP3ScoreRecord _v_ : _o_.records)
				records.add(new PvP3ScoreRecord.Data(_v_));
		}

		private void assign(PvP3ScoreRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.PvP3ScoreRecord>();
			for (xbean.PvP3ScoreRecord _v_ : _o_.records)
				records.add(new PvP3ScoreRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.PvP3ScoreRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.PvP3ScoreRecord _v_ = xbean.Pod.newPvP3ScoreRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.PvP3ScoreRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.PvP3ScoreRankList toData() {
			return new Data(this);
		}

		public xbean.PvP3ScoreRankList toBean() {
			return new PvP3ScoreRankList(this, null, null);
		}

		@Override
		public xbean.PvP3ScoreRankList toDataIf() {
			return this;
		}

		public xbean.PvP3ScoreRankList toBeanIf() {
			return new PvP3ScoreRankList(this, null, null);
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
		public java.util.List<xbean.PvP3ScoreRecord> getRecords() { // 所有的记录
			return records;
		}

		@Override
		public java.util.List<xbean.PvP3ScoreRecord> getRecordsAsData() { // 所有的记录
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PvP3ScoreRankList.Data)) return false;
			PvP3ScoreRankList.Data _o_ = (PvP3ScoreRankList.Data) _o1_;
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


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PetScoreRankList extends mkdb.XBean implements xbean.PetScoreRankList {
	private java.util.LinkedList<xbean.PetScoreListRecord> records; // 所有的记录

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	PetScoreRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.PetScoreListRecord>();
	}

	public PetScoreRankList() {
		this(0, null, null);
	}

	public PetScoreRankList(PetScoreRankList _o_) {
		this(_o_, null, null);
	}

	PetScoreRankList(xbean.PetScoreRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PetScoreRankList) assign((PetScoreRankList)_o1_);
		else if (_o1_ instanceof PetScoreRankList.Data) assign((PetScoreRankList.Data)_o1_);
		else if (_o1_ instanceof PetScoreRankList.Const) assign(((PetScoreRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PetScoreRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.PetScoreListRecord>();
		for (xbean.PetScoreListRecord _v_ : _o_.records)
			records.add(new PetScoreListRecord(_v_, this, "records"));
	}

	private void assign(PetScoreRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.PetScoreListRecord>();
		for (xbean.PetScoreListRecord _v_ : _o_.records)
			records.add(new PetScoreListRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.PetScoreListRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.PetScoreListRecord _v_ = new PetScoreListRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.PetScoreRankList copy() {
		_xdb_verify_unsafe_();
		return new PetScoreRankList(this);
	}

	@Override
	public xbean.PetScoreRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetScoreRankList toBean() {
		_xdb_verify_unsafe_();
		return new PetScoreRankList(this); // same as copy()
	}

	@Override
	public xbean.PetScoreRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetScoreRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.PetScoreListRecord> getRecords() { // 所有的记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.PetScoreListRecord> getRecordsAsData() { // 所有的记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.PetScoreListRecord> records;
		PetScoreRankList _o_ = this;
		records = new java.util.LinkedList<xbean.PetScoreListRecord>();
		for (xbean.PetScoreListRecord _v_ : _o_.records)
			records.add(new PetScoreListRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PetScoreRankList _o_ = null;
		if ( _o1_ instanceof PetScoreRankList ) _o_ = (PetScoreRankList)_o1_;
		else if ( _o1_ instanceof PetScoreRankList.Const ) _o_ = ((PetScoreRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.PetScoreRankList {
		PetScoreRankList nThis() {
			return PetScoreRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PetScoreRankList copy() {
			return PetScoreRankList.this.copy();
		}

		@Override
		public xbean.PetScoreRankList toData() {
			return PetScoreRankList.this.toData();
		}

		public xbean.PetScoreRankList toBean() {
			return PetScoreRankList.this.toBean();
		}

		@Override
		public xbean.PetScoreRankList toDataIf() {
			return PetScoreRankList.this.toDataIf();
		}

		public xbean.PetScoreRankList toBeanIf() {
			return PetScoreRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.PetScoreListRecord> getRecords() { // 所有的记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.PetScoreListRecord> getRecordsAsData() { // 所有的记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.PetScoreListRecord> records;
			PetScoreRankList _o_ = PetScoreRankList.this;
		records = new java.util.LinkedList<xbean.PetScoreListRecord>();
		for (xbean.PetScoreListRecord _v_ : _o_.records)
			records.add(new PetScoreListRecord.Data(_v_));
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
			return PetScoreRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PetScoreRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PetScoreRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PetScoreRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PetScoreRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PetScoreRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PetScoreRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PetScoreRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return PetScoreRankList.this.toString();
		}

	}

	public static final class Data implements xbean.PetScoreRankList {
		private java.util.LinkedList<xbean.PetScoreListRecord> records; // 所有的记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.PetScoreListRecord>();
		}

		Data(xbean.PetScoreRankList _o1_) {
			if (_o1_ instanceof PetScoreRankList) assign((PetScoreRankList)_o1_);
			else if (_o1_ instanceof PetScoreRankList.Data) assign((PetScoreRankList.Data)_o1_);
			else if (_o1_ instanceof PetScoreRankList.Const) assign(((PetScoreRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PetScoreRankList _o_) {
			records = new java.util.LinkedList<xbean.PetScoreListRecord>();
			for (xbean.PetScoreListRecord _v_ : _o_.records)
				records.add(new PetScoreListRecord.Data(_v_));
		}

		private void assign(PetScoreRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.PetScoreListRecord>();
			for (xbean.PetScoreListRecord _v_ : _o_.records)
				records.add(new PetScoreListRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.PetScoreListRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.PetScoreListRecord _v_ = xbean.Pod.newPetScoreListRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.PetScoreRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.PetScoreRankList toData() {
			return new Data(this);
		}

		public xbean.PetScoreRankList toBean() {
			return new PetScoreRankList(this, null, null);
		}

		@Override
		public xbean.PetScoreRankList toDataIf() {
			return this;
		}

		public xbean.PetScoreRankList toBeanIf() {
			return new PetScoreRankList(this, null, null);
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
		public java.util.List<xbean.PetScoreListRecord> getRecords() { // 所有的记录
			return records;
		}

		@Override
		public java.util.List<xbean.PetScoreListRecord> getRecordsAsData() { // 所有的记录
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PetScoreRankList.Data)) return false;
			PetScoreRankList.Data _o_ = (PetScoreRankList.Data) _o1_;
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

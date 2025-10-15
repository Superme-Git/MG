
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BingFengRankList extends mkdb.XBean implements xbean.BingFengRankList {
	private java.util.LinkedList<xbean.BingFengListRecord> records; // 所有的记录

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	BingFengRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.BingFengListRecord>();
	}

	public BingFengRankList() {
		this(0, null, null);
	}

	public BingFengRankList(BingFengRankList _o_) {
		this(_o_, null, null);
	}

	BingFengRankList(xbean.BingFengRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BingFengRankList) assign((BingFengRankList)_o1_);
		else if (_o1_ instanceof BingFengRankList.Data) assign((BingFengRankList.Data)_o1_);
		else if (_o1_ instanceof BingFengRankList.Const) assign(((BingFengRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BingFengRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.BingFengListRecord>();
		for (xbean.BingFengListRecord _v_ : _o_.records)
			records.add(new BingFengListRecord(_v_, this, "records"));
	}

	private void assign(BingFengRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.BingFengListRecord>();
		for (xbean.BingFengListRecord _v_ : _o_.records)
			records.add(new BingFengListRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.BingFengListRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.BingFengListRecord _v_ = new BingFengListRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.BingFengRankList copy() {
		_xdb_verify_unsafe_();
		return new BingFengRankList(this);
	}

	@Override
	public xbean.BingFengRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengRankList toBean() {
		_xdb_verify_unsafe_();
		return new BingFengRankList(this); // same as copy()
	}

	@Override
	public xbean.BingFengRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.BingFengListRecord> getRecords() { // 所有的记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.BingFengListRecord> getRecordsAsData() { // 所有的记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.BingFengListRecord> records;
		BingFengRankList _o_ = this;
		records = new java.util.LinkedList<xbean.BingFengListRecord>();
		for (xbean.BingFengListRecord _v_ : _o_.records)
			records.add(new BingFengListRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BingFengRankList _o_ = null;
		if ( _o1_ instanceof BingFengRankList ) _o_ = (BingFengRankList)_o1_;
		else if ( _o1_ instanceof BingFengRankList.Const ) _o_ = ((BingFengRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.BingFengRankList {
		BingFengRankList nThis() {
			return BingFengRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BingFengRankList copy() {
			return BingFengRankList.this.copy();
		}

		@Override
		public xbean.BingFengRankList toData() {
			return BingFengRankList.this.toData();
		}

		public xbean.BingFengRankList toBean() {
			return BingFengRankList.this.toBean();
		}

		@Override
		public xbean.BingFengRankList toDataIf() {
			return BingFengRankList.this.toDataIf();
		}

		public xbean.BingFengRankList toBeanIf() {
			return BingFengRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.BingFengListRecord> getRecords() { // 所有的记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.BingFengListRecord> getRecordsAsData() { // 所有的记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.BingFengListRecord> records;
			BingFengRankList _o_ = BingFengRankList.this;
		records = new java.util.LinkedList<xbean.BingFengListRecord>();
		for (xbean.BingFengListRecord _v_ : _o_.records)
			records.add(new BingFengListRecord.Data(_v_));
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
			return BingFengRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BingFengRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BingFengRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BingFengRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BingFengRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BingFengRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BingFengRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BingFengRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return BingFengRankList.this.toString();
		}

	}

	public static final class Data implements xbean.BingFengRankList {
		private java.util.LinkedList<xbean.BingFengListRecord> records; // 所有的记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.BingFengListRecord>();
		}

		Data(xbean.BingFengRankList _o1_) {
			if (_o1_ instanceof BingFengRankList) assign((BingFengRankList)_o1_);
			else if (_o1_ instanceof BingFengRankList.Data) assign((BingFengRankList.Data)_o1_);
			else if (_o1_ instanceof BingFengRankList.Const) assign(((BingFengRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BingFengRankList _o_) {
			records = new java.util.LinkedList<xbean.BingFengListRecord>();
			for (xbean.BingFengListRecord _v_ : _o_.records)
				records.add(new BingFengListRecord.Data(_v_));
		}

		private void assign(BingFengRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.BingFengListRecord>();
			for (xbean.BingFengListRecord _v_ : _o_.records)
				records.add(new BingFengListRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.BingFengListRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.BingFengListRecord _v_ = xbean.Pod.newBingFengListRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.BingFengRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.BingFengRankList toData() {
			return new Data(this);
		}

		public xbean.BingFengRankList toBean() {
			return new BingFengRankList(this, null, null);
		}

		@Override
		public xbean.BingFengRankList toDataIf() {
			return this;
		}

		public xbean.BingFengRankList toBeanIf() {
			return new BingFengRankList(this, null, null);
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
		public java.util.List<xbean.BingFengListRecord> getRecords() { // 所有的记录
			return records;
		}

		@Override
		public java.util.List<xbean.BingFengListRecord> getRecordsAsData() { // 所有的记录
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BingFengRankList.Data)) return false;
			BingFengRankList.Data _o_ = (BingFengRankList.Data) _o1_;
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

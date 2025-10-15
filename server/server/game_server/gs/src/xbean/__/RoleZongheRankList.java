
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleZongheRankList extends mkdb.XBean implements xbean.RoleZongheRankList {
	private java.util.LinkedList<xbean.RoleZongheRankRecord> records; // 所有的记录

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	RoleZongheRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.RoleZongheRankRecord>();
	}

	public RoleZongheRankList() {
		this(0, null, null);
	}

	public RoleZongheRankList(RoleZongheRankList _o_) {
		this(_o_, null, null);
	}

	RoleZongheRankList(xbean.RoleZongheRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleZongheRankList) assign((RoleZongheRankList)_o1_);
		else if (_o1_ instanceof RoleZongheRankList.Data) assign((RoleZongheRankList.Data)_o1_);
		else if (_o1_ instanceof RoleZongheRankList.Const) assign(((RoleZongheRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleZongheRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.RoleZongheRankRecord>();
		for (xbean.RoleZongheRankRecord _v_ : _o_.records)
			records.add(new RoleZongheRankRecord(_v_, this, "records"));
	}

	private void assign(RoleZongheRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.RoleZongheRankRecord>();
		for (xbean.RoleZongheRankRecord _v_ : _o_.records)
			records.add(new RoleZongheRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.RoleZongheRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RoleZongheRankRecord _v_ = new RoleZongheRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RoleZongheRankList copy() {
		_xdb_verify_unsafe_();
		return new RoleZongheRankList(this);
	}

	@Override
	public xbean.RoleZongheRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleZongheRankList toBean() {
		_xdb_verify_unsafe_();
		return new RoleZongheRankList(this); // same as copy()
	}

	@Override
	public xbean.RoleZongheRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleZongheRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.RoleZongheRankRecord> getRecords() { // 所有的记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.RoleZongheRankRecord> getRecordsAsData() { // 所有的记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.RoleZongheRankRecord> records;
		RoleZongheRankList _o_ = this;
		records = new java.util.LinkedList<xbean.RoleZongheRankRecord>();
		for (xbean.RoleZongheRankRecord _v_ : _o_.records)
			records.add(new RoleZongheRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleZongheRankList _o_ = null;
		if ( _o1_ instanceof RoleZongheRankList ) _o_ = (RoleZongheRankList)_o1_;
		else if ( _o1_ instanceof RoleZongheRankList.Const ) _o_ = ((RoleZongheRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.RoleZongheRankList {
		RoleZongheRankList nThis() {
			return RoleZongheRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleZongheRankList copy() {
			return RoleZongheRankList.this.copy();
		}

		@Override
		public xbean.RoleZongheRankList toData() {
			return RoleZongheRankList.this.toData();
		}

		public xbean.RoleZongheRankList toBean() {
			return RoleZongheRankList.this.toBean();
		}

		@Override
		public xbean.RoleZongheRankList toDataIf() {
			return RoleZongheRankList.this.toDataIf();
		}

		public xbean.RoleZongheRankList toBeanIf() {
			return RoleZongheRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.RoleZongheRankRecord> getRecords() { // 所有的记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.RoleZongheRankRecord> getRecordsAsData() { // 所有的记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.RoleZongheRankRecord> records;
			RoleZongheRankList _o_ = RoleZongheRankList.this;
		records = new java.util.LinkedList<xbean.RoleZongheRankRecord>();
		for (xbean.RoleZongheRankRecord _v_ : _o_.records)
			records.add(new RoleZongheRankRecord.Data(_v_));
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
			return RoleZongheRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleZongheRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleZongheRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleZongheRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleZongheRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleZongheRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleZongheRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleZongheRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleZongheRankList.this.toString();
		}

	}

	public static final class Data implements xbean.RoleZongheRankList {
		private java.util.LinkedList<xbean.RoleZongheRankRecord> records; // 所有的记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.RoleZongheRankRecord>();
		}

		Data(xbean.RoleZongheRankList _o1_) {
			if (_o1_ instanceof RoleZongheRankList) assign((RoleZongheRankList)_o1_);
			else if (_o1_ instanceof RoleZongheRankList.Data) assign((RoleZongheRankList.Data)_o1_);
			else if (_o1_ instanceof RoleZongheRankList.Const) assign(((RoleZongheRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleZongheRankList _o_) {
			records = new java.util.LinkedList<xbean.RoleZongheRankRecord>();
			for (xbean.RoleZongheRankRecord _v_ : _o_.records)
				records.add(new RoleZongheRankRecord.Data(_v_));
		}

		private void assign(RoleZongheRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.RoleZongheRankRecord>();
			for (xbean.RoleZongheRankRecord _v_ : _o_.records)
				records.add(new RoleZongheRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.RoleZongheRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RoleZongheRankRecord _v_ = xbean.Pod.newRoleZongheRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RoleZongheRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleZongheRankList toData() {
			return new Data(this);
		}

		public xbean.RoleZongheRankList toBean() {
			return new RoleZongheRankList(this, null, null);
		}

		@Override
		public xbean.RoleZongheRankList toDataIf() {
			return this;
		}

		public xbean.RoleZongheRankList toBeanIf() {
			return new RoleZongheRankList(this, null, null);
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
		public java.util.List<xbean.RoleZongheRankRecord> getRecords() { // 所有的记录
			return records;
		}

		@Override
		public java.util.List<xbean.RoleZongheRankRecord> getRecordsAsData() { // 所有的记录
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleZongheRankList.Data)) return false;
			RoleZongheRankList.Data _o_ = (RoleZongheRankList.Data) _o1_;
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

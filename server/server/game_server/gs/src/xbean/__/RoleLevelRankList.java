
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleLevelRankList extends mkdb.XBean implements xbean.RoleLevelRankList {
	private java.util.LinkedList<xbean.RoleLevelListRecord> records; // 所有的记录

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	RoleLevelRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.RoleLevelListRecord>();
	}

	public RoleLevelRankList() {
		this(0, null, null);
	}

	public RoleLevelRankList(RoleLevelRankList _o_) {
		this(_o_, null, null);
	}

	RoleLevelRankList(xbean.RoleLevelRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleLevelRankList) assign((RoleLevelRankList)_o1_);
		else if (_o1_ instanceof RoleLevelRankList.Data) assign((RoleLevelRankList.Data)_o1_);
		else if (_o1_ instanceof RoleLevelRankList.Const) assign(((RoleLevelRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleLevelRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.RoleLevelListRecord>();
		for (xbean.RoleLevelListRecord _v_ : _o_.records)
			records.add(new RoleLevelListRecord(_v_, this, "records"));
	}

	private void assign(RoleLevelRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.RoleLevelListRecord>();
		for (xbean.RoleLevelListRecord _v_ : _o_.records)
			records.add(new RoleLevelListRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.RoleLevelListRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RoleLevelListRecord _v_ = new RoleLevelListRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RoleLevelRankList copy() {
		_xdb_verify_unsafe_();
		return new RoleLevelRankList(this);
	}

	@Override
	public xbean.RoleLevelRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleLevelRankList toBean() {
		_xdb_verify_unsafe_();
		return new RoleLevelRankList(this); // same as copy()
	}

	@Override
	public xbean.RoleLevelRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleLevelRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.RoleLevelListRecord> getRecords() { // 所有的记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.RoleLevelListRecord> getRecordsAsData() { // 所有的记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.RoleLevelListRecord> records;
		RoleLevelRankList _o_ = this;
		records = new java.util.LinkedList<xbean.RoleLevelListRecord>();
		for (xbean.RoleLevelListRecord _v_ : _o_.records)
			records.add(new RoleLevelListRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleLevelRankList _o_ = null;
		if ( _o1_ instanceof RoleLevelRankList ) _o_ = (RoleLevelRankList)_o1_;
		else if ( _o1_ instanceof RoleLevelRankList.Const ) _o_ = ((RoleLevelRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.RoleLevelRankList {
		RoleLevelRankList nThis() {
			return RoleLevelRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleLevelRankList copy() {
			return RoleLevelRankList.this.copy();
		}

		@Override
		public xbean.RoleLevelRankList toData() {
			return RoleLevelRankList.this.toData();
		}

		public xbean.RoleLevelRankList toBean() {
			return RoleLevelRankList.this.toBean();
		}

		@Override
		public xbean.RoleLevelRankList toDataIf() {
			return RoleLevelRankList.this.toDataIf();
		}

		public xbean.RoleLevelRankList toBeanIf() {
			return RoleLevelRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.RoleLevelListRecord> getRecords() { // 所有的记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.RoleLevelListRecord> getRecordsAsData() { // 所有的记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.RoleLevelListRecord> records;
			RoleLevelRankList _o_ = RoleLevelRankList.this;
		records = new java.util.LinkedList<xbean.RoleLevelListRecord>();
		for (xbean.RoleLevelListRecord _v_ : _o_.records)
			records.add(new RoleLevelListRecord.Data(_v_));
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
			return RoleLevelRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleLevelRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleLevelRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleLevelRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleLevelRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleLevelRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleLevelRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleLevelRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleLevelRankList.this.toString();
		}

	}

	public static final class Data implements xbean.RoleLevelRankList {
		private java.util.LinkedList<xbean.RoleLevelListRecord> records; // 所有的记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.RoleLevelListRecord>();
		}

		Data(xbean.RoleLevelRankList _o1_) {
			if (_o1_ instanceof RoleLevelRankList) assign((RoleLevelRankList)_o1_);
			else if (_o1_ instanceof RoleLevelRankList.Data) assign((RoleLevelRankList.Data)_o1_);
			else if (_o1_ instanceof RoleLevelRankList.Const) assign(((RoleLevelRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleLevelRankList _o_) {
			records = new java.util.LinkedList<xbean.RoleLevelListRecord>();
			for (xbean.RoleLevelListRecord _v_ : _o_.records)
				records.add(new RoleLevelListRecord.Data(_v_));
		}

		private void assign(RoleLevelRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.RoleLevelListRecord>();
			for (xbean.RoleLevelListRecord _v_ : _o_.records)
				records.add(new RoleLevelListRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.RoleLevelListRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RoleLevelListRecord _v_ = xbean.Pod.newRoleLevelListRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RoleLevelRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleLevelRankList toData() {
			return new Data(this);
		}

		public xbean.RoleLevelRankList toBean() {
			return new RoleLevelRankList(this, null, null);
		}

		@Override
		public xbean.RoleLevelRankList toDataIf() {
			return this;
		}

		public xbean.RoleLevelRankList toBeanIf() {
			return new RoleLevelRankList(this, null, null);
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
		public java.util.List<xbean.RoleLevelListRecord> getRecords() { // 所有的记录
			return records;
		}

		@Override
		public java.util.List<xbean.RoleLevelListRecord> getRecordsAsData() { // 所有的记录
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleLevelRankList.Data)) return false;
			RoleLevelRankList.Data _o_ = (RoleLevelRankList.Data) _o1_;
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

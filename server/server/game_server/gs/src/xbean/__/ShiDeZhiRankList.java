
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ShiDeZhiRankList extends mkdb.XBean implements xbean.ShiDeZhiRankList {
	private java.util.LinkedList<xbean.MasterRankRecord> records; // 所有的记录

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	ShiDeZhiRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.MasterRankRecord>();
	}

	public ShiDeZhiRankList() {
		this(0, null, null);
	}

	public ShiDeZhiRankList(ShiDeZhiRankList _o_) {
		this(_o_, null, null);
	}

	ShiDeZhiRankList(xbean.ShiDeZhiRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ShiDeZhiRankList) assign((ShiDeZhiRankList)_o1_);
		else if (_o1_ instanceof ShiDeZhiRankList.Data) assign((ShiDeZhiRankList.Data)_o1_);
		else if (_o1_ instanceof ShiDeZhiRankList.Const) assign(((ShiDeZhiRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ShiDeZhiRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.MasterRankRecord>();
		for (xbean.MasterRankRecord _v_ : _o_.records)
			records.add(new MasterRankRecord(_v_, this, "records"));
	}

	private void assign(ShiDeZhiRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.MasterRankRecord>();
		for (xbean.MasterRankRecord _v_ : _o_.records)
			records.add(new MasterRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.MasterRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.MasterRankRecord _v_ = new MasterRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ShiDeZhiRankList copy() {
		_xdb_verify_unsafe_();
		return new ShiDeZhiRankList(this);
	}

	@Override
	public xbean.ShiDeZhiRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ShiDeZhiRankList toBean() {
		_xdb_verify_unsafe_();
		return new ShiDeZhiRankList(this); // same as copy()
	}

	@Override
	public xbean.ShiDeZhiRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ShiDeZhiRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.MasterRankRecord> getRecords() { // 所有的记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.MasterRankRecord> getRecordsAsData() { // 所有的记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.MasterRankRecord> records;
		ShiDeZhiRankList _o_ = this;
		records = new java.util.LinkedList<xbean.MasterRankRecord>();
		for (xbean.MasterRankRecord _v_ : _o_.records)
			records.add(new MasterRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ShiDeZhiRankList _o_ = null;
		if ( _o1_ instanceof ShiDeZhiRankList ) _o_ = (ShiDeZhiRankList)_o1_;
		else if ( _o1_ instanceof ShiDeZhiRankList.Const ) _o_ = ((ShiDeZhiRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.ShiDeZhiRankList {
		ShiDeZhiRankList nThis() {
			return ShiDeZhiRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ShiDeZhiRankList copy() {
			return ShiDeZhiRankList.this.copy();
		}

		@Override
		public xbean.ShiDeZhiRankList toData() {
			return ShiDeZhiRankList.this.toData();
		}

		public xbean.ShiDeZhiRankList toBean() {
			return ShiDeZhiRankList.this.toBean();
		}

		@Override
		public xbean.ShiDeZhiRankList toDataIf() {
			return ShiDeZhiRankList.this.toDataIf();
		}

		public xbean.ShiDeZhiRankList toBeanIf() {
			return ShiDeZhiRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.MasterRankRecord> getRecords() { // 所有的记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.MasterRankRecord> getRecordsAsData() { // 所有的记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.MasterRankRecord> records;
			ShiDeZhiRankList _o_ = ShiDeZhiRankList.this;
		records = new java.util.LinkedList<xbean.MasterRankRecord>();
		for (xbean.MasterRankRecord _v_ : _o_.records)
			records.add(new MasterRankRecord.Data(_v_));
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
			return ShiDeZhiRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ShiDeZhiRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ShiDeZhiRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ShiDeZhiRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ShiDeZhiRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ShiDeZhiRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ShiDeZhiRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ShiDeZhiRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return ShiDeZhiRankList.this.toString();
		}

	}

	public static final class Data implements xbean.ShiDeZhiRankList {
		private java.util.LinkedList<xbean.MasterRankRecord> records; // 所有的记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.MasterRankRecord>();
		}

		Data(xbean.ShiDeZhiRankList _o1_) {
			if (_o1_ instanceof ShiDeZhiRankList) assign((ShiDeZhiRankList)_o1_);
			else if (_o1_ instanceof ShiDeZhiRankList.Data) assign((ShiDeZhiRankList.Data)_o1_);
			else if (_o1_ instanceof ShiDeZhiRankList.Const) assign(((ShiDeZhiRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ShiDeZhiRankList _o_) {
			records = new java.util.LinkedList<xbean.MasterRankRecord>();
			for (xbean.MasterRankRecord _v_ : _o_.records)
				records.add(new MasterRankRecord.Data(_v_));
		}

		private void assign(ShiDeZhiRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.MasterRankRecord>();
			for (xbean.MasterRankRecord _v_ : _o_.records)
				records.add(new MasterRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.MasterRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.MasterRankRecord _v_ = xbean.Pod.newMasterRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ShiDeZhiRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.ShiDeZhiRankList toData() {
			return new Data(this);
		}

		public xbean.ShiDeZhiRankList toBean() {
			return new ShiDeZhiRankList(this, null, null);
		}

		@Override
		public xbean.ShiDeZhiRankList toDataIf() {
			return this;
		}

		public xbean.ShiDeZhiRankList toBeanIf() {
			return new ShiDeZhiRankList(this, null, null);
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
		public java.util.List<xbean.MasterRankRecord> getRecords() { // 所有的记录
			return records;
		}

		@Override
		public java.util.List<xbean.MasterRankRecord> getRecordsAsData() { // 所有的记录
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ShiDeZhiRankList.Data)) return false;
			ShiDeZhiRankList.Data _o_ = (ShiDeZhiRankList.Data) _o1_;
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

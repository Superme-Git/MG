
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ReceFlowerRankList extends mkdb.XBean implements xbean.ReceFlowerRankList {
	private java.util.LinkedList<xbean.ReceFlowerRecord> records; // 送花记录 by changhao

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	ReceFlowerRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.ReceFlowerRecord>();
	}

	public ReceFlowerRankList() {
		this(0, null, null);
	}

	public ReceFlowerRankList(ReceFlowerRankList _o_) {
		this(_o_, null, null);
	}

	ReceFlowerRankList(xbean.ReceFlowerRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ReceFlowerRankList) assign((ReceFlowerRankList)_o1_);
		else if (_o1_ instanceof ReceFlowerRankList.Data) assign((ReceFlowerRankList.Data)_o1_);
		else if (_o1_ instanceof ReceFlowerRankList.Const) assign(((ReceFlowerRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ReceFlowerRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.ReceFlowerRecord>();
		for (xbean.ReceFlowerRecord _v_ : _o_.records)
			records.add(new ReceFlowerRecord(_v_, this, "records"));
	}

	private void assign(ReceFlowerRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.ReceFlowerRecord>();
		for (xbean.ReceFlowerRecord _v_ : _o_.records)
			records.add(new ReceFlowerRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.ReceFlowerRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ReceFlowerRecord _v_ = new ReceFlowerRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ReceFlowerRankList copy() {
		_xdb_verify_unsafe_();
		return new ReceFlowerRankList(this);
	}

	@Override
	public xbean.ReceFlowerRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ReceFlowerRankList toBean() {
		_xdb_verify_unsafe_();
		return new ReceFlowerRankList(this); // same as copy()
	}

	@Override
	public xbean.ReceFlowerRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ReceFlowerRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.ReceFlowerRecord> getRecords() { // 送花记录 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.ReceFlowerRecord> getRecordsAsData() { // 送花记录 by changhao
		_xdb_verify_unsafe_();
		java.util.List<xbean.ReceFlowerRecord> records;
		ReceFlowerRankList _o_ = this;
		records = new java.util.LinkedList<xbean.ReceFlowerRecord>();
		for (xbean.ReceFlowerRecord _v_ : _o_.records)
			records.add(new ReceFlowerRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ReceFlowerRankList _o_ = null;
		if ( _o1_ instanceof ReceFlowerRankList ) _o_ = (ReceFlowerRankList)_o1_;
		else if ( _o1_ instanceof ReceFlowerRankList.Const ) _o_ = ((ReceFlowerRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.ReceFlowerRankList {
		ReceFlowerRankList nThis() {
			return ReceFlowerRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ReceFlowerRankList copy() {
			return ReceFlowerRankList.this.copy();
		}

		@Override
		public xbean.ReceFlowerRankList toData() {
			return ReceFlowerRankList.this.toData();
		}

		public xbean.ReceFlowerRankList toBean() {
			return ReceFlowerRankList.this.toBean();
		}

		@Override
		public xbean.ReceFlowerRankList toDataIf() {
			return ReceFlowerRankList.this.toDataIf();
		}

		public xbean.ReceFlowerRankList toBeanIf() {
			return ReceFlowerRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.ReceFlowerRecord> getRecords() { // 送花记录 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.ReceFlowerRecord> getRecordsAsData() { // 送花记录 by changhao
			_xdb_verify_unsafe_();
			java.util.List<xbean.ReceFlowerRecord> records;
			ReceFlowerRankList _o_ = ReceFlowerRankList.this;
		records = new java.util.LinkedList<xbean.ReceFlowerRecord>();
		for (xbean.ReceFlowerRecord _v_ : _o_.records)
			records.add(new ReceFlowerRecord.Data(_v_));
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
			return ReceFlowerRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ReceFlowerRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ReceFlowerRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ReceFlowerRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ReceFlowerRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ReceFlowerRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ReceFlowerRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ReceFlowerRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return ReceFlowerRankList.this.toString();
		}

	}

	public static final class Data implements xbean.ReceFlowerRankList {
		private java.util.LinkedList<xbean.ReceFlowerRecord> records; // 送花记录 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.ReceFlowerRecord>();
		}

		Data(xbean.ReceFlowerRankList _o1_) {
			if (_o1_ instanceof ReceFlowerRankList) assign((ReceFlowerRankList)_o1_);
			else if (_o1_ instanceof ReceFlowerRankList.Data) assign((ReceFlowerRankList.Data)_o1_);
			else if (_o1_ instanceof ReceFlowerRankList.Const) assign(((ReceFlowerRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ReceFlowerRankList _o_) {
			records = new java.util.LinkedList<xbean.ReceFlowerRecord>();
			for (xbean.ReceFlowerRecord _v_ : _o_.records)
				records.add(new ReceFlowerRecord.Data(_v_));
		}

		private void assign(ReceFlowerRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.ReceFlowerRecord>();
			for (xbean.ReceFlowerRecord _v_ : _o_.records)
				records.add(new ReceFlowerRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.ReceFlowerRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ReceFlowerRecord _v_ = xbean.Pod.newReceFlowerRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ReceFlowerRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.ReceFlowerRankList toData() {
			return new Data(this);
		}

		public xbean.ReceFlowerRankList toBean() {
			return new ReceFlowerRankList(this, null, null);
		}

		@Override
		public xbean.ReceFlowerRankList toDataIf() {
			return this;
		}

		public xbean.ReceFlowerRankList toBeanIf() {
			return new ReceFlowerRankList(this, null, null);
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
		public java.util.List<xbean.ReceFlowerRecord> getRecords() { // 送花记录 by changhao
			return records;
		}

		@Override
		public java.util.List<xbean.ReceFlowerRecord> getRecordsAsData() { // 送花记录 by changhao
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ReceFlowerRankList.Data)) return false;
			ReceFlowerRankList.Data _o_ = (ReceFlowerRankList.Data) _o1_;
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

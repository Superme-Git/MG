
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class GiveFlowerRankList extends mkdb.XBean implements xbean.GiveFlowerRankList {
	private java.util.LinkedList<xbean.GiveFlowerRecord> records; // 送花记录 by changhao

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	GiveFlowerRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.GiveFlowerRecord>();
	}

	public GiveFlowerRankList() {
		this(0, null, null);
	}

	public GiveFlowerRankList(GiveFlowerRankList _o_) {
		this(_o_, null, null);
	}

	GiveFlowerRankList(xbean.GiveFlowerRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof GiveFlowerRankList) assign((GiveFlowerRankList)_o1_);
		else if (_o1_ instanceof GiveFlowerRankList.Data) assign((GiveFlowerRankList.Data)_o1_);
		else if (_o1_ instanceof GiveFlowerRankList.Const) assign(((GiveFlowerRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(GiveFlowerRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.GiveFlowerRecord>();
		for (xbean.GiveFlowerRecord _v_ : _o_.records)
			records.add(new GiveFlowerRecord(_v_, this, "records"));
	}

	private void assign(GiveFlowerRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.GiveFlowerRecord>();
		for (xbean.GiveFlowerRecord _v_ : _o_.records)
			records.add(new GiveFlowerRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.GiveFlowerRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.GiveFlowerRecord _v_ = new GiveFlowerRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.GiveFlowerRankList copy() {
		_xdb_verify_unsafe_();
		return new GiveFlowerRankList(this);
	}

	@Override
	public xbean.GiveFlowerRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GiveFlowerRankList toBean() {
		_xdb_verify_unsafe_();
		return new GiveFlowerRankList(this); // same as copy()
	}

	@Override
	public xbean.GiveFlowerRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GiveFlowerRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.GiveFlowerRecord> getRecords() { // 送花记录 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.GiveFlowerRecord> getRecordsAsData() { // 送花记录 by changhao
		_xdb_verify_unsafe_();
		java.util.List<xbean.GiveFlowerRecord> records;
		GiveFlowerRankList _o_ = this;
		records = new java.util.LinkedList<xbean.GiveFlowerRecord>();
		for (xbean.GiveFlowerRecord _v_ : _o_.records)
			records.add(new GiveFlowerRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		GiveFlowerRankList _o_ = null;
		if ( _o1_ instanceof GiveFlowerRankList ) _o_ = (GiveFlowerRankList)_o1_;
		else if ( _o1_ instanceof GiveFlowerRankList.Const ) _o_ = ((GiveFlowerRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.GiveFlowerRankList {
		GiveFlowerRankList nThis() {
			return GiveFlowerRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.GiveFlowerRankList copy() {
			return GiveFlowerRankList.this.copy();
		}

		@Override
		public xbean.GiveFlowerRankList toData() {
			return GiveFlowerRankList.this.toData();
		}

		public xbean.GiveFlowerRankList toBean() {
			return GiveFlowerRankList.this.toBean();
		}

		@Override
		public xbean.GiveFlowerRankList toDataIf() {
			return GiveFlowerRankList.this.toDataIf();
		}

		public xbean.GiveFlowerRankList toBeanIf() {
			return GiveFlowerRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.GiveFlowerRecord> getRecords() { // 送花记录 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.GiveFlowerRecord> getRecordsAsData() { // 送花记录 by changhao
			_xdb_verify_unsafe_();
			java.util.List<xbean.GiveFlowerRecord> records;
			GiveFlowerRankList _o_ = GiveFlowerRankList.this;
		records = new java.util.LinkedList<xbean.GiveFlowerRecord>();
		for (xbean.GiveFlowerRecord _v_ : _o_.records)
			records.add(new GiveFlowerRecord.Data(_v_));
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
			return GiveFlowerRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return GiveFlowerRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return GiveFlowerRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return GiveFlowerRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return GiveFlowerRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return GiveFlowerRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return GiveFlowerRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return GiveFlowerRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return GiveFlowerRankList.this.toString();
		}

	}

	public static final class Data implements xbean.GiveFlowerRankList {
		private java.util.LinkedList<xbean.GiveFlowerRecord> records; // 送花记录 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.GiveFlowerRecord>();
		}

		Data(xbean.GiveFlowerRankList _o1_) {
			if (_o1_ instanceof GiveFlowerRankList) assign((GiveFlowerRankList)_o1_);
			else if (_o1_ instanceof GiveFlowerRankList.Data) assign((GiveFlowerRankList.Data)_o1_);
			else if (_o1_ instanceof GiveFlowerRankList.Const) assign(((GiveFlowerRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(GiveFlowerRankList _o_) {
			records = new java.util.LinkedList<xbean.GiveFlowerRecord>();
			for (xbean.GiveFlowerRecord _v_ : _o_.records)
				records.add(new GiveFlowerRecord.Data(_v_));
		}

		private void assign(GiveFlowerRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.GiveFlowerRecord>();
			for (xbean.GiveFlowerRecord _v_ : _o_.records)
				records.add(new GiveFlowerRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.GiveFlowerRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.GiveFlowerRecord _v_ = xbean.Pod.newGiveFlowerRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.GiveFlowerRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.GiveFlowerRankList toData() {
			return new Data(this);
		}

		public xbean.GiveFlowerRankList toBean() {
			return new GiveFlowerRankList(this, null, null);
		}

		@Override
		public xbean.GiveFlowerRankList toDataIf() {
			return this;
		}

		public xbean.GiveFlowerRankList toBeanIf() {
			return new GiveFlowerRankList(this, null, null);
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
		public java.util.List<xbean.GiveFlowerRecord> getRecords() { // 送花记录 by changhao
			return records;
		}

		@Override
		public java.util.List<xbean.GiveFlowerRecord> getRecordsAsData() { // 送花记录 by changhao
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof GiveFlowerRankList.Data)) return false;
			GiveFlowerRankList.Data _o_ = (GiveFlowerRankList.Data) _o1_;
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


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanFightHistroyRankList extends mkdb.XBean implements xbean.ClanFightHistroyRankList {
	private java.util.LinkedList<xbean.ClanFightHistroyRankRecord> records; // 公会战历史记录 by changhao

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	ClanFightHistroyRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.ClanFightHistroyRankRecord>();
	}

	public ClanFightHistroyRankList() {
		this(0, null, null);
	}

	public ClanFightHistroyRankList(ClanFightHistroyRankList _o_) {
		this(_o_, null, null);
	}

	ClanFightHistroyRankList(xbean.ClanFightHistroyRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanFightHistroyRankList) assign((ClanFightHistroyRankList)_o1_);
		else if (_o1_ instanceof ClanFightHistroyRankList.Data) assign((ClanFightHistroyRankList.Data)_o1_);
		else if (_o1_ instanceof ClanFightHistroyRankList.Const) assign(((ClanFightHistroyRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanFightHistroyRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.ClanFightHistroyRankRecord>();
		for (xbean.ClanFightHistroyRankRecord _v_ : _o_.records)
			records.add(new ClanFightHistroyRankRecord(_v_, this, "records"));
	}

	private void assign(ClanFightHistroyRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.ClanFightHistroyRankRecord>();
		for (xbean.ClanFightHistroyRankRecord _v_ : _o_.records)
			records.add(new ClanFightHistroyRankRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.ClanFightHistroyRankRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ClanFightHistroyRankRecord _v_ = new ClanFightHistroyRankRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ClanFightHistroyRankList copy() {
		_xdb_verify_unsafe_();
		return new ClanFightHistroyRankList(this);
	}

	@Override
	public xbean.ClanFightHistroyRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFightHistroyRankList toBean() {
		_xdb_verify_unsafe_();
		return new ClanFightHistroyRankList(this); // same as copy()
	}

	@Override
	public xbean.ClanFightHistroyRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFightHistroyRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.ClanFightHistroyRankRecord> getRecords() { // 公会战历史记录 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.ClanFightHistroyRankRecord> getRecordsAsData() { // 公会战历史记录 by changhao
		_xdb_verify_unsafe_();
		java.util.List<xbean.ClanFightHistroyRankRecord> records;
		ClanFightHistroyRankList _o_ = this;
		records = new java.util.LinkedList<xbean.ClanFightHistroyRankRecord>();
		for (xbean.ClanFightHistroyRankRecord _v_ : _o_.records)
			records.add(new ClanFightHistroyRankRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanFightHistroyRankList _o_ = null;
		if ( _o1_ instanceof ClanFightHistroyRankList ) _o_ = (ClanFightHistroyRankList)_o1_;
		else if ( _o1_ instanceof ClanFightHistroyRankList.Const ) _o_ = ((ClanFightHistroyRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.ClanFightHistroyRankList {
		ClanFightHistroyRankList nThis() {
			return ClanFightHistroyRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanFightHistroyRankList copy() {
			return ClanFightHistroyRankList.this.copy();
		}

		@Override
		public xbean.ClanFightHistroyRankList toData() {
			return ClanFightHistroyRankList.this.toData();
		}

		public xbean.ClanFightHistroyRankList toBean() {
			return ClanFightHistroyRankList.this.toBean();
		}

		@Override
		public xbean.ClanFightHistroyRankList toDataIf() {
			return ClanFightHistroyRankList.this.toDataIf();
		}

		public xbean.ClanFightHistroyRankList toBeanIf() {
			return ClanFightHistroyRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.ClanFightHistroyRankRecord> getRecords() { // 公会战历史记录 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.ClanFightHistroyRankRecord> getRecordsAsData() { // 公会战历史记录 by changhao
			_xdb_verify_unsafe_();
			java.util.List<xbean.ClanFightHistroyRankRecord> records;
			ClanFightHistroyRankList _o_ = ClanFightHistroyRankList.this;
		records = new java.util.LinkedList<xbean.ClanFightHistroyRankRecord>();
		for (xbean.ClanFightHistroyRankRecord _v_ : _o_.records)
			records.add(new ClanFightHistroyRankRecord.Data(_v_));
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
			return ClanFightHistroyRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanFightHistroyRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanFightHistroyRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanFightHistroyRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanFightHistroyRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanFightHistroyRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanFightHistroyRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanFightHistroyRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanFightHistroyRankList.this.toString();
		}

	}

	public static final class Data implements xbean.ClanFightHistroyRankList {
		private java.util.LinkedList<xbean.ClanFightHistroyRankRecord> records; // 公会战历史记录 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.ClanFightHistroyRankRecord>();
		}

		Data(xbean.ClanFightHistroyRankList _o1_) {
			if (_o1_ instanceof ClanFightHistroyRankList) assign((ClanFightHistroyRankList)_o1_);
			else if (_o1_ instanceof ClanFightHistroyRankList.Data) assign((ClanFightHistroyRankList.Data)_o1_);
			else if (_o1_ instanceof ClanFightHistroyRankList.Const) assign(((ClanFightHistroyRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanFightHistroyRankList _o_) {
			records = new java.util.LinkedList<xbean.ClanFightHistroyRankRecord>();
			for (xbean.ClanFightHistroyRankRecord _v_ : _o_.records)
				records.add(new ClanFightHistroyRankRecord.Data(_v_));
		}

		private void assign(ClanFightHistroyRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.ClanFightHistroyRankRecord>();
			for (xbean.ClanFightHistroyRankRecord _v_ : _o_.records)
				records.add(new ClanFightHistroyRankRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.ClanFightHistroyRankRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ClanFightHistroyRankRecord _v_ = xbean.Pod.newClanFightHistroyRankRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ClanFightHistroyRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanFightHistroyRankList toData() {
			return new Data(this);
		}

		public xbean.ClanFightHistroyRankList toBean() {
			return new ClanFightHistroyRankList(this, null, null);
		}

		@Override
		public xbean.ClanFightHistroyRankList toDataIf() {
			return this;
		}

		public xbean.ClanFightHistroyRankList toBeanIf() {
			return new ClanFightHistroyRankList(this, null, null);
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
		public java.util.List<xbean.ClanFightHistroyRankRecord> getRecords() { // 公会战历史记录 by changhao
			return records;
		}

		@Override
		public java.util.List<xbean.ClanFightHistroyRankRecord> getRecordsAsData() { // 公会战历史记录 by changhao
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanFightHistroyRankList.Data)) return false;
			ClanFightHistroyRankList.Data _o_ = (ClanFightHistroyRankList.Data) _o1_;
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

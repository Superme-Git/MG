
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RedPackRankList extends mkdb.XBean implements xbean.RedPackRankList {
	private java.util.LinkedList<xbean.RedPackRecord> records; // 红包所有的记录 by changhao

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	RedPackRankList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.RedPackRecord>();
	}

	public RedPackRankList() {
		this(0, null, null);
	}

	public RedPackRankList(RedPackRankList _o_) {
		this(_o_, null, null);
	}

	RedPackRankList(xbean.RedPackRankList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RedPackRankList) assign((RedPackRankList)_o1_);
		else if (_o1_ instanceof RedPackRankList.Data) assign((RedPackRankList.Data)_o1_);
		else if (_o1_ instanceof RedPackRankList.Const) assign(((RedPackRankList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RedPackRankList _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.RedPackRecord>();
		for (xbean.RedPackRecord _v_ : _o_.records)
			records.add(new RedPackRecord(_v_, this, "records"));
	}

	private void assign(RedPackRankList.Data _o_) {
		records = new java.util.LinkedList<xbean.RedPackRecord>();
		for (xbean.RedPackRecord _v_ : _o_.records)
			records.add(new RedPackRecord(_v_, this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.RedPackRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RedPackRecord _v_ = new RedPackRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.RedPackRankList copy() {
		_xdb_verify_unsafe_();
		return new RedPackRankList(this);
	}

	@Override
	public xbean.RedPackRankList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackRankList toBean() {
		_xdb_verify_unsafe_();
		return new RedPackRankList(this); // same as copy()
	}

	@Override
	public xbean.RedPackRankList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackRankList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.RedPackRecord> getRecords() { // 红包所有的记录 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.RedPackRecord> getRecordsAsData() { // 红包所有的记录 by changhao
		_xdb_verify_unsafe_();
		java.util.List<xbean.RedPackRecord> records;
		RedPackRankList _o_ = this;
		records = new java.util.LinkedList<xbean.RedPackRecord>();
		for (xbean.RedPackRecord _v_ : _o_.records)
			records.add(new RedPackRecord.Data(_v_));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RedPackRankList _o_ = null;
		if ( _o1_ instanceof RedPackRankList ) _o_ = (RedPackRankList)_o1_;
		else if ( _o1_ instanceof RedPackRankList.Const ) _o_ = ((RedPackRankList.Const)_o1_).nThis();
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

	private class Const implements xbean.RedPackRankList {
		RedPackRankList nThis() {
			return RedPackRankList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RedPackRankList copy() {
			return RedPackRankList.this.copy();
		}

		@Override
		public xbean.RedPackRankList toData() {
			return RedPackRankList.this.toData();
		}

		public xbean.RedPackRankList toBean() {
			return RedPackRankList.this.toBean();
		}

		@Override
		public xbean.RedPackRankList toDataIf() {
			return RedPackRankList.this.toDataIf();
		}

		public xbean.RedPackRankList toBeanIf() {
			return RedPackRankList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.RedPackRecord> getRecords() { // 红包所有的记录 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.RedPackRecord> getRecordsAsData() { // 红包所有的记录 by changhao
			_xdb_verify_unsafe_();
			java.util.List<xbean.RedPackRecord> records;
			RedPackRankList _o_ = RedPackRankList.this;
		records = new java.util.LinkedList<xbean.RedPackRecord>();
		for (xbean.RedPackRecord _v_ : _o_.records)
			records.add(new RedPackRecord.Data(_v_));
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
			return RedPackRankList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RedPackRankList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RedPackRankList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RedPackRankList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RedPackRankList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RedPackRankList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RedPackRankList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RedPackRankList.this.hashCode();
		}

		@Override
		public String toString() {
			return RedPackRankList.this.toString();
		}

	}

	public static final class Data implements xbean.RedPackRankList {
		private java.util.LinkedList<xbean.RedPackRecord> records; // 红包所有的记录 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.RedPackRecord>();
		}

		Data(xbean.RedPackRankList _o1_) {
			if (_o1_ instanceof RedPackRankList) assign((RedPackRankList)_o1_);
			else if (_o1_ instanceof RedPackRankList.Data) assign((RedPackRankList.Data)_o1_);
			else if (_o1_ instanceof RedPackRankList.Const) assign(((RedPackRankList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RedPackRankList _o_) {
			records = new java.util.LinkedList<xbean.RedPackRecord>();
			for (xbean.RedPackRecord _v_ : _o_.records)
				records.add(new RedPackRecord.Data(_v_));
		}

		private void assign(RedPackRankList.Data _o_) {
			records = new java.util.LinkedList<xbean.RedPackRecord>();
			for (xbean.RedPackRecord _v_ : _o_.records)
				records.add(new RedPackRecord.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.RedPackRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RedPackRecord _v_ = xbean.Pod.newRedPackRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.RedPackRankList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RedPackRankList toData() {
			return new Data(this);
		}

		public xbean.RedPackRankList toBean() {
			return new RedPackRankList(this, null, null);
		}

		@Override
		public xbean.RedPackRankList toDataIf() {
			return this;
		}

		public xbean.RedPackRankList toBeanIf() {
			return new RedPackRankList(this, null, null);
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
		public java.util.List<xbean.RedPackRecord> getRecords() { // 红包所有的记录 by changhao
			return records;
		}

		@Override
		public java.util.List<xbean.RedPackRecord> getRecordsAsData() { // 红包所有的记录 by changhao
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RedPackRankList.Data)) return false;
			RedPackRankList.Data _o_ = (RedPackRankList.Data) _o1_;
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

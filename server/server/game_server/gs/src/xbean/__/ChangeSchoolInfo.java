
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ChangeSchoolInfo extends mkdb.XBean implements xbean.ChangeSchoolInfo {
	private java.util.LinkedList<xbean.ChangeSchoolRecord> records; // 转职记录列表
	private int changeweaponcount; // 当前转职后,转换武器的次数
	private int changegemcount; // 当前转职后,转换宝石的次数

	@Override
	public void _reset_unsafe_() {
		records.clear();
		changeweaponcount = 0;
		changegemcount = 0;
	}

	ChangeSchoolInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.LinkedList<xbean.ChangeSchoolRecord>();
	}

	public ChangeSchoolInfo() {
		this(0, null, null);
	}

	public ChangeSchoolInfo(ChangeSchoolInfo _o_) {
		this(_o_, null, null);
	}

	ChangeSchoolInfo(xbean.ChangeSchoolInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ChangeSchoolInfo) assign((ChangeSchoolInfo)_o1_);
		else if (_o1_ instanceof ChangeSchoolInfo.Data) assign((ChangeSchoolInfo.Data)_o1_);
		else if (_o1_ instanceof ChangeSchoolInfo.Const) assign(((ChangeSchoolInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ChangeSchoolInfo _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.LinkedList<xbean.ChangeSchoolRecord>();
		for (xbean.ChangeSchoolRecord _v_ : _o_.records)
			records.add(new ChangeSchoolRecord(_v_, this, "records"));
		changeweaponcount = _o_.changeweaponcount;
		changegemcount = _o_.changegemcount;
	}

	private void assign(ChangeSchoolInfo.Data _o_) {
		records = new java.util.LinkedList<xbean.ChangeSchoolRecord>();
		for (xbean.ChangeSchoolRecord _v_ : _o_.records)
			records.add(new ChangeSchoolRecord(_v_, this, "records"));
		changeweaponcount = _o_.changeweaponcount;
		changegemcount = _o_.changegemcount;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (xbean.ChangeSchoolRecord _v_ : records) {
			_v_.marshal(_os_);
		}
		_os_.marshal(changeweaponcount);
		_os_.marshal(changegemcount);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ChangeSchoolRecord _v_ = new ChangeSchoolRecord(0, this, "records");
			_v_.unmarshal(_os_);
			records.add(_v_);
		}
		changeweaponcount = _os_.unmarshal_int();
		changegemcount = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ChangeSchoolInfo copy() {
		_xdb_verify_unsafe_();
		return new ChangeSchoolInfo(this);
	}

	@Override
	public xbean.ChangeSchoolInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChangeSchoolInfo toBean() {
		_xdb_verify_unsafe_();
		return new ChangeSchoolInfo(this); // same as copy()
	}

	@Override
	public xbean.ChangeSchoolInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChangeSchoolInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.ChangeSchoolRecord> getRecords() { // 转职记录列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "records"), records);
	}

	public java.util.List<xbean.ChangeSchoolRecord> getRecordsAsData() { // 转职记录列表
		_xdb_verify_unsafe_();
		java.util.List<xbean.ChangeSchoolRecord> records;
		ChangeSchoolInfo _o_ = this;
		records = new java.util.LinkedList<xbean.ChangeSchoolRecord>();
		for (xbean.ChangeSchoolRecord _v_ : _o_.records)
			records.add(new ChangeSchoolRecord.Data(_v_));
		return records;
	}

	@Override
	public int getChangeweaponcount() { // 当前转职后,转换武器的次数
		_xdb_verify_unsafe_();
		return changeweaponcount;
	}

	@Override
	public int getChangegemcount() { // 当前转职后,转换宝石的次数
		_xdb_verify_unsafe_();
		return changegemcount;
	}

	@Override
	public void setChangeweaponcount(int _v_) { // 当前转职后,转换武器的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "changeweaponcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, changeweaponcount) {
					public void rollback() { changeweaponcount = _xdb_saved; }
				};}});
		changeweaponcount = _v_;
	}

	@Override
	public void setChangegemcount(int _v_) { // 当前转职后,转换宝石的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "changegemcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, changegemcount) {
					public void rollback() { changegemcount = _xdb_saved; }
				};}});
		changegemcount = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ChangeSchoolInfo _o_ = null;
		if ( _o1_ instanceof ChangeSchoolInfo ) _o_ = (ChangeSchoolInfo)_o1_;
		else if ( _o1_ instanceof ChangeSchoolInfo.Const ) _o_ = ((ChangeSchoolInfo.Const)_o1_).nThis();
		else return false;
		if (!records.equals(_o_.records)) return false;
		if (changeweaponcount != _o_.changeweaponcount) return false;
		if (changegemcount != _o_.changegemcount) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += records.hashCode();
		_h_ += changeweaponcount;
		_h_ += changegemcount;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(records);
		_sb_.append(",");
		_sb_.append(changeweaponcount);
		_sb_.append(",");
		_sb_.append(changegemcount);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("records"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("changeweaponcount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("changegemcount"));
		return lb;
	}

	private class Const implements xbean.ChangeSchoolInfo {
		ChangeSchoolInfo nThis() {
			return ChangeSchoolInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ChangeSchoolInfo copy() {
			return ChangeSchoolInfo.this.copy();
		}

		@Override
		public xbean.ChangeSchoolInfo toData() {
			return ChangeSchoolInfo.this.toData();
		}

		public xbean.ChangeSchoolInfo toBean() {
			return ChangeSchoolInfo.this.toBean();
		}

		@Override
		public xbean.ChangeSchoolInfo toDataIf() {
			return ChangeSchoolInfo.this.toDataIf();
		}

		public xbean.ChangeSchoolInfo toBeanIf() {
			return ChangeSchoolInfo.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.ChangeSchoolRecord> getRecords() { // 转职记录列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(records);
		}

		public java.util.List<xbean.ChangeSchoolRecord> getRecordsAsData() { // 转职记录列表
			_xdb_verify_unsafe_();
			java.util.List<xbean.ChangeSchoolRecord> records;
			ChangeSchoolInfo _o_ = ChangeSchoolInfo.this;
		records = new java.util.LinkedList<xbean.ChangeSchoolRecord>();
		for (xbean.ChangeSchoolRecord _v_ : _o_.records)
			records.add(new ChangeSchoolRecord.Data(_v_));
			return records;
		}

		@Override
		public int getChangeweaponcount() { // 当前转职后,转换武器的次数
			_xdb_verify_unsafe_();
			return changeweaponcount;
		}

		@Override
		public int getChangegemcount() { // 当前转职后,转换宝石的次数
			_xdb_verify_unsafe_();
			return changegemcount;
		}

		@Override
		public void setChangeweaponcount(int _v_) { // 当前转职后,转换武器的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChangegemcount(int _v_) { // 当前转职后,转换宝石的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
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
			return ChangeSchoolInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ChangeSchoolInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ChangeSchoolInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ChangeSchoolInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ChangeSchoolInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ChangeSchoolInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ChangeSchoolInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ChangeSchoolInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ChangeSchoolInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ChangeSchoolInfo {
		private java.util.LinkedList<xbean.ChangeSchoolRecord> records; // 转职记录列表
		private int changeweaponcount; // 当前转职后,转换武器的次数
		private int changegemcount; // 当前转职后,转换宝石的次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.LinkedList<xbean.ChangeSchoolRecord>();
		}

		Data(xbean.ChangeSchoolInfo _o1_) {
			if (_o1_ instanceof ChangeSchoolInfo) assign((ChangeSchoolInfo)_o1_);
			else if (_o1_ instanceof ChangeSchoolInfo.Data) assign((ChangeSchoolInfo.Data)_o1_);
			else if (_o1_ instanceof ChangeSchoolInfo.Const) assign(((ChangeSchoolInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ChangeSchoolInfo _o_) {
			records = new java.util.LinkedList<xbean.ChangeSchoolRecord>();
			for (xbean.ChangeSchoolRecord _v_ : _o_.records)
				records.add(new ChangeSchoolRecord.Data(_v_));
			changeweaponcount = _o_.changeweaponcount;
			changegemcount = _o_.changegemcount;
		}

		private void assign(ChangeSchoolInfo.Data _o_) {
			records = new java.util.LinkedList<xbean.ChangeSchoolRecord>();
			for (xbean.ChangeSchoolRecord _v_ : _o_.records)
				records.add(new ChangeSchoolRecord.Data(_v_));
			changeweaponcount = _o_.changeweaponcount;
			changegemcount = _o_.changegemcount;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (xbean.ChangeSchoolRecord _v_ : records) {
				_v_.marshal(_os_);
			}
			_os_.marshal(changeweaponcount);
			_os_.marshal(changegemcount);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ChangeSchoolRecord _v_ = xbean.Pod.newChangeSchoolRecordData();
				_v_.unmarshal(_os_);
				records.add(_v_);
			}
			changeweaponcount = _os_.unmarshal_int();
			changegemcount = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ChangeSchoolInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ChangeSchoolInfo toData() {
			return new Data(this);
		}

		public xbean.ChangeSchoolInfo toBean() {
			return new ChangeSchoolInfo(this, null, null);
		}

		@Override
		public xbean.ChangeSchoolInfo toDataIf() {
			return this;
		}

		public xbean.ChangeSchoolInfo toBeanIf() {
			return new ChangeSchoolInfo(this, null, null);
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
		public java.util.List<xbean.ChangeSchoolRecord> getRecords() { // 转职记录列表
			return records;
		}

		@Override
		public java.util.List<xbean.ChangeSchoolRecord> getRecordsAsData() { // 转职记录列表
			return records;
		}

		@Override
		public int getChangeweaponcount() { // 当前转职后,转换武器的次数
			return changeweaponcount;
		}

		@Override
		public int getChangegemcount() { // 当前转职后,转换宝石的次数
			return changegemcount;
		}

		@Override
		public void setChangeweaponcount(int _v_) { // 当前转职后,转换武器的次数
			changeweaponcount = _v_;
		}

		@Override
		public void setChangegemcount(int _v_) { // 当前转职后,转换宝石的次数
			changegemcount = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ChangeSchoolInfo.Data)) return false;
			ChangeSchoolInfo.Data _o_ = (ChangeSchoolInfo.Data) _o1_;
			if (!records.equals(_o_.records)) return false;
			if (changeweaponcount != _o_.changeweaponcount) return false;
			if (changegemcount != _o_.changegemcount) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += records.hashCode();
			_h_ += changeweaponcount;
			_h_ += changegemcount;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(records);
			_sb_.append(",");
			_sb_.append(changeweaponcount);
			_sb_.append(",");
			_sb_.append(changegemcount);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

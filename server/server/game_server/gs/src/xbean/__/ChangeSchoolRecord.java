
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ChangeSchoolRecord extends mkdb.XBean implements xbean.ChangeSchoolRecord {
	private int shape; // 造型
	private int school; // 职业
	private long time; // 转职时间

	@Override
	public void _reset_unsafe_() {
		shape = 0;
		school = 0;
		time = 0L;
	}

	ChangeSchoolRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ChangeSchoolRecord() {
		this(0, null, null);
	}

	public ChangeSchoolRecord(ChangeSchoolRecord _o_) {
		this(_o_, null, null);
	}

	ChangeSchoolRecord(xbean.ChangeSchoolRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ChangeSchoolRecord) assign((ChangeSchoolRecord)_o1_);
		else if (_o1_ instanceof ChangeSchoolRecord.Data) assign((ChangeSchoolRecord.Data)_o1_);
		else if (_o1_ instanceof ChangeSchoolRecord.Const) assign(((ChangeSchoolRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ChangeSchoolRecord _o_) {
		_o_._xdb_verify_unsafe_();
		shape = _o_.shape;
		school = _o_.school;
		time = _o_.time;
	}

	private void assign(ChangeSchoolRecord.Data _o_) {
		shape = _o_.shape;
		school = _o_.school;
		time = _o_.time;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(shape);
		_os_.marshal(school);
		_os_.marshal(time);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		shape = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		time = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ChangeSchoolRecord copy() {
		_xdb_verify_unsafe_();
		return new ChangeSchoolRecord(this);
	}

	@Override
	public xbean.ChangeSchoolRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChangeSchoolRecord toBean() {
		_xdb_verify_unsafe_();
		return new ChangeSchoolRecord(this); // same as copy()
	}

	@Override
	public xbean.ChangeSchoolRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChangeSchoolRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getShape() { // 造型
		_xdb_verify_unsafe_();
		return shape;
	}

	@Override
	public int getSchool() { // 职业
		_xdb_verify_unsafe_();
		return school;
	}

	@Override
	public long getTime() { // 转职时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public void setShape(int _v_) { // 造型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shape") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shape) {
					public void rollback() { shape = _xdb_saved; }
				};}});
		shape = _v_;
	}

	@Override
	public void setSchool(int _v_) { // 职业
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "school") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, school) {
					public void rollback() { school = _xdb_saved; }
				};}});
		school = _v_;
	}

	@Override
	public void setTime(long _v_) { // 转职时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ChangeSchoolRecord _o_ = null;
		if ( _o1_ instanceof ChangeSchoolRecord ) _o_ = (ChangeSchoolRecord)_o1_;
		else if ( _o1_ instanceof ChangeSchoolRecord.Const ) _o_ = ((ChangeSchoolRecord.Const)_o1_).nThis();
		else return false;
		if (shape != _o_.shape) return false;
		if (school != _o_.school) return false;
		if (time != _o_.time) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += shape;
		_h_ += school;
		_h_ += time;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shape);
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(",");
		_sb_.append(time);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shape"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		return lb;
	}

	private class Const implements xbean.ChangeSchoolRecord {
		ChangeSchoolRecord nThis() {
			return ChangeSchoolRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ChangeSchoolRecord copy() {
			return ChangeSchoolRecord.this.copy();
		}

		@Override
		public xbean.ChangeSchoolRecord toData() {
			return ChangeSchoolRecord.this.toData();
		}

		public xbean.ChangeSchoolRecord toBean() {
			return ChangeSchoolRecord.this.toBean();
		}

		@Override
		public xbean.ChangeSchoolRecord toDataIf() {
			return ChangeSchoolRecord.this.toDataIf();
		}

		public xbean.ChangeSchoolRecord toBeanIf() {
			return ChangeSchoolRecord.this.toBeanIf();
		}

		@Override
		public int getShape() { // 造型
			_xdb_verify_unsafe_();
			return shape;
		}

		@Override
		public int getSchool() { // 职业
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public long getTime() { // 转职时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public void setShape(int _v_) { // 造型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 职业
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // 转职时间
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
			return ChangeSchoolRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ChangeSchoolRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ChangeSchoolRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ChangeSchoolRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ChangeSchoolRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ChangeSchoolRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ChangeSchoolRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ChangeSchoolRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return ChangeSchoolRecord.this.toString();
		}

	}

	public static final class Data implements xbean.ChangeSchoolRecord {
		private int shape; // 造型
		private int school; // 职业
		private long time; // 转职时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ChangeSchoolRecord _o1_) {
			if (_o1_ instanceof ChangeSchoolRecord) assign((ChangeSchoolRecord)_o1_);
			else if (_o1_ instanceof ChangeSchoolRecord.Data) assign((ChangeSchoolRecord.Data)_o1_);
			else if (_o1_ instanceof ChangeSchoolRecord.Const) assign(((ChangeSchoolRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ChangeSchoolRecord _o_) {
			shape = _o_.shape;
			school = _o_.school;
			time = _o_.time;
		}

		private void assign(ChangeSchoolRecord.Data _o_) {
			shape = _o_.shape;
			school = _o_.school;
			time = _o_.time;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(shape);
			_os_.marshal(school);
			_os_.marshal(time);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			shape = _os_.unmarshal_int();
			school = _os_.unmarshal_int();
			time = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ChangeSchoolRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.ChangeSchoolRecord toData() {
			return new Data(this);
		}

		public xbean.ChangeSchoolRecord toBean() {
			return new ChangeSchoolRecord(this, null, null);
		}

		@Override
		public xbean.ChangeSchoolRecord toDataIf() {
			return this;
		}

		public xbean.ChangeSchoolRecord toBeanIf() {
			return new ChangeSchoolRecord(this, null, null);
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
		public int getShape() { // 造型
			return shape;
		}

		@Override
		public int getSchool() { // 职业
			return school;
		}

		@Override
		public long getTime() { // 转职时间
			return time;
		}

		@Override
		public void setShape(int _v_) { // 造型
			shape = _v_;
		}

		@Override
		public void setSchool(int _v_) { // 职业
			school = _v_;
		}

		@Override
		public void setTime(long _v_) { // 转职时间
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ChangeSchoolRecord.Data)) return false;
			ChangeSchoolRecord.Data _o_ = (ChangeSchoolRecord.Data) _o1_;
			if (shape != _o_.shape) return false;
			if (school != _o_.school) return false;
			if (time != _o_.time) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += shape;
			_h_ += school;
			_h_ += time;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(shape);
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(",");
			_sb_.append(time);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

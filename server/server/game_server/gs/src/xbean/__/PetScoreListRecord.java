
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PetScoreListRecord extends mkdb.XBean implements xbean.PetScoreListRecord {
	private long time; // 达到这个数量的时间
	private xbean.MarshalPetScoreRecord marshaldata; // 

	@Override
	public void _reset_unsafe_() {
		time = 0L;
		marshaldata._reset_unsafe_();
	}

	PetScoreListRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		marshaldata = new MarshalPetScoreRecord(0, this, "marshaldata");
	}

	public PetScoreListRecord() {
		this(0, null, null);
	}

	public PetScoreListRecord(PetScoreListRecord _o_) {
		this(_o_, null, null);
	}

	PetScoreListRecord(xbean.PetScoreListRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PetScoreListRecord) assign((PetScoreListRecord)_o1_);
		else if (_o1_ instanceof PetScoreListRecord.Data) assign((PetScoreListRecord.Data)_o1_);
		else if (_o1_ instanceof PetScoreListRecord.Const) assign(((PetScoreListRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PetScoreListRecord _o_) {
		_o_._xdb_verify_unsafe_();
		time = _o_.time;
		marshaldata = new MarshalPetScoreRecord(_o_.marshaldata, this, "marshaldata");
	}

	private void assign(PetScoreListRecord.Data _o_) {
		time = _o_.time;
		marshaldata = new MarshalPetScoreRecord(_o_.marshaldata, this, "marshaldata");
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(time);
		marshaldata.marshal(_os_);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		time = _os_.unmarshal_long();
		marshaldata.unmarshal(_os_);
		return _os_;
	}

	@Override
	public xbean.PetScoreListRecord copy() {
		_xdb_verify_unsafe_();
		return new PetScoreListRecord(this);
	}

	@Override
	public xbean.PetScoreListRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetScoreListRecord toBean() {
		_xdb_verify_unsafe_();
		return new PetScoreListRecord(this); // same as copy()
	}

	@Override
	public xbean.PetScoreListRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetScoreListRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTime() { // 达到这个数量的时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public xbean.MarshalPetScoreRecord getMarshaldata() { // 
		_xdb_verify_unsafe_();
		return marshaldata;
	}

	@Override
	public void setTime(long _v_) { // 达到这个数量的时间
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
		PetScoreListRecord _o_ = null;
		if ( _o1_ instanceof PetScoreListRecord ) _o_ = (PetScoreListRecord)_o1_;
		else if ( _o1_ instanceof PetScoreListRecord.Const ) _o_ = ((PetScoreListRecord.Const)_o1_).nThis();
		else return false;
		if (time != _o_.time) return false;
		if (!marshaldata.equals(_o_.marshaldata)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += time;
		_h_ += marshaldata.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(marshaldata);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("marshaldata"));
		return lb;
	}

	private class Const implements xbean.PetScoreListRecord {
		PetScoreListRecord nThis() {
			return PetScoreListRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PetScoreListRecord copy() {
			return PetScoreListRecord.this.copy();
		}

		@Override
		public xbean.PetScoreListRecord toData() {
			return PetScoreListRecord.this.toData();
		}

		public xbean.PetScoreListRecord toBean() {
			return PetScoreListRecord.this.toBean();
		}

		@Override
		public xbean.PetScoreListRecord toDataIf() {
			return PetScoreListRecord.this.toDataIf();
		}

		public xbean.PetScoreListRecord toBeanIf() {
			return PetScoreListRecord.this.toBeanIf();
		}

		@Override
		public long getTime() { // 达到这个数量的时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public xbean.MarshalPetScoreRecord getMarshaldata() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(marshaldata);
		}

		@Override
		public void setTime(long _v_) { // 达到这个数量的时间
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
			return PetScoreListRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PetScoreListRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PetScoreListRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PetScoreListRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PetScoreListRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PetScoreListRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PetScoreListRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PetScoreListRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return PetScoreListRecord.this.toString();
		}

	}

	public static final class Data implements xbean.PetScoreListRecord {
		private long time; // 达到这个数量的时间
		private xbean.MarshalPetScoreRecord marshaldata; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			marshaldata = new MarshalPetScoreRecord.Data();
		}

		Data(xbean.PetScoreListRecord _o1_) {
			if (_o1_ instanceof PetScoreListRecord) assign((PetScoreListRecord)_o1_);
			else if (_o1_ instanceof PetScoreListRecord.Data) assign((PetScoreListRecord.Data)_o1_);
			else if (_o1_ instanceof PetScoreListRecord.Const) assign(((PetScoreListRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PetScoreListRecord _o_) {
			time = _o_.time;
			marshaldata = new MarshalPetScoreRecord.Data(_o_.marshaldata);
		}

		private void assign(PetScoreListRecord.Data _o_) {
			time = _o_.time;
			marshaldata = new MarshalPetScoreRecord.Data(_o_.marshaldata);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(time);
			marshaldata.marshal(_os_);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			time = _os_.unmarshal_long();
			marshaldata.unmarshal(_os_);
			return _os_;
		}

		@Override
		public xbean.PetScoreListRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.PetScoreListRecord toData() {
			return new Data(this);
		}

		public xbean.PetScoreListRecord toBean() {
			return new PetScoreListRecord(this, null, null);
		}

		@Override
		public xbean.PetScoreListRecord toDataIf() {
			return this;
		}

		public xbean.PetScoreListRecord toBeanIf() {
			return new PetScoreListRecord(this, null, null);
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
		public long getTime() { // 达到这个数量的时间
			return time;
		}

		@Override
		public xbean.MarshalPetScoreRecord getMarshaldata() { // 
			return marshaldata;
		}

		@Override
		public void setTime(long _v_) { // 达到这个数量的时间
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PetScoreListRecord.Data)) return false;
			PetScoreListRecord.Data _o_ = (PetScoreListRecord.Data) _o1_;
			if (time != _o_.time) return false;
			if (!marshaldata.equals(_o_.marshaldata)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += time;
			_h_ += marshaldata.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(marshaldata);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

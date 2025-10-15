
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BingFengListRecord extends mkdb.XBean implements xbean.BingFengListRecord {
	private long time; // 达到这个数量的时间
	private xbean.MarshalBingFengRecord marshaldata; // 

	@Override
	public void _reset_unsafe_() {
		time = 0L;
		marshaldata._reset_unsafe_();
	}

	BingFengListRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		marshaldata = new MarshalBingFengRecord(0, this, "marshaldata");
	}

	public BingFengListRecord() {
		this(0, null, null);
	}

	public BingFengListRecord(BingFengListRecord _o_) {
		this(_o_, null, null);
	}

	BingFengListRecord(xbean.BingFengListRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BingFengListRecord) assign((BingFengListRecord)_o1_);
		else if (_o1_ instanceof BingFengListRecord.Data) assign((BingFengListRecord.Data)_o1_);
		else if (_o1_ instanceof BingFengListRecord.Const) assign(((BingFengListRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BingFengListRecord _o_) {
		_o_._xdb_verify_unsafe_();
		time = _o_.time;
		marshaldata = new MarshalBingFengRecord(_o_.marshaldata, this, "marshaldata");
	}

	private void assign(BingFengListRecord.Data _o_) {
		time = _o_.time;
		marshaldata = new MarshalBingFengRecord(_o_.marshaldata, this, "marshaldata");
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
	public xbean.BingFengListRecord copy() {
		_xdb_verify_unsafe_();
		return new BingFengListRecord(this);
	}

	@Override
	public xbean.BingFengListRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengListRecord toBean() {
		_xdb_verify_unsafe_();
		return new BingFengListRecord(this); // same as copy()
	}

	@Override
	public xbean.BingFengListRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengListRecord toBeanIf() {
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
	public xbean.MarshalBingFengRecord getMarshaldata() { // 
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
		BingFengListRecord _o_ = null;
		if ( _o1_ instanceof BingFengListRecord ) _o_ = (BingFengListRecord)_o1_;
		else if ( _o1_ instanceof BingFengListRecord.Const ) _o_ = ((BingFengListRecord.Const)_o1_).nThis();
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

	private class Const implements xbean.BingFengListRecord {
		BingFengListRecord nThis() {
			return BingFengListRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BingFengListRecord copy() {
			return BingFengListRecord.this.copy();
		}

		@Override
		public xbean.BingFengListRecord toData() {
			return BingFengListRecord.this.toData();
		}

		public xbean.BingFengListRecord toBean() {
			return BingFengListRecord.this.toBean();
		}

		@Override
		public xbean.BingFengListRecord toDataIf() {
			return BingFengListRecord.this.toDataIf();
		}

		public xbean.BingFengListRecord toBeanIf() {
			return BingFengListRecord.this.toBeanIf();
		}

		@Override
		public long getTime() { // 达到这个数量的时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public xbean.MarshalBingFengRecord getMarshaldata() { // 
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
			return BingFengListRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BingFengListRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BingFengListRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BingFengListRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BingFengListRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BingFengListRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BingFengListRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BingFengListRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return BingFengListRecord.this.toString();
		}

	}

	public static final class Data implements xbean.BingFengListRecord {
		private long time; // 达到这个数量的时间
		private xbean.MarshalBingFengRecord marshaldata; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			marshaldata = new MarshalBingFengRecord.Data();
		}

		Data(xbean.BingFengListRecord _o1_) {
			if (_o1_ instanceof BingFengListRecord) assign((BingFengListRecord)_o1_);
			else if (_o1_ instanceof BingFengListRecord.Data) assign((BingFengListRecord.Data)_o1_);
			else if (_o1_ instanceof BingFengListRecord.Const) assign(((BingFengListRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BingFengListRecord _o_) {
			time = _o_.time;
			marshaldata = new MarshalBingFengRecord.Data(_o_.marshaldata);
		}

		private void assign(BingFengListRecord.Data _o_) {
			time = _o_.time;
			marshaldata = new MarshalBingFengRecord.Data(_o_.marshaldata);
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
		public xbean.BingFengListRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.BingFengListRecord toData() {
			return new Data(this);
		}

		public xbean.BingFengListRecord toBean() {
			return new BingFengListRecord(this, null, null);
		}

		@Override
		public xbean.BingFengListRecord toDataIf() {
			return this;
		}

		public xbean.BingFengListRecord toBeanIf() {
			return new BingFengListRecord(this, null, null);
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
		public xbean.MarshalBingFengRecord getMarshaldata() { // 
			return marshaldata;
		}

		@Override
		public void setTime(long _v_) { // 达到这个数量的时间
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BingFengListRecord.Data)) return false;
			BingFengListRecord.Data _o_ = (BingFengListRecord.Data) _o1_;
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


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class npcawardrecords extends mkdb.XBean implements xbean.npcawardrecords {
	private java.util.HashMap<Integer, xbean.npcaward> records; // 

	@Override
	public void _reset_unsafe_() {
		records.clear();
	}

	npcawardrecords(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		records = new java.util.HashMap<Integer, xbean.npcaward>();
	}

	public npcawardrecords() {
		this(0, null, null);
	}

	public npcawardrecords(npcawardrecords _o_) {
		this(_o_, null, null);
	}

	npcawardrecords(xbean.npcawardrecords _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof npcawardrecords) assign((npcawardrecords)_o1_);
		else if (_o1_ instanceof npcawardrecords.Data) assign((npcawardrecords.Data)_o1_);
		else if (_o1_ instanceof npcawardrecords.Const) assign(((npcawardrecords.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(npcawardrecords _o_) {
		_o_._xdb_verify_unsafe_();
		records = new java.util.HashMap<Integer, xbean.npcaward>();
		for (java.util.Map.Entry<Integer, xbean.npcaward> _e_ : _o_.records.entrySet())
			records.put(_e_.getKey(), new npcaward(_e_.getValue(), this, "records"));
	}

	private void assign(npcawardrecords.Data _o_) {
		records = new java.util.HashMap<Integer, xbean.npcaward>();
		for (java.util.Map.Entry<Integer, xbean.npcaward> _e_ : _o_.records.entrySet())
			records.put(_e_.getKey(), new npcaward(_e_.getValue(), this, "records"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(records.size());
		for (java.util.Map.Entry<Integer, xbean.npcaward> _e_ : records.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				records = new java.util.HashMap<Integer, xbean.npcaward>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.npcaward _v_ = new npcaward(0, this, "records");
				_v_.unmarshal(_os_);
				records.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.npcawardrecords copy() {
		_xdb_verify_unsafe_();
		return new npcawardrecords(this);
	}

	@Override
	public xbean.npcawardrecords toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.npcawardrecords toBean() {
		_xdb_verify_unsafe_();
		return new npcawardrecords(this); // same as copy()
	}

	@Override
	public xbean.npcawardrecords toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.npcawardrecords toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.npcaward> getRecords() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "records"), records);
	}

	@Override
	public java.util.Map<Integer, xbean.npcaward> getRecordsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.npcaward> records;
		npcawardrecords _o_ = this;
		records = new java.util.HashMap<Integer, xbean.npcaward>();
		for (java.util.Map.Entry<Integer, xbean.npcaward> _e_ : _o_.records.entrySet())
			records.put(_e_.getKey(), new npcaward.Data(_e_.getValue()));
		return records;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		npcawardrecords _o_ = null;
		if ( _o1_ instanceof npcawardrecords ) _o_ = (npcawardrecords)_o1_;
		else if ( _o1_ instanceof npcawardrecords.Const ) _o_ = ((npcawardrecords.Const)_o1_).nThis();
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
		lb.add(new mkdb.logs.ListenableMap().setVarName("records"));
		return lb;
	}

	private class Const implements xbean.npcawardrecords {
		npcawardrecords nThis() {
			return npcawardrecords.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.npcawardrecords copy() {
			return npcawardrecords.this.copy();
		}

		@Override
		public xbean.npcawardrecords toData() {
			return npcawardrecords.this.toData();
		}

		public xbean.npcawardrecords toBean() {
			return npcawardrecords.this.toBean();
		}

		@Override
		public xbean.npcawardrecords toDataIf() {
			return npcawardrecords.this.toDataIf();
		}

		public xbean.npcawardrecords toBeanIf() {
			return npcawardrecords.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.npcaward> getRecords() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(records);
		}

		@Override
		public java.util.Map<Integer, xbean.npcaward> getRecordsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.npcaward> records;
			npcawardrecords _o_ = npcawardrecords.this;
			records = new java.util.HashMap<Integer, xbean.npcaward>();
			for (java.util.Map.Entry<Integer, xbean.npcaward> _e_ : _o_.records.entrySet())
				records.put(_e_.getKey(), new npcaward.Data(_e_.getValue()));
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
			return npcawardrecords.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return npcawardrecords.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return npcawardrecords.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return npcawardrecords.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return npcawardrecords.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return npcawardrecords.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return npcawardrecords.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return npcawardrecords.this.hashCode();
		}

		@Override
		public String toString() {
			return npcawardrecords.this.toString();
		}

	}

	public static final class Data implements xbean.npcawardrecords {
		private java.util.HashMap<Integer, xbean.npcaward> records; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			records = new java.util.HashMap<Integer, xbean.npcaward>();
		}

		Data(xbean.npcawardrecords _o1_) {
			if (_o1_ instanceof npcawardrecords) assign((npcawardrecords)_o1_);
			else if (_o1_ instanceof npcawardrecords.Data) assign((npcawardrecords.Data)_o1_);
			else if (_o1_ instanceof npcawardrecords.Const) assign(((npcawardrecords.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(npcawardrecords _o_) {
			records = new java.util.HashMap<Integer, xbean.npcaward>();
			for (java.util.Map.Entry<Integer, xbean.npcaward> _e_ : _o_.records.entrySet())
				records.put(_e_.getKey(), new npcaward.Data(_e_.getValue()));
		}

		private void assign(npcawardrecords.Data _o_) {
			records = new java.util.HashMap<Integer, xbean.npcaward>();
			for (java.util.Map.Entry<Integer, xbean.npcaward> _e_ : _o_.records.entrySet())
				records.put(_e_.getKey(), new npcaward.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(records.size());
			for (java.util.Map.Entry<Integer, xbean.npcaward> _e_ : records.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					records = new java.util.HashMap<Integer, xbean.npcaward>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.npcaward _v_ = xbean.Pod.newnpcawardData();
					_v_.unmarshal(_os_);
					records.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.npcawardrecords copy() {
			return new Data(this);
		}

		@Override
		public xbean.npcawardrecords toData() {
			return new Data(this);
		}

		public xbean.npcawardrecords toBean() {
			return new npcawardrecords(this, null, null);
		}

		@Override
		public xbean.npcawardrecords toDataIf() {
			return this;
		}

		public xbean.npcawardrecords toBeanIf() {
			return new npcawardrecords(this, null, null);
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
		public java.util.Map<Integer, xbean.npcaward> getRecords() { // 
			return records;
		}

		@Override
		public java.util.Map<Integer, xbean.npcaward> getRecordsAsData() { // 
			return records;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof npcawardrecords.Data)) return false;
			npcawardrecords.Data _o_ = (npcawardrecords.Data) _o1_;
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


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanIndex extends mkdb.XBean implements xbean.ClanIndex {
	private java.util.HashMap<Integer, Long> index; // 

	@Override
	public void _reset_unsafe_() {
		index.clear();
	}

	ClanIndex(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		index = new java.util.HashMap<Integer, Long>();
	}

	public ClanIndex() {
		this(0, null, null);
	}

	public ClanIndex(ClanIndex _o_) {
		this(_o_, null, null);
	}

	ClanIndex(xbean.ClanIndex _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanIndex) assign((ClanIndex)_o1_);
		else if (_o1_ instanceof ClanIndex.Data) assign((ClanIndex.Data)_o1_);
		else if (_o1_ instanceof ClanIndex.Const) assign(((ClanIndex.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanIndex _o_) {
		_o_._xdb_verify_unsafe_();
		index = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.index.entrySet())
			index.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(ClanIndex.Data _o_) {
		index = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.index.entrySet())
			index.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(index.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : index.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				index = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				index.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ClanIndex copy() {
		_xdb_verify_unsafe_();
		return new ClanIndex(this);
	}

	@Override
	public xbean.ClanIndex toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanIndex toBean() {
		_xdb_verify_unsafe_();
		return new ClanIndex(this); // same as copy()
	}

	@Override
	public xbean.ClanIndex toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanIndex toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getIndex() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "index"), index);
	}

	@Override
	public java.util.Map<Integer, Long> getIndexAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> index;
		ClanIndex _o_ = this;
		index = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.index.entrySet())
			index.put(_e_.getKey(), _e_.getValue());
		return index;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanIndex _o_ = null;
		if ( _o1_ instanceof ClanIndex ) _o_ = (ClanIndex)_o1_;
		else if ( _o1_ instanceof ClanIndex.Const ) _o_ = ((ClanIndex.Const)_o1_).nThis();
		else return false;
		if (!index.equals(_o_.index)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += index.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(index);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("index"));
		return lb;
	}

	private class Const implements xbean.ClanIndex {
		ClanIndex nThis() {
			return ClanIndex.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanIndex copy() {
			return ClanIndex.this.copy();
		}

		@Override
		public xbean.ClanIndex toData() {
			return ClanIndex.this.toData();
		}

		public xbean.ClanIndex toBean() {
			return ClanIndex.this.toBean();
		}

		@Override
		public xbean.ClanIndex toDataIf() {
			return ClanIndex.this.toDataIf();
		}

		public xbean.ClanIndex toBeanIf() {
			return ClanIndex.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getIndex() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(index);
		}

		@Override
		public java.util.Map<Integer, Long> getIndexAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> index;
			ClanIndex _o_ = ClanIndex.this;
			index = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.index.entrySet())
				index.put(_e_.getKey(), _e_.getValue());
			return index;
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
			return ClanIndex.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanIndex.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanIndex.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanIndex.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanIndex.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanIndex.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanIndex.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanIndex.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanIndex.this.toString();
		}

	}

	public static final class Data implements xbean.ClanIndex {
		private java.util.HashMap<Integer, Long> index; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			index = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.ClanIndex _o1_) {
			if (_o1_ instanceof ClanIndex) assign((ClanIndex)_o1_);
			else if (_o1_ instanceof ClanIndex.Data) assign((ClanIndex.Data)_o1_);
			else if (_o1_ instanceof ClanIndex.Const) assign(((ClanIndex.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanIndex _o_) {
			index = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.index.entrySet())
				index.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(ClanIndex.Data _o_) {
			index = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.index.entrySet())
				index.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(index.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : index.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					index = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					index.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ClanIndex copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanIndex toData() {
			return new Data(this);
		}

		public xbean.ClanIndex toBean() {
			return new ClanIndex(this, null, null);
		}

		@Override
		public xbean.ClanIndex toDataIf() {
			return this;
		}

		public xbean.ClanIndex toBeanIf() {
			return new ClanIndex(this, null, null);
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
		public java.util.Map<Integer, Long> getIndex() { // 
			return index;
		}

		@Override
		public java.util.Map<Integer, Long> getIndexAsData() { // 
			return index;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanIndex.Data)) return false;
			ClanIndex.Data _o_ = (ClanIndex.Data) _o1_;
			if (!index.equals(_o_.index)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += index.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(index);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CompensationRole extends mkdb.XBean implements xbean.CompensationRole {
	private java.util.HashMap<Long, Long> singlecompensations; // key = 补偿key, value = 是否已读 0=未读 1=已读

	@Override
	public void _reset_unsafe_() {
		singlecompensations.clear();
	}

	CompensationRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		singlecompensations = new java.util.HashMap<Long, Long>();
	}

	public CompensationRole() {
		this(0, null, null);
	}

	public CompensationRole(CompensationRole _o_) {
		this(_o_, null, null);
	}

	CompensationRole(xbean.CompensationRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CompensationRole) assign((CompensationRole)_o1_);
		else if (_o1_ instanceof CompensationRole.Data) assign((CompensationRole.Data)_o1_);
		else if (_o1_ instanceof CompensationRole.Const) assign(((CompensationRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CompensationRole _o_) {
		_o_._xdb_verify_unsafe_();
		singlecompensations = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.singlecompensations.entrySet())
			singlecompensations.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(CompensationRole.Data _o_) {
		singlecompensations = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.singlecompensations.entrySet())
			singlecompensations.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(singlecompensations.size());
		for (java.util.Map.Entry<Long, Long> _e_ : singlecompensations.entrySet())
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
				singlecompensations = new java.util.HashMap<Long, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				singlecompensations.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.CompensationRole copy() {
		_xdb_verify_unsafe_();
		return new CompensationRole(this);
	}

	@Override
	public xbean.CompensationRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CompensationRole toBean() {
		_xdb_verify_unsafe_();
		return new CompensationRole(this); // same as copy()
	}

	@Override
	public xbean.CompensationRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CompensationRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Long> getSinglecompensations() { // key = 补偿key, value = 是否已读 0=未读 1=已读
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "singlecompensations"), singlecompensations);
	}

	@Override
	public java.util.Map<Long, Long> getSinglecompensationsAsData() { // key = 补偿key, value = 是否已读 0=未读 1=已读
		_xdb_verify_unsafe_();
		java.util.Map<Long, Long> singlecompensations;
		CompensationRole _o_ = this;
		singlecompensations = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.singlecompensations.entrySet())
			singlecompensations.put(_e_.getKey(), _e_.getValue());
		return singlecompensations;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CompensationRole _o_ = null;
		if ( _o1_ instanceof CompensationRole ) _o_ = (CompensationRole)_o1_;
		else if ( _o1_ instanceof CompensationRole.Const ) _o_ = ((CompensationRole.Const)_o1_).nThis();
		else return false;
		if (!singlecompensations.equals(_o_.singlecompensations)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += singlecompensations.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(singlecompensations);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("singlecompensations"));
		return lb;
	}

	private class Const implements xbean.CompensationRole {
		CompensationRole nThis() {
			return CompensationRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CompensationRole copy() {
			return CompensationRole.this.copy();
		}

		@Override
		public xbean.CompensationRole toData() {
			return CompensationRole.this.toData();
		}

		public xbean.CompensationRole toBean() {
			return CompensationRole.this.toBean();
		}

		@Override
		public xbean.CompensationRole toDataIf() {
			return CompensationRole.this.toDataIf();
		}

		public xbean.CompensationRole toBeanIf() {
			return CompensationRole.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Long> getSinglecompensations() { // key = 补偿key, value = 是否已读 0=未读 1=已读
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(singlecompensations);
		}

		@Override
		public java.util.Map<Long, Long> getSinglecompensationsAsData() { // key = 补偿key, value = 是否已读 0=未读 1=已读
			_xdb_verify_unsafe_();
			java.util.Map<Long, Long> singlecompensations;
			CompensationRole _o_ = CompensationRole.this;
			singlecompensations = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.singlecompensations.entrySet())
				singlecompensations.put(_e_.getKey(), _e_.getValue());
			return singlecompensations;
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
			return CompensationRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CompensationRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CompensationRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CompensationRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CompensationRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CompensationRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CompensationRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CompensationRole.this.hashCode();
		}

		@Override
		public String toString() {
			return CompensationRole.this.toString();
		}

	}

	public static final class Data implements xbean.CompensationRole {
		private java.util.HashMap<Long, Long> singlecompensations; // key = 补偿key, value = 是否已读 0=未读 1=已读

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			singlecompensations = new java.util.HashMap<Long, Long>();
		}

		Data(xbean.CompensationRole _o1_) {
			if (_o1_ instanceof CompensationRole) assign((CompensationRole)_o1_);
			else if (_o1_ instanceof CompensationRole.Data) assign((CompensationRole.Data)_o1_);
			else if (_o1_ instanceof CompensationRole.Const) assign(((CompensationRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CompensationRole _o_) {
			singlecompensations = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.singlecompensations.entrySet())
				singlecompensations.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(CompensationRole.Data _o_) {
			singlecompensations = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.singlecompensations.entrySet())
				singlecompensations.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(singlecompensations.size());
			for (java.util.Map.Entry<Long, Long> _e_ : singlecompensations.entrySet())
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
					singlecompensations = new java.util.HashMap<Long, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					singlecompensations.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.CompensationRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.CompensationRole toData() {
			return new Data(this);
		}

		public xbean.CompensationRole toBean() {
			return new CompensationRole(this, null, null);
		}

		@Override
		public xbean.CompensationRole toDataIf() {
			return this;
		}

		public xbean.CompensationRole toBeanIf() {
			return new CompensationRole(this, null, null);
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
		public java.util.Map<Long, Long> getSinglecompensations() { // key = 补偿key, value = 是否已读 0=未读 1=已读
			return singlecompensations;
		}

		@Override
		public java.util.Map<Long, Long> getSinglecompensationsAsData() { // key = 补偿key, value = 是否已读 0=未读 1=已读
			return singlecompensations;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CompensationRole.Data)) return false;
			CompensationRole.Data _o_ = (CompensationRole.Data) _o1_;
			if (!singlecompensations.equals(_o_.singlecompensations)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += singlecompensations.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(singlecompensations);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

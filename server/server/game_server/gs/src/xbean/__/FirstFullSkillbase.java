
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FirstFullSkillbase extends mkdb.XBean implements xbean.FirstFullSkillbase {
	private java.util.HashMap<Long, Long> roleids; // key=roleid，value=time

	@Override
	public void _reset_unsafe_() {
		roleids.clear();
	}

	FirstFullSkillbase(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		roleids = new java.util.HashMap<Long, Long>();
	}

	public FirstFullSkillbase() {
		this(0, null, null);
	}

	public FirstFullSkillbase(FirstFullSkillbase _o_) {
		this(_o_, null, null);
	}

	FirstFullSkillbase(xbean.FirstFullSkillbase _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FirstFullSkillbase) assign((FirstFullSkillbase)_o1_);
		else if (_o1_ instanceof FirstFullSkillbase.Data) assign((FirstFullSkillbase.Data)_o1_);
		else if (_o1_ instanceof FirstFullSkillbase.Const) assign(((FirstFullSkillbase.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FirstFullSkillbase _o_) {
		_o_._xdb_verify_unsafe_();
		roleids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.roleids.entrySet())
			roleids.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(FirstFullSkillbase.Data _o_) {
		roleids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.roleids.entrySet())
			roleids.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(roleids.size());
		for (java.util.Map.Entry<Long, Long> _e_ : roleids.entrySet())
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
				roleids = new java.util.HashMap<Long, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				roleids.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.FirstFullSkillbase copy() {
		_xdb_verify_unsafe_();
		return new FirstFullSkillbase(this);
	}

	@Override
	public xbean.FirstFullSkillbase toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FirstFullSkillbase toBean() {
		_xdb_verify_unsafe_();
		return new FirstFullSkillbase(this); // same as copy()
	}

	@Override
	public xbean.FirstFullSkillbase toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FirstFullSkillbase toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Long> getRoleids() { // key=roleid，value=time
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "roleids"), roleids);
	}

	@Override
	public java.util.Map<Long, Long> getRoleidsAsData() { // key=roleid，value=time
		_xdb_verify_unsafe_();
		java.util.Map<Long, Long> roleids;
		FirstFullSkillbase _o_ = this;
		roleids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.roleids.entrySet())
			roleids.put(_e_.getKey(), _e_.getValue());
		return roleids;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FirstFullSkillbase _o_ = null;
		if ( _o1_ instanceof FirstFullSkillbase ) _o_ = (FirstFullSkillbase)_o1_;
		else if ( _o1_ instanceof FirstFullSkillbase.Const ) _o_ = ((FirstFullSkillbase.Const)_o1_).nThis();
		else return false;
		if (!roleids.equals(_o_.roleids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("roleids"));
		return lb;
	}

	private class Const implements xbean.FirstFullSkillbase {
		FirstFullSkillbase nThis() {
			return FirstFullSkillbase.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FirstFullSkillbase copy() {
			return FirstFullSkillbase.this.copy();
		}

		@Override
		public xbean.FirstFullSkillbase toData() {
			return FirstFullSkillbase.this.toData();
		}

		public xbean.FirstFullSkillbase toBean() {
			return FirstFullSkillbase.this.toBean();
		}

		@Override
		public xbean.FirstFullSkillbase toDataIf() {
			return FirstFullSkillbase.this.toDataIf();
		}

		public xbean.FirstFullSkillbase toBeanIf() {
			return FirstFullSkillbase.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Long> getRoleids() { // key=roleid，value=time
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(roleids);
		}

		@Override
		public java.util.Map<Long, Long> getRoleidsAsData() { // key=roleid，value=time
			_xdb_verify_unsafe_();
			java.util.Map<Long, Long> roleids;
			FirstFullSkillbase _o_ = FirstFullSkillbase.this;
			roleids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.roleids.entrySet())
				roleids.put(_e_.getKey(), _e_.getValue());
			return roleids;
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
			return FirstFullSkillbase.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FirstFullSkillbase.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FirstFullSkillbase.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FirstFullSkillbase.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FirstFullSkillbase.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FirstFullSkillbase.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FirstFullSkillbase.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FirstFullSkillbase.this.hashCode();
		}

		@Override
		public String toString() {
			return FirstFullSkillbase.this.toString();
		}

	}

	public static final class Data implements xbean.FirstFullSkillbase {
		private java.util.HashMap<Long, Long> roleids; // key=roleid，value=time

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			roleids = new java.util.HashMap<Long, Long>();
		}

		Data(xbean.FirstFullSkillbase _o1_) {
			if (_o1_ instanceof FirstFullSkillbase) assign((FirstFullSkillbase)_o1_);
			else if (_o1_ instanceof FirstFullSkillbase.Data) assign((FirstFullSkillbase.Data)_o1_);
			else if (_o1_ instanceof FirstFullSkillbase.Const) assign(((FirstFullSkillbase.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FirstFullSkillbase _o_) {
			roleids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.roleids.entrySet())
				roleids.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(FirstFullSkillbase.Data _o_) {
			roleids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.roleids.entrySet())
				roleids.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(roleids.size());
			for (java.util.Map.Entry<Long, Long> _e_ : roleids.entrySet())
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
					roleids = new java.util.HashMap<Long, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					roleids.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.FirstFullSkillbase copy() {
			return new Data(this);
		}

		@Override
		public xbean.FirstFullSkillbase toData() {
			return new Data(this);
		}

		public xbean.FirstFullSkillbase toBean() {
			return new FirstFullSkillbase(this, null, null);
		}

		@Override
		public xbean.FirstFullSkillbase toDataIf() {
			return this;
		}

		public xbean.FirstFullSkillbase toBeanIf() {
			return new FirstFullSkillbase(this, null, null);
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
		public java.util.Map<Long, Long> getRoleids() { // key=roleid，value=time
			return roleids;
		}

		@Override
		public java.util.Map<Long, Long> getRoleidsAsData() { // key=roleid，value=time
			return roleids;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FirstFullSkillbase.Data)) return false;
			FirstFullSkillbase.Data _o_ = (FirstFullSkillbase.Data) _o1_;
			if (!roleids.equals(_o_.roleids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

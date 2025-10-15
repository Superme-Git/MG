
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class StoredBuffRole extends mkdb.XBean implements xbean.StoredBuffRole {
	private java.util.HashMap<Integer, xbean.Buff> buffs; // 

	@Override
	public void _reset_unsafe_() {
		buffs.clear();
	}

	StoredBuffRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		buffs = new java.util.HashMap<Integer, xbean.Buff>();
	}

	public StoredBuffRole() {
		this(0, null, null);
	}

	public StoredBuffRole(StoredBuffRole _o_) {
		this(_o_, null, null);
	}

	StoredBuffRole(xbean.StoredBuffRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof StoredBuffRole) assign((StoredBuffRole)_o1_);
		else if (_o1_ instanceof StoredBuffRole.Data) assign((StoredBuffRole.Data)_o1_);
		else if (_o1_ instanceof StoredBuffRole.Const) assign(((StoredBuffRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(StoredBuffRole _o_) {
		_o_._xdb_verify_unsafe_();
		buffs = new java.util.HashMap<Integer, xbean.Buff>();
		for (java.util.Map.Entry<Integer, xbean.Buff> _e_ : _o_.buffs.entrySet())
			buffs.put(_e_.getKey(), new Buff(_e_.getValue(), this, "buffs"));
	}

	private void assign(StoredBuffRole.Data _o_) {
		buffs = new java.util.HashMap<Integer, xbean.Buff>();
		for (java.util.Map.Entry<Integer, xbean.Buff> _e_ : _o_.buffs.entrySet())
			buffs.put(_e_.getKey(), new Buff(_e_.getValue(), this, "buffs"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(buffs.size());
		for (java.util.Map.Entry<Integer, xbean.Buff> _e_ : buffs.entrySet())
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
				buffs = new java.util.HashMap<Integer, xbean.Buff>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Buff _v_ = new Buff(0, this, "buffs");
				_v_.unmarshal(_os_);
				buffs.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.StoredBuffRole copy() {
		_xdb_verify_unsafe_();
		return new StoredBuffRole(this);
	}

	@Override
	public xbean.StoredBuffRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.StoredBuffRole toBean() {
		_xdb_verify_unsafe_();
		return new StoredBuffRole(this); // same as copy()
	}

	@Override
	public xbean.StoredBuffRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.StoredBuffRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.Buff> getBuffs() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "buffs"), buffs);
	}

	@Override
	public java.util.Map<Integer, xbean.Buff> getBuffsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.Buff> buffs;
		StoredBuffRole _o_ = this;
		buffs = new java.util.HashMap<Integer, xbean.Buff>();
		for (java.util.Map.Entry<Integer, xbean.Buff> _e_ : _o_.buffs.entrySet())
			buffs.put(_e_.getKey(), new Buff.Data(_e_.getValue()));
		return buffs;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		StoredBuffRole _o_ = null;
		if ( _o1_ instanceof StoredBuffRole ) _o_ = (StoredBuffRole)_o1_;
		else if ( _o1_ instanceof StoredBuffRole.Const ) _o_ = ((StoredBuffRole.Const)_o1_).nThis();
		else return false;
		if (!buffs.equals(_o_.buffs)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += buffs.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buffs);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("buffs"));
		return lb;
	}

	private class Const implements xbean.StoredBuffRole {
		StoredBuffRole nThis() {
			return StoredBuffRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.StoredBuffRole copy() {
			return StoredBuffRole.this.copy();
		}

		@Override
		public xbean.StoredBuffRole toData() {
			return StoredBuffRole.this.toData();
		}

		public xbean.StoredBuffRole toBean() {
			return StoredBuffRole.this.toBean();
		}

		@Override
		public xbean.StoredBuffRole toDataIf() {
			return StoredBuffRole.this.toDataIf();
		}

		public xbean.StoredBuffRole toBeanIf() {
			return StoredBuffRole.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.Buff> getBuffs() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(buffs);
		}

		@Override
		public java.util.Map<Integer, xbean.Buff> getBuffsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.Buff> buffs;
			StoredBuffRole _o_ = StoredBuffRole.this;
			buffs = new java.util.HashMap<Integer, xbean.Buff>();
			for (java.util.Map.Entry<Integer, xbean.Buff> _e_ : _o_.buffs.entrySet())
				buffs.put(_e_.getKey(), new Buff.Data(_e_.getValue()));
			return buffs;
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
			return StoredBuffRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return StoredBuffRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return StoredBuffRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return StoredBuffRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return StoredBuffRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return StoredBuffRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return StoredBuffRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return StoredBuffRole.this.hashCode();
		}

		@Override
		public String toString() {
			return StoredBuffRole.this.toString();
		}

	}

	public static final class Data implements xbean.StoredBuffRole {
		private java.util.HashMap<Integer, xbean.Buff> buffs; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			buffs = new java.util.HashMap<Integer, xbean.Buff>();
		}

		Data(xbean.StoredBuffRole _o1_) {
			if (_o1_ instanceof StoredBuffRole) assign((StoredBuffRole)_o1_);
			else if (_o1_ instanceof StoredBuffRole.Data) assign((StoredBuffRole.Data)_o1_);
			else if (_o1_ instanceof StoredBuffRole.Const) assign(((StoredBuffRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(StoredBuffRole _o_) {
			buffs = new java.util.HashMap<Integer, xbean.Buff>();
			for (java.util.Map.Entry<Integer, xbean.Buff> _e_ : _o_.buffs.entrySet())
				buffs.put(_e_.getKey(), new Buff.Data(_e_.getValue()));
		}

		private void assign(StoredBuffRole.Data _o_) {
			buffs = new java.util.HashMap<Integer, xbean.Buff>();
			for (java.util.Map.Entry<Integer, xbean.Buff> _e_ : _o_.buffs.entrySet())
				buffs.put(_e_.getKey(), new Buff.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(buffs.size());
			for (java.util.Map.Entry<Integer, xbean.Buff> _e_ : buffs.entrySet())
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
					buffs = new java.util.HashMap<Integer, xbean.Buff>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.Buff _v_ = xbean.Pod.newBuffData();
					_v_.unmarshal(_os_);
					buffs.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.StoredBuffRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.StoredBuffRole toData() {
			return new Data(this);
		}

		public xbean.StoredBuffRole toBean() {
			return new StoredBuffRole(this, null, null);
		}

		@Override
		public xbean.StoredBuffRole toDataIf() {
			return this;
		}

		public xbean.StoredBuffRole toBeanIf() {
			return new StoredBuffRole(this, null, null);
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
		public java.util.Map<Integer, xbean.Buff> getBuffs() { // 
			return buffs;
		}

		@Override
		public java.util.Map<Integer, xbean.Buff> getBuffsAsData() { // 
			return buffs;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof StoredBuffRole.Data)) return false;
			StoredBuffRole.Data _o_ = (StoredBuffRole.Data) _o1_;
			if (!buffs.equals(_o_.buffs)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += buffs.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(buffs);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleRecourse extends mkdb.XBean implements xbean.RoleRecourse {
	private java.util.HashMap<Integer, xbean.TaskRecourse> recoursetask; // 

	@Override
	public void _reset_unsafe_() {
		recoursetask.clear();
	}

	RoleRecourse(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>();
	}

	public RoleRecourse() {
		this(0, null, null);
	}

	public RoleRecourse(RoleRecourse _o_) {
		this(_o_, null, null);
	}

	RoleRecourse(xbean.RoleRecourse _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleRecourse) assign((RoleRecourse)_o1_);
		else if (_o1_ instanceof RoleRecourse.Data) assign((RoleRecourse.Data)_o1_);
		else if (_o1_ instanceof RoleRecourse.Const) assign(((RoleRecourse.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleRecourse _o_) {
		_o_._xdb_verify_unsafe_();
		recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>();
		for (java.util.Map.Entry<Integer, xbean.TaskRecourse> _e_ : _o_.recoursetask.entrySet())
			recoursetask.put(_e_.getKey(), new TaskRecourse(_e_.getValue(), this, "recoursetask"));
	}

	private void assign(RoleRecourse.Data _o_) {
		recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>();
		for (java.util.Map.Entry<Integer, xbean.TaskRecourse> _e_ : _o_.recoursetask.entrySet())
			recoursetask.put(_e_.getKey(), new TaskRecourse(_e_.getValue(), this, "recoursetask"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(recoursetask.size());
		for (java.util.Map.Entry<Integer, xbean.TaskRecourse> _e_ : recoursetask.entrySet())
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
				recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.TaskRecourse _v_ = new TaskRecourse(0, this, "recoursetask");
				_v_.unmarshal(_os_);
				recoursetask.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleRecourse copy() {
		_xdb_verify_unsafe_();
		return new RoleRecourse(this);
	}

	@Override
	public xbean.RoleRecourse toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleRecourse toBean() {
		_xdb_verify_unsafe_();
		return new RoleRecourse(this); // same as copy()
	}

	@Override
	public xbean.RoleRecourse toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleRecourse toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.TaskRecourse> getRecoursetask() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "recoursetask"), recoursetask);
	}

	@Override
	public java.util.Map<Integer, xbean.TaskRecourse> getRecoursetaskAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.TaskRecourse> recoursetask;
		RoleRecourse _o_ = this;
		recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>();
		for (java.util.Map.Entry<Integer, xbean.TaskRecourse> _e_ : _o_.recoursetask.entrySet())
			recoursetask.put(_e_.getKey(), new TaskRecourse.Data(_e_.getValue()));
		return recoursetask;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleRecourse _o_ = null;
		if ( _o1_ instanceof RoleRecourse ) _o_ = (RoleRecourse)_o1_;
		else if ( _o1_ instanceof RoleRecourse.Const ) _o_ = ((RoleRecourse.Const)_o1_).nThis();
		else return false;
		if (!recoursetask.equals(_o_.recoursetask)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += recoursetask.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(recoursetask);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("recoursetask"));
		return lb;
	}

	private class Const implements xbean.RoleRecourse {
		RoleRecourse nThis() {
			return RoleRecourse.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleRecourse copy() {
			return RoleRecourse.this.copy();
		}

		@Override
		public xbean.RoleRecourse toData() {
			return RoleRecourse.this.toData();
		}

		public xbean.RoleRecourse toBean() {
			return RoleRecourse.this.toBean();
		}

		@Override
		public xbean.RoleRecourse toDataIf() {
			return RoleRecourse.this.toDataIf();
		}

		public xbean.RoleRecourse toBeanIf() {
			return RoleRecourse.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.TaskRecourse> getRecoursetask() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(recoursetask);
		}

		@Override
		public java.util.Map<Integer, xbean.TaskRecourse> getRecoursetaskAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.TaskRecourse> recoursetask;
			RoleRecourse _o_ = RoleRecourse.this;
			recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>();
			for (java.util.Map.Entry<Integer, xbean.TaskRecourse> _e_ : _o_.recoursetask.entrySet())
				recoursetask.put(_e_.getKey(), new TaskRecourse.Data(_e_.getValue()));
			return recoursetask;
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
			return RoleRecourse.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleRecourse.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleRecourse.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleRecourse.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleRecourse.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleRecourse.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleRecourse.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleRecourse.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleRecourse.this.toString();
		}

	}

	public static final class Data implements xbean.RoleRecourse {
		private java.util.HashMap<Integer, xbean.TaskRecourse> recoursetask; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>();
		}

		Data(xbean.RoleRecourse _o1_) {
			if (_o1_ instanceof RoleRecourse) assign((RoleRecourse)_o1_);
			else if (_o1_ instanceof RoleRecourse.Data) assign((RoleRecourse.Data)_o1_);
			else if (_o1_ instanceof RoleRecourse.Const) assign(((RoleRecourse.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleRecourse _o_) {
			recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>();
			for (java.util.Map.Entry<Integer, xbean.TaskRecourse> _e_ : _o_.recoursetask.entrySet())
				recoursetask.put(_e_.getKey(), new TaskRecourse.Data(_e_.getValue()));
		}

		private void assign(RoleRecourse.Data _o_) {
			recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>();
			for (java.util.Map.Entry<Integer, xbean.TaskRecourse> _e_ : _o_.recoursetask.entrySet())
				recoursetask.put(_e_.getKey(), new TaskRecourse.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(recoursetask.size());
			for (java.util.Map.Entry<Integer, xbean.TaskRecourse> _e_ : recoursetask.entrySet())
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
					recoursetask = new java.util.HashMap<Integer, xbean.TaskRecourse>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.TaskRecourse _v_ = xbean.Pod.newTaskRecourseData();
					_v_.unmarshal(_os_);
					recoursetask.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleRecourse copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleRecourse toData() {
			return new Data(this);
		}

		public xbean.RoleRecourse toBean() {
			return new RoleRecourse(this, null, null);
		}

		@Override
		public xbean.RoleRecourse toDataIf() {
			return this;
		}

		public xbean.RoleRecourse toBeanIf() {
			return new RoleRecourse(this, null, null);
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
		public java.util.Map<Integer, xbean.TaskRecourse> getRecoursetask() { // 
			return recoursetask;
		}

		@Override
		public java.util.Map<Integer, xbean.TaskRecourse> getRecoursetaskAsData() { // 
			return recoursetask;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleRecourse.Data)) return false;
			RoleRecourse.Data _o_ = (RoleRecourse.Data) _o1_;
			if (!recoursetask.equals(_o_.recoursetask)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += recoursetask.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(recoursetask);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

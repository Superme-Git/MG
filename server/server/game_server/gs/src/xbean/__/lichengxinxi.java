
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class lichengxinxi extends mkdb.XBean implements xbean.lichengxinxi {
	private java.util.HashMap<Integer, xbean.Course> licheng; // 

	@Override
	public void _reset_unsafe_() {
		licheng.clear();
	}

	lichengxinxi(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		licheng = new java.util.HashMap<Integer, xbean.Course>();
	}

	public lichengxinxi() {
		this(0, null, null);
	}

	public lichengxinxi(lichengxinxi _o_) {
		this(_o_, null, null);
	}

	lichengxinxi(xbean.lichengxinxi _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof lichengxinxi) assign((lichengxinxi)_o1_);
		else if (_o1_ instanceof lichengxinxi.Data) assign((lichengxinxi.Data)_o1_);
		else if (_o1_ instanceof lichengxinxi.Const) assign(((lichengxinxi.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(lichengxinxi _o_) {
		_o_._xdb_verify_unsafe_();
		licheng = new java.util.HashMap<Integer, xbean.Course>();
		for (java.util.Map.Entry<Integer, xbean.Course> _e_ : _o_.licheng.entrySet())
			licheng.put(_e_.getKey(), new Course(_e_.getValue(), this, "licheng"));
	}

	private void assign(lichengxinxi.Data _o_) {
		licheng = new java.util.HashMap<Integer, xbean.Course>();
		for (java.util.Map.Entry<Integer, xbean.Course> _e_ : _o_.licheng.entrySet())
			licheng.put(_e_.getKey(), new Course(_e_.getValue(), this, "licheng"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(licheng.size());
		for (java.util.Map.Entry<Integer, xbean.Course> _e_ : licheng.entrySet())
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
				licheng = new java.util.HashMap<Integer, xbean.Course>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Course _v_ = new Course(0, this, "licheng");
				_v_.unmarshal(_os_);
				licheng.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.lichengxinxi copy() {
		_xdb_verify_unsafe_();
		return new lichengxinxi(this);
	}

	@Override
	public xbean.lichengxinxi toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.lichengxinxi toBean() {
		_xdb_verify_unsafe_();
		return new lichengxinxi(this); // same as copy()
	}

	@Override
	public xbean.lichengxinxi toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.lichengxinxi toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.Course> getLicheng() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "licheng"), licheng);
	}

	@Override
	public java.util.Map<Integer, xbean.Course> getLichengAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.Course> licheng;
		lichengxinxi _o_ = this;
		licheng = new java.util.HashMap<Integer, xbean.Course>();
		for (java.util.Map.Entry<Integer, xbean.Course> _e_ : _o_.licheng.entrySet())
			licheng.put(_e_.getKey(), new Course.Data(_e_.getValue()));
		return licheng;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		lichengxinxi _o_ = null;
		if ( _o1_ instanceof lichengxinxi ) _o_ = (lichengxinxi)_o1_;
		else if ( _o1_ instanceof lichengxinxi.Const ) _o_ = ((lichengxinxi.Const)_o1_).nThis();
		else return false;
		if (!licheng.equals(_o_.licheng)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += licheng.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(licheng);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("licheng"));
		return lb;
	}

	private class Const implements xbean.lichengxinxi {
		lichengxinxi nThis() {
			return lichengxinxi.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.lichengxinxi copy() {
			return lichengxinxi.this.copy();
		}

		@Override
		public xbean.lichengxinxi toData() {
			return lichengxinxi.this.toData();
		}

		public xbean.lichengxinxi toBean() {
			return lichengxinxi.this.toBean();
		}

		@Override
		public xbean.lichengxinxi toDataIf() {
			return lichengxinxi.this.toDataIf();
		}

		public xbean.lichengxinxi toBeanIf() {
			return lichengxinxi.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.Course> getLicheng() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(licheng);
		}

		@Override
		public java.util.Map<Integer, xbean.Course> getLichengAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.Course> licheng;
			lichengxinxi _o_ = lichengxinxi.this;
			licheng = new java.util.HashMap<Integer, xbean.Course>();
			for (java.util.Map.Entry<Integer, xbean.Course> _e_ : _o_.licheng.entrySet())
				licheng.put(_e_.getKey(), new Course.Data(_e_.getValue()));
			return licheng;
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
			return lichengxinxi.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return lichengxinxi.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return lichengxinxi.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return lichengxinxi.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return lichengxinxi.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return lichengxinxi.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return lichengxinxi.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return lichengxinxi.this.hashCode();
		}

		@Override
		public String toString() {
			return lichengxinxi.this.toString();
		}

	}

	public static final class Data implements xbean.lichengxinxi {
		private java.util.HashMap<Integer, xbean.Course> licheng; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			licheng = new java.util.HashMap<Integer, xbean.Course>();
		}

		Data(xbean.lichengxinxi _o1_) {
			if (_o1_ instanceof lichengxinxi) assign((lichengxinxi)_o1_);
			else if (_o1_ instanceof lichengxinxi.Data) assign((lichengxinxi.Data)_o1_);
			else if (_o1_ instanceof lichengxinxi.Const) assign(((lichengxinxi.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(lichengxinxi _o_) {
			licheng = new java.util.HashMap<Integer, xbean.Course>();
			for (java.util.Map.Entry<Integer, xbean.Course> _e_ : _o_.licheng.entrySet())
				licheng.put(_e_.getKey(), new Course.Data(_e_.getValue()));
		}

		private void assign(lichengxinxi.Data _o_) {
			licheng = new java.util.HashMap<Integer, xbean.Course>();
			for (java.util.Map.Entry<Integer, xbean.Course> _e_ : _o_.licheng.entrySet())
				licheng.put(_e_.getKey(), new Course.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(licheng.size());
			for (java.util.Map.Entry<Integer, xbean.Course> _e_ : licheng.entrySet())
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
					licheng = new java.util.HashMap<Integer, xbean.Course>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.Course _v_ = xbean.Pod.newCourseData();
					_v_.unmarshal(_os_);
					licheng.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.lichengxinxi copy() {
			return new Data(this);
		}

		@Override
		public xbean.lichengxinxi toData() {
			return new Data(this);
		}

		public xbean.lichengxinxi toBean() {
			return new lichengxinxi(this, null, null);
		}

		@Override
		public xbean.lichengxinxi toDataIf() {
			return this;
		}

		public xbean.lichengxinxi toBeanIf() {
			return new lichengxinxi(this, null, null);
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
		public java.util.Map<Integer, xbean.Course> getLicheng() { // 
			return licheng;
		}

		@Override
		public java.util.Map<Integer, xbean.Course> getLichengAsData() { // 
			return licheng;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof lichengxinxi.Data)) return false;
			lichengxinxi.Data _o_ = (lichengxinxi.Data) _o1_;
			if (!licheng.equals(_o_.licheng)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += licheng.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(licheng);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

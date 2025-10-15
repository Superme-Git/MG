
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FunOpenClose extends mkdb.XBean implements xbean.FunOpenClose {
	private java.util.HashMap<Integer, Integer> funmap; // 

	@Override
	public void _reset_unsafe_() {
		funmap.clear();
	}

	FunOpenClose(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		funmap = new java.util.HashMap<Integer, Integer>();
	}

	public FunOpenClose() {
		this(0, null, null);
	}

	public FunOpenClose(FunOpenClose _o_) {
		this(_o_, null, null);
	}

	FunOpenClose(xbean.FunOpenClose _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FunOpenClose) assign((FunOpenClose)_o1_);
		else if (_o1_ instanceof FunOpenClose.Data) assign((FunOpenClose.Data)_o1_);
		else if (_o1_ instanceof FunOpenClose.Const) assign(((FunOpenClose.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FunOpenClose _o_) {
		_o_._xdb_verify_unsafe_();
		funmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.funmap.entrySet())
			funmap.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(FunOpenClose.Data _o_) {
		funmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.funmap.entrySet())
			funmap.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(funmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : funmap.entrySet())
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
				funmap = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				funmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.FunOpenClose copy() {
		_xdb_verify_unsafe_();
		return new FunOpenClose(this);
	}

	@Override
	public xbean.FunOpenClose toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FunOpenClose toBean() {
		_xdb_verify_unsafe_();
		return new FunOpenClose(this); // same as copy()
	}

	@Override
	public xbean.FunOpenClose toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FunOpenClose toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getFunmap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "funmap"), funmap);
	}

	@Override
	public java.util.Map<Integer, Integer> getFunmapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> funmap;
		FunOpenClose _o_ = this;
		funmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.funmap.entrySet())
			funmap.put(_e_.getKey(), _e_.getValue());
		return funmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FunOpenClose _o_ = null;
		if ( _o1_ instanceof FunOpenClose ) _o_ = (FunOpenClose)_o1_;
		else if ( _o1_ instanceof FunOpenClose.Const ) _o_ = ((FunOpenClose.Const)_o1_).nThis();
		else return false;
		if (!funmap.equals(_o_.funmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += funmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(funmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("funmap"));
		return lb;
	}

	private class Const implements xbean.FunOpenClose {
		FunOpenClose nThis() {
			return FunOpenClose.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FunOpenClose copy() {
			return FunOpenClose.this.copy();
		}

		@Override
		public xbean.FunOpenClose toData() {
			return FunOpenClose.this.toData();
		}

		public xbean.FunOpenClose toBean() {
			return FunOpenClose.this.toBean();
		}

		@Override
		public xbean.FunOpenClose toDataIf() {
			return FunOpenClose.this.toDataIf();
		}

		public xbean.FunOpenClose toBeanIf() {
			return FunOpenClose.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getFunmap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(funmap);
		}

		@Override
		public java.util.Map<Integer, Integer> getFunmapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> funmap;
			FunOpenClose _o_ = FunOpenClose.this;
			funmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.funmap.entrySet())
				funmap.put(_e_.getKey(), _e_.getValue());
			return funmap;
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
			return FunOpenClose.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FunOpenClose.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FunOpenClose.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FunOpenClose.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FunOpenClose.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FunOpenClose.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FunOpenClose.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FunOpenClose.this.hashCode();
		}

		@Override
		public String toString() {
			return FunOpenClose.this.toString();
		}

	}

	public static final class Data implements xbean.FunOpenClose {
		private java.util.HashMap<Integer, Integer> funmap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			funmap = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.FunOpenClose _o1_) {
			if (_o1_ instanceof FunOpenClose) assign((FunOpenClose)_o1_);
			else if (_o1_ instanceof FunOpenClose.Data) assign((FunOpenClose.Data)_o1_);
			else if (_o1_ instanceof FunOpenClose.Const) assign(((FunOpenClose.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FunOpenClose _o_) {
			funmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.funmap.entrySet())
				funmap.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(FunOpenClose.Data _o_) {
			funmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.funmap.entrySet())
				funmap.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(funmap.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : funmap.entrySet())
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
					funmap = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					funmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.FunOpenClose copy() {
			return new Data(this);
		}

		@Override
		public xbean.FunOpenClose toData() {
			return new Data(this);
		}

		public xbean.FunOpenClose toBean() {
			return new FunOpenClose(this, null, null);
		}

		@Override
		public xbean.FunOpenClose toDataIf() {
			return this;
		}

		public xbean.FunOpenClose toBeanIf() {
			return new FunOpenClose(this, null, null);
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
		public java.util.Map<Integer, Integer> getFunmap() { // 
			return funmap;
		}

		@Override
		public java.util.Map<Integer, Integer> getFunmapAsData() { // 
			return funmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FunOpenClose.Data)) return false;
			FunOpenClose.Data _o_ = (FunOpenClose.Data) _o1_;
			if (!funmap.equals(_o_.funmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += funmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(funmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

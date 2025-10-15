
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BiaoQing extends mkdb.XBean implements xbean.BiaoQing {
	private java.util.HashMap<Integer, Long> biaoqinginfos; // 

	@Override
	public void _reset_unsafe_() {
		biaoqinginfos.clear();
	}

	BiaoQing(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		biaoqinginfos = new java.util.HashMap<Integer, Long>();
	}

	public BiaoQing() {
		this(0, null, null);
	}

	public BiaoQing(BiaoQing _o_) {
		this(_o_, null, null);
	}

	BiaoQing(xbean.BiaoQing _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BiaoQing) assign((BiaoQing)_o1_);
		else if (_o1_ instanceof BiaoQing.Data) assign((BiaoQing.Data)_o1_);
		else if (_o1_ instanceof BiaoQing.Const) assign(((BiaoQing.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BiaoQing _o_) {
		_o_._xdb_verify_unsafe_();
		biaoqinginfos = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.biaoqinginfos.entrySet())
			biaoqinginfos.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(BiaoQing.Data _o_) {
		biaoqinginfos = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.biaoqinginfos.entrySet())
			biaoqinginfos.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(biaoqinginfos.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : biaoqinginfos.entrySet())
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
				biaoqinginfos = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				biaoqinginfos.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.BiaoQing copy() {
		_xdb_verify_unsafe_();
		return new BiaoQing(this);
	}

	@Override
	public xbean.BiaoQing toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BiaoQing toBean() {
		_xdb_verify_unsafe_();
		return new BiaoQing(this); // same as copy()
	}

	@Override
	public xbean.BiaoQing toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BiaoQing toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getBiaoqinginfos() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "biaoqinginfos"), biaoqinginfos);
	}

	@Override
	public java.util.Map<Integer, Long> getBiaoqinginfosAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> biaoqinginfos;
		BiaoQing _o_ = this;
		biaoqinginfos = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.biaoqinginfos.entrySet())
			biaoqinginfos.put(_e_.getKey(), _e_.getValue());
		return biaoqinginfos;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BiaoQing _o_ = null;
		if ( _o1_ instanceof BiaoQing ) _o_ = (BiaoQing)_o1_;
		else if ( _o1_ instanceof BiaoQing.Const ) _o_ = ((BiaoQing.Const)_o1_).nThis();
		else return false;
		if (!biaoqinginfos.equals(_o_.biaoqinginfos)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += biaoqinginfos.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(biaoqinginfos);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("biaoqinginfos"));
		return lb;
	}

	private class Const implements xbean.BiaoQing {
		BiaoQing nThis() {
			return BiaoQing.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BiaoQing copy() {
			return BiaoQing.this.copy();
		}

		@Override
		public xbean.BiaoQing toData() {
			return BiaoQing.this.toData();
		}

		public xbean.BiaoQing toBean() {
			return BiaoQing.this.toBean();
		}

		@Override
		public xbean.BiaoQing toDataIf() {
			return BiaoQing.this.toDataIf();
		}

		public xbean.BiaoQing toBeanIf() {
			return BiaoQing.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getBiaoqinginfos() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(biaoqinginfos);
		}

		@Override
		public java.util.Map<Integer, Long> getBiaoqinginfosAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> biaoqinginfos;
			BiaoQing _o_ = BiaoQing.this;
			biaoqinginfos = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.biaoqinginfos.entrySet())
				biaoqinginfos.put(_e_.getKey(), _e_.getValue());
			return biaoqinginfos;
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
			return BiaoQing.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BiaoQing.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BiaoQing.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BiaoQing.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BiaoQing.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BiaoQing.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BiaoQing.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BiaoQing.this.hashCode();
		}

		@Override
		public String toString() {
			return BiaoQing.this.toString();
		}

	}

	public static final class Data implements xbean.BiaoQing {
		private java.util.HashMap<Integer, Long> biaoqinginfos; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			biaoqinginfos = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.BiaoQing _o1_) {
			if (_o1_ instanceof BiaoQing) assign((BiaoQing)_o1_);
			else if (_o1_ instanceof BiaoQing.Data) assign((BiaoQing.Data)_o1_);
			else if (_o1_ instanceof BiaoQing.Const) assign(((BiaoQing.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BiaoQing _o_) {
			biaoqinginfos = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.biaoqinginfos.entrySet())
				biaoqinginfos.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(BiaoQing.Data _o_) {
			biaoqinginfos = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.biaoqinginfos.entrySet())
				biaoqinginfos.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(biaoqinginfos.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : biaoqinginfos.entrySet())
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
					biaoqinginfos = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					biaoqinginfos.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.BiaoQing copy() {
			return new Data(this);
		}

		@Override
		public xbean.BiaoQing toData() {
			return new Data(this);
		}

		public xbean.BiaoQing toBean() {
			return new BiaoQing(this, null, null);
		}

		@Override
		public xbean.BiaoQing toDataIf() {
			return this;
		}

		public xbean.BiaoQing toBeanIf() {
			return new BiaoQing(this, null, null);
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
		public java.util.Map<Integer, Long> getBiaoqinginfos() { // 
			return biaoqinginfos;
		}

		@Override
		public java.util.Map<Integer, Long> getBiaoqinginfosAsData() { // 
			return biaoqinginfos;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BiaoQing.Data)) return false;
			BiaoQing.Data _o_ = (BiaoQing.Data) _o1_;
			if (!biaoqinginfos.equals(_o_.biaoqinginfos)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += biaoqinginfos.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(biaoqinginfos);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

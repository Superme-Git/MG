
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EPet extends mkdb.XBean implements xbean.EPet {
	private java.util.HashMap<Integer, Float> effects; // key = effect type id
	private java.util.HashMap<Integer, Float> finalattrs; // key = attr type

	@Override
	public void _reset_unsafe_() {
		effects.clear();
		finalattrs.clear();
	}

	EPet(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		effects = new java.util.HashMap<Integer, Float>();
		finalattrs = new java.util.HashMap<Integer, Float>();
	}

	public EPet() {
		this(0, null, null);
	}

	public EPet(EPet _o_) {
		this(_o_, null, null);
	}

	EPet(xbean.EPet _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EPet) assign((EPet)_o1_);
		else if (_o1_ instanceof EPet.Data) assign((EPet.Data)_o1_);
		else if (_o1_ instanceof EPet.Const) assign(((EPet.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EPet _o_) {
		_o_._xdb_verify_unsafe_();
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(EPet.Data _o_) {
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(effects.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : effects.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(finalattrs.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : finalattrs.entrySet())
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
				effects = new java.util.HashMap<Integer, Float>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				float _v_ = 0.0f;
				_v_ = _os_.unmarshal_float();
				effects.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				finalattrs = new java.util.HashMap<Integer, Float>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				float _v_ = 0.0f;
				_v_ = _os_.unmarshal_float();
				finalattrs.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.EPet copy() {
		_xdb_verify_unsafe_();
		return new EPet(this);
	}

	@Override
	public xbean.EPet toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EPet toBean() {
		_xdb_verify_unsafe_();
		return new EPet(this); // same as copy()
	}

	@Override
	public xbean.EPet toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EPet toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Float> getEffects() { // key = effect type id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "effects"), effects);
	}

	@Override
	public java.util.Map<Integer, Float> getEffectsAsData() { // key = effect type id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Float> effects;
		EPet _o_ = this;
		effects = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
			effects.put(_e_.getKey(), _e_.getValue());
		return effects;
	}

	@Override
	public java.util.Map<Integer, Float> getFinalattrs() { // key = attr type
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "finalattrs"), finalattrs);
	}

	@Override
	public java.util.Map<Integer, Float> getFinalattrsAsData() { // key = attr type
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Float> finalattrs;
		EPet _o_ = this;
		finalattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
			finalattrs.put(_e_.getKey(), _e_.getValue());
		return finalattrs;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EPet _o_ = null;
		if ( _o1_ instanceof EPet ) _o_ = (EPet)_o1_;
		else if ( _o1_ instanceof EPet.Const ) _o_ = ((EPet.Const)_o1_).nThis();
		else return false;
		if (!effects.equals(_o_.effects)) return false;
		if (!finalattrs.equals(_o_.finalattrs)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += effects.hashCode();
		_h_ += finalattrs.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(effects);
		_sb_.append(",");
		_sb_.append(finalattrs);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("effects"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("finalattrs"));
		return lb;
	}

	private class Const implements xbean.EPet {
		EPet nThis() {
			return EPet.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EPet copy() {
			return EPet.this.copy();
		}

		@Override
		public xbean.EPet toData() {
			return EPet.this.toData();
		}

		public xbean.EPet toBean() {
			return EPet.this.toBean();
		}

		@Override
		public xbean.EPet toDataIf() {
			return EPet.this.toDataIf();
		}

		public xbean.EPet toBeanIf() {
			return EPet.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Float> getEffects() { // key = effect type id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(effects);
		}

		@Override
		public java.util.Map<Integer, Float> getEffectsAsData() { // key = effect type id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Float> effects;
			EPet _o_ = EPet.this;
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrs() { // key = attr type
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(finalattrs);
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrsAsData() { // key = attr type
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Float> finalattrs;
			EPet _o_ = EPet.this;
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
			return finalattrs;
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
			return EPet.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EPet.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EPet.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EPet.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EPet.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EPet.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EPet.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EPet.this.hashCode();
		}

		@Override
		public String toString() {
			return EPet.this.toString();
		}

	}

	public static final class Data implements xbean.EPet {
		private java.util.HashMap<Integer, Float> effects; // key = effect type id
		private java.util.HashMap<Integer, Float> finalattrs; // key = attr type

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			effects = new java.util.HashMap<Integer, Float>();
			finalattrs = new java.util.HashMap<Integer, Float>();
		}

		Data(xbean.EPet _o1_) {
			if (_o1_ instanceof EPet) assign((EPet)_o1_);
			else if (_o1_ instanceof EPet.Data) assign((EPet.Data)_o1_);
			else if (_o1_ instanceof EPet.Const) assign(((EPet.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EPet _o_) {
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(EPet.Data _o_) {
			effects = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.effects.entrySet())
				effects.put(_e_.getKey(), _e_.getValue());
			finalattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.finalattrs.entrySet())
				finalattrs.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(effects.size());
			for (java.util.Map.Entry<Integer, Float> _e_ : effects.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(finalattrs.size());
			for (java.util.Map.Entry<Integer, Float> _e_ : finalattrs.entrySet())
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
					effects = new java.util.HashMap<Integer, Float>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					float _v_ = 0.0f;
					_v_ = _os_.unmarshal_float();
					effects.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					finalattrs = new java.util.HashMap<Integer, Float>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					float _v_ = 0.0f;
					_v_ = _os_.unmarshal_float();
					finalattrs.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.EPet copy() {
			return new Data(this);
		}

		@Override
		public xbean.EPet toData() {
			return new Data(this);
		}

		public xbean.EPet toBean() {
			return new EPet(this, null, null);
		}

		@Override
		public xbean.EPet toDataIf() {
			return this;
		}

		public xbean.EPet toBeanIf() {
			return new EPet(this, null, null);
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
		public java.util.Map<Integer, Float> getEffects() { // key = effect type id
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getEffectsAsData() { // key = effect type id
			return effects;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrs() { // key = attr type
			return finalattrs;
		}

		@Override
		public java.util.Map<Integer, Float> getFinalattrsAsData() { // key = attr type
			return finalattrs;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EPet.Data)) return false;
			EPet.Data _o_ = (EPet.Data) _o1_;
			if (!effects.equals(_o_.effects)) return false;
			if (!finalattrs.equals(_o_.finalattrs)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += effects.hashCode();
			_h_ += finalattrs.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(effects);
			_sb_.append(",");
			_sb_.append(finalattrs);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

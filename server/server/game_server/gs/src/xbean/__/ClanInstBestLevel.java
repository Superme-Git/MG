
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanInstBestLevel extends mkdb.XBean implements xbean.ClanInstBestLevel {
	private java.util.HashMap<Integer, Long> instsave; // 最先通过公会副本某一层的公会,key 层数, value 公会key

	@Override
	public void _reset_unsafe_() {
		instsave.clear();
	}

	ClanInstBestLevel(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		instsave = new java.util.HashMap<Integer, Long>();
	}

	public ClanInstBestLevel() {
		this(0, null, null);
	}

	public ClanInstBestLevel(ClanInstBestLevel _o_) {
		this(_o_, null, null);
	}

	ClanInstBestLevel(xbean.ClanInstBestLevel _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanInstBestLevel) assign((ClanInstBestLevel)_o1_);
		else if (_o1_ instanceof ClanInstBestLevel.Data) assign((ClanInstBestLevel.Data)_o1_);
		else if (_o1_ instanceof ClanInstBestLevel.Const) assign(((ClanInstBestLevel.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanInstBestLevel _o_) {
		_o_._xdb_verify_unsafe_();
		instsave = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instsave.entrySet())
			instsave.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(ClanInstBestLevel.Data _o_) {
		instsave = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instsave.entrySet())
			instsave.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(instsave.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : instsave.entrySet())
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
				instsave = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				instsave.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ClanInstBestLevel copy() {
		_xdb_verify_unsafe_();
		return new ClanInstBestLevel(this);
	}

	@Override
	public xbean.ClanInstBestLevel toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanInstBestLevel toBean() {
		_xdb_verify_unsafe_();
		return new ClanInstBestLevel(this); // same as copy()
	}

	@Override
	public xbean.ClanInstBestLevel toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanInstBestLevel toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getInstsave() { // 最先通过公会副本某一层的公会,key 层数, value 公会key
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "instsave"), instsave);
	}

	@Override
	public java.util.Map<Integer, Long> getInstsaveAsData() { // 最先通过公会副本某一层的公会,key 层数, value 公会key
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> instsave;
		ClanInstBestLevel _o_ = this;
		instsave = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instsave.entrySet())
			instsave.put(_e_.getKey(), _e_.getValue());
		return instsave;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanInstBestLevel _o_ = null;
		if ( _o1_ instanceof ClanInstBestLevel ) _o_ = (ClanInstBestLevel)_o1_;
		else if ( _o1_ instanceof ClanInstBestLevel.Const ) _o_ = ((ClanInstBestLevel.Const)_o1_).nThis();
		else return false;
		if (!instsave.equals(_o_.instsave)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += instsave.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instsave);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("instsave"));
		return lb;
	}

	private class Const implements xbean.ClanInstBestLevel {
		ClanInstBestLevel nThis() {
			return ClanInstBestLevel.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanInstBestLevel copy() {
			return ClanInstBestLevel.this.copy();
		}

		@Override
		public xbean.ClanInstBestLevel toData() {
			return ClanInstBestLevel.this.toData();
		}

		public xbean.ClanInstBestLevel toBean() {
			return ClanInstBestLevel.this.toBean();
		}

		@Override
		public xbean.ClanInstBestLevel toDataIf() {
			return ClanInstBestLevel.this.toDataIf();
		}

		public xbean.ClanInstBestLevel toBeanIf() {
			return ClanInstBestLevel.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getInstsave() { // 最先通过公会副本某一层的公会,key 层数, value 公会key
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(instsave);
		}

		@Override
		public java.util.Map<Integer, Long> getInstsaveAsData() { // 最先通过公会副本某一层的公会,key 层数, value 公会key
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> instsave;
			ClanInstBestLevel _o_ = ClanInstBestLevel.this;
			instsave = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instsave.entrySet())
				instsave.put(_e_.getKey(), _e_.getValue());
			return instsave;
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
			return ClanInstBestLevel.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanInstBestLevel.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanInstBestLevel.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanInstBestLevel.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanInstBestLevel.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanInstBestLevel.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanInstBestLevel.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanInstBestLevel.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanInstBestLevel.this.toString();
		}

	}

	public static final class Data implements xbean.ClanInstBestLevel {
		private java.util.HashMap<Integer, Long> instsave; // 最先通过公会副本某一层的公会,key 层数, value 公会key

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			instsave = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.ClanInstBestLevel _o1_) {
			if (_o1_ instanceof ClanInstBestLevel) assign((ClanInstBestLevel)_o1_);
			else if (_o1_ instanceof ClanInstBestLevel.Data) assign((ClanInstBestLevel.Data)_o1_);
			else if (_o1_ instanceof ClanInstBestLevel.Const) assign(((ClanInstBestLevel.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanInstBestLevel _o_) {
			instsave = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instsave.entrySet())
				instsave.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(ClanInstBestLevel.Data _o_) {
			instsave = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.instsave.entrySet())
				instsave.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(instsave.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : instsave.entrySet())
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
					instsave = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					instsave.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ClanInstBestLevel copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanInstBestLevel toData() {
			return new Data(this);
		}

		public xbean.ClanInstBestLevel toBean() {
			return new ClanInstBestLevel(this, null, null);
		}

		@Override
		public xbean.ClanInstBestLevel toDataIf() {
			return this;
		}

		public xbean.ClanInstBestLevel toBeanIf() {
			return new ClanInstBestLevel(this, null, null);
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
		public java.util.Map<Integer, Long> getInstsave() { // 最先通过公会副本某一层的公会,key 层数, value 公会key
			return instsave;
		}

		@Override
		public java.util.Map<Integer, Long> getInstsaveAsData() { // 最先通过公会副本某一层的公会,key 层数, value 公会key
			return instsave;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanInstBestLevel.Data)) return false;
			ClanInstBestLevel.Data _o_ = (ClanInstBestLevel.Data) _o1_;
			if (!instsave.equals(_o_.instsave)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += instsave.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(instsave);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

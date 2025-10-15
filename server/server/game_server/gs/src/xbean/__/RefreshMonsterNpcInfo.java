
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RefreshMonsterNpcInfo extends mkdb.XBean implements xbean.RefreshMonsterNpcInfo {
	private java.util.HashMap<Long, Integer> npcinfos; // //npckey 对应的战斗状态 0=空闲  1=战斗中

	@Override
	public void _reset_unsafe_() {
		npcinfos.clear();
	}

	RefreshMonsterNpcInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		npcinfos = new java.util.HashMap<Long, Integer>();
	}

	public RefreshMonsterNpcInfo() {
		this(0, null, null);
	}

	public RefreshMonsterNpcInfo(RefreshMonsterNpcInfo _o_) {
		this(_o_, null, null);
	}

	RefreshMonsterNpcInfo(xbean.RefreshMonsterNpcInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RefreshMonsterNpcInfo) assign((RefreshMonsterNpcInfo)_o1_);
		else if (_o1_ instanceof RefreshMonsterNpcInfo.Data) assign((RefreshMonsterNpcInfo.Data)_o1_);
		else if (_o1_ instanceof RefreshMonsterNpcInfo.Const) assign(((RefreshMonsterNpcInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RefreshMonsterNpcInfo _o_) {
		_o_._xdb_verify_unsafe_();
		npcinfos = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.npcinfos.entrySet())
			npcinfos.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(RefreshMonsterNpcInfo.Data _o_) {
		npcinfos = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.npcinfos.entrySet())
			npcinfos.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(npcinfos.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : npcinfos.entrySet())
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
				npcinfos = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				npcinfos.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RefreshMonsterNpcInfo copy() {
		_xdb_verify_unsafe_();
		return new RefreshMonsterNpcInfo(this);
	}

	@Override
	public xbean.RefreshMonsterNpcInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RefreshMonsterNpcInfo toBean() {
		_xdb_verify_unsafe_();
		return new RefreshMonsterNpcInfo(this); // same as copy()
	}

	@Override
	public xbean.RefreshMonsterNpcInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RefreshMonsterNpcInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Integer> getNpcinfos() { // //npckey 对应的战斗状态 0=空闲  1=战斗中
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "npcinfos"), npcinfos);
	}

	@Override
	public java.util.Map<Long, Integer> getNpcinfosAsData() { // //npckey 对应的战斗状态 0=空闲  1=战斗中
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> npcinfos;
		RefreshMonsterNpcInfo _o_ = this;
		npcinfos = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.npcinfos.entrySet())
			npcinfos.put(_e_.getKey(), _e_.getValue());
		return npcinfos;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RefreshMonsterNpcInfo _o_ = null;
		if ( _o1_ instanceof RefreshMonsterNpcInfo ) _o_ = (RefreshMonsterNpcInfo)_o1_;
		else if ( _o1_ instanceof RefreshMonsterNpcInfo.Const ) _o_ = ((RefreshMonsterNpcInfo.Const)_o1_).nThis();
		else return false;
		if (!npcinfos.equals(_o_.npcinfos)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += npcinfos.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcinfos);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("npcinfos"));
		return lb;
	}

	private class Const implements xbean.RefreshMonsterNpcInfo {
		RefreshMonsterNpcInfo nThis() {
			return RefreshMonsterNpcInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RefreshMonsterNpcInfo copy() {
			return RefreshMonsterNpcInfo.this.copy();
		}

		@Override
		public xbean.RefreshMonsterNpcInfo toData() {
			return RefreshMonsterNpcInfo.this.toData();
		}

		public xbean.RefreshMonsterNpcInfo toBean() {
			return RefreshMonsterNpcInfo.this.toBean();
		}

		@Override
		public xbean.RefreshMonsterNpcInfo toDataIf() {
			return RefreshMonsterNpcInfo.this.toDataIf();
		}

		public xbean.RefreshMonsterNpcInfo toBeanIf() {
			return RefreshMonsterNpcInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Integer> getNpcinfos() { // //npckey 对应的战斗状态 0=空闲  1=战斗中
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(npcinfos);
		}

		@Override
		public java.util.Map<Long, Integer> getNpcinfosAsData() { // //npckey 对应的战斗状态 0=空闲  1=战斗中
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> npcinfos;
			RefreshMonsterNpcInfo _o_ = RefreshMonsterNpcInfo.this;
			npcinfos = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.npcinfos.entrySet())
				npcinfos.put(_e_.getKey(), _e_.getValue());
			return npcinfos;
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
			return RefreshMonsterNpcInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RefreshMonsterNpcInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RefreshMonsterNpcInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RefreshMonsterNpcInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RefreshMonsterNpcInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RefreshMonsterNpcInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RefreshMonsterNpcInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RefreshMonsterNpcInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RefreshMonsterNpcInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RefreshMonsterNpcInfo {
		private java.util.HashMap<Long, Integer> npcinfos; // //npckey 对应的战斗状态 0=空闲  1=战斗中

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			npcinfos = new java.util.HashMap<Long, Integer>();
		}

		Data(xbean.RefreshMonsterNpcInfo _o1_) {
			if (_o1_ instanceof RefreshMonsterNpcInfo) assign((RefreshMonsterNpcInfo)_o1_);
			else if (_o1_ instanceof RefreshMonsterNpcInfo.Data) assign((RefreshMonsterNpcInfo.Data)_o1_);
			else if (_o1_ instanceof RefreshMonsterNpcInfo.Const) assign(((RefreshMonsterNpcInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RefreshMonsterNpcInfo _o_) {
			npcinfos = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.npcinfos.entrySet())
				npcinfos.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(RefreshMonsterNpcInfo.Data _o_) {
			npcinfos = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.npcinfos.entrySet())
				npcinfos.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(npcinfos.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : npcinfos.entrySet())
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
					npcinfos = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					npcinfos.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RefreshMonsterNpcInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RefreshMonsterNpcInfo toData() {
			return new Data(this);
		}

		public xbean.RefreshMonsterNpcInfo toBean() {
			return new RefreshMonsterNpcInfo(this, null, null);
		}

		@Override
		public xbean.RefreshMonsterNpcInfo toDataIf() {
			return this;
		}

		public xbean.RefreshMonsterNpcInfo toBeanIf() {
			return new RefreshMonsterNpcInfo(this, null, null);
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
		public java.util.Map<Long, Integer> getNpcinfos() { // //npckey 对应的战斗状态 0=空闲  1=战斗中
			return npcinfos;
		}

		@Override
		public java.util.Map<Long, Integer> getNpcinfosAsData() { // //npckey 对应的战斗状态 0=空闲  1=战斗中
			return npcinfos;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RefreshMonsterNpcInfo.Data)) return false;
			RefreshMonsterNpcInfo.Data _o_ = (RefreshMonsterNpcInfo.Data) _o1_;
			if (!npcinfos.equals(_o_.npcinfos)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += npcinfos.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(npcinfos);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class npcBattleInfoCol extends mkdb.XBean implements xbean.npcBattleInfoCol {
	private java.util.HashMap<Long, Integer> battleroles; // key为roleid,value是role对应队伍的人数

	@Override
	public void _reset_unsafe_() {
		battleroles.clear();
	}

	npcBattleInfoCol(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		battleroles = new java.util.HashMap<Long, Integer>();
	}

	public npcBattleInfoCol() {
		this(0, null, null);
	}

	public npcBattleInfoCol(npcBattleInfoCol _o_) {
		this(_o_, null, null);
	}

	npcBattleInfoCol(xbean.npcBattleInfoCol _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof npcBattleInfoCol) assign((npcBattleInfoCol)_o1_);
		else if (_o1_ instanceof npcBattleInfoCol.Data) assign((npcBattleInfoCol.Data)_o1_);
		else if (_o1_ instanceof npcBattleInfoCol.Const) assign(((npcBattleInfoCol.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(npcBattleInfoCol _o_) {
		_o_._xdb_verify_unsafe_();
		battleroles = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.battleroles.entrySet())
			battleroles.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(npcBattleInfoCol.Data _o_) {
		battleroles = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.battleroles.entrySet())
			battleroles.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(battleroles.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : battleroles.entrySet())
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
				battleroles = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				battleroles.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.npcBattleInfoCol copy() {
		_xdb_verify_unsafe_();
		return new npcBattleInfoCol(this);
	}

	@Override
	public xbean.npcBattleInfoCol toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.npcBattleInfoCol toBean() {
		_xdb_verify_unsafe_();
		return new npcBattleInfoCol(this); // same as copy()
	}

	@Override
	public xbean.npcBattleInfoCol toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.npcBattleInfoCol toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Integer> getBattleroles() { // key为roleid,value是role对应队伍的人数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "battleroles"), battleroles);
	}

	@Override
	public java.util.Map<Long, Integer> getBattlerolesAsData() { // key为roleid,value是role对应队伍的人数
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> battleroles;
		npcBattleInfoCol _o_ = this;
		battleroles = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.battleroles.entrySet())
			battleroles.put(_e_.getKey(), _e_.getValue());
		return battleroles;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		npcBattleInfoCol _o_ = null;
		if ( _o1_ instanceof npcBattleInfoCol ) _o_ = (npcBattleInfoCol)_o1_;
		else if ( _o1_ instanceof npcBattleInfoCol.Const ) _o_ = ((npcBattleInfoCol.Const)_o1_).nThis();
		else return false;
		if (!battleroles.equals(_o_.battleroles)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += battleroles.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(battleroles);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("battleroles"));
		return lb;
	}

	private class Const implements xbean.npcBattleInfoCol {
		npcBattleInfoCol nThis() {
			return npcBattleInfoCol.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.npcBattleInfoCol copy() {
			return npcBattleInfoCol.this.copy();
		}

		@Override
		public xbean.npcBattleInfoCol toData() {
			return npcBattleInfoCol.this.toData();
		}

		public xbean.npcBattleInfoCol toBean() {
			return npcBattleInfoCol.this.toBean();
		}

		@Override
		public xbean.npcBattleInfoCol toDataIf() {
			return npcBattleInfoCol.this.toDataIf();
		}

		public xbean.npcBattleInfoCol toBeanIf() {
			return npcBattleInfoCol.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Integer> getBattleroles() { // key为roleid,value是role对应队伍的人数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(battleroles);
		}

		@Override
		public java.util.Map<Long, Integer> getBattlerolesAsData() { // key为roleid,value是role对应队伍的人数
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> battleroles;
			npcBattleInfoCol _o_ = npcBattleInfoCol.this;
			battleroles = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.battleroles.entrySet())
				battleroles.put(_e_.getKey(), _e_.getValue());
			return battleroles;
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
			return npcBattleInfoCol.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return npcBattleInfoCol.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return npcBattleInfoCol.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return npcBattleInfoCol.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return npcBattleInfoCol.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return npcBattleInfoCol.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return npcBattleInfoCol.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return npcBattleInfoCol.this.hashCode();
		}

		@Override
		public String toString() {
			return npcBattleInfoCol.this.toString();
		}

	}

	public static final class Data implements xbean.npcBattleInfoCol {
		private java.util.HashMap<Long, Integer> battleroles; // key为roleid,value是role对应队伍的人数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			battleroles = new java.util.HashMap<Long, Integer>();
		}

		Data(xbean.npcBattleInfoCol _o1_) {
			if (_o1_ instanceof npcBattleInfoCol) assign((npcBattleInfoCol)_o1_);
			else if (_o1_ instanceof npcBattleInfoCol.Data) assign((npcBattleInfoCol.Data)_o1_);
			else if (_o1_ instanceof npcBattleInfoCol.Const) assign(((npcBattleInfoCol.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(npcBattleInfoCol _o_) {
			battleroles = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.battleroles.entrySet())
				battleroles.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(npcBattleInfoCol.Data _o_) {
			battleroles = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.battleroles.entrySet())
				battleroles.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(battleroles.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : battleroles.entrySet())
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
					battleroles = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					battleroles.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.npcBattleInfoCol copy() {
			return new Data(this);
		}

		@Override
		public xbean.npcBattleInfoCol toData() {
			return new Data(this);
		}

		public xbean.npcBattleInfoCol toBean() {
			return new npcBattleInfoCol(this, null, null);
		}

		@Override
		public xbean.npcBattleInfoCol toDataIf() {
			return this;
		}

		public xbean.npcBattleInfoCol toBeanIf() {
			return new npcBattleInfoCol(this, null, null);
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
		public java.util.Map<Long, Integer> getBattleroles() { // key为roleid,value是role对应队伍的人数
			return battleroles;
		}

		@Override
		public java.util.Map<Long, Integer> getBattlerolesAsData() { // key为roleid,value是role对应队伍的人数
			return battleroles;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof npcBattleInfoCol.Data)) return false;
			npcBattleInfoCol.Data _o_ = (npcBattleInfoCol.Data) _o1_;
			if (!battleroles.equals(_o_.battleroles)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += battleroles.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(battleroles);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

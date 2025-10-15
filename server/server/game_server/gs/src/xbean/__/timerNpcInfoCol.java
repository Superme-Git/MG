
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class timerNpcInfoCol extends mkdb.XBean implements xbean.timerNpcInfoCol {
	private java.util.HashMap<Long, xbean.timerNpcInfo> npcinfo; // //npckey 对应的Npc状态信息

	@Override
	public void _reset_unsafe_() {
		npcinfo.clear();
	}

	timerNpcInfoCol(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>();
	}

	public timerNpcInfoCol() {
		this(0, null, null);
	}

	public timerNpcInfoCol(timerNpcInfoCol _o_) {
		this(_o_, null, null);
	}

	timerNpcInfoCol(xbean.timerNpcInfoCol _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof timerNpcInfoCol) assign((timerNpcInfoCol)_o1_);
		else if (_o1_ instanceof timerNpcInfoCol.Data) assign((timerNpcInfoCol.Data)_o1_);
		else if (_o1_ instanceof timerNpcInfoCol.Const) assign(((timerNpcInfoCol.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(timerNpcInfoCol _o_) {
		_o_._xdb_verify_unsafe_();
		npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>();
		for (java.util.Map.Entry<Long, xbean.timerNpcInfo> _e_ : _o_.npcinfo.entrySet())
			npcinfo.put(_e_.getKey(), new timerNpcInfo(_e_.getValue(), this, "npcinfo"));
	}

	private void assign(timerNpcInfoCol.Data _o_) {
		npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>();
		for (java.util.Map.Entry<Long, xbean.timerNpcInfo> _e_ : _o_.npcinfo.entrySet())
			npcinfo.put(_e_.getKey(), new timerNpcInfo(_e_.getValue(), this, "npcinfo"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(npcinfo.size());
		for (java.util.Map.Entry<Long, xbean.timerNpcInfo> _e_ : npcinfo.entrySet())
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
				npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.timerNpcInfo _v_ = new timerNpcInfo(0, this, "npcinfo");
				_v_.unmarshal(_os_);
				npcinfo.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.timerNpcInfoCol copy() {
		_xdb_verify_unsafe_();
		return new timerNpcInfoCol(this);
	}

	@Override
	public xbean.timerNpcInfoCol toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.timerNpcInfoCol toBean() {
		_xdb_verify_unsafe_();
		return new timerNpcInfoCol(this); // same as copy()
	}

	@Override
	public xbean.timerNpcInfoCol toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.timerNpcInfoCol toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.timerNpcInfo> getNpcinfo() { // //npckey 对应的Npc状态信息
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "npcinfo"), npcinfo);
	}

	@Override
	public java.util.Map<Long, xbean.timerNpcInfo> getNpcinfoAsData() { // //npckey 对应的Npc状态信息
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.timerNpcInfo> npcinfo;
		timerNpcInfoCol _o_ = this;
		npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>();
		for (java.util.Map.Entry<Long, xbean.timerNpcInfo> _e_ : _o_.npcinfo.entrySet())
			npcinfo.put(_e_.getKey(), new timerNpcInfo.Data(_e_.getValue()));
		return npcinfo;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		timerNpcInfoCol _o_ = null;
		if ( _o1_ instanceof timerNpcInfoCol ) _o_ = (timerNpcInfoCol)_o1_;
		else if ( _o1_ instanceof timerNpcInfoCol.Const ) _o_ = ((timerNpcInfoCol.Const)_o1_).nThis();
		else return false;
		if (!npcinfo.equals(_o_.npcinfo)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += npcinfo.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcinfo);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("npcinfo"));
		return lb;
	}

	private class Const implements xbean.timerNpcInfoCol {
		timerNpcInfoCol nThis() {
			return timerNpcInfoCol.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.timerNpcInfoCol copy() {
			return timerNpcInfoCol.this.copy();
		}

		@Override
		public xbean.timerNpcInfoCol toData() {
			return timerNpcInfoCol.this.toData();
		}

		public xbean.timerNpcInfoCol toBean() {
			return timerNpcInfoCol.this.toBean();
		}

		@Override
		public xbean.timerNpcInfoCol toDataIf() {
			return timerNpcInfoCol.this.toDataIf();
		}

		public xbean.timerNpcInfoCol toBeanIf() {
			return timerNpcInfoCol.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.timerNpcInfo> getNpcinfo() { // //npckey 对应的Npc状态信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(npcinfo);
		}

		@Override
		public java.util.Map<Long, xbean.timerNpcInfo> getNpcinfoAsData() { // //npckey 对应的Npc状态信息
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.timerNpcInfo> npcinfo;
			timerNpcInfoCol _o_ = timerNpcInfoCol.this;
			npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>();
			for (java.util.Map.Entry<Long, xbean.timerNpcInfo> _e_ : _o_.npcinfo.entrySet())
				npcinfo.put(_e_.getKey(), new timerNpcInfo.Data(_e_.getValue()));
			return npcinfo;
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
			return timerNpcInfoCol.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return timerNpcInfoCol.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return timerNpcInfoCol.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return timerNpcInfoCol.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return timerNpcInfoCol.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return timerNpcInfoCol.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return timerNpcInfoCol.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return timerNpcInfoCol.this.hashCode();
		}

		@Override
		public String toString() {
			return timerNpcInfoCol.this.toString();
		}

	}

	public static final class Data implements xbean.timerNpcInfoCol {
		private java.util.HashMap<Long, xbean.timerNpcInfo> npcinfo; // //npckey 对应的Npc状态信息

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>();
		}

		Data(xbean.timerNpcInfoCol _o1_) {
			if (_o1_ instanceof timerNpcInfoCol) assign((timerNpcInfoCol)_o1_);
			else if (_o1_ instanceof timerNpcInfoCol.Data) assign((timerNpcInfoCol.Data)_o1_);
			else if (_o1_ instanceof timerNpcInfoCol.Const) assign(((timerNpcInfoCol.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(timerNpcInfoCol _o_) {
			npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>();
			for (java.util.Map.Entry<Long, xbean.timerNpcInfo> _e_ : _o_.npcinfo.entrySet())
				npcinfo.put(_e_.getKey(), new timerNpcInfo.Data(_e_.getValue()));
		}

		private void assign(timerNpcInfoCol.Data _o_) {
			npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>();
			for (java.util.Map.Entry<Long, xbean.timerNpcInfo> _e_ : _o_.npcinfo.entrySet())
				npcinfo.put(_e_.getKey(), new timerNpcInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(npcinfo.size());
			for (java.util.Map.Entry<Long, xbean.timerNpcInfo> _e_ : npcinfo.entrySet())
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
					npcinfo = new java.util.HashMap<Long, xbean.timerNpcInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.timerNpcInfo _v_ = xbean.Pod.newtimerNpcInfoData();
					_v_.unmarshal(_os_);
					npcinfo.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.timerNpcInfoCol copy() {
			return new Data(this);
		}

		@Override
		public xbean.timerNpcInfoCol toData() {
			return new Data(this);
		}

		public xbean.timerNpcInfoCol toBean() {
			return new timerNpcInfoCol(this, null, null);
		}

		@Override
		public xbean.timerNpcInfoCol toDataIf() {
			return this;
		}

		public xbean.timerNpcInfoCol toBeanIf() {
			return new timerNpcInfoCol(this, null, null);
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
		public java.util.Map<Long, xbean.timerNpcInfo> getNpcinfo() { // //npckey 对应的Npc状态信息
			return npcinfo;
		}

		@Override
		public java.util.Map<Long, xbean.timerNpcInfo> getNpcinfoAsData() { // //npckey 对应的Npc状态信息
			return npcinfo;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof timerNpcInfoCol.Data)) return false;
			timerNpcInfoCol.Data _o_ = (timerNpcInfoCol.Data) _o1_;
			if (!npcinfo.equals(_o_.npcinfo)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += npcinfo.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(npcinfo);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

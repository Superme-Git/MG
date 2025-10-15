
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class eventNpcInfoCol extends mkdb.XBean implements xbean.eventNpcInfoCol {
	private java.util.HashMap<Long, xbean.eventNpcInfo> npcinfo; // //npckey 对应的Npc状态信息

	@Override
	public void _reset_unsafe_() {
		npcinfo.clear();
	}

	eventNpcInfoCol(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>();
	}

	public eventNpcInfoCol() {
		this(0, null, null);
	}

	public eventNpcInfoCol(eventNpcInfoCol _o_) {
		this(_o_, null, null);
	}

	eventNpcInfoCol(xbean.eventNpcInfoCol _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof eventNpcInfoCol) assign((eventNpcInfoCol)_o1_);
		else if (_o1_ instanceof eventNpcInfoCol.Data) assign((eventNpcInfoCol.Data)_o1_);
		else if (_o1_ instanceof eventNpcInfoCol.Const) assign(((eventNpcInfoCol.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(eventNpcInfoCol _o_) {
		_o_._xdb_verify_unsafe_();
		npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>();
		for (java.util.Map.Entry<Long, xbean.eventNpcInfo> _e_ : _o_.npcinfo.entrySet())
			npcinfo.put(_e_.getKey(), new eventNpcInfo(_e_.getValue(), this, "npcinfo"));
	}

	private void assign(eventNpcInfoCol.Data _o_) {
		npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>();
		for (java.util.Map.Entry<Long, xbean.eventNpcInfo> _e_ : _o_.npcinfo.entrySet())
			npcinfo.put(_e_.getKey(), new eventNpcInfo(_e_.getValue(), this, "npcinfo"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(npcinfo.size());
		for (java.util.Map.Entry<Long, xbean.eventNpcInfo> _e_ : npcinfo.entrySet())
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
				npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.eventNpcInfo _v_ = new eventNpcInfo(0, this, "npcinfo");
				_v_.unmarshal(_os_);
				npcinfo.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.eventNpcInfoCol copy() {
		_xdb_verify_unsafe_();
		return new eventNpcInfoCol(this);
	}

	@Override
	public xbean.eventNpcInfoCol toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.eventNpcInfoCol toBean() {
		_xdb_verify_unsafe_();
		return new eventNpcInfoCol(this); // same as copy()
	}

	@Override
	public xbean.eventNpcInfoCol toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.eventNpcInfoCol toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.eventNpcInfo> getNpcinfo() { // //npckey 对应的Npc状态信息
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "npcinfo"), npcinfo);
	}

	@Override
	public java.util.Map<Long, xbean.eventNpcInfo> getNpcinfoAsData() { // //npckey 对应的Npc状态信息
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.eventNpcInfo> npcinfo;
		eventNpcInfoCol _o_ = this;
		npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>();
		for (java.util.Map.Entry<Long, xbean.eventNpcInfo> _e_ : _o_.npcinfo.entrySet())
			npcinfo.put(_e_.getKey(), new eventNpcInfo.Data(_e_.getValue()));
		return npcinfo;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		eventNpcInfoCol _o_ = null;
		if ( _o1_ instanceof eventNpcInfoCol ) _o_ = (eventNpcInfoCol)_o1_;
		else if ( _o1_ instanceof eventNpcInfoCol.Const ) _o_ = ((eventNpcInfoCol.Const)_o1_).nThis();
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

	private class Const implements xbean.eventNpcInfoCol {
		eventNpcInfoCol nThis() {
			return eventNpcInfoCol.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.eventNpcInfoCol copy() {
			return eventNpcInfoCol.this.copy();
		}

		@Override
		public xbean.eventNpcInfoCol toData() {
			return eventNpcInfoCol.this.toData();
		}

		public xbean.eventNpcInfoCol toBean() {
			return eventNpcInfoCol.this.toBean();
		}

		@Override
		public xbean.eventNpcInfoCol toDataIf() {
			return eventNpcInfoCol.this.toDataIf();
		}

		public xbean.eventNpcInfoCol toBeanIf() {
			return eventNpcInfoCol.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.eventNpcInfo> getNpcinfo() { // //npckey 对应的Npc状态信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(npcinfo);
		}

		@Override
		public java.util.Map<Long, xbean.eventNpcInfo> getNpcinfoAsData() { // //npckey 对应的Npc状态信息
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.eventNpcInfo> npcinfo;
			eventNpcInfoCol _o_ = eventNpcInfoCol.this;
			npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>();
			for (java.util.Map.Entry<Long, xbean.eventNpcInfo> _e_ : _o_.npcinfo.entrySet())
				npcinfo.put(_e_.getKey(), new eventNpcInfo.Data(_e_.getValue()));
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
			return eventNpcInfoCol.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return eventNpcInfoCol.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return eventNpcInfoCol.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return eventNpcInfoCol.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return eventNpcInfoCol.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return eventNpcInfoCol.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return eventNpcInfoCol.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return eventNpcInfoCol.this.hashCode();
		}

		@Override
		public String toString() {
			return eventNpcInfoCol.this.toString();
		}

	}

	public static final class Data implements xbean.eventNpcInfoCol {
		private java.util.HashMap<Long, xbean.eventNpcInfo> npcinfo; // //npckey 对应的Npc状态信息

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>();
		}

		Data(xbean.eventNpcInfoCol _o1_) {
			if (_o1_ instanceof eventNpcInfoCol) assign((eventNpcInfoCol)_o1_);
			else if (_o1_ instanceof eventNpcInfoCol.Data) assign((eventNpcInfoCol.Data)_o1_);
			else if (_o1_ instanceof eventNpcInfoCol.Const) assign(((eventNpcInfoCol.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(eventNpcInfoCol _o_) {
			npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>();
			for (java.util.Map.Entry<Long, xbean.eventNpcInfo> _e_ : _o_.npcinfo.entrySet())
				npcinfo.put(_e_.getKey(), new eventNpcInfo.Data(_e_.getValue()));
		}

		private void assign(eventNpcInfoCol.Data _o_) {
			npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>();
			for (java.util.Map.Entry<Long, xbean.eventNpcInfo> _e_ : _o_.npcinfo.entrySet())
				npcinfo.put(_e_.getKey(), new eventNpcInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(npcinfo.size());
			for (java.util.Map.Entry<Long, xbean.eventNpcInfo> _e_ : npcinfo.entrySet())
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
					npcinfo = new java.util.HashMap<Long, xbean.eventNpcInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.eventNpcInfo _v_ = xbean.Pod.neweventNpcInfoData();
					_v_.unmarshal(_os_);
					npcinfo.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.eventNpcInfoCol copy() {
			return new Data(this);
		}

		@Override
		public xbean.eventNpcInfoCol toData() {
			return new Data(this);
		}

		public xbean.eventNpcInfoCol toBean() {
			return new eventNpcInfoCol(this, null, null);
		}

		@Override
		public xbean.eventNpcInfoCol toDataIf() {
			return this;
		}

		public xbean.eventNpcInfoCol toBeanIf() {
			return new eventNpcInfoCol(this, null, null);
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
		public java.util.Map<Long, xbean.eventNpcInfo> getNpcinfo() { // //npckey 对应的Npc状态信息
			return npcinfo;
		}

		@Override
		public java.util.Map<Long, xbean.eventNpcInfo> getNpcinfoAsData() { // //npckey 对应的Npc状态信息
			return npcinfo;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof eventNpcInfoCol.Data)) return false;
			eventNpcInfoCol.Data _o_ = (eventNpcInfoCol.Data) _o1_;
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

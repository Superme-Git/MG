
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleEventNpcInfoColumn extends mkdb.XBean implements xbean.RoleEventNpcInfoColumn {
	private java.util.HashMap<Integer, xbean.RoleEventNpcInfo> eventinfo; // //key是活动id

	@Override
	public void _reset_unsafe_() {
		eventinfo.clear();
	}

	RoleEventNpcInfoColumn(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>();
	}

	public RoleEventNpcInfoColumn() {
		this(0, null, null);
	}

	public RoleEventNpcInfoColumn(RoleEventNpcInfoColumn _o_) {
		this(_o_, null, null);
	}

	RoleEventNpcInfoColumn(xbean.RoleEventNpcInfoColumn _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleEventNpcInfoColumn) assign((RoleEventNpcInfoColumn)_o1_);
		else if (_o1_ instanceof RoleEventNpcInfoColumn.Data) assign((RoleEventNpcInfoColumn.Data)_o1_);
		else if (_o1_ instanceof RoleEventNpcInfoColumn.Const) assign(((RoleEventNpcInfoColumn.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleEventNpcInfoColumn _o_) {
		_o_._xdb_verify_unsafe_();
		eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleEventNpcInfo> _e_ : _o_.eventinfo.entrySet())
			eventinfo.put(_e_.getKey(), new RoleEventNpcInfo(_e_.getValue(), this, "eventinfo"));
	}

	private void assign(RoleEventNpcInfoColumn.Data _o_) {
		eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleEventNpcInfo> _e_ : _o_.eventinfo.entrySet())
			eventinfo.put(_e_.getKey(), new RoleEventNpcInfo(_e_.getValue(), this, "eventinfo"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(eventinfo.size());
		for (java.util.Map.Entry<Integer, xbean.RoleEventNpcInfo> _e_ : eventinfo.entrySet())
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
				eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.RoleEventNpcInfo _v_ = new RoleEventNpcInfo(0, this, "eventinfo");
				_v_.unmarshal(_os_);
				eventinfo.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleEventNpcInfoColumn copy() {
		_xdb_verify_unsafe_();
		return new RoleEventNpcInfoColumn(this);
	}

	@Override
	public xbean.RoleEventNpcInfoColumn toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleEventNpcInfoColumn toBean() {
		_xdb_verify_unsafe_();
		return new RoleEventNpcInfoColumn(this); // same as copy()
	}

	@Override
	public xbean.RoleEventNpcInfoColumn toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleEventNpcInfoColumn toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.RoleEventNpcInfo> getEventinfo() { // //key是活动id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "eventinfo"), eventinfo);
	}

	@Override
	public java.util.Map<Integer, xbean.RoleEventNpcInfo> getEventinfoAsData() { // //key是活动id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.RoleEventNpcInfo> eventinfo;
		RoleEventNpcInfoColumn _o_ = this;
		eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleEventNpcInfo> _e_ : _o_.eventinfo.entrySet())
			eventinfo.put(_e_.getKey(), new RoleEventNpcInfo.Data(_e_.getValue()));
		return eventinfo;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleEventNpcInfoColumn _o_ = null;
		if ( _o1_ instanceof RoleEventNpcInfoColumn ) _o_ = (RoleEventNpcInfoColumn)_o1_;
		else if ( _o1_ instanceof RoleEventNpcInfoColumn.Const ) _o_ = ((RoleEventNpcInfoColumn.Const)_o1_).nThis();
		else return false;
		if (!eventinfo.equals(_o_.eventinfo)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += eventinfo.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(eventinfo);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("eventinfo"));
		return lb;
	}

	private class Const implements xbean.RoleEventNpcInfoColumn {
		RoleEventNpcInfoColumn nThis() {
			return RoleEventNpcInfoColumn.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleEventNpcInfoColumn copy() {
			return RoleEventNpcInfoColumn.this.copy();
		}

		@Override
		public xbean.RoleEventNpcInfoColumn toData() {
			return RoleEventNpcInfoColumn.this.toData();
		}

		public xbean.RoleEventNpcInfoColumn toBean() {
			return RoleEventNpcInfoColumn.this.toBean();
		}

		@Override
		public xbean.RoleEventNpcInfoColumn toDataIf() {
			return RoleEventNpcInfoColumn.this.toDataIf();
		}

		public xbean.RoleEventNpcInfoColumn toBeanIf() {
			return RoleEventNpcInfoColumn.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.RoleEventNpcInfo> getEventinfo() { // //key是活动id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(eventinfo);
		}

		@Override
		public java.util.Map<Integer, xbean.RoleEventNpcInfo> getEventinfoAsData() { // //key是活动id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.RoleEventNpcInfo> eventinfo;
			RoleEventNpcInfoColumn _o_ = RoleEventNpcInfoColumn.this;
			eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleEventNpcInfo> _e_ : _o_.eventinfo.entrySet())
				eventinfo.put(_e_.getKey(), new RoleEventNpcInfo.Data(_e_.getValue()));
			return eventinfo;
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
			return RoleEventNpcInfoColumn.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleEventNpcInfoColumn.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleEventNpcInfoColumn.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleEventNpcInfoColumn.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleEventNpcInfoColumn.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleEventNpcInfoColumn.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleEventNpcInfoColumn.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleEventNpcInfoColumn.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleEventNpcInfoColumn.this.toString();
		}

	}

	public static final class Data implements xbean.RoleEventNpcInfoColumn {
		private java.util.HashMap<Integer, xbean.RoleEventNpcInfo> eventinfo; // //key是活动id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>();
		}

		Data(xbean.RoleEventNpcInfoColumn _o1_) {
			if (_o1_ instanceof RoleEventNpcInfoColumn) assign((RoleEventNpcInfoColumn)_o1_);
			else if (_o1_ instanceof RoleEventNpcInfoColumn.Data) assign((RoleEventNpcInfoColumn.Data)_o1_);
			else if (_o1_ instanceof RoleEventNpcInfoColumn.Const) assign(((RoleEventNpcInfoColumn.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleEventNpcInfoColumn _o_) {
			eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleEventNpcInfo> _e_ : _o_.eventinfo.entrySet())
				eventinfo.put(_e_.getKey(), new RoleEventNpcInfo.Data(_e_.getValue()));
		}

		private void assign(RoleEventNpcInfoColumn.Data _o_) {
			eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleEventNpcInfo> _e_ : _o_.eventinfo.entrySet())
				eventinfo.put(_e_.getKey(), new RoleEventNpcInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(eventinfo.size());
			for (java.util.Map.Entry<Integer, xbean.RoleEventNpcInfo> _e_ : eventinfo.entrySet())
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
					eventinfo = new java.util.HashMap<Integer, xbean.RoleEventNpcInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.RoleEventNpcInfo _v_ = xbean.Pod.newRoleEventNpcInfoData();
					_v_.unmarshal(_os_);
					eventinfo.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleEventNpcInfoColumn copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleEventNpcInfoColumn toData() {
			return new Data(this);
		}

		public xbean.RoleEventNpcInfoColumn toBean() {
			return new RoleEventNpcInfoColumn(this, null, null);
		}

		@Override
		public xbean.RoleEventNpcInfoColumn toDataIf() {
			return this;
		}

		public xbean.RoleEventNpcInfoColumn toBeanIf() {
			return new RoleEventNpcInfoColumn(this, null, null);
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
		public java.util.Map<Integer, xbean.RoleEventNpcInfo> getEventinfo() { // //key是活动id
			return eventinfo;
		}

		@Override
		public java.util.Map<Integer, xbean.RoleEventNpcInfo> getEventinfoAsData() { // //key是活动id
			return eventinfo;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleEventNpcInfoColumn.Data)) return false;
			RoleEventNpcInfoColumn.Data _o_ = (RoleEventNpcInfoColumn.Data) _o1_;
			if (!eventinfo.equals(_o_.eventinfo)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += eventinfo.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(eventinfo);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RolePingBiRolesInfo extends mkdb.XBean implements xbean.RolePingBiRolesInfo {
	private java.util.HashMap<Long, Long> pingbiroles; // //存储黑名单角色id

	@Override
	public void _reset_unsafe_() {
		pingbiroles.clear();
	}

	RolePingBiRolesInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		pingbiroles = new java.util.HashMap<Long, Long>();
	}

	public RolePingBiRolesInfo() {
		this(0, null, null);
	}

	public RolePingBiRolesInfo(RolePingBiRolesInfo _o_) {
		this(_o_, null, null);
	}

	RolePingBiRolesInfo(xbean.RolePingBiRolesInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RolePingBiRolesInfo) assign((RolePingBiRolesInfo)_o1_);
		else if (_o1_ instanceof RolePingBiRolesInfo.Data) assign((RolePingBiRolesInfo.Data)_o1_);
		else if (_o1_ instanceof RolePingBiRolesInfo.Const) assign(((RolePingBiRolesInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RolePingBiRolesInfo _o_) {
		_o_._xdb_verify_unsafe_();
		pingbiroles = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.pingbiroles.entrySet())
			pingbiroles.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(RolePingBiRolesInfo.Data _o_) {
		pingbiroles = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.pingbiroles.entrySet())
			pingbiroles.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(pingbiroles.size());
		for (java.util.Map.Entry<Long, Long> _e_ : pingbiroles.entrySet())
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
				pingbiroles = new java.util.HashMap<Long, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				pingbiroles.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RolePingBiRolesInfo copy() {
		_xdb_verify_unsafe_();
		return new RolePingBiRolesInfo(this);
	}

	@Override
	public xbean.RolePingBiRolesInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RolePingBiRolesInfo toBean() {
		_xdb_verify_unsafe_();
		return new RolePingBiRolesInfo(this); // same as copy()
	}

	@Override
	public xbean.RolePingBiRolesInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RolePingBiRolesInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Long> getPingbiroles() { // //存储黑名单角色id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "pingbiroles"), pingbiroles);
	}

	@Override
	public java.util.Map<Long, Long> getPingbirolesAsData() { // //存储黑名单角色id
		_xdb_verify_unsafe_();
		java.util.Map<Long, Long> pingbiroles;
		RolePingBiRolesInfo _o_ = this;
		pingbiroles = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.pingbiroles.entrySet())
			pingbiroles.put(_e_.getKey(), _e_.getValue());
		return pingbiroles;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RolePingBiRolesInfo _o_ = null;
		if ( _o1_ instanceof RolePingBiRolesInfo ) _o_ = (RolePingBiRolesInfo)_o1_;
		else if ( _o1_ instanceof RolePingBiRolesInfo.Const ) _o_ = ((RolePingBiRolesInfo.Const)_o1_).nThis();
		else return false;
		if (!pingbiroles.equals(_o_.pingbiroles)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += pingbiroles.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pingbiroles);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("pingbiroles"));
		return lb;
	}

	private class Const implements xbean.RolePingBiRolesInfo {
		RolePingBiRolesInfo nThis() {
			return RolePingBiRolesInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RolePingBiRolesInfo copy() {
			return RolePingBiRolesInfo.this.copy();
		}

		@Override
		public xbean.RolePingBiRolesInfo toData() {
			return RolePingBiRolesInfo.this.toData();
		}

		public xbean.RolePingBiRolesInfo toBean() {
			return RolePingBiRolesInfo.this.toBean();
		}

		@Override
		public xbean.RolePingBiRolesInfo toDataIf() {
			return RolePingBiRolesInfo.this.toDataIf();
		}

		public xbean.RolePingBiRolesInfo toBeanIf() {
			return RolePingBiRolesInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Long> getPingbiroles() { // //存储黑名单角色id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(pingbiroles);
		}

		@Override
		public java.util.Map<Long, Long> getPingbirolesAsData() { // //存储黑名单角色id
			_xdb_verify_unsafe_();
			java.util.Map<Long, Long> pingbiroles;
			RolePingBiRolesInfo _o_ = RolePingBiRolesInfo.this;
			pingbiroles = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.pingbiroles.entrySet())
				pingbiroles.put(_e_.getKey(), _e_.getValue());
			return pingbiroles;
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
			return RolePingBiRolesInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RolePingBiRolesInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RolePingBiRolesInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RolePingBiRolesInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RolePingBiRolesInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RolePingBiRolesInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RolePingBiRolesInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RolePingBiRolesInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RolePingBiRolesInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RolePingBiRolesInfo {
		private java.util.HashMap<Long, Long> pingbiroles; // //存储黑名单角色id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			pingbiroles = new java.util.HashMap<Long, Long>();
		}

		Data(xbean.RolePingBiRolesInfo _o1_) {
			if (_o1_ instanceof RolePingBiRolesInfo) assign((RolePingBiRolesInfo)_o1_);
			else if (_o1_ instanceof RolePingBiRolesInfo.Data) assign((RolePingBiRolesInfo.Data)_o1_);
			else if (_o1_ instanceof RolePingBiRolesInfo.Const) assign(((RolePingBiRolesInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RolePingBiRolesInfo _o_) {
			pingbiroles = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.pingbiroles.entrySet())
				pingbiroles.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(RolePingBiRolesInfo.Data _o_) {
			pingbiroles = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.pingbiroles.entrySet())
				pingbiroles.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(pingbiroles.size());
			for (java.util.Map.Entry<Long, Long> _e_ : pingbiroles.entrySet())
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
					pingbiroles = new java.util.HashMap<Long, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					pingbiroles.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RolePingBiRolesInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RolePingBiRolesInfo toData() {
			return new Data(this);
		}

		public xbean.RolePingBiRolesInfo toBean() {
			return new RolePingBiRolesInfo(this, null, null);
		}

		@Override
		public xbean.RolePingBiRolesInfo toDataIf() {
			return this;
		}

		public xbean.RolePingBiRolesInfo toBeanIf() {
			return new RolePingBiRolesInfo(this, null, null);
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
		public java.util.Map<Long, Long> getPingbiroles() { // //存储黑名单角色id
			return pingbiroles;
		}

		@Override
		public java.util.Map<Long, Long> getPingbirolesAsData() { // //存储黑名单角色id
			return pingbiroles;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RolePingBiRolesInfo.Data)) return false;
			RolePingBiRolesInfo.Data _o_ = (RolePingBiRolesInfo.Data) _o1_;
			if (!pingbiroles.equals(_o_.pingbiroles)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += pingbiroles.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(pingbiroles);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

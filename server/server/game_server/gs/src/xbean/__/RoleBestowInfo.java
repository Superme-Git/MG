
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleBestowInfo extends mkdb.XBean implements xbean.RoleBestowInfo {
	private java.util.HashMap<Long, xbean.RoleBestowCount> rolebestowinfo; // 每个宝箱的开启次数

	@Override
	public void _reset_unsafe_() {
		rolebestowinfo.clear();
	}

	RoleBestowInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>();
	}

	public RoleBestowInfo() {
		this(0, null, null);
	}

	public RoleBestowInfo(RoleBestowInfo _o_) {
		this(_o_, null, null);
	}

	RoleBestowInfo(xbean.RoleBestowInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleBestowInfo) assign((RoleBestowInfo)_o1_);
		else if (_o1_ instanceof RoleBestowInfo.Data) assign((RoleBestowInfo.Data)_o1_);
		else if (_o1_ instanceof RoleBestowInfo.Const) assign(((RoleBestowInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleBestowInfo _o_) {
		_o_._xdb_verify_unsafe_();
		rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>();
		for (java.util.Map.Entry<Long, xbean.RoleBestowCount> _e_ : _o_.rolebestowinfo.entrySet())
			rolebestowinfo.put(_e_.getKey(), new RoleBestowCount(_e_.getValue(), this, "rolebestowinfo"));
	}

	private void assign(RoleBestowInfo.Data _o_) {
		rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>();
		for (java.util.Map.Entry<Long, xbean.RoleBestowCount> _e_ : _o_.rolebestowinfo.entrySet())
			rolebestowinfo.put(_e_.getKey(), new RoleBestowCount(_e_.getValue(), this, "rolebestowinfo"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(rolebestowinfo.size());
		for (java.util.Map.Entry<Long, xbean.RoleBestowCount> _e_ : rolebestowinfo.entrySet())
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
				rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.RoleBestowCount _v_ = new RoleBestowCount(0, this, "rolebestowinfo");
				_v_.unmarshal(_os_);
				rolebestowinfo.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleBestowInfo copy() {
		_xdb_verify_unsafe_();
		return new RoleBestowInfo(this);
	}

	@Override
	public xbean.RoleBestowInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleBestowInfo toBean() {
		_xdb_verify_unsafe_();
		return new RoleBestowInfo(this); // same as copy()
	}

	@Override
	public xbean.RoleBestowInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleBestowInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.RoleBestowCount> getRolebestowinfo() { // 每个宝箱的开启次数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "rolebestowinfo"), rolebestowinfo);
	}

	@Override
	public java.util.Map<Long, xbean.RoleBestowCount> getRolebestowinfoAsData() { // 每个宝箱的开启次数
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.RoleBestowCount> rolebestowinfo;
		RoleBestowInfo _o_ = this;
		rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>();
		for (java.util.Map.Entry<Long, xbean.RoleBestowCount> _e_ : _o_.rolebestowinfo.entrySet())
			rolebestowinfo.put(_e_.getKey(), new RoleBestowCount.Data(_e_.getValue()));
		return rolebestowinfo;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleBestowInfo _o_ = null;
		if ( _o1_ instanceof RoleBestowInfo ) _o_ = (RoleBestowInfo)_o1_;
		else if ( _o1_ instanceof RoleBestowInfo.Const ) _o_ = ((RoleBestowInfo.Const)_o1_).nThis();
		else return false;
		if (!rolebestowinfo.equals(_o_.rolebestowinfo)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rolebestowinfo.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolebestowinfo);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("rolebestowinfo"));
		return lb;
	}

	private class Const implements xbean.RoleBestowInfo {
		RoleBestowInfo nThis() {
			return RoleBestowInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleBestowInfo copy() {
			return RoleBestowInfo.this.copy();
		}

		@Override
		public xbean.RoleBestowInfo toData() {
			return RoleBestowInfo.this.toData();
		}

		public xbean.RoleBestowInfo toBean() {
			return RoleBestowInfo.this.toBean();
		}

		@Override
		public xbean.RoleBestowInfo toDataIf() {
			return RoleBestowInfo.this.toDataIf();
		}

		public xbean.RoleBestowInfo toBeanIf() {
			return RoleBestowInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.RoleBestowCount> getRolebestowinfo() { // 每个宝箱的开启次数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(rolebestowinfo);
		}

		@Override
		public java.util.Map<Long, xbean.RoleBestowCount> getRolebestowinfoAsData() { // 每个宝箱的开启次数
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.RoleBestowCount> rolebestowinfo;
			RoleBestowInfo _o_ = RoleBestowInfo.this;
			rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>();
			for (java.util.Map.Entry<Long, xbean.RoleBestowCount> _e_ : _o_.rolebestowinfo.entrySet())
				rolebestowinfo.put(_e_.getKey(), new RoleBestowCount.Data(_e_.getValue()));
			return rolebestowinfo;
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
			return RoleBestowInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleBestowInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleBestowInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleBestowInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleBestowInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleBestowInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleBestowInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleBestowInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleBestowInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RoleBestowInfo {
		private java.util.HashMap<Long, xbean.RoleBestowCount> rolebestowinfo; // 每个宝箱的开启次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>();
		}

		Data(xbean.RoleBestowInfo _o1_) {
			if (_o1_ instanceof RoleBestowInfo) assign((RoleBestowInfo)_o1_);
			else if (_o1_ instanceof RoleBestowInfo.Data) assign((RoleBestowInfo.Data)_o1_);
			else if (_o1_ instanceof RoleBestowInfo.Const) assign(((RoleBestowInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleBestowInfo _o_) {
			rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>();
			for (java.util.Map.Entry<Long, xbean.RoleBestowCount> _e_ : _o_.rolebestowinfo.entrySet())
				rolebestowinfo.put(_e_.getKey(), new RoleBestowCount.Data(_e_.getValue()));
		}

		private void assign(RoleBestowInfo.Data _o_) {
			rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>();
			for (java.util.Map.Entry<Long, xbean.RoleBestowCount> _e_ : _o_.rolebestowinfo.entrySet())
				rolebestowinfo.put(_e_.getKey(), new RoleBestowCount.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(rolebestowinfo.size());
			for (java.util.Map.Entry<Long, xbean.RoleBestowCount> _e_ : rolebestowinfo.entrySet())
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
					rolebestowinfo = new java.util.HashMap<Long, xbean.RoleBestowCount>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.RoleBestowCount _v_ = xbean.Pod.newRoleBestowCountData();
					_v_.unmarshal(_os_);
					rolebestowinfo.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleBestowInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleBestowInfo toData() {
			return new Data(this);
		}

		public xbean.RoleBestowInfo toBean() {
			return new RoleBestowInfo(this, null, null);
		}

		@Override
		public xbean.RoleBestowInfo toDataIf() {
			return this;
		}

		public xbean.RoleBestowInfo toBeanIf() {
			return new RoleBestowInfo(this, null, null);
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
		public java.util.Map<Long, xbean.RoleBestowCount> getRolebestowinfo() { // 每个宝箱的开启次数
			return rolebestowinfo;
		}

		@Override
		public java.util.Map<Long, xbean.RoleBestowCount> getRolebestowinfoAsData() { // 每个宝箱的开启次数
			return rolebestowinfo;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleBestowInfo.Data)) return false;
			RoleBestowInfo.Data _o_ = (RoleBestowInfo.Data) _o1_;
			if (!rolebestowinfo.equals(_o_.rolebestowinfo)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rolebestowinfo.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rolebestowinfo);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

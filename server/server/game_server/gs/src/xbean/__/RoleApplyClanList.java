
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleApplyClanList extends mkdb.XBean implements xbean.RoleApplyClanList {
	private long onekeylasttime; // 上次一键申请时间
	private java.util.HashMap<Long, xbean.RoleApplyClan> onekeyapplymap; // 
	private java.util.HashMap<Long, xbean.RoleApplyClan> applymap; // 

	@Override
	public void _reset_unsafe_() {
		onekeylasttime = 0L;
		onekeyapplymap.clear();
		applymap.clear();
	}

	RoleApplyClanList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
		applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
	}

	public RoleApplyClanList() {
		this(0, null, null);
	}

	public RoleApplyClanList(RoleApplyClanList _o_) {
		this(_o_, null, null);
	}

	RoleApplyClanList(xbean.RoleApplyClanList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleApplyClanList) assign((RoleApplyClanList)_o1_);
		else if (_o1_ instanceof RoleApplyClanList.Data) assign((RoleApplyClanList.Data)_o1_);
		else if (_o1_ instanceof RoleApplyClanList.Const) assign(((RoleApplyClanList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleApplyClanList _o_) {
		_o_._xdb_verify_unsafe_();
		onekeylasttime = _o_.onekeylasttime;
		onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
		for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.onekeyapplymap.entrySet())
			onekeyapplymap.put(_e_.getKey(), new RoleApplyClan(_e_.getValue(), this, "onekeyapplymap"));
		applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
		for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.applymap.entrySet())
			applymap.put(_e_.getKey(), new RoleApplyClan(_e_.getValue(), this, "applymap"));
	}

	private void assign(RoleApplyClanList.Data _o_) {
		onekeylasttime = _o_.onekeylasttime;
		onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
		for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.onekeyapplymap.entrySet())
			onekeyapplymap.put(_e_.getKey(), new RoleApplyClan(_e_.getValue(), this, "onekeyapplymap"));
		applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
		for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.applymap.entrySet())
			applymap.put(_e_.getKey(), new RoleApplyClan(_e_.getValue(), this, "applymap"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(onekeylasttime);
		_os_.compact_uint32(onekeyapplymap.size());
		for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : onekeyapplymap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(applymap.size());
		for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : applymap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		onekeylasttime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.RoleApplyClan _v_ = new RoleApplyClan(0, this, "onekeyapplymap");
				_v_.unmarshal(_os_);
				onekeyapplymap.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.RoleApplyClan _v_ = new RoleApplyClan(0, this, "applymap");
				_v_.unmarshal(_os_);
				applymap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleApplyClanList copy() {
		_xdb_verify_unsafe_();
		return new RoleApplyClanList(this);
	}

	@Override
	public xbean.RoleApplyClanList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleApplyClanList toBean() {
		_xdb_verify_unsafe_();
		return new RoleApplyClanList(this); // same as copy()
	}

	@Override
	public xbean.RoleApplyClanList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleApplyClanList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getOnekeylasttime() { // 上次一键申请时间
		_xdb_verify_unsafe_();
		return onekeylasttime;
	}

	@Override
	public java.util.Map<Long, xbean.RoleApplyClan> getOnekeyapplymap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "onekeyapplymap"), onekeyapplymap);
	}

	@Override
	public java.util.Map<Long, xbean.RoleApplyClan> getOnekeyapplymapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.RoleApplyClan> onekeyapplymap;
		RoleApplyClanList _o_ = this;
		onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
		for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.onekeyapplymap.entrySet())
			onekeyapplymap.put(_e_.getKey(), new RoleApplyClan.Data(_e_.getValue()));
		return onekeyapplymap;
	}

	@Override
	public java.util.Map<Long, xbean.RoleApplyClan> getApplymap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "applymap"), applymap);
	}

	@Override
	public java.util.Map<Long, xbean.RoleApplyClan> getApplymapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.RoleApplyClan> applymap;
		RoleApplyClanList _o_ = this;
		applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
		for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.applymap.entrySet())
			applymap.put(_e_.getKey(), new RoleApplyClan.Data(_e_.getValue()));
		return applymap;
	}

	@Override
	public void setOnekeylasttime(long _v_) { // 上次一键申请时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "onekeylasttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, onekeylasttime) {
					public void rollback() { onekeylasttime = _xdb_saved; }
				};}});
		onekeylasttime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleApplyClanList _o_ = null;
		if ( _o1_ instanceof RoleApplyClanList ) _o_ = (RoleApplyClanList)_o1_;
		else if ( _o1_ instanceof RoleApplyClanList.Const ) _o_ = ((RoleApplyClanList.Const)_o1_).nThis();
		else return false;
		if (onekeylasttime != _o_.onekeylasttime) return false;
		if (!onekeyapplymap.equals(_o_.onekeyapplymap)) return false;
		if (!applymap.equals(_o_.applymap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += onekeylasttime;
		_h_ += onekeyapplymap.hashCode();
		_h_ += applymap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(onekeylasttime);
		_sb_.append(",");
		_sb_.append(onekeyapplymap);
		_sb_.append(",");
		_sb_.append(applymap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("onekeylasttime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("onekeyapplymap"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("applymap"));
		return lb;
	}

	private class Const implements xbean.RoleApplyClanList {
		RoleApplyClanList nThis() {
			return RoleApplyClanList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleApplyClanList copy() {
			return RoleApplyClanList.this.copy();
		}

		@Override
		public xbean.RoleApplyClanList toData() {
			return RoleApplyClanList.this.toData();
		}

		public xbean.RoleApplyClanList toBean() {
			return RoleApplyClanList.this.toBean();
		}

		@Override
		public xbean.RoleApplyClanList toDataIf() {
			return RoleApplyClanList.this.toDataIf();
		}

		public xbean.RoleApplyClanList toBeanIf() {
			return RoleApplyClanList.this.toBeanIf();
		}

		@Override
		public long getOnekeylasttime() { // 上次一键申请时间
			_xdb_verify_unsafe_();
			return onekeylasttime;
		}

		@Override
		public java.util.Map<Long, xbean.RoleApplyClan> getOnekeyapplymap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(onekeyapplymap);
		}

		@Override
		public java.util.Map<Long, xbean.RoleApplyClan> getOnekeyapplymapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.RoleApplyClan> onekeyapplymap;
			RoleApplyClanList _o_ = RoleApplyClanList.this;
			onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
			for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.onekeyapplymap.entrySet())
				onekeyapplymap.put(_e_.getKey(), new RoleApplyClan.Data(_e_.getValue()));
			return onekeyapplymap;
		}

		@Override
		public java.util.Map<Long, xbean.RoleApplyClan> getApplymap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(applymap);
		}

		@Override
		public java.util.Map<Long, xbean.RoleApplyClan> getApplymapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.RoleApplyClan> applymap;
			RoleApplyClanList _o_ = RoleApplyClanList.this;
			applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
			for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.applymap.entrySet())
				applymap.put(_e_.getKey(), new RoleApplyClan.Data(_e_.getValue()));
			return applymap;
		}

		@Override
		public void setOnekeylasttime(long _v_) { // 上次一键申请时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
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
			return RoleApplyClanList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleApplyClanList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleApplyClanList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleApplyClanList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleApplyClanList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleApplyClanList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleApplyClanList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleApplyClanList.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleApplyClanList.this.toString();
		}

	}

	public static final class Data implements xbean.RoleApplyClanList {
		private long onekeylasttime; // 上次一键申请时间
		private java.util.HashMap<Long, xbean.RoleApplyClan> onekeyapplymap; // 
		private java.util.HashMap<Long, xbean.RoleApplyClan> applymap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
			applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
		}

		Data(xbean.RoleApplyClanList _o1_) {
			if (_o1_ instanceof RoleApplyClanList) assign((RoleApplyClanList)_o1_);
			else if (_o1_ instanceof RoleApplyClanList.Data) assign((RoleApplyClanList.Data)_o1_);
			else if (_o1_ instanceof RoleApplyClanList.Const) assign(((RoleApplyClanList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleApplyClanList _o_) {
			onekeylasttime = _o_.onekeylasttime;
			onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
			for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.onekeyapplymap.entrySet())
				onekeyapplymap.put(_e_.getKey(), new RoleApplyClan.Data(_e_.getValue()));
			applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
			for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.applymap.entrySet())
				applymap.put(_e_.getKey(), new RoleApplyClan.Data(_e_.getValue()));
		}

		private void assign(RoleApplyClanList.Data _o_) {
			onekeylasttime = _o_.onekeylasttime;
			onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
			for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.onekeyapplymap.entrySet())
				onekeyapplymap.put(_e_.getKey(), new RoleApplyClan.Data(_e_.getValue()));
			applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>();
			for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : _o_.applymap.entrySet())
				applymap.put(_e_.getKey(), new RoleApplyClan.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(onekeylasttime);
			_os_.compact_uint32(onekeyapplymap.size());
			for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : onekeyapplymap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(applymap.size());
			for (java.util.Map.Entry<Long, xbean.RoleApplyClan> _e_ : applymap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			onekeylasttime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					onekeyapplymap = new java.util.HashMap<Long, xbean.RoleApplyClan>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.RoleApplyClan _v_ = xbean.Pod.newRoleApplyClanData();
					_v_.unmarshal(_os_);
					onekeyapplymap.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					applymap = new java.util.HashMap<Long, xbean.RoleApplyClan>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.RoleApplyClan _v_ = xbean.Pod.newRoleApplyClanData();
					_v_.unmarshal(_os_);
					applymap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleApplyClanList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleApplyClanList toData() {
			return new Data(this);
		}

		public xbean.RoleApplyClanList toBean() {
			return new RoleApplyClanList(this, null, null);
		}

		@Override
		public xbean.RoleApplyClanList toDataIf() {
			return this;
		}

		public xbean.RoleApplyClanList toBeanIf() {
			return new RoleApplyClanList(this, null, null);
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
		public long getOnekeylasttime() { // 上次一键申请时间
			return onekeylasttime;
		}

		@Override
		public java.util.Map<Long, xbean.RoleApplyClan> getOnekeyapplymap() { // 
			return onekeyapplymap;
		}

		@Override
		public java.util.Map<Long, xbean.RoleApplyClan> getOnekeyapplymapAsData() { // 
			return onekeyapplymap;
		}

		@Override
		public java.util.Map<Long, xbean.RoleApplyClan> getApplymap() { // 
			return applymap;
		}

		@Override
		public java.util.Map<Long, xbean.RoleApplyClan> getApplymapAsData() { // 
			return applymap;
		}

		@Override
		public void setOnekeylasttime(long _v_) { // 上次一键申请时间
			onekeylasttime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleApplyClanList.Data)) return false;
			RoleApplyClanList.Data _o_ = (RoleApplyClanList.Data) _o1_;
			if (onekeylasttime != _o_.onekeylasttime) return false;
			if (!onekeyapplymap.equals(_o_.onekeyapplymap)) return false;
			if (!applymap.equals(_o_.applymap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += onekeylasttime;
			_h_ += onekeyapplymap.hashCode();
			_h_ += applymap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(onekeylasttime);
			_sb_.append(",");
			_sb_.append(onekeyapplymap);
			_sb_.append(",");
			_sb_.append(applymap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

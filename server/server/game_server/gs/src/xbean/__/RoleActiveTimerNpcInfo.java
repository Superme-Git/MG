
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleActiveTimerNpcInfo extends mkdb.XBean implements xbean.RoleActiveTimerNpcInfo {
	private java.util.HashMap<Integer, xbean.RoleTimerNpcInfo> actinfo; // //key是活动id

	@Override
	public void _reset_unsafe_() {
		actinfo.clear();
	}

	RoleActiveTimerNpcInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>();
	}

	public RoleActiveTimerNpcInfo() {
		this(0, null, null);
	}

	public RoleActiveTimerNpcInfo(RoleActiveTimerNpcInfo _o_) {
		this(_o_, null, null);
	}

	RoleActiveTimerNpcInfo(xbean.RoleActiveTimerNpcInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleActiveTimerNpcInfo) assign((RoleActiveTimerNpcInfo)_o1_);
		else if (_o1_ instanceof RoleActiveTimerNpcInfo.Data) assign((RoleActiveTimerNpcInfo.Data)_o1_);
		else if (_o1_ instanceof RoleActiveTimerNpcInfo.Const) assign(((RoleActiveTimerNpcInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleActiveTimerNpcInfo _o_) {
		_o_._xdb_verify_unsafe_();
		actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleTimerNpcInfo> _e_ : _o_.actinfo.entrySet())
			actinfo.put(_e_.getKey(), new RoleTimerNpcInfo(_e_.getValue(), this, "actinfo"));
	}

	private void assign(RoleActiveTimerNpcInfo.Data _o_) {
		actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleTimerNpcInfo> _e_ : _o_.actinfo.entrySet())
			actinfo.put(_e_.getKey(), new RoleTimerNpcInfo(_e_.getValue(), this, "actinfo"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(actinfo.size());
		for (java.util.Map.Entry<Integer, xbean.RoleTimerNpcInfo> _e_ : actinfo.entrySet())
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
				actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.RoleTimerNpcInfo _v_ = new RoleTimerNpcInfo(0, this, "actinfo");
				_v_.unmarshal(_os_);
				actinfo.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleActiveTimerNpcInfo copy() {
		_xdb_verify_unsafe_();
		return new RoleActiveTimerNpcInfo(this);
	}

	@Override
	public xbean.RoleActiveTimerNpcInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleActiveTimerNpcInfo toBean() {
		_xdb_verify_unsafe_();
		return new RoleActiveTimerNpcInfo(this); // same as copy()
	}

	@Override
	public xbean.RoleActiveTimerNpcInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleActiveTimerNpcInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.RoleTimerNpcInfo> getActinfo() { // //key是活动id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "actinfo"), actinfo);
	}

	@Override
	public java.util.Map<Integer, xbean.RoleTimerNpcInfo> getActinfoAsData() { // //key是活动id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.RoleTimerNpcInfo> actinfo;
		RoleActiveTimerNpcInfo _o_ = this;
		actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>();
		for (java.util.Map.Entry<Integer, xbean.RoleTimerNpcInfo> _e_ : _o_.actinfo.entrySet())
			actinfo.put(_e_.getKey(), new RoleTimerNpcInfo.Data(_e_.getValue()));
		return actinfo;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleActiveTimerNpcInfo _o_ = null;
		if ( _o1_ instanceof RoleActiveTimerNpcInfo ) _o_ = (RoleActiveTimerNpcInfo)_o1_;
		else if ( _o1_ instanceof RoleActiveTimerNpcInfo.Const ) _o_ = ((RoleActiveTimerNpcInfo.Const)_o1_).nThis();
		else return false;
		if (!actinfo.equals(_o_.actinfo)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += actinfo.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(actinfo);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("actinfo"));
		return lb;
	}

	private class Const implements xbean.RoleActiveTimerNpcInfo {
		RoleActiveTimerNpcInfo nThis() {
			return RoleActiveTimerNpcInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleActiveTimerNpcInfo copy() {
			return RoleActiveTimerNpcInfo.this.copy();
		}

		@Override
		public xbean.RoleActiveTimerNpcInfo toData() {
			return RoleActiveTimerNpcInfo.this.toData();
		}

		public xbean.RoleActiveTimerNpcInfo toBean() {
			return RoleActiveTimerNpcInfo.this.toBean();
		}

		@Override
		public xbean.RoleActiveTimerNpcInfo toDataIf() {
			return RoleActiveTimerNpcInfo.this.toDataIf();
		}

		public xbean.RoleActiveTimerNpcInfo toBeanIf() {
			return RoleActiveTimerNpcInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.RoleTimerNpcInfo> getActinfo() { // //key是活动id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(actinfo);
		}

		@Override
		public java.util.Map<Integer, xbean.RoleTimerNpcInfo> getActinfoAsData() { // //key是活动id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.RoleTimerNpcInfo> actinfo;
			RoleActiveTimerNpcInfo _o_ = RoleActiveTimerNpcInfo.this;
			actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleTimerNpcInfo> _e_ : _o_.actinfo.entrySet())
				actinfo.put(_e_.getKey(), new RoleTimerNpcInfo.Data(_e_.getValue()));
			return actinfo;
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
			return RoleActiveTimerNpcInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleActiveTimerNpcInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleActiveTimerNpcInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleActiveTimerNpcInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleActiveTimerNpcInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleActiveTimerNpcInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleActiveTimerNpcInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleActiveTimerNpcInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleActiveTimerNpcInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RoleActiveTimerNpcInfo {
		private java.util.HashMap<Integer, xbean.RoleTimerNpcInfo> actinfo; // //key是活动id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>();
		}

		Data(xbean.RoleActiveTimerNpcInfo _o1_) {
			if (_o1_ instanceof RoleActiveTimerNpcInfo) assign((RoleActiveTimerNpcInfo)_o1_);
			else if (_o1_ instanceof RoleActiveTimerNpcInfo.Data) assign((RoleActiveTimerNpcInfo.Data)_o1_);
			else if (_o1_ instanceof RoleActiveTimerNpcInfo.Const) assign(((RoleActiveTimerNpcInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleActiveTimerNpcInfo _o_) {
			actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleTimerNpcInfo> _e_ : _o_.actinfo.entrySet())
				actinfo.put(_e_.getKey(), new RoleTimerNpcInfo.Data(_e_.getValue()));
		}

		private void assign(RoleActiveTimerNpcInfo.Data _o_) {
			actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>();
			for (java.util.Map.Entry<Integer, xbean.RoleTimerNpcInfo> _e_ : _o_.actinfo.entrySet())
				actinfo.put(_e_.getKey(), new RoleTimerNpcInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(actinfo.size());
			for (java.util.Map.Entry<Integer, xbean.RoleTimerNpcInfo> _e_ : actinfo.entrySet())
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
					actinfo = new java.util.HashMap<Integer, xbean.RoleTimerNpcInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.RoleTimerNpcInfo _v_ = xbean.Pod.newRoleTimerNpcInfoData();
					_v_.unmarshal(_os_);
					actinfo.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleActiveTimerNpcInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleActiveTimerNpcInfo toData() {
			return new Data(this);
		}

		public xbean.RoleActiveTimerNpcInfo toBean() {
			return new RoleActiveTimerNpcInfo(this, null, null);
		}

		@Override
		public xbean.RoleActiveTimerNpcInfo toDataIf() {
			return this;
		}

		public xbean.RoleActiveTimerNpcInfo toBeanIf() {
			return new RoleActiveTimerNpcInfo(this, null, null);
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
		public java.util.Map<Integer, xbean.RoleTimerNpcInfo> getActinfo() { // //key是活动id
			return actinfo;
		}

		@Override
		public java.util.Map<Integer, xbean.RoleTimerNpcInfo> getActinfoAsData() { // //key是活动id
			return actinfo;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleActiveTimerNpcInfo.Data)) return false;
			RoleActiveTimerNpcInfo.Data _o_ = (RoleActiveTimerNpcInfo.Data) _o1_;
			if (!actinfo.equals(_o_.actinfo)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += actinfo.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(actinfo);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

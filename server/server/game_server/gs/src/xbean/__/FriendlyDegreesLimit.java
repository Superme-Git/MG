
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FriendlyDegreesLimit extends mkdb.XBean implements xbean.FriendlyDegreesLimit {
	private java.util.HashMap<Long, Integer> todayfriendlydegreesmap; // key=roleid value=今日增加的好友度
	private long resettime; // 每日增加的好友度重置时间

	@Override
	public void _reset_unsafe_() {
		todayfriendlydegreesmap.clear();
		resettime = 0L;
	}

	FriendlyDegreesLimit(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>();
	}

	public FriendlyDegreesLimit() {
		this(0, null, null);
	}

	public FriendlyDegreesLimit(FriendlyDegreesLimit _o_) {
		this(_o_, null, null);
	}

	FriendlyDegreesLimit(xbean.FriendlyDegreesLimit _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FriendlyDegreesLimit) assign((FriendlyDegreesLimit)_o1_);
		else if (_o1_ instanceof FriendlyDegreesLimit.Data) assign((FriendlyDegreesLimit.Data)_o1_);
		else if (_o1_ instanceof FriendlyDegreesLimit.Const) assign(((FriendlyDegreesLimit.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FriendlyDegreesLimit _o_) {
		_o_._xdb_verify_unsafe_();
		todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.todayfriendlydegreesmap.entrySet())
			todayfriendlydegreesmap.put(_e_.getKey(), _e_.getValue());
		resettime = _o_.resettime;
	}

	private void assign(FriendlyDegreesLimit.Data _o_) {
		todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.todayfriendlydegreesmap.entrySet())
			todayfriendlydegreesmap.put(_e_.getKey(), _e_.getValue());
		resettime = _o_.resettime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(todayfriendlydegreesmap.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : todayfriendlydegreesmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(resettime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				todayfriendlydegreesmap.put(_k_, _v_);
			}
		}
		resettime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.FriendlyDegreesLimit copy() {
		_xdb_verify_unsafe_();
		return new FriendlyDegreesLimit(this);
	}

	@Override
	public xbean.FriendlyDegreesLimit toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FriendlyDegreesLimit toBean() {
		_xdb_verify_unsafe_();
		return new FriendlyDegreesLimit(this); // same as copy()
	}

	@Override
	public xbean.FriendlyDegreesLimit toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FriendlyDegreesLimit toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Integer> getTodayfriendlydegreesmap() { // key=roleid value=今日增加的好友度
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "todayfriendlydegreesmap"), todayfriendlydegreesmap);
	}

	@Override
	public java.util.Map<Long, Integer> getTodayfriendlydegreesmapAsData() { // key=roleid value=今日增加的好友度
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> todayfriendlydegreesmap;
		FriendlyDegreesLimit _o_ = this;
		todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.todayfriendlydegreesmap.entrySet())
			todayfriendlydegreesmap.put(_e_.getKey(), _e_.getValue());
		return todayfriendlydegreesmap;
	}

	@Override
	public long getResettime() { // 每日增加的好友度重置时间
		_xdb_verify_unsafe_();
		return resettime;
	}

	@Override
	public void setResettime(long _v_) { // 每日增加的好友度重置时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "resettime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, resettime) {
					public void rollback() { resettime = _xdb_saved; }
				};}});
		resettime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FriendlyDegreesLimit _o_ = null;
		if ( _o1_ instanceof FriendlyDegreesLimit ) _o_ = (FriendlyDegreesLimit)_o1_;
		else if ( _o1_ instanceof FriendlyDegreesLimit.Const ) _o_ = ((FriendlyDegreesLimit.Const)_o1_).nThis();
		else return false;
		if (!todayfriendlydegreesmap.equals(_o_.todayfriendlydegreesmap)) return false;
		if (resettime != _o_.resettime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += todayfriendlydegreesmap.hashCode();
		_h_ += resettime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(todayfriendlydegreesmap);
		_sb_.append(",");
		_sb_.append(resettime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("todayfriendlydegreesmap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("resettime"));
		return lb;
	}

	private class Const implements xbean.FriendlyDegreesLimit {
		FriendlyDegreesLimit nThis() {
			return FriendlyDegreesLimit.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FriendlyDegreesLimit copy() {
			return FriendlyDegreesLimit.this.copy();
		}

		@Override
		public xbean.FriendlyDegreesLimit toData() {
			return FriendlyDegreesLimit.this.toData();
		}

		public xbean.FriendlyDegreesLimit toBean() {
			return FriendlyDegreesLimit.this.toBean();
		}

		@Override
		public xbean.FriendlyDegreesLimit toDataIf() {
			return FriendlyDegreesLimit.this.toDataIf();
		}

		public xbean.FriendlyDegreesLimit toBeanIf() {
			return FriendlyDegreesLimit.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Integer> getTodayfriendlydegreesmap() { // key=roleid value=今日增加的好友度
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(todayfriendlydegreesmap);
		}

		@Override
		public java.util.Map<Long, Integer> getTodayfriendlydegreesmapAsData() { // key=roleid value=今日增加的好友度
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> todayfriendlydegreesmap;
			FriendlyDegreesLimit _o_ = FriendlyDegreesLimit.this;
			todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.todayfriendlydegreesmap.entrySet())
				todayfriendlydegreesmap.put(_e_.getKey(), _e_.getValue());
			return todayfriendlydegreesmap;
		}

		@Override
		public long getResettime() { // 每日增加的好友度重置时间
			_xdb_verify_unsafe_();
			return resettime;
		}

		@Override
		public void setResettime(long _v_) { // 每日增加的好友度重置时间
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
			return FriendlyDegreesLimit.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FriendlyDegreesLimit.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FriendlyDegreesLimit.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FriendlyDegreesLimit.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FriendlyDegreesLimit.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FriendlyDegreesLimit.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FriendlyDegreesLimit.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FriendlyDegreesLimit.this.hashCode();
		}

		@Override
		public String toString() {
			return FriendlyDegreesLimit.this.toString();
		}

	}

	public static final class Data implements xbean.FriendlyDegreesLimit {
		private java.util.HashMap<Long, Integer> todayfriendlydegreesmap; // key=roleid value=今日增加的好友度
		private long resettime; // 每日增加的好友度重置时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>();
		}

		Data(xbean.FriendlyDegreesLimit _o1_) {
			if (_o1_ instanceof FriendlyDegreesLimit) assign((FriendlyDegreesLimit)_o1_);
			else if (_o1_ instanceof FriendlyDegreesLimit.Data) assign((FriendlyDegreesLimit.Data)_o1_);
			else if (_o1_ instanceof FriendlyDegreesLimit.Const) assign(((FriendlyDegreesLimit.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FriendlyDegreesLimit _o_) {
			todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.todayfriendlydegreesmap.entrySet())
				todayfriendlydegreesmap.put(_e_.getKey(), _e_.getValue());
			resettime = _o_.resettime;
		}

		private void assign(FriendlyDegreesLimit.Data _o_) {
			todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.todayfriendlydegreesmap.entrySet())
				todayfriendlydegreesmap.put(_e_.getKey(), _e_.getValue());
			resettime = _o_.resettime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(todayfriendlydegreesmap.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : todayfriendlydegreesmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(resettime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					todayfriendlydegreesmap = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					todayfriendlydegreesmap.put(_k_, _v_);
				}
			}
			resettime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.FriendlyDegreesLimit copy() {
			return new Data(this);
		}

		@Override
		public xbean.FriendlyDegreesLimit toData() {
			return new Data(this);
		}

		public xbean.FriendlyDegreesLimit toBean() {
			return new FriendlyDegreesLimit(this, null, null);
		}

		@Override
		public xbean.FriendlyDegreesLimit toDataIf() {
			return this;
		}

		public xbean.FriendlyDegreesLimit toBeanIf() {
			return new FriendlyDegreesLimit(this, null, null);
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
		public java.util.Map<Long, Integer> getTodayfriendlydegreesmap() { // key=roleid value=今日增加的好友度
			return todayfriendlydegreesmap;
		}

		@Override
		public java.util.Map<Long, Integer> getTodayfriendlydegreesmapAsData() { // key=roleid value=今日增加的好友度
			return todayfriendlydegreesmap;
		}

		@Override
		public long getResettime() { // 每日增加的好友度重置时间
			return resettime;
		}

		@Override
		public void setResettime(long _v_) { // 每日增加的好友度重置时间
			resettime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FriendlyDegreesLimit.Data)) return false;
			FriendlyDegreesLimit.Data _o_ = (FriendlyDegreesLimit.Data) _o1_;
			if (!todayfriendlydegreesmap.equals(_o_.todayfriendlydegreesmap)) return false;
			if (resettime != _o_.resettime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += todayfriendlydegreesmap.hashCode();
			_h_ += resettime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(todayfriendlydegreesmap);
			_sb_.append(",");
			_sb_.append(resettime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

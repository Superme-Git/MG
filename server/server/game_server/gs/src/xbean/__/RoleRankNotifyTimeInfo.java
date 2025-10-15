
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleRankNotifyTimeInfo extends mkdb.XBean implements xbean.RoleRankNotifyTimeInfo {
	private java.util.HashMap<Integer, Long> lasttime; // 上次各个排行榜变化通知时间

	@Override
	public void _reset_unsafe_() {
		lasttime.clear();
	}

	RoleRankNotifyTimeInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		lasttime = new java.util.HashMap<Integer, Long>();
	}

	public RoleRankNotifyTimeInfo() {
		this(0, null, null);
	}

	public RoleRankNotifyTimeInfo(RoleRankNotifyTimeInfo _o_) {
		this(_o_, null, null);
	}

	RoleRankNotifyTimeInfo(xbean.RoleRankNotifyTimeInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleRankNotifyTimeInfo) assign((RoleRankNotifyTimeInfo)_o1_);
		else if (_o1_ instanceof RoleRankNotifyTimeInfo.Data) assign((RoleRankNotifyTimeInfo.Data)_o1_);
		else if (_o1_ instanceof RoleRankNotifyTimeInfo.Const) assign(((RoleRankNotifyTimeInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleRankNotifyTimeInfo _o_) {
		_o_._xdb_verify_unsafe_();
		lasttime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lasttime.entrySet())
			lasttime.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(RoleRankNotifyTimeInfo.Data _o_) {
		lasttime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lasttime.entrySet())
			lasttime.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(lasttime.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : lasttime.entrySet())
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
				lasttime = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				lasttime.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleRankNotifyTimeInfo copy() {
		_xdb_verify_unsafe_();
		return new RoleRankNotifyTimeInfo(this);
	}

	@Override
	public xbean.RoleRankNotifyTimeInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleRankNotifyTimeInfo toBean() {
		_xdb_verify_unsafe_();
		return new RoleRankNotifyTimeInfo(this); // same as copy()
	}

	@Override
	public xbean.RoleRankNotifyTimeInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleRankNotifyTimeInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getLasttime() { // 上次各个排行榜变化通知时间
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "lasttime"), lasttime);
	}

	@Override
	public java.util.Map<Integer, Long> getLasttimeAsData() { // 上次各个排行榜变化通知时间
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> lasttime;
		RoleRankNotifyTimeInfo _o_ = this;
		lasttime = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lasttime.entrySet())
			lasttime.put(_e_.getKey(), _e_.getValue());
		return lasttime;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleRankNotifyTimeInfo _o_ = null;
		if ( _o1_ instanceof RoleRankNotifyTimeInfo ) _o_ = (RoleRankNotifyTimeInfo)_o1_;
		else if ( _o1_ instanceof RoleRankNotifyTimeInfo.Const ) _o_ = ((RoleRankNotifyTimeInfo.Const)_o1_).nThis();
		else return false;
		if (!lasttime.equals(_o_.lasttime)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += lasttime.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(lasttime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("lasttime"));
		return lb;
	}

	private class Const implements xbean.RoleRankNotifyTimeInfo {
		RoleRankNotifyTimeInfo nThis() {
			return RoleRankNotifyTimeInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleRankNotifyTimeInfo copy() {
			return RoleRankNotifyTimeInfo.this.copy();
		}

		@Override
		public xbean.RoleRankNotifyTimeInfo toData() {
			return RoleRankNotifyTimeInfo.this.toData();
		}

		public xbean.RoleRankNotifyTimeInfo toBean() {
			return RoleRankNotifyTimeInfo.this.toBean();
		}

		@Override
		public xbean.RoleRankNotifyTimeInfo toDataIf() {
			return RoleRankNotifyTimeInfo.this.toDataIf();
		}

		public xbean.RoleRankNotifyTimeInfo toBeanIf() {
			return RoleRankNotifyTimeInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getLasttime() { // 上次各个排行榜变化通知时间
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(lasttime);
		}

		@Override
		public java.util.Map<Integer, Long> getLasttimeAsData() { // 上次各个排行榜变化通知时间
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> lasttime;
			RoleRankNotifyTimeInfo _o_ = RoleRankNotifyTimeInfo.this;
			lasttime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lasttime.entrySet())
				lasttime.put(_e_.getKey(), _e_.getValue());
			return lasttime;
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
			return RoleRankNotifyTimeInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleRankNotifyTimeInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleRankNotifyTimeInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleRankNotifyTimeInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleRankNotifyTimeInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleRankNotifyTimeInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleRankNotifyTimeInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleRankNotifyTimeInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleRankNotifyTimeInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RoleRankNotifyTimeInfo {
		private java.util.HashMap<Integer, Long> lasttime; // 上次各个排行榜变化通知时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			lasttime = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.RoleRankNotifyTimeInfo _o1_) {
			if (_o1_ instanceof RoleRankNotifyTimeInfo) assign((RoleRankNotifyTimeInfo)_o1_);
			else if (_o1_ instanceof RoleRankNotifyTimeInfo.Data) assign((RoleRankNotifyTimeInfo.Data)_o1_);
			else if (_o1_ instanceof RoleRankNotifyTimeInfo.Const) assign(((RoleRankNotifyTimeInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleRankNotifyTimeInfo _o_) {
			lasttime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lasttime.entrySet())
				lasttime.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(RoleRankNotifyTimeInfo.Data _o_) {
			lasttime = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.lasttime.entrySet())
				lasttime.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(lasttime.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : lasttime.entrySet())
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
					lasttime = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					lasttime.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleRankNotifyTimeInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleRankNotifyTimeInfo toData() {
			return new Data(this);
		}

		public xbean.RoleRankNotifyTimeInfo toBean() {
			return new RoleRankNotifyTimeInfo(this, null, null);
		}

		@Override
		public xbean.RoleRankNotifyTimeInfo toDataIf() {
			return this;
		}

		public xbean.RoleRankNotifyTimeInfo toBeanIf() {
			return new RoleRankNotifyTimeInfo(this, null, null);
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
		public java.util.Map<Integer, Long> getLasttime() { // 上次各个排行榜变化通知时间
			return lasttime;
		}

		@Override
		public java.util.Map<Integer, Long> getLasttimeAsData() { // 上次各个排行榜变化通知时间
			return lasttime;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleRankNotifyTimeInfo.Data)) return false;
			RoleRankNotifyTimeInfo.Data _o_ = (RoleRankNotifyTimeInfo.Data) _o1_;
			if (!lasttime.equals(_o_.lasttime)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += lasttime.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(lasttime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

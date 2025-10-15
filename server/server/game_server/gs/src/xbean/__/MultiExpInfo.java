
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MultiExpInfo extends mkdb.XBean implements xbean.MultiExpInfo {
	private java.util.HashMap<Integer, Integer> multiexptime; // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
	private int remainnum; // 本周可以使用的个数
	private int dupdannum; // 本周可以使用的双倍丹的个数
	private long lastbacktime; // 上次返还多倍时间的时间
	private long refreshtime; // 刷新的时间

	@Override
	public void _reset_unsafe_() {
		multiexptime.clear();
		remainnum = 0;
		dupdannum = 0;
		lastbacktime = 0L;
		refreshtime = 0L;
	}

	MultiExpInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		multiexptime = new java.util.HashMap<Integer, Integer>();
	}

	public MultiExpInfo() {
		this(0, null, null);
	}

	public MultiExpInfo(MultiExpInfo _o_) {
		this(_o_, null, null);
	}

	MultiExpInfo(xbean.MultiExpInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MultiExpInfo) assign((MultiExpInfo)_o1_);
		else if (_o1_ instanceof MultiExpInfo.Data) assign((MultiExpInfo.Data)_o1_);
		else if (_o1_ instanceof MultiExpInfo.Const) assign(((MultiExpInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MultiExpInfo _o_) {
		_o_._xdb_verify_unsafe_();
		multiexptime = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.multiexptime.entrySet())
			multiexptime.put(_e_.getKey(), _e_.getValue());
		remainnum = _o_.remainnum;
		dupdannum = _o_.dupdannum;
		lastbacktime = _o_.lastbacktime;
		refreshtime = _o_.refreshtime;
	}

	private void assign(MultiExpInfo.Data _o_) {
		multiexptime = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.multiexptime.entrySet())
			multiexptime.put(_e_.getKey(), _e_.getValue());
		remainnum = _o_.remainnum;
		dupdannum = _o_.dupdannum;
		lastbacktime = _o_.lastbacktime;
		refreshtime = _o_.refreshtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(multiexptime.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : multiexptime.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(remainnum);
		_os_.marshal(dupdannum);
		_os_.marshal(lastbacktime);
		_os_.marshal(refreshtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				multiexptime = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				multiexptime.put(_k_, _v_);
			}
		}
		remainnum = _os_.unmarshal_int();
		dupdannum = _os_.unmarshal_int();
		lastbacktime = _os_.unmarshal_long();
		refreshtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.MultiExpInfo copy() {
		_xdb_verify_unsafe_();
		return new MultiExpInfo(this);
	}

	@Override
	public xbean.MultiExpInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MultiExpInfo toBean() {
		_xdb_verify_unsafe_();
		return new MultiExpInfo(this); // same as copy()
	}

	@Override
	public xbean.MultiExpInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MultiExpInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getMultiexptime() { // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "multiexptime"), multiexptime);
	}

	@Override
	public java.util.Map<Integer, Integer> getMultiexptimeAsData() { // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> multiexptime;
		MultiExpInfo _o_ = this;
		multiexptime = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.multiexptime.entrySet())
			multiexptime.put(_e_.getKey(), _e_.getValue());
		return multiexptime;
	}

	@Override
	public int getRemainnum() { // 本周可以使用的个数
		_xdb_verify_unsafe_();
		return remainnum;
	}

	@Override
	public int getDupdannum() { // 本周可以使用的双倍丹的个数
		_xdb_verify_unsafe_();
		return dupdannum;
	}

	@Override
	public long getLastbacktime() { // 上次返还多倍时间的时间
		_xdb_verify_unsafe_();
		return lastbacktime;
	}

	@Override
	public long getRefreshtime() { // 刷新的时间
		_xdb_verify_unsafe_();
		return refreshtime;
	}

	@Override
	public void setRemainnum(int _v_) { // 本周可以使用的个数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "remainnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, remainnum) {
					public void rollback() { remainnum = _xdb_saved; }
				};}});
		remainnum = _v_;
	}

	@Override
	public void setDupdannum(int _v_) { // 本周可以使用的双倍丹的个数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dupdannum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dupdannum) {
					public void rollback() { dupdannum = _xdb_saved; }
				};}});
		dupdannum = _v_;
	}

	@Override
	public void setLastbacktime(long _v_) { // 上次返还多倍时间的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastbacktime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastbacktime) {
					public void rollback() { lastbacktime = _xdb_saved; }
				};}});
		lastbacktime = _v_;
	}

	@Override
	public void setRefreshtime(long _v_) { // 刷新的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "refreshtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, refreshtime) {
					public void rollback() { refreshtime = _xdb_saved; }
				};}});
		refreshtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MultiExpInfo _o_ = null;
		if ( _o1_ instanceof MultiExpInfo ) _o_ = (MultiExpInfo)_o1_;
		else if ( _o1_ instanceof MultiExpInfo.Const ) _o_ = ((MultiExpInfo.Const)_o1_).nThis();
		else return false;
		if (!multiexptime.equals(_o_.multiexptime)) return false;
		if (remainnum != _o_.remainnum) return false;
		if (dupdannum != _o_.dupdannum) return false;
		if (lastbacktime != _o_.lastbacktime) return false;
		if (refreshtime != _o_.refreshtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += multiexptime.hashCode();
		_h_ += remainnum;
		_h_ += dupdannum;
		_h_ += lastbacktime;
		_h_ += refreshtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(multiexptime);
		_sb_.append(",");
		_sb_.append(remainnum);
		_sb_.append(",");
		_sb_.append(dupdannum);
		_sb_.append(",");
		_sb_.append(lastbacktime);
		_sb_.append(",");
		_sb_.append(refreshtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("multiexptime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("remainnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dupdannum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastbacktime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("refreshtime"));
		return lb;
	}

	private class Const implements xbean.MultiExpInfo {
		MultiExpInfo nThis() {
			return MultiExpInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MultiExpInfo copy() {
			return MultiExpInfo.this.copy();
		}

		@Override
		public xbean.MultiExpInfo toData() {
			return MultiExpInfo.this.toData();
		}

		public xbean.MultiExpInfo toBean() {
			return MultiExpInfo.this.toBean();
		}

		@Override
		public xbean.MultiExpInfo toDataIf() {
			return MultiExpInfo.this.toDataIf();
		}

		public xbean.MultiExpInfo toBeanIf() {
			return MultiExpInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getMultiexptime() { // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(multiexptime);
		}

		@Override
		public java.util.Map<Integer, Integer> getMultiexptimeAsData() { // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> multiexptime;
			MultiExpInfo _o_ = MultiExpInfo.this;
			multiexptime = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.multiexptime.entrySet())
				multiexptime.put(_e_.getKey(), _e_.getValue());
			return multiexptime;
		}

		@Override
		public int getRemainnum() { // 本周可以使用的个数
			_xdb_verify_unsafe_();
			return remainnum;
		}

		@Override
		public int getDupdannum() { // 本周可以使用的双倍丹的个数
			_xdb_verify_unsafe_();
			return dupdannum;
		}

		@Override
		public long getLastbacktime() { // 上次返还多倍时间的时间
			_xdb_verify_unsafe_();
			return lastbacktime;
		}

		@Override
		public long getRefreshtime() { // 刷新的时间
			_xdb_verify_unsafe_();
			return refreshtime;
		}

		@Override
		public void setRemainnum(int _v_) { // 本周可以使用的个数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDupdannum(int _v_) { // 本周可以使用的双倍丹的个数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastbacktime(long _v_) { // 上次返还多倍时间的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRefreshtime(long _v_) { // 刷新的时间
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
			return MultiExpInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MultiExpInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MultiExpInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MultiExpInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MultiExpInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MultiExpInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MultiExpInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MultiExpInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return MultiExpInfo.this.toString();
		}

	}

	public static final class Data implements xbean.MultiExpInfo {
		private java.util.HashMap<Integer, Integer> multiexptime; // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
		private int remainnum; // 本周可以使用的个数
		private int dupdannum; // 本周可以使用的双倍丹的个数
		private long lastbacktime; // 上次返还多倍时间的时间
		private long refreshtime; // 刷新的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			multiexptime = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.MultiExpInfo _o1_) {
			if (_o1_ instanceof MultiExpInfo) assign((MultiExpInfo)_o1_);
			else if (_o1_ instanceof MultiExpInfo.Data) assign((MultiExpInfo.Data)_o1_);
			else if (_o1_ instanceof MultiExpInfo.Const) assign(((MultiExpInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MultiExpInfo _o_) {
			multiexptime = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.multiexptime.entrySet())
				multiexptime.put(_e_.getKey(), _e_.getValue());
			remainnum = _o_.remainnum;
			dupdannum = _o_.dupdannum;
			lastbacktime = _o_.lastbacktime;
			refreshtime = _o_.refreshtime;
		}

		private void assign(MultiExpInfo.Data _o_) {
			multiexptime = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.multiexptime.entrySet())
				multiexptime.put(_e_.getKey(), _e_.getValue());
			remainnum = _o_.remainnum;
			dupdannum = _o_.dupdannum;
			lastbacktime = _o_.lastbacktime;
			refreshtime = _o_.refreshtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(multiexptime.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : multiexptime.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(remainnum);
			_os_.marshal(dupdannum);
			_os_.marshal(lastbacktime);
			_os_.marshal(refreshtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					multiexptime = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					multiexptime.put(_k_, _v_);
				}
			}
			remainnum = _os_.unmarshal_int();
			dupdannum = _os_.unmarshal_int();
			lastbacktime = _os_.unmarshal_long();
			refreshtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.MultiExpInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.MultiExpInfo toData() {
			return new Data(this);
		}

		public xbean.MultiExpInfo toBean() {
			return new MultiExpInfo(this, null, null);
		}

		@Override
		public xbean.MultiExpInfo toDataIf() {
			return this;
		}

		public xbean.MultiExpInfo toBeanIf() {
			return new MultiExpInfo(this, null, null);
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
		public java.util.Map<Integer, Integer> getMultiexptime() { // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
			return multiexptime;
		}

		@Override
		public java.util.Map<Integer, Integer> getMultiexptimeAsData() { // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
			return multiexptime;
		}

		@Override
		public int getRemainnum() { // 本周可以使用的个数
			return remainnum;
		}

		@Override
		public int getDupdannum() { // 本周可以使用的双倍丹的个数
			return dupdannum;
		}

		@Override
		public long getLastbacktime() { // 上次返还多倍时间的时间
			return lastbacktime;
		}

		@Override
		public long getRefreshtime() { // 刷新的时间
			return refreshtime;
		}

		@Override
		public void setRemainnum(int _v_) { // 本周可以使用的个数
			remainnum = _v_;
		}

		@Override
		public void setDupdannum(int _v_) { // 本周可以使用的双倍丹的个数
			dupdannum = _v_;
		}

		@Override
		public void setLastbacktime(long _v_) { // 上次返还多倍时间的时间
			lastbacktime = _v_;
		}

		@Override
		public void setRefreshtime(long _v_) { // 刷新的时间
			refreshtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MultiExpInfo.Data)) return false;
			MultiExpInfo.Data _o_ = (MultiExpInfo.Data) _o1_;
			if (!multiexptime.equals(_o_.multiexptime)) return false;
			if (remainnum != _o_.remainnum) return false;
			if (dupdannum != _o_.dupdannum) return false;
			if (lastbacktime != _o_.lastbacktime) return false;
			if (refreshtime != _o_.refreshtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += multiexptime.hashCode();
			_h_ += remainnum;
			_h_ += dupdannum;
			_h_ += lastbacktime;
			_h_ += refreshtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(multiexptime);
			_sb_.append(",");
			_sb_.append(remainnum);
			_sb_.append(",");
			_sb_.append(dupdannum);
			_sb_.append(",");
			_sb_.append(lastbacktime);
			_sb_.append(",");
			_sb_.append(refreshtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

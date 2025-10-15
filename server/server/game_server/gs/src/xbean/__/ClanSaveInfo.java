
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanSaveInfo extends mkdb.XBean implements xbean.ClanSaveInfo {
	private java.util.HashMap<Integer, Long> savestate; // key为进度id; value为进度使用的时间
	private long refreshtime; // 刷新时间

	@Override
	public void _reset_unsafe_() {
		savestate.clear();
		refreshtime = 0L;
	}

	ClanSaveInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		savestate = new java.util.HashMap<Integer, Long>();
	}

	public ClanSaveInfo() {
		this(0, null, null);
	}

	public ClanSaveInfo(ClanSaveInfo _o_) {
		this(_o_, null, null);
	}

	ClanSaveInfo(xbean.ClanSaveInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanSaveInfo) assign((ClanSaveInfo)_o1_);
		else if (_o1_ instanceof ClanSaveInfo.Data) assign((ClanSaveInfo.Data)_o1_);
		else if (_o1_ instanceof ClanSaveInfo.Const) assign(((ClanSaveInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanSaveInfo _o_) {
		_o_._xdb_verify_unsafe_();
		savestate = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.savestate.entrySet())
			savestate.put(_e_.getKey(), _e_.getValue());
		refreshtime = _o_.refreshtime;
	}

	private void assign(ClanSaveInfo.Data _o_) {
		savestate = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.savestate.entrySet())
			savestate.put(_e_.getKey(), _e_.getValue());
		refreshtime = _o_.refreshtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(savestate.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : savestate.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(refreshtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				savestate = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				savestate.put(_k_, _v_);
			}
		}
		refreshtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ClanSaveInfo copy() {
		_xdb_verify_unsafe_();
		return new ClanSaveInfo(this);
	}

	@Override
	public xbean.ClanSaveInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanSaveInfo toBean() {
		_xdb_verify_unsafe_();
		return new ClanSaveInfo(this); // same as copy()
	}

	@Override
	public xbean.ClanSaveInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanSaveInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getSavestate() { // key为进度id; value为进度使用的时间
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "savestate"), savestate);
	}

	@Override
	public java.util.Map<Integer, Long> getSavestateAsData() { // key为进度id; value为进度使用的时间
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> savestate;
		ClanSaveInfo _o_ = this;
		savestate = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.savestate.entrySet())
			savestate.put(_e_.getKey(), _e_.getValue());
		return savestate;
	}

	@Override
	public long getRefreshtime() { // 刷新时间
		_xdb_verify_unsafe_();
		return refreshtime;
	}

	@Override
	public void setRefreshtime(long _v_) { // 刷新时间
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
		ClanSaveInfo _o_ = null;
		if ( _o1_ instanceof ClanSaveInfo ) _o_ = (ClanSaveInfo)_o1_;
		else if ( _o1_ instanceof ClanSaveInfo.Const ) _o_ = ((ClanSaveInfo.Const)_o1_).nThis();
		else return false;
		if (!savestate.equals(_o_.savestate)) return false;
		if (refreshtime != _o_.refreshtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += savestate.hashCode();
		_h_ += refreshtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(savestate);
		_sb_.append(",");
		_sb_.append(refreshtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("savestate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("refreshtime"));
		return lb;
	}

	private class Const implements xbean.ClanSaveInfo {
		ClanSaveInfo nThis() {
			return ClanSaveInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanSaveInfo copy() {
			return ClanSaveInfo.this.copy();
		}

		@Override
		public xbean.ClanSaveInfo toData() {
			return ClanSaveInfo.this.toData();
		}

		public xbean.ClanSaveInfo toBean() {
			return ClanSaveInfo.this.toBean();
		}

		@Override
		public xbean.ClanSaveInfo toDataIf() {
			return ClanSaveInfo.this.toDataIf();
		}

		public xbean.ClanSaveInfo toBeanIf() {
			return ClanSaveInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getSavestate() { // key为进度id; value为进度使用的时间
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(savestate);
		}

		@Override
		public java.util.Map<Integer, Long> getSavestateAsData() { // key为进度id; value为进度使用的时间
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> savestate;
			ClanSaveInfo _o_ = ClanSaveInfo.this;
			savestate = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.savestate.entrySet())
				savestate.put(_e_.getKey(), _e_.getValue());
			return savestate;
		}

		@Override
		public long getRefreshtime() { // 刷新时间
			_xdb_verify_unsafe_();
			return refreshtime;
		}

		@Override
		public void setRefreshtime(long _v_) { // 刷新时间
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
			return ClanSaveInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanSaveInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanSaveInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanSaveInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanSaveInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanSaveInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanSaveInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanSaveInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanSaveInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ClanSaveInfo {
		private java.util.HashMap<Integer, Long> savestate; // key为进度id; value为进度使用的时间
		private long refreshtime; // 刷新时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			savestate = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.ClanSaveInfo _o1_) {
			if (_o1_ instanceof ClanSaveInfo) assign((ClanSaveInfo)_o1_);
			else if (_o1_ instanceof ClanSaveInfo.Data) assign((ClanSaveInfo.Data)_o1_);
			else if (_o1_ instanceof ClanSaveInfo.Const) assign(((ClanSaveInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanSaveInfo _o_) {
			savestate = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.savestate.entrySet())
				savestate.put(_e_.getKey(), _e_.getValue());
			refreshtime = _o_.refreshtime;
		}

		private void assign(ClanSaveInfo.Data _o_) {
			savestate = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.savestate.entrySet())
				savestate.put(_e_.getKey(), _e_.getValue());
			refreshtime = _o_.refreshtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(savestate.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : savestate.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(refreshtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					savestate = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					savestate.put(_k_, _v_);
				}
			}
			refreshtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ClanSaveInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanSaveInfo toData() {
			return new Data(this);
		}

		public xbean.ClanSaveInfo toBean() {
			return new ClanSaveInfo(this, null, null);
		}

		@Override
		public xbean.ClanSaveInfo toDataIf() {
			return this;
		}

		public xbean.ClanSaveInfo toBeanIf() {
			return new ClanSaveInfo(this, null, null);
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
		public java.util.Map<Integer, Long> getSavestate() { // key为进度id; value为进度使用的时间
			return savestate;
		}

		@Override
		public java.util.Map<Integer, Long> getSavestateAsData() { // key为进度id; value为进度使用的时间
			return savestate;
		}

		@Override
		public long getRefreshtime() { // 刷新时间
			return refreshtime;
		}

		@Override
		public void setRefreshtime(long _v_) { // 刷新时间
			refreshtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanSaveInfo.Data)) return false;
			ClanSaveInfo.Data _o_ = (ClanSaveInfo.Data) _o1_;
			if (!savestate.equals(_o_.savestate)) return false;
			if (refreshtime != _o_.refreshtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += savestate.hashCode();
			_h_ += refreshtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(savestate);
			_sb_.append(",");
			_sb_.append(refreshtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

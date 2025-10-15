
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AccusationInfo extends mkdb.XBean implements xbean.AccusationInfo {
	private long roleid; // 被举报人ID
	private java.util.ArrayList<Long> accusationedroleids; // 举报人IDs
	private long lasttime; // 最后一次被举报时间

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		accusationedroleids.clear();
		lasttime = 0;
	}

	AccusationInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		accusationedroleids = new java.util.ArrayList<Long>();
		lasttime = 0;
	}

	public AccusationInfo() {
		this(0, null, null);
	}

	public AccusationInfo(AccusationInfo _o_) {
		this(_o_, null, null);
	}

	AccusationInfo(xbean.AccusationInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AccusationInfo) assign((AccusationInfo)_o1_);
		else if (_o1_ instanceof AccusationInfo.Data) assign((AccusationInfo.Data)_o1_);
		else if (_o1_ instanceof AccusationInfo.Const) assign(((AccusationInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AccusationInfo _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		accusationedroleids = new java.util.ArrayList<Long>();
		accusationedroleids.addAll(_o_.accusationedroleids);
		lasttime = _o_.lasttime;
	}

	private void assign(AccusationInfo.Data _o_) {
		roleid = _o_.roleid;
		accusationedroleids = new java.util.ArrayList<Long>();
		accusationedroleids.addAll(_o_.accusationedroleids);
		lasttime = _o_.lasttime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.compact_uint32(accusationedroleids.size());
		for (Long _v_ : accusationedroleids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(lasttime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			accusationedroleids.add(_v_);
		}
		lasttime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.AccusationInfo copy() {
		_xdb_verify_unsafe_();
		return new AccusationInfo(this);
	}

	@Override
	public xbean.AccusationInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AccusationInfo toBean() {
		_xdb_verify_unsafe_();
		return new AccusationInfo(this); // same as copy()
	}

	@Override
	public xbean.AccusationInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AccusationInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 被举报人ID
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public java.util.List<Long> getAccusationedroleids() { // 举报人IDs
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "accusationedroleids"), accusationedroleids);
	}

	public java.util.List<Long> getAccusationedroleidsAsData() { // 举报人IDs
		_xdb_verify_unsafe_();
		java.util.List<Long> accusationedroleids;
		AccusationInfo _o_ = this;
		accusationedroleids = new java.util.ArrayList<Long>();
		accusationedroleids.addAll(_o_.accusationedroleids);
		return accusationedroleids;
	}

	@Override
	public long getLasttime() { // 最后一次被举报时间
		_xdb_verify_unsafe_();
		return lasttime;
	}

	@Override
	public void setRoleid(long _v_) { // 被举报人ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setLasttime(long _v_) { // 最后一次被举报时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lasttime) {
					public void rollback() { lasttime = _xdb_saved; }
				};}});
		lasttime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AccusationInfo _o_ = null;
		if ( _o1_ instanceof AccusationInfo ) _o_ = (AccusationInfo)_o1_;
		else if ( _o1_ instanceof AccusationInfo.Const ) _o_ = ((AccusationInfo.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!accusationedroleids.equals(_o_.accusationedroleids)) return false;
		if (lasttime != _o_.lasttime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += accusationedroleids.hashCode();
		_h_ += lasttime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(accusationedroleids);
		_sb_.append(",");
		_sb_.append(lasttime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("accusationedroleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttime"));
		return lb;
	}

	private class Const implements xbean.AccusationInfo {
		AccusationInfo nThis() {
			return AccusationInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AccusationInfo copy() {
			return AccusationInfo.this.copy();
		}

		@Override
		public xbean.AccusationInfo toData() {
			return AccusationInfo.this.toData();
		}

		public xbean.AccusationInfo toBean() {
			return AccusationInfo.this.toBean();
		}

		@Override
		public xbean.AccusationInfo toDataIf() {
			return AccusationInfo.this.toDataIf();
		}

		public xbean.AccusationInfo toBeanIf() {
			return AccusationInfo.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 被举报人ID
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public java.util.List<Long> getAccusationedroleids() { // 举报人IDs
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(accusationedroleids);
		}

		public java.util.List<Long> getAccusationedroleidsAsData() { // 举报人IDs
			_xdb_verify_unsafe_();
			java.util.List<Long> accusationedroleids;
			AccusationInfo _o_ = AccusationInfo.this;
		accusationedroleids = new java.util.ArrayList<Long>();
		accusationedroleids.addAll(_o_.accusationedroleids);
			return accusationedroleids;
		}

		@Override
		public long getLasttime() { // 最后一次被举报时间
			_xdb_verify_unsafe_();
			return lasttime;
		}

		@Override
		public void setRoleid(long _v_) { // 被举报人ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttime(long _v_) { // 最后一次被举报时间
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
			return AccusationInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AccusationInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AccusationInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AccusationInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AccusationInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AccusationInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AccusationInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AccusationInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return AccusationInfo.this.toString();
		}

	}

	public static final class Data implements xbean.AccusationInfo {
		private long roleid; // 被举报人ID
		private java.util.ArrayList<Long> accusationedroleids; // 举报人IDs
		private long lasttime; // 最后一次被举报时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			accusationedroleids = new java.util.ArrayList<Long>();
			lasttime = 0;
		}

		Data(xbean.AccusationInfo _o1_) {
			if (_o1_ instanceof AccusationInfo) assign((AccusationInfo)_o1_);
			else if (_o1_ instanceof AccusationInfo.Data) assign((AccusationInfo.Data)_o1_);
			else if (_o1_ instanceof AccusationInfo.Const) assign(((AccusationInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AccusationInfo _o_) {
			roleid = _o_.roleid;
			accusationedroleids = new java.util.ArrayList<Long>();
			accusationedroleids.addAll(_o_.accusationedroleids);
			lasttime = _o_.lasttime;
		}

		private void assign(AccusationInfo.Data _o_) {
			roleid = _o_.roleid;
			accusationedroleids = new java.util.ArrayList<Long>();
			accusationedroleids.addAll(_o_.accusationedroleids);
			lasttime = _o_.lasttime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.compact_uint32(accusationedroleids.size());
			for (Long _v_ : accusationedroleids) {
				_os_.marshal(_v_);
			}
			_os_.marshal(lasttime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				accusationedroleids.add(_v_);
			}
			lasttime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.AccusationInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.AccusationInfo toData() {
			return new Data(this);
		}

		public xbean.AccusationInfo toBean() {
			return new AccusationInfo(this, null, null);
		}

		@Override
		public xbean.AccusationInfo toDataIf() {
			return this;
		}

		public xbean.AccusationInfo toBeanIf() {
			return new AccusationInfo(this, null, null);
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
		public long getRoleid() { // 被举报人ID
			return roleid;
		}

		@Override
		public java.util.List<Long> getAccusationedroleids() { // 举报人IDs
			return accusationedroleids;
		}

		@Override
		public java.util.List<Long> getAccusationedroleidsAsData() { // 举报人IDs
			return accusationedroleids;
		}

		@Override
		public long getLasttime() { // 最后一次被举报时间
			return lasttime;
		}

		@Override
		public void setRoleid(long _v_) { // 被举报人ID
			roleid = _v_;
		}

		@Override
		public void setLasttime(long _v_) { // 最后一次被举报时间
			lasttime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AccusationInfo.Data)) return false;
			AccusationInfo.Data _o_ = (AccusationInfo.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!accusationedroleids.equals(_o_.accusationedroleids)) return false;
			if (lasttime != _o_.lasttime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += accusationedroleids.hashCode();
			_h_ += lasttime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(accusationedroleids);
			_sb_.append(",");
			_sb_.append(lasttime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

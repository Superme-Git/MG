
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleUpdateTime extends mkdb.XBean implements xbean.RoleUpdateTime {
	private long dateupdatetime; // 每日更新时间
	private long weekupdatetime; // 每周更新时间

	@Override
	public void _reset_unsafe_() {
		dateupdatetime = 0;
		weekupdatetime = 0;
	}

	RoleUpdateTime(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		dateupdatetime = 0;
		weekupdatetime = 0;
	}

	public RoleUpdateTime() {
		this(0, null, null);
	}

	public RoleUpdateTime(RoleUpdateTime _o_) {
		this(_o_, null, null);
	}

	RoleUpdateTime(xbean.RoleUpdateTime _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleUpdateTime) assign((RoleUpdateTime)_o1_);
		else if (_o1_ instanceof RoleUpdateTime.Data) assign((RoleUpdateTime.Data)_o1_);
		else if (_o1_ instanceof RoleUpdateTime.Const) assign(((RoleUpdateTime.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleUpdateTime _o_) {
		_o_._xdb_verify_unsafe_();
		dateupdatetime = _o_.dateupdatetime;
		weekupdatetime = _o_.weekupdatetime;
	}

	private void assign(RoleUpdateTime.Data _o_) {
		dateupdatetime = _o_.dateupdatetime;
		weekupdatetime = _o_.weekupdatetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(dateupdatetime);
		_os_.marshal(weekupdatetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		dateupdatetime = _os_.unmarshal_long();
		weekupdatetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleUpdateTime copy() {
		_xdb_verify_unsafe_();
		return new RoleUpdateTime(this);
	}

	@Override
	public xbean.RoleUpdateTime toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleUpdateTime toBean() {
		_xdb_verify_unsafe_();
		return new RoleUpdateTime(this); // same as copy()
	}

	@Override
	public xbean.RoleUpdateTime toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleUpdateTime toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getDateupdatetime() { // 每日更新时间
		_xdb_verify_unsafe_();
		return dateupdatetime;
	}

	@Override
	public long getWeekupdatetime() { // 每周更新时间
		_xdb_verify_unsafe_();
		return weekupdatetime;
	}

	@Override
	public void setDateupdatetime(long _v_) { // 每日更新时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dateupdatetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, dateupdatetime) {
					public void rollback() { dateupdatetime = _xdb_saved; }
				};}});
		dateupdatetime = _v_;
	}

	@Override
	public void setWeekupdatetime(long _v_) { // 每周更新时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "weekupdatetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, weekupdatetime) {
					public void rollback() { weekupdatetime = _xdb_saved; }
				};}});
		weekupdatetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleUpdateTime _o_ = null;
		if ( _o1_ instanceof RoleUpdateTime ) _o_ = (RoleUpdateTime)_o1_;
		else if ( _o1_ instanceof RoleUpdateTime.Const ) _o_ = ((RoleUpdateTime.Const)_o1_).nThis();
		else return false;
		if (dateupdatetime != _o_.dateupdatetime) return false;
		if (weekupdatetime != _o_.weekupdatetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += dateupdatetime;
		_h_ += weekupdatetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(dateupdatetime);
		_sb_.append(",");
		_sb_.append(weekupdatetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dateupdatetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("weekupdatetime"));
		return lb;
	}

	private class Const implements xbean.RoleUpdateTime {
		RoleUpdateTime nThis() {
			return RoleUpdateTime.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleUpdateTime copy() {
			return RoleUpdateTime.this.copy();
		}

		@Override
		public xbean.RoleUpdateTime toData() {
			return RoleUpdateTime.this.toData();
		}

		public xbean.RoleUpdateTime toBean() {
			return RoleUpdateTime.this.toBean();
		}

		@Override
		public xbean.RoleUpdateTime toDataIf() {
			return RoleUpdateTime.this.toDataIf();
		}

		public xbean.RoleUpdateTime toBeanIf() {
			return RoleUpdateTime.this.toBeanIf();
		}

		@Override
		public long getDateupdatetime() { // 每日更新时间
			_xdb_verify_unsafe_();
			return dateupdatetime;
		}

		@Override
		public long getWeekupdatetime() { // 每周更新时间
			_xdb_verify_unsafe_();
			return weekupdatetime;
		}

		@Override
		public void setDateupdatetime(long _v_) { // 每日更新时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWeekupdatetime(long _v_) { // 每周更新时间
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
			return RoleUpdateTime.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleUpdateTime.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleUpdateTime.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleUpdateTime.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleUpdateTime.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleUpdateTime.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleUpdateTime.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleUpdateTime.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleUpdateTime.this.toString();
		}

	}

	public static final class Data implements xbean.RoleUpdateTime {
		private long dateupdatetime; // 每日更新时间
		private long weekupdatetime; // 每周更新时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			dateupdatetime = 0;
			weekupdatetime = 0;
		}

		Data(xbean.RoleUpdateTime _o1_) {
			if (_o1_ instanceof RoleUpdateTime) assign((RoleUpdateTime)_o1_);
			else if (_o1_ instanceof RoleUpdateTime.Data) assign((RoleUpdateTime.Data)_o1_);
			else if (_o1_ instanceof RoleUpdateTime.Const) assign(((RoleUpdateTime.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleUpdateTime _o_) {
			dateupdatetime = _o_.dateupdatetime;
			weekupdatetime = _o_.weekupdatetime;
		}

		private void assign(RoleUpdateTime.Data _o_) {
			dateupdatetime = _o_.dateupdatetime;
			weekupdatetime = _o_.weekupdatetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(dateupdatetime);
			_os_.marshal(weekupdatetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			dateupdatetime = _os_.unmarshal_long();
			weekupdatetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleUpdateTime copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleUpdateTime toData() {
			return new Data(this);
		}

		public xbean.RoleUpdateTime toBean() {
			return new RoleUpdateTime(this, null, null);
		}

		@Override
		public xbean.RoleUpdateTime toDataIf() {
			return this;
		}

		public xbean.RoleUpdateTime toBeanIf() {
			return new RoleUpdateTime(this, null, null);
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
		public long getDateupdatetime() { // 每日更新时间
			return dateupdatetime;
		}

		@Override
		public long getWeekupdatetime() { // 每周更新时间
			return weekupdatetime;
		}

		@Override
		public void setDateupdatetime(long _v_) { // 每日更新时间
			dateupdatetime = _v_;
		}

		@Override
		public void setWeekupdatetime(long _v_) { // 每周更新时间
			weekupdatetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleUpdateTime.Data)) return false;
			RoleUpdateTime.Data _o_ = (RoleUpdateTime.Data) _o1_;
			if (dateupdatetime != _o_.dateupdatetime) return false;
			if (weekupdatetime != _o_.weekupdatetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += dateupdatetime;
			_h_ += weekupdatetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(dateupdatetime);
			_sb_.append(",");
			_sb_.append(weekupdatetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

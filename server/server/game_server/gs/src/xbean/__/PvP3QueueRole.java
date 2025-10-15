
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PvP3QueueRole extends mkdb.XBean implements xbean.PvP3QueueRole {
	private long roleid; // 
	private long enterqueuetime; // 进入队列的时间

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		enterqueuetime = 0L;
	}

	PvP3QueueRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public PvP3QueueRole() {
		this(0, null, null);
	}

	public PvP3QueueRole(PvP3QueueRole _o_) {
		this(_o_, null, null);
	}

	PvP3QueueRole(xbean.PvP3QueueRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PvP3QueueRole) assign((PvP3QueueRole)_o1_);
		else if (_o1_ instanceof PvP3QueueRole.Data) assign((PvP3QueueRole.Data)_o1_);
		else if (_o1_ instanceof PvP3QueueRole.Const) assign(((PvP3QueueRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PvP3QueueRole _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		enterqueuetime = _o_.enterqueuetime;
	}

	private void assign(PvP3QueueRole.Data _o_) {
		roleid = _o_.roleid;
		enterqueuetime = _o_.enterqueuetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(enterqueuetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		enterqueuetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.PvP3QueueRole copy() {
		_xdb_verify_unsafe_();
		return new PvP3QueueRole(this);
	}

	@Override
	public xbean.PvP3QueueRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP3QueueRole toBean() {
		_xdb_verify_unsafe_();
		return new PvP3QueueRole(this); // same as copy()
	}

	@Override
	public xbean.PvP3QueueRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP3QueueRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public long getEnterqueuetime() { // 进入队列的时间
		_xdb_verify_unsafe_();
		return enterqueuetime;
	}

	@Override
	public void setRoleid(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setEnterqueuetime(long _v_) { // 进入队列的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "enterqueuetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, enterqueuetime) {
					public void rollback() { enterqueuetime = _xdb_saved; }
				};}});
		enterqueuetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PvP3QueueRole _o_ = null;
		if ( _o1_ instanceof PvP3QueueRole ) _o_ = (PvP3QueueRole)_o1_;
		else if ( _o1_ instanceof PvP3QueueRole.Const ) _o_ = ((PvP3QueueRole.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (enterqueuetime != _o_.enterqueuetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += enterqueuetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(enterqueuetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("enterqueuetime"));
		return lb;
	}

	private class Const implements xbean.PvP3QueueRole {
		PvP3QueueRole nThis() {
			return PvP3QueueRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PvP3QueueRole copy() {
			return PvP3QueueRole.this.copy();
		}

		@Override
		public xbean.PvP3QueueRole toData() {
			return PvP3QueueRole.this.toData();
		}

		public xbean.PvP3QueueRole toBean() {
			return PvP3QueueRole.this.toBean();
		}

		@Override
		public xbean.PvP3QueueRole toDataIf() {
			return PvP3QueueRole.this.toDataIf();
		}

		public xbean.PvP3QueueRole toBeanIf() {
			return PvP3QueueRole.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getEnterqueuetime() { // 进入队列的时间
			_xdb_verify_unsafe_();
			return enterqueuetime;
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEnterqueuetime(long _v_) { // 进入队列的时间
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
			return PvP3QueueRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PvP3QueueRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PvP3QueueRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PvP3QueueRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PvP3QueueRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PvP3QueueRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PvP3QueueRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PvP3QueueRole.this.hashCode();
		}

		@Override
		public String toString() {
			return PvP3QueueRole.this.toString();
		}

	}

	public static final class Data implements xbean.PvP3QueueRole {
		private long roleid; // 
		private long enterqueuetime; // 进入队列的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.PvP3QueueRole _o1_) {
			if (_o1_ instanceof PvP3QueueRole) assign((PvP3QueueRole)_o1_);
			else if (_o1_ instanceof PvP3QueueRole.Data) assign((PvP3QueueRole.Data)_o1_);
			else if (_o1_ instanceof PvP3QueueRole.Const) assign(((PvP3QueueRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PvP3QueueRole _o_) {
			roleid = _o_.roleid;
			enterqueuetime = _o_.enterqueuetime;
		}

		private void assign(PvP3QueueRole.Data _o_) {
			roleid = _o_.roleid;
			enterqueuetime = _o_.enterqueuetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(enterqueuetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			enterqueuetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.PvP3QueueRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.PvP3QueueRole toData() {
			return new Data(this);
		}

		public xbean.PvP3QueueRole toBean() {
			return new PvP3QueueRole(this, null, null);
		}

		@Override
		public xbean.PvP3QueueRole toDataIf() {
			return this;
		}

		public xbean.PvP3QueueRole toBeanIf() {
			return new PvP3QueueRole(this, null, null);
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
		public long getRoleid() { // 
			return roleid;
		}

		@Override
		public long getEnterqueuetime() { // 进入队列的时间
			return enterqueuetime;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setEnterqueuetime(long _v_) { // 进入队列的时间
			enterqueuetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PvP3QueueRole.Data)) return false;
			PvP3QueueRole.Data _o_ = (PvP3QueueRole.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (enterqueuetime != _o_.enterqueuetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += enterqueuetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(enterqueuetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PvP1QueueRole extends mkdb.XBean implements xbean.PvP1QueueRole {
	private long roleid; // 
	private long enterqueuetime; // 进入队列的时间

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		enterqueuetime = 0L;
	}

	PvP1QueueRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public PvP1QueueRole() {
		this(0, null, null);
	}

	public PvP1QueueRole(PvP1QueueRole _o_) {
		this(_o_, null, null);
	}

	PvP1QueueRole(xbean.PvP1QueueRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PvP1QueueRole) assign((PvP1QueueRole)_o1_);
		else if (_o1_ instanceof PvP1QueueRole.Data) assign((PvP1QueueRole.Data)_o1_);
		else if (_o1_ instanceof PvP1QueueRole.Const) assign(((PvP1QueueRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PvP1QueueRole _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		enterqueuetime = _o_.enterqueuetime;
	}

	private void assign(PvP1QueueRole.Data _o_) {
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
	public xbean.PvP1QueueRole copy() {
		_xdb_verify_unsafe_();
		return new PvP1QueueRole(this);
	}

	@Override
	public xbean.PvP1QueueRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP1QueueRole toBean() {
		_xdb_verify_unsafe_();
		return new PvP1QueueRole(this); // same as copy()
	}

	@Override
	public xbean.PvP1QueueRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP1QueueRole toBeanIf() {
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
		PvP1QueueRole _o_ = null;
		if ( _o1_ instanceof PvP1QueueRole ) _o_ = (PvP1QueueRole)_o1_;
		else if ( _o1_ instanceof PvP1QueueRole.Const ) _o_ = ((PvP1QueueRole.Const)_o1_).nThis();
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

	private class Const implements xbean.PvP1QueueRole {
		PvP1QueueRole nThis() {
			return PvP1QueueRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PvP1QueueRole copy() {
			return PvP1QueueRole.this.copy();
		}

		@Override
		public xbean.PvP1QueueRole toData() {
			return PvP1QueueRole.this.toData();
		}

		public xbean.PvP1QueueRole toBean() {
			return PvP1QueueRole.this.toBean();
		}

		@Override
		public xbean.PvP1QueueRole toDataIf() {
			return PvP1QueueRole.this.toDataIf();
		}

		public xbean.PvP1QueueRole toBeanIf() {
			return PvP1QueueRole.this.toBeanIf();
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
			return PvP1QueueRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PvP1QueueRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PvP1QueueRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PvP1QueueRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PvP1QueueRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PvP1QueueRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PvP1QueueRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PvP1QueueRole.this.hashCode();
		}

		@Override
		public String toString() {
			return PvP1QueueRole.this.toString();
		}

	}

	public static final class Data implements xbean.PvP1QueueRole {
		private long roleid; // 
		private long enterqueuetime; // 进入队列的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.PvP1QueueRole _o1_) {
			if (_o1_ instanceof PvP1QueueRole) assign((PvP1QueueRole)_o1_);
			else if (_o1_ instanceof PvP1QueueRole.Data) assign((PvP1QueueRole.Data)_o1_);
			else if (_o1_ instanceof PvP1QueueRole.Const) assign(((PvP1QueueRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PvP1QueueRole _o_) {
			roleid = _o_.roleid;
			enterqueuetime = _o_.enterqueuetime;
		}

		private void assign(PvP1QueueRole.Data _o_) {
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
		public xbean.PvP1QueueRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.PvP1QueueRole toData() {
			return new Data(this);
		}

		public xbean.PvP1QueueRole toBean() {
			return new PvP1QueueRole(this, null, null);
		}

		@Override
		public xbean.PvP1QueueRole toDataIf() {
			return this;
		}

		public xbean.PvP1QueueRole toBeanIf() {
			return new PvP1QueueRole(this, null, null);
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
			if (!(_o1_ instanceof PvP1QueueRole.Data)) return false;
			PvP1QueueRole.Data _o_ = (PvP1QueueRole.Data) _o1_;
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

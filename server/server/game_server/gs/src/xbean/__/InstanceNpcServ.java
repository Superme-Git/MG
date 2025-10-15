
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceNpcServ extends mkdb.XBean implements xbean.InstanceNpcServ {
	private long serviceid; // serviceid
	private int actid; // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作

	@Override
	public void _reset_unsafe_() {
		serviceid = 0L;
		actid = 0;
	}

	InstanceNpcServ(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public InstanceNpcServ() {
		this(0, null, null);
	}

	public InstanceNpcServ(InstanceNpcServ _o_) {
		this(_o_, null, null);
	}

	InstanceNpcServ(xbean.InstanceNpcServ _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceNpcServ) assign((InstanceNpcServ)_o1_);
		else if (_o1_ instanceof InstanceNpcServ.Data) assign((InstanceNpcServ.Data)_o1_);
		else if (_o1_ instanceof InstanceNpcServ.Const) assign(((InstanceNpcServ.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceNpcServ _o_) {
		_o_._xdb_verify_unsafe_();
		serviceid = _o_.serviceid;
		actid = _o_.actid;
	}

	private void assign(InstanceNpcServ.Data _o_) {
		serviceid = _o_.serviceid;
		actid = _o_.actid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(serviceid);
		_os_.marshal(actid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		serviceid = _os_.unmarshal_long();
		actid = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.InstanceNpcServ copy() {
		_xdb_verify_unsafe_();
		return new InstanceNpcServ(this);
	}

	@Override
	public xbean.InstanceNpcServ toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceNpcServ toBean() {
		_xdb_verify_unsafe_();
		return new InstanceNpcServ(this); // same as copy()
	}

	@Override
	public xbean.InstanceNpcServ toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceNpcServ toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getServiceid() { // serviceid
		_xdb_verify_unsafe_();
		return serviceid;
	}

	@Override
	public int getActid() { // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作
		_xdb_verify_unsafe_();
		return actid;
	}

	@Override
	public void setServiceid(long _v_) { // serviceid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "serviceid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, serviceid) {
					public void rollback() { serviceid = _xdb_saved; }
				};}});
		serviceid = _v_;
	}

	@Override
	public void setActid(int _v_) { // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "actid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, actid) {
					public void rollback() { actid = _xdb_saved; }
				};}});
		actid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceNpcServ _o_ = null;
		if ( _o1_ instanceof InstanceNpcServ ) _o_ = (InstanceNpcServ)_o1_;
		else if ( _o1_ instanceof InstanceNpcServ.Const ) _o_ = ((InstanceNpcServ.Const)_o1_).nThis();
		else return false;
		if (serviceid != _o_.serviceid) return false;
		if (actid != _o_.actid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += serviceid;
		_h_ += actid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(serviceid);
		_sb_.append(",");
		_sb_.append(actid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("serviceid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("actid"));
		return lb;
	}

	private class Const implements xbean.InstanceNpcServ {
		InstanceNpcServ nThis() {
			return InstanceNpcServ.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceNpcServ copy() {
			return InstanceNpcServ.this.copy();
		}

		@Override
		public xbean.InstanceNpcServ toData() {
			return InstanceNpcServ.this.toData();
		}

		public xbean.InstanceNpcServ toBean() {
			return InstanceNpcServ.this.toBean();
		}

		@Override
		public xbean.InstanceNpcServ toDataIf() {
			return InstanceNpcServ.this.toDataIf();
		}

		public xbean.InstanceNpcServ toBeanIf() {
			return InstanceNpcServ.this.toBeanIf();
		}

		@Override
		public long getServiceid() { // serviceid
			_xdb_verify_unsafe_();
			return serviceid;
		}

		@Override
		public int getActid() { // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作
			_xdb_verify_unsafe_();
			return actid;
		}

		@Override
		public void setServiceid(long _v_) { // serviceid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActid(int _v_) { // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作
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
			return InstanceNpcServ.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceNpcServ.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceNpcServ.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceNpcServ.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceNpcServ.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceNpcServ.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceNpcServ.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceNpcServ.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceNpcServ.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceNpcServ {
		private long serviceid; // serviceid
		private int actid; // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.InstanceNpcServ _o1_) {
			if (_o1_ instanceof InstanceNpcServ) assign((InstanceNpcServ)_o1_);
			else if (_o1_ instanceof InstanceNpcServ.Data) assign((InstanceNpcServ.Data)_o1_);
			else if (_o1_ instanceof InstanceNpcServ.Const) assign(((InstanceNpcServ.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceNpcServ _o_) {
			serviceid = _o_.serviceid;
			actid = _o_.actid;
		}

		private void assign(InstanceNpcServ.Data _o_) {
			serviceid = _o_.serviceid;
			actid = _o_.actid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(serviceid);
			_os_.marshal(actid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			serviceid = _os_.unmarshal_long();
			actid = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.InstanceNpcServ copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceNpcServ toData() {
			return new Data(this);
		}

		public xbean.InstanceNpcServ toBean() {
			return new InstanceNpcServ(this, null, null);
		}

		@Override
		public xbean.InstanceNpcServ toDataIf() {
			return this;
		}

		public xbean.InstanceNpcServ toBeanIf() {
			return new InstanceNpcServ(this, null, null);
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
		public long getServiceid() { // serviceid
			return serviceid;
		}

		@Override
		public int getActid() { // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作
			return actid;
		}

		@Override
		public void setServiceid(long _v_) { // serviceid
			serviceid = _v_;
		}

		@Override
		public void setActid(int _v_) { // 0为隐藏，大于0则为某个服务动作的ID，显示出来肯定得有服务动作
			actid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceNpcServ.Data)) return false;
			InstanceNpcServ.Data _o_ = (InstanceNpcServ.Data) _o1_;
			if (serviceid != _o_.serviceid) return false;
			if (actid != _o_.actid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += serviceid;
			_h_ += actid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(serviceid);
			_sb_.append(",");
			_sb_.append(actid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

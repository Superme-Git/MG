
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceNpc extends mkdb.XBean implements xbean.InstanceNpc {
	private int npcbaseid; // npcbaseId
	private int state; // 状态 1 显示 2 隐藏

	@Override
	public void _reset_unsafe_() {
		npcbaseid = 0;
		state = 0;
	}

	InstanceNpc(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public InstanceNpc() {
		this(0, null, null);
	}

	public InstanceNpc(InstanceNpc _o_) {
		this(_o_, null, null);
	}

	InstanceNpc(xbean.InstanceNpc _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceNpc) assign((InstanceNpc)_o1_);
		else if (_o1_ instanceof InstanceNpc.Data) assign((InstanceNpc.Data)_o1_);
		else if (_o1_ instanceof InstanceNpc.Const) assign(((InstanceNpc.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceNpc _o_) {
		_o_._xdb_verify_unsafe_();
		npcbaseid = _o_.npcbaseid;
		state = _o_.state;
	}

	private void assign(InstanceNpc.Data _o_) {
		npcbaseid = _o_.npcbaseid;
		state = _o_.state;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(npcbaseid);
		_os_.marshal(state);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		npcbaseid = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.InstanceNpc copy() {
		_xdb_verify_unsafe_();
		return new InstanceNpc(this);
	}

	@Override
	public xbean.InstanceNpc toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceNpc toBean() {
		_xdb_verify_unsafe_();
		return new InstanceNpc(this); // same as copy()
	}

	@Override
	public xbean.InstanceNpc toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceNpc toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getNpcbaseid() { // npcbaseId
		_xdb_verify_unsafe_();
		return npcbaseid;
	}

	@Override
	public int getState() { // 状态 1 显示 2 隐藏
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public void setNpcbaseid(int _v_) { // npcbaseId
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npcbaseid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, npcbaseid) {
					public void rollback() { npcbaseid = _xdb_saved; }
				};}});
		npcbaseid = _v_;
	}

	@Override
	public void setState(int _v_) { // 状态 1 显示 2 隐藏
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceNpc _o_ = null;
		if ( _o1_ instanceof InstanceNpc ) _o_ = (InstanceNpc)_o1_;
		else if ( _o1_ instanceof InstanceNpc.Const ) _o_ = ((InstanceNpc.Const)_o1_).nThis();
		else return false;
		if (npcbaseid != _o_.npcbaseid) return false;
		if (state != _o_.state) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += npcbaseid;
		_h_ += state;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcbaseid);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npcbaseid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		return lb;
	}

	private class Const implements xbean.InstanceNpc {
		InstanceNpc nThis() {
			return InstanceNpc.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceNpc copy() {
			return InstanceNpc.this.copy();
		}

		@Override
		public xbean.InstanceNpc toData() {
			return InstanceNpc.this.toData();
		}

		public xbean.InstanceNpc toBean() {
			return InstanceNpc.this.toBean();
		}

		@Override
		public xbean.InstanceNpc toDataIf() {
			return InstanceNpc.this.toDataIf();
		}

		public xbean.InstanceNpc toBeanIf() {
			return InstanceNpc.this.toBeanIf();
		}

		@Override
		public int getNpcbaseid() { // npcbaseId
			_xdb_verify_unsafe_();
			return npcbaseid;
		}

		@Override
		public int getState() { // 状态 1 显示 2 隐藏
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public void setNpcbaseid(int _v_) { // npcbaseId
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 状态 1 显示 2 隐藏
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
			return InstanceNpc.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceNpc.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceNpc.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceNpc.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceNpc.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceNpc.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceNpc.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceNpc.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceNpc.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceNpc {
		private int npcbaseid; // npcbaseId
		private int state; // 状态 1 显示 2 隐藏

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.InstanceNpc _o1_) {
			if (_o1_ instanceof InstanceNpc) assign((InstanceNpc)_o1_);
			else if (_o1_ instanceof InstanceNpc.Data) assign((InstanceNpc.Data)_o1_);
			else if (_o1_ instanceof InstanceNpc.Const) assign(((InstanceNpc.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceNpc _o_) {
			npcbaseid = _o_.npcbaseid;
			state = _o_.state;
		}

		private void assign(InstanceNpc.Data _o_) {
			npcbaseid = _o_.npcbaseid;
			state = _o_.state;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(npcbaseid);
			_os_.marshal(state);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			npcbaseid = _os_.unmarshal_int();
			state = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.InstanceNpc copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceNpc toData() {
			return new Data(this);
		}

		public xbean.InstanceNpc toBean() {
			return new InstanceNpc(this, null, null);
		}

		@Override
		public xbean.InstanceNpc toDataIf() {
			return this;
		}

		public xbean.InstanceNpc toBeanIf() {
			return new InstanceNpc(this, null, null);
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
		public int getNpcbaseid() { // npcbaseId
			return npcbaseid;
		}

		@Override
		public int getState() { // 状态 1 显示 2 隐藏
			return state;
		}

		@Override
		public void setNpcbaseid(int _v_) { // npcbaseId
			npcbaseid = _v_;
		}

		@Override
		public void setState(int _v_) { // 状态 1 显示 2 隐藏
			state = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceNpc.Data)) return false;
			InstanceNpc.Data _o_ = (InstanceNpc.Data) _o1_;
			if (npcbaseid != _o_.npcbaseid) return false;
			if (state != _o_.state) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += npcbaseid;
			_h_ += state;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(npcbaseid);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

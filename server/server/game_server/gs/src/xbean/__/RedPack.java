
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RedPack extends mkdb.XBean implements xbean.RedPack {
	private long worldredpack; // 

	@Override
	public void _reset_unsafe_() {
		worldredpack = 0L;
	}

	RedPack(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RedPack() {
		this(0, null, null);
	}

	public RedPack(RedPack _o_) {
		this(_o_, null, null);
	}

	RedPack(xbean.RedPack _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RedPack) assign((RedPack)_o1_);
		else if (_o1_ instanceof RedPack.Data) assign((RedPack.Data)_o1_);
		else if (_o1_ instanceof RedPack.Const) assign(((RedPack.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RedPack _o_) {
		_o_._xdb_verify_unsafe_();
		worldredpack = _o_.worldredpack;
	}

	private void assign(RedPack.Data _o_) {
		worldredpack = _o_.worldredpack;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(worldredpack);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		worldredpack = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RedPack copy() {
		_xdb_verify_unsafe_();
		return new RedPack(this);
	}

	@Override
	public xbean.RedPack toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPack toBean() {
		_xdb_verify_unsafe_();
		return new RedPack(this); // same as copy()
	}

	@Override
	public xbean.RedPack toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPack toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getWorldredpack() { // 
		_xdb_verify_unsafe_();
		return worldredpack;
	}

	@Override
	public void setWorldredpack(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "worldredpack") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, worldredpack) {
					public void rollback() { worldredpack = _xdb_saved; }
				};}});
		worldredpack = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RedPack _o_ = null;
		if ( _o1_ instanceof RedPack ) _o_ = (RedPack)_o1_;
		else if ( _o1_ instanceof RedPack.Const ) _o_ = ((RedPack.Const)_o1_).nThis();
		else return false;
		if (worldredpack != _o_.worldredpack) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += worldredpack;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(worldredpack);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("worldredpack"));
		return lb;
	}

	private class Const implements xbean.RedPack {
		RedPack nThis() {
			return RedPack.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RedPack copy() {
			return RedPack.this.copy();
		}

		@Override
		public xbean.RedPack toData() {
			return RedPack.this.toData();
		}

		public xbean.RedPack toBean() {
			return RedPack.this.toBean();
		}

		@Override
		public xbean.RedPack toDataIf() {
			return RedPack.this.toDataIf();
		}

		public xbean.RedPack toBeanIf() {
			return RedPack.this.toBeanIf();
		}

		@Override
		public long getWorldredpack() { // 
			_xdb_verify_unsafe_();
			return worldredpack;
		}

		@Override
		public void setWorldredpack(long _v_) { // 
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
			return RedPack.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RedPack.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RedPack.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RedPack.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RedPack.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RedPack.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RedPack.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RedPack.this.hashCode();
		}

		@Override
		public String toString() {
			return RedPack.this.toString();
		}

	}

	public static final class Data implements xbean.RedPack {
		private long worldredpack; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RedPack _o1_) {
			if (_o1_ instanceof RedPack) assign((RedPack)_o1_);
			else if (_o1_ instanceof RedPack.Data) assign((RedPack.Data)_o1_);
			else if (_o1_ instanceof RedPack.Const) assign(((RedPack.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RedPack _o_) {
			worldredpack = _o_.worldredpack;
		}

		private void assign(RedPack.Data _o_) {
			worldredpack = _o_.worldredpack;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(worldredpack);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			worldredpack = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RedPack copy() {
			return new Data(this);
		}

		@Override
		public xbean.RedPack toData() {
			return new Data(this);
		}

		public xbean.RedPack toBean() {
			return new RedPack(this, null, null);
		}

		@Override
		public xbean.RedPack toDataIf() {
			return this;
		}

		public xbean.RedPack toBeanIf() {
			return new RedPack(this, null, null);
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
		public long getWorldredpack() { // 
			return worldredpack;
		}

		@Override
		public void setWorldredpack(long _v_) { // 
			worldredpack = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RedPack.Data)) return false;
			RedPack.Data _o_ = (RedPack.Data) _o1_;
			if (worldredpack != _o_.worldredpack) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += worldredpack;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(worldredpack);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

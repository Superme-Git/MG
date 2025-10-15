
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class GetRolesCallBackInst extends mkdb.XBean implements xbean.GetRolesCallBackInst {
	private fire.msp.IGetRolesCallBack callback; // 

	@Override
	public void _reset_unsafe_() {
		callback = null;
	}

	GetRolesCallBackInst(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		callback = null;
	}

	public GetRolesCallBackInst() {
		this(0, null, null);
	}

	public GetRolesCallBackInst(GetRolesCallBackInst _o_) {
		this(_o_, null, null);
	}

	GetRolesCallBackInst(xbean.GetRolesCallBackInst _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public xbean.GetRolesCallBackInst copy() {
		_xdb_verify_unsafe_();
		return new GetRolesCallBackInst(this);
	}

	@Override
	public xbean.GetRolesCallBackInst toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GetRolesCallBackInst toBean() {
		_xdb_verify_unsafe_();
		return new GetRolesCallBackInst(this); // same as copy()
	}

	@Override
	public xbean.GetRolesCallBackInst toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GetRolesCallBackInst toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public fire.msp.IGetRolesCallBack getCallback() { // 
		_xdb_verify_unsafe_();
		return callback;
	}

	@Override
	public void setCallback(fire.msp.IGetRolesCallBack _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "callback") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<fire.msp.IGetRolesCallBack>(this, callback) {
					public void rollback() { callback = _xdb_saved; }
			}; }});
		callback = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		GetRolesCallBackInst _o_ = null;
		if ( _o1_ instanceof GetRolesCallBackInst ) _o_ = (GetRolesCallBackInst)_o1_;
		else if ( _o1_ instanceof GetRolesCallBackInst.Const ) _o_ = ((GetRolesCallBackInst.Const)_o1_).nThis();
		else return false;
		if ((null == callback && null != _o_.callback) || (null != callback && null == _o_.callback) || (null != callback && null != _o_.callback && !callback.equals(_o_.callback))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (callback == null ? 0 : callback.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(callback);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("callback"));
		return lb;
	}

	private class Const implements xbean.GetRolesCallBackInst {
		GetRolesCallBackInst nThis() {
			return GetRolesCallBackInst.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.GetRolesCallBackInst copy() {
			return GetRolesCallBackInst.this.copy();
		}

		@Override
		public xbean.GetRolesCallBackInst toData() {
			return GetRolesCallBackInst.this.toData();
		}

		public xbean.GetRolesCallBackInst toBean() {
			return GetRolesCallBackInst.this.toBean();
		}

		@Override
		public xbean.GetRolesCallBackInst toDataIf() {
			return GetRolesCallBackInst.this.toDataIf();
		}

		public xbean.GetRolesCallBackInst toBeanIf() {
			return GetRolesCallBackInst.this.toBeanIf();
		}

		@Override
		public fire.msp.IGetRolesCallBack getCallback() { // 
			_xdb_verify_unsafe_();
			return callback;
		}

		@Override
		public void setCallback(fire.msp.IGetRolesCallBack _v_) { // 
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
			return GetRolesCallBackInst.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return GetRolesCallBackInst.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return GetRolesCallBackInst.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return GetRolesCallBackInst.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return GetRolesCallBackInst.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return GetRolesCallBackInst.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return GetRolesCallBackInst.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return GetRolesCallBackInst.this.hashCode();
		}

		@Override
		public String toString() {
			return GetRolesCallBackInst.this.toString();
		}

	}

	public static final class Data implements xbean.GetRolesCallBackInst {
		private fire.msp.IGetRolesCallBack callback; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			callback = null;
		}

		Data(xbean.GetRolesCallBackInst _o1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.GetRolesCallBackInst copy() {
			return new Data(this);
		}

		@Override
		public xbean.GetRolesCallBackInst toData() {
			return new Data(this);
		}

		public xbean.GetRolesCallBackInst toBean() {
			return new GetRolesCallBackInst(this, null, null);
		}

		@Override
		public xbean.GetRolesCallBackInst toDataIf() {
			return this;
		}

		public xbean.GetRolesCallBackInst toBeanIf() {
			return new GetRolesCallBackInst(this, null, null);
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
		public fire.msp.IGetRolesCallBack getCallback() { // 
			return callback;
		}

		@Override
		public void setCallback(fire.msp.IGetRolesCallBack _v_) { // 
			callback = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof GetRolesCallBackInst.Data)) return false;
			GetRolesCallBackInst.Data _o_ = (GetRolesCallBackInst.Data) _o1_;
			if ((null == callback && null != _o_.callback) || (null != callback && null == _o_.callback) || (null != callback && null != _o_.callback && !callback.equals(_o_.callback))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (callback == null ? 0 : callback.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(callback);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

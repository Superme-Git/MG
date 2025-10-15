
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CreateNpcExecType extends mkdb.XBean implements xbean.CreateNpcExecType {
	private fire.msp.GMCreateNpcExec execinstance; // 

	@Override
	public void _reset_unsafe_() {
		execinstance = null;
	}

	CreateNpcExecType(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		execinstance = null;
	}

	public CreateNpcExecType() {
		this(0, null, null);
	}

	public CreateNpcExecType(CreateNpcExecType _o_) {
		this(_o_, null, null);
	}

	CreateNpcExecType(xbean.CreateNpcExecType _o1_, mkdb.XBean _xp_, String _vn_) {
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
	public xbean.CreateNpcExecType copy() {
		_xdb_verify_unsafe_();
		return new CreateNpcExecType(this);
	}

	@Override
	public xbean.CreateNpcExecType toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CreateNpcExecType toBean() {
		_xdb_verify_unsafe_();
		return new CreateNpcExecType(this); // same as copy()
	}

	@Override
	public xbean.CreateNpcExecType toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CreateNpcExecType toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public fire.msp.GMCreateNpcExec getExecinstance() { // 
		_xdb_verify_unsafe_();
		return execinstance;
	}

	@Override
	public void setExecinstance(fire.msp.GMCreateNpcExec _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "execinstance") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<fire.msp.GMCreateNpcExec>(this, execinstance) {
					public void rollback() { execinstance = _xdb_saved; }
			}; }});
		execinstance = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CreateNpcExecType _o_ = null;
		if ( _o1_ instanceof CreateNpcExecType ) _o_ = (CreateNpcExecType)_o1_;
		else if ( _o1_ instanceof CreateNpcExecType.Const ) _o_ = ((CreateNpcExecType.Const)_o1_).nThis();
		else return false;
		if ((null == execinstance && null != _o_.execinstance) || (null != execinstance && null == _o_.execinstance) || (null != execinstance && null != _o_.execinstance && !execinstance.equals(_o_.execinstance))) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += (execinstance == null ? 0 : execinstance.hashCode());
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(execinstance);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("execinstance"));
		return lb;
	}

	private class Const implements xbean.CreateNpcExecType {
		CreateNpcExecType nThis() {
			return CreateNpcExecType.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CreateNpcExecType copy() {
			return CreateNpcExecType.this.copy();
		}

		@Override
		public xbean.CreateNpcExecType toData() {
			return CreateNpcExecType.this.toData();
		}

		public xbean.CreateNpcExecType toBean() {
			return CreateNpcExecType.this.toBean();
		}

		@Override
		public xbean.CreateNpcExecType toDataIf() {
			return CreateNpcExecType.this.toDataIf();
		}

		public xbean.CreateNpcExecType toBeanIf() {
			return CreateNpcExecType.this.toBeanIf();
		}

		@Override
		public fire.msp.GMCreateNpcExec getExecinstance() { // 
			_xdb_verify_unsafe_();
			return execinstance;
		}

		@Override
		public void setExecinstance(fire.msp.GMCreateNpcExec _v_) { // 
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
			return CreateNpcExecType.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CreateNpcExecType.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CreateNpcExecType.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CreateNpcExecType.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CreateNpcExecType.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CreateNpcExecType.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CreateNpcExecType.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CreateNpcExecType.this.hashCode();
		}

		@Override
		public String toString() {
			return CreateNpcExecType.this.toString();
		}

	}

	public static final class Data implements xbean.CreateNpcExecType {
		private fire.msp.GMCreateNpcExec execinstance; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			execinstance = null;
		}

		Data(xbean.CreateNpcExecType _o1_) {
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
		public xbean.CreateNpcExecType copy() {
			return new Data(this);
		}

		@Override
		public xbean.CreateNpcExecType toData() {
			return new Data(this);
		}

		public xbean.CreateNpcExecType toBean() {
			return new CreateNpcExecType(this, null, null);
		}

		@Override
		public xbean.CreateNpcExecType toDataIf() {
			return this;
		}

		public xbean.CreateNpcExecType toBeanIf() {
			return new CreateNpcExecType(this, null, null);
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
		public fire.msp.GMCreateNpcExec getExecinstance() { // 
			return execinstance;
		}

		@Override
		public void setExecinstance(fire.msp.GMCreateNpcExec _v_) { // 
			execinstance = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CreateNpcExecType.Data)) return false;
			CreateNpcExecType.Data _o_ = (CreateNpcExecType.Data) _o1_;
			if ((null == execinstance && null != _o_.execinstance) || (null != execinstance && null == _o_.execinstance) || (null != execinstance && null != _o_.execinstance && !execinstance.equals(_o_.execinstance))) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += (execinstance == null ? 0 : execinstance.hashCode());
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(execinstance);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

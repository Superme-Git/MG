
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SingleCompensationCond extends mkdb.XBean implements xbean.SingleCompensationCond {
	private int condid; // 条件的id
	private java.util.LinkedList<String> condparams; // 条件的参数

	@Override
	public void _reset_unsafe_() {
		condid = 0;
		condparams.clear();
	}

	SingleCompensationCond(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		condparams = new java.util.LinkedList<String>();
	}

	public SingleCompensationCond() {
		this(0, null, null);
	}

	public SingleCompensationCond(SingleCompensationCond _o_) {
		this(_o_, null, null);
	}

	SingleCompensationCond(xbean.SingleCompensationCond _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SingleCompensationCond) assign((SingleCompensationCond)_o1_);
		else if (_o1_ instanceof SingleCompensationCond.Data) assign((SingleCompensationCond.Data)_o1_);
		else if (_o1_ instanceof SingleCompensationCond.Const) assign(((SingleCompensationCond.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SingleCompensationCond _o_) {
		_o_._xdb_verify_unsafe_();
		condid = _o_.condid;
		condparams = new java.util.LinkedList<String>();
		condparams.addAll(_o_.condparams);
	}

	private void assign(SingleCompensationCond.Data _o_) {
		condid = _o_.condid;
		condparams = new java.util.LinkedList<String>();
		condparams.addAll(_o_.condparams);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(condid);
		_os_.compact_uint32(condparams.size());
		for (String _v_ : condparams) {
			_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		condid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			String _v_ = "";
			_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			condparams.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.SingleCompensationCond copy() {
		_xdb_verify_unsafe_();
		return new SingleCompensationCond(this);
	}

	@Override
	public xbean.SingleCompensationCond toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SingleCompensationCond toBean() {
		_xdb_verify_unsafe_();
		return new SingleCompensationCond(this); // same as copy()
	}

	@Override
	public xbean.SingleCompensationCond toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SingleCompensationCond toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getCondid() { // 条件的id
		_xdb_verify_unsafe_();
		return condid;
	}

	@Override
	public java.util.List<String> getCondparams() { // 条件的参数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "condparams"), condparams);
	}

	public java.util.List<String> getCondparamsAsData() { // 条件的参数
		_xdb_verify_unsafe_();
		java.util.List<String> condparams;
		SingleCompensationCond _o_ = this;
		condparams = new java.util.LinkedList<String>();
		condparams.addAll(_o_.condparams);
		return condparams;
	}

	@Override
	public void setCondid(int _v_) { // 条件的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "condid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, condid) {
					public void rollback() { condid = _xdb_saved; }
				};}});
		condid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SingleCompensationCond _o_ = null;
		if ( _o1_ instanceof SingleCompensationCond ) _o_ = (SingleCompensationCond)_o1_;
		else if ( _o1_ instanceof SingleCompensationCond.Const ) _o_ = ((SingleCompensationCond.Const)_o1_).nThis();
		else return false;
		if (condid != _o_.condid) return false;
		if (!condparams.equals(_o_.condparams)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += condid;
		_h_ += condparams.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(condid);
		_sb_.append(",");
		_sb_.append(condparams);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("condid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("condparams"));
		return lb;
	}

	private class Const implements xbean.SingleCompensationCond {
		SingleCompensationCond nThis() {
			return SingleCompensationCond.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SingleCompensationCond copy() {
			return SingleCompensationCond.this.copy();
		}

		@Override
		public xbean.SingleCompensationCond toData() {
			return SingleCompensationCond.this.toData();
		}

		public xbean.SingleCompensationCond toBean() {
			return SingleCompensationCond.this.toBean();
		}

		@Override
		public xbean.SingleCompensationCond toDataIf() {
			return SingleCompensationCond.this.toDataIf();
		}

		public xbean.SingleCompensationCond toBeanIf() {
			return SingleCompensationCond.this.toBeanIf();
		}

		@Override
		public int getCondid() { // 条件的id
			_xdb_verify_unsafe_();
			return condid;
		}

		@Override
		public java.util.List<String> getCondparams() { // 条件的参数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(condparams);
		}

		public java.util.List<String> getCondparamsAsData() { // 条件的参数
			_xdb_verify_unsafe_();
			java.util.List<String> condparams;
			SingleCompensationCond _o_ = SingleCompensationCond.this;
		condparams = new java.util.LinkedList<String>();
		condparams.addAll(_o_.condparams);
			return condparams;
		}

		@Override
		public void setCondid(int _v_) { // 条件的id
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
			return SingleCompensationCond.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SingleCompensationCond.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SingleCompensationCond.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SingleCompensationCond.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SingleCompensationCond.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SingleCompensationCond.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SingleCompensationCond.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SingleCompensationCond.this.hashCode();
		}

		@Override
		public String toString() {
			return SingleCompensationCond.this.toString();
		}

	}

	public static final class Data implements xbean.SingleCompensationCond {
		private int condid; // 条件的id
		private java.util.LinkedList<String> condparams; // 条件的参数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			condparams = new java.util.LinkedList<String>();
		}

		Data(xbean.SingleCompensationCond _o1_) {
			if (_o1_ instanceof SingleCompensationCond) assign((SingleCompensationCond)_o1_);
			else if (_o1_ instanceof SingleCompensationCond.Data) assign((SingleCompensationCond.Data)_o1_);
			else if (_o1_ instanceof SingleCompensationCond.Const) assign(((SingleCompensationCond.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SingleCompensationCond _o_) {
			condid = _o_.condid;
			condparams = new java.util.LinkedList<String>();
			condparams.addAll(_o_.condparams);
		}

		private void assign(SingleCompensationCond.Data _o_) {
			condid = _o_.condid;
			condparams = new java.util.LinkedList<String>();
			condparams.addAll(_o_.condparams);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(condid);
			_os_.compact_uint32(condparams.size());
			for (String _v_ : condparams) {
				_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			condid = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				condparams.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.SingleCompensationCond copy() {
			return new Data(this);
		}

		@Override
		public xbean.SingleCompensationCond toData() {
			return new Data(this);
		}

		public xbean.SingleCompensationCond toBean() {
			return new SingleCompensationCond(this, null, null);
		}

		@Override
		public xbean.SingleCompensationCond toDataIf() {
			return this;
		}

		public xbean.SingleCompensationCond toBeanIf() {
			return new SingleCompensationCond(this, null, null);
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
		public int getCondid() { // 条件的id
			return condid;
		}

		@Override
		public java.util.List<String> getCondparams() { // 条件的参数
			return condparams;
		}

		@Override
		public java.util.List<String> getCondparamsAsData() { // 条件的参数
			return condparams;
		}

		@Override
		public void setCondid(int _v_) { // 条件的id
			condid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SingleCompensationCond.Data)) return false;
			SingleCompensationCond.Data _o_ = (SingleCompensationCond.Data) _o1_;
			if (condid != _o_.condid) return false;
			if (!condparams.equals(_o_.condparams)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += condid;
			_h_ += condparams.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(condid);
			_sb_.append(",");
			_sb_.append(condparams);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

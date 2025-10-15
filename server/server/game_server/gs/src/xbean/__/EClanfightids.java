
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EClanfightids extends mkdb.XBean implements xbean.EClanfightids {
	private java.util.LinkedList<Long> ids; // 本周公会战id by changhao

	@Override
	public void _reset_unsafe_() {
		ids.clear();
	}

	EClanfightids(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		ids = new java.util.LinkedList<Long>();
	}

	public EClanfightids() {
		this(0, null, null);
	}

	public EClanfightids(EClanfightids _o_) {
		this(_o_, null, null);
	}

	EClanfightids(xbean.EClanfightids _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EClanfightids) assign((EClanfightids)_o1_);
		else if (_o1_ instanceof EClanfightids.Data) assign((EClanfightids.Data)_o1_);
		else if (_o1_ instanceof EClanfightids.Const) assign(((EClanfightids.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EClanfightids _o_) {
		_o_._xdb_verify_unsafe_();
		ids = new java.util.LinkedList<Long>();
		ids.addAll(_o_.ids);
	}

	private void assign(EClanfightids.Data _o_) {
		ids = new java.util.LinkedList<Long>();
		ids.addAll(_o_.ids);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(ids.size());
		for (Long _v_ : ids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			ids.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.EClanfightids copy() {
		_xdb_verify_unsafe_();
		return new EClanfightids(this);
	}

	@Override
	public xbean.EClanfightids toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EClanfightids toBean() {
		_xdb_verify_unsafe_();
		return new EClanfightids(this); // same as copy()
	}

	@Override
	public xbean.EClanfightids toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EClanfightids toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Long> getIds() { // 本周公会战id by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "ids"), ids);
	}

	public java.util.List<Long> getIdsAsData() { // 本周公会战id by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> ids;
		EClanfightids _o_ = this;
		ids = new java.util.LinkedList<Long>();
		ids.addAll(_o_.ids);
		return ids;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EClanfightids _o_ = null;
		if ( _o1_ instanceof EClanfightids ) _o_ = (EClanfightids)_o1_;
		else if ( _o1_ instanceof EClanfightids.Const ) _o_ = ((EClanfightids.Const)_o1_).nThis();
		else return false;
		if (!ids.equals(_o_.ids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += ids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ids"));
		return lb;
	}

	private class Const implements xbean.EClanfightids {
		EClanfightids nThis() {
			return EClanfightids.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EClanfightids copy() {
			return EClanfightids.this.copy();
		}

		@Override
		public xbean.EClanfightids toData() {
			return EClanfightids.this.toData();
		}

		public xbean.EClanfightids toBean() {
			return EClanfightids.this.toBean();
		}

		@Override
		public xbean.EClanfightids toDataIf() {
			return EClanfightids.this.toDataIf();
		}

		public xbean.EClanfightids toBeanIf() {
			return EClanfightids.this.toBeanIf();
		}

		@Override
		public java.util.List<Long> getIds() { // 本周公会战id by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(ids);
		}

		public java.util.List<Long> getIdsAsData() { // 本周公会战id by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> ids;
			EClanfightids _o_ = EClanfightids.this;
		ids = new java.util.LinkedList<Long>();
		ids.addAll(_o_.ids);
			return ids;
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
			return EClanfightids.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EClanfightids.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EClanfightids.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EClanfightids.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EClanfightids.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EClanfightids.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EClanfightids.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EClanfightids.this.hashCode();
		}

		@Override
		public String toString() {
			return EClanfightids.this.toString();
		}

	}

	public static final class Data implements xbean.EClanfightids {
		private java.util.LinkedList<Long> ids; // 本周公会战id by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			ids = new java.util.LinkedList<Long>();
		}

		Data(xbean.EClanfightids _o1_) {
			if (_o1_ instanceof EClanfightids) assign((EClanfightids)_o1_);
			else if (_o1_ instanceof EClanfightids.Data) assign((EClanfightids.Data)_o1_);
			else if (_o1_ instanceof EClanfightids.Const) assign(((EClanfightids.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EClanfightids _o_) {
			ids = new java.util.LinkedList<Long>();
			ids.addAll(_o_.ids);
		}

		private void assign(EClanfightids.Data _o_) {
			ids = new java.util.LinkedList<Long>();
			ids.addAll(_o_.ids);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(ids.size());
			for (Long _v_ : ids) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				ids.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.EClanfightids copy() {
			return new Data(this);
		}

		@Override
		public xbean.EClanfightids toData() {
			return new Data(this);
		}

		public xbean.EClanfightids toBean() {
			return new EClanfightids(this, null, null);
		}

		@Override
		public xbean.EClanfightids toDataIf() {
			return this;
		}

		public xbean.EClanfightids toBeanIf() {
			return new EClanfightids(this, null, null);
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
		public java.util.List<Long> getIds() { // 本周公会战id by changhao
			return ids;
		}

		@Override
		public java.util.List<Long> getIdsAsData() { // 本周公会战id by changhao
			return ids;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EClanfightids.Data)) return false;
			EClanfightids.Data _o_ = (EClanfightids.Data) _o1_;
			if (!ids.equals(_o_.ids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += ids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(ids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Protectors extends mkdb.XBean implements xbean.Protectors {
	private java.util.LinkedList<Integer> protectorlist; // 

	@Override
	public void _reset_unsafe_() {
		protectorlist.clear();
	}

	Protectors(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		protectorlist = new java.util.LinkedList<Integer>();
	}

	public Protectors() {
		this(0, null, null);
	}

	public Protectors(Protectors _o_) {
		this(_o_, null, null);
	}

	Protectors(xbean.Protectors _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Protectors) assign((Protectors)_o1_);
		else if (_o1_ instanceof Protectors.Data) assign((Protectors.Data)_o1_);
		else if (_o1_ instanceof Protectors.Const) assign(((Protectors.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Protectors _o_) {
		_o_._xdb_verify_unsafe_();
		protectorlist = new java.util.LinkedList<Integer>();
		protectorlist.addAll(_o_.protectorlist);
	}

	private void assign(Protectors.Data _o_) {
		protectorlist = new java.util.LinkedList<Integer>();
		protectorlist.addAll(_o_.protectorlist);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(protectorlist.size());
		for (Integer _v_ : protectorlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			protectorlist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.Protectors copy() {
		_xdb_verify_unsafe_();
		return new Protectors(this);
	}

	@Override
	public xbean.Protectors toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Protectors toBean() {
		_xdb_verify_unsafe_();
		return new Protectors(this); // same as copy()
	}

	@Override
	public xbean.Protectors toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Protectors toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Integer> getProtectorlist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "protectorlist"), protectorlist);
	}

	public java.util.List<Integer> getProtectorlistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Integer> protectorlist;
		Protectors _o_ = this;
		protectorlist = new java.util.LinkedList<Integer>();
		protectorlist.addAll(_o_.protectorlist);
		return protectorlist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Protectors _o_ = null;
		if ( _o1_ instanceof Protectors ) _o_ = (Protectors)_o1_;
		else if ( _o1_ instanceof Protectors.Const ) _o_ = ((Protectors.Const)_o1_).nThis();
		else return false;
		if (!protectorlist.equals(_o_.protectorlist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += protectorlist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(protectorlist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("protectorlist"));
		return lb;
	}

	private class Const implements xbean.Protectors {
		Protectors nThis() {
			return Protectors.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Protectors copy() {
			return Protectors.this.copy();
		}

		@Override
		public xbean.Protectors toData() {
			return Protectors.this.toData();
		}

		public xbean.Protectors toBean() {
			return Protectors.this.toBean();
		}

		@Override
		public xbean.Protectors toDataIf() {
			return Protectors.this.toDataIf();
		}

		public xbean.Protectors toBeanIf() {
			return Protectors.this.toBeanIf();
		}

		@Override
		public java.util.List<Integer> getProtectorlist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(protectorlist);
		}

		public java.util.List<Integer> getProtectorlistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Integer> protectorlist;
			Protectors _o_ = Protectors.this;
		protectorlist = new java.util.LinkedList<Integer>();
		protectorlist.addAll(_o_.protectorlist);
			return protectorlist;
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
			return Protectors.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Protectors.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Protectors.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Protectors.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Protectors.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Protectors.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Protectors.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Protectors.this.hashCode();
		}

		@Override
		public String toString() {
			return Protectors.this.toString();
		}

	}

	public static final class Data implements xbean.Protectors {
		private java.util.LinkedList<Integer> protectorlist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			protectorlist = new java.util.LinkedList<Integer>();
		}

		Data(xbean.Protectors _o1_) {
			if (_o1_ instanceof Protectors) assign((Protectors)_o1_);
			else if (_o1_ instanceof Protectors.Data) assign((Protectors.Data)_o1_);
			else if (_o1_ instanceof Protectors.Const) assign(((Protectors.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Protectors _o_) {
			protectorlist = new java.util.LinkedList<Integer>();
			protectorlist.addAll(_o_.protectorlist);
		}

		private void assign(Protectors.Data _o_) {
			protectorlist = new java.util.LinkedList<Integer>();
			protectorlist.addAll(_o_.protectorlist);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(protectorlist.size());
			for (Integer _v_ : protectorlist) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				protectorlist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.Protectors copy() {
			return new Data(this);
		}

		@Override
		public xbean.Protectors toData() {
			return new Data(this);
		}

		public xbean.Protectors toBean() {
			return new Protectors(this, null, null);
		}

		@Override
		public xbean.Protectors toDataIf() {
			return this;
		}

		public xbean.Protectors toBeanIf() {
			return new Protectors(this, null, null);
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
		public java.util.List<Integer> getProtectorlist() { // 
			return protectorlist;
		}

		@Override
		public java.util.List<Integer> getProtectorlistAsData() { // 
			return protectorlist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Protectors.Data)) return false;
			Protectors.Data _o_ = (Protectors.Data) _o1_;
			if (!protectorlist.equals(_o_.protectorlist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += protectorlist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(protectorlist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

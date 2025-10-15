
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class DebugFlag extends mkdb.XBean implements xbean.DebugFlag {
	private java.util.LinkedList<Integer> debugs; // 不在list中的为非调试状态

	@Override
	public void _reset_unsafe_() {
		debugs.clear();
	}

	DebugFlag(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		debugs = new java.util.LinkedList<Integer>();
	}

	public DebugFlag() {
		this(0, null, null);
	}

	public DebugFlag(DebugFlag _o_) {
		this(_o_, null, null);
	}

	DebugFlag(xbean.DebugFlag _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof DebugFlag) assign((DebugFlag)_o1_);
		else if (_o1_ instanceof DebugFlag.Data) assign((DebugFlag.Data)_o1_);
		else if (_o1_ instanceof DebugFlag.Const) assign(((DebugFlag.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(DebugFlag _o_) {
		_o_._xdb_verify_unsafe_();
		debugs = new java.util.LinkedList<Integer>();
		debugs.addAll(_o_.debugs);
	}

	private void assign(DebugFlag.Data _o_) {
		debugs = new java.util.LinkedList<Integer>();
		debugs.addAll(_o_.debugs);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(debugs.size());
		for (Integer _v_ : debugs) {
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
			debugs.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.DebugFlag copy() {
		_xdb_verify_unsafe_();
		return new DebugFlag(this);
	}

	@Override
	public xbean.DebugFlag toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DebugFlag toBean() {
		_xdb_verify_unsafe_();
		return new DebugFlag(this); // same as copy()
	}

	@Override
	public xbean.DebugFlag toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DebugFlag toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Integer> getDebugs() { // 不在list中的为非调试状态
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "debugs"), debugs);
	}

	public java.util.List<Integer> getDebugsAsData() { // 不在list中的为非调试状态
		_xdb_verify_unsafe_();
		java.util.List<Integer> debugs;
		DebugFlag _o_ = this;
		debugs = new java.util.LinkedList<Integer>();
		debugs.addAll(_o_.debugs);
		return debugs;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		DebugFlag _o_ = null;
		if ( _o1_ instanceof DebugFlag ) _o_ = (DebugFlag)_o1_;
		else if ( _o1_ instanceof DebugFlag.Const ) _o_ = ((DebugFlag.Const)_o1_).nThis();
		else return false;
		if (!debugs.equals(_o_.debugs)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += debugs.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(debugs);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("debugs"));
		return lb;
	}

	private class Const implements xbean.DebugFlag {
		DebugFlag nThis() {
			return DebugFlag.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.DebugFlag copy() {
			return DebugFlag.this.copy();
		}

		@Override
		public xbean.DebugFlag toData() {
			return DebugFlag.this.toData();
		}

		public xbean.DebugFlag toBean() {
			return DebugFlag.this.toBean();
		}

		@Override
		public xbean.DebugFlag toDataIf() {
			return DebugFlag.this.toDataIf();
		}

		public xbean.DebugFlag toBeanIf() {
			return DebugFlag.this.toBeanIf();
		}

		@Override
		public java.util.List<Integer> getDebugs() { // 不在list中的为非调试状态
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(debugs);
		}

		public java.util.List<Integer> getDebugsAsData() { // 不在list中的为非调试状态
			_xdb_verify_unsafe_();
			java.util.List<Integer> debugs;
			DebugFlag _o_ = DebugFlag.this;
		debugs = new java.util.LinkedList<Integer>();
		debugs.addAll(_o_.debugs);
			return debugs;
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
			return DebugFlag.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return DebugFlag.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return DebugFlag.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return DebugFlag.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return DebugFlag.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return DebugFlag.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return DebugFlag.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return DebugFlag.this.hashCode();
		}

		@Override
		public String toString() {
			return DebugFlag.this.toString();
		}

	}

	public static final class Data implements xbean.DebugFlag {
		private java.util.LinkedList<Integer> debugs; // 不在list中的为非调试状态

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			debugs = new java.util.LinkedList<Integer>();
		}

		Data(xbean.DebugFlag _o1_) {
			if (_o1_ instanceof DebugFlag) assign((DebugFlag)_o1_);
			else if (_o1_ instanceof DebugFlag.Data) assign((DebugFlag.Data)_o1_);
			else if (_o1_ instanceof DebugFlag.Const) assign(((DebugFlag.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(DebugFlag _o_) {
			debugs = new java.util.LinkedList<Integer>();
			debugs.addAll(_o_.debugs);
		}

		private void assign(DebugFlag.Data _o_) {
			debugs = new java.util.LinkedList<Integer>();
			debugs.addAll(_o_.debugs);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(debugs.size());
			for (Integer _v_ : debugs) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				debugs.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.DebugFlag copy() {
			return new Data(this);
		}

		@Override
		public xbean.DebugFlag toData() {
			return new Data(this);
		}

		public xbean.DebugFlag toBean() {
			return new DebugFlag(this, null, null);
		}

		@Override
		public xbean.DebugFlag toDataIf() {
			return this;
		}

		public xbean.DebugFlag toBeanIf() {
			return new DebugFlag(this, null, null);
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
		public java.util.List<Integer> getDebugs() { // 不在list中的为非调试状态
			return debugs;
		}

		@Override
		public java.util.List<Integer> getDebugsAsData() { // 不在list中的为非调试状态
			return debugs;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof DebugFlag.Data)) return false;
			DebugFlag.Data _o_ = (DebugFlag.Data) _o1_;
			if (!debugs.equals(_o_.debugs)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += debugs.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(debugs);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

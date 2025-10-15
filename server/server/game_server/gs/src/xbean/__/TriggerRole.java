
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TriggerRole extends mkdb.XBean implements xbean.TriggerRole {
	private java.util.LinkedList<Integer> triggeredids; // 

	@Override
	public void _reset_unsafe_() {
		triggeredids.clear();
	}

	TriggerRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		triggeredids = new java.util.LinkedList<Integer>();
	}

	public TriggerRole() {
		this(0, null, null);
	}

	public TriggerRole(TriggerRole _o_) {
		this(_o_, null, null);
	}

	TriggerRole(xbean.TriggerRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TriggerRole) assign((TriggerRole)_o1_);
		else if (_o1_ instanceof TriggerRole.Data) assign((TriggerRole.Data)_o1_);
		else if (_o1_ instanceof TriggerRole.Const) assign(((TriggerRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TriggerRole _o_) {
		_o_._xdb_verify_unsafe_();
		triggeredids = new java.util.LinkedList<Integer>();
		triggeredids.addAll(_o_.triggeredids);
	}

	private void assign(TriggerRole.Data _o_) {
		triggeredids = new java.util.LinkedList<Integer>();
		triggeredids.addAll(_o_.triggeredids);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(triggeredids.size());
		for (Integer _v_ : triggeredids) {
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
			triggeredids.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.TriggerRole copy() {
		_xdb_verify_unsafe_();
		return new TriggerRole(this);
	}

	@Override
	public xbean.TriggerRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TriggerRole toBean() {
		_xdb_verify_unsafe_();
		return new TriggerRole(this); // same as copy()
	}

	@Override
	public xbean.TriggerRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TriggerRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Integer> getTriggeredids() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "triggeredids"), triggeredids);
	}

	public java.util.List<Integer> getTriggeredidsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Integer> triggeredids;
		TriggerRole _o_ = this;
		triggeredids = new java.util.LinkedList<Integer>();
		triggeredids.addAll(_o_.triggeredids);
		return triggeredids;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TriggerRole _o_ = null;
		if ( _o1_ instanceof TriggerRole ) _o_ = (TriggerRole)_o1_;
		else if ( _o1_ instanceof TriggerRole.Const ) _o_ = ((TriggerRole.Const)_o1_).nThis();
		else return false;
		if (!triggeredids.equals(_o_.triggeredids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += triggeredids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(triggeredids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("triggeredids"));
		return lb;
	}

	private class Const implements xbean.TriggerRole {
		TriggerRole nThis() {
			return TriggerRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TriggerRole copy() {
			return TriggerRole.this.copy();
		}

		@Override
		public xbean.TriggerRole toData() {
			return TriggerRole.this.toData();
		}

		public xbean.TriggerRole toBean() {
			return TriggerRole.this.toBean();
		}

		@Override
		public xbean.TriggerRole toDataIf() {
			return TriggerRole.this.toDataIf();
		}

		public xbean.TriggerRole toBeanIf() {
			return TriggerRole.this.toBeanIf();
		}

		@Override
		public java.util.List<Integer> getTriggeredids() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(triggeredids);
		}

		public java.util.List<Integer> getTriggeredidsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Integer> triggeredids;
			TriggerRole _o_ = TriggerRole.this;
		triggeredids = new java.util.LinkedList<Integer>();
		triggeredids.addAll(_o_.triggeredids);
			return triggeredids;
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
			return TriggerRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TriggerRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TriggerRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TriggerRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TriggerRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TriggerRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TriggerRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TriggerRole.this.hashCode();
		}

		@Override
		public String toString() {
			return TriggerRole.this.toString();
		}

	}

	public static final class Data implements xbean.TriggerRole {
		private java.util.LinkedList<Integer> triggeredids; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			triggeredids = new java.util.LinkedList<Integer>();
		}

		Data(xbean.TriggerRole _o1_) {
			if (_o1_ instanceof TriggerRole) assign((TriggerRole)_o1_);
			else if (_o1_ instanceof TriggerRole.Data) assign((TriggerRole.Data)_o1_);
			else if (_o1_ instanceof TriggerRole.Const) assign(((TriggerRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TriggerRole _o_) {
			triggeredids = new java.util.LinkedList<Integer>();
			triggeredids.addAll(_o_.triggeredids);
		}

		private void assign(TriggerRole.Data _o_) {
			triggeredids = new java.util.LinkedList<Integer>();
			triggeredids.addAll(_o_.triggeredids);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(triggeredids.size());
			for (Integer _v_ : triggeredids) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				triggeredids.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.TriggerRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.TriggerRole toData() {
			return new Data(this);
		}

		public xbean.TriggerRole toBean() {
			return new TriggerRole(this, null, null);
		}

		@Override
		public xbean.TriggerRole toDataIf() {
			return this;
		}

		public xbean.TriggerRole toBeanIf() {
			return new TriggerRole(this, null, null);
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
		public java.util.List<Integer> getTriggeredids() { // 
			return triggeredids;
		}

		@Override
		public java.util.List<Integer> getTriggeredidsAsData() { // 
			return triggeredids;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TriggerRole.Data)) return false;
			TriggerRole.Data _o_ = (TriggerRole.Data) _o1_;
			if (!triggeredids.equals(_o_.triggeredids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += triggeredids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(triggeredids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

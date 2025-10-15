
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class DayFinishTask extends mkdb.XBean implements xbean.DayFinishTask {
	private java.util.LinkedList<Integer> tasklist; // 

	@Override
	public void _reset_unsafe_() {
		tasklist.clear();
	}

	DayFinishTask(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		tasklist = new java.util.LinkedList<Integer>();
	}

	public DayFinishTask() {
		this(0, null, null);
	}

	public DayFinishTask(DayFinishTask _o_) {
		this(_o_, null, null);
	}

	DayFinishTask(xbean.DayFinishTask _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof DayFinishTask) assign((DayFinishTask)_o1_);
		else if (_o1_ instanceof DayFinishTask.Data) assign((DayFinishTask.Data)_o1_);
		else if (_o1_ instanceof DayFinishTask.Const) assign(((DayFinishTask.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(DayFinishTask _o_) {
		_o_._xdb_verify_unsafe_();
		tasklist = new java.util.LinkedList<Integer>();
		tasklist.addAll(_o_.tasklist);
	}

	private void assign(DayFinishTask.Data _o_) {
		tasklist = new java.util.LinkedList<Integer>();
		tasklist.addAll(_o_.tasklist);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(tasklist.size());
		for (Integer _v_ : tasklist) {
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
			tasklist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.DayFinishTask copy() {
		_xdb_verify_unsafe_();
		return new DayFinishTask(this);
	}

	@Override
	public xbean.DayFinishTask toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DayFinishTask toBean() {
		_xdb_verify_unsafe_();
		return new DayFinishTask(this); // same as copy()
	}

	@Override
	public xbean.DayFinishTask toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DayFinishTask toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Integer> getTasklist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "tasklist"), tasklist);
	}

	public java.util.List<Integer> getTasklistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Integer> tasklist;
		DayFinishTask _o_ = this;
		tasklist = new java.util.LinkedList<Integer>();
		tasklist.addAll(_o_.tasklist);
		return tasklist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		DayFinishTask _o_ = null;
		if ( _o1_ instanceof DayFinishTask ) _o_ = (DayFinishTask)_o1_;
		else if ( _o1_ instanceof DayFinishTask.Const ) _o_ = ((DayFinishTask.Const)_o1_).nThis();
		else return false;
		if (!tasklist.equals(_o_.tasklist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += tasklist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tasklist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tasklist"));
		return lb;
	}

	private class Const implements xbean.DayFinishTask {
		DayFinishTask nThis() {
			return DayFinishTask.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.DayFinishTask copy() {
			return DayFinishTask.this.copy();
		}

		@Override
		public xbean.DayFinishTask toData() {
			return DayFinishTask.this.toData();
		}

		public xbean.DayFinishTask toBean() {
			return DayFinishTask.this.toBean();
		}

		@Override
		public xbean.DayFinishTask toDataIf() {
			return DayFinishTask.this.toDataIf();
		}

		public xbean.DayFinishTask toBeanIf() {
			return DayFinishTask.this.toBeanIf();
		}

		@Override
		public java.util.List<Integer> getTasklist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(tasklist);
		}

		public java.util.List<Integer> getTasklistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Integer> tasklist;
			DayFinishTask _o_ = DayFinishTask.this;
		tasklist = new java.util.LinkedList<Integer>();
		tasklist.addAll(_o_.tasklist);
			return tasklist;
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
			return DayFinishTask.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return DayFinishTask.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return DayFinishTask.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return DayFinishTask.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return DayFinishTask.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return DayFinishTask.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return DayFinishTask.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return DayFinishTask.this.hashCode();
		}

		@Override
		public String toString() {
			return DayFinishTask.this.toString();
		}

	}

	public static final class Data implements xbean.DayFinishTask {
		private java.util.LinkedList<Integer> tasklist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			tasklist = new java.util.LinkedList<Integer>();
		}

		Data(xbean.DayFinishTask _o1_) {
			if (_o1_ instanceof DayFinishTask) assign((DayFinishTask)_o1_);
			else if (_o1_ instanceof DayFinishTask.Data) assign((DayFinishTask.Data)_o1_);
			else if (_o1_ instanceof DayFinishTask.Const) assign(((DayFinishTask.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(DayFinishTask _o_) {
			tasklist = new java.util.LinkedList<Integer>();
			tasklist.addAll(_o_.tasklist);
		}

		private void assign(DayFinishTask.Data _o_) {
			tasklist = new java.util.LinkedList<Integer>();
			tasklist.addAll(_o_.tasklist);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(tasklist.size());
			for (Integer _v_ : tasklist) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				tasklist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.DayFinishTask copy() {
			return new Data(this);
		}

		@Override
		public xbean.DayFinishTask toData() {
			return new Data(this);
		}

		public xbean.DayFinishTask toBean() {
			return new DayFinishTask(this, null, null);
		}

		@Override
		public xbean.DayFinishTask toDataIf() {
			return this;
		}

		public xbean.DayFinishTask toBeanIf() {
			return new DayFinishTask(this, null, null);
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
		public java.util.List<Integer> getTasklist() { // 
			return tasklist;
		}

		@Override
		public java.util.List<Integer> getTasklistAsData() { // 
			return tasklist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof DayFinishTask.Data)) return false;
			DayFinishTask.Data _o_ = (DayFinishTask.Data) _o1_;
			if (!tasklist.equals(_o_.tasklist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += tasklist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(tasklist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

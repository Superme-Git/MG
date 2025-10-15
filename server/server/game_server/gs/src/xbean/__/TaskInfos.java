
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TaskInfos extends mkdb.XBean implements xbean.TaskInfos {
	private java.util.HashMap<Integer, xbean.TaskDlgInfo> tasksmap; // 

	@Override
	public void _reset_unsafe_() {
		tasksmap.clear();
	}

	TaskInfos(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>();
	}

	public TaskInfos() {
		this(0, null, null);
	}

	public TaskInfos(TaskInfos _o_) {
		this(_o_, null, null);
	}

	TaskInfos(xbean.TaskInfos _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TaskInfos) assign((TaskInfos)_o1_);
		else if (_o1_ instanceof TaskInfos.Data) assign((TaskInfos.Data)_o1_);
		else if (_o1_ instanceof TaskInfos.Const) assign(((TaskInfos.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TaskInfos _o_) {
		_o_._xdb_verify_unsafe_();
		tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>();
		for (java.util.Map.Entry<Integer, xbean.TaskDlgInfo> _e_ : _o_.tasksmap.entrySet())
			tasksmap.put(_e_.getKey(), new TaskDlgInfo(_e_.getValue(), this, "tasksmap"));
	}

	private void assign(TaskInfos.Data _o_) {
		tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>();
		for (java.util.Map.Entry<Integer, xbean.TaskDlgInfo> _e_ : _o_.tasksmap.entrySet())
			tasksmap.put(_e_.getKey(), new TaskDlgInfo(_e_.getValue(), this, "tasksmap"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(tasksmap.size());
		for (java.util.Map.Entry<Integer, xbean.TaskDlgInfo> _e_ : tasksmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.TaskDlgInfo _v_ = new TaskDlgInfo(0, this, "tasksmap");
				_v_.unmarshal(_os_);
				tasksmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.TaskInfos copy() {
		_xdb_verify_unsafe_();
		return new TaskInfos(this);
	}

	@Override
	public xbean.TaskInfos toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TaskInfos toBean() {
		_xdb_verify_unsafe_();
		return new TaskInfos(this); // same as copy()
	}

	@Override
	public xbean.TaskInfos toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TaskInfos toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.TaskDlgInfo> getTasksmap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "tasksmap"), tasksmap);
	}

	@Override
	public java.util.Map<Integer, xbean.TaskDlgInfo> getTasksmapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.TaskDlgInfo> tasksmap;
		TaskInfos _o_ = this;
		tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>();
		for (java.util.Map.Entry<Integer, xbean.TaskDlgInfo> _e_ : _o_.tasksmap.entrySet())
			tasksmap.put(_e_.getKey(), new TaskDlgInfo.Data(_e_.getValue()));
		return tasksmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TaskInfos _o_ = null;
		if ( _o1_ instanceof TaskInfos ) _o_ = (TaskInfos)_o1_;
		else if ( _o1_ instanceof TaskInfos.Const ) _o_ = ((TaskInfos.Const)_o1_).nThis();
		else return false;
		if (!tasksmap.equals(_o_.tasksmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += tasksmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tasksmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("tasksmap"));
		return lb;
	}

	private class Const implements xbean.TaskInfos {
		TaskInfos nThis() {
			return TaskInfos.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TaskInfos copy() {
			return TaskInfos.this.copy();
		}

		@Override
		public xbean.TaskInfos toData() {
			return TaskInfos.this.toData();
		}

		public xbean.TaskInfos toBean() {
			return TaskInfos.this.toBean();
		}

		@Override
		public xbean.TaskInfos toDataIf() {
			return TaskInfos.this.toDataIf();
		}

		public xbean.TaskInfos toBeanIf() {
			return TaskInfos.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.TaskDlgInfo> getTasksmap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(tasksmap);
		}

		@Override
		public java.util.Map<Integer, xbean.TaskDlgInfo> getTasksmapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.TaskDlgInfo> tasksmap;
			TaskInfos _o_ = TaskInfos.this;
			tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>();
			for (java.util.Map.Entry<Integer, xbean.TaskDlgInfo> _e_ : _o_.tasksmap.entrySet())
				tasksmap.put(_e_.getKey(), new TaskDlgInfo.Data(_e_.getValue()));
			return tasksmap;
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
			return TaskInfos.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TaskInfos.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TaskInfos.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TaskInfos.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TaskInfos.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TaskInfos.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TaskInfos.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TaskInfos.this.hashCode();
		}

		@Override
		public String toString() {
			return TaskInfos.this.toString();
		}

	}

	public static final class Data implements xbean.TaskInfos {
		private java.util.HashMap<Integer, xbean.TaskDlgInfo> tasksmap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>();
		}

		Data(xbean.TaskInfos _o1_) {
			if (_o1_ instanceof TaskInfos) assign((TaskInfos)_o1_);
			else if (_o1_ instanceof TaskInfos.Data) assign((TaskInfos.Data)_o1_);
			else if (_o1_ instanceof TaskInfos.Const) assign(((TaskInfos.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TaskInfos _o_) {
			tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>();
			for (java.util.Map.Entry<Integer, xbean.TaskDlgInfo> _e_ : _o_.tasksmap.entrySet())
				tasksmap.put(_e_.getKey(), new TaskDlgInfo.Data(_e_.getValue()));
		}

		private void assign(TaskInfos.Data _o_) {
			tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>();
			for (java.util.Map.Entry<Integer, xbean.TaskDlgInfo> _e_ : _o_.tasksmap.entrySet())
				tasksmap.put(_e_.getKey(), new TaskDlgInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(tasksmap.size());
			for (java.util.Map.Entry<Integer, xbean.TaskDlgInfo> _e_ : tasksmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					tasksmap = new java.util.HashMap<Integer, xbean.TaskDlgInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.TaskDlgInfo _v_ = xbean.Pod.newTaskDlgInfoData();
					_v_.unmarshal(_os_);
					tasksmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.TaskInfos copy() {
			return new Data(this);
		}

		@Override
		public xbean.TaskInfos toData() {
			return new Data(this);
		}

		public xbean.TaskInfos toBean() {
			return new TaskInfos(this, null, null);
		}

		@Override
		public xbean.TaskInfos toDataIf() {
			return this;
		}

		public xbean.TaskInfos toBeanIf() {
			return new TaskInfos(this, null, null);
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
		public java.util.Map<Integer, xbean.TaskDlgInfo> getTasksmap() { // 
			return tasksmap;
		}

		@Override
		public java.util.Map<Integer, xbean.TaskDlgInfo> getTasksmapAsData() { // 
			return tasksmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TaskInfos.Data)) return false;
			TaskInfos.Data _o_ = (TaskInfos.Data) _o1_;
			if (!tasksmap.equals(_o_.tasksmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += tasksmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(tasksmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

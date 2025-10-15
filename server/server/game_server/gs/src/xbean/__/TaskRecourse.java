
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TaskRecourse extends mkdb.XBean implements xbean.TaskRecourse {
	private java.util.ArrayList<Integer> channellist; // 频道id 的list

	@Override
	public void _reset_unsafe_() {
		channellist.clear();
	}

	TaskRecourse(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		channellist = new java.util.ArrayList<Integer>();
	}

	public TaskRecourse() {
		this(0, null, null);
	}

	public TaskRecourse(TaskRecourse _o_) {
		this(_o_, null, null);
	}

	TaskRecourse(xbean.TaskRecourse _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TaskRecourse) assign((TaskRecourse)_o1_);
		else if (_o1_ instanceof TaskRecourse.Data) assign((TaskRecourse.Data)_o1_);
		else if (_o1_ instanceof TaskRecourse.Const) assign(((TaskRecourse.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TaskRecourse _o_) {
		_o_._xdb_verify_unsafe_();
		channellist = new java.util.ArrayList<Integer>();
		channellist.addAll(_o_.channellist);
	}

	private void assign(TaskRecourse.Data _o_) {
		channellist = new java.util.ArrayList<Integer>();
		channellist.addAll(_o_.channellist);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(channellist.size());
		for (Integer _v_ : channellist) {
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
			channellist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.TaskRecourse copy() {
		_xdb_verify_unsafe_();
		return new TaskRecourse(this);
	}

	@Override
	public xbean.TaskRecourse toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TaskRecourse toBean() {
		_xdb_verify_unsafe_();
		return new TaskRecourse(this); // same as copy()
	}

	@Override
	public xbean.TaskRecourse toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TaskRecourse toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Integer> getChannellist() { // 频道id 的list
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "channellist"), channellist);
	}

	public java.util.List<Integer> getChannellistAsData() { // 频道id 的list
		_xdb_verify_unsafe_();
		java.util.List<Integer> channellist;
		TaskRecourse _o_ = this;
		channellist = new java.util.ArrayList<Integer>();
		channellist.addAll(_o_.channellist);
		return channellist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TaskRecourse _o_ = null;
		if ( _o1_ instanceof TaskRecourse ) _o_ = (TaskRecourse)_o1_;
		else if ( _o1_ instanceof TaskRecourse.Const ) _o_ = ((TaskRecourse.Const)_o1_).nThis();
		else return false;
		if (!channellist.equals(_o_.channellist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += channellist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(channellist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("channellist"));
		return lb;
	}

	private class Const implements xbean.TaskRecourse {
		TaskRecourse nThis() {
			return TaskRecourse.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TaskRecourse copy() {
			return TaskRecourse.this.copy();
		}

		@Override
		public xbean.TaskRecourse toData() {
			return TaskRecourse.this.toData();
		}

		public xbean.TaskRecourse toBean() {
			return TaskRecourse.this.toBean();
		}

		@Override
		public xbean.TaskRecourse toDataIf() {
			return TaskRecourse.this.toDataIf();
		}

		public xbean.TaskRecourse toBeanIf() {
			return TaskRecourse.this.toBeanIf();
		}

		@Override
		public java.util.List<Integer> getChannellist() { // 频道id 的list
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(channellist);
		}

		public java.util.List<Integer> getChannellistAsData() { // 频道id 的list
			_xdb_verify_unsafe_();
			java.util.List<Integer> channellist;
			TaskRecourse _o_ = TaskRecourse.this;
		channellist = new java.util.ArrayList<Integer>();
		channellist.addAll(_o_.channellist);
			return channellist;
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
			return TaskRecourse.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TaskRecourse.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TaskRecourse.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TaskRecourse.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TaskRecourse.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TaskRecourse.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TaskRecourse.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TaskRecourse.this.hashCode();
		}

		@Override
		public String toString() {
			return TaskRecourse.this.toString();
		}

	}

	public static final class Data implements xbean.TaskRecourse {
		private java.util.ArrayList<Integer> channellist; // 频道id 的list

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			channellist = new java.util.ArrayList<Integer>();
		}

		Data(xbean.TaskRecourse _o1_) {
			if (_o1_ instanceof TaskRecourse) assign((TaskRecourse)_o1_);
			else if (_o1_ instanceof TaskRecourse.Data) assign((TaskRecourse.Data)_o1_);
			else if (_o1_ instanceof TaskRecourse.Const) assign(((TaskRecourse.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TaskRecourse _o_) {
			channellist = new java.util.ArrayList<Integer>();
			channellist.addAll(_o_.channellist);
		}

		private void assign(TaskRecourse.Data _o_) {
			channellist = new java.util.ArrayList<Integer>();
			channellist.addAll(_o_.channellist);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(channellist.size());
			for (Integer _v_ : channellist) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				channellist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.TaskRecourse copy() {
			return new Data(this);
		}

		@Override
		public xbean.TaskRecourse toData() {
			return new Data(this);
		}

		public xbean.TaskRecourse toBean() {
			return new TaskRecourse(this, null, null);
		}

		@Override
		public xbean.TaskRecourse toDataIf() {
			return this;
		}

		public xbean.TaskRecourse toBeanIf() {
			return new TaskRecourse(this, null, null);
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
		public java.util.List<Integer> getChannellist() { // 频道id 的list
			return channellist;
		}

		@Override
		public java.util.List<Integer> getChannellistAsData() { // 频道id 的list
			return channellist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TaskRecourse.Data)) return false;
			TaskRecourse.Data _o_ = (TaskRecourse.Data) _o1_;
			if (!channellist.equals(_o_.channellist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += channellist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(channellist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}


package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Mission extends mkdb.XBean implements xbean.Mission {
	private int id; // 
	private int status; // -2放弃 -1未接受 1已提交 2执行失败 3完成 4进行中
	private java.util.ArrayList<Integer> path; // 
	private int taskvalue; // 
	private long deadline; // 
	private int round; // 剧情任务和主线任务用默认的0就可以了

	@Override
	public void _reset_unsafe_() {
		id = 0;
		status = 0;
		path.clear();
		taskvalue = 0;
		deadline = 0;
		round = 0;
	}

	Mission(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		status = 0;
		path = new java.util.ArrayList<Integer>();
		taskvalue = 0;
		deadline = 0;
		round = 0;
	}

	public Mission() {
		this(0, null, null);
	}

	public Mission(Mission _o_) {
		this(_o_, null, null);
	}

	Mission(xbean.Mission _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Mission) assign((Mission)_o1_);
		else if (_o1_ instanceof Mission.Data) assign((Mission.Data)_o1_);
		else if (_o1_ instanceof Mission.Const) assign(((Mission.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Mission _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		status = _o_.status;
		path = new java.util.ArrayList<Integer>();
		path.addAll(_o_.path);
		taskvalue = _o_.taskvalue;
		deadline = _o_.deadline;
		round = _o_.round;
	}

	private void assign(Mission.Data _o_) {
		id = _o_.id;
		status = _o_.status;
		path = new java.util.ArrayList<Integer>();
		path.addAll(_o_.path);
		taskvalue = _o_.taskvalue;
		deadline = _o_.deadline;
		round = _o_.round;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(status);
		_os_.compact_uint32(path.size());
		for (Integer _v_ : path) {
			_os_.marshal(_v_);
		}
		_os_.marshal(taskvalue);
		_os_.marshal(deadline);
		_os_.marshal(round);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		status = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			path.add(_v_);
		}
		taskvalue = _os_.unmarshal_int();
		deadline = _os_.unmarshal_long();
		round = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.Mission copy() {
		_xdb_verify_unsafe_();
		return new Mission(this);
	}

	@Override
	public xbean.Mission toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Mission toBean() {
		_xdb_verify_unsafe_();
		return new Mission(this); // same as copy()
	}

	@Override
	public xbean.Mission toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Mission toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getStatus() { // -2放弃 -1未接受 1已提交 2执行失败 3完成 4进行中
		_xdb_verify_unsafe_();
		return status;
	}

	@Override
	public java.util.List<Integer> getPath() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "path"), path);
	}

	public java.util.List<Integer> getPathAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<Integer> path;
		Mission _o_ = this;
		path = new java.util.ArrayList<Integer>();
		path.addAll(_o_.path);
		return path;
	}

	@Override
	public int getTaskvalue() { // 
		_xdb_verify_unsafe_();
		return taskvalue;
	}

	@Override
	public long getDeadline() { // 
		_xdb_verify_unsafe_();
		return deadline;
	}

	@Override
	public int getRound() { // 剧情任务和主线任务用默认的0就可以了
		_xdb_verify_unsafe_();
		return round;
	}

	@Override
	public void setId(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setStatus(int _v_) { // -2放弃 -1未接受 1已提交 2执行失败 3完成 4进行中
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "status") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, status) {
					public void rollback() { status = _xdb_saved; }
				};}});
		status = _v_;
	}

	@Override
	public void setTaskvalue(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "taskvalue") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, taskvalue) {
					public void rollback() { taskvalue = _xdb_saved; }
				};}});
		taskvalue = _v_;
	}

	@Override
	public void setDeadline(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "deadline") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, deadline) {
					public void rollback() { deadline = _xdb_saved; }
				};}});
		deadline = _v_;
	}

	@Override
	public void setRound(int _v_) { // 剧情任务和主线任务用默认的0就可以了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "round") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, round) {
					public void rollback() { round = _xdb_saved; }
				};}});
		round = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Mission _o_ = null;
		if ( _o1_ instanceof Mission ) _o_ = (Mission)_o1_;
		else if ( _o1_ instanceof Mission.Const ) _o_ = ((Mission.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (status != _o_.status) return false;
		if (!path.equals(_o_.path)) return false;
		if (taskvalue != _o_.taskvalue) return false;
		if (deadline != _o_.deadline) return false;
		if (round != _o_.round) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += status;
		_h_ += path.hashCode();
		_h_ += taskvalue;
		_h_ += deadline;
		_h_ += round;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(status);
		_sb_.append(",");
		_sb_.append(path);
		_sb_.append(",");
		_sb_.append(taskvalue);
		_sb_.append(",");
		_sb_.append(deadline);
		_sb_.append(",");
		_sb_.append(round);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("status"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("path"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("taskvalue"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("deadline"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("round"));
		return lb;
	}

	private class Const implements xbean.Mission {
		Mission nThis() {
			return Mission.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Mission copy() {
			return Mission.this.copy();
		}

		@Override
		public xbean.Mission toData() {
			return Mission.this.toData();
		}

		public xbean.Mission toBean() {
			return Mission.this.toBean();
		}

		@Override
		public xbean.Mission toDataIf() {
			return Mission.this.toDataIf();
		}

		public xbean.Mission toBeanIf() {
			return Mission.this.toBeanIf();
		}

		@Override
		public int getId() { // 
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getStatus() { // -2放弃 -1未接受 1已提交 2执行失败 3完成 4进行中
			_xdb_verify_unsafe_();
			return status;
		}

		@Override
		public java.util.List<Integer> getPath() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(path);
		}

		public java.util.List<Integer> getPathAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<Integer> path;
			Mission _o_ = Mission.this;
		path = new java.util.ArrayList<Integer>();
		path.addAll(_o_.path);
			return path;
		}

		@Override
		public int getTaskvalue() { // 
			_xdb_verify_unsafe_();
			return taskvalue;
		}

		@Override
		public long getDeadline() { // 
			_xdb_verify_unsafe_();
			return deadline;
		}

		@Override
		public int getRound() { // 剧情任务和主线任务用默认的0就可以了
			_xdb_verify_unsafe_();
			return round;
		}

		@Override
		public void setId(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStatus(int _v_) { // -2放弃 -1未接受 1已提交 2执行失败 3完成 4进行中
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTaskvalue(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDeadline(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRound(int _v_) { // 剧情任务和主线任务用默认的0就可以了
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
			return Mission.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Mission.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Mission.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Mission.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Mission.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Mission.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Mission.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Mission.this.hashCode();
		}

		@Override
		public String toString() {
			return Mission.this.toString();
		}

	}

	public static final class Data implements xbean.Mission {
		private int id; // 
		private int status; // -2放弃 -1未接受 1已提交 2执行失败 3完成 4进行中
		private java.util.ArrayList<Integer> path; // 
		private int taskvalue; // 
		private long deadline; // 
		private int round; // 剧情任务和主线任务用默认的0就可以了

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			status = 0;
			path = new java.util.ArrayList<Integer>();
			taskvalue = 0;
			deadline = 0;
			round = 0;
		}

		Data(xbean.Mission _o1_) {
			if (_o1_ instanceof Mission) assign((Mission)_o1_);
			else if (_o1_ instanceof Mission.Data) assign((Mission.Data)_o1_);
			else if (_o1_ instanceof Mission.Const) assign(((Mission.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Mission _o_) {
			id = _o_.id;
			status = _o_.status;
			path = new java.util.ArrayList<Integer>();
			path.addAll(_o_.path);
			taskvalue = _o_.taskvalue;
			deadline = _o_.deadline;
			round = _o_.round;
		}

		private void assign(Mission.Data _o_) {
			id = _o_.id;
			status = _o_.status;
			path = new java.util.ArrayList<Integer>();
			path.addAll(_o_.path);
			taskvalue = _o_.taskvalue;
			deadline = _o_.deadline;
			round = _o_.round;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(status);
			_os_.compact_uint32(path.size());
			for (Integer _v_ : path) {
				_os_.marshal(_v_);
			}
			_os_.marshal(taskvalue);
			_os_.marshal(deadline);
			_os_.marshal(round);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			status = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				path.add(_v_);
			}
			taskvalue = _os_.unmarshal_int();
			deadline = _os_.unmarshal_long();
			round = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.Mission copy() {
			return new Data(this);
		}

		@Override
		public xbean.Mission toData() {
			return new Data(this);
		}

		public xbean.Mission toBean() {
			return new Mission(this, null, null);
		}

		@Override
		public xbean.Mission toDataIf() {
			return this;
		}

		public xbean.Mission toBeanIf() {
			return new Mission(this, null, null);
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
		public int getId() { // 
			return id;
		}

		@Override
		public int getStatus() { // -2放弃 -1未接受 1已提交 2执行失败 3完成 4进行中
			return status;
		}

		@Override
		public java.util.List<Integer> getPath() { // 
			return path;
		}

		@Override
		public java.util.List<Integer> getPathAsData() { // 
			return path;
		}

		@Override
		public int getTaskvalue() { // 
			return taskvalue;
		}

		@Override
		public long getDeadline() { // 
			return deadline;
		}

		@Override
		public int getRound() { // 剧情任务和主线任务用默认的0就可以了
			return round;
		}

		@Override
		public void setId(int _v_) { // 
			id = _v_;
		}

		@Override
		public void setStatus(int _v_) { // -2放弃 -1未接受 1已提交 2执行失败 3完成 4进行中
			status = _v_;
		}

		@Override
		public void setTaskvalue(int _v_) { // 
			taskvalue = _v_;
		}

		@Override
		public void setDeadline(long _v_) { // 
			deadline = _v_;
		}

		@Override
		public void setRound(int _v_) { // 剧情任务和主线任务用默认的0就可以了
			round = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Mission.Data)) return false;
			Mission.Data _o_ = (Mission.Data) _o1_;
			if (id != _o_.id) return false;
			if (status != _o_.status) return false;
			if (!path.equals(_o_.path)) return false;
			if (taskvalue != _o_.taskvalue) return false;
			if (deadline != _o_.deadline) return false;
			if (round != _o_.round) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += status;
			_h_ += path.hashCode();
			_h_ += taskvalue;
			_h_ += deadline;
			_h_ += round;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(status);
			_sb_.append(",");
			_sb_.append(path);
			_sb_.append(",");
			_sb_.append(taskvalue);
			_sb_.append(",");
			_sb_.append(deadline);
			_sb_.append(",");
			_sb_.append(round);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

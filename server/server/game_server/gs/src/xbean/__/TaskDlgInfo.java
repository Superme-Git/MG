
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TaskDlgInfo extends mkdb.XBean implements xbean.TaskDlgInfo {
	private int taskid; // 任务id
	private int taskstate; // 任务状态
	private int round; // 当前任务的环数
	private int sumnum; // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
	private int dstmapid; // 目的地图id
	private long dstnpckey; // 目的npc的key
	private long endtime; // 任务的结束时间
	private int dstnpcid; // 目的npc的id
	private int dstitemid; // 目的道具的id
	private int dstitemnum; // 目的道具的数量
	private int dstx; // 目的坐标
	private int dsty; // 目的坐标

	@Override
	public void _reset_unsafe_() {
		taskid = 0;
		taskstate = 0;
		round = 0;
		sumnum = 0;
		dstmapid = 0;
		dstnpckey = 0L;
		endtime = 0L;
		dstnpcid = 0;
		dstitemid = 0;
		dstitemnum = 0;
		dstx = 0;
		dsty = 0;
	}

	TaskDlgInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public TaskDlgInfo() {
		this(0, null, null);
	}

	public TaskDlgInfo(TaskDlgInfo _o_) {
		this(_o_, null, null);
	}

	TaskDlgInfo(xbean.TaskDlgInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TaskDlgInfo) assign((TaskDlgInfo)_o1_);
		else if (_o1_ instanceof TaskDlgInfo.Data) assign((TaskDlgInfo.Data)_o1_);
		else if (_o1_ instanceof TaskDlgInfo.Const) assign(((TaskDlgInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TaskDlgInfo _o_) {
		_o_._xdb_verify_unsafe_();
		taskid = _o_.taskid;
		taskstate = _o_.taskstate;
		round = _o_.round;
		sumnum = _o_.sumnum;
		dstmapid = _o_.dstmapid;
		dstnpckey = _o_.dstnpckey;
		endtime = _o_.endtime;
		dstnpcid = _o_.dstnpcid;
		dstitemid = _o_.dstitemid;
		dstitemnum = _o_.dstitemnum;
		dstx = _o_.dstx;
		dsty = _o_.dsty;
	}

	private void assign(TaskDlgInfo.Data _o_) {
		taskid = _o_.taskid;
		taskstate = _o_.taskstate;
		round = _o_.round;
		sumnum = _o_.sumnum;
		dstmapid = _o_.dstmapid;
		dstnpckey = _o_.dstnpckey;
		endtime = _o_.endtime;
		dstnpcid = _o_.dstnpcid;
		dstitemid = _o_.dstitemid;
		dstitemnum = _o_.dstitemnum;
		dstx = _o_.dstx;
		dsty = _o_.dsty;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(taskid);
		_os_.marshal(taskstate);
		_os_.marshal(round);
		_os_.marshal(sumnum);
		_os_.marshal(dstmapid);
		_os_.marshal(dstnpckey);
		_os_.marshal(endtime);
		_os_.marshal(dstnpcid);
		_os_.marshal(dstitemid);
		_os_.marshal(dstitemnum);
		_os_.marshal(dstx);
		_os_.marshal(dsty);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		taskid = _os_.unmarshal_int();
		taskstate = _os_.unmarshal_int();
		round = _os_.unmarshal_int();
		sumnum = _os_.unmarshal_int();
		dstmapid = _os_.unmarshal_int();
		dstnpckey = _os_.unmarshal_long();
		endtime = _os_.unmarshal_long();
		dstnpcid = _os_.unmarshal_int();
		dstitemid = _os_.unmarshal_int();
		dstitemnum = _os_.unmarshal_int();
		dstx = _os_.unmarshal_int();
		dsty = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.TaskDlgInfo copy() {
		_xdb_verify_unsafe_();
		return new TaskDlgInfo(this);
	}

	@Override
	public xbean.TaskDlgInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TaskDlgInfo toBean() {
		_xdb_verify_unsafe_();
		return new TaskDlgInfo(this); // same as copy()
	}

	@Override
	public xbean.TaskDlgInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TaskDlgInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getTaskid() { // 任务id
		_xdb_verify_unsafe_();
		return taskid;
	}

	@Override
	public int getTaskstate() { // 任务状态
		_xdb_verify_unsafe_();
		return taskstate;
	}

	@Override
	public int getRound() { // 当前任务的环数
		_xdb_verify_unsafe_();
		return round;
	}

	@Override
	public int getSumnum() { // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
		_xdb_verify_unsafe_();
		return sumnum;
	}

	@Override
	public int getDstmapid() { // 目的地图id
		_xdb_verify_unsafe_();
		return dstmapid;
	}

	@Override
	public long getDstnpckey() { // 目的npc的key
		_xdb_verify_unsafe_();
		return dstnpckey;
	}

	@Override
	public long getEndtime() { // 任务的结束时间
		_xdb_verify_unsafe_();
		return endtime;
	}

	@Override
	public int getDstnpcid() { // 目的npc的id
		_xdb_verify_unsafe_();
		return dstnpcid;
	}

	@Override
	public int getDstitemid() { // 目的道具的id
		_xdb_verify_unsafe_();
		return dstitemid;
	}

	@Override
	public int getDstitemnum() { // 目的道具的数量
		_xdb_verify_unsafe_();
		return dstitemnum;
	}

	@Override
	public int getDstx() { // 目的坐标
		_xdb_verify_unsafe_();
		return dstx;
	}

	@Override
	public int getDsty() { // 目的坐标
		_xdb_verify_unsafe_();
		return dsty;
	}

	@Override
	public void setTaskid(int _v_) { // 任务id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "taskid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, taskid) {
					public void rollback() { taskid = _xdb_saved; }
				};}});
		taskid = _v_;
	}

	@Override
	public void setTaskstate(int _v_) { // 任务状态
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "taskstate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, taskstate) {
					public void rollback() { taskstate = _xdb_saved; }
				};}});
		taskstate = _v_;
	}

	@Override
	public void setRound(int _v_) { // 当前任务的环数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "round") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, round) {
					public void rollback() { round = _xdb_saved; }
				};}});
		round = _v_;
	}

	@Override
	public void setSumnum(int _v_) { // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sumnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sumnum) {
					public void rollback() { sumnum = _xdb_saved; }
				};}});
		sumnum = _v_;
	}

	@Override
	public void setDstmapid(int _v_) { // 目的地图id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstmapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstmapid) {
					public void rollback() { dstmapid = _xdb_saved; }
				};}});
		dstmapid = _v_;
	}

	@Override
	public void setDstnpckey(long _v_) { // 目的npc的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstnpckey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, dstnpckey) {
					public void rollback() { dstnpckey = _xdb_saved; }
				};}});
		dstnpckey = _v_;
	}

	@Override
	public void setEndtime(long _v_) { // 任务的结束时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "endtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, endtime) {
					public void rollback() { endtime = _xdb_saved; }
				};}});
		endtime = _v_;
	}

	@Override
	public void setDstnpcid(int _v_) { // 目的npc的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstnpcid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstnpcid) {
					public void rollback() { dstnpcid = _xdb_saved; }
				};}});
		dstnpcid = _v_;
	}

	@Override
	public void setDstitemid(int _v_) { // 目的道具的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstitemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstitemid) {
					public void rollback() { dstitemid = _xdb_saved; }
				};}});
		dstitemid = _v_;
	}

	@Override
	public void setDstitemnum(int _v_) { // 目的道具的数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstitemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstitemnum) {
					public void rollback() { dstitemnum = _xdb_saved; }
				};}});
		dstitemnum = _v_;
	}

	@Override
	public void setDstx(int _v_) { // 目的坐标
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstx) {
					public void rollback() { dstx = _xdb_saved; }
				};}});
		dstx = _v_;
	}

	@Override
	public void setDsty(int _v_) { // 目的坐标
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dsty") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dsty) {
					public void rollback() { dsty = _xdb_saved; }
				};}});
		dsty = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TaskDlgInfo _o_ = null;
		if ( _o1_ instanceof TaskDlgInfo ) _o_ = (TaskDlgInfo)_o1_;
		else if ( _o1_ instanceof TaskDlgInfo.Const ) _o_ = ((TaskDlgInfo.Const)_o1_).nThis();
		else return false;
		if (taskid != _o_.taskid) return false;
		if (taskstate != _o_.taskstate) return false;
		if (round != _o_.round) return false;
		if (sumnum != _o_.sumnum) return false;
		if (dstmapid != _o_.dstmapid) return false;
		if (dstnpckey != _o_.dstnpckey) return false;
		if (endtime != _o_.endtime) return false;
		if (dstnpcid != _o_.dstnpcid) return false;
		if (dstitemid != _o_.dstitemid) return false;
		if (dstitemnum != _o_.dstitemnum) return false;
		if (dstx != _o_.dstx) return false;
		if (dsty != _o_.dsty) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += taskid;
		_h_ += taskstate;
		_h_ += round;
		_h_ += sumnum;
		_h_ += dstmapid;
		_h_ += dstnpckey;
		_h_ += endtime;
		_h_ += dstnpcid;
		_h_ += dstitemid;
		_h_ += dstitemnum;
		_h_ += dstx;
		_h_ += dsty;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(taskid);
		_sb_.append(",");
		_sb_.append(taskstate);
		_sb_.append(",");
		_sb_.append(round);
		_sb_.append(",");
		_sb_.append(sumnum);
		_sb_.append(",");
		_sb_.append(dstmapid);
		_sb_.append(",");
		_sb_.append(dstnpckey);
		_sb_.append(",");
		_sb_.append(endtime);
		_sb_.append(",");
		_sb_.append(dstnpcid);
		_sb_.append(",");
		_sb_.append(dstitemid);
		_sb_.append(",");
		_sb_.append(dstitemnum);
		_sb_.append(",");
		_sb_.append(dstx);
		_sb_.append(",");
		_sb_.append(dsty);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("taskid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("taskstate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("round"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sumnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstmapid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstnpckey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstnpcid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstitemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstitemnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dsty"));
		return lb;
	}

	private class Const implements xbean.TaskDlgInfo {
		TaskDlgInfo nThis() {
			return TaskDlgInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TaskDlgInfo copy() {
			return TaskDlgInfo.this.copy();
		}

		@Override
		public xbean.TaskDlgInfo toData() {
			return TaskDlgInfo.this.toData();
		}

		public xbean.TaskDlgInfo toBean() {
			return TaskDlgInfo.this.toBean();
		}

		@Override
		public xbean.TaskDlgInfo toDataIf() {
			return TaskDlgInfo.this.toDataIf();
		}

		public xbean.TaskDlgInfo toBeanIf() {
			return TaskDlgInfo.this.toBeanIf();
		}

		@Override
		public int getTaskid() { // 任务id
			_xdb_verify_unsafe_();
			return taskid;
		}

		@Override
		public int getTaskstate() { // 任务状态
			_xdb_verify_unsafe_();
			return taskstate;
		}

		@Override
		public int getRound() { // 当前任务的环数
			_xdb_verify_unsafe_();
			return round;
		}

		@Override
		public int getSumnum() { // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
			_xdb_verify_unsafe_();
			return sumnum;
		}

		@Override
		public int getDstmapid() { // 目的地图id
			_xdb_verify_unsafe_();
			return dstmapid;
		}

		@Override
		public long getDstnpckey() { // 目的npc的key
			_xdb_verify_unsafe_();
			return dstnpckey;
		}

		@Override
		public long getEndtime() { // 任务的结束时间
			_xdb_verify_unsafe_();
			return endtime;
		}

		@Override
		public int getDstnpcid() { // 目的npc的id
			_xdb_verify_unsafe_();
			return dstnpcid;
		}

		@Override
		public int getDstitemid() { // 目的道具的id
			_xdb_verify_unsafe_();
			return dstitemid;
		}

		@Override
		public int getDstitemnum() { // 目的道具的数量
			_xdb_verify_unsafe_();
			return dstitemnum;
		}

		@Override
		public int getDstx() { // 目的坐标
			_xdb_verify_unsafe_();
			return dstx;
		}

		@Override
		public int getDsty() { // 目的坐标
			_xdb_verify_unsafe_();
			return dsty;
		}

		@Override
		public void setTaskid(int _v_) { // 任务id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTaskstate(int _v_) { // 任务状态
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRound(int _v_) { // 当前任务的环数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSumnum(int _v_) { // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstmapid(int _v_) { // 目的地图id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstnpckey(long _v_) { // 目的npc的key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEndtime(long _v_) { // 任务的结束时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstnpcid(int _v_) { // 目的npc的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstitemid(int _v_) { // 目的道具的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstitemnum(int _v_) { // 目的道具的数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstx(int _v_) { // 目的坐标
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDsty(int _v_) { // 目的坐标
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
			return TaskDlgInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TaskDlgInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TaskDlgInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TaskDlgInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TaskDlgInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TaskDlgInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TaskDlgInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TaskDlgInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return TaskDlgInfo.this.toString();
		}

	}

	public static final class Data implements xbean.TaskDlgInfo {
		private int taskid; // 任务id
		private int taskstate; // 任务状态
		private int round; // 当前任务的环数
		private int sumnum; // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
		private int dstmapid; // 目的地图id
		private long dstnpckey; // 目的npc的key
		private long endtime; // 任务的结束时间
		private int dstnpcid; // 目的npc的id
		private int dstitemid; // 目的道具的id
		private int dstitemnum; // 目的道具的数量
		private int dstx; // 目的坐标
		private int dsty; // 目的坐标

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.TaskDlgInfo _o1_) {
			if (_o1_ instanceof TaskDlgInfo) assign((TaskDlgInfo)_o1_);
			else if (_o1_ instanceof TaskDlgInfo.Data) assign((TaskDlgInfo.Data)_o1_);
			else if (_o1_ instanceof TaskDlgInfo.Const) assign(((TaskDlgInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TaskDlgInfo _o_) {
			taskid = _o_.taskid;
			taskstate = _o_.taskstate;
			round = _o_.round;
			sumnum = _o_.sumnum;
			dstmapid = _o_.dstmapid;
			dstnpckey = _o_.dstnpckey;
			endtime = _o_.endtime;
			dstnpcid = _o_.dstnpcid;
			dstitemid = _o_.dstitemid;
			dstitemnum = _o_.dstitemnum;
			dstx = _o_.dstx;
			dsty = _o_.dsty;
		}

		private void assign(TaskDlgInfo.Data _o_) {
			taskid = _o_.taskid;
			taskstate = _o_.taskstate;
			round = _o_.round;
			sumnum = _o_.sumnum;
			dstmapid = _o_.dstmapid;
			dstnpckey = _o_.dstnpckey;
			endtime = _o_.endtime;
			dstnpcid = _o_.dstnpcid;
			dstitemid = _o_.dstitemid;
			dstitemnum = _o_.dstitemnum;
			dstx = _o_.dstx;
			dsty = _o_.dsty;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(taskid);
			_os_.marshal(taskstate);
			_os_.marshal(round);
			_os_.marshal(sumnum);
			_os_.marshal(dstmapid);
			_os_.marshal(dstnpckey);
			_os_.marshal(endtime);
			_os_.marshal(dstnpcid);
			_os_.marshal(dstitemid);
			_os_.marshal(dstitemnum);
			_os_.marshal(dstx);
			_os_.marshal(dsty);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			taskid = _os_.unmarshal_int();
			taskstate = _os_.unmarshal_int();
			round = _os_.unmarshal_int();
			sumnum = _os_.unmarshal_int();
			dstmapid = _os_.unmarshal_int();
			dstnpckey = _os_.unmarshal_long();
			endtime = _os_.unmarshal_long();
			dstnpcid = _os_.unmarshal_int();
			dstitemid = _os_.unmarshal_int();
			dstitemnum = _os_.unmarshal_int();
			dstx = _os_.unmarshal_int();
			dsty = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.TaskDlgInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.TaskDlgInfo toData() {
			return new Data(this);
		}

		public xbean.TaskDlgInfo toBean() {
			return new TaskDlgInfo(this, null, null);
		}

		@Override
		public xbean.TaskDlgInfo toDataIf() {
			return this;
		}

		public xbean.TaskDlgInfo toBeanIf() {
			return new TaskDlgInfo(this, null, null);
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
		public int getTaskid() { // 任务id
			return taskid;
		}

		@Override
		public int getTaskstate() { // 任务状态
			return taskstate;
		}

		@Override
		public int getRound() { // 当前任务的环数
			return round;
		}

		@Override
		public int getSumnum() { // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
			return sumnum;
		}

		@Override
		public int getDstmapid() { // 目的地图id
			return dstmapid;
		}

		@Override
		public long getDstnpckey() { // 目的npc的key
			return dstnpckey;
		}

		@Override
		public long getEndtime() { // 任务的结束时间
			return endtime;
		}

		@Override
		public int getDstnpcid() { // 目的npc的id
			return dstnpcid;
		}

		@Override
		public int getDstitemid() { // 目的道具的id
			return dstitemid;
		}

		@Override
		public int getDstitemnum() { // 目的道具的数量
			return dstitemnum;
		}

		@Override
		public int getDstx() { // 目的坐标
			return dstx;
		}

		@Override
		public int getDsty() { // 目的坐标
			return dsty;
		}

		@Override
		public void setTaskid(int _v_) { // 任务id
			taskid = _v_;
		}

		@Override
		public void setTaskstate(int _v_) { // 任务状态
			taskstate = _v_;
		}

		@Override
		public void setRound(int _v_) { // 当前任务的环数
			round = _v_;
		}

		@Override
		public void setSumnum(int _v_) { // 累积次数. 根据不同的任务，拥有不同的含义：如职业，是每天的累积次数。的话，是每周的累积次数
			sumnum = _v_;
		}

		@Override
		public void setDstmapid(int _v_) { // 目的地图id
			dstmapid = _v_;
		}

		@Override
		public void setDstnpckey(long _v_) { // 目的npc的key
			dstnpckey = _v_;
		}

		@Override
		public void setEndtime(long _v_) { // 任务的结束时间
			endtime = _v_;
		}

		@Override
		public void setDstnpcid(int _v_) { // 目的npc的id
			dstnpcid = _v_;
		}

		@Override
		public void setDstitemid(int _v_) { // 目的道具的id
			dstitemid = _v_;
		}

		@Override
		public void setDstitemnum(int _v_) { // 目的道具的数量
			dstitemnum = _v_;
		}

		@Override
		public void setDstx(int _v_) { // 目的坐标
			dstx = _v_;
		}

		@Override
		public void setDsty(int _v_) { // 目的坐标
			dsty = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TaskDlgInfo.Data)) return false;
			TaskDlgInfo.Data _o_ = (TaskDlgInfo.Data) _o1_;
			if (taskid != _o_.taskid) return false;
			if (taskstate != _o_.taskstate) return false;
			if (round != _o_.round) return false;
			if (sumnum != _o_.sumnum) return false;
			if (dstmapid != _o_.dstmapid) return false;
			if (dstnpckey != _o_.dstnpckey) return false;
			if (endtime != _o_.endtime) return false;
			if (dstnpcid != _o_.dstnpcid) return false;
			if (dstitemid != _o_.dstitemid) return false;
			if (dstitemnum != _o_.dstitemnum) return false;
			if (dstx != _o_.dstx) return false;
			if (dsty != _o_.dsty) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += taskid;
			_h_ += taskstate;
			_h_ += round;
			_h_ += sumnum;
			_h_ += dstmapid;
			_h_ += dstnpckey;
			_h_ += endtime;
			_h_ += dstnpcid;
			_h_ += dstitemid;
			_h_ += dstitemnum;
			_h_ += dstx;
			_h_ += dsty;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(taskid);
			_sb_.append(",");
			_sb_.append(taskstate);
			_sb_.append(",");
			_sb_.append(round);
			_sb_.append(",");
			_sb_.append(sumnum);
			_sb_.append(",");
			_sb_.append(dstmapid);
			_sb_.append(",");
			_sb_.append(dstnpckey);
			_sb_.append(",");
			_sb_.append(endtime);
			_sb_.append(",");
			_sb_.append(dstnpcid);
			_sb_.append(",");
			_sb_.append(dstitemid);
			_sb_.append(",");
			_sb_.append(dstitemnum);
			_sb_.append(",");
			_sb_.append(dstx);
			_sb_.append(",");
			_sb_.append(dsty);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

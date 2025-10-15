
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceTaskInfo extends mkdb.XBean implements xbean.InstanceTaskInfo {
	private int id; // excel表唯一id
	private long instanceonlyid; // instancetask 表里的唯一id
	private int instanceflag; // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
	private int counts; // 今天做了多少次
	private int defaulttimes; // 一天默认做多少次
	private int currentstep; // 当前做到哪一阶段
	private int currenttaskid; // 当前做到哪一步了
	private long accepttime; // 最后接任务时间
	private int totalexp; // 经验
	private int state; // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
	private java.util.HashMap<Integer, Integer> stepawardtimes; // key stepId, value 该step给的奖励次数
	private int awarding; // 是否还能再拿奖励,0 不能, 1 可以拿

	@Override
	public void _reset_unsafe_() {
		id = 0;
		instanceonlyid = 0L;
		instanceflag = 0;
		counts = 0;
		defaulttimes = 0;
		currentstep = 1;
		currenttaskid = 0;
		accepttime = 0L;
		totalexp = 0;
		state = 0;
		stepawardtimes.clear();
		awarding = 0;
	}

	InstanceTaskInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		currentstep = 1;
		stepawardtimes = new java.util.HashMap<Integer, Integer>();
		awarding = 0;
	}

	public InstanceTaskInfo() {
		this(0, null, null);
	}

	public InstanceTaskInfo(InstanceTaskInfo _o_) {
		this(_o_, null, null);
	}

	InstanceTaskInfo(xbean.InstanceTaskInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceTaskInfo) assign((InstanceTaskInfo)_o1_);
		else if (_o1_ instanceof InstanceTaskInfo.Data) assign((InstanceTaskInfo.Data)_o1_);
		else if (_o1_ instanceof InstanceTaskInfo.Const) assign(((InstanceTaskInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceTaskInfo _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		instanceonlyid = _o_.instanceonlyid;
		instanceflag = _o_.instanceflag;
		counts = _o_.counts;
		defaulttimes = _o_.defaulttimes;
		currentstep = _o_.currentstep;
		currenttaskid = _o_.currenttaskid;
		accepttime = _o_.accepttime;
		totalexp = _o_.totalexp;
		state = _o_.state;
		stepawardtimes = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
			stepawardtimes.put(_e_.getKey(), _e_.getValue());
		awarding = _o_.awarding;
	}

	private void assign(InstanceTaskInfo.Data _o_) {
		id = _o_.id;
		instanceonlyid = _o_.instanceonlyid;
		instanceflag = _o_.instanceflag;
		counts = _o_.counts;
		defaulttimes = _o_.defaulttimes;
		currentstep = _o_.currentstep;
		currenttaskid = _o_.currenttaskid;
		accepttime = _o_.accepttime;
		totalexp = _o_.totalexp;
		state = _o_.state;
		stepawardtimes = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
			stepawardtimes.put(_e_.getKey(), _e_.getValue());
		awarding = _o_.awarding;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(instanceonlyid);
		_os_.marshal(instanceflag);
		_os_.marshal(counts);
		_os_.marshal(defaulttimes);
		_os_.marshal(currentstep);
		_os_.marshal(currenttaskid);
		_os_.marshal(accepttime);
		_os_.marshal(totalexp);
		_os_.marshal(state);
		_os_.compact_uint32(stepawardtimes.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : stepawardtimes.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(awarding);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		instanceonlyid = _os_.unmarshal_long();
		instanceflag = _os_.unmarshal_int();
		counts = _os_.unmarshal_int();
		defaulttimes = _os_.unmarshal_int();
		currentstep = _os_.unmarshal_int();
		currenttaskid = _os_.unmarshal_int();
		accepttime = _os_.unmarshal_long();
		totalexp = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				stepawardtimes = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				stepawardtimes.put(_k_, _v_);
			}
		}
		awarding = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.InstanceTaskInfo copy() {
		_xdb_verify_unsafe_();
		return new InstanceTaskInfo(this);
	}

	@Override
	public xbean.InstanceTaskInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceTaskInfo toBean() {
		_xdb_verify_unsafe_();
		return new InstanceTaskInfo(this); // same as copy()
	}

	@Override
	public xbean.InstanceTaskInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceTaskInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // excel表唯一id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public long getInstanceonlyid() { // instancetask 表里的唯一id
		_xdb_verify_unsafe_();
		return instanceonlyid;
	}

	@Override
	public int getInstanceflag() { // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
		_xdb_verify_unsafe_();
		return instanceflag;
	}

	@Override
	public int getCounts() { // 今天做了多少次
		_xdb_verify_unsafe_();
		return counts;
	}

	@Override
	public int getDefaulttimes() { // 一天默认做多少次
		_xdb_verify_unsafe_();
		return defaulttimes;
	}

	@Override
	public int getCurrentstep() { // 当前做到哪一阶段
		_xdb_verify_unsafe_();
		return currentstep;
	}

	@Override
	public int getCurrenttaskid() { // 当前做到哪一步了
		_xdb_verify_unsafe_();
		return currenttaskid;
	}

	@Override
	public long getAccepttime() { // 最后接任务时间
		_xdb_verify_unsafe_();
		return accepttime;
	}

	@Override
	public int getTotalexp() { // 经验
		_xdb_verify_unsafe_();
		return totalexp;
	}

	@Override
	public int getState() { // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public java.util.Map<Integer, Integer> getStepawardtimes() { // key stepId, value 该step给的奖励次数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stepawardtimes"), stepawardtimes);
	}

	@Override
	public java.util.Map<Integer, Integer> getStepawardtimesAsData() { // key stepId, value 该step给的奖励次数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> stepawardtimes;
		InstanceTaskInfo _o_ = this;
		stepawardtimes = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
			stepawardtimes.put(_e_.getKey(), _e_.getValue());
		return stepawardtimes;
	}

	@Override
	public int getAwarding() { // 是否还能再拿奖励,0 不能, 1 可以拿
		_xdb_verify_unsafe_();
		return awarding;
	}

	@Override
	public void setId(int _v_) { // excel表唯一id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setInstanceonlyid(long _v_) { // instancetask 表里的唯一id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instanceonlyid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, instanceonlyid) {
					public void rollback() { instanceonlyid = _xdb_saved; }
				};}});
		instanceonlyid = _v_;
	}

	@Override
	public void setInstanceflag(int _v_) { // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instanceflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, instanceflag) {
					public void rollback() { instanceflag = _xdb_saved; }
				};}});
		instanceflag = _v_;
	}

	@Override
	public void setCounts(int _v_) { // 今天做了多少次
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "counts") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, counts) {
					public void rollback() { counts = _xdb_saved; }
				};}});
		counts = _v_;
	}

	@Override
	public void setDefaulttimes(int _v_) { // 一天默认做多少次
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "defaulttimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, defaulttimes) {
					public void rollback() { defaulttimes = _xdb_saved; }
				};}});
		defaulttimes = _v_;
	}

	@Override
	public void setCurrentstep(int _v_) { // 当前做到哪一阶段
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currentstep") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currentstep) {
					public void rollback() { currentstep = _xdb_saved; }
				};}});
		currentstep = _v_;
	}

	@Override
	public void setCurrenttaskid(int _v_) { // 当前做到哪一步了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currenttaskid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currenttaskid) {
					public void rollback() { currenttaskid = _xdb_saved; }
				};}});
		currenttaskid = _v_;
	}

	@Override
	public void setAccepttime(long _v_) { // 最后接任务时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "accepttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, accepttime) {
					public void rollback() { accepttime = _xdb_saved; }
				};}});
		accepttime = _v_;
	}

	@Override
	public void setTotalexp(int _v_) { // 经验
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalexp) {
					public void rollback() { totalexp = _xdb_saved; }
				};}});
		totalexp = _v_;
	}

	@Override
	public void setState(int _v_) { // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setAwarding(int _v_) { // 是否还能再拿奖励,0 不能, 1 可以拿
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "awarding") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, awarding) {
					public void rollback() { awarding = _xdb_saved; }
				};}});
		awarding = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceTaskInfo _o_ = null;
		if ( _o1_ instanceof InstanceTaskInfo ) _o_ = (InstanceTaskInfo)_o1_;
		else if ( _o1_ instanceof InstanceTaskInfo.Const ) _o_ = ((InstanceTaskInfo.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (instanceonlyid != _o_.instanceonlyid) return false;
		if (instanceflag != _o_.instanceflag) return false;
		if (counts != _o_.counts) return false;
		if (defaulttimes != _o_.defaulttimes) return false;
		if (currentstep != _o_.currentstep) return false;
		if (currenttaskid != _o_.currenttaskid) return false;
		if (accepttime != _o_.accepttime) return false;
		if (totalexp != _o_.totalexp) return false;
		if (state != _o_.state) return false;
		if (!stepawardtimes.equals(_o_.stepawardtimes)) return false;
		if (awarding != _o_.awarding) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += instanceonlyid;
		_h_ += instanceflag;
		_h_ += counts;
		_h_ += defaulttimes;
		_h_ += currentstep;
		_h_ += currenttaskid;
		_h_ += accepttime;
		_h_ += totalexp;
		_h_ += state;
		_h_ += stepawardtimes.hashCode();
		_h_ += awarding;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(instanceonlyid);
		_sb_.append(",");
		_sb_.append(instanceflag);
		_sb_.append(",");
		_sb_.append(counts);
		_sb_.append(",");
		_sb_.append(defaulttimes);
		_sb_.append(",");
		_sb_.append(currentstep);
		_sb_.append(",");
		_sb_.append(currenttaskid);
		_sb_.append(",");
		_sb_.append(accepttime);
		_sb_.append(",");
		_sb_.append(totalexp);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(stepawardtimes);
		_sb_.append(",");
		_sb_.append(awarding);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instanceonlyid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instanceflag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("counts"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("defaulttimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currentstep"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currenttaskid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("accepttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalexp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("stepawardtimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awarding"));
		return lb;
	}

	private class Const implements xbean.InstanceTaskInfo {
		InstanceTaskInfo nThis() {
			return InstanceTaskInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceTaskInfo copy() {
			return InstanceTaskInfo.this.copy();
		}

		@Override
		public xbean.InstanceTaskInfo toData() {
			return InstanceTaskInfo.this.toData();
		}

		public xbean.InstanceTaskInfo toBean() {
			return InstanceTaskInfo.this.toBean();
		}

		@Override
		public xbean.InstanceTaskInfo toDataIf() {
			return InstanceTaskInfo.this.toDataIf();
		}

		public xbean.InstanceTaskInfo toBeanIf() {
			return InstanceTaskInfo.this.toBeanIf();
		}

		@Override
		public int getId() { // excel表唯一id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public long getInstanceonlyid() { // instancetask 表里的唯一id
			_xdb_verify_unsafe_();
			return instanceonlyid;
		}

		@Override
		public int getInstanceflag() { // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
			_xdb_verify_unsafe_();
			return instanceflag;
		}

		@Override
		public int getCounts() { // 今天做了多少次
			_xdb_verify_unsafe_();
			return counts;
		}

		@Override
		public int getDefaulttimes() { // 一天默认做多少次
			_xdb_verify_unsafe_();
			return defaulttimes;
		}

		@Override
		public int getCurrentstep() { // 当前做到哪一阶段
			_xdb_verify_unsafe_();
			return currentstep;
		}

		@Override
		public int getCurrenttaskid() { // 当前做到哪一步了
			_xdb_verify_unsafe_();
			return currenttaskid;
		}

		@Override
		public long getAccepttime() { // 最后接任务时间
			_xdb_verify_unsafe_();
			return accepttime;
		}

		@Override
		public int getTotalexp() { // 经验
			_xdb_verify_unsafe_();
			return totalexp;
		}

		@Override
		public int getState() { // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public java.util.Map<Integer, Integer> getStepawardtimes() { // key stepId, value 该step给的奖励次数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stepawardtimes);
		}

		@Override
		public java.util.Map<Integer, Integer> getStepawardtimesAsData() { // key stepId, value 该step给的奖励次数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> stepawardtimes;
			InstanceTaskInfo _o_ = InstanceTaskInfo.this;
			stepawardtimes = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
				stepawardtimes.put(_e_.getKey(), _e_.getValue());
			return stepawardtimes;
		}

		@Override
		public int getAwarding() { // 是否还能再拿奖励,0 不能, 1 可以拿
			_xdb_verify_unsafe_();
			return awarding;
		}

		@Override
		public void setId(int _v_) { // excel表唯一id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInstanceonlyid(long _v_) { // instancetask 表里的唯一id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInstanceflag(int _v_) { // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCounts(int _v_) { // 今天做了多少次
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDefaulttimes(int _v_) { // 一天默认做多少次
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentstep(int _v_) { // 当前做到哪一阶段
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrenttaskid(int _v_) { // 当前做到哪一步了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAccepttime(long _v_) { // 最后接任务时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalexp(int _v_) { // 经验
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAwarding(int _v_) { // 是否还能再拿奖励,0 不能, 1 可以拿
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
			return InstanceTaskInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceTaskInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceTaskInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceTaskInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceTaskInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceTaskInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceTaskInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceTaskInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceTaskInfo.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceTaskInfo {
		private int id; // excel表唯一id
		private long instanceonlyid; // instancetask 表里的唯一id
		private int instanceflag; // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
		private int counts; // 今天做了多少次
		private int defaulttimes; // 一天默认做多少次
		private int currentstep; // 当前做到哪一阶段
		private int currenttaskid; // 当前做到哪一步了
		private long accepttime; // 最后接任务时间
		private int totalexp; // 经验
		private int state; // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
		private java.util.HashMap<Integer, Integer> stepawardtimes; // key stepId, value 该step给的奖励次数
		private int awarding; // 是否还能再拿奖励,0 不能, 1 可以拿

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			currentstep = 1;
			stepawardtimes = new java.util.HashMap<Integer, Integer>();
			awarding = 0;
		}

		Data(xbean.InstanceTaskInfo _o1_) {
			if (_o1_ instanceof InstanceTaskInfo) assign((InstanceTaskInfo)_o1_);
			else if (_o1_ instanceof InstanceTaskInfo.Data) assign((InstanceTaskInfo.Data)_o1_);
			else if (_o1_ instanceof InstanceTaskInfo.Const) assign(((InstanceTaskInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceTaskInfo _o_) {
			id = _o_.id;
			instanceonlyid = _o_.instanceonlyid;
			instanceflag = _o_.instanceflag;
			counts = _o_.counts;
			defaulttimes = _o_.defaulttimes;
			currentstep = _o_.currentstep;
			currenttaskid = _o_.currenttaskid;
			accepttime = _o_.accepttime;
			totalexp = _o_.totalexp;
			state = _o_.state;
			stepawardtimes = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
				stepawardtimes.put(_e_.getKey(), _e_.getValue());
			awarding = _o_.awarding;
		}

		private void assign(InstanceTaskInfo.Data _o_) {
			id = _o_.id;
			instanceonlyid = _o_.instanceonlyid;
			instanceflag = _o_.instanceflag;
			counts = _o_.counts;
			defaulttimes = _o_.defaulttimes;
			currentstep = _o_.currentstep;
			currenttaskid = _o_.currenttaskid;
			accepttime = _o_.accepttime;
			totalexp = _o_.totalexp;
			state = _o_.state;
			stepawardtimes = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
				stepawardtimes.put(_e_.getKey(), _e_.getValue());
			awarding = _o_.awarding;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(instanceonlyid);
			_os_.marshal(instanceflag);
			_os_.marshal(counts);
			_os_.marshal(defaulttimes);
			_os_.marshal(currentstep);
			_os_.marshal(currenttaskid);
			_os_.marshal(accepttime);
			_os_.marshal(totalexp);
			_os_.marshal(state);
			_os_.compact_uint32(stepawardtimes.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : stepawardtimes.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(awarding);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			instanceonlyid = _os_.unmarshal_long();
			instanceflag = _os_.unmarshal_int();
			counts = _os_.unmarshal_int();
			defaulttimes = _os_.unmarshal_int();
			currentstep = _os_.unmarshal_int();
			currenttaskid = _os_.unmarshal_int();
			accepttime = _os_.unmarshal_long();
			totalexp = _os_.unmarshal_int();
			state = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					stepawardtimes = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					stepawardtimes.put(_k_, _v_);
				}
			}
			awarding = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.InstanceTaskInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceTaskInfo toData() {
			return new Data(this);
		}

		public xbean.InstanceTaskInfo toBean() {
			return new InstanceTaskInfo(this, null, null);
		}

		@Override
		public xbean.InstanceTaskInfo toDataIf() {
			return this;
		}

		public xbean.InstanceTaskInfo toBeanIf() {
			return new InstanceTaskInfo(this, null, null);
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
		public int getId() { // excel表唯一id
			return id;
		}

		@Override
		public long getInstanceonlyid() { // instancetask 表里的唯一id
			return instanceonlyid;
		}

		@Override
		public int getInstanceflag() { // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
			return instanceflag;
		}

		@Override
		public int getCounts() { // 今天做了多少次
			return counts;
		}

		@Override
		public int getDefaulttimes() { // 一天默认做多少次
			return defaulttimes;
		}

		@Override
		public int getCurrentstep() { // 当前做到哪一阶段
			return currentstep;
		}

		@Override
		public int getCurrenttaskid() { // 当前做到哪一步了
			return currenttaskid;
		}

		@Override
		public long getAccepttime() { // 最后接任务时间
			return accepttime;
		}

		@Override
		public int getTotalexp() { // 经验
			return totalexp;
		}

		@Override
		public int getState() { // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
			return state;
		}

		@Override
		public java.util.Map<Integer, Integer> getStepawardtimes() { // key stepId, value 该step给的奖励次数
			return stepawardtimes;
		}

		@Override
		public java.util.Map<Integer, Integer> getStepawardtimesAsData() { // key stepId, value 该step给的奖励次数
			return stepawardtimes;
		}

		@Override
		public int getAwarding() { // 是否还能再拿奖励,0 不能, 1 可以拿
			return awarding;
		}

		@Override
		public void setId(int _v_) { // excel表唯一id
			id = _v_;
		}

		@Override
		public void setInstanceonlyid(long _v_) { // instancetask 表里的唯一id
			instanceonlyid = _v_;
		}

		@Override
		public void setInstanceflag(int _v_) { // 0=没有任务 1=任务成功 2=放弃任务 3=任务领取 4=任务超时
			instanceflag = _v_;
		}

		@Override
		public void setCounts(int _v_) { // 今天做了多少次
			counts = _v_;
		}

		@Override
		public void setDefaulttimes(int _v_) { // 一天默认做多少次
			defaulttimes = _v_;
		}

		@Override
		public void setCurrentstep(int _v_) { // 当前做到哪一阶段
			currentstep = _v_;
		}

		@Override
		public void setCurrenttaskid(int _v_) { // 当前做到哪一步了
			currenttaskid = _v_;
		}

		@Override
		public void setAccepttime(long _v_) { // 最后接任务时间
			accepttime = _v_;
		}

		@Override
		public void setTotalexp(int _v_) { // 经验
			totalexp = _v_;
		}

		@Override
		public void setState(int _v_) { // 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
			state = _v_;
		}

		@Override
		public void setAwarding(int _v_) { // 是否还能再拿奖励,0 不能, 1 可以拿
			awarding = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceTaskInfo.Data)) return false;
			InstanceTaskInfo.Data _o_ = (InstanceTaskInfo.Data) _o1_;
			if (id != _o_.id) return false;
			if (instanceonlyid != _o_.instanceonlyid) return false;
			if (instanceflag != _o_.instanceflag) return false;
			if (counts != _o_.counts) return false;
			if (defaulttimes != _o_.defaulttimes) return false;
			if (currentstep != _o_.currentstep) return false;
			if (currenttaskid != _o_.currenttaskid) return false;
			if (accepttime != _o_.accepttime) return false;
			if (totalexp != _o_.totalexp) return false;
			if (state != _o_.state) return false;
			if (!stepawardtimes.equals(_o_.stepawardtimes)) return false;
			if (awarding != _o_.awarding) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += instanceonlyid;
			_h_ += instanceflag;
			_h_ += counts;
			_h_ += defaulttimes;
			_h_ += currentstep;
			_h_ += currenttaskid;
			_h_ += accepttime;
			_h_ += totalexp;
			_h_ += state;
			_h_ += stepawardtimes.hashCode();
			_h_ += awarding;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(instanceonlyid);
			_sb_.append(",");
			_sb_.append(instanceflag);
			_sb_.append(",");
			_sb_.append(counts);
			_sb_.append(",");
			_sb_.append(defaulttimes);
			_sb_.append(",");
			_sb_.append(currentstep);
			_sb_.append(",");
			_sb_.append(currenttaskid);
			_sb_.append(",");
			_sb_.append(accepttime);
			_sb_.append(",");
			_sb_.append(totalexp);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(stepawardtimes);
			_sb_.append(",");
			_sb_.append(awarding);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

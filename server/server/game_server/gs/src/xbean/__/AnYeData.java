
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class AnYeData extends mkdb.XBean implements xbean.AnYeData {
	private long jointime; // 首次参加时间
	private long randomtime; // 上次随机时间(已经淘汰)
	private long refreshtime; // 上次清空时间(已经淘汰)
	private int times; // 任务累计次数
	private int todaynum; // 今天次数(已经淘汰)
	private long today; // 今天时间(已经淘汰)
	private int renxins; // 任性次数
	private int reqhelptimes; // 请求援助物品次数
	private int helptimes; // 援助别人物品次数
	private int legendtask; // 当前传说任务pos
	private java.util.HashMap<Integer, xbean.AnYeTask> anyetasks; // key为任务位置id

	@Override
	public void _reset_unsafe_() {
		jointime = 0L;
		randomtime = 0L;
		refreshtime = 0L;
		times = 0;
		todaynum = 0;
		today = 0L;
		renxins = 0;
		reqhelptimes = 0;
		helptimes = 0;
		legendtask = 999;
		anyetasks.clear();
	}

	AnYeData(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		legendtask = 999;
		anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>();
	}

	public AnYeData() {
		this(0, null, null);
	}

	public AnYeData(AnYeData _o_) {
		this(_o_, null, null);
	}

	AnYeData(xbean.AnYeData _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof AnYeData) assign((AnYeData)_o1_);
		else if (_o1_ instanceof AnYeData.Data) assign((AnYeData.Data)_o1_);
		else if (_o1_ instanceof AnYeData.Const) assign(((AnYeData.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(AnYeData _o_) {
		_o_._xdb_verify_unsafe_();
		jointime = _o_.jointime;
		randomtime = _o_.randomtime;
		refreshtime = _o_.refreshtime;
		times = _o_.times;
		todaynum = _o_.todaynum;
		today = _o_.today;
		renxins = _o_.renxins;
		reqhelptimes = _o_.reqhelptimes;
		helptimes = _o_.helptimes;
		legendtask = _o_.legendtask;
		anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>();
		for (java.util.Map.Entry<Integer, xbean.AnYeTask> _e_ : _o_.anyetasks.entrySet())
			anyetasks.put(_e_.getKey(), new AnYeTask(_e_.getValue(), this, "anyetasks"));
	}

	private void assign(AnYeData.Data _o_) {
		jointime = _o_.jointime;
		randomtime = _o_.randomtime;
		refreshtime = _o_.refreshtime;
		times = _o_.times;
		todaynum = _o_.todaynum;
		today = _o_.today;
		renxins = _o_.renxins;
		reqhelptimes = _o_.reqhelptimes;
		helptimes = _o_.helptimes;
		legendtask = _o_.legendtask;
		anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>();
		for (java.util.Map.Entry<Integer, xbean.AnYeTask> _e_ : _o_.anyetasks.entrySet())
			anyetasks.put(_e_.getKey(), new AnYeTask(_e_.getValue(), this, "anyetasks"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(jointime);
		_os_.marshal(randomtime);
		_os_.marshal(refreshtime);
		_os_.marshal(times);
		_os_.marshal(todaynum);
		_os_.marshal(today);
		_os_.marshal(renxins);
		_os_.marshal(reqhelptimes);
		_os_.marshal(helptimes);
		_os_.marshal(legendtask);
		_os_.compact_uint32(anyetasks.size());
		for (java.util.Map.Entry<Integer, xbean.AnYeTask> _e_ : anyetasks.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		jointime = _os_.unmarshal_long();
		randomtime = _os_.unmarshal_long();
		refreshtime = _os_.unmarshal_long();
		times = _os_.unmarshal_int();
		todaynum = _os_.unmarshal_int();
		today = _os_.unmarshal_long();
		renxins = _os_.unmarshal_int();
		reqhelptimes = _os_.unmarshal_int();
		helptimes = _os_.unmarshal_int();
		legendtask = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.AnYeTask _v_ = new AnYeTask(0, this, "anyetasks");
				_v_.unmarshal(_os_);
				anyetasks.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.AnYeData copy() {
		_xdb_verify_unsafe_();
		return new AnYeData(this);
	}

	@Override
	public xbean.AnYeData toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AnYeData toBean() {
		_xdb_verify_unsafe_();
		return new AnYeData(this); // same as copy()
	}

	@Override
	public xbean.AnYeData toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.AnYeData toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getJointime() { // 首次参加时间
		_xdb_verify_unsafe_();
		return jointime;
	}

	@Override
	public long getRandomtime() { // 上次随机时间(已经淘汰)
		_xdb_verify_unsafe_();
		return randomtime;
	}

	@Override
	public long getRefreshtime() { // 上次清空时间(已经淘汰)
		_xdb_verify_unsafe_();
		return refreshtime;
	}

	@Override
	public int getTimes() { // 任务累计次数
		_xdb_verify_unsafe_();
		return times;
	}

	@Override
	public int getTodaynum() { // 今天次数(已经淘汰)
		_xdb_verify_unsafe_();
		return todaynum;
	}

	@Override
	public long getToday() { // 今天时间(已经淘汰)
		_xdb_verify_unsafe_();
		return today;
	}

	@Override
	public int getRenxins() { // 任性次数
		_xdb_verify_unsafe_();
		return renxins;
	}

	@Override
	public int getReqhelptimes() { // 请求援助物品次数
		_xdb_verify_unsafe_();
		return reqhelptimes;
	}

	@Override
	public int getHelptimes() { // 援助别人物品次数
		_xdb_verify_unsafe_();
		return helptimes;
	}

	@Override
	public int getLegendtask() { // 当前传说任务pos
		_xdb_verify_unsafe_();
		return legendtask;
	}

	@Override
	public java.util.Map<Integer, xbean.AnYeTask> getAnyetasks() { // key为任务位置id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "anyetasks"), anyetasks);
	}

	@Override
	public java.util.Map<Integer, xbean.AnYeTask> getAnyetasksAsData() { // key为任务位置id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.AnYeTask> anyetasks;
		AnYeData _o_ = this;
		anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>();
		for (java.util.Map.Entry<Integer, xbean.AnYeTask> _e_ : _o_.anyetasks.entrySet())
			anyetasks.put(_e_.getKey(), new AnYeTask.Data(_e_.getValue()));
		return anyetasks;
	}

	@Override
	public void setJointime(long _v_) { // 首次参加时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "jointime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, jointime) {
					public void rollback() { jointime = _xdb_saved; }
				};}});
		jointime = _v_;
	}

	@Override
	public void setRandomtime(long _v_) { // 上次随机时间(已经淘汰)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "randomtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, randomtime) {
					public void rollback() { randomtime = _xdb_saved; }
				};}});
		randomtime = _v_;
	}

	@Override
	public void setRefreshtime(long _v_) { // 上次清空时间(已经淘汰)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "refreshtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, refreshtime) {
					public void rollback() { refreshtime = _xdb_saved; }
				};}});
		refreshtime = _v_;
	}

	@Override
	public void setTimes(int _v_) { // 任务累计次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "times") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, times) {
					public void rollback() { times = _xdb_saved; }
				};}});
		times = _v_;
	}

	@Override
	public void setTodaynum(int _v_) { // 今天次数(已经淘汰)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "todaynum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, todaynum) {
					public void rollback() { todaynum = _xdb_saved; }
				};}});
		todaynum = _v_;
	}

	@Override
	public void setToday(long _v_) { // 今天时间(已经淘汰)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "today") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, today) {
					public void rollback() { today = _xdb_saved; }
				};}});
		today = _v_;
	}

	@Override
	public void setRenxins(int _v_) { // 任性次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "renxins") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, renxins) {
					public void rollback() { renxins = _xdb_saved; }
				};}});
		renxins = _v_;
	}

	@Override
	public void setReqhelptimes(int _v_) { // 请求援助物品次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "reqhelptimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, reqhelptimes) {
					public void rollback() { reqhelptimes = _xdb_saved; }
				};}});
		reqhelptimes = _v_;
	}

	@Override
	public void setHelptimes(int _v_) { // 援助别人物品次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "helptimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, helptimes) {
					public void rollback() { helptimes = _xdb_saved; }
				};}});
		helptimes = _v_;
	}

	@Override
	public void setLegendtask(int _v_) { // 当前传说任务pos
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "legendtask") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, legendtask) {
					public void rollback() { legendtask = _xdb_saved; }
				};}});
		legendtask = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		AnYeData _o_ = null;
		if ( _o1_ instanceof AnYeData ) _o_ = (AnYeData)_o1_;
		else if ( _o1_ instanceof AnYeData.Const ) _o_ = ((AnYeData.Const)_o1_).nThis();
		else return false;
		if (jointime != _o_.jointime) return false;
		if (randomtime != _o_.randomtime) return false;
		if (refreshtime != _o_.refreshtime) return false;
		if (times != _o_.times) return false;
		if (todaynum != _o_.todaynum) return false;
		if (today != _o_.today) return false;
		if (renxins != _o_.renxins) return false;
		if (reqhelptimes != _o_.reqhelptimes) return false;
		if (helptimes != _o_.helptimes) return false;
		if (legendtask != _o_.legendtask) return false;
		if (!anyetasks.equals(_o_.anyetasks)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += jointime;
		_h_ += randomtime;
		_h_ += refreshtime;
		_h_ += times;
		_h_ += todaynum;
		_h_ += today;
		_h_ += renxins;
		_h_ += reqhelptimes;
		_h_ += helptimes;
		_h_ += legendtask;
		_h_ += anyetasks.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(jointime);
		_sb_.append(",");
		_sb_.append(randomtime);
		_sb_.append(",");
		_sb_.append(refreshtime);
		_sb_.append(",");
		_sb_.append(times);
		_sb_.append(",");
		_sb_.append(todaynum);
		_sb_.append(",");
		_sb_.append(today);
		_sb_.append(",");
		_sb_.append(renxins);
		_sb_.append(",");
		_sb_.append(reqhelptimes);
		_sb_.append(",");
		_sb_.append(helptimes);
		_sb_.append(",");
		_sb_.append(legendtask);
		_sb_.append(",");
		_sb_.append(anyetasks);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("jointime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("randomtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("refreshtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("times"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("todaynum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("today"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("renxins"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("reqhelptimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("helptimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("legendtask"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("anyetasks"));
		return lb;
	}

	private class Const implements xbean.AnYeData {
		AnYeData nThis() {
			return AnYeData.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.AnYeData copy() {
			return AnYeData.this.copy();
		}

		@Override
		public xbean.AnYeData toData() {
			return AnYeData.this.toData();
		}

		public xbean.AnYeData toBean() {
			return AnYeData.this.toBean();
		}

		@Override
		public xbean.AnYeData toDataIf() {
			return AnYeData.this.toDataIf();
		}

		public xbean.AnYeData toBeanIf() {
			return AnYeData.this.toBeanIf();
		}

		@Override
		public long getJointime() { // 首次参加时间
			_xdb_verify_unsafe_();
			return jointime;
		}

		@Override
		public long getRandomtime() { // 上次随机时间(已经淘汰)
			_xdb_verify_unsafe_();
			return randomtime;
		}

		@Override
		public long getRefreshtime() { // 上次清空时间(已经淘汰)
			_xdb_verify_unsafe_();
			return refreshtime;
		}

		@Override
		public int getTimes() { // 任务累计次数
			_xdb_verify_unsafe_();
			return times;
		}

		@Override
		public int getTodaynum() { // 今天次数(已经淘汰)
			_xdb_verify_unsafe_();
			return todaynum;
		}

		@Override
		public long getToday() { // 今天时间(已经淘汰)
			_xdb_verify_unsafe_();
			return today;
		}

		@Override
		public int getRenxins() { // 任性次数
			_xdb_verify_unsafe_();
			return renxins;
		}

		@Override
		public int getReqhelptimes() { // 请求援助物品次数
			_xdb_verify_unsafe_();
			return reqhelptimes;
		}

		@Override
		public int getHelptimes() { // 援助别人物品次数
			_xdb_verify_unsafe_();
			return helptimes;
		}

		@Override
		public int getLegendtask() { // 当前传说任务pos
			_xdb_verify_unsafe_();
			return legendtask;
		}

		@Override
		public java.util.Map<Integer, xbean.AnYeTask> getAnyetasks() { // key为任务位置id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(anyetasks);
		}

		@Override
		public java.util.Map<Integer, xbean.AnYeTask> getAnyetasksAsData() { // key为任务位置id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.AnYeTask> anyetasks;
			AnYeData _o_ = AnYeData.this;
			anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>();
			for (java.util.Map.Entry<Integer, xbean.AnYeTask> _e_ : _o_.anyetasks.entrySet())
				anyetasks.put(_e_.getKey(), new AnYeTask.Data(_e_.getValue()));
			return anyetasks;
		}

		@Override
		public void setJointime(long _v_) { // 首次参加时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRandomtime(long _v_) { // 上次随机时间(已经淘汰)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRefreshtime(long _v_) { // 上次清空时间(已经淘汰)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimes(int _v_) { // 任务累计次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTodaynum(int _v_) { // 今天次数(已经淘汰)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setToday(long _v_) { // 今天时间(已经淘汰)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRenxins(int _v_) { // 任性次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReqhelptimes(int _v_) { // 请求援助物品次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHelptimes(int _v_) { // 援助别人物品次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLegendtask(int _v_) { // 当前传说任务pos
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
			return AnYeData.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return AnYeData.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return AnYeData.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return AnYeData.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return AnYeData.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return AnYeData.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return AnYeData.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return AnYeData.this.hashCode();
		}

		@Override
		public String toString() {
			return AnYeData.this.toString();
		}

	}

	public static final class Data implements xbean.AnYeData {
		private long jointime; // 首次参加时间
		private long randomtime; // 上次随机时间(已经淘汰)
		private long refreshtime; // 上次清空时间(已经淘汰)
		private int times; // 任务累计次数
		private int todaynum; // 今天次数(已经淘汰)
		private long today; // 今天时间(已经淘汰)
		private int renxins; // 任性次数
		private int reqhelptimes; // 请求援助物品次数
		private int helptimes; // 援助别人物品次数
		private int legendtask; // 当前传说任务pos
		private java.util.HashMap<Integer, xbean.AnYeTask> anyetasks; // key为任务位置id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			legendtask = 999;
			anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>();
		}

		Data(xbean.AnYeData _o1_) {
			if (_o1_ instanceof AnYeData) assign((AnYeData)_o1_);
			else if (_o1_ instanceof AnYeData.Data) assign((AnYeData.Data)_o1_);
			else if (_o1_ instanceof AnYeData.Const) assign(((AnYeData.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(AnYeData _o_) {
			jointime = _o_.jointime;
			randomtime = _o_.randomtime;
			refreshtime = _o_.refreshtime;
			times = _o_.times;
			todaynum = _o_.todaynum;
			today = _o_.today;
			renxins = _o_.renxins;
			reqhelptimes = _o_.reqhelptimes;
			helptimes = _o_.helptimes;
			legendtask = _o_.legendtask;
			anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>();
			for (java.util.Map.Entry<Integer, xbean.AnYeTask> _e_ : _o_.anyetasks.entrySet())
				anyetasks.put(_e_.getKey(), new AnYeTask.Data(_e_.getValue()));
		}

		private void assign(AnYeData.Data _o_) {
			jointime = _o_.jointime;
			randomtime = _o_.randomtime;
			refreshtime = _o_.refreshtime;
			times = _o_.times;
			todaynum = _o_.todaynum;
			today = _o_.today;
			renxins = _o_.renxins;
			reqhelptimes = _o_.reqhelptimes;
			helptimes = _o_.helptimes;
			legendtask = _o_.legendtask;
			anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>();
			for (java.util.Map.Entry<Integer, xbean.AnYeTask> _e_ : _o_.anyetasks.entrySet())
				anyetasks.put(_e_.getKey(), new AnYeTask.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(jointime);
			_os_.marshal(randomtime);
			_os_.marshal(refreshtime);
			_os_.marshal(times);
			_os_.marshal(todaynum);
			_os_.marshal(today);
			_os_.marshal(renxins);
			_os_.marshal(reqhelptimes);
			_os_.marshal(helptimes);
			_os_.marshal(legendtask);
			_os_.compact_uint32(anyetasks.size());
			for (java.util.Map.Entry<Integer, xbean.AnYeTask> _e_ : anyetasks.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			jointime = _os_.unmarshal_long();
			randomtime = _os_.unmarshal_long();
			refreshtime = _os_.unmarshal_long();
			times = _os_.unmarshal_int();
			todaynum = _os_.unmarshal_int();
			today = _os_.unmarshal_long();
			renxins = _os_.unmarshal_int();
			reqhelptimes = _os_.unmarshal_int();
			helptimes = _os_.unmarshal_int();
			legendtask = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					anyetasks = new java.util.HashMap<Integer, xbean.AnYeTask>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.AnYeTask _v_ = xbean.Pod.newAnYeTaskData();
					_v_.unmarshal(_os_);
					anyetasks.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.AnYeData copy() {
			return new Data(this);
		}

		@Override
		public xbean.AnYeData toData() {
			return new Data(this);
		}

		public xbean.AnYeData toBean() {
			return new AnYeData(this, null, null);
		}

		@Override
		public xbean.AnYeData toDataIf() {
			return this;
		}

		public xbean.AnYeData toBeanIf() {
			return new AnYeData(this, null, null);
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
		public long getJointime() { // 首次参加时间
			return jointime;
		}

		@Override
		public long getRandomtime() { // 上次随机时间(已经淘汰)
			return randomtime;
		}

		@Override
		public long getRefreshtime() { // 上次清空时间(已经淘汰)
			return refreshtime;
		}

		@Override
		public int getTimes() { // 任务累计次数
			return times;
		}

		@Override
		public int getTodaynum() { // 今天次数(已经淘汰)
			return todaynum;
		}

		@Override
		public long getToday() { // 今天时间(已经淘汰)
			return today;
		}

		@Override
		public int getRenxins() { // 任性次数
			return renxins;
		}

		@Override
		public int getReqhelptimes() { // 请求援助物品次数
			return reqhelptimes;
		}

		@Override
		public int getHelptimes() { // 援助别人物品次数
			return helptimes;
		}

		@Override
		public int getLegendtask() { // 当前传说任务pos
			return legendtask;
		}

		@Override
		public java.util.Map<Integer, xbean.AnYeTask> getAnyetasks() { // key为任务位置id
			return anyetasks;
		}

		@Override
		public java.util.Map<Integer, xbean.AnYeTask> getAnyetasksAsData() { // key为任务位置id
			return anyetasks;
		}

		@Override
		public void setJointime(long _v_) { // 首次参加时间
			jointime = _v_;
		}

		@Override
		public void setRandomtime(long _v_) { // 上次随机时间(已经淘汰)
			randomtime = _v_;
		}

		@Override
		public void setRefreshtime(long _v_) { // 上次清空时间(已经淘汰)
			refreshtime = _v_;
		}

		@Override
		public void setTimes(int _v_) { // 任务累计次数
			times = _v_;
		}

		@Override
		public void setTodaynum(int _v_) { // 今天次数(已经淘汰)
			todaynum = _v_;
		}

		@Override
		public void setToday(long _v_) { // 今天时间(已经淘汰)
			today = _v_;
		}

		@Override
		public void setRenxins(int _v_) { // 任性次数
			renxins = _v_;
		}

		@Override
		public void setReqhelptimes(int _v_) { // 请求援助物品次数
			reqhelptimes = _v_;
		}

		@Override
		public void setHelptimes(int _v_) { // 援助别人物品次数
			helptimes = _v_;
		}

		@Override
		public void setLegendtask(int _v_) { // 当前传说任务pos
			legendtask = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof AnYeData.Data)) return false;
			AnYeData.Data _o_ = (AnYeData.Data) _o1_;
			if (jointime != _o_.jointime) return false;
			if (randomtime != _o_.randomtime) return false;
			if (refreshtime != _o_.refreshtime) return false;
			if (times != _o_.times) return false;
			if (todaynum != _o_.todaynum) return false;
			if (today != _o_.today) return false;
			if (renxins != _o_.renxins) return false;
			if (reqhelptimes != _o_.reqhelptimes) return false;
			if (helptimes != _o_.helptimes) return false;
			if (legendtask != _o_.legendtask) return false;
			if (!anyetasks.equals(_o_.anyetasks)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += jointime;
			_h_ += randomtime;
			_h_ += refreshtime;
			_h_ += times;
			_h_ += todaynum;
			_h_ += today;
			_h_ += renxins;
			_h_ += reqhelptimes;
			_h_ += helptimes;
			_h_ += legendtask;
			_h_ += anyetasks.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(jointime);
			_sb_.append(",");
			_sb_.append(randomtime);
			_sb_.append(",");
			_sb_.append(refreshtime);
			_sb_.append(",");
			_sb_.append(times);
			_sb_.append(",");
			_sb_.append(todaynum);
			_sb_.append(",");
			_sb_.append(today);
			_sb_.append(",");
			_sb_.append(renxins);
			_sb_.append(",");
			_sb_.append(reqhelptimes);
			_sb_.append(",");
			_sb_.append(helptimes);
			_sb_.append(",");
			_sb_.append(legendtask);
			_sb_.append(",");
			_sb_.append(anyetasks);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

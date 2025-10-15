
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceInfo extends mkdb.XBean implements xbean.InstanceInfo {
	private long ownerid; // 副本的组织者,队长id
	private mkdb.util.SetX<Long> roleids; // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
	private java.util.ArrayList<xbean.InstanceNpcInfo> npcs; // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
	private java.util.ArrayList<xbean.InstanceNpcInfo> asistnpcs; // 召出来的辅助npc列表
	private int copyflag; // 副本类型  1为单人副本   2为组队副本
	private int instanceid; // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
	private int currentstep; // 当前做到哪一阶段
	private int currentstate; // 当前阶段的状态
	private int currenttaskid; // 当前做到哪一步了
	private int serviceid; // 当前服务id
	private int currentmapid; // 当前在哪个地图
	private long accepttime; // 接任务时间
	private int finalcounts; // 已经完成的次数
	private java.util.HashMap<Integer, Integer> subsaves; // 子进度状态，value是子进度计数

	@Override
	public void _reset_unsafe_() {
		ownerid = 0L;
		roleids.clear();
		npcs.clear();
		asistnpcs.clear();
		copyflag = 0;
		instanceid = 0;
		currentstep = 1;
		currentstate = 0;
		currenttaskid = 0;
		serviceid = 0;
		currentmapid = 0;
		accepttime = 0L;
		finalcounts = 0;
		subsaves.clear();
	}

	InstanceInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		roleids = new mkdb.util.SetX<Long>();
		npcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		asistnpcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		currentstep = 1;
		subsaves = new java.util.HashMap<Integer, Integer>();
	}

	public InstanceInfo() {
		this(0, null, null);
	}

	public InstanceInfo(InstanceInfo _o_) {
		this(_o_, null, null);
	}

	InstanceInfo(xbean.InstanceInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceInfo) assign((InstanceInfo)_o1_);
		else if (_o1_ instanceof InstanceInfo.Data) assign((InstanceInfo.Data)_o1_);
		else if (_o1_ instanceof InstanceInfo.Const) assign(((InstanceInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceInfo _o_) {
		_o_._xdb_verify_unsafe_();
		ownerid = _o_.ownerid;
		roleids = new mkdb.util.SetX<Long>();
		roleids.addAll(_o_.roleids);
		npcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		for (xbean.InstanceNpcInfo _v_ : _o_.npcs)
			npcs.add(new InstanceNpcInfo(_v_, this, "npcs"));
		asistnpcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		for (xbean.InstanceNpcInfo _v_ : _o_.asistnpcs)
			asistnpcs.add(new InstanceNpcInfo(_v_, this, "asistnpcs"));
		copyflag = _o_.copyflag;
		instanceid = _o_.instanceid;
		currentstep = _o_.currentstep;
		currentstate = _o_.currentstate;
		currenttaskid = _o_.currenttaskid;
		serviceid = _o_.serviceid;
		currentmapid = _o_.currentmapid;
		accepttime = _o_.accepttime;
		finalcounts = _o_.finalcounts;
		subsaves = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
			subsaves.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(InstanceInfo.Data _o_) {
		ownerid = _o_.ownerid;
		roleids = new mkdb.util.SetX<Long>();
		roleids.addAll(_o_.roleids);
		npcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		for (xbean.InstanceNpcInfo _v_ : _o_.npcs)
			npcs.add(new InstanceNpcInfo(_v_, this, "npcs"));
		asistnpcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		for (xbean.InstanceNpcInfo _v_ : _o_.asistnpcs)
			asistnpcs.add(new InstanceNpcInfo(_v_, this, "asistnpcs"));
		copyflag = _o_.copyflag;
		instanceid = _o_.instanceid;
		currentstep = _o_.currentstep;
		currentstate = _o_.currentstate;
		currenttaskid = _o_.currenttaskid;
		serviceid = _o_.serviceid;
		currentmapid = _o_.currentmapid;
		accepttime = _o_.accepttime;
		finalcounts = _o_.finalcounts;
		subsaves = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
			subsaves.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(ownerid);
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(npcs.size());
		for (xbean.InstanceNpcInfo _v_ : npcs) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(asistnpcs.size());
		for (xbean.InstanceNpcInfo _v_ : asistnpcs) {
			_v_.marshal(_os_);
		}
		_os_.marshal(copyflag);
		_os_.marshal(instanceid);
		_os_.marshal(currentstep);
		_os_.marshal(currentstate);
		_os_.marshal(currenttaskid);
		_os_.marshal(serviceid);
		_os_.marshal(currentmapid);
		_os_.marshal(accepttime);
		_os_.marshal(finalcounts);
		_os_.compact_uint32(subsaves.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : subsaves.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		ownerid = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.InstanceNpcInfo _v_ = new InstanceNpcInfo(0, this, "npcs");
			_v_.unmarshal(_os_);
			npcs.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.InstanceNpcInfo _v_ = new InstanceNpcInfo(0, this, "asistnpcs");
			_v_.unmarshal(_os_);
			asistnpcs.add(_v_);
		}
		copyflag = _os_.unmarshal_int();
		instanceid = _os_.unmarshal_int();
		currentstep = _os_.unmarshal_int();
		currentstate = _os_.unmarshal_int();
		currenttaskid = _os_.unmarshal_int();
		serviceid = _os_.unmarshal_int();
		currentmapid = _os_.unmarshal_int();
		accepttime = _os_.unmarshal_long();
		finalcounts = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				subsaves = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				subsaves.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.InstanceInfo copy() {
		_xdb_verify_unsafe_();
		return new InstanceInfo(this);
	}

	@Override
	public xbean.InstanceInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceInfo toBean() {
		_xdb_verify_unsafe_();
		return new InstanceInfo(this); // same as copy()
	}

	@Override
	public xbean.InstanceInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getOwnerid() { // 副本的组织者,队长id
		_xdb_verify_unsafe_();
		return ownerid;
	}

	@Override
	public java.util.Set<Long> getRoleids() { // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logSet(new mkdb.LogKey(this, "roleids"), roleids);
	}

	public java.util.Set<Long> getRoleidsAsData() { // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
		_xdb_verify_unsafe_();
		java.util.Set<Long> roleids;
		InstanceInfo _o_ = this;
		roleids = new mkdb.util.SetX<Long>();
		roleids.addAll(_o_.roleids);
		return roleids;
	}

	@Override
	public java.util.List<xbean.InstanceNpcInfo> getNpcs() { // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "npcs"), npcs);
	}

	public java.util.List<xbean.InstanceNpcInfo> getNpcsAsData() { // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
		_xdb_verify_unsafe_();
		java.util.List<xbean.InstanceNpcInfo> npcs;
		InstanceInfo _o_ = this;
		npcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		for (xbean.InstanceNpcInfo _v_ : _o_.npcs)
			npcs.add(new InstanceNpcInfo.Data(_v_));
		return npcs;
	}

	@Override
	public java.util.List<xbean.InstanceNpcInfo> getAsistnpcs() { // 召出来的辅助npc列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "asistnpcs"), asistnpcs);
	}

	public java.util.List<xbean.InstanceNpcInfo> getAsistnpcsAsData() { // 召出来的辅助npc列表
		_xdb_verify_unsafe_();
		java.util.List<xbean.InstanceNpcInfo> asistnpcs;
		InstanceInfo _o_ = this;
		asistnpcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		for (xbean.InstanceNpcInfo _v_ : _o_.asistnpcs)
			asistnpcs.add(new InstanceNpcInfo.Data(_v_));
		return asistnpcs;
	}

	@Override
	public int getCopyflag() { // 副本类型  1为单人副本   2为组队副本
		_xdb_verify_unsafe_();
		return copyflag;
	}

	@Override
	public int getInstanceid() { // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
		_xdb_verify_unsafe_();
		return instanceid;
	}

	@Override
	public int getCurrentstep() { // 当前做到哪一阶段
		_xdb_verify_unsafe_();
		return currentstep;
	}

	@Override
	public int getCurrentstate() { // 当前阶段的状态
		_xdb_verify_unsafe_();
		return currentstate;
	}

	@Override
	public int getCurrenttaskid() { // 当前做到哪一步了
		_xdb_verify_unsafe_();
		return currenttaskid;
	}

	@Override
	public int getServiceid() { // 当前服务id
		_xdb_verify_unsafe_();
		return serviceid;
	}

	@Override
	public int getCurrentmapid() { // 当前在哪个地图
		_xdb_verify_unsafe_();
		return currentmapid;
	}

	@Override
	public long getAccepttime() { // 接任务时间
		_xdb_verify_unsafe_();
		return accepttime;
	}

	@Override
	public int getFinalcounts() { // 已经完成的次数
		_xdb_verify_unsafe_();
		return finalcounts;
	}

	@Override
	public java.util.Map<Integer, Integer> getSubsaves() { // 子进度状态，value是子进度计数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "subsaves"), subsaves);
	}

	@Override
	public java.util.Map<Integer, Integer> getSubsavesAsData() { // 子进度状态，value是子进度计数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> subsaves;
		InstanceInfo _o_ = this;
		subsaves = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
			subsaves.put(_e_.getKey(), _e_.getValue());
		return subsaves;
	}

	@Override
	public void setOwnerid(long _v_) { // 副本的组织者,队长id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ownerid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, ownerid) {
					public void rollback() { ownerid = _xdb_saved; }
				};}});
		ownerid = _v_;
	}

	@Override
	public void setCopyflag(int _v_) { // 副本类型  1为单人副本   2为组队副本
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "copyflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, copyflag) {
					public void rollback() { copyflag = _xdb_saved; }
				};}});
		copyflag = _v_;
	}

	@Override
	public void setInstanceid(int _v_) { // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instanceid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, instanceid) {
					public void rollback() { instanceid = _xdb_saved; }
				};}});
		instanceid = _v_;
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
	public void setCurrentstate(int _v_) { // 当前阶段的状态
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currentstate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currentstate) {
					public void rollback() { currentstate = _xdb_saved; }
				};}});
		currentstate = _v_;
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
	public void setServiceid(int _v_) { // 当前服务id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "serviceid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, serviceid) {
					public void rollback() { serviceid = _xdb_saved; }
				};}});
		serviceid = _v_;
	}

	@Override
	public void setCurrentmapid(int _v_) { // 当前在哪个地图
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "currentmapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, currentmapid) {
					public void rollback() { currentmapid = _xdb_saved; }
				};}});
		currentmapid = _v_;
	}

	@Override
	public void setAccepttime(long _v_) { // 接任务时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "accepttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, accepttime) {
					public void rollback() { accepttime = _xdb_saved; }
				};}});
		accepttime = _v_;
	}

	@Override
	public void setFinalcounts(int _v_) { // 已经完成的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "finalcounts") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, finalcounts) {
					public void rollback() { finalcounts = _xdb_saved; }
				};}});
		finalcounts = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceInfo _o_ = null;
		if ( _o1_ instanceof InstanceInfo ) _o_ = (InstanceInfo)_o1_;
		else if ( _o1_ instanceof InstanceInfo.Const ) _o_ = ((InstanceInfo.Const)_o1_).nThis();
		else return false;
		if (ownerid != _o_.ownerid) return false;
		if (!roleids.equals(_o_.roleids)) return false;
		if (!npcs.equals(_o_.npcs)) return false;
		if (!asistnpcs.equals(_o_.asistnpcs)) return false;
		if (copyflag != _o_.copyflag) return false;
		if (instanceid != _o_.instanceid) return false;
		if (currentstep != _o_.currentstep) return false;
		if (currentstate != _o_.currentstate) return false;
		if (currenttaskid != _o_.currenttaskid) return false;
		if (serviceid != _o_.serviceid) return false;
		if (currentmapid != _o_.currentmapid) return false;
		if (accepttime != _o_.accepttime) return false;
		if (finalcounts != _o_.finalcounts) return false;
		if (!subsaves.equals(_o_.subsaves)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += ownerid;
		_h_ += roleids.hashCode();
		_h_ += npcs.hashCode();
		_h_ += asistnpcs.hashCode();
		_h_ += copyflag;
		_h_ += instanceid;
		_h_ += currentstep;
		_h_ += currentstate;
		_h_ += currenttaskid;
		_h_ += serviceid;
		_h_ += currentmapid;
		_h_ += accepttime;
		_h_ += finalcounts;
		_h_ += subsaves.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ownerid);
		_sb_.append(",");
		_sb_.append(roleids);
		_sb_.append(",");
		_sb_.append(npcs);
		_sb_.append(",");
		_sb_.append(asistnpcs);
		_sb_.append(",");
		_sb_.append(copyflag);
		_sb_.append(",");
		_sb_.append(instanceid);
		_sb_.append(",");
		_sb_.append(currentstep);
		_sb_.append(",");
		_sb_.append(currentstate);
		_sb_.append(",");
		_sb_.append(currenttaskid);
		_sb_.append(",");
		_sb_.append(serviceid);
		_sb_.append(",");
		_sb_.append(currentmapid);
		_sb_.append(",");
		_sb_.append(accepttime);
		_sb_.append(",");
		_sb_.append(finalcounts);
		_sb_.append(",");
		_sb_.append(subsaves);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ownerid"));
		lb.add(new mkdb.logs.ListenableSet().setVarName("roleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npcs"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("asistnpcs"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("copyflag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instanceid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currentstep"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currentstate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currenttaskid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("serviceid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currentmapid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("accepttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("finalcounts"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("subsaves"));
		return lb;
	}

	private class Const implements xbean.InstanceInfo {
		InstanceInfo nThis() {
			return InstanceInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceInfo copy() {
			return InstanceInfo.this.copy();
		}

		@Override
		public xbean.InstanceInfo toData() {
			return InstanceInfo.this.toData();
		}

		public xbean.InstanceInfo toBean() {
			return InstanceInfo.this.toBean();
		}

		@Override
		public xbean.InstanceInfo toDataIf() {
			return InstanceInfo.this.toDataIf();
		}

		public xbean.InstanceInfo toBeanIf() {
			return InstanceInfo.this.toBeanIf();
		}

		@Override
		public long getOwnerid() { // 副本的组织者,队长id
			_xdb_verify_unsafe_();
			return ownerid;
		}

		@Override
		public java.util.Set<Long> getRoleids() { // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constSet(roleids);
		}

		public java.util.Set<Long> getRoleidsAsData() { // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
			_xdb_verify_unsafe_();
			java.util.Set<Long> roleids;
			InstanceInfo _o_ = InstanceInfo.this;
		roleids = new mkdb.util.SetX<Long>();
		roleids.addAll(_o_.roleids);
			return roleids;
		}

		@Override
		public java.util.List<xbean.InstanceNpcInfo> getNpcs() { // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(npcs);
		}

		public java.util.List<xbean.InstanceNpcInfo> getNpcsAsData() { // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
			_xdb_verify_unsafe_();
			java.util.List<xbean.InstanceNpcInfo> npcs;
			InstanceInfo _o_ = InstanceInfo.this;
		npcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		for (xbean.InstanceNpcInfo _v_ : _o_.npcs)
			npcs.add(new InstanceNpcInfo.Data(_v_));
			return npcs;
		}

		@Override
		public java.util.List<xbean.InstanceNpcInfo> getAsistnpcs() { // 召出来的辅助npc列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(asistnpcs);
		}

		public java.util.List<xbean.InstanceNpcInfo> getAsistnpcsAsData() { // 召出来的辅助npc列表
			_xdb_verify_unsafe_();
			java.util.List<xbean.InstanceNpcInfo> asistnpcs;
			InstanceInfo _o_ = InstanceInfo.this;
		asistnpcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
		for (xbean.InstanceNpcInfo _v_ : _o_.asistnpcs)
			asistnpcs.add(new InstanceNpcInfo.Data(_v_));
			return asistnpcs;
		}

		@Override
		public int getCopyflag() { // 副本类型  1为单人副本   2为组队副本
			_xdb_verify_unsafe_();
			return copyflag;
		}

		@Override
		public int getInstanceid() { // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
			_xdb_verify_unsafe_();
			return instanceid;
		}

		@Override
		public int getCurrentstep() { // 当前做到哪一阶段
			_xdb_verify_unsafe_();
			return currentstep;
		}

		@Override
		public int getCurrentstate() { // 当前阶段的状态
			_xdb_verify_unsafe_();
			return currentstate;
		}

		@Override
		public int getCurrenttaskid() { // 当前做到哪一步了
			_xdb_verify_unsafe_();
			return currenttaskid;
		}

		@Override
		public int getServiceid() { // 当前服务id
			_xdb_verify_unsafe_();
			return serviceid;
		}

		@Override
		public int getCurrentmapid() { // 当前在哪个地图
			_xdb_verify_unsafe_();
			return currentmapid;
		}

		@Override
		public long getAccepttime() { // 接任务时间
			_xdb_verify_unsafe_();
			return accepttime;
		}

		@Override
		public int getFinalcounts() { // 已经完成的次数
			_xdb_verify_unsafe_();
			return finalcounts;
		}

		@Override
		public java.util.Map<Integer, Integer> getSubsaves() { // 子进度状态，value是子进度计数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(subsaves);
		}

		@Override
		public java.util.Map<Integer, Integer> getSubsavesAsData() { // 子进度状态，value是子进度计数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> subsaves;
			InstanceInfo _o_ = InstanceInfo.this;
			subsaves = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
				subsaves.put(_e_.getKey(), _e_.getValue());
			return subsaves;
		}

		@Override
		public void setOwnerid(long _v_) { // 副本的组织者,队长id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCopyflag(int _v_) { // 副本类型  1为单人副本   2为组队副本
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInstanceid(int _v_) { // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentstep(int _v_) { // 当前做到哪一阶段
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentstate(int _v_) { // 当前阶段的状态
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrenttaskid(int _v_) { // 当前做到哪一步了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setServiceid(int _v_) { // 当前服务id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCurrentmapid(int _v_) { // 当前在哪个地图
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAccepttime(long _v_) { // 接任务时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFinalcounts(int _v_) { // 已经完成的次数
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
			return InstanceInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceInfo.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceInfo {
		private long ownerid; // 副本的组织者,队长id
		private java.util.HashSet<Long> roleids; // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
		private java.util.ArrayList<xbean.InstanceNpcInfo> npcs; // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
		private java.util.ArrayList<xbean.InstanceNpcInfo> asistnpcs; // 召出来的辅助npc列表
		private int copyflag; // 副本类型  1为单人副本   2为组队副本
		private int instanceid; // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
		private int currentstep; // 当前做到哪一阶段
		private int currentstate; // 当前阶段的状态
		private int currenttaskid; // 当前做到哪一步了
		private int serviceid; // 当前服务id
		private int currentmapid; // 当前在哪个地图
		private long accepttime; // 接任务时间
		private int finalcounts; // 已经完成的次数
		private java.util.HashMap<Integer, Integer> subsaves; // 子进度状态，value是子进度计数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			roleids = new java.util.HashSet<Long>();
			npcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
			asistnpcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
			currentstep = 1;
			subsaves = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.InstanceInfo _o1_) {
			if (_o1_ instanceof InstanceInfo) assign((InstanceInfo)_o1_);
			else if (_o1_ instanceof InstanceInfo.Data) assign((InstanceInfo.Data)_o1_);
			else if (_o1_ instanceof InstanceInfo.Const) assign(((InstanceInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceInfo _o_) {
			ownerid = _o_.ownerid;
			roleids = new java.util.HashSet<Long>();
			roleids.addAll(_o_.roleids);
			npcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
			for (xbean.InstanceNpcInfo _v_ : _o_.npcs)
				npcs.add(new InstanceNpcInfo.Data(_v_));
			asistnpcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
			for (xbean.InstanceNpcInfo _v_ : _o_.asistnpcs)
				asistnpcs.add(new InstanceNpcInfo.Data(_v_));
			copyflag = _o_.copyflag;
			instanceid = _o_.instanceid;
			currentstep = _o_.currentstep;
			currentstate = _o_.currentstate;
			currenttaskid = _o_.currenttaskid;
			serviceid = _o_.serviceid;
			currentmapid = _o_.currentmapid;
			accepttime = _o_.accepttime;
			finalcounts = _o_.finalcounts;
			subsaves = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
				subsaves.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(InstanceInfo.Data _o_) {
			ownerid = _o_.ownerid;
			roleids = new java.util.HashSet<Long>();
			roleids.addAll(_o_.roleids);
			npcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
			for (xbean.InstanceNpcInfo _v_ : _o_.npcs)
				npcs.add(new InstanceNpcInfo.Data(_v_));
			asistnpcs = new java.util.ArrayList<xbean.InstanceNpcInfo>();
			for (xbean.InstanceNpcInfo _v_ : _o_.asistnpcs)
				asistnpcs.add(new InstanceNpcInfo.Data(_v_));
			copyflag = _o_.copyflag;
			instanceid = _o_.instanceid;
			currentstep = _o_.currentstep;
			currentstate = _o_.currentstate;
			currenttaskid = _o_.currenttaskid;
			serviceid = _o_.serviceid;
			currentmapid = _o_.currentmapid;
			accepttime = _o_.accepttime;
			finalcounts = _o_.finalcounts;
			subsaves = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.subsaves.entrySet())
				subsaves.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(ownerid);
			_os_.compact_uint32(roleids.size());
			for (Long _v_ : roleids) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(npcs.size());
			for (xbean.InstanceNpcInfo _v_ : npcs) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(asistnpcs.size());
			for (xbean.InstanceNpcInfo _v_ : asistnpcs) {
				_v_.marshal(_os_);
			}
			_os_.marshal(copyflag);
			_os_.marshal(instanceid);
			_os_.marshal(currentstep);
			_os_.marshal(currentstate);
			_os_.marshal(currenttaskid);
			_os_.marshal(serviceid);
			_os_.marshal(currentmapid);
			_os_.marshal(accepttime);
			_os_.marshal(finalcounts);
			_os_.compact_uint32(subsaves.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : subsaves.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			ownerid = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				roleids.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.InstanceNpcInfo _v_ = xbean.Pod.newInstanceNpcInfoData();
				_v_.unmarshal(_os_);
				npcs.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.InstanceNpcInfo _v_ = xbean.Pod.newInstanceNpcInfoData();
				_v_.unmarshal(_os_);
				asistnpcs.add(_v_);
			}
			copyflag = _os_.unmarshal_int();
			instanceid = _os_.unmarshal_int();
			currentstep = _os_.unmarshal_int();
			currentstate = _os_.unmarshal_int();
			currenttaskid = _os_.unmarshal_int();
			serviceid = _os_.unmarshal_int();
			currentmapid = _os_.unmarshal_int();
			accepttime = _os_.unmarshal_long();
			finalcounts = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					subsaves = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					subsaves.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.InstanceInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceInfo toData() {
			return new Data(this);
		}

		public xbean.InstanceInfo toBean() {
			return new InstanceInfo(this, null, null);
		}

		@Override
		public xbean.InstanceInfo toDataIf() {
			return this;
		}

		public xbean.InstanceInfo toBeanIf() {
			return new InstanceInfo(this, null, null);
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
		public long getOwnerid() { // 副本的组织者,队长id
			return ownerid;
		}

		@Override
		public java.util.Set<Long> getRoleids() { // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
			return roleids;
		}

		@Override
		public java.util.Set<Long> getRoleidsAsData() { // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
			return roleids;
		}

		@Override
		public java.util.List<xbean.InstanceNpcInfo> getNpcs() { // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
			return npcs;
		}

		@Override
		public java.util.List<xbean.InstanceNpcInfo> getNpcsAsData() { // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
			return npcs;
		}

		@Override
		public java.util.List<xbean.InstanceNpcInfo> getAsistnpcs() { // 召出来的辅助npc列表
			return asistnpcs;
		}

		@Override
		public java.util.List<xbean.InstanceNpcInfo> getAsistnpcsAsData() { // 召出来的辅助npc列表
			return asistnpcs;
		}

		@Override
		public int getCopyflag() { // 副本类型  1为单人副本   2为组队副本
			return copyflag;
		}

		@Override
		public int getInstanceid() { // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
			return instanceid;
		}

		@Override
		public int getCurrentstep() { // 当前做到哪一阶段
			return currentstep;
		}

		@Override
		public int getCurrentstate() { // 当前阶段的状态
			return currentstate;
		}

		@Override
		public int getCurrenttaskid() { // 当前做到哪一步了
			return currenttaskid;
		}

		@Override
		public int getServiceid() { // 当前服务id
			return serviceid;
		}

		@Override
		public int getCurrentmapid() { // 当前在哪个地图
			return currentmapid;
		}

		@Override
		public long getAccepttime() { // 接任务时间
			return accepttime;
		}

		@Override
		public int getFinalcounts() { // 已经完成的次数
			return finalcounts;
		}

		@Override
		public java.util.Map<Integer, Integer> getSubsaves() { // 子进度状态，value是子进度计数
			return subsaves;
		}

		@Override
		public java.util.Map<Integer, Integer> getSubsavesAsData() { // 子进度状态，value是子进度计数
			return subsaves;
		}

		@Override
		public void setOwnerid(long _v_) { // 副本的组织者,队长id
			ownerid = _v_;
		}

		@Override
		public void setCopyflag(int _v_) { // 副本类型  1为单人副本   2为组队副本
			copyflag = _v_;
		}

		@Override
		public void setInstanceid(int _v_) { // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
			instanceid = _v_;
		}

		@Override
		public void setCurrentstep(int _v_) { // 当前做到哪一阶段
			currentstep = _v_;
		}

		@Override
		public void setCurrentstate(int _v_) { // 当前阶段的状态
			currentstate = _v_;
		}

		@Override
		public void setCurrenttaskid(int _v_) { // 当前做到哪一步了
			currenttaskid = _v_;
		}

		@Override
		public void setServiceid(int _v_) { // 当前服务id
			serviceid = _v_;
		}

		@Override
		public void setCurrentmapid(int _v_) { // 当前在哪个地图
			currentmapid = _v_;
		}

		@Override
		public void setAccepttime(long _v_) { // 接任务时间
			accepttime = _v_;
		}

		@Override
		public void setFinalcounts(int _v_) { // 已经完成的次数
			finalcounts = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceInfo.Data)) return false;
			InstanceInfo.Data _o_ = (InstanceInfo.Data) _o1_;
			if (ownerid != _o_.ownerid) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			if (!npcs.equals(_o_.npcs)) return false;
			if (!asistnpcs.equals(_o_.asistnpcs)) return false;
			if (copyflag != _o_.copyflag) return false;
			if (instanceid != _o_.instanceid) return false;
			if (currentstep != _o_.currentstep) return false;
			if (currentstate != _o_.currentstate) return false;
			if (currenttaskid != _o_.currenttaskid) return false;
			if (serviceid != _o_.serviceid) return false;
			if (currentmapid != _o_.currentmapid) return false;
			if (accepttime != _o_.accepttime) return false;
			if (finalcounts != _o_.finalcounts) return false;
			if (!subsaves.equals(_o_.subsaves)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += ownerid;
			_h_ += roleids.hashCode();
			_h_ += npcs.hashCode();
			_h_ += asistnpcs.hashCode();
			_h_ += copyflag;
			_h_ += instanceid;
			_h_ += currentstep;
			_h_ += currentstate;
			_h_ += currenttaskid;
			_h_ += serviceid;
			_h_ += currentmapid;
			_h_ += accepttime;
			_h_ += finalcounts;
			_h_ += subsaves.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(ownerid);
			_sb_.append(",");
			_sb_.append(roleids);
			_sb_.append(",");
			_sb_.append(npcs);
			_sb_.append(",");
			_sb_.append(asistnpcs);
			_sb_.append(",");
			_sb_.append(copyflag);
			_sb_.append(",");
			_sb_.append(instanceid);
			_sb_.append(",");
			_sb_.append(currentstep);
			_sb_.append(",");
			_sb_.append(currentstate);
			_sb_.append(",");
			_sb_.append(currenttaskid);
			_sb_.append(",");
			_sb_.append(serviceid);
			_sb_.append(",");
			_sb_.append(currentmapid);
			_sb_.append(",");
			_sb_.append(accepttime);
			_sb_.append(",");
			_sb_.append(finalcounts);
			_sb_.append(",");
			_sb_.append(subsaves);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

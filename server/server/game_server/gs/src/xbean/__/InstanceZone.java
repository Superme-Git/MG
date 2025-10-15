
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceZone extends mkdb.XBean implements xbean.InstanceZone {
	private int instzoneid; // 
	private long ownerid; // 
	private int zonestate; // 副本区域当前的状态
	private long extid; // 副本如果有额外存储的数据,-1为没有
	private java.util.LinkedList<Long> roleids; // 当前在副本内的角色
	private java.util.HashMap<Integer, xbean.InstanceSave> saves; // 进度状态
	private java.util.HashMap<Integer, xbean.InstanceNpc> npcs; // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
	private java.util.HashMap<Integer, xbean.InstanceNpcServ> services; // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
	private java.util.HashMap<Integer, xbean.InstanceTimer> timers; // 副本定时器，key=定时器id
	private long starttime; // 副本的开始时间(表里配置的时间,不是创建的时间)

	@Override
	public void _reset_unsafe_() {
		instzoneid = 0;
		ownerid = 0L;
		zonestate = 0;
		extid = -1;
		roleids.clear();
		saves.clear();
		npcs.clear();
		services.clear();
		timers.clear();
		starttime = 0L;
	}

	InstanceZone(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		extid = -1;
		roleids = new java.util.LinkedList<Long>();
		saves = new java.util.HashMap<Integer, xbean.InstanceSave>();
		npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>();
		services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>();
		timers = new java.util.HashMap<Integer, xbean.InstanceTimer>();
	}

	public InstanceZone() {
		this(0, null, null);
	}

	public InstanceZone(InstanceZone _o_) {
		this(_o_, null, null);
	}

	InstanceZone(xbean.InstanceZone _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceZone) assign((InstanceZone)_o1_);
		else if (_o1_ instanceof InstanceZone.Data) assign((InstanceZone.Data)_o1_);
		else if (_o1_ instanceof InstanceZone.Const) assign(((InstanceZone.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceZone _o_) {
		_o_._xdb_verify_unsafe_();
		instzoneid = _o_.instzoneid;
		ownerid = _o_.ownerid;
		zonestate = _o_.zonestate;
		extid = _o_.extid;
		roleids = new java.util.LinkedList<Long>();
		roleids.addAll(_o_.roleids);
		saves = new java.util.HashMap<Integer, xbean.InstanceSave>();
		for (java.util.Map.Entry<Integer, xbean.InstanceSave> _e_ : _o_.saves.entrySet())
			saves.put(_e_.getKey(), new InstanceSave(_e_.getValue(), this, "saves"));
		npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>();
		for (java.util.Map.Entry<Integer, xbean.InstanceNpc> _e_ : _o_.npcs.entrySet())
			npcs.put(_e_.getKey(), new InstanceNpc(_e_.getValue(), this, "npcs"));
		services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>();
		for (java.util.Map.Entry<Integer, xbean.InstanceNpcServ> _e_ : _o_.services.entrySet())
			services.put(_e_.getKey(), new InstanceNpcServ(_e_.getValue(), this, "services"));
		timers = new java.util.HashMap<Integer, xbean.InstanceTimer>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTimer> _e_ : _o_.timers.entrySet())
			timers.put(_e_.getKey(), new InstanceTimer(_e_.getValue(), this, "timers"));
		starttime = _o_.starttime;
	}

	private void assign(InstanceZone.Data _o_) {
		instzoneid = _o_.instzoneid;
		ownerid = _o_.ownerid;
		zonestate = _o_.zonestate;
		extid = _o_.extid;
		roleids = new java.util.LinkedList<Long>();
		roleids.addAll(_o_.roleids);
		saves = new java.util.HashMap<Integer, xbean.InstanceSave>();
		for (java.util.Map.Entry<Integer, xbean.InstanceSave> _e_ : _o_.saves.entrySet())
			saves.put(_e_.getKey(), new InstanceSave(_e_.getValue(), this, "saves"));
		npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>();
		for (java.util.Map.Entry<Integer, xbean.InstanceNpc> _e_ : _o_.npcs.entrySet())
			npcs.put(_e_.getKey(), new InstanceNpc(_e_.getValue(), this, "npcs"));
		services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>();
		for (java.util.Map.Entry<Integer, xbean.InstanceNpcServ> _e_ : _o_.services.entrySet())
			services.put(_e_.getKey(), new InstanceNpcServ(_e_.getValue(), this, "services"));
		timers = new java.util.HashMap<Integer, xbean.InstanceTimer>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTimer> _e_ : _o_.timers.entrySet())
			timers.put(_e_.getKey(), new InstanceTimer(_e_.getValue(), this, "timers"));
		starttime = _o_.starttime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(instzoneid);
		_os_.marshal(ownerid);
		_os_.marshal(zonestate);
		_os_.marshal(extid);
		_os_.compact_uint32(roleids.size());
		for (Long _v_ : roleids) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(saves.size());
		for (java.util.Map.Entry<Integer, xbean.InstanceSave> _e_ : saves.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(npcs.size());
		for (java.util.Map.Entry<Integer, xbean.InstanceNpc> _e_ : npcs.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(services.size());
		for (java.util.Map.Entry<Integer, xbean.InstanceNpcServ> _e_ : services.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(timers.size());
		for (java.util.Map.Entry<Integer, xbean.InstanceTimer> _e_ : timers.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(starttime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		instzoneid = _os_.unmarshal_int();
		ownerid = _os_.unmarshal_long();
		zonestate = _os_.unmarshal_int();
		extid = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			roleids.add(_v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				saves = new java.util.HashMap<Integer, xbean.InstanceSave>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.InstanceSave _v_ = new InstanceSave(0, this, "saves");
				_v_.unmarshal(_os_);
				saves.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.InstanceNpc _v_ = new InstanceNpc(0, this, "npcs");
				_v_.unmarshal(_os_);
				npcs.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.InstanceNpcServ _v_ = new InstanceNpcServ(0, this, "services");
				_v_.unmarshal(_os_);
				services.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				timers = new java.util.HashMap<Integer, xbean.InstanceTimer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.InstanceTimer _v_ = new InstanceTimer(0, this, "timers");
				_v_.unmarshal(_os_);
				timers.put(_k_, _v_);
			}
		}
		starttime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.InstanceZone copy() {
		_xdb_verify_unsafe_();
		return new InstanceZone(this);
	}

	@Override
	public xbean.InstanceZone toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceZone toBean() {
		_xdb_verify_unsafe_();
		return new InstanceZone(this); // same as copy()
	}

	@Override
	public xbean.InstanceZone toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceZone toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getInstzoneid() { // 
		_xdb_verify_unsafe_();
		return instzoneid;
	}

	@Override
	public long getOwnerid() { // 
		_xdb_verify_unsafe_();
		return ownerid;
	}

	@Override
	public int getZonestate() { // 副本区域当前的状态
		_xdb_verify_unsafe_();
		return zonestate;
	}

	@Override
	public long getExtid() { // 副本如果有额外存储的数据,-1为没有
		_xdb_verify_unsafe_();
		return extid;
	}

	@Override
	public java.util.List<Long> getRoleids() { // 当前在副本内的角色
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "roleids"), roleids);
	}

	public java.util.List<Long> getRoleidsAsData() { // 当前在副本内的角色
		_xdb_verify_unsafe_();
		java.util.List<Long> roleids;
		InstanceZone _o_ = this;
		roleids = new java.util.LinkedList<Long>();
		roleids.addAll(_o_.roleids);
		return roleids;
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceSave> getSaves() { // 进度状态
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "saves"), saves);
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceSave> getSavesAsData() { // 进度状态
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.InstanceSave> saves;
		InstanceZone _o_ = this;
		saves = new java.util.HashMap<Integer, xbean.InstanceSave>();
		for (java.util.Map.Entry<Integer, xbean.InstanceSave> _e_ : _o_.saves.entrySet())
			saves.put(_e_.getKey(), new InstanceSave.Data(_e_.getValue()));
		return saves;
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceNpc> getNpcs() { // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "npcs"), npcs);
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceNpc> getNpcsAsData() { // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.InstanceNpc> npcs;
		InstanceZone _o_ = this;
		npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>();
		for (java.util.Map.Entry<Integer, xbean.InstanceNpc> _e_ : _o_.npcs.entrySet())
			npcs.put(_e_.getKey(), new InstanceNpc.Data(_e_.getValue()));
		return npcs;
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceNpcServ> getServices() { // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "services"), services);
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceNpcServ> getServicesAsData() { // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.InstanceNpcServ> services;
		InstanceZone _o_ = this;
		services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>();
		for (java.util.Map.Entry<Integer, xbean.InstanceNpcServ> _e_ : _o_.services.entrySet())
			services.put(_e_.getKey(), new InstanceNpcServ.Data(_e_.getValue()));
		return services;
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceTimer> getTimers() { // 副本定时器，key=定时器id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "timers"), timers);
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceTimer> getTimersAsData() { // 副本定时器，key=定时器id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.InstanceTimer> timers;
		InstanceZone _o_ = this;
		timers = new java.util.HashMap<Integer, xbean.InstanceTimer>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTimer> _e_ : _o_.timers.entrySet())
			timers.put(_e_.getKey(), new InstanceTimer.Data(_e_.getValue()));
		return timers;
	}

	@Override
	public long getStarttime() { // 副本的开始时间(表里配置的时间,不是创建的时间)
		_xdb_verify_unsafe_();
		return starttime;
	}

	@Override
	public void setInstzoneid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instzoneid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, instzoneid) {
					public void rollback() { instzoneid = _xdb_saved; }
				};}});
		instzoneid = _v_;
	}

	@Override
	public void setOwnerid(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ownerid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, ownerid) {
					public void rollback() { ownerid = _xdb_saved; }
				};}});
		ownerid = _v_;
	}

	@Override
	public void setZonestate(int _v_) { // 副本区域当前的状态
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "zonestate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, zonestate) {
					public void rollback() { zonestate = _xdb_saved; }
				};}});
		zonestate = _v_;
	}

	@Override
	public void setExtid(long _v_) { // 副本如果有额外存储的数据,-1为没有
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "extid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, extid) {
					public void rollback() { extid = _xdb_saved; }
				};}});
		extid = _v_;
	}

	@Override
	public void setStarttime(long _v_) { // 副本的开始时间(表里配置的时间,不是创建的时间)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "starttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, starttime) {
					public void rollback() { starttime = _xdb_saved; }
				};}});
		starttime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceZone _o_ = null;
		if ( _o1_ instanceof InstanceZone ) _o_ = (InstanceZone)_o1_;
		else if ( _o1_ instanceof InstanceZone.Const ) _o_ = ((InstanceZone.Const)_o1_).nThis();
		else return false;
		if (instzoneid != _o_.instzoneid) return false;
		if (ownerid != _o_.ownerid) return false;
		if (zonestate != _o_.zonestate) return false;
		if (extid != _o_.extid) return false;
		if (!roleids.equals(_o_.roleids)) return false;
		if (!saves.equals(_o_.saves)) return false;
		if (!npcs.equals(_o_.npcs)) return false;
		if (!services.equals(_o_.services)) return false;
		if (!timers.equals(_o_.timers)) return false;
		if (starttime != _o_.starttime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += instzoneid;
		_h_ += ownerid;
		_h_ += zonestate;
		_h_ += extid;
		_h_ += roleids.hashCode();
		_h_ += saves.hashCode();
		_h_ += npcs.hashCode();
		_h_ += services.hashCode();
		_h_ += timers.hashCode();
		_h_ += starttime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instzoneid);
		_sb_.append(",");
		_sb_.append(ownerid);
		_sb_.append(",");
		_sb_.append(zonestate);
		_sb_.append(",");
		_sb_.append(extid);
		_sb_.append(",");
		_sb_.append(roleids);
		_sb_.append(",");
		_sb_.append(saves);
		_sb_.append(",");
		_sb_.append(npcs);
		_sb_.append(",");
		_sb_.append(services);
		_sb_.append(",");
		_sb_.append(timers);
		_sb_.append(",");
		_sb_.append(starttime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instzoneid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ownerid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("zonestate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("extid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("saves"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("npcs"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("services"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("timers"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("starttime"));
		return lb;
	}

	private class Const implements xbean.InstanceZone {
		InstanceZone nThis() {
			return InstanceZone.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceZone copy() {
			return InstanceZone.this.copy();
		}

		@Override
		public xbean.InstanceZone toData() {
			return InstanceZone.this.toData();
		}

		public xbean.InstanceZone toBean() {
			return InstanceZone.this.toBean();
		}

		@Override
		public xbean.InstanceZone toDataIf() {
			return InstanceZone.this.toDataIf();
		}

		public xbean.InstanceZone toBeanIf() {
			return InstanceZone.this.toBeanIf();
		}

		@Override
		public int getInstzoneid() { // 
			_xdb_verify_unsafe_();
			return instzoneid;
		}

		@Override
		public long getOwnerid() { // 
			_xdb_verify_unsafe_();
			return ownerid;
		}

		@Override
		public int getZonestate() { // 副本区域当前的状态
			_xdb_verify_unsafe_();
			return zonestate;
		}

		@Override
		public long getExtid() { // 副本如果有额外存储的数据,-1为没有
			_xdb_verify_unsafe_();
			return extid;
		}

		@Override
		public java.util.List<Long> getRoleids() { // 当前在副本内的角色
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(roleids);
		}

		public java.util.List<Long> getRoleidsAsData() { // 当前在副本内的角色
			_xdb_verify_unsafe_();
			java.util.List<Long> roleids;
			InstanceZone _o_ = InstanceZone.this;
		roleids = new java.util.LinkedList<Long>();
		roleids.addAll(_o_.roleids);
			return roleids;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceSave> getSaves() { // 进度状态
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(saves);
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceSave> getSavesAsData() { // 进度状态
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.InstanceSave> saves;
			InstanceZone _o_ = InstanceZone.this;
			saves = new java.util.HashMap<Integer, xbean.InstanceSave>();
			for (java.util.Map.Entry<Integer, xbean.InstanceSave> _e_ : _o_.saves.entrySet())
				saves.put(_e_.getKey(), new InstanceSave.Data(_e_.getValue()));
			return saves;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceNpc> getNpcs() { // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(npcs);
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceNpc> getNpcsAsData() { // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.InstanceNpc> npcs;
			InstanceZone _o_ = InstanceZone.this;
			npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>();
			for (java.util.Map.Entry<Integer, xbean.InstanceNpc> _e_ : _o_.npcs.entrySet())
				npcs.put(_e_.getKey(), new InstanceNpc.Data(_e_.getValue()));
			return npcs;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceNpcServ> getServices() { // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(services);
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceNpcServ> getServicesAsData() { // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.InstanceNpcServ> services;
			InstanceZone _o_ = InstanceZone.this;
			services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>();
			for (java.util.Map.Entry<Integer, xbean.InstanceNpcServ> _e_ : _o_.services.entrySet())
				services.put(_e_.getKey(), new InstanceNpcServ.Data(_e_.getValue()));
			return services;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTimer> getTimers() { // 副本定时器，key=定时器id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(timers);
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTimer> getTimersAsData() { // 副本定时器，key=定时器id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.InstanceTimer> timers;
			InstanceZone _o_ = InstanceZone.this;
			timers = new java.util.HashMap<Integer, xbean.InstanceTimer>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTimer> _e_ : _o_.timers.entrySet())
				timers.put(_e_.getKey(), new InstanceTimer.Data(_e_.getValue()));
			return timers;
		}

		@Override
		public long getStarttime() { // 副本的开始时间(表里配置的时间,不是创建的时间)
			_xdb_verify_unsafe_();
			return starttime;
		}

		@Override
		public void setInstzoneid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOwnerid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setZonestate(int _v_) { // 副本区域当前的状态
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExtid(long _v_) { // 副本如果有额外存储的数据,-1为没有
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStarttime(long _v_) { // 副本的开始时间(表里配置的时间,不是创建的时间)
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
			return InstanceZone.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceZone.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceZone.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceZone.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceZone.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceZone.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceZone.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceZone.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceZone.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceZone {
		private int instzoneid; // 
		private long ownerid; // 
		private int zonestate; // 副本区域当前的状态
		private long extid; // 副本如果有额外存储的数据,-1为没有
		private java.util.LinkedList<Long> roleids; // 当前在副本内的角色
		private java.util.HashMap<Integer, xbean.InstanceSave> saves; // 进度状态
		private java.util.HashMap<Integer, xbean.InstanceNpc> npcs; // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
		private java.util.HashMap<Integer, xbean.InstanceNpcServ> services; // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
		private java.util.HashMap<Integer, xbean.InstanceTimer> timers; // 副本定时器，key=定时器id
		private long starttime; // 副本的开始时间(表里配置的时间,不是创建的时间)

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			extid = -1;
			roleids = new java.util.LinkedList<Long>();
			saves = new java.util.HashMap<Integer, xbean.InstanceSave>();
			npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>();
			services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>();
			timers = new java.util.HashMap<Integer, xbean.InstanceTimer>();
		}

		Data(xbean.InstanceZone _o1_) {
			if (_o1_ instanceof InstanceZone) assign((InstanceZone)_o1_);
			else if (_o1_ instanceof InstanceZone.Data) assign((InstanceZone.Data)_o1_);
			else if (_o1_ instanceof InstanceZone.Const) assign(((InstanceZone.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceZone _o_) {
			instzoneid = _o_.instzoneid;
			ownerid = _o_.ownerid;
			zonestate = _o_.zonestate;
			extid = _o_.extid;
			roleids = new java.util.LinkedList<Long>();
			roleids.addAll(_o_.roleids);
			saves = new java.util.HashMap<Integer, xbean.InstanceSave>();
			for (java.util.Map.Entry<Integer, xbean.InstanceSave> _e_ : _o_.saves.entrySet())
				saves.put(_e_.getKey(), new InstanceSave.Data(_e_.getValue()));
			npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>();
			for (java.util.Map.Entry<Integer, xbean.InstanceNpc> _e_ : _o_.npcs.entrySet())
				npcs.put(_e_.getKey(), new InstanceNpc.Data(_e_.getValue()));
			services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>();
			for (java.util.Map.Entry<Integer, xbean.InstanceNpcServ> _e_ : _o_.services.entrySet())
				services.put(_e_.getKey(), new InstanceNpcServ.Data(_e_.getValue()));
			timers = new java.util.HashMap<Integer, xbean.InstanceTimer>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTimer> _e_ : _o_.timers.entrySet())
				timers.put(_e_.getKey(), new InstanceTimer.Data(_e_.getValue()));
			starttime = _o_.starttime;
		}

		private void assign(InstanceZone.Data _o_) {
			instzoneid = _o_.instzoneid;
			ownerid = _o_.ownerid;
			zonestate = _o_.zonestate;
			extid = _o_.extid;
			roleids = new java.util.LinkedList<Long>();
			roleids.addAll(_o_.roleids);
			saves = new java.util.HashMap<Integer, xbean.InstanceSave>();
			for (java.util.Map.Entry<Integer, xbean.InstanceSave> _e_ : _o_.saves.entrySet())
				saves.put(_e_.getKey(), new InstanceSave.Data(_e_.getValue()));
			npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>();
			for (java.util.Map.Entry<Integer, xbean.InstanceNpc> _e_ : _o_.npcs.entrySet())
				npcs.put(_e_.getKey(), new InstanceNpc.Data(_e_.getValue()));
			services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>();
			for (java.util.Map.Entry<Integer, xbean.InstanceNpcServ> _e_ : _o_.services.entrySet())
				services.put(_e_.getKey(), new InstanceNpcServ.Data(_e_.getValue()));
			timers = new java.util.HashMap<Integer, xbean.InstanceTimer>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTimer> _e_ : _o_.timers.entrySet())
				timers.put(_e_.getKey(), new InstanceTimer.Data(_e_.getValue()));
			starttime = _o_.starttime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(instzoneid);
			_os_.marshal(ownerid);
			_os_.marshal(zonestate);
			_os_.marshal(extid);
			_os_.compact_uint32(roleids.size());
			for (Long _v_ : roleids) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(saves.size());
			for (java.util.Map.Entry<Integer, xbean.InstanceSave> _e_ : saves.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(npcs.size());
			for (java.util.Map.Entry<Integer, xbean.InstanceNpc> _e_ : npcs.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(services.size());
			for (java.util.Map.Entry<Integer, xbean.InstanceNpcServ> _e_ : services.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(timers.size());
			for (java.util.Map.Entry<Integer, xbean.InstanceTimer> _e_ : timers.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(starttime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			instzoneid = _os_.unmarshal_int();
			ownerid = _os_.unmarshal_long();
			zonestate = _os_.unmarshal_int();
			extid = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				roleids.add(_v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					saves = new java.util.HashMap<Integer, xbean.InstanceSave>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.InstanceSave _v_ = xbean.Pod.newInstanceSaveData();
					_v_.unmarshal(_os_);
					saves.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					npcs = new java.util.HashMap<Integer, xbean.InstanceNpc>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.InstanceNpc _v_ = xbean.Pod.newInstanceNpcData();
					_v_.unmarshal(_os_);
					npcs.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					services = new java.util.HashMap<Integer, xbean.InstanceNpcServ>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.InstanceNpcServ _v_ = xbean.Pod.newInstanceNpcServData();
					_v_.unmarshal(_os_);
					services.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					timers = new java.util.HashMap<Integer, xbean.InstanceTimer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.InstanceTimer _v_ = xbean.Pod.newInstanceTimerData();
					_v_.unmarshal(_os_);
					timers.put(_k_, _v_);
				}
			}
			starttime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.InstanceZone copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceZone toData() {
			return new Data(this);
		}

		public xbean.InstanceZone toBean() {
			return new InstanceZone(this, null, null);
		}

		@Override
		public xbean.InstanceZone toDataIf() {
			return this;
		}

		public xbean.InstanceZone toBeanIf() {
			return new InstanceZone(this, null, null);
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
		public int getInstzoneid() { // 
			return instzoneid;
		}

		@Override
		public long getOwnerid() { // 
			return ownerid;
		}

		@Override
		public int getZonestate() { // 副本区域当前的状态
			return zonestate;
		}

		@Override
		public long getExtid() { // 副本如果有额外存储的数据,-1为没有
			return extid;
		}

		@Override
		public java.util.List<Long> getRoleids() { // 当前在副本内的角色
			return roleids;
		}

		@Override
		public java.util.List<Long> getRoleidsAsData() { // 当前在副本内的角色
			return roleids;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceSave> getSaves() { // 进度状态
			return saves;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceSave> getSavesAsData() { // 进度状态
			return saves;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceNpc> getNpcs() { // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
			return npcs;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceNpc> getNpcsAsData() { // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
			return npcs;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceNpcServ> getServices() { // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
			return services;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceNpcServ> getServicesAsData() { // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
			return services;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTimer> getTimers() { // 副本定时器，key=定时器id
			return timers;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTimer> getTimersAsData() { // 副本定时器，key=定时器id
			return timers;
		}

		@Override
		public long getStarttime() { // 副本的开始时间(表里配置的时间,不是创建的时间)
			return starttime;
		}

		@Override
		public void setInstzoneid(int _v_) { // 
			instzoneid = _v_;
		}

		@Override
		public void setOwnerid(long _v_) { // 
			ownerid = _v_;
		}

		@Override
		public void setZonestate(int _v_) { // 副本区域当前的状态
			zonestate = _v_;
		}

		@Override
		public void setExtid(long _v_) { // 副本如果有额外存储的数据,-1为没有
			extid = _v_;
		}

		@Override
		public void setStarttime(long _v_) { // 副本的开始时间(表里配置的时间,不是创建的时间)
			starttime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceZone.Data)) return false;
			InstanceZone.Data _o_ = (InstanceZone.Data) _o1_;
			if (instzoneid != _o_.instzoneid) return false;
			if (ownerid != _o_.ownerid) return false;
			if (zonestate != _o_.zonestate) return false;
			if (extid != _o_.extid) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			if (!saves.equals(_o_.saves)) return false;
			if (!npcs.equals(_o_.npcs)) return false;
			if (!services.equals(_o_.services)) return false;
			if (!timers.equals(_o_.timers)) return false;
			if (starttime != _o_.starttime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += instzoneid;
			_h_ += ownerid;
			_h_ += zonestate;
			_h_ += extid;
			_h_ += roleids.hashCode();
			_h_ += saves.hashCode();
			_h_ += npcs.hashCode();
			_h_ += services.hashCode();
			_h_ += timers.hashCode();
			_h_ += starttime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(instzoneid);
			_sb_.append(",");
			_sb_.append(ownerid);
			_sb_.append(",");
			_sb_.append(zonestate);
			_sb_.append(",");
			_sb_.append(extid);
			_sb_.append(",");
			_sb_.append(roleids);
			_sb_.append(",");
			_sb_.append(saves);
			_sb_.append(",");
			_sb_.append(npcs);
			_sb_.append(",");
			_sb_.append(services);
			_sb_.append(",");
			_sb_.append(timers);
			_sb_.append(",");
			_sb_.append(starttime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

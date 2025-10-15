
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TeamInfo extends mkdb.XBean implements xbean.TeamInfo {
	private long teamleaderid; // 队长ID
	private long commanderroleid; // 指挥ID
	private int minlevel; // 最小级别
	private int maxlevel; // 最大级别
	private int formation; // 阵型id
	private int formationlevel; // 光环等级
	private long switchleaderid; // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
	private long switchleadertime; // 记录改变队长操作的时间，判断超时用（30秒）
	private long successswitchtime; // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
	private java.util.LinkedList<xbean.TeamMember> members; // 
	private java.util.HashMap<Long, Long> applierids; // 
	private java.util.HashMap<Long, Long> invitingids; // 被邀请者的IDs 和时间 map by changhao
	private int state; // 队伍状态，参考协议中的fire.pb.team.TeamState
	private int smapid; // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
	private java.util.HashMap<Long, Long> hugs; // 拥抱的队员们
	private int targetid; // 
	private long onekeytimestamp; // 一键喊话时间戳
	private long createtime; // 创建队伍时的时间戳

	@Override
	public void _reset_unsafe_() {
		teamleaderid = 0L;
		commanderroleid = 0L;
		minlevel = 0;
		maxlevel = 0;
		formation = 0;
		formationlevel = 0;
		switchleaderid = 0L;
		switchleadertime = 0L;
		successswitchtime = 0L;
		members.clear();
		applierids.clear();
		invitingids.clear();
		state = 0;
		smapid = 0;
		hugs.clear();
		targetid = 0;
		onekeytimestamp = 0L;
		createtime = 0L;
	}

	TeamInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		members = new java.util.LinkedList<xbean.TeamMember>();
		applierids = new java.util.HashMap<Long, Long>();
		invitingids = new java.util.HashMap<Long, Long>();
		hugs = new java.util.HashMap<Long, Long>();
	}

	public TeamInfo() {
		this(0, null, null);
	}

	public TeamInfo(TeamInfo _o_) {
		this(_o_, null, null);
	}

	TeamInfo(xbean.TeamInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TeamInfo) assign((TeamInfo)_o1_);
		else if (_o1_ instanceof TeamInfo.Data) assign((TeamInfo.Data)_o1_);
		else if (_o1_ instanceof TeamInfo.Const) assign(((TeamInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TeamInfo _o_) {
		_o_._xdb_verify_unsafe_();
		teamleaderid = _o_.teamleaderid;
		commanderroleid = _o_.commanderroleid;
		minlevel = _o_.minlevel;
		maxlevel = _o_.maxlevel;
		formation = _o_.formation;
		formationlevel = _o_.formationlevel;
		switchleaderid = _o_.switchleaderid;
		switchleadertime = _o_.switchleadertime;
		successswitchtime = _o_.successswitchtime;
		members = new java.util.LinkedList<xbean.TeamMember>();
		for (xbean.TeamMember _v_ : _o_.members)
			members.add(new TeamMember(_v_, this, "members"));
		applierids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.applierids.entrySet())
			applierids.put(_e_.getKey(), _e_.getValue());
		invitingids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
			invitingids.put(_e_.getKey(), _e_.getValue());
		state = _o_.state;
		smapid = _o_.smapid;
		hugs = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.hugs.entrySet())
			hugs.put(_e_.getKey(), _e_.getValue());
		targetid = _o_.targetid;
		onekeytimestamp = _o_.onekeytimestamp;
		createtime = _o_.createtime;
	}

	private void assign(TeamInfo.Data _o_) {
		teamleaderid = _o_.teamleaderid;
		commanderroleid = _o_.commanderroleid;
		minlevel = _o_.minlevel;
		maxlevel = _o_.maxlevel;
		formation = _o_.formation;
		formationlevel = _o_.formationlevel;
		switchleaderid = _o_.switchleaderid;
		switchleadertime = _o_.switchleadertime;
		successswitchtime = _o_.successswitchtime;
		members = new java.util.LinkedList<xbean.TeamMember>();
		for (xbean.TeamMember _v_ : _o_.members)
			members.add(new TeamMember(_v_, this, "members"));
		applierids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.applierids.entrySet())
			applierids.put(_e_.getKey(), _e_.getValue());
		invitingids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
			invitingids.put(_e_.getKey(), _e_.getValue());
		state = _o_.state;
		smapid = _o_.smapid;
		hugs = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.hugs.entrySet())
			hugs.put(_e_.getKey(), _e_.getValue());
		targetid = _o_.targetid;
		onekeytimestamp = _o_.onekeytimestamp;
		createtime = _o_.createtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(teamleaderid);
		_os_.marshal(commanderroleid);
		_os_.marshal(minlevel);
		_os_.marshal(maxlevel);
		_os_.marshal(formation);
		_os_.marshal(formationlevel);
		_os_.marshal(switchleaderid);
		_os_.marshal(switchleadertime);
		_os_.marshal(successswitchtime);
		_os_.compact_uint32(members.size());
		for (xbean.TeamMember _v_ : members) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(applierids.size());
		for (java.util.Map.Entry<Long, Long> _e_ : applierids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(invitingids.size());
		for (java.util.Map.Entry<Long, Long> _e_ : invitingids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(state);
		_os_.marshal(smapid);
		_os_.compact_uint32(hugs.size());
		for (java.util.Map.Entry<Long, Long> _e_ : hugs.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(targetid);
		_os_.marshal(onekeytimestamp);
		_os_.marshal(createtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		teamleaderid = _os_.unmarshal_long();
		commanderroleid = _os_.unmarshal_long();
		minlevel = _os_.unmarshal_int();
		maxlevel = _os_.unmarshal_int();
		formation = _os_.unmarshal_int();
		formationlevel = _os_.unmarshal_int();
		switchleaderid = _os_.unmarshal_long();
		switchleadertime = _os_.unmarshal_long();
		successswitchtime = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.TeamMember _v_ = new TeamMember(0, this, "members");
			_v_.unmarshal(_os_);
			members.add(_v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				applierids = new java.util.HashMap<Long, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				applierids.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				invitingids = new java.util.HashMap<Long, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				invitingids.put(_k_, _v_);
			}
		}
		state = _os_.unmarshal_int();
		smapid = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				hugs = new java.util.HashMap<Long, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				hugs.put(_k_, _v_);
			}
		}
		targetid = _os_.unmarshal_int();
		onekeytimestamp = _os_.unmarshal_long();
		createtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.TeamInfo copy() {
		_xdb_verify_unsafe_();
		return new TeamInfo(this);
	}

	@Override
	public xbean.TeamInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamInfo toBean() {
		_xdb_verify_unsafe_();
		return new TeamInfo(this); // same as copy()
	}

	@Override
	public xbean.TeamInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getTeamleaderid() { // 队长ID
		_xdb_verify_unsafe_();
		return teamleaderid;
	}

	@Override
	public long getCommanderroleid() { // 指挥ID
		_xdb_verify_unsafe_();
		return commanderroleid;
	}

	@Override
	public int getMinlevel() { // 最小级别
		_xdb_verify_unsafe_();
		return minlevel;
	}

	@Override
	public int getMaxlevel() { // 最大级别
		_xdb_verify_unsafe_();
		return maxlevel;
	}

	@Override
	public int getFormation() { // 阵型id
		_xdb_verify_unsafe_();
		return formation;
	}

	@Override
	public int getFormationlevel() { // 光环等级
		_xdb_verify_unsafe_();
		return formationlevel;
	}

	@Override
	public long getSwitchleaderid() { // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
		_xdb_verify_unsafe_();
		return switchleaderid;
	}

	@Override
	public long getSwitchleadertime() { // 记录改变队长操作的时间，判断超时用（30秒）
		_xdb_verify_unsafe_();
		return switchleadertime;
	}

	@Override
	public long getSuccessswitchtime() { // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
		_xdb_verify_unsafe_();
		return successswitchtime;
	}

	@Override
	public java.util.List<xbean.TeamMember> getMembers() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "members"), members);
	}

	public java.util.List<xbean.TeamMember> getMembersAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.TeamMember> members;
		TeamInfo _o_ = this;
		members = new java.util.LinkedList<xbean.TeamMember>();
		for (xbean.TeamMember _v_ : _o_.members)
			members.add(new TeamMember.Data(_v_));
		return members;
	}

	@Override
	public java.util.Map<Long, Long> getApplierids() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "applierids"), applierids);
	}

	@Override
	public java.util.Map<Long, Long> getApplieridsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, Long> applierids;
		TeamInfo _o_ = this;
		applierids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.applierids.entrySet())
			applierids.put(_e_.getKey(), _e_.getValue());
		return applierids;
	}

	@Override
	public java.util.Map<Long, Long> getInvitingids() { // 被邀请者的IDs 和时间 map by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "invitingids"), invitingids);
	}

	@Override
	public java.util.Map<Long, Long> getInvitingidsAsData() { // 被邀请者的IDs 和时间 map by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Long> invitingids;
		TeamInfo _o_ = this;
		invitingids = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
			invitingids.put(_e_.getKey(), _e_.getValue());
		return invitingids;
	}

	@Override
	public int getState() { // 队伍状态，参考协议中的fire.pb.team.TeamState
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public int getSmapid() { // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
		_xdb_verify_unsafe_();
		return smapid;
	}

	@Override
	public java.util.Map<Long, Long> getHugs() { // 拥抱的队员们
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "hugs"), hugs);
	}

	@Override
	public java.util.Map<Long, Long> getHugsAsData() { // 拥抱的队员们
		_xdb_verify_unsafe_();
		java.util.Map<Long, Long> hugs;
		TeamInfo _o_ = this;
		hugs = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.hugs.entrySet())
			hugs.put(_e_.getKey(), _e_.getValue());
		return hugs;
	}

	@Override
	public int getTargetid() { // 
		_xdb_verify_unsafe_();
		return targetid;
	}

	@Override
	public long getOnekeytimestamp() { // 一键喊话时间戳
		_xdb_verify_unsafe_();
		return onekeytimestamp;
	}

	@Override
	public long getCreatetime() { // 创建队伍时的时间戳
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public void setTeamleaderid(long _v_) { // 队长ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "teamleaderid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, teamleaderid) {
					public void rollback() { teamleaderid = _xdb_saved; }
				};}});
		teamleaderid = _v_;
	}

	@Override
	public void setCommanderroleid(long _v_) { // 指挥ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "commanderroleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, commanderroleid) {
					public void rollback() { commanderroleid = _xdb_saved; }
				};}});
		commanderroleid = _v_;
	}

	@Override
	public void setMinlevel(int _v_) { // 最小级别
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "minlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, minlevel) {
					public void rollback() { minlevel = _xdb_saved; }
				};}});
		minlevel = _v_;
	}

	@Override
	public void setMaxlevel(int _v_) { // 最大级别
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxlevel) {
					public void rollback() { maxlevel = _xdb_saved; }
				};}});
		maxlevel = _v_;
	}

	@Override
	public void setFormation(int _v_) { // 阵型id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "formation") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, formation) {
					public void rollback() { formation = _xdb_saved; }
				};}});
		formation = _v_;
	}

	@Override
	public void setFormationlevel(int _v_) { // 光环等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "formationlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, formationlevel) {
					public void rollback() { formationlevel = _xdb_saved; }
				};}});
		formationlevel = _v_;
	}

	@Override
	public void setSwitchleaderid(long _v_) { // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "switchleaderid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, switchleaderid) {
					public void rollback() { switchleaderid = _xdb_saved; }
				};}});
		switchleaderid = _v_;
	}

	@Override
	public void setSwitchleadertime(long _v_) { // 记录改变队长操作的时间，判断超时用（30秒）
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "switchleadertime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, switchleadertime) {
					public void rollback() { switchleadertime = _xdb_saved; }
				};}});
		switchleadertime = _v_;
	}

	@Override
	public void setSuccessswitchtime(long _v_) { // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "successswitchtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, successswitchtime) {
					public void rollback() { successswitchtime = _xdb_saved; }
				};}});
		successswitchtime = _v_;
	}

	@Override
	public void setState(int _v_) { // 队伍状态，参考协议中的fire.pb.team.TeamState
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setSmapid(int _v_) { // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "smapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, smapid) {
					public void rollback() { smapid = _xdb_saved; }
				};}});
		smapid = _v_;
	}

	@Override
	public void setTargetid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "targetid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, targetid) {
					public void rollback() { targetid = _xdb_saved; }
				};}});
		targetid = _v_;
	}

	@Override
	public void setOnekeytimestamp(long _v_) { // 一键喊话时间戳
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "onekeytimestamp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, onekeytimestamp) {
					public void rollback() { onekeytimestamp = _xdb_saved; }
				};}});
		onekeytimestamp = _v_;
	}

	@Override
	public void setCreatetime(long _v_) { // 创建队伍时的时间戳
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TeamInfo _o_ = null;
		if ( _o1_ instanceof TeamInfo ) _o_ = (TeamInfo)_o1_;
		else if ( _o1_ instanceof TeamInfo.Const ) _o_ = ((TeamInfo.Const)_o1_).nThis();
		else return false;
		if (teamleaderid != _o_.teamleaderid) return false;
		if (commanderroleid != _o_.commanderroleid) return false;
		if (minlevel != _o_.minlevel) return false;
		if (maxlevel != _o_.maxlevel) return false;
		if (formation != _o_.formation) return false;
		if (formationlevel != _o_.formationlevel) return false;
		if (switchleaderid != _o_.switchleaderid) return false;
		if (switchleadertime != _o_.switchleadertime) return false;
		if (successswitchtime != _o_.successswitchtime) return false;
		if (!members.equals(_o_.members)) return false;
		if (!applierids.equals(_o_.applierids)) return false;
		if (!invitingids.equals(_o_.invitingids)) return false;
		if (state != _o_.state) return false;
		if (smapid != _o_.smapid) return false;
		if (!hugs.equals(_o_.hugs)) return false;
		if (targetid != _o_.targetid) return false;
		if (onekeytimestamp != _o_.onekeytimestamp) return false;
		if (createtime != _o_.createtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += teamleaderid;
		_h_ += commanderroleid;
		_h_ += minlevel;
		_h_ += maxlevel;
		_h_ += formation;
		_h_ += formationlevel;
		_h_ += switchleaderid;
		_h_ += switchleadertime;
		_h_ += successswitchtime;
		_h_ += members.hashCode();
		_h_ += applierids.hashCode();
		_h_ += invitingids.hashCode();
		_h_ += state;
		_h_ += smapid;
		_h_ += hugs.hashCode();
		_h_ += targetid;
		_h_ += onekeytimestamp;
		_h_ += createtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(teamleaderid);
		_sb_.append(",");
		_sb_.append(commanderroleid);
		_sb_.append(",");
		_sb_.append(minlevel);
		_sb_.append(",");
		_sb_.append(maxlevel);
		_sb_.append(",");
		_sb_.append(formation);
		_sb_.append(",");
		_sb_.append(formationlevel);
		_sb_.append(",");
		_sb_.append(switchleaderid);
		_sb_.append(",");
		_sb_.append(switchleadertime);
		_sb_.append(",");
		_sb_.append(successswitchtime);
		_sb_.append(",");
		_sb_.append(members);
		_sb_.append(",");
		_sb_.append(applierids);
		_sb_.append(",");
		_sb_.append(invitingids);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(smapid);
		_sb_.append(",");
		_sb_.append(hugs);
		_sb_.append(",");
		_sb_.append(targetid);
		_sb_.append(",");
		_sb_.append(onekeytimestamp);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("teamleaderid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("commanderroleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("minlevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxlevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("formation"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("formationlevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("switchleaderid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("switchleadertime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("successswitchtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("members"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("applierids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("invitingids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("smapid"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("hugs"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("targetid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("onekeytimestamp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		return lb;
	}

	private class Const implements xbean.TeamInfo {
		TeamInfo nThis() {
			return TeamInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TeamInfo copy() {
			return TeamInfo.this.copy();
		}

		@Override
		public xbean.TeamInfo toData() {
			return TeamInfo.this.toData();
		}

		public xbean.TeamInfo toBean() {
			return TeamInfo.this.toBean();
		}

		@Override
		public xbean.TeamInfo toDataIf() {
			return TeamInfo.this.toDataIf();
		}

		public xbean.TeamInfo toBeanIf() {
			return TeamInfo.this.toBeanIf();
		}

		@Override
		public long getTeamleaderid() { // 队长ID
			_xdb_verify_unsafe_();
			return teamleaderid;
		}

		@Override
		public long getCommanderroleid() { // 指挥ID
			_xdb_verify_unsafe_();
			return commanderroleid;
		}

		@Override
		public int getMinlevel() { // 最小级别
			_xdb_verify_unsafe_();
			return minlevel;
		}

		@Override
		public int getMaxlevel() { // 最大级别
			_xdb_verify_unsafe_();
			return maxlevel;
		}

		@Override
		public int getFormation() { // 阵型id
			_xdb_verify_unsafe_();
			return formation;
		}

		@Override
		public int getFormationlevel() { // 光环等级
			_xdb_verify_unsafe_();
			return formationlevel;
		}

		@Override
		public long getSwitchleaderid() { // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
			_xdb_verify_unsafe_();
			return switchleaderid;
		}

		@Override
		public long getSwitchleadertime() { // 记录改变队长操作的时间，判断超时用（30秒）
			_xdb_verify_unsafe_();
			return switchleadertime;
		}

		@Override
		public long getSuccessswitchtime() { // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
			_xdb_verify_unsafe_();
			return successswitchtime;
		}

		@Override
		public java.util.List<xbean.TeamMember> getMembers() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(members);
		}

		public java.util.List<xbean.TeamMember> getMembersAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.TeamMember> members;
			TeamInfo _o_ = TeamInfo.this;
		members = new java.util.LinkedList<xbean.TeamMember>();
		for (xbean.TeamMember _v_ : _o_.members)
			members.add(new TeamMember.Data(_v_));
			return members;
		}

		@Override
		public java.util.Map<Long, Long> getApplierids() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(applierids);
		}

		@Override
		public java.util.Map<Long, Long> getApplieridsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, Long> applierids;
			TeamInfo _o_ = TeamInfo.this;
			applierids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.applierids.entrySet())
				applierids.put(_e_.getKey(), _e_.getValue());
			return applierids;
		}

		@Override
		public java.util.Map<Long, Long> getInvitingids() { // 被邀请者的IDs 和时间 map by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(invitingids);
		}

		@Override
		public java.util.Map<Long, Long> getInvitingidsAsData() { // 被邀请者的IDs 和时间 map by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Long> invitingids;
			TeamInfo _o_ = TeamInfo.this;
			invitingids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
				invitingids.put(_e_.getKey(), _e_.getValue());
			return invitingids;
		}

		@Override
		public int getState() { // 队伍状态，参考协议中的fire.pb.team.TeamState
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public int getSmapid() { // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
			_xdb_verify_unsafe_();
			return smapid;
		}

		@Override
		public java.util.Map<Long, Long> getHugs() { // 拥抱的队员们
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(hugs);
		}

		@Override
		public java.util.Map<Long, Long> getHugsAsData() { // 拥抱的队员们
			_xdb_verify_unsafe_();
			java.util.Map<Long, Long> hugs;
			TeamInfo _o_ = TeamInfo.this;
			hugs = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.hugs.entrySet())
				hugs.put(_e_.getKey(), _e_.getValue());
			return hugs;
		}

		@Override
		public int getTargetid() { // 
			_xdb_verify_unsafe_();
			return targetid;
		}

		@Override
		public long getOnekeytimestamp() { // 一键喊话时间戳
			_xdb_verify_unsafe_();
			return onekeytimestamp;
		}

		@Override
		public long getCreatetime() { // 创建队伍时的时间戳
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public void setTeamleaderid(long _v_) { // 队长ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCommanderroleid(long _v_) { // 指挥ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMinlevel(int _v_) { // 最小级别
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMaxlevel(int _v_) { // 最大级别
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFormation(int _v_) { // 阵型id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFormationlevel(int _v_) { // 光环等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSwitchleaderid(long _v_) { // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSwitchleadertime(long _v_) { // 记录改变队长操作的时间，判断超时用（30秒）
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSuccessswitchtime(long _v_) { // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 队伍状态，参考协议中的fire.pb.team.TeamState
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSmapid(int _v_) { // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTargetid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOnekeytimestamp(long _v_) { // 一键喊话时间戳
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 创建队伍时的时间戳
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
			return TeamInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TeamInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TeamInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TeamInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TeamInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TeamInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TeamInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TeamInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return TeamInfo.this.toString();
		}

	}

	public static final class Data implements xbean.TeamInfo {
		private long teamleaderid; // 队长ID
		private long commanderroleid; // 指挥ID
		private int minlevel; // 最小级别
		private int maxlevel; // 最大级别
		private int formation; // 阵型id
		private int formationlevel; // 光环等级
		private long switchleaderid; // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
		private long switchleadertime; // 记录改变队长操作的时间，判断超时用（30秒）
		private long successswitchtime; // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
		private java.util.LinkedList<xbean.TeamMember> members; // 
		private java.util.HashMap<Long, Long> applierids; // 
		private java.util.HashMap<Long, Long> invitingids; // 被邀请者的IDs 和时间 map by changhao
		private int state; // 队伍状态，参考协议中的fire.pb.team.TeamState
		private int smapid; // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
		private java.util.HashMap<Long, Long> hugs; // 拥抱的队员们
		private int targetid; // 
		private long onekeytimestamp; // 一键喊话时间戳
		private long createtime; // 创建队伍时的时间戳

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			members = new java.util.LinkedList<xbean.TeamMember>();
			applierids = new java.util.HashMap<Long, Long>();
			invitingids = new java.util.HashMap<Long, Long>();
			hugs = new java.util.HashMap<Long, Long>();
		}

		Data(xbean.TeamInfo _o1_) {
			if (_o1_ instanceof TeamInfo) assign((TeamInfo)_o1_);
			else if (_o1_ instanceof TeamInfo.Data) assign((TeamInfo.Data)_o1_);
			else if (_o1_ instanceof TeamInfo.Const) assign(((TeamInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TeamInfo _o_) {
			teamleaderid = _o_.teamleaderid;
			commanderroleid = _o_.commanderroleid;
			minlevel = _o_.minlevel;
			maxlevel = _o_.maxlevel;
			formation = _o_.formation;
			formationlevel = _o_.formationlevel;
			switchleaderid = _o_.switchleaderid;
			switchleadertime = _o_.switchleadertime;
			successswitchtime = _o_.successswitchtime;
			members = new java.util.LinkedList<xbean.TeamMember>();
			for (xbean.TeamMember _v_ : _o_.members)
				members.add(new TeamMember.Data(_v_));
			applierids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.applierids.entrySet())
				applierids.put(_e_.getKey(), _e_.getValue());
			invitingids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
				invitingids.put(_e_.getKey(), _e_.getValue());
			state = _o_.state;
			smapid = _o_.smapid;
			hugs = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.hugs.entrySet())
				hugs.put(_e_.getKey(), _e_.getValue());
			targetid = _o_.targetid;
			onekeytimestamp = _o_.onekeytimestamp;
			createtime = _o_.createtime;
		}

		private void assign(TeamInfo.Data _o_) {
			teamleaderid = _o_.teamleaderid;
			commanderroleid = _o_.commanderroleid;
			minlevel = _o_.minlevel;
			maxlevel = _o_.maxlevel;
			formation = _o_.formation;
			formationlevel = _o_.formationlevel;
			switchleaderid = _o_.switchleaderid;
			switchleadertime = _o_.switchleadertime;
			successswitchtime = _o_.successswitchtime;
			members = new java.util.LinkedList<xbean.TeamMember>();
			for (xbean.TeamMember _v_ : _o_.members)
				members.add(new TeamMember.Data(_v_));
			applierids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.applierids.entrySet())
				applierids.put(_e_.getKey(), _e_.getValue());
			invitingids = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.invitingids.entrySet())
				invitingids.put(_e_.getKey(), _e_.getValue());
			state = _o_.state;
			smapid = _o_.smapid;
			hugs = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.hugs.entrySet())
				hugs.put(_e_.getKey(), _e_.getValue());
			targetid = _o_.targetid;
			onekeytimestamp = _o_.onekeytimestamp;
			createtime = _o_.createtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(teamleaderid);
			_os_.marshal(commanderroleid);
			_os_.marshal(minlevel);
			_os_.marshal(maxlevel);
			_os_.marshal(formation);
			_os_.marshal(formationlevel);
			_os_.marshal(switchleaderid);
			_os_.marshal(switchleadertime);
			_os_.marshal(successswitchtime);
			_os_.compact_uint32(members.size());
			for (xbean.TeamMember _v_ : members) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(applierids.size());
			for (java.util.Map.Entry<Long, Long> _e_ : applierids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(invitingids.size());
			for (java.util.Map.Entry<Long, Long> _e_ : invitingids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(state);
			_os_.marshal(smapid);
			_os_.compact_uint32(hugs.size());
			for (java.util.Map.Entry<Long, Long> _e_ : hugs.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(targetid);
			_os_.marshal(onekeytimestamp);
			_os_.marshal(createtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			teamleaderid = _os_.unmarshal_long();
			commanderroleid = _os_.unmarshal_long();
			minlevel = _os_.unmarshal_int();
			maxlevel = _os_.unmarshal_int();
			formation = _os_.unmarshal_int();
			formationlevel = _os_.unmarshal_int();
			switchleaderid = _os_.unmarshal_long();
			switchleadertime = _os_.unmarshal_long();
			successswitchtime = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.TeamMember _v_ = xbean.Pod.newTeamMemberData();
				_v_.unmarshal(_os_);
				members.add(_v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					applierids = new java.util.HashMap<Long, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					applierids.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					invitingids = new java.util.HashMap<Long, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					invitingids.put(_k_, _v_);
				}
			}
			state = _os_.unmarshal_int();
			smapid = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					hugs = new java.util.HashMap<Long, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					hugs.put(_k_, _v_);
				}
			}
			targetid = _os_.unmarshal_int();
			onekeytimestamp = _os_.unmarshal_long();
			createtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.TeamInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.TeamInfo toData() {
			return new Data(this);
		}

		public xbean.TeamInfo toBean() {
			return new TeamInfo(this, null, null);
		}

		@Override
		public xbean.TeamInfo toDataIf() {
			return this;
		}

		public xbean.TeamInfo toBeanIf() {
			return new TeamInfo(this, null, null);
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
		public long getTeamleaderid() { // 队长ID
			return teamleaderid;
		}

		@Override
		public long getCommanderroleid() { // 指挥ID
			return commanderroleid;
		}

		@Override
		public int getMinlevel() { // 最小级别
			return minlevel;
		}

		@Override
		public int getMaxlevel() { // 最大级别
			return maxlevel;
		}

		@Override
		public int getFormation() { // 阵型id
			return formation;
		}

		@Override
		public int getFormationlevel() { // 光环等级
			return formationlevel;
		}

		@Override
		public long getSwitchleaderid() { // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
			return switchleaderid;
		}

		@Override
		public long getSwitchleadertime() { // 记录改变队长操作的时间，判断超时用（30秒）
			return switchleadertime;
		}

		@Override
		public long getSuccessswitchtime() { // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
			return successswitchtime;
		}

		@Override
		public java.util.List<xbean.TeamMember> getMembers() { // 
			return members;
		}

		@Override
		public java.util.List<xbean.TeamMember> getMembersAsData() { // 
			return members;
		}

		@Override
		public java.util.Map<Long, Long> getApplierids() { // 
			return applierids;
		}

		@Override
		public java.util.Map<Long, Long> getApplieridsAsData() { // 
			return applierids;
		}

		@Override
		public java.util.Map<Long, Long> getInvitingids() { // 被邀请者的IDs 和时间 map by changhao
			return invitingids;
		}

		@Override
		public java.util.Map<Long, Long> getInvitingidsAsData() { // 被邀请者的IDs 和时间 map by changhao
			return invitingids;
		}

		@Override
		public int getState() { // 队伍状态，参考协议中的fire.pb.team.TeamState
			return state;
		}

		@Override
		public int getSmapid() { // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
			return smapid;
		}

		@Override
		public java.util.Map<Long, Long> getHugs() { // 拥抱的队员们
			return hugs;
		}

		@Override
		public java.util.Map<Long, Long> getHugsAsData() { // 拥抱的队员们
			return hugs;
		}

		@Override
		public int getTargetid() { // 
			return targetid;
		}

		@Override
		public long getOnekeytimestamp() { // 一键喊话时间戳
			return onekeytimestamp;
		}

		@Override
		public long getCreatetime() { // 创建队伍时的时间戳
			return createtime;
		}

		@Override
		public void setTeamleaderid(long _v_) { // 队长ID
			teamleaderid = _v_;
		}

		@Override
		public void setCommanderroleid(long _v_) { // 指挥ID
			commanderroleid = _v_;
		}

		@Override
		public void setMinlevel(int _v_) { // 最小级别
			minlevel = _v_;
		}

		@Override
		public void setMaxlevel(int _v_) { // 最大级别
			maxlevel = _v_;
		}

		@Override
		public void setFormation(int _v_) { // 阵型id
			formation = _v_;
		}

		@Override
		public void setFormationlevel(int _v_) { // 光环等级
			formationlevel = _v_;
		}

		@Override
		public void setSwitchleaderid(long _v_) { // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
			switchleaderid = _v_;
		}

		@Override
		public void setSwitchleadertime(long _v_) { // 记录改变队长操作的时间，判断超时用（30秒）
			switchleadertime = _v_;
		}

		@Override
		public void setSuccessswitchtime(long _v_) { // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
			successswitchtime = _v_;
		}

		@Override
		public void setState(int _v_) { // 队伍状态，参考协议中的fire.pb.team.TeamState
			state = _v_;
		}

		@Override
		public void setSmapid(int _v_) { // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
			smapid = _v_;
		}

		@Override
		public void setTargetid(int _v_) { // 
			targetid = _v_;
		}

		@Override
		public void setOnekeytimestamp(long _v_) { // 一键喊话时间戳
			onekeytimestamp = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 创建队伍时的时间戳
			createtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TeamInfo.Data)) return false;
			TeamInfo.Data _o_ = (TeamInfo.Data) _o1_;
			if (teamleaderid != _o_.teamleaderid) return false;
			if (commanderroleid != _o_.commanderroleid) return false;
			if (minlevel != _o_.minlevel) return false;
			if (maxlevel != _o_.maxlevel) return false;
			if (formation != _o_.formation) return false;
			if (formationlevel != _o_.formationlevel) return false;
			if (switchleaderid != _o_.switchleaderid) return false;
			if (switchleadertime != _o_.switchleadertime) return false;
			if (successswitchtime != _o_.successswitchtime) return false;
			if (!members.equals(_o_.members)) return false;
			if (!applierids.equals(_o_.applierids)) return false;
			if (!invitingids.equals(_o_.invitingids)) return false;
			if (state != _o_.state) return false;
			if (smapid != _o_.smapid) return false;
			if (!hugs.equals(_o_.hugs)) return false;
			if (targetid != _o_.targetid) return false;
			if (onekeytimestamp != _o_.onekeytimestamp) return false;
			if (createtime != _o_.createtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += teamleaderid;
			_h_ += commanderroleid;
			_h_ += minlevel;
			_h_ += maxlevel;
			_h_ += formation;
			_h_ += formationlevel;
			_h_ += switchleaderid;
			_h_ += switchleadertime;
			_h_ += successswitchtime;
			_h_ += members.hashCode();
			_h_ += applierids.hashCode();
			_h_ += invitingids.hashCode();
			_h_ += state;
			_h_ += smapid;
			_h_ += hugs.hashCode();
			_h_ += targetid;
			_h_ += onekeytimestamp;
			_h_ += createtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(teamleaderid);
			_sb_.append(",");
			_sb_.append(commanderroleid);
			_sb_.append(",");
			_sb_.append(minlevel);
			_sb_.append(",");
			_sb_.append(maxlevel);
			_sb_.append(",");
			_sb_.append(formation);
			_sb_.append(",");
			_sb_.append(formationlevel);
			_sb_.append(",");
			_sb_.append(switchleaderid);
			_sb_.append(",");
			_sb_.append(switchleadertime);
			_sb_.append(",");
			_sb_.append(successswitchtime);
			_sb_.append(",");
			_sb_.append(members);
			_sb_.append(",");
			_sb_.append(applierids);
			_sb_.append(",");
			_sb_.append(invitingids);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(smapid);
			_sb_.append(",");
			_sb_.append(hugs);
			_sb_.append(",");
			_sb_.append(targetid);
			_sb_.append(",");
			_sb_.append(onekeytimestamp);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}

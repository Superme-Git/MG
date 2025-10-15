
package fire.msp.circletask;

import java.util.Calendar;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.SpecialQuestType;
import fire.pb.circletask.catchit.CatchItQuest;
import fire.pb.map.SceneManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCreateNpcForCatchIt__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCreateNpcForCatchIt extends __MCreateNpcForCatchIt__ {
	@Override
	protected void process() {
		// protocol handle
		new mkdb.Procedure() {

			public boolean process() {
				//在此处设置明雷怪NPC的坐标位置
				if (teamnum > 0) {
					//需要组队
					fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
					if (null == team || !team.isTeamLeader(roleid)) {
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 140273, npcid, null);
						return false;
					}
					// 全队正常状态成员 加锁
					final java.util.List<Long> members = team.getNormalMemberIds();
					this.lock(xtable.Locks.ROLELOCK, members);

					java.util.ArrayList<String> args = new java.util.ArrayList<String>();
					args.add(SceneManager.getMapNameByMapID(mapid));
					
					int teamLeaderRound = 0;
					xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleid);
					if (null == quest_map) {
						return false;
					}
					xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(questid);
					if (null == questinfo) {
						return false;
					}
					
					final CircTask circTask = CircleTaskManager.getInstance().getCircTask(questinfo.getId());
					
					teamLeaderRound = questinfo.getRound();
					final long now = Calendar.getInstance().getTimeInMillis();
					for (Long rid : members) {
						xbean.CircleTaskMap questMap = xtable.Rolecircletask.get(rid);
						if (null == questMap) {
							return false;
						}
						xbean.CircleTaskInfo sqinfo = questMap.getTaskmap().get(questid);
						if (null == sqinfo) {
							return false;
						}
						
						sqinfo.setDstitemid(battleid);
						sqinfo.setDstmapid(mapid);
						sqinfo.setDstnpcid(npcid);
						sqinfo.setDstnpckey(npckey);
						sqinfo.setDstx(posx);
						sqinfo.setDsty(posy);
						sqinfo.setBigaward(posx1); //假坐标
						sqinfo.setSmallaward(posy1);
						sqinfo.setLastgiveuptime(0);
						sqinfo.setQuestid(questid);
						sqinfo.setNpcname(name);
						sqinfo.setQueststate(SpecialQuestState.UNDONE);
						sqinfo.setQuesttype(questtype);
						sqinfo.setQuestclass(CircTaskClass.CircTask_CatchIt);
						
						if (questtype == circTask.specialtask) {
							//鬼王任务直接设置为最后一轮
							sqinfo.setRound(circTask.getRing() + 1);
						}
						else {
							sqinfo.setRound((teamLeaderRound % circTask.getRing()) + 1);
						}					
						
						if (sqinfo.getRound()%circTask.getRing()==1) {
							sqinfo.setFirsttasktime(now);
						}
						sqinfo.setTakequesttime(now);
						sqinfo.setAcceptquesttime(now);
						
						if (firsttime == 1)
							CircleTaskManager.getInstance().refresh2role(sqinfo, rid, true);
						else
							CircleTaskManager.getInstance().refresh2role(sqinfo, rid, false);
					}
					// 开启定时器
					CatchItQuest.addCatchItQuestTimer(roleid, questid, questinfo.getId());
				}
				else {
					//不需要组队
					final long now = Calendar.getInstance().getTimeInMillis();
					xbean.CircleTaskMap questMap = xtable.Rolecircletask.get(roleid);
					if (null == questMap) {
						return false;
					}
					xbean.CircleTaskInfo sqinfo = questMap.getTaskmap().get(questid);
					if (null == sqinfo) {
						return false;
					}
					
					final CircTask circTask = CircleTaskManager.getInstance().getCircTask(sqinfo.getId());
					
					sqinfo.setDstitemid(battleid);
					sqinfo.setDstmapid(mapid);
					sqinfo.setDstnpcid(npcid);
					sqinfo.setDstnpckey(npckey);
					sqinfo.setDstx(posx);
					sqinfo.setDsty(posy);
					sqinfo.setBigaward(posx1); //假坐标
					sqinfo.setSmallaward(posy1);
					sqinfo.setLastgiveuptime(0);
					sqinfo.setQuestid(questid);
					sqinfo.setNpcname(name);
					sqinfo.setQueststate(SpecialQuestState.UNDONE);
					sqinfo.setQuesttype(questtype);
					sqinfo.setQuestclass(CircTaskClass.CircTask_CatchIt);
					
					if (circTask.getRing() == 0) {
						sqinfo.setRound(sqinfo.getRound() + 1);
						
						if (sqinfo.getRound() == 1) {
							sqinfo.setFirsttasktime(now);
						}
					}
					else {
						if (questtype == SpecialQuestType.CatchIt_Increase) {
							//鬼王任务直接设置为最后一轮
							sqinfo.setRound(circTask.getRing() + 1);
						}
						else {
							sqinfo.setRound((sqinfo.getRound() % circTask.getRing()) + 1);
						}
						
						if (sqinfo.getRound()%circTask.getRing()==1) {
							sqinfo.setFirsttasktime(now);
						}
					}
					
					sqinfo.setTakequesttime(now);
					sqinfo.setAcceptquesttime(now);
					// 开启定时器
					CatchItQuest.addCatchItQuestTimer(roleid, questid, sqinfo.getId());
					if (firsttime == 1)
						CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, true);
					else
						CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, false);
				}
				
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 741906;

	public int getType() {
		return 741906;
	}

	public long roleid;
	public long npckey;
	public int npcid;
	public int mapid;
	public int battleid; // 战斗信息
	public int posx;
	public int posy;
	public int posx1;
	public int posy1;
	public java.lang.String name;
	public int givetasknpcid; // 给予任务的npcid
	public int questtype; // 子任务类型
	public int questid; // 子任务ID
	public int teamnum; // 队伍人数,0表示不需要组队
	public byte firsttime; // 1 表示从NPC处接取任务, 0 表示自动接取的

	public MCreateNpcForCatchIt() {
		name = "";
	}

	public MCreateNpcForCatchIt(long _roleid_, long _npckey_, int _npcid_, int _mapid_, int _battleid_, int _posx_, int _posy_, int _posx1_, int _posy1_, java.lang.String _name_, int _givetasknpcid_, int _questtype_, int _questid_, int _teamnum_, byte _firsttime_) {
		this.roleid = _roleid_;
		this.npckey = _npckey_;
		this.npcid = _npcid_;
		this.mapid = _mapid_;
		this.battleid = _battleid_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.posx1 = _posx1_;
		this.posy1 = _posy1_;
		this.name = _name_;
		this.givetasknpcid = _givetasknpcid_;
		this.questtype = _questtype_;
		this.questid = _questid_;
		this.teamnum = _teamnum_;
		this.firsttime = _firsttime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(npckey);
		_os_.marshal(npcid);
		_os_.marshal(mapid);
		_os_.marshal(battleid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(posx1);
		_os_.marshal(posy1);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(givetasknpcid);
		_os_.marshal(questtype);
		_os_.marshal(questid);
		_os_.marshal(teamnum);
		_os_.marshal(firsttime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		npckey = _os_.unmarshal_long();
		npcid = _os_.unmarshal_int();
		mapid = _os_.unmarshal_int();
		battleid = _os_.unmarshal_int();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		posx1 = _os_.unmarshal_int();
		posy1 = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		givetasknpcid = _os_.unmarshal_int();
		questtype = _os_.unmarshal_int();
		questid = _os_.unmarshal_int();
		teamnum = _os_.unmarshal_int();
		firsttime = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MCreateNpcForCatchIt) {
			MCreateNpcForCatchIt _o_ = (MCreateNpcForCatchIt)_o1_;
			if (roleid != _o_.roleid) return false;
			if (npckey != _o_.npckey) return false;
			if (npcid != _o_.npcid) return false;
			if (mapid != _o_.mapid) return false;
			if (battleid != _o_.battleid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (posx1 != _o_.posx1) return false;
			if (posy1 != _o_.posy1) return false;
			if (!name.equals(_o_.name)) return false;
			if (givetasknpcid != _o_.givetasknpcid) return false;
			if (questtype != _o_.questtype) return false;
			if (questid != _o_.questid) return false;
			if (teamnum != _o_.teamnum) return false;
			if (firsttime != _o_.firsttime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)npckey;
		_h_ += npcid;
		_h_ += mapid;
		_h_ += battleid;
		_h_ += posx;
		_h_ += posy;
		_h_ += posx1;
		_h_ += posy1;
		_h_ += name.hashCode();
		_h_ += givetasknpcid;
		_h_ += questtype;
		_h_ += questid;
		_h_ += teamnum;
		_h_ += (int)firsttime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(battleid).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(posx1).append(",");
		_sb_.append(posy1).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(givetasknpcid).append(",");
		_sb_.append(questtype).append(",");
		_sb_.append(questid).append(",");
		_sb_.append(teamnum).append(",");
		_sb_.append(firsttime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


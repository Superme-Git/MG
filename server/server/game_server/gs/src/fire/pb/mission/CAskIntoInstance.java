
package fire.pb.mission;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import fire.pb.mission.instance.PEnterInstanceMap;
import fire.pb.talk.MessageMgr;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAskIntoInstance__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAskIntoInstance extends __CAskIntoInstance__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		mkdb.Procedure askIntoInst = new mkdb.Procedure() {
			@Override
			protected boolean process()	{
				Long teamid = xtable.Roleid2teamid.select(roleid);
				if (teamid == null)
					return false;
				
				fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByTeamID(teamid);
				if (team == null)
					return false;
				
				final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
				roleids.addAll(team.getNormalMemberIds());
				this.lock(xtable.Locks.ROLELOCK, roleids);
				
				xbean.InstanceAskInfo askInst = xtable.Instanceaskinfos.get(teamid);
				if (askInst == null)
					return false;
				
				if (!askInst.getAnswerinfo().containsKey(roleid)) {
					return false;
				}
				
				if (answer == 0) {
					askInst.getAnswerinfo().put(roleid, 0);
					String roleName = xtable.Properties.selectRolename(roleid);
					if (roleName == null)
						return false;
					List<String> para = new ArrayList<String>(1);
					para.add(roleName);
					for (Long rid : roleids) {
						if (rid != roleid) {
							MessageMgr.psendMsgNotifyWhileCommit(rid, 160211, para);
						}
					}
					
					/*if (insttype == 0) {
						ScheduledFuture<?> future = fire.pb.mission.instance.InstanceManager.askInstFuture.get(teamid);
						if (future != null) {
							future.cancel(true);
							fire.pb.mission.instance.InstanceManager.askInstFuture.remove(teamid);
						}
					}
					else if (insttype == 1) {
						ScheduledFuture<?> future = fire.pb.mission.instance.line.LineInstManager.askInstFuture.get(teamid);
						if (future != null) {
							future.cancel(true);
							fire.pb.mission.instance.line.LineInstManager.askInstFuture.remove(teamid);
						}
					}
					
					xtable.Instanceaskinfos.remove(teamid);*/
					
					SAnswerInstance answerInst = new SAnswerInstance();
					answerInst.roleid = roleid;
					answerInst.answer = 0;
					mkdb.Procedure.psendWhileCommit(roleids, answerInst);
					
					return true;
				}
				
				//判断队伍人数有无变化
				if (askInst.getAnswerinfo().size() != roleids.size()) {
					for (Long rid : roleids) {
						MessageMgr.psendMsgNotifyWhileCommit(rid, 166057, null);
					}

					if (insttype == 0) {
						ScheduledFuture<?> future = fire.pb.mission.instance.InstanceManager.askInstFuture.get(teamid);
						if (future != null) {
							future.cancel(true);
							fire.pb.mission.instance.InstanceManager.askInstFuture.remove(teamid);
						}
					}
					else if (insttype == 1) {
						ScheduledFuture<?> future = fire.pb.mission.instance.line.LineInstManager.askInstFuture.get(teamid);
						if (future != null) {
							future.cancel(true);
							fire.pb.mission.instance.line.LineInstManager.askInstFuture.remove(teamid);
						}
					}
					
					xtable.Instanceaskinfos.remove(teamid);
					return true;
				}
				
				//设置为同意
				SAnswerInstance answerInst = new SAnswerInstance();
				answerInst.roleid = roleid;
				answerInst.answer = 1;
				mkdb.Procedure.psendWhileCommit(roleids, answerInst);
				
				int nNum = 0;
				askInst.getAnswerinfo().put(roleid, 1);
				for (Integer ret : askInst.getAnswerinfo().values()) {
					if (ret == 1) nNum ++;
				}
				
				if (nNum == askInst.getAnswerinfo().size()) {
					xbean.InstanceInfoCol teamLeadreInfoCol = xtable.Roleinstancetask.get(team.getTeamLeaderId());
					if (teamLeadreInfoCol == null) {
						return false;
					}					
					int instanceZoneId = teamLeadreInfoCol.getLastinstanceid();
					
					if (insttype == 0) {
						ScheduledFuture<?> future = fire.pb.mission.instance.InstanceManager.askInstFuture.get(teamid);
						if (future == null)
							return false;
						future.cancel(true);
						
						fire.pb.mission.instance.InstanceManager.askInstFuture.remove(teamid);
						
						new PEnterInstanceMap(team.getTeamId(), instanceZoneId).call();
					}
					else if (insttype == 1) {
						ScheduledFuture<?> future = fire.pb.mission.instance.line.LineInstManager.askInstFuture.get(teamid);
						if (future == null)
							return false;
						future.cancel(true);
						
						fire.pb.mission.instance.line.LineInstManager.askInstFuture.remove(teamid);
						
						new fire.pb.mission.instance.line.PEnterLineMap(team.getTeamId(), instanceZoneId).call();
					}
					xtable.Instanceaskinfos.remove(teamid);
				}
				
				return true;
			}
		};
		
		askIntoInst.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805539;

	public int getType() {
		return 805539;
	}

	public short answer; // 1表示同意, 0表示不同意
	public int insttype; // 0 日常副本; 1时光之穴

	public CAskIntoInstance() {
	}

	public CAskIntoInstance(short _answer_, int _insttype_) {
		this.answer = _answer_;
		this.insttype = _insttype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(answer);
		_os_.marshal(insttype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		answer = _os_.unmarshal_short();
		insttype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAskIntoInstance) {
			CAskIntoInstance _o_ = (CAskIntoInstance)_o1_;
			if (answer != _o_.answer) return false;
			if (insttype != _o_.insttype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += answer;
		_h_ += insttype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(answer).append(",");
		_sb_.append(insttype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAskIntoInstance _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = answer - _o_.answer;
		if (0 != _c_) return _c_;
		_c_ = insttype - _o_.insttype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


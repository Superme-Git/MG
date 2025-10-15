
package fire.msp.task;

import fire.pb.battle.PNewBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.mission.MissionBattleEndHandler;
import fire.pb.mission.MissionColumn;
import fire.pb.mission.Module;
import fire.pb.mission.RoleMission;
import fire.pb.mission.SUseMissionItemFail;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MScenarioQuestUseItemVerifyPosSucc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MScenarioQuestUseItemVerifyPosSucc extends __MScenarioQuestUseItemVerifyPosSucc__ {
	@Override
	protected void process() {
		// protocol handle
		dealUseItem(roleid, scenarioquestid, useitemid);
	}
	private static void onFail(final long roleid) {
		if (mkdb.Transaction.current() == null)
			gnet.link.Onlines.getInstance().send(roleid, new SUseMissionItemFail());
		else
			mkdb.Procedure.psend(roleid, new SUseMissionItemFail());
	}
	public static boolean dealUseItem(final long roleid, final int scenarioquestid, final int useitemid) {
		RoleMission task = new MissionColumn(roleid, true).getMission(scenarioquestid);
		if (task == null) {
			return false;
		}
		Module.logger.info("角色[" + roleid + "]使用道具[" + useitemid + "],任务[" + scenarioquestid + "]!");
		if (task.getConf().aiInfo.aIID != 0) {
			PNewBattle battle = new PNewBattle(roleid, task.getConf().aiInfo.aIID, 
					new MissionBattleEndHandler(scenarioquestid, task.getConf().aiInfo.battleResult
							, roleid));
			if (mkdb.Transaction.current() == null)
				battle.submit();
			else
				battle.call();
			return true;
		//	gnet.link.Onlines.getInstance().send(roleid, new SUseTaskItemFail());
		} else {
			BuffAgent buffagent = new BuffRoleImpl(roleid,true);
			if (buffagent.existBuff(BuffConstant.StateType.STATE_TEAM) && buffagent.existBuff(BuffConstant.StateType.STATE_TEAM_LEADER)) {
				if (task.getConf().exeIndo.teamState == 0) {
				//	onFail(roleid);
					Module.logger.info("角色[" + roleid + "]使用道具[" + useitemid + "],任务[" + scenarioquestid + "],有队伍且为队长,但配置teamState==0.");
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 141711, null);
					return false;
				}
				if (task.getConf().exeIndo.share == 0) {
					//不共享
					fire.pb.mission.PCommitMajorMission taskproc=
							new fire.pb.mission.PCommitMajorMission(roleid, scenarioquestid, true, true);
					if (mkdb.Transaction.current() == null)
						taskproc.submit();
					else
						taskproc.call();
				} else {
					//共享也没用
					fire.pb.mission.PCommitMajorMission taskproc=
							new fire.pb.mission.PCommitMajorMission(roleid, scenarioquestid, true, true);
					if (mkdb.Transaction.current() == null)
						taskproc.submit();
					else
						taskproc.call();
				}
			} else if(buffagent.existBuff(BuffConstant.StateType.STATE_TEAM) && !buffagent.existBuff(BuffConstant.StateType.STATE_TEAM_LEADER)) {
				Module.logger.info("角色[" + roleid + "]使用道具[" + useitemid + "],任务[" + scenarioquestid + "],有队伍且不为队长.");
				fire.pb.mission.PCommitMajorMission taskproc=
						new fire.pb.mission.PCommitMajorMission(roleid, scenarioquestid, true, true);
				if (mkdb.Transaction.current() == null)
					taskproc.submit();
				else
					taskproc.call();
			} else {
				if (task.getConf().exeIndo.teamState == 2) {
					Module.logger.info("角色[" + roleid + "]使用道具[" + useitemid + "],任务[" + scenarioquestid + "],没有队伍,但teamState==2.");
					onFail(roleid);
					return false;
				}
				Module.logger.info("角色[" + roleid + "]使用道具[" + useitemid + "],任务[" + scenarioquestid + "],没有队伍.");
				fire.pb.mission.PCommitMajorMission taskproc=
						new fire.pb.mission.PCommitMajorMission(roleid, scenarioquestid, true, true);
				if (mkdb.Transaction.current() == null)
					taskproc.submit();
				else
					taskproc.call();
			}
			mkdb.Procedure p = new mkdb.Procedure() {
				@Override
				protected boolean process() {
					return fire.pb.item.Module.getInstance().unlockAndDelItem(roleid, useitemid);
				}
			};
			if (mkdb.Transaction.current() == null) {
				p.submit();
			} else {
				p.call();
			}
			return true;
		}
	}
//	private boolean checkAIBattle() {
//		MissionConfig conf = MissionManager.getInstance().getTaskConfig( scenarioquestid );
//		if ( conf == null ) {
//			return false;
//		}
//		if ( Helper.getTaskFinishRequireType( conf.activeInfo.missionType ) == TaskFinishRequire.AREA || 
//				Helper.getTaskExeType( conf.activeInfo.missionType ) == TaskExecuteTypes.USE ) {
//			if ( conf.aiInfo.aIID != 0 ) {
//				new TaskBattle( roleid, conf.aiInfo.aIID, 
//					new TaskAIBattleEndHandler( scenarioquestid, conf.aiInfo.battleResult ),
//					conf.scenarioInfo.animationID ).submit();
//				gnet.link.Onlines.getInstance().send(roleid, new SUseTaskItemFail());
//				return true;
//			}
//		}
//		return false;
//	}
//	private final class PCommitMajorMission extends fire.pb.mission.PCommitMajorMission {
//		private final long memid;
//		PCommitMajorMission( final long memid ) {
//			super(memid, scenarioquestid, new fire.pb.mission.NullCommitParam() );
//			this.memid = memid;
//		}
//		@Override
//		protected boolean taskProcess() {
////			fire.pb.mission.TaskScenarioColumn sml =
////				new fire.pb.mission.TaskScenarioColumn( memid, false );
//			if (roleid == memid) {
//				fire.pb.item.Module.getInstance().unlockAndDelItem( memid );
//			}
//			return super.taskProcess();
////			fire.pb.mission.TaskScenario task = sml.getTask( scenarioquestid );
////			
////			return sml.(scenarioquestid);
//				
//		}
//		
//	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 739904;

	public int getType() {
		return 739904;
	}

	public long roleid;
	public int scenarioquestid;
	public int useitemid;

	public MScenarioQuestUseItemVerifyPosSucc() {
	}

	public MScenarioQuestUseItemVerifyPosSucc(long _roleid_, int _scenarioquestid_, int _useitemid_) {
		this.roleid = _roleid_;
		this.scenarioquestid = _scenarioquestid_;
		this.useitemid = _useitemid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(scenarioquestid);
		_os_.marshal(useitemid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		scenarioquestid = _os_.unmarshal_int();
		useitemid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MScenarioQuestUseItemVerifyPosSucc) {
			MScenarioQuestUseItemVerifyPosSucc _o_ = (MScenarioQuestUseItemVerifyPosSucc)_o1_;
			if (roleid != _o_.roleid) return false;
			if (scenarioquestid != _o_.scenarioquestid) return false;
			if (useitemid != _o_.useitemid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += scenarioquestid;
		_h_ += useitemid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(scenarioquestid).append(",");
		_sb_.append(useitemid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MScenarioQuestUseItemVerifyPosSucc _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = scenarioquestid - _o_.scenarioquestid;
		if (0 != _c_) return _c_;
		_c_ = useitemid - _o_.useitemid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}


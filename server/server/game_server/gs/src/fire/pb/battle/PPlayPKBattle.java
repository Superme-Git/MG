package fire.pb.battle;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.battle.BattleType;
import fire.pb.battle.PNewBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PPlayPKBattle extends Procedure {
	private final long hostid;
	private final long guestid;

	public PPlayPKBattle(long hostroleid, long guestRoleID) {
		this.hostid = hostroleid;
		this.guestid = guestRoleID;
	}

	@Override
	protected boolean process() throws Exception {
		
		List<Long> roleids = new ArrayList<Long>();
		roleids.add(hostid);
		roleids.add(guestid);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));
		fire.pb.PropRole hostRole = new PropRole(hostid, false);
		fire.pb.PropRole guestRole = new PropRole(guestid, true);
		
		int guest_Level = guestRole.getLevel();
		int host_Level = hostRole.getLevel();
		
		if (guest_Level < fire.pb.battle.CSendInvitePlayPK.PVP_LEVEL || host_Level <  fire.pb.battle.CSendInvitePlayPK.PVP_LEVEL) {
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 145000 , 0, null);
			return true;
		}
		
//		if(hostRole.getProperties().getCruise() > 0 || guestRole.getProperties().getCruise() > 0) {
//			TeamManager.logger.debug("巡游状态,此时pvp不能!");
//			//fire.pb.talk.MessageMgr.sendMsgNotify(memberRoleId, 160434, null);
//			return true;
//		}
		
		if (!StateCommon.isOnlineBuffer(guestid)) {
			fire.pb.talk.MessageMgr.psendMsgNotify(hostid, 140534, null);
			return false;
		}
		
		BuffAgent agent = new BuffRoleImpl(guestid,true);
		if (agent.existState(BuffConstant.StateType.STATE_BATTLE_WATCHER)){
			new fire.pb.battle.watch.PEndWatchBattle(guestid).call();
		}
		if (agent.existState(BuffConstant.StateType.STATE_REPLAY)){
			agent.removeCBuff(BuffConstant.StateType.STATE_REPLAY);
		}
		// 发消息
		sendMsgToRole(hostRole, guestid);
		sendMsgToRole(guestRole, hostid);

		pexecuteWhileCommit(new PNewBattle(hostid, guestid, BattleType.BATTLE_PVP, new PlayPKEndHandler()));
		
		
		// 活跃度日历
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(hostid);
		if (actrole != null)
			actrole.addActiveNum(RoleLiveness.LEITAI);
		
		// 活跃度日历
		RoleLiveness gestrole = RoleLiveness.getRoleLiveness(guestid);
		if (gestrole != null)
			gestrole.addActiveNum(RoleLiveness.LEITAI);
		return true;
	}

	private void sendMsgToRole(PropRole role, long otherid) {
		String schoolName = CircleTaskManager.getInstance().getSchoolName(role.getSchool());
		int level = role.getLevel();
		List<String> paras = new ArrayList<String>();
		
		paras.add(String.valueOf(level));
		paras.add(schoolName);
		Team team = TeamManager.getTeamByRoleId(otherid);
		List<Long> role_members = null;
		
		if (team != null&&team.isNormalMember(otherid)) {
			role_members = team.getNormalMemberIds();
		} else {
			role_members = new ArrayList<Long>();
			role_members.add(otherid);
		}
		
		for (Long mem : role_members) {
			MessageMgr.sendMsgNotify(mem, 120067, paras);
		}
	}

}

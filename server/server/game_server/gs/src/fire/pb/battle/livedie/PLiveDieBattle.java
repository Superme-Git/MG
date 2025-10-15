package fire.pb.battle.livedie;

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
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PLiveDieBattle extends Procedure {
	private final long hostid;
	private final long guestid;
	private final int isteamfight;

	public PLiveDieBattle(long hostroleid, long guestRoleID,int isteamfight) {
		this.hostid = hostroleid;
		this.guestid = guestRoleID;
		this.isteamfight = isteamfight;
	}

	@Override
	protected boolean process() throws Exception {
		
		List<Long> roleids = new ArrayList<Long>();
		roleids.add(hostid);
		roleids.add(guestid);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));
//		fire.pb.PropRole hostRole = new PropRole(hostid, false);
//		fire.pb.PropRole guestRole = new PropRole(guestid, true);
//		
//		int guest_Level = guestRole.getLevel();
//		int host_Level = hostRole.getLevel();
		
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
		
		PNewBattle pnewbattle=new PNewBattle(hostid, guestid, BattleType.BATTLE_LIVEDIE, new LiveDieEndHandler());
		pnewbattle.isLiveDieTeamFight=isteamfight;
		pexecuteWhileCommit(pnewbattle);
		
//		// 活跃度日历
//		RoleLiveness actrole = RoleLiveness.getRoleLiveness(hostid);
//		if (actrole != null)
//			actrole.addActiveNum(RoleLiveness.LEITAI);
//		
//		// 活跃度日历
//		RoleLiveness gestrole = RoleLiveness.getRoleLiveness(guestid);
//		if (gestrole != null)
//			gestrole.addActiveNum(RoleLiveness.LEITAI);
		return true;
	}

//	private void sendMsgToRole(PropRole role, long otherid) {
//		String schoolName = CircleTaskManager.getInstance().getSchoolName(role.getSchool());
//		int level = role.getLevel();
//		List<String> paras = new ArrayList<String>();
//		
//		paras.add(String.valueOf(level));
//		paras.add(schoolName);
//		Team team = TeamManager.getTeamByRoleId(otherid);
//		List<Long> role_members = null;
//		
//		if (team != null&&team.isNormalMember(otherid)) {
//			role_members = team.getNormalMemberIds();
//		} else {
//			role_members = new ArrayList<Long>();
//			role_members.add(otherid);
//		}
//		
//		for (Long mem : role_members) {
//			MessageMgr.sendMsgNotify(mem, 120067, paras);
//		}
//	}

}

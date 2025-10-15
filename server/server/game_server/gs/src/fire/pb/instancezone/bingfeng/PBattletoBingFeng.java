package fire.pb.instancezone.bingfeng;

import fire.pb.PropRole;
import fire.pb.battle.BattleType;
import fire.pb.battle.PNewBattle;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import mkdb.Procedure;

public class PBattletoBingFeng extends Procedure {

	private final long roleid;
	private final int npcid;

	public PBattletoBingFeng(long roleid, int npcid) {

		super();
		this.roleid = roleid;
		this.npcid = npcid;
	}

	@Override
	protected boolean process() throws Exception {
		Team team = TeamManager.selectTeamByRoleId(roleid);
		if (team != null/* && !team.isAbsentMember(roleid)*/) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 144883, npcid, null);
			return false;
		}
		int landid = BingFengLandMgr.getInstance().getInstZoneIdByNpcId(npcid);
		if (landid == 0)
			return false;
		BingFengWangZuoConfig sBingFengCfg = BingFengLandMgr.getInstance().getBingFengConfigByNpcId(landid, npcid);
		if (sBingFengCfg == null)
			return false;

		int roleLv = new PropRole(roleid, true).getLevel();
		if (roleLv < sBingFengCfg.getMinLv()) {
			MessageMgr.sendMsgNotify(roleid, 170005, null);
			return false;
		}
		xbean.RoleBFInfo roleBingFengInfo = BingFengLandMgr.getInstance()
				.getRoleBingFengInfo(roleid, landid, false);
		if (roleBingFengInfo == null)
			return true;

		final long curTime = System.currentTimeMillis();
		boolean newday = false;
		if (!DateValidate.inTheSameDay(roleBingFengInfo.getLastchangetime(), curTime)) {
			BingFengRole bingFengRole = new BingFengRole(roleid, false);
			BingFengWangZuoConfig zonecfg = BingFengLandMgr.getInstance().getBingFengNewConfig(landid, 0);
			if (roleLv <= zonecfg.getMaxLv()) {
				roleBingFengInfo.setLastchangetime(curTime);
				if (roleBingFengInfo.getYesterstage() < roleBingFengInfo.getStage())
					roleBingFengInfo.setYesterstage(roleBingFengInfo.getStage());
				roleBingFengInfo.setStage(0);
				roleBingFengInfo.setLastenterstage(0);
				roleBingFengInfo.getStagefanpai().clear();
				roleBingFengInfo.getStagetime().clear();
				roleBingFengInfo.getStageround().clear();
			}

			bingFengRole.getBingFengRoleBean().setChargetimes(0);
			bingFengRole.getBingFengRoleBean().setTimes(2);
			newday = true;
		}

		xbean.BingFengRole bingFengRole = xtable.Bingfengroles.select(roleid);
		if (bingFengRole == null)
			return false;
		if (bingFengRole.getTimes() <= 0) {
			MessageMgr.sendMsgNotify(roleid, 143246, null);
			return false;
		}
		int stage = BingFengLandMgr.getInstance().getStageByNpcId(bingFengRole.getInstzoneid(), npcid);
		if (stage > roleBingFengInfo.getStage()) {
			if (newday) {
				MessageMgr.sendMsgNotify(roleid, 160428, npcid,	null);
			} else {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160450, npcid, null);
			}
			return false;
		}

		int battleid = sBingFengCfg.getBattleId();
		PNewBattle pNewBattle = new PNewBattle(roleid, battleid,
				new BingFengBattleHandler(roleid, stage, npcid));
		pNewBattle.setBattleType(BattleType.BATTLE_BINGFENG_WAR);
		mkdb.Procedure.pexecuteWhileCommit(pNewBattle);
		
		return true;
	}

}

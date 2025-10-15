package fire.pb.instancezone.bingfeng;

import fire.pb.PropRole;
import fire.pb.item.EnterBingFengInstEvent;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;
import xbean.RoleBFInfo;

public class PEnterBingFengLand extends Procedure {
	private final long roleid;
	private final int landId;
	private final int stage;
	private final boolean teamChallenge;

	public PEnterBingFengLand(long roleId, int landId, int step,
			boolean teamChallenge) {
		this.roleid = roleId;
		this.landId = landId;
		this.stage = step;
		this.teamChallenge = teamChallenge;
	}

	@Override
	protected boolean process() throws Exception {
		Team team = TeamManager.selectTeamByRoleId(roleid);
		if (team != null /*&& !team.isAbsentMember(roleid)*/) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 144883, 0, null);
			return false;
		}
		if (!teamChallenge) {
			BingFengRole bingFengRole = new BingFengRole(roleid, false);
			xbean.RoleBFInfo rolebingFengInfo = BingFengLandMgr.getInstance()
					.getRoleBingFengInfo(roleid, landId, false);
			enterZone(rolebingFengInfo, bingFengRole.getBingFengRoleBean());
			rolebingFengInfo.setLastenterstage(stage);
			rolebingFengInfo.setAutogo(0);
		} else {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 144883, 0, null);
		}
		return true;
	}

	private void enterZone(RoleBFInfo rolebingFengInfo,
			xbean.BingFengRole bingfengRole) {
		long curTime = System.currentTimeMillis();
		BingFengLandMgr.needClearTimesAndStage(roleid, bingfengRole, curTime,
				true);
		if (bingfengRole.getTimes() <= 0) {
			return;
		}
		BingFengWangZuoConfig sBingFengNewConfig = BingFengLandMgr
				.getInstance().getBingFengNewConfig(landId, stage);
		int mapid = sBingFengNewConfig.getMapid();
		Transfer.justGotoWhileCommit(roleid, mapid,
				sBingFengNewConfig.getPosx(), sBingFengNewConfig.getPosy(),
				SRoleEnterScene.BING_FENG_GOTO);
		fire.pb.event.Poster.getPoster().dispatchEvent(
				new EnterBingFengInstEvent(roleid));
		int lv = new PropRole(roleid, true).getLevel();
		if (sBingFengNewConfig.getMinLv() + 30 <= lv
				&& stage < rolebingFengInfo.getStage()) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145415, 0, null);
		}
	}
}

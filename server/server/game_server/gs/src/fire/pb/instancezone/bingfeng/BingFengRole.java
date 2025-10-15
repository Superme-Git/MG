package fire.pb.instancezone.bingfeng;

import fire.pb.PropRole;
import fire.pb.util.DateValidate;
import mkdb.Transaction;

public class BingFengRole {
	public static final long THREE_HOUR_MIILITIMES = 3 * 60 * 60 * 1000;

	private final long roleId;
	private xbean.BingFengRole xbfrole;

	public BingFengRole(long roleId, boolean readonly) {
		this.roleId = roleId;
		if (readonly)
			xbfrole = xtable.Bingfengroles.select(roleId);
		else
			xbfrole = xtable.Bingfengroles.get(roleId);
		if (xbfrole == null) {
			if (readonly)
				xbfrole = xbean.Pod.newBingFengRoleData();
			else {
				xbfrole = xbean.Pod.newBingFengRole();
				xtable.Bingfengroles.insert(roleId, xbfrole);
			}
		}
	}

	public xbean.BingFengRole getBingFengRoleBean() {
		return xbfrole;
	}

	public void sendBingFengInfo(int landId) {
		int rankId = BingFengLandMgr.getInstance().getRankIdByInstzoneId(
				landId);

		BingFengRole bingFengRole = new BingFengRole(roleId, true);
		xbean.RoleBFInfo xinfo = BingFengLandMgr.getInstance()
				.getRoleBingFengInfo(roleId, landId, true);
		int yesterdaystage = xinfo.getYesterstage();
		final long curTime = System.currentTimeMillis();
		if (!DateValidate.inTheSameDay(xinfo.getLastchangetime(),
				curTime)) {
			if (yesterdaystage < xinfo.getStage())
				yesterdaystage = xinfo.getStage();
			
		}

		SBingFengLandInfo snd = new SBingFengLandInfo();
		snd.ranktype = rankId;
		snd.lastrank = xinfo.getLastrank();
		snd.entertimes = xbfrole.getTimes();
		snd.landid = landId;
		snd.stage = xinfo.getStage();
		snd.yesterdaystage = yesterdaystage;
		snd.vip = 0;
		final int roleLv = new PropRole(roleId, true).getLevel();
		BingFengWangZuoConfig zonecfg = BingFengLandMgr.getInstance()
				.getBingFengNewConfig(landId, 0);
		if (roleLv <= zonecfg.getMaxLv()) {
			if (BingFengLandMgr.needClearTimesAndStage(roleId,	
					bingFengRole.getBingFengRoleBean(), 
					System.currentTimeMillis(),	
					false)) {
				snd.entertimes = 2;
				snd.stage = 0;
				if (snd.yesterdaystage < xinfo.getStage())
					snd.yesterdaystage = xinfo.getStage();
			}
		}

		snd.receiveaward = xinfo.getReceivelastaward();
		if (Transaction.current() != null)
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		else
			gnet.link.Onlines.getInstance().send(roleId, snd);
	}

	public void updateRanklist(int landId, int saveId) {

	}

}

package fire.pb.instancezone.bingfeng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import mkdb.Transaction;
import xbean.RoleBFInfo;
import fire.pb.PropRole;
import fire.pb.common.SCommon;
import fire.pb.instance.SBingFengCfg;
import fire.pb.instance.SBingFengJiangLiCfg;
import fire.pb.instance.SBingFengPingJi;
import fire.pb.main.ConfigManager;
import fire.pb.map.SceneManager;
import fire.pb.statistics.StatisticUtil;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.BagUtil;
import fire.pb.util.DateValidate;
import fire.pb.util.MessageUtil;
import gnet.link.Onlines;

public class BingFengLandMgr {
	private static class PRefreshRanklist extends mkdb.Procedure {
		private final int rankId;

		private PRefreshRanklist(int rankId) {
			this.rankId = rankId;
		}

		@Override
		protected boolean process() throws Exception {
			xbean.BingFengRank xrank = xtable.Bingfenglists.select(rankId);
			if (xrank == null)
				return false;
			List<Long> roleids = new LinkedList<Long>();

			Map<Integer, List<Long>> rankMap = new HashMap<Integer, List<Long>>();
			for (Map.Entry<Integer, xbean.BingFengRankList> xrankList : xrank
					.getRanks().entrySet()) {

				List<Long> schoolList = rankMap.get(xrankList.getKey());
				if (schoolList == null) {
					schoolList = new ArrayList<Long>(RANKAWARD);
					rankMap.put(xrankList.getKey(), schoolList);
				}

				for (xbean.BingFengListRecord record : xrankList.getValue()
						.getRecords()) {
					roleids.add(record.getMarshaldata().getRoleid());
					if (RANKAWARD > schoolList.size()) {
						schoolList.add(record.getMarshaldata().getRoleid());
					} else {
						break;
					}
				}
			}

			int landId = BingFengLandMgr.getInstance()
					.getInstzoneIdByRankId(rankId);
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK,
					roleids.toArray(new Object[] {})));
			int rank = 1;
			for (long roleId : roleids) {
				xbean.RoleBFInfo rBingFengInfo = BingFengLandMgr
						.getInstance().getRoleBingFengInfo(roleId, landId,
								false);
				rBingFengInfo.setLastrank(rank);
				rBingFengInfo.setReceivelastaward(0);
				rank++;
			}

			int[] awardList = { 240, 250, 251, 252, 253, 254, 255, 256, 257, 258 };

			Map<Integer, SCommon> commonMap = ConfigManager.getInstance()
					.getConf(SCommon.class);
			if (commonMap != null) {
				for (Map.Entry<Integer, List<Long>> schoolList : rankMap
						.entrySet()) {
					int i = 0;
					for (Long rid : schoolList.getValue()) {
						if (i > (RANKAWARD - 1))
							break;

						SCommon sCommon = commonMap.get(awardList[i]);
						if (sCommon != null) {
							int itemId = Integer.parseInt(sCommon.getValue());
							BagUtil.addItem(rid, itemId, 1, "冰封王座排行榜",
									fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli, itemId);

							MessageMgr.psendSystemMessageToRole(rid, 170004,
									null);

							List<String> paras = new ArrayList<String>();
							PropRole pRole = new PropRole(rid, true);
							paras.add(pRole.getName());
							paras.addAll(MessageUtil.getItemMsgParas(itemId, 1));
							STransChatMessageNotify2Client ssmn = MessageMgr.getMsgNotify(
									160430, 0, paras);
							SceneManager.sendAll(ssmn);
						}
						i++;
					}
				}
			}
			xtable.Bingfenglists.remove(rankId);

			return true;
		}
	}

	private static BingFengLandMgr _instance = new BingFengLandMgr();
	public static final Set<Integer> bingFengMaps = new HashSet<Integer>();

	public static final Map<Integer, Integer> bingfengNpcToInst = new HashMap<Integer, Integer>();
	public static final Set<Integer> bingFengServiceids = new HashSet<Integer>();

	public static final Map<Integer, Map<Integer, Integer>> bingFengStageConfig = new HashMap<Integer, Map<Integer, Integer>>();

	public static final Map<Integer, Map<Integer, BingFengWangZuoConfig>> bingFengWangZuoNewConfig = new HashMap<Integer, Map<Integer, BingFengWangZuoConfig>>();

	public static final Map<Integer, BingFengWangZuoConfig> bingFengWangZuoBattleConfig = new HashMap<Integer, BingFengWangZuoConfig>();
	public static final List<SBingFengPingJi> pingJiConfig = new ArrayList<SBingFengPingJi>();

	public static int RANKAWARD = 10;

	public static boolean freshStageTime(int landId, int stage, long roleid, int time, int round) {
		int rankId = BingFengLandMgr.getInstance().getRankIdByInstzoneId(landId);
		if (rankId == -1)
			return false;

		SBingFengJiangLiCfg cfg = BingFengLandMgr.getInstance().getBingFengAwardCfg(rankId);
		if (cfg == null)
			return false;

		final int roleLv = xtable.Properties.selectLevel(roleid);
		if (roleLv > cfg.ranklv)
			return false;

		boolean bupdate = false;
		xbean.BingFengInfo bingFeng = xtable.Bingfenginfos.get(landId);
		if (bingFeng == null) {
			bingFeng = xbean.Pod.newBingFengInfo();
			xtable.Bingfenginfos.insert(landId, bingFeng);
		}

		Integer bestTime = bingFeng.getStagetime().get(stage);
		Integer bestRound = bingFeng.getStageround().get(stage);
		if (bestTime == null || bestRound == null) {
			bingFeng.getStagetime().put(stage, time);
			bingFeng.getStageround().put(stage, round);
			bingFeng.getStagebest().put(stage, roleid);
			bupdate = true;
		} else {
			if (round < bestRound) {
				bingFeng.getStageround().put(stage, round);
				bingFeng.getStagetime().put(stage, time);
				bingFeng.getStagebest().put(stage, roleid);
				bupdate = true;
			}
			else if (bestRound == round) {
				if (time < bestTime) {
					bingFeng.getStageround().put(stage, round);
					bingFeng.getStagetime().put(stage, time);
					bingFeng.getStagebest().put(stage, roleid);
					bupdate = true;
				}
			}
		}
		
		//保存职业最快
		final int schoolid = xtable.Properties.selectSchool(roleid);
		xbean.SchoolStageTime schoolTime = bingFeng.getSchoolstage().get(schoolid);
		if (schoolTime == null) {
			schoolTime = xbean.Pod.newSchoolStageTime();
			bingFeng.getSchoolstage().put(schoolid, schoolTime);
		}

		Integer bbTime = schoolTime.getStagetime().get(stage);
		Integer bbRound = schoolTime.getStageround().get(stage);
		if (bbTime == null || bbRound == null) {
			schoolTime.getStageround().put(stage, round);
			schoolTime.getStagetime().put(stage, time);
			schoolTime.getStagebest().put(stage, roleid);
			bupdate = true;
		}
		else {
			if (round < bbRound) {
				schoolTime.getStageround().put(stage, round);
				schoolTime.getStagetime().put(stage, time);
				schoolTime.getStagebest().put(stage, roleid);
				bupdate = true;
			}
			else if (round == bbRound) {
				if (time < bbTime) {
					schoolTime.getStageround().put(stage, round);
					schoolTime.getStagetime().put(stage, time);
					schoolTime.getStagebest().put(stage, roleid);
					bupdate = true;
				}
			}
		} //OK
		return bupdate;
	}

	public static SGetBingFengDetail getBingFengDetail(long roleid,	int landid, int stage) {
		xbean.RoleBFInfo roleBingFengInfo = BingFengLandMgr.getInstance().getRoleBingFengInfo(roleid, landid, false);
		BingFengWangZuoConfig zonecfg = BingFengLandMgr.getInstance().getBingFengNewConfig(roleBingFengInfo.getInstzoneid(), 0);

		final int roleLv = xtable.Properties.selectLevel(roleid);
		if (roleLv <= zonecfg.getMaxLv()) {
			final long curTime = System.currentTimeMillis();
			if (!DateValidate.inTheSameDay(roleBingFengInfo.getLastchangetime(),
					curTime)) {
				BingFengRole bingFengRole = new BingFengRole(roleid, false);

				roleBingFengInfo.setLastchangetime(curTime);
				if (roleBingFengInfo.getYesterstage() < roleBingFengInfo.getStage())
					roleBingFengInfo.setYesterstage(roleBingFengInfo.getStage());
				roleBingFengInfo.setStage(0);
				roleBingFengInfo.setLastenterstage(0);
				roleBingFengInfo.getStagefanpai().clear();
				roleBingFengInfo.getStagetime().clear();
				roleBingFengInfo.getStageround().clear();

				bingFengRole.getBingFengRoleBean().setChargetimes(0);
				bingFengRole.getBingFengRoleBean().setTimes(2);
			}
		}

		SGetBingFengDetail sDetail = new SGetBingFengDetail();

//		Long myusetime = roleBingFengInfo.getStagetime().get(stage);
//		if (myusetime == null)
//			myusetime = 0L;
		Integer myusetime = roleBingFengInfo.getStageround().get(stage);
		if (myusetime == null)
			myusetime = 0;

		sDetail.myusetime = myusetime;
		xbean.BingFengInfo bingFeng = xtable.Bingfenginfos.select(landid);
		if (bingFeng == null) {
			if (stage >= roleBingFengInfo.getStage()) {
				sDetail.stagestate = 0;
			} else {
				sDetail.stagestate = 1;
			}

			return sDetail;
		}

		if (stage >= roleBingFengInfo.getStage()) {
			sDetail.stagestate = 0;
		} else {
			sDetail.stagestate = 1;
		}
		
		if (stage < bingFeng.getStagebest().size()) {
			final int schoolId = xtable.Properties.selectSchool(roleid);
			if (bingFeng.getSchoolstage().containsKey(schoolId)) {
				xbean.SchoolStageTime schoolTime = bingFeng.getSchoolstage().get(schoolId);
				if (schoolTime.getStagebest().containsKey(stage)) {
					String roleName = xtable.Properties.selectRolename(schoolTime.getStagebest().get(stage));
					sDetail.rolename = roleName;
					sDetail.usetime = schoolTime.getStageround().get(stage);
					return sDetail;
				}
			}

			if (bingFeng.getStagebest().containsKey(stage)) {
				String roleName = xtable.Properties.selectRolename(bingFeng.getStagebest().get(stage));
				sDetail.rolename = roleName;
				sDetail.usetime = bingFeng.getStageround().get(stage);
			}
		}

		return sDetail;
	}

	public static BingFengLandMgr getInstance() {
		return _instance;
	}

	public static int getRank(int rankId, long roleId) {
		xbean.BingFengRank rank = xtable.Bingfenglists.select(rankId);
		if (rank == null)
			return -1;
		int i = 1;

		int schoolId = xtable.Properties.selectSchool(roleId);
		xbean.BingFengRankList ranklist = rank.getRanks().get(schoolId);

		for (xbean.BingFengListRecord record : ranklist.getRecords()) {
			if (record.getMarshaldata().getRoleid() == roleId)
				return i;
			i++;
		}
		return -1;
	}

	public static boolean needClearTimesAndStage(long roleid,
			xbean.BingFengRole bingFengRole, long curTime, boolean clear) {

		Collection<xbean.RoleBFInfo> roleBingFengInfos = bingFengRole
				.getInfos().values();
		if (roleBingFengInfos.isEmpty())
			return true;
		for (xbean.RoleBFInfo roleBingFengInfo : roleBingFengInfos) {
			if (DateValidate.inTheSameDay(roleBingFengInfo.getLastchangetime(),
					curTime))
				return false;
		}
		if (clear) {
			final int roleLv = new PropRole(roleid, true).getLevel();
			for (RoleBFInfo roleBingFengInfo : roleBingFengInfos) {
				roleBingFengInfo.setLastchangetime(curTime);
				if (roleBingFengInfo.getYesterstage() < roleBingFengInfo.getStage())
					roleBingFengInfo.setYesterstage(roleBingFengInfo.getStage());

				if (roleBingFengInfo.getInstzoneid() > 0) {
					BingFengWangZuoConfig zonecfg = BingFengLandMgr
							.getInstance().getBingFengNewConfig(
									roleBingFengInfo.getInstzoneid(), 0);
					if (roleLv <= zonecfg.getMaxLv()) {
						roleBingFengInfo.setStage(0);
						roleBingFengInfo.setLastenterstage(0);
						roleBingFengInfo.getStagefanpai().clear();
						roleBingFengInfo.getStagetime().clear();
						roleBingFengInfo.getStageround().clear();
					}
				}
			}
			bingFengRole.setChargetimes(0);
			bingFengRole.setTimes(2);
		}
		return true;
	}

	public static void sendSBingFengInfo(long roleId, int rankId) {
		int landId = getInstance().getInstzoneIdByRankId(rankId);
		if (landId <= 0)
			return;

		BingFengRole ecrole = new BingFengRole(roleId, true);
		ecrole.sendBingFengInfo(landId);
	}

	public static boolean validTimes(long roleId, int landId,
			boolean teamChallenge) {
		BingFengRole bingFengRole = new BingFengRole(roleId, true);
		if (bingFengRole.getBingFengRoleBean().getTimes() <= 0
				&& !needClearTimesAndStage(roleId,
						bingFengRole.getBingFengRoleBean(),
						System.currentTimeMillis(), false)) {
			MessageMgr.sendMsgNotify(roleId, 143246, null);
			return false;
		}
		Team team = TeamManager.selectTeamByRoleId(roleId);
		if (!teamChallenge && team != null/* && !team.isAbsentMember(roleId)*/) {
			MessageMgr.sendMsgNotify(roleId, 144883, 0, null);
			return false;
		}
		if (teamChallenge && team == null)
			return false;
		return true;
	}

	private Map<Integer, SBingFengJiangLiCfg> awardcfgs;

	Map<Integer, Integer> rankId2InstzoneId = new HashMap<Integer, Integer>();

	private BingFengLandMgr() {
	}

	public Map<Integer, SBingFengJiangLiCfg> getAwardConfigs() {
		return awardcfgs;
	}

	public SBingFengJiangLiCfg getBingFengAwardCfg(int rankId) {
		for (SBingFengJiangLiCfg awardcfg : awardcfgs.values()) {
			if (awardcfg.rankid == rankId)
				return awardcfg;
		}
		return null;
	}

	public BingFengWangZuoConfig getBingFengConfigByNpcId(int landId,
			int npcId) {
		int stage = bingFengStageConfig.get(landId).get(npcId);
		return bingFengWangZuoNewConfig.get(landId).get(stage);
	}

	public BingFengWangZuoConfig getBingFengConfigByRoleLv(final long roleid) {
		final int lv = new PropRole(roleid, true).getLevel();
		for (Map<Integer, BingFengWangZuoConfig> bingfeng : bingFengWangZuoNewConfig
				.values()) {
			BingFengWangZuoConfig config = bingfeng.get(0);
			if (lv >= config.getMinLv() && lv <= config.getMaxLv()) {
				return config;
			}
		}
		return null;
	}

	public BingFengWangZuoConfig getBingFengNewConfig(int landId, int stage) {
		if (bingFengWangZuoNewConfig.containsKey(landId)) {
			return bingFengWangZuoNewConfig.get(landId).get(stage);
		}
		return null;
	}

	public int getFanPaiId(int landId, int npcId, int grade) {
		int stage = bingFengStageConfig.get(landId).get(npcId);
		BingFengWangZuoConfig config = bingFengWangZuoNewConfig.get(landId)
				.get(stage);
		return config.getFanPaiId(grade);
	}

	public SBingFengPingJi getGradeByRound(int round) {
		for (SBingFengPingJi bingFeng : pingJiConfig) {
			if (bingFeng.getMinround() <= round
					&& bingFeng.getMaxround() >= round) {
				return bingFeng;
			}
		}

		return null;
	}

	public int getInstZoneIdByNpcId(int npcId) {
		if (bingfengNpcToInst.containsKey(npcId)) {
			return bingfengNpcToInst.get(npcId);
		}
		return 0;
	}

	public int getInstzoneIdByRankId(int rankId) {
		Integer landid = rankId2InstzoneId.get(rankId);
		if (landid == null)
			return -1;
		else
			return landid;
	}

	public int getRankIdByInstzoneId(int landId) {
		for (Map.Entry<Integer, Integer> entry : rankId2InstzoneId.entrySet()) {
			if (entry.getValue() == landId)
				return entry.getKey();
		}
		return -1;
	}

	public xbean.RoleBFInfo getRoleBingFengInfo(long roleid,
			int landId, boolean readOnly) {
		xbean.BingFengRole bingFengRole = null;
		if (readOnly)
			bingFengRole = xtable.Bingfengroles.select(roleid);
		else
			bingFengRole = xtable.Bingfengroles.get(roleid);
		if (bingFengRole == null) {
			if (readOnly)
				return xbean.Pod.newRoleBFInfoData();
			else {
				bingFengRole = xbean.Pod.newBingFengRole();
				xtable.Bingfengroles.insert(roleid, bingFengRole);
			}
		}
		if (landId == 0)
			landId = bingFengRole.getInstzoneid();
		xbean.RoleBFInfo roleBingFengInfo = bingFengRole.getInfos().get(landId);
		if (roleBingFengInfo == null) {
			if (readOnly)
				roleBingFengInfo = xbean.Pod.newRoleBFInfoData();
			else {
				roleBingFengInfo = xbean.Pod.newRoleBFInfo();
				roleBingFengInfo.setInstzoneid(landId);
				bingFengRole.getInfos().put(landId, roleBingFengInfo);
			}
		}
		if (!readOnly && landId != bingFengRole.getInstzoneid())
			bingFengRole.setInstzoneid(landId);
		return roleBingFengInfo;
	}

	public int getStageByNpcId(int landId, int npcId) {
		return bingFengStageConfig.get(landId).get(npcId);
	}
	
	public BingFengWangZuoConfig getBingFengWangZuoConfigByBattleId(final int battleid) {
		if (bingFengWangZuoBattleConfig.containsKey(battleid))
			return bingFengWangZuoBattleConfig.get(battleid);
		
		return null;
	}
	
	public void init() {
		for (SBingFengCfg sBingFengWangZuoConfig : ConfigManager.getInstance().getConf(SBingFengCfg.class).values()) {
			Map<Integer, BingFengWangZuoConfig> confs = bingFengWangZuoNewConfig.get(sBingFengWangZuoConfig.instzoneid);
			if (confs == null) {
				confs = new HashMap<Integer, BingFengWangZuoConfig>();
				bingFengWangZuoNewConfig.put(sBingFengWangZuoConfig.instzoneid,	confs);
			}
			confs.put(sBingFengWangZuoConfig.state, new BingFengWangZuoConfig(sBingFengWangZuoConfig));
			bingFengMaps.add(sBingFengWangZuoConfig.Map);
			bingFengServiceids.add(sBingFengWangZuoConfig.serviceid);

			Map<Integer, Integer> stageCnf = bingFengStageConfig.get(sBingFengWangZuoConfig.instzoneid);
			if (stageCnf == null) {
				stageCnf = new HashMap<Integer, Integer>();
				bingFengStageConfig.put(sBingFengWangZuoConfig.instzoneid, stageCnf);
			}

			stageCnf.put(sBingFengWangZuoConfig.FocusNpc, sBingFengWangZuoConfig.state);

			bingfengNpcToInst.put(sBingFengWangZuoConfig.FocusNpc, sBingFengWangZuoConfig.instzoneid);
			
			BingFengWangZuoConfig bfcfg = bingFengWangZuoBattleConfig.get(sBingFengWangZuoConfig.getFightid());
			if (bfcfg == null) {
				bfcfg = new BingFengWangZuoConfig(sBingFengWangZuoConfig);
				bingFengWangZuoBattleConfig.put(sBingFengWangZuoConfig.getFightid(), bfcfg);
			}
		}

		for (SBingFengPingJi sBingFeng : ConfigManager.getInstance().getConf(SBingFengPingJi.class).values()) {
			pingJiConfig.add(sBingFeng);
		}

		awardcfgs = ConfigManager.getInstance()
				.getConf(SBingFengJiangLiCfg.class);
		for (SBingFengJiangLiCfg awardcfg : awardcfgs.values()) {
			if (rankId2InstzoneId.containsKey(awardcfg.rankid))
				continue;
			rankId2InstzoneId.put(awardcfg.rankid, awardcfg.fubenid);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("manager", this);
		scheduleNextRefresh();
	}

	private void refreshAllRankList() {
		try {
			for (int rankId : rankId2InstzoneId.keySet())
				new PRefreshRanklist(rankId).submit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeRoleFromRankList(long roleid, int schoolid) {
		for (int rankId : rankId2InstzoneId.keySet()) {
			mkdb.Procedure proc = new PRemoveRankList(rankId, roleid, schoolid);
			if (Transaction.current() != null) {
				mkdb.Procedure.pexecuteWhileCommit(proc);
			} else {
				proc.submit();
			}
		}
	}

	public void scheduleNextRefresh() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		long refresh = cal.getTimeInMillis() - System.currentTimeMillis();
		mkdb.Mkdb.executor().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				refreshAllRankList();
			}
		}, refresh, StatisticUtil.dayMills, TimeUnit.MILLISECONDS);
		mkdb.Mkdb.executor().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				new PClearBingFengMap().submit();
			}
		}, refresh + 60000, StatisticUtil.dayMills, TimeUnit.MILLISECONDS);
	}

	public void sendAfterEnterBingFengLand(long roleid, boolean online) {
		xbean.RoleBFInfo roleBingFengInfo = BingFengLandMgr.getInstance()
				.getRoleBingFengInfo(roleid, 0, true);
		if (online) {
			SEnterBingFengLand sEnterBingFengZone = new SEnterBingFengLand(
					roleBingFengInfo.getInstzoneid(),
					roleBingFengInfo.getLastenterstage(), 0,
					roleBingFengInfo.getStage());
			Onlines.getInstance().send(roleid, sEnterBingFengZone);
		} else {
			SEnterBingFengLand sEnterBingFengZone = new SEnterBingFengLand(
					roleBingFengInfo.getInstzoneid(),
					roleBingFengInfo.getLastenterstage(),
					roleBingFengInfo.getAutogo(), roleBingFengInfo.getStage());
			Onlines.getInstance().send(roleid, sEnterBingFengZone);
		}
	}
}

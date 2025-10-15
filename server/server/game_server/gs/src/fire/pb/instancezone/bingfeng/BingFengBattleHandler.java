package fire.pb.instancezone.bingfeng;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;
import fire.log.YYLogger;
import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.BattleConfig;
import fire.pb.battle.BattleField;
import fire.pb.fanpai.PReqCardProc;
import fire.pb.instance.SBingFengPingJi;
import fire.pb.map.Transfer;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.SPingJi;
import fire.pb.ranklist.proc.PTryInsertBingFeng;
import fire.pb.ranklist.proc.PTryInsertBingFeng.BingFingWangZuoRankData;
import fire.pb.util.DateValidate;

public class BingFengBattleHandler extends BasicEndHandler {
	private final int npcid;
	private final long roleid;
	private final int stage;

	public BingFengBattleHandler(long roleid, int stage, int npcid) {
		this.roleid = roleid;
		this.stage = stage;
		this.npcid = npcid;
	}

	private void fanPai(final long roleId, int landId, final int totalRound) {
		SBingFengPingJi pingJiCnf = BingFengLandMgr.getInstance()
				.getGradeByRound(totalRound);
		if (pingJiCnf == null)
			return;

		int fanPaiId = BingFengLandMgr.getInstance().getFanPaiId(landId,
				npcid, pingJiCnf.getId());
		if (fanPaiId == 0) {
			return;
		}

		boolean send = new PReqCardProc(roleId, fanPaiId, 4).call();
		if (send) {
			SPingJi pingji = new SPingJi();
			pingji.exp = 0;
			pingji.grade = (byte) pingJiCnf.getId();
			mkdb.Procedure.psendWhileCommit(roleId, pingji);
		}
	}
	
	/*@Override
	protected boolean handleCamera() {
		BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battle.getConfigid());
		if(battleConfig!=null && battleConfig.cameratype > 0){
			BattleField.outBattlevideoProcess(battle,
					(byte)battleConfig.cameratype, 
					battle.getConfigid(),
					BattleField.getLeaderRoleid(battle,true));
		}
		return true;
	}*/
	
	@Override
	protected boolean handleAward() {
		long currentTime = System.currentTimeMillis();

		xbean.RoleBFInfo roleBingFengInfo = BingFengLandMgr.getInstance().getRoleBingFengInfo(roleid, 0, false);
		BingFengWangZuoConfig bingFengWangZuoConfig = BingFengLandMgr.getInstance().getBingFengNewConfig(roleBingFengInfo.getInstzoneid(), stage);
		int lv = new PropRole(roleid, true).getLevel();
		if (stage == roleBingFengInfo.getStage()) {
			int expawardid = bingFengWangZuoConfig.getExpAwardid();
			if (expawardid > 0) {
				Map<String, Object> paras = new HashMap<String, Object>();
				paras.put(RewardMgr.TIME, roleBingFengInfo.getStage() + 1);
				paras.put(RewardMgr.BINGFENG_DISCOUNT, 1.0);
				RewardMgr.getInstance().distributeAllAward(roleid,
						expawardid, paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_bingfeng,
						expawardid, PAddExpProc.OTHER, "冰封王座战斗奖励", true);

				RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
				if (actrole != null)
					actrole.handleActiveLivenessData(RoleLiveness.BING_FENG);
				
				// 运营日志
				YYLogger.completeActivityLog(roleid, 119, YYLogger.SUCC_FLAG, "");
			}
			int baoxiangAward = bingFengWangZuoConfig.giveAward();
			if (baoxiangAward > 0) {
				Map<String, Object> paras = new HashMap<String, Object>();
				paras.put(RewardMgr.TIME, roleBingFengInfo.getStage() + 1);
				RewardMgr.getInstance().distributeAllAward(roleid,
						baoxiangAward, paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_bingfeng,
						baoxiangAward, PAddExpProc.OTHER, "冰封王座战斗奖励");
			}
		}

		if (stage == roleBingFengInfo.getStage()) {
			roleBingFengInfo.setStage(roleBingFengInfo.getStage() + 1);
		}

		roleBingFengInfo.setAutogo(1);
		SEnterBingFengLand sEnterBingFengLand = new SEnterBingFengLand(
				roleBingFengInfo.getInstzoneid(), stage + 1, 1,
				roleBingFengInfo.getStage());
		roleBingFengInfo.setLastenterstage(stage + 1);
		Procedure.psendWhileCommit(roleid, sEnterBingFengLand);

		//计算战斗用的时间
		Long useTime = currentTime - battle.getBattlebegintime();
		Long stageUseTime = roleBingFengInfo.getStagetime().get(stage);
		if (stageUseTime == null) {
			roleBingFengInfo.getStagetime().put(stage, useTime);
		}
		else {
			if (useTime < stageUseTime) {
				roleBingFengInfo.getStagetime().put(stage, useTime);
			}
		}
		
		Integer useRound = battle.getRound();
		Integer stageUseRound = roleBingFengInfo.getStageround().get(stage);
		if (stageUseRound == null) {
			roleBingFengInfo.getStageround().put(stage, useRound);
		}
		else {
			if (useRound < stageUseRound) {
				roleBingFengInfo.getStageround().put(stage, useRound);
			}
		}

		if (lv <= bingFengWangZuoConfig.getMaxLv()) {
			if (BingFengLandMgr.freshStageTime(roleBingFengInfo.getInstzoneid(),
					stage, roleid, (int) (useTime / 1000), useRound)) {
				//保存录像
				BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battle.getConfigid());
				if (battleConfig != null && battleConfig.cameratype > 0) {
					BattleField.outBattlevideoBFProcess(battle, battle.getConfigid(),
							roleid, currentTime, useTime);
				}
			}
			if (DateValidate.inTheSameDay(roleBingFengInfo.getLastchangetime(),
					System.currentTimeMillis()))
				updateRank(roleid, roleBingFengInfo);

			if (bingFengWangZuoConfig.getIsBoos()) {
				Integer fanpaitimes = roleBingFengInfo.getStagefanpai().get(stage);
				if (fanpaitimes == null)
					fanpaitimes = 0;

				if (fanpaitimes < bingFengWangZuoConfig.getFanPaiTimes()) {
					fanPai(roleid, roleBingFengInfo.getInstzoneid(), battle.getRound());
					fanpaitimes++;
					roleBingFengInfo.getStagefanpai().put(stage, fanpaitimes);
				}
			}
		}
		
		/*BingFengWangZuoConfig bingFengWangZuoNextConfig = BingFengLandMgr.getInstance().getBingFengNewConfig(roleBingFengInfo.getInstzoneid(), stage + 1);
		if (bingFengWangZuoNextConfig.getBattleNpc() == 0) {
			SCanEnterBingFeng scanenter = new SCanEnterBingFeng();
			scanenter.finish = 1;
		}*/
		
		return true;
	}

	@Override
	public boolean handleDeath() {
		super.handleDeath();
		if (battle.getBattleresult() == BattleField.BATTLE_LOSE) {
			List<xbean.Fighter> fighters = super.getDeadRoles();
			if (!fighters.isEmpty()) {
				for (xbean.Fighter f : fighters) {
					if (roleid == f.getUniqueid()) {
						BingFengRole bingFengRole = new BingFengRole(roleid, false);
						xbean.RoleBFInfo rolebingFengInfo = BingFengLandMgr.getInstance().getRoleBingFengInfo(roleid, 0, false);
						bingFengRole.getBingFengRoleBean().setTimes(bingFengRole.getBingFengRoleBean().getTimes() - 1);
						long currentTime = System.currentTimeMillis();
						if (DateValidate.inTheSameDay(currentTime,
								rolebingFengInfo.getLastchangetime()))
							rolebingFengInfo.setLastchangetime(currentTime);
						if (bingFengRole.getBingFengRoleBean().getTimes() <= 0) {
							transformRoleids.add(roleid);
							
							SCanEnterBingFeng scanenter = new SCanEnterBingFeng();
							scanenter.finish = 1;
							
							Procedure.psendWhileCommit(roleid, scanenter);
						}

						SEnterBingFengLand sEnterBingFengLand = new SEnterBingFengLand(
								rolebingFengInfo.getInstzoneid(), stage, 0,
								rolebingFengInfo.getStage());
						rolebingFengInfo.setLastenterstage(stage);
						Procedure.psendWhileCommit(roleid, sEnterBingFengLand);
					}
				}
			} else {
				xbean.RoleBFInfo rolebingFengInfo = BingFengLandMgr.getInstance().getRoleBingFengInfo(roleid, 0, false);
				SEnterBingFengLand sEnterBingFengLand = new SEnterBingFengLand(
						rolebingFengInfo.getInstzoneid(), stage, 0,
						rolebingFengInfo.getStage());
				rolebingFengInfo.setLastenterstage(stage);
				Procedure.psendWhileCommit(roleid, sEnterBingFengLand);
			}
		} else if (battle.getBattleresult() == BattleField.BATTLE_DRAW) {
			xbean.RoleBFInfo rolebingFengInfo = BingFengLandMgr.getInstance().getRoleBingFengInfo(roleid, 0, false);
			SEnterBingFengLand sEnterBingFengLand = new SEnterBingFengLand(
					rolebingFengInfo.getInstzoneid(), stage, 0,
					rolebingFengInfo.getStage());
			rolebingFengInfo.setLastenterstage(stage);
			Procedure.psendWhileCommit(roleid, sEnterBingFengLand);
		}

		return true;
	}

	@Override
	protected void transformAfterBattle() {
		if (transformRoleids.isEmpty())
			return;

		for (long roleid : transformRoleids) {
			Transfer.justGotoWhileCommit(roleid, 1615, 74, 129,	SRoleEnterScene.CHEFU);
		}
	}

	private void updateRank(long roleid, xbean.RoleBFInfo roleBingFengInfo) {
		int rankId = BingFengLandMgr.getInstance().getRankIdByInstzoneId(
				roleBingFengInfo.getInstzoneid());
		if (rankId == -1)
			return;
		int schoolId = xtable.Properties.selectSchool(roleid);

		int second = 0;
		for (Long times : roleBingFengInfo.getStagetime().values()) {
			second += (times / 1000);
		}
		
		int round = 0;
		for (Integer rd : roleBingFengInfo.getStageround().values()) {
			round += rd;
		}

		List<PTryInsertBingFeng.BingFingWangZuoRankData> datas = new LinkedList<PTryInsertBingFeng.BingFingWangZuoRankData>();
		datas.add(new BingFingWangZuoRankData(roleid, roleBingFengInfo.getStage(),
				schoolId, round, second));
		PTryInsertBingFeng p = new PTryInsertBingFeng(datas, rankId);
		mkdb.Procedure.pexecuteWhileCommit(p);
	}
}

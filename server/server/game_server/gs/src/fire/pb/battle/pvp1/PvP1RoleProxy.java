package fire.pb.battle.pvp1;

import mkdb.Transaction;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.battle.pvp.PvPHelper;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.talk.MessageMgr;
import fire.pb.util.StringConstant;

/**
 * 1v1角色代理
 * @author XGM
 */
public class PvP1RoleProxy {

	/**
	 * 获取PvP角色的代理
	 * @param roleId
	 * @param readonly
	 * @return
	 */
	public static PvP1RoleProxy getPvP1RoleProxy(long roleId, boolean readonly) {

		xbean.PvP1Role xpvprole = null;
		if (readonly) {
			xpvprole = xtable.Pvp1roles.select(roleId);
		} else {
			xpvprole = xtable.Pvp1roles.get(roleId);
		}

		if (xpvprole == null) {
			if (readonly) {
				xpvprole = xbean.Pod.newPvP1RoleData();
			} else {
				xpvprole = xbean.Pod.newPvP1Role();
				xtable.Pvp1roles.insert(roleId, xpvprole);
			}
		}
		return new PvP1RoleProxy(roleId, xpvprole, readonly);
	}

	private final long roleId;
	private xbean.PvP1Role xrole;
	private final boolean readonly;

	/**
	 * 构造
	 * @param roleId
	 * @param xpvprole
	 * @param readonly
	 */
	private PvP1RoleProxy(long roleId, xbean.PvP1Role xpvprole, boolean readonly) {
		this.roleId = roleId;
		this.xrole = xpvprole;
		this.readonly = readonly;
	}

	/**
	 * 清空战斗记录
	 */
	public void clearBattleInfo() {
		xrole.setBattlenum(0);
		xrole.setWinnum(0);
		xrole.setContinuewinnum(0);
		xrole.setFirstwinaward(0);
		xrole.setFivebattleaward(0);
	}

	/**
	 * 获得角色ID
	 * @return
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * 获得此记录所属的赛级
	 * @return
	 */
	public int getGrade() {
		return xrole.getGrade();
	}

	/**
	 * 获得积分
	 * @return
	 */
	public int getScore() {
		return xrole.getScore();
	}

	/**
	 * 根据积分取得等级
	 * @return
	 */
	public int getScoreLevel() {
		return PvPHelper.getScoreLevel(getScore());
	}

	public int getBattleNum() {
		return xrole.getBattlenum();
	}

	public int getWinNum() {
		return xrole.getWinnum();
	}

	public int getContinueWinNum() {
		return xrole.getContinuewinnum();
	}

	public long getLastBattleTime() {
		return xrole.getLastbattletime();
	}

	public long getlastEnterTime() {
		return xrole.getLastentertime();
	}

	/**
	 * 获得当前活动中的赛级
	 * @return
	 */
	public int getGradeInActivity() {

		// 如果活动还在进行中就返回赛级
		if (PvP1Control.getInstance().isActivityTime(xrole.getLastentertime())) {
			if (xrole.getGrade() > 0) {
				return xrole.getGrade();
			}
		}
		return -1;
	}

	/**
	 * 响应入场
	 * @param grade
	 */
	public void onEnter(int grade) {

		long now = System.currentTimeMillis();
		if (PvP1Control.getInstance().isActivityTime(xrole.getLastentertime()) == false) {
			// 清空上次活动的信息
			clearBattleInfo();
			xrole.setLastbattletime(0);
			xrole.setScore(0);
		}

		xrole.setGrade(grade);
		xrole.setLastentertime(now);
	}

	/**
	 * 增加积分
	 * @param addValue
	 * @param levelUp
	 * @return
	 */
	public int addScore(int addValue, boolean levelUp) {
		onAddScore(addValue);
		return addValue;
	}

	/**
	 * 增加积分
	 * @param addValue
	 */
	private void onAddScore(int addValue) {
		int lastScore = getScore();
		xrole.setScore(lastScore + addValue);
	}

	/**
	 * 战斗结束的结果处理
	 * @param isWin
	 * @param enemyId
	 * @param addScore
	 * @param race
	 */
	public void onBattleEnd(boolean isWin, long enemyId, int addScore, PvP1RaceProxy race) {

		if (readonly)
			throw new UnsupportedOperationException("If readonly then cant invoke 1v1 onBattleEnd");

		if (race.getXRace() == null)
			return;

		long now = System.currentTimeMillis();
		xrole.setLastbattletime(now);
		xrole.setLastentertime(now);

		if (isWin) {
			xrole.setWinnum(xrole.getWinnum() + 1);
			xrole.setContinuewinnum(Math.min(PvP1Helper.RACE_MAX_BATTLE_NUM, xrole.getContinuewinnum() + 1));
			if (xrole.getWinnum() == 1 && xrole.getFirstwinaward() == 0)
				xrole.setFirstwinaward(1); // 首胜
//			 if (xrole.getContinuewinnum() > 0 && xrole.getContinuewinnum() % 10 == 0)
//				 xrole.setTenwinaward(1); // 10连胜
		} else {
			xrole.setContinuewinnum(0);
		}

		xrole.setBattlenum(xrole.getBattlenum() + 1);

		if (xrole.getBattlenum() == 5 && xrole.getFivebattleaward() == 0)
			xrole.setFivebattleaward(1); // 5战

		// 增加活动计数
		RoleLiveness activeRole = RoleLiveness.getRoleLiveness(roleId);
		if (activeRole != null) {
			activeRole.handleActiveLivenessData(RoleLiveness.PVP1);
		}
		// 增加成就
		fire.pb.course.CourseManager.achieveCourse(roleId, fire.pb.course.CourseType.ACTIVE_COURSE, RoleLiveness.PVP1, 0);

		// 更新积分排行榜
		String rolename = xtable.Properties.selectRolename(roleId);
		race.updateRankingList(roleId, rolename, addScore,
				xrole.getScore(), xrole.getBattlenum(), xrole.getWinnum(), xrole.getContinuewinnum());

		// 赢的重新加入队列
		boolean isReady = false;
		if (isWin && xrole.getBattlenum() < PvP1Helper.RACE_MAX_BATTLE_NUM) {
			isReady = race.roleReady(roleId);
		}

		// 发刷新协议
		sendSPvP1MyInfo(isReady);
		//race.sendSPvP1RankingList(roleId); 排行榜需要单独请求 [4/18/2016 XGM]

		if (PvP1Control.getLogger().isInfoEnabled()) {
			PvP1Control.getLogger().info("PVP1::[PvP1RoleProxy.onBattleEnd] roleId:" + roleId
					+ " isWin:" + isWin
					+ " enemyId:" + enemyId
					+ " addScore:" + addScore
					+ " score:" + xrole.getScore()
					+ " continueWinNum:" + xrole.getContinuewinnum()
					+ " winNum:" + xrole.getWinnum()
					+ " battleNum:" + xrole.getBattlenum());
		}
	}

	/**
	 * 发送自己的信息
	 * @param ready
	 */
	public void sendSPvP1MyInfo(boolean ready) {

		SPvP1MyInfo snd = new SPvP1MyInfo();
		snd.firstwin = (byte) xrole.getFirstwinaward();
		snd.tenfight = (byte) xrole.getFivebattleaward();
		snd.tencombowin = 0;
		snd.battlenum = (byte) xrole.getBattlenum();
		snd.score = xrole.getScore();
		snd.winnum = (byte) xrole.getWinnum();
		snd.combowinnum = (byte) xrole.getContinuewinnum();
		snd.formation = xtable.Properties.selectDefultform(roleId).byteValue();
		snd.ready = ready ? (byte) 1 : (byte) 0;
		snd.jieci = 0;
		snd.changci = 0;

		if (Transaction.current() != null) {
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		} else {
			gnet.link.Onlines.getInstance().send(roleId, snd);
		}
	}

	/**
	 * 打开箱子
	 * @param boxType
	 * @return
	 */
	public boolean openBox(int boxType) {

		switch (boxType) {
		case 1:// 首胜
			if (xrole.getFirstwinaward() != 1) {
				if (xrole.getFirstwinaward() == 0)
					MessageMgr.sendMsgNotify(roleId, 160342, MessageMgr.getStringList(StringConstant.ID2String("首胜奖励", 304)));
				else
					MessageMgr.sendMsgNotify(roleId, 172000, MessageMgr.getStringList(StringConstant.ID2String("首胜奖励", 304)));
				return false;
			}

			// 发首胜奖励
			RewardMgr.getInstance().distributeAllAward(roleId, PvP1Helper.FIRST_WIN_AWARD_ID, null,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP1, 0, PAddExpProc.OTHER_QUEST, "首胜奖励");

			xrole.setFirstwinaward(2);
			mkdb.Procedure.psendWhileCommit(roleId, new SPvP1OpenBoxState((byte) 1, (byte) 2));

			// 记录日志
			if (PvP1Control.getLogger().isInfoEnabled()) {
				PvP1Control.getLogger().info("PVP1::[PvP1RoleProxy.openBox] roleId:" + roleId
						+ " boxType:" + boxType
						+ " firstWinAwardId:" + PvP1Helper.FIRST_WIN_AWARD_ID);
			}
			return true;
		case 2:// 五战
			if (xrole.getFivebattleaward() != 1) {
				if (xrole.getFivebattleaward() == 0)
					MessageMgr.sendMsgNotify(roleId, 160342, MessageMgr.getStringList(StringConstant.ID2String("五战奖励", 306)));
				else
					MessageMgr.sendMsgNotify(roleId, 172000, MessageMgr.getStringList(StringConstant.ID2String("五战奖励", 306)));
				return false;
			}

			// 发五战奖励
			RewardMgr.getInstance().distributeAllAward(roleId, PvP1Helper.FIVE_FIGHT_AWARD_ID, null,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_PVP1, 0, PAddExpProc.OTHER_QUEST, "五战奖励");

			mkdb.Procedure.psendWhileCommit(roleId, new SPvP1OpenBoxState((byte) 2, (byte) 2));
			xrole.setFivebattleaward(2);

			// 记录日志
			if (PvP1Control.getLogger().isInfoEnabled()) {
				PvP1Control.getLogger().info("PVP1::[PvP1RoleProxy.openBox] roleId:" + roleId
						+ " boxType:" + boxType
						+ " fiveFightAwardId:" + PvP1Helper.FIVE_FIGHT_AWARD_ID);
			}
			return true;
		}
		return false;
	}
}

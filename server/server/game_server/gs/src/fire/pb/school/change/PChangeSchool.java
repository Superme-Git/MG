package fire.pb.school.change;

import java.util.LinkedList;
import java.util.List;

import mkdb.Procedure;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PAddExpProc;
import fire.pb.RoleConfigManager;
import fire.pb.activity.award.RewardMgr;
import fire.pb.buff.BuffConstant;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.skill.SkillRole;
import fire.pb.timer.LevelLast;

/**
 * 转职-转换造型,职业
 * @author XGM
 */
public class PChangeSchool extends Procedure {

	public static final int CHANGESCHOOL_AWARD_ID = 6754; // 转职成功的奖励id

	public final long roleId;
	public final int newShape; // 新造型
	public final int newSchool; // 新职业

	public PChangeSchool(long roleId, int newShape, int newSchool) {
		this.roleId = roleId;
		this.newShape = newShape;
		this.newSchool = newSchool;
	}

	@Override
	protected boolean process() {

		// 服务器等级75级开启转职
		LevelLast serverLv = fire.pb.timer.Module.getLevelLast();
		if (serverLv == null || serverLv.serverlv < getChangesChoolMinServerlv()) {
			return false;
		}

		// 战斗中、观战中不能转职
		if (fire.pb.buff.Module.existState(roleId, BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			return false;
		}
		if (fire.pb.buff.Module.existState(roleId, BuffConstant.StateType.STATE_BATTLE_WATCHER)) {
			return false;
		}
		if (fire.pb.buff.Module.existState(roleId, BuffConstant.StateType.STATE_TEAM)) {
			return false;
		}

		// 先锁user,因为扣符石需要用到user锁
		List<Integer> userIds = new LinkedList<Integer>();
		userIds.add(xtable.Properties.selectUserid(roleId));
		lock(xtable.Locks.USERLOCK, userIds);

		xbean.Properties prop = xtable.Properties.get(roleId);
		if (prop == null) {
			return false;
		}

		// 65级才能转职
		if (prop.getLevel() < getChangeSchoolMinRoleLv()) {
			return false;
		}

		int oldShape = prop.getShape();
		int oldSchool = prop.getSchool();
		int oldSex = prop.getSex();

		if (newShape == oldShape && newSchool == oldSchool) {
			return false;
		}

		final fire.pb.role.RoleConfig roleConfig = RoleConfigManager.getRoleConfigBySchoolID(newSchool);
		if (roleConfig == null) {
			return false;
		}

		final fire.pb.role.SCreateRoleConfig createRoleConfig = RoleConfigManager.getCreateRoleConfig(newShape);
		if (createRoleConfig == null) {
			return false;
		}
		// 获得新造型的性别
		int newSex = createRoleConfig.sex;

		// 性别不同不能转职(临时限制,以后要去掉)
		if (newSex != oldSex) {
			return false;
		}

		// 获取转职信息
		xbean.ChangeSchoolInfo info = xtable.Changeschool.get(roleId);
		if (info == null) {
			info = xbean.Pod.newChangeSchoolInfo();
			xtable.Changeschool.insert(roleId, info);
		}

		// 检查转职间隔,7天
		if (info.getRecords().size() > 0) {
			xbean.ChangeSchoolRecord last = info.getRecords().get(info.getRecords().size() - 1);
			int diffDay = fire.pb.util.DateValidate.getDaysBetween(last.getTime(), System.currentTimeMillis());
			int cooldownDay = getChangeSchoolCooldownDay(); // 冷却天数
			if (diffDay <= cooldownDay) {
				return false;
			}
		}

		// 转职不能穿装备
		fire.pb.item.Equip equip = new fire.pb.item.Equip(roleId, false);
		if (equip.size() > 0) {
			return false;
		}

		// 转职消耗
		int cost = 0;

		// 以前转过的形象和职业消耗减半
		boolean isHaveShapeRecord = false;
		boolean isHaveSchoolRecord = false;
		for (xbean.ChangeSchoolRecord record : info.getRecords()) {
			if (record.getShape() == newShape) {
				isHaveShapeRecord = true;
			}
			if (record.getSchool() == newSchool) {
				isHaveSchoolRecord = true;
			}
		}

		// 转换造型消耗
		if (newShape != oldShape) {
			int c = getChangesChoolCost();
			if (isHaveShapeRecord) {
				c /= 2;
			}
			cost += c;
		}

		// 转换职业消耗
		if (newSchool != oldSchool) {
			int c = getChangesChoolCost();
			if (isHaveSchoolRecord) {
				c /= 2;
			}
			cost += c;
		}

		// 修改部分影响的系统
		updateConflict(roleId, oldSchool);

		// 改变技能
		changeSkill(roleId, oldSchool, newSchool);

		// 记录转职信息
		xbean.ChangeSchoolRecord record = xbean.Pod.newChangeSchoolRecord();
		record.setShape(oldShape);
		record.setSchool(oldSchool);
		record.setTime(System.currentTimeMillis());
		info.getRecords().add(record);
		// 重置转换武器、转宝石的次数
		info.setChangeweaponcount(0);
		info.setChangegemcount(0);

		// 重置造型、职业、性别
		if (newShape != oldShape) {
			prop.setShape(newShape);
		}
		if (newSchool != oldSchool) {
			prop.setSchool(newSchool);
		}
		if (newSex != oldSex) {
			prop.setSex(newSex);
		}

		// 符石消耗
		if (FushiManager.subFushiFromUser(prop.getUserid(), roleId, cost, 0, 0,
				FushiConst.REASON_SUB_CHANGESCHOOL,
				YYLoggerTuJingEnum.tujing_Value_changeschoolcost, false) == false) {
			return false;
		}

		// 转职奖励
		RewardMgr.getInstance().distributeAllAward(roleId, CHANGESCHOOL_AWARD_ID, null,
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_changeschool, 0,
				PAddExpProc.OTHER_QUEST, "转职奖励");

		// 转职完成,彻底地下线,需重新登陆
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.state.CompletelyOffline(roleId));

		if (fire.log.LogManager.logger.isInfoEnabled()) {
			fire.log.LogManager.logger.info("[PChangeSchool] roleId:" + roleId
					+ " oldShape:" + oldShape
					+ " newShape:" + newShape
					+ " oldSchool:" + oldSchool
					+ " newSchool:" + newSchool
					+ " oldSex:" + oldSex
					+ " newSex:" + newSex
					+ " recordSize:" + info.getRecords().size());
		}
		return true;
	}

	/**
	 * 转职后修改部分影响的系统
	 */
	public void updateConflict(long roleId, int curSchool) {

		// 取消首席资格
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.school.shouxi.PDeleteProfessionLeader(roleId, curSchool));

		// 清空冰封王座的排行
		fire.pb.instancezone.bingfeng.BingFengLandMgr.getInstance().removeRoleFromRankList(roleId, curSchool);
	}

	/**
	 * 改变技能
	 */
	public void changeSkill(long roleId, int oldSchool, int newSchool) {
		int v = (newSchool - oldSchool) * 100;
		SkillRole srole = new SkillRole(roleId);
		srole.changeSchool(v);
	}

	/**
	 * 转职消耗
	 */
	public static int getChangesChoolCost() {
		Integer value = ChangeSchoolUtils.getCommonConf(446);
		if (value == null) {
			value = 1800;
		}
		return value;
	}

	/**
	 * 转职需要的最小服务器等级
	 */
	public static int getChangesChoolMinServerlv() {
		Integer value = ChangeSchoolUtils.getCommonConf(447);
		if (value == null) {
			value = 75;
		}
		return value;
	}

	/**
	 * 转职需要的最小角色等级
	 */
	public static int getChangeSchoolMinRoleLv() {
		Integer value = ChangeSchoolUtils.getCommonConf(448);
		if (value == null) {
			value = 65;
		}
		return value;
	}

	/**
	 * 转职冷却天数
	 */
	public static int getChangeSchoolCooldownDay() {
		Integer value = ChangeSchoolUtils.getCommonConf(449);
		if (value == null) {
			value = 7;
		}
		return value;
	}
}

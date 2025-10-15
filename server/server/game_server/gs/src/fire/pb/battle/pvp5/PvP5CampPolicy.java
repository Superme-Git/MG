package fire.pb.battle.pvp5;

import java.util.HashMap;
import java.util.Map;

/**
 * 5v5角色分阵营策略
 * @author XGM
 */
public class PvP5CampPolicy {

	// 统计人数
	private static Map<Integer, Map<Integer, Integer>> campRoleNumMap = new HashMap<Integer, Map<Integer, Integer>>();
	/**
	 * 初始化阵营人数
	 */
	public static void init() {
		synchronized (campRoleNumMap) {
			campRoleNumMap.clear();
		}
	}
	/**
	 * 获取某一个阵营的人数
	 */
	public static int getRoleNumByCamp(int grade, int camp) {
		synchronized (campRoleNumMap) {
			Map<Integer, Integer> campRoleNum = campRoleNumMap.get(grade);
			if (campRoleNum != null) {
				Integer roleNum = campRoleNum.get(camp);
				if (roleNum != null) {
					return roleNum;
				}
			}
		}
		return 0;
	}
	/**
	 * 某个阵营增加人数
	 */
	public static int addRoleNumByCamp(int grade, int camp) {
		synchronized (campRoleNumMap) {
			Map<Integer, Integer> campRoleNum = campRoleNumMap.get(grade);
			if (campRoleNum == null) {
				campRoleNum = new HashMap<Integer, Integer>();
				campRoleNumMap.put(grade, campRoleNum);
			}
			Integer roleNum = campRoleNum.get(camp);
			if (roleNum == null) {
				roleNum = 0;
			}
			roleNum += 1;
			campRoleNum.put(camp, roleNum);
			return roleNum;
		}
	}

	/**
	 * 创建分阵营策略
	 * @param roleId
	 * @param grade
	 * @return
	 */
	public static Policy create(long roleId, int grade) {

		// 是不是今天已经参加过了
		PvP5RoleProxy roleProxy = PvP5RoleProxy.getPvP5RoleProxy(roleId, true);
		if (roleProxy.isTodayJoinActivity()) {
			return new PolicyAlreadyJoin(roleId, grade); // 已经参加过了
		}

		long now = System.currentTimeMillis();
		long fightStartTime = PvP5Control.getInstance().getFightStartTime();
		long interval = 30 * 60 * 1000;

		// 战斗开始后半小时,再申请进入场景时,分配到普通场
		if (now > fightStartTime && now - fightStartTime > interval) {
			return new PolicyGeneralCamp(roleId, grade); // 普通场
		} else {
			return new PolicyExcellentCamp(roleId, grade); // 优胜场
		}
	}

	/**
	 * 分阵营策略的抽象类
	 * @author XGM
	 */
	public static abstract class Policy {

		protected final long roleId;
		protected final int grade;
		protected int camp = 0;
		protected int mapId = 0;

		protected Policy(long roleId, int grade) {
			this.roleId = roleId;
			this.grade = grade;
		}

		/**
		 * 分阵营
		 */
		public abstract void divide();

		int getCamp() {
			return camp;
		}

		int getMapId() {
			return mapId;
		}
	}

	/**
	 * 分配优胜场阵营
	 * @author XGM
	 */
	public static class PolicyExcellentCamp extends Policy {

		public PolicyExcellentCamp(long roleId, int grade) {
			super(roleId, grade);
		}

		public void divide() {
			int roleNumCampA = getRoleNumByCamp(grade, 0);
			int roleNumCampB = getRoleNumByCamp(grade, 1);
			if (roleNumCampA <= roleNumCampB) {
				camp = 0;
			} else {
				camp = 1;
			}
			mapId = PvP5Helper.getMapIdByRaceGrade(grade, true, camp);
		}

	}

	/**
	 * 分配普通场阵营
	 * @author XGM
	 */
	public static class PolicyGeneralCamp extends Policy {

		protected PolicyGeneralCamp(long roleId, int grade) {
			super(roleId, grade);
		}

		@Override
		public void divide() {
			int roleNumCampA = getRoleNumByCamp(grade, 0);
			int roleNumCampB = getRoleNumByCamp(grade, 1);
			if (roleNumCampA <= roleNumCampB) {
				camp = 0;
			} else {
				camp = 1;
			}
			mapId = PvP5Helper.getMapIdByRaceGrade(grade, false, camp);
		}
	}

	/**
	 * 已经参加过了
	 * @author XGM
	 */
	public static class PolicyAlreadyJoin extends Policy {

		protected PolicyAlreadyJoin(long roleId, int grade) {
			super(roleId, grade);
		}

		@Override
		public void divide() {
			// 在优胜场输2次降为普通场
			PvP5RoleProxy roleProxy = PvP5RoleProxy.getPvP5RoleProxy(roleId, true);
			boolean isExcellent = (roleProxy.getExcellent() == 1);
			if (isExcellent) {
				int battleNum = roleProxy.getBattleNum();
				int winNum = roleProxy.getWinNum();
				if (battleNum - winNum >= PvP5Helper.DROP_SCENE_BATTLE_LOSE_NUM) {
					isExcellent = false;
				}
			}
			camp = roleProxy.getCamp();
			mapId = PvP5Helper.getMapIdByRaceGrade(grade, isExcellent, camp);
		}

	}
}


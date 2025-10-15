package fire.pb.battle.pvp5;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mkdb.Transaction;
import fire.pb.battle.pvp.PvPHelper;
import fire.pb.battle.pvp.PvPResult;
import fire.pb.circletask.SPVP5MapConfig;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SGeneralSummonCommand;
import fire.pb.npc.TransmitTypes;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.StringConstant;

/**
 * PvP5辅助类
 * @author XGM
 */
public abstract class PvP5Helper extends PvPHelper {

//	static class SingletonHolder {
//		static PvP5Helper singleton = new PvP5Helper();
//	}
//
//	public static PvP5Helper getInstance() {
//		return SingletonHolder.singleton;
//	}

	public static final int ENTER_MIN_LEVEL = 50;		// 入场的最小等级

	public static final int TEAM_MAX_MEMBER_NUM = 5;	// 队伍最大成员数

	public static final int RACE_MAX_BATTLE_NUM = 100;	// 最大战斗次数

	public static final int AWARD_MAX_BATTLE_NUM = 10;	// 给奖励的战斗次数

	public static final int CONTINUE_WIN_MIN_NUM = 5;		// 连胜的最小次数

	public static final long BATTLE_BETWEEN_MILLISECONDS = 1000 * 5;	// 两场战斗之间的间隔

	public static final long MATCH_WAITING_MILLISECONDS = 1000 * (30 + 1);	// 匹配等待时间(+1秒的延迟时间)

	public static final int DROP_SCENE_BATTLE_LOSE_NUM = 2;	// 优胜场失败几次掉落到普通场

	// 奖励的ID
	public static final int FIRST_WIN_AWARD_ID = 6251;	// 首胜奖励
	public static final int FIVE_FIGHT_AWARD_ID = 6252;	// 五战奖励

	// 排名奖励
	public static final int RANK_01_05_AWARD_ID = 340010;	// 1-5名奖励
	public static final int RANK_06_10_AWARD_ID = 340011;	// 6-10名奖励
	public static final int RANK_11_15_AWARD_ID = 340012;	// 11-15名奖励

	// 称号奖励
	public static final Map<Integer, Integer[]> TITLE_AWARD_MAP = new HashMap<Integer, Integer[]>();
	static {
		Integer[] t1 = { 100, 101, 102, 103, 104 };
		Integer[] t2 = { 105, 106, 107, 108, 109 };
		Integer[] t3 = { 110, 111, 112, 113, 114 };
		TITLE_AWARD_MAP.put(1, t1);
		TITLE_AWARD_MAP.put(2, t2);
		TITLE_AWARD_MAP.put(3, t3);
	}

	/**
	 * 获得称号奖励
	 * @param grade
	 * @return
	 */
	public static Integer[] getTitleAward(int grade) {
		return TITLE_AWARD_MAP.get(grade);
	}

	/**
	 * 检测角色进入的结果
	 * @author XGM
	 */
	public static class CheckRoleEnterResult extends PvPResult {

		private final long roleId;
		private long errorRoleId = 0;

		public CheckRoleEnterResult(int result, long roleId) {
			super(result);
			this.roleId = roleId;
		}
		public CheckRoleEnterResult(int result, long roleId, long errorRoleId) {
			super(result);
			this.roleId = roleId;
			this.errorRoleId = errorRoleId;
		}
		@Override
		public int handle() {
			switch (get()) {
			case -1: // 只有队长才能操作
				MessageMgr.sendMsgNotify(roleId, 141206, null);
				break;
			case -10:
			case -20: // 队伍中有人未参加活动，无法进入
				do {
					String roleName = xtable.Properties.selectRolename(errorRoleId);
					for (long id : PvP5RoleProxy.getTeamMemberIds(roleId)) {
						MessageMgr.sendMsgNotify(id, 172031, MessageMgr.getStringList(roleName));
					}
				} while (false);
				break;
			case -30: // 你的队伍中因为成员等级不在同一竞技组无法组队进入
				for (long id : PvP5RoleProxy.getTeamMemberIds(roleId)) {
					MessageMgr.sendMsgNotify(id, 160339, null);
				}
				break;
			case -40:
			case -50:
			case -60: // 队伍中有不是同一阵营的玩家，无法进入
			default:
				for (long id : PvP5RoleProxy.getTeamMemberIds(roleId)) {
					MessageMgr.sendMsgNotify(id, 172028, null);
				}
				break;
			}
			return 0;
		}
	}

	// 检测进入条件是否使用分阵营策略
	private static boolean CHECK_USE_CAMP_POLICY = false;

	/**
	 * 检测角色进入的条件
	 * @param roleId
	 * @param grade
	 * @param camp
	 * @param mapId
	 * @return
	 */
	public static PvPResult checkRoleEnterCondition(final long roleId, final int grade, final int camp, final int mapId) {

		// 检测队伍
		Team team = TeamManager.selectTeamByRoleId(roleId);
		if (team != null) {
			// 必须是队长,队长不能暂离,队伍人数必须<=5
			if (team.isTeamLeader(roleId) == false || team.isAbsentMember(roleId)) {
				return new CheckRoleEnterResult(-1, roleId);
			}
			List<Long> ids = team.getAllMemberIds();
			int teamSize = ids.size();
			if (teamSize > PvP5Helper.TEAM_MAX_MEMBER_NUM) {
				return new CheckRoleEnterResult(-2, roleId);
			}

			// 有队员的情况下,检测自己和队员,自己一个人进去不需要检测
			if (ids.size() > 1) {
				// 队伍成员是否都符合进一个赛场
				for (long id : ids) {
					
					// 如果比赛进行中出去了,可以再进相同的赛场
					PvP5RoleProxy pvpRoleProxy = PvP5RoleProxy.getPvP5RoleProxy(id, true);
					if (pvpRoleProxy == null) {
						return new CheckRoleEnterResult(-10, roleId, id);
					}
					int grade2 = pvpRoleProxy.getGradeInActivity(); 
					if (grade2 <= 0) {
						//grade2 = getRaceGradeByRoleId(id);
						return new CheckRoleEnterResult(-20, roleId, id); // 如果没进去过,必须单独进入,因为需要分阵营
					}
					if (grade2 != grade) {
						return new CheckRoleEnterResult(-30, roleId, id);
					}

					if (CHECK_USE_CAMP_POLICY) {
						PvP5CampPolicy.Policy policy = PvP5CampPolicy.create(id, grade);
						if (policy == null) {
							return new CheckRoleEnterResult(-40, roleId, id);
						}
						policy.divide();

						int camp2 = policy.getCamp();
						int mapId2 = policy.getMapId();
						if (camp2 != camp) {
							return new CheckRoleEnterResult(-50, roleId, id);
						}
						if (mapId2 != mapId) {
							return new CheckRoleEnterResult(-60, roleId, id);
						}
					} else {
						int camp2 = pvpRoleProxy.getCamp();
						if (camp2 != camp) {
							return new CheckRoleEnterResult(-50, roleId, id);
						}
						int mapId2 = PvP5Helper.getMapIdByRaceGrade(grade2, (pvpRoleProxy.getExcellent() == 1), camp2);
						if (mapId2 != mapId) {
							return new CheckRoleEnterResult(-60, roleId, id);
						}
					}
				}
			}
		}

		// 其他条件

		return new CheckRoleEnterResult(0, roleId);
	}

	/**
	 * 拉人进场
	 * @param roleId
	 * @return
	 */
	public static boolean summonByRole(long roleId, int roleMapId) {

		if (isPvPMap(roleMapId)) {
			return false;
		}

		int grade = getRaceGradeByRoleId(roleId);
		if (grade <= 0) {
			return false;
		}

		SGeneralSummonCommand snd = new SGeneralSummonCommand();
		snd.roleid = 0;
		snd.mapid = 0;
		snd.minimal = 0;
		snd.summontype = TransmitTypes.pvp5;
		if (Transaction.current() != null) {
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		} else {
			gnet.link.Onlines.getInstance().send(roleId, snd);
		}
		return true;
	}

	/**
	 * 获取所有赛场ID
	 * @return
	 */
	public static Collection<Integer> getAllRaceIds() {
		Map<Integer, SPVP5MapConfig> cfgs = ConfigManager.getInstance().getConf(SPVP5MapConfig.class);
		return cfgs.keySet();
	}

	/**
	 * 通过角色ID获取可进入赛场等级
	 * @param roleId
	 * @return
	 */
	public static int getRaceGradeByRoleId(long roleId) {
		int level = xtable.Properties.selectLevel(roleId);
		if (level < ENTER_MIN_LEVEL) {
			return -1;
		}
		return getRaceGradeByRoleLevel(level);
	}

	/**
	 * 通过角色等级获取可进入赛场等级
	 * @param level
	 * @return
	 */
	public static int getRaceGradeByRoleLevel(int level) {
		Map<Integer,SPVP5MapConfig> confs = ConfigManager.getInstance().getConf(SPVP5MapConfig.class);
		for(Map.Entry<Integer, SPVP5MapConfig> entry : confs.entrySet()) {
			if (entry.getValue().getLevel1() <= level && level <= entry.getValue().getLevel2()) {
				return entry.getKey();
			}
		}
		return 1;
	}

	/**
	 * 通过赛场等级和阵营获取地图ID
	 * @param grade
	 * @param isExcellent
	 * @param camp
	 * @return
	 */
	public static int getMapIdByRaceGrade(int grade, boolean isExcellent, int camp) {

		SPVP5MapConfig conf = ConfigManager.getInstance().getConf(SPVP5MapConfig.class).get(grade);
		if (conf != null) {
			if (isExcellent) {
				return conf.getExcellentMap().get(camp);
			} else {
				return conf.getGeneralMap().get(camp);
			}
		}
		return -1;
	}

	/**
	 * 通过地图ID获取赛场等级
	 * @param mapId
	 * @return
	 */
	public static int getRaceGradeByMapId(int mapId) {
		Map<Integer, SPVP5MapConfig> confs = ConfigManager.getInstance().getConf(SPVP5MapConfig.class);
		for (Map.Entry<Integer, SPVP5MapConfig> entry : confs.entrySet()) {
			if (entry.getValue().getGeneralMap().contains(mapId)
					|| entry.getValue().getExcellentMap().contains(mapId)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	/**
	 * 通过地图ID获取优胜标记
	 * @param mapId
	 * @return
	 */
	public static int getExcellentByMapId(int mapId) {
		Map<Integer, SPVP5MapConfig> confs = ConfigManager.getInstance().getConf(SPVP5MapConfig.class);
		for (Map.Entry<Integer, SPVP5MapConfig> entry : confs.entrySet()) {
			if (entry.getValue().getGeneralMap().contains(mapId))
				return 0;
			if (entry.getValue().getExcellentMap().contains(mapId))
				return 1;
		}
		return 0;
	}

	/**
	 * 通过地图ID获取阵营
	 * @param mapId
	 * @return
	 */
	public static int getCampByMapId(int mapId) {
		Map<Integer, SPVP5MapConfig> confs = ConfigManager.getInstance().getConf(SPVP5MapConfig.class);
		for (Map.Entry<Integer, SPVP5MapConfig> entry : confs.entrySet()) {
			int index = entry.getValue().getGeneralMap().indexOf(mapId);
			if (index < 0) {
				index = entry.getValue().getExcellentMap().indexOf(mapId);
			}
			if (index >= 0) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * 是不是PvP5的地图
	 * @param mapId
	 * @return
	 */
	public static boolean isPvPMap(int mapId) {
		return getRaceGradeByMapId(mapId) != -1;
	}

	/**
	 * 是不是同一个等级段
	 * @param roleId1
	 * @param roleId2
	 * @return
	 */
	public static boolean isSameGrade(long roleId1, long roleId2) {
		int grade1 = getRaceGradeByRoleId(roleId1);
		int grade2 = getRaceGradeByRoleId(roleId2);
		return grade1 == grade2;
	}

	/**
	 * 是不是同一个比赛地图
	 * @param roleId1
	 * @param roleId2
	 * @return
	 */
	public static boolean isSameMap(long roleId1, long roleId2) {

		// 如果比赛进行中出去了,可以再进相同的赛场
		PvP5RoleProxy pvpRoleProxy1 = PvP5RoleProxy.getPvP5RoleProxy(roleId1, true);
		PvP5RoleProxy pvpRoleProxy2 = PvP5RoleProxy.getPvP5RoleProxy(roleId2, true);
		if (pvpRoleProxy1 == null || pvpRoleProxy2 == null) {
			return false;
		}
		int grade1 = pvpRoleProxy1.getGradeInActivity(); 
		if (grade1 <= 0) {
			//grade1 = getRaceGradeByRoleId(roleId1);
			return false; // 如果没进去过,必须单独进入,因为需要分阵营
		}
		int grade2 = pvpRoleProxy2.getGradeInActivity(); 
		if (grade2 <= 0) {
			//grade2 = getRaceGradeByRoleId(roleId2);
			return false; // 如果没进去过,必须单独进入,因为需要分阵营
		}
		if (grade1 != grade2) {
			return false;
		}

		if (CHECK_USE_CAMP_POLICY) {
			PvP5CampPolicy.Policy policy1 = PvP5CampPolicy.create(roleId1, grade1);
			PvP5CampPolicy.Policy policy2 = PvP5CampPolicy.create(roleId2, grade2);
			if (policy1 == null || policy2 == null) {
				return false;
			}
			policy1.divide();
			policy2.divide();
	
			if (policy1.getCamp() != policy2.getCamp()) {
				return false;
			}
			if (policy1.getMapId() != policy2.getMapId()) {
				return false;
			}
		} else {
			int camp1 = pvpRoleProxy1.getCamp();
			int camp2 = pvpRoleProxy2.getCamp();
			if (camp1 != camp2) {
				return false;
			}
			int mapId1 = PvP5Helper.getMapIdByRaceGrade(grade1, (pvpRoleProxy1.getExcellent() == 1), camp1);
			int mapId2 = PvP5Helper.getMapIdByRaceGrade(grade2, (pvpRoleProxy2.getExcellent() == 1), camp2);
			if (mapId1 != mapId2) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 竞技场外的玩家是否能加入里面的队伍
	 * @param leaderRoleId
	 * @param applyRoleId
	 * @return
	 */
	public static boolean isCanJoinPvPMapTeam(long leaderRoleId, long applyRoleId) {
		// 竞技场不是同一个等级段不能组队
		final fire.pb.map.Role leaderMapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(leaderRoleId);
		if (leaderMapRole == null) {
			return false;
		}
		if (isPvPMap(leaderMapRole.getMapId())) {
			if (isSameGrade(leaderRoleId, applyRoleId) == false
					|| isSameMap(leaderRoleId, applyRoleId) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获得竞技场等级名称
	 * @param grade
	 * @return
	 */
	public static String getGradeName(int grade) {
		switch (grade) {
		case 1: return StringConstant.ID2String("精英组", 308);
		case 2: return StringConstant.ID2String("神威组", 309);
		case 3: return StringConstant.ID2String("王者组", 310);
		}
		return "";
	}

	/**
	 * 获得竞技场阵营名称
	 * @param camp
	 * @return
	 */
	public static String getCampName(int camp) {
		if (camp == 0) {
			return StringConstant.ID2String("雷毛", 311);
		} else {
			return StringConstant.ID2String("双狼", 312);
		}
	}
}

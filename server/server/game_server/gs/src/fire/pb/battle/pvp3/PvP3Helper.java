package fire.pb.battle.pvp3;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import mkdb.Transaction;
import fire.pb.battle.pvp.PvPHelper;
import fire.pb.circletask.SPVP3MapConfig;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SGeneralSummonCommand;
import fire.pb.npc.TransmitTypes;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

/**
 * PvP3辅助类
 * @author XGM
 */
public abstract class PvP3Helper extends PvPHelper {

//	static class SingletonHolder {
//		static PvP3Helper singleton = new PvP3Helper();
//	}
//
//	public static PvP3Helper getInstance() {
//		return SingletonHolder.singleton;
//	}

	public static final int ENTER_MIN_LEVEL = 45;		// 入场的最小等级

	public static final int TEAM_MAX_MEMBER_NUM = 3;	// 队伍最大成员数

	public static final int RACE_MAX_BATTLE_NUM = 99;	// 最大战斗次数

	public static final int SCORE_MAX_BATTLE_NUM = 15;	// 给积分的战斗次数

	public static final int CONTINUE_WIN_MIN_NUM = 5;	// 连胜的最小次数

	public static final long BATTLE_BETWEEN_MILLISECONDS = 1000 * 5;	// 两场战斗之间的间隔

	// 奖励的ID
	public static final int FIRST_WIN_AWARD_ID = 6170;
	public static final int TEN_FIGHT_AWARD_ID = 6171;
	public static final int CONTINUE_WIN_AWARD_ID = 6172;

	/**
	 * 检测角色进入的条件
	 * @param roleId
	 * @param grade
	 * @param error
	 * @return
	 */
	public static int checkRoleEnterCondition(long roleId, int grade) {

		// 检测队伍
		Team team = TeamManager.selectTeamByRoleId(roleId);
		if (team != null) {
			// 必须是队长,队长不能暂离,队伍人数必须<=3
			if (team.isTeamLeader(roleId) == false || team.isAbsentMember(roleId)) {
				return -1;
			}
			List<Long> ids = team.getAllMemberIds();
			int teamSize = ids.size();
			if (teamSize > PvP3Helper.TEAM_MAX_MEMBER_NUM) {
				return -2;
			}

			// 队伍成员是否都符合进一个赛场
			for (long id : ids) {

				if (getRaceGradeByRoleId(id) != grade) {

					// 如果比赛进行中出去了,可以再进相同的赛场
					PvP3RoleProxy pvpRoleProxy = PvP3RoleProxy.getPvP3RoleProxy(id, true);
					if (pvpRoleProxy == null) {
						return -3;
					}
					if (pvpRoleProxy.getGradeInActivity() != grade) {
						return -4;
					}
				}
			}
		}

		// 其他条件

		return 0;
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
		snd.summontype = TransmitTypes.pvp3;
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
		Map<Integer, SPVP3MapConfig> cfgs = ConfigManager.getInstance().getConf(SPVP3MapConfig.class);
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

		Map<Integer,SPVP3MapConfig> confs = ConfigManager.getInstance().getConf(SPVP3MapConfig.class);
		for(Map.Entry<Integer, SPVP3MapConfig> entry : confs.entrySet()) {
			if (entry.getValue().getLevel1() <= level && level <= entry.getValue().getLevel2()) {
				return entry.getKey();
			}
		}
		return 1;
	}

	/**
	 * 通过赛场等级获取地图ID
	 * @param grade
	 * @return
	 */
	public static int getMapIdByRaceGrade(int grade) {

		SPVP3MapConfig conf = ConfigManager.getInstance().getConf(SPVP3MapConfig.class).get(grade);
		if (conf == null) {
			return -1;
		}
		return conf.map;
	}

	/**
	 * 通过地图ID获取赛场等级
	 * @param mapId
	 * @return
	 */
	public static int getRaceGradeByMapId(int mapId) {

		Map<Integer, SPVP3MapConfig> confs = ConfigManager.getInstance().getConf(SPVP3MapConfig.class);
		for (Map.Entry<Integer, SPVP3MapConfig> entry : confs.entrySet()) {
			if (entry.getValue().getMap() == mapId)
				return entry.getKey();
		}
		return -1;
	}

	/**
	 * 是不是PvP3的地图
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
		if (isPvPMap(leaderMapRole.getMapId())
				&& isSameGrade(leaderRoleId, applyRoleId) == false) {
			return false;
		}
		return true;
	}

}
